package org.karabela.totp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KarabelaTotpTest {

    @Test
    void testVersionIsNotNull() {
        assertNotNull(KarabelaTotp.VERSION);
        assertFalse(KarabelaTotp.VERSION.isEmpty());
    }

    @Test
    void testVersionMatchesExpected() {
        assertEquals("2.0.0", KarabelaTotp.VERSION);
    }
}
