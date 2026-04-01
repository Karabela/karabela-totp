package org.karabela.totp.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CodeGenerationExceptionTest {

    @Test
    void testMessageAndCause() {
        RuntimeException cause = new RuntimeException("root cause");
        CodeGenerationException exception = new CodeGenerationException("test message", cause);

        assertEquals("test message", exception.getMessage());
        assertSame(cause, exception.getCause());
    }

    @Test
    void testNullCause() {
        CodeGenerationException exception = new CodeGenerationException("test message", null);

        assertEquals("test message", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testIsCheckedException() {
        Exception exception = new CodeGenerationException("msg", null);
        assertInstanceOf(Exception.class, exception);
        assertFalse(RuntimeException.class.isAssignableFrom(CodeGenerationException.class));
    }
}
