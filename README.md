# Karabela TOTP — Time-based One-Time Password Library for Java

[![Maven Central](https://img.shields.io/maven-central/v/io.github.karabela/karabela-totp.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.karabela%22%20AND%20a:%22karabela-totp%22)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java 21+](https://img.shields.io/badge/Java-21%2B-blue.svg)](https://openjdk.org/projects/jdk/21/)

A maintained Java library for generating and verifying TOTP (Time-based One-Time Passwords) for Multi-Factor Authentication. Generates QR codes compatible with Google Authenticator, Microsoft Authenticator, and other TOTP apps.

> **This is an actively maintained fork of [java-totp](https://github.com/samdjstevens/java-totp) by Sam Stevens.** See [CHANGELOG.md](CHANGELOG.md) for all improvements.

## Requirements

- Java 21+

## Installation

### Maven

```xml
<dependency>
    <groupId>io.github.karabela</groupId>
    <artifactId>karabela-totp</artifactId>
    <version>2.0.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'io.github.karabela:karabela-totp:2.0.0'
```

## Spring Boot

For Spring Boot 3.x projects, use the starter for auto-configuration:

```xml
<dependency>
    <groupId>io.github.karabela</groupId>
    <artifactId>karabela-totp-spring-boot-starter</artifactId>
    <version>2.0.0</version>
</dependency>
```

See [Spring Boot Starter documentation](totp-spring-boot-starter/README.md) for details.

## Usage

- [Generating secrets](#generating-a-shared-secret)
- [Generating QR codes](#generating-a-qr-code)
- [Verifying one-time passwords](#verifying-one-time-passwords)
- [Using different time providers](#using-different-time-providers)
- [Recovery codes](#recovery-codes)

### Generating a shared secret

```java
SecretGenerator secretGenerator = new DefaultSecretGenerator();
String secret = secretGenerator.generate();
// secret = "BP26TDZUZ5SVPZJRIHCAUVREO5EWMHHV"
```

By default, secrets are 32 characters long. This is configurable:

```java
SecretGenerator secretGenerator = new DefaultSecretGenerator(64);
```

### Generating a QR code

Create a `QrData` instance with the provisioning information:

```java
QrData data = new QrData.Builder()
    .label("example@example.com")
    .secret(secret)
    .issuer("AppName")
    .algorithm(HashingAlgorithm.SHA1)
    .digits(6)
    .period(30)
    .build();
```

Generate a PNG QR code image:

```java
QrGenerator generator = new ZxingPngQrGenerator();
byte[] imageData = generator.generate(data);
```

Custom image sizes:

```java
QrGenerator generator = new ZxingPngQrGenerator(500); // 500x500 pixels
```

#### Embedding the QR code in HTML

```java
import static org.karabela.totp.util.Utils.getDataUriForImage;

String dataUri = getDataUriForImage(imageData, generator.getImageMimeType());
// dataUri = data:image/png;base64,iVBORw0KGgoAAAANSU...
```

### Verifying one-time passwords

```java
TimeProvider timeProvider = new SystemTimeProvider();
CodeGenerator codeGenerator = new DefaultCodeGenerator();
CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

boolean successful = verifier.isValidCode(secret, code);
```

#### Hashing algorithms

The default is SHA1 (most compatible). SHA256 and SHA512 are also supported:

```java
CodeGenerator codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA512);
```

> **Note:** Most authenticator apps only support SHA1. Using SHA256 or SHA512 may cause compatibility issues with some apps.

#### Time period and discrepancy

Default: 30-second period, ±1 period discrepancy (allows ~30 second clock drift):

```java
DefaultCodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
verifier.setTimePeriod(60);                    // 60-second periods
verifier.setAllowedTimePeriodDiscrepancy(2);   // allow ±2 periods
```

#### Code length

```java
CodeGenerator codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA1, 8); // 8-digit codes
```

### Using different time providers

#### System clock (recommended)

```java
TimeProvider timeProvider = new SystemTimeProvider();
```

#### java.time.Clock (useful for testing)

```java
// Fixed clock for deterministic tests
TimeProvider timeProvider = new ClockTimeProvider(Clock.fixed(instant, ZoneOffset.UTC));

// Or use the system UTC clock
TimeProvider timeProvider = new ClockTimeProvider(Clock.systemUTC());
```

#### NTP server

Requires `commons-net` on the classpath:

```java
TimeProvider timeProvider = new NtpTimeProvider("pool.ntp.org");
TimeProvider timeProvider = new NtpTimeProvider("pool.ntp.org", 5000); // 5s timeout
```

Add the dependency:

```xml
<dependency>
    <groupId>commons-net</groupId>
    <artifactId>commons-net</artifactId>
    <version>3.11.1</version>
</dependency>
```

### Recovery Codes

Generate one-time recovery codes for backup access:

```java
RecoveryCodeGenerator recoveryCodes = new RecoveryCodeGenerator();
String[] codes = recoveryCodes.generateCodes(16);
// codes = ["tf8i-exmo-3lcb-slkm", "boyv-yq75-z99k-r308", ...]
```

Codes are 16 characters (a-z, 0-9), grouped with dashes, providing 82 bits of entropy.

## Running Tests

```bash
mvn test
```

## Migration from java-totp 1.x

Replace the dependency:

```xml
<!-- Old -->
<groupId>dev.samstevens.totp</groupId>
<artifactId>totp</artifactId>

<!-- New -->
<groupId>io.github.karabela</groupId>
<artifactId>karabela-totp</artifactId>
```

Update imports:

```
dev.samstevens.totp.* → org.karabela.totp.*
```

API changes:
- `ZxingPngQrGenerator.setImageSize()` removed — pass size via constructor: `new ZxingPngQrGenerator(500)`
- `InvalidParameterException` replaced with `IllegalArgumentException`
- `QrData.Builder` now validates null values (throws `NullPointerException`)
- `TotpInfo` renamed to `KarabelaTotp`

## License

This project is licensed under the [MIT License](LICENSE).

Original work Copyright (c) 2019 Sam Stevens.
Fork Copyright (c) 2026 Karabela.
