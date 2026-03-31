package org.karabela.totp.qr;

import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import org.karabela.totp.exceptions.QrGenerationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ZxingPngQrGeneratorTest {

    @Test
    void testGeneratesQrCode() throws QrGenerationException {
        ZxingPngQrGenerator generator = new ZxingPngQrGenerator();
        byte[] data = generator.generate(getData());

        assertNotNull(data);
        assertTrue(data.length > 0);
        // PNG magic bytes: 137 80 78 71 13 10 26 10
        assertEquals((byte) 0x89, data[0]);
        assertEquals((byte) 0x50, data[1]); // 'P'
        assertEquals((byte) 0x4E, data[2]); // 'N'
        assertEquals((byte) 0x47, data[3]); // 'G'
    }

    @Test
    void testMimeType() {
        assertEquals("image/png", new ZxingPngQrGenerator().getImageMimeType());
    }

    @Test
    void testImageSizeIsStored() {
        ZxingPngQrGenerator generator = new ZxingPngQrGenerator(500);
        assertEquals(500, generator.getImageSize());
    }

    @Test
    void testCustomSizeGeneratesLargerImage() throws QrGenerationException {
        byte[] defaultImage = new ZxingPngQrGenerator().generate(getData());
        byte[] largerImage = new ZxingPngQrGenerator(500).generate(getData());

        // A larger QR code should produce more image bytes
        assertTrue(largerImage.length > defaultImage.length,
                "500px image should be larger than default 350px");
    }

    @Test
    void testExceptionIsWrapped() throws WriterException {
        WriterException exception = new WriterException("test failure");
        Writer writer = mock(Writer.class);
        when(writer.encode(anyString(), any(), anyInt(), anyInt())).thenThrow(exception);

        ZxingPngQrGenerator generator = new ZxingPngQrGenerator(writer, 350);

        QrGenerationException e = assertThrows(QrGenerationException.class, () ->
                generator.generate(getData())
        );

        assertEquals("Failed to generate QR code. See nested exception.", e.getMessage());
        assertEquals(exception, e.getCause());
    }

    private QrData getData() {
        return new QrData.Builder()
                .label("example@example.com")
                .secret("EX47GINFPBK5GNLYLILGD2H6ZLGJNNWB")
                .issuer("AppName")
                .digits(6)
                .period(30)
                .build();
    }
}
