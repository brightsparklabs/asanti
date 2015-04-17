/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

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
 * Units tests for {@link Ia5StringValidator}
 *
 * @author brightSPARK Labs
 */
public class Ia5StringValidatorTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final Ia5StringValidator instance = Ia5StringValidator.getInstance();

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
        for (int b = 127; b >= 0; b--)
        {
            bytes[0] = (byte) b;
            assertEquals(0, instance.validate(bytes).size());
        }

        // test invalid
        final String errorPrefix
                = "Supplied bytes do not conform to the IA5String format. All bytes must be within the range 0 - 127. Supplied bytes contain a byte with value: ";
        for (byte b = Byte.MIN_VALUE; b < 0; b++)
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
        assertEquals("No data present", failure.getFailureReason());
    }
}
