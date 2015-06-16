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
 * Units tests for {@link BitStringValidator}
 *
 * @author brightSPARK Labs
 */
public class BitStringValidatorTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final BitStringValidator instance = BitStringValidator.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testValidateBytes() throws Exception
    {
        // test valid
        byte[] bytes = new byte[] { 0x00 };
        assertEquals(0, instance.validate(bytes).size());

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
