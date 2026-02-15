package com.solutiongameofficial.email;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.security.BasicAuthCredentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.solutiongameofficial.email.testutil.ContactRequestFactory.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

@DisplayName("ContactHandler")
class ContactHandlerTest {

    private ContactHandler handler;
    private Context context;

    @BeforeEach
    void setUp() {
        handler = new ContactHandler(
                new BasicAuthCredentials("test-user", "test-pass"),
                "target@example.com"
        );
        context = createMockContext();
    }

    private Context createMockContext() {
        Context ctx = mock(Context.class);
        when(ctx.status(any(HttpStatus.class))).thenReturn(ctx);
        when(ctx.result(anyString())).thenReturn(ctx);
        when(ctx.queryParam("application")).thenReturn("false");
        return ctx;
    }

    private void givenRequest(ContactRequest request) {
        when(context.bodyAsClass(ContactRequest.class)).thenReturn(request);
    }

    private void thenStatusIs(HttpStatus status) {
        verify(context).status(status);
    }

    private void thenErrorContains(String errorFragment) {
        verify(context).result(contains(errorFragment));
    }

    @Nested
    @DisplayName("when request is null")
    class NullRequestTests {

        @Test
        @DisplayName("returns BAD_REQUEST with appropriate error message")
        void returnsBadRequestWithError() {
            givenRequest(null);

            handler.handle(context);

            thenStatusIs(HttpStatus.BAD_REQUEST);
            verify(context).result("{\"error\":\"request cannot be null\"}");
        }
    }

    @Nested
    @DisplayName("title validation")
    class TitleValidationTests {

        @ParameterizedTest(name = "title=\"{0}\" is rejected")
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("rejects null, empty, or blank titles")
        void rejectsBlankTitle(String title) {
            givenRequest(withTitle(title));

            handler.handle(context);

            thenStatusIs(HttpStatus.BAD_REQUEST);
            thenErrorContains("title, mail and message are required");
        }

        @ParameterizedTest(name = "title=\"{0}\" is too short")
        @ValueSource(strings = {"a", "ab", "abc", "abcd"})
        @DisplayName("rejects titles shorter than 5 characters")
        void rejectsTooShortTitle(String title) {
            givenRequest(withTitle(title));

            handler.handle(context);

            thenStatusIs(HttpStatus.BAD_REQUEST);
            thenErrorContains("title must be between 5 and 100");
        }

        @Test
        @DisplayName("rejects titles longer than 100 characters")
        void rejectsTooLongTitle() {
            String longTitle = "a".repeat(101);
            givenRequest(withTitle(longTitle));

            handler.handle(context);

            thenStatusIs(HttpStatus.BAD_REQUEST);
            thenErrorContains("title must be between 5 and 100");
        }

        @Test
        @DisplayName("accepts title with exactly 5 characters (minimum boundary)")
        void acceptsMinBoundaryTitle() {
            givenRequest(withTitle("abcde"));

            handler.handle(context);

            verify(context, never()).result(contains("title must be"));
        }

        @Test
        @DisplayName("accepts title with exactly 100 characters (maximum boundary)")
        void acceptsMaxBoundaryTitle() {
            givenRequest(withTitle("a".repeat(100)));

            handler.handle(context);

            verify(context, never()).result(contains("title must be"));
        }
    }

    @Nested
    @DisplayName("message validation")
    class MessageValidationTests {

        @ParameterizedTest(name = "message=\"{0}\" is rejected")
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t"})
        @DisplayName("rejects null, empty, or blank messages")
        void rejectsBlankMessage(String message) {
            givenRequest(withMessage(message));

            handler.handle(context);

            thenStatusIs(HttpStatus.BAD_REQUEST);
            thenErrorContains("title, mail and message are required");
        }

        @Test
        @DisplayName("rejects messages shorter than 30 characters")
        void rejectsTooShortMessage() {
            givenRequest(withMessage("a".repeat(29)));

            handler.handle(context);

            thenStatusIs(HttpStatus.BAD_REQUEST);
            thenErrorContains("message must be between 30 and 2000");
        }

        @Test
        @DisplayName("rejects messages longer than 2000 characters")
        void rejectsTooLongMessage() {
            givenRequest(withMessage("a".repeat(2001)));

            handler.handle(context);

            thenStatusIs(HttpStatus.BAD_REQUEST);
            thenErrorContains("message must be between 30 and 2000");
        }

        @Test
        @DisplayName("accepts message with exactly 30 characters (minimum boundary)")
        void acceptsMinBoundaryMessage() {
            givenRequest(withMessage("a".repeat(30)));

            handler.handle(context);

            verify(context, never()).result(contains("message must be"));
        }

        @Test
        @DisplayName("accepts message with exactly 2000 characters (maximum boundary)")
        void acceptsMaxBoundaryMessage() {
            givenRequest(withMessage("a".repeat(2000)));

            handler.handle(context);

            verify(context, never()).result(contains("message must be"));
        }
    }

    @Nested
    @DisplayName("email validation")
    class EmailValidationTests {

        @ParameterizedTest(name = "email=\"{0}\" is rejected")
        @NullAndEmptySource
        @ValueSource(strings = {"   "})
        @DisplayName("rejects null, empty, or blank emails")
        void rejectsBlankEmail(String email) {
            givenRequest(withEmail(email));

            handler.handle(context);

            thenStatusIs(HttpStatus.BAD_REQUEST);
            thenErrorContains("title, mail and message are required");
        }

        @Test
        @DisplayName("rejects emails shorter than 4 characters")
        void rejectsTooShortEmail() {
            givenRequest(withEmail("a@b"));

            handler.handle(context);

            thenStatusIs(HttpStatus.BAD_REQUEST);
            thenErrorContains("fromEmail must be between 4 and 254");
        }

        @Test
        @DisplayName("rejects emails longer than 254 characters")
        void rejectsTooLongEmail() {
            String longEmail = "a".repeat(250) + "@b.com";
            givenRequest(withEmail(longEmail));

            handler.handle(context);

            thenStatusIs(HttpStatus.BAD_REQUEST);
            thenErrorContains("fromEmail must be between 4 and 254");
        }

        @Test
        @DisplayName("accepts email with exactly 4 characters (minimum boundary)")
        void acceptsMinBoundaryEmail() {
            givenRequest(withEmail("a@bc"));

            handler.handle(context);

            verify(context, never()).result(contains("fromEmail must be"));
        }

        @Test
        @DisplayName("accepts email with exactly 254 characters (maximum boundary)")
        void acceptsMaxBoundaryEmail() {
            String maxEmail = "a".repeat(245) + "@test.com";
            givenRequest(withEmail(maxEmail));

            handler.handle(context);

            verify(context, never()).result(contains("fromEmail must be"));
        }
    }

    @Nested
    @DisplayName("application query parameter")
    class ApplicationParamTests {

        @Test
        @DisplayName("reads application query parameter")
        void readsApplicationParam() {
            when(context.queryParam("application")).thenReturn("true");
            givenRequest(valid());

            handler.handle(context);

            verify(context).queryParam("application");
        }

        @Test
        @DisplayName("handles null application parameter gracefully")
        void handlesNullParam() {
            when(context.queryParam("application")).thenReturn(null);
            givenRequest(valid());

            handler.handle(context);

            verify(context).queryParam("application");
        }
    }

    @Nested
    @DisplayName("successful validation")
    class SuccessfulValidationTests {

        @Test
        @DisplayName("passes validation with all valid fields")
        void passesValidationWithValidRequest() {
            givenRequest(valid());

            handler.handle(context);

            verify(context, never()).result(contains("error"));
        }
    }
}
