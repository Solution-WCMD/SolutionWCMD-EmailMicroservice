package com.solutiongameofficial.email;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import io.javalin.security.BasicAuthCredentials;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.jetbrains.annotations.NotNull;

public record ContactHandler(BasicAuthCredentials credentials, String targetMail) implements Handler {

    @Override
    public void handle(@NotNull Context context) {
        ContactRequest request = context.bodyAsClass(ContactRequest.class);

        if (configureContextIfInvalidRequest(context, request)) {
            return;
        }

        String title = request.title();
        String message = request.message();
        String from = request.fromEmail();

        try {
            sendGmailSmtp(credentials.getUsername(), credentials.getPassword(), targetMail, title, message, from);
        } catch (MessagingException exception) {
            context.status(HttpStatus.INTERNAL_SERVER_ERROR);
            return;
        }

        context.status(HttpStatus.OK);
    }

    private void sendGmailSmtp(String smtpUser,
                               String smtpPass,
                               String toEmail,
                               String title,
                               String message,
                               String fromEmail) throws MessagingException {

        Session session = Session.getInstance(new MailProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUser, smtpPass);
            }
        });

        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(smtpUser));
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
        mimeMessage.setSubject("[Contact] " + title, "UTF-8");

        String body = """
                Title: %s
                From: %s
                
                %s
                """.formatted(title, fromEmail, message);

        mimeMessage.setText(body, "UTF-8");
        Transport.send(mimeMessage);
    }

    /**
     * @return true if the request was invalid
     */
    private boolean configureContextIfInvalidRequest(Context context, ContactRequest request) {
        if (request == null) {
            context.status(HttpStatus.BAD_REQUEST)
                    .result("{\"error\":\"request cannot be null\"}");
            return true;
        }

        if (isNullOrEmpty(request.title()) || isNullOrEmpty(request.message()) || isNullOrEmpty(request.fromEmail())) {
            context.status(HttpStatus.BAD_REQUEST)
                    .result("{\"error\":\"title, mail and message are required\"}");
            return true;
        }

        if (request.title().length() < 5 || request.title().length() > 100) {
            context.status(HttpStatus.BAD_REQUEST)
                    .result("{\"error\":\"title must be between 5 and 100 characters\"}");
            return true;
        }

        if (request.message().length() < 30 || request.message().length() > 2000) {
            context.status(HttpStatus.BAD_REQUEST)
                    .result("{\"error\":\"message must be between 30 and 2000 characters\"}");
            return true;
        }

        if (request.fromEmail().length() < 4 || request.fromEmail().length() > 254) {
            context.status(HttpStatus.BAD_REQUEST)
                    .result("{\"error\":\"fromEmail must be between 4 and 254 characters\"}");
            return true;
        }

        return false;
    }

    private boolean isNullOrEmpty(String string) {
        return string == null || string.isBlank();
    }
}
