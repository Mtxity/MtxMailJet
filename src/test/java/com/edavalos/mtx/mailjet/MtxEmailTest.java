package com.edavalos.mtx.mailjet;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MtxEmailTest {

    @Test
    void testBasicEmailCreation() {
        MtxEmail email = new MtxEmail("sender@example.com", "recipient@example.com", "Test Subject", "Test Body");

        assertEquals("sender@example.com", email.getSender());
        assertEquals("recipient@example.com", email.getRecipient());
        assertEquals(List.of("recipient@example.com"), email.getRecipients());
        assertEquals("Test Subject", email.getSubject());
        assertEquals("Test Body", email.getBody());
        assertFalse(email.isHtml());
        assertTrue(email.getCc().isEmpty());
        assertTrue(email.getBcc().isEmpty());
    }

    @Test
    void testBuilderWithMultipleRecipientsCcBccHtml() {
        MtxEmail email = MtxEmail.builder()
                .from("sender@example.com")
                .to("to1@example.com")
                .recipient("to2@example.com")
                .cc(List.of("cc1@example.com"))
                .cc("cc2@example.com")
                .bcc("bcc1@example.com")
                .subject("HTML Subject")
                .htmlBody("<p>Hello</p>")
                .build();

        assertEquals("sender@example.com", email.getSender());
        assertEquals(List.of("to1@example.com", "to2@example.com"), email.getRecipients());
        assertEquals(List.of("cc1@example.com", "cc2@example.com"), email.getCc());
        assertEquals(List.of("bcc1@example.com"), email.getBcc());
        assertEquals("HTML Subject", email.getSubject());
        assertEquals("<p>Hello</p>", email.getBody());
        assertTrue(email.isHtml());
    }

    @Test
    void testValidationFailureMissingSender() {
        assertThrows(IllegalArgumentException.class, () -> {
            MtxEmail.builder()
                    .recipient("to@example.com")
                    .subject("Sub")
                    .body("Body")
                    .build();
        });
    }

    @Test
    void testValidationFailureMissingRecipient() {
        assertThrows(IllegalArgumentException.class, () -> {
            MtxEmail.builder()
                    .sender("sender@example.com")
                    .subject("Sub")
                    .body("Body")
                    .build();
        });
    }

    @Test
    void testValidationFailureCrlfHeaderInjection() {
        assertThrows(IllegalArgumentException.class, () -> {
            MtxEmail.builder()
                    .sender("sender@example.com\r\nBCC: attacker@example.com")
                    .recipient("to@example.com")
                    .subject("Sub")
                    .body("Body")
                    .build();
        });
    }

    @Test
    void testValidationFailureInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            MtxEmail.builder()
                    .sender("invalid-email")
                    .recipient("to@example.com")
                    .subject("Sub")
                    .body("Body")
                    .build();
        });
    }
}
