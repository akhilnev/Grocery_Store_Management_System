package returns;

import returns.model.Return;
import returns.service.ReturnManager;
import inventory.service.InventoryManager;
import inventory.service.HeadOfficeManager;
import inventory.model.Product;
import java.util.Map;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class ReturnManagementSystem {
    private ReturnManager returnManager;
    private InventoryManager inventoryManager;
    private Scanner scanner;
    private String storeId;

    public ReturnManagementSystem(String storeId) {
        this.storeId = storeId;
        this.inventoryManager = new InventoryManager(new HeadOfficeManager());
        this.inventoryManager.setCurrentStore(storeId);
        this.returnManager = new ReturnManager(storeId, inventoryManager);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\nReturn Management System");
            System.out.println("1. Process New Return");
            System.out.println("2. View Return History");
            System.out.println("3. Approve Pending Returns");
            System.out.println("4. Update Inventory for Returns");
            System.out.println("5. Return to Main Menu");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    processNewReturn();
                    break;
                case 2:
                    viewReturnHistory();
                    break;
                case 3:
                    approvePendingReturns();
                    break;
                case 4:
                    updateInventoryForReturns();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private boolean verifyOrder(String orderId) {
        String orderFileName = "./src/main/java/store/data/" + storeId + "_orders.txt";
        File orderFile = new File(orderFileName);
        
        if (!orderFile.exists()) {
            return false;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(orderFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].equals(orderId)) {
                    LocalDate orderDate = LocalDate.parse(parts[0]);
                    // Check if return is within 30 days
                    return !orderDate.plusDays(30).isBefore(LocalDate.now());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading order data: " + e.getMessage());
        }
        return false;
    }

    private void processNewReturn() {
        System.out.print("Enter original order ID: ");
        String orderId = scanner.nextLine();
        
        if (!verifyOrder(orderId)) {
            System.out.println("Invalid order ID or order not eligible for return (30-day return policy)");
            return;
        }
        
        System.out.print("Enter return ID: ");
        String returnId = scanner.nextLine();
        
        Return returnOrder = new Return(returnId, orderId, storeId);
        
        System.out.println("\nEnter product details for return:");
        while (true) {
            System.out.print("Enter product ID (or 'done' to finish): ");
            String input = scanner.nextLine();
            
            if (input.equalsIgnoreCase("done")) {
                break;
            }
            
            Product product = inventoryManager.getProduct(input);
            if (product == null) {
                System.out.println("Product not found");
                continue;
            }
            
            System.out.print("Enter quantity to return: ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            System.out.print("Is the product damaged? (yes/no): ");
            boolean isDamaged = scanner.nextLine().equalsIgnoreCase("yes");
            
            returnOrder.addItem(product, quantity, isDamaged);
            
            // Update inventory only for undamaged items
            if (!isDamaged) {
                inventoryManager.restockProduct(product.getId(), quantity);
                System.out.println("Inventory updated: " + quantity + " units of " + product.getName() + " restocked");
            }
        }
        
        returnManager.processReturn(returnOrder);
        System.out.println("Return processed successfully");
        saveReturnReport(returnOrder);
    }

    private void saveReturnReport(Return returnOrder) {
        String fileName = "./src/main/java/store/data/" + storeId + "_returns.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(String.format("Return ID: %s, Order ID: %s, Date: %s%n", 
                returnOrder.getReturnId(), returnOrder.getOrderId(), LocalDate.now()));
            writer.write("Items returned:\n");
            
            for (Map.Entry<Product, ReturnItem> entry : returnOrder.getItems().entrySet()) {
                Product product = entry.getKey();
                ReturnItem item = entry.getValue();
                writer.write(String.format("- %s (ID: %s), Quantity: %d, Damaged: %s, Refund Amount: $%.2f%n",
                    product.getName(),
                    product.getId(),
                    item.getQuantity(),
                    item.isDamaged() ? "Yes" : "No",
                    item.getRefundAmount()));
            }
            writer.write("----------------------------------------\n");
        } catch (IOException e) {
            System.err.println("Error saving return report: " + e.getMessage());
        }
    }

    private void viewReturnHistory() {
        System.out.println("\nReturn History:");
        System.out.println("--------------------------------------------------");
        System.out.printf("%-12s %-12s %-10s %-8s %s%n", 
            "Return ID", "Order ID", "Amount", "Status", "Date");
        System.out.println("--------------------------------------------------");
        
        Map<String, Return> returns = returnManager.getAllReturns();
        for (Return ret : returns.values()) {
            System.out.printf("%-12s %-12s $%-9.2f %-8s %s%n",
                ret.getReturnId(),
                ret.getOriginalOrderId(),
                ret.getRefundAmount(),
                ret.isApproved() ? "Approved" : "Pending",
                ret.getReturnDate().toLocalDate());
        }
        System.out.println("--------------------------------------------------");
    }

    private void approvePendingReturns() {
        System.out.print("Enter return ID to approve: ");
        String returnId = scanner.nextLine();
        
        if (returnManager.approveReturn(returnId)) {
            System.out.println("Return approved successfully");
            System.out.println("Inventory will be updated for non-damaged items");
        } else {
            System.out.println("Failed to approve return");
        }
    }

    private void updateInventoryForReturns() {
        if (returnManager.processApprovedReturns()) {
            System.out.println("Inventory updated successfully for approved returns");
        } else {
            System.out.println("No pending inventory updates");
        }
    }
}