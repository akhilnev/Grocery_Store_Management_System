package returns.model;

import inventory.model.Product;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Return {
    private String returnId;
    private String originalOrderId;
    private String storeId;
    private Map<Product, Integer> items;
    private double refundAmount;
    private String refundMethod;
    private boolean isApproved;
    private boolean isDamaged;
    private LocalDateTime returnDate;

    public Return(String returnId, String originalOrderId, String storeId) {
        this.returnId = returnId;
        this.originalOrderId = originalOrderId;
        this.storeId = storeId;
        this.items = new HashMap<>();
        this.returnDate = LocalDateTime.now();
        this.isApproved = false;
        this.isDamaged = false;
    }

    // Getters and setters
    public String getReturnId() { return returnId; }
    public String getOriginalOrderId() { return originalOrderId; }
    public String getStoreId() { return storeId; }
    public Map<Product, Integer> getItems() { return items; }
    public double getRefundAmount() { return refundAmount; }
    public String getRefundMethod() { return refundMethod; }
    public boolean isApproved() { return isApproved; }
    public boolean isDamaged() { return isDamaged; }
    public LocalDateTime getReturnDate() { return returnDate; }

    public void addItem(Product product, int quantity, boolean isDamaged) {
        this.isDamaged = isDamaged;
        items.put(product, quantity);
        calculateRefund();
    }

    public void setRefundMethod(String refundMethod) {
        this.refundMethod = refundMethod;
    }

    public void approve() {
        this.isApproved = true;
    }

    private void calculateRefund() {
        this.refundAmount = items.entrySet().stream()
            .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
            .sum();
    }
}