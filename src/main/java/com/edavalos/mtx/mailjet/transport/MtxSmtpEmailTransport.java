package com.edavalos.mtx.mailjet.transport;

import com.edavalos.mtx.mailjet.MtxEmail;
import com.edavalos.mtx.mailjet.config.MtxEmailConfig;
import com.edavalos.mtx.mailjet.exception.MtxEmailException;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.List;
import java.util.Properties;

public class MtxSmtpEmailTransport implements MtxEmailTransport {
    private final MtxEmailConfig config;
    private final Session session;

    public MtxSmtpEmailTransport(MtxEmailConfig config) {
        this.config = config;
        this.session = createSession();
    }

    @Override
    public void send(MtxEmail email) {
        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(email.getSender()));

            List<String> recipients = email.getRecipients();
            InternetAddress[] toAddresses = new InternetAddress[recipients.size()];
            for (int i = 0; i < recipients.size(); i++) {
                toAddresses[i] = new InternetAddress(recipients.get(i));
            }
            message.setRecipients(Message.RecipientType.TO, toAddresses);

            List<String> cc = email.getCc();
            if (!cc.isEmpty()) {
                InternetAddress[] ccAddresses = new InternetAddress[cc.size()];
                for (int i = 0; i < cc.size(); i++) {
                    ccAddresses[i] = new InternetAddress(cc.get(i));
                }
                message.setRecipients(Message.RecipientType.CC, ccAddresses);
            }

            List<String> bcc = email.getBcc();
            if (!bcc.isEmpty()) {
                InternetAddress[] bccAddresses = new InternetAddress[bcc.size()];
                for (int i = 0; i < bcc.size(); i++) {
                    bccAddresses[i] = new InternetAddress(bcc.get(i));
                }
                message.setRecipients(Message.RecipientType.BCC, bccAddresses);
            }

            message.setSubject(email.getSubject(), "UTF-8");

            if (email.getAttachments().isEmpty()) {
                if (email.isHtml()) {
                    message.setContent(email.getBody(), "text/html; charset=utf-8");
                } else {
                    message.setText(email.getBody(), "UTF-8");
                }
            } else {
                jakarta.mail.internet.MimeMultipart multipart = new jakarta.mail.internet.MimeMultipart();

                jakarta.mail.internet.MimeBodyPart bodyPart = new jakarta.mail.internet.MimeBodyPart();
                if (email.isHtml()) {
                    bodyPart.setContent(email.getBody(), "text/html; charset=utf-8");
                } else {
                    bodyPart.setText(email.getBody(), "UTF-8");
                }
                multipart.addBodyPart(bodyPart);

                for (MtxEmail.Attachment att : email.getAttachments()) {
                    jakarta.mail.internet.MimeBodyPart attachmentPart = new jakarta.mail.internet.MimeBodyPart();
                    jakarta.mail.util.ByteArrayDataSource ds = new jakarta.mail.util.ByteArrayDataSource(att.getData(), att.getContentType());
                    attachmentPart.setDataHandler(new jakarta.activation.DataHandler(ds));
                    attachmentPart.setFileName(att.getFileName());
                    multipart.addBodyPart(attachmentPart);
                }

                message.setContent(multipart);
            }

            Transport.send(message);

        } catch (MessagingException exception) {
            throw new MtxEmailException("Failed to send email", exception);
        }
    }

    private Session createSession() {
        Properties properties = new Properties();

        properties.put("mail.smtp.host", config.getHost());
        properties.put("mail.smtp.port", String.valueOf(config.getPort()));
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", String.valueOf(config.isStartTls()));
        properties.put("mail.smtp.starttls.required", String.valueOf(config.isStartTls()));
        properties.put("mail.smtp.ssl.enable", String.valueOf(config.isSsl()));
        properties.put("mail.smtp.connectiontimeout", "10000");
        properties.put("mail.smtp.timeout", "10000");
        properties.put("mail.smtp.writetimeout", "10000");
        properties.put("mail.smtp.ssl.checkserveridentity", "true");

        return Session.getInstance(
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
    }
}
