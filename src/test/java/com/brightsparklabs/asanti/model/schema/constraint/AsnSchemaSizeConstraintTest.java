/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema.constraint;

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
    public void testApply() throws Exception
    {
        // test minimum and maximum possible
        AsnSchemaSizeConstraint instance = new AsnSchemaSizeConstraint(Integer.MIN_VALUE,
                Integer.MAX_VALUE);
        AsnSchemaConstraintTest.checkSuccess(instance, new byte[0]);
        AsnSchemaConstraintTest.checkSuccess(instance, new byte[1]);
        AsnSchemaConstraintTest.checkSuccess(instance, new byte[256]);
        AsnSchemaConstraintTest.checkSuccess(instance, new byte[1000000]);

        // test lower bound
        instance = new AsnSchemaSizeConstraint(2, Integer.MAX_VALUE);
        AsnSchemaConstraintTest.checkFailure(instance,
                new byte[0],
                "Expected a value between 2 and 2147483647, but found: 0");
        AsnSchemaConstraintTest.checkFailure(instance,
                new byte[1],
                "Expected a value between 2 and 2147483647, but found: 1");
        AsnSchemaConstraintTest.checkSuccess(instance, new byte[2]);
        AsnSchemaConstraintTest.checkSuccess(instance, new byte[256]);

        // test upper bound
        instance = new AsnSchemaSizeConstraint(Integer.MIN_VALUE, 255);
        AsnSchemaConstraintTest.checkSuccess(instance, new byte[0]);
        AsnSchemaConstraintTest.checkSuccess(instance, new byte[1]);
        AsnSchemaConstraintTest.checkSuccess(instance, new byte[255]);
        AsnSchemaConstraintTest.checkFailure(instance,
                new byte[256],
                "Expected a value between -2147483648 and 255, but found: 256");

        // test invalid bounds
        instance = new AsnSchemaSizeConstraint(5, -5);
        AsnSchemaConstraintTest.checkFailure(instance,
                new byte[0],
                "Expected a value between 5 and -5, but found: 0");
        AsnSchemaConstraintTest.checkFailure(instance,
                new byte[1],
                "Expected a value between 5 and -5, but found: 1");
        AsnSchemaConstraintTest.checkFailure(instance,
                new byte[255],
                "Expected a value between 5 and -5, but found: 255");
        AsnSchemaConstraintTest.checkFailure(instance,
                new byte[256],
                "Expected a value between 5 and -5, but found: 256");
    }
}
