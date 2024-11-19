/**
 * Represents a delivery order in the retail system.
 *
 * @author Raghuram Guddati
 */
package delivery.model;

public class DeliveryOrder {
    private String id;
    private String customerName;
    private String address;
    private String phone;
    private String status;
    private String items;

    public DeliveryOrder(String[] parts) {
        this.id = parts[0].trim();
        this.customerName = parts[1].trim();
        this.address = parts[2].trim();
        this.phone = parts[3].trim();
        this.status = parts[4].trim();
        this.items = parts[5].trim();
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getStatus() { return status; }
    public String getItems() { return items; }
    
    public void setStatus(String status) { this.status = status; }
}