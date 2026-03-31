package org.karabela.totp.exceptions;

public final class CodeGenerationException extends Exception {
    public CodeGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
