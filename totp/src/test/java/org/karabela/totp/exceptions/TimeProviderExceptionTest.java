package org.karabela.totp.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeProviderExceptionTest {

    @Test
    void testMessageOnlyConstructor() {
        TimeProviderException exception = new TimeProviderException("test message");

        assertEquals("test message", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testMessageAndCauseConstructor() {
        RuntimeException cause = new RuntimeException("root cause");
        TimeProviderException exception = new TimeProviderException("test message", cause);

        assertEquals("test message", exception.getMessage());
        assertSame(cause, exception.getCause());
    }

    @Test
    void testIsRuntimeException() {
        assertInstanceOf(RuntimeException.class, new TimeProviderException("msg"));
    }
}
