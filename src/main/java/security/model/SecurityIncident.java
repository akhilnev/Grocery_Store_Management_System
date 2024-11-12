/**
 * Represents a security incident in the retail system.
 * This class manages security-related incidents including their severity,
 * resolution status, and timing information.
 *
 * @author Raghuram Guddati
 */
package security.model;

import java.time.LocalDateTime;

public class SecurityIncident {
    // Unique identifier for the incident
    private String id;
    // Description of the security incident
    private String description;
    // Severity level of the incident
    private String severity;
    // When the incident occurred
    private LocalDateTime timestamp;
    // Whether the incident has been resolved
    private boolean isResolved;
    // Status of the incident
    private String status;

    /**
     * Constructor for creating a security incident
     */
    public SecurityIncident(String id, String description, String severity, String timestamp, boolean isResolved, String status) {
        this.id = id;
        this.description = description;
        this.severity = severity;
        this.timestamp = LocalDateTime.parse(timestamp.replace(" ", "T"));
        this.isResolved = isResolved;
        this.status = status;
    }

    // Getters
    public String getId() { return id; }
    public String getDescription() { return description; }
    public String getSeverity() { return severity; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public boolean isResolved() { return isResolved; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
