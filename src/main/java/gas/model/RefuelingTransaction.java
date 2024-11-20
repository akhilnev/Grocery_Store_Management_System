package gas.model;

import java.time.LocalDateTime;

public class RefuelingTransaction {
    private String transactionId;
    private int pumpNumber;
    private FuelType fuelType;
    private double gallons;
    private double amount;
    private LocalDateTime timestamp;

    public RefuelingTransaction(String transactionId, int pumpNumber, 
                              FuelType fuelType, double gallons) {
        this.transactionId = transactionId;
        this.pumpNumber = pumpNumber;
        this.fuelType = fuelType;
        this.gallons = gallons;
        this.amount = gallons * fuelType.getPricePerGallon();
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public String getTransactionId() { return transactionId; }
    public int getPumpNumber() { return pumpNumber; }
    public FuelType getFuelType() { return fuelType; }
    public double getGallons() { return gallons; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
}