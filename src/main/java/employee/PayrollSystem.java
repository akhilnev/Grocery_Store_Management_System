/**
 * Main PayrollSystem class that handles the user interface and interaction
 * for employee time tracking and payroll management.
 *
 * @author Akhilesh Nevatia
 */
package employee;

import employee.model.Employee;
import employee.service.PayrollManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class PayrollSystem {
    // Manager instance to handle payroll operations
    private PayrollManager payrollManager;
    // Scanner for user input
    private Scanner scanner;
    // Store identifier
    private String storeId;

    /**
     * Constructor initializes the PayrollSystem with a specific store ID
     * @param storeId The unique identifier for the store
     */
    public PayrollSystem(String storeId) {
        this.storeId = storeId;
        this.payrollManager = new PayrollManager(storeId);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\nPayroll System");
            System.out.println("1. Clock In");
            System.out.println("2. Clock Out");
            System.out.println("3. Start Break");
            System.out.println("4. End Break");
            System.out.println("5. View Time Records");
            System.out.println("6. Approve Overtime");
            System.out.println("7. Generate Payroll Report");
            System.out.println("8. Enter Custom Hours");
            System.out.println("9. Return to Main Menu");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    clockIn();
                    break;
                case 2:
                    clockOut();
                    break;
                case 3:
                    startBreak();
                    break;
                case 4:
                    endBreak();
                    break;
                case 5:
                    viewTimeRecords();
                    break;
                case 6:
                    approveOvertime();
                    break;
                case 7:
                    generatePayrollReport();
                    break;
                case 8:
                    enterCustomHours();
                    break;
                case 9:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void clockIn() {
        System.out.print("Enter employee ID: ");
        String employeeId = scanner.nextLine();
        
        if (payrollManager.clockInEmployee(employeeId)) {
            System.out.println("Clock in successful");
        } else {
            System.out.println("Clock in failed. Employee may already be clocked in or ID invalid");
        }
    }

    private void clockOut() {
        System.out.print("Enter employee ID: ");
        String employeeId = scanner.nextLine();
        
        if (payrollManager.clockOutEmployee(employeeId)) {
            System.out.println("Clock out successful");
        } else {
            System.out.println("Clock out failed. Employee may not be clocked in or ID invalid");
        }
    }

    private void startBreak() {
        System.out.print("Enter employee ID: ");
        String employeeId = scanner.nextLine();
        
        if (payrollManager.startBreak(employeeId)) {
            System.out.println("Break started successfully");
        } else {
            System.out.println("Failed to start break. Employee may not be clocked in or ID invalid");
        }
    }

    private void endBreak() {
        System.out.print("Enter employee ID: ");
        String employeeId = scanner.nextLine();
        
        if (payrollManager.endBreak(employeeId)) {
            System.out.println("Break ended successfully");
        } else {
            System.out.println("Failed to end break. Employee may not be on break or ID invalid");
        }
    }

    private void viewTimeRecords() {
        System.out.print("Enter employee ID: ");
        String employeeId = scanner.nextLine();
        System.out.print("Enter date (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        payrollManager.displayTimeRecords(employeeId, date);
    }

    private void approveOvertime() {
        payrollManager.displayPendingOvertimeRequests();
        
        System.out.print("\nEnter employee ID to approve (or press Enter to cancel): ");
        String employeeId = scanner.nextLine().trim();
        
        if (employeeId.isEmpty()) {
            System.out.println("Approval cancelled");
            return;
        }
        
        System.out.print("Enter date (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        
        try {
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            if (payrollManager.approveOvertime(employeeId, date)) {
                System.out.println("Overtime approved successfully");
            } else {
                System.out.println("Failed to approve overtime. Check employee ID and date");
            }
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd");
        }
    }

    private void generatePayrollReport() {
        System.out.print("Enter start date (yyyy-MM-dd): ");
        String startDateStr = scanner.nextLine();
        System.out.print("Enter end date (yyyy-MM-dd): ");
        String endDateStr = scanner.nextLine();
        
        LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        
        if (endDate.isBefore(startDate)) {
            System.out.println("End date cannot be before start date");
            return;
        }

        double totalPayout = payrollManager.generatePayrollReport(startDate, endDate);
        System.out.printf("Payroll report generated. Total payout: $%.2f%n", totalPayout);
    }

    private void enterCustomHours() {
        System.out.print("Enter employee ID: ");
        String employeeId = scanner.nextLine();
        
        System.out.print("Enter date (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateStr);
        
        System.out.print("Enter start time (HH:mm): ");
        String startTimeStr = scanner.nextLine();
        
        System.out.print("Enter end time (HH:mm): ");
        String endTimeStr = scanner.nextLine();
        
        System.out.print("Enter total break time in minutes: ");
        int breakMinutes = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        payrollManager.addCustomTimeRecord(employeeId, date, startTimeStr, endTimeStr, breakMinutes);
    }
}