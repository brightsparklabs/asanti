package com.brightsparklabs.asanti.reader.parser;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.tagtype.AsnSchemaTagTypePlaceHolder;
import com.brightsparklabs.asanti.model.schema.type.*;
import com.brightsparklabs.asanti.model.schema.typedefinition.*;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /** pattern to match a SET/SEQUENCE type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_SEQUENCE = Pattern.compile(
            "^SEQUENCE ?\\{(.+)\\} ?(\\(.+\\))?$");

    /** pattern to match a SET/SEQUENCE type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_SET = Pattern.compile(
            "^SET ?\\{(.+)\\} ?(\\(.+\\))?$");

    /** pattern to match a ENUMERATED type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_ENUMERATED = Pattern.compile(
            "^ENUMERATED ?\\{(.+)\\}$");

    /** pattern to match a CHOICE type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_CHOICE = Pattern.compile(
            "^CHOICE ?\\{(.+)\\} ?(\\(.+\\))?$");

    /** pattern to match a SEQUENCE OF type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_SEQUENCE_OF = Pattern.compile(
            "^SEQUENCE(( SIZE)? \\(.+?\\)\\)?)? OF (.+)$");

    /** pattern to match a SET OF type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_SET_OF = Pattern.compile(
            "^SET(( SIZE)? \\(.+?\\)\\)?)? OF (.+)$");

    /** pattern to match a CLASS type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_CLASS = Pattern.compile(
            "^CLASS ?\\{(.+)\\}$");

    /** pattern to match an OCTET STRING type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_OCTET_STRING = Pattern.compile(
            "^OCTET STRING ?(\\((.+)\\))?$");

    /** pattern to match a BIT STRING type definition */
    /* TODO MJF - I don't understand why this is inclusive of DEFAULT?
    private static final Pattern PATTERN_TYPE_DEFINITION_BIT_STRING = Pattern.compile(
            "^BIT STRING ?(\\{(.+?)\\})? ?(DEFAULT ?\\{(.+?)\\})? ?(\\((.+?)\\))?$");
*/
    private static final Pattern PATTERN_TYPE_DEFINITION_BIT_STRING = Pattern.compile(
            "^BIT STRING ?(\\{(.+?)\\})?$");

    /** pattern to match an IA5String type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_IA5_STRING = Pattern.compile(
            "^IA5String ?(\\((.+)\\))?$");

    /** pattern to match a UTF8 type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_UTF8_STRING = Pattern.compile(
            "^UTF8String ?(\\((.+)\\))?$");

    /** pattern to match a NumericString type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_NUMERIC_STRING = Pattern.compile(
            "^NumericString ?(\\((.+)\\))?$");

    /** pattern to match a VisibleString type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_VISIBLE_STRING = Pattern.compile(
            "^VisibleString ?(\\((.+)\\))?$");

    /** pattern to match a GeneralString type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_GENERAL_STRING = Pattern.compile(
            "^GeneralString ?(\\((.+)\\))?$");

    /** pattern to match a GeneralString type definition */
    private static final Pattern PATTERN_TAG_TYPE_PRINTABLE_STRING = Pattern.compile(
            "^PrintableString ?(\\((.+)\\))?$");

    /** pattern to match a GeneralizedTime type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_GENERALIZED_TIME = Pattern.compile(
            "^GeneralizedTime$");

    /** pattern to match an Integer type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_INTEGER = Pattern.compile(
            "^INTEGER ?(\\{(.+?)\\})? ?(\\((.+?)\\))? ?(DEFAULT ?.+)?$");


    /** pattern to match an Integer type definition */
    private static final Pattern PATTERN_TAG_TYPE_OBJECT_IDENTIFIER = Pattern.compile(
            "^OBJECT IDENTIFIER ?(\\((.+)\\))?$");

    // TODO ASN-25 remove this once ASN-25 is completed

    /** pattern to match a PRIMITIVE type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_PRIMITIVE = Pattern.compile(
            "^(BOOLEAN|DATE|CHARACTER STRING|DATE_TIME|DURATION|EMBEDDED PDV|EXTERNAL|INTEGER|OID-IRI|NULL|OBJECT IDENTIFIER|REAL|RELATIVE-OID-IRI|RELATIVE-OID|BMPString|GraphicString|ISO646String|PrintableString|TeletexString|T61String|UniversalString|VideotexString|TIME|TIME-OF-DAY|CHARACTER STRING) ?(\\{(.+)\\})? ?(\\((.+)\\))?$");


    /** pattern for non-primitive type mathcing */
    private static final Pattern PATTERN_DEFINED_TYPE = Pattern.compile(
            "^((([a-zA-Z0-9\\-]+)\\.)?([a-zA-Z0-9\\-]+)) ?(\\{(.+?)\\})? ?(\\((.+?)\\))? ?(DEFAULT ?.+)?$");



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
/*
        // check if defining a SEQUENCE
        Matcher matcher = PATTERN_TYPE_DEFINITION_SEQUENCE.matcher(value);
        if (matcher.matches())
        {
            return parseSequence(name, matcher);
        }
*/

        // check if defining a Utf8String
        matcher = PATTERN_TYPE_DEFINITION_UTF8_STRING.matcher(value);
        if (matcher.matches())
        {
            return parseGeneric(matcher, AsnPrimitiveType.Utf8String);
        }
        // check if defining a PrintableString
        matcher = PATTERN_TAG_TYPE_PRINTABLE_STRING.matcher(value);
        if (matcher.matches())
        {
            return parseGeneric(matcher, AsnPrimitiveType.PrintableString);
        }
        // check if defining an OctetString
        matcher = PATTERN_TYPE_DEFINITION_OCTET_STRING.matcher(value);
        if (matcher.matches())
        {
            return parseGeneric(matcher, AsnPrimitiveType.OctetString);
        }
        // check if defining a BIT String
        matcher = PATTERN_TYPE_DEFINITION_BIT_STRING.matcher(value);
        if (matcher.matches())
        {
            return parseGeneric(matcher, AsnPrimitiveType.BitString);
        }



        // check if defining an Object Identifier
        matcher = PATTERN_TAG_TYPE_OBJECT_IDENTIFIER.matcher(value);
        if (matcher.matches())
        {
            return parseGeneric(matcher, AsnPrimitiveType.Oid);
        }



        // check if defining a Integer
        matcher = PATTERN_TYPE_DEFINITION_INTEGER.matcher(value);
        if (matcher.matches())
        {
            return parseInteger(matcher);
        }

        matcher = PATTERN_DEFINED_TYPE.matcher(value);
        if (matcher.matches())
        {
            //return ImmutableList.<AsnSchemaTagType>of(parseInteger(name, matcher));
            return parsePlaceHolder(matcher);
        }

        // unknown definition
        final String error = ERROR_UNKNOWN_BUILT_IN_TYPE +  value;
        throw new ParseException(error, -1);


    }


    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------


    /**
     * Parses a type of the form Typename Constraint, eg "^UTF8String ?(\\((.+)\\))?$"
     *
     * @param matcher
     *         matcher which matched on a corresponding "generic" matcher
     *
     * @param primitiveType
     *          the {@code AsnPrimitiveType}.  In the above example this would be {@code AsnPrimitiveUtf8String}
     *
     * @return an {@link com.brightsparklabs.asanti.model.schema.type.AsnSchemaType} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaType parseGeneric(Matcher matcher, AsnPrimitiveType primitiveType)
            throws ParseException
    {
        final String constraintText = Strings.nullToEmpty(matcher.group(2));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);

        return new AbstractAsnSchemaType(primitiveType, constraint);
    }

    /**
     * Parses an Integer type definition
     *
     * @param matcher
     *         matcher which matched on {@link #PATTERN_TYPE_DEFINITION_INTEGER}
     *
     * @return an {@link OLDAsnSchemaTypeDefinitionInteger} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaTypeInteger parseInteger(Matcher matcher)
            throws ParseException
    {
        final String distinguishedValuesText = matcher.group(2);
        final ImmutableList<AsnSchemaNamedTag> distinguishedValues
                = AsnSchemaNamedTagParser.parseIntegerDistinguishedValues(distinguishedValuesText);
        final String constraintText = Strings.nullToEmpty(matcher.group(3));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);

        return new AsnSchemaTypeInteger(distinguishedValues, constraint);
    }

    /**
     * Parses a type that we don't know about
     *
     * @param name
     *         name of the defined type
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
        final String moduleName = Strings.nullToEmpty(matcher.group(3));
        final String typeName = Strings.nullToEmpty(matcher.group(4));

        final String constraintText = Strings.nullToEmpty(matcher.group(6));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);

        logger.debug("creating placeholder - moduleName:" + moduleName + " typeName: " + typeName + " contraints: " + constraintText);
        return new AsnSchemaTypePlaceholder(moduleName, typeName, constraint);
    }


}
