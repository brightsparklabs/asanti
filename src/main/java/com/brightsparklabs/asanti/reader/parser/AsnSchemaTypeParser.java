package com.brightsparklabs.asanti.reader.parser;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.type.*;
import com.brightsparklabs.asanti.model.schema.typedefinition.*;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Logic for parsing a type (either a Type Definition, or Component Type)
 *
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeParser
{
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // Collection types (SEQUENCE Of and Set Of)
    // -------------------------------------------------------------------------
//    /** pattern to match a SEQUENCE OF type definition */
//    private static final Pattern PATTERN_TYPE_SEQUENCE_OF = Pattern.compile(
//            "^SEQUENCE(( SIZE)? \\(.+?\\)\\)?)? OF (.+)$");
//
//    /** pattern to match a SET OF type definition */
//    private static final Pattern PATTERN_TYPE_SET_OF = Pattern.compile(
//            "^SET(( SIZE)? \\(.+?\\)\\)?)? OF (.+)$");



    /** pattern to match a SET OF type definition */
    private static final Pattern PATTERN_TYPE_COLLECTION = Pattern.compile(
            "^(SEQUENCE|SET)(( SIZE)? \\(.+?\\)\\)?)? OF (.+)$");
    private static final ImmutableMap<String, AsnPrimitiveType> collectionTypes = ImmutableMap.of(
            "SEQUENCE", AsnPrimitiveType.SEQUENCE_OF,
            "SET", AsnPrimitiveType.SET_OF);

    // -------------------------------------------------------------------------
    // Constructed types (SEQUENCE, Set etc)
    // -------------------------------------------------------------------------
//    /** pattern to match a SET/SEQUENCE type definition */
//    private static final Pattern PATTERN_TYPE_SEQUENCE = Pattern.compile(
//            "^SEQUENCE ?\\{(.+)\\} ?(\\(.+\\))?$");
//
//    /** pattern to match a SET/SEQUENCE type definition */
//    private static final Pattern PATTERN_TYPE_SET = Pattern.compile(
//            "^SET ?\\{(.+)\\} ?(\\(.+\\))?$");
//
//    /** pattern to match a CHOICE type definition */
//    private static final Pattern PATTERN_TYPE_CHOICE = Pattern.compile(
//            "^CHOICE ?\\{(.+)\\} ?(\\(.+\\))?$");

    // TODO MJF - should the EXPLICIT|IMPLICIT keyword be included in any of these?
    // If so then what do we do?

    /** pattern to match a Constructed type definition */
    private static final Pattern PATTERN_TYPE_CONSTRUCTED = Pattern.compile(
            "^(SEQUENCE|SET|CHOICE) ?\\{(.+)\\} ?(\\(.+\\))?$");

    private static final ImmutableMap<String, AsnPrimitiveType> constructedTypes = ImmutableMap.of(
            "SEQUENCE", AsnPrimitiveType.SEQUENCE,
            "SET", AsnPrimitiveType.SET,
            "CHOICE", AsnPrimitiveType.CHOICE);


    // TODO MJF - how to handle Enumerated??
    /** pattern to match a ENUMERATED type definition */
    private static final Pattern PATTERN_TYPE_ENUMERATED = Pattern.compile(
            "^ENUMERATED ?\\{(.+)\\}$");




    // TODO MJF - parse class
    /** pattern to match a CLASS type definition */
    private static final Pattern PATTERN_TYPE_CLASS = Pattern.compile(
            "^CLASS ?\\{(.+)\\}$");

    // -------------------------------------------------------------------------
    // types with Constraints and Distinguished values
    // -------------------------------------------------------------------------
//    /** pattern to match an Integer type definition */
//    private static final Pattern PATTERN_TYPE_INTEGER = Pattern.compile(
//            "^INTEGER ?(\\{(.+?)\\})? ?(\\((.+?)\\))? ?(DEFAULT ?.+)?$");
//
//    /** pattern to match a BIT STRING type definition */
//    private static final Pattern PATTERN_TYPE_BIT_STRING = Pattern.compile(
//            "^BIT STRING ?(\\{(.+?)\\})? ?(DEFAULT ?\\{(.+?)\\})? ?(\\((.+?)\\))?$");

    /** pattern to match a type with distinguished values */
    private static final Pattern PATTERN_TYPE_WITH_NAMED_VALUES = Pattern.compile(
            "^(BIT STRING|INTEGER) ?(\\{(.+?)\\})? ?(DEFAULT ?\\{(.+?)\\})? ?(\\((.+?)\\))?$");

    private static final ImmutableMap<String, AsnPrimitiveType> distiguishedValuesTypes = ImmutableMap.of(
            "BIT STRING", AsnPrimitiveType.BIT_STRING,
            "INTEGER", AsnPrimitiveType.INTEGER
    );


    // -------------------------------------------------------------------------
    // types with Constraints
    // -------------------------------------------------------------------------
    /** pattern to match a type with constraints */
    private static final Pattern PATTERN_TYPE_WITH_CONSTRAINTS = Pattern.compile(
            "^(GeneralString|VisibleString|NumericString|IA5String|OCTET STRING|UTF8String|OBJECT IDENTIFIER|PrintableString|IA5String|RELATIVE-OID|BOOLEAN|NULL) ?(\\((.+)\\))?$");


    // TODO MJF - add the others
    private static final ImmutableMap<String, AsnPrimitiveType> constrainedTypes = ImmutableMap.<String, AsnPrimitiveType>builder()
            .put("BOOLEAN", AsnPrimitiveType.BOOLEAN)
            .put("IA5String", AsnPrimitiveType.IA5_STRING)
            .put("NULL", AsnPrimitiveType.NULL)
            .put("NumericString", AsnPrimitiveType.NUMERIC_STRING)
            .put("OBJECT IDENTIFIER", AsnPrimitiveType.OID)
            .put("OCTET STRING", AsnPrimitiveType.OCTET_STRING)
            .put("PrintableString", AsnPrimitiveType.PRINTABLE_STRING)
            .put("RELATIVE-OID", AsnPrimitiveType.RELATIVE_OID)
            .put("UTF8String", AsnPrimitiveType.UTF8_STRING)
            .put("VisibleString", AsnPrimitiveType.VISIBLE_STRING)
            .build();

    // -------------------------------------------------------------------------
    // types without Constraints
    // -------------------------------------------------------------------------
    /** pattern to match a GeneralizedTime type definition */
    private static final Pattern PATTERN_TYPE_GENERALIZED_TIME = Pattern.compile(
            "^GeneralizedTime$");




    // TODO ASN-25 remove this once ASN-25 is completed

    /** pattern to match a PRIMITIVE type definition */
    private static final Pattern PATTERN_TYPE_PRIMITIVE = Pattern.compile(
            "^(BIT STRING|BMPString|BOOLEAN|CHARACTER STRING|DATE|DATE_TIME|DURATION|EMBEDDED PDV|GeneralizedTime|GeneralString|GraphicString|IA5String|INTEGER|ISO646String|NULL|NumericString|OCTET STRING|OBJECT IDENTIFIER|OID-IRI|PrintableString|REAL|RELATIVE-OID|RELATIVE-OID-IRI|T61String|TeletexString|TIME|TIME-OF-DAY|UniversalString|UTCTime|UTF8String|VideotexString|VisibleString) ?(\\{(.+)\\})? ?(\\((.+)\\))?$");



    /** pattern for non-primitive type mathcing */
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
     *         the string to parse.  This should be either the Type of the defined type (i.e. the text on the right hand side of the {@code
     *         ::=}), or the Type of a Component Type
     *
     * @return an ImmutableList of {@link OLDAsnSchemaTypeDefinition} objects representing the parsed
     * type definitions
     *
     * @throws ParseException
     *         if either of the parameters are {@code null}/empty or any errors occur while parsing
     *         the type
     */
    public static AsnSchemaType parse(String value)
            throws ParseException
    {
        if (value == null || value.trim().isEmpty())
        {
            throw new ParseException("A value must be supplied for a Type", -1);
        }
        Matcher matcher;


        // TODO MJF - give that this is regex'ing each of these in order, from a performance
        // perspective we should put the checks in order of frequency of the expected data?!

        // -------------------------------------------------------------------------
        // Constructed types (SEQUENCE, Set etc)
        // -------------------------------------------------------------------------
        matcher = PATTERN_TYPE_CONSTRUCTED.matcher(value);
        if (matcher.matches())
        {
            return parseConstructed(matcher);
        }
        matcher = PATTERN_TYPE_ENUMERATED.matcher(value);
        if (matcher.matches())
        {
            return parseEnumerated(matcher);
        }



        // -------------------------------------------------------------------------
        // Collection types (SEQUENCE Of and Set Of)
        // -------------------------------------------------------------------------
        matcher = PATTERN_TYPE_COLLECTION.matcher(value);
        if (matcher.matches())
        {
            return parseCollection(matcher);
        }



        // -------------------------------------------------------------------------
        // types with Constraints and Distinguished values
        // -------------------------------------------------------------------------
        matcher = PATTERN_TYPE_WITH_NAMED_VALUES.matcher(value);
        if (matcher.matches())
        {
            return parseWithNamedValues(matcher);
        }

        // -------------------------------------------------------------------------
        // types with Constraints
        // -------------------------------------------------------------------------
        // check if defining an Object Identifier
        matcher = PATTERN_TYPE_WITH_CONSTRAINTS.matcher(value);
        if (matcher.matches())
        {
            return parseWithConstraints(matcher);
        }


        // -------------------------------------------------------------------------
        // types without Constraints (TODO MJF - is this right??? - what others don't have constraints?)
        // -------------------------------------------------------------------------
        // check if defining a GeneralizedTime
        matcher = PATTERN_TYPE_GENERALIZED_TIME.matcher(value);
        if (matcher.matches())
        {
            return parseWithoutConstraints(matcher, AsnPrimitiveType.GENERALIZED_TIME);
        }

        // catch logging for if we missed a Primitive.
        matcher = PATTERN_TYPE_PRIMITIVE.matcher(value);
        if (matcher.matches())
        {
            logger.warn("Did not parse as Primitive: " + value);
        }

        // -------------------------------------------------------------------------
        // types that are not primitives.
        // -------------------------------------------------------------------------
        matcher = PATTERN_DEFINED_TYPE.matcher(value);
        if (matcher.matches())
        {
            //return ImmutableList.<AsnSchemaTagType>of(parseWithNamedValues(name, matcher));
            return parsePlaceHolder(matcher);
        }


        // TODO MJF - for now if we are here it is most likely because we have not implemented something
        // to see where we are at I will create a placeholder

        // TODO MJF.  Should we really throw or should we try such a placeholder to do our best to keep going?!
        final String constraintText = "";
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        logger.warn("Creating last ditch tag type placeholder of type: " + value);
        return new AsnSchemaTypePlaceholder("", value, constraint);

/*
        // unknown definition
        final String error = ERROR_UNKNOWN_BUILT_IN_TYPE +  value;
        throw new ParseException(error, -1);
*/

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
     * @return an {@link com.brightsparklabs.asanti.model.schema.type.AsnSchemaType} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaType parseConstructed(Matcher matcher)
            throws ParseException
    {

        final String typeName = Strings.nullToEmpty(matcher.group(1));
        AsnPrimitiveType primitiveType = constructedTypes.get(typeName);


        final String componentTypesText = matcher.group(2);
        final String constraintText = Strings.nullToEmpty(matcher.group(3));

        final ImmutableList<AsnSchemaComponentType> componentTypes
                = AsnSchemaComponentTypeParser.parse(componentTypesText);

        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);

        return new AsnSchemaTypeConstructed(primitiveType, componentTypes, constraint);
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
    private static AsnSchemaTypeWithNamedTags parseEnumerated(Matcher matcher)
            throws ParseException
    {

        final String namedValuesText = matcher.group(1);
        logger.debug("named values {}", namedValuesText);
        final ImmutableList<AsnSchemaNamedTag> namedValues
                = AsnSchemaNamedTagParser.parseEnumeratedOptions(namedValuesText);

        return new AsnSchemaTypeWithNamedTags(namedValues, AsnSchemaConstraint.NULL, AsnPrimitiveType.ENUMERATED);
    }



    /**
     * Parses a collection type (eg SEQUENCE Of)
     *
     * @param matcher
     *         matcher which matched on a corresponding
     *
     * @return an {@link com.brightsparklabs.asanti.model.schema.type.AsnSchemaType} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaType parseCollection(Matcher matcher)
            throws ParseException
    {

        final String type = Strings.nullToEmpty(matcher.group(1));
        AsnPrimitiveType primitiveType = collectionTypes.get(type);

        final String constraintText = Strings.nullToEmpty(matcher.group(2));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);

        final String rawCollectionType = Strings.nullToEmpty(matcher.group(4));

        AsnSchemaType collectionType = parse(rawCollectionType);

        return new AsnSchemaTypeCollection(primitiveType, constraint, collectionType);
    }


    /**
     * Parses a type of the form Typename, eg "^GeneralizedTime$"
     *
     * @param matcher
     *         matcher which matched on a corresponding "generic" matcher
     *
     * @param primitiveType
     *          the {@code AsnPrimitiveType}.  In the above example this would be {@code AsnSchemaTypeGeneralizedTime}
     *
     * @return an {@link com.brightsparklabs.asanti.model.schema.type.AsnSchemaType} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaType parseWithoutConstraints(Matcher matcher,
            AsnPrimitiveType primitiveType)
            throws ParseException
    {

        return new AbstractAsnSchemaType(primitiveType, AsnSchemaConstraint.NULL);
    }

    /**
     * Parses a type of the form Typename Constraint, eg "^UTF8String ?(\\((.+)\\))?$"
     *
     * @param matcher
     *         matcher which matched on a corresponding "generic" matcher
     *
     * @return an {@link com.brightsparklabs.asanti.model.schema.type.AsnSchemaType} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaType parseWithConstraints(Matcher matcher)
            throws ParseException
    {

        AsnPrimitiveType primitiveType = constrainedTypes.get(matcher.group(1));
        if (primitiveType == null) // TODO MJF - this can go as soon as we've added all the types to the map.
        {
            // unknown definition
            final String error = ERROR_UNKNOWN_BUILT_IN_TYPE + matcher.group(0);
            throw new ParseException(error, -1);
        }

        final String constraintText = Strings.nullToEmpty(matcher.group(3));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);

        return new AbstractAsnSchemaType(primitiveType, constraint);
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
        AsnPrimitiveType primitiveType = distiguishedValuesTypes.get(matcher.group(1));
        if (primitiveType == null) // TODO MJF - make reusable
        {
            // unknown definition
            final String error = ERROR_UNKNOWN_BUILT_IN_TYPE + matcher.group(0);
            throw new ParseException(error, -1);
        }

        final String distinguishedValuesText = matcher.group(3);
        final ImmutableList<AsnSchemaNamedTag> distinguishedValues
                = AsnSchemaNamedTagParser.parseIntegerDistinguishedValues(distinguishedValuesText);
        final String constraintText = Strings.nullToEmpty(matcher.group(4));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);

        return new AsnSchemaTypeWithNamedTags(distinguishedValues, constraint, primitiveType);
    }

    /**
     * Parses a type that we don't know about
     *
     * @param matcher
     *         matcher which matched on {@link #PATTERN_DEFINED_TYPE}
     *
     * @return an {@link AsnSchemaTypePlaceholder} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaTypePlaceholder parsePlaceHolder(Matcher matcher)
            throws ParseException
    {

        //"^(((EXPLICIT)[ ]+)?(([a-zA-Z0-9\\-]+)\\.)?([a-zA-Z0-9\\-]+)) ?(\\{(.+?)\\})? ?(\\((.+?)\\))? ?(DEFAULT ?.+)?$");
        //"^((([a-zA-Z0-9\\-]+)\\.)?([a-zA-Z0-9\\-]+)) (EXPLICIT|IMPLICIT)? ?(\\{(.+?)\\})? ?(\\((.+?)\\))? ?(DEFAULT ?.+)?$");
        //"^((([a-zA-Z0-9\\-]+)\\.)?([a-zA-Z0-9\\-]+)) ?(\\{(.+?)\\})? ?(\\((.+?)\\))? ?(DEFAULT ?.+)?$");


        final String moduleName = Strings.nullToEmpty(matcher.group(5));
        final String typeName = Strings.nullToEmpty(matcher.group(6));

        final String tagMode = matcher.group(3);
        if (tagMode != null)
        {
            logger.debug("Have a defined TAG mode. {} has tag mode of {}", typeName, tagMode);
        }

        final String constraintText = Strings.nullToEmpty(matcher.group(8));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);

        logger.debug("creating placeholder - moduleName:" + moduleName + " typeName: " + typeName + " contraints: " + constraintText);
        return new AsnSchemaTypePlaceholder(moduleName, typeName, constraint);
    }
}
