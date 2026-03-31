package org.karabela.totp.code;

import org.karabela.totp.exceptions.CodeGenerationException;
import org.karabela.totp.time.TimeProvider;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;

/**
 * Default implementation of {@link CodeVerifier} that checks TOTP codes
 * against a configurable time window.
 *
 * <p>The {@code timePeriod} and {@code allowedTimePeriodDiscrepancy} fields
 * are declared {@code volatile} for safe publication across threads when
 * setters are called during startup. For best thread-safety, configure
 * these values before sharing the instance across threads.</p>
 */
public final class DefaultCodeVerifier implements CodeVerifier {

    private final CodeGenerator codeGenerator;
    private final TimeProvider timeProvider;
    private volatile int timePeriod = 30;
    private volatile int allowedTimePeriodDiscrepancy = 1;

    public DefaultCodeVerifier(CodeGenerator codeGenerator, TimeProvider timeProvider) {
        this.codeGenerator = codeGenerator;
        this.timeProvider = timeProvider;
    }

    public void setTimePeriod(int timePeriod) {
        if (timePeriod < 1) {
            throw new IllegalArgumentException("Time period must be at least 1 second.");
        }
        this.timePeriod = timePeriod;
    }

    public void setAllowedTimePeriodDiscrepancy(int allowedTimePeriodDiscrepancy) {
        if (allowedTimePeriodDiscrepancy < 0) {
            throw new IllegalArgumentException("Allowed time period discrepancy must not be negative.");
        }
        this.allowedTimePeriodDiscrepancy = allowedTimePeriodDiscrepancy;
    }

    @Override
    public boolean isValidCode(String secret, String code) {
        Objects.requireNonNull(secret, "secret must not be null");
        Objects.requireNonNull(code, "code must not be null");

        long currentBucket = Math.floorDiv(timeProvider.getTime(), timePeriod);

        // Check all valid time periods. Always check all windows to avoid timing attacks.
        boolean success = false;
        for (int i = -allowedTimePeriodDiscrepancy; i <= allowedTimePeriodDiscrepancy; i++) {
            success = checkCode(secret, currentBucket + i, code) || success;
        }

        return success;
    }

    private boolean checkCode(String secret, long counter, String code) {
        try {
            String actualCode = codeGenerator.generate(secret, counter);
            return MessageDigest.isEqual(
                    actualCode.getBytes(StandardCharsets.UTF_8),
                    code.getBytes(StandardCharsets.UTF_8)
            );
        } catch (CodeGenerationException e) {
            return false;
        }
    }
}
