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
public class AsnSchemaNumericValueConstraintTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaNumericValueConstraint() throws Exception
    {
        // test valid
        new AsnSchemaNumericValueConstraint(BigInteger.ZERO, BigInteger.ONE);

        // test null
        try
        {
            new AsnSchemaNumericValueConstraint(null, BigInteger.ONE);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new AsnSchemaNumericValueConstraint(BigInteger.ZERO, null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
    }

    @Test
    public void testIsMet() throws Exception
    {
        // test entire Long range
        AsnSchemaNumericValueConstraint instance =
                new AsnSchemaNumericValueConstraint(BigInteger.valueOf(Long.MIN_VALUE),
                        BigInteger.valueOf(Long.MAX_VALUE));
        // test values within range
        assertEquals(true, instance.isMet(BigInteger.valueOf(Long.MIN_VALUE)
                .toByteArray()));
        assertEquals(true, instance.isMet(BigInteger.valueOf(Long.MIN_VALUE)
                .add(BigInteger.TEN)
                .toByteArray()));
        assertEquals(true, instance.isMet(BigInteger.valueOf(Long.MAX_VALUE)
                .subtract(BigInteger.TEN)
                .toByteArray()));
        assertEquals(true, instance.isMet(BigInteger.valueOf(Long.MAX_VALUE)
                .toByteArray()));
        // test values outside of range
        assertEquals(false, instance.isMet(BigInteger.valueOf(Long.MIN_VALUE)
                .subtract(BigInteger.ONE)
                .toByteArray()));
        assertEquals(false, instance.isMet(BigInteger.valueOf(Long.MAX_VALUE)
                .add(BigInteger.ONE)
                .toByteArray()));

        // test values outside of Long range
        instance =
                new AsnSchemaNumericValueConstraint(new BigInteger("-92233720368547758080"),
                        new BigInteger("92233720368547758070"));
        // test minimum/maximum
        assertEquals(true, instance.isMet(new BigInteger("-92233720368547758080").toByteArray()));
        assertEquals(true, instance.isMet(new BigInteger("92233720368547758070").toByteArray()));
        // test valid
        assertEquals(true, instance.isMet(BigInteger.valueOf(Long.MIN_VALUE)
                .toByteArray()));
        assertEquals(true, instance.isMet(new BigInteger("0").toByteArray()));
        assertEquals(true, instance.isMet(new BigInteger("1000000").toByteArray()));
        assertEquals(true, instance.isMet(BigInteger.valueOf(Long.MAX_VALUE)
                .toByteArray()));
        // test ranges beyond range
        assertEquals(false, instance.isMet(new BigInteger("-92233720368547758081").toByteArray()));
        assertEquals(false, instance.isMet(new BigInteger("92233720368547758071").toByteArray()));

        // test invalid bounds
        instance = new AsnSchemaNumericValueConstraint(new BigInteger("5"), new BigInteger("-5"));
        assertEquals(false, instance.isMet(new BigInteger("-5").toByteArray()));
        assertEquals(false, instance.isMet(new BigInteger("0").toByteArray()));
        assertEquals(false, instance.isMet(new BigInteger("5").toByteArray()));
        assertEquals(false, instance.isMet(new BigInteger("10").toByteArray()));
        assertEquals(false, instance.isMet(new BigInteger("-10").toByteArray()));
    }
}
