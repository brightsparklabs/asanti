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
public class AsnSchemaExactNumericValueConstraintTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaFixedNumericValueConstraint() throws Exception
    {
        // test valid
        new AsnSchemaExactNumericValueConstraint(BigInteger.ZERO);

        // test null
        try
        {
            new AsnSchemaExactNumericValueConstraint(null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
    }

    @Test
    public void testApply() throws Exception
    {
        // test against Long.MIN_VALUE
        final BigInteger minLong = BigInteger.valueOf(Long.MIN_VALUE);
        AsnSchemaExactNumericValueConstraint instance = new AsnSchemaExactNumericValueConstraint(minLong);
        OperationResult<byte[]> result = instance.apply(minLong.toByteArray());
        assertEquals(true, result.wasSuccessful());
        assertEquals("", result.getFailureReason());
        result = instance.apply(minLong.subtract(BigInteger.TEN)
                .toByteArray());
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value of -9223372036854775808, but found: -9223372036854775818",
                result.getFailureReason());
        result = instance.apply(BigInteger.valueOf(Long.MAX_VALUE)
                .add(BigInteger.TEN)
                .toByteArray());
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value of -9223372036854775808, but found: 9223372036854775817",
                result.getFailureReason());

        // test Long.MAX_VALUE
        final BigInteger maxLong = BigInteger.valueOf(Long.MAX_VALUE);
        instance = new AsnSchemaExactNumericValueConstraint(maxLong);
        result = instance.apply(maxLong.toByteArray());
        assertEquals(true, result.wasSuccessful());
        assertEquals("", result.getFailureReason());
        result = instance.apply(maxLong.subtract(BigInteger.TEN)
                .toByteArray());
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value of 9223372036854775807, but found: 9223372036854775797",
                result.getFailureReason());
        result = instance.apply(maxLong.add(BigInteger.TEN)
                .toByteArray());
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value of 9223372036854775807, but found: 9223372036854775817",
                result.getFailureReason());

        // test values outside of Long range
        instance = new AsnSchemaExactNumericValueConstraint(new BigInteger("-92233720368547758080"));
        result = instance.apply(new BigInteger("-92233720368547758080").toByteArray());
        assertEquals(true, result.wasSuccessful());
        assertEquals("", result.getFailureReason());
        result = instance.apply(new BigInteger("92233720368547758070").toByteArray());
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value of -92233720368547758080, but found: 92233720368547758070",
                result.getFailureReason());
        result = instance.apply(new BigInteger("-92233720368547758081").toByteArray());
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value of -92233720368547758080, but found: -92233720368547758081",
                result.getFailureReason());
        result = instance.apply(new BigInteger("92233720368547758071").toByteArray());
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value of -92233720368547758080, but found: 92233720368547758071",
                result.getFailureReason());

        // test invalid array
        result = instance.apply(new byte[0]);
        assertEquals(false, result.wasSuccessful());
        assertEquals("Expected a value of -92233720368547758080, but no value found", result.getFailureReason());
    }
}
