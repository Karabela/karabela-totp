package org.karabela.totp.code;

import org.karabela.totp.exceptions.CodeGenerationException;

/**
 * Contract for generating one-time password codes.
 */
public interface CodeGenerator {

    /**
     * @param secret  The shared secret/key to generate the code with.
     * @param counter The current time bucket number (seconds since epoch / bucket period).
     * @return The n-digit code for the secret/counter.
     * @throws CodeGenerationException if code generation fails.
     */
    String generate(String secret, long counter) throws CodeGenerationException;
}
