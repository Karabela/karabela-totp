package org.karabela.totp.time;

import java.time.Instant;

/**
 * Time provider that uses the system clock.
 */
public final class SystemTimeProvider implements TimeProvider {
    @Override
    public long getTime() {
        return Instant.now().getEpochSecond();
    }
}
