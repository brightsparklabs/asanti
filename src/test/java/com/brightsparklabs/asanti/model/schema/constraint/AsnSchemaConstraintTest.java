/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema.constraint;

import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.SchemaConstraintValidationFailure;
import com.google.common.collect.ImmutableSet;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link AsnSchemaConstraint}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaConstraintTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /**
     * Utility method to test that applying the supplied constraint to the specified bytes produces
     * no failures
     *
     * @param constraint
     *         constraint to apply
     * @param bytes
     *         bytes to test
     */
    public static void checkSuccess(AsnSchemaConstraint constraint, byte[] bytes)
    {
        final ImmutableSet<SchemaConstraintValidationFailure> failures = constraint.apply(bytes);
        assertEquals(true, failures.isEmpty());
    }

    /**
     * Utility method to test that applying the supplied constraint to the specified bytes produces
     * a single failure
     *
     * @param constraint
     *         constraint to apply
     * @param bytes
     *         bytes to test
     * @param expectedFailureReason
     *         expected failure reason from applying the constraint to the bytes
     */
    public static void checkFailure(AsnSchemaConstraint constraint, byte[] bytes,
            String expectedFailureReason)
    {
        final ImmutableSet<SchemaConstraintValidationFailure> failures = constraint.apply(bytes);
        assertEquals(1, failures.size());
        final SchemaConstraintValidationFailure failure = failures.iterator().next();
        assertEquals(FailureType.SchemaConstraint, failure.getFailureType());
        assertEquals(expectedFailureReason, failure.getFailureReason());
    }
}
