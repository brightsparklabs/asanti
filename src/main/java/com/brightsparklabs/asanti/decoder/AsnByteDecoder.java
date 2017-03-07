/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder;

import com.brightsparklabs.asanti.decoder.builtin.*;
import com.brightsparklabs.assam.exception.DecodeException;
import com.brightsparklabs.assam.schema.AsnBuiltinType;

import java.math.BigInteger;
import java.time.OffsetDateTime;

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
        return BitStringDecoder.getInstance().decode(bytes);
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
        return BmpStringDecoder.getInstance().decode(bytes);
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
     */
    public static boolean decodeAsBoolean(byte[] bytes) throws DecodeException
    {
        return BooleanDecoder.getInstance().decode(bytes);
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
        return CharacterStringDecoder.getInstance().decode(bytes);
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
    public static OffsetDateTime decodeAsDate(byte[] bytes) throws DecodeException
    {
        return DateDecoder.getInstance().decode(bytes);
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
    public static OffsetDateTime decodeAsDateTime(byte[] bytes) throws DecodeException
    {
        return DateTimeDecoder.getInstance().decode(bytes);
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
        return DurationDecoder.getInstance().decode(bytes);
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
        return EmbeddedPDVDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Enumerated}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static String decodeAsEnumerated(byte[] bytes) throws DecodeException
    {
        return EnumeratedDecoder.getInstance().decode(bytes);
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
        return ExternalDecoder.getInstance().decode(bytes);
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
    public static OffsetDateTime decodeAsGeneralizedTime(byte[] bytes) throws DecodeException
    {
        return GeneralizedTimeDecoder.getInstance().decode(bytes);
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
        return GeneralStringDecoder.getInstance().decode(bytes);
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
        return GraphicStringDecoder.getInstance().decode(bytes);
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
        return Ia5StringDecoder.getInstance().decode(bytes);
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
        return InstanceOfDecoder.getInstance().decode(bytes);
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
        return IntegerDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Iri}
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
        return IriDecoder.getInstance().decode(bytes);
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
        return Iso646StringDecoder.getInstance().decode(bytes);
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
        return NullDecoder.getInstance().decode(bytes);
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
        return NumericStringDecoder.getInstance().decode(bytes);
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
        return ObjectClassFieldDecoder.getInstance().decode(bytes);
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
        return OctetStringDecoder.getInstance().decode(bytes);
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
        return OidDecoder.getInstance().decode(bytes);
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
        return OidIriDecoder.getInstance().decode(bytes);
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
        return PrefixedDecoder.getInstance().decode(bytes);
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
        return PrintableStringDecoder.getInstance().decode(bytes);
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
        return RealDecoder.getInstance().decode(bytes);
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
        return RelativeIriDecoder.getInstance().decode(bytes);
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
        return RelativeOidDecoder.getInstance().decode(bytes);
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
        return RelativeOidIriDecoder.getInstance().decode(bytes);
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
        return TeletexStringDecoder.getInstance().decode(bytes);
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
    public static OffsetDateTime decodeAsTime(byte[] bytes) throws DecodeException
    {
        return TimeDecoder.getInstance().decode(bytes);
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
    public static OffsetDateTime decodeAsTimeOfDay(byte[] bytes) throws DecodeException
    {
        return TimeOfDayDecoder.getInstance().decode(bytes);
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
        return UniversalStringDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#UtcTime}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws DecodeException
     *         if any errors occur while decoding the supplied data
     */
    public static OffsetDateTime decodeAsUtcTime(byte[] bytes) throws DecodeException
    {
        return UtcTimeDecoder.getInstance().decode(bytes);
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
        return Utf8StringDecoder.getInstance().decode(bytes);
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
        return VideotexStringDecoder.getInstance().decode(bytes);
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
        return VisibleStringDecoder.getInstance().decode(bytes);
    }
}
