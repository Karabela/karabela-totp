package org.karabela.totp.qr;

import org.karabela.totp.code.HashingAlgorithm;

/**
 * Factory for creating pre-configured {@link QrData.Builder} instances.
 */
public final class QrDataFactory {

    private final HashingAlgorithm defaultAlgorithm;
    private final int defaultDigits;
    private final int defaultTimePeriod;

    public QrDataFactory(HashingAlgorithm defaultAlgorithm, int defaultDigits, int defaultTimePeriod) {
        this.defaultAlgorithm = defaultAlgorithm;
        this.defaultDigits = defaultDigits;
        this.defaultTimePeriod = defaultTimePeriod;
    }

    public QrData.Builder newBuilder() {
        return new QrData.Builder()
                .algorithm(defaultAlgorithm)
                .digits(defaultDigits)
                .period(defaultTimePeriod);
    }
}
