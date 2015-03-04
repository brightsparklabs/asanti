/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for {@link ValidationFailureImpl}
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
        new ValidationFailureImpl("TEST_TAG", FailureType.None, "TEST_REASON");

        // test null
        try
        {
            new ValidationFailureImpl(null, FailureType.None, "TEST_REASON");
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new ValidationFailureImpl("TEST_TAG", null, "TEST_REASON");
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new ValidationFailureImpl("TEST_TAG", FailureType.None, null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test empty
        try
        {
            new ValidationFailureImpl("", FailureType.None, "TEST_REASON");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new ValidationFailureImpl(" ", FailureType.None, "TEST_REASON");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new ValidationFailureImpl("TEST_TAG", FailureType.None, "");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new ValidationFailureImpl("TEST_TAG", FailureType.None, " ");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetTag() throws Exception
    {
        final ValidationFailureImpl instance = new ValidationFailureImpl("TEST_TAG", FailureType.None, "TEST_REASON");
        assertEquals("TEST_TAG", instance.getTag());
    }

    @Test
    public void testGetFailureType() throws Exception
    {
        ValidationFailureImpl instance = new ValidationFailureImpl("TEST_TAG", FailureType.None, "TEST_REASON");
        assertEquals(FailureType.None, instance.getFailureType());
        instance = new ValidationFailureImpl("TEST_TAG", FailureType.MandatoryFieldMissing, "TEST_REASON");
        assertEquals(FailureType.MandatoryFieldMissing, instance.getFailureType());
    }

    @Test
    public void testGetFailureReason() throws Exception
    {
        final ValidationFailureImpl instance = new ValidationFailureImpl("TEST_TAG", FailureType.None, "TEST_REASON");
        assertEquals("TEST_REASON", instance.getFailureReason());
    }
}
