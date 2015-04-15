/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.common;

import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.ValidationResult;
import com.brightsparklabs.asanti.validator.ValidationResultImpl;
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
        ValidationResult validationResult = ValidationResultImpl.builder().build();
        DecodeException.throwIfHasFailures(validationResult);

        // test with validation failures
        validationResult =
                ValidationResultImpl.builder()
                        .add("TEST_LOCATION", FailureType.DataIncorrectlyFormatted, "TEST_REASON")
                        .build();
        try
        {
            DecodeException.throwIfHasFailures(validationResult);
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
        }
    }
}
