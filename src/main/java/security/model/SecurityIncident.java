package security.model;

import java.time.LocalDateTime;

public class SecurityIncident {
    private String id;
    private String description;
    private String severity;
    private LocalDateTime timestamp;
    private boolean isResolved;

    public SecurityIncident(String id, String description, String severity, String timestamp, boolean isResolved) {
        this.id = id;
        this.description = description;
        this.severity = severity;
        this.timestamp = LocalDateTime.parse(timestamp.replace(" ", "T"));
        this.isResolved = isResolved;
    }

    public String getId() { return id; }
    public String getDescription() { return description; }
    public String getSeverity() { return severity; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public boolean isResolved() { return isResolved; }
}