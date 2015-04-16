/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.brightsparklabs.asanti.validator.failure.ValidationFailureImpl;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link DecodedTagValidationFailure}
 *
 * @author brightSPARK Labs
 */
public class ValidationFailureImplTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testValidationResultImpl() throws Exception
    {
        // test valid
        new DecodedTagValidationFailure("TEST_TAG", FailureType.None, "TEST_REASON");

        // test null
        try
        {
            new DecodedTagValidationFailure(null, FailureType.None, "TEST_REASON");
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new DecodedTagValidationFailure("TEST_TAG", null, "TEST_REASON");
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new DecodedTagValidationFailure("TEST_TAG", FailureType.None, null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test empty
        try
        {
            new DecodedTagValidationFailure("", FailureType.None, "TEST_REASON");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new DecodedTagValidationFailure(" ", FailureType.None, "TEST_REASON");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new DecodedTagValidationFailure("TEST_TAG", FailureType.None, "");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new DecodedTagValidationFailure("TEST_TAG", FailureType.None, " ");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetTag() throws Exception
    {
        final DecodedTagValidationFailure instance = new DecodedTagValidationFailure("TEST_TAG", FailureType.None, "TEST_REASON");
        assertEquals("TEST_TAG", instance.getLocation());
    }

    @Test
    public void testGetFailureType() throws Exception
    {
        DecodedTagValidationFailure instance = new DecodedTagValidationFailure("TEST_TAG", FailureType.None, "TEST_REASON");
        assertEquals(FailureType.None, instance.getFailureType());
        instance = new DecodedTagValidationFailure("TEST_TAG", FailureType.MandatoryFieldMissing, "TEST_REASON");
        assertEquals(FailureType.MandatoryFieldMissing, instance.getFailureType());
    }

    @Test
    public void testGetFailureReason() throws Exception
    {
        final DecodedTagValidationFailure instance = new DecodedTagValidationFailure("TEST_TAG", FailureType.None, "TEST_REASON");
        assertEquals("TEST_REASON", instance.getFailureReason());
    }
}
