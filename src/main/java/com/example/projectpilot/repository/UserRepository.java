package com.example.projectpilot.repository;

import com.example.projectpilot.model.User;
import com.example.projectpilot.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository
{
    private final DatabaseService databaseService;

    @Autowired
    public UserRepository(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    //Method 1 get user from SQL. This method will return a user object from the database.
    private User getUser(ResultSet resultSet) throws SQLException
    {
        //get user_id from result set
        int id = resultSet.getInt(1);
        //get admin status from result set
        boolean admin = resultSet.getBoolean(2);
        //get first_name from result set
        String first_name = resultSet.getString(3);
        //get last_name from result set
        String last_name = resultSet.getString(4);
        //get email from result set
        String email = resultSet.getString(5);
        //get password from result set
        String password = resultSet.getString(6);
        //create user object
        return new User(id, admin, first_name, last_name, email, password);
    }

    //Method 2 get all users. This method will return a list of all users in the database.
    public List<User> getAllUsers()
    {
        //create list of users
        List<User> userList = new ArrayList<>();
        //execute statement, here there is no exceptions that need to be caught. It does need to be in try/catch.
        // Limit the scope of a try block to only the code that might throw an exception.
        final String SQL_QUERY = "SELECT * FROM ProjectPilotDB.user";
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
                User user = getUser(resultSet);
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
            Connection connection = databaseService.getConnection();
            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY);
            //set parameters
            preparedStatement.setString(1, checkEmail);
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


    //Method 4 add user. This method will return true if the user was successfully added to the database.
    public boolean addUser(User user)
    {
        // Query to insert user
        final String INSERT_QUERY = "INSERT INTO ProjectPilotDB.user (admin, first_name, last_name, email, password) VALUES (?,?,?,?,?)";
        try
        {
            // DB connection
            Connection connection = databaseService.getConnection();
            // Prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
            // Set admin status
            preparedStatement.setBoolean(1, user.isAdmin());
            // Set first_name
            preparedStatement.setString(2, user.getFirstName());
            // Set last_name
            preparedStatement.setString(3, user.getLastName());
            // Set email
            preparedStatement.setString(4, user.getEmail());
            // Encrypt password
            String encryptedPassword = encoder.encode(user.getPassword());
            // Set encrypted password
            preparedStatement.setString(5, encryptedPassword);
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

    //Method 5 get user by ID. This method will return a user object if the user exists in the database.
    public User getUserByID(int id)
    {
        //query to find user
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.user WHERE id = ?";
        try
        {
            //db connection
            Connection connection = databaseService.getConnection();
            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY);
            //set parameters for prepared statement (user_id)
            preparedStatement.setInt(1, id);
            //execute statement
            ResultSet resultSet = preparedStatement.executeQuery();
            //return user if user exists
            if ( resultSet.next() )
            {
                return getUser(resultSet);
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

    public User getUserByEmailAndPassword(String email, String password)
    {
        // SQL QUERY
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.user WHERE email = ?";
        User user = new User();
        user.setEmail(email);
        try
        {
            Connection connection = databaseService.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if ( resultSet.next() )
            {
                int id = resultSet.getInt(1);
                boolean admin = resultSet.getBoolean(2);
                String first_name = resultSet.getString(3);
                String last_name = resultSet.getString(4);
                String hashedPassword = resultSet.getString(6);

                if ( encoder.matches(password, hashedPassword) )
                { // Compare plain password with hashed one
                    user.setID(id);
                    user.setAdmin(admin);
                    user.setFirstName(first_name);
                    user.setLastName(last_name);
                }
                else
                {
                    System.out.println("Invalid password");
                    return null;
                }
            }
            else
            {
                System.out.println("User not found");
                return null;
            }

        }
        catch (SQLException e)
        {
            System.out.println("Error - Password");
            e.printStackTrace();
        }
        return user;
    }


    // Method 6 update user. This method will update the selected user in the database. Without returning anything.
    public void updateUser(User user)
    {
        //query to update user
        final String UPDATE_QUERY = "UPDATE ProjectPilotDB.user SET admin = ?, first_name = ?, last_name = ?, email = ?, password = ? WHERE id = ?";
        try
        {
            //db connection
            Connection connection = databaseService.getConnection();
            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            //set parameters for prepared statement

            //set admin status
            preparedStatement.setBoolean(1, user.isAdmin());
            //set first_name
            preparedStatement.setString(2, user.getFirstName());
            //set last_name
            preparedStatement.setString(3, user.getLastName());
            //set email
            preparedStatement.setString(4, user.getEmail());
            //set password
            preparedStatement.setString(5, user.getPassword());
            //set user_id
            preparedStatement.setInt(6, user.getId());
            //execute statement

            preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
    }

    // Method 7 verify user. This method will return true if the login credentials are matched in the database.
    public boolean verifyUser(String email, String password)
    {
        // Make a sentinel to check user, assume the user does not exist
        boolean userExists = false;

        // Query to find user
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.user WHERE email = ?";
        // Try to query the database
        try
        {
            // Establish a database connection
            Connection connection = databaseService.getConnection();
            // Prepare the SQL query
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY);
            // Set the parameters for the query (email)
            preparedStatement.setString(1, email);
            // Execute the query and retrieve the result
            ResultSet resultSet = preparedStatement.executeQuery();
            // Check if a user with the given email exists
            if ( resultSet.next() )
            {
                // If a user exists, retrieve the stored password
                String savedCode = resultSet.getString("password");
                if ( savedCode == null )
                {
                    System.out.println("Password not found for the user");
                }
                else if ( encoder.matches(password, savedCode) )
                {
                    userExists = true;
                }
                else
                {
                    System.out.println("Invalid password");
                }
            }
            else
            {
                System.out.println("User not found");
            }
        }
        catch (SQLException e)
        {
            System.out.println("Could not verify user");
            e.printStackTrace();
        }
        // Return the sentinel
        return userExists;
    }


    //Method 8 delete user by ID. This method will return true if the user was successfully deleted from the database.
    public boolean deleteUserByID(User user)
    {
        //query to delete user
        final String DELETE_QUERY = "DELETE FROM ProjectPilotDB.user WHERE id = ?";
        try
        {
            //db connection
            Connection connection = databaseService.getConnection();
            //prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            //set parameters for prepared statement(user_id)
            preparedStatement.setInt(1, user.getId());
            //execute statement
            int foundUser = preparedStatement.executeUpdate();
            //return true if user was found and deleted (foundUser should be 1).
            if ( foundUser == 1 )
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
