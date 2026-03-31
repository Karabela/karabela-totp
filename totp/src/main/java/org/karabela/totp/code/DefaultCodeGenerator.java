package org.karabela.totp.code;

import org.apache.commons.codec.binary.Base32;
import org.karabela.totp.exceptions.CodeGenerationException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * Default implementation of {@link CodeGenerator} that produces TOTP codes
 * per RFC 6238.
 */
public final class DefaultCodeGenerator implements CodeGenerator {

    private static final int MAX_DIGITS = 10;

    private final HashingAlgorithm algorithm;
    private final int digits;
    private final long digitsPower;

    public DefaultCodeGenerator() {
        this(HashingAlgorithm.SHA1, 6);
    }

    public DefaultCodeGenerator(HashingAlgorithm algorithm) {
        this(algorithm, 6);
    }

    public DefaultCodeGenerator(HashingAlgorithm algorithm, int digits) {
        Objects.requireNonNull(algorithm, "HashingAlgorithm must not be null.");
        if (digits < 1 || digits > MAX_DIGITS) {
            throw new IllegalArgumentException(
                    "Number of digits must be between 1 and " + MAX_DIGITS + ", got: " + digits);
        }

        this.algorithm = algorithm;
        this.digits = digits;

        // Pre-compute 10^digits as long — safe because digits <= MAX_DIGITS
        long power = 1;
        for (int i = 0; i < digits; i++) {
            power *= 10;
        }
        this.digitsPower = power;
    }

    @Override
    public String generate(String key, long counter) throws CodeGenerationException {
        try {
            byte[] hash = generateHash(key, counter);
            return getDigitsFromHash(hash);
        } catch (Exception e) {
            throw new CodeGenerationException("Failed to generate code. See nested exception.", e);
        }
    }

    private byte[] generateHash(String key, long counter) throws InvalidKeyException, NoSuchAlgorithmException {
        byte[] data = new byte[8];
        long value = counter;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }

        // Create a new Base32 per call — Base32 is not thread-safe (maintains internal buffers)
        Base32 base32 = new Base32();
        byte[] decodedKey = base32.decode(key);
        SecretKeySpec signKey = new SecretKeySpec(decodedKey, algorithm.getHmacAlgorithm());
        Mac mac = Mac.getInstance(algorithm.getHmacAlgorithm());
        mac.init(signKey);

        return mac.doFinal(data);
    }

    private String getDigitsFromHash(byte[] hash) {
        int offset = hash[hash.length - 1] & 0xF;

        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            truncatedHash |= (hash[offset + i] & 0xFF);
        }

        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= digitsPower;

        return String.format("%0" + digits + "d", truncatedHash);
    }
}
