/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.mocks.model.data.MockDecodedAsnData;
import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaSizeConstraint;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.typedefinition.OLDAsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Units tests for {@link Utf8StringValidator}
 *
 * @author brightSPARK Labs
 */
public class Utf8StringValidatorTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final Utf8StringValidator instance = Utf8StringValidator.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testValidateTag() throws Exception
    {
/* TODO MJF
        // setup mock
        final AsnSchemaType type = MockAsnSchemaTypeDefinition.builder(
                "MockUtf8StringType",
                AsnBuiltinType.Utf8String).setConstraint(new AsnSchemaSizeConstraint(1, 3)).build();
        final DecodedAsnData mockDecodedAsnData = MockDecodedAsnData.builder(type)
                .addBytes("/valid", new byte[] { (byte) 0b11000010, (byte) 0b10000000 })
                .addBytes("/invalid/bytes", new byte[] { (byte) 0b11000010 })
                .addBytes("/invalid/constraint", new byte[] { 0x01, 0x02, 0x03, 0x04 })
                .build();

        // test valid
        ImmutableSet<DecodedTagValidationFailure> failures = instance.validate("/valid",
                mockDecodedAsnData);
        assertEquals(0, failures.size());

        // test invalid - bytes
        failures = instance.validate("/invalid/bytes", mockDecodedAsnData);
        assertEquals(1, failures.size());
        DecodedTagValidationFailure failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals("ASN.1 UTF8String type must be encoded in UTF-8", failure.getFailureReason());

        // test invalid - constraint
        failures = instance.validate("/invalid/constraint", mockDecodedAsnData);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.SchemaConstraint, failure.getFailureType());
        assertEquals("Expected a value between 1 and 3, but found: 4", failure.getFailureReason());

        // test empty
        failures = instance.validate("/empty", mockDecodedAsnData);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.SchemaConstraint, failure.getFailureType());
        assertEquals("Expected a value between 1 and 3, but found: 0", failure.getFailureReason());

        // test null
        failures = instance.validate("/null", mockDecodedAsnData);
        assertEquals(2, failures.size());
        boolean byteErrorPresent = true;
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
*/
    }

    @Test
    public void testValidateBytes() throws Exception
    {
        // test valid - one byte (all ASCII characters)
        byte[] bytes = new byte[1];
        for (byte b = Byte.MAX_VALUE; b >= 0; b--)
        {
            bytes[0] = b;
            assertEquals(0, instance.validate(bytes).size());
        }

        // test valid - two bytes (minimum/maximum)
        bytes = new byte[] { (byte) 0b11000010, (byte) 0b10000000 };
        assertEquals(0, instance.validate(bytes).size());
        bytes = new byte[] { (byte) 0b11011111, (byte) 0b10111111 };
        assertEquals(0, instance.validate(bytes).size());

        // test valid - three bytes (minimum/maximum)
        bytes = new byte[] { (byte) 0b11100010, (byte) 0b10000000, (byte) 0b10000000 };
        assertEquals(0, instance.validate(bytes).size());
        bytes = new byte[] { (byte) 0b11101111, (byte) 0b10111111, (byte) 0b10111111 };
        assertEquals(0, instance.validate(bytes).size());

        // test valid - four bytes (minimum/maximum)
        bytes = new byte[] { (byte) 0b11110010, (byte) 0b10000000, (byte) 0b10000000,
                             (byte) 0b10000000 };
        assertEquals(0, instance.validate(bytes).size());
        bytes = new byte[] { (byte) 0b11110000, (byte) 0b10111111, (byte) 0b10111111,
                             (byte) 0b10111111 };
        assertEquals(0, instance.validate(bytes).size());

        // test invalid - leading byte requires two trailing bytes
        bytes = new byte[] { (byte) 0b11000000 };
        ImmutableSet<ByteValidationFailure> failures = instance.validate(bytes);
        assertEquals(1, failures.size());
        ByteValidationFailure failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals("ASN.1 UTF8String type must be encoded in UTF-8", failure.getFailureReason());

        // test invalid - leading byte requires three trailing bytes
        bytes = new byte[] { (byte) 0b11100000 };
        failures = instance.validate(bytes);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals("ASN.1 UTF8String type must be encoded in UTF-8", failure.getFailureReason());
        bytes = new byte[] { (byte) 0b11100000, (byte) 0b10000000 };
        failures = instance.validate(bytes);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals("ASN.1 UTF8String type must be encoded in UTF-8", failure.getFailureReason());

        // test invalid - leading byte requires four trailing bytes
        bytes = new byte[] { (byte) 0b11110000 };
        failures = instance.validate(bytes);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals("ASN.1 UTF8String type must be encoded in UTF-8", failure.getFailureReason());
        bytes = new byte[] { (byte) 0b11110000, (byte) 0b10000000, (byte) 0b10000000 };
        failures = instance.validate(bytes);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals("ASN.1 UTF8String type must be encoded in UTF-8", failure.getFailureReason());

        // test invalid - leading byte requires five trailing bytes
        bytes = new byte[] { (byte) 0b11111000 };
        failures = instance.validate(bytes);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals("ASN.1 UTF8String type must be encoded in UTF-8", failure.getFailureReason());
        bytes = new byte[] { (byte) 0b11111000, (byte) 0b10000000, (byte) 0b10000000,
                             (byte) 0b10000000 };
        failures = instance.validate(bytes);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals("ASN.1 UTF8String type must be encoded in UTF-8", failure.getFailureReason());

        // test invalid - leading byte requires six trailing bytes
        bytes = new byte[] { (byte) 0b11111100 };
        failures = instance.validate(bytes);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals("ASN.1 UTF8String type must be encoded in UTF-8", failure.getFailureReason());
        bytes = new byte[] { (byte) 0b11111100, (byte) 0b10000000, (byte) 0b10000000,
                             (byte) 0b10000000, (byte) 0b10000000 };
        failures = instance.validate(bytes);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals("ASN.1 UTF8String type must be encoded in UTF-8", failure.getFailureReason());

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
