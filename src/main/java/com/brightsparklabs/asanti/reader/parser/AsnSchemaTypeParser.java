package com.brightsparklabs.asanti.reader.parser;

import com.brightsparklabs.asanti.model.schema.AsnModuleTaggingMode;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.tag.TagCreator;
import com.brightsparklabs.asanti.model.schema.type.*;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaNamedTag;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.*;

/**
 * Logic for parsing a type (either a Type Definition, or Component Type)
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeParser
{
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /* TODO ASN-105, ASN-107 - the list of AsnPrimitiveTypes is not complete, as
     * we want to build support for more types (through Decoders and Validators)
     * then we also need to create the supporting primitive type and add it to
     * the parsing logic in the appropriate place.
     */

    // -------------------------------------------------------------------------
    // COLLECTION TYPES (SEQUENCE Of and SET OF)
    // -------------------------------------------------------------------------

    /** pattern to match a SET OF type definition */
    private static final Pattern PATTERN_TYPE_COLLECTION = Pattern.compile(
            "^(SEQUENCE|SET)(( SIZE)? \\(.+?\\)\\)?)? OF (.+)$");

    /** all valid 'collection' types */
    private static final ImmutableMap<String, AsnPrimitiveType> collectionTypes = ImmutableMap.of(
            "SEQUENCE",
            AsnPrimitiveType.SEQUENCE_OF,
            "SET",
            AsnPrimitiveType.SET_OF);

    // -------------------------------------------------------------------------
    // CONSTRUCTED TYPES (SEQUENCE, SET, CHOICE)
    // -------------------------------------------------------------------------

    /* TODO ASN-137 / ASN-80 - should the EXPLICIT|IMPLICIT keyword be included in any of these?
     * If so then where and how do we change behaviour as a result of extracting them?
     */

    /** pattern to match a Constructed type definition */
    private static final Pattern PATTERN_TYPE_CONSTRUCTED = Pattern.compile(
            "^(SEQUENCE|SET|CHOICE) ?\\{(.+)\\} ?(\\(.+\\))?$");

    /** all valid 'constructed' types */
    private static final ImmutableMap<String, AsnPrimitiveType> constructedTypes = ImmutableMap.of(
            "SEQUENCE",
            AsnPrimitiveType.SEQUENCE,
            "SET",
            AsnPrimitiveType.SET,
            "CHOICE",
            AsnPrimitiveType.CHOICE);

    /** pattern to match a ENUMERATED type definition */
    private static final Pattern PATTERN_TYPE_ENUMERATED = Pattern.compile(
            "^ENUMERATED ?\\{(.+)\\}$");

    /** pattern to match a CLASS type definition */
    private static final Pattern PATTERN_TYPE_CLASS = Pattern.compile("^CLASS ?\\{(.+)\\}$");

    // -------------------------------------------------------------------------
    // TYPES WITH CONSTRAINTS AND DISTINGUISHED VALUES
    // -------------------------------------------------------------------------
    //    /** pattern to match an Integer type definition */
    //    private static final Pattern PATTERN_TYPE_INTEGER = Pattern.compile(
    //            "^INTEGER ?(\\{(.+?)\\})? ?(\\((.+?)\\))? ?(DEFAULT ?.+)?$");
    //
    //    /** pattern to match a BIT STRING type definition */
    //    private static final Pattern PATTERN_TYPE_BIT_STRING = Pattern.compile(
    //            "^BIT STRING ?(\\{(.+?)\\})? ?(DEFAULT ?\\{(.+?)\\})? ?(\\((.+?)\\))?$");
    // TODO ASN-135 - ^ these two are actually different.  Set up some tests and decide what to do

    /** pattern to match a type with distinguished values */
    private static final Pattern PATTERN_TYPE_WITH_NAMED_VALUES = Pattern.compile(
            "^(BIT STRING|INTEGER) ?(\\{(.+?)\\})? ?(DEFAULT ?\\{(.+?)\\})? ?(\\((.+?)\\))?$");

    /** all valid 'distinguished value' types */
    private static final ImmutableMap<String, AsnPrimitiveType> distinguishedValuesTypes
            = ImmutableMap.of("BIT STRING",
            AsnPrimitiveType.BIT_STRING,
            "INTEGER",
            AsnPrimitiveType.INTEGER);

    // -------------------------------------------------------------------------
    // TYPES WITH CONSTRAINTS
    // -------------------------------------------------------------------------

    /** pattern to match a type with constraints */
    private static final Pattern PATTERN_TYPE_WITH_CONSTRAINTS = Pattern.compile(
            "^(GeneralString|VisibleString|NumericString|IA5String|OCTET STRING|UTF8String|OBJECT IDENTIFIER|PrintableString|IA5String|RELATIVE-OID|BOOLEAN|UTCTime|NULL) ?(\\((.+)\\))?$");

    /** all valid 'constrained' types */
    private static final ImmutableMap<String, AsnPrimitiveType> constrainedTypes
            = ImmutableMap.<String, AsnPrimitiveType>builder()
            .put("BOOLEAN", AsnPrimitiveType.BOOLEAN)
            .put("IA5String", AsnPrimitiveType.IA5_STRING)
            .put("GeneralString", AsnPrimitiveType.GENERAL_STRING)
            .put("NULL", AsnPrimitiveType.NULL)
            .put("NumericString", AsnPrimitiveType.NUMERIC_STRING)
            .put("OBJECT IDENTIFIER", AsnPrimitiveType.OID)
            .put("OCTET STRING", AsnPrimitiveType.OCTET_STRING)
            .put("PrintableString", AsnPrimitiveType.PRINTABLE_STRING)
            .put("RELATIVE-OID", AsnPrimitiveType.RELATIVE_OID)
            .put("UTCTime", AsnPrimitiveType.UTC_TIME)
            .put("UTF8String", AsnPrimitiveType.UTF8_STRING)
            .put("VisibleString", AsnPrimitiveType.VISIBLE_STRING)
            .build();

    // -------------------------------------------------------------------------
    // TYPES WITHOUT CONSTRAINTS
    // -------------------------------------------------------------------------

    /** pattern to match a type without constraints */
    private static final Pattern PATTERN_TYPE_WITH_NO_CONSTRAINTS = Pattern.compile(
            "^(GeneralizedTime)$");

    /** all valid 'unconstrained' types */
    private static final ImmutableMap<String, AsnPrimitiveType> noConstraintsTypes
            = ImmutableMap.<String, AsnPrimitiveType>builder()
            .put("GeneralizedTime", AsnPrimitiveType.GENERALIZED_TIME)
            .build();

    // TODO ASN-25 remove this once ASN-25 is completed

    /** pattern to match a PRIMITIVE type definition */
    private static final Pattern PATTERN_TYPE_PRIMITIVE = Pattern.compile(
            "^(BIT STRING|BMPString|BOOLEAN|CHARACTER STRING|DATE|DATE_TIME|DURATION|EMBEDDED PDV|GeneralizedTime|GeneralString|GraphicString|IA5String|INTEGER|ISO646String|NULL|NumericString|OCTET STRING|OBJECT IDENTIFIER|OID-IRI|PrintableString|REAL|RELATIVE-OID|RELATIVE-OID-IRI|T61String|TeletexString|TIME|TIME-OF-DAY|UniversalString|UTCTime|UTF8String|VideotexString|VisibleString) ?(\\{(.+)\\})? ?(\\((.+)\\))?$");

    /** pattern for non-primitive type matching */
    private static final Pattern PATTERN_DEFINED_TYPE = Pattern.compile(
            "^(((EXPLICIT|IMPLICIT)[ ]+)?(([a-zA-Z0-9\\-]+)\\.)?([a-zA-Z0-9\\-]+)) ?(\\{(.+?)\\})? ?(\\((.+?)\\))? ?(DEFAULT ?.+)?$");
    //"^((([a-zA-Z0-9\\-]+)\\.)?([a-zA-Z0-9\\-]+)) ?(\\{(.+?)\\})? ?(\\((.+?)\\))? ?(DEFAULT ?.+)?$");

    /** error message if an unknown ASN.1 built-in type is found */
    private static final String ERROR_UNKNOWN_BUILT_IN_TYPE
            = "Parser expected a constructed built-in type (SEQUENCE, SET, ENUMERATED, SEQUENCE OF, SET OF, CHOICE, CLASS) or a primitive built-in type (BIT STRING, GeneralizedTime, Ia5String, INTEGER, NumericString, OCTET STRING, Utf8String, VisibleString, BOOLEAN, DATE, CHARACTER STRING, DATE_TIME, DURATION, EMBEDDED PDV, EXTERNAL, INTEGER, OID-IRI, NULL, OBJECT IDENTIFIER, REAL, RELATIVE-OID-IRI, RELATIVE-OID, BmpString, GeneralString, GraphicString, Iso646String, PrintableString, TeletexString, T61String, UniversalString, VideotexString, TIME, TIME-OF-DAY, CHARACTER STRING) but found: ";

    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(AsnSchemaTypeDefinitionParser.class
            .getName());

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Private constructor. There should be no need to ever instantiate this static class.
     */
    private AsnSchemaTypeParser()
    {
        throw new AssertionError();
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses a type definition from a module from an ASN.1 schema
     *
     * @param value
     *         the string to parse.  This should be either the Type of the defined type (i.e. the
     *         text on the right hand side of the {@code ::=}), or the Type of a Component Type
     * @param taggingMode
     *         dictates the mode in which to handle/generate tags
     *
     * @return an {@link AsnSchemaType} object representing the parsed type
     *
     * @throws ParseException
     *         if either of the parameters are {@code null}/empty or any errors occur while parsing
     *         the type
     * @throws NullPointerException
     *         if taggingMode is {@code null}
     */
    public static AsnSchemaType parse(String value, AsnModuleTaggingMode taggingMode)
            throws ParseException
    {
        checkNotNull(taggingMode);
        if (value == null || value.trim().isEmpty())
        {
            throw new ParseException("A value must be supplied for a Type", -1);
        }

        // -------------------------------------------------------------------------
        // CONSTRUCTED TYPES (SEQUENCE, SET ETC)
        // -------------------------------------------------------------------------
        Matcher matcher = PATTERN_TYPE_CONSTRUCTED.matcher(value);
        if (matcher.matches())
        {
            return parseConstructed(matcher, taggingMode);
        }

        matcher = PATTERN_TYPE_ENUMERATED.matcher(value);
        if (matcher.matches())
        {
            return parseEnumerated(matcher);
        }

        // -------------------------------------------------------------------------
        // COLLECTION TYPES (SEQUENCE OF AND SET OF)
        // -------------------------------------------------------------------------
        matcher = PATTERN_TYPE_COLLECTION.matcher(value);
        if (matcher.matches())
        {
            return parseCollection(matcher, taggingMode);
        }

        // -------------------------------------------------------------------------
        // TYPES WITH CONSTRAINTS AND DISTINGUISHED VALUES
        // -------------------------------------------------------------------------
        matcher = PATTERN_TYPE_WITH_NAMED_VALUES.matcher(value);
        if (matcher.matches())
        {
            return parseWithNamedValues(matcher);
        }

        // -------------------------------------------------------------------------
        // TYPES WITH CONSTRAINTS
        // -------------------------------------------------------------------------
        // check if defining an Object Identifier
        matcher = PATTERN_TYPE_WITH_CONSTRAINTS.matcher(value);
        if (matcher.matches())
        {
            return parseWithConstraints(matcher);
        }

        // -------------------------------------------------------------------------
        // TYPES WITHOUT CONSTRAINTS
        // -------------------------------------------------------------------------
        // check if defining a GeneralizedTime
        matcher = PATTERN_TYPE_WITH_NO_CONSTRAINTS.matcher(value);
        if (matcher.matches())
        {
            return parseWithoutConstraints(matcher);
        }

        // catch logging for if we missed a Primitive.
        matcher = PATTERN_TYPE_PRIMITIVE.matcher(value);
        if (matcher.matches())
        {
            logger.warn("Did not parse as Primitive: " + value);
        }

        // check if defining a CLASS
        matcher = PATTERN_TYPE_CLASS.matcher(value);
        if (matcher.matches())
        {
            // TODO ASN-39 - handle CLASS
            logger.warn("Type Definitions for CLASS not yet supported");
            return AsnSchemaType.NULL;
        }

        // -------------------------------------------------------------------------
        // TYPES THAT ARE NOT PRIMITIVES, I.E. THEY MUST BE TYPE DEFINITIONS
        // -------------------------------------------------------------------------
        matcher = PATTERN_DEFINED_TYPE.matcher(value);
        if (matcher.matches())
        {
            return parsePlaceHolder(matcher);
        }

        // unknown definition
        final String error = ERROR_UNKNOWN_BUILT_IN_TYPE + value;
        throw new ParseException(error, -1);
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses a constructed type (eg SEQUENCE)
     *
     * @param matcher
     *         matcher which matched on a corresponding
     *
     * @return an {@link AsnSchemaTypeConstructed} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaTypeConstructed parseConstructed(Matcher matcher,
            AsnModuleTaggingMode taggingMode) throws ParseException
    {
        final AsnPrimitiveType primitiveType = getPrimitiveType(matcher, constructedTypes);

        final String componentTypesText = matcher.group(2);
        final String constraintText = Strings.nullToEmpty(matcher.group(3));

        final ImmutableList<AsnSchemaComponentType> componentTypes
                = AsnSchemaComponentTypeParser.parse(componentTypesText, taggingMode);

        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);

        return new AsnSchemaTypeConstructed(primitiveType,
                constraint,
                componentTypes,
                TagCreator.create(primitiveType, taggingMode));
    }

    /**
     * Parses an Enumerated type
     *
     * @param matcher
     *         matcher which matched on eg {@link #PATTERN_TYPE_ENUMERATED}
     *
     * @return an {@link AsnSchemaTypeWithNamedTags} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaTypeWithNamedTags parseEnumerated(Matcher matcher) throws ParseException
    {

        final String namedValuesText = matcher.group(1);
        logger.debug("named values {}", namedValuesText);
        final ImmutableList<AsnSchemaNamedTag> namedValues
                = AsnSchemaNamedTagParser.parseEnumeratedOptions(namedValuesText);

        return new AsnSchemaTypeWithNamedTags(AsnPrimitiveType.ENUMERATED,
                AsnSchemaConstraint.NULL,
                namedValues);
    }

    /**
     * Parses a collection type (eg SEQUENCE Of)
     *
     * @param matcher
     *         matcher which matched on a corresponding
     *
     * @return an {@link AsnSchemaTypeCollection} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaTypeCollection parseCollection(Matcher matcher,
            AsnModuleTaggingMode taggingMode) throws ParseException
    {
        final AsnPrimitiveType primitiveType = getPrimitiveType(matcher, collectionTypes);

        final String constraintText = Strings.nullToEmpty(matcher.group(2));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);

        final String rawCollectionType = Strings.nullToEmpty(matcher.group(4));

        final AsnSchemaType collectionType = parse(rawCollectionType, taggingMode);

        return new AsnSchemaTypeCollection(primitiveType, constraint, collectionType);
    }

    /**
     * Parses a type of the form Typename, eg "^GeneralizedTime$"
     *
     * @param matcher
     *         matcher which matched on a corresponding "generic" matcher
     *
     * @return an {@link BaseAsnSchemaType} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static BaseAsnSchemaType parseWithoutConstraints(Matcher matcher) throws ParseException
    {
        final AsnPrimitiveType primitiveType = getPrimitiveType(matcher, noConstraintsTypes);

        return new BaseAsnSchemaType(primitiveType, AsnSchemaConstraint.NULL);
    }

    /**
     * Parses a type of the form Typename Constraint, eg "INTEGER (1..100)"
     *
     * @param matcher
     *         matcher which matched on a corresponding "generic" matcher
     *
     * @return an {@link BaseAsnSchemaType} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */

    private static BaseAsnSchemaType parseWithConstraints(Matcher matcher) throws ParseException
    {

        final AsnPrimitiveType primitiveType = getPrimitiveType(matcher, constrainedTypes);

        final String constraintText = Strings.nullToEmpty(matcher.group(3));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);

        return new BaseAsnSchemaType(primitiveType, constraint);
    }

    /**
     * Parses a type that may have both Constraints and Named values
     *
     * @param matcher
     *         matcher which matched on eg {@link #PATTERN_TYPE_WITH_NAMED_VALUES}
     *
     * @return an {@link AsnSchemaTypeWithNamedTags} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaTypeWithNamedTags parseWithNamedValues(Matcher matcher)
            throws ParseException
    {
        final AsnPrimitiveType primitiveType = getPrimitiveType(matcher, distinguishedValuesTypes);

        final String distinguishedValuesText = Strings.nullToEmpty(matcher.group(3));
        final ImmutableList<AsnSchemaNamedTag> distinguishedValues
                = AsnSchemaNamedTagParser.parseIntegerDistinguishedValues(distinguishedValuesText);
        final String constraintText = Strings.nullToEmpty(matcher.group(7));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);

        return new AsnSchemaTypeWithNamedTags(primitiveType, constraint, distinguishedValues);
    }

    /**
     * Parses a type that is not a ASN.1 primitive type.
     *
     * @param matcher
     *         matcher which matched on {@link #PATTERN_DEFINED_TYPE}
     *
     * @return an {@link AsnSchemaTypePlaceholder} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaTypePlaceholder parsePlaceHolder(Matcher matcher) throws ParseException
    {
        final String moduleName = Strings.nullToEmpty(matcher.group(5));
        final String typeName = Strings.nullToEmpty(matcher.group(6));

        final String tagMode = matcher.group(3);
        if (tagMode != null)
        {
            logger.debug("Have a defined TAG mode. {} has tag mode of {}", typeName, tagMode);
        }

        final String constraintText = Strings.nullToEmpty(matcher.group(10));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);

        logger.debug("Creating placeholder -{} typeName: {} constraints: {}",
                (moduleName.isEmpty() ? "" : " moduleName: " + moduleName),
                typeName,
                constraintText);
        return new AsnSchemaTypePlaceholder(moduleName, typeName, constraint);
    }

    /**
     * helper function to extract the AsnPrimitiveType from the matcher
     *
     * @param matcher
     *         matcher which should have the primitive type as group 1
     * @param sourceTypes
     *         Map of ASN.1 primitive types to AsnPrimitiveType
     *
     * @return a {@link AsnPrimitiveType} as extracted from the matcher
     *
     * @throws ParseException
     *         if the type is not in the sourceTypes
     */
    private static AsnPrimitiveType getPrimitiveType(Matcher matcher,
            ImmutableMap<String, AsnPrimitiveType> sourceTypes) throws ParseException
    {
        final AsnPrimitiveType primitiveType = sourceTypes.get(matcher.group(1));
        if (primitiveType == null)
        {
            // unknown definition
            final String error = ERROR_UNKNOWN_BUILT_IN_TYPE + matcher.group(0);
            throw new ParseException(error, -1);
        }
        return primitiveType;
    }
}
