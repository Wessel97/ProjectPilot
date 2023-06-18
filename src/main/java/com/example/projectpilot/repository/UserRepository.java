package com.example.projectpilot.repository;

import com.example.projectpilot.model.User;
import com.example.projectpilot.service.DatabaseService;
import org.springframework.stereotype.Repository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository
{
    private final DatabaseService databaseService;

    public UserRepository(DatabaseService databaseService)
    {
        this.databaseService = databaseService;
    }

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    // Denne metode vil returnere et brugerobjekt fra databasen.
    private User getUser(ResultSet resultSet) throws SQLException
    {
        int id = resultSet.getInt(1);
        boolean admin = resultSet.getBoolean(2);
        String first_name = resultSet.getString(3);
        String last_name = resultSet.getString(4);
        String email = resultSet.getString(5);
        String password = resultSet.getString(6);
        return new User(id, admin, first_name, last_name, email, password);
    }

    // Denne metode vil returnere en liste over alle brugere i databasen.
    public List<User> getAllUsers()
    {
        List<User> userList = new ArrayList<>();
        final String SQL_QUERY = "SELECT * FROM ProjectPilotDB.user";
        try (Connection connection = databaseService.getConnection();
             Statement statement = connection.createStatement())
        {
            ResultSet resultSet = statement.executeQuery(SQL_QUERY);
            while ( resultSet.next() )
            {
                User user = getUser(resultSet);
                userList.add(user);
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

    //Metode 3: Tjek om brugeren findes. Denne metode vil returnere sandt, hvis brugeren findes i databasen.
    public boolean checkIfUserExists(String checkEmail)
    {
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.user WHERE email = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY))
        {
            preparedStatement.setString(1, checkEmail);
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


    // Denne metode vil returnere sandt, hvis brugeren blev tilføjet til databasen.
    public boolean addUser(User user)
    {
        final String INSERT_QUERY = "INSERT INTO ProjectPilotDB.user (admin, first_name, last_name, email, password) VALUES (?,?,?,?,?)";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY))
        {
            preparedStatement.setBoolean(1, user.isAdmin());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getEmail());
            String encryptedPassword = encoder.encode(user.getPassword());
            preparedStatement.setString(5, encryptedPassword);
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

    // Denne metode vil returnere et brugerobjekt, hvis brugeren findes i databasen.
    public User getUserByID(int id)
    {
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.user WHERE id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY))
        {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
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
        return null;
    }

    // Denne metode vil returnere en user hvis den findes i databasen.
    public User getUserByEmailAndPassword(String email, String password)
    {
        User user = new User();
        user.setEmail(email);
        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.user WHERE email = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY))
        {
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
                {
                    // Sammenlign det indtastede password med det hashede password i databasen.
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


    // Denne metode vil opdatere den valgte bruger i databasen uden at returnere noget.
    public void updateUser(User user, boolean passwordChanged)
    {
        final String UPDATE_QUERY = "UPDATE ProjectPilotDB.user SET admin = ?, first_name = ?, last_name = ?, email = ?, password = ? WHERE id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY))

        {
            preparedStatement.setBoolean(1, user.isAdmin());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getEmail());

            if ( passwordChanged )
            {
                String encryptedPassword = encoder.encode(user.getPassword());
                preparedStatement.setString(5, encryptedPassword);
            }
            else
            {
                preparedStatement.setString(5, user.getPassword());
            }
            preparedStatement.setInt(6, user.getId());

            preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Could not query database");
            e.printStackTrace();
        }
    }

    // Denne metode vil returnere sandt, hvis loginoplysningerne matcher i databasen.
    public boolean verifyUser(String email, String password)
    {
        boolean userExists = false;

        final String FIND_QUERY = "SELECT * FROM ProjectPilotDB.user WHERE email = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_QUERY))
        {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if ( resultSet.next() )
            {
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
        return userExists;
    }


    // Denne metode vil returnere sandt, hvis brugeren blev slettet fra databasen.
    public boolean deleteUserByID(int userId)
    {

        final String UNASSIGN_QUERY = "UPDATE ProjectPilotDB.task SET user_id = NULL WHERE user_id = ?";
        final String DELETE_QUERY = "DELETE FROM ProjectPilotDB.user WHERE id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement unassignPreparedStatement = connection.prepareStatement(UNASSIGN_QUERY);
             PreparedStatement deletePreparedStatement = connection.prepareStatement(DELETE_QUERY))
        {
            unassignPreparedStatement.setInt(1, userId);
            deletePreparedStatement.setInt(1, userId);

            unassignPreparedStatement.executeUpdate();
            int foundUser = deletePreparedStatement.executeUpdate();
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
        return false;
    }
}
