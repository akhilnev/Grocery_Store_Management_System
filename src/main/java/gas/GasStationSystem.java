package gas;

import gas.model.*;
import gas.service.*;
import payment.service.PaymentProcessor;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GasStationSystem {
    private GasStationManager gasManager;
    private PaymentProcessor paymentProcessor;
    private Scanner scanner;
    private String storeId;
    private GasAnalyticsService analyticsService;

    public GasStationSystem(String storeId) {
        this.storeId = storeId;
        this.gasManager = new GasStationManager(storeId);
        this.paymentProcessor = new PaymentProcessor();
        this.analyticsService = new GasAnalyticsService(gasManager, storeId);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\nGas Station Management System");
            System.out.println("1. Start New Refueling");
            System.out.println("2. View Fuel Stock Levels");
            System.out.println("3. Record Tank Refill");
            System.out.println("4. Perform Safety Check");
            System.out.println("5. Generate Sales Report");
            System.out.println("6. View Pump Status");
            System.out.println("7. Return to Main Menu");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: processRefueling(); break;
                case 2: viewFuelStock(); break;
                case 3: recordTankRefill(); break;
                case 4: performSafetyCheck(); break;
                case 5: analyticsService.generateSalesReport(); break;
                case 6: viewPumpStatus(); break;
                case 7: return;
                default: System.out.println("Invalid option");
            }
        }
    }

    private void processRefueling() {
        System.out.print("Enter pump number (1-8): ");
        int pumpNumber = scanner.nextInt();
        scanner.nextLine();

        if (!gasManager.isPumpOperational(pumpNumber)) {
            System.out.println("Pump is not operational. Please choose another pump.");
            return;
        }

        System.out.println("Select payment method:");
        System.out.println("1. Pre-pay at station");
        System.out.println("2. Pay at pump");
        System.out.println("3. Post-pay at counter");
        
        int paymentChoice = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Select fuel type:");
        System.out.println("1. Regular (87)");
        System.out.println("2. Plus (89)");
        System.out.println("3. Premium (93)");
        
        int fuelChoice = scanner.nextInt();
        scanner.nextLine();

        FuelType fuelType = FuelType.values()[fuelChoice - 1];
        
        System.out.print("Enter amount (gallons): ");
        double gallons = scanner.nextDouble();
        scanner.nextLine();

        // Create refueling transaction
        String transactionId = "GAS" + System.currentTimeMillis();
        RefuelingTransaction transaction = new RefuelingTransaction(
            transactionId, pumpNumber, fuelType, gallons
        );

        if (gasManager.startRefueling(transaction)) {
            double amount = transaction.getAmount();
            boolean paymentSuccess = processPayment(paymentChoice, amount);
            
            if (paymentSuccess) {
                gasManager.completeRefueling(transaction);
                printReceipt(transaction);
            } else {
                gasManager.cancelRefueling(transaction);
                System.out.println("Transaction cancelled due to payment failure");
            }
        }
    }

    private boolean processPayment(int paymentChoice, double amount) {
        switch (paymentChoice) {
            case 1: // Pre-pay
            case 2: // Pay at pump
                System.out.println("Enter card number: ");
                String cardNumber = scanner.nextLine();
                return paymentProcessor.processCardPayment(cardNumber, amount);
            
            case 3: // Post-pay
                System.out.println("Collect payment at counter");
                return true;
            
            default:
                return false;
        }
    }

    private void printReceipt(RefuelingTransaction transaction) {
        System.out.println("\n=== Gas Station Receipt ===");
        System.out.println("Transaction ID: " + transaction.getTransactionId());
        System.out.println("Date: " + transaction.getTimestamp());
        System.out.println("Pump: " + transaction.getPumpNumber());
        System.out.println("Fuel Type: " + transaction.getFuelType());
        System.out.println("Gallons: " + transaction.getGallons());
        System.out.printf("Amount: $%.2f%n", transaction.getAmount());
        System.out.println("Thank you for your business!");
        System.out.println("========================");
    }

    private void viewFuelStock() {
        System.out.println("\nCurrent Fuel Stock Levels");
        System.out.println("--------------------------------------------------");
        System.out.printf("%-15s %-12s %-10s%n", "Fuel Type", "Octane", "Gallons");
        
        for (FuelType type : FuelType.values()) {
            System.out.printf("%-15s %-12d %.2f%n",
                type.getName(),
                type.getOctane(),
                gasManager.getFuelStock(type));
        }
    }

    private void recordTankRefill() {
        System.out.println("\nRecord Tank Refill");
        System.out.println("Select fuel type:");
        System.out.println("1. Regular (87)");
        System.out.println("2. Plus (89)");
        System.out.println("3. Premium (93)");
        
        int fuelChoice = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter amount (gallons): ");
        double gallons = scanner.nextDouble();
        scanner.nextLine();

        FuelType fuelType = FuelType.values()[fuelChoice - 1];
        gasManager.refillTank(fuelType, gallons);
        System.out.println("Tank refill recorded successfully");
    }

    private void performSafetyCheck() {
        System.out.println("\nPerforming Safety Check");
        System.out.println("--------------------------------------------------");
        
        for (int i = 1; i <= 8; i++) {
            boolean isOperational = gasManager.performPumpSafetyCheck(i);
            System.out.printf("Pump %d: %s%n", i, 
                isOperational ? "Passed safety check" : "Failed safety check");
        }
    }

    private void viewPumpStatus() {
        System.out.println("\nPump Status Overview");
        System.out.println("--------------------------------------------------");
        System.out.printf("%-8s %-15s %-10s %-15s%n", 
            "Pump #", "Status", "In Use", "Last Maintenance");
        
        for (int i = 1; i <= 8; i++) {
            PumpStatus status = gasManager.getPumpStatus(i);
            System.out.printf("%-8d %-15s %-10s %-15s%n",
                i,
                status.isOperational() ? "Operational" : "Out of Service",
                status.isInUse() ? "Yes" : "No",
                status.getLastMaintenance().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
    }
}