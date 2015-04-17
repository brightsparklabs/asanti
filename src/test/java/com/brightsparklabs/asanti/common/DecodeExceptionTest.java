/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.common;

import com.brightsparklabs.asanti.mocks.validator.MockDecodedTagValidationFailure;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link DecodeException}
 *
 * @author brightSPARK Labs
 */
public class DecodeExceptionTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecodeException() throws Exception
    {
        DecodeException instance = new DecodeException("");
        assertEquals("", instance.getMessage());
        instance = new DecodeException(null);
        assertEquals(null, instance.getMessage());
        instance = new DecodeException("TEST");
        assertEquals("TEST", instance.getMessage());
    }

    @Test
    public void testThrowIfHasFailures() throws Exception
    {
        // test with no validation failures
        DecodeException.throwIfHasFailures(ImmutableSet.<DecodedTagValidationFailure>of());

        // test with validation failures
        final DecodedTagValidationFailure failure
                = MockDecodedTagValidationFailure.createFailedValidationResult("TEST_TAG",
                FailureType.UnknownTag,
                "TEST_REASON");
        try
        {
            DecodeException.throwIfHasFailures(ImmutableSet.<DecodedTagValidationFailure>of(failure));
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
        }
    }
}
