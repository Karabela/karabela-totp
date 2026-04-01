package org.karabela.totp.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QrGenerationExceptionTest {

    @Test
    void testMessageAndCause() {
        RuntimeException cause = new RuntimeException("root cause");
        QrGenerationException exception = new QrGenerationException("test message", cause);

        assertEquals("test message", exception.getMessage());
        assertSame(cause, exception.getCause());
    }

    @Test
    void testNullCause() {
        QrGenerationException exception = new QrGenerationException("test message", null);

        assertEquals("test message", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testIsCheckedException() {
        Exception exception = new QrGenerationException("msg", null);
        assertInstanceOf(Exception.class, exception);
        assertFalse(RuntimeException.class.isAssignableFrom(QrGenerationException.class));
    }
}
