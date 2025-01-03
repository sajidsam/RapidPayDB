package org.example;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import org.mindrot.jbcrypt.BCrypt;

public class SignUp {

    public JPanel createSignUpPage(JPanel mainPanel, CardLayout cardLayout) {
        JPanel panel = new JPanel(null);

        // Background image
        JLabel background = new JLabel(new ImageIcon("C:\\propic\\propic.png"));
        background.setBounds(0, 0, 800, 480);

        JPanel signUpPanel = new JPanel();
        signUpPanel.setLayout(null);
        signUpPanel.setOpaque(false);
        signUpPanel.setBounds(350, 100, 400, 300);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userLabel.setBounds(20, 20, 100, 30);
        signUpPanel.add(userLabel);

        JTextField userTextField = new JTextField();
        userTextField.setBounds(130, 20, 200, 30);
        signUpPanel.add(userTextField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(new Font("Arial", Font.BOLD, 16));
        emailLabel.setBounds(20, 70, 100, 30);
        signUpPanel.add(emailLabel);

        JTextField emailTextField = new JTextField();
        emailTextField.setBounds(130, 70, 200, 30);
        signUpPanel.add(emailTextField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(new Font("Arial", Font.BOLD, 16));
        passLabel.setBounds(20, 120, 100, 30);
        signUpPanel.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(130, 120, 200, 30);
        signUpPanel.add(passField);

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(130, 170, 100, 30);
        signUpButton.addActionListener(e -> {
            String username = userTextField.getText().trim();
            String email = emailTextField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            // Validate user input
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Generate a unique account number and credit card number
            String accountNumber = generateAccountNumber();
            String creditCardNumber = generateCreditCardNumber();

            try (Connection connection = DatabaseUtil.getConnection()) {
                // Check for duplicate email
                String checkEmailQuery = "SELECT * FROM users WHERE email = ?";
                try (PreparedStatement checkEmailStmt = connection.prepareStatement(checkEmailQuery)) {
                    checkEmailStmt.setString(1, email);
                    try (ResultSet resultSet = checkEmailStmt.executeQuery()) {
                        if (resultSet.next()) {
                            JOptionPane.showMessageDialog(panel, "Email already exists. Please use a different email.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

                // Hash the password using bcrypt
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

                // Insert new user with account number, credit card number, and hashed password
                String query = "INSERT INTO users (username, email, password, account_number, credit_card_number) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, email);
                    preparedStatement.setString(3, hashedPassword); // Store the hashed password
                    preparedStatement.setString(4, accountNumber); // Insert the generated account number
                    preparedStatement.setString(5, creditCardNumber); // Insert the generated credit card number

                    int rowsInserted = preparedStatement.executeUpdate();

                    if (rowsInserted > 0) {
                        // Insert the balance record for the user
                        String queryInsertBalance = "INSERT INTO user_balance (account_number, balance) VALUES (?, ?)";
                        try (PreparedStatement balanceStmt = connection.prepareStatement(queryInsertBalance)) {
                            balanceStmt.setString(1, accountNumber); // Set the generated account number
                            balanceStmt.setBigDecimal(2, new BigDecimal("1000.00")); // Set default balance to 1000.00

                            int rowsInsertedBalance = balanceStmt.executeUpdate();

                            if (rowsInsertedBalance > 0) {
                                // Insert the account number into the user_profile table
                                String queryInsertUserProfile = "INSERT INTO user_profile (account_number) VALUES (?)";
                                try (PreparedStatement profileStmt = connection.prepareStatement(queryInsertUserProfile)) {
                                    profileStmt.setString(1, accountNumber); // Set the generated account number

                                    int rowsInsertedProfile = profileStmt.executeUpdate();

                                    if (rowsInsertedProfile > 0) {
                                        JOptionPane.showMessageDialog(panel, "Account Created Successfully!");

                                        // Send a welcome email
                                        EmailSender.sendEmail(
                                                email,
                                                "Welcome to RapidPay!",
                                                "Dear " + username + ",\n\nThank you for signing up for RapidPay. We're excited to have you on board!\n\nYour account number is: " + accountNumber + "\nYour credit card number is: " + creditCardNumber + "\n\nBest Regards,\nRapidPay Team"
                                        );
                                    } else {
                                        JOptionPane.showMessageDialog(panel, "Failed to update user profile. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            } else {
                                JOptionPane.showMessageDialog(panel, "Failed to initialize user balance. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(panel, "Failed to create account. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        signUpPanel.add(signUpButton);

        JButton backButton = new JButton("Back to Login");
        backButton.setBounds(240, 170, 100, 30);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "LoginPage"));
        signUpPanel.add(backButton);

        background.setLayout(null);
        background.add(signUpPanel);

        panel.setLayout(null);
        panel.add(background);

        return panel;
    }

    // Method to generate a unique account number starting with "RP"
    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder("RP");

        // Add a random number between 1000 and 9999
        accountNumber.append(random.nextInt(9000) + 1000);

        // Add a random letter for added uniqueness
        char randomChar = (char) (random.nextInt(26) + 'A');
        accountNumber.append(randomChar);

        // Add another random number between 10 and 99
        accountNumber.append(random.nextInt(90) + 10);

        return accountNumber.toString();
    }

    // Method to generate a valid fake credit card number using the Luhn algorithm
    private String generateCreditCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder("4"); // Start with '4' for Visa

        // Generate the first 15 digits
        for (int i = 1; i < 15; i++) {
            cardNumber.append(random.nextInt(10));
        }

        // Now calculate the check digit using the Luhn algorithm
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));

            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }

            sum += n;
            alternate = !alternate;
        }

        // Calculate check digit and append it to the card number
        int checkDigit = (10 - (sum % 10)) % 10;
        cardNumber.append(checkDigit);

        return cardNumber.toString();
    }
}
