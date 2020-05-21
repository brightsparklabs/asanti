/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.decoder.builtin;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import com.brightsparklabs.asanti.exception.DecodeException;
import com.brightsparklabs.asanti.model.data.AsantiAsnData;
import com.google.common.base.Charsets;
import java.util.Optional;
import org.junit.Test;

/**
 * Units tests for {@link Ia5StringDecoder}
 *
 * @author brightSPARK Labs
 */
public class Ia5StringDecoderTest {
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final Ia5StringDecoder instance = Ia5StringDecoder.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecode() throws Exception {
        // test valid
        byte[] bytes = new byte[1];
        for (byte b = Byte.MAX_VALUE; b >= 0; b--) {
            bytes[0] = b;
            final String expected = new String(bytes, Charsets.UTF_8);
            assertEquals(expected, instance.decode(bytes));

            // test other overload
            AsantiAsnData data = mock(AsantiAsnData.class);
            final String tag = "tag";
            when(data.getBytes(eq(tag))).thenReturn(Optional.of(bytes));

            assertEquals(expected, instance.decode(bytes));
        }

        // test invalid
        for (byte b = Byte.MIN_VALUE; b < 0; b++) {
            bytes[0] = b;
            try {
                instance.decode(bytes);
                fail("DecodeExceptions not thrown");
            } catch (DecodeException ex) {
            }
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
        // test valid
        byte[] bytes = "TEST".getBytes(Charsets.UTF_8);
        assertEquals("TEST", instance.decodeAsString(bytes));

        // test other overload
        AsantiAsnData data = mock(AsantiAsnData.class);
        final String tag = "tag";
        when(data.getBytes(eq(tag))).thenReturn(Optional.of(bytes));
        assertEquals("TEST", instance.decodeAsString(bytes));

        // test invalid
        for (byte b = Byte.MIN_VALUE; b < 0; b++) {
            bytes[0] = b;
            try {
                instance.decodeAsString(bytes);
                fail("DecodeExceptions not thrown");
            } catch (DecodeException ex) {
            }
        }

        // test null
        try {
            instance.decodeAsString(null);
            fail("DecodeExceptions not thrown");
        } catch (DecodeException ex) {
        }
    }
}
