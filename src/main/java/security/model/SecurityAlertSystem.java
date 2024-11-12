/**
 * Manages the security alert system for the retail environment.
 * This class handles loading and tracking security incidents.
 *
 * @author Raghuram Guddati
 */
package security.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SecurityAlertSystem {
    // List to store all security incidents
    private List<SecurityIncident> incidents;

    /**
     * Constructor initializes the system and loads existing incidents
     */
    public SecurityAlertSystem() {
        this.incidents = new ArrayList<>();
        loadIncidents();
    }

    /**
     * Loads security incidents from the data file
     */
    private void loadIncidents() {
        try (BufferedReader reader = new BufferedReader(new FileReader("./src/main/java/security/data/security_incidents.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                SecurityIncident incident = new SecurityIncident(parts[0], parts[1], parts[2], parts[3], parts[4].equals("Resolved"), "Reported");
                incidents.add(incident);
            }
        } catch (IOException e) {
            System.err.println("Error loading incidents: " + e.getMessage());
        }
    }

    /**
     * Returns the list of all security incidents
     */
    public List<SecurityIncident> getIncidents() {
        return incidents;
    }

    /**
     * Returns the list of security incidents within a specified time range
     */
    public List<SecurityIncident> getIncidentsInRange(LocalDateTime start, LocalDateTime end) {
        List<SecurityIncident> filteredIncidents = new ArrayList<>();
        for (SecurityIncident incident : incidents) {
            if (!incident.getTimestamp().isBefore(start) && !incident.getTimestamp().isAfter(end)) {
                filteredIncidents.add(incident);
            }
        }
        return filteredIncidents;
    }

    public void reportSecurityIssue(String description, String severity) {
        String id = String.valueOf(incidents.size() + 1); // Simple ID generation
        LocalDateTime timestamp = LocalDateTime.now();
        SecurityIncident newIncident = new SecurityIncident(id, description, severity, timestamp.toString(), false, "Reported");
        incidents.add(newIncident);
        System.out.println("Security issue reported successfully.");
    }

    public List<SecurityIncident> getReportedIssues() {
        return incidents;
    }
}