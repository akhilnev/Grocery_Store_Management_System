package headoffice;

import store.model.Store;
import store.service.StoreManager;
import java.util.*;
import java.time.LocalDate;

public class StorePerformanceSystem {
    private StoreManager storeManager;
    private Map<String, StoreMetrics> storeMetricsMap;

    public StorePerformanceSystem(StoreManager storeManager) {
        this.storeManager = storeManager;
        this.storeMetricsMap = new HashMap<>();
        initializeMetrics();
    }

    private void initializeMetrics() {
        for (Store store : storeManager.getAllStores()) {
            storeMetricsMap.put(store.getStoreId(), new StoreMetrics());
        }
    }

    public void generateLeaderboard() {
        List<Map.Entry<String, StoreMetrics>> leaderboard = new ArrayList<>(storeMetricsMap.entrySet());
        leaderboard.sort((a, b) -> Double.compare(b.getValue().getTotalScore(), a.getValue().getTotalScore()));

        System.out.println("\n=== Store Performance Leaderboard ===");
        int rank = 1;
        for (Map.Entry<String, StoreMetrics> entry : leaderboard) {
            Store store = storeManager.getStoreById(entry.getKey());
            StoreMetrics metrics = entry.getValue();
            System.out.printf("%d. Store: %s (%s)%n", rank++, store.getName(), store.getStoreId());
            System.out.printf("   Total Sales: $%.2f%n", metrics.getTotalSales());
            System.out.printf("   Customer Satisfaction: %.1f/5.0%n", metrics.getCustomerSatisfaction());
            System.out.printf("   Inventory Turnover: %.2f%n", metrics.getInventoryTurnover());
            System.out.println();
        }
    }

    public void viewDetailedMetrics(String storeId) {
        StoreMetrics metrics = storeMetricsMap.get(storeId);
        if (metrics == null) {
            System.out.println("Store not found!");
            return;
        }

        Store store = storeManager.getStoreById(storeId);
        System.out.println("\n=== Detailed Performance Metrics ===");
        System.out.println("Store: " + store.getName() + " (" + storeId + ")");
        System.out.println("\nSales Metrics:");
        System.out.printf("- Total Sales: $%.2f%n", metrics.getTotalSales());
        System.out.printf("- Daily Average: $%.2f%n", metrics.getDailyAverage());
        System.out.printf("- Customer Conversion Rate: %.1f%%%n", metrics.getConversionRate());

        System.out.println("\nCustomer Metrics:");
        System.out.printf("- Customer Satisfaction: %.1f/5.0%n", metrics.getCustomerSatisfaction());
        System.out.printf("- Daily Footfall: %d%n", metrics.getDailyFootfall());

        System.out.println("\nInventory Metrics:");
        System.out.printf("- Inventory Turnover: %.2f%n", metrics.getInventoryTurnover());
        System.out.printf("- Stockout Rate: %.1f%%%n", metrics.getStockoutRate());
        System.out.printf("- Wastage Rate: %.1f%%%n", metrics.getWastageRate());
    }

    public void generateSalesProjections(String storeId) {
        StoreMetrics metrics = storeMetricsMap.get(storeId);
        if (metrics == null) {
            System.out.println("Store not found!");
            return;
        }

        Store store = storeManager.getStoreById(storeId);
        System.out.println("\n=== Sales Projections ===");
        System.out.println("Store: " + store.getName() + " (" + storeId + ")");
        
        // Calculate growth factor based on multiple metrics
        double customerSatisfactionImpact = (metrics.getCustomerSatisfaction() / 5.0) * 0.3; // 30% weight
        double inventoryEfficiencyImpact = calculateInventoryEfficiency(metrics) * 0.2; // 20% weight
        double conversionRateImpact = (metrics.getConversionRate() / 100.0) * 0.3; // 30% weight
        double footfallImpact = Math.min(metrics.getDailyFootfall() / 300.0, 1.0) * 0.2; // 20% weight

        double growthFactor = 1.0 + (customerSatisfactionImpact + inventoryEfficiencyImpact + 
                                    conversionRateImpact + footfallImpact);

        // Calculate projected sales
        double projectedSales = metrics.getTotalSales() * growthFactor;

        // Calculate confidence interval based on metric stability
        double confidenceInterval = calculateConfidenceInterval(metrics);

        // Print detailed projection analysis
        System.out.println("\nGrowth Factor Analysis:");
        System.out.printf("- Customer Satisfaction Impact: %.1f%%%n", customerSatisfactionImpact * 100);
        System.out.printf("- Inventory Efficiency Impact: %.1f%%%n", inventoryEfficiencyImpact * 100);
        System.out.printf("- Conversion Rate Impact: %.1f%%%n", conversionRateImpact * 100);
        System.out.printf("- Footfall Impact: %.1f%%%n", footfallImpact * 100);
        System.out.printf("\nTotal Growth Factor: %.1f%%%n", (growthFactor - 1) * 100);

        System.out.printf("\nProjected Monthly Sales: $%.2f%n", projectedSales);
        System.out.printf("Confidence Interval: ±%.1f%%%n", confidenceInterval * 100);
        System.out.printf("Range: $%.2f - $%.2f%n", 
            projectedSales * (1 - confidenceInterval),
            projectedSales * (1 + confidenceInterval));
    }

    private double calculateInventoryEfficiency(StoreMetrics metrics) {
        // Higher inventory turnover is good, but high stockout rate is bad
        double turnoverScore = Math.min(metrics.getInventoryTurnover() / 8.0, 1.0); // normalize to max of 1.0
        double stockoutPenalty = metrics.getStockoutRate() / 100.0; // convert to decimal
        double wastagePenalty = metrics.getWastageRate() / 100.0; // convert to decimal
        
        return Math.max(0, turnoverScore - (stockoutPenalty + wastagePenalty));
    }

    private double calculateConfidenceInterval(StoreMetrics metrics) {
        // Base confidence interval starts at 5%
        double baseInterval = 0.05;
        
        // Adjust based on metric volatility
        double volatilityFactor = 1.0;
        
        // Higher stockout rates increase uncertainty
        if (metrics.getStockoutRate() > 3.0) {
            volatilityFactor += 0.2;
        }
        
        // Lower customer satisfaction increases uncertainty
        if (metrics.getCustomerSatisfaction() < 4.0) {
            volatilityFactor += 0.15;
        }
        
        // High wastage rates increase uncertainty
        if (metrics.getWastageRate() > 2.0) {
            volatilityFactor += 0.15;
        }
        
        // Low conversion rates increase uncertainty
        if (metrics.getConversionRate() < 25.0) {
            volatilityFactor += 0.1;
        }
        
        return baseInterval * volatilityFactor;
    }
}

class StoreMetrics {
    private double totalSales;
    private double customerSatisfaction;
    private double inventoryTurnover;
    private int dailyFootfall;
    private double conversionRate;
    private double stockoutRate;
    private double wastageRate;

    public StoreMetrics() {
        // Initialize with sample data (in real implementation, these would come from database)
        this.totalSales = Math.random() * 100000;
        this.customerSatisfaction = 3.5 + Math.random() * 1.5;
        this.inventoryTurnover = 5 + Math.random() * 3;
        this.dailyFootfall = (int)(100 + Math.random() * 500);
        this.conversionRate = 20 + Math.random() * 40;
        this.stockoutRate = Math.random() * 5;
        this.wastageRate = Math.random() * 3;
    }

    // Getters
    public double getTotalSales() { return totalSales; }
    public double getCustomerSatisfaction() { return customerSatisfaction; }
    public double getInventoryTurnover() { return inventoryTurnover; }
    public int getDailyFootfall() { return dailyFootfall; }
    public double getConversionRate() { return conversionRate; }
    public double getStockoutRate() { return stockoutRate; }
    public double getWastageRate() { return wastageRate; }
    public double getDailyAverage() { return totalSales / 30; }
    public double getTotalScore() { 
        return (totalSales / 10000) + (customerSatisfaction * 10) + inventoryTurnover;
    }
}