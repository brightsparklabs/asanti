/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.decoder.builtin;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
 * Units tests for {@link GeneralizedTimeDecoder}
 *
 * @author brightSPARK Labs
 */
public class GeneralizedTimeDecoderTest {
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final GeneralizedTimeDecoder instance = GeneralizedTimeDecoder.getInstance();

    private static final long ONE_HOUR_IN_MILLI_SECONDS = 1000 * 60 * 60;

    private static final long ONE_MINUTE_IN_MILLI_SECONDS = 1000 * 60;
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecodeErrors() throws Exception {
        try {
            String time = "19700101"; // not enough bytes - no hours
            byte[] bytes = time.getBytes(Charsets.UTF_8);
            instance.decode(bytes);

            fail("Should have thrown DecodeExceptions");
        } catch (DecodeException e) {
        }
        try {
            String time = "19700101"; // not enough bytes - no hours
            byte[] bytes = time.getBytes(Charsets.UTF_8);
            instance.decodeAsString(bytes);

            fail("Should have thrown DecodeExceptions");
        } catch (DecodeException e) {
        }
        try {
            String time = "2015022901"; // not a leap year
            byte[] bytes = time.getBytes(Charsets.UTF_8);
            instance.decode(bytes);

            fail("Should have thrown DecodeExceptions");
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
        String time = "19700101000000.0";
        // Timestamp expectedTime = Timestamp.valueOf("1970-01-01 00:00:00.0");
        final Instant of = Instant.parse("1970-01-01T00:00:00Z");
        final ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset(of);
        OffsetDateTime expectedTime = OffsetDateTime.of(1970, 1, 1, 0, 0, 0, 0, offset);
        // final OffsetDateTime expectedTime = OffsetDateTime.of(1970, 01, 01, 0, 0, 0, 0,
        // OffsetDateTime.now().getOffset());
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

        time = "1900010100";
        bytes = time.getBytes(Charsets.UTF_8);
        OffsetDateTime decoded = instance.decode(bytes);
        long expectedMs = -2208988800000L + rawOffset;
        assertEquals(expectedMs, decoded.toInstant().toEpochMilli());

        time = "19850416141516.123";
        bytes = time.getBytes(Charsets.UTF_8);
        decoded = instance.decode(bytes);
        expectedMs = 482508916123L + rawOffset;
        assertEquals(expectedMs, decoded.toInstant().toEpochMilli());

        time = "19850416141516.123Z";
        bytes = time.getBytes(Charsets.UTF_8);
        decoded = instance.decode(bytes);
        expectedMs = 482508916123L;
        assertEquals(expectedMs, decoded.toInstant().toEpochMilli());

        time = "19850416141516.123-01";
        bytes = time.getBytes(Charsets.UTF_8);
        decoded = instance.decode(bytes);
        expectedMs = 482508916123L + ONE_HOUR_IN_MILLI_SECONDS;
        assertEquals(expectedMs, decoded.toInstant().toEpochMilli());

        time = "19850416141516.123+1030";
        bytes = time.getBytes(Charsets.UTF_8);
        decoded = instance.decode(bytes);
        expectedMs = 482508916123L - (long) (ONE_HOUR_IN_MILLI_SECONDS * 10.5);
        assertEquals(expectedMs, decoded.toInstant().toEpochMilli());

        time = "19850416141516.123+1031";
        bytes = time.getBytes(Charsets.UTF_8);
        decoded = instance.decode(bytes);
        expectedMs =
                482508916123L
                        - (long) (ONE_HOUR_IN_MILLI_SECONDS * 10.5)
                        - ONE_MINUTE_IN_MILLI_SECONDS;
        assertEquals(expectedMs, decoded.toInstant().toEpochMilli());

        // Maximum values (Local time)
        time = "99991231235959.999999999Z";
        expectedTime = OffsetDateTime.of(9999, 12, 31, 23, 59, 59, 999999999, ZoneOffset.UTC);
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(expectedTime.toInstant(), instance.decode(bytes).toInstant());

        // Another valid time (Local time)
        time = "19181111110000.123456789Z";
        expectedTime = OffsetDateTime.of(1918, 11, 11, 11, 0, 0, 123456789, ZoneOffset.UTC);
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(expectedTime.toInstant(), instance.decode(bytes).toInstant());
        /*
                // Zero Unix Epoch (Universal time)
                time = "1970010100Z";
                bytes = time.getBytes(Charsets.UTF_8);
                assertEquals(0, instance.decode(bytes).toInstant().toEpochMilli());

                // Sub milliseconds
                time = "19181111110000.123456789";
                expectedTime = Timestamp.valueOf("1918-11-11 11:00:00.123456789");
                bytes = time.getBytes(Charsets.UTF_8);
                assertEquals(expectedTime, instance.decode(bytes));

                time = "19181111110000.123456789+01";
                expectedTime = Timestamp.valueOf("1918-11-11 11:00:00.123456789");

                ms = expectedTime.getTime();
                int ns = expectedTime.getNanos();
                expectedTime = new Timestamp(ms - rawOffset - ONE_HOUR_IN_MILLI_SECONDS);
                expectedTime.setNanos(ns);
                bytes = time.getBytes(Charsets.UTF_8);
                assertEquals(expectedTime, instance.decode(bytes));

                time = "19181111110000.123456789Z";
                expectedTime = Timestamp.valueOf("1918-11-11 11:00:00.123456789");

                ms = expectedTime.getTime();
                ns = expectedTime.getNanos();
                expectedTime = new Timestamp(ms - rawOffset);
                expectedTime.setNanos(ns);
                bytes = time.getBytes(Charsets.UTF_8);
                assertEquals(expectedTime, instance.decode(bytes));

                // Sub milliseconds
                // ensure we don't throw with lots of decimals, but for now we can only handle nano second
                // precision
                time
                        = "19181111110000.123456789123456789123456789123456789123456789123456789123456789123456789123456789";
                expectedTime = Timestamp.valueOf("1918-11-11 11:00:00.123456789");
                bytes = time.getBytes(Charsets.UTF_8);
                assertEquals(expectedTime, instance.decode(bytes));

                // More than 18 decimal places - minutes
                // Note that we discard everything after milliseconds...
                time
                        = "191811111100.123456789123456789123456789123456789123456789123456789123456789123456789123456789";
                expectedTime = Timestamp.valueOf("1918-11-11 11:00:07.407");
                bytes = time.getBytes(Charsets.UTF_8);
                assertEquals(expectedTime, instance.decode(bytes));

                // time than 18 decimal places - hours
                // Note that we discard everything after milliseconds...
                time
                        = "1918111111.123456789123456789123456789123456789123456789123456789123456789123456789123456789";
                // my manual calculation thinks that it should be 444 milliseconds.  Joda seems to disagree...
                expectedTime = Timestamp.valueOf("1918-11-11 11:07:24.443");
                bytes = time.getBytes(Charsets.UTF_8);
                assertEquals(expectedTime, instance.decode(bytes));
        */
        // null
        try {
            instance.decode(null);
            fail("DecodeExceptions not thrown");
        } catch (DecodeException ex) {
        }

        // test our specific validation bits
        try {
            instance.decode("1900010100z".getBytes(Charsets.UTF_8)); // should be upper case
            fail("DecodeExceptions not thrown");
        } catch (DecodeException ex) {
        }

        try {
            bytes =
                    new byte[] {
                        0x1F, '9', '1', '8', '1', '1', '1', '1', '1', '1', '0', '0', '0', '0', '.',
                        '1', '1'
                    };
            instance.decode(bytes); // not a valid VisibleString
            fail("DecodeExceptions not thrown");
        } catch (DecodeException ex) {
        }
    }

    @Test
    public void testDecodeAsString() throws Exception {

        // The expected behaviour is that decodeAsString should throw if validation fails,
        // otherwise it should return the "raw" input string.
        // This includes if the raw string had precision digits that were discarded during parsing
        // ie sub milliseconds

        String time = "19700101000000.0";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));

        time = "19700101000000.0";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "1900010100";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "19850416141516.123";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "19850416141516.123Z";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "19850416141516.123-01";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "19850416141516.123+1030";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "19850416141516.123+1031";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "99991231235959.999999999";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "19181111110000.123456789";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "1970010100Z";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "19181111110000.123456789";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "19181111110000.123456789+01";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "19181111110000.123456789Z";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time =
                "19181111110000.123456789123456789123456789123456789123456789123456789123456789123456789123456789";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));

        try {
            instance.decodeAsString("19271111".getBytes(Charsets.UTF_8)); // Too short
            fail("DecodeExceptions not thrown");
        } catch (DecodeException ex) {
        }

        // test null
        try {
            instance.decodeAsString(null);
            fail("DecodeExceptions not thrown");
        } catch (DecodeException ex) {
        }
    }

    @Test
    public void testDecodeAsStringOverload() throws Exception {
        AsantiAsnData data = mock(AsantiAsnData.class);
        when(data.getBytes(anyString())).thenReturn(Optional.empty());

        final String tag1 = "tag1";
        final String time1 = "19700101000000.0";
        final byte[] bytes1 = time1.getBytes(Charsets.UTF_8);
        when(data.getBytes(eq(tag1))).thenReturn(Optional.of(bytes1));
        final String tag2 = "tag2";
        final String time2 = "1900010100";
        final byte[] bytes2 = time2.getBytes(Charsets.UTF_8);
        when(data.getBytes(eq(tag2))).thenReturn(Optional.of(bytes2));
        final String tag3 = "tag3";
        final String time3 = "19850416141516.123";
        final byte[] bytes3 = time3.getBytes(Charsets.UTF_8);
        when(data.getBytes(eq(tag3))).thenReturn(Optional.of(bytes3));
        final String tag4 = "tag4";
        final String time4 = "19850416141516.123Z";
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
