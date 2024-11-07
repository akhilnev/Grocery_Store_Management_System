package inventory.service;

import inventory.interfaces.InventoryInterface;
import inventory.model.Product;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class InventoryManager implements InventoryInterface {
    private Map<String, Product> inventory;
    private HeadOfficeManager headOffice;
    private static final String INVENTORY_FILE = "./src/main/java/inventory/service/inventory.txt";

    public InventoryManager(HeadOfficeManager headOffice) {
        this.inventory = new HashMap<>();
        this.headOffice = headOffice;
        loadInventoryFromFile();
    }

    @Override
    public boolean updateStock() {
        saveInventoryToFile();
        return true;
    }

    @Override
    public boolean checkLowStock() {
        List<Product> lowStockProducts = inventory.values().stream()
            .filter(p -> p.getStockLevel() < 10 && !p.isObsolete())
            .collect(Collectors.toList());
        return !lowStockProducts.isEmpty();
    }

    @Override
    public boolean trackExpiry() {
        LocalDate warningDate = LocalDate.now().plusDays(7);
        List<Product> expiringProducts = inventory.values().stream()
            .filter(p -> p.getExpirationDate() != null && 
                        p.getExpirationDate().isBefore(warningDate) &&
                        !p.isObsolete())
            .collect(Collectors.toList());
        return !expiringProducts.isEmpty();
    }

    @Override
    public boolean removeObsoleteProducts() {
        inventory.values().stream()
            .filter(Product::isObsolete)
            .forEach(p -> inventory.remove(p.getId()));
        return updateStock();
    }

    // Additional methods for the main success scenario
    public boolean addNewProduct(Product product) {
        if (inventory.containsKey(product.getId())) {
            return false;
        }
        if (headOffice.approveChanges()) {
            inventory.put(product.getId(), product);
            return updateStock();
        }
        return false;
    }

    public boolean updateProductPrice(String productId, double newPrice) {
        Product product = inventory.get(productId);
        if (product != null && headOffice.approveChanges()) {
            product.setPrice(newPrice);
            return updateStock();
        }
        return false;
    }

    public boolean restockProduct(String productId, int additionalQuantity) {
        Product product = inventory.get(productId);
        if (product != null) {
            product.setStockLevel(product.getStockLevel() + additionalQuantity);
            return updateStock();
        }
        return false;
    }

    // File operations
    private void loadInventoryFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(INVENTORY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Product product = new Product(
                    parts[0], // id
                    parts[1], // name
                    Double.parseDouble(parts[2]), // price
                    Integer.parseInt(parts[3]), // stockLevel
                    parts[4], // supplier
                    LocalDate.parse(parts[5]) // expirationDate
                );
                inventory.put(product.getId(), product);
            }
        } catch (IOException e) {
            System.err.println("Error loading inventory: " + e.getMessage());
        }
    }

    private void saveInventoryToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(INVENTORY_FILE))) {
            for (Product product : inventory.values()) {
                writer.write(String.format("%s,%s,%.2f,%d,%s,%s\n",
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getStockLevel(),
                    product.getSupplier(),
                    product.getExpirationDate()
                ));
            }
        } catch (IOException e) {
            System.err.println("Error saving inventory: " + e.getMessage());
        }
    }
}