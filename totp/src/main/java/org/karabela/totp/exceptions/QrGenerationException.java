package org.karabela.totp.exceptions;

public final class QrGenerationException extends Exception {
    public QrGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
