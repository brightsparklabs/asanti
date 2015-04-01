/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;

import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * Utility class for decoding bytes from ASN.1 String Types.
 * <p/>
 * This class was created to prevent {@link AsnByteDecoder} from containing too much logic. The methods in here are
 * designed to be called by {@link AsnByteDecoder} (hence why they use package visibility). Do not call the methods in
 * this class directly. Use {@link AsnByteDecoder} instead.
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
     */
    static String decodeAsBitString(byte[] bytes)
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
     */
    static String decodeAsBmpString(byte[] bytes)
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
     */
    static String decodeAsCharacterString(byte[] bytes)
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
     */
    static String decodeAsGeneralString(byte[] bytes)
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
     */
    static String decodeAsGraphicString(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Ia5String}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    static String decodeAsIa5String(byte[] bytes)
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
     */
    static String decodeAsIso646String(byte[] bytes)
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
     */
    static String decodeAsNumericString(byte[] bytes)
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
     * @return the decoded bytes
     */
    static String decodeAsOctetString(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#PrintableString}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    static String decodeAsPrintableString(byte[] bytes)
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
     */
    static String decodeAsTeletexString(byte[] bytes)
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
     */
    static String decodeAsUniversalString(byte[] bytes)
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
     */
    static String decodeAsUtf8String(byte[] bytes)
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
     */
    static String decodeAsVideotexString(byte[] bytes)
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
     */
    static String decodeAsVisibleString(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }
}
