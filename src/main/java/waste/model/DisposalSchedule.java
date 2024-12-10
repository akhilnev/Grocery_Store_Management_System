package waste.model;

import java.time.LocalDateTime;

public class DisposalSchedule {
    private String vendorId;
    private String vendorName;
    private LocalDateTime pickupTime;
    private String wasteCategory;
    private String status; // SCHEDULED, COMPLETED, CANCELLED
    private String manifestId;

    public DisposalSchedule(String vendorId, String vendorName, LocalDateTime pickupTime, String wasteCategory) {
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.pickupTime = pickupTime;
        this.wasteCategory = wasteCategory;
        this.status = "SCHEDULED";
        this.manifestId = "MAN" + System.currentTimeMillis();
    }

    // Getters and setters
    public String getVendorId() { return vendorId; }
    public String getVendorName() { return vendorName; }
    public LocalDateTime getPickupTime() { return pickupTime; }
    public String getWasteCategory() { return wasteCategory; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getManifestId() { return manifestId; }
} 