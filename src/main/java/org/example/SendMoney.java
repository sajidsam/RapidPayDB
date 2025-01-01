package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SendMoney extends JFrame {

    public SendMoney() {
        setTitle("Send Money");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel setup
        JPanel sendMoneyPanel = new JPanel();
        sendMoneyPanel.setLayout(new BoxLayout(sendMoneyPanel, BoxLayout.Y_AXIS));
        sendMoneyPanel.setBackground(new Color(20, 60, 120));
        sendMoneyPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Components
        JLabel accountNumberLabel = new JLabel("Enter Account Number:");
        accountNumberLabel.setFont(new Font("Arial", Font.BOLD, 14));
        accountNumberLabel.setForeground(Color.WHITE);
        JTextField accountNumberTextField = new JTextField(16);
        accountNumberTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        accountNumberTextField.setBackground(Color.WHITE);
        accountNumberTextField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JLabel amountLabel = new JLabel("Amount to Send:");
        amountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        amountLabel.setForeground(Color.WHITE);
        JTextField amountField = new JTextField(10);
        styleTextField(amountField);

        JLabel passwordLabel = new JLabel("Password for Verification:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(Color.WHITE);
        JPasswordField passwordField = new JPasswordField(10);
        stylePasswordField(passwordField);

        // Add components to panel
        sendMoneyPanel.add(accountNumberLabel);
        sendMoneyPanel.add(accountNumberTextField);
        sendMoneyPanel.add(amountLabel);
        sendMoneyPanel.add(amountField);
        sendMoneyPanel.add(passwordLabel);
        sendMoneyPanel.add(passwordField);

        // Submit Button
        JButton submitButton = new JButton("Send Money");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setBackground(new Color(20, 60, 120));
        submitButton.setForeground(Color.WHITE);
        submitButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        submitButton.addActionListener(e -> {
            String accountNumber = accountNumberTextField.getText();
            String amountText = amountField.getText();
            String passwordText = new String(passwordField.getPassword());

            if (accountNumber.isEmpty() || amountText.isEmpty() || passwordText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    double amount = Double.parseDouble(amountText);
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(this, "Please enter a valid amount!", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // Assuming there's a method to process the money transfer
                        processMoneyTransfer(accountNumber, amount);
                        JOptionPane.showMessageDialog(this, "Successfully sent $" + amount + " to Account: " + accountNumber);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid amount. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(20, 60, 120));
        backButton.setForeground(Color.WHITE);
        backButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        backButton.addActionListener(e -> {
            // Clear the text fields
            accountNumberTextField.setText("");
            amountField.setText("");
            passwordField.setText("");

            // Go back to the User Dashboard
            dispose(); // Close the current window
            UserDashBoard dashboard = new UserDashBoard(); // Create an instance of UserDashBoard
            dashboard.setVisible(true); // Make the UserDashBoard visible
        });

        sendMoneyPanel.add(submitButton);
        sendMoneyPanel.add(backButton);

        // Add panel to the frame
        add(sendMoneyPanel);
        revalidate();
        repaint();
    }

    // Helper method for styling
    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBackground(Color.WHITE);
        textField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
    }

    private void stylePasswordField(JPasswordField passwordField) {
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBackground(Color.WHITE);
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
    }

    // Simulated method to process money transfer (implement actual logic in your main app)
    private void processMoneyTransfer(String accountNumber, double amount) {
        // Logic for money transfer goes here
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SendMoney sendMoney = new SendMoney();
            sendMoney.setVisible(true);
        });
    }
}
