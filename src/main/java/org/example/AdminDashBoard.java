package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class AdminDashBoard extends JFrame {

    public AdminDashBoard() {
        // Frame setup
        setTitle("RapidPay - Admin Dashboard");
        setSize(800, 600); // Frame size 800x600
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        // Top panel (Header)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 123, 255));
        headerPanel.setPreferredSize(new Dimension(800, 70));

        JLabel logoLabel = new JLabel("RapidPay", JLabel.LEFT);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome, Admin", JLabel.RIGHT);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));

        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(welcomeLabel, BorderLayout.EAST);

        // Sidebar panel (Navigation)
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new GridLayout(6, 1, 10, 10));
        sidebarPanel.setPreferredSize(new Dimension(180, 0));
        sidebarPanel.setBackground(new Color(52, 58, 64));

        String[] navItems = {"Dashboard", "Manage Users", "Transactions", "Reports", "Settings", "Logout"};
        for (String item : navItems) {
            JButton button = new JButton(item);
            button.setFont(new Font("Arial", Font.PLAIN, 14));
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(52, 58, 64));
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            sidebarPanel.add(button);

            // Add hover effect
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(73, 80, 87));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(52, 58, 64));
                }
            });

            // Add action listener for Logout button
            if ("Logout".equals(item)) {
                button.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to log out?",
                            "Logout Confirmation",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        System.exit(0); // Exit the application
                    }
                });
            }
        }

        // Main panel (Content)
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BorderLayout());

        JLabel dashboardTitle = new JLabel("Admin Dashboard", JLabel.LEFT);
        dashboardTitle.setFont(new Font("Arial", Font.BOLD, 20));
        dashboardTitle.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Stats panel (Landscape-oriented small boxes)
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        statsPanel.setBackground(Color.WHITE);

        String[] stats = {"Total Users: 1500", "Daily Transactions: 250", "Active Users: 1350"};
        Color[] statColors = {new Color(40, 167, 69), new Color(255, 193, 7), new Color(23, 162, 184)};

        for (int i = 0; i < stats.length; i++) {
            JPanel statCard = new JPanel();
            statCard.setBackground(statColors[i]);
            statCard.setLayout(new BorderLayout());
            statCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            statCard.setPreferredSize(new Dimension(180, 80)); // Smaller boxes

            JLabel statLabel = new JLabel(stats[i], JLabel.CENTER);
            statLabel.setFont(new Font("Arial", Font.BOLD, 14));
            statLabel.setForeground(Color.WHITE);

            statCard.add(statLabel, BorderLayout.CENTER);
            statsPanel.add(statCard);
        }

        // Create chart panel
        JPanel chartPanel = createChartPanel();

        // Add components to main panel
        mainPanel.add(dashboardTitle, BorderLayout.NORTH);
        mainPanel.add(statsPanel, BorderLayout.CENTER);
        mainPanel.add(chartPanel, BorderLayout.EAST); // Place the chart on the right side

        // Add panels to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(sidebarPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }

    // Create the chart panel using JFreeChart
    private JPanel createChartPanel() {
        // Create a dataset for the chart
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Add data for the chart (example data for user transactions)
        dataset.addValue(25, "Transactions", "Day 1");
        dataset.addValue(40, "Transactions", "Day 2");
        dataset.addValue(30, "Transactions", "Day 3");
        dataset.addValue(50, "Transactions", "Day 4");

        // Create the chart
        JFreeChart chart = ChartFactory.createLineChart(
                "User Transactions Over Time", // chart title
                "Time (Days)",                  // X-axis label
                "Transactions",                 // Y-axis label
                dataset,                        // dataset
                PlotOrientation.VERTICAL,       // chart orientation
                true,                           // include legend
                true,                           // tooltips
                false                           // URLs
        );

        // Create a panel to display the chart
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300)); // Adjust size of the chart panel
        return chartPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminDashBoard adminDashBoard = new AdminDashBoard();
            adminDashBoard.setVisible(true);
        });
    }
}
