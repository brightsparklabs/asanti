/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.common;

import com.brightsparklabs.asanti.mocks.validator.MockDecodedTagValidationFailure;
import com.brightsparklabs.assam.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.brightsparklabs.assam.exception.DecodeException;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link DecodeExceptions}
 *
 * @author brightSPARK Labs
 */
public class DecodeExceptionTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testThrowIfHasFailures() throws Exception
    {
        // test with no validation failures
        DecodeExceptions.throwIfHasFailures(ImmutableSet.<DecodedTagValidationFailure>of());

        // test with validation failures
        final DecodedTagValidationFailure failure
                = MockDecodedTagValidationFailure.createFailedValidationResult("TEST_TAG",
                FailureType.UnknownTag,
                "TEST_REASON");
        try
        {
            DecodeExceptions.throwIfHasFailures(ImmutableSet.<DecodedTagValidationFailure>of(failure));
            fail("DecodeExceptions not thrown");
        }
        catch (DecodeException ex)
        {
        }
    }
}
