/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.constraint;

import static org.junit.Assert.*;

import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveTypeBitString;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveTypeOctetString;
import com.brightsparklabs.asanti.validator.FailureType;
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
        ImmutableSet<SchemaConstraintValidationFailure> failures =
                instance.apply(new byte[0], new AsnPrimitiveTypeOctetString());
        assertEquals(true, failures.isEmpty());
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[1], "Expected a value of 0, but found: 1");
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[256], "Expected a value of 0, but found: 256");
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[10000], "Expected a value of 0, but found: 10000");

        // test large (1 MB)
        instance = new AsnSchemaExactSizeConstraint(1000000);
        failures = instance.apply(new byte[1000000], new AsnPrimitiveTypeOctetString());
        assertEquals(true, failures.isEmpty());
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[0], "Expected a value of 1000000, but found: 0");
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[1], "Expected a value of 1000000, but found: 1");
        AsnSchemaConstraintTest.checkFailure(
                instance, new byte[256], "Expected a value of 1000000, but found: 256");

        // test normal
        instance = new AsnSchemaExactSizeConstraint(256);
        failures = instance.apply(new byte[256], new AsnPrimitiveTypeOctetString());
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

    @Test
    public void testBitStringOk() throws Exception {

        // 2 bytes that decodes to "111" (length 3)
        byte[] bytes = {(byte) 0x05, (byte) 0xE0};

        AsnSchemaExactSizeConstraint instance = new AsnSchemaExactSizeConstraint(3);
        ImmutableSet<SchemaConstraintValidationFailure> failures =
                instance.apply(bytes, new AsnPrimitiveTypeBitString());
        assertTrue(failures.isEmpty());
    }

    @Test
    public void testBitStringSizeBad() throws Exception {

        // test valid values of unused bits with two byte bit string
        byte[] bytes = {(byte) 0x00, (byte) 0xAA, (byte) 0x80};
        // decodes to "1010101010000000"

        AsnSchemaExactSizeConstraint instance = new AsnSchemaExactSizeConstraint(3);
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

        AsnSchemaExactSizeConstraint instance = new AsnSchemaExactSizeConstraint(3);
        ImmutableSet<SchemaConstraintValidationFailure> failures =
                instance.apply(bytes, new AsnPrimitiveTypeBitString());
        assertEquals(1, failures.size());

        final SchemaConstraintValidationFailure failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
    }
}
