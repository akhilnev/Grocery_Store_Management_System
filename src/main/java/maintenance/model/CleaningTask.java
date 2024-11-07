package maintenance.model;

import java.time.LocalDateTime;

public class CleaningTask {
    private String taskId;
    private String description;
    private String area;
    private String assignedTo;
    private TaskStatus status;
    private TaskFrequency frequency;
    private LocalDateTime scheduledTime;
    private LocalDateTime completedTime;
    private String notes;

    public CleaningTask(String taskId, String description, String area, 
                       TaskFrequency frequency, LocalDateTime scheduledTime) {
        this.taskId = taskId;
        this.description = description;
        this.area = area;
        this.frequency = frequency;
        this.scheduledTime = scheduledTime;
        this.status = TaskStatus.PENDING;
    }

    // Getters and setters
    public String getTaskId() { return taskId; }
    public String getDescription() { return description; }
    public String getArea() { return area; }
    public String getAssignedTo() { return assignedTo; }
    public TaskStatus getStatus() { return status; }
    public TaskFrequency getFrequency() { return frequency; }
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public LocalDateTime getCompletedTime() { return completedTime; }
    public String getNotes() { return notes; }

    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public void setCompletedTime(LocalDateTime completedTime) { this.completedTime = completedTime; }
    public void setNotes(String notes) { this.notes = notes; }
}