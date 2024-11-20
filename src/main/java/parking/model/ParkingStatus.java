/**
 * Manages the parking lot status including spaces and cart stations.
 *
 * @author Raghuram Guddati
 */
package parking.model;

public class ParkingStatus {
    // Parking section details
    private String sectionId;
    private int totalSpaces;
    private int occupied;
    private int disabled;
    private String status;
    private String lastUpdated;

    // Cart station details
    private String stationId;
    private String location;
    private int capacity;
    private int currentCarts;
    private String stationStatus;
    private String lastCollected;

    // Constructor for parking section
    public ParkingStatus(String[] parts, boolean isCartStation) {
        if (!isCartStation) {
            this.sectionId = parts[0].trim();
            this.totalSpaces = Integer.parseInt(parts[1].trim());
            this.occupied = Integer.parseInt(parts[2].trim());
            this.disabled = Integer.parseInt(parts[3].trim());
            this.status = parts[4].trim();
            this.lastUpdated = parts[5].trim();
        } else {
            this.stationId = parts[0].trim();
            this.location = parts[1].trim();
            this.capacity = Integer.parseInt(parts[2].trim());
            this.currentCarts = Integer.parseInt(parts[3].trim());
            this.stationStatus = parts[4].trim();
            this.lastCollected = parts[5].trim();
        }
    }

    // Getters for parking section
    public String getSectionId() { return sectionId; }
    public int getTotalSpaces() { return totalSpaces; }
    public int getOccupied() { return occupied; }
    public int getDisabled() { return disabled; }
    public String getStatus() { return status; }
    public String getLastUpdated() { return lastUpdated; }

    // Getters for cart station
    public String getStationId() { return stationId; }
    public String getLocation() { return location; }
    public int getCapacity() { return capacity; }
    public int getCurrentCarts() { return currentCarts; }
    public String getStationStatus() { return stationStatus; }
    public String getLastCollected() { return lastCollected; }

    // Update methods
    public void updateOccupancy(int newOccupied) {
        this.occupied = newOccupied;
    }

    public void updateCartCount(int newCount) {
        this.currentCarts = newCount;
    }
}