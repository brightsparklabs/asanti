/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.constraint;

import static org.junit.Assert.*;

import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveTypeBitString;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.SchemaConstraintValidationFailure;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

/**
 * Unit tests for {@link AsnSchemaSizeConstraint}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaSizeConstraintTest {
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testApply() throws Exception {
        // test minimum and maximum possible
        AsnSchemaSizeConstraint instance =
                new AsnSchemaSizeConstraint(Integer.MIN_VALUE, Integer.MAX_VALUE);
        AsnSchemaConstraintTest.checkSuccess(instance, new byte[0]);
        AsnSchemaConstraintTest.checkSuccess(instance, new byte[1]);
        AsnSchemaConstraintTest.checkSuccess(instance, new byte[256]);
        AsnSchemaConstraintTest.checkSuccess(instance, new byte[1000000]);

        // test lower bound
        instance = new AsnSchemaSizeConstraint(2, Integer.MAX_VALUE);
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[0], "Expected a value between 2 and 2147483647, but found: 0");
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[1], "Expected a value between 2 and 2147483647, but found: 1");
        AsnSchemaConstraintTest.checkSuccess(instance, new byte[2]);
        AsnSchemaConstraintTest.checkSuccess(instance, new byte[256]);

        // test upper bound
        instance = new AsnSchemaSizeConstraint(Integer.MIN_VALUE, 255);
        AsnSchemaConstraintTest.checkSuccess(instance, new byte[0]);
        AsnSchemaConstraintTest.checkSuccess(instance, new byte[1]);
        AsnSchemaConstraintTest.checkSuccess(instance, new byte[255]);
        AsnSchemaConstraintTest.checkFailure(
                instance,
                new byte[256],
                "Expected a value between -2147483648 and 255, but found: 256");

        // test invalid bounds
        instance = new AsnSchemaSizeConstraint(5, -5);
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[0], "Expected a value between 5 and -5, but found: 0");
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[1], "Expected a value between 5 and -5, but found: 1");
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[255], "Expected a value between 5 and -5, but found: 255");
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[256], "Expected a value between 5 and -5, but found: 256");
    }

    @Test
    public void testBitStringOk() throws Exception {

        // 2 bytes that decodes to "111" (length 3)
        byte[] bytes = {(byte) 0x05, (byte) 0xE0};

        AsnSchemaSizeConstraint instance = new AsnSchemaSizeConstraint(2, 3);
        ImmutableSet<SchemaConstraintValidationFailure> failures =
                instance.apply(bytes, new AsnPrimitiveTypeBitString());
        assertTrue(failures.isEmpty());
    }

    @Test
    public void testBitStringSizeBad() throws Exception {

        // test valid values of unused bits with two byte bit string
        byte[] bytes = {(byte) 0x00, (byte) 0xAA, (byte) 0x80};
        // decodes to "1010101010000000"

        AsnSchemaSizeConstraint instance = new AsnSchemaSizeConstraint(3, 15);
        ImmutableSet<SchemaConstraintValidationFailure> failures =
                instance.apply(bytes, new AsnPrimitiveTypeBitString());
        assertEquals(1, failures.size());
        final SchemaConstraintValidationFailure failure = failures.iterator().next();
        assertEquals(FailureType.SchemaConstraint, failure.getFailureType());
    }

    @Test
    public void testBitStringDecodeError() throws Exception {

        // fail to decode - invalid length octet (single byte)
        byte[] bytes = {(byte) 0x08, (byte) 0xFF};

        AsnSchemaSizeConstraint instance = new AsnSchemaSizeConstraint(3, 15);
        ImmutableSet<SchemaConstraintValidationFailure> failures =
                instance.apply(bytes, new AsnPrimitiveTypeBitString());
        assertEquals(1, failures.size());

        final SchemaConstraintValidationFailure failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
    }
}
