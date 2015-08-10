/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.mocks.model.data.MockDecodedAsnData;
import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaType;
import com.brightsparklabs.asanti.model.data.AsantiAsnData;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaSizeConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveTypes;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Units tests for {@link VisibleStringValidator}
 *
 * @author brightSPARK Labs
 */
public class VisibleStringValidatorTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final VisibleStringValidator instance = VisibleStringValidator.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testValidateTag() throws Exception
    {
        // TODO ASN-136 - use mock Constraints, not real.

        // setup mock
        final AsnSchemaType type = MockAsnSchemaType.createMockedAsnSchemaType(AsnPrimitiveTypes.VISIBLE_STRING,
                new AsnSchemaSizeConstraint(1, 10));

        final AsantiAsnData mockAsnData = MockDecodedAsnData.builder(type)
                .addBytes("/valid", new byte[] { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' })
                .addBytes("/invalid/bytes", new byte[] { (byte) 0x7F, (byte) 0x00 })
                .addBytes("/invalid/constraint",
                        new byte[] { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'x' })
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
        assertEquals(BuiltinTypeValidator.VISIBLESTRING_VALIDATION_ERROR + "0x7f00",
                failure.getFailureReason());

        // test invalid - constraint
        failures = instance.validate("/invalid/constraint", mockAsnData);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.SchemaConstraint, failure.getFailureType());
        assertEquals("Expected a value between 1 and 10, but found: 11",
                failure.getFailureReason());

        // test empty
        failures = instance.validate("/empty", mockAsnData);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.SchemaConstraint, failure.getFailureType());
        assertEquals("Expected a value between 1 and 10, but found: 0", failure.getFailureReason());

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
        for (int b = 126; b >= 32; b--)
        {
            bytes[0] = (byte) b;
            assertEquals(0, instance.validate(bytes).size());
        }

        // test invalid
        final String errorPrefix = BuiltinTypeValidator.VISIBLESTRING_VALIDATION_ERROR;
        for (byte b = Byte.MIN_VALUE; b < 32; b++)
        {
            bytes[0] = b;
            final ImmutableSet<ByteValidationFailure> failures = instance.validate(bytes);
            assertEquals(1, failures.size());
            final ByteValidationFailure failure = failures.iterator().next();
            assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
            assertEquals(errorPrefix + String.format("0x%02x", b), failure.getFailureReason());
        }

        bytes[0] = Byte.MAX_VALUE;
        ImmutableSet<ByteValidationFailure> failures = instance.validate(bytes);
        assertEquals(1, failures.size());
        ByteValidationFailure failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals(errorPrefix + "0x7f", failure.getFailureReason());

        // test empty
        bytes = new byte[0];
        assertEquals(0, instance.validate(bytes).size());

        // test null
        failures = instance.validate(null);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.DataMissing, failure.getFailureType());
        assertEquals("No bytes present to validate", failure.getFailureReason());
    }
}
