package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UserDashBoard extends JFrame {

    private JLabel balanceAmount; // Declare the balance label as an instance variable to update it
    private JLabel creditScoreLabel; // Label to display the credit score
    private int creditScore = 0; // Initial credit score

    public UserDashBoard() {
        setTitle("RapidPay - User Dashboard");
        setSize(800, 600); // Increased size for better layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Dashboard panel with light theme
        JPanel dashboardPanel = new JPanel(null);
        dashboardPanel.setBackground(new Color(240, 248, 255)); // Light theme background

        // Add header
        JLabel header = new JLabel(" RapidPay ", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 28));
        header.setForeground(new Color(20, 60, 120));
        header.setBounds(0, 10, 800, 50);
        dashboardPanel.add(header);

        // Add balance overview
        JLabel balanceLabel = new JLabel(" Your Balance ");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 20));
        balanceLabel.setBounds(470, 80, 200, 30);
        dashboardPanel.add(balanceLabel);

        // Balance label that will update when money is added
        balanceAmount = new JLabel(" $ 0.00", SwingConstants.LEFT); // Initial balance
        balanceAmount.setFont(new Font("Arial", Font.PLAIN, 24));
        balanceAmount.setForeground(new Color(34, 139, 34)); // Green for balance
        balanceAmount.setBounds(500, 120, 200, 30);
        dashboardPanel.add(balanceAmount);

        // Add credit score overview
        JLabel creditScoreTextLabel = new JLabel(" Credit Score ");
        creditScoreTextLabel.setFont(new Font("Arial", Font.BOLD, 20));
        creditScoreTextLabel.setBounds(620, 80, 200, 30);
        dashboardPanel.add(creditScoreTextLabel);

        // Credit Score label
        creditScoreLabel = new JLabel(String.valueOf(creditScore), SwingConstants.LEFT);
        creditScoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        creditScoreLabel.setForeground(new Color(255, 140, 0)); // Orange for credit score
        creditScoreLabel.setBounds(630, 120, 200, 30);
        dashboardPanel.add(creditScoreLabel);

        // Add Profile Button
        JButton profileButton = new JButton("Profile");
        styleButton(profileButton);
        profileButton.setBounds(300, 80, 150, 40); // Position next to "Your Balance" section
        profileButton.addActionListener(e -> {
            dispose(); // Close the UserDashBoard window
            ProfileInfo profileInfo = new ProfileInfo(); // Open ProfileInfo class
            profileInfo.setVisible(true);
        });
        addHoverEffect(profileButton);
        dashboardPanel.add(profileButton);

        // Send Money button
        JButton sendMoneyButton = new JButton("Send Money");
        styleButton(sendMoneyButton);
        sendMoneyButton.setBounds(30, 180, 150, 40);
        sendMoneyButton.addActionListener(e -> {
            dispose();
            SendMoney sendMoneyWindow = new SendMoney(); // Open SendMoney class
            sendMoneyWindow.setVisible(true);
        });
        addHoverEffect(sendMoneyButton);
        dashboardPanel.add(sendMoneyButton);

        // Add Money button
        JButton addMoneyButton = new JButton("Add Money");
        styleButton(addMoneyButton);
        addMoneyButton.setBounds(200, 180, 150, 40);
        addMoneyButton.addActionListener(e -> {
            dispose();
            AddMoney addMoneyWindow = new AddMoney(); // Open AddMoney class
            addMoneyWindow.setVisible(true);
        });
        addHoverEffect(addMoneyButton);
        dashboardPanel.add(addMoneyButton);

        // Pay Bill button
        JButton payBillButton = new JButton("Pay Bill");
        styleButton(payBillButton);
        payBillButton.setBounds(370, 180, 150, 40);
        payBillButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Pay Bill feature coming soon!");
            increaseCreditScore(); // Increment credit score after a bill payment
        });
        addHoverEffect(payBillButton);
        dashboardPanel.add(payBillButton);

        JPanel sideBySidePanel = new JPanel(new GridLayout(1, 2, 10, 0));
        sideBySidePanel.setBounds(30, 250, 740, 120);

        JPanel transactionPanel = new JPanel();
        transactionPanel.setLayout(new BorderLayout());
        JTextArea transactionArea = new JTextArea(
                "1. Received 500 from John\n" +
                        "2. Sent 300 to Grocery Store\n" +
                        "3. Paid 100 for Electricity Bill\n"
        );
        transactionArea.setFont(new Font("Arial", Font.PLAIN, 16));
        transactionArea.setEditable(false);
        transactionArea.setBackground(new Color(245, 245, 245));
        transactionArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        transactionPanel.add(new JScrollPane(transactionArea), BorderLayout.CENTER);
        transactionPanel.setBorder(BorderFactory.createTitledBorder("Recent Transactions"));

        JPanel notificationsPanel = new JPanel();
        notificationsPanel.setLayout(new BorderLayout());
        JTextArea notificationsArea = new JTextArea(
                "1. New feature: Scheduled Payments\n" +
                        "2. Update: Increased security measures\n"
        );
        notificationsArea.setFont(new Font("Arial", Font.PLAIN, 16));
        notificationsArea.setEditable(false);
        notificationsArea.setBackground(new Color(245, 245, 245));
        notificationsArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        notificationsPanel.add(new JScrollPane(notificationsArea), BorderLayout.CENTER);
        notificationsPanel.setBorder(BorderFactory.createTitledBorder("Notifications"));

        sideBySidePanel.add(transactionPanel);
        sideBySidePanel.add(notificationsPanel);
        dashboardPanel.add(sideBySidePanel);

        JButton logoutButton = new JButton("Log Out");
        styleButton(logoutButton);
        logoutButton.setBounds(670, 520, 100, 30);
        logoutButton.addActionListener((ActionEvent e) -> {
            int response = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to log out?",
                    "Log Out Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        addHoverEffect(logoutButton);
        dashboardPanel.add(logoutButton);

        add(dashboardPanel);
        revalidate();
        repaint();
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(20, 60, 120));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    }

    private void addHoverEffect(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(30, 80, 150));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(20, 60, 120));
            }
        });
    }

    private void increaseCreditScore() {
        creditScore += 2;
        creditScoreLabel.setText(String.valueOf(creditScore));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserDashBoard dashboard = new UserDashBoard();
            dashboard.setVisible(true);
        });
    }
}
