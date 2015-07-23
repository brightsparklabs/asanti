/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.DecodeException;
import org.junit.Test;

import java.sql.Timestamp;

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

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecode() throws Exception
    {
        // Minimum values (Local time)
        String time = "19700101000000.0";
        Timestamp expectedTime = Timestamp.valueOf("1970-01-01 00:00:00.0");
        byte[] bytes = time.getBytes();
        assertEquals(expectedTime, instance.decode(bytes));

        // Maximum values (Local time)
        time = "99991231235959.999999999";
        expectedTime = Timestamp.valueOf("9999-12-31 23:59:59.999");
        bytes = time.getBytes();
        assertEquals(expectedTime, instance.decode(bytes));

        // Another valid time (Local time)
        time = "19181111110000.123456789";
        expectedTime = Timestamp.valueOf("1918-11-11 11:00:00.123");
        bytes = time.getBytes();
        assertEquals(expectedTime, instance.decode(bytes));

        // Valid value (Universal time)
        time = "19450508230123.123Z";
        expectedTime = Timestamp.valueOf("1945-05-08 23:01:23.123");
        bytes = time.getBytes();
        assertEquals(expectedTime, instance.decode(bytes));

        // Valid values (Time differential)
        time = "19450508230112.123+0801";
        expectedTime = Timestamp.valueOf("9999-12-31 23:59:59.999999999");
        bytes = time.getBytes();
        assertEquals(expectedTime, instance.decode(bytes));
        time = "19450508230145.123-2359";
        expectedTime = Timestamp.valueOf("9999-12-31 23:59:59.999999999");
        bytes = time.getBytes();
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
        // Minimum values (Local time)
        // 19700101000000.0
        String time = "19700101000000.0";
        Timestamp expectedTime = Timestamp.valueOf("1970-01-01 00:00:00.0");

        byte[] bytes = time.getBytes();
        assertEquals(expectedTime.toString(), instance.decodeAsString(bytes));

        // Maximum values (Local time)
        time = "99991231235959.999999999999999999";
        expectedTime = Timestamp.valueOf("9999-12-31 23:59:59.999");
        bytes = time.getBytes();
        assertEquals(expectedTime.toString(), instance.decodeAsString(bytes));

        // Another valid time (Local time)
        time = "19181111110000.0123456789";
        expectedTime = Timestamp.valueOf("1918-11-11 11:00:00.012");
        bytes = time.getBytes();
        assertEquals(expectedTime.toString(), instance.decodeAsString(bytes));

        // Valid value (Universal time)
        time = "19450508230123.123Z";
        expectedTime = Timestamp.valueOf("1945-05-08 23:01:23.123");
        instance.decodeAsString(bytes);
        bytes = time.getBytes();
        assertEquals(expectedTime.toString(), instance.decodeAsString(bytes));

        // Valid values (Time differential)
        // 19450508000000.123+0800
        // 19450508230145.123-2359

        // TODO add tests here
        time = "19450508000000.123+0800";
        expectedTime = Timestamp.valueOf("1945-05-08 23:01:12.123");
        bytes = time.getBytes();
        assertEquals(expectedTime.toString(), instance.decodeAsString(bytes));
        time = "19450508230145.123-2359";
        bytes = time.getBytes();
        assertEquals(expectedTime.toString(), instance.decodeAsString(bytes));

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
