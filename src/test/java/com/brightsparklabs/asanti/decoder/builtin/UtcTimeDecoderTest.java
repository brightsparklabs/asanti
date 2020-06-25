/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.decoder.builtin;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import com.brightsparklabs.asanti.exception.DecodeException;
import com.brightsparklabs.asanti.model.data.AsantiAsnData;
import com.google.common.base.Charsets;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Optional;
import org.junit.Test;

/**
 * Units tests for {@link UtcTimeDecoder}
 *
 * @author brightSPARK Labs
 */
public class UtcTimeDecoderTest {
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final UtcTimeDecoder instance = UtcTimeDecoder.getInstance();

    private static final long ONE_HOUR_IN_MILLI_SECONDS = 1000 * 60 * 60;

    private static final long ONE_MINUTE_IN_MILLI_SECONDS = 1000 * 60;
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecodeErrors() throws Exception {
        try {
            String time = "700101"; // not enough bytes - no hours
            byte[] bytes = time.getBytes(Charsets.UTF_8);
            instance.decode(bytes);

            fail("Should have thrown DecodeException");
        } catch (DecodeException e) {
        }
        try {
            String time = "15022901"; // not a leap year
            byte[] bytes = time.getBytes(Charsets.UTF_8);
            instance.decode(bytes);

            fail("Should have thrown DecodeException");
        } catch (DecodeException e) {
        }

        try {
            instance.decode(null);
            fail("Should have thrown DecodeExceptions");
        } catch (DecodeException e) {
        }
        try {
            instance.decodeAsString(null);
            fail("Should have thrown DecodeExceptions");
        } catch (DecodeException e) {
        }

        // null for tag and AsantiAsnData
        try {
            AsantiAsnData data = mock(AsantiAsnData.class);
            instance.decode(null, data);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
        }
        try {
            instance.decode("someTag", null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
        }
        try {
            AsantiAsnData data = mock(AsantiAsnData.class);
            instance.decodeAsString(null, data);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
        }
        try {
            instance.decodeAsString("someTag", null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void testDecode() throws Exception {
        final Calendar calendar = Calendar.getInstance();
        final int rawOffset = -1 * calendar.getTimeZone().getRawOffset();

        // Minimum values (Local time)
        String time = "700101000000";
        final Instant of = Instant.parse("1970-01-01T00:00:00Z");
        final ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset(of);
        OffsetDateTime expectedTime = OffsetDateTime.of(1970, 1, 1, 0, 0, 0, 0, offset);

        byte[] bytes = time.getBytes(Charsets.UTF_8);
        long ms = expectedTime.toInstant().toEpochMilli();
        assertEquals(ms, rawOffset);
        assertEquals(expectedTime, instance.decode(bytes));

        // check that the other overload works
        AsantiAsnData data = mock(AsantiAsnData.class);
        when(data.getBytes(anyString())).thenReturn(Optional.empty());
        final String tag = "tag";
        when(data.getBytes(eq(tag))).thenReturn(Optional.of(time.getBytes(Charsets.UTF_8)));

        assertEquals(expectedTime, instance.decode(tag, data));

        time = "850416141516";
        bytes = time.getBytes(Charsets.UTF_8);
        OffsetDateTime decoded = instance.decode(bytes);
        long expectedMs = 482508916000L + rawOffset;
        assertEquals(expectedMs, decoded.toInstant().toEpochMilli());

        time = "850416141516-01";
        bytes = time.getBytes(Charsets.UTF_8);
        decoded = instance.decode(bytes);
        expectedMs = 482508916000L + ONE_HOUR_IN_MILLI_SECONDS;
        assertEquals(expectedMs, decoded.toInstant().toEpochMilli());

        time = "850416141516+1030";
        bytes = time.getBytes(Charsets.UTF_8);
        decoded = instance.decode(bytes);
        expectedMs = 482508916000L - (long) (ONE_HOUR_IN_MILLI_SECONDS * 10.5);
        assertEquals(expectedMs, decoded.toInstant().toEpochMilli());

        time = "850416141516+1031";
        bytes = time.getBytes(Charsets.UTF_8);
        decoded = instance.decode(bytes);
        expectedMs =
                482508916000L
                        - (long) (ONE_HOUR_IN_MILLI_SECONDS * 10.5)
                        - ONE_MINUTE_IN_MILLI_SECONDS;
        assertEquals(expectedMs, decoded.toInstant().toEpochMilli());

        // Maximum values (Local time)
        time = "991231235959Z";
        expectedTime = OffsetDateTime.of(1999, 12, 31, 23, 59, 59, 0, ZoneOffset.UTC);
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(
                expectedTime.toInstant().toEpochMilli(),
                instance.decode(bytes).toInstant().toEpochMilli());

        // Another valid time (Local summer time)
        time = "181111110000";
        // figure out the offset at this time.
        final Instant of2 = Instant.parse("2018-11-11T11:00:00Z");
        final ZoneOffset offset2 = ZoneId.systemDefault().getRules().getOffset(of2);
        expectedTime = OffsetDateTime.of(2018, 11, 11, 11, 0, 0, 0, offset2);
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(
                expectedTime.toInstant().toEpochMilli(),
                instance.decode(bytes).toInstant().toEpochMilli());

        // Another valid time (Local winter time)
        time = "180611110000";
        // figure out the offset at this time.
        final Instant of3 = Instant.parse("2018-06-11T11:00:00Z");
        final ZoneOffset offset3 = ZoneId.systemDefault().getRules().getOffset(of3);
        expectedTime = OffsetDateTime.of(2018, 6, 11, 11, 0, 0, 0, offset3);
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(
                expectedTime.toInstant().toEpochMilli(),
                instance.decode(bytes).toInstant().toEpochMilli());

        // Test pivot year 2000 minimum (1950)
        time = "500101000000Z";
        expectedTime = OffsetDateTime.of(1950, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(
                expectedTime.toInstant().toEpochMilli(),
                instance.decode(bytes).toInstant().toEpochMilli());

        // Test pivot year 2000 minimum (1950)
        time = "500101000000";
        final Instant of4 = Instant.parse("1950-01-01T00:00:00Z");
        final ZoneOffset offset4 = ZoneId.systemDefault().getRules().getOffset(of4);
        expectedTime = OffsetDateTime.of(1950, 1, 1, 0, 0, 0, 0, offset4);
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(
                expectedTime.toInstant().toEpochMilli(),
                instance.decode(bytes).toInstant().toEpochMilli());

        // Test pivot year 2000 maximum (2049)
        time = "491231235959Z";
        // expectedTime = Timestamp.valueOf("2049-12-31 23:59:59");
        expectedTime = OffsetDateTime.of(2049, 12, 31, 23, 59, 59, 0, ZoneOffset.UTC);
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(
                expectedTime.toInstant().toEpochMilli(),
                instance.decode(bytes).toInstant().toEpochMilli());

        // Zero Unix Epoch (Universal time)
        time = "7001010000Z";
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(0, instance.decode(bytes).toInstant().toEpochMilli());

        // null
        try {
            instance.decode(null);
            fail("DecodeException not thrown");
        } catch (DecodeException ex) {
        }

        // test our specific validation bits
        try {
            instance.decode("0001010000z".getBytes(Charsets.UTF_8)); // should be upper case
            fail("DecodeException not thrown");
        } catch (DecodeException ex) {
        }

        try {
            bytes = new byte[] {0x1F, '8', '1', '1', '1', '1', '1', '1', '0', '0', '0', '0'};
            instance.decode(bytes); // not a valid VisibleString
            fail("DecodeException not thrown");
        } catch (DecodeException ex) {
        }
    }

    @Test
    public void testDecodeAsString() throws Exception {
        // The expected behaviour is that decodeAsString should throw if validation fails,
        // otherwise it should return the "raw" input string.
        String time = "700101000000";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "700101000000";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "0001010000";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "850416141516";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "850416141516Z";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "850416141516-01";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "850416141516+1030";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "850416141516+1031";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "991231235959";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "7001010000Z";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "181111110000";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "181111110000+01";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "181111110000Z";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));

        try {
            instance.decodeAsString("27111100".getBytes(Charsets.UTF_8)); // Too short
            fail("DecodeException not thrown");
        } catch (DecodeException ex) {
        }

        // test null
        try {
            instance.decodeAsString(null);
            fail("DecodeException not thrown");
        } catch (DecodeException ex) {
        }
    }

    @Test
    public void testDecodeAsStringOverload() throws Exception {
        AsantiAsnData data = mock(AsantiAsnData.class);
        when(data.getBytes(anyString())).thenReturn(Optional.empty());

        final String tag1 = "tag1";
        final String time1 = "700101000000";
        final byte[] bytes1 = time1.getBytes(Charsets.UTF_8);
        when(data.getBytes(eq(tag1))).thenReturn(Optional.of(bytes1));
        final String tag2 = "tag2";
        final String time2 = "0001010000";
        final byte[] bytes2 = time2.getBytes(Charsets.UTF_8);
        when(data.getBytes(eq(tag2))).thenReturn(Optional.of(bytes2));
        final String tag3 = "tag3";
        final String time3 = "850416141516";
        final byte[] bytes3 = time3.getBytes(Charsets.UTF_8);
        when(data.getBytes(eq(tag3))).thenReturn(Optional.of(bytes3));
        final String tag4 = "tag4";
        final String time4 = "850416141516Z";
        final byte[] bytes4 = time4.getBytes(Charsets.UTF_8);
        when(data.getBytes(eq(tag4))).thenReturn(Optional.of(bytes4));

        assertEquals(time1, instance.decodeAsString(tag1, data));
        assertEquals(time2, instance.decodeAsString(tag2, data));
        assertEquals(time3, instance.decodeAsString(tag3, data));
        assertEquals(time4, instance.decodeAsString(tag4, data));

        // empty bytes
        try {
            final String tagEmpty = "tagEmpty";
            final byte[] bytesEmpty = new byte[0];
            when(data.getBytes(eq(tagEmpty))).thenReturn(Optional.of(bytesEmpty));
            instance.decodeAsString(tagEmpty, data);
            fail("DecodeExceptions not thrown");
        } catch (DecodeException e) {
        }
    }
}
