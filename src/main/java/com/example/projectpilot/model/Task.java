package com.example.projectpilot.model;

public class Task {

    // Parameters
    private int task_id;
    private int user_id;
    private String title;
    private String description;
    private String note;
    private int hours;
    private int pay_rate;
    private boolean flag;
    private String start_date;
    private String end_date;
    private String status;
    private String department;

    // Constructor
    public Task(int task_id, int user_id, String title, String description, String note, int hours, int pay_rate, boolean flag, String start_date, String end_date, String status, String department)
    {
        this.task_id = task_id;
        this.user_id = user_id;
        this.title = title;
        this.description = description;
        this.note = note;
        this.hours = hours;
        this.pay_rate = pay_rate;
        this.flag = flag;
        this.start_date = start_date;
        this.end_date = end_date;
        this.status = status;
        this.department = department;
    }

    // Empty Constructor
    public Task() {
    }

    public Task(String title, String description, String note, int hour, boolean flag, String start_date, String end_date, String status, String department) {
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
        return pay_rate;
    }

    public void setPayRate(int payRate) {
        this.pay_rate = payRate;
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
        return start_date;
    }

    public void setStart_Date(String start_Date) {
        this.start_date = start_Date;
    }

    public String getEnd_Date() {
        return end_date;
    }

    public void setEnd_Date(String end_Date) {
        this.end_date = end_Date;
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


