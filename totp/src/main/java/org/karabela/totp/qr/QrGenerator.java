package org.karabela.totp.qr;

import org.karabela.totp.exceptions.QrGenerationException;

/**
 * Contract for generating QR code images from {@link QrData}.
 */
public interface QrGenerator {

    /**
     * @return The MIME type of the generated image (e.g. "image/png").
     */
    String getImageMimeType();

    /**
     * @param data The QR data to encode.
     * @return The raw image data as a byte array.
     * @throws QrGenerationException if image generation fails.
     */
    byte[] generate(QrData data) throws QrGenerationException;
}
