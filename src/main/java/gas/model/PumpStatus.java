package gas.model;

import java.time.LocalDateTime;

public class PumpStatus {
    private int pumpNumber;
    private boolean operational;
    private boolean inUse;
    private LocalDateTime lastMaintenance;

    public PumpStatus(int pumpNumber) {
        this.pumpNumber = pumpNumber;
        this.operational = true;
        this.inUse = false;
        this.lastMaintenance = LocalDateTime.now();
    }

    public boolean isOperational() { return operational; }
    public boolean isInUse() { return inUse; }
    public void setInUse(boolean inUse) { this.inUse = inUse; }
    public int getPumpNumber() { return pumpNumber; }
    public LocalDateTime getLastMaintenance() { return lastMaintenance; }
}