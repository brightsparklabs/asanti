package com.brightsparklabs.asanti.reader.parser;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.tagtype.AsnSchemaTagType;
import com.brightsparklabs.asanti.model.schema.tagtype.AsnSchemaTagTypeInteger;
import com.brightsparklabs.asanti.model.schema.tagtype.AsnSchemaTagTypePlaceHolder;
import com.brightsparklabs.asanti.model.schema.tagtype.AsnSchemaTagTypeUtf8String;
import com.brightsparklabs.asanti.model.schema.typedefinition.*;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Logic for parsing a Type Definition from a module within an ASN.1 schema
 *
 * @author brightSPARK Labs
 */
public final class AnsSchemaTagTypeParser
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
    private static final Pattern PATTERN_TYPE_DEFINITION_BIT_STRING = Pattern.compile(
            "^BIT STRING ?(\\{(.+?)\\})? ?(DEFAULT ?\\{(.+?)\\})? ?(\\((.+?)\\))?$");

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

    /** pattern to match a GeneralizedTime type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_GENERALIZED_TIME = Pattern.compile(
            "^GeneralizedTime$");

    /** pattern to match an Integer type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_INTEGER = Pattern.compile(
            "^INTEGER ?(\\{(.+?)\\})? ?(\\((.+?)\\))? ?(DEFAULT ?.+)?$");

    // TODO ASN-25 remove this once ASN-25 is completed

    /** pattern to match a PRIMITIVE type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_PRIMITIVE = Pattern.compile(
            "^(BOOLEAN|DATE|CHARACTER STRING|DATE_TIME|DURATION|EMBEDDED PDV|EXTERNAL|INTEGER|OID-IRI|NULL|OBJECT IDENTIFIER|REAL|RELATIVE-OID-IRI|RELATIVE-OID|BMPString|GraphicString|ISO646String|PrintableString|TeletexString|T61String|UniversalString|VideotexString|TIME|TIME-OF-DAY|CHARACTER STRING) ?(\\{(.+)\\})? ?(\\((.+)\\))?$");

    /** error message if an unknown ASN.1 built-in type is found */
    private static final String ERROR_UNKNOWN_BUILT_IN_TYPE
            = "Parser expected a constructed built-in type (SEQUENCE, SET, ENUMERATED, SEQUENCE OF, SET OF, CHOICE, CLASS) or a primitive built-in type (BIT STRING, GeneralizedTime, Ia5String, INTEGER, NumericString, OCTET STRING, Utf8String, VisibleString, BOOLEAN, DATE, CHARACTER STRING, DATE_TIME, DURATION, EMBEDDED PDV, EXTERNAL, INTEGER, OID-IRI, NULL, OBJECT IDENTIFIER, REAL, RELATIVE-OID-IRI, RELATIVE-OID, BmpString, GeneralString, GraphicString, Iso646String, PrintableString, TeletexString, T61String, UniversalString, VideotexString, TIME, TIME-OF-DAY, CHARACTER STRING) but found: ";


    // -------------------------------------------------------------------------
    // CONSTANTS (AsnSchemaComponentTypeParser)
    // -------------------------------------------------------------------------

    /** pattern to break text into: tag name, tag, type, optional/default */
    private static final Pattern PATTERN_COMPONENT_TYPE = Pattern.compile(
            "([a-zA-Z0-9\\-]+) ?(\\[(\\d+)\\])? ?(.+?) ?((OPTIONAL)|(DEFAULT ([a-zA-Z0-9\\-]+)))?");

    /**
     * pattern to break the raw type string into: set/sequence of, construct constraints, type name,
     * type definition, type constraints
     */
    private static final Pattern PATTERN_RAW_TYPE = Pattern.compile(
            "(((SET)|(SEQUENCE))(( SIZE)? \\(.+?\\)\\)?)? OF )?([a-zA-Z0-9\\-\\.& ]+)(\\{.+\\})? ?(\\((.+)\\))?");

    /** pattern to determine whether the raw type is a pseudo type definition */
    private static final Pattern PATTERN_PSEUDO_TYPE = Pattern.compile(
            "(SEQUENCE *\\{|(SET|SEQUENCE)(( SIZE)? \\(.+?\\)\\)?)? OF (SET|SEQUENCE) *\\{|SET *\\{|ENUMERATED *\\{|CHOICE *\\{)(.*?)\\}");




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
    private AnsSchemaTagTypeParser()
    {
        throw new AssertionError();
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses a Component from a Type Definition in a module from an ASN.1 schema
     *
     * @param name
     *         the name of the defined type (i.e. the text on the left hand side of the {@code
     *         ::=})
     * @param value
     *         the value of the defined type (i.e. the text on the right hand side of the {@code
     *         ::=})
     *
     * @return an ImmutableList of {@link AsnSchemaTypeDefinition} objects representing the parsed
     * type definitions
     *
     * @throws ParseException
     *         if either of the parameters are {@code null}/empty or any errors occur while parsing
     *         the type
     */
   // public static ImmutableList<AsnSchemaTagType> parse(String name, String value)
    public static AsnSchemaTagType parse(String name, String value)
            throws ParseException
    {
        logger.debug("Found type definition: {} = {}", name, value);
        if (name == null || name.trim().isEmpty())
        {
            throw new ParseException("A name must be supplied for a Type Definition", -1);
        }
        if (value == null || value.trim().isEmpty())
        {
            throw new ParseException("A value must be supplied for a Type Definition", -1);
        }
        Matcher matcher;
/*
        // check if defining a SEQUENCE
        Matcher matcher = PATTERN_TYPE_DEFINITION_SEQUENCE.matcher(value);
        if (matcher.matches())
        {
            return parseSequence(name, matcher);
        }

        // check if defining a SET
        matcher = PATTERN_TYPE_DEFINITION_SET.matcher(value);
        if (matcher.matches())
        {
            return parseSet(name, matcher);
        }

        // check if defining a CHOICE
        matcher = PATTERN_TYPE_DEFINITION_CHOICE.matcher(value);
        if (matcher.matches())
        {
            return parseChoice(name, matcher);
        }

        // check if defining an ENUMERATED
        matcher = PATTERN_TYPE_DEFINITION_ENUMERATED.matcher(value);
        if (matcher.matches())
        {
            return ImmutableList.<AsnSchemaTypeDefinition>of(parseEnumerated(name, matcher));
        }

        // check if defining an OCTET STRING
        matcher = PATTERN_TYPE_DEFINITION_OCTET_STRING.matcher(value);
        if (matcher.matches())
        {
            return ImmutableList.<AsnSchemaTypeDefinition>of(parseOctetString(name, matcher));
        }

        // check if defining a BIT STRING
        matcher = PATTERN_TYPE_DEFINITION_BIT_STRING.matcher(value);
        if (matcher.matches())
        {
            return ImmutableList.<AsnSchemaTypeDefinition>of(parseBitString(name, matcher));

        }

        // check if defining an Ia5String
        matcher = PATTERN_TYPE_DEFINITION_IA5_STRING.matcher(value);
        if (matcher.matches())
        {
            return ImmutableList.<AsnSchemaTypeDefinition>of(parseIA5String(name, matcher));
        }

*/
        // check if defining a Utf8String
        matcher = PATTERN_TYPE_DEFINITION_UTF8_STRING.matcher(value);
        if (matcher.matches())
        {
            return parseUTF8String(name, matcher);
        }
/*
        // check if defining a NumericString
        matcher = PATTERN_TYPE_DEFINITION_NUMERIC_STRING.matcher(value);
        if (matcher.matches())
        {
            return ImmutableList.<AsnSchemaTypeDefinition>of(parseNumericString(name, matcher));
        }

        // check if defining a VisibleString
        matcher = PATTERN_TYPE_DEFINITION_VISIBLE_STRING.matcher(value);
        if (matcher.matches())
        {
            return ImmutableList.<AsnSchemaTypeDefinition>of(parseVisibleString(name, matcher));
        }

        // check if defining a GeneralString
        matcher = PATTERN_TYPE_DEFINITION_GENERAL_STRING.matcher(value);
        if (matcher.matches())
        {
            return ImmutableList.<AsnSchemaTypeDefinition>of(parseGeneralString(name, matcher));
        }

        // check if defining a GeneralizedTime
        matcher = PATTERN_TYPE_DEFINITION_GENERALIZED_TIME.matcher(value);
        if (matcher.matches())
        {
            return ImmutableList.<AsnSchemaTypeDefinition>of(parseGeneralizedTime(name));
        }
*/
        // check if defining a Integer
        matcher = PATTERN_TYPE_DEFINITION_INTEGER.matcher(value);
        if (matcher.matches())
        {
            //return ImmutableList.<AsnSchemaTagType>of(parseInteger(name, matcher));
            return parseInteger(name, matcher);
        }
/*

        // check if defining a SEQUENCE OF
        matcher = PATTERN_TYPE_DEFINITION_SEQUENCE_OF.matcher(value);
        if (matcher.matches())
        {
            return parseSequenceOf(name, matcher);
        }

        // check if defining a SET OF
        matcher = PATTERN_TYPE_DEFINITION_SET_OF.matcher(value);
        if (matcher.matches())
        {
            return parseSetOf(name, matcher);
        }

        // check if defining a CLASS
        matcher = PATTERN_TYPE_DEFINITION_CLASS.matcher(value);
        if (matcher.matches())
        {
            // TODO ASN-39 - handle CLASS
            logger.warn("Type Definitions for CLASS not yet supported");
            return ImmutableList.<AsnSchemaTypeDefinition>of(AsnSchemaTypeDefinition.NULL);
        }
*/
        /* TODO - MJF. What happens if this is an indirection, eg (within a Sequence):
         *    foo Bar
         * where Bar is defined somewhere else.  Surely that is legal?
         * Complicated by the fact that Bar may not have been parsed by the time we get to the line Foo ::= Bar
         */
        // So, to answer the above, if we get there then we must have a Tag that is of a non-Primitive type,
        // ie it must be of a Type Definition type.
        matcher = PATTERN_COMPONENT_TYPE.matcher(value);
        if (matcher.matches())
        {

            //return ImmutableList.<AsnSchemaTagType>of(parseInteger(name, matcher));
            return parsePlaceHolder(name, matcher);
        }


        // unknown definition
        final String error = ERROR_UNKNOWN_BUILT_IN_TYPE + name + " ::= " + value;
        throw new ParseException(error, -1);
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------



    /**
     * Parses a Utf8String type definition
     *
     * @param name
     *         name of the defined type
     * @param matcher
     *         matcher which matched on {@link #PATTERN_TYPE_DEFINITION_UTF8_STRING}
     *
     * @return an {@link AsnSchemaTypeDefinitionUtf8String} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */

    private static AsnSchemaTagTypeUtf8String parseUTF8String(String name, Matcher matcher)
            throws ParseException
    {
        final String constraintText = Strings.nullToEmpty(matcher.group(2));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        return new AsnSchemaTagTypeUtf8String(constraint);
    }


    /**
     * Parses an Integer type definition
     *
     * @param name
     *         name of the defined type
     * @param matcher
     *         matcher which matched on {@link #PATTERN_TYPE_DEFINITION_INTEGER}
     *
     * @return an {@link AsnSchemaTypeDefinitionInteger} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaTagTypeInteger parseInteger(String name, Matcher matcher)
            throws ParseException
    {
        final String distinguishedValuesText = matcher.group(2);
        final ImmutableList<AsnSchemaNamedTag> distinguishedValues
                = AsnSchemaNamedTagParser.parseIntegerDistinguishedValues(distinguishedValuesText);
        final String constraintText = Strings.nullToEmpty(matcher.group(3));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        return new AsnSchemaTagTypeInteger(distinguishedValues, constraint);
    }

    /**
     * Parses a Utf8String type definition
     *
     * @param name
     *         name of the defined type
     * @param matcher
     *         matcher which matched on {@link #PATTERN_TYPE_DEFINITION_UTF8_STRING}
     *
     * @return an {@link AsnSchemaTypeDefinitionUtf8String} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */

    private static AsnSchemaTagTypePlaceHolder parsePlaceHolder(String name, Matcher matcher)
            throws ParseException
    {
        final String typeName = Strings.nullToEmpty(matcher.group(0));
        final String b = Strings.nullToEmpty(matcher.group(1));
        final String c = Strings.nullToEmpty(matcher.group(2));
        final String d = Strings.nullToEmpty(matcher.group(3));
        final String f = Strings.nullToEmpty(matcher.group(4));
        final String h = Strings.nullToEmpty(matcher.group(5));
        final String i = Strings.nullToEmpty(matcher.group(6));

        final String constraintText = Strings.nullToEmpty(matcher.group(2));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        return new AsnSchemaTagTypePlaceHolder(typeName, constraint);

    }

}
