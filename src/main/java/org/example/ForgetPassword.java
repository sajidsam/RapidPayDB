package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class ForgetPassword {

    public JPanel createForgotPasswordPage(JPanel mainPanel, CardLayout cardLayout) {
        JPanel panel = new JPanel(null);

        JLabel background = new JLabel(new ImageIcon("C:\\propic\\propic.png"));
        background.setBounds(0, 0, 800, 480);

        JPanel forgotPasswordPanel = new JPanel();
        forgotPasswordPanel.setLayout(null);
        forgotPasswordPanel.setOpaque(false);
        forgotPasswordPanel.setBounds(350, 100, 400, 230);

        JLabel instructionLabel = new JLabel("Please enter your email to reset password.");
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        instructionLabel.setBounds(20, 20, 360, 30);
        forgotPasswordPanel.add(instructionLabel);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(new Font("Arial", Font.BOLD, 16));
        emailLabel.setBounds(20, 70, 100, 30);
        forgotPasswordPanel.add(emailLabel);

        JTextField emailTextField = new JTextField();
        emailTextField.setBounds(130, 70, 200, 30);
        forgotPasswordPanel.add(emailTextField);

        JButton sendResetLinkButton = new JButton("Send Reset Link");
        sendResetLinkButton.setBounds(130, 120, 200, 30);
        sendResetLinkButton.addActionListener(e -> {
            String email = emailTextField.getText();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    // Send the password reset email
                    EmailSender.sendEmail(
                            email,
                            "Password Reset Request",
                            "Dear User,\n\nPlease click the link below to reset your password:\n\nhttp://example.com/reset-password\n\nThank you,\nRapidPay Team"
                    );
                    JOptionPane.showMessageDialog(panel, "Password reset link sent to: " + email, "Success", JOptionPane.INFORMATION_MESSAGE);
                    cardLayout.show(mainPanel, "LoginPage"); // Go back to the login page after sending the email
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Failed to send email. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        forgotPasswordPanel.add(sendResetLinkButton);

        JButton backButton = new JButton("Back to Login");
        backButton.setBounds(130, 160, 200, 30);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "LoginPage"));
        forgotPasswordPanel.add(backButton);

        background.setLayout(null);
        background.add(forgotPasswordPanel);

        panel.setLayout(null);
        panel.add(background);

        return panel;
    }
}
