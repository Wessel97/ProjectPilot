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
    private User extractUser(ResultSet resultSet) throws SQLException
    {
        //get user_id from result set
        int user_id = resultSet.getInt(1);
        //get first_name from result set
        String first_name = resultSet.getString(2);
        //get last_name from result set
        String last_name = resultSet.getString(3);
        //get email from result set
        String email = resultSet.getString(4);
        //get password from result set
        String password = resultSet.getString(5);
        //create user object
        User user = new User(first_name, last_name, email, password);
        //set user_id
        user.setID(user_id);
        //return user
        return user;
    }

    //Method 2 get all users. This method will return a list of all users in the database.
    public List<User> getAllUsers()
    {
        //create list of users
        List<User> userList = new ArrayList<>();
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
                User user = extractUser(resultSet);
                //add user to list
                userList.add(user);
                //print user
                System.out.println(user);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error querying database");
            e.printStackTrace();
        }
        return userList;
    }

    //Method 3 check if user exists. This method will return true if the user exists in the database.
    public boolean checkIfUserExists(String checkEmail)
    {
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.user WHERE email = ?";
        try
        {
            //db connection
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY);
            //set parameters
            preparedStatement.setString(1, checkEmail);
            //execute statement
            ResultSet resultSet = preparedStatement.executeQuery();
            // Check if there is a row in the resultSet with the specified email
            if (resultSet.next())
            {
                return true;
            }
        } catch (SQLException e)
        {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
        //return false if user does not exist
        return false;
    }



    //Method 4 add user. This method will return true if the user was successfully added to the database.
    public boolean addUser(User user)
    {
        //query to insert user
        final String INSERT_QUERY = "INSERT INTO ProjectPilotDB.user (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";
        try
        {
            //db connection
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
            //set first_name
            preparedStatement.setString(1, user.getFirstName());
            //set last_name
            preparedStatement.setString(2, user.getLastName());
            //set email
            preparedStatement.setString(3, user.getEmail());
            //set password
            preparedStatement.setString(4, user.getPassword());
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

    //Method 5 get user by ID. This method will return a user object if the user exists in the database.
    public User getUserByID(String user_id)
    {
        //query to find user
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.user WHERE user_id = ?";
        try
        {
            //db connection
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY);
            //set parameters for prepared statement (user_id)
            preparedStatement.setString(1, user_id);
            //execute statement
            ResultSet resultSet = preparedStatement.executeQuery();
            //return user if user exists
            if (resultSet.next())
            {
                return extractUser(resultSet);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
        //return null if user does not exist
        return null;
    }

    // Method 6 update user. This method will update the selected user in the database. Without returning anything.
    public void updateUser(User user)
    {
        //query to update user
        final String UPDATE_QUERY = "UPDATE ProjectPilotDB.user SET first_name = ?, last_name = ?, email = ?, password = ? WHERE user_id = ?";
        try
        {
            //db connection
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            //set parameters for prepared statement
            preparedStatement.setString(1, user.getFirstName());
            //set last_name
            preparedStatement.setString(2, user.getLastName());
            //set email
            preparedStatement.setString(3, user.getEmail());
            //set password
            preparedStatement.setString(4, user.getPassword());
            //set user_id
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
    public boolean verifyUser(String email, String password)
    {
        // Query to find user
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.user WHERE email = ? AND password = ?";
        //Make a sentinel to check user, assume the user does not exist
        boolean userExists = false;
        // Try to query the database
        try
        {
            // Establish a database connection
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            // Prepare the SQL query
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY);
            // Set the parameters for the query (email)
            preparedStatement.setString(1, email);
            // Set the parameters for the query (password)
            preparedStatement.setString(2, password);
            // Execute the query and retrieve the result
            ResultSet resultSet = preparedStatement.executeQuery();
            // Check if a user with the given email and password exists
            if (resultSet.next())
            {
                // If a user exists, set the sentinel to true
                userExists = true;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
        // Return the sentinel
        return userExists;
    }

    //Method 8 delete user by ID. This method will return true if the user was successfully deleted from the database.
    public boolean deleteUserByID(User user)
    {
        //query to delete user
        final String DELETE_QUERY = "DELETE FROM ProjectPilotDB.user WHERE user_id = ?";
        try
        {
            //db connection
            Connection connection = DriverManager.getConnection(DB_URL, UID, PWD);
            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            //set parameters for prepared statement(user_id)
            preparedStatement.setInt(1, user.getID());
            //execute statement
            int foundUser = preparedStatement.executeUpdate();
            //return true if user was found and deleted (foundUser should be 1).
            if(foundUser == 1)
            {
                return true;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
        //return false if user was not found and deleted
        return false;
    }
}
