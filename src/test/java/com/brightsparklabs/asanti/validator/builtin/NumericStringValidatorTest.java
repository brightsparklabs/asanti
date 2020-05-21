/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator.builtin;

import static org.junit.Assert.*;

import com.brightsparklabs.asanti.mocks.model.data.MockDecodedAsnData;
import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaType;
import com.brightsparklabs.asanti.model.data.AsantiAsnData;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaSizeConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveTypes;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

/**
 * Units tests for {@link NumericStringValidator}
 *
 * @author brightSPARK Labs
 */
public class NumericStringValidatorTest {
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final NumericStringValidator instance = NumericStringValidator.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testValidateTag() throws Exception {
        // TODO ASN-136 - use mock Constraints, not real.

        // setup mock
        final AsnSchemaType type =
                MockAsnSchemaType.createMockedAsnSchemaType(
                        AsnPrimitiveTypes.NUMERIC_STRING, new AsnSchemaSizeConstraint(0, 4));

        final AsantiAsnData mockAsnData =
                MockDecodedAsnData.builder(type)
                        .addBytes("/valid", new byte[] {'1', '2', '3', '4'})
                        .addBytes("/invalid/bytes", new byte[] {'x'})
                        .addBytes("/invalid/constraint", new byte[] {'1', '2', '3', '4', '5'})
                        .build();

        // test valid
        ImmutableSet<DecodedTagValidationFailure> failures =
                instance.validate("/valid", mockAsnData);
        assertEquals(0, failures.size());

        // test invalid - bytes
        failures = instance.validate("/invalid/bytes", mockAsnData);
        assertEquals(1, failures.size());
        DecodedTagValidationFailure failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals(
                BuiltinTypeValidator.NUMERICSTRING_VALIDATION_ERROR + "0x78",
                failure.getFailureReason());

        // test invalid - constraint
        failures = instance.validate("/invalid/constraint", mockAsnData);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.SchemaConstraint, failure.getFailureType());
        assertEquals("Expected a value between 0 and 4, but found: 5", failure.getFailureReason());

        // test empty
        failures = instance.validate("/empty", mockAsnData);
        assertEquals(0, failures.size());

        // test null
        failures = instance.validate("/null", mockAsnData);
        assertEquals(2, failures.size());
        boolean byteErrorPresent = false;
        boolean constraintErrorPresent = false;
        for (DecodedTagValidationFailure nullFailure : failures) {
            assertEquals(FailureType.DataMissing, nullFailure.getFailureType());
            if (nullFailure
                    .getFailureReason()
                    .equals("No data found to validate against constraint")) {
                constraintErrorPresent = true;
            } else if (nullFailure.getFailureReason().equals("No bytes present to validate")) {
                byteErrorPresent = true;
            }
        }
        assertTrue(byteErrorPresent);
        assertTrue(constraintErrorPresent);
    }

    @Test
    public void testValidateBytes() throws Exception {
        // -------------------------------------------------------------------------
        // test valid, numbers and space
        // -------------------------------------------------------------------------

        // numbers
        byte[] bytes = new byte[1];
        for (int b = '9'; b >= '0'; b--) {
            bytes[0] = (byte) b;
            assertEquals(0, instance.validate(bytes).size());
        }

        // test space
        bytes[0] = ' ';
        assertEquals(0, instance.validate(bytes).size());

        // test a "phone number"
        bytes = "0400 123 456".getBytes(Charsets.UTF_8);
        assertEquals(0, instance.validate(bytes).size());

        // -------------------------------------------------------------------------
        // test invalid
        // -------------------------------------------------------------------------
        final String errorPrefix = BuiltinTypeValidator.NUMERICSTRING_VALIDATION_ERROR;
        for (byte b = Byte.MIN_VALUE; b < ' '; b++) {
            bytes[0] = b;
            final ImmutableSet<ByteValidationFailure> failures = instance.validate(bytes);
            assertEquals(1, failures.size());
            final ByteValidationFailure failure = failures.iterator().next();
            assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
            assertEquals(errorPrefix + String.format("0x%02X", b), failure.getFailureReason());
        }

        for (byte b = '!'; b < '0'; b++) {
            bytes[0] = b;
            final ImmutableSet<ByteValidationFailure> failures = instance.validate(bytes);
            assertEquals(1, failures.size());
            final ByteValidationFailure failure = failures.iterator().next();
            assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
            assertEquals(errorPrefix + String.format("0x%02X", b), failure.getFailureReason());
        }

        for (byte b = Byte.MAX_VALUE; b > '9'; b--) {
            bytes[0] = b;
            final ImmutableSet<ByteValidationFailure> failures = instance.validate(bytes);
            assertEquals(1, failures.size());
            final ByteValidationFailure failure = failures.iterator().next();
            assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
            assertEquals(errorPrefix + String.format("0x%02X", b), failure.getFailureReason());
        }

        // -------------------------------------------------------------------------
        // test edge cases (empty and null)
        // -------------------------------------------------------------------------

        // test empty
        bytes = new byte[0];
        assertEquals(0, instance.validate(bytes).size());

        // test null
        final ImmutableSet<ByteValidationFailure> failures = instance.validate(null);
        assertEquals(1, failures.size());
        final ByteValidationFailure failure = failures.iterator().next();
        assertEquals(FailureType.DataMissing, failure.getFailureType());
        assertEquals("No bytes present to validate", failure.getFailureReason());
    }
}
