package com.solutiongameofficial.email;

import io.javalin.Javalin;
import io.javalin.security.BasicAuthCredentials;

public class Main {

    public static void main(String[] args) {
        String smtpUsername = fromEnvironment("MAIL_USERNAME");
        String smtpPassword = fromEnvironment("MAIL_PASSWORD");
        String targetMail = fromEnvironment("MAIL_CONTACT");
        int port = 587;

        BasicAuthCredentials credentials = new BasicAuthCredentials(smtpUsername, smtpPassword);

        createApplication(credentials, targetMail).start(port);
    }

    private static Javalin createApplication(BasicAuthCredentials credentials, String targetMail) {
        Javalin app = Javalin.create(config -> config.http.defaultContentType = "application/json");

        app.post("/contact/send", new ContactHandler(credentials, targetMail));

        return app;
    }

    private static String fromEnvironment(String key) {
        String environmentVariable = System.getenv(key);
        if (environmentVariable == null || environmentVariable.isBlank()) {
            throw new IllegalStateException("Missing environment variable: " + key);
        }
        return environmentVariable;
    }

}