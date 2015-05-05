/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Units tests for {@link VisibleStringValidator}
 *
 * @author brightSPARK Labs
 */
public class VisibleStringValidatorTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final VisibleStringValidator instance = VisibleStringValidator.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testValidateTag() throws Exception
    {
        // TODO: ASN-105 need to mock DecodedAsnData
    }

    @Test
    public void testValidateBytes() throws Exception
    {
        // test valid
        byte[] bytes = new byte[1];
        for (int b = 126; b >= 32; b--)
        {
            bytes[0] = (byte) b;
            assertEquals(0, instance.validate(bytes).size());
        }

        // test invalid
        final String errorPrefix
                = "Supplied bytes do not conform to the VisibleString format. All bytes must be within the range 32 - 126. Supplied bytes contain a byte with value: ";
        for (byte b = Byte.MIN_VALUE; b < 32; b++)
        {
            bytes[0] = b;
            final ImmutableSet<ByteValidationFailure> failures = instance.validate(bytes);
            assertEquals(1, failures.size());
            final ByteValidationFailure failure = failures.iterator().next();
            assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
            assertEquals(errorPrefix + b, failure.getFailureReason());
        }

        // TODO ASN-105 (review): Byte.MAX_VALUE is 127, no need for a loop here
        for (byte b = Byte.MAX_VALUE; b > 126; b--)
        {
            bytes[0] = b;
            final ImmutableSet<ByteValidationFailure> failures = instance.validate(bytes);
            assertEquals(1, failures.size());
            final ByteValidationFailure failure = failures.iterator().next();
            assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
            assertEquals(errorPrefix + b, failure.getFailureReason());
        }

        // test empty
        bytes = new byte[0];
        assertEquals(0, instance.validate(bytes).size());

        // test null
        final ImmutableSet<ByteValidationFailure> failures = instance.validate(null);
        assertEquals(1, failures.size());
        final ByteValidationFailure failure = failures.iterator().next();
        assertEquals(FailureType.DataMissing, failure.getFailureType());
        assertEquals("No bytes present to validate", failure.getFailureReason());
    }
}
