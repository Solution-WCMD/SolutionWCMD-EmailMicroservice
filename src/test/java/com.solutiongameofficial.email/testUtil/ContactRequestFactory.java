package com.solutiongameofficial.email.testutil;

import com.solutiongameofficial.email.ContactRequest;

/**
 * Factory for creating ContactRequest instances in tests.
 * Provides valid defaults and builder-style methods for specific field overrides.
 */
public final class ContactRequestFactory {

    public static final String VALID_NAME = "John Doe";
    public static final String VALID_EMAIL = "john@example.com";
    public static final String VALID_TITLE = "Valid Title Here";
    public static final String VALID_MESSAGE = "This message is definitely longer than thirty characters for validation purposes.";

    private ContactRequestFactory() {
    }

    public static ContactRequest valid() {
        return new ContactRequest(VALID_NAME, VALID_EMAIL, VALID_TITLE, VALID_MESSAGE);
    }

    public static ContactRequest withTitle(String title) {
        return new ContactRequest(VALID_NAME, VALID_EMAIL, title, VALID_MESSAGE);
    }

    public static ContactRequest withMessage(String message) {
        return new ContactRequest(VALID_NAME, VALID_EMAIL, VALID_TITLE, message);
    }

    public static ContactRequest withEmail(String email) {
        return new ContactRequest(VALID_NAME, email, VALID_TITLE, VALID_MESSAGE);
    }

    public static ContactRequest withName(String name) {
        return new ContactRequest(name, VALID_EMAIL, VALID_TITLE, VALID_MESSAGE);
    }
}
