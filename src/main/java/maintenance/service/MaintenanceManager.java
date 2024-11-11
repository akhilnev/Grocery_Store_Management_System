package maintenance.service;

import maintenance.model.*;
import java.time.*;
import java.util.*;
import java.io.*;

public class MaintenanceManager {
    private String storeId;
    private Map<String, CleaningTask> cleaningTasks;
    private Map<String, MaintenanceTask> maintenanceTasks;
    private static final String DATA_DIR = "./src/main/java/store/data/";

    public MaintenanceManager(String storeId) {
        this.storeId = storeId;
        this.cleaningTasks = new HashMap<>();
        this.maintenanceTasks = new HashMap<>();
        loadTasks();
        generateDailyTasks();
    }

    private void loadTasks() {
        loadCleaningTasks();
        loadMaintenanceTasks();
    }

    private void loadCleaningTasks() {
        String fileName = DATA_DIR + storeId + "_cleaning_tasks.txt";
        File file = new File(fileName);
        
        if (!file.exists()) {
            initializeDefaultCleaningTasks();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                CleaningTask task = new CleaningTask(
                    parts[0], // taskId
                    parts[1], // description
                    parts[2], // area
                    TaskFrequency.valueOf(parts[3]),
                    LocalDateTime.parse(parts[4])
                );
                task.setStatus(TaskStatus.valueOf(parts[5]));
                if (parts.length > 6) task.setAssignedTo(parts[6]);
                if (parts.length > 7) task.setNotes(parts[7]);
                cleaningTasks.put(task.getTaskId(), task);
            }
        } catch (IOException e) {
            System.err.println("Error loading cleaning tasks: " + e.getMessage());
        }
    }

    private void loadMaintenanceTasks() {
        String fileName = DATA_DIR + storeId + "_maintenance_tasks.txt";
        File file = new File(fileName);
        
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                MaintenanceTask task = new MaintenanceTask(
                    parts[0], // taskId
                    parts[1], // description
                    parts[2], // area
                    TaskPriority.valueOf(parts[3]),
                    LocalDateTime.parse(parts[4])
                );
                task.setStatus(TaskStatus.valueOf(parts[5]));
                if (parts.length > 6) task.setAssignedTo(parts[6]);
                if (parts.length > 7) task.setNotes(parts[7]);
                maintenanceTasks.put(task.getTaskId(), task);
            }
        } catch (IOException e) {
            System.err.println("Error loading maintenance tasks: " + e.getMessage());
        }
    }

    private void saveTasks() {
        saveCleaningTasks();
        saveMaintenanceTasks();
    }

    private void saveCleaningTasks() {
        String fileName = DATA_DIR + storeId + "_cleaning_tasks.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (CleaningTask task : cleaningTasks.values()) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s%n",
                    task.getTaskId(),
                    task.getDescription(),
                    task.getArea(),
                    task.getFrequency(),
                    task.getScheduledTime(),
                    task.getStatus(),
                    task.getAssignedTo() != null ? task.getAssignedTo() : "",
                    task.getNotes() != null ? task.getNotes() : ""
                ));
            }
        } catch (IOException e) {
            System.err.println("Error saving cleaning tasks: " + e.getMessage());
        }
    }

    private void saveMaintenanceTasks() {
        String fileName = DATA_DIR + storeId + "_maintenance_tasks.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (MaintenanceTask task : maintenanceTasks.values()) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s%n",
                    task.getTaskId(),
                    task.getDescription(),
                    task.getArea(),
                    task.getPriority(),
                    task.getReportedTime(),
                    task.getStatus(),
                    task.getAssignedTo() != null ? task.getAssignedTo() : "",
                    task.getNotes() != null ? task.getNotes() : ""
                ));
            }
        } catch (IOException e) {
            System.err.println("Error saving maintenance tasks: " + e.getMessage());
        }
    }

    private void initializeDefaultCleaningTasks() {
        String[] dailyTasks = {
            "Clean floors in all aisles",
            "Sanitize checkout counters",
            "Clean restrooms",
            "Empty trash bins",
            "Wipe down shopping carts"
        };

        String[] weeklyTasks = {
            "Deep clean refrigerators",
            "Clean windows",
            "Sanitize storage areas",
            "Clean staff break room"
        };

        String[] monthlyTasks = {
            "Deep clean ventilation systems",
            "Clean light fixtures",
            "Pressure wash exterior",
            "Deep clean freezers"
        };

        int taskId = 1;
        LocalDateTime now = LocalDateTime.now();

        for (String task : dailyTasks) {
            CleaningTask cleaningTask = new CleaningTask(
                "CLN" + String.format("%03d", taskId++),
                task,
                "Store",
                TaskFrequency.DAILY,
                now
            );
            cleaningTasks.put(cleaningTask.getTaskId(), cleaningTask);
        }

        for (String task : weeklyTasks) {
            CleaningTask cleaningTask = new CleaningTask(
                "CLN" + String.format("%03d", taskId++),
                task,
                "Store",
                TaskFrequency.WEEKLY,
                now
            );
            cleaningTasks.put(cleaningTask.getTaskId(), cleaningTask);
        }

        for (String task : monthlyTasks) {
            CleaningTask cleaningTask = new CleaningTask(
                "CLN" + String.format("%03d", taskId++),
                task,
                "Store",
                TaskFrequency.MONTHLY,
                now
            );
            cleaningTasks.put(cleaningTask.getTaskId(), cleaningTask);
        }
        saveCleaningTasks();
    }

    public void generateDailyTasks() {
        LocalDate today = LocalDate.now();
        for (CleaningTask task : new ArrayList<>(cleaningTasks.values())) {
            if (shouldGenerateTask(task, today)) {
                CleaningTask newTask = new CleaningTask(
                    generateNewTaskId(),
                    task.getDescription(),
                    task.getArea(),
                    task.getFrequency(),
                    LocalDateTime.now()
                );
                cleaningTasks.put(newTask.getTaskId(), newTask);
            }
        }
        saveCleaningTasks();
    }

    private boolean shouldGenerateTask(CleaningTask task, LocalDate today) {
        if (task.getStatus() != TaskStatus.COMPLETED) return false;
        
        LocalDate taskDate = task.getScheduledTime().toLocalDate();
        switch (task.getFrequency()) {
            case DAILY:
                return !taskDate.equals(today);
            case WEEKLY:
                return taskDate.plusWeeks(1).isBefore(today);
            case MONTHLY:
                return taskDate.plusMonths(1).isBefore(today);
            default:
                return false;
        }
    }

    private String generateNewTaskId() {
        return "CLN" + String.format("%03d", cleaningTasks.size() + 1);
    }

    // Task Management Methods
    public void assignCleaningTask(String taskId, String staffId) {
        CleaningTask task = cleaningTasks.get(taskId);
        if (task != null) {
            task.setAssignedTo(staffId);
            task.setStatus(TaskStatus.IN_PROGRESS);
            saveCleaningTasks();
        }
    }

    public void completeCleaningTask(String taskId, String notes) {
        CleaningTask task = cleaningTasks.get(taskId);
        if (task != null) {
            task.setStatus(TaskStatus.COMPLETED);
            task.setCompletedTime(LocalDateTime.now());
            task.setNotes(notes);
            saveCleaningTasks();
        }
    }

    public void reportMaintenanceIssue(String description, String area, TaskPriority priority) {
        MaintenanceTask task = new MaintenanceTask(
            "MNT" + String.format("%03d", maintenanceTasks.size() + 1),
            description,
            area,
            priority,
            LocalDateTime.now()
        );
        maintenanceTasks.put(task.getTaskId(), task);
        saveMaintenanceTasks();
    }

    public void assignMaintenanceTask(String taskId, String technicianId) {
        MaintenanceTask task = maintenanceTasks.get(taskId);
        if (task != null) {
            task.setAssignedTo(technicianId);
            task.setStatus(TaskStatus.IN_PROGRESS);
            saveMaintenanceTasks();
        }
    }

    public void completeMaintenanceTask(String taskId, String notes) {
        MaintenanceTask task = maintenanceTasks.get(taskId);
        if (task != null) {
            task.setStatus(TaskStatus.COMPLETED);
            task.setCompletedTime(LocalDateTime.now());
            task.setNotes(notes);
            saveMaintenanceTasks();
        }
    }

    public void generateMaintenanceReport(LocalDate startDate, LocalDate endDate) {
        String fileName = DATA_DIR + storeId + "_maintenance_report_" + startDate + "_to_" + endDate + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Maintenance Report: " + startDate + " to " + endDate + "\n");
            writer.write("----------------------------------------\n\n");
            
            writer.write("Cleaning Tasks:\n");
            for (CleaningTask task : cleaningTasks.values()) {
                if (isWithinDateRange(task.getScheduledTime(), startDate, endDate)) {
                    writer.write(String.format("%s - %s - %s - %s%n",
                        task.getTaskId(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getNotes() != null ? task.getNotes() : "No notes"));
                }
            }
            
            writer.write("\nMaintenance Tasks:\n");
            for (MaintenanceTask task : maintenanceTasks.values()) {
                if (isWithinDateRange(task.getReportedTime(), startDate, endDate)) {
                    writer.write(String.format("%s - %s - %s - Priority: %s - %s%n",
                        task.getTaskId(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getPriority(),
                        task.getNotes() != null ? task.getNotes() : "No notes"));
                }
            }
        } catch (IOException e) {
            System.err.println("Error generating maintenance report: " + e.getMessage());
        }
    }

    private boolean isWithinDateRange(LocalDateTime dateTime, LocalDate startDate, LocalDate endDate) {
        LocalDate date = dateTime.toLocalDate();
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public void displayPendingTasks() {
        System.out.println("\nPending Cleaning Tasks:");
        System.out.println("--------------------------------------------------");
        System.out.printf("%-10s %-30s %-15s %-10s%n", "ID", "Description", "Frequency", "Status");
        
        for (CleaningTask task : cleaningTasks.values()) {
            if (task.getStatus() != TaskStatus.COMPLETED) {
                System.out.printf("%-10s %-30s %-15s %-10s%n",
                    task.getTaskId(),
                    task.getDescription(),
                    task.getFrequency(),
                    task.getStatus());
            }
        }

        System.out.println("\nPending Maintenance Tasks:");
        System.out.println("--------------------------------------------------");
        System.out.printf("%-10s %-30s %-15s %-10s%n", "ID", "Description", "Priority", "Status");
        
        for (MaintenanceTask task : maintenanceTasks.values()) {
            if (task.getStatus() != TaskStatus.COMPLETED) {
                System.out.printf("%-10s %-30s %-15s %-10s%n",
                    task.getTaskId(),
                    task.getDescription(),
                    task.getPriority(),
                    task.getStatus());
            }
        }
    }

}
