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
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Units tests for {@link TimeValidator}
 *
 * @author brightSPARK Labs
 */
public class TimeValidatorTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final TimeValidator instance = TimeValidator.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testValidateTag() throws Exception
    {
        // setup mock
        final AsnSchemaType type
                = MockAsnSchemaType.createMockedAsnSchemaType(AsnPrimitiveType.UTC_TIME,
                AsnSchemaConstraint.NULL);

        String validTime = "181111110000";
        String invalidTime = "181111110000.1";

        final DecodedAsnData mockDecodedAsnData = MockDecodedAsnData.builder(type)
                .addBytes("/valid", validTime.getBytes(Charsets.UTF_8))
                .addBytes("/invalid/bytes", invalidTime.getBytes(Charsets.UTF_8))
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
        String time = "700101000000";
        byte[] bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(0, instance.validate(bytes).size());

        // Maximum values (Local time)
        time = "991231235959";
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(0, instance.validate(bytes).size());

        // Valid value (Universal time)
        time = "450508230123Z";
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(0, instance.validate(bytes).size());

        // Valid values (Time differential)
        time = "450508230112+0801";
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(0, instance.validate(bytes).size());
        time = "450508230134+1031";
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(0, instance.validate(bytes).size());
        time = "450508230145-2359";
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(0, instance.validate(bytes).size());
        time = "450508230156+2359";
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(0, instance.validate(bytes).size());

        // Invalid times (incorrect lengths)
        time = "450508";
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(1, instance.validate(bytes).size());

        ImmutableList<byte[]> rawDates = ImmutableList.<byte[]>builder()
                .add("45050823".getBytes(Charsets.UTF_8))
                .add("4505082314".getBytes(Charsets.UTF_8))
                .add("450508231415".getBytes(Charsets.UTF_8))
                .add("450508231415Z".getBytes(Charsets.UTF_8))
                .add("450508231415+10".getBytes(Charsets.UTF_8))
                .add("450508231415+1030".getBytes(Charsets.UTF_8))
                .add("450508231415-1030".getBytes(Charsets.UTF_8))
                .build();

        for (byte[] dateString : rawDates)
        {
            assertEquals(0, instance.validate(dateString).size());
        }

        // Minimal
        String rawDateTime = "85010213";
        assertEquals(0, instance.validate(rawDateTime.getBytes(Charsets.UTF_8)).size());

        // comma as a separator
        rawDateTime = "85010213";
        assertEquals(0, instance.validate(rawDateTime.getBytes(Charsets.UTF_8)).size());

        // with Zulu
        rawDateTime = "85010213Z";
        assertEquals(0, instance.validate(rawDateTime.getBytes(Charsets.UTF_8)).size());

        // With + offset, just hours
        rawDateTime = "85010213+11";
        assertEquals(0, instance.validate(rawDateTime.getBytes(Charsets.UTF_8)).size());

        // offset with minutes
        rawDateTime = "85010213+0130";
        assertEquals(0, instance.validate(rawDateTime.getBytes(Charsets.UTF_8)).size());

        // - offset
        rawDateTime = "85010213-1022";
        assertEquals(0, instance.validate(rawDateTime.getBytes(Charsets.UTF_8)).size());

        // include minutes
        rawDateTime = "8501021314";
        assertEquals(0, instance.validate(rawDateTime.getBytes(Charsets.UTF_8)).size());

        rawDateTime = "8501021314Z";
        assertEquals(0, instance.validate(rawDateTime.getBytes(Charsets.UTF_8)).size());

        rawDateTime = "8501021314+1100";
        assertEquals(0, instance.validate(rawDateTime.getBytes(Charsets.UTF_8)).size());

        rawDateTime = "8501021314-1130";
        assertEquals(0, instance.validate(rawDateTime.getBytes(Charsets.UTF_8)).size());

        // include seconds
        rawDateTime = "850102131415";
        assertEquals(0, instance.validate(rawDateTime.getBytes(Charsets.UTF_8)).size());

        rawDateTime = "850102131415Z";
        assertEquals(0, instance.validate(rawDateTime.getBytes(Charsets.UTF_8)).size());

        rawDateTime = "850102131415+1100";
        assertEquals(0, instance.validate(rawDateTime.getBytes(Charsets.UTF_8)).size());

        rawDateTime = "850102131415-01";
        assertEquals(0, instance.validate(rawDateTime.getBytes(Charsets.UTF_8)).size());

        rawDateTime = "850102131415-0130";
        assertEquals(0, instance.validate(rawDateTime.getBytes(Charsets.UTF_8)).size());

        // Valid/Invalid times (not VisibleString)
        // 181111110000
        bytes = new byte[] { '1', '8', '1', '1', '1', '1', '1', '1', '0', '0', '0', '0' };
        assertEquals(0, instance.validate(bytes).size());
        bytes = new byte[] { 0x1F, '8', '1', '1', '1', '1', '1', '1', '0', '0', '0', '0' };
        assertEquals(1, instance.validate(bytes).size());

        // Decimal places
        rawDateTime = "181111110000.1234";
        assertEquals(1, instance.validate(rawDateTime.getBytes(Charsets.UTF_8)).size());

        // Invalid separators
        time = "181111110000%11";
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(1, instance.validate(bytes).size());
        time = "181111110000++11";
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(1, instance.validate(bytes).size());
        time = "18-11-11 11:00:00";
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(1, instance.validate(bytes).size());

        // Invalid/unused suffixes (Universal time)
        time = "450508230123A";
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(1, instance.validate(bytes).size());
        time = "450508230123Y";
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(1, instance.validate(bytes).size());
        time = "450508230123z";
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(1, instance.validate(bytes).size());

        // Invalid time differentials
        time = "450508230112+2400";
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(1, instance.validate(bytes).size());
        time = "450508230134+9999";
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(1, instance.validate(bytes).size());
        time = "450508230145-2400";
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(1, instance.validate(bytes).size());
        time = "450508230156-9999";
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(1, instance.validate(bytes).size());

        // test null
        final ImmutableSet<ByteValidationFailure> failures = instance.validate(null);
        assertEquals(1, failures.size());
        final ByteValidationFailure failure = failures.iterator().next();
        assertEquals(FailureType.DataMissing, failure.getFailureType());
        assertEquals("No bytes present to validate", failure.getFailureReason());
    }
}
