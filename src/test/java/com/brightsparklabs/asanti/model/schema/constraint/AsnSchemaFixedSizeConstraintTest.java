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
public class AsnSchemaFixedSizeConstraintTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testIsMet() throws Exception
    {
        // test minimum
        AsnSchemaFixedSizeConstraint instance = new AsnSchemaFixedSizeConstraint(0);
        assertEquals(true, instance.isMet(new byte[0]));
        assertEquals(false, instance.isMet(new byte[1]));
        assertEquals(false, instance.isMet(new byte[256]));
        assertEquals(false, instance.isMet(new byte[10000]));

        // test large (1 MB)
        instance = new AsnSchemaFixedSizeConstraint(1000000);
        assertEquals(false, instance.isMet(new byte[0]));
        assertEquals(false, instance.isMet(new byte[1]));
        assertEquals(false, instance.isMet(new byte[256]));
        assertEquals(true, instance.isMet(new byte[1000000]));

        // test normal
        instance = new AsnSchemaFixedSizeConstraint(256);
        assertEquals(false, instance.isMet(new byte[0]));
        assertEquals(false, instance.isMet(new byte[1]));
        assertEquals(false, instance.isMet(new byte[255]));
        assertEquals(true, instance.isMet(new byte[256]));

        // test invalid bounds
        instance = new AsnSchemaFixedSizeConstraint(Integer.MIN_VALUE);
        assertEquals(false, instance.isMet(new byte[0]));
        assertEquals(false, instance.isMet(new byte[1]));
        assertEquals(false, instance.isMet(new byte[255]));
        assertEquals(false, instance.isMet(new byte[256]));
    }
}
