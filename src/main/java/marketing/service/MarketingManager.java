package marketing.service;

import marketing.model.*;
import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MarketingManager {
    private static final String PROMOTIONS_FILE = "src/main/java/marketing/data/promotions.txt";
    private Map<String, Promotion> activePromotions;
    private Map<String, PromotionPerformance> performances;
    private Scanner scanner;
    private DateTimeFormatter formatter;

    public MarketingManager() {
        this.activePromotions = new HashMap<>();
        this.performances = new HashMap<>();
        this.scanner = new Scanner(System.in);
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        loadPromotions();
    }

    public void start() {
        while (true) {
            try {
                System.out.println("\nMarketing Management");
                System.out.println("1. Create Promotion");
                System.out.println("2. View Promotions");
                System.out.println("3. Add Sale to Promotion");
                System.out.println("4. Exit");
                
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1: createPromotion(); break;
                    case 2: viewPromotions(); break;
                    case 3: addSale(); break;
                    case 4: return;
                    default: System.out.println("Invalid option");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private void createPromotion() {
        try {
            System.out.print("Enter promotion name: ");
            String name = scanner.nextLine();
            
            System.out.println("Available types: PERCENTAGE_OFF, BOGO");
            System.out.print("Enter type: ");
            String type = scanner.nextLine().toUpperCase();
            
            if (!type.equals("PERCENTAGE_OFF") && !type.equals("BOGO")) {
                System.out.println("Invalid promotion type. Using PERCENTAGE_OFF as default.");
                type = "PERCENTAGE_OFF";
            }
            
            double discount = 0.0;
            while (true) {
                try {
                    System.out.print("Enter discount percentage (without % symbol): ");
                    String discountStr = scanner.nextLine().replace("%", "").trim();
                    discount = Double.parseDouble(discountStr);
                    if (discount <= 0 || discount > 100) {
                        System.out.println("Please enter a valid discount between 0 and 100");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number");
                }
            }
            
            LocalDateTime startDate = getDateInput("start");
            LocalDateTime endDate = getDateInput("end");
            
            if (endDate.isBefore(startDate)) {
                System.out.println("End date must be after start date. Setting end date to start date + 7 days");
                endDate = startDate.plusDays(7);
            }
            
            Promotion promo = new Promotion(name, type, discount, startDate, endDate);
            activePromotions.put(promo.getId(), promo);
            performances.put(promo.getId(), new PromotionPerformance(promo.getId()));
            
            savePromotion(promo);
            System.out.println("Promotion created with ID: " + promo.getId());
            
        } catch (Exception e) {
            System.out.println("Error creating promotion: " + e.getMessage());
        }
    }

    private LocalDateTime getDateInput(String dateType) {
        while (true) {
            try {
                System.out.printf("Enter %s date (yyyy-MM-dd HH:mm): ", dateType);
                String dateStr = scanner.nextLine();
                return LocalDateTime.parse(dateStr, formatter);
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use format: yyyy-MM-dd HH:mm");
            }
        }
    }

    private void viewPromotions() {
        if (activePromotions.isEmpty()) {
            System.out.println("No active promotions");
            return;
        }

        for (Promotion promo : activePromotions.values()) {
            PromotionPerformance perf = performances.get(promo.getId());
            System.out.printf("ID: %s, Name: %s, Type: %s, Discount: %.1f%%, Sales: %d, Revenue: $%.2f%n",
                promo.getId(), promo.getName(), promo.getType(), 
                promo.getDiscount(), perf.getTotalSales(), perf.getRevenue());
        }
    }

    private void addSale() {
        try {
            System.out.print("Enter promotion ID: ");
            String promoId = scanner.nextLine();
            
            if (!activePromotions.containsKey(promoId)) {
                System.out.println("Promotion not found");
                return;
            }

            System.out.print("Enter sale amount: $");
            double amount = Double.parseDouble(scanner.nextLine());
            
            if (amount < 0) {
                System.out.println("Sale amount cannot be negative");
                return;
            }
            
            performances.get(promoId).addSale(amount);
            System.out.println("Sale added to promotion");
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number for sale amount");
        }
    }

    private void loadPromotions() {
        // Create directory if it doesn't exist
        File directory = new File("src/main/java/marketing/data");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(PROMOTIONS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split(",");
                    if (parts.length >= 6) {
                        // Parse the data
                        String id = parts[0];
                        String name = parts[1];
                        String type = parts[2];
                        double discount = Double.parseDouble(parts[3]);
                        LocalDateTime startDate = LocalDateTime.parse(parts[4], formatter);
                        LocalDateTime endDate = LocalDateTime.parse(parts[5], formatter);

                        // Create and store the promotion
                        Promotion promo = new Promotion(name, type, discount, startDate, endDate);
                        activePromotions.put(id, promo);
                        performances.put(id, new PromotionPerformance(id));
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing promotion: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("No existing promotions found. Starting fresh.");
            // Create the file if it doesn't exist
            try {
                new File(PROMOTIONS_FILE).createNewFile();
            } catch (IOException ex) {
                System.err.println("Error creating promotions file: " + ex.getMessage());
            }
        }
    }

    private void savePromotion(Promotion promo) {
        try (FileWriter writer = new FileWriter(PROMOTIONS_FILE, true)) {
            // Format: ID,Name,Type,Discount,StartDate,EndDate
            writer.write(String.format("%s,%s,%s,%.2f,%s,%s%n",
                promo.getId(),
                promo.getName(),
                promo.getType(),
                promo.getDiscount(),
                promo.getStartDate().format(formatter),
                promo.getEndDate().format(formatter)));
        } catch (IOException e) {
            System.err.println("Error saving promotion: " + e.getMessage());
        }
    }

    // Optional: Method to update promotion file (removes old data and rewrites everything)
    private void updatePromotionsFile() {
        try (FileWriter writer = new FileWriter(PROMOTIONS_FILE, false)) {
            for (Promotion promo : activePromotions.values()) {
                writer.write(String.format("%s,%s,%s,%.2f,%s,%s%n",
                    promo.getId(),
                    promo.getName(),
                    promo.getType(),
                    promo.getDiscount(),
                    promo.getStartDate().format(formatter),
                    promo.getEndDate().format(formatter)));
            }
        } catch (IOException e) {
            System.err.println("Error updating promotions file: " + e.getMessage());
        }
    }
}