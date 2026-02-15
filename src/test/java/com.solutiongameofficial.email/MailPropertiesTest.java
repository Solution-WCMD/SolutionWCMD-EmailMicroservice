package com.solutiongameofficial.email;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("MailProperties")
class MailPropertiesTest {

    private MailProperties properties;

    @BeforeEach
    void setUp() {
        properties = new MailProperties();
    }

    @Test
    @DisplayName("configures all required Gmail SMTP settings")
    void configuresAllRequiredSettings() {
        assertAll(
                () -> assertEquals("true", properties.getProperty("mail.smtp.auth"),
                        "SMTP authentication should be enabled"),
                () -> assertEquals("true", properties.getProperty("mail.smtp.starttls.enable"),
                        "STARTTLS should be enabled for secure connection"),
                () -> assertEquals("smtp.gmail.com", properties.getProperty("mail.smtp.host"),
                        "Should use Gmail SMTP host"),
                () -> assertEquals("587", properties.getProperty("mail.smtp.port"),
                        "Should use port 587 for TLS")
        );
    }

    @Test
    @DisplayName("contains exactly 4 properties")
    void containsExpectedPropertyCount() {
        assertEquals(4, properties.size(),
                "Should contain only the required SMTP properties");
    }
}
