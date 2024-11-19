package gas.service;

import gas.model.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GasAnalyticsService {
    private GasStationManager gasManager;
    private String storeId;

    public GasAnalyticsService(GasStationManager gasManager, String storeId) {
        this.gasManager = gasManager;
        this.storeId = storeId;
    }

    public void generateSalesReport() {
        String fileName = "./src/main/java/store/data/" + storeId + "_gas_sales_" + 
                         LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".txt";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Gas Station Sales Report for Store: " + storeId + "\n");
            writer.write("Generated on: " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
            // Add sales data
        } catch (IOException e) {
            System.err.println("Error generating sales report: " + e.getMessage());
        }
    }
}