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
        // test valid single byte values
        byte[] bytes = new byte[1];
        for (int b = Byte.MAX_VALUE; b >= Byte.MIN_VALUE; b--)
        {
            bytes[0] = (byte) b;
            final String binaryString = String.format("%8s", Integer.toBinaryString(b & 0xFF))
                    .replace(' ', '0');
            assertEquals(binaryString, instance.decode(bytes));
        }

        // test valid - two bytes (minimum/maximum)
        bytes = new byte[] { (byte) 0b11000010, (byte) 0b10000000 };
        assertEquals("1100001010000000", instance.decode(bytes));
        bytes = new byte[] { (byte) 0b11011111, (byte) 0b10111111 };
        assertEquals("1101111110111111", instance.decode(bytes));

        // test valid - three bytes (minimum/maximum)
        bytes = new byte[] { (byte) 0b11100010, (byte) 0b10000000, (byte) 0b10000000 };
        assertEquals("111000101000000010000000", instance.decode(bytes));
        bytes = new byte[] { (byte) 0b11101111, (byte) 0b10111111, (byte) 0b10111111 };
        assertEquals("111011111011111110111111", instance.decode(bytes));

        // test valid - four bytes (minimum/maximum)
        bytes = new byte[] { (byte) 0b11110010, (byte) 0b10000000, (byte) 0b10000000,
                             (byte) 0b10000000 };
        assertEquals("11110010100000001000000010000000", instance.decode(bytes));
        bytes = new byte[] { (byte) 0b11110000, (byte) 0b10111111, (byte) 0b10111111,
                             (byte) 0b10111111 };
        assertEquals("11110000101111111011111110111111", instance.decode(bytes));

        //test null
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
        // test valid single byte values
        byte[] bytes = new byte[1];
        for (int b = Byte.MAX_VALUE; b >= Byte.MIN_VALUE; b--)
        {
            bytes[0] = (byte) b;
            final String binaryString = String.format("%8s", Integer.toBinaryString(b & 0xFF))
                    .replace(' ', '0');
            assertEquals(binaryString, instance.decodeAsString(bytes));
        }

        // test valid - two bytes (minimum/maximum)
        bytes = new byte[] { (byte) 0b11000010, (byte) 0b10000000 };
        assertEquals("1100001010000000", instance.decodeAsString(bytes));
        bytes = new byte[] { (byte) 0b11011111, (byte) 0b10111111 };
        assertEquals("1101111110111111", instance.decodeAsString(bytes));

        // test valid - three bytes (minimum/maximum)
        bytes = new byte[] { (byte) 0b11100010, (byte) 0b10000000, (byte) 0b10000000 };
        assertEquals("111000101000000010000000", instance.decodeAsString(bytes));
        bytes = new byte[] { (byte) 0b11101111, (byte) 0b10111111, (byte) 0b10111111 };
        assertEquals("111011111011111110111111", instance.decodeAsString(bytes));

        // test valid - four bytes (minimum/maximum)
        bytes = new byte[] { (byte) 0b11110010, (byte) 0b10000000, (byte) 0b10000000,
                             (byte) 0b10000000 };
        assertEquals("11110010100000001000000010000000", instance.decodeAsString(bytes));
        bytes = new byte[] { (byte) 0b11110000, (byte) 0b10111111, (byte) 0b10111111,
                             (byte) 0b10111111 };
        assertEquals("11110000101111111011111110111111", instance.decodeAsString(bytes));

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
