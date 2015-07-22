/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.tag;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Models a tag in a 'constructed' type. A tag conforms to one of the following formats: <ul>
 * <li>tagNumber (e.g. {@code 1}</li> <li>tagIndex.tagNumber (e.g. {@code 0.1} or {@code 1.u.2}</li>
 * </ul>
 */
public class AsnSchemaTag
{
    // ---------------------------------------------------------------------
    // CONSTANTS
    // ---------------------------------------------------------------------

    /** null instance */
    private static final AsnSchemaTag NULL = new AsnSchemaTag("", "", "");

    /** pattern to match a raw tag coming out of the BER decoder */
    private static final Pattern PATTERN_TAG = Pattern.compile(
            "^([0-9]+)(\\[(([0-9]+)|(UNIVERSAL ([a-zA-Z0-9]+)))\\])$");

    // ---------------------------------------------------------------------
    // INSTANCE VARIABLES
    // ---------------------------------------------------------------------

    /**
     * the context specific tag number component of the raw tag.  Note that this and tagUniversal
     * are mutually exclusive
     */
    private final String tagContextSpecific;

    /**
     * the Universal tag number component of the raw tag.  Note that this and the tagContextSpecific
     * are mutually exclusive
     */
    private final String tagUniversal;

    /** the tag index component of the raw tag. Blank if no index component */
    private final String tagIndex;

    // ---------------------------------------------------------------------
    // CONSTRUCTION
    // ---------------------------------------------------------------------

    /**
     * Default constructor. Private, use {@link #create(String)} instead.
     *
     * @param tagIndex
     *         tag index component of the raw tag
     * @param tagContextSpecific
     *         tag context-specific component of the raw tag. Set to {@code null} if no
     *         context-specific component.
     * @param tagUniversal
     *         tag universal component of the raw tag.  Set to (@code null} if no universal
     *         component.
     */
    private AsnSchemaTag(String tagIndex, String tagContextSpecific, String tagUniversal)
    {
        this.tagIndex = tagIndex;
        this.tagContextSpecific = Strings.nullToEmpty(tagContextSpecific).trim();
        this.tagUniversal = Strings.nullToEmpty(tagUniversal).trim();
    }

    /**
     * Creates an instance from the supplied raw tag
     *
     * @param rawTag
     *         raw tag to create instance from
     *
     * @return instance which models the raw tag, or {@link #NULL} if the raw tag is invalid
     */
    public static AsnSchemaTag create(String rawTag)
    {
        if (rawTag == null)
        {
            return NULL;
        }

        final Matcher matcher = PATTERN_TAG.matcher(rawTag);
        if (matcher.matches())
        {
            return new AsnSchemaTag(matcher.group(1), matcher.group(4), matcher.group(5));
        }
        else
        {
            return NULL;
        }
    }

    /**
     * Creates an instance, deriving the Universal type from the type passed in
     *
     * @param tagIndex
     *         tag index component of the raw tag
     * @param type
     *         tag universal component of the raw tag.  Set to (@code null} if no universal
     *         component.
     *
     * @return a new AsnSchemaTag
     */
    public static AsnSchemaTag create(String tagIndex, AsnBuiltinType type)
    {
        return new AsnSchemaTag(tagIndex, "", createUniversalPortion(type));
    }

    /**
     * Creates an instance, combining the index and tag
     *
     * @param tagIndex
     *         tag index component of the raw tag
     * @param tag
     *         the tag portion
     *
     * @return a new AsnSchemaTag
     */
    public static AsnSchemaTag create(int tagIndex, String tag)
    {
        return create(createRawTag(tagIndex, tag));
    }

    /**
     * Creates a new tag with an updated index
     *
     * @param tagIndex
     *         tag index component of the raw tag
     * @param tag
     *         the old tag to get the tag Portion from
     *
     * @return a new AsnSchemaTag
     */
    public static AsnSchemaTag create(int tagIndex, AsnSchemaTag tag)
    {
        return new AsnSchemaTag(Integer.toString(tagIndex),
                tag.getTagContextSpecific(),
                tag.getTagUniversal());
    }

    /**
     * @param tagIndex
     *         tag index component of the raw tag
     * @param tag
     *         tag potion
     *
     * @return a String representation of what toString of this tag would be, or this string can be
     * passed in to create a new tag
     */
    public static String createRawTag(int tagIndex, String tag)
    {
        return String.format("%d[%s]", tagIndex, tag);
    }

    /**
     * @param tagIndex
     *         tag index component of the raw tag
     * @param universalTagNumber
     *         ASN.1 Universal tag number
     *
     * @return a String representation of what toString of this tag would be, or this string can be
     * passed in to create a new tag
     */
    public static String createRawTag(int tagIndex, int universalTagNumber)
    {
        return createRawTag(tagIndex,
                createUniversalPortion(getBuiltInTypeForUniversalTag(universalTagNumber)));
    }

    /**
     * Creates the Universal portion of a tag based on the type
     *
     * @param type
     *         AsnBuiltinType to map to universal tag number
     *
     * @return the equivalent of the getTagUniversal for a tag created from this type
     */
    public static String createUniversalPortion(AsnBuiltinType type)
    {
        // Some AsnBuiltinType's don't translate to a UNIVERSAL tag, eg Choice
        final String universalType = getUniversalTagForBuiltInType(type);
        return (universalType.isEmpty()) ? "" : ("UNIVERSAL " + universalType);
    }

    // ---------------------------------------------------------------------
    // PUBLIC METHODS
    // ---------------------------------------------------------------------

    @Override
    public String toString()
    {
        return getRawTag();
    }

    /**
     * Returns the raw tag (that came from the AsnData)
     *
     * @return the raw tag (that came from the AsnData)
     */
    public String getRawTag()
    {
        if (tagIndex.isEmpty())
        {
            // If this is a valid tag then the index will not be empty, so if it
            // is empty it is invalid and we should return an empty string
            return "";
        }

        return tagIndex + "[" + getTagPortion() + "]";
    }

    /**
     * Returns the tag index component of the raw tag. For a raw tag {@code "1.2"} this is {@code
     * "1"}.
     *
     * @return the tag index or a blank string if no index component
     */
    public String getTagIndex()
    {
        return tagIndex;
    }

    /**
     * Returns the context-specific component of the raw tag. For a raw tag {@code "0.1"} this is
     * {@code "1"}.  For raw tag {@code "0.u.2"} this is {@code ""}.
     *
     * @return the tag number
     */
    public String getTagContextSpecific()
    {
        return tagContextSpecific;
    }

    /**
     * Returns the universal component of the raw tag. For a raw tag {@code "0.u.2"} this is {@code
     * "u.2"}.  For raw tag {@code "0.1"} this is {@code ""}.
     *
     * @return the tag number
     */
    public String getTagUniversal()
    {
        return tagUniversal;
    }

    /**
     * Returns the non-index component of the raw tag. For a raw tag {@code "0.u.2"} this is {@code
     * "u.2"}.  For raw tag {@code "0.1"} this is {@code "1"}.  This returns either the
     * context-specific or universal tag, which ever is not empty
     *
     * @return the tag number
     */
    public String getTagPortion()
    {
        return tagUniversal.isEmpty() ? tagContextSpecific : tagUniversal;
    }

    /**
     * Helper to map from AsnBuiltinType to the ASN.1 value for the Universal tag of this type.
     *
     * @param type
     *         AsnBuiltinType to map to universal tag number
     *
     * @return the String representation of the universal type (integer), empty string if there is
     * no mapping
     */
    private static String getUniversalTagForBuiltInType(AsnBuiltinType type)
    {
        return Optional.fromNullable(BUILTIN_TYPE_TO_UNIVERSAL_TAG.get(type)).or("");
    }

    /**
     * Map from the ASN.1 Universal tag to our AsnBuiltinType
     *
     * @param universalTag
     *         ASN.1 Universal tag
     *
     * @return respective AsnBuiltinType for the Universal tag, AsnBuiltinType#NULL if there is no
     * match
     */
    public static AsnBuiltinType getBuiltInTypeForUniversalTag(int universalTag)
    {
        return Optional.fromNullable(UNIVERSAL_TAG_TO_BUILTIN_TYPE.get(universalTag))
                .or(AsnBuiltinType.Null);
    }

    private static final ImmutableMap<AsnBuiltinType, String> BUILTIN_TYPE_TO_UNIVERSAL_TAG
            = ImmutableMap.<AsnBuiltinType, String>builder()
            .put(AsnBuiltinType.Boolean, "1")
            .put(AsnBuiltinType.Integer, "2")
            .put(AsnBuiltinType.BitString, "3")
            .put(AsnBuiltinType.OctetString, "4")
            .put(AsnBuiltinType.Null, "5").put(AsnBuiltinType.Oid, "6")
                    //.put(AsnBuiltinType. ObjectDescriptor, 7)
            .put(AsnBuiltinType.InstanceOf, "8")
            .put(AsnBuiltinType.External, "8")
            .put(AsnBuiltinType.Real, "9")
            .put(AsnBuiltinType.Enumerated, "10")
            .put(AsnBuiltinType.EmbeddedPDV, "11")
            .put(AsnBuiltinType.Utf8String, "12").put(AsnBuiltinType.RelativeOid, "13")
                    //.put( reserved, 14)
                    //.put( reserved, 15)
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

    private static final ImmutableMap<Integer, AsnBuiltinType> UNIVERSAL_TAG_TO_BUILTIN_TYPE
            = ImmutableMap.<Integer, AsnBuiltinType>builder()
            .put(1, AsnBuiltinType.Boolean)
            .put(2, AsnBuiltinType.Integer)
            .put(3, AsnBuiltinType.BitString)
            .put(4, AsnBuiltinType.OctetString)
            .put(5, AsnBuiltinType.Null).put(6, AsnBuiltinType.Oid)
                    //.put(AsnBuiltinType. ObjectDescriptor, 7)
            .put(8, AsnBuiltinType.InstanceOf).put(9, AsnBuiltinType.External)
                    //.put(9, AsnBuiltinType.Real)
            .put(10, AsnBuiltinType.Enumerated)
            .put(11, AsnBuiltinType.EmbeddedPDV)
            .put(12, AsnBuiltinType.Utf8String).put(13, AsnBuiltinType.RelativeOid)
                    //.put( reserved, 14)
                    //.put( reserved, 15)
            .put(16, AsnBuiltinType.Sequence)
                    //.put(16, AsnBuiltinType.SequenceOf)
            .put(17, AsnBuiltinType.Set)
                    //.put(17, AsnBuiltinType.SetOf)
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
}
