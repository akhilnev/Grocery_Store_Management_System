package inventory;

import inventory.model.Product;
import inventory.service.InventoryManager;
import inventory.service.HeadOfficeManager;
import java.time.LocalDate;
import java.util.Scanner;

public class InventoryManagementSystem {
    private InventoryManager inventoryManager;
    private Scanner scanner;
    private String storeId;

    public InventoryManagementSystem(String storeId) {
        this.storeId = storeId;
        this.inventoryManager = new InventoryManager(new HeadOfficeManager());
        this.inventoryManager.setCurrentStore(storeId);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\nInventory Management System");
            System.out.println("1. Add New Product");
            System.out.println("2. Update Stock Level");
            System.out.println("3. Adjust Product Price");
            System.out.println("4. Check Low Stock");
            System.out.println("5. Track Expiring Products");
            System.out.println("6. Remove Obsolete Products");
            System.out.println("7. Exit");
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
        
        Product newProduct = new Product(id, name, price, stockLevel, supplier, LocalDate.now().plusMonths(6));
        if (inventoryManager.addNewProduct(newProduct)) {
            System.out.println("Product added successfully");
        } else {
            System.out.println("Failed to add product");
        }
    }

    private void updateStockLevel() {
        System.out.print("Enter product ID: ");
        String id = scanner.nextLine();
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
        if (inventoryManager.checkLowStock()) {
            System.out.println("There are products with low stock levels");
        } else {
            System.out.println("All stock levels are adequate");
        }
    }

    private void trackExpiringProducts() {
        if (inventoryManager.trackExpiry()) {
            System.out.println("There are products nearing expiration");
        } else {
            System.out.println("No products are nearing expiration");
        }
    }

    private void removeObsoleteProducts() {
        if (inventoryManager.removeObsoleteProducts()) {
            System.out.println("Obsolete products removed successfully");
        } else {
            System.out.println("Failed to remove obsolete products");
        }
    }

}
