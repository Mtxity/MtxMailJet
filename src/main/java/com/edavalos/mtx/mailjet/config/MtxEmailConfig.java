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

