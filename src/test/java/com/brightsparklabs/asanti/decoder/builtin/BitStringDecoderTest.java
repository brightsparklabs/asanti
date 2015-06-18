/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.DecodeException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Units tests for {@link BitStringDecoder}
 *
 * @author brightSPARK Labs
 */
public class BitStringDecoderTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final BitStringDecoder instance = BitStringDecoder.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecode() throws Exception
    {
        // test valid values of unused bits with single byte bit string
        byte[] bytes = { (byte) 0x05, (byte) 0xE0 };
        assertEquals("111", instance.decode(bytes));

        bytes = new byte[] { (byte) 0x04, (byte) 0xE0 };
        assertEquals("1110", instance.decode(bytes));

        bytes = new byte[] { (byte) 0x07, (byte) 0x00 };
        assertEquals("0", instance.decode(bytes));

        bytes = new byte[] { (byte) 0x00, (byte) 0xFF };
        assertEquals("11111111", instance.decode(bytes));

        // showing how the unused bits octet ignores trailing bits of last octet
        bytes = new byte[] { (byte) 0x07, (byte) 0xFF };
        assertEquals("1", instance.decode(bytes));

        // test valid values of unused bits with two byte bit string
        bytes = new byte[] { (byte) 0x06, (byte) 0xAA, (byte) 0x80 };
        assertEquals("1010101010", instance.decode(bytes));

        // test valid values of unused bits with two byte bit string
        bytes = new byte[] { (byte) 0x00, (byte) 0xAA, (byte) 0x80 };
        assertEquals("1010101010000000", instance.decode(bytes));

        // empty string
        bytes = new byte[] { (byte) 0x00 };
        assertEquals("", instance.decode(bytes));

        // test invalid length octet (single byte)
        try
        {
            bytes = new byte[] { (byte) 0x08, (byte) 0xFF };
            instance.decode(bytes);
            fail("DecodeException not thrown");
        }
        catch (DecodeException e)
        {
        }

        // test invalid length octet (two bytes)
        try
        {
            bytes = new byte[] { (byte) 0x0F, (byte) 0xFF, (byte) 0xE0 };
            instance.decode(bytes);
            fail("DecodeException not thrown");
        }
        catch (DecodeException e)
        {
        }

        // test empty byte array
        try
        {
            bytes = new byte[] { };
            instance.decode(bytes);
            fail("DecodeException not thrown");
        }
        catch (DecodeException e)
        {
        }
    }

    @Test
    public void testDecodeAsString() throws Exception
    {
        // test that decodeAsString produces the same value as decode
        byte[] bytes = { (byte) 0x05, (byte) 0xE0 };
        assertEquals(instance.decode(bytes), instance.decodeAsString(bytes));
    }
}
