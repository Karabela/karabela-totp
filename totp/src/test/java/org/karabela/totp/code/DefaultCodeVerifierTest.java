package org.karabela.totp.code;

import org.karabela.totp.exceptions.CodeGenerationException;
import org.karabela.totp.time.TimeProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultCodeVerifierTest {

    @Test
    void testCodeIsValid() {
        String secret = "EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB";
        long timeToRunAt = 1567975936;
        String correctCode = "862707";
        int timePeriod = 30;

        assertTrue(isValidCode(secret, correctCode, timeToRunAt - timePeriod, timePeriod));
        assertTrue(isValidCode(secret, correctCode, timeToRunAt, timePeriod));
        assertTrue(isValidCode(secret, correctCode, timeToRunAt + timePeriod, timePeriod));

        assertFalse(isValidCode(secret, correctCode, timeToRunAt + timePeriod + 15, timePeriod));
        assertFalse(isValidCode(secret, "123", timeToRunAt, timePeriod));
    }

    @Test
    void testCodeGenerationFailureReturnsFalse() throws CodeGenerationException {
        String secret = "EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB";

        TimeProvider timeProvider = mock(TimeProvider.class);
        when(timeProvider.getTime()).thenReturn(1567975936L);

        CodeGenerator codeGenerator = mock(CodeGenerator.class);
        when(codeGenerator.generate(anyString(), anyLong())).thenThrow(new CodeGenerationException("Test", new RuntimeException()));

        DefaultCodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        verifier.setAllowedTimePeriodDiscrepancy(1);

        assertFalse(verifier.isValidCode(secret, "1234"));
    }

    @Test
    void testZeroDiscrepancyDoesNotThrow() {
        String secret = "EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB";
        long timeToRunAt = 1567975936;

        TimeProvider timeProvider = mock(TimeProvider.class);
        when(timeProvider.getTime()).thenReturn(timeToRunAt);

        DefaultCodeVerifier verifier = new DefaultCodeVerifier(new DefaultCodeGenerator(), timeProvider);
        verifier.setTimePeriod(30);
        verifier.setAllowedTimePeriodDiscrepancy(0);

        // Should not throw ArithmeticException (issue #61)
        assertDoesNotThrow(() -> verifier.isValidCode(secret, "000000"));
    }

    @Test
    void testInvalidTimePeriodThrowsException() {
        TimeProvider timeProvider = mock(TimeProvider.class);
        DefaultCodeVerifier verifier = new DefaultCodeVerifier(new DefaultCodeGenerator(), timeProvider);

        assertThrows(IllegalArgumentException.class, () -> verifier.setTimePeriod(0));
        assertThrows(IllegalArgumentException.class, () -> verifier.setTimePeriod(-1));
    }

    @Test
    void testNegativeDiscrepancyThrowsException() {
        TimeProvider timeProvider = mock(TimeProvider.class);
        DefaultCodeVerifier verifier = new DefaultCodeVerifier(new DefaultCodeGenerator(), timeProvider);

        assertThrows(IllegalArgumentException.class, () -> verifier.setAllowedTimePeriodDiscrepancy(-1));
    }

    @Test
    void testNullSecretThrowsException() {
        TimeProvider timeProvider = mock(TimeProvider.class);
        when(timeProvider.getTime()).thenReturn(1567975936L);
        DefaultCodeVerifier verifier = new DefaultCodeVerifier(new DefaultCodeGenerator(), timeProvider);

        assertThrows(NullPointerException.class, () -> verifier.isValidCode(null, "123456"));
    }

    @Test
    void testNullCodeThrowsException() {
        TimeProvider timeProvider = mock(TimeProvider.class);
        when(timeProvider.getTime()).thenReturn(1567975936L);
        DefaultCodeVerifier verifier = new DefaultCodeVerifier(new DefaultCodeGenerator(), timeProvider);

        assertThrows(NullPointerException.class, () -> verifier.isValidCode("EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB", null));
    }

    private boolean isValidCode(String secret, String code, long time, int timePeriod) {
        TimeProvider timeProvider = mock(TimeProvider.class);
        when(timeProvider.getTime()).thenReturn(time);

        DefaultCodeVerifier verifier = new DefaultCodeVerifier(new DefaultCodeGenerator(), timeProvider);
        verifier.setTimePeriod(timePeriod);

        return verifier.isValidCode(secret, code);
    }
}
