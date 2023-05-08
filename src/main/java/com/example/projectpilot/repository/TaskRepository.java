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

    /*--------------------------------------------------------------------
                                //Get metoder.
     ------------------------------------------------------------------*/


    //Method 1 get task from SQL. This method will return a task object from the database.
    private Task getTask(ResultSet resultSet) throws SQLException
    {
        int taskID = resultSet.getInt(1);
        int userID = resultSet.getInt(2);
        String title = resultSet.getString(3);
        String description = resultSet.getString(4);
        String note = resultSet.getString(5);
        int hours = resultSet.getInt(6);
        int payRate = resultSet.getInt(7);
        int totalPay = resultSet.getInt(8);
        boolean flag = resultSet.getBoolean(9);
        String startDate = resultSet.getString(10);
        String endDate = resultSet.getString(11);
        String status = resultSet.getString(12);
        String department = resultSet.getString(13);
        //create task object and return task object.
        return new Task(taskID, userID, title, description, note, hours, payRate, totalPay, flag, startDate, endDate, status, department);
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
            //Handle any errors while querying the database.
            System.out.println("Error trying to query database: " + e);
            //This method will print the error, what line it is on and what method it is in.
            e.printStackTrace();
        }
        return allTasksList;
    }

    // Method 3 get task by user ID. This method will return the tasks with the given ID in a list.
    public List<Task> getAllTasksByUserID(int userId)
    {
        // Initialize an empty list to store tasks with the given userID
        List<Task> userIdTasksList = new ArrayList<>();
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
                userIdTasksList.add(task);
                //print user. Debugging purposes to see list in terminal.
                System.out.println(task);
            }
        }
        catch (SQLException e)
        {
            //Handle any errors while querying the database.
            System.out.println("Error trying to query database: " + e);
            //This method will print the error, what line it is on and what method it is in.
            e.printStackTrace();
        }
        // Return the list of tasks with the given userID
        return userIdTasksList;
    }

    // Method 4 get task by task ID. This method will find the task with the given ID.
    public Task getTaskByTaskId(int taskId)
    {
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.task WHERE task_id = ?";
        // Make a boolean to check if the task was found (sentinel). Makes the code more readable.
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
            //Handle any errors while querying the database.
            System.out.println("Error trying to query database: " + e);
            //This method will print the error, what line it is on and what method it is in.
            e.printStackTrace();
        }
        return selectTask;
    }

    public List<Task> getAllTasksByDepartment(String department)
    {
        // Initialize an empty list to store tasks with the given userID
        List<Task> departmentTasksList = new ArrayList<>();
        // Define the SQL query to find all tasks with the given userID
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.task WHERE department = ?";
        try
        {
            // Establish a connection to the database
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            // Prepare a statement with the given FIND_QUERY
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY);
            // Set the userId parameter for the prepared statement
            preparedStatement.setString(1, department);
            // Execute the query and get the result set
            ResultSet resultSet = preparedStatement.executeQuery();

            // Loop through the result set
            while (resultSet.next())
            {
                // Extract the task from the result set
                Task task = getTask(resultSet);
                // Add the extracted task to the tasksByUserId list
                departmentTasksList.add(task);
                //print user. Debugging purposes to see list in terminal.
                System.out.println(task);
            }
        }
        catch (SQLException e)
        {
            //Handle any errors while querying the database.
            System.out.println("Error trying to query database: " + e);
            //This method will print the error, what line it is on and what method it is in.
            e.printStackTrace();
        }
        // Return the list of tasks with the given userID
        return departmentTasksList;
    }

     /*--------------------------------------------------------------------
                    //Add + update + delete metoder.
    --------------------------------------------------------------------*/

    //Method 5 add task. This method will add a task to the database.
    public boolean addTask(Task task)
    {
        //query to insert task
        final String INSERT_QUERY = "INSERT INTO ProjectPilotDB.task (title, description, hours, start_date, end_date, department) VALUES (?, ?, ?, ?, ?, ?, ?)";
        // Make a boolean to check if the task was added (sentinel). Makes the code more readable.
        boolean taskAdded = false;
        try
        {
            //db connection
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
            //set title
            preparedStatement.setString(1, task.getTitle());
            //set description
            preparedStatement.setString(2, task.getDescription());
            //set hours
            preparedStatement.setInt(3, task.getHours());
            //set start_date
            preparedStatement.setString(5, task.getStartDate());
            //set end_date
            preparedStatement.setString(6, task.getEndDate());
            //set department
            preparedStatement.setString(7, task.getDepartment());
            //execute SQL statement and get number of rows affected by query (should be 1) and store in rowsAffected.
            int rowsAffected = preparedStatement.executeUpdate();
            //return true if rowsAffected is 1, it will return false if rowsAffected is 0 or more than 1.

            if(rowsAffected == 1)
            {
                taskAdded = true;
            }
        }
        catch (SQLException e)
        {
            //Handle any errors while querying the database.
            System.out.println("Error trying to query database: " + e);
            //This method will print the error, what line it is on and what method it is in.
            e.printStackTrace();
        }
        //return false if task was not added or there was an error in the try block.
        return taskAdded;
    }

    // Method 6 update task. This method will update the selected task in the database. Without returning anything.
    public boolean updateTask(Task task)
    {
        //query to update user
        final String UPDATE_QUERY = "UPDATE ProjectPilotDB.task SET title = ?, description = ?, note = ?, hours = ?, flag = ?, start_date = ?, end_date = ?, status = ?, department = ? WHERE task_id = ?";
        // Make a boolean to check if the task was updated (sentinel). Makes the code more readable.
        boolean taskUpdated = false;
        try
        {
            // db connection
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            // prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            // set title parameter
            preparedStatement.setString(1, task.getTitle());
            // set description parameter
            preparedStatement.setString(2, task.getDescription());
            // set note parameter
            preparedStatement.setString(3, task.getNote());
            // set hours parameter
            preparedStatement.setInt(4, task.getHours());
            // set flag parameter
            preparedStatement.setBoolean(6, task.isFlag());
            // set start_date parameter
            preparedStatement.setString(7, task.getStartDate());
            // set end_date parameter
            preparedStatement.setString(8, task.getEndDate());
            // set status parameter
            preparedStatement.setString(9, task.getStatus());
            // set department parameter
            preparedStatement.setString(10, task.getDepartment());

            // execute statement
            int updatedRow = preparedStatement.executeUpdate();
            // if updatedRow is 1, task was updated.
            if(updatedRow == 1)
            {
                taskUpdated = true;
            }
        }
        catch (SQLException e)
        {
            //Handle any errors while querying the database.
            System.out.println("Error trying to query database: " + e);
            //This method will print the error, what line it is on and what method it is in.
            e.printStackTrace();
        }
        // return true if task was updated, false if not.
        return taskUpdated;
    }

    //Method 7 delete task by ID. This method will return true if the task was successfully deleted from the database.
    public boolean deleteTaskByID(Task task)
    {
        //query to delete user
        final String DELETE_QUERY = "DELETE FROM ProjectPilotDB.task WHERE task_id = ?";
        // Make a boolean to check if the task was deleted (sentinel). Makes the code more readable.
        boolean taskDeleted = false;
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
                taskDeleted = true;
            }
        }
        catch (SQLException e)
        {
            //Handle any errors while querying the database.
            System.out.println("Error trying to query database: " + e);
            //This method will print the error, what line it is on and what method it is in.
            e.printStackTrace();
        }
        //return false if task was not deleted
        return taskDeleted;
    }

    /*--------------------------------------------------------------------
                            // Sort Metoder
    --------------------------------------------------------------------*/

    //Method 8 Generic sorting method. You can define your sorting parameter.
    public List<Task> getTasksSorted(String sortingParameter)
    {
        List<Task> sortedTasksList = new ArrayList<>();
        //query to get all tasks
        final String SQL_QUERY = "SELECT * FROM ProjectPilotDB.task ORDER BY " + sortingParameter;
        try
        {
            //db connection
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            // create statement. This will be used to execute the query.
            Statement statement = connection.createStatement();
            // execute query and store result in resultSet.
            ResultSet resultSet = statement.executeQuery(SQL_QUERY);
            // loop through resultSet and add each task to sortedTasksList.
            while (resultSet.next())
            {
                //create task object and add to sortedTasksList.
                Task task = getTask(resultSet);
                sortedTasksList.add(task);
            }
        }
        catch (SQLException e)
        {
            //Handle any errors while querying the database.
            System.out.println("Error trying to query database: " + e);
            //This method will print the error, what line it is on and what method it is in.
            e.printStackTrace();
        }
        return sortedTasksList;
    }

    //Method 9 sort by hours. This method will sort the tasks by hours.
    public List<Task> getAllTasksSortedByHours()
    {
        return getTasksSorted("hours");
    }

    //Method 10 sort by department. This method will sort the tasks by department.
    public List<Task> getAllTasksSortedByDepartment()
    {
        return getTasksSorted("department");
    }

    //Method 11 sort by start date. This method will sort the tasks by start date.
    public List<Task> getAllTasksSortedByStartDate()
    {
        return getTasksSorted("start_date");
    }

    //Method 12 sort by end date. This method will sort the tasks by end date.
    public List<Task> getAllTasksSortedByEndDate()
    {
        return getTasksSorted("end_date");
    }

    //Method 13 sort by status. This method will sort the tasks by status (unassigned, assigned, in progress, done).
    public List<Task> getAllTasksSortedByStatus()
    {
        return getTasksSorted("start_date");
    }

    //sort by flag
    public List<Task> getAllTasksSortedByFlag()
    {
        return getTasksSorted("start_date");
    }

    /*--------------------------------------------------------------------
                            // Projekt kalkulering Metoder
    --------------------------------------------------------------------*/

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
