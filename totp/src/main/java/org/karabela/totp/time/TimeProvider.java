package org.karabela.totp.time;

import org.karabela.totp.exceptions.TimeProviderException;

/**
 * Contract for providing the current time in seconds since the Unix epoch.
 */
public interface TimeProvider {

    /**
     * @return The number of seconds since 1970-01-01T00:00:00Z.
     * @throws TimeProviderException if the time cannot be determined.
     */
    long getTime() throws TimeProviderException;
}
