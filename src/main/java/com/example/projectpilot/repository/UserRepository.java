package com.example.projectpilot.repository;

import com.example.projectpilot.model.User;
import org.springframework.beans.factory.annotation.Value;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository
{
    @Value("${spring.datasource.url}") //jdbc:mysql://localhost:3306/ProjectPilotDB
    private String DB_URL;
    @Value("${spring.datasource.username}") //ProjectPilotDB
    private String UID;
    @Value("${spring.datasource.password}") //ProjectPilot23
    private String PWD;

    public List<User> getAllUsers()
    {
        List<User> userList = new ArrayList<>();
        try
        {
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            Statement statement = connection.createStatement();
            final String SQL_QUERY = "SELECT * FROM ProjectPilotDB.user";
            ResultSet resultSet = statement.executeQuery(SQL_QUERY);
            while (resultSet.next())
            {
                int id = resultSet.getInt(1);
                String firstName = resultSet.getString(2);
                String lastName = resultSet.getString(3);
                String email = resultSet.getString(4);
                String pw = resultSet.getString(5);
                User user = new User(firstName, lastName, email, pw);
                user.setId(id);
                userList.add(user);
                System.out.println(user);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
        return userList;
    }
}
