/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.decoder.builtin;

import static org.junit.Assert.*;

import com.brightsparklabs.asanti.exception.DecodeException;
import com.google.common.base.Charsets;
import org.junit.Test;

/**
 * Units tests for {@link Utf8StringDecoder}
 *
 * @author brightSPARK Labs
 */
public class Utf8StringDecoderTest {
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final Utf8StringDecoder instance = Utf8StringDecoder.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecode() throws Exception {
        // test valid - one byte (all ASCII characters)
        byte[] bytes = new byte[1];
        for (byte b = Byte.MAX_VALUE; b >= 0; b--) {
            bytes[0] = b;
            assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));
        }

        // test valid - two bytes (minimum/maximum)
        bytes = new byte[] {(byte) 0b11000010, (byte) 0b10000000};
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));
        bytes = new byte[] {(byte) 0b11011111, (byte) 0b10111111};
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));

        // test valid - three bytes (minimum/maximum)
        bytes = new byte[] {(byte) 0b11100010, (byte) 0b10000000, (byte) 0b10000000};
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));
        bytes = new byte[] {(byte) 0b11101111, (byte) 0b10111111, (byte) 0b10111111};
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));

        // test valid - four bytes (minimum/maximum)
        bytes =
                new byte[] {
                    (byte) 0b11110010, (byte) 0b10000000, (byte) 0b10000000, (byte) 0b10000000
                };
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));
        bytes =
                new byte[] {
                    (byte) 0b11110000, (byte) 0b10111111, (byte) 0b10111111, (byte) 0b10111111
                };
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decode(bytes));

        // test invalid - single byte
        bytes = new byte[1];
        for (byte b = Byte.MIN_VALUE; b < 0; b++) {
            bytes[0] = b;
            try {
                instance.decode(bytes);
                fail("DecodeExceptions not thrown");
            } catch (DecodeException ex) {
            }
        }

        // test invalid - leading byte requires two trailing bytes
        try {
            bytes[0] = (byte) 0b11000000;
            instance.decode(bytes);
            fail("DecodeExceptions not thrown");
        } catch (DecodeException ex) {
        }

        // test invalid - leading byte requires three trailing bytes
        try {
            bytes[0] = (byte) 0b11100000;
            instance.decode(bytes);
            fail("DecodeExceptions not thrown");
        } catch (DecodeException ex) {
        }

        // test invalid - leading byte requires four trailing bytes
        try {
            bytes[0] = (byte) 0b11110000;
            instance.decode(bytes);
            fail("DecodeExceptions not thrown");
        } catch (DecodeException ex) {
        }

        // test null
        try {
            instance.decode(null);
            fail("DecodeExceptions not thrown");
        } catch (DecodeException ex) {
        }
    }

    @Test
    public void testDecodeAsString() throws Exception {
        // test valid - one byte (all ASCII characters)
        byte[] bytes = new byte[1];
        for (byte b = Byte.MAX_VALUE; b >= 0; b--) {
            bytes[0] = b;
            assertEquals(new String(bytes, Charsets.UTF_8), instance.decodeAsString(bytes));
        }

        // test valid - two bytes (minimum/maximum)
        bytes = new byte[] {(byte) 0b11000010, (byte) 0b10000000};
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decodeAsString(bytes));
        bytes = new byte[] {(byte) 0b11011111, (byte) 0b10111111};
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decodeAsString(bytes));

        // test valid - three bytes (minimum/maximum)
        bytes = new byte[] {(byte) 0b11100010, (byte) 0b10000000, (byte) 0b10000000};
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decodeAsString(bytes));
        bytes = new byte[] {(byte) 0b11101111, (byte) 0b10111111, (byte) 0b10111111};
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decodeAsString(bytes));

        // test valid - four bytes (minimum/maximum)
        bytes =
                new byte[] {
                    (byte) 0b11110010, (byte) 0b10000000, (byte) 0b10000000, (byte) 0b10000000
                };
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decodeAsString(bytes));
        bytes =
                new byte[] {
                    (byte) 0b11110000, (byte) 0b10111111, (byte) 0b10111111, (byte) 0b10111111
                };
        assertEquals(new String(bytes, Charsets.UTF_8), instance.decodeAsString(bytes));

        // test invalid - single byte
        bytes = new byte[1];
        for (byte b = Byte.MIN_VALUE; b < 0; b++) {
            bytes[0] = b;
            try {
                instance.decodeAsString(bytes);
                fail("DecodeExceptions not thrown");
            } catch (DecodeException ex) {
            }
        }

        // test invalid - leading byte requires two trailing bytes
        try {
            bytes[0] = (byte) 0b11000000;
            instance.decodeAsString(bytes);
            fail("DecodeExceptions not thrown");
        } catch (DecodeException ex) {
        }

        // test invalid - leading byte requires three trailing bytes
        try {
            bytes[0] = (byte) 0b11100000;
            instance.decodeAsString(bytes);
            fail("DecodeExceptions not thrown");
        } catch (DecodeException ex) {
        }

        // test invalid - leading byte requires four trailing bytes
        try {
            bytes[0] = (byte) 0b11110000;
            instance.decodeAsString(bytes);
            fail("DecodeExceptions not thrown");
        } catch (DecodeException ex) {
        }

        // test null
        try {
            instance.decodeAsString(null);
            fail("DecodeExceptions not thrown");
        } catch (DecodeException ex) {
        }
    }
}
