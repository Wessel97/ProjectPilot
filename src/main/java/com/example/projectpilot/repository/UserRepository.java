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
    @Value("${spring.datasource.password}") //Bugbusters23
    private String PWD;

    //Method 1 extract user from SQL. This method will return a user object from the database.
    private User extractUser(ResultSet resultSet) throws SQLException {
        int user_id = resultSet.getInt(1);
        String first_name = resultSet.getString(2);
        String last_name = resultSet.getString(3);
        String email = resultSet.getString(4);
        String password = resultSet.getString(5);
        User user = new User(first_name, last_name, email, password);
        user.setID(user_id);
        return user;
    }

    //Method 2 get all users. This method will return a list of all users in the database.
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            Statement statement = connection.createStatement();
            final String SQL_QUERY = "SELECT * FROM ProjectPilotDB.user";
            ResultSet resultSet = statement.executeQuery(SQL_QUERY);
            while (resultSet.next()) {
                User user = extractUser(resultSet);
                userList.add(user);
                System.out.println(user);
            }
        } catch (SQLException e) {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
        return userList;
    }

    //Method 3 check if user exists. This method will return true if the user exists in the database.
    public boolean checkIfUserExists(String checkEmail) {
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.user WHERE email = ?";
        try {
            //db connection
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);

            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY);
            preparedStatement.setString(1, checkEmail);
            //execute statement
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                String DB_email = resultSet.getString(4);
                if(DB_email != null && DB_email.equals(checkEmail)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
        return false;
    }


    //Method 4 add user. This method will return true if the user was successfully added to the database.
    public boolean addUser(User user)
    {
        final String INSERT_QUERY = "INSERT INTO ProjectPilotDB.user (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";
        try
        {
            //db connection
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);

            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());

            //execute statement
            int rowsAffected = preparedStatement.executeUpdate();
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
        return false;
    }

    //Method 5 get user by ID. This method will return a user object if the user exists in the database.
    public User getUserByID(String user_id) {
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.user WHERE user_id = ?";
        try {
            //db connection
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);

            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY);
            preparedStatement.setString(1, user_id);
            //execute statement
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return extractUser(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
        return null;
    }

    // Method 6 update user. This method will return true if the user was successfully updated in the database.
    public void updateUser(User user)
    {
        final String UPDATE_QUERY = "UPDATE ProjectPilotDB.user SET first_name = ?, last_name = ?, email = ?, password = ? WHERE user_id = ?";
        try
        {
            //db connection
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);

            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setInt(5, user.getID());

            //execute statement
            preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
    }

    // Method 7 verify user. This method will return true if the user exists in the database.
    public boolean verifyUser(String email, String password) {
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.user WHERE email = ? AND password = ?";
        boolean userExists = false;
        try {
            // Establish a database connection
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);

            // Prepare the SQL query
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            // Execute the query and retrieve the result
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if a user with the given email and password exists
            if (resultSet.next()) {
                userExists = true;
            }
        } catch (SQLException e) {
            System.out.println("Could not query database");
            e.printStackTrace();
        }

        return userExists;
    }

    //Method 7 delete user by ID. This method will return true if the user was successfully deleted from the database.
    //Method 7 delete user by ID. This method will return true if the user was successfully deleted from the database.
    public boolean deleteUserByID(User user) {
        final String DELETE_QUERY = "DELETE FROM ProjectPilotDB.user WHERE user_id = ?";
        try {
            //db connection
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);

            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            preparedStatement.setInt(1, user.getID());

            //execute statement
            int foundUser = preparedStatement.executeUpdate();
            if(foundUser == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
        return false;
    }
}
