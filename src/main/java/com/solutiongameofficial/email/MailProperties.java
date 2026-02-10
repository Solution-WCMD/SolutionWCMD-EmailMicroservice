package com.solutiongameofficial.email;

import java.util.Properties;

public class MailProperties extends Properties {

    public MailProperties() {
        put("mail.smtp.auth", "true");
        put("mail.smtp.starttls.enable", "true");
        put("mail.smtp.host", "smtp.gmail.com");
        put("mail.smtp.port", "587");
    }
}
