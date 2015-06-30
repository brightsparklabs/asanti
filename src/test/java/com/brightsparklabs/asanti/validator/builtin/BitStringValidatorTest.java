/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.mocks.model.data.MockDecodedAsnData;
import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaType;
import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaExactSizeConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Units tests for {@link BitStringValidator}
 *
 * @author brightSPARK Labs
 */
public class BitStringValidatorTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final BitStringValidator instance = BitStringValidator.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testValidateTag() throws Exception
    {
        // setup mock
        final AsnSchemaType type = MockAsnSchemaType.createMockedAsnSchemaType(AsnPrimitiveType.BIT_STRING,
                AsnSchemaConstraint.NULL);

        final DecodedAsnData mockDecodedAsnData = MockDecodedAsnData.builder(type)
                .addBytes("/valid", new byte[] { (byte) 0x05, (byte) 0xE0 })
                .addBytes("/invalid", new byte[] { (byte) 0x08, (byte) 0xFF })
                .build();

        // test valid
        ImmutableSet<DecodedTagValidationFailure> failures = instance.validate("/valid",
                mockDecodedAsnData);
        assertEquals(0, failures.size());

        // test invalid
        failures = instance.validate("/invalid", mockDecodedAsnData);
        assertEquals(1, failures.size());
        DecodedTagValidationFailure failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());

        // test empty
        failures = instance.validate("/empty", mockDecodedAsnData);
        assertEquals(1, failures.size());

        // test null
        failures = instance.validate("/null", mockDecodedAsnData);
        assertEquals(1, failures.size());
        boolean byteErrorPresent = false;
        for (DecodedTagValidationFailure nullFailure : failures)
        {
            assertEquals(FailureType.DataMissing, nullFailure.getFailureType());
            if (nullFailure.getFailureReason().equals("No bytes present to validate"))
            {
                byteErrorPresent = true;
            }
        }
        assertTrue(byteErrorPresent);
    }

    @Test
    public void testValidateBytes() throws Exception
    {
        // test valid
        byte[] bytes = { (byte) 0x05, (byte) 0xE0 };
        assertEquals(0, instance.validate(bytes).size());

        bytes = new byte[] { (byte) 0x06, (byte) 0xAA, (byte) 0x80 };
        assertEquals(0, instance.validate(bytes).size());

        bytes = new byte[] { (byte) 0x00 };
        assertEquals(0, instance.validate(bytes).size());

        // test invalid
        bytes = new byte[] { (byte) 0x08, (byte) 0xFF };
        ImmutableSet<ByteValidationFailure> failures = instance.validate(bytes);
        assertEquals(1, failures.size());
        ByteValidationFailure failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());

        bytes = new byte[] { (byte) 0x0F, (byte) 0xFF, (byte) 0xE0 };
        failures = instance.validate(bytes);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());

        // test empty
        bytes = new byte[0];
        failures = instance.validate(bytes);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());

        // test null
        failures = instance.validate(null);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.DataMissing, failure.getFailureType());
        assertEquals("No bytes present to validate", failure.getFailureReason());
    }
}
