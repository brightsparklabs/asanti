/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.mocks.model.data.MockDecodedAsnData;
import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaType;
import com.brightsparklabs.asanti.model.data.AsantiAsnData;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveTypes;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.assam.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Units tests for {@link NullValidator}
 *
 * @author brightSPARK Labs
 */
public class NullValidatorTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final NullValidator instance = NullValidator.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testValidateTag() throws Exception
    {
        // TODO ASN-136 - use mock Constraints, not real.

        // setup mock
        final AsnSchemaType type = MockAsnSchemaType.createMockedAsnSchemaType(AsnPrimitiveTypes.INVALID,
                AsnSchemaConstraint.NULL);

        final AsantiAsnData mockAsnData = MockDecodedAsnData.builder(type)
                .addBytes("/invalid/bytes", new byte[] { (byte) 0x00, (byte) 0xFF })
                .build();

        // test valid
        ImmutableSet<DecodedTagValidationFailure> failures = instance.validate("/empty",
                mockAsnData);
        assertEquals(0, failures.size());

        // test invalid - bytes
        failures = instance.validate("/invalid/bytes", mockAsnData);
        assertEquals(1, failures.size());
        DecodedTagValidationFailure failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals(BuiltinTypeValidator.NULL_VALIDATION_ERROR, failure.getFailureReason());

        // test null
        failures = instance.validate("/null", mockAsnData);
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
        byte[] bytes = new byte[0];
        assertEquals(0, instance.validate(bytes).size());

        {
            // test invalid
            bytes = new byte[]{0x00};
            final ImmutableSet<ByteValidationFailure> failures = instance.validate(bytes);
            assertEquals(1, failures.size());
            final ByteValidationFailure failure = failures.iterator().next();
            assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        }

        {
            // test null
            final ImmutableSet<ByteValidationFailure> failures = instance.validate(null);
            assertEquals(1, failures.size());
            final ByteValidationFailure failure = failures.iterator().next();
            assertEquals(FailureType.DataMissing, failure.getFailureType());
        }
    }
}
