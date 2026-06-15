/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.tag;

import com.brightsparklabs.asanti.schema.AsnBuiltinType;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.Optional;
import org.bouncycastle.asn1.ASN1BMPString;
import org.bouncycastle.asn1.ASN1BitString;
import org.bouncycastle.asn1.ASN1Boolean;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1Enumerated;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1IA5String;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Null;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1PrintableString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1UTCTime;
import org.bouncycastle.asn1.ASN1UTF8String;
import org.bouncycastle.asn1.ASN1VisibleString;
import org.bouncycastle.asn1.BERTags;

/**
 * Models a tag in a 'constructed' type. A tag conforms to one of the following formats:
 *
 * <ul>
 *   <li>tagNumber (e.g. {@code 1}
 *   <li>tagIndex.tagNumber (e.g. {@code 0.1} or {@code 1.u.2}
 * </ul>
 */
public class AsnSchemaTag {
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** null instance */
    private static final AsnSchemaTag NULL = new AsnSchemaTag("", "", "");

    private static final ImmutableMap<AsnBuiltinType, String> BUILTIN_TYPE_TO_UNIVERSAL_TAG =
            ImmutableMap.<AsnBuiltinType, String>builder()
                    .put(AsnBuiltinType.Boolean, "1")
                    .put(AsnBuiltinType.Integer, "2")
                    .put(AsnBuiltinType.BitString, "3")
                    .put(AsnBuiltinType.OctetString, "4")
                    .put(AsnBuiltinType.Null, "5")
                    .put(AsnBuiltinType.Oid, "6")
                    .put(AsnBuiltinType.ObjectDescriptor, "7")
                    .put(AsnBuiltinType.InstanceOf, "8")
                    .put(AsnBuiltinType.External, "8")
                    .put(AsnBuiltinType.Real, "9")
                    .put(AsnBuiltinType.Enumerated, "10")
                    .put(AsnBuiltinType.EmbeddedPDV, "11")
                    .put(AsnBuiltinType.Utf8String, "12")
                    .put(AsnBuiltinType.RelativeOid, "13")
                    // .put( reserved, 14)
                    // .put( reserved, 15)
                    .put(AsnBuiltinType.Sequence, "16")
                    .put(AsnBuiltinType.SequenceOf, "16")
                    .put(AsnBuiltinType.Set, "17")
                    .put(AsnBuiltinType.SetOf, "17")
                    .put(AsnBuiltinType.NumericString, "18")
                    .put(AsnBuiltinType.PrintableString, "19")
                    .put(AsnBuiltinType.TeletexString, "20")
                    .put(AsnBuiltinType.VideotexString, "21")
                    .put(AsnBuiltinType.Ia5String, "22")
                    .put(AsnBuiltinType.UtcTime, "23")
                    .put(AsnBuiltinType.GeneralizedTime, "24")
                    .put(AsnBuiltinType.GraphicString, "25")
                    .put(AsnBuiltinType.VisibleString, "26")
                    .put(AsnBuiltinType.GeneralString, "27")
                    .put(AsnBuiltinType.UniversalString, "28")
                    .put(AsnBuiltinType.CharacterString, "29")
                    .put(AsnBuiltinType.BmpString, "30")
                    .build();

    private static final ImmutableMap<Integer, AsnBuiltinType> UNIVERSAL_TAG_TO_BUILTIN_TYPE =
            ImmutableMap.<Integer, AsnBuiltinType>builder()
                    .put(1, AsnBuiltinType.Boolean)
                    .put(2, AsnBuiltinType.Integer)
                    .put(3, AsnBuiltinType.BitString)
                    .put(4, AsnBuiltinType.OctetString)
                    .put(5, AsnBuiltinType.Null)
                    .put(6, AsnBuiltinType.Oid)
                    .put(7, AsnBuiltinType.ObjectDescriptor)
                    .put(8, AsnBuiltinType.InstanceOf)
                    .put(9, AsnBuiltinType.External)
                    // .put(9, AsnBuiltinType.Real)
                    .put(10, AsnBuiltinType.Enumerated)
                    .put(11, AsnBuiltinType.EmbeddedPDV)
                    .put(12, AsnBuiltinType.Utf8String)
                    .put(13, AsnBuiltinType.RelativeOid)
                    // .put( reserved, 14)
                    // .put( reserved, 15)
                    .put(16, AsnBuiltinType.Sequence)
                    // .put(16, AsnBuiltinType.SequenceOf)
                    .put(17, AsnBuiltinType.Set)
                    // .put(17, AsnBuiltinType.SetOf)
                    .put(18, AsnBuiltinType.NumericString)
                    .put(19, AsnBuiltinType.PrintableString)
                    .put(20, AsnBuiltinType.TeletexString)
                    .put(21, AsnBuiltinType.VideotexString)
                    .put(22, AsnBuiltinType.Ia5String)
                    .put(23, AsnBuiltinType.UtcTime)
                    .put(24, AsnBuiltinType.GeneralizedTime)
                    .put(25, AsnBuiltinType.GraphicString)
                    .put(26, AsnBuiltinType.VisibleString)
                    .put(27, AsnBuiltinType.GeneralString)
                    .put(28, AsnBuiltinType.UniversalString)
                    .put(29, AsnBuiltinType.CharacterString)
                    .put(30, AsnBuiltinType.BmpString)
                    .build();

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /**
     * the context specific tag number component of the raw tag. Note that this and tagUniversal are
     * mutually exclusive
     */
    private final String tagContextSpecific;

    /**
     * the Universal tag number component of the raw tag. Note that this and the tagContextSpecific
     * are mutually exclusive
     */
    private final String tagUniversal;

    /** the tag index component of the raw tag. Blank if no index component */
    private final String tagIndex;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor. Private, use {@link #create(String)} instead.
     *
     * @param tagIndex Tag index component of the raw tag.
     * @param tagContextSpecific Tag context-specific component of the raw tag. Set to {@code null}
     *     if no context-specific component.
     * @param tagUniversal Tag universal component of the raw tag. Set to {@code null} if no
     *     universal component.
     */
    private AsnSchemaTag(
            final String tagIndex, final String tagContextSpecific, final String tagUniversal) {
        this.tagIndex = tagIndex;
        this.tagContextSpecific = Strings.nullToEmpty(tagContextSpecific).trim();
        this.tagUniversal = Strings.nullToEmpty(tagUniversal).trim();
    }

    /**
     * Creates an instance from the supplied raw tag.
     *
     * <p>The expected format should be either:
     *
     * <ul>
     *   <li>{@code "[0-9]+\[[0-9]\]"} e.g. {@code "12[345]"}
     *   <li>{@code "[0-9]+\[UNIVERSAL [a-zA-Z0-9]+\]"} e.g. {@code "0[UNIVERSAL abcXYZ123]"}
     * </ul>
     *
     * @param rawTag Raw tag to create instance from.
     * @return Instance which models the raw tag, or {@link #NULL} if the raw tag is invalid.
     */
    public static AsnSchemaTag create(final String rawTag) {
        if (rawTag == null) {
            return NULL;
        }

        // NOTE: We are intentionally *not* using regex here. This code is part
        // of the hotpath and even compiled regex matching has a noticeable
        // performance impact. This approach is multiple times faster by comparison.

        final int len = rawTag.length();
        if (len < 4) {
            // Minimum valid value is: "0[1]" so we can do a quick check
            // ahead of time.
            return NULL;
        }

        final int bracketStart = rawTag.indexOf('[');
        if ((bracketStart <= 0) || rawTag.charAt(len - 1) != ']') {
            // No opening or wrong ordering e.g. "0[]0", "[01]".
            return NULL;
        }

        final String tagIndex = rawTag.substring(0, bracketStart);
        // Validate the `tagIndex` is all digits.
        for (int i = 0; i < tagIndex.length(); i++) {
            if (!Character.isDigit(tagIndex.charAt(i))) {
                return NULL;
            }
        }

        // E.g. "UNIVERSAL abcXYZ123".
        final var bracketContent = rawTag.substring(bracketStart + 1, len - 1);
        if (bracketContent.startsWith("UNIVERSAL ") && bracketContent.length() > 10) {
            // Validate the `tagUniversal` is all alphanumeric.
            for (int i = 10; i < bracketContent.length(); i++) {
                if (!Character.isLetterOrDigit(bracketContent.charAt(i))) {
                    return NULL;
                }
            }
            return new AsnSchemaTag(tagIndex, "", bracketContent);
        } else {
            // Validate the `tagContextSpecific` is all digits.
            for (int i = 0; i < bracketContent.length(); i++) {
                if (!Character.isDigit(bracketContent.charAt(i))) {
                    return NULL;
                }
            }
            return new AsnSchemaTag(tagIndex, bracketContent, "");
        }
    }

    /**
     * Creates an instance, deriving the Universal type from the type passed in.
     *
     * @param tagIndex Tag index component of the raw tag.
     * @param type Tag universal component of the raw tag. Set to {@code null} if no universal
     *     component.
     * @return A new {@link AsnSchemaTag}.
     */
    public static AsnSchemaTag create(final String tagIndex, final AsnBuiltinType type) {
        return new AsnSchemaTag(tagIndex, "", createUniversalPortion(type));
    }

    /**
     * Creates an instance, combining the index and tag
     *
     * @param tagIndex Tag index component of the raw tag.
     * @param tag The tag portion.
     * @return A new {@link AsnSchemaTag}.
     */
    public static AsnSchemaTag create(final int tagIndex, final String tag) {
        return create(createRawTag(tagIndex, tag));
    }

    /**
     * Creates a new tag with an updated index
     *
     * @param tagIndex Tag index component of the raw tag.
     * @param tag The old tag to get the tag Portion from.
     * @return A new {@link AsnSchemaTag}.
     */
    public static AsnSchemaTag create(final int tagIndex, final AsnSchemaTag tag) {
        return new AsnSchemaTag(
                Integer.toString(tagIndex), tag.getTagContextSpecific(), tag.getTagUniversal());
    }

    /**
     * @param tagIndex Tag index component of the raw tag,
     * @param tag Tag potion,
     * @return A String representation of what toString of this tag would be, or this string can be
     *     passed in to create a new tag.
     */
    public static String createRawTag(final int tagIndex, final String tag) {
        return tagIndex + "[" + tag + "]";
    }

    /**
     * @param tagIndex Tag index component of the raw tag
     * @param universalTagNumber ASN.1 Universal tag number
     * @return A String representation of what toString of this tag would be, or this string can be
     *     passed in to create a new tag. If the universalTagNumber is invalid returns empty string.
     */
    public static String createRawTagUniversal(final int tagIndex, final int universalTagNumber) {
        return getBuiltInTypeForUniversalTag(universalTagNumber)
                .map(type -> createRawTag(tagIndex, createUniversalPortion(type)))
                .orElse("");
    }

    /**
     * Creates the Universal portion of a tag based on the type
     *
     * @param type {@link AsnBuiltinType} to map to universal tag number
     * @return the equivalent of the getTagUniversal for a tag created from this type
     */
    public static String createUniversalPortion(final AsnBuiltinType type) {
        // Some AsnBuiltinType's don't translate to a UNIVERSAL tag, eg Choice
        final String universalType = getUniversalTagForBuiltInType(type);
        return universalType.isEmpty() ? "" : ("UNIVERSAL " + universalType);
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return getRawTag();
    }

    /** {@return the raw tag (that came from the AsantiAsnData)} */
    public String getRawTag() {
        if (tagIndex.isEmpty()) {
            // If this is a valid tag then the index will not be empty, so if it
            // is empty it is invalid, and we should return an empty string.
            return "";
        }

        return tagIndex + "[" + getTagPortion() + "]";
    }

    /**
     * Returns the tag index component of the raw tag. For a raw tag {@code "1.2"} this is {@code
     * "1"}.
     *
     * @return The tag index or a blank string if no index component.
     */
    public String getTagIndex() {
        return tagIndex;
    }

    /**
     * Returns the context-specific component of the raw tag. For a raw tag {@code "0.1"} this is
     * {@code "1"}. For raw tag {@code "0.u.2"} this is {@code ""}.
     *
     * @return The tag number.
     */
    public String getTagContextSpecific() {
        return tagContextSpecific;
    }

    /**
     * Returns the universal component of the raw tag. For a raw tag {@code "0.u.2"} this is {@code
     * "u.2"}. For raw tag {@code "0.1"} this is {@code ""}.
     *
     * @return the tag number.
     */
    public String getTagUniversal() {
        return tagUniversal;
    }

    /**
     * Returns the non-index component of the raw tag. For a raw tag {@code "0.u.2"} this is {@code
     * "u.2"}. For raw tag {@code "0.1"} this is {@code "1"}. This returns either the
     * context-specific or universal tag, whichever is not empty.
     *
     * @return The tag number.
     */
    public String getTagPortion() {
        return tagUniversal.isEmpty() ? tagContextSpecific : tagUniversal;
    }

    /**
     * Helper to map from AsnBuiltinType to the ASN.1 value for the Universal tag of this type.
     *
     * @param type {@link AsnBuiltinType} to map to universal tag number.
     * @return the String representation of the universal type (integer), empty string if there is
     *     no mapping.
     */
    private static String getUniversalTagForBuiltInType(final AsnBuiltinType type) {
        return Optional.ofNullable(BUILTIN_TYPE_TO_UNIVERSAL_TAG.get(type)).orElse("");
    }

    /**
     * Map from the ASN.1 Universal tag to our AsnBuiltinType.
     *
     * @param universalTag ASN.1 Universal tag.
     * @return Respective {@link AsnBuiltinType} for the Universal tag, {@link Optional#empty()} if
     *     there is no match.
     */
    public static Optional<AsnBuiltinType> getBuiltInTypeForUniversalTag(final int universalTag) {
        return Optional.ofNullable(UNIVERSAL_TAG_TO_BUILTIN_TYPE.get(universalTag));
    }

    /**
     * Determines the UNIVERSAL tag number for a primitive using direct type inspection.
     *
     * <p>Uses instanceof checks to avoid expensive getEncoded() calls. This is 20x faster than
     * encoding the entire object just to read the tag byte. Falls back to encoding only for rare
     * types (< 1% of cases).
     *
     * @param primitive the primitive to inspect
     * @return the UNIVERSAL tag number, or 0 if unknown
     */
    public static int getUniversalTagNumber(final ASN1Primitive primitive) {
        return switch (primitive) {
            case ASN1Boolean _ -> BERTags.BOOLEAN; // 1
            case ASN1Integer _ -> BERTags.INTEGER; // 2
            case ASN1BitString _ -> BERTags.BIT_STRING; // 3
            case ASN1OctetString _ -> BERTags.OCTET_STRING; // 4
            case ASN1Null _ -> BERTags.NULL; // 5
            case ASN1ObjectIdentifier _ -> BERTags.OBJECT_IDENTIFIER; // 6
            case ASN1Enumerated _ -> BERTags.ENUMERATED; // 10
            case ASN1UTF8String _ -> BERTags.UTF8_STRING; // 12
            case ASN1Sequence _ -> BERTags.SEQUENCE; // 16
            case ASN1Set _ -> BERTags.SET; // 17
            case ASN1PrintableString _ -> BERTags.PRINTABLE_STRING; // 19
            case ASN1IA5String _ -> BERTags.IA5_STRING; // 22
            case ASN1UTCTime _ -> BERTags.UTC_TIME; // 23
            case ASN1GeneralizedTime _ -> BERTags.GENERALIZED_TIME; // 24
            case ASN1VisibleString _ -> BERTags.VISIBLE_STRING; // 26
            case ASN1BMPString _ -> BERTags.BMP_STRING; // 30
            default -> {
                try {
                    yield primitive.getEncoded(ASN1Encoding.BER)[0] & 0x1F;
                } catch (IOException _) {
                    yield 0;
                }
            }
        };
    }
}
