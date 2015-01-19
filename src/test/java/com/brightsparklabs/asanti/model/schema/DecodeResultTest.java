/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema;

import static org.junit.Assert.*;

import java.sql.Timestamp;

import org.junit.Test;

/**
 * Unit test for {@link DecodeResult}
 *
 * @author brightSPARK Labs
 */
public class DecodeResultTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testWasSuccessful() throws Exception
    {
        DecodeResult<String> instance = DecodeResult.create(true, "TEST");
        assertEquals(true, instance.wasSuccessful());
        instance = DecodeResult.create(false, "TEST");
        assertEquals(false, instance.wasSuccessful());
    }

    @Test
    public void testGetDecodedData() throws Exception
    {
        DecodeResult<String> instanceString = DecodeResult.create(true, "TEST");
        assertEquals("TEST", instanceString.getDecodedData());
        instanceString = DecodeResult.create(false, "TEST");
        assertEquals("TEST", instanceString.getDecodedData());

        DecodeResult<Integer> instanceInteger = DecodeResult.create(true, Integer.MIN_VALUE);
        assertEquals((Integer) Integer.MIN_VALUE, instanceInteger.getDecodedData());
        instanceInteger = DecodeResult.create(true, Integer.MAX_VALUE);
        assertEquals((Integer) Integer.MAX_VALUE, instanceInteger.getDecodedData());
        instanceInteger = DecodeResult.create(true, 0);
        assertEquals((Integer) 0, instanceInteger.getDecodedData());

        final Timestamp expected = new Timestamp(1000);
        final DecodeResult<Timestamp> instanceTimestamp = DecodeResult.create(true, expected);
        assertEquals(expected, instanceTimestamp.getDecodedData());
    }
}
