package org.karabela.totp.time;

import java.time.Clock;
import java.util.Objects;

/**
 * Time provider backed by a {@link Clock} instance.
 *
 * <p>Useful for testing (with {@link Clock#fixed}) or for applications
 * that need to control the time source.</p>
 */
public final class ClockTimeProvider implements TimeProvider {

    private final Clock clock;

    public ClockTimeProvider(Clock clock) {
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
    }

    @Override
    public long getTime() {
        return clock.instant().getEpochSecond();
    }
}
