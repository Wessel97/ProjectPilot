package com.example.projectpilot.repository;

import com.example.projectpilot.model.Task;
import com.example.projectpilot.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepository
{

    private final DatabaseService databaseService;

    @Autowired
    public TaskRepository(DatabaseService databaseService)
    {
        this.databaseService = databaseService;
    }



    /*--------------------------------------------------------------------
                        //Get metoder (metode 1-5)
     ------------------------------------------------------------------*/

    //Method 1 get task from SQL. This method will return a task object from the database.
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
        //create task object and return task object.
        return new Task(id, user_id, department_id, title, description, note, hours, pay_rate, flag, start_date, end_date, status, department, project);
    }

    //Method 2 get all tasks. This method will return a list of all tasks in the database.
    public List<Task> getAllTasks()
    {
        //create list of tasks
        List<Task> allTasksList = new ArrayList<>();
        try (Connection connection = databaseService.getConnection();
             Statement statement = connection.createStatement())
        {
            //execute statement
            final String SQL_QUERY = "SELECT * FROM ProjectPilotDB.task";
            //get result set
            ResultSet resultSet = statement.executeQuery(SQL_QUERY);
            //loop through result set
            while ( resultSet.next() )
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

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY))
        {
            // Set the userId parameter for the prepared statement
            preparedStatement.setInt(1, userId);
            // Execute the query and get the result set
            ResultSet resultSet = preparedStatement.executeQuery();

            // Loop through the result set
            while ( resultSet.next() )
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

    public List<Task> getAllTasksByDepartmentID(int departmentId)
    {
        // Initialize an empty list to store tasks with the given userID
        List<Task> userIdTasksList = new ArrayList<>();
        // Define the SQL query to find all tasks with the given userID
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.task WHERE department_id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY))
        {
            // Set the userId parameter for the prepared statement
            preparedStatement.setInt(1, departmentId);
            // Execute the query and get the result set
            ResultSet resultSet = preparedStatement.executeQuery();

            // Loop through the result set
            while ( resultSet.next() )
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
            //Handle any errors while querying the database.
            System.out.println("Error trying to query database: " + e);
            //This method will print the error, what line it is on and what method it is in.
            e.printStackTrace();
        }
        return selectTask;
    }

    // Method 5 get task by department. This method will return a list of all tasks in the given department.
    public List<Task> getAllTasksByDepartment(String department)
    {
        // Initialize an empty list to store tasks with the given userID
        List<Task> departmentTasksList = new ArrayList<>();
        // Define the SQL query to find all tasks with the given userID
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.task WHERE department = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY))
        {
            // Set the userId parameter for the prepared statement
            preparedStatement.setString(1, department);
            // Execute the query and get the result set
            ResultSet resultSet = preparedStatement.executeQuery();

            // Loop through the result set
            while ( resultSet.next() )
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
                  //Add + update + delete metoder (metode 6-8)
    --------------------------------------------------------------------*/

    //Method 6 add task. This method will add a task to the database.
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
            //Handle any errors while querying the database.
            System.out.println("Error trying to query database: " + e);
            //This method will print the error, what line it is on and what method it is in.
            e.printStackTrace();
        }
    }

    // Method 7 update task. This method will update the selected task in the database. Without returning anything.
    public void updateTask(Task task)
    {
        final String UPDATE_QUERY = "UPDATE task SET title = ?, description = ?, note = ?, hours = ?, pay_rate = ?, flag = ?, start_date = ?, end_date = ?, status = ?, department = ? WHERE id = ?";

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
            String department = task.getDepartment();
            int task_id = task.getId();

            preparedStatement.setString(1, title);
            // set description parameter
            preparedStatement.setString(2, description);
            // set note parameter
            preparedStatement.setString(3, note);
            // set hours parameter
            preparedStatement.setInt(4, hours);
            // set pay_rate parameter
            preparedStatement.setInt(5, payRate);
            // set flag parameter
            preparedStatement.setBoolean(6, flag);
            // set start_date parameter
            preparedStatement.setDate(7, start_date);
            // set end_date parameter
            preparedStatement.setDate(8, end_date);
            // set status parameter
            preparedStatement.setString(9, status);
            // set department parameter
            preparedStatement.setString(10, department);
            // set task_id parameter
            preparedStatement.setInt(11, task_id);

            preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            //Handle any errors while querying the database.
            System.out.println("Error trying to query database: " + e);
            //This method will print the error, what line it is on and what method it is in.
            e.printStackTrace();
        }
    }

    //Method 8 delete task by ID. This method will return true if the task was successfully deleted from the database.
    public boolean deleteTaskByID(int taskId)
    {
        // Make a boolean to check if the task was deleted (sentinel). Makes the code more readable.
        boolean taskDeleted = false;
        //query to delete user
        final String DELETE_QUERY = "DELETE FROM ProjectPilotDB.task WHERE id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY))
        {
            //set parameters for prepared statement(task_id)
            preparedStatement.setInt(1, taskId);
            //execute statement
            int foundTask = preparedStatement.executeUpdate();
            //return true if task was found and deleted (foundTask should be 1).
            if ( foundTask == 1 )
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
        }
        catch (SQLException e)
        {
            System.out.println("Error trying to query the database: " + e);
            e.printStackTrace();
        }
    }

    /*--------------------------------------------------------------------
                            // Sort Metoder (Metode 9-15)
    --------------------------------------------------------------------*/

    //Method 9 Generic sorting method. You can define your sorting parameter.
    public List<Task> getTasksSorted(String sortingParameter)
    {
        List<Task> sortedTasksList = new ArrayList<>();
        //query to get all tasks
        final String SQL_QUERY = "SELECT * FROM ProjectPilotDB.task ORDER BY " + sortingParameter;

        try (Connection connection = databaseService.getConnection();
             Statement statement = connection.createStatement())
        {
            // execute query and store result in resultSet.
            ResultSet resultSet = statement.executeQuery(SQL_QUERY);
            // loop through resultSet and add each task to sortedTasksList.
            while ( resultSet.next() )
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

    //Method 10 sort by hours. This method will sort the tasks by hours.
    public List<Task> getAllTasksSortedByHours()
    {
        return getTasksSorted("hours");
    }

    //Method 11 sort by department. This method will sort the tasks by department.
    public List<Task> getAllTasksSortedByDepartment()
    {
        return getTasksSorted("department");
    }

    //Method 12 sort by start date. This method will sort the tasks by start date.
    public List<Task> getAllTasksSortedByStartDate()
    {
        return getTasksSorted("start_date");
    }

    //Method 13 sort by end date. This method will sort the tasks by end date.
    public List<Task> getAllTasksSortedByEndDate()
    {
        return getTasksSorted("end_date");
    }

    //Method 14 sort by status. This method will sort the tasks by status (unassigned, assigned, in progress, done).
    public List<Task> getAllTasksSortedByStatus()
    {
        return getTasksSorted("status");
    }

    /*
    //Method 15 sort by flag. This method will sort the tasks by flag (true or false).
    public List<Task> getAllTasksSortedByFlag()
    {
        return getTasksSorted("flag");
    }
    */

    /*--------------------------------------------------------------------
                 // Projekt kalkulering Metoder (Metode 16-21)
    --------------------------------------------------------------------*/

    // Method 16 calculates the total number of hours for all tasks.
    public int totalHours()
    {
        int sumHours = 0;
        String CALCULATE_QUERY = "SELECT SUM(hours) FROM ProjectPilotDB.task";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(CALCULATE_QUERY))
        {
            ResultSet resultSet = statement.executeQuery();
            if ( resultSet.next() )
            {
                sumHours = resultSet.getInt(7);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return sumHours;
    }

    // Method 17 calculates the total number of hours for a given department.
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

    // Method 18 calculates the total number of hours for a given user.
    public int totalHoursByID(int userID)
    {
        int sumHoursByID = 0;
        String CALCULATE_QUERY = "SELECT SUM(hours) FROM ProjectPilotDB.task WHERE user_id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(CALCULATE_QUERY))
        {
            statement.setInt(1, userID);
            ResultSet resultSet = statement.executeQuery();
            if ( resultSet.next() )
            {
                sumHoursByID = resultSet.getInt(1);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return sumHoursByID;
    }

    // Method 19 calculates the total price for all tasks.
    public int totalPrice()
    {
        int sumPrice = 0;
        String CALCULATE_QUERY = "SELECT SUM(hours * pay_rate) FROM ProjectPilotDB.task";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(CALCULATE_QUERY))
        {
            ResultSet resultSet = statement.executeQuery();
            if ( resultSet.next() )
            {
                sumPrice = resultSet.getInt(1);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return sumPrice;
    }

    // Method 20 calculates the total price for the tasks in a given department.
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

    // Method 21 calculates the total price for the tasks in a given user.
    public int totalPriceByID(int userID)
    {
        int sumPriceByID = 0;
        String CALCULATE_QUERY = "SELECT SUM(hours * pay_rate) FROM ProjectPilotDB.task WHERE user_id=?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(CALCULATE_QUERY))
        {
            statement.setInt(1, userID);
            ResultSet resultSet = statement.executeQuery();
            if ( resultSet.next() )
            {
                sumPriceByID = resultSet.getInt(1);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return sumPriceByID;
    }
}
