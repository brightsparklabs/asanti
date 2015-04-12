/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;

import java.math.BigInteger;
import java.sql.Timestamp;

import static com.google.common.base.Preconditions.*;

/**
 * Utility class for decoding bytes in ASN.1 Types
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
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static String decodeAsBitString(byte[] bytes)
    {
        return AsnStringByteDecoder.decodeAsBitString(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#BmpString}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static String decodeAsBmpString(byte[] bytes)
    {
        return AsnStringByteDecoder.decodeAsBmpString(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Boolean}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     * @throws IllegalArgumentException
     *         if the byte array is not have a length of 1
     */
    public static boolean decodeAsBoolean(byte[] bytes)
    {
        checkNotNull(bytes);
        checkArgument(bytes.length == 1, "ASN.1 BOOLEAN type can only be one byte long");
        return bytes[0] != 0;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#CharacterString}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static String decodeAsCharacterString(byte[] bytes)
    {
        return AsnStringByteDecoder.decodeAsCharacterString(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Date}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static Timestamp decodeAsDate(byte[] bytes)
    {
        return AsnTimestampByteDecoder.decodeAsDate(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#DateTime}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static Timestamp decodeAsDateTime(byte[] bytes)
    {
        return AsnTimestampByteDecoder.decodeAsDateTime(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Duration}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
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
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
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
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
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
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static Timestamp decodeAsGeneralizedTime(byte[] bytes)
    {
        return AsnTimestampByteDecoder.decodeAsGeneralizedTime(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#GeneralString}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static String decodeAsGeneralString(byte[] bytes)
    {
        return AsnStringByteDecoder.decodeAsGeneralString(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#GraphicString}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static String decodeAsGraphicString(byte[] bytes)
    {
        return AsnStringByteDecoder.decodeAsGraphicString(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Ia5String}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static String decodeAsIa5String(byte[] bytes)
    {
        return AsnStringByteDecoder.decodeAsIa5String(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#InstanceOf}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
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
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
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
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static String decodeAsIri(byte[] bytes)
    {
        return AsnIdentifierByteDecoder.decodeAsIri(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Iso646String}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static String decodeAsIso646String(byte[] bytes)
    {
        return AsnStringByteDecoder.decodeAsIso646String(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Null}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
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
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static String decodeAsNumericString(byte[] bytes)
    {
        return AsnStringByteDecoder.decodeAsNumericString(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#ObjectClassField}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
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
     *
     * @throws NullPointerException
     *         if parameters are {@code null}. No transformation is done for bytes in an OCTET STRING. I.e. the bytes are returned as
     * is.
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static byte[] decodeAsOctetString(byte[] bytes)
    {
        return AsnStringByteDecoder.decodeAsOctetString(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Oid}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static String decodeAsOid(byte[] bytes)
    {
        return AsnIdentifierByteDecoder.decodeAsOid(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#OidIri}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static String decodeAsOidIri(byte[] bytes)
    {
        return AsnIdentifierByteDecoder.decodeAsOidIri(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Prefixed}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
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
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static String decodeAsPrintableString(byte[] bytes)
    {
        return AsnStringByteDecoder.decodeAsPrintableString(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Real}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
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
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static String decodeAsRelativeIri(byte[] bytes)
    {
        return AsnIdentifierByteDecoder.decodeAsRelativeIri(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#RelativeOid}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static String decodeAsRelativeOid(byte[] bytes)
    {
        return AsnIdentifierByteDecoder.decodeAsRelativeOid(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#RelativeOidIri}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static String decodeAsRelativeOidIri(byte[] bytes)
    {
        return AsnIdentifierByteDecoder.decodeAsRelativeOidIri(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#TeletexString}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static String decodeAsTeletexString(byte[] bytes)
    {
        return AsnStringByteDecoder.decodeAsTeletexString(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Time}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static Timestamp decodeAsTime(byte[] bytes)
    {
        return AsnTimestampByteDecoder.decodeAsTime(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#TimeOfDay}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static Timestamp decodeAsTimeOfDay(byte[] bytes)
    {
        return AsnTimestampByteDecoder.decodeAsTimeOfDay(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#UniversalString}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static String decodeAsUniversalString(byte[] bytes)
    {
        return AsnStringByteDecoder.decodeAsUniversalString(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Utf8String}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static String decodeAsUtf8String(byte[] bytes)
    {
        return AsnStringByteDecoder.decodeAsUtf8String(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#VideotexString}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static String decodeAsVideotexString(byte[] bytes)
    {
        return AsnStringByteDecoder.decodeAsVideotexString(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#VisibleString}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static String decodeAsVisibleString(byte[] bytes)
    {
        return AsnStringByteDecoder.decodeAsVisibleString(bytes);
    }
}
