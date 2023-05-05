package com.example.projectpilot.repository;
import com.example.projectpilot.model.Task;
import org.springframework.beans.factory.annotation.Value;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    @Value("${spring.datasource.url}") //jdbc:mysql://localhost:3306/ProjectPilotDB
    private String DB_URL;
    @Value("${spring.datasource.username}") //ProjectPilotDB
    private String UID;
    @Value("${spring.datasource.password}") //Bugbusters23
    private String PWD;


                                //Get metoder.


    //Method 1 get task from SQL. This method will return a task object from the database.
    private Task getTask(ResultSet resultSet) throws SQLException
    {
        int taskID = resultSet.getInt(1);
        int userID = resultSet.getInt(2);
        String assignedTo = resultSet.getString(3);
        String title = resultSet.getString(4);
        String description = resultSet.getString(5);
        String note = resultSet.getString(6);
        int hours = resultSet.getInt(7);
        boolean flag = resultSet.getBoolean(8);
        String startDate = resultSet.getString(9);
        String endDate = resultSet.getString(10);
        String status = resultSet.getString(11);
        String department = resultSet.getString(12);
        //create task object and return task object.
        return new Task(taskID, userID, assignedTo, title, description, note, hours, flag, startDate, endDate, status, department);
    }

    //Method 2 get all tasks. This method will return a list of all tasks in the database.
    public List<Task> getAllTasks()
    {
        //create list of tasks
        List<Task> allTasksList = new ArrayList<>();
        try
        {
            //db connection
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            //create statement
            Statement statement = connection.createStatement();
            //execute statement
            final String SQL_QUERY = "SELECT * FROM ProjectPilotDB.task";
            //get result set
            ResultSet resultSet = statement.executeQuery(SQL_QUERY);
            //loop through result set
            while (resultSet.next())
            {
                //extract user from result set
                Task task = getTask(resultSet);
                //add user to list
                allTasksList.add(task);
                //print user. Debugging purposes to see list.
                System.out.println(task);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error querying database");
            e.printStackTrace();
        }
        return allTasksList;
    }

    // Method 3 get task by user ID. This method will return the tasks with the given ID in a list.
    public List<Task> getAllTasksByUserID(int userId)
    {
        // Initialize an empty list to store tasks with the given userID
        List<Task> UserIdTasksList = new ArrayList<>();
        // Define the SQL query to find all tasks with the given userID
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.task WHERE user_id = ?";
        try
        {
            // Establish a connection to the database
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            // Prepare a statement with the given FIND_QUERY
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY);
            // Set the userId parameter for the prepared statement
            preparedStatement.setInt(1, userId);
            // Execute the query and get the result set
            ResultSet resultSet = preparedStatement.executeQuery();

            // Loop through the result set
            while (resultSet.next())
            {
                // Extract the task from the result set
                Task task = getTask(resultSet);
                // Add the extracted task to the tasksByUserId list
                UserIdTasksList.add(task);
                //print user. Debugging purposes to see list in terminal.
                System.out.println(task);
            }
        }
        catch (SQLException e)
        {
            // Handle any errors while querying the database
            System.out.println("Could not query database");
            e.printStackTrace();
        }
        // Return the list of tasks with the given userID
        return UserIdTasksList;
    }

    // Method 4 get task by task ID. This method will find the task with the given ID.
    public Task getTaskByTaskId(int taskId)
    {
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.task WHERE task_id = ?";
        Task selectTask = null;
        try
        {
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY);
            preparedStatement.setInt(1, taskId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                selectTask = getTask(resultSet);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
        return selectTask;
    }

    //Method 5 add task. This method will add a task to the database.
    public boolean addTask(Task task)
    {
        //query to insert task
        final String INSERT_QUERY = "INSERT INTO ProjectPilotDB.task (assigned_to, title, description, hours, start_date, end_date, department) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try
        {
            //db connection
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
            //set assigned_to
            preparedStatement.setString(1, task.getAssignedTo());
            //set title
            preparedStatement.setString(2, task.getTitle());
            //set description
            preparedStatement.setString(3, task.getDescription());
            //set hours
            preparedStatement.setInt(4, task.getHours());
            //set start_date
            preparedStatement.setString(5, task.getStartDate());
            //set end_date
            preparedStatement.setString(6, task.getEndDate());
            //set department
            preparedStatement.setString(7, task.getDepartment());
            //execute SQL statement and get number of rows affected by query (should be 1) and store in rowsAffected.
            int rowsAffected = preparedStatement.executeUpdate();
            //return true if rowsAffected is 1
            if(rowsAffected == 1)
            {
                return true;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
        //return false if task was not added
        return false;
    }

    // Method 6 update task. This method will update the selected task in the database. Without returning anything.
    public void updateTask(Task task)
    {
        //query to update user
        final String UPDATE_QUERY = "UPDATE ProjectPilotDB.task SET assigned_to = ?, title = ?, description = ?, note = ?, hours = ?, flag = ?, start_date = ?, end_date = ?, status = ?, department = ? WHERE task_id = ?";
        try
        {
            //db connection
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            //set parameters for prepared statement
            preparedStatement.setString(1, task.getAssignedTo());

            preparedStatement.setString(2, task.getTitle());

            preparedStatement.setString(3, task.getDescription());

            preparedStatement.setString(4, task.getNote());

            preparedStatement.setInt(5, task.getHours());

            preparedStatement.setBoolean(6, task.isFlag());

            preparedStatement.setString(7, task.getStartDate());

            preparedStatement.setString(8, task.getEndDate());

            preparedStatement.setString(9, task.getStatus());

            preparedStatement.setString(10, task.getDepartment());

            //execute statement
            preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
    }

    //Method 7 delete task by ID. This method will return true if the task was successfully deleted from the database.
    public boolean deleteTaskByID(Task task)
    {
        //query to delete user
        final String DELETE_QUERY = "DELETE FROM ProjectPilotDB.task WHERE task_id = ?";
        try
        {
            //db connection
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            //set parameters for prepared statement(task_id)
            preparedStatement.setInt(1, task.getTaskID());
            //execute statement
            int foundTask = preparedStatement.executeUpdate();
            //return true if task was found and deleted (foundTask should be 1).
            if(foundTask == 1)
            {
                return true;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
        //return false if task was not found and deleted
        return false;
    }

                            // Sort Metoder

    public void sortByHours() {
        /*
        Skal sortere tasks efter timer
         */
    }

    public void sortByDepartment() {
        /*
        Skal sortere tasks efter afdeling
         */
    }

    public void sortByStatus() {
        /*
        Skal sortere tasks efter status
         */
    }

    public void sortByFlag() {
        /*
        Skal sortere tasks efter flag
         */
    }


    public int timeOverview() {
        /*
        Skal vise resultatet af totale timer der er
        brugt og er til overs i tasks
         */

        // int totalHours = ???

        // return totalHours;
        return  0;
    }

    public int priceOverview() {
        /*
        Skal vise resultatet af totale timer der er
        brugt og er til overs i tasks, ganget med
        gennemsnitlig timeløn (payRate)
         */

        // int payRate = 300;
        // int totalHours = ???

        // int price = totalHours * payRate;

        // return price;
        return 0;
    }

}
