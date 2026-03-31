package org.karabela.totp.time;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class ClockTimeProviderTest {

    @Test
    void testProvidesFixedTime() {
        long expectedEpoch = 1567975936L;
        Clock fixedClock = Clock.fixed(Instant.ofEpochSecond(expectedEpoch), ZoneOffset.UTC);
        ClockTimeProvider provider = new ClockTimeProvider(fixedClock);

        assertEquals(expectedEpoch, provider.getTime());
    }

    @Test
    void testNullClockThrowsException() {
        assertThrows(NullPointerException.class, () -> new ClockTimeProvider(null));
    }

    @Test
    void testSystemClock() {
        ClockTimeProvider provider = new ClockTimeProvider(Clock.systemUTC());
        long time = provider.getTime();
        long now = Instant.now().getEpochSecond();

        assertTrue(Math.abs(time - now) <= 1);
    }
}
