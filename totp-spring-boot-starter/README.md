# Using Karabela TOTP with Spring Boot

## Installation

Add the starter dependency to your Spring Boot 3.x project:

### Maven

```xml
<dependency>
    <groupId>io.github.karabela</groupId>
    <artifactId>karabela-totp-spring-boot-starter</artifactId>
    <version>2.0.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'io.github.karabela:karabela-totp-spring-boot-starter:2.0.0'
```

## Usage

### Generating QR codes

```java
@Controller
public class MfaSetupController {

    private final SecretGenerator secretGenerator;
    private final QrDataFactory qrDataFactory;
    private final QrGenerator qrGenerator;

    public MfaSetupController(SecretGenerator secretGenerator,
                              QrDataFactory qrDataFactory,
                              QrGenerator qrGenerator) {
        this.secretGenerator = secretGenerator;
        this.qrDataFactory = qrDataFactory;
        this.qrGenerator = qrGenerator;
    }

    @GetMapping("/mfa/setup")
    public String setupDevice() throws QrGenerationException {
        String secret = secretGenerator.generate();

        QrData data = qrDataFactory.newBuilder()
            .label("example@example.com")
            .secret(secret)
            .issuer("AppName")
            .build();

        String qrCodeImage = getDataUriForImage(
            qrGenerator.generate(data),
            qrGenerator.getImageMimeType()
        );
        // ...
    }
}
```

### Verifying a code

```java
@Controller
public class MfaVerifyController {

    private final CodeVerifier verifier;

    public MfaVerifyController(CodeVerifier verifier) {
        this.verifier = verifier;
    }

    @PostMapping("/mfa/verify")
    @ResponseBody
    public String verify(@RequestParam String code) {
        // secret fetched from storage
        if (verifier.isValidCode(secret, code)) {
            return "CORRECT CODE";
        }
        return "INCORRECT CODE";
    }
}
```

### Generating recovery codes

```java
@Controller
public class MfaRecoveryCodesController {

    private final RecoveryCodeGenerator recoveryCodeGenerator;

    public MfaRecoveryCodesController(RecoveryCodeGenerator recoveryCodeGenerator) {
        this.recoveryCodeGenerator = recoveryCodeGenerator;
    }

    @GetMapping("/mfa/recovery-codes")
    public String recoveryCodes() {
        String[] codes = recoveryCodeGenerator.generateCodes(16);
        // ...
    }
}
```

## Configuration

Configure via `application.properties`:

```properties
# Secret length (default: 32)
totp.secret.length=32

# Code length (default: 6)
totp.code.length=6

# Time period in seconds (default: 30)
totp.time.period=30

# Allowed time period discrepancy (default: 1)
totp.time.discrepancy=1
```

### Custom hashing algorithm

```java
@Configuration
public class AppConfig {
    @Bean
    public HashingAlgorithm hashingAlgorithm() {
        return HashingAlgorithm.SHA256;
    }
}
```

### Custom time provider

```java
@Configuration
public class AppConfig {
    @Bean
    public TimeProvider timeProvider() {
        return new NtpTimeProvider("pool.ntp.org");
    }
}
```
