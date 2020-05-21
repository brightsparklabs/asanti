/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator.rule;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.brightsparklabs.asanti.data.AsnData;
import com.brightsparklabs.asanti.model.data.AsantiAsnData;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.ValidationFailure;
import com.google.common.collect.ImmutableSet;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Optional;
import org.hamcrest.core.StringStartsWith;
import org.junit.Test;

/**
 * Units tests for {@link com.brightsparklabs.asanti.validator.rule.IsEqualValidationRule}
 *
 * @author brightSPARK Labs
 */
public class IsEqualValidationRuleTest {
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testIsEqualValidationRuleTest() throws Exception {
        // test {@code null}
        try {
            new IsEqualValidationRule<>(null, String.class);
            fail("NullPointerException not thrown");
        } catch (Exception ex) {
            assertTrue(true);
        }
    }

    @Test
    public void testValidate() throws Exception {
        final String tag = "/tag";
        // test strings
        testValidate(tag, "expectedValue", "unexpectedValue", String.class);
        // test bytes
        testValidate(tag, new byte[] {1, 2, 3, 4, 5}, new byte[] {1}, byte[].class);
        // test timestamp
        testValidate(tag, new Timestamp(0), new Timestamp(1), Timestamp.class);
        // test big integer
        testValidate(tag, BigInteger.ZERO, BigInteger.TEN, BigInteger.class);
        // test boolean
        testValidate(tag, true, false, boolean.class);
    }

    @Test
    public void testTypeMismatch() throws Exception {
        final String tag = "/tag";

        // setup the mock AsnData to throw if we ask it for BigInteger for that tag
        final AsnData mockAsnData = mock(AsantiAsnData.class);
        when(mockAsnData.getDecodedObject(tag, BigInteger.class))
                .thenThrow(new ClassCastException("oops"));

        // setup the Validation to expect and BigInteger for that tag
        IsEqualValidationRule<BigInteger> instance =
                new IsEqualValidationRule<>(BigInteger.ONE, BigInteger.class);
        ImmutableSet<ValidationFailure> failures = instance.validate(tag, mockAsnData);

        assertEquals(failures.size(), 1);
        final ValidationFailure failure = failures.asList().get(0);
        assertEquals(FailureType.CustomValidationFailed, failure.getFailureType());
        assertThat(
                failure.getFailureReason(),
                StringStartsWith.startsWith("Data was not in the expected format"));
        assertEquals(tag, failure.getFailureTag());
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * Tests that the validation rule fires correctly for the given inputs.
     *
     * @param tag tag to validate
     * @param validValue the value the tag should have
     * @param invalidValue any value which differs from {@code validValue}
     * @param <T> the class of {@code validValue} and {@code invalidValue}
     * @throws Exception if any unexpected errors occur
     */
    private <T> void testValidate(String tag, T validValue, T invalidValue, Class<T> classOfT)
            throws Exception {

        // test valid
        final AsnData mockValidAsnData = createMock(tag, validValue, classOfT);
        IsEqualValidationRule<T> instance = new IsEqualValidationRule<>(validValue, classOfT);
        ImmutableSet<ValidationFailure> failures = instance.validate(tag, mockValidAsnData);
        assertEquals(0, failures.size());

        // test invalid
        final AsnData mockInvalidAsnData = createMock(tag, invalidValue, classOfT);
        failures = instance.validate(tag, mockInvalidAsnData);
        assertEquals(failures.size(), 1);
        final ValidationFailure failure = failures.asList().get(0);
        assertEquals(FailureType.CustomValidationFailed, failure.getFailureType());
        assertEquals(
                "Found value: ["
                        + invalidValue.toString()
                        + "] but expected value: ["
                        + validValue.toString()
                        + "]",
                failure.getFailureReason());
        assertEquals(tag, failure.getFailureTag());

        // test no value
        final AsnData mockMissingAsnData = createMock(tag, null, classOfT);
        failures = instance.validate(tag, mockMissingAsnData);
        assertEquals(failures.size(), 1);
        final ValidationFailure missingFailure = failures.asList().get(0);
        assertEquals(FailureType.CustomValidationFailed, missingFailure.getFailureType());
        assertEquals(
                "No value found when expecting value: [" + validValue.toString() + "]",
                missingFailure.getFailureReason());
        assertEquals(tag, missingFailure.getFailureTag());
    }

    /**
     * Creates a mock @{@link AsnData} instance containing a single tag.
     *
     * @param tag the tag the data will contain
     * @param value the value associated with the tag. Pass in null to have no value assigned
     * @return a mocked instance which returns the supplied value when {@link
     *     AsnData#getDecodedObject(String)} is called using the supplied tag, or Optional.empty, if
     *     there is no value
     * @throws Exception if any unexpected errors occur
     */
    private <T> AsnData createMock(String tag, T value, Class<T> classOfT) throws Exception {
        final AsnData mockAsnData = mock(AsantiAsnData.class);
        when(mockAsnData.<T>getDecodedObject(tag, classOfT)).thenReturn(Optional.ofNullable(value));
        return mockAsnData;
    }
}
