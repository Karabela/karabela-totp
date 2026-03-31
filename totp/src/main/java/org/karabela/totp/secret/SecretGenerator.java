package org.karabela.totp.secret;

/**
 * Contract for generating shared TOTP secrets.
 */
public interface SecretGenerator {

    /**
     * @return A random Base32-encoded string for use as a shared secret.
     */
    String generate();
}
