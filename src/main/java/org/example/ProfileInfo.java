package org.example;

import javax.swing.*;
import java.awt.*;

public class ProfileInfo extends JFrame {
    public ProfileInfo() {
        setTitle("User Profile");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ensure it doesn't close the main dashboard
        setLocationRelativeTo(null);

        JPanel profilePanel = new JPanel(new GridLayout(0, 2, 10, 10));
        profilePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        profilePanel.setBackground(new Color(245, 245, 245)); // Light gray background

        // Add labels and user info fields
        profilePanel.add(new JLabel("Username:"));
        profilePanel.add(new JLabel("JohnDoe123")); // Replace with dynamic data retrieval

        profilePanel.add(new JLabel("Email:"));
        profilePanel.add(new JLabel("johndoe@example.com")); // Replace with dynamic data retrieval

        profilePanel.add(new JLabel("Phone:"));
        profilePanel.add(new JLabel("123-456-7890")); // Replace with dynamic data retrieval

        profilePanel.add(new JLabel("Address:"));
        profilePanel.add(new JLabel("123 Main St, City, Country")); // Replace with dynamic data retrieval

        add(profilePanel);
    }
}
