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

    @Test
    void testGetters() {
        QrData data = new QrData.Builder()
                .label("user@example.com")
                .secret("JBSWY3DPEHPK3PXP")
                .issuer("MyApp")
                .algorithm(HashingAlgorithm.SHA512)
                .digits(8)
                .period(60)
                .build();

        assertEquals("totp", data.getType());
        assertEquals("user@example.com", data.getLabel());
        assertEquals("JBSWY3DPEHPK3PXP", data.getSecret());
        assertEquals("MyApp", data.getIssuer());
        assertEquals("SHA512", data.getAlgorithm());
        assertEquals(8, data.getDigits());
        assertEquals(60, data.getPeriod());
    }

    @Test
    void testBuilderDefaults() {
        QrData data = new QrData.Builder()
                .label("test")
                .secret("test-secret")
                .build();

        assertEquals("SHA1", data.getAlgorithm());
        assertEquals(6, data.getDigits());
        assertEquals(30, data.getPeriod());
    }

    @Test
    void testUriWithNullIssuer() {
        QrData data = new QrData.Builder()
                .label("test")
                .secret("test-secret")
                .build();

        String uri = data.getUri();
        assertNotNull(uri);
        assertTrue(uri.contains("issuer=&"));
    }

    @Test
    void testUriEncodesSpecialCharacters() {
        QrData data = new QrData.Builder()
                .label("user name+test@example.com")
                .secret("ABC123")
                .issuer("My App & Co.")
                .build();

        String uri = data.getUri();
        assertFalse(uri.contains(" "));
        assertTrue(uri.contains("%20"));
        assertTrue(uri.contains("%26"));
        assertTrue(uri.contains("%40"));
    }

    @Test
    void testNullSecretThrowsOnSet() {
        assertThrows(NullPointerException.class, () ->
                new QrData.Builder().secret(null)
        );
    }

    @Test
    void testNullIssuerThrowsOnSet() {
        assertThrows(NullPointerException.class, () ->
                new QrData.Builder().issuer(null)
        );
    }

    @Test
    void testNullAlgorithmThrowsOnSet() {
        assertThrows(NullPointerException.class, () ->
                new QrData.Builder().algorithm(null)
        );
    }
}
