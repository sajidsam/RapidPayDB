package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

public class ProfileInfo extends JFrame {
    private String accountNumber;  // Account number passed from UserDashboard
    private JTextField firstNameField, lastNameField, emailField, phoneField, addressField, nationalityField, occupationField, maritalStatusField, spouseNameField, childrenField, bankAgencyField, zipCodeField;
    private JComboBox<String> genderComboBox; // Gender selection box
    private JSpinner dobSpinner; // Date of birth spinner

    public ProfileInfo(String accountNumber) {
        this.accountNumber = accountNumber;
        setTitle("User Profile");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ensure it doesn't close the main dashboard
        setLocationRelativeTo(null);

        // Main container panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 235, 59)); // Yellow background

        // Header panel with dark blue color
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 95, 115)); // Dark blue background for header
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel headerLabel = new JLabel("User Profile");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(headerLabel);

        // Profile details panel with GridBagLayout (2 columns)
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new GridBagLayout());
        profilePanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        profilePanel.setBackground(new Color(255, 235, 59)); // Yellow background for profile fields

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add some padding between components
        gbc.anchor = GridBagConstraints.WEST;

        // Initialize text fields for user data
        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        addressField = new JTextField(20);
        nationalityField = new JTextField(20);
        occupationField = new JTextField(20);
        maritalStatusField = new JTextField(20);
        spouseNameField = new JTextField(20);
        childrenField = new JTextField(20);
        bankAgencyField = new JTextField(20);
        zipCodeField = new JTextField(20);

        // Initialize combo box for gender
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});

        // Initialize spinner for date of birth
        Calendar calendar = Calendar.getInstance();
        dobSpinner = new JSpinner(new SpinnerDateModel(calendar.getTime(), null, null, Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dobSpinner, "yyyy-MM-dd");
        dobSpinner.setEditor(dateEditor);

        // Add labels and fields to the profile panel
        gbc.gridx = 0; gbc.gridy = 0;
        profilePanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        profilePanel.add(firstNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        profilePanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        profilePanel.add(lastNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        profilePanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        profilePanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        profilePanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        profilePanel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        profilePanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        profilePanel.add(addressField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        profilePanel.add(new JLabel("Nationality:"), gbc);
        gbc.gridx = 1;
        profilePanel.add(nationalityField, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        profilePanel.add(new JLabel("Occupation:"), gbc);
        gbc.gridx = 1;
        profilePanel.add(occupationField, gbc);

        gbc.gridx = 2; gbc.gridy = 0;
        profilePanel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 3;
        profilePanel.add(genderComboBox, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        profilePanel.add(new JLabel("Date of Birth:"), gbc);
        gbc.gridx = 3;
        profilePanel.add(dobSpinner, gbc);

        gbc.gridx = 2; gbc.gridy = 2;
        profilePanel.add(new JLabel("Marital Status:"), gbc);
        gbc.gridx = 3;
        profilePanel.add(maritalStatusField, gbc);

        gbc.gridx = 2; gbc.gridy = 3;
        profilePanel.add(new JLabel("Spouse Name:"), gbc);
        gbc.gridx = 3;
        profilePanel.add(spouseNameField, gbc);

        gbc.gridx = 2; gbc.gridy = 4;
        profilePanel.add(new JLabel("Children:"), gbc);
        gbc.gridx = 3;
        profilePanel.add(childrenField, gbc);

        gbc.gridx = 2; gbc.gridy = 5;
        profilePanel.add(new JLabel("Bank Agency:"), gbc);
        gbc.gridx = 3;
        profilePanel.add(bankAgencyField, gbc);

        gbc.gridx = 2; gbc.gridy = 6;
        profilePanel.add(new JLabel("Zip Code:"), gbc);
        gbc.gridx = 3;
        profilePanel.add(zipCodeField, gbc);

        // Footer panel with buttons
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(255, 235, 59));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> updateProfileData());
        footerPanel.add(updateButton);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(profilePanel), BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        fetchUserProfileData();
    }

    private void fetchUserProfileData() {
        try (Connection connection = DatabaseUtil.getConnection()) {
            if (connection == null) {
                JOptionPane.showMessageDialog(this, "Database connection error.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "SELECT * FROM user_profile WHERE account_number = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, accountNumber);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        firstNameField.setText(resultSet.getString("first_name"));
                        lastNameField.setText(resultSet.getString("last_name"));
                        emailField.setText(resultSet.getString("email"));
                        phoneField.setText(resultSet.getString("phone"));
                        addressField.setText(resultSet.getString("address"));
                        nationalityField.setText(resultSet.getString("nationality"));
                        occupationField.setText(resultSet.getString("occupation"));
                        maritalStatusField.setText(resultSet.getString("marital_status"));
                        spouseNameField.setText(resultSet.getString("spouse_name"));
                        childrenField.setText(resultSet.getString("children"));
                        bankAgencyField.setText(resultSet.getString("bank_agency"));
                        zipCodeField.setText(resultSet.getString("zip_code"));
                        genderComboBox.setSelectedItem(resultSet.getString("gender"));
                        dobSpinner.setValue(resultSet.getDate("date_of_birth"));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading profile data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateProfileData() {
        try (Connection connection = DatabaseUtil.getConnection()) {
            if (connection == null) {
                JOptionPane.showMessageDialog(this, "Database connection error.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "UPDATE user_profile SET first_name = ?, last_name = ?, email = ?, phone = ?, address = ?, nationality = ?, occupation = ?, marital_status = ?, spouse_name = ?, children = ?, bank_agency = ?, zip_code = ?, gender = ?, date_of_birth = ? WHERE account_number = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, firstNameField.getText());
                preparedStatement.setString(2, lastNameField.getText());
                preparedStatement.setString(3, emailField.getText());
                preparedStatement.setString(4, phoneField.getText());
                preparedStatement.setString(5, addressField.getText());
                preparedStatement.setString(6, nationalityField.getText());
                preparedStatement.setString(7, occupationField.getText());
                preparedStatement.setString(8, maritalStatusField.getText());
                preparedStatement.setString(9, spouseNameField.getText());
                preparedStatement.setString(10, childrenField.getText());
                preparedStatement.setString(11, bankAgencyField.getText());
                preparedStatement.setString(12, zipCodeField.getText());
                preparedStatement.setString(13, genderComboBox.getSelectedItem().toString());

                java.util.Date dob = (java.util.Date) dobSpinner.getValue();
                if (dob != null) {
                    preparedStatement.setDate(14, new java.sql.Date(dob.getTime()));
                } else {
                    preparedStatement.setNull(14, java.sql.Types.DATE);  // Handle if no date is selected
                }

                preparedStatement.setString(15, accountNumber);

                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Profile updated successfully!");
                    // Close the current window and return to the UserDashboard
                    dispose();  // Close ProfileInfo window
                    new UserDashBoard(accountNumber).setVisible(true);  // Open UserDashboard
                } else {
                    JOptionPane.showMessageDialog(this, "Update failed. Account not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();  // This will help you debug
            JOptionPane.showMessageDialog(this, "Error updating profile: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
