# Changelog

## 2.0.0 (2026-04-01) — Initial Karabela Release

Actively maintained fork of [java-totp](https://github.com/samdjstevens/java-totp) v1.7.1.

### Breaking Changes

- **Java 21+ required** (was Java 8)
- **Package renamed:** `dev.samstevens.totp.*` → `org.karabela.totp.*`
- **Maven coordinates changed:** `io.github.karabela:karabela-totp` (was `dev.samstevens.totp:totp`)
- **`ZxingPngQrGenerator`**: removed `setImageSize()` — image size is now set via constructor (`new ZxingPngQrGenerator(500)`)
- **`QrData.Builder`**: now validates null values on `label()`, `secret()`, `issuer()` and requires `secret` on `build()`
- **`TotpInfo`** class renamed to **`KarabelaTotp`**
- **Exception types**: `InvalidParameterException` replaced with `IllegalArgumentException` throughout
- All concrete classes are now `final`

### Security Fixes

- **Fixed CVE in transitive dependency** (issues [#66](https://github.com/samdjstevens/java-totp/issues/66), [#55](https://github.com/samdjstevens/java-totp/issues/55)): upgraded ZXing from 3.4.0 to 3.5.3, removing vulnerable jcommander 1.72 (WS-2019-0490, score 8.1)
- **Fixed CVE-2021-37533** in commons-net: upgraded from 3.6 to 3.11.1 (SSRF vulnerability, score 6.5)
- **Constant-time code comparison**: replaced custom `timeSafeStringComparison()` with `MessageDigest.isEqual()` in `DefaultCodeVerifier`
- **Explicit charset encoding**: all `String ↔ byte[]` conversions now use `StandardCharsets.UTF_8`

### Bug Fixes

- **Fixed division by zero** (issue [#61](https://github.com/samdjstevens/java-totp/issues/61)): `setAllowedTimePeriodDiscrepancy(0)` no longer throws `ArithmeticException`; validation rejects negative values and `setTimePeriod()` rejects zero/negative values
- **Fixed floating-point modulo** in `DefaultCodeGenerator`: replaced `Math.pow(10, digits)` (double) with integer arithmetic to avoid precision loss
- **Fixed mutable `VERSION` field** in `TotpInfo` (now `KarabelaTotp.VERSION` is `static final`)
- **Fixed mutable static fields** in `Utils` (Base64 codec is now properly `final`)

### Improvements

- **All classes marked `final`** (issue [#24](https://github.com/samdjstevens/java-totp/issues/24)): follows Java Secure Coding Guidelines to prevent malicious subclassing
- **New `ClockTimeProvider`** (issue [#28](https://github.com/samdjstevens/java-totp/issues/28)): time provider backed by `java.time.Clock`, useful for deterministic testing
- **Immutable `ZxingPngQrGenerator`**: image size set at construction time (no more mutable setter)
- **Immutable `QrDataFactory`**: fields are now `final`
- **Input validation**: `DefaultSecretGenerator` validates character count, `DefaultCodeVerifier` validates time period and discrepancy

### Spring Boot 3 Support

- **Fixed Spring Boot 3 compatibility** (issues [#53](https://github.com/samdjstevens/java-totp/issues/53), [#58](https://github.com/samdjstevens/java-totp/issues/58)): replaced `spring.factories` (removed in Boot 3) with `AutoConfiguration.imports`
- **Upgraded Spring Boot starter** from 2.2.5 to 3.4.3
- **Uses `@AutoConfiguration`** annotation instead of `@Configuration`
- **Removed `@Autowired`** from constructor (implicit since Spring 4.3)

### Modernization

- **Java 21 target**: uses `URLEncoder.encode(String, Charset)`, `java.util.Base64` (replaces commons-codec Base64), `MessageDigest.isEqual()`
- **JPMS support** (issue [#65](https://github.com/samdjstevens/java-totp/issues/65)): added `module-info.java`
- **Dependency updates:**
  - commons-codec: 1.13 → 1.17.1
  - commons-net: 3.6 → 3.11.1 (optional)
  - ZXing: 3.4.0 → 3.5.3
  - JUnit Jupiter: 5.6.0 → 5.11.4
  - Mockito: 3.2.4 → 5.14.2
  - JaCoCo: 0.8.5 → 0.8.13
  - Spring Boot (starter): 2.2.5 → 3.4.3
  - All Maven plugins updated to latest versions

### Addresses Original Issues

| Original Issue | Resolution |
|----------------|------------|
| [#24](https://github.com/samdjstevens/java-totp/issues/24) Limit extensibility (make final) | All concrete classes are `final` |
| [#28](https://github.com/samdjstevens/java-totp/issues/28) TimeProvider with Clock | New `ClockTimeProvider` class |
| [#30](https://github.com/samdjstevens/java-totp/issues/30) SHA256/SHA512 compatibility warning | Added note in README and `HashingAlgorithm` javadoc |
| [#53](https://github.com/samdjstevens/java-totp/issues/53) Spring Boot 3 auto-config broken | Fixed with `AutoConfiguration.imports` |
| [#55](https://github.com/samdjstevens/java-totp/issues/55) Vulnerable dependency (jcommander) | Fixed by upgrading ZXing to 3.5.3 |
| [#58](https://github.com/samdjstevens/java-totp/issues/58) Spring Boot 3 support | Full Spring Boot 3.4.x support |
| [#61](https://github.com/samdjstevens/java-totp/issues/61) ArithmeticException: / by zero | Fixed with input validation |
| [#62](https://github.com/samdjstevens/java-totp/issues/62) SecureRandom thread contention | Addressed — `SecureRandom` instance per generator, injectable via constructor |
| [#65](https://github.com/samdjstevens/java-totp/issues/65) JPMS support | Added `module-info.java` |
| [#66](https://github.com/samdjstevens/java-totp/issues/66) Vulnerable dependency | Fixed by upgrading ZXing to 3.5.3 |
