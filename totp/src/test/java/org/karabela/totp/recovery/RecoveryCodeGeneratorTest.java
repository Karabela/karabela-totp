package org.karabela.totp.recovery;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RecoveryCodeGeneratorTest {

    @Test
    void testCorrectAmountGenerated() {
        RecoveryCodeGenerator generator = new RecoveryCodeGenerator();
        String[] codes = generator.generateCodes(16);

        assertEquals(16, codes.length);
        for (String code : codes) {
            assertNotNull(code);
        }
    }

    @Test
    void testCodesMatchFormat() {
        RecoveryCodeGenerator generator = new RecoveryCodeGenerator();
        String[] codes = generator.generateCodes(16);

        for (String code : codes) {
            assertTrue(code.matches("[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}"), code);
        }
    }

    @Test
    void testCodesAreUnique() {
        RecoveryCodeGenerator generator = new RecoveryCodeGenerator();
        String[] codes = generator.generateCodes(25);

        Set<String> uniqueCodes = new HashSet<>(Arrays.asList(codes));
        assertEquals(25, uniqueCodes.size());
    }

    @Test
    void testInvalidNumberThrowsException() {
        RecoveryCodeGenerator generator = new RecoveryCodeGenerator();
        assertThrows(IllegalArgumentException.class, () -> generator.generateCodes(-1));
        assertThrows(IllegalArgumentException.class, () -> generator.generateCodes(0));
    }

    @Test
    void testCustomSecureRandom() {
        SecureRandom fixedRandom = new SecureRandom(new byte[]{42, 43, 44});
        RecoveryCodeGenerator generator = new RecoveryCodeGenerator(fixedRandom);
        String[] codes = generator.generateCodes(5);

        assertEquals(5, codes.length);
        for (String code : codes) {
            assertTrue(code.matches("[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}"), code);
        }
    }

    @Test
    void testSingleCodeGeneration() {
        RecoveryCodeGenerator generator = new RecoveryCodeGenerator();
        String[] codes = generator.generateCodes(1);

        assertEquals(1, codes.length);
        assertTrue(codes[0].matches("[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}"), codes[0]);
    }

    @Test
    void testCodeLength() {
        RecoveryCodeGenerator generator = new RecoveryCodeGenerator();
        String[] codes = generator.generateCodes(1);

        // 16 chars + 3 dashes = 19 total
        assertEquals(19, codes[0].length());
    }
}
