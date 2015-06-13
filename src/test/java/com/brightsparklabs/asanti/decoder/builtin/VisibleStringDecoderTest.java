/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.DecodeException;
import com.google.common.base.Charsets;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Units tests for {@link VisibleStringDecoder}
 *
 * @author brightSPARK Labs
 */
public class VisibleStringDecoderTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final VisibleStringDecoder instance = VisibleStringDecoder.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecode() throws Exception
    {
        // test valid
        byte[] bytes = new byte[1];
        for (int b = 126; b >= 32; b--)
        {
            bytes[0] = (byte) b;
            assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));
        }

        // test invalid
        for (byte b = Byte.MIN_VALUE; b < 32; b++)
        {
            bytes[0] = b;
            try
            {
                instance.decode(bytes);
                fail("DecodeException not thrown");
            }
            catch (DecodeException ex)
            {
            }
        }

        bytes[0] = Byte.MAX_VALUE;
        try
        {
            instance.decode(bytes);
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
        }

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
        // test valid
        byte[] bytes = "TEST".getBytes(Charsets.UTF_8);
        assertEquals("TEST", instance.decodeAsString(bytes));

        // test invalid
        for (byte b = Byte.MIN_VALUE; b < 32; b++)
        {
            bytes[0] = b;
            try
            {
                instance.decodeAsString(bytes);
                fail("DecodeException not thrown");
            }
            catch (DecodeException ex)
            {
            }
        }

        bytes[0] = Byte.MAX_VALUE;
        try
        {
            instance.decodeAsString(bytes);
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
