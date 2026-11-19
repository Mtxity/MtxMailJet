package com.edavalos.mtx.mailjet.config;

public final class MtxEmailConfig {
    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final boolean ssl;
    private final boolean startTls;
    private final int threadPoolSize;

    private MtxEmailConfig(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.username = builder.username;
        this.password = builder.password;
        this.ssl = builder.ssl;
        this.startTls = builder.startTls;
        this.threadPoolSize = builder.threadPoolSize;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isSsl() {
        return ssl;
    }

    public boolean isStartTls() {
        return startTls;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static MtxEmailConfig fromEnv() {
        Builder builder = builder();
        String host = System.getenv("SMTP_HOST");
        if (host != null) builder.host(host);

        String port = System.getenv("SMTP_PORT");
        if (port != null && !port.isBlank()) {
            builder.port(Integer.parseInt(port.trim()));
        }

        String username = System.getenv("SMTP_USER");
        if (username == null) username = System.getenv("SMTP_USERNAME");
        if (username != null) builder.username(username);

        String password = System.getenv("SMTP_PASS");
        if (password == null) password = System.getenv("SMTP_PASSWORD");
        if (password != null) builder.password(password);

        String ssl = System.getenv("SMTP_SSL");
        if (ssl != null) builder.ssl(Boolean.parseBoolean(ssl));

        String startTls = System.getenv("SMTP_STARTTLS");
        if (startTls != null) builder.startTls(Boolean.parseBoolean(startTls));

        String threadPoolSize = System.getenv("SMTP_THREAD_POOL_SIZE");
        if (threadPoolSize != null && !threadPoolSize.isBlank()) {
            builder.threadPoolSize(Integer.parseInt(threadPoolSize.trim()));
        }

        return builder.build();
    }

    public static MtxEmailConfig fromProperties(java.util.Properties props) {
        Builder builder = builder();
        String host = props.getProperty("smtp.host", props.getProperty("host"));
        if (host != null) builder.host(host);

        String port = props.getProperty("smtp.port", props.getProperty("port"));
        if (port != null && !port.isBlank()) {
            builder.port(Integer.parseInt(port.trim()));
        }

        String username = props.getProperty("smtp.username", props.getProperty("smtp.user", props.getProperty("username")));
        if (username != null) builder.username(username);

        String password = props.getProperty("smtp.password", props.getProperty("smtp.pass", props.getProperty("password")));
        if (password != null) builder.password(password);

        String ssl = props.getProperty("smtp.ssl", props.getProperty("ssl"));
        if (ssl != null) builder.ssl(Boolean.parseBoolean(ssl));

        String startTls = props.getProperty("smtp.starttls", props.getProperty("starttls"));
        if (startTls != null) builder.startTls(Boolean.parseBoolean(startTls));

        String threadPoolSize = props.getProperty("smtp.threadPoolSize", props.getProperty("threadPoolSize"));
        if (threadPoolSize != null && !threadPoolSize.isBlank()) {
            builder.threadPoolSize(Integer.parseInt(threadPoolSize.trim()));
        }

        return builder.build();
    }

    public static MtxEmailConfig fromFile(java.nio.file.Path path) throws java.io.IOException {
        java.util.Properties props = new java.util.Properties();
        try (java.io.Reader reader = java.nio.file.Files.newBufferedReader(path)) {
            props.load(reader);
        }
        return fromProperties(props);
    }

    public static class Builder {
        private String host;
        private int port = 587;
        private String username;
        private String password;
        private boolean ssl = false;
        private boolean startTls = true;
        private int threadPoolSize = 5;

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder ssl(boolean ssl) {
            this.ssl = ssl;
            return this;
        }

        public Builder startTls(boolean startTls) {
            this.startTls = startTls;
            return this;
        }

        public Builder threadPoolSize(int threadPoolSize) {
            this.threadPoolSize = threadPoolSize;
            return this;
        }

        public MtxEmailConfig build() {
            if (host == null || host.isBlank()) {
                throw new IllegalArgumentException(
                        "SMTP host is required"
                );
            }

            if (port < 1 || port > 65535) {
                throw new IllegalArgumentException(
                        "Invalid SMTP port"
                );
            }

            if (threadPoolSize < 1) {
                throw new IllegalArgumentException(
                        "Thread pool size must be positive"
                );
            }

            if (ssl && startTls) {
                throw new IllegalArgumentException(
                        "SSL and STARTTLS cannot both be enabled"
                );
            }

            return new MtxEmailConfig(this);
        }
    }
}

