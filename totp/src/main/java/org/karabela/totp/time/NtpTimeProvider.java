package org.karabela.totp.time;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.karabela.totp.exceptions.TimeProviderException;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Time provider that fetches the current time from an NTP server.
 *
 * <p>Requires {@code commons-net} on the classpath (optional dependency).</p>
 *
 * <p>Implements {@link Closeable} — call {@link #close()} when done to
 * release the underlying UDP socket.</p>
 */
public final class NtpTimeProvider implements TimeProvider, Closeable {

    private final NTPUDPClient client;
    private final InetAddress ntpHost;

    public NtpTimeProvider(String ntpHostname) throws UnknownHostException {
        this(ntpHostname, 3000);
    }

    public NtpTimeProvider(String ntpHostname, int timeoutMs) throws UnknownHostException {
        checkHasDependency();
        client = new NTPUDPClient();
        client.setDefaultTimeout(timeoutMs);
        ntpHost = InetAddress.getByName(ntpHostname);
    }

    // Package-private for testing
    NtpTimeProvider(String ntpHostname, String dependentClass) throws UnknownHostException {
        checkHasDependency(dependentClass);
        client = new NTPUDPClient();
        client.setDefaultTimeout(3000);
        ntpHost = InetAddress.getByName(ntpHostname);
    }

    @Override
    public long getTime() throws TimeProviderException {
        TimeInfo timeInfo;
        try {
            timeInfo = client.getTime(ntpHost);
            timeInfo.computeDetails();
        } catch (IOException e) {
            throw new TimeProviderException("Failed to provide time from NTP server. See nested exception.", e);
        }

        if (timeInfo.getOffset() == null) {
            throw new TimeProviderException("Failed to calculate NTP offset.");
        }

        return (System.currentTimeMillis() + timeInfo.getOffset()) / 1000;
    }

    @Override
    public void close() {
        client.close();
    }

    private static void checkHasDependency() {
        checkHasDependency("org.apache.commons.net.ntp.NTPUDPClient");
    }

    private static void checkHasDependency(String dependentClass) {
        try {
            Class.forName(dependentClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "The Apache Commons Net library must be on the classpath to use NtpTimeProvider. " +
                    "Add commons-net as a dependency.");
        }
    }
}
