package com.cartas.jaktani.controller;

import com.cartas.jaktani.exceptions.ResourceNotFoundException;
import com.cartas.jaktani.jwt.JwtUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {
    static Logger logger = LoggerFactory.getLogger(SendMail.class);

    public static void sentOTPToEmail(String email, String messageBody, String messageSubject) throws Exception {

        // Recipient's email ID needs to be mentioned.
        String to = email;

        // Sender's email ID needs to be mentioned
        String from = "smtp.jaktani@gmail.com";

        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, "smtpJaktani!");
            }
        });

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(messageSubject);

            // Now set the actual message
            message.setText(messageBody);

//            // Send the actual HTML message.
//            message.setContent(
//                    "<h1>This is actual message embedded in HTML tags</h1>",
//                    "text/html");

//            System.out.println("sending...");
            // Send message
            Transport.send(message);
            logger.debug("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
            logger.debug("Failed send message");
            throw new ResourceNotFoundException("Failed send message");
        }

    }

}
