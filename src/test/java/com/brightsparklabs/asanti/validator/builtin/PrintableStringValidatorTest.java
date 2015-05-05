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
 * Units tests for {@link PrintableStringValidator}
 *
 * @author brightSPARK Labs
 */
public class PrintableStringValidatorTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final PrintableStringValidator instance = PrintableStringValidator.getInstance();

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
        // TODO ASN-105 (review): this is unnecessary long, just use for(byte b = 32; b < 127; b++)
        // test valid
        byte[] bytes = new byte[1];
        for (int b = 'Z'; b >= 'A'; b--)
        {
            bytes[0] = (byte) b;
            assertEquals(0, instance.validate(bytes).size());
        }
        for (int b = 'z'; b >= 'a'; b--)
        {
            bytes[0] = (byte) b;
            assertEquals(0, instance.validate(bytes).size());
        }
        for (int b = '9'; b >= '0'; b--)
        {
            bytes[0] = (byte) b;
            assertEquals(0, instance.validate(bytes).size());
        }
        bytes[0] = (byte) ' ';
        assertEquals(0, instance.validate(bytes).size());

        bytes[0] = (byte) '\'';
        assertEquals(0, instance.validate(bytes).size());

        bytes[0] = (byte) '(';
        assertEquals(0, instance.validate(bytes).size());

        bytes[0] = (byte) ')';
        assertEquals(0, instance.validate(bytes).size());

        bytes[0] = (byte) '+';
        assertEquals(0, instance.validate(bytes).size());

        bytes[0] = (byte) ',';
        assertEquals(0, instance.validate(bytes).size());

        bytes[0] = (byte) '-';
        assertEquals(0, instance.validate(bytes).size());

        bytes[0] = (byte) '.';
        assertEquals(0, instance.validate(bytes).size());

        bytes[0] = (byte) '/';
        assertEquals(0, instance.validate(bytes).size());

        bytes[0] = (byte) ':';
        assertEquals(0, instance.validate(bytes).size());

        bytes[0] = (byte) '=';
        assertEquals(0, instance.validate(bytes).size());

        bytes[0] = (byte) '?';
        assertEquals(0, instance.validate(bytes).size());

        // test invalid
        final String errorPrefix = "Supplied bytes do not conform to the PrintableString format.";

        for (byte b = Byte.MIN_VALUE; b < 32; b++)
        {
            bytes[0] = b;
            final ImmutableSet<ByteValidationFailure> failures = instance.validate(bytes);
            assertEquals(1, failures.size());
            final ByteValidationFailure failure = failures.iterator().next();
            assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
            assertEquals(errorPrefix, failure.getFailureReason());
        }

        // TODO ASN-105 (review): also test 127 (Byte.MAX_VALUE) as it is invalid

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
