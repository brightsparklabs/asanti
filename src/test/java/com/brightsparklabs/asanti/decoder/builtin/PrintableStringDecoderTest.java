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
 * Units tests for {@link PrintableStringDecoder}
 *
 * @author brightSPARK Labs
 */
public class PrintableStringDecoderTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final PrintableStringDecoder instance = PrintableStringDecoder.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecode() throws Exception
    {
        // test valid
        byte[] bytes = new byte[1];
        for (int b = 'Z'; b >= 'A'; b--)
        {
            bytes[0] = (byte) b;
            assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));
        }
        for (int b = 'z'; b >= 'a'; b--)
        {
            bytes[0] = (byte) b;
            assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));
        }
        for (int b = '9'; b >= '0'; b--)
        {
            bytes[0] = (byte) b;
            assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));
        }
        bytes[0] = (byte) ' ';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));

        bytes[0] = (byte) '\'';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));

        bytes[0] = (byte) '(';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));

        bytes[0] = (byte) ')';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));

        bytes[0] = (byte) '+';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));

        bytes[0] = (byte) ',';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));

        bytes[0] = (byte) '-';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));

        bytes[0] = (byte) '.';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));

        bytes[0] = (byte) '/';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));

        bytes[0] = (byte) ':';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));

        bytes[0] = (byte) '=';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));

        bytes[0] = (byte) '?';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));

        for (byte b = Byte.MIN_VALUE; b < 32; b++)
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

        {
            bytes[0] = Byte.MAX_VALUE;
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
        byte[] bytes = "TEST1234+-:= ()/".getBytes(Charsets.UTF_8);
        assertEquals("TEST1234+-:= ()/", instance.decodeAsString(bytes));

        // test invalid
        bytes = "TEST1234+-:= ()/^".getBytes(Charsets.UTF_8);
        try
        {
            instance.decodeAsString(bytes);
            fail("DecodeExceptions not thrown");
        }
        catch (DecodeException ex)
        {
        }

        // test valid
        bytes = new byte[1];
        for (int b = 'Z'; b >= 'A'; b--)
        {
            bytes[0] = (byte) b;
            assertEquals(new String(bytes, Charsets.UTF_8), instance.decodeAsString(bytes));
        }
        for (int b = 'z'; b >= 'a'; b--)
        {
            bytes[0] = (byte) b;
            assertEquals(new String(bytes, Charsets.UTF_8), instance.decodeAsString(bytes));
        }
        for (int b = '9'; b >= '0'; b--)
        {
            bytes[0] = (byte) b;
            assertEquals(new String(bytes, Charsets.UTF_8), instance.decodeAsString(bytes));
        }
        bytes[0] = (byte) ' ';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decodeAsString(bytes));

        bytes[0] = (byte) '\'';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decodeAsString(bytes));

        bytes[0] = (byte) '(';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decodeAsString(bytes));

        bytes[0] = (byte) ')';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decodeAsString(bytes));

        bytes[0] = (byte) '+';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decodeAsString(bytes));

        bytes[0] = (byte) ',';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decodeAsString(bytes));

        bytes[0] = (byte) '-';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decodeAsString(bytes));

        bytes[0] = (byte) '.';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decodeAsString(bytes));

        bytes[0] = (byte) '/';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decodeAsString(bytes));

        bytes[0] = (byte) ':';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decodeAsString(bytes));

        bytes[0] = (byte) '=';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));

        bytes[0] = (byte) '?';
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));

        for (byte b = Byte.MIN_VALUE; b < 32; b++)
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

        {
            bytes[0] = Byte.MAX_VALUE;
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
            instance.decodeAsString(null);
            fail("DecodeExceptions not thrown");
        }
        catch (DecodeException ex)
        {
        }
    }
}
