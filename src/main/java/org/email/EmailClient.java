package org.email;

/**
 * Created by Brent on 2017-03-02.
 */
import org.EmailVar;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * A utility class for sending e-mail messages
 * @author www.codejava.net
 *
 */
public class EmailClient {
    public static void sendEmail(String toAddress, String subject, String message)
            throws AddressException, MessagingException {

        String host = "smtp.gmail.com";
        String port = "587";

        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EmailVar.DEV_EMAIL_USERNAME, EmailVar.DEV_EMAIL_PASSWORD);
            }
        };

        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        Message msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress("noreply@tracker.com", "Tracker Email")); //username
        }catch(UnsupportedEncodingException uee){
            System.out.println("ERROR sending email - " + uee.getMessage());
        }
        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setText(message);

        // sends the e-mail
        Transport.send(msg);

    }
}