package com.example.projectpilot.repository;

import com.example.projectpilot.model.Task;
import com.example.projectpilot.service.DatabaseService;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepository
{

    //Lav et database service objekt
    private final DatabaseService databaseService;

    //Lav en constructor som tager imod database service objektet
    public TaskRepository(DatabaseService databaseService)
    {
        this.databaseService = databaseService;
    }

    // Denne metode vil returnere et task objekt fra databasen.
    private Task getTask(ResultSet resultSet) throws SQLException
    {
        int id = resultSet.getInt(1);
        int user_id = resultSet.getInt(2);
        int department_id = resultSet.getInt(3);
        String title = resultSet.getString(4);
        String description = resultSet.getString(5);
        String note = resultSet.getString(6);
        int hours = resultSet.getInt(7);
        int pay_rate = resultSet.getInt(8);
        boolean flag = resultSet.getBoolean(9);
        Date start_date = resultSet.getDate(10);
        Date end_date = resultSet.getDate(11);
        String status = resultSet.getString(12);
        String department = resultSet.getString(13);
        String project = resultSet.getString(14);
        //Returner et task objekt med de ovenstående parametre
        return new Task(id, user_id, department_id, title, description, note, hours, pay_rate, flag, start_date, end_date, status, department, project);
    }


    // Denne metode vil returnere alle tasks i en liste med projectId.
    public List<Task> getAllTasksByProjectId(int projectId, String sortingParameter)
    {
        // Intialiser en tom liste til at gemme alle tasks i
        List<Task> allTasksList = new ArrayList<>();
        // Definer SQL query til at finde alle tasks med det givne projectId
        String FIND_QUERY = "SELECT * FROM ProjectPilotDB.task JOIN ProjectPilotDB.department ON task.department_id = department.id WHERE department.project_id = ?";

        if ( sortingParameter != null)
        {
            FIND_QUERY += " ORDER BY  task." + sortingParameter;
        }

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY))
        {
            preparedStatement.setInt(1, projectId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while ( resultSet.next() )
            {
                Task task = getTask(resultSet);
                allTasksList.add(task);
                System.out.println(task);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error trying to query database: " + e);
            e.printStackTrace();
        }
        return allTasksList;
    }

    // Denne metode vil returnere alle tasks i en liste med userId og en sortingParameter.
    //SortingParameteren defineres i html filen, så når du klikker på værdien sættes den i metoden.
    public List<Task> getAllTasksByUserID(int userId, String sortingParameter)
    {
        List<Task> userIdTasksList = new ArrayList<>();
        String FIND_QUERY = "SELECT * FROM ProjectPilotDB.task WHERE user_id = ?";
        //Hvis sortingParameter ikke er null, så tilføj til query
        if ( sortingParameter != null)
        {
            FIND_QUERY += " ORDER BY  task." + sortingParameter;
        }

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY))
        {
            // Sæt userId parameteret for prepared statement
            preparedStatement.setInt(1, userId);
            // Eksekver query og få result set
            ResultSet resultSet = preparedStatement.executeQuery();

            // Loop igennem result set
            while ( resultSet.next() )
            {
                // Tag task ud fra result set
                Task task = getTask(resultSet);
                // Tilføj task til tasksByUserId listen
                userIdTasksList.add(task);
                //Print user. Til Debugging
                System.out.println(task);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error trying to query database: " + e);
            e.printStackTrace();
        }
        return userIdTasksList;
    }

    // Den her metode returnerer alle task i en list med departmentId
    public List<Task> getAllTasksByDepartmentID(int departmentId)
    {
        // Intialiser en tom liste til at gemme alle tasks i
        List<Task> userIdTasksList = new ArrayList<>();
        // Definer SQL query til at finde alle tasks med det givne departmentId
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.task WHERE department_id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY))
        {
            // Sæt departmentId parameteret for prepared statement
            preparedStatement.setInt(1, departmentId);
            // Eksekver query og få result set
            ResultSet resultSet = preparedStatement.executeQuery();

            // Loop igennem result set
            while ( resultSet.next() )
            {
                Task task = getTask(resultSet);
                userIdTasksList.add(task);
                System.out.println(task);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error trying to query database: " + e);
            // Denne metode vil printe en stack trace, hvis der er en fejl i SQL
            e.printStackTrace();
        }
        return userIdTasksList;
    }


    //Get Task by task id. Denne metode vil returnere en task med et givent taskId.
    //Bliver brugt når man skal update, delete eller assign
    public Task getTaskByTaskId(int taskId)
    {
        Task selectTask = null;
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.task WHERE id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY))
        {
            preparedStatement.setInt(1, taskId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if ( resultSet.next() )
            {
                selectTask = getTask(resultSet);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error trying to query database: " + e);
            e.printStackTrace();
        }
        return selectTask;
    }

    //Denne metode vil tilføje en task til databasen.
    public void addTask(Task task)
    {
        final String CREATE_QUERY = "INSERT INTO task(department_id, title, description, note, hours, start_date, end_date, status, department) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_QUERY))
        {
            preparedStatement.setInt(1, task.getDepartment_id());
            preparedStatement.setString(2, task.getTitle());
            preparedStatement.setString(3, task.getDescription());
            preparedStatement.setString(4, task.getNote());
            preparedStatement.setInt(5, task.getHours());
            preparedStatement.setDate(6, task.getStart_Date());
            preparedStatement.setDate(7, task.getEnd_Date());
            preparedStatement.setString(8, task.getStatus());
            preparedStatement.setString(9, task.getDepartment());

            preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Error trying to query database: " + e);
            e.printStackTrace();
        }
    }

    //Denne metode vil opdatere en task i databasen. Uden at returnerer noget
    public void updateTask(Task task)
    {
        final String UPDATE_QUERY = "UPDATE task SET title = ?, description = ?, note = ?, hours = ?, pay_rate = ?, flag = ?, start_date = ?, end_date = ?, status = ? WHERE id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY))
        {
            String title = task.getTitle();
            String description = task.getDescription();
            String note = task.getNote();
            int hours = task.getHours();
            int payRate = task.getPayRate();
            boolean flag = task.isFlag();
            Date start_date = task.getStart_Date();
            Date end_date = task.getEnd_Date();
            String status = task.getStatus();
            int task_id = task.getId();

            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, note);
            preparedStatement.setInt(4, hours);
            preparedStatement.setInt(5, payRate);
            preparedStatement.setBoolean(6, flag);
            preparedStatement.setDate(7, start_date);
            preparedStatement.setDate(8, end_date);
            preparedStatement.setString(9, status);
            preparedStatement.setInt(10, task_id);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Error trying to query database: " + e);
            e.printStackTrace();
        }
    }

    /*
    Metoden sletter en task fra databasen ud fra det ID den får, den returnere en boolean, der er
    "true" hvis en task var slettes succesfuldt.
    */
    public boolean deleteTaskByID(int taskId)
    {
        boolean taskDeleted = false;
        final String DELETE_QUERY = "DELETE FROM ProjectPilotDB.task WHERE id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY))
        {
            preparedStatement.setInt(1, taskId);
            int foundTask = preparedStatement.executeUpdate();
            if ( foundTask == 1 )
            {
                taskDeleted = true;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error trying to query database: " + e);
            e.printStackTrace();
        }
        return taskDeleted;
    }

    // Metoden tildeler en valgt task til en valgt User
    public void assignTo(Task task, int userID)
    {
        final String UPDATE_QUERY = "UPDATE task SET user_id = ? WHERE id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY))
        {
            int task_id = task.getId();

            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, task_id);

            preparedStatement.executeUpdate();
        } catch (SQLException e)
        {
            System.out.println("Error trying to query the database: " + e);
            e.printStackTrace();
        }
    }


    // Metoden kalkulerer det totale timeantal for tasks i det givne department.
    public int totalHoursByDepartment(int id)
    {
        int sumHoursByDept = 0;
        String CALCULATE_QUERY = "SELECT SUM(hours) FROM ProjectPilotDB.task WHERE department_id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(CALCULATE_QUERY))
        {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if ( resultSet.next() )
            {
                sumHoursByDept = resultSet.getInt(1);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return sumHoursByDept;
    }

    // Metoden kalkulerer den totale pris for tasks i det givne department.
    public int totalPriceByDepartment(int id)
    {
        int sumPriceByDept = 0;
        String CALCULATE_QUERY = "SELECT SUM(hours * pay_rate) FROM ProjectPilotDB.task WHERE department_id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(CALCULATE_QUERY))
        {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if ( resultSet.next() )
            {
                sumPriceByDept = resultSet.getInt(1);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return sumPriceByDept;
    }
}
