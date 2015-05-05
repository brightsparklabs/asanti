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
 * Units tests for {@link IntegerValidator}
 *
 * @author brightSPARK Labs
 */
public class IntegerValidatorTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final IntegerValidator instance = IntegerValidator.getInstance();

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
        byte[] bytes = new byte[] { 0x00 };
        assertEquals(0, instance.validate(bytes).size());

        bytes = new byte[] { (byte) 0xff };
        assertEquals(0, instance.validate(bytes).size());

        // TODO ASN-105 (review): do some test for multiple bytes as well. Integer type is unbounded, so try to test some big numbers (e.g. exceeding Long.MAX_VALUE)

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
