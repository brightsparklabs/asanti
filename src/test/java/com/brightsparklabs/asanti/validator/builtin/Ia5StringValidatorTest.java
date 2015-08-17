/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.mocks.model.data.MockDecodedAsnData;
import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaType;
import com.brightsparklabs.asanti.model.data.AsantiAsnData;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaExactSizeConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveTypes;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.assam.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Units tests for {@link Ia5StringValidator}
 *
 * @author brightSPARK Labs
 */
public class Ia5StringValidatorTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final Ia5StringValidator instance = Ia5StringValidator.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testValidateTag() throws Exception
    {
        // TODO ASN-136 - use mock Constraints, not real.

        // setup mock
        final AsnSchemaType type
                = MockAsnSchemaType.createMockedAsnSchemaType(AsnPrimitiveTypes.IA5_STRING,
                new AsnSchemaExactSizeConstraint(5));
        final AsantiAsnData mockAsnData = MockDecodedAsnData.builder(type)
                .addBytes("/valid", new byte[] { '0', '1', '2', '3', '4' })
                .addBytes("/invalid/bytes", new byte[] { '0', '1', '2', '3', (byte) 0xFF })
                .addBytes("/invalid/constraint", new byte[] { '0', '1', '2', '3', '4', '5' })
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
        assertEquals(BuiltinTypeValidator.IA5STRING_VALIDATION_ERROR + "0xFF",
                failure.getFailureReason());

        // test invalid - constraint
        failures = instance.validate("/invalid/constraint", mockAsnData);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.SchemaConstraint, failure.getFailureType());
        assertEquals("Expected a value of 5, but found: 6", failure.getFailureReason());

        // test empty
        failures = instance.validate("/empty", mockAsnData);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.SchemaConstraint, failure.getFailureType());
        assertEquals("Expected a value of 5, but found: 0", failure.getFailureReason());

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
        for (int b = 127; b >= 0; b--)
        {
            bytes[0] = (byte) b;
            assertEquals(0, instance.validate(bytes).size());
        }

        // test invalid
        final String errorPrefix = BuiltinTypeValidator.IA5STRING_VALIDATION_ERROR;

        for (byte b = Byte.MIN_VALUE; b < 0; b++)
        {
            bytes[0] = b;
            final ImmutableSet<ByteValidationFailure> failures = instance.validate(bytes);
            assertEquals(1, failures.size());
            final ByteValidationFailure failure = failures.iterator().next();
            assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
            assertEquals(errorPrefix + String.format("0x%02X", b), failure.getFailureReason());
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
