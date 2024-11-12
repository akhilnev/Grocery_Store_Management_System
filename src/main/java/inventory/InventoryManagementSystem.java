package inventory;

import inventory.model.Product;
import inventory.service.InventoryManager;
import inventory.service.HeadOfficeManager;
import java.time.LocalDate;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;

/**
 * Main system class that handles the user interface and interaction
 * for inventory management operations.
 *
 * @author Akhilesh Nevatia
 */
public class InventoryManagementSystem {
    private InventoryManager inventoryManager;
    private Scanner scanner;
    private String storeId;

    /**
     * Constructor initializes the system with a specific store ID
     * @param storeId The unique identifier for the store
     */
    public InventoryManagementSystem(String storeId) {
        this.storeId = storeId;
        this.inventoryManager = new InventoryManager(new HeadOfficeManager());
        this.inventoryManager.setCurrentStore(storeId);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\nInventory Management System");
            System.out.println("1. Add New Product ( Store Manager )");
            System.out.println("2. Update Stock Level ( Inventory Clerk )");
            System.out.println("3. Adjust Product Price (Store Manager)");
            System.out.println("4. Check Low Stock ( Inventory Clerk )");
            System.out.println("5. Track Expiring Products ( Inventory Clerk )");
            System.out.println("6. Remove Obsolete Products ( Store Manager )");
            System.out.println("7. View Store Inventory");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addNewProduct();
                    break;
                case 2:
                    updateStockLevel();
                    break;
                case 3:
                    adjustProductPrice();
                    break;
                case 4:
                    checkLowStock();
                    break;
                case 5:
                    trackExpiringProducts();
                    break;
                case 6:
                    removeObsoleteProducts();
                    break;
                case 7:
                    viewStoreInventory();
                    break;
                case 8:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void addNewProduct() {
        System.out.print("Enter product ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter initial stock level: ");
        int stockLevel = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter supplier: ");
        String supplier = scanner.nextLine();
        
        System.out.println("\nSelect expiry timeline:");
        System.out.println("1. 6 months (default)");
        System.out.println("2. 1 year");
        System.out.println("3. 2 years");
        System.out.println("4. Custom date (YYYY-MM-DD)");
        System.out.print("Choose option: ");
        
        int expiryChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        LocalDate expiryDate;
        switch (expiryChoice) {
            case 2:
                expiryDate = LocalDate.now().plusYears(1);
                break;
            case 3:
                expiryDate = LocalDate.now().plusYears(2);
                break;
            case 4:
                System.out.print("Enter expiry date (YYYY-MM-DD): ");
                String dateStr = scanner.nextLine();
                try {
                    expiryDate = LocalDate.parse(dateStr);
                    if (expiryDate.isBefore(LocalDate.now())) {
                        System.out.println("Warning: Expiry date is in the past. Using default (6 months)");
                        expiryDate = LocalDate.now().plusMonths(6);
                    }
                } catch (Exception e) {
                    System.out.println("Invalid date format. Using default (6 months)");
                    expiryDate = LocalDate.now().plusMonths(6);
                }
                break;
            default:
                expiryDate = LocalDate.now().plusMonths(6);
        }
        
        Product newProduct = new Product(id, name, price, stockLevel, supplier, expiryDate);
        if (inventoryManager.addNewProduct(newProduct)) {
            System.out.println("Product added successfully");
        } else {
            System.out.println("Failed to add product");
        }
    }

    private void updateStockLevel() {
        System.out.print("Enter product ID: ");
        String id = scanner.nextLine();
        
        if (!inventoryManager.getCurrentInventory().containsKey(id)) {
            System.out.println("Failed to update stock: No such product exists with ID " + id);
            return;
        }
        
        System.out.print("Enter additional quantity: ");
        int quantity = scanner.nextInt();
        
        if (inventoryManager.restockProduct(id, quantity)) {
            System.out.println("Stock updated successfully");
        } else {
            System.out.println("Failed to update stock");
        }
    }

    private void adjustProductPrice() {
        System.out.print("Enter product ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter new price: ");
        double price = scanner.nextDouble();
        
        if (inventoryManager.updateProductPrice(id, price)) {
            System.out.println("Price updated successfully");
        } else {
            System.out.println("Failed to update price");
        }
    }

    private void checkLowStock() {
        List<Product> lowStockProducts = inventoryManager.checkLowStock();
        if (!lowStockProducts.isEmpty()) {
            System.out.println("\nProducts with Low Stock (Below 5 units):");
            System.out.println("ID | Name | Current Stock | Supplier");
            System.out.println("----------------------------------------");
            
            lowStockProducts.forEach(product -> {
                System.out.printf("%s | %s | %d | %s%n",
                    product.getId(),
                    product.getName(),
                    product.getStockLevel(),
                    product.getSupplier());
            });
        } else {
            System.out.println("No products are running low on stock.");
        }
    }

    private void trackExpiringProducts() {
        List<Product> expiringProducts = inventoryManager.trackExpiry();
        if (!expiringProducts.isEmpty()) {
            System.out.println("\nProducts Expiring Within 30 Days:");
            System.out.println("ID | Name | Stock | Expiry Date");
            System.out.println("--------------------------------");
            
            expiringProducts.forEach(product -> {
                System.out.printf("%s | %s | %d | %s%n",
                    product.getId(),
                    product.getName(),
                    product.getStockLevel(),
                    product.getExpirationDate());
            });
        } else {
            System.out.println("No products are expiring within the next 30 days");
        }
    }

    private void removeObsoleteProducts() {
        List<Product> expiredProducts = inventoryManager.getExpiredProducts();
        
        if (expiredProducts.isEmpty()) {
            System.out.println("No expired products found.");
            return;
        }

        System.out.println("\nExpired Products Found:");
        System.out.println("ID | Name | Stock | Expiry Date");
        System.out.println("--------------------------------");
        
        expiredProducts.forEach(product -> {
            System.out.printf("%s | %s | %d | %s%n",
                product.getId(),
                product.getName(),
                product.getStockLevel(),
                product.getExpirationDate());
        });

        System.out.print("\nDo you want to remove these expired products? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        
        if (response.equals("yes")) {
            if (inventoryManager.removeObsoleteProducts(expiredProducts)) {
                System.out.println("Expired products have been removed successfully");
            } else {
                System.out.println("Failed to remove expired products");
            }
        } else {
            System.out.println("Operation cancelled. Products retained in inventory.");
        }
    }

    private void viewStoreInventory() {
        try {
            String filePath = "src/main/java/store/data/" + storeId + "_inventory.txt";
            System.out.println("\nCurrent Inventory for " + storeId + ":");
            System.out.println("ID | Name | Price | Stock | Supplier | Expiry Date");
            System.out.println("------------------------------------------------");
            
            Files.lines(Paths.get(filePath))
                 .forEach(line -> {
                     String[] parts = line.split(",");
                     System.out.printf("%s | %s | $%.2f | %s | %s | %s%n",
                         parts[0], parts[1], Double.parseDouble(parts[2]),
                         parts[3], parts[4], parts[5]);
                 });
        } catch (IOException e) {
            System.out.println("Error reading inventory file: " + e.getMessage());
        }
    }

}
