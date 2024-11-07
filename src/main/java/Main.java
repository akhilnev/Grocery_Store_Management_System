import inventory.InventoryManagementSystem;
import order.OrderManagementSystem;
import store.model.Store;
import store.service.StoreManager;
import security.service.SecurityManager;
import assistance.service.CustomerAssistanceManager;
import marketing.service.MarketingManager;
import java.util.Scanner;
import supplier.SupplierManagementSystem;
import report.SalesReportSystem;
import returns.ReturnManagementSystem;
import employee.PayrollSystem;

public class Main {
    private static StoreManager storeManager = new StoreManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            String storeId = selectStore();
            if (storeId == null) {
                System.out.println("Exiting...");
                break;
            }

            System.out.println("\nGrocery Store Management System");
            System.out.println("1. Inventory Management (Store Manager)");
            System.out.println("2. Order Management (Store Cashier)");
            System.out.println("3. Supplier Management (Store Manager)");
            System.out.println("4. Sales Report (Store Manager)");
            System.out.println("5. Returns and Refunds (Store Manager)");
            System.out.println("6. Employee Payroll (Store Manager)");
            System.out.println("7. Customer Assistance (Service Associate)");
            System.out.println("8. Security Management (Security Officer)");
            System.out.println("9. Marketing Management (Marketing Manager)");
            System.out.println("10. Exit");
            System.out.print("Choose a use case: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    new InventoryManagementSystem(storeId).start();
                    break;
                case 2:
                    new OrderManagementSystem(storeId).start();
                    break;
                case 3:
                    new SupplierManagementSystem(storeId).start();
                    break;
                case 4:
                    new SalesReportSystem(storeId).start();
                    break;
                case 5:
                    new ReturnManagementSystem(storeId).start();
                    break;
                case 6:
                    new PayrollSystem(storeId).start();
                    break;
                case 7:
                    new CustomerAssistanceManager(storeId).startAssistance();
                    break;
                case 8:
                    new SecurityManager().startMonitoring();
                    break;
                case 9:
                    new MarketingManager().start();
                    break;
                case 10:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private static String selectStore() {
        System.out.println("\nAvailable Stores:");
        for (Store store : storeManager.getAllStores()) {
            System.out.printf("%s - %s (%s)%n", 
                store.getStoreId(), 
                store.getName(), 
                store.getLocation());
        }
        System.out.print("Enter Store ID (or 'exit' to quit): ");
        String input = scanner.nextLine();
        return input.equalsIgnoreCase("exit") ? null : input;
    }
}