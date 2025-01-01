package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddMoney extends JFrame {

    public AddMoney() {
        setTitle("Add Money");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel setup
        JPanel addMoneyPanel = new JPanel();
        addMoneyPanel.setLayout(new BoxLayout(addMoneyPanel, BoxLayout.Y_AXIS));
        addMoneyPanel.setBackground(new Color(20, 60, 120));
        addMoneyPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Components
        JLabel creditCardLabel = new JLabel("Enter Credit Card Number:");
        creditCardLabel.setFont(new Font("Arial", Font.BOLD, 14));
        creditCardLabel.setForeground(Color.WHITE);
        JTextField creditCardTextField = new JTextField(16);
        creditCardTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        creditCardTextField.setBackground(Color.WHITE);
        creditCardTextField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JLabel amountLabel = new JLabel("Amount to Add:");
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
        addMoneyPanel.add(creditCardLabel);
        addMoneyPanel.add(creditCardTextField);
        addMoneyPanel.add(amountLabel);
        addMoneyPanel.add(amountField);
        addMoneyPanel.add(passwordLabel);
        addMoneyPanel.add(passwordField);

        // Submit Button
        JButton submitButton = new JButton("Add Money");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setBackground(new Color(20, 60, 120));
        submitButton.setForeground(Color.WHITE);
        submitButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        submitButton.addActionListener(e -> {
            String enteredCard = creditCardTextField.getText();
            String amountText = amountField.getText();
            String passwordText = new String(passwordField.getPassword());

            if (enteredCard.isEmpty() || amountText.isEmpty() || passwordText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (enteredCard.length() != 16) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid 16-digit credit card number!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        double amount = Double.parseDouble(amountText);
                        if (amount <= 0) {
                            JOptionPane.showMessageDialog(this, "Please enter a valid amount!", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            // Assuming there's a method to update balance in the main app
                            updateBalance(amount);
                            JOptionPane.showMessageDialog(this, "Successfully added $" + amount + " from Credit Card: " + enteredCard);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid amount. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        addMoneyPanel.add(submitButton);

        // Add panel to the frame
        add(addMoneyPanel);
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

    // Simulated method to update balance (implement actual logic in your main app)
    private void updateBalance(double amount) {
        // Logic to update balance goes here
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AddMoney addMoney = new AddMoney();
            addMoney.setVisible(true);
        });
    }
}
