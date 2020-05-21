/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.decoder.builtin;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.brightsparklabs.asanti.exception.DecodeException;
import com.brightsparklabs.asanti.model.data.AsantiAsnData;
import java.util.Optional;
import org.junit.Test;

/**
 * Units tests for {@link BooleanDecoder}
 *
 * @author brightSPARK Labs
 */
public class BooleanDecoderTest {
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final BooleanDecoder instance = BooleanDecoder.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecode() throws Exception {
        // test valid
        byte[] bytes = new byte[1];
        bytes[0] = 0;
        assertFalse(instance.decode(bytes));
        for (byte b = 1; b < Byte.MAX_VALUE; b++) {
            bytes[0] = b;
            assertTrue(instance.decode(bytes));
        }
        for (byte b = Byte.MIN_VALUE; b < 0; b++) {
            bytes[0] = b;
            assertTrue(instance.decode(bytes));
        }

        // test null
        try {
            instance.decode(null);
            fail("DecodeExceptions not thrown");
        } catch (DecodeException ex) {
        }

        // test invalid
        try {
            bytes = new byte[0];
            instance.decode(bytes);
            fail("DecodeExceptions not thrown");
        } catch (DecodeException ex) {
        }
        try {
            bytes = new byte[2];
            instance.decode(bytes);
            fail("DecodeExceptions not thrown");
        } catch (DecodeException ex) {
        }
        try {
            bytes = new byte[100];
            instance.decode(bytes);
            fail("DecodeExceptions not thrown");
        } catch (DecodeException ex) {
        }

        // test other overload
        AsantiAsnData data = mock(AsantiAsnData.class);
        final String tag = "tag";
        when(data.getBytes(eq(tag))).thenReturn(Optional.of(new byte[] {1}));

        assertTrue(instance.decode(tag, data));
    }

    @Test
    public void testDecodeAsString() throws Exception {
        // test valid
        byte[] bytes = new byte[1];
        bytes[0] = 0;
        assertEquals("false", instance.decodeAsString(bytes));
        for (byte b = 1; b < Byte.MAX_VALUE; b++) {
            bytes[0] = b;
            assertEquals("true", instance.decodeAsString(bytes));
        }
        for (byte b = Byte.MIN_VALUE; b < 0; b++) {
            bytes[0] = b;
            assertEquals("true", instance.decodeAsString(bytes));
        }

        // test null
        try {
            instance.decode(null);
            fail("DecodeExceptions not thrown");
        } catch (DecodeException ex) {
        }

        // test invalid
        try {
            bytes = new byte[0];
            instance.decode(bytes);
            fail("DecodeExceptions not thrown");
        } catch (DecodeException ex) {
        }

        AsantiAsnData data = mock(AsantiAsnData.class);
        final String tag = "tag";
        when(data.getBytes(eq(tag))).thenReturn(Optional.of(new byte[] {1}));

        assertEquals("true", instance.decodeAsString(tag, data));
    }
}
