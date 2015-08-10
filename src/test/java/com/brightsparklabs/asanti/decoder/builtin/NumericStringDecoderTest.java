/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.assam.exception.DecodeException;
import com.google.common.base.Charsets;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Units tests for {@link NumericStringDecoder}
 *
 * @author brightSPARK Labs
 */
public class NumericStringDecoderTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final NumericStringDecoder instance = NumericStringDecoder.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecode() throws Exception
    {
        // test valid
        byte[] bytes = new byte[1];
        for (int b = '9'; b >= '0'; b--)
        {
            bytes[0] = (byte) b;
            assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));
        }

        // test space
        bytes[0] = ' ';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));

        // test a "phone number"
        bytes = "0400 123 456".getBytes(Charsets.UTF_8);
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));


        // test invalid
        for (byte b = Byte.MIN_VALUE; b < ' '; b++)
        {
            bytes[0] = b;
            try
            {
                instance.decode(bytes);
                fail("DecodeExceptions not thrown");
            }
            catch (DecodeException ex)
            {
            }
        }

        for (byte b = '!'; b < '0'; b++)
        {
            bytes[0] = b;
            try
            {
                instance.decode(bytes);
                fail("DecodeExceptions not thrown");
            }
            catch (DecodeException ex)
            {
            }
        }

        for (byte b = Byte.MAX_VALUE; b > '9'; b--)
        {
            bytes[0] = b;
            try
            {
                instance.decode(bytes);
                fail("DecodeExceptions not thrown");
            }
            catch (DecodeException ex)
            {
            }
        }

        // test null
        try
        {
            instance.decode(null);
            fail("DecodeExceptions not thrown");
        }
        catch (DecodeException ex)
        {
        }
    }

    @Test
    public void testDecodeAsString() throws Exception
    {
        // test valid
        byte[] bytes = "0123456789".getBytes(Charsets.UTF_8);
        assertEquals("0123456789", instance.decodeAsString(bytes));


        bytes = " 0123456789 ".getBytes(Charsets.UTF_8);
        assertEquals(" 0123456789 ", instance.decodeAsString(bytes));

        bytes = "0 1 2 3 4 5 6 7 8 9".getBytes(Charsets.UTF_8);
        assertEquals("0 1 2 3 4 5 6 7 8 9", instance.decodeAsString(bytes));

        bytes = "     ".getBytes(Charsets.UTF_8);
        assertEquals("     ", instance.decodeAsString(bytes));


        // test invalid
        bytes = "0123456789z".getBytes(Charsets.UTF_8);
        try
        {
            instance.decodeAsString(bytes);
            fail("DecodeExceptions not thrown");
        }
        catch (DecodeException ex)
        {
        }

        for (byte b = Byte.MIN_VALUE; b < ' '; b++)
        {
            bytes[0] = b;
            try
            {
                instance.decodeAsString(bytes);
                fail("DecodeExceptions not thrown");
            }
            catch (DecodeException ex)
            {
            }
        }

        for (byte b = '!'; b < '0'; b++)
        {
            bytes[0] = b;
            try
            {
                instance.decodeAsString(bytes);
                fail("DecodeExceptions not thrown");
            }
            catch (DecodeException ex)
            {
            }
        }

        for (byte b = Byte.MAX_VALUE; b > '9'; b--)
        {
            bytes[0] = b;
            try
            {
                instance.decodeAsString(bytes);
                fail("DecodeExceptions not thrown");
            }
            catch (DecodeException ex)
            {
            }
        }

        // test null
        try
        {
            instance.decodeAsString(null);
            fail("DecodeExceptions not thrown");
        }
        catch (DecodeException ex)
        {
        }
    }
}
