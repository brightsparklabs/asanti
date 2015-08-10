/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.mocks.model.data.MockDecodedAsnData;
import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaType;
import com.brightsparklabs.asanti.model.data.AsnData;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Units tests for {@link OidValidator}
 *
 * @author brightSPARK Labs
 */
public class OidValidatorTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final OidValidator instance = OidValidator.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testValidateTag() throws Exception
    {
        // TODO ASN-136 - use mock Constraints, not real.

        // setup mock
        final AsnSchemaType type = MockAsnSchemaType.createMockedAsnSchemaType(AsnPrimitiveType.OID,
                AsnSchemaConstraint.NULL);

        final AsnData mockAsnData = MockDecodedAsnData.builder(type)
                .addBytes("/valid", new byte[] { (byte) 0x2B, (byte) 0xFF, (byte) 0x7F })
                .addBytes("/invalid/bytes", new byte[] { (byte) 0x2B, (byte) 0xFF, (byte) 0xFF })
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
        assertEquals(BuiltinTypeValidator.OID_VALIDATION_ERROR_INCOMPLETE + "0xFF",
                failure.getFailureReason());

        // test empty
        failures = instance.validate("/empty", mockAsnData);
        assertEquals(1, failures.size());

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
        // test valid single octet (minimum value)
        // OID: 0.0
        byte[] bytes = new byte[] { (byte) 0x00 };
        assertEquals(0, instance.validate(bytes).size());

        // test valid single octet (maximum value)
        // OID: 3.7
        bytes = new byte[] { (byte) 0x7F };
        assertEquals(0, instance.validate(bytes).size());

        // test invalid single octet
        bytes = new byte[] { (byte) 0x80 };
        assertEquals(1, instance.validate(bytes).size());

        // test valid multiple octets
        // OID: 1.3.16383
        bytes = new byte[] { (byte) 0x2B, (byte) 0xFF, (byte) 0x7F };
        assertEquals(0, instance.validate(bytes).size());

        // test valid multiple octets
        // OID: 1.3.2097152.16
        bytes = new byte[] { (byte) 0x2B, (byte) 0x81, (byte) 0x80, (byte) 0x80, (byte) 0x00,
                             (byte) 0x10 };
        assertEquals(0, instance.validate(bytes).size());

        // test invalid multiple octets
        // OID is incomplete
        bytes = new byte[] { (byte) 0x2B, (byte) 0x80 };
        assertEquals(1, instance.validate(bytes).size());

        // test invalid multiple octets
        // OID is incomplete
        bytes = new byte[] { (byte) 0x2B, (byte) 0xFF, (byte) 0xFF };
        assertEquals(1, instance.validate(bytes).size());

        // test invalid multiple octets
        // OID is incomplete.
        bytes = new byte[] { (byte) 0x2B, (byte) 0x81, (byte) 0x80, (byte) 0x80, (byte) 0x80 };
        assertEquals(1, instance.validate(bytes).size());

        // test valid maximum Sub ID (268,435,455)
        // OID: 1.3.0.268435455.127
        bytes = new byte[] { (byte) 0x2B, (byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                             (byte) 0x7F, (byte) 0x7F };
        assertEquals(0, instance.validate(bytes).size());

        // test empty
        bytes = new byte[0];
        assertEquals(1, instance.validate(bytes).size());

        // test null
        final ImmutableSet<ByteValidationFailure> failures = instance.validate(null);
        assertEquals(1, failures.size());
        final ByteValidationFailure failure = failures.iterator().next();
        assertEquals(FailureType.DataMissing, failure.getFailureType());
        assertEquals("No bytes present to validate", failure.getFailureReason());
    }
}
