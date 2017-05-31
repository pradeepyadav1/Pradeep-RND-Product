package com.fstm.process.user;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.fstm.data.access.impl.ShareHoldersDAOImpl;

/**
 * The Class SimpleMailService.
 */
public class SimpleMailService {

    /**
     * Sets the properties.
     *
     * @param prop the prop
     * @return the properties
     */
    private Properties setProperties(Properties prop) {
        prop.setProperty("mail.smtp.host", "smtp.gmail.com");
        prop.setProperty("mail.smtp.auth", "true");
        prop.setProperty("mail.smtp.port", "587");
        prop.setProperty("mail.smtp.starttls.enable", "true");
        return prop;
    }
    private static Logger logger = Logger.getLogger(SimpleMailService.class.getName());
    /**
     *  This method will send compose and send the message.
     *
     * @param from the from
     * @param to the to
     * @param subject the subject
     * @param body the body
     */
    public void sendMail(String from, String to, String subject, String body) {

        // Check the email address entered by user from the database
        boolean exist = true;
        if (exist) {
            // would allow to send email for password recovery
        } else {
            // would show error in this block and exit
        }

        // Get system properties
        Properties properties = System.getProperties();
        properties = setProperties(properties);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("financialsharetest@gmail.com", "impetus123");
            }
        });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Request to retain password");

            /** Here we generate the random password and send over the link when user click on the link then reset password screen would appear and */

            // Now set the actual message and append one link
            message.setText("Your new password is: " + body);

            // Send message
            Transport transport = session.getTransport("smtp");

            String host = "smtp.gmail.com";
            String pass = "impetus123";

            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            /* Transport.send(message); */
        } catch (MessagingException mex) {
        	logger.info("Exception in Sending email "+ mex.getMessage());
        }
    }

}
