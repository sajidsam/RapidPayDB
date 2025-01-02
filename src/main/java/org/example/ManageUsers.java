package org.example;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ManageUsers {

    private static final String URL = "jdbc:mysql://localhost:3306/RapidPayDB";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    ManageUsers() {
        // Create a frame to display the table
        JFrame frame = new JFrame("User Information");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);

        // Set up the table model and JTable
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Email");
        tableModel.addColumn("Username");
        tableModel.addColumn("Account Number");

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);

        // Fetch user data and add it to the table
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {

            String query = "SELECT email, username, account_number FROM users";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String email = resultSet.getString("email");
                String username = resultSet.getString("username");
                String accountNumber = resultSet.getString("account_number");

                // Add a row to the table with user information
                tableModel.addRow(new Object[]{email, username, accountNumber});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Display the frame
        frame.setVisible(true);
    }

}
