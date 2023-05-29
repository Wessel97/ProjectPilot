package com.example.projectpilot.repository;

import com.example.projectpilot.model.Task;
import com.example.projectpilot.service.DatabaseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.List;

import static org.mockito.Mockito.*;

public class TaskRepositoryTest {

    @Mock
    private DatabaseService databaseService;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private TaskRepository taskRepository;

    // Denne test tester at metoden "addTask" i TaskRepository interagere
    // med sine dependencies som forventet når den kaldes.
    // Dens dependencies er DatabaseService, Connection og PreparedStatement.

    // Vi bruger MockitoAnnotations.openMocks(this) til at initialisere alle @Mock objekter.
    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        taskRepository = new TaskRepository(databaseService);

        // Set up mock behavior for databaseService and connection
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);

        when(preparedStatement.executeQuery()).thenReturn(resultSet);
    }

    // I @Test verifyer vi at getConnection() og prepareStatement() og executeUpdate() bliver kaldt 1 gang.
    @Test
    public void testAddTask() throws SQLException
    {
        Task task = new Task();

        // Vi bruger when() til at definere hvad der skal returneres når metoderne bliver kaldt.
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);

        // Her kalder vi metoden addTask() fra TaskRepository.
        taskRepository.addTask(task);

        // Vi bruger verify() til at verificere at metoderne bliver kaldt.
        // Vi bruger any(String.class) til at matche alle String objekter.
        // Vi bruger times(1) til at verificere at metoderne bliver kaldt 1 gang når addTask() bliver kaldt.
        verify(databaseService, times(1)).getConnection();
        verify(connection, times(1)).prepareStatement(any(String.class));
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testGetAllTasksByUserID() throws SQLException {
        // Set the user ID and sorting parameter for testing
        int userId = 1;
        String sortingParameter = "task_name";

        // Set up mock behavior for resultSet
        when(resultSet.next()).thenReturn(true, false); // Simulate a single row in the result set
        when(resultSet.getInt("user_id")).thenReturn(userId);

        // Call the getAllTasksByUserID method and get the list of tasks
        List<Task> tasks = taskRepository.getAllTasksByUserID(userId, sortingParameter);

        // Perform assertions to validate the results
        Assertions.assertNotNull(tasks, "Returned tasks list should not be null");
        Assertions.assertFalse(tasks.isEmpty(), "Returned tasks list should not be empty");

        // Additional assertions based on your requirements
        for (Task task : tasks) {
            Assertions.assertEquals(userId, task.getUser_id(), "Task has a different user ID");
        }

        // Verify that the methods were called
        verify(databaseService, times(1)).getConnection();
        verify(connection, times(1)).prepareStatement(any(String.class));
        verify(preparedStatement, times(1)).setInt(1, userId);
        verify(preparedStatement, times(1)).executeQuery();
        verify(resultSet, times(1)).next();
        verify(resultSet, times(1)).getInt("user_id");
    }

    @Test
    public void testGetAllTasksByUserID_NegativeCase() throws SQLException {
        // Set the user ID for testing
        int userId = 100; // Assuming a non-existing user ID
        String sortingParameter = "task_name";

        // Set up mock behavior for an empty resultSet
        when(resultSet.next()).thenReturn(false);

        // Call the getAllTasksByUserID method and get the list of tasks
        List<Task> tasks = taskRepository.getAllTasksByUserID(userId, sortingParameter);

        // Perform assertions to validate the results
        Assertions.assertNotNull(tasks, "Returned tasks list should not be null");
        Assertions.assertTrue(tasks.isEmpty(), "Returned tasks list should be null");
    }
}