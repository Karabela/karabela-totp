package org.karabela.totp.code;

/**
 * Supported HMAC algorithms for TOTP code generation.
 *
 * <p>Note: Most authenticator apps only support SHA1. Using SHA256 or SHA512
 * may cause compatibility issues with some apps.</p>
 */
public enum HashingAlgorithm {

    SHA1("HmacSHA1", "SHA1"),
    SHA256("HmacSHA256", "SHA256"),
    SHA512("HmacSHA512", "SHA512");

    private final String hmacAlgorithm;
    private final String friendlyName;

    HashingAlgorithm(String hmacAlgorithm, String friendlyName) {
        this.hmacAlgorithm = hmacAlgorithm;
        this.friendlyName = friendlyName;
    }

    public String getHmacAlgorithm() {
        return hmacAlgorithm;
    }

    public String getFriendlyName() {
        return friendlyName;
    }
}
