package org.karabela.totp.qr;

import org.karabela.totp.code.HashingAlgorithm;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Holds the data needed to generate an OTPAuth QR code for authenticator app provisioning.
 *
 * <p>Use {@link Builder} to construct instances.</p>
 *
 * @see <a href="https://github.com/google/google-authenticator/wiki/Key-Uri-Format">Key URI Format</a>
 */
public final class QrData {

    private final String type;
    private final String label;
    private final String secret;
    private final String issuer;
    private final String algorithm;
    private final int digits;
    private final int period;

    private QrData(String type, String label, String secret, String issuer, String algorithm, int digits, int period) {
        this.type = type;
        this.label = label;
        this.secret = secret;
        this.issuer = issuer;
        this.algorithm = algorithm;
        this.digits = digits;
        this.period = period;
    }

    public String getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public String getSecret() {
        return secret;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public int getDigits() {
        return digits;
    }

    public int getPeriod() {
        return period;
    }

    /**
     * @return The OTPAuth URI to encode into a QR image.
     */
    public String getUri() {
        return "otpauth://" +
                uriEncode(type) + "/" +
                uriEncode(label) + "?" +
                "secret=" + uriEncode(secret) +
                "&issuer=" + uriEncode(issuer) +
                "&algorithm=" + uriEncode(algorithm) +
                "&digits=" + digits +
                "&period=" + period;
    }

    private static String uriEncode(String text) {
        if (text == null) {
            return "";
        }
        return URLEncoder.encode(text, StandardCharsets.UTF_8).replace("+", "%20");
    }

    public static final class Builder {
        private String label;
        private String secret;
        private String issuer;
        private HashingAlgorithm algorithm = HashingAlgorithm.SHA1;
        private int digits = 6;
        private int period = 30;

        public Builder label(String label) {
            this.label = Objects.requireNonNull(label, "label must not be null");
            return this;
        }

        public Builder secret(String secret) {
            this.secret = Objects.requireNonNull(secret, "secret must not be null");
            return this;
        }

        public Builder issuer(String issuer) {
            this.issuer = Objects.requireNonNull(issuer, "issuer must not be null");
            return this;
        }

        public Builder algorithm(HashingAlgorithm algorithm) {
            this.algorithm = Objects.requireNonNull(algorithm, "algorithm must not be null");
            return this;
        }

        public Builder digits(int digits) {
            this.digits = digits;
            return this;
        }

        public Builder period(int period) {
            this.period = period;
            return this;
        }

        public QrData build() {
            Objects.requireNonNull(secret, "secret is required");
            Objects.requireNonNull(label, "label is required");
            return new QrData("totp", label, secret, issuer, algorithm.getFriendlyName(), digits, period);
        }
    }
}
