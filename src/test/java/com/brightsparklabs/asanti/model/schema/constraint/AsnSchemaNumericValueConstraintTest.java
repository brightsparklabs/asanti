/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema.constraint;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import com.brightsparklabs.asanti.common.OperationResult;

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
        AsnSchemaNumericValueConstraint instance =
                new AsnSchemaNumericValueConstraint(BigInteger.valueOf(Long.MIN_VALUE),
                        BigInteger.valueOf(Long.MAX_VALUE));
        // test values within range
        OperationResult<byte[]> result = instance.apply(BigInteger.valueOf(Long.MIN_VALUE)
                .toByteArray());
        assertEquals(true, result.wasSuccessful());
        assertEquals("", result.getFailureReason());
        result = instance.apply(BigInteger.valueOf(Long.MIN_VALUE)
                .add(BigInteger.TEN)
                .toByteArray());
        assertEquals(true, result.wasSuccessful());
        assertEquals("", result.getFailureReason());
        result = instance.apply(BigInteger.valueOf(Long.MAX_VALUE)
                .subtract(BigInteger.TEN)
                .toByteArray());
        assertEquals(true, result.wasSuccessful());
        assertEquals("", result.getFailureReason());
        result = instance.apply(BigInteger.valueOf(Long.MAX_VALUE)
                .toByteArray());
        assertEquals(true, result.wasSuccessful());
        assertEquals("", result.getFailureReason());
        // test values outside of range
        result = instance.apply(BigInteger.valueOf(Long.MIN_VALUE)
                .subtract(BigInteger.ONE)
                .toByteArray());
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value between -9223372036854775808 and 9223372036854775807, but found: -9223372036854775809",
                result.getFailureReason());
        result = instance.apply(BigInteger.valueOf(Long.MAX_VALUE)
                .add(BigInteger.ONE)
                .toByteArray());
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value between -9223372036854775808 and 9223372036854775807, but found: 9223372036854775808",
                result.getFailureReason());

        // test values outside of Long range
        instance =
                new AsnSchemaNumericValueConstraint(new BigInteger("-92233720368547758080"),
                        new BigInteger("92233720368547758070"));
        // test minimum/maximum
        result = instance.apply(new BigInteger("-92233720368547758080").toByteArray());
        assertEquals(true, result.wasSuccessful());
        assertEquals("", result.getFailureReason());
        result = instance.apply(new BigInteger("92233720368547758070").toByteArray());
        assertEquals(true, result.wasSuccessful());
        assertEquals("", result.getFailureReason());
        // test valid
        result = instance.apply(BigInteger.valueOf(Long.MIN_VALUE)
                .toByteArray());
        assertEquals(true, result.wasSuccessful());
        assertEquals("", result.getFailureReason());
        result = instance.apply(new BigInteger("0").toByteArray());
        assertEquals(true, result.wasSuccessful());
        assertEquals("", result.getFailureReason());
        result = instance.apply(new BigInteger("1000000").toByteArray());
        assertEquals(true, result.wasSuccessful());
        assertEquals("", result.getFailureReason());
        result = instance.apply(BigInteger.valueOf(Long.MAX_VALUE)
                .toByteArray());
        assertEquals(true, result.wasSuccessful());
        assertEquals("", result.getFailureReason());
        // test ranges beyond range
        result = instance.apply(new BigInteger("-92233720368547758081").toByteArray());
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value between -92233720368547758080 and 92233720368547758070, but found: -92233720368547758081",
                result.getFailureReason());
        result = instance.apply(new BigInteger("92233720368547758071").toByteArray());
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value between -92233720368547758080 and 92233720368547758070, but found: 92233720368547758071",
                result.getFailureReason());

        // test invalid bounds
        instance = new AsnSchemaNumericValueConstraint(new BigInteger("5"), new BigInteger("-5"));
        result = instance.apply(new BigInteger("-5").toByteArray());
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value between 5 and -5, but found: -5", result.getFailureReason());
        result = instance.apply(new BigInteger("0").toByteArray());
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value between 5 and -5, but found: 0", result.getFailureReason());
        result = instance.apply(new BigInteger("5").toByteArray());
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value between 5 and -5, but found: 5", result.getFailureReason());
        result = instance.apply(new BigInteger("10").toByteArray());
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value between 5 and -5, but found: 10", result.getFailureReason());
        result = instance.apply(new BigInteger("-10").toByteArray());
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value between 5 and -5, but found: -10", result.getFailureReason());
        // test invalid array
        result = instance.apply(new byte[0]);
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value between 5 and -5, but no value found", result.getFailureReason());

    }
}
