/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator.builtin;

import static org.junit.Assert.*;

import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

/**
 * Units tests for {@link OidIriValidator}
 *
 * @author brightSPARK Labs
 */
public class OidIriValidatorTest {
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final OidIriValidator instance = OidIriValidator.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testValidateTag() throws Exception {
        // TODO: ASN-105 need to mock AsantiAsnData
    }

    @Test
    public void testValidateBytes() throws Exception {
        // TODO: ASN-105 implement
        // test valid
        byte[] bytes = new byte[] {0x00};
        assertEquals(0, instance.validate(bytes).size());

        // test invalid
        /*
        final String errorPrefix = "";
        bytes[0] = new byte[] { 0x00 };
        final ImmutableSet<ByteValidationFailure> failures = instance.validate(bytes);
        assertEquals(1, failures.size());
        final ByteValidationFailure failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals(errorPrefix + b, failure.getFailureReason());
        */

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
