package org.karabela.totp.recovery;

import org.junit.jupiter.api.Test;

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
}
