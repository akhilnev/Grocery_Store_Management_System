package deli.service;

import deli.model.DeliItem;
import deli.model.DeliOrder;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class DeliCounterManager {
    private static final String INVENTORY_FILE = "./src/main/java/deli/data/deli_inventory.txt";
    private static final String ORDERS_FILE = "./src/main/java/deli/data/deli_orders.txt";

    private List<DeliItem> inventory;
    private Scanner scanner;

    public DeliCounterManager() {
        this.scanner = new Scanner(System.in);
        loadInventory();
    }

    private void loadInventory() {
        inventory = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(INVENTORY_FILE))) {
            reader.readLine(); // Skip header
            reader.readLine(); // Skip separator
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    DeliItem item = DeliItem.fromLine(line);
                    if (item != null) {
                        inventory.add(item);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading deli inventory: " + e.getMessage());
        }
    }

    public void startDeliOperations() {
        while (true) {
            System.out.println("\n=== Deli Counter Operations ===");
            System.out.println("1. View Deli Inventory");
            System.out.println("2. Take Customer Order (Weigh/Prepare)");
            System.out.println("3. View All Orders");
            System.out.println("4. Return to Main Menu");
            System.out.print("Choose an option: ");
            
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
                continue;
            }

            switch (choice) {
                case 1:
                    viewInventory();
                    break;
                case 2:
                    takeCustomerOrder();
                    break;
                case 3:
                    viewAllOrders();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void viewInventory() {
        if (inventory.isEmpty()) {
            System.out.println("No deli items available.");
            return;
        }
        System.out.println("\nDeli Inventory:");
        for (DeliItem item : inventory) {
            System.out.printf("%s - %s: $%.2f per %s, Stock: %d%n",
                item.getItemId(), item.getName(), item.getPricePerUnit(), item.getUnitType(), item.getStockLevel());
        }
    }

    private void takeCustomerOrder() {
        List<String> orderedItems = new ArrayList<>();
        double totalPrice = 0.0;

        System.out.println("\nTaking a new order. Enter 'done' when finished adding items.");

        while (true) {
            System.out.print("Enter item ID or 'done': ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("done")) break;

            DeliItem item = findItemById(input);
            if (item == null) {
                System.out.println("Item not found. Try again.");
                continue;
            }

            System.out.print("Enter quantity (for 'lb' enter weight, for 'each' or 'loaf' enter count): ");
            double quantity;
            try {
                quantity = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Try again.");
                continue;
            }

            if (quantity <= 0 || quantity > item.getStockLevel()) {
                System.out.println("Invalid quantity or not enough stock. Current stock: " + item.getStockLevel());
                continue;
            }

            double cost = quantity * item.getPricePerUnit();
            totalPrice += cost;
            orderedItems.add(item.getName() + " x" + quantity + " " + item.getUnitType());

            // Reduce stock
            item.reduceStock((int)Math.ceil(quantity));

            System.out.printf("Added %s for $%.2f%n", item.getName(), cost);
        }

        if (orderedItems.isEmpty()) {
            System.out.println("No items ordered.");
            return;
        }

        String orderId = "O" + System.currentTimeMillis();
        DeliOrder order = new DeliOrder(orderId, orderedItems, totalPrice);

        saveOrder(order);
        System.out.printf("Order %s created. Total: $%.2f%n", orderId, totalPrice);
        System.out.println("Label printed. Handing over to customer. Inventory updated.");
    }

    private void viewAllOrders() {
        List<DeliOrder> orders = loadOrders();
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        System.out.println("\nDeli Orders:");
        for (DeliOrder order : orders) {
            System.out.printf("%s: %s - $%.2f%n", order.getOrderId(),
                String.join(", ", order.getItems()), order.getTotalPrice());
        }
    }

    private DeliItem findItemById(String id) {
        return inventory.stream()
            .filter(i -> i.getItemId().equalsIgnoreCase(id))
            .findFirst()
            .orElse(null);
    }

    private void saveOrder(DeliOrder order) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDERS_FILE, true))) {
            writer.write(order.toLine());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving order: " + e.getMessage());
        }
    }

    private List<DeliOrder> loadOrders() {
        List<DeliOrder> orders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDERS_FILE))) {
            // The file might have 2 header lines, try to skip them
            reader.readLine(); // header line
            reader.readLine(); // separator line
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    // Format: O001|Item1, Item2|TotalPrice
                    String[] parts = line.split("\\|");
                    if (parts.length == 3) {
                        String oid = parts[0].trim();
                        String itemLine = parts[1].trim();
                        double price = Double.parseDouble(parts[2].trim());
                        List<String> itms = Arrays.stream(itemLine.split(",")).map(String::trim).toList();
                        orders.add(new DeliOrder(oid, itms, price));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading orders: " + e.getMessage());
        }
        return orders;
    }
}
