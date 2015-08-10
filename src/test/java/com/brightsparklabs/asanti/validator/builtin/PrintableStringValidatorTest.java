/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.mocks.model.data.MockDecodedAsnData;
import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaType;
import com.brightsparklabs.asanti.model.data.AsnData;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaSizeConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Units tests for {@link PrintableStringValidator}
 *
 * @author brightSPARK Labs
 */
public class PrintableStringValidatorTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final PrintableStringValidator instance = PrintableStringValidator.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testValidateTag() throws Exception
    {
        // setup mock
        final AsnSchemaType type = MockAsnSchemaType.createMockedAsnSchemaType(
                AsnPrimitiveType.PRINTABLE_STRING,
                new AsnSchemaSizeConstraint(1, 4));
        final AsnData mockAsnData = MockDecodedAsnData.builder(type)
                .addBytes("/valid", new byte[] { '1', '2', '3', '4' })
                .addBytes("/invalid/bytes", new byte[] { '%' })
                .addBytes("/invalid/constraint", new byte[] { '1', '2', '3', '4', '5' })
                .build();

        // test valid
        ImmutableSet<DecodedTagValidationFailure> failures = instance.validate("/valid",
                mockAsnData);
        assertEquals(0, failures.size());

        // test invalid - bytes
        failures = instance.validate("/invalid/bytes", mockAsnData);
        assertEquals(1, failures.size());
        DecodedTagValidationFailure failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals(BuiltinTypeValidator.PRINTABLESTRING_VALIDATION_ERROR + "0x25",
                failure.getFailureReason());

        // test invalid - constraint
        failures = instance.validate("/invalid/constraint", mockAsnData);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.SchemaConstraint, failure.getFailureType());
        assertEquals("Expected a value between 1 and 4, but found: 5", failure.getFailureReason());

        // test empty
        failures = instance.validate("/empty", mockAsnData);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.SchemaConstraint, failure.getFailureType());
        assertEquals("Expected a value between 1 and 4, but found: 0", failure.getFailureReason());

        // test null
        failures = instance.validate("/null", mockAsnData);
        assertEquals(2, failures.size());
        boolean byteErrorPresent = false;
        boolean constraintErrorPresent = false;
        for (DecodedTagValidationFailure nullFailure : failures)
        {
            assertEquals(FailureType.DataMissing, nullFailure.getFailureType());
            if (nullFailure.getFailureReason()
                    .equals("No data found to validate against constraint"))
            {
                constraintErrorPresent = true;
            }
            else if (nullFailure.getFailureReason().equals("No bytes present to validate"))
            {
                byteErrorPresent = true;
            }
        }
        assertTrue(byteErrorPresent);
        assertTrue(constraintErrorPresent);
    }

    @Test
    public void testValidateBytes() throws Exception
    {
        // test valid
        byte[] bytes = new byte[1];
        for (int b = 'Z'; b >= 'A'; b--)
        {
            bytes[0] = (byte) b;
            assertEquals(0, instance.validate(bytes).size());
        }
        for (int b = 'z'; b >= 'a'; b--)
        {
            bytes[0] = (byte) b;
            assertEquals(0, instance.validate(bytes).size());
        }
        for (int b = '9'; b >= '0'; b--)
        {
            bytes[0] = (byte) b;
            assertEquals(0, instance.validate(bytes).size());
        }
        bytes[0] = (byte) ' ';
        assertEquals(0, instance.validate(bytes).size());

        bytes[0] = (byte) '\'';
        assertEquals(0, instance.validate(bytes).size());

        bytes[0] = (byte) '(';
        assertEquals(0, instance.validate(bytes).size());

        bytes[0] = (byte) ')';
        assertEquals(0, instance.validate(bytes).size());

        bytes[0] = (byte) '+';
        assertEquals(0, instance.validate(bytes).size());

        bytes[0] = (byte) ',';
        assertEquals(0, instance.validate(bytes).size());

        bytes[0] = (byte) '-';
        assertEquals(0, instance.validate(bytes).size());

        bytes[0] = (byte) '.';
        assertEquals(0, instance.validate(bytes).size());

        bytes[0] = (byte) '/';
        assertEquals(0, instance.validate(bytes).size());

        bytes[0] = (byte) ':';
        assertEquals(0, instance.validate(bytes).size());

        bytes[0] = (byte) '=';
        assertEquals(0, instance.validate(bytes).size());

        bytes[0] = (byte) '?';
        assertEquals(0, instance.validate(bytes).size());

        // test invalid
        final String errorPrefix = BuiltinTypeValidator.PRINTABLESTRING_VALIDATION_ERROR;

        for (byte b = Byte.MIN_VALUE; b < 32; b++)
        {
            bytes[0] = b;
            final ImmutableSet<ByteValidationFailure> failures = instance.validate(bytes);
            assertEquals(1, failures.size());
            final ByteValidationFailure failure = failures.iterator().next();
            assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
            assertEquals(errorPrefix + String.format("0x%02X", b), failure.getFailureReason());
        }

        {
            bytes[0] = Byte.MAX_VALUE;
            final ImmutableSet<ByteValidationFailure> failures = instance.validate(bytes);
            assertEquals(1, failures.size());
            final ByteValidationFailure failure = failures.iterator().next();
            assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
            assertEquals(errorPrefix + String.format("0x%02X", bytes[0]),
                    failure.getFailureReason());
        }

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
