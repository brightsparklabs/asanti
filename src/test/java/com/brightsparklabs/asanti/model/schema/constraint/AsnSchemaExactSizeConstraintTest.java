/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.constraint;

import static org.junit.Assert.*;

import com.brightsparklabs.asanti.validator.failure.SchemaConstraintValidationFailure;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

/**
 * Unit tests for {@link AsnSchemaSizeConstraint}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaExactSizeConstraintTest {
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testApply() throws Exception {
        // test minimum
        AsnSchemaExactSizeConstraint instance = new AsnSchemaExactSizeConstraint(0);
        ImmutableSet<SchemaConstraintValidationFailure> failures = instance.apply(new byte[0]);
        assertEquals(true, failures.isEmpty());
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[1], "Expected a value of 0, but found: 1");
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[256], "Expected a value of 0, but found: 256");
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[10000], "Expected a value of 0, but found: 10000");

        // test large (1 MB)
        instance = new AsnSchemaExactSizeConstraint(1000000);
        failures = instance.apply(new byte[1000000]);
        assertEquals(true, failures.isEmpty());
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[0], "Expected a value of 1000000, but found: 0");
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[1], "Expected a value of 1000000, but found: 1");
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[256], "Expected a value of 1000000, but found: 256");

        // test normal
        instance = new AsnSchemaExactSizeConstraint(256);
        failures = instance.apply(new byte[256]);
        assertEquals(true, failures.isEmpty());
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[0], "Expected a value of 256, but found: 0");
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[1], "Expected a value of 256, but found: 1");
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[255], "Expected a value of 256, but found: 255");

        // test invalid bounds
        instance = new AsnSchemaExactSizeConstraint(Integer.MIN_VALUE);
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[0], "Expected a value of -2147483648, but found: 0");
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[1], "Expected a value of -2147483648, but found: 1");
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[255], "Expected a value of -2147483648, but found: 255");
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[256], "Expected a value of -2147483648, but found: 256");
    }
}
