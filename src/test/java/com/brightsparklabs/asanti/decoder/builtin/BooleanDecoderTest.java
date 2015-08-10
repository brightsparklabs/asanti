/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.DecodeException;
import com.brightsparklabs.asanti.model.data.AsnData;
import com.google.common.base.Optional;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Units tests for {@link BooleanDecoder}
 *
 * @author brightSPARK Labs
 */
public class BooleanDecoderTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final BooleanDecoder instance = BooleanDecoder.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecode() throws Exception
    {
        // test valid
        byte[] bytes = new byte[1];
        bytes[0] = 0;
        assertFalse(instance.decode(bytes));
        for (byte b = 1; b < Byte.MAX_VALUE; b++)
        {
            bytes[0] = b;
            assertTrue(instance.decode(bytes));
        }
        for (byte b = Byte.MIN_VALUE; b < 0; b++)
        {
            bytes[0] = b;
            assertTrue(instance.decode(bytes));
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

        // test invalid
        try
        {
            bytes = new byte[0];
            instance.decode(bytes);
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
        }
        try
        {
            bytes = new byte[2];
            instance.decode(bytes);
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
        }
        try
        {
            bytes = new byte[100];
            instance.decode(bytes);
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
        }

        // test other overload
        AsnData data = mock(AsnData.class);
        final String tag = "tag";
        when(data.getBytes(eq(tag))).thenReturn(Optional.of(new byte [] { 1 }));

        assertTrue(instance.decode(tag, data));
    }

    @Test
    public void testDecodeAsString() throws Exception
    {
        // test valid
        byte[] bytes = new byte[1];
        bytes[0] = 0;
        assertEquals("false", instance.decodeAsString(bytes));
        for (byte b = 1; b < Byte.MAX_VALUE; b++)
        {
            bytes[0] = b;
            assertEquals("true", instance.decodeAsString(bytes));
        }
        for (byte b = Byte.MIN_VALUE; b < 0; b++)
        {
            bytes[0] = b;
            assertEquals("true", instance.decodeAsString(bytes));
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

        // test invalid
        try
        {
            bytes = new byte[0];
            instance.decode(bytes);
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
        }


        AsnData data = mock(AsnData.class);
        final String tag = "tag";
        when(data.getBytes(eq(tag))).thenReturn(Optional.of(new byte [] { 1 }));

        assertEquals("true", instance.decodeAsString(tag, data));
    }
}
