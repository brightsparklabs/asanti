/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.DecodeException;
import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.google.common.base.Optional;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

    @Test
    public void testDecodeAsStringOverload() throws Exception
    {
        DecodedAsnData data = mock(DecodedAsnData.class);
        when(data.getBytes(anyString())).thenReturn(Optional.<byte[]>absent());

        final String tagIsAscii = "IsAscii";
        final byte[] bytesIsAscii = new byte[] { 0x54, 0x45, 0x53, 0x54 };
        when(data.getBytes(eq(tagIsAscii))).thenReturn(Optional.of(bytesIsAscii));
        final String tagNotAscii = "NotAscii";
        final byte[] bytesNotAscii = new byte[] { 0x00, 0x54, 0x45, 0x53, 0x54, 0x00 };
        when(data.getBytes(eq(tagNotAscii))).thenReturn(Optional.of(bytesNotAscii));
        final String tagEmpty = "Empty";
        final byte[] bytesEmpty = new byte[0];
        when(data.getBytes(eq(tagEmpty))).thenReturn(Optional.of(bytesEmpty));

        // test valid
        assertEquals("0x54455354 (\"TEST\")", instance.decodeAsString(tagIsAscii, data));
        assertEquals("0x005445535400", instance.decodeAsString(tagNotAscii, data));

        assertEquals("", instance.decodeAsString(tagEmpty, data));
        // test null
        try
        {
            instance.decodeAsString(null, data);
            fail("NullPointerException not thrown");
        }
        catch (NullPointerException ex)
        {
        }
        try
        {
            instance.decodeAsString("someTag", null);
            fail("NullPointerException not thrown");
        }
        catch (NullPointerException ex)
        {
        }

        try
        {
            // this will produce a null for getBytes
            assertEquals("", instance.decodeAsString("badTag", data));
            fail("DecodeException not thrown");
        }
        catch (DecodeException e)
        {
        }
    }
}
