package org.karabela.totp.qr;

import org.karabela.totp.code.HashingAlgorithm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QrDataFactoryTest {

    @Test
    void testFactorySetsDefaultsOnBuilder() {
        QrDataFactory qrDataFactory = new QrDataFactory(HashingAlgorithm.SHA256, 6, 30);
        QrData data = qrDataFactory.newBuilder().label("test").secret("test-secret").build();

        assertEquals(HashingAlgorithm.SHA256.getFriendlyName(), data.getAlgorithm());
        assertEquals(6, data.getDigits());
        assertEquals(30, data.getPeriod());
    }
}
