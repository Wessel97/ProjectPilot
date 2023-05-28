package com.example.projectpilot.repository;

import com.example.projectpilot.model.Task;
import com.example.projectpilot.service.DatabaseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TaskRepositoryTest {
    @Mock
    private DatabaseService databaseService;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;

    private TaskRepository taskRepository;


    @BeforeEach
    public void setup() throws SQLException {
        taskRepository = new TaskRepository(databaseService);
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    @Test
    public void testAddTask() throws SQLException {
        // Prepare test data
        Task task = new Task();
        // Set task properties
        task.setDepartment_id(1);
        task.setTitle("Task Title");
        task.setDescription("Task Description");
        task.setNote("Task Note");
        task.setHours(5);
        task.setStart_Date(java.sql.Date.valueOf("2021-01-01"));
        task.setEnd_Date(java.sql.Date.valueOf("2021-01-02"));
        task.setStatus("Task Status");
        task.setDepartment("Task Department");
        // Call the method
        taskRepository.addTask(task);

        // Verify the interactions with mocks
        verify(databaseService).getConnection();
        verify(connection).prepareStatement(eq("INSERT INTO task(department_id, title, description, note, hours, start_date, end_date, status, department) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"));
        verify(preparedStatement).setInt(eq(1), eq(task.getDepartment_id()));
        verify(preparedStatement).setString(eq(2), eq(task.getTitle()));
        verify(preparedStatement).setString(eq(3), eq(task.getDescription()));
        verify(preparedStatement).setString(eq(4), eq(task.getNote()));
        verify(preparedStatement).setInt(eq(5), eq(task.getHours()));
        verify(preparedStatement).setDate(eq(6), eq(task.getStart_Date()));
        verify(preparedStatement).setDate(eq(7), eq(task.getEnd_Date()));
        verify(preparedStatement).setString(eq(8), eq(task.getStatus()));
        verify(preparedStatement).setString(eq(9), eq(task.getDepartment()));
        verify(preparedStatement).executeUpdate();
    }
}