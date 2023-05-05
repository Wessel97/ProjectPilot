package com.example.projectpilot.repository;
import com.example.projectpilot.model.Task;
import com.example.projectpilot.model.User;
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

    //Method 1 extract user from SQL. This method will return a user object from the database.
    private Task extractTask(ResultSet resultSet) throws SQLException
    {
        int taskId = resultSet.getInt(1);

        int userId = resultSet.getInt(2);

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
        //create task object
        Task task = new Task(taskId, userId, assignedTo, title, description, note, hours, flag, startDate, endDate, status, department);
        //set task_id
        task.setTaskId(taskId);
        //return task
        return task;
    }

    //Method 2 get all users. This method will return a list of all users in the database.
    public List<Task> getAllTask()
    {
        //create list of users
        List<Task> allTaskList = new ArrayList<>();
        try
        {
            //db connection
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            //create statement
            Statement statement = connection.createStatement();
            //execute statement
            final String SQL_QUERY = "SELECT * FROM ProjectPilotDB.user";
            //get result set
            ResultSet resultSet = statement.executeQuery(SQL_QUERY);
            //loop through result set
            while (resultSet.next())
            {
                //extract user from result set
                Task task = extractTask(resultSet);
                //add user to list
                allTaskList.add(task);
                //print user
                System.out.println(task);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error querying database");
            e.printStackTrace();
        }
        return allTaskList;
    }
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
            //set first_name
            preparedStatement.setString(1, task.getAssignedTo());
            //set last_name
            preparedStatement.setString(2, task.getTitle());
            //set email
            preparedStatement.setString(3, task.getDescription());
            //set password
            preparedStatement.setInt(4, task.getHours());

            preparedStatement.setString(5, task.getStartDate());

            preparedStatement.setString(6, task.getEndDate());

            preparedStatement.setString(7, task.getDepartment());

            //execute SQL statement and get number of rows affected by query (should be 1) and store in rowsAffected
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
        //return false if user was not added
        return false;
    }

    // Method 6 update user. This method will update the selected user in the database. Without returning anything.
    public void updateTask(Task task)
    {
        //query to update user
        final String UPDATE_QUERY = "UPDATE ProjectPilotDB.task SET assigned_to = ?, title = ?, description = ?, hours = ?, start_date = ?, end_date = ?, department = ? WHERE task_id = ?";
        try
        {
            //db connection
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            //set parameters for prepared statement
            preparedStatement.setString(1, user.getFirstName());

            //execute statement
            preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
    }


    public void updateTask(Task task) {
        // SQL Query + Connection Try Catch
    }

    public void deleteTask(int taskId) {
        // SQL Query + Connection Try Catch
    }

    public Task findTaskById(int taskId) {
        Task task = new Task();
        // SQL Query + Connection Try Catch
        return task;
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
        gennesnitlig timeløn (payRate)
         */

        // int payRate = 300;
        // int totalHours = ???

        // int price = totalHours * payRate;

        // return price;
        return 0;
    }

}
