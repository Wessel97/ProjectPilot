package com.example.projectpilot.repository;

import com.example.projectpilot.model.Department;
import com.example.projectpilot.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DepartmentRepository
{
    private final DatabaseService databaseService;

    @Autowired
    public DepartmentRepository(DatabaseService databaseService)
    {
        this.databaseService = databaseService;
    }

    public Department getDepartment(ResultSet resultSet) throws SQLException
    {
        //get department_id from result set
        int department_id = resultSet.getInt(1);
        //get department_project_id from result set
        int department_project_id = resultSet.getInt(2);
        //get first_name from result set
        String departmentName = resultSet.getString(3);

        //create user object
        return new Department(department_id, department_project_id, departmentName);
    }

    public List<Department> getAllDepartments()
    {
        //create list of departments
        List<Department> departmentList = new ArrayList<>();
        //execute statement, here there is no exceptions that need to be caught. It does need to be in try/catch.
        // Limit the scope of a try block to only the code that might throw an exception.
        final String SQL_QUERY = "SELECT * FROM ProjectPilotDB.department";

        try (Connection connection = databaseService.getConnection();
             Statement statement = connection.createStatement())
        {
            //get result set
            ResultSet resultSet = statement.executeQuery(SQL_QUERY);
            //loop through result set
            while ( resultSet.next() )
            {
                //get user from result set
                Department department = getDepartment(resultSet);
                //add user to list
                departmentList.add(department);
                //print user
                System.out.println(department);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error querying database");
            e.printStackTrace();
        }
        return departmentList;

    }

    public boolean checkIfDepartmentExists(String checkName)
    {
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.department WHERE name = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY))
        {
            //set parameters
            preparedStatement.setString(1, checkName);
            //execute statement
            ResultSet resultSet = preparedStatement.executeQuery();
            // Check if there is a row in the resultSet with the specified email
            if ( resultSet.next() )
            {
                return true;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
        //return false if user does not exist
        return false;
    }

    public boolean addDepartment(Department department)
    {
        // Query to insert user
        final String INSERT_QUERY = "INSERT INTO ProjectPilotDB.department (project_id, name) VALUES (?,?)";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY))
        {
            // Set project_id
            preparedStatement.setInt(1, department.getProjectId());
            // Set first_name
            preparedStatement.setString(2, department.getDepartmentName());
            // Execute SQL statement and get number of rows affected by query (should be 1) and store in rowsAffected
            int rowsAffected = preparedStatement.executeUpdate();
            // Return true if rowsAffected is 1
            if ( rowsAffected == 1 )
            {
                return true;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
        // Return false if user was not added
        return false;

    }

    public Department getDepartmentById(int departmentId)
    {
        Department selectDepartment = null;
        //query to find user
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.department WHERE id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY))
        {
            //set parameters for prepared statement (user_id)
            preparedStatement.setInt(1, departmentId);
            //execute statement
            ResultSet resultSet = preparedStatement.executeQuery();
            //return user if user exists
            if ( resultSet.next() )
            {
                selectDepartment = getDepartment(resultSet);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
        //return null if user does not exist
        return selectDepartment;
    }

    public void updateDepartment(Department department)
    { //query to update user
        final String UPDATE_QUERY = "UPDATE ProjectPilotDB.department SET name = ? WHERE id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY))
        {
            //set parameters for prepared statement
            preparedStatement.setString(1, department.getDepartmentName());
            //set user_id
            preparedStatement.setInt(2, department.getId());
            //execute statement
            preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
    }

    public boolean deleteDepartmentById(int departmentId)
    {
        //query to delete user
        final String DELETE_QUERY = "DELETE FROM ProjectPilotDB.department WHERE id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY))
        {
            //set parameters for prepared statement(user_id)
            preparedStatement.setInt(1, departmentId);
            //execute statement
            int foundDepartment = preparedStatement.executeUpdate();
            //return true if user was found and deleted (foundUser should be 1).
            if ( foundDepartment == 1 )
            {
                return true;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
        //return false if department was not found and deleted
        return false;
    }


    public List<Department> getAllDepartmentsByProjectId(int projectId)
    {
        // Initialize an empty list to store tasks with the given userID
        List<Department> departmentsByProjectList = new ArrayList<>();
        // Define the SQL query to find all tasks with the given userID
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.department WHERE project_id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY))
        {
            // Set the userId parameter for the prepared statement
            preparedStatement.setInt(1, projectId);
            // Execute the query and get the result set
            ResultSet resultSet = preparedStatement.executeQuery();

            // Loop through the result set
            while ( resultSet.next() )
            {
                // Extract the task from the result set
                Department department = getDepartment(resultSet);
                // Add the extracted task to the tasksByUserId list
                departmentsByProjectList.add(department);
                //print user. Debugging purposes to see list in terminal.
                System.out.println(department);
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
        return departmentsByProjectList;
    }

}
