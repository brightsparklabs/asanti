/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.decoder.builtin;

import static org.junit.Assert.*;

import com.brightsparklabs.assam.exception.DecodeException;
import org.junit.Test;

/**
 * Units tests for {@link NullDecoder}
 *
 * @author brightSPARK Labs
 */
public class NullDecoderTest {
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final NullDecoder instance = NullDecoder.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecode() throws Exception {
        // test valid bytes
        byte[] bytes = new byte[0];
        assertEquals("", instance.decode(bytes));

        // test invalid bytes
        try {
            bytes = new byte[] {0x00};
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
        // test valid bytes
        byte[] bytes = new byte[0];
        assertEquals("", instance.decodeAsString(bytes));

        // test invalid bytes
        try {
            bytes = new byte[] {0x00};
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
