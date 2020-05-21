/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.decoder.builtin;

import static org.junit.Assert.*;

import com.brightsparklabs.asanti.exception.DecodeException;
import org.junit.Test;

/**
 * Units tests for {@link DateTimeDecoder}
 *
 * @author brightSPARK Labs
 */
public class DateTimeDecoderTest {
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final DateTimeDecoder instance = DateTimeDecoder.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecode() throws Exception {
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
        try {
            instance.decode(null);
            fail("DecodeExceptions not thrown");
        } catch (DecodeException ex) {
        }
    }

    @Test
    public void testDecodeAsString() throws Exception {
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
        try {
            instance.decodeAsString(null);
            fail("DecodeExceptions not thrown");
        } catch (DecodeException ex) {
        }
    }
}
