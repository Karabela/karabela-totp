package org.karabela.totp.util;

import java.util.Base64;

/**
 * Utility methods for working with OTP-related data.
 */
public final class Utils {

    private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();

    private Utils() {
    }

    /**
     * Encodes image data as a
     * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/Data_URIs">data URI</a>
     * suitable for embedding in HTML.
     *
     * @param data     The raw image bytes.
     * @param mimeType The MIME type (e.g. "image/png").
     * @return The data URI string.
     */
    public static String getDataUriForImage(byte[] data, String mimeType) {
        String encodedData = BASE64_ENCODER.encodeToString(data);
        return String.format("data:%s;base64,%s", mimeType, encodedData);
    }
}
