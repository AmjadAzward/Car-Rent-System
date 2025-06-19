/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author ilma
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ilma
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.ResultSetMetaData; 


public class Database {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/pharmacy_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "AMJU";
    //Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);


    public void insertUserData(String username, String email, String phoneNumber, String gender, String Password, String address, String firstname, String lastname) {
        String insertSQL = "INSERT INTO users (username, email, phone_number, gender, password , address , firstname , lastname) VALUES (?, ?, ?, ?, ?,? ,? ,?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phoneNumber);
            preparedStatement.setString(4, gender);
            preparedStatement.setString(5, Password);
            preparedStatement.setString(6, firstname);
            preparedStatement.setString(7, lastname);
            preparedStatement.setString(8, address);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User data inserted successfully.");
            } else {
                System.out.println("No rows were affected.");
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Error inserting data: " + e);
            e.printStackTrace();
        }
    }
      public boolean updateUserData(String oldUsername, String newUsername, String email, String phoneNumber, String address, String firstName, String lastName, String gender, String password) {
        String updateSQL = "UPDATE users SET username=?, email=?, phone_number=?, address=?, firstname=?, lastname=?, gender=?, password=? WHERE username=?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Check if the old username exists
            if (!checkIfUserExists(connection, oldUsername)) {
                System.err.println("Old username does not exist in the database.");
                return false; // Old username does not exist
            }

            // Check for unique username
            if (!isUsernameUnique(connection, newUsername, oldUsername)) {
                System.err.println("Username already exists!"); // Log username conflict
                return false; // Handle as needed
            }

            // Prepare the update statement
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
                preparedStatement.setString(1, newUsername);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, phoneNumber);
                preparedStatement.setString(4, address);
                preparedStatement.setString(5, firstName);
                preparedStatement.setString(6, lastName);
                preparedStatement.setString(7, gender);
                preparedStatement.setString(8, password); // Ensure the password is hashed before being set
                preparedStatement.setString(9, oldUsername);

                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected); // Log number of affected rows
                return rowsAffected > 0; // Return true if the update was successful
            }
        } catch (SQLException e) {
            System.err.println("Error updating user data: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            return false; // Return false if there was an error
        }
    }

    private boolean checkIfUserExists(Connection connection, String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement checkUserStmt = connection.prepareStatement(query)) {
            checkUserStmt.setString(1, username);
            try (ResultSet userExists = checkUserStmt.executeQuery()) {
                return userExists.next() && userExists.getInt(1) > 0;
            }
        }
    }

    private boolean isUsernameUnique(Connection connection, String newUsername, String oldUsername) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE username = ? AND username != ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(query)) {
            checkStmt.setString(1, newUsername);
            checkStmt.setString(2, oldUsername);
            try (ResultSet rs = checkStmt.executeQuery()) {
                return rs.next() && rs.getInt(1) == 0;
            }
        }
    }






}



