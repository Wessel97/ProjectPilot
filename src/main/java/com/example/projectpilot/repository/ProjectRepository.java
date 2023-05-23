package com.example.projectpilot.repository;

import com.example.projectpilot.model.Project;
import com.example.projectpilot.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Repository
public class ProjectRepository
{
    private final DatabaseService databaseService;

    @Autowired
    public ProjectRepository(DatabaseService databaseService)
    {
        this.databaseService = databaseService;
    }

    public Project getProject(ResultSet resultSet) throws SQLException
    {
        //get department_id from result set
        int projectID = resultSet.getInt(1);
        //get first_name from result set
        String projectName = resultSet.getString(2);
        //create user object
        return new Project(projectID, projectName);
    }

    public List<Project> getAllProjects()
    {
        //create list of users
        List<Project> projectList = new ArrayList<>();
        //execute statement, here there is no exceptions that need to be caught. It does need to be in try/catch.
        // Limit the scope of a try block to only the code that might throw an exception.
        final String SQL_QUERY = "SELECT * FROM ProjectPilotDB.project";
        try
        {
            //db connection
            Connection connection = databaseService.getConnection();
            //create statement
            Statement statement = connection.createStatement();
            //get result set
            ResultSet resultSet = statement.executeQuery(SQL_QUERY);
            //loop through result set
            while ( resultSet.next() )
            {
                //get user from result set
                Project project = getProject(resultSet);
                //add user to list
                projectList.add(project);
                //print user
                System.out.println(project);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error querying database");
            e.printStackTrace();
        }
        return projectList;
    }

    public boolean checkIfProjectExists(String checkName)
    {
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.project WHERE name = ?";
        try
        {
            //db connection
            Connection connection = databaseService.getConnection();
            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY);
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

    public boolean addProject(Project project)
    {
        // Query to insert user
        final String INSERT_QUERY = "INSERT INTO ProjectPilotDB.project (name) VALUES (?)";
        try
        {
            // DB connection
            Connection connection = databaseService.getConnection();
            // Prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
            // Set first_name
            preparedStatement.setString(1, project.getProjectName());
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

    public Project getProjectByID(int projectID)
    {
        //query to find user
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.project WHERE id = ?";
        Project selectedProject = null;
        try
        {
            //db connection
            Connection connection = databaseService.getConnection();
            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY);
            //set parameters for prepared statement (user_id)
            preparedStatement.setInt(1, projectID);
            //execute statement
            ResultSet resultSet = preparedStatement.executeQuery();
            //return user if user exists
            if ( resultSet.next() )
            {
                selectedProject = getProject(resultSet);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
        //return null if user does not exist
        return selectedProject;
    }

    public void updateProject(Project project)
    { //query to update user
        final String UPDATE_QUERY = "UPDATE ProjectPilotDB.project SET name = ? WHERE id = ?";
        try
        {
            //db connection
            Connection connection = databaseService.getConnection();
            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            //set parameters for prepared statement
            preparedStatement.setString(1, project.getProjectName());
            //set user_id
            preparedStatement.setInt(2, project.getId());
            //execute statement
            preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
    }

    public boolean deleteProjectById(int projectId){
        //query to delete project
        final String DELETE_QUERY = "DELETE FROM ProjectPilotDB.project WHERE id = ?";
        try{
            //db connection
            Connection connection = databaseService.getConnection();
            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            //set parameters for prepared statement (project_id)
            preparedStatement.setInt(1, projectId);
            //execute statement
            int foundProject = preparedStatement.executeUpdate();
            //return true if project was found and deleted (foundProject should be 1).
            if ( foundProject == 1 ){
                return true;
            }
        }
        catch (SQLException e){
            System.out.println("Could not query database");
            e.printStackTrace();
        }
        //return false if project was not found and deleted
        return false;
    }
}
