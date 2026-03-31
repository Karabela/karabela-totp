package org.karabela.totp.spring.autoconfigure;

import org.karabela.totp.code.*;
import org.karabela.totp.qr.QrDataFactory;
import org.karabela.totp.qr.QrGenerator;
import org.karabela.totp.qr.ZxingPngQrGenerator;
import org.karabela.totp.recovery.RecoveryCodeGenerator;
import org.karabela.totp.secret.DefaultSecretGenerator;
import org.karabela.totp.secret.SecretGenerator;
import org.karabela.totp.time.SystemTimeProvider;
import org.karabela.totp.time.TimeProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import org.karabela.totp.KarabelaTotp;

@AutoConfiguration
@ConditionalOnClass(KarabelaTotp.class)
@EnableConfigurationProperties(TotpProperties.class)
public class TotpAutoConfiguration {

    private final TotpProperties props;

    public TotpAutoConfiguration(TotpProperties props) {
        this.props = props;
    }

    @Bean
    @ConditionalOnMissingBean
    public SecretGenerator secretGenerator() {
        return new DefaultSecretGenerator(props.getSecret().getLength());
    }

    @Bean
    @ConditionalOnMissingBean
    public TimeProvider timeProvider() {
        return new SystemTimeProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public HashingAlgorithm hashingAlgorithm() {
        return HashingAlgorithm.SHA1;
    }

    @Bean
    @ConditionalOnMissingBean
    public QrDataFactory qrDataFactory(HashingAlgorithm hashingAlgorithm) {
        return new QrDataFactory(hashingAlgorithm, getCodeLength(), getTimePeriod());
    }

    @Bean
    @ConditionalOnMissingBean
    public QrGenerator qrGenerator() {
        return new ZxingPngQrGenerator();
    }

    @Bean
    @ConditionalOnMissingBean
    public CodeGenerator codeGenerator(HashingAlgorithm algorithm) {
        return new DefaultCodeGenerator(algorithm, getCodeLength());
    }

    @Bean
    @ConditionalOnMissingBean
    public CodeVerifier codeVerifier(CodeGenerator codeGenerator, TimeProvider timeProvider) {
        DefaultCodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        verifier.setTimePeriod(getTimePeriod());
        verifier.setAllowedTimePeriodDiscrepancy(props.getTime().getDiscrepancy());
        return verifier;
    }

    @Bean
    @ConditionalOnMissingBean
    public RecoveryCodeGenerator recoveryCodeGenerator() {
        return new RecoveryCodeGenerator();
    }

    private int getCodeLength() {
        return props.getCode().getLength();
    }

    private int getTimePeriod() {
        return props.getTime().getPeriod();
    }
}
