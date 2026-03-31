package org.karabela.totp.secret;

import org.apache.commons.codec.binary.Base32;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * Default implementation of {@link SecretGenerator} that produces
 * Base32-encoded secrets using {@link SecureRandom}.
 *
 * <p>The {@code numCharacters} parameter controls the output length and must
 * be a positive multiple of 8 (Base32 encoding produces 8-character groups).
 * Minimum recommended value is 16 (80 bits of entropy).</p>
 */
public final class DefaultSecretGenerator implements SecretGenerator {

    private static final int MIN_CHARACTERS = 8;

    private final SecureRandom random;
    private final int numCharacters;

    public DefaultSecretGenerator() {
        this(32);
    }

    public DefaultSecretGenerator(int numCharacters) {
        this(numCharacters, new SecureRandom());
    }

    public DefaultSecretGenerator(int numCharacters, SecureRandom random) {
        if (numCharacters < MIN_CHARACTERS) {
            throw new IllegalArgumentException(
                    "Number of characters must be at least " + MIN_CHARACTERS + ", got: " + numCharacters);
        }
        this.numCharacters = numCharacters;
        this.random = random;
    }

    @Override
    public String generate() {
        // 5 bits per char in Base32
        byte[] bytes = new byte[(numCharacters * 5) / 8];
        random.nextBytes(bytes);
        // New Base32 per call — Base32 is not thread-safe (maintains internal buffers)
        Base32 encoder = new Base32();
        return new String(encoder.encode(bytes), StandardCharsets.UTF_8);
    }
}
