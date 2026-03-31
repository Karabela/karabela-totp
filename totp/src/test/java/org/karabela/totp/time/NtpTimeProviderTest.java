package org.karabela.totp.time;

import org.karabela.totp.exceptions.TimeProviderException;
import org.junit.jupiter.api.Test;

import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

class NtpTimeProviderTest {

    @Test
    void testProvidesTime() throws UnknownHostException {
        TimeProvider time = new NtpTimeProvider("pool.ntp.org");
        long currentTime = time.getTime();

        assertEquals(10, String.valueOf(currentTime).length());
    }

    @Test
    void testUnknownHostThrowsException() {
        assertThrows(UnknownHostException.class, () -> new NtpTimeProvider("sdfsf/safsf"));
    }

    @Test
    void testNonNtpHostThrowsException() throws UnknownHostException {
        TimeProvider time = new NtpTimeProvider("www.example.com");

        TimeProviderException e = assertThrows(TimeProviderException.class, time::getTime);
        assertNotNull(e.getCause());
    }

    @Test
    void testRequiresDependency() {
        RuntimeException e = assertThrows(RuntimeException.class, () ->
                new NtpTimeProvider("www.example.com", "fake.class.Here")
        );

        assertTrue(e.getMessage().contains("Apache Commons Net"));
    }
}
