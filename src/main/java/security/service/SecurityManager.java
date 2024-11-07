package security.service;

import java.util.Scanner;
import security.model.SecurityAlertSystem;
import security.model.SecurityIncident;

public class SecurityManager {
    private SecurityAlertSystem alertSystem;
    private Scanner scanner;

    public SecurityManager() {
        this.alertSystem = new SecurityAlertSystem();
        this.scanner = new Scanner(System.in);
    }

    public void startMonitoring() {
        while (true) {
            System.out.println("\nSecurity Management System");
            System.out.println("1. View Incidents");
            System.out.println("2. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewIncidents();
                    break;
                case 2:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void viewIncidents() {
        System.out.println("Security Incidents:");
        for (SecurityIncident incident : alertSystem.getIncidents()) {
            System.out.printf("ID: %s, Description: %s, Severity: %s, Timestamp: %s, Resolved: %s%n",
                    incident.getId(), incident.getDescription(), incident.getSeverity(),
                    incident.getTimestamp(), incident.isResolved() ? "Yes" : "No");
        }
    }
}