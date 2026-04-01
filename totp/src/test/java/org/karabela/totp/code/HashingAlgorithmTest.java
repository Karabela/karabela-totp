package org.karabela.totp.code;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class HashingAlgorithmTest {

    @ParameterizedTest
    @MethodSource("algorithmProvider")
    void testHmacAlgorithm(HashingAlgorithm algorithm, String expectedHmac, String expectedFriendly) {
        assertEquals(expectedHmac, algorithm.getHmacAlgorithm());
        assertEquals(expectedFriendly, algorithm.getFriendlyName());
    }

    static Stream<Arguments> algorithmProvider() {
        return Stream.of(
                arguments(HashingAlgorithm.SHA1, "HmacSHA1", "SHA1"),
                arguments(HashingAlgorithm.SHA256, "HmacSHA256", "SHA256"),
                arguments(HashingAlgorithm.SHA512, "HmacSHA512", "SHA512")
        );
    }

    @Test
    void testAllValuesAreCovered() {
        assertEquals(3, HashingAlgorithm.values().length);
    }

    @Test
    void testValueOf() {
        assertEquals(HashingAlgorithm.SHA1, HashingAlgorithm.valueOf("SHA1"));
        assertEquals(HashingAlgorithm.SHA256, HashingAlgorithm.valueOf("SHA256"));
        assertEquals(HashingAlgorithm.SHA512, HashingAlgorithm.valueOf("SHA512"));
    }
}
