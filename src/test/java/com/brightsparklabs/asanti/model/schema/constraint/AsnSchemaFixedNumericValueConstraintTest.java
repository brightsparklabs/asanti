/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema.constraint;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

/**
 * Unit tests for {@link AsnSchemaNumericValueConstraint}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaFixedNumericValueConstraintTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaFixedNumericValueConstraint() throws Exception
    {
        // test valid
        new AsnSchemaFixedNumericValueConstraint(BigInteger.ZERO);

        // test null
        try
        {
            new AsnSchemaFixedNumericValueConstraint(null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
    }

    @Test
    public void testIsMet() throws Exception
    {
        // test against Long.MIN_VALUE
        AsnSchemaFixedNumericValueConstraint instance =
                new AsnSchemaFixedNumericValueConstraint(BigInteger.valueOf(Long.MIN_VALUE));

        assertEquals(true, instance.isMet(BigInteger.valueOf(Long.MIN_VALUE)
                .toByteArray()));
        assertEquals(false, instance.isMet(BigInteger.valueOf(Long.MIN_VALUE)
                .subtract(BigInteger.TEN)
                .toByteArray()));
        assertEquals(false, instance.isMet(BigInteger.valueOf(Long.MAX_VALUE)
                .add(BigInteger.TEN)
                .toByteArray()));

        // test Long.MAX_VALUE
        instance = new AsnSchemaFixedNumericValueConstraint(BigInteger.valueOf(Long.MIN_VALUE));
        assertEquals(true, instance.isMet(BigInteger.valueOf(Long.MIN_VALUE)
                .toByteArray()));
        assertEquals(false, instance.isMet(BigInteger.valueOf(Long.MIN_VALUE)
                .subtract(BigInteger.TEN)
                .toByteArray()));
        assertEquals(false, instance.isMet(BigInteger.valueOf(Long.MAX_VALUE)
                .add(BigInteger.TEN)
                .toByteArray()));

        // test values outside of Long range
        instance = new AsnSchemaFixedNumericValueConstraint(new BigInteger("-92233720368547758080"));
        assertEquals(true, instance.isMet(new BigInteger("-92233720368547758080").toByteArray()));
        assertEquals(false, instance.isMet(new BigInteger("92233720368547758070").toByteArray()));
        assertEquals(false, instance.isMet(new BigInteger("-92233720368547758081").toByteArray()));
        assertEquals(false, instance.isMet(new BigInteger("92233720368547758071").toByteArray()));
    }
}
