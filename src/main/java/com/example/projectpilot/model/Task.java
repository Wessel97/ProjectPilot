package com.example.projectpilot.model;

public class Task {

    // Parameters
    private int task_id;
    private int user_id;
    private String title;
    private String description;
    private String note;
    private int hours;
    private int payRate;
    private boolean flag;
    private String start_Date;
    private String end_Date;
    private String status;
    private String department;

    // Constructor
    public Task(int taskID, int userID, String title, String description, String note, int hours, int payRate, boolean flag, String startDate, String endDate, String status, String department)
    {
        this.task_id = task_id;
        this.user_id = user_id;
        this.title = title;
        this.description = description;
        this.note = note;
        this.hours = hours;
        this.payRate = payRate;
        this.flag = flag;
        this.start_Date = start_Date;
        this.end_Date = end_Date;
        this.status = status;
        this.department = department;
    }

    // Empty Constructor
    public Task() {
    }

    // Getters and Setters
    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPayRate() {
        return payRate;
    }

    public void setPayRate(int payRate) {
        this.payRate = payRate;
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

    public String getStart_Date() {
        return start_Date;
    }

    public void setStart_Date(String start_Date) {
        this.start_Date = start_Date;
    }

    public String getEnd_Date() {
        return end_Date;
    }

    public void setEnd_Date(String end_Date) {
        this.end_Date = end_Date;
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


