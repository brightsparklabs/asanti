/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema.constraint;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

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
    public void testApply() throws Exception
    {
        // test entire Long range
        AsnSchemaNumericValueConstraint instance
                = new AsnSchemaNumericValueConstraint(BigInteger.valueOf(Long.MIN_VALUE),
                BigInteger.valueOf(Long.MAX_VALUE));
        // test values within range
        AsnSchemaConstraintTest.checkSuccess(instance,
                BigInteger.valueOf(Long.MIN_VALUE).toByteArray());
        AsnSchemaConstraintTest.checkSuccess(instance,
                BigInteger.valueOf(Long.MIN_VALUE).add(BigInteger.TEN).toByteArray());
        AsnSchemaConstraintTest.checkSuccess(instance,
                BigInteger.valueOf(Long.MAX_VALUE).subtract(BigInteger.TEN).toByteArray());
        AsnSchemaConstraintTest.checkSuccess(instance,
                BigInteger.valueOf(Long.MAX_VALUE).toByteArray());
        // test values outside of range
        AsnSchemaConstraintTest.checkFailure(instance,
                BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE).toByteArray(),
                "Expected a value between -9223372036854775808 and 9223372036854775807, but found: -9223372036854775809");
        AsnSchemaConstraintTest.checkFailure(instance,
                BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE).toByteArray(),
                "Expected a value between -9223372036854775808 and 9223372036854775807, but found: 9223372036854775808");

        // test values outside of Long range
        instance = new AsnSchemaNumericValueConstraint(new BigInteger("-92233720368547758080"),
                new BigInteger("92233720368547758070"));
        // test minimum/maximum
        AsnSchemaConstraintTest.checkSuccess(instance,
                new BigInteger("-92233720368547758080").toByteArray());
        AsnSchemaConstraintTest.checkSuccess(instance,
                new BigInteger("92233720368547758070").toByteArray());
        // test valid
        AsnSchemaConstraintTest.checkSuccess(instance,
                BigInteger.valueOf(Long.MIN_VALUE).toByteArray());
        AsnSchemaConstraintTest.checkSuccess(instance, new BigInteger("0").toByteArray());
        AsnSchemaConstraintTest.checkSuccess(instance, new BigInteger("1000000").toByteArray());
        AsnSchemaConstraintTest.checkSuccess(instance,
                BigInteger.valueOf(Long.MAX_VALUE).toByteArray());
        // test ranges beyond range
        AsnSchemaConstraintTest.checkFailure(instance,
                new BigInteger("-92233720368547758081").toByteArray(),
                "Expected a value between -92233720368547758080 and 92233720368547758070, but found: -92233720368547758081");
        AsnSchemaConstraintTest.checkFailure(instance,
                new BigInteger("92233720368547758071").toByteArray(),
                "Expected a value between -92233720368547758080 and 92233720368547758070, but found: 92233720368547758071");

        // test invalid bounds
        instance = new AsnSchemaNumericValueConstraint(new BigInteger("5"), new BigInteger("-5"));
        AsnSchemaConstraintTest.checkFailure(instance,
                new BigInteger("-5").toByteArray(),
                "Expected a value between 5 and -5, but found: -5");
        AsnSchemaConstraintTest.checkFailure(instance,
                new BigInteger("0").toByteArray(),
                "Expected a value between 5 and -5, but found: 0");
        AsnSchemaConstraintTest.checkFailure(instance,
                new BigInteger("5").toByteArray(),
                "Expected a value between 5 and -5, but found: 5");
        AsnSchemaConstraintTest.checkFailure(instance,
                new BigInteger("10").toByteArray(),
                "Expected a value between 5 and -5, but found: 10");
        AsnSchemaConstraintTest.checkFailure(instance,
                new BigInteger("-10").toByteArray(),
                "Expected a value between 5 and -5, but found: -10");
        // test invalid array
        AsnSchemaConstraintTest.checkFailure(instance,
                new byte[0],
                "Expected a value between 5 and -5, but no value found");

    }
}
