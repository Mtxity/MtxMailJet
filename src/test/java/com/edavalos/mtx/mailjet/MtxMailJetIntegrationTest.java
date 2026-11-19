package com.edavalos.mtx.mailjet;

import com.edavalos.mtx.mailjet.config.MtxEmailConfig;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}
