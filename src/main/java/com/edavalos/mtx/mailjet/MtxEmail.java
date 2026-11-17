package com.edavalos.mtx.mailjet;

public class MtxEmail {
    private final String sender;
    private final String recipient;
    private final String subject;
    private final String body;

    public MtxEmail(
            String sender,
            String recipient,
            String subject,
            String body
    ) {
        this.sender = sender;
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
}
