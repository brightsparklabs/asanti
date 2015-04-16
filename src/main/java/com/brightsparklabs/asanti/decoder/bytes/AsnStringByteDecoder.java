/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.bytes;

import com.brightsparklabs.asanti.common.DecodeException;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;

import static com.google.common.base.Preconditions.*;

/**
 * Utility class for decoding bytes from ASN.1 String Types.
 *
 * <p>This class was created to prevent {@link AsnByteDecoder} from containing too much logic. The
 * methods in here are designed to be called by {@link AsnByteDecoder} (hence why they use package
 * visibility). Do not call the methods in this class directly. Use {@link AsnByteDecoder} instead.
 *
 * @author brightSPARK Labs
 */
class AsnStringByteDecoder
{
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     */
    private AsnStringByteDecoder()
    {
        // constructor should never be called for static class
        assert false;
    }

    // -------------------------------------------------------------------------
    //  METHODS
    // -------------------------------------------------------------------------

    /*
     * NOTE: Please keep these methods ordered alphabetically for simplicity
     */

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#BitString}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    static String decodeAsBitString(byte[] bytes) throws DecodeException
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#BmpString}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    static String decodeAsBmpString(byte[] bytes) throws DecodeException
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#CharacterString}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    static String decodeAsCharacterString(byte[] bytes) throws DecodeException
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#GeneralString}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    static String decodeAsGeneralString(byte[] bytes) throws DecodeException
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#GraphicString}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    static String decodeAsGraphicString(byte[] bytes) throws DecodeException
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Iso646String}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    static String decodeAsIso646String(byte[] bytes) throws DecodeException
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#NumericString}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    static String decodeAsNumericString(byte[] bytes) throws DecodeException
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#OctetString}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes. No transformation is done for bytes in an OCTET STRING. I.e. the
     * bytes are returned as is.
     *
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    static byte[] decodeAsOctetString(byte[] bytes) throws DecodeException
    {
        checkNotNull(bytes);
        // no transformation needed for OCTET STRING. Returns raw bytes
        return bytes;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#PrintableString}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */

    static String decodeAsPrintableString(byte[] bytes) throws DecodeException
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#TeletexString}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    static String decodeAsTeletexString(byte[] bytes) throws DecodeException
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#UniversalString}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    static String decodeAsUniversalString(byte[] bytes) throws DecodeException
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Utf8String}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    static String decodeAsUtf8String(byte[] bytes) throws DecodeException
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#VideotexString}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    static String decodeAsVideotexString(byte[] bytes) throws DecodeException
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#VisibleString}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    static String decodeAsVisibleString(byte[] bytes) throws DecodeException
    {
        // TODO: ASN-8
        return "";
    }
}
