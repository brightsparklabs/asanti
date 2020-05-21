/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.common;

import static org.junit.Assert.*;

import com.brightsparklabs.asanti.exception.DecodeException;
import com.brightsparklabs.asanti.mocks.validator.MockDecodedTagValidationFailure;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

/**
 * Unit tests for {@link DecodeExceptions}
 *
 * @author brightSPARK Labs
 */
public class DecodeExceptionTest {
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testThrowIfHasFailures() throws Exception {
        // test with no validation failures
        DecodeExceptions.throwIfHasFailures(ImmutableSet.<DecodedTagValidationFailure>of());

        // test with validation failures
        final DecodedTagValidationFailure failure =
                MockDecodedTagValidationFailure.createFailedValidationResult(
                        "TEST_TAG", FailureType.UnknownTag, "TEST_REASON");
        try {
            DecodeExceptions.throwIfHasFailures(ImmutableSet.of(failure));
            fail("DecodeExceptions not thrown");
        } catch (DecodeException ex) {
        }
    }
}
