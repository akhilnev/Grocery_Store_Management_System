package security.service;

import java.util.Scanner;
import security.model.SecurityAlertSystem;
import security.model.SecurityIncident;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Manages security operations and incident monitoring in the retail system.
 * This class provides an interface for viewing and managing security incidents.
 *
 * @author Raghuram Guddati
 */
public class SecurityManager {
    private SecurityAlertSystem alertSystem;
    private Scanner scanner;
    private DateTimeFormatter formatter;

    /**
     * Constructor initializes the security management system
     */
    public SecurityManager() {
        this.alertSystem = new SecurityAlertSystem();
        this.scanner = new Scanner(System.in);
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }

    /**
     * Starts the security monitoring interface
     */
    public void startMonitoring() {
        while (true) {
            System.out.println("\nSecurity Management System");
            System.out.println("1. View Incidents");
            System.out.println("2. Report Security Issue");
            System.out.println("3. View Reported Issues");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewIncidents();
                    break;
                case 2:
                    reportSecurityIssue();
                    break;
                case 3:
                    viewReportedIssues();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    /**
     * Displays all security incidents in the system
     */
    private void viewIncidents() {
        System.out.print("Enter start date (yyyy-MM-dd HH:mm): ");
        String startDateStr = scanner.nextLine();
        System.out.print("Enter end date (yyyy-MM-dd HH:mm): ");
        String endDateStr = scanner.nextLine();

        try {
            LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
            LocalDateTime endDate = LocalDateTime.parse(endDateStr, formatter);

            List<SecurityIncident> filteredIncidents = alertSystem.getIncidentsInRange(startDate, endDate);
            if (filteredIncidents.isEmpty()) {
                System.out.println("No incidents found in the specified time range.");
            } else {
                for (SecurityIncident incident : filteredIncidents) {
                    System.out.printf("ID: %s, Description: %s, Severity: %s, Timestamp: %s, Resolved: %s%n",
                            incident.getId(), incident.getDescription(), incident.getSeverity(),
                            incident.getTimestamp(), incident.isResolved() ? "Yes" : "No");
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use format: yyyy-MM-dd HH:mm");
        }
    }

    private void reportSecurityIssue() {
        System.out.print("Enter description of the security issue: ");
        String description = scanner.nextLine();
        System.out.print("Enter severity (Low, Medium, High): ");
        String severity = scanner.nextLine();
        alertSystem.reportSecurityIssue(description, severity);
    }

    private void viewReportedIssues() {
        List<SecurityIncident> reportedIssues = alertSystem.getReportedIssues();
        if (reportedIssues.isEmpty()) {
            System.out.println("No reported issues found.");
        } else {
            for (SecurityIncident incident : reportedIssues) {
                System.out.printf("ID: %s, Description: %s, Severity: %s, Timestamp: %s, Resolved: %s, Status: %s%n",
                        incident.getId(), incident.getDescription(), incident.getSeverity(),
                        incident.getTimestamp(), incident.isResolved() ? "Yes" : "No", incident.getStatus());
            }
        }
    }
}
