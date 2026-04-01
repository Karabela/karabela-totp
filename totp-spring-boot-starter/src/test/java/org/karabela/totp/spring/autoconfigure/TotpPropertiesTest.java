package org.karabela.totp.spring.autoconfigure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TotpPropertiesTest {

    @Test
    void testDefaultValues() {
        TotpProperties props = new TotpProperties();

        assertEquals(32, props.getSecret().getLength());
        assertEquals(6, props.getCode().getLength());
        assertEquals(30, props.getTime().getPeriod());
        assertEquals(1, props.getTime().getDiscrepancy());
    }

    @Test
    void testSecretLengthSetterGetter() {
        TotpProperties props = new TotpProperties();
        props.getSecret().setLength(64);
        assertEquals(64, props.getSecret().getLength());
    }

    @Test
    void testCodeLengthSetterGetter() {
        TotpProperties props = new TotpProperties();
        props.getCode().setLength(8);
        assertEquals(8, props.getCode().getLength());
    }

    @Test
    void testTimePeriodSetterGetter() {
        TotpProperties props = new TotpProperties();
        props.getTime().setPeriod(60);
        assertEquals(60, props.getTime().getPeriod());
    }

    @Test
    void testTimeDiscrepancySetterGetter() {
        TotpProperties props = new TotpProperties();
        props.getTime().setDiscrepancy(2);
        assertEquals(2, props.getTime().getDiscrepancy());
    }
}
