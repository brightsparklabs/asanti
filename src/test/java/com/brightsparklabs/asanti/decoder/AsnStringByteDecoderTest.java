/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Units tests for {@link AsnStringByteDecoder}
 *
 * @author brightSPARK Labs
 */
public class AsnStringByteDecoderTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecodeAsBitString() throws Exception
    {
        // TODO: ASN-8
    }

    @Test
    public void testDecodeAsBmpString() throws Exception
    {
        // TODO: ASN-8
    }

    @Test
    public void testDecodeAsCharacterString() throws Exception
    {
        // TODO: ASN-8
    }

    @Test
    public void testDecodeAsGeneralString() throws Exception
    {
        // TODO: ASN-8
    }

    @Test
    public void testDecodeAsGraphicString() throws Exception
    {
        // TODO: ASN-8
    }

    @Test
    public void testDecodeAsIa5String() throws Exception
    {
        // TODO: ASN-8
    }

    @Test
    public void testDecodeAsIso646String() throws Exception
    {
        // TODO: ASN-8
    }

    @Test
    public void testDecodeAsNumericString() throws Exception
    {
        // TODO: ASN-8
    }

    @Test
    public void testDecodeAsOctetString() throws Exception
    {
        // test valid
        byte[] bytes = new byte[0];
        assertArrayEquals(bytes, AsnStringByteDecoder.decodeAsOctetString(bytes));
        bytes = new byte[] { Byte.MIN_VALUE, -1, 0, 1, Byte.MAX_VALUE };
        assertArrayEquals(bytes, AsnStringByteDecoder.decodeAsOctetString(bytes));

        // test null
        try
        {
            AsnByteDecoder.decodeAsOctetString(null);
            fail("NullPointerException not thrown");
        }
        catch (NullPointerException ex)
        {
        }
    }

    @Test
    public void testDecodeAsPrintableString() throws Exception
    {
        // TODO: ASN-8
    }

    @Test
    public void testDecodeAsTeletexString() throws Exception
    {
        // TODO: ASN-8
    }

    @Test
    public void testDecodeAsUniversalString() throws Exception
    {
        // TODO: ASN-8
    }

    @Test
    public void testDecodeAsUtf8String() throws Exception
    {
        // TODO: ASN-8
    }

    @Test
    public void testDecodeAsVideotexString() throws Exception
    {
        // TODO: ASN-8
    }

    @Test
    public void testDecodeAsVisibleString() throws Exception
    {
        // TODO: ASN-8
    }
}
