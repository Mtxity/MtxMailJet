package com.edavalos.mtx.mailjet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an email message with sender, recipients, subject, body, and optional CC/BCC.
 */
public class MtxEmail {
    private final String sender;
    private final List<String> recipients;
    private final List<String> cc;
    private final List<String> bcc;
    private final String subject;
    private final String body;
    private final boolean html;
    private final List<Attachment> attachments;

    /**
     * Constructs a new plain-text {@code MtxEmail}.
     *
     * @param sender the sender email address
     * @param recipient the recipient email address
     * @param subject the email subject
     * @param body the email body content
     * @throws IllegalArgumentException if sender or recipient is null/blank or contains CRLF characters
     */
    public MtxEmail(
            String sender,
            String recipient,
            String subject,
            String body
    ) {
        this(
                builder()
                .sender(sender)
                .recipient(recipient)
                .subject(subject)
                .body(body)
        );
    }

    private MtxEmail(Builder builder) {
        this.sender = builder.sender;
        this.recipients = List.copyOf(builder.recipients);
        this.cc = List.copyOf(builder.cc);
        this.bcc = List.copyOf(builder.bcc);
        this.subject = builder.subject != null ? builder.subject : "";
        this.body = builder.body != null ? builder.body : "";
        this.html = builder.html;
        this.attachments = List.copyOf(builder.attachments);
    }

    /**
     * @return the sender email address
     */
    public String getSender() {
        return sender;
    }

    /**
     * @return the first recipient email address
     */
    public String getRecipient() {
        return recipients.isEmpty() ? null : recipients.getFirst();
    }

    /**
     * @return an unmodifiable list of recipient email addresses
     */
    public List<String> getRecipients() {
        return recipients;
    }

    /**
     * @return an unmodifiable list of CC recipient email addresses
     */
    public List<String> getCc() {
        return cc;
    }

    /**
     * @return an unmodifiable list of BCC recipient email addresses
     */
    public List<String> getBcc() {
        return bcc;
    }

    /**
     * @return the email subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @return the email body content
     */
    public String getBody() {
        return body;
    }

    /**
     * @return {@code true} if the body is HTML, {@code false} if plain text
     */
    public boolean isHtml() {
        return html;
    }

    /**
     * @return an unmodifiable list of attachments
     */
    public List<Attachment> getAttachments() {
        return attachments;
    }

    /**
     * @return a new {@code Builder} for constructing an {@code MtxEmail}
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MtxEmail mtxEmail)) return false;
        return html == mtxEmail.html &&
               Objects.equals(sender, mtxEmail.sender) &&
               Objects.equals(recipients, mtxEmail.recipients) &&
               Objects.equals(cc, mtxEmail.cc) &&
               Objects.equals(bcc, mtxEmail.bcc) &&
               Objects.equals(subject, mtxEmail.subject) &&
               Objects.equals(body, mtxEmail.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, recipients, cc, bcc, subject, body, html);
    }

    @Override
    public String toString() {
        return "MtxEmail{" +
                "sender='" + sender + '\'' +
                ", recipients=" + recipients +
                ", cc=" + cc +
                ", bcc=" + bcc +
                ", subject='" + subject + '\'' +
                ", html=" + html +
                '}';
    }

    /**
     * Builder for constructing instances of {@link MtxEmail}.
     */
    public static class Builder {
        private String sender;
        private final List<String> recipients = new ArrayList<>();
        private final List<String> cc = new ArrayList<>();
        private final List<String> bcc = new ArrayList<>();
        private String subject = "";
        private String body = "";
        private boolean html = false;
        private final List<Attachment> attachments = new ArrayList<>();

        /**
         * Sets the sender address.
         *
         * @param sender the sender email address
         * @return this builder
         */
        public Builder sender(String sender) {
            this.sender = sender;
            return this;
        }

        /**
         * Alias for {@link #sender(String)}.
         *
         * @param from the sender email address
         * @return this builder
         */
        public Builder from(String from) {
            return sender(from);
        }

        /**
         * Adds a single recipient.
         *
         * @param recipient the recipient email address
         * @return this builder
         */
        public Builder recipient(String recipient) {
            if (recipient != null && !recipient.isBlank()) {
                this.recipients.add(recipient.trim());
            }
            return this;
        }

        /**
         * Alias for {@link #recipient(String)}.
         *
         * @param to the recipient email address
         * @return this builder
         */
        public Builder to(String to) {
            return recipient(to);
        }

        /**
         * Adds multiple recipients.
         *
         * @param recipients the recipient email addresses
         * @return this builder
         */
        public Builder recipients(List<String> recipients) {
            if (recipients != null) {
                for (String r : recipients) {
                    recipient(r);
                }
            }
            return this;
        }

        /**
         * Adds multiple recipients.
         *
         * @param recipients the recipient email addresses
         * @return this builder
         */
        public Builder recipients(String... recipients) {
            if (recipients != null) {
                for (String r : recipients) {
                    recipient(r);
                }
            }
            return this;
        }

        /**
         * Adds a CC recipient.
         *
         * @param cc the CC recipient email address
         * @return this builder
         */
        public Builder cc(String cc) {
            if (cc != null && !cc.isBlank()) {
                this.cc.add(cc.trim());
            }
            return this;
        }

        /**
         * Adds multiple CC recipients.
         *
         * @param ccRecipients the CC recipient email addresses
         * @return this builder
         */
        public Builder cc(List<String> ccRecipients) {
            if (ccRecipients != null) {
                for (String c : ccRecipients) {
                    cc(c);
                }
            }
            return this;
        }

        /**
         * Adds a BCC recipient.
         *
         * @param bcc the BCC recipient email address
         * @return this builder
         */
        public Builder bcc(String bcc) {
            if (bcc != null && !bcc.isBlank()) {
                this.bcc.add(bcc.trim());
            }
            return this;
        }

        /**
         * Adds multiple BCC recipients.
         *
         * @param bccRecipients the BCC recipient email addresses
         * @return this builder
         */
        public Builder bcc(List<String> bccRecipients) {
            if (bccRecipients != null) {
                for (String b : bccRecipients) {
                    bcc(b);
                }
            }
            return this;
        }

        /**
         * Sets the subject of the email.
         *
         * @param subject the subject
         * @return this builder
         */
        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        /**
         * Sets the body content of the email.
         *
         * @param body the body content
         * @return this builder
         */
        public Builder body(String body) {
            this.body = body;
            return this;
        }

        /**
         * Sets whether the body content is HTML.
         *
         * @param html {@code true} if HTML, {@code false} if plain text
         * @return this builder
         */
        public Builder html(boolean html) {
            this.html = html;
            return this;
        }

        /**
         * Sets HTML body content and sets {@code html} flag to {@code true}.
         *
         * @param htmlBody the HTML body content
         * @return this builder
         */
        public Builder htmlBody(String htmlBody) {
            this.body = htmlBody;
            this.html = true;
            return this;
        }

        public Builder attachment(Attachment attachment) {
            if (attachment != null) {
                this.attachments.add(attachment);
            }
            return this;
        }

        public Builder attachment(String fileName, byte[] data) {
            return attachment(new Attachment(fileName, data, null));
        }

        public Builder attachment(String fileName, byte[] data, String contentType) {
            return attachment(new Attachment(fileName, data, contentType));
        }

        public Builder attachment(java.io.File file) {
            if (file == null || !file.exists() || !file.isFile()) {
                throw new IllegalArgumentException("Invalid file for attachment");
            }
            try {
                byte[] data = java.nio.file.Files.readAllBytes(file.toPath());
                String contentType = java.nio.file.Files.probeContentType(file.toPath());
                return attachment(new Attachment(file.getName(), data, contentType));
            } catch (java.io.IOException e) {
                throw new RuntimeException("Failed to read attachment file: " + file.getName(), e);
            }
        }

        public Builder attachment(java.nio.file.Path path) {
            if (path == null || !java.nio.file.Files.exists(path) || !java.nio.file.Files.isRegularFile(path)) {
                throw new IllegalArgumentException("Invalid file path for attachment");
            }
            try {
                byte[] data = java.nio.file.Files.readAllBytes(path);
                String contentType = java.nio.file.Files.probeContentType(path);
                String fileName = path.getFileName() != null ? path.getFileName().toString() : "attachment";
                return attachment(new Attachment(fileName, data, contentType));
            } catch (java.io.IOException e) {
                throw new RuntimeException("Failed to read attachment path: " + path, e);
            }
        }

        public Builder attachments(List<Attachment> attachments) {
            if (attachments != null) {
                this.attachments.addAll(attachments);
            }
            return this;
        }

        /**
         * Builds and returns a validated {@link MtxEmail}.
         *
         * @return the constructed {@code MtxEmail}
         * @throws IllegalArgumentException if validation fails
         */
        public MtxEmail build() {
            if (sender == null || sender.isBlank()) {
                throw new IllegalArgumentException("Sender is required");
            }
            validateNoCrlf("Sender", sender);
            validateEmailAddress("Sender", sender);

            if (recipients.isEmpty()) {
                throw new IllegalArgumentException("At least one recipient is required");
            }

            for (String r : recipients) {
                validateNoCrlf("Recipient", r);
                validateEmailAddress("Recipient", r);
            }

            for (String c : cc) {
                validateNoCrlf("CC", c);
                validateEmailAddress("CC", c);
            }

            for (String b : bcc) {
                validateNoCrlf("BCC", b);
                validateEmailAddress("BCC", b);
            }

            if (subject != null) {
                validateNoCrlf("Subject", subject);
            }

            return new MtxEmail(this);
        }

        private static void validateNoCrlf(String fieldName, String value) {
            if (value != null && (value.indexOf('\r') >= 0 || value.indexOf('\n') >= 0)) {
                throw new IllegalArgumentException(fieldName + " must not contain CRLF characters");
            }
        }

        private static void validateEmailAddress(String fieldName, String email) {
            String trimmed = email.trim();
            if (!trimmed.contains("@") || trimmed.startsWith("@") || trimmed.endsWith("@")) {
                throw new IllegalArgumentException("Invalid email address for " + fieldName + ": " + email);
            }
        }
    }

    /**
     * Represents an email attachment.
     */
    public static class Attachment {
        private final String fileName;
        private final byte[] data;
        private final String contentType;

        public Attachment(String fileName, byte[] data, String contentType) {
            if (fileName == null || fileName.isBlank()) {
                throw new IllegalArgumentException("Attachment filename is required");
            }
            if (data == null) {
                throw new IllegalArgumentException("Attachment data cannot be null");
            }
            this.fileName = fileName.trim();
            this.data = data.clone();
            this.contentType = contentType != null && !contentType.isBlank() ? contentType.trim() : "application/octet-stream";
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getData() {
            return data.clone();
        }

        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Attachment that)) return false;
            return java.util.Arrays.equals(data, that.data) &&
                   Objects.equals(fileName, that.fileName) &&
                   Objects.equals(contentType, that.contentType);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(fileName, contentType);
            result = 31 * result + java.util.Arrays.hashCode(data);
            return result;
        }
    }
}
