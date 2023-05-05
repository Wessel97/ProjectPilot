package com.example.projectpilot.model;

public class Task {

    // Parameters
    private int taskID;
    private int userID;
    private String assigned_to;
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
    public Task(int taskID, int userID, String assigned_to, String title, String description, String note, int hours, boolean flag, String startDate, String endDate, String status, String department) {
        this.taskID = taskID;
        this.userID = userID;
        this.assigned_to = assigned_to;
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
    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getAssignedTo() {
        return assigned_to;
    }

    public void setAssignedTo(String assigned_to) {
        this.assigned_to = assigned_to;
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


