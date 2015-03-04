/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema.constraint;

import static org.junit.Assert.*;

import org.junit.Test;

import com.brightsparklabs.asanti.common.OperationResult;

/**
 * Unit tests for {@link AsnSchemaSizeConstraint}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaExactSizeConstraintTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testApply() throws Exception
    {
        // test minimum
        AsnSchemaExactSizeConstraint instance = new AsnSchemaExactSizeConstraint(0);
        OperationResult<byte[]> result = instance.apply(new byte[0]);
        assertEquals(true, result.wasSuccessful());
        assertEquals("", result.getFailureReason());
        result = instance.apply(new byte[1]);
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value of 0, but found: 1", result.getFailureReason());
        result = instance.apply(new byte[256]);
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value of 0, but found: 256", result.getFailureReason());
        result = instance.apply(new byte[10000]);
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value of 0, but found: 10000", result.getFailureReason());

        // test large (1 MB)
        instance = new AsnSchemaExactSizeConstraint(1000000);
        result = instance.apply(new byte[1000000]);
        assertEquals(true, result.wasSuccessful());
        assertEquals("", result.getFailureReason());
        result = instance.apply(new byte[0]);
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value of 1000000, but found: 0", result.getFailureReason());
        result = instance.apply(new byte[1]);
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value of 1000000, but found: 1", result.getFailureReason());
        result = instance.apply(new byte[256]);
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value of 1000000, but found: 256", result.getFailureReason());

        // test normal
        instance = new AsnSchemaExactSizeConstraint(256);
        result = instance.apply(new byte[256]);
        assertEquals(true, result.wasSuccessful());
        assertEquals("", result.getFailureReason());
        result = instance.apply(new byte[0]);
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value of 256, but found: 0", result.getFailureReason());
        result = instance.apply(new byte[1]);
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value of 256, but found: 1", result.getFailureReason());
        result = instance.apply(new byte[255]);
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value of 256, but found: 255", result.getFailureReason());

        // test invalid bounds
        instance = new AsnSchemaExactSizeConstraint(Integer.MIN_VALUE);
        result = instance.apply(new byte[0]);
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value of -2147483648, but found: 0", result.getFailureReason());
        result = instance.apply(new byte[1]);
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value of -2147483648, but found: 1", result.getFailureReason());
        result = instance.apply(new byte[255]);
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value of -2147483648, but found: 255", result.getFailureReason());
        result = instance.apply(new byte[256]);
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value of -2147483648, but found: 256", result.getFailureReason());
    }
}
