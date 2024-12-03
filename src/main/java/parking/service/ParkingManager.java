/**
 * Manages parking lot operations and monitoring.
 *
 * @author Chandrashekar Tirunagiri
 */
package parking.service;

import parking.model.ParkingStatus;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParkingManager {
    private List<ParkingStatus> parkingSections;
    private List<ParkingStatus> cartStations;
    private Scanner scanner;
    private static final String PARKING_FILE = "./src/main/java/parking/data/parking_spaces.txt";
    private static final String CART_FILE = "./src/main/java/parking/data/cart_stations.txt";

    public ParkingManager() {
        this.parkingSections = new ArrayList<>();
        this.cartStations = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        loadData();
    }

    private void loadData() {
        loadParkingData();
        loadCartData();
    }

    private void loadParkingData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PARKING_FILE))) {
            reader.readLine(); // Skip header
            reader.readLine(); // Skip separator
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 6) {
                        parkingSections.add(new ParkingStatus(parts, false));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading parking data: " + e.getMessage());
        }
    }

    private void loadCartData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CART_FILE))) {
            reader.readLine(); // Skip header
            reader.readLine(); // Skip separator
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 6) {
                        cartStations.add(new ParkingStatus(parts, true));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading cart data: " + e.getMessage());
        }
    }

    public void startParkingManagement() {
        while (true) {
            System.out.println("\nParking Lot Management System");
            System.out.println("1. View Parking Status");
            System.out.println("2. View Cart Stations Status");
            System.out.println("3. Update Space Occupancy");
            System.out.println("4. Update Cart Station");
            System.out.println("5. Generate Lot Report");
            System.out.println("6. Return to Main Menu");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewParkingStatus();
                    break;
                case 2:
                    viewCartStatus();
                    break;
                case 3:
                    updateSpaceOccupancy();
                    break;
                case 4:
                    updateCartStation();
                    break;
                case 5:
                    generateLotReport();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void viewParkingStatus() {
        System.out.println("\nCurrent Parking Status:");
        System.out.println("-".repeat(80));
        for (ParkingStatus section : parkingSections) {
            int available = section.getTotalSpaces() - section.getOccupied();
            double occupancyRate = (double) section.getOccupied() / section.getTotalSpaces() * 100;
            
            System.out.printf("Section: %s | Available: %d/%d | Disabled: %d | Status: %s | Occupancy: %.1f%%%n",
                section.getSectionId(), available, section.getTotalSpaces(),
                section.getDisabled(), section.getStatus(), occupancyRate);
        }
        System.out.println("-".repeat(80));
    }

    private void viewCartStatus() {
        System.out.println("\nCart Stations Status:");
        System.out.println("-".repeat(80));
        for (ParkingStatus station : cartStations) {
            double fillRate = (double) station.getCurrentCarts() / station.getCapacity() * 100;
            
            System.out.printf("Station: %s | Location: %s | Carts: %d/%d | Status: %s | Fill Rate: %.1f%%%n",
                station.getStationId(), station.getLocation(),
                station.getCurrentCarts(), station.getCapacity(),
                station.getStationStatus(), fillRate);

            if (fillRate >= 80) {
                System.out.println("*** ATTENTION: Collection needed at " + station.getStationId() + " ***");
            }
        }
        System.out.println("-".repeat(80));
    }

    private void updateSpaceOccupancy() {
        System.out.print("Enter section ID: ");
        String sectionId = scanner.nextLine();

        ParkingStatus section = parkingSections.stream()
            .filter(s -> s.getSectionId().equals(sectionId))
            .findFirst()
            .orElse(null);

        if (section == null) {
            System.out.println("Section not found.");
            return;
        }

        System.out.print("Enter new occupied spaces count: ");
        int newCount = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (newCount > section.getTotalSpaces()) {
            System.out.println("Error: Count exceeds total spaces.");
            return;
        }

        section.updateOccupancy(newCount);
        System.out.println("Occupancy updated successfully.");
    }

    private void updateCartStation() {
        System.out.print("Enter station ID: ");
        String stationId = scanner.nextLine();

        ParkingStatus station = cartStations.stream()
            .filter(s -> s.getStationId().equals(stationId))
            .findFirst()
            .orElse(null);

        if (station == null) {
            System.out.println("Station not found.");
            return;
        }

        System.out.print("Enter new cart count: ");
        int newCount = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (newCount > station.getCapacity()) {
            System.out.println("Error: Count exceeds station capacity.");
            return;
        }

        station.updateCartCount(newCount);
        System.out.println("Cart count updated successfully.");
    }

    private void generateLotReport() {
        System.out.println("\nParking Lot Status Report");
        System.out.println("=".repeat(50));

        // Overall parking statistics
        int totalSpaces = parkingSections.stream().mapToInt(ParkingStatus::getTotalSpaces).sum();
        int totalOccupied = parkingSections.stream().mapToInt(ParkingStatus::getOccupied).sum();
        double overallOccupancy = (double) totalOccupied / totalSpaces * 100;

        System.out.printf("Total Spaces: %d%n", totalSpaces);
        System.out.printf("Occupied Spaces: %d%n", totalOccupied);
        System.out.printf("Overall Occupancy: %.1f%%%n", overallOccupancy);

        // Cart station statistics
        int totalCarts = cartStations.stream().mapToInt(ParkingStatus::getCurrentCarts).sum();
        int totalCapacity = cartStations.stream().mapToInt(ParkingStatus::getCapacity).sum();
        double cartUtilization = (double) totalCarts / totalCapacity * 100;

        System.out.println("\nCart Station Status");
        System.out.printf("Total Carts in Stations: %d%n", totalCarts);
        System.out.printf("Total Station Capacity: %d%n", totalCapacity);
        System.out.printf("Cart Station Utilization: %.1f%%%n", cartUtilization);

        // Alerts
        if (overallOccupancy >= 85) {
            System.out.println("\n*** ALERT: Lot nearing capacity! ***");
        }
        
        System.out.println("=".repeat(50));
    }
}