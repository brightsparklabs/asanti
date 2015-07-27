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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

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

    /** class logger */
    private static Logger logger = LoggerFactory.getLogger(GeneralizedTimeValidatorTest.class);

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

        ImmutableList<byte[]> rawDates = ImmutableList.<byte[]>builder()
                .add("1945050823".getBytes())
                .add("194505082314".getBytes())
                .add("19450508231415".getBytes())
                .add("19450508231415.1".getBytes())
                .add("19450508231415.12".getBytes())
                .add("19450508231415.12".getBytes())
                .add("19450508231415.123".getBytes())
                .add("19450508231415.1234".getBytes())
                .add("19450508231415.12345".getBytes())
                .add("19450508231415.123456".getBytes())
                .add("19450508231415.1234567".getBytes())
                .add("19450508231415.12345678".getBytes())
                .add("19450508231415.123456789".getBytes())
                .add("19450508231415.1234567890987654321234567890".getBytes())
                .add("19450508231415.1234567890987654321234567890Z".getBytes())
                .add("19450508231415.1234567890987654321234567890+10".getBytes())
                .add("19450508231415.1234567890987654321234567890+1030".getBytes())
                .add("19450508231415.1234567890987654321234567890-1030".getBytes())
                .add("19450508231415.123Z".getBytes())
                .add("19450508231415.123+10".getBytes())
                .add("19450508231415.123+1030".getBytes())
                .add("19450508231415.123-1030".getBytes())
                .build();

        logger.info("Start");
        for (byte[] dateString : rawDates)
        {
            assertEquals(0, instance.validate(dateString).size());
        }
        logger.info("End");

        // Minimal
        String rawDateTime = "1985010213";
        //Timestamp ts = getTimeStamp(parsedDateTime);
        assertEquals(0, instance.validate(rawDateTime.getBytes()).size());

        // comma as a separator
        rawDateTime = "1985010213,1";
        assertEquals(0, instance.validate(rawDateTime.getBytes()).size());
        // dot as a separator
        rawDateTime = "1985010213.1";
        assertEquals(0, instance.validate(rawDateTime.getBytes()).size());

        // with Zulu
        rawDateTime = "1985010213.1Z";
        assertEquals(0, instance.validate(rawDateTime.getBytes()).size());

        // With + offset, just hours
        rawDateTime = "1985010213.1+11";
        assertEquals(0, instance.validate(rawDateTime.getBytes()).size());

        // offset with minutes
        rawDateTime = "1985010213.1+0130";
        assertEquals(0, instance.validate(rawDateTime.getBytes()).size());

        // - offset
        rawDateTime = "1985010213.1-1022";
        assertEquals(0, instance.validate(rawDateTime.getBytes()).size());

        // include minutes
        rawDateTime = "198501021314";
        assertEquals(0, instance.validate(rawDateTime.getBytes()).size());

        rawDateTime = "198501021314Z";
        assertEquals(0, instance.validate(rawDateTime.getBytes()).size());

        rawDateTime = "198501021314.1Z";
        assertEquals(0, instance.validate(rawDateTime.getBytes()).size());

        rawDateTime = "198501021314.1+1100";
        assertEquals(0, instance.validate(rawDateTime.getBytes()).size());

        rawDateTime = "198501021314.1-1130";
        assertEquals(0, instance.validate(rawDateTime.getBytes()).size());

        // include seconds
        rawDateTime = "19850102131415";
        assertEquals(0, instance.validate(rawDateTime.getBytes()).size());

        rawDateTime = "19850102131415,1Z";
        assertEquals(0, instance.validate(rawDateTime.getBytes()).size());

        rawDateTime = "19850102131415.1+1100";
        assertEquals(0, instance.validate(rawDateTime.getBytes()).size());

        rawDateTime = "19850102131415.1-01";
        assertEquals(0, instance.validate(rawDateTime.getBytes()).size());

        rawDateTime = "19850102131415.123-0130";
        assertEquals(0, instance.validate(rawDateTime.getBytes()).size());

        rawDateTime = "19850102131415.678901234567890123";
        assertEquals(0, instance.validate(rawDateTime.getBytes()).size());

        rawDateTime
                = "19181111110000.123456789123456789123456789123456789123456789123456789123456789123456789123456789";
        assertEquals(0, instance.validate(rawDateTime.getBytes()).size());

        // Valid/Invalid times (not VisibleString)
        // 19181111110000.11
        bytes = new byte[] { '1', '9', '1', '8', '1', '1', '1', '1', '1', '1', '0', '0', '0', '0',
                             '.', '1', '1' };
        assertEquals(0, instance.validate(bytes).size());
        bytes = new byte[] { 0x1F, '9', '1', '8', '1', '1', '1', '1', '1', '1', '0', '0', '0', '0',
                             '.', '1', '1' };
        assertEquals(1, instance.validate(bytes).size());

        // Invalid - too many characters
        time = "191811111100000";
        bytes = time.getBytes();
        assertEquals(1, instance.validate(bytes).size());

        // Invalid - bad dates
        time = "2015022900"; // not a leap year
        bytes = time.getBytes();
        assertEquals(1, instance.validate(bytes).size());
        time = "2015130100"; // 13th month
        bytes = time.getBytes();
        assertEquals(1, instance.validate(bytes).size());
        time = "2015123200"; // 32nd day
        bytes = time.getBytes();
        assertEquals(1, instance.validate(bytes).size());
        // not exhaustively testing this as it is really a Joda issue

        // Invalid separators
        time = "19181111110000%11";
        bytes = time.getBytes();
        assertEquals(1, instance.validate(bytes).size());
        time = "19181111110000++11";
        bytes = time.getBytes();
        assertEquals(1, instance.validate(bytes).size());
        time = "1918-11-11 11:00:00";
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
