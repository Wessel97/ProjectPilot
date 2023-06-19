package com.example.projectpilot.repository;

import com.example.projectpilot.model.Department;
import com.example.projectpilot.service.DatabaseService;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DepartmentRepository
{
    //Lav et database service objekt
    private final DatabaseService databaseService;

    //Lav en constructor der tager imod database service objektet

    public DepartmentRepository(DatabaseService databaseService)
    {
        this.databaseService = databaseService;
    }

    //Lav en metode der returnerer en liste af departments
    public Department getDepartment(ResultSet resultSet) throws SQLException
    {
        int department_id = resultSet.getInt(1);
        int department_project_id = resultSet.getInt(2);
        String departmentName = resultSet.getString(3);
        return new Department(department_id, department_project_id, departmentName);
    }

    //En metode som checker at et department eksisterer
    public boolean checkIfDepartmentExists(String checkName, int projectId) {
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.department WHERE name = ? AND project_id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY)) {
            preparedStatement.setString(1, checkName);
            preparedStatement.setInt(2, projectId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Department exists in the specified project ID
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Could not query the database");
            e.printStackTrace();
        }

        return false;
    }

    //Lav en metode der kan tilføje departments til databasen
    public boolean addDepartment(Department department)
    {
        final String INSERT_QUERY = "INSERT INTO ProjectPilotDB.department (project_id, name) VALUES (?,?)";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY))
        {
            preparedStatement.setInt(1, department.getProjectId());
            preparedStatement.setString(2, department.getDepartmentName());
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

    //Lav en metode der kan hente en department fra databasen med Id
    public Department getDepartmentById(int departmentId)
    {
        Department selectDepartment = null;

        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.department WHERE id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY))
        {
            preparedStatement.setInt(1, departmentId);
            ResultSet resultSet = preparedStatement.executeQuery();
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
        return selectDepartment;
    }

    public List<Department> getAllDepartmentsByProjectId(int projectId)
    {
        List<Department> departmentsByProjectList = new ArrayList<>();
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.department WHERE project_id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY))
        {
            preparedStatement.setInt(1, projectId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while ( resultSet.next() )
            {
                Department department = getDepartment(resultSet);
                departmentsByProjectList.add(department);
                System.out.println(department);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error trying to query database: " + e);
            e.printStackTrace();
        }
        return departmentsByProjectList;
    }

    public String getDepartmentNameById(int id)
    {
        String departmentName = null;
        final String FIND_QUERY = "SELECT name FROM ProjectPilotDB.department WHERE id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY))
        {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                departmentName = resultSet.getString("name");
            }
        } catch (SQLException e)
        {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
        return departmentName;
    }


   public void updateDepartment(Department department)
    {
        final String UPDATE_QUERY = "UPDATE ProjectPilotDB.department SET name = ? WHERE id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY))
        {
            preparedStatement.setString(1, department.getDepartmentName());
            preparedStatement.setInt(2, department.getId());
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
        final String DELETE_QUERY = "DELETE FROM ProjectPilotDB.department WHERE id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY))
        {
            preparedStatement.setInt(1, departmentId);
            int foundDepartment = preparedStatement.executeUpdate();
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
        return false;
    }

}
