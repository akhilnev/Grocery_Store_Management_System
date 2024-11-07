package returns.model;

import inventory.model.Product;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Return {
    private String returnId;
    private String orderId;
    private String storeId;
    private Map<Product, ReturnItem> items;
    private boolean approved;
    private String refundMethod;
    private double refundAmount;
    private LocalDateTime returnDate;

    public Return(String returnId, String orderId, String storeId) {
        this.returnId = returnId;
        this.orderId = orderId;
        this.storeId = storeId;
        this.items = new HashMap<>();
        this.approved = false;
        this.returnDate = LocalDateTime.now();
        this.refundAmount = 0.0;
    }

    public void addItem(Product product, int quantity, boolean isDamaged) {
        ReturnItem item = new ReturnItem(product, quantity, isDamaged);
        items.put(product, item);
        this.refundAmount += item.getRefundAmount();
    }

    public String getReturnId() { return returnId; }
    public String getOrderId() { return orderId; }
    public String getOriginalOrderId() { return orderId; }
    public Map<Product, ReturnItem> getItems() { return items; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public double getRefundAmount() { return refundAmount; }
    public LocalDateTime getReturnDate() { return returnDate; }
    public void setRefundMethod(String refundMethod) { this.refundMethod = refundMethod; }
}