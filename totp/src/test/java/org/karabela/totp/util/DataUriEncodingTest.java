package org.karabela.totp.util;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.karabela.totp.util.Utils.getDataUriForImage;
import static org.junit.jupiter.api.Assertions.*;

class DataUriEncodingTest {

    @Test
    void testDataUriEncode() {
        // 1x1 white px PNG image base64 encoded
        final String pngImage = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+ip1sAAAAASUVORK5CYII=";
        final byte[] imageData = Base64.getDecoder().decode(pngImage);

        assertEquals(
                "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+ip1sAAAAASUVORK5CYII=",
                getDataUriForImage(imageData, "image/png")
        );
    }
}
