package com.edavalos.mtx.mailjet;

import com.edavalos.mtx.mailjet.config.MtxEmailConfig;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class MtxMailJetIntegrationTest {
    private GreenMail greenMail;

    @BeforeEach
    void setUp() {
        greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.start();
        greenMail.setUser("user@example.com", "user@example.com", "password123");
    }

    @AfterEach
    void tearDown() {
        greenMail.stop();
    }

    @Test
    void testSendEmailWithGreenMail() throws Exception {
        MtxEmailConfig config = MtxEmailConfig.builder()
                .host("localhost")
                .port(greenMail.getSmtp().getPort())
                .username("user@example.com")
                .password("password123")
                .startTls(false)
                .ssl(false)
                .build();

        try (MtxMailJet mailJet = new MtxMailJet(config)) {
            MtxEmail email = MtxEmail.builder()
                    .from("user@example.com")
                    .to("recipient@example.com")
                    .subject("Integration Test Subject")
                    .body("Hello from GreenMail integration test!")
                    .build();

            mailJet.send(email).join();
        }

        assertTrue(greenMail.waitForIncomingEmail(5000, 1));
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
        assertEquals("Integration Test Subject", receivedMessages[0].getSubject());
        assertEquals("Hello from GreenMail integration test!", receivedMessages[0].getContent().toString().trim());
    }

    @Test
    void testConfigFromProperties() {
        Properties props = new Properties();
        props.setProperty("smtp.host", "smtp.mailjet.com");
        props.setProperty("smtp.port", "465");
        props.setProperty("smtp.username", "testuser");
        props.setProperty("smtp.password", "testpass");
        props.setProperty("smtp.ssl", "true");
        props.setProperty("smtp.starttls", "false");
        props.setProperty("smtp.threadPoolSize", "8");

        MtxEmailConfig config = MtxEmailConfig.fromProperties(props);
        assertEquals("smtp.mailjet.com", config.getHost());
        assertEquals(465, config.getPort());
        assertEquals("testuser", config.getUsername());
        assertEquals("testpass", config.getPassword());
        assertTrue(config.isSsl());
        assertEquals(8, config.getThreadPoolSize());
    }

    @Test
    void testSendLiveEmailIfConfigured() throws Exception {
        Path propsPath = Path.of("mail.properties");
        Properties props = new Properties();
        if (Files.exists(propsPath)) {
            try (var reader = Files.newBufferedReader(propsPath)) {
                props.load(reader);
            }
        }

        String host = props.getProperty("smtp.host", System.getenv("SMTP_HOST"));
        String username = props.getProperty("smtp.username", System.getenv("SMTP_USER"));
        if (username == null) {
            username = System.getenv("SMTP_USERNAME");
        }
        String password = props.getProperty("smtp.password", System.getenv("SMTP_PASS"));
        if (password == null) {
            password = System.getenv("SMTP_PASSWORD");
        }
        String mailFrom = props.getProperty("mail.from", System.getenv("MAIL_FROM"));
        String mailTo = props.getProperty("mail.to", System.getenv("MAIL_TO"));

        // Skip if not configured or still using template placeholders
        assumeTrue(host != null && !host.isBlank() && !host.contains("yourdomain"), "Skipping live email test: SMTP not configured");
        assumeTrue(username != null && !username.contains("your_mailjet_api_key"), "Skipping live email test: SMTP username not configured");
        assumeTrue(mailFrom != null && mailTo != null, "Skipping live email test: mail.from and mail.to required");

        MtxEmailConfig config;
        if (Files.exists(propsPath)) {
            config = MtxEmailConfig.fromFile(propsPath);
        } else {
            config = MtxEmailConfig.fromEnv();
        }

        try (MtxMailJet mailJet = new MtxMailJet(config)) {
            MtxEmail email = MtxEmail.builder()
                    .from(mailFrom)
                    .to(mailTo)
                    .subject("MtxMailJet Live Test Email")
                    .body("Hello! This is a test email sent from MtxMailJet integration test.")
                    .build();

            mailJet.send(email).join();
            System.out.println("Live email sent successfully to " + mailTo);
        }
    }
}
