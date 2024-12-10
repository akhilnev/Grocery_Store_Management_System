package curbside.model;

public class OrderItem {
    private String productId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private boolean requiresTemperatureControl;
    
    public OrderItem(String productId, String productName, int quantity, 
                    double unitPrice, boolean requiresTemperatureControl) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.requiresTemperatureControl = requiresTemperatureControl;
    }
    
    public double getSubtotal() {
        return quantity * unitPrice;
    }
    
    // Getters
    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public boolean requiresTemperatureControl() { return requiresTemperatureControl; }
}