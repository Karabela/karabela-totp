package org.karabela.totp.exceptions;

public final class TimeProviderException extends RuntimeException {
    public TimeProviderException(String message) {
        super(message);
    }

    public TimeProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
