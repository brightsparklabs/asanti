/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.bytes;

import com.brightsparklabs.asanti.common.DecodeException;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.validator.ValidationResult;
import com.brightsparklabs.asanti.validator.bytes.AsnByteValidator;

import java.math.BigInteger;
import java.sql.Timestamp;

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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsBitString(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsBmpString(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     * @throws IllegalArgumentException
     *         if the byte array is not have a length of 1
     */
    public static boolean decodeAsBoolean(byte[] bytes) throws DecodeException
    {
        final ValidationResult validationResult = AsnByteValidator.validateAsBoolean(bytes);
        DecodeException.throwIfHasFailures(validationResult);
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsCharacterString(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static Timestamp decodeAsDate(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static Timestamp decodeAsDateTime(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsDuration(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsEmbeddedPDV(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsExternal(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static Timestamp decodeAsGeneralizedTime(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsGeneralString(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsGraphicString(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsIa5String(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsInstanceOf(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static BigInteger decodeAsInteger(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsIri(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsIso646String(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsNull(byte[] bytes) throws DecodeException
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
    public static String decodeAsNumericString(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsObjectClassField(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data. No transformation is done for
     *         bytes in an OCTET STRING. I.e. the bytes are returned as is.
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static byte[] decodeAsOctetString(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsOid(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsOidIri(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsPrefixed(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsPrintableString(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsReal(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsRelativeIri(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsRelativeOid(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsRelativeOidIri(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsTeletexString(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static Timestamp decodeAsTime(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static Timestamp decodeAsTimeOfDay(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsUniversalString(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsUtf8String(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsVideotexString(byte[] bytes) throws DecodeException
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
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsVisibleString(byte[] bytes) throws DecodeException
    {
        return AsnStringByteDecoder.decodeAsVisibleString(bytes);
    }
}
