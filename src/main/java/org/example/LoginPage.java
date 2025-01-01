package org.example;

import javax.swing.*;
import java.awt.*;

public class LoginPage {
    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("RapidPay");
        frame.setSize(800, 480); // Set the desired size of your window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the window on the screen

        // Prevent maximizing the window
        frame.setExtendedState(JFrame.NORMAL); // Ensures the frame is not maximized by default

        // Optionally, prevent the window from being resized
        frame.setResizable(false); // Set this to true if you want to allow resizing

        // Set up the card layout and main panelz
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // Add the login page, sign-up page, and forgot password page
        Login login = new Login();
        JPanel loginPanel = login.createLoginPage(mainPanel, cardLayout);
        mainPanel.add(loginPanel, "LoginPage");

        SignUp signUp = new SignUp();
        JPanel signUpPanel = signUp.createSignUpPage(mainPanel, cardLayout);
        mainPanel.add(signUpPanel, "SignUpPage");

        ForgetPassword forgetPassword = new ForgetPassword();
        JPanel forgetPasswordPanel = forgetPassword.createForgotPasswordPage(mainPanel, cardLayout);
        mainPanel.add(forgetPasswordPanel, "ForgotPasswordPage");


        // Set up the frame and make it visible
        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
