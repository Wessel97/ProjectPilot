package com.example.projectpilot.model;

public class Department {
    private int id;
    private int project_id;
    private String departmentName;

    public Department(){}

    public Department(int id, int project_id, String departmentName) {
        this.id = id;
        this.project_id = project_id;
        this.departmentName = departmentName;
    }

    public Department(int id, String departmentName) {
        this.id = id;
        this.departmentName = departmentName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String name) {
        this.departmentName = departmentName;
    }

    public int getProjectId() {
        return project_id;
    }

    public void setProjectId(int project_id) {
        this.project_id = project_id;
    }
}
