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
 * Units tests for {@link TimeDecoder}
 *
 * @author brightSPARK Labs
 */
public class TimeDecoderTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final TimeDecoder instance = TimeDecoder.getInstance();

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
            String time = "700101";   // not enough bytes - no hours
            byte[] bytes = time.getBytes(Charsets.UTF_8);
            instance.decode(bytes);

            fail("Should have thrown DecodeException");
        }
        catch (DecodeException e)
        {
        }
        try
        {
            String time = "15022901";   // not a leap year
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
        String time = "700101000000";
        Timestamp expectedTime = Timestamp.valueOf("1970-01-01 00:00:00.0");
        byte[] bytes = time.getBytes(Charsets.UTF_8);
        long ms = expectedTime.getTime();
        assertEquals(ms, rawOffset);
        assertEquals(expectedTime, instance.decode(bytes));

        time = "850416141516";
        bytes = time.getBytes(Charsets.UTF_8);
        Timestamp decoded = instance.decode(bytes);
        long expectedMs = 482508916000L + rawOffset;
        assertEquals(expectedMs, decoded.getTime());

        time = "850416141516-01";
        bytes = time.getBytes(Charsets.UTF_8);
        decoded = instance.decode(bytes);
        expectedMs = 482508916000L + ONE_HOUR_IN_MILLI_SECONDS;
        assertEquals(expectedMs, decoded.getTime());

        time = "850416141516+1030";
        bytes = time.getBytes(Charsets.UTF_8);
        decoded = instance.decode(bytes);
        expectedMs = 482508916000L - (long) (ONE_HOUR_IN_MILLI_SECONDS * 10.5);
        assertEquals(expectedMs, decoded.getTime());

        time = "850416141516+1031";
        bytes = time.getBytes(Charsets.UTF_8);
        decoded = instance.decode(bytes);
        expectedMs = 482508916000L - (long) (ONE_HOUR_IN_MILLI_SECONDS * 10.5)
                - ONE_MINUTE_IN_MILLI_SECONDS;
        assertEquals(expectedMs, decoded.getTime());

        // Maximum values (Local time)
        time = "991231235959";
        expectedTime = Timestamp.valueOf("1999-12-31 23:59:59");
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(expectedTime, instance.decode(bytes));

        // Another valid time (Local time)
        time = "181111110000";
        expectedTime = Timestamp.valueOf("2018-11-11 11:00:00");
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(expectedTime.getTime(), instance.decode(bytes).getTime());

        // Test pivot year 2000 minimum (1950)
        time = "500101000000";
        expectedTime = Timestamp.valueOf("1950-01-01 00:00:00");
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(expectedTime.getTime(), instance.decode(bytes).getTime());

        // Test pivot year 2000 maximum (2049)
        time = "491231235959";
        expectedTime = Timestamp.valueOf("2049-12-31 23:59:59");
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(expectedTime.getTime(), instance.decode(bytes).getTime());

        // Zero Unix Epoch (Universal time)
        time = "70010100Z";
        bytes = time.getBytes(Charsets.UTF_8);
        assertEquals(0, instance.decode(bytes).getTime());

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
            instance.decode("00010100z".getBytes(Charsets.UTF_8)); // should be upper case
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
        }

        try
        {
            bytes = new byte[] { 0x1F, '8', '1', '1', '1', '1', '1', '1', '0', '0', '0', '0' };
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
        String time = "700101000000";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "700101000000";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "00010100";
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
        time = "70010100Z";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "181111110000";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "181111110000+01";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));
        time = "181111110000Z";
        assertEquals(time, instance.decodeAsString(time.getBytes(Charsets.UTF_8)));

        try
        {
            instance.decodeAsString("271111".getBytes(Charsets.UTF_8));    // Too short
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
