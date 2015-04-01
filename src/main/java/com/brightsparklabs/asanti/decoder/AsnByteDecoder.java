/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;

import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * Utility class for decoding bytes in an ASN.1 Type
 *
 * @author brightSPARK Labs
 */
public class AsnByteDecoder
{
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     */
    private AsnByteDecoder()
    {
        // constructor should never be called for static class
        assert false;
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
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
    public static String decodeAsBitString(byte[] bytes)
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
    public static String decodeAsBmpString(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Boolean}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    public static boolean decodeAsBoolean(byte[] bytes)
    {
        // TODO: ASN-8
        return false;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#CharacterString}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    public static String decodeAsCharacterString(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Date}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    public static Timestamp decodeAsDate(byte[] bytes)
    {
        // TODO: ASN-8
        return new Timestamp(0);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#DateTime}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    public static Timestamp decodeAsDateTime(byte[] bytes)
    {
        // TODO: ASN-8
        return new Timestamp(0);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Duration}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    public static String decodeAsDuration(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#EmbeddedPDV}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    public static String decodeAsEmbeddedPDV(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#External}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    public static String decodeAsExternal(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#GeneralizedTime}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    public static Timestamp decodeAsGeneralizedTime(byte[] bytes)
    {
        // TODO: ASN-8
        return new Timestamp(0);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#GeneralString}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    public static String decodeAsGeneralString(byte[] bytes)
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
    public static String decodeAsGraphicString(byte[] bytes)
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
    public static String decodeAsIa5String(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#InstanceOf}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    public static String decodeAsInstanceOf(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Integer}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    public static BigInteger decodeAsInteger(byte[] bytes)
    {
        // TODO: ASN-8
        return BigInteger.ZERO;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#IRI}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    public static String decodeAsIri(byte[] bytes)
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
    public static String decodeAsIso646String(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Null}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    public static String decodeAsNull(byte[] bytes)
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
    public static String decodeAsNumericString(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#ObjectClassField}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    public static String decodeAsObjectClassField(byte[] bytes)
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
    public static String decodeAsOctetString(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Oid}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    public static String decodeAsOid(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#OidIri}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    public static String decodeAsOidIri(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Prefixed}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    public static String decodeAsPrefixed(byte[] bytes)
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
    public static String decodeAsPrintableString(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Real}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    public static String decodeAsReal(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#RelativeIri}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    public static String decodeAsRelativeIri(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#RelativeOid}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    public static String decodeAsRelativeOid(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }


    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#RelativeOidIri}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    public static String decodeAsRelativeOidIri(byte[] bytes)
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
    public static String decodeAsTeletexString(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Time}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    public static Timestamp decodeAsTime(byte[] bytes)
    {
        // TODO: ASN-8
        return new Timestamp(0);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#TimeOfDay}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    public static Timestamp decodeAsTimeOfDay(byte[] bytes)
    {
        // TODO: ASN-8
        return new Timestamp(0);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#UniversalString}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     */
    public static String decodeAsUniversalString(byte[] bytes)
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
    public static String decodeAsUtf8String(byte[] bytes)
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
    public static String decodeAsVideotexString(byte[] bytes)
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
    public static String decodeAsVisibleString(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }
}
