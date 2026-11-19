# MtxMailJet

`MtxMailJet` is a lightweight, modern, and robust Java email-sending utility library built on top of Jakarta Mail (`org.eclipse.angus:jakarta.mail`). It provides a clean fluent API for constructing emails, asynchronous sending via thread pools, support for plain text and HTML bodies, email attachments, and flexible configuration options via environment variables or property files.

---

## Features

- **Fluent Builder API**: Easily construct emails with senders, recipients, CC, BCC, subjects, bodies, and attachments.
- **Asynchronous Sending**: Send emails asynchronously using managed `CompletableFuture` and thread pools.
- **Rich Content**: Support for both plain text and HTML email formats.
- **Attachments Support**: Attach files from byte arrays, `File`, or `Path` objects with automatic or explicit content types.
- **Flexible Configuration**: Load SMTP settings programmatically, via environment variables (`SMTP_HOST`, `SMTP_PORT`, etc.), or from Java `.properties` files.
- **Robust Security**: Header injection protection (CRLF checks) and strict validation.
- **Test-Friendly**: Seamless local testing using GreenMail embedded SMTP servers or Mailpit/MailHog.

---

## Requirements

- **Java**: JDK 27 or higher
- **Build Tool**: Gradle (with Gradle Wrapper included)

---

## Installation & Importing

### As a Local Project Dependency
If you are consuming this library within another local project using Gradle:

```groovy
dependencies {
    implementation(project(":MtxMailJet"))
}
```

Or by publishing to local maven (`./gradlew publishToMavenLocal`) and adding:

```groovy
dependencies {
    implementation("com.edavalos.mtx.mailjet:MtxMailJet:0.1.0-SNAPSHOT")
}
```

---

## Configuration

`MtxMailJet` supports configuration through three methods:

### 1. Environment Variables
You can configure your SMTP settings using environment variables:
- `SMTP_HOST` (e.g., `in-v3.mailjet.com`)
- `SMTP_PORT` (e.g., `587`)
- `SMTP_USER` or `SMTP_USERNAME`
- `SMTP_PASS` or `SMTP_PASSWORD`
- `SMTP_SSL` (`true`/`false`)
- `SMTP_STARTTLS` (`true`/`false`, default `true`)
- `SMTP_THREAD_POOL_SIZE` (default `5`)

Load via:
```java
MtxEmailConfig config = MtxEmailConfig.fromEnv();
```

### 2. Properties File (`mail.properties`)
You can copy `mail.properties.example` to `mail.properties` and load it:

```properties
smtp.host=in-v3.mailjet.com
smtp.port=587
smtp.username=your_api_key
smtp.password=your_secret_key
smtp.ssl=false
smtp.starttls=true
smtp.threadPoolSize=5
```

Load via:
```java
MtxEmailConfig config = MtxEmailConfig.fromFile(Path.of("mail.properties"));
```

### 3. Programmatic Builder
```java
MtxEmailConfig config = MtxEmailConfig.builder()
        .host("smtp.example.com")
        .port(587)
        .username("user")
        .password("pass")
        .startTls(true)
        .build();
```

---

## Usage Examples

### Sending a Basic Plain Text Email
```java
import com.edavalos.mtx.mailjet.*;
import com.edavalos.mtx.mailjet.config.MtxEmailConfig;

MtxEmailConfig config = MtxEmailConfig.fromEnv();

try (MtxMailJet mailJet = new MtxMailJet(config)) {
    MtxEmail email = new MtxEmail(
            "sender@example.com",
            "recipient@example.com",
            "Hello",
            "This is a test email."
    );

    mailJet.send(email).join(); // Blocks until sent asynchronously
}
```

### Sending an HTML Email with CC, BCC, and Attachments
```java
byte[] pdfBytes = ...; // Your file data

MtxEmail email = MtxEmail.builder()
        .from("sender@example.com")
        .to("recipient@example.com")
        .cc("manager@example.com")
        .bcc("audit@example.com")
        .subject("Invoice #1234")
        .htmlBody("<h1>Invoice</h1><p>Please find your invoice attached.</p>")
        .attachment("invoice.pdf", pdfBytes, "application/pdf")
        .build();

try (MtxMailJet mailJet = new MtxMailJet(config)) {
    mailJet.send(email).join();
}
```

---

## Testing Locally

### 1. Unit & Integration Tests (GreenMail)
The project includes automated tests using GreenMail (an in-memory SMTP test server). Run all tests locally with:
```bash
./gradlew test
```

### 2. Live Testing with Mailjet / SMTP
To test sending a live email to your inbox:
1. Copy `mail.properties.example` to `mail.properties` and fill in your real SMTP credentials and recipient email.
2. Run the integration live test:
```bash
./gradlew test --tests com.edavalos.mtx.mailjet.MtxMailJetIntegrationTest.testSendLiveEmailIfConfigured -i
```
