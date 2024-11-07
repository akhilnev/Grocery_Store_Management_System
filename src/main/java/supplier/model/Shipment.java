package supplier.model;

import inventory.model.Product;
import java.time.LocalDateTime;
import java.util.*;

public class Shipment {
    private String id;
    private String storeId;
    private String supplierId;
    private Map<Product, Integer> items;
    private LocalDateTime scheduledDate;
    private LocalDateTime deliveryDate;
    private String status; // SCHEDULED, CONFIRMED, DELIVERED, VERIFIED
    private Map<String, Integer> discrepancies; // productId -> quantity difference

    public Shipment(String id, String storeId, String supplierId, LocalDateTime scheduledDate) {
        this.id = id;
        this.storeId = storeId;
        this.supplierId = supplierId;
        this.scheduledDate = scheduledDate;
        this.items = new HashMap<>();
        this.discrepancies = new HashMap<>();
        this.status = "SCHEDULED";
    }

    public void addItem(Product product, int quantity) {
        items.put(product, quantity);
    }

    public void recordDiscrepancy(String productId, int difference) {
        discrepancies.put(productId, difference);
    }
    public LocalDateTime getScheduledDate() {
    return scheduledDate;
}

    // Getters and setters
    public String getId() { return id; }
    public String getStoreId() { return storeId; }
    public String getSupplierId() { return supplierId; }
    public Map<Product, Integer> getItems() { return items; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public void setDeliveryDate(LocalDateTime date) { this.deliveryDate = date; }
}