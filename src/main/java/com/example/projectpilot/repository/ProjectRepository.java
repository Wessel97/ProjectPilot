package com.example.projectpilot.repository;

import com.example.projectpilot.model.Project;
import com.example.projectpilot.service.DatabaseService;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectRepository
{
    private final DatabaseService databaseService;

    public ProjectRepository(DatabaseService databaseService)
    {
        this.databaseService = databaseService;
    }

    //Henter et projekt, bliver brugt i getAllProjects
    public Project getProject(ResultSet resultSet) throws SQLException
    {
        int projectID = resultSet.getInt(1);
        String projectName = resultSet.getString(2);
        return new Project(projectID, projectName);
    }

    //Hent alle projekter, med at bruge getProject metoden i et loop
    public List<Project> getAllProjects()
    {
        List<Project> projectList = new ArrayList<>();
        final String SQL_QUERY = "SELECT * FROM ProjectPilotDB.project";

        try (Connection connection = databaseService.getConnection();
             Statement statement = connection.createStatement())
        {
            ResultSet resultSet = statement.executeQuery(SQL_QUERY);
            while ( resultSet.next() )
            {
                Project project = getProject(resultSet);
                projectList.add(project);
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

    //Tilføj et projekt
    public boolean addProject(Project project)
    {
        final String INSERT_QUERY = "INSERT INTO ProjectPilotDB.project (name) VALUES (?)";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY))
        {
            preparedStatement.setString(1, project.getProjectName());
            int rowsAffected = preparedStatement.executeUpdate();
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
        return false;
    }

    //Hent et projekt med et specifikt id
    public Project getProjectByID(int projectID)
    {
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.project WHERE id = ?";

        Project selectedProject = null;
        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY))
        {
            preparedStatement.setInt(1, projectID);
            ResultSet resultSet = preparedStatement.executeQuery();
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
        return selectedProject;
    }

    // Opdater et projekt
    public void updateProject(Project project)
    {
        final String UPDATE_QUERY = "UPDATE ProjectPilotDB.project SET name = ? WHERE id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY))
        {
            preparedStatement.setString(1, project.getProjectName());
            preparedStatement.setInt(2, project.getId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
    }

    // Slet et projekt med et specifikt id
    public boolean deleteProjectById(int projectId)
    {
        final String DELETE_QUERY = "DELETE FROM ProjectPilotDB.project WHERE id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY))
        {
            preparedStatement.setInt(1, projectId);
            int foundProject = preparedStatement.executeUpdate();
            if ( foundProject == 1 )
            {
                return true;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
        return false;
    }

    //Check om et projekt eksisterer
    public boolean checkIfProjectExists(String checkName)
    {
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.project WHERE name = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY))
        {
            preparedStatement.setString(1, checkName);
            ResultSet resultSet = preparedStatement.executeQuery();
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
        return false;
    }
}
