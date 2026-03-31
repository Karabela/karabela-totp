package org.karabela.totp.recovery;

import java.security.SecureRandom;

/**
 * Generates one-time recovery codes for MFA backup access.
 *
 * <p>Default settings produce codes with 82 bits of entropy:
 * 16 characters from a 36-character alphabet (a-z, 0-9), split into
 * groups of 4 separated by dashes (e.g. {@code tf8i-exmo-3lcb-slkm}).</p>
 */
public final class RecoveryCodeGenerator {

    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    private static final int CODE_LENGTH = 16;
    private static final int GROUP_SIZE = 4;

    private final SecureRandom random;

    public RecoveryCodeGenerator() {
        this(new SecureRandom());
    }

    public RecoveryCodeGenerator(SecureRandom random) {
        this.random = random;
    }

    public String[] generateCodes(int amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("Amount must be at least 1.");
        }

        String[] codes = new String[amount];
        for (int i = 0; i < amount; i++) {
            codes[i] = generateCode();
        }
        return codes;
    }

    private String generateCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH + (CODE_LENGTH / GROUP_SIZE) - 1);

        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS[random.nextInt(CHARACTERS.length)]);

            if ((i + 1) % GROUP_SIZE == 0 && (i + 1) != CODE_LENGTH) {
                code.append('-');
            }
        }

        return code.toString();
    }
}
