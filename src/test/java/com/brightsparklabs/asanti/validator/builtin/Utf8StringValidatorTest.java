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
 * Units tests for {@link Utf8StringValidator}
 *
 * @author brightSPARK Labs
 */
public class Utf8StringValidatorTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final Utf8StringValidator instance = Utf8StringValidator.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testValidateTag() throws Exception
    {
        // TODO: ASN-118 need to mock DecodedAsnData
    }

    @Test
    public void testValidateBytes() throws Exception
    {
        // test valid - one byte (all ASCII characters)
        byte[] bytes = new byte[1];
        for (byte b = Byte.MAX_VALUE; b >= 0; b--)
        {
            bytes[0] = b;
            assertEquals(0, instance.validate(bytes).size());
        }

        // test valid - two bytes (minimum/maximum)
        bytes = new byte[] { (byte) 0b11000010, (byte) 0b10000000 };
        assertEquals(0, instance.validate(bytes).size());
        bytes = new byte[] { (byte) 0b11011111, (byte) 0b10111111 };
        assertEquals(0, instance.validate(bytes).size());

        // test valid - three bytes (minimum/maximum)
        bytes = new byte[] { (byte) 0b11100010, (byte) 0b10000000, (byte) 0b10000000 };
        assertEquals(0, instance.validate(bytes).size());
        bytes = new byte[] { (byte) 0b11101111, (byte) 0b10111111, (byte) 0b10111111 };
        assertEquals(0, instance.validate(bytes).size());

        // test valid - four bytes (minimum/maximum)
        bytes = new byte[] { (byte) 0b11110010, (byte) 0b10000000, (byte) 0b10000000,
                             (byte) 0b10000000 };
        assertEquals(0, instance.validate(bytes).size());
        bytes = new byte[] { (byte) 0b11110000, (byte) 0b10111111, (byte) 0b10111111,
                             (byte) 0b10111111 };
        assertEquals(0, instance.validate(bytes).size());

        // test invalid - leading byte requires two trailing bytes
        bytes = new byte[] { (byte) 0b11000000 };
        ImmutableSet<ByteValidationFailure> failures = instance.validate(bytes);
        assertEquals(1, failures.size());
        ByteValidationFailure failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals("ASN.1 UTF8String type must be encoded in UTF-8", failure.getFailureReason());

        // test invalid - leading byte requires three trailing bytes
        bytes = new byte[] { (byte) 0b11100000 };
        failures = instance.validate(bytes);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals("ASN.1 UTF8String type must be encoded in UTF-8", failure.getFailureReason());
        bytes = new byte[] { (byte) 0b11100000, (byte) 0b10000000 };
        failures = instance.validate(bytes);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals("ASN.1 UTF8String type must be encoded in UTF-8", failure.getFailureReason());

        // test invalid - leading byte requires four trailing bytes
        bytes = new byte[] { (byte) 0b11110000 };
        failures = instance.validate(bytes);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals("ASN.1 UTF8String type must be encoded in UTF-8", failure.getFailureReason());
        bytes = new byte[] { (byte) 0b11110000, (byte) 0b10000000, (byte) 0b10000000 };
        failures = instance.validate(bytes);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals("ASN.1 UTF8String type must be encoded in UTF-8", failure.getFailureReason());

        // test invalid - leading byte requires five trailing bytes
        bytes = new byte[] { (byte) 0b11111000 };
        failures = instance.validate(bytes);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals("ASN.1 UTF8String type must be encoded in UTF-8", failure.getFailureReason());
        bytes = new byte[] { (byte) 0b11111000, (byte) 0b10000000, (byte) 0b10000000,
                             (byte) 0b10000000 };
        failures = instance.validate(bytes);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals("ASN.1 UTF8String type must be encoded in UTF-8", failure.getFailureReason());

        // test invalid - leading byte requires six trailing bytes
        bytes = new byte[] { (byte) 0b11111100 };
        failures = instance.validate(bytes);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals("ASN.1 UTF8String type must be encoded in UTF-8", failure.getFailureReason());
        bytes = new byte[] { (byte) 0b11111100, (byte) 0b10000000, (byte) 0b10000000,
                             (byte) 0b10000000, (byte) 0b10000000 };
        failures = instance.validate(bytes);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals("ASN.1 UTF8String type must be encoded in UTF-8", failure.getFailureReason());

        // test empty
        bytes = new byte[0];
        assertEquals(0, instance.validate(bytes).size());

        // test null
        failures = instance.validate(null);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.DataMissing, failure.getFailureType());
        assertEquals("No data present", failure.getFailureReason());
    }
}
