/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.mocks.model.data.MockDecodedAsnData;
import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaType;
import com.brightsparklabs.asanti.model.data.DecodedAsnData;
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
 * Units tests for {@link GeneralizedTimeValidator}
 *
 * @author brightSPARK Labs
 */
public class GeneralizedTimeValidatorTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final GeneralizedTimeValidator instance = GeneralizedTimeValidator.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testValidateTag() throws Exception
    {
        // setup mock
        final AsnSchemaType type
                = MockAsnSchemaType.createMockedAsnSchemaType(AsnPrimitiveType.GENERALIZED_TIME,
                AsnSchemaConstraint.NULL);

        String validTime = "19181111110000.0123456789";
        String invalidTime = "19450508230112.123Z+2400";

        final DecodedAsnData mockDecodedAsnData = MockDecodedAsnData.builder(type)
                .addBytes("/valid", validTime.getBytes())
                .addBytes("/invalid/bytes", invalidTime.getBytes())
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
        // Minimum values (Local time)
        String time = "19700101000000.1";
        byte[] bytes = time.getBytes();
        assertEquals(0, instance.validate(bytes).size());

        // Maximum values (Local time)
        time = "99991231235959.999";
        bytes = time.getBytes();
        assertEquals(0, instance.validate(bytes).size());

        // Another valid time (Local time)
        time = "19181111110000.123";
        bytes = time.getBytes();
        assertEquals(0, instance.validate(bytes).size());

        // Valid value (Universal time)
        time = "19450508230123.123Z";
        bytes = time.getBytes();
        assertEquals(0, instance.validate(bytes).size());

        // Valid values (Time differential)
        time = "19450508230112.123+0801";
        bytes = time.getBytes();
        assertEquals(0, instance.validate(bytes).size());
        time = "19450508230134.123+1031";
        bytes = time.getBytes();
        assertEquals(0, instance.validate(bytes).size());
        time = "19450508230145.123-2359";
        bytes = time.getBytes();
        assertEquals(0, instance.validate(bytes).size());
        time = "19450508230156.123+2359";
        bytes = time.getBytes();
        assertEquals(0, instance.validate(bytes).size());

        // Invalid times (incorrect lengths)
        time = "191811111100007.11";
        bytes = time.getBytes();
        assertEquals(1, instance.validate(bytes).size());

        // Valid/Invalid times (not VisibleString)
        // 19181111110000.11
        bytes = new byte[] { '1', '9', '1', '8', '1', '1', '1', '1', '1', '1', '0', '0', '0', '0',
                             '.', '1', '1' };
        assertEquals(0, instance.validate(bytes).size());
        bytes = new byte[] { 0x1F, '9', '1', '8', '1', '1', '1', '1', '1', '1', '0', '0', '0', '0',
                             '.', '1', '1' };
        assertEquals(1, instance.validate(bytes).size());

        // Invalid separators
        time = "19181111110000+11";
        bytes = time.getBytes();
        assertEquals(1, instance.validate(bytes).size());
        time = "19181111110000%11";
        bytes = time.getBytes();
        assertEquals(1, instance.validate(bytes).size());
        time = "19181111110000++11";
        bytes = time.getBytes();
        assertEquals(1, instance.validate(bytes).size());

        // Invalid/unused suffixes (Universal time)
        time = "19450508230123.123A";
        bytes = time.getBytes();
        assertEquals(1, instance.validate(bytes).size());
        time = "19450508230123.123Y";
        bytes = time.getBytes();
        assertEquals(1, instance.validate(bytes).size());
        time = "19450508230123.123z";
        bytes = time.getBytes();
        assertEquals(1, instance.validate(bytes).size());

        // Invalid time differentials
        time = "19450508230112.123+2400";
        bytes = time.getBytes();
        assertEquals(1, instance.validate(bytes).size());
        time = "19450508230134.123+9999";
        bytes = time.getBytes();
        assertEquals(1, instance.validate(bytes).size());
        time = "19450508230145.123-2400";
        bytes = time.getBytes();
        assertEquals(1, instance.validate(bytes).size());
        time = "19450508230156.123-9999";
        bytes = time.getBytes();
        assertEquals(1, instance.validate(bytes).size());

        // test null
        final ImmutableSet<ByteValidationFailure> failures = instance.validate(null);
        assertEquals(1, failures.size());
        final ByteValidationFailure failure = failures.iterator().next();
        assertEquals(FailureType.DataMissing, failure.getFailureType());
        assertEquals("No bytes present to validate", failure.getFailureReason());
    }
}
