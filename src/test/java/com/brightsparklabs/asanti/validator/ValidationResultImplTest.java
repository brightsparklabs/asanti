/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator;

import static org.junit.Assert.*;

import org.junit.Test;

import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.ValidationResultImpl;

public class ValidationResultImplTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /**
     * New definition of test class with dummy abstract methods
     */
    private static class TestInstance extends ValidationResultImpl
    {
        public TestInstance(String tag, boolean isFailure)
        {
            super(tag, isFailure);
        }

        @Override
        public FailureType getFailureType()
        {
            return null;
        }

        @Override
        public String getFailureReason()
        {
            return null;
        }
    }

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testValidationResultImpl() throws Exception
    {
        // test valid
        new TestInstance("TEST_TAG", false);
        new TestInstance("TEST_TAG", false);

        // test null
        try
        {
            new TestInstance(null, false);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test empty
        try
        {
            new TestInstance("", false);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new TestInstance(" ", false);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetTag() throws Exception
    {
        final ValidationResultImpl instance = new TestInstance("TEST_TAG", false);
        assertEquals("TEST_TAG", instance.getTag());
    }

    @Test
    public void testIsFailure() throws Exception
    {
        ValidationResultImpl instance = new TestInstance("TEST_TAG", false);
        assertEquals(false, instance.isFailure());
        instance = new TestInstance("TEST_TAG", true);
        assertEquals(true, instance.isFailure());
    }
}
