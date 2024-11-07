package supplier.model;

public class Supplier {
    private String id;
    private String name;
    private String contact;
    private String email;
    private double rating;
    private int totalShipments;
    private int lateDeliveries;

    public Supplier(String id, String name, String contact, String email) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.rating = 5.0;
        this.totalShipments = 0;
        this.lateDeliveries = 0;
    }

    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getContact() { return contact; }
    public String getEmail() { return email; }
    public double getRating() { return rating; }
    
    public void updatePerformance(boolean isLateDelivery) {
        totalShipments++;
        if (isLateDelivery) lateDeliveries++;
        rating = 5.0 * (1 - ((double)lateDeliveries / totalShipments));
    }
}