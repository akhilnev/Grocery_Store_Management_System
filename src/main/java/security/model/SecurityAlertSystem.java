package security.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SecurityAlertSystem {
    private List<SecurityIncident> incidents;

    public SecurityAlertSystem() {
        this.incidents = new ArrayList<>();
        loadIncidents();
    }

    private void loadIncidents() {
        try (BufferedReader reader = new BufferedReader(new FileReader("./src/main/java/security/data/security_incidents.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                SecurityIncident incident = new SecurityIncident(parts[0], parts[1], parts[2], parts[3], parts[4].equals("Resolved"));
                incidents.add(incident);
            }
        } catch (IOException e) {
            System.err.println("Error loading incidents: " + e.getMessage());
        }
    }

    public List<SecurityIncident> getIncidents() {
        return incidents;
    }
}