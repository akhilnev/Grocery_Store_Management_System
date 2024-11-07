package employee.model;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Employee {
    private String employeeId;
    private String name;
    private double hourlyRate;
    private List<TimeRecord> timeRecords;
    private boolean clockedIn;

    public Employee(String employeeId, String name, double hourlyRate) {
        this.employeeId = employeeId;
        this.name = name;
        this.hourlyRate = hourlyRate;
        this.timeRecords = new ArrayList<>();
        this.clockedIn = false;
    }

    public String getEmployeeId() { return employeeId; }
    public String getName() { return name; }
    public double getHourlyRate() { return hourlyRate; }
    public List<TimeRecord> getTimeRecords() { return timeRecords; }
    public boolean isClockIn() { return clockedIn; }
    
    public void setClockIn(boolean clockedIn) { this.clockedIn = clockedIn; }
    
    public void addTimeRecord(TimeRecord record) {
        timeRecords.add(record);
    }

    public void clockIn() {
        if (!clockedIn) {
            TimeRecord record = new TimeRecord(LocalDateTime.now());
            timeRecords.add(record);
            clockedIn = true;
        }
    }

    public void clockOut() {
        if (clockedIn && !timeRecords.isEmpty()) {
            TimeRecord currentRecord = timeRecords.get(timeRecords.size() - 1);
            currentRecord.setEndTime(LocalDateTime.now());
            clockedIn = false;
        }
    }
}