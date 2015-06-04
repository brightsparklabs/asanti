/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.DecodeException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Units tests for {@link NullDecoder}
 *
 * @author brightSPARK Labs
 */
public class NullDecoderTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final NullDecoder instance = NullDecoder.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecode() throws Exception
    {
        // test valid bytes
        byte[] bytes = new byte[0];
        assertEquals("", instance.decode(bytes));

        // test invalid bytes
        try
        {
            bytes = new byte[] { 0x00 };
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
        // test valid bytes
        byte[] bytes = new byte[0];
        assertEquals("", instance.decodeAsString(bytes));

        // test invalid bytes
        try
        {
            bytes = new byte[] { 0x00 };
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
