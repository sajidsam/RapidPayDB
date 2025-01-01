package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/RapidPayDB"; // Replace with your database URL
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = ""; // Replace with your MySQL password

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Method to get user balance from the database
    public double getUserBalance(String username) {
        double balance = 0.0;
        String query = "SELECT balance FROM user_balance WHERE username = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                balance = rs.getDouble("balance");  // Retrieve balance
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return balance;
    }
}
