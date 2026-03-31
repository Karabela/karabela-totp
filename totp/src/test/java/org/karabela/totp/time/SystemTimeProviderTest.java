package org.karabela.totp.time;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class SystemTimeProviderTest {

    @Test
    void testProvidesTime() {
        long currentTime = Instant.now().getEpochSecond();
        TimeProvider time = new SystemTimeProvider();
        long providedTime = time.getTime();

        assertTrue(currentTime - 5 <= providedTime);
        assertTrue(providedTime <= currentTime + 5);
        assertEquals(10, String.valueOf(currentTime).length());
    }
}
