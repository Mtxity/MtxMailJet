package com.edavalos.mtx.mailjet.transport;

import com.edavalos.mtx.mailjet.MtxEmail;
import com.edavalos.mtx.mailjet.config.MtxEmailConfig;

public class MtxSmtpEmailTransport implements MtxEmailTransport {
    private final MtxEmailConfig config;

    public MtxSmtpEmailTransport(MtxEmailConfig config) {
        this.config = config;
    }

    @Override
    public void send(MtxEmail email) {
        // Connect to SMTP server.
        // Authenticate.
        // Construct MIME message.
        // Send email.
    }
}
