package com.example.projectpilot.repository;

import com.example.projectpilot.model.Task;
import com.example.projectpilot.service.DatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

import static org.mockito.Mockito.*;

public class TaskRepositoryTest {

    @Mock
    private DatabaseService databaseService;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    private TaskRepository taskRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        taskRepository = new TaskRepository(databaseService);
    }

    //  

    @Test
    public void testAddTask() throws SQLException {
        Task task = new Task();
        task.setDepartment_id(1);
        task.setTitle("Test Title");
        task.setDescription("Test Description");
        task.setNote("Test Note");
        task.setHours(5);
        task.setStart_Date(Date.valueOf("2021-05-05"));
        task.setEnd_Date(Date.valueOf("2021-05-05"));
        task.setStatus("Pending");
        task.setDepartment("Test Department");

        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);

        taskRepository.addTask(task);

        verify(databaseService, times(1)).getConnection();
        verify(connection, times(1)).prepareStatement(any(String.class));
        verify(preparedStatement, times(1)).executeUpdate();
    }

}