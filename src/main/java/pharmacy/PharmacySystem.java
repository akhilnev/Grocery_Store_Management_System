package pharmacy;

import pharmacy.model.*;
import pharmacy.service.*;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PharmacySystem {
    private PharmacyManager pharmacyManager;
    private PaymentProcessor paymentProcessor;
    private Scanner scanner;
    private String storeId;
    private PharmacyAnalyticsService analyticsService;

    public PharmacySystem(String storeId) {
        this.storeId = storeId;
        this.pharmacyManager = new PharmacyManager(storeId);
        this.paymentProcessor = new PaymentProcessor();
        this.analyticsService = new PharmacyAnalyticsService(pharmacyManager, storeId);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\nPharmacy Management System");
            System.out.println("1. Process Prescription");
            System.out.println("2. View Medication Inventory");
            System.out.println("3. Record Inventory Restock");
            System.out.println("4. Process OTC Purchase");
            System.out.println("5. Generate Inventory Report");
            System.out.println("6. Return to Main Menu");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: processPrescription(); break;
                case 2: viewInventory(); break;
                case 3: recordRestock(); break;
                case 4: processOTCPurchase(); break;
                case 5: generateReport(); break;
                case 6: return;
                default: System.out.println("Invalid option");
            }
        }
    }

    private void processPrescription() {
        System.out.println("\nPrescription Processing");
        System.out.print("Enter prescription ID: ");
        String prescriptionId = scanner.nextLine();

        if (!pharmacyManager.verifyPrescription(prescriptionId)) {
            System.out.println("Invalid or expired prescription");
            return;
        }

        Prescription prescription = pharmacyManager.getPrescription(prescriptionId);
        System.out.println("\nPrescription Details:");
        System.out.println("Patient ID: " + prescription.getCustomerId());
        System.out.println("Medication: " + prescription.getMedicationName());
        System.out.println("Dosage: " + prescription.getDosage());
        System.out.println("Quantity: " + prescription.getQuantity());
        System.out.println("Instructions: " + prescription.getInstructions());
        System.out.println("Doctor: " + prescription.getDoctorName());
        System.out.println("Insurance: " + prescription.getInsuranceProvider());
        System.out.printf("Price: $%.2f%n", prescription.getPrice());

        System.out.print("\nPharmacist verification required. Approve? (yes/no): ");
        String approval = scanner.nextLine();
        
        if (!approval.equalsIgnoreCase("yes")) {
            System.out.println("Prescription rejected by pharmacist");
            return;
        }

        // Get medication ID from name
        String medicationId = null;
        for (Medication med : pharmacyManager.getAllMedications()) {
            if (med.getName().equalsIgnoreCase(prescription.getMedicationName())) {
                medicationId = med.getMedicationId();
                break;
            }
        }

        if (medicationId == null) {
            System.out.println("Medication not found in inventory");
            return;
        }

        // Check medication stock
        if (!pharmacyManager.hasSufficientStock(medicationId, prescription.getQuantity())) {
            System.out.println("Insufficient medication stock");
            return;
        }

        // Process payment
        System.out.println("\nProcessing insurance payment...");
        if (processPayment(1, prescription.getPrice())) {
            pharmacyManager.updateStock(medicationId, prescription.getQuantity());
            System.out.println("\nPrescription filled successfully!");
            System.out.println("Please provide usage instructions to the patient:");
            System.out.println("- " + prescription.getInstructions());
            System.out.println("- " + pharmacyManager.getMedication(medicationId).getWarnings());
        } else {
            System.out.println("Payment processing failed. Please verify insurance information.");
        }
    }

    private void recordRestock() {
        System.out.println("\nRecord Medication Restock");
        System.out.print("Enter medication ID: ");
        String medicationId = scanner.nextLine();
        
        System.out.print("Enter quantity received: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Enter expiration date (YYYY-MM-DD): ");
        String expirationDate = scanner.nextLine();
        
        if (pharmacyManager.recordRestock(medicationId, quantity, expirationDate)) {
            System.out.println("Restock recorded successfully");
        } else {
            System.out.println("Failed to record restock");
        }
    }

    private void generateReport() {
        System.out.println("\nGenerating Pharmacy Report...");
        analyticsService.generateAnalyticsReport();
        System.out.println("Report generated successfully!");
        System.out.println("Location: ./src/main/java/store/data/" + storeId + "_pharmacy_report_" + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".txt");
    }

    private boolean processPayment(int paymentChoice, double amount) {
        String paymentMethod;
        switch (paymentChoice) {
            case 1:
                paymentMethod = "INSURANCE";
                break;
            case 2:
                paymentMethod = "CASH";
                break;
            case 3:
                paymentMethod = "CARD";
                break;
            default:
                return false;
        }
        
        return paymentProcessor.processPayment(paymentMethod, amount);
    }

    private void viewInventory() {
        System.out.println("\nCurrent Medication Inventory");
        System.out.println("--------------------------------------------------");
        System.out.printf("%-15s %-20s %-10s %-10s%n", 
            "ID", "Name", "Stock", "Type");
        
        for (Medication med : pharmacyManager.getAllMedications()) {
            System.out.printf("%-15s %-20s %-10d %-10s%n",
                med.getMedicationId(),
                med.getName(),
                med.getStockLevel(),
                med.getType());
        }
    }

    private void processOTCPurchase() {
        System.out.println("\nOver-the-Counter Purchase");
        System.out.print("Enter medication ID: ");
        String medicationId = scanner.nextLine();

        Medication medication = pharmacyManager.getMedication(medicationId);
        if (medication == null || !medication.getType().equals("OTC")) {
            System.out.println("Invalid OTC medication ID");
            return;
        }

        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        if (!pharmacyManager.hasSufficientStock(medicationId, quantity)) {
            System.out.println("Insufficient stock");
            return;
        }

        double amount = medication.getPrice() * quantity;
        System.out.printf("Total amount: $%.2f%n", amount);

        System.out.println("\nSelect payment method:");
        System.out.println("1. Insurance");
        System.out.println("2. Cash");
        System.out.println("3. Card");
        System.out.print("Choose an option: ");
        
        int paymentChoice = scanner.nextInt();
        scanner.nextLine();

        if (processPayment(paymentChoice, amount)) {
            pharmacyManager.updateStock(medicationId, quantity);
            System.out.println("Purchase completed successfully!");
        } else {
            System.out.println("Payment failed. Transaction cancelled.");
        }
    }
}