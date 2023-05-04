package com.example.projectpilot.model;

public class Task {

    // Parameters
    private int taskId;
    private int userId;
    private String assignedTo;
    private String title;
    private String description;
    private String note;
    private int hours;
    private boolean flag;
    private String startDate;
    private String endDate;
    private String status;
    private String department;

    // Constructor
    public Task(int taskId, int userId, String assignedTo, String title, String description, String note, int hours, boolean flag, String startDate, String endDate, String status, String department) {
        this.taskId = taskId;
        this.userId = userId;
        this.assignedTo = assignedTo;
        this.title = title;
        this.description = description;
        this.note = note;
        this.hours = hours;
        this.flag = flag;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.department = department;
    }

    // Empty Constructor
    public Task() {
    }

    // Getters and Setters
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

}


