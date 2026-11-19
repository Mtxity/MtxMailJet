package com.edavalos.mtx.mailjet.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MtxEmailConfigTest {

    @Test
    void testValidConfig() {
        MtxEmailConfig config = MtxEmailConfig.builder()
                .host("smtp.example.com")
                .port(465)
                .username("user")
                .password("pass")
                .ssl(true)
                .startTls(false)
                .threadPoolSize(10)
                .build();

        assertEquals("smtp.example.com", config.getHost());
        assertEquals(465, config.getPort());
        assertEquals("user", config.getUsername());
        assertEquals("pass", config.getPassword());
        assertTrue(config.isSsl());
        assertFalse(config.isStartTls());
        assertEquals(10, config.getThreadPoolSize());
    }

    @Test
    void testInvalidSslAndStartTls() {
        assertThrows(IllegalArgumentException.class, () -> {
            MtxEmailConfig.builder()
                    .host("smtp.example.com")
                    .ssl(true)
                    .startTls(true)
                    .build();
        });
    }

    @Test
    void testInvalidPort() {
        assertThrows(IllegalArgumentException.class, () -> {
            MtxEmailConfig.builder()
                    .host("smtp.example.com")
                    .port(70000)
                    .build();
        });
    }

    @Test
    void testMissingHost() {
        assertThrows(IllegalArgumentException.class, () -> {
            MtxEmailConfig.builder()
                    .host("")
                    .build();
        });
    }
}
