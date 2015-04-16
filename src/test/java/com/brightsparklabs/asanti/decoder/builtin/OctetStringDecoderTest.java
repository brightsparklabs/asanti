/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.DecodeException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Units tests for {@link OctetStringDecoder}
 *
 * @author brightSPARK Labs
 */
public class OctetStringDecoderTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final OctetStringDecoder instance = OctetStringDecoder.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecode() throws Exception
    {
        // test valid
        byte[] bytes = new byte[Byte.MAX_VALUE - Byte.MIN_VALUE];
        for (int i = 0; i < bytes.length; i++)
        {
            bytes[i] = (byte) (i + Byte.MIN_VALUE);
        }
        assertArrayEquals(bytes, instance.decode(bytes));

        // test null
        try
        {
            instance.decode(null);
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
        }

        // test empty
        bytes = new byte[0];
        assertArrayEquals(bytes, instance.decode(bytes));
    }

    @Test
    public void testDecodeAsString() throws Exception
    {
        // test valid
        byte[] bytes = new byte[] { 0x54, 0x45, 0x53, 0x54 };
        assertEquals("0x54455354 (\"TEST\")", instance.decodeAsString(bytes));
        bytes = new byte[] { 0x00, 0x54, 0x45, 0x53, 0x54, 0x00 };
        assertEquals("0x005445535400", instance.decodeAsString(bytes));

        // test null
        try
        {
            instance.decodeAsString(null);
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
        }

        // test empty
        bytes = new byte[0];
        assertEquals("", instance.decodeAsString(bytes));
    }
}
