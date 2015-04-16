/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.typedefinitions;

import com.brightsparklabs.asanti.common.DecodeException;
import com.google.common.base.Charsets;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Units tests for {@link Ia5StringDecoder}
 *
 * @author brightSPARK Labs
 */
public class Ia5StringDecoderTest
{

    @Test
    public void testDecode() throws Exception
    {
        // test valid
        byte[] bytes = new byte[1];
        for (byte b = Byte.MAX_VALUE; b >= 0; b--)
        {
            bytes[0] = b;
            assertEquals(new String(bytes, Charsets.UTF_8),
                    Ia5StringDecoder.getInstance().decode(bytes));
        }

        // test invalid
        for (byte b = Byte.MIN_VALUE; b < 0; b++)
        {
            bytes[0] = b;
            try
            {
                Ia5StringDecoder.getInstance().decode(bytes);
                fail("DecodeException not thrown");
            }
            catch (DecodeException ex)
            {
            }
        }

        // test null
        try
        {
            Ia5StringDecoder.getInstance().decode(null);
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
        }
    }

    @Test
    public void testDecodeAsString() throws Exception
    {
        // test valid
        byte[] bytes = "TEST".getBytes(Charsets.UTF_8);
        assertEquals("TEST", Ia5StringDecoder.getInstance().decodeAsString(bytes));

        // test invalid
        for (byte b = Byte.MIN_VALUE; b < 0; b++)
        {
            bytes[0] = b;
            try
            {
                Ia5StringDecoder.getInstance().decodeAsString(bytes);
                fail("DecodeException not thrown");
            }
            catch (DecodeException ex)
            {
            }
        }

        // test null
        try
        {
            Ia5StringDecoder.getInstance().decodeAsString(null);
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
        }
    }
}
