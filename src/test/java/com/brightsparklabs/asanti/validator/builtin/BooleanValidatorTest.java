/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator.builtin;

import static org.junit.Assert.*;

import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.assam.validator.FailureType;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

/**
 * Units tests for {@link BooleanValidator}
 *
 * @author brightSPARK Labs
 */
public class BooleanValidatorTest {
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final BooleanValidator instance = BooleanValidator.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testValidateTag() throws Exception {
        // TODO: ASN-105 need to mock AsantiAsnData
    }

    @Test
    public void testValidateBytes() throws Exception {
        // test valid
        byte[] bytes = new byte[1];
        for (byte b = Byte.MIN_VALUE; b < 0; b++) {
            bytes[0] = b;
            assertEquals(0, instance.validate(bytes).size());
        }
        for (byte b = Byte.MAX_VALUE; b > 0; b--) {
            bytes[0] = b;
            assertEquals(0, instance.validate(bytes).size());
        }

        // test invalid/empty
        bytes = new byte[2];
        ImmutableSet<ByteValidationFailure> failures = instance.validate(bytes);
        assertEquals(1, failures.size());
        ByteValidationFailure failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        final String errorMessage =
                "ASN.1 BOOLEAN type can only contain one byte. Supplied array contains 2 bytes";
        assertEquals(errorMessage, failure.getFailureReason());

        // test null
        failures = instance.validate(null);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.DataMissing, failure.getFailureType());
        assertEquals("No bytes present to validate", failure.getFailureReason());
    }
}
