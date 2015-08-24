/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.assam.exception.DecodeException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Units tests for {@link DateDecoder}
 *
 * @author brightSPARK Labs
 */
public class DateDecoderTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final DateDecoder instance = DateDecoder.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecode() throws Exception
    {
        // TODO: ASN-107 implement
        /*
        // test valid
        byte[] bytes = new byte[1];
        for (byte b = Byte.MAX_VALUE; b >= 0; b--)
        {
            bytes[0] = b;
            assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));
        }

        // test invalid
        for (byte b = Byte.MIN_VALUE; b < 0; b++)
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
        */

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
        // TODO: ASN-107 implement
        /*
        // test valid
        byte[] bytes = "TEST".getBytes(Charsets.UTF_8);
        assertEquals("TEST", instance.decodeAsString(bytes));

        // test invalid
        for (byte b = Byte.MIN_VALUE; b < 0; b++)
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
        */

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
