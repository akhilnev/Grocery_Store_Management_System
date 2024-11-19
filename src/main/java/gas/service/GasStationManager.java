package gas.service;

import gas.model.*;
import java.util.*;
import java.io.*;
import java.time.LocalDateTime;

public class GasStationManager {
    private Map<Integer, PumpStatus> pumps;
    private Map<FuelType, Double> fuelStock;
    private Map<String, RefuelingTransaction> transactions;
    private String storeId;
    private static final double MINIMUM_STOCK_LEVEL = 500.0; // gallons

    public GasStationManager(String storeId) {
        this.storeId = storeId;
        this.pumps = new HashMap<>();
        this.fuelStock = new HashMap<>();
        this.transactions = new HashMap<>();
        initializePumps();
        loadFuelStock();
    }

    private void initializePumps() {
        for (int i = 1; i <= 8; i++) {
            pumps.put(i, new PumpStatus(i));
        }
    }

    public boolean isPumpOperational(int pumpNumber) {
        PumpStatus status = pumps.get(pumpNumber);
        return status != null && status.isOperational() && !status.isInUse();
    }

    public boolean startRefueling(RefuelingTransaction transaction) {
        // Check fuel availability
        if (!hasSufficientFuel(transaction.getFuelType(), transaction.getGallons())) {
            System.out.println("Insufficient fuel stock");
            return false;
        }

        // Mark pump as in use
        PumpStatus pump = pumps.get(transaction.getPumpNumber());
        pump.setInUse(true);
        
        // Record transaction
        transactions.put(transaction.getTransactionId(), transaction);
        return true;
    }

    public void completeRefueling(RefuelingTransaction transaction) {
        // Update fuel stock
        updateFuelStock(transaction.getFuelType(), -transaction.getGallons());
        
        // Free up pump
        PumpStatus pump = pumps.get(transaction.getPumpNumber());
        pump.setInUse(false);
        
        // Save transaction
        saveTransaction(transaction);
        
        // Check stock levels
        checkStockLevels();
    }

    private void checkStockLevels() {
        for (Map.Entry<FuelType, Double> entry : fuelStock.entrySet()) {
            if (entry.getValue() < MINIMUM_STOCK_LEVEL) {
                System.out.println("WARNING: Low fuel stock for " + entry.getKey() + 
                    " - Current level: " + entry.getValue() + " gallons");
            }
        }
    }

    private void saveTransaction(RefuelingTransaction transaction) {
        String fileName = "./src/main/java/store/data/" + storeId + "_gas_transactions.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(String.format("%s,%s,%d,%s,%.2f,%.2f%n",
                transaction.getTransactionId(),
                transaction.getTimestamp(),
                transaction.getPumpNumber(),
                transaction.getFuelType(),
                transaction.getGallons(),
                transaction.getAmount()));
        } catch (IOException e) {
            System.err.println("Error saving transaction: " + e.getMessage());
        }
    }

    private void loadFuelStock() {
        for (FuelType type : FuelType.values()) {
            fuelStock.put(type, 1000.0); // Initialize with 1000 gallons each
        }
    }

    private boolean hasSufficientFuel(FuelType type, double gallons) {
        return fuelStock.getOrDefault(type, 0.0) >= gallons;
    }

    private void updateFuelStock(FuelType type, double gallons) {
        fuelStock.merge(type, gallons, Double::sum);
    }

    public void cancelRefueling(RefuelingTransaction transaction) {
        PumpStatus pump = pumps.get(transaction.getPumpNumber());
        pump.setInUse(false);
        transactions.remove(transaction.getTransactionId());
    }

    public double getFuelStock(FuelType type) {
        return fuelStock.getOrDefault(type, 0.0);
    }

    public void refillTank(FuelType type, double gallons) {
        fuelStock.merge(type, gallons, Double::sum);
        checkStockLevels();
    }

    public boolean performPumpSafetyCheck(int pumpNumber) {
        PumpStatus status = pumps.get(pumpNumber);
        return status != null && status.isOperational();
    }

    public PumpStatus getPumpStatus(int pumpNumber) {
        return pumps.get(pumpNumber);
    }
}