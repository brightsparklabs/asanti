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
public class AsnSchemaSizeConstraintTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testApply() throws Exception
    {
        // test minimum and maximum possible
        AsnSchemaSizeConstraint instance = new AsnSchemaSizeConstraint(Integer.MIN_VALUE, Integer.MAX_VALUE);
        OperationResult<byte[]> result = instance.apply(new byte[0]);
        assertEquals(true, result.wasSuccessful());
        result = instance.apply(new byte[1]);
        assertEquals(true, result.wasSuccessful());
        assertEquals("", result.getFailureReason());
        result = instance.apply(new byte[256]);
        assertEquals(true, result.wasSuccessful());
        assertEquals("", result.getFailureReason());
        result = instance.apply(new byte[1000000]);
        assertEquals(true, result.wasSuccessful());
        assertEquals("", result.getFailureReason());

        // test lower bound
        instance = new AsnSchemaSizeConstraint(2, Integer.MAX_VALUE);
        result = instance.apply(new byte[0]);
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value between 2 and 2147483647, but found: 0", result.getFailureReason());
        result = instance.apply(new byte[1]);
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value between 2 and 2147483647, but found: 1", result.getFailureReason());
        result = instance.apply(new byte[2]);
        assertEquals(true, result.wasSuccessful());
        assertEquals("", result.getFailureReason());
        result = instance.apply(new byte[256]);
        assertEquals(true, result.wasSuccessful());
        assertEquals("", result.getFailureReason());

        // test upper bound
        instance = new AsnSchemaSizeConstraint(Integer.MIN_VALUE, 255);
        result = instance.apply(new byte[0]);
        assertEquals(true, result.wasSuccessful());
        assertEquals("", result.getFailureReason());
        result = instance.apply(new byte[1]);
        assertEquals(true, result.wasSuccessful());
        assertEquals("", result.getFailureReason());
        result = instance.apply(new byte[255]);
        assertEquals(true, result.wasSuccessful());
        assertEquals("", result.getFailureReason());
        result = instance.apply(new byte[256]);
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value between -2147483648 and 255, but found: 256", result.getFailureReason());

        // test invalid bounds
        instance = new AsnSchemaSizeConstraint(5, -5);
        result = instance.apply(new byte[0]);
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value between 5 and -5, but found: 0", result.getFailureReason());
        result = instance.apply(new byte[1]);
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value between 5 and -5, but found: 1", result.getFailureReason());
        result = instance.apply(new byte[255]);
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value between 5 and -5, but found: 255", result.getFailureReason());
        result = instance.apply(new byte[256]);
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value between 5 and -5, but found: 256", result.getFailureReason());
    }
}
