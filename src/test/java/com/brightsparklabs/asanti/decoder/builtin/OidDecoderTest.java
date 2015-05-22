/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.DecodeException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Units tests for {@link OidDecoder}
 *
 * @author brightSPARK Labs
 */
public class OidDecoderTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final OidDecoder instance = OidDecoder.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecode() throws Exception
    {
        // test valid single octet (minimum value)
        // OID: 0.0
        byte[] bytes = new byte[] { (byte) 0x00 };
        assertEquals("0.0", instance.decode(bytes));

        // test valid single octet (maximum value)
        // OID: 3.7
        bytes = new byte[] { (byte) 0x7F };
        assertEquals("3.7", instance.decode(bytes));

        // test valid multiple octets
        // OID: 1.3.16383
        bytes = new byte[] { (byte) 0x2B, (byte) 0xFF, (byte) 0x7F };
        assertEquals("1.3.16383", instance.decode(bytes));

        // test valid multiple octets
        // OID: 1.3.2097152.16
        bytes = new byte[] { (byte) 0x2B, (byte) 0x81, (byte) 0x80, (byte) 0x80, (byte) 0x00,
                             (byte) 0x10 };
        assertEquals("1.3.2097152.16", instance.decode(bytes));

        // test valid maximum Sub ID (268,435,455)
        // OID: 1.3.0.268435455.127
        bytes = new byte[] { (byte) 0x2B, (byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                             (byte) 0x7F, (byte) 0x7F };
        assertEquals("1.3.0.268435455.127", instance.decode(bytes));

        // 1.3.6.1.4.1.311.21.20
        bytes = new byte[] { (byte) 0x2B, (byte) 0x06, (byte) 0x01, (byte) 0x04, (byte) 0x01,
                             (byte) 0x82, (byte) 0x37, (byte) 0x15, (byte) 0x14 };
        assertEquals("1.3.6.1.4.1.311.21.20", instance.decode(bytes));

        // test invalid single octet
        try
        {
            bytes = new byte[] { (byte) 0x80 };
            instance.decode(bytes);
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
        }

        // test invalid multiple octets
        // OID is incomplete
        try
        {
            bytes = new byte[] { (byte) 0x2B, (byte) 0xFF, (byte) 0xFF };
            instance.decode(bytes);
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
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
    }

    @Test
    public void testDecodeAsString() throws Exception
    {
        // test valid single octet (minimum value)
        // OID: 0.0
        byte[] bytes = new byte[] { (byte) 0x00 };
        assertEquals("0.0", instance.decodeAsString(bytes));

        // test valid single octet (maximum value)
        // OID: 3.7
        bytes = new byte[] { (byte) 0x7F };
        assertEquals("3.7", instance.decodeAsString(bytes));

        // test valid multiple octets
        // OID: 1.3.16383
        bytes = new byte[] { (byte) 0x2B, (byte) 0xFF, (byte) 0x7F };
        assertEquals("1.3.16383", instance.decodeAsString(bytes));

        // test valid multiple octets
        // OID: 1.3.2097152.16
        bytes = new byte[] { (byte) 0x2B, (byte) 0x81, (byte) 0x80, (byte) 0x80, (byte) 0x00,
                             (byte) 0x10 };
        assertEquals("1.3.2097152.16", instance.decodeAsString(bytes));

        // test valid maximum Sub ID (268,435,455)
        // OID: 1.3.0.268435455.127
        bytes = new byte[] { (byte) 0x2B, (byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                             (byte) 0x7F, (byte) 0x7F };
        assertEquals("1.3.0.268435455.127", instance.decodeAsString(bytes));

        // 1.3.6.1.4.1.311.21.20
        bytes = new byte[] { (byte) 0x2B, (byte) 0x06, (byte) 0x01, (byte) 0x04, (byte) 0x01,
                             (byte) 0x82, (byte) 0x37, (byte) 0x15, (byte) 0x14 };
        assertEquals("1.3.6.1.4.1.311.21.20", instance.decodeAsString(bytes));

        // test invalid single octet
        try
        {
            bytes = new byte[] { (byte) 0x80 };
            instance.decodeAsString(bytes);
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
        }

        // test invalid multiple octets
        // OID is incomplete
        try
        {
            bytes = new byte[] { (byte) 0x2B, (byte) 0xFF, (byte) 0xFF };
            instance.decodeAsString(bytes);
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
        }

        // test null
        try
        {
            instance.decodeAsString(null);
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
        }
    }
}
