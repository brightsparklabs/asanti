/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.decoder;

import com.brightsparklabs.asanti.decoder.builtin.*;
import com.brightsparklabs.asanti.exception.DecodeException;
import com.brightsparklabs.asanti.schema.AsnBuiltinType;
import java.math.BigInteger;
import java.time.OffsetDateTime;

/**
 * Utility class for decoding bytes in ASN.1 Types
 *
 * @author brightSPARK Labs
 */
public class AsnByteDecoder {
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /** Default constructor */
    private AsnByteDecoder() {
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
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsBitString(final byte[] bytes) throws DecodeException {
        return BitStringDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#BmpString}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsBmpString(final byte[] bytes) throws DecodeException {
        return BmpStringDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Boolean}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static boolean decodeAsBoolean(final byte[] bytes) throws DecodeException {
        return BooleanDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#CharacterString}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsCharacterString(final byte[] bytes) throws DecodeException {
        return CharacterStringDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Date}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static OffsetDateTime decodeAsDate(final byte[] bytes) throws DecodeException {
        return DateDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#DateTime}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static OffsetDateTime decodeAsDateTime(final byte[] bytes) throws DecodeException {
        return DateTimeDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Duration}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsDuration(final byte[] bytes) throws DecodeException {
        return DurationDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#EmbeddedPDV}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsEmbeddedPDV(final byte[] bytes) throws DecodeException {
        return EmbeddedPDVDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Enumerated}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsEnumerated(final byte[] bytes) throws DecodeException {
        return EnumeratedDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#External}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsExternal(final byte[] bytes) throws DecodeException {
        return ExternalDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#GeneralizedTime}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static OffsetDateTime decodeAsGeneralizedTime(final byte[] bytes)
            throws DecodeException {
        return GeneralizedTimeDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#GeneralString}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsGeneralString(final byte[] bytes) throws DecodeException {
        return GeneralStringDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#GraphicString}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsGraphicString(final byte[] bytes) throws DecodeException {
        return GraphicStringDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Ia5String}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsIa5String(final byte[] bytes) throws DecodeException {
        return Ia5StringDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#InstanceOf}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsInstanceOf(final byte[] bytes) throws DecodeException {
        return InstanceOfDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Integer}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static BigInteger decodeAsInteger(final byte[] bytes) throws DecodeException {
        return IntegerDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Iri}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsIri(final byte[] bytes) throws DecodeException {
        return IriDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Iso646String}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsIso646String(final byte[] bytes) throws DecodeException {
        return Iso646StringDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Null}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsNull(final byte[] bytes) throws DecodeException {
        return NullDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#NumericString}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsNumericString(final byte[] bytes) throws DecodeException {
        return NumericStringDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#ObjectClassField}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsObjectClassField(final byte[] bytes) throws DecodeException {
        return ObjectClassFieldDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#OctetString}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data. No
     *     transformation is done for bytes in an OCTET STRING. I.e. the bytes are returned as is.
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static byte[] decodeAsOctetString(final byte[] bytes) throws DecodeException {
        return OctetStringDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Oid}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsOid(final byte[] bytes) throws DecodeException {
        return OidDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#OidIri}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsOidIri(final byte[] bytes) throws DecodeException {
        return OidIriDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Prefixed}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsPrefixed(final byte[] bytes) throws DecodeException {
        return PrefixedDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#PrintableString}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsPrintableString(final byte[] bytes) throws DecodeException {
        return PrintableStringDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Real}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsReal(final byte[] bytes) throws DecodeException {
        return RealDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#RelativeIri}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsRelativeIri(final byte[] bytes) throws DecodeException {
        return RelativeIriDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#RelativeOid}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsRelativeOid(final byte[] bytes) throws DecodeException {
        return RelativeOidDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#RelativeOidIri}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsRelativeOidIri(final byte[] bytes) throws DecodeException {
        return RelativeOidIriDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#TeletexString}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsTeletexString(final byte[] bytes) throws DecodeException {
        return TeletexStringDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Time}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static OffsetDateTime decodeAsTime(final byte[] bytes) throws DecodeException {
        return TimeDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#TimeOfDay}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static OffsetDateTime decodeAsTimeOfDay(final byte[] bytes) throws DecodeException {
        return TimeOfDayDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#UniversalString}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsUniversalString(final byte[] bytes) throws DecodeException {
        return UniversalStringDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#UtcTime}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static OffsetDateTime decodeAsUtcTime(final byte[] bytes) throws DecodeException {
        return UtcTimeDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Utf8String}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsUtf8String(final byte[] bytes) throws DecodeException {
        return Utf8StringDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#VideotexString}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsVideotexString(final byte[] bytes) throws DecodeException {
        return VideotexStringDecoder.getInstance().decode(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#VisibleString}
     *
     * @param bytes bytes to decode
     * @return the decoded bytes
     * @throws DecodeException if any errors occur while decoding the supplied data
     */
    public static String decodeAsVisibleString(final byte[] bytes) throws DecodeException {
        return VisibleStringDecoder.getInstance().decode(bytes);
    }
}
