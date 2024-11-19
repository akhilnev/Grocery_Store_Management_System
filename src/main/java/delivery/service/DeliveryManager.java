/**
 * Manages delivery operations in the retail system.
 *
 * @author Raghuram Guddati
 */
package delivery.service;

import delivery.model.DeliveryOrder;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeliveryManager {
    private List<DeliveryOrder> deliveries;
    private Scanner scanner;
    private static final String DELIVERY_FILE = "./src/main/java/delivery/data/delivery_orders.txt";

    public DeliveryManager() {
        this.deliveries = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        loadDeliveries();
    }

    private void loadDeliveries() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DELIVERY_FILE))) {
            reader.readLine(); // Skip header
            reader.readLine(); // Skip separator
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 6) {
                        deliveries.add(new DeliveryOrder(parts));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading deliveries: " + e.getMessage());
        }
    }

    public void startDeliveryManagement() {
        while (true) {
            System.out.println("\nDelivery Management System");
            System.out.println("1. View All Deliveries");
            System.out.println("2. Create New Delivery");  // New option
            System.out.println("3. Update Delivery Status");
            System.out.println("4. Return to Main Menu");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewDeliveries();
                    break;
                case 2:
                    createDelivery();  // New method
                    break;
                case 3:
                    updateDeliveryStatus();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void viewDeliveries() {
        if (deliveries.isEmpty()) {
            System.out.println("No deliveries found.");
            return;
        }

        System.out.println("\nCurrent Deliveries:");
        System.out.println("-".repeat(100));
        for (DeliveryOrder delivery : deliveries) {
            System.out.printf("ID: %s | Customer: %s | Address: %s | Status: %s | Items: %s%n",
                delivery.getId(), delivery.getCustomerName(), 
                delivery.getAddress(), delivery.getStatus(), delivery.getItems());
        }
        System.out.println("-".repeat(100));
    }

    // New method to create delivery
    private void createDelivery() {
        System.out.println("\nCreating New Delivery");
        System.out.println("-".repeat(30));

        // Generate new ID based on existing deliveries
        String newId = String.valueOf(deliveries.size() + 1);

        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine();

        System.out.print("Enter delivery address: ");
        String address = scanner.nextLine();

        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();

        System.out.print("Enter items (comma-separated, e.g., TV, Fridge): ");
        String items = scanner.nextLine();

        // Create array of parts for new delivery
        String[] deliveryParts = new String[]{
            newId,
            customerName,
            address,
            phone,
            "Pending",  // Initial status
            items
        };

        try {
            DeliveryOrder newDelivery = new DeliveryOrder(deliveryParts);
            deliveries.add(newDelivery);
            System.out.println("Delivery created successfully! Delivery ID: " + newId);
        } catch (Exception e) {
            System.out.println("Error creating delivery: " + e.getMessage());
        }
    }

    private void updateDeliveryStatus() {
        System.out.print("Enter delivery ID: ");
        String id = scanner.nextLine();

        DeliveryOrder delivery = deliveries.stream()
            .filter(d -> d.getId().equals(id))
            .findFirst()
            .orElse(null);

        if (delivery == null) {
            System.out.println("Delivery not found.");
            return;
        }

        System.out.println("Select new status:");
        System.out.println("1. Pending");
        System.out.println("2. En Route");
        System.out.println("3. Delivered");
        System.out.print("Enter choice (1-3): ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String newStatus = switch (choice) {
            case 1 -> "Pending";
            case 2 -> "En Route";
            case 3 -> "Delivered";
            default -> null;
        };

        if (newStatus != null) {
            delivery.setStatus(newStatus);
            System.out.println("Status updated successfully.");
        } else {
            System.out.println("Invalid status choice.");
        }
    }
}