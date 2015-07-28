/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.DecodeException;
import com.google.common.base.Charsets;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Units tests for {@link GeneralizedTimeDecoder}
 *
 * @author brightSPARK Labs
 */
public class GeneralizedTimeDecoderTest
{
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
    public void testDecodeErrors() throws Exception
    {
        try
        {
            String time = "19700101";   // not enough bytes - no hours
            byte[] bytes = time.getBytes(Charsets.UTF_8);
            instance.decode(bytes);

            fail("Should have thrown DecodeException");
        }
        catch (DecodeException e)
        {
        }
        try
        {
            String time = "2015022901";   // not a leap year
            byte[] bytes = time.getBytes(Charsets.UTF_8);
            instance.decode(bytes);

            fail("Should have thrown DecodeException");
        }
        catch (DecodeException e)
        {
        }

    }

    @Test
    public void testDecode() throws Exception
    {

        final Calendar calendar = Calendar.getInstance();
        final int rawOffset = -1 * calendar.getTimeZone().getRawOffset();

        // Minimum values (Local time)
        String time = "19700101000000.0";
        Timestamp expectedTime = Timestamp.valueOf("1970-01-01 00:00:00.0");
        byte[] bytes = time.getBytes(Charsets.UTF_8);
        long ms = expectedTime.getTime();
        assertEquals(ms, rawOffset);
        assertEquals(expectedTime, instance.decode(bytes));

        time = "1900010100";
        bytes = time.getBytes(Charsets.UTF_8);
        Timestamp decoded = instance.decode(bytes);
        long expectedMs = -2208988800000L + rawOffset;
        assertEquals(expectedMs, decoded.getTime());

        time = "19850416141516.123";
        bytes = time.getBytes(Charsets.UTF_8);
        decoded = instance.decode(bytes);
        expectedMs = 482508916123L + rawOffset;
        assertEquals(expectedMs, decoded.getTime());

        time = "19850416141516.123Z";
        bytes = time.getBytes(Charsets.UTF_8);
        decoded = instance.decode(bytes);
        expectedMs = 482508916123L;
        assertEquals(expectedMs, decoded.getTime());

        time = "19850416141516.123-01";
        bytes = time.getBytes(Charsets.UTF_8);
        decoded = instance.decode(bytes);
        expectedMs = 482508916123L + ONE_HOUR_IN_MILLI_SECONDS;
        assertEquals(expectedMs, decoded.getTime());

        time = "19850416141516.123+1030";
        bytes = time.getBytes(Charsets.UTF_8);
        decoded = instance.decode(bytes);
        expectedMs = 482508916123L - (long) (ONE_HOUR_IN_MILLI_SECONDS * 10.5);
        assertEquals(expectedMs, decoded.getTime());

        time = "19850416141516.123+1031";
        bytes = time.getBytes(Charsets.UTF_8);
        decoded = instance.decode(bytes);
        expectedMs = 482508916123L - (long) (ONE_HOUR_IN_MILLI_SECONDS * 10.5)
                - ONE_MINUTE_IN_MILLI_SECONDS;
        assertEquals(expectedMs, decoded.getTime());

        // Maximum values (Local time)
        time = "99991231235959.999999999";
        expectedTime = Timestamp.valueOf("9999-12-31 23:59:59.999999999");
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(expectedTime, instance.decode(bytes));

        // Another valid time (Local time)
        time = "19181111110000.123456789";
        expectedTime = Timestamp.valueOf("1918-11-11 11:00:00.123");
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(expectedTime.getTime(), instance.decode(bytes).getTime());

        // Zero Unix Epoch (Universal time)
        time = "1970010100Z";
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(0, instance.decode(bytes).getTime());

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

        // null
        try
        {
            instance.decode(null);
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
        }

        // test our specific validation bits
        try
        {
            instance.decode("1900010100z".getBytes(Charsets.UTF_8)); // should be upper case
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
        }

        try
        {
            bytes = new byte[] { 0x1F, '9', '1', '8', '1', '1', '1', '1', '1', '1', '0', '0', '0', '0',
                                 '.', '1', '1' };
            instance.decode(bytes); // not a valid VisibleString
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
        }

    }

    @Test
    public void testDecodeAsString() throws Exception
    {

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
        time
                = "19181111110000.123456789123456789123456789123456789123456789123456789123456789123456789123456789";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));

        try
        {
            instance.decodeAsString("19271111".getBytes(Charsets.UTF_8));    // Too short
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
        }

        // test null
        try
        {
            instance.decodeAsString(null);
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
        }
    }
}
