package org.karabela.totp.secret;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.*;

class DefaultSecretGeneratorTest {

    @Test
    void testSecretGenerated() {
        DefaultSecretGenerator generator = new DefaultSecretGenerator();
        String secret = generator.generate();
        assertNotNull(secret);
        assertFalse(secret.isEmpty());
    }

    @Test
    void testCharacterLengths() {
        for (int charCount : new int[]{16, 32, 64, 128}) {
            DefaultSecretGenerator generator = new DefaultSecretGenerator(charCount);
            String secret = generator.generate();
            assertEquals(charCount, secret.length());
        }
    }

    @Test
    void testValidBase32Encoded() {
        DefaultSecretGenerator generator = new DefaultSecretGenerator();
        String secret = generator.generate();

        assertTrue(secret.matches("^[A-Z2-7]+=*$"));
        assertEquals(0, secret.length() % 8);
    }

    @Test
    void testInvalidLengthThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new DefaultSecretGenerator(0));
        assertThrows(IllegalArgumentException.class, () -> new DefaultSecretGenerator(-1));
        assertThrows(IllegalArgumentException.class, () -> new DefaultSecretGenerator(7));
    }

    @Test
    void testCustomSecureRandom() {
        SecureRandom fixedRandom = new SecureRandom(new byte[]{1, 2, 3, 4, 5});
        DefaultSecretGenerator generator = new DefaultSecretGenerator(32, fixedRandom);
        String secret = generator.generate();
        assertNotNull(secret);
        assertEquals(32, secret.length());
        assertTrue(secret.matches("^[A-Z2-7]+=*$"));
    }

    @Test
    void testMinimumValidLength() {
        DefaultSecretGenerator generator = new DefaultSecretGenerator(8);
        String secret = generator.generate();
        assertEquals(8, secret.length());
    }

    @Test
    void testGeneratedSecretsAreUnique() {
        DefaultSecretGenerator generator = new DefaultSecretGenerator();
        String secret1 = generator.generate();
        String secret2 = generator.generate();
        assertNotEquals(secret1, secret2);
    }
}
