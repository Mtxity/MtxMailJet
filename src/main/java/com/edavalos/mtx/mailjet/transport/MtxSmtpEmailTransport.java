package com.edavalos.mtx.mailjet.transport;

import com.edavalos.mtx.mailjet.MtxEmail;
import com.edavalos.mtx.mailjet.config.MtxEmailConfig;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class MtxSmtpEmailTransport implements MtxEmailTransport {
    private final MtxEmailConfig config;

    public MtxSmtpEmailTransport(MtxEmailConfig config) {
        this.config = config;
    }

    @Override
    public void send(MtxEmail email) {
        Properties properties = new Properties();

        properties.put("mail.smtp.host", config.getHost());
        properties.put("mail.smtp.port", config.getPort());
        properties.put("mail.smtp.auth", "true");

        properties.put(
                "mail.smtp.starttls.enable",
                String.valueOf(config.isStartTls())
        );

        properties.put(
                "mail.smtp.ssl.enable",
                String.valueOf(config.isSsl())
        );

        properties.put("mail.smtp.connectiontimeout", "10000");
        properties.put("mail.smtp.timeout", "10000");
        properties.put("mail.smtp.writetimeout", "10000");

        Session session = Session.getInstance(
                properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                config.getUsername(),
                                config.getPassword()
                        );
                    }
                }
        );

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(
                    new InternetAddress(email.getSender())
            );
            message.setRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(email.getRecipient())
            );

            message.setSubject(email.getSubject(), "UTF-8");
            message.setText(email.getBody(), "UTF-8");

            // Actually send the email
            Transport.send(message);

        } catch (MessagingException exception) {
            System.out.println("Error sending an email: " + exception.getMessage());
            throw new RuntimeException(
                    "Failed to send email",
                    exception
            );
        }
    }
}
