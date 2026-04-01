package org.karabela.totp.spring.autoconfigure;

import org.karabela.totp.code.CodeGenerator;
import org.karabela.totp.code.CodeVerifier;
import org.karabela.totp.code.DefaultCodeGenerator;
import org.karabela.totp.code.HashingAlgorithm;
import org.karabela.totp.qr.QrDataFactory;
import org.karabela.totp.qr.QrGenerator;
import org.karabela.totp.qr.ZxingPngQrGenerator;
import org.karabela.totp.recovery.RecoveryCodeGenerator;
import org.karabela.totp.secret.DefaultSecretGenerator;
import org.karabela.totp.secret.SecretGenerator;
import org.karabela.totp.time.SystemTimeProvider;
import org.karabela.totp.time.TimeProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class TotpAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(TotpAutoConfiguration.class));

    @Test
    void testDefaultBeansAreCreated() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(SecretGenerator.class);
            assertThat(context).hasSingleBean(TimeProvider.class);
            assertThat(context).hasSingleBean(HashingAlgorithm.class);
            assertThat(context).hasSingleBean(QrDataFactory.class);
            assertThat(context).hasSingleBean(QrGenerator.class);
            assertThat(context).hasSingleBean(CodeGenerator.class);
            assertThat(context).hasSingleBean(CodeVerifier.class);
            assertThat(context).hasSingleBean(RecoveryCodeGenerator.class);
        });
    }

    @Test
    void testDefaultBeanTypes() {
        contextRunner.run(context -> {
            assertThat(context.getBean(SecretGenerator.class)).isInstanceOf(DefaultSecretGenerator.class);
            assertThat(context.getBean(TimeProvider.class)).isInstanceOf(SystemTimeProvider.class);
            assertThat(context.getBean(QrGenerator.class)).isInstanceOf(ZxingPngQrGenerator.class);
            assertThat(context.getBean(CodeGenerator.class)).isInstanceOf(DefaultCodeGenerator.class);
            assertThat(context.getBean(HashingAlgorithm.class)).isEqualTo(HashingAlgorithm.SHA1);
        });
    }

    @Test
    void testCustomBeanOverridesDefault() {
        contextRunner
                .withBean(SecretGenerator.class, () -> new DefaultSecretGenerator(64))
                .run(context -> {
                    assertThat(context).hasSingleBean(SecretGenerator.class);
                    // The custom bean should be used, not the auto-configured one
                    String secret = context.getBean(SecretGenerator.class).generate();
                    assertThat(secret).hasSize(64);
                });
    }

    @Test
    void testCustomPropertiesAreApplied() {
        contextRunner
                .withPropertyValues(
                        "totp.secret.length=64",
                        "totp.code.length=8",
                        "totp.time.period=60",
                        "totp.time.discrepancy=2"
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(SecretGenerator.class);
                    String secret = context.getBean(SecretGenerator.class).generate();
                    assertThat(secret).hasSize(64);
                });
    }
}
