/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaSizeConstraint;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

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
        final AsnSchemaTypeDefinition type = MockAsnSchemaTypeDefinition.builder(
                "MockPrintableStringType",
                AsnBuiltinType.PrintableString)
                .setConstraint(new AsnSchemaSizeConstraint(1, 4))
                .build();
        final DecodedAsnData mockDecodedAsnData = mock(DecodedAsnData.class);
        when(mockDecodedAsnData.getType(anyString())).thenReturn(type);
        when(mockDecodedAsnData.getBytes("/valid")).thenReturn(new byte[] { '1', '2', '3', '4' });
        when(mockDecodedAsnData.getBytes("/invalid/bytes")).thenReturn(new byte[] { '%' });
        when(mockDecodedAsnData.getBytes("/invalid/constraint")).thenReturn(new byte[] { '1', '2',
                                                                                         '3', '4',
                                                                                         '5' });
        when(mockDecodedAsnData.getBytes("/empty")).thenReturn(new byte[0]);
        when(mockDecodedAsnData.getBytes("/null")).thenReturn(null);

        // test valid
        ImmutableSet<DecodedTagValidationFailure> failures = instance.validate("/valid",
                mockDecodedAsnData);
        assertEquals(0, failures.size());

        // test invalid - bytes
        failures = instance.validate("/invalid/bytes", mockDecodedAsnData);
        assertEquals(1, failures.size());
        DecodedTagValidationFailure failure = failures.iterator().next();
        assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
        assertEquals(BuiltinTypeValidator.PRINTABLESTRING_VALIDATION_ERROR + 37,
                failure.getFailureReason());

        // test invalid - constraint
        failures = instance.validate("/invalid/constraint", mockDecodedAsnData);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.SchemaConstraint, failure.getFailureType());
        assertEquals("Expected a value between 1 and 4, but found: 5", failure.getFailureReason());

        // test empty
        failures = instance.validate("/empty", mockDecodedAsnData);
        assertEquals(1, failures.size());
        failure = failures.iterator().next();
        assertEquals(FailureType.SchemaConstraint, failure.getFailureType());
        assertEquals("Expected a value between 1 and 4, but found: 0", failure.getFailureReason());

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
            assertEquals(errorPrefix + b, failure.getFailureReason());
        }

        {
            bytes[0] = Byte.MAX_VALUE;
            final ImmutableSet<ByteValidationFailure> failures = instance.validate(bytes);
            assertEquals(1, failures.size());
            final ByteValidationFailure failure = failures.iterator().next();
            assertEquals(FailureType.DataIncorrectlyFormatted, failure.getFailureType());
            assertEquals(errorPrefix + bytes[0], failure.getFailureReason());
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
