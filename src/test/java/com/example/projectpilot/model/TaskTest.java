package com.example.projectpilot.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {

    private Task task;

    // Her laves et nyt task objekt før hver test.
    @BeforeEach
    public void setUp() {
        task = new Task();
    }

    // Her testes getters og setters for task objektet.
    @Test
    public void testTaskSettersAndGetters() {
        int departmentId = 1;
        String title = "Test Title";
        String description = "Test Description";
        String note = "Test Note";
        int hours = 5;
        Date startDate = Date.valueOf(LocalDate.now());
        Date endDate = Date.valueOf(LocalDate.now());
        String status = "Test Status";
        String department = "Test Department";

        // Her sættes de forskellige værdier til task objektet.
        task.setDepartment_id(departmentId);
        task.setTitle(title);
        task.setDescription(description);
        task.setNote(note);
        task.setHours(hours);
        task.setStart_Date(startDate);
        task.setEnd_Date(endDate);
        task.setStatus(status);
        task.setDepartment(department);

        // Her bruger vi assertEquals til at sammenligne de forventede værdier med de faktiske værdier.
        assertEquals(departmentId, task.getDepartment_id());
        assertEquals(title, task.getTitle());
        assertEquals(description, task.getDescription());
        assertEquals(note, task.getNote());
        assertEquals(hours, task.getHours());
        assertEquals(startDate, task.getStart_Date());
        assertEquals(endDate, task.getEnd_Date());
        assertEquals(status, task.getStatus());
        assertEquals(department, task.getDepartment());
    }

}