/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.DecodeException;
import org.junit.Test;
import org.mockito.internal.verification.Times;

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
            byte[] bytes = time.getBytes();
            instance.decode(bytes);

            fail("Should have thrown DecodeException");
        }
        catch (DecodeException e)
        {
        }
        try
        {
            String time = "2015022901";   // not a leap year
            byte[] bytes = time.getBytes();
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
        byte[] bytes = time.getBytes();
        long ms = expectedTime.getTime();
        assertEquals(ms, rawOffset);
        assertEquals(expectedTime, instance.decode(bytes));


        time = "1900010100";
        bytes = time.getBytes();
        Timestamp decoded = instance.decode(bytes);
        long expectedMs = -2208988800000L + rawOffset;
        assertEquals(expectedMs, decoded.getTime());


        time = "19850416141516.123";
        bytes = time.getBytes();
        decoded = instance.decode(bytes);
        expectedMs = 482508916123L + rawOffset;
        assertEquals(expectedMs, decoded.getTime());

        time = "19850416141516.123Z";
        bytes = time.getBytes();
        decoded = instance.decode(bytes);
        expectedMs = 482508916123L;
        assertEquals(expectedMs, decoded.getTime());

        time = "19850416141516.123-01";
        bytes = time.getBytes();
        decoded = instance.decode(bytes);
        expectedMs = 482508916123L + ONE_HOUR_IN_MILLI_SECONDS;
        assertEquals(expectedMs, decoded.getTime());

        time = "19850416141516.123+1030";
        bytes = time.getBytes();
        decoded = instance.decode(bytes);
        expectedMs = 482508916123L - (long)(ONE_HOUR_IN_MILLI_SECONDS * 10.5);
        assertEquals(expectedMs, decoded.getTime());

        time = "19850416141516.123+1031";
        bytes = time.getBytes();
        decoded = instance.decode(bytes);
        expectedMs = 482508916123L - (long)(ONE_HOUR_IN_MILLI_SECONDS * 10.5) - ONE_MINUTE_IN_MILLI_SECONDS;
        assertEquals(expectedMs, decoded.getTime());


        // Maximum values (Local time)
        time = "99991231235959.999999999";
        expectedTime = Timestamp.valueOf("9999-12-31 23:59:59.999999999");
        bytes = time.getBytes();
        assertEquals(expectedTime, instance.decode(bytes));

        // Another valid time (Local time)
        time = "19181111110000.123456789";
        expectedTime = Timestamp.valueOf("1918-11-11 11:00:00.123");
        bytes = time.getBytes();
        assertEquals(expectedTime.getTime(), instance.decode(bytes).getTime());

        // Zero Unix Epoch (Universal time)
        time = "1970010100Z";
        bytes = time.getBytes();
        assertEquals(0, instance.decode(bytes).getTime());


        // Sub milliseconds
        time = "19181111110000.123456789";
        expectedTime = Timestamp.valueOf("1918-11-11 11:00:00.123456789");
        bytes = time.getBytes();
        assertEquals(expectedTime, instance.decode(bytes));


        time = "19181111110000.123456789+01";
        expectedTime = Timestamp.valueOf("1918-11-11 11:00:00.123456789");

        ms = expectedTime.getTime();
        int ns = expectedTime.getNanos();
        expectedTime = new Timestamp(ms - rawOffset - ONE_HOUR_IN_MILLI_SECONDS);
        expectedTime.setNanos(ns);
        bytes = time.getBytes();
        assertEquals(expectedTime, instance.decode(bytes));

        time = "19181111110000.123456789Z";
        expectedTime = Timestamp.valueOf("1918-11-11 11:00:00.123456789");

        ms = expectedTime.getTime();
        ns = expectedTime.getNanos();
        expectedTime = new Timestamp(ms - rawOffset);
        expectedTime.setNanos(ns);
        bytes = time.getBytes();
        assertEquals(expectedTime, instance.decode(bytes));


        // Sub milliseconds
        // ensure we don't throw with lots of decimals, but for now we can only handle nano second
        // precision
        time = "19181111110000.123456789123456789123456789123456789123456789123456789123456789123456789123456789";
        expectedTime = Timestamp.valueOf("1918-11-11 11:00:00.123456789");
        bytes = time.getBytes();
        assertEquals(expectedTime, instance.decode(bytes));


        // TODO Play
        time = "20140525233541.0000Z";
        expectedTime = Timestamp.valueOf("2014-05-25 23:35:41.0");
        bytes = time.getBytes();
        ms = expectedTime.getTime();
        long decodedMS = instance.decode(bytes).getTime();
        String s = instance.decode(bytes).toString();

        time = "20140525233541.0000";
        expectedTime = Timestamp.valueOf("2014-05-25 23:35:41.0");
        bytes = time.getBytes();
        ms = expectedTime.getTime();
        decodedMS = instance.decode(bytes).getTime();
        s = instance.decode(bytes).toString();


        assertEquals(expectedTime, instance.decode(bytes));

        // test null
        try
        {
            instance.decode(null);
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
        }
    }

    @Test
    public void testDecodeAsString() throws Exception
    {

        // TODO MJF - given that we are dealing with a "useful" type, which is a VisibleString
        // at heart I think the decodeAsString should return the raw string (assume that the validator passes)


//        // Minimum values (Local time)
//        // 19700101000000.0
//        String time = "19700101000000.0";
//        Timestamp expectedTime = Timestamp.valueOf("1970-01-01 00:00:00.0");
//
//        byte[] bytes = time.getBytes();
//        assertEquals(expectedTime.toString(), instance.decodeAsString(bytes));
//
//        // Maximum values (Local time)
//        time = "99991231235959.999999999999999999";
//        expectedTime = Timestamp.valueOf("9999-12-31 23:59:59.999");
//        bytes = time.getBytes();
//        assertEquals(expectedTime.toString(), instance.decodeAsString(bytes));
//
//        // Another valid time (Local time)
//        time = "19181111110000.0123456789";
//        expectedTime = Timestamp.valueOf("1918-11-11 11:00:00.012");
//        bytes = time.getBytes();
//        assertEquals(expectedTime.toString(), instance.decodeAsString(bytes));
//
//        // Valid value (Universal time)
//        time = "19450508230123.123Z";
//        expectedTime = Timestamp.valueOf("1945-05-08 23:01:23.123");
//        instance.decodeAsString(bytes);
//        bytes = time.getBytes();
//        assertEquals(expectedTime.toString(), instance.decodeAsString(bytes));
//
//        // Valid values (Time differential)
//        // 19450508000000.123+0800
//        // 19450508230145.123-2359
//
//        // TODO add tests here
//        time = "19450508000000.123+0800";
//        expectedTime = Timestamp.valueOf("1945-05-08 23:01:12.123");
//        bytes = time.getBytes();
//        assertEquals(expectedTime.toString(), instance.decodeAsString(bytes));
//        time = "19450508230145.123-2359";
//        bytes = time.getBytes();
//        assertEquals(expectedTime.toString(), instance.decodeAsString(bytes));

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
