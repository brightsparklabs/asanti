/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema.constraint;

import static org.junit.Assert.*;

import org.junit.Test;

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
    public void testIsMet() throws Exception
    {
        // test minimum and maximum possible
        AsnSchemaSizeConstraint instance = new AsnSchemaSizeConstraint(Integer.MIN_VALUE, Integer.MAX_VALUE);
        assertEquals(true, instance.isMet(new byte[0]));
        assertEquals(true, instance.isMet(new byte[1]));
        assertEquals(true, instance.isMet(new byte[256]));
        assertEquals(true, instance.isMet(new byte[1000000]));

        // test lower bound
        instance = new AsnSchemaSizeConstraint(2, Integer.MAX_VALUE);
        assertEquals(false, instance.isMet(new byte[0]));
        assertEquals(false, instance.isMet(new byte[1]));
        assertEquals(true, instance.isMet(new byte[2]));
        assertEquals(true, instance.isMet(new byte[256]));

        // test upper bound
        instance = new AsnSchemaSizeConstraint(Integer.MIN_VALUE, 255);
        assertEquals(true, instance.isMet(new byte[0]));
        assertEquals(true, instance.isMet(new byte[1]));
        assertEquals(true, instance.isMet(new byte[255]));
        assertEquals(false, instance.isMet(new byte[256]));

        // test invalid bounds
        instance = new AsnSchemaSizeConstraint(5, -5);
        assertEquals(false, instance.isMet(new byte[0]));
        assertEquals(false, instance.isMet(new byte[1]));
        assertEquals(false, instance.isMet(new byte[255]));
        assertEquals(false, instance.isMet(new byte[256]));
    }
}
