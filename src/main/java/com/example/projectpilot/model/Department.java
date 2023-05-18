package com.example.projectpilot.model;

public class Department {
    private int department_id;

    private String departmentName;

    public Department(){

    }

    public Department(int department_id, String departmentName) {
        this.department_id = department_id;
        this.departmentName = departmentName;
    }

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String name) {
        this.departmentName = departmentName;
    }
}
