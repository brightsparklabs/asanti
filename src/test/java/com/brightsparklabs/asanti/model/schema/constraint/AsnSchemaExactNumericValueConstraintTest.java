/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
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
public class AsnSchemaExactNumericValueConstraintTest {
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaFixedNumericValueConstraint() throws Exception {
        // test valid
        new AsnSchemaExactNumericValueConstraint(BigInteger.ZERO);

        // test null
        try {
            new AsnSchemaExactNumericValueConstraint(null);
            fail("NullPointerException not thrown");
        } catch (final NullPointerException ex) {
        }
    }

    @Test
    public void testApply() throws Exception {
        // test against Long.MIN_VALUE
        final BigInteger minLong = BigInteger.valueOf(Long.MIN_VALUE);
        AsnSchemaExactNumericValueConstraint instance =
                new AsnSchemaExactNumericValueConstraint(minLong);
        AsnSchemaConstraintTest.checkSuccess(instance, minLong.toByteArray());
        AsnSchemaConstraintTest.checkFailure(
                instance,
                minLong.subtract(BigInteger.TEN).toByteArray(),
                "Expected a value of -9223372036854775808, but found: -9223372036854775818");
        AsnSchemaConstraintTest.checkFailure(
                instance,
                BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.TEN).toByteArray(),
                "Expected a value of -9223372036854775808, but found: 9223372036854775817");

        // test Long.MAX_VALUE
        final BigInteger maxLong = BigInteger.valueOf(Long.MAX_VALUE);
        instance = new AsnSchemaExactNumericValueConstraint(maxLong);
        AsnSchemaConstraintTest.checkSuccess(instance, maxLong.toByteArray());
        AsnSchemaConstraintTest.checkFailure(
                instance,
                maxLong.subtract(BigInteger.TEN).toByteArray(),
                "Expected a value of 9223372036854775807, but found: 9223372036854775797");
        AsnSchemaConstraintTest.checkFailure(
                instance,
                maxLong.add(BigInteger.TEN).toByteArray(),
                "Expected a value of 9223372036854775807, but found: 9223372036854775817");

        // test values outside of Long range
        instance =
                new AsnSchemaExactNumericValueConstraint(new BigInteger("-92233720368547758080"));
        AsnSchemaConstraintTest.checkSuccess(
                instance, new BigInteger("-92233720368547758080").toByteArray());
        AsnSchemaConstraintTest.checkFailure(
                instance,
                new BigInteger("92233720368547758070").toByteArray(),
                "Expected a value of -92233720368547758080, but found: 92233720368547758070");
        AsnSchemaConstraintTest.checkFailure(
                instance,
                new BigInteger("-92233720368547758081").toByteArray(),
                "Expected a value of -92233720368547758080, but found: -92233720368547758081");
        AsnSchemaConstraintTest.checkFailure(
                instance,
                new BigInteger("92233720368547758071").toByteArray(),
                "Expected a value of -92233720368547758080, but found: 92233720368547758071");

        // test invalid array
        AsnSchemaConstraintTest.checkFailure(
                instance,
                new byte[0],
                "Expected a value of -92233720368547758080, but no value found");
    }
}
