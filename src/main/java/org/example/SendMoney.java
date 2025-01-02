package org.example;

import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SendMoney extends JFrame {

    private String accountNumber; // Store the account number (or username)

    // Modify the constructor to accept the account number (or username)
    public SendMoney(String accountNumber) {
        this.accountNumber = accountNumber; // Set account number

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

        JButton sendButton = new JButton("Send");
        styleButton(sendButton);
        sendMoneyPanel.add(sendButton);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String recipientAccount = accountNumberTextField.getText();
                String amount = amountField.getText();
                char[] password = passwordField.getPassword();

               try (Connection conn = DatabaseUtil.getConnection()) {
                 conn.setAutoCommit(false);
                 String passwordQuery = "SELECT password FROM users WHERE account_number = ?";
                    try (PreparedStatement passwordStmt = conn.prepareStatement(passwordQuery)) {
                        passwordStmt.setString(1, accountNumber);
                        try (ResultSet rs = passwordStmt.executeQuery()) {
                            if (rs.next()) {
                                String correctPassword = rs.getString("password");
                                if (BCrypt.checkpw(String.valueOf(password), correctPassword)) {
                                    // Password is correct
                                } else {
                                    JOptionPane.showMessageDialog(SendMoney.this,
                                            "Incorrect password. Please try again.",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }
                        }
                    }

    // Retrieve the current balance
    String getBalanceQuery = "SELECT balance FROM user_balance WHERE account_number = ?";
    try (PreparedStatement getBalanceStmt = conn.prepareStatement(getBalanceQuery)) {
        getBalanceStmt.setString(1, accountNumber);
        try (ResultSet rs = getBalanceStmt.executeQuery()) {
            if (rs.next()) {
                double currentBalance = rs.getDouble("balance");

                // Update the balance
                String updateBalanceQuery = "UPDATE user_balance SET balance = ? WHERE account_number = ?";
                String UpdateReceiverBalance = "UPDATE user_balance SET balance = balance + ? WHERE account_number = ?";
                try (PreparedStatement updateBalanceStmt = conn.prepareStatement(updateBalanceQuery)) {
                    double newBalance = currentBalance - Double.parseDouble(amount);
                    updateBalanceStmt.setDouble(1, newBalance);
                    updateBalanceStmt.setString(2, accountNumber);
                    updateBalanceStmt.executeUpdate();

                    conn.commit();
                    JOptionPane.showMessageDialog(SendMoney.this,
                            "Money sent to account " + recipientAccount + " amounting to " + amount,
                            "Transaction Successful", JOptionPane.INFORMATION_MESSAGE);
                }
                try (PreparedStatement updateBalanceStmt = conn.prepareStatement(UpdateReceiverBalance)) {
                    updateBalanceStmt.setDouble(1, Double.parseDouble(amount));
                    updateBalanceStmt.setString(2, recipientAccount);
                    updateBalanceStmt.executeUpdate();

                    conn.commit();
                    JOptionPane.showMessageDialog(SendMoney.this,
                            "Money received from account " + accountNumber + " amounting to " + amount,
                            "Transaction Successful", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    } catch (Exception ex) {
        conn.rollback();
        ex.printStackTrace();
    }
} catch (Exception ex) {
    ex.printStackTrace();
}
            }
        });

        add(sendMoneyPanel);
    }

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

    private void styleButton(JButton button) {
        button.setBackground(new Color(20, 60, 120));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    }
}
