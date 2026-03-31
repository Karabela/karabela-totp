package org.karabela.totp.qr;

import org.karabela.totp.code.HashingAlgorithm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QrDataTest {

    @Test
    void testUriGeneration() {
        QrData data = new QrData.Builder()
                .label("example@example.com")
                .secret("the-secret-here")
                .issuer("AppName AppCorp")
                .algorithm(HashingAlgorithm.SHA256)
                .digits(6)
                .period(30)
                .build();

        assertEquals(
                "otpauth://totp/example%40example.com?secret=the-secret-here&issuer=AppName%20AppCorp&algorithm=SHA256&digits=6&period=30",
                data.getUri()
        );
    }

    @Test
    void testNullSecretThrowsOnBuild() {
        assertThrows(NullPointerException.class, () ->
                new QrData.Builder()
                        .label("test")
                        .issuer("test")
                        .build()
        );
    }

    @Test
    void testNullLabelThrowsOnSet() {
        assertThrows(NullPointerException.class, () ->
                new QrData.Builder().label(null)
        );
    }

    @Test
    void testMissingLabelThrowsOnBuild() {
        assertThrows(NullPointerException.class, () ->
                new QrData.Builder()
                        .secret("test-secret")
                        .issuer("test")
                        .build()
        );
    }
}
