package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

class EmailSender {
    // Method to send an email
    public static void sendEmail(String recipient, String subject, String body) throws MessagingException {
        final String senderEmail = "rapidpay247@gmail.com"; // Replace with your email
        final String appPassword = "pshcngkobcfaymsi"; // Replace with your App Password

        // SMTP server configuration
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // Create a mail session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, appPassword);
            }
        });

        // Create a new email message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail)); // Sender's email
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient)); // Recipient's email
        message.setSubject(subject); // Email subject
        message.setText(body); // Email body

        // Send the email
        Transport.send(message);
    }
}
