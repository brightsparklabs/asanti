/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaNumericValueConstraint;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Units tests for {@link IntegerValidator}
 *
 * @author brightSPARK Labs
 */
public class IntegerValidatorTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final IntegerValidator instance = IntegerValidator.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testValidateTag() throws Exception
    {
        // setup mock
        final AsnSchemaTypeDefinition type = MockAsnSchemaTypeDefinition.builder("MockIntegerType",
                AsnBuiltinType.Integer)
                .setConstraint(new AsnSchemaNumericValueConstraint(BigInteger.valueOf(1),
                        BigInteger.valueOf(32639)))
                .build();

        final DecodedAsnData mockDecodedAsnData = mock(DecodedAsnData.class);
        when(mockDecodedAsnData.getType(anyString())).thenReturn(type);
        // 32639 within constraint
        when(mockDecodedAsnData.getBytes("/valid")).thenReturn(new byte[] { (byte) 0b01111111,
                                                                            (byte) 0b01111111 });
        // 32640 outside constraint (1..32639)
        when(mockDecodedAsnData.getBytes("/invalid/constraint")).thenReturn(new byte[] {
                (byte) 0b01111111, (byte) 0b10000000 });
        when(mockDecodedAsnData.getBytes("/empty")).thenReturn(new byte[0]);
        when(mockDecodedAsnData.getBytes("/null")).thenReturn(null);

        // test valid
        ImmutableSet<DecodedTagValidationFailure> failures = instance.validate("/valid",
                mockDecodedAsnData);
        assertEquals(0, failures.size());

        // test invalid - constraint
        failures = instance.validate("/invalid/constraint", mockDecodedAsnData);
        assertEquals(1, failures.size());
        DecodedTagValidationFailure failure = failures.iterator().next();
        assertEquals(FailureType.SchemaConstraint, failure.getFailureType());
        assertEquals("Expected a value between 1 and 32639, but found: 32640",
                failure.getFailureReason());

        // test null
        failures = instance.validate("/null", mockDecodedAsnData);
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
        byte[] bytes = new byte[] { 0x00 };
        assertEquals(0, instance.validate(bytes).size());

        bytes = new byte[] { (byte) 0xff };
        assertEquals(0, instance.validate(bytes).size());

        // test valid positive number 128 (with leading bit 1). Positive number => A leading 0x00 octet is added.
        bytes = new byte[] { (byte) 0b00000000, (byte) 0b10000000 };
        assertEquals(0, instance.validate(bytes).size());

        // test valid negative number -128 with leading bit 1
        bytes = new byte[] { (byte) 0b10000000 };
        assertEquals(0, instance.validate(bytes).size());

        // test valid large positive number spanning multiple bytes (+32639)
        bytes = new byte[] { (byte) 0b01111111, (byte) 0b01111111 };
        assertEquals(0, instance.validate(bytes).size());

        // test valid large negative number spanning multiple bytes (-32639)
        bytes = new byte[] { (byte) 0b10000000, (byte) 0b10000001 };
        assertEquals(0, instance.validate(bytes).size());

        // test valid 16 byte number
        bytes = new byte[] { (byte) 0x00, (byte) 0x8f, (byte) 0xe2, (byte) 0x41, (byte) 0x2a,
                             (byte) 0x08, (byte) 0xe8, (byte) 0x51, (byte) 0xa8, (byte) 0x8c,
                             (byte) 0xb3, (byte) 0xe8, (byte) 0x53, (byte) 0xe7, (byte) 0xd5,
                             (byte) 0x49 };
        assertEquals(0, instance.validate(bytes).size());

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
