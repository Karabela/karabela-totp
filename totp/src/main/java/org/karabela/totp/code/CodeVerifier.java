package org.karabela.totp.code;

/**
 * Contract for verifying one-time password codes.
 */
public interface CodeVerifier {

    /**
     * @param secret The shared secret/key to check the code against.
     * @param code   The n-digit code given by the end user to check.
     * @return {@code true} if the code is valid.
     */
    boolean isValidCode(String secret, String code);
}
