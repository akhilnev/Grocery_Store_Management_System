/**
 * Manages self-checkout operations in the retail system.
 *
 * @author Raghuram Guddati
 */
package checkout.service;

import checkout.model.CheckoutStation;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CheckoutManager {
    private List<CheckoutStation> stations;
    private Scanner scanner;
    private static final String STATIONS_FILE = "./src/main/java/checkout/data/checkout_stations.txt";

    public CheckoutManager() {
        this.stations = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        loadStations();
    }

    private void loadStations() {
        try (BufferedReader reader = new BufferedReader(new FileReader(STATIONS_FILE))) {
            reader.readLine(); // Skip header
            reader.readLine(); // Skip separator
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 6) {
                        stations.add(new CheckoutStation(parts));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading stations: " + e.getMessage());
        }
    }

    public void startCheckoutManagement() {
        while (true) {
            System.out.println("\nSelf-Checkout Management System");
            System.out.println("1. View All Stations");
            System.out.println("2. Update Station Status");
            System.out.println("3. Handle Age Verification");
            System.out.println("4. Manage Cash Levels");
            System.out.println("5. View Alerts");
            System.out.println("6. Return to Main Menu");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewStations();
                    break;
                case 2:
                    updateStationStatus();
                    break;
                case 3:
                    handleAgeVerification();
                    break;
                case 4:
                    manageCashLevels();
                    break;
                case 5:
                    viewAlerts();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void viewStations() {
        System.out.println("\nSelf-Checkout Stations Status:");
        System.out.println("-".repeat(100));
        for (CheckoutStation station : stations) {
            System.out.printf("Station: %s | Status: %s | Cash Level: %s | Transaction: %s | Needs Assistance: %s%n",
                station.getId(), station.getStatus(), station.getCashLevel(),
                station.getTransactionStatus(), station.getNeedsAssistance() ? "YES" : "No");
        }
        System.out.println("-".repeat(100));
    }

    private void updateStationStatus() {
        System.out.print("Enter station ID: ");
        String stationId = scanner.nextLine();

        CheckoutStation station = findStation(stationId);
        if (station == null) {
            System.out.println("Station not found.");
            return;
        }

        System.out.println("Select new status:");
        System.out.println("1. Active");
        System.out.println("2. Inactive");
        System.out.println("3. Maintenance");
        System.out.print("Enter choice (1-3): ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String newStatus = switch (choice) {
            case 1 -> "Active";
            case 2 -> "Inactive";
            case 3 -> "Maintenance";
            default -> null;
        };

        if (newStatus != null) {
            station.setStatus(newStatus);
            System.out.println("Station status updated successfully.");
        } else {
            System.out.println("Invalid status choice.");
        }
    }

    private void handleAgeVerification() {
        List<CheckoutStation> needVerification = stations.stream()
            .filter(s -> s.getTransactionStatus().equals("AgeVerification"))
            .toList();

        if (needVerification.isEmpty()) {
            System.out.println("No stations currently need age verification.");
            return;
        }

        System.out.println("\nStations Requiring Age Verification:");
        for (CheckoutStation station : needVerification) {
            System.out.println("Station: " + station.getId());
            
            System.out.print("Verify age? (Y/N): ");
            String verify = scanner.nextLine();
            
            if (verify.equalsIgnoreCase("Y")) {
                station.setTransactionStatus("InProgress");
                station.setNeedsAssistance(false);
                System.out.println("Age verified for station " + station.getId());
            }
        }
    }

    private void manageCashLevels() {
        List<CheckoutStation> lowCash = stations.stream()
            .filter(s -> s.getCashLevel().equals("Low"))
            .toList();

        if (lowCash.isEmpty()) {
            System.out.println("All stations have normal cash levels.");
            return;
        }

        System.out.println("\nStations with Low Cash:");
        for (CheckoutStation station : lowCash) {
            System.out.println("Station: " + station.getId());
            
            System.out.print("Refill cash? (Y/N): ");
            String refill = scanner.nextLine();
            
            if (refill.equalsIgnoreCase("Y")) {
                station.setCashLevel("Normal");
                System.out.println("Cash refilled for station " + station.getId());
            }
        }
    }

    private void viewAlerts() {
        List<CheckoutStation> alertStations = stations.stream()
            .filter(CheckoutStation::getNeedsAssistance)
            .toList();

        if (alertStations.isEmpty()) {
            System.out.println("No stations currently need assistance.");
            return;
        }

        System.out.println("\nStations Needing Assistance:");
        System.out.println("-".repeat(50));
        for (CheckoutStation station : alertStations) {
            System.out.printf("Station: %s | Status: %s | Issue: %s%n",
                station.getId(), station.getStatus(), station.getTransactionStatus());
        }
    }

    private CheckoutStation findStation(String id) {
        return stations.stream()
            .filter(s -> s.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
}