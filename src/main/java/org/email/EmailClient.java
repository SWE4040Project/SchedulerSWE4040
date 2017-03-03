package org.email;

/**
 * Created by Brent on 2017-03-02.
 */
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
    public static void sendEmail(String host, String port,
                                 final String userName, final String password, String toAddress,
                                 String subject, String message) throws AddressException,
            MessagingException {

        host = "smtp.gmail.com";
        port = "587";
        toAddress = "brent.simmons@unb.ca";
        subject = "Hello World";
        message = "Sending email via JavaMail API";

        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("bsimmons367@gmail.com", "gottogo2");
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