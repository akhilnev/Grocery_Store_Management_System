/**
 * Represents a self-checkout station in the retail system.
 *
 * @author Raghuram Guddati
 */
package checkout.model;

public class CheckoutStation {
    private String id;
    private String status;
    private String cashLevel;
    private String transactionStatus;
    private String lastMaintenance;
    private boolean needsAssistance;

    public CheckoutStation(String[] parts) {
        this.id = parts[0].trim();
        this.status = parts[1].trim();
        this.cashLevel = parts[2].trim();
        this.transactionStatus = parts[3].trim();
        this.lastMaintenance = parts[4].trim();
        this.needsAssistance = Boolean.parseBoolean(parts[5].trim());
    }

    // Getters
    public String getId() { return id; }
    public String getStatus() { return status; }
    public String getCashLevel() { return cashLevel; }
    public String getTransactionStatus() { return transactionStatus; }
    public String getLastMaintenance() { return lastMaintenance; }
    public boolean getNeedsAssistance() { return needsAssistance; }

    // Setters
    public void setStatus(String status) { this.status = status; }
    public void setCashLevel(String level) { this.cashLevel = level; }
    public void setTransactionStatus(String status) { this.transactionStatus = status; }
    public void setNeedsAssistance(boolean needs) { this.needsAssistance = needs; }
}