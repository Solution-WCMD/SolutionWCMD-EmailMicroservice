package com.solutiongameofficial.email;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ContactRequest")
class ContactRequestTest {

    @Test
    @DisplayName("stores all fields correctly via accessors")
    void storesAllFieldsCorrectly() {
        ContactRequest request = new ContactRequest("John", "john@test.com", "Title", "Message");

        assertAll(
                () -> assertEquals("John", request.name()),
                () -> assertEquals("john@test.com", request.email()),
                () -> assertEquals("Title", request.title()),
                () -> assertEquals("Message", request.message())
        );
    }

    @Test
    @DisplayName("allows null values (validation delegated to handler)")
    void allowsNullValues() {
        ContactRequest request = new ContactRequest(null, null, null, null);

        assertAll(
                () -> assertNull(request.name()),
                () -> assertNull(request.email()),
                () -> assertNull(request.title()),
                () -> assertNull(request.message())
        );
    }

    @Test
    @DisplayName("two requests with same values are equal")
    void implementsValueEquality() {
        ContactRequest first = new ContactRequest("a", "b", "c", "d");
        ContactRequest second = new ContactRequest("a", "b", "c", "d");

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    @DisplayName("two requests with different values are not equal")
    void detectsDifferentValues() {
        ContactRequest first = new ContactRequest("a", "b", "c", "d");
        ContactRequest second = new ContactRequest("a", "b", "c", "different");

        assertNotEquals(first, second);
    }

    @Test
    @DisplayName("toString contains all field values")
    void toStringContainsAllFields() {
        ContactRequest request = new ContactRequest("name", "email", "title", "message");
        String result = request.toString();

        assertAll(
                () -> assertTrue(result.contains("name")),
                () -> assertTrue(result.contains("email")),
                () -> assertTrue(result.contains("title")),
                () -> assertTrue(result.contains("message"))
        );
    }
}
