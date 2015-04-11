/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Units tests for {@link AsnByteDecoder}.
 * <p/>
 * Since {@link AsnByteDecoder} delegates to a number of other classes, not all methods are tested directly here. Refer
 * to the following test classes to see the delegated tests: {@link AsnStringByteDecoderTest}, {@link
 * AsnIdentifierByteDecoder}, {@link AsnTimestampByteDecoderTest}.
 *
 * @author brightSPARK Labs
 */
public class AsnByteDecoderTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecodeAsBoolean() throws Exception
    {
        // test valid
        byte[] bytes = new byte[1];
        bytes[0] = 0;
        assertFalse(AsnByteDecoder.decodeAsBoolean(bytes));
        for (byte b = 1; b < Byte.MAX_VALUE; b++)
        {
            bytes[0] = b;
            assertTrue(AsnByteDecoder.decodeAsBoolean(bytes));
        }
        for (byte b = Byte.MIN_VALUE; b < 0; b++)
        {
            bytes[0] = b;
            assertTrue(AsnByteDecoder.decodeAsBoolean(bytes));
        }

        // test null
        try
        {
            AsnByteDecoder.decodeAsBoolean(null);
            fail("NullPointerException not thrown");
        }
        catch (NullPointerException ex)
        {
        }

        // test invalid
        try
        {
            bytes = new byte[0];
            AsnByteDecoder.decodeAsBoolean(bytes);
            fail("IllegalArgumentException not thrown");
        }
        catch (IllegalArgumentException ex)
        {
        }
        try
        {
            bytes = new byte[2];
            AsnByteDecoder.decodeAsBoolean(bytes);
            fail("IllegalArgumentException not thrown");
        }
        catch (IllegalArgumentException ex)
        {
        }
        try
        {
            bytes = new byte[100];
            AsnByteDecoder.decodeAsBoolean(bytes);
            fail("IllegalArgumentException not thrown");
        }
        catch (IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testDecodeAsDuration() throws Exception
    {
        // TODO: ASN-8
    }

    @Test
    public void testDecodeAsEmbeddedPDV() throws Exception
    {
        // TODO: ASN-8
    }

    @Test
    public void testDecodeAsExternal() throws Exception
    {
        // TODO: ASN-8
    }

    @Test
    public void testDecodeAsInstanceOf() throws Exception
    {
        // TODO: ASN-8
    }

    @Test
    public void testDecodeAsInteger() throws Exception
    {
        // TODO: ASN-8
    }

    @Test
    public void testDecodeAsNull() throws Exception
    {
        // TODO: ASN-8
    }

    @Test
    public void testDecodeAsObjectClassField() throws Exception
    {
        // TODO: ASN-8
    }

    @Test
    public void testDecodeAsPrefixed() throws Exception
    {
        // TODO: ASN-8
    }

    @Test
    public void testDecodeAsReal() throws Exception
    {
        // TODO: ASN-8
    }
}
