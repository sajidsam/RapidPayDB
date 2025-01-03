package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDashBoard extends JFrame {

    private JLabel balanceAmount;
    private JLabel creditScoreLabel;
    private int creditScore = 0;
    private String accountNumber;

    public UserDashBoard(String accountNumber) {
        this.accountNumber = accountNumber;
        setTitle("RapidPay - User Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel dashboardPanel = new JPanel(null);
        dashboardPanel.setBackground(new Color(240, 248, 255));

        JLabel header = new JLabel(" RapidPay ", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 28));
        header.setForeground(new Color(20, 60, 120));
        header.setBounds(0, 10, 800, 50);
        dashboardPanel.add(header);

        JLabel balanceLabel = new JLabel(" Your Balance ");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 20));
        balanceLabel.setBounds(470, 80, 200, 30);
        dashboardPanel.add(balanceLabel);

        balanceAmount = new JLabel(" $ 0.00", SwingConstants.LEFT);
        balanceAmount.setFont(new Font("Arial", Font.PLAIN, 24));
        balanceAmount.setForeground(new Color(34, 139, 34));
        balanceAmount.setBounds(500, 120, 200, 30);
        dashboardPanel.add(balanceAmount);

        retrieveAndUpdateBalance();

        JLabel creditScoreTextLabel = new JLabel(" Credit Score ");
        creditScoreTextLabel.setFont(new Font("Arial", Font.BOLD, 20));
        creditScoreTextLabel.setBounds(620, 80, 200, 30);
        dashboardPanel.add(creditScoreTextLabel);

        creditScoreLabel = new JLabel(String.valueOf(creditScore), SwingConstants.LEFT);
        creditScoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        creditScoreLabel.setForeground(new Color(255, 140, 0));
        creditScoreLabel.setBounds(630, 120, 200, 30);
        dashboardPanel.add(creditScoreLabel);

        JButton profileButton = new JButton("Profile");
        styleButton(profileButton);
        profileButton.setBounds(300, 80, 150, 40);
        profileButton.addActionListener(e -> {
            ProfileInfo profileInfo = new ProfileInfo(accountNumber); // Pass accountNumber here
            profileInfo.setVisible(true);
        });
        addHoverEffect(profileButton);
        dashboardPanel.add(profileButton);

        JButton sendMoneyButton = new JButton("Send Money");
        styleButton(sendMoneyButton);
        sendMoneyButton.setBounds(30, 180, 150, 40);
        sendMoneyButton.addActionListener(e -> {
            dispose();
            SendMoney sendMoneyWindow = new SendMoney(accountNumber);
            sendMoneyWindow.setVisible(true);
        });
        addHoverEffect(sendMoneyButton);
        dashboardPanel.add(sendMoneyButton);

        JButton addMoneyButton = new JButton("Add Money");
        styleButton(addMoneyButton);
        addMoneyButton.setBounds(200, 180, 150, 40);
        addMoneyButton.addActionListener(e -> {
            dispose();
            AddMoney addMoneyWindow = new AddMoney();
            addMoneyWindow.setVisible(true);
        });
        addHoverEffect(addMoneyButton);
        dashboardPanel.add(addMoneyButton);

        JButton payBillButton = new JButton("Pay Bill");
        styleButton(payBillButton);
        payBillButton.setBounds(370, 180, 150, 40);
        payBillButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Pay Bill feature coming soon!");
            increaseCreditScore();
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

    private void retrieveAndUpdateBalance() {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT balance FROM user_balance WHERE account_number = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, accountNumber);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        double balance = resultSet.getDouble("balance");
                        balanceAmount.setText(" $ " + String.format("%.2f", balance));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving balance.", "Error", JOptionPane.ERROR_MESSAGE);
        }
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
            UserDashBoard dashboard = new UserDashBoard("someAccountNumber"); // Replace "someAccountNumber" with actual account number for testing
            dashboard.setVisible(true);
        });
    }
}
