package com.brightsparklabs.asanti.reader.parser;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.tagtype.*;
import com.brightsparklabs.asanti.model.schema.typedefinition.*;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.*;

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

    // TODO - All these Regex are copied from elsewhere.  They need to be TESTED
    // to work with the way the input parameters are going to be passed.


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
    private static final Pattern PATTERN_TAG_TYPE_SEQUENCE_OF = Pattern.compile(
            "^SEQUENCE(( SIZE)? \\(.+?\\)\\)?)? OF (.+)$");

    /** pattern to match a SET OF type definition */
    private static final Pattern PATTERN_TAG_TYPE_SET_OF = Pattern.compile(
            "^SET(( SIZE)? \\(.+?\\)\\)?)? OF (.+)$");

    /** pattern to match a CLASS type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_CLASS = Pattern.compile(
            "^CLASS ?\\{(.+)\\}$");

    /** pattern to match an OCTET STRING type definition */
    private static final Pattern PATTERN_TAG_TYPE_OCTET_STRING = Pattern.compile(
            "^OCTET STRING ?(\\((.+)\\))?$");

    /** pattern to match a BIT STRING type definition */
    private static final Pattern PATTERN_TAG_TYPE_BIT_STRING = Pattern.compile(
            "^BIT STRING ?(\\{(.+?)\\})? ?(DEFAULT ?\\{(.+?)\\})? ?(\\((.+?)\\))?$");

    /** pattern to match an IA5String type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_IA5_STRING = Pattern.compile(
            "^IA5String ?(\\((.+)\\))?$");

    /** pattern to match a UTF8 type definition */
    private static final Pattern PATTERN_TAG_TYPE_UTF8_STRING = Pattern.compile(
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

    /** pattern to match a PrintableString type definition */
    private static final Pattern PATTERN_TAG_TYPE_PRINTABLE_STRING = Pattern.compile(
            "^PrintableString ?(\\((.+)\\))?$");

    /** pattern to match a GeneralizedTime type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_GENERALIZED_TIME = Pattern.compile(
            "^GeneralizedTime$");

    /** pattern to match an Integer type definition */
    private static final Pattern PATTERN_TAG_TYPE_INTEGER = Pattern.compile(
            "^INTEGER ?(\\{(.+?)\\})? ?(\\((.+?)\\))? ?(DEFAULT ?.+)?$");

    /** pattern to match an Integer type definition */
    private static final Pattern PATTERN_TAG_TYPE_OBJECT_IDENTIFIER = Pattern.compile(
            "^OBJECT IDENTIFIER ?(\\((.+)\\))?$");


    /** pattern to match an Integer type definition */
    private static final Pattern PATTERN_TAG_TYPE_NULL = Pattern.compile(
            "^NULL$");


    // TODO ASN-25 remove this once ASN-25 is completed

    /** pattern to match a PRIMITIVE type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_PRIMITIVE = Pattern.compile(
            "^(BOOLEAN|DATE|CHARACTER STRING|DATE_TIME|DURATION|EMBEDDED PDV|EXTERNAL|INTEGER|OID-IRI|NULL|OBJECT IDENTIFIER|REAL|RELATIVE-OID-IRI|RELATIVE-OID|BMPString|GraphicString|ISO646String|PrintableString|TeletexString|T61String|UniversalString|VideotexString|TIME|TIME-OF-DAY|CHARACTER STRING|BIT STRING) ?(\\{(.+)\\})? ?(\\((.+)\\))?$");

    /** error message if an unknown ASN.1 built-in type is found */
    private static final String ERROR_UNKNOWN_BUILT_IN_TYPE
            = "Parser expected a constructed built-in type (SEQUENCE, SET, ENUMERATED, SEQUENCE OF, SET OF, CHOICE, CLASS) or a primitive built-in type (BIT STRING, GeneralizedTime, Ia5String, INTEGER, NumericString, OCTET STRING, Utf8String, VisibleString, BOOLEAN, DATE, CHARACTER STRING, DATE_TIME, DURATION, EMBEDDED PDV, EXTERNAL, INTEGER, OID-IRI, NULL, OBJECT IDENTIFIER, REAL, RELATIVE-OID-IRI, RELATIVE-OID, BmpString, GeneralString, GraphicString, Iso646String, PrintableString, TeletexString, T61String, UniversalString, VideotexString, TIME, TIME-OF-DAY, CHARACTER STRING) but found: ";


    // -------------------------------------------------------------------------
    // CONSTANTS (AsnSchemaComponentTypeParser)
    // -------------------------------------------------------------------------

    /** pattern to break text into: tag name, tag, type, optional/default */
    private static final Pattern PATTERN_COMPONENT_TYPE = Pattern.compile(
            "([a-zA-Z0-9\\-]+) ?(\\[(\\d+)\\])? ?(.+?) ?((OPTIONAL)|(DEFAULT ([a-zA-Z0-9\\-]+)))?");


    /** pattern to break text into: type, optional/default */
/*
    private static final Pattern PATTERN_DEFINED_TYPE = Pattern.compile(
            "^([a-zA-Z0-9\\-]+)(.([a-zA-Z0-9\\-]+))? ?(\\{(.+?)\\})? ?(\\((.+?)\\))? ?(DEFAULT ?.+)?$");
*/
    private static final Pattern PATTERN_DEFINED_TYPE = Pattern.compile(
            "^((([a-zA-Z0-9\\-]+)\\.)?([a-zA-Z0-9\\-]+)) ?(\\{(.+?)\\})? ?(\\((.+?)\\))? ?(DEFAULT ?.+)?$");

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
     * @return an ImmutableList of {@link OLDAsnSchemaTypeDefinition} objects representing the parsed
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

        checkNotNull(name);
        checkArgument(!name.trim().isEmpty(),
                "name must be specified");
        checkNotNull(value);
        checkArgument(!value.trim().isEmpty(),
                "value must be specified");

        logger.debug("Found tag type: {} = {}", name, value);
        Matcher matcher;
/* TODO MJF - these 4 are Constructed types, so in theory we should never get to this function with these types?
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
            return ImmutableList.<OLDAsnSchemaTypeDefinition>of(parseEnumerated(name, matcher));
        }
*/
        // check if defining an OCTET STRING
        matcher = PATTERN_TAG_TYPE_OCTET_STRING.matcher(value);
        if (matcher.matches())
        {
            return parseOctetString(name, matcher);
        }

        // check if defining a BIT STRING
        matcher = PATTERN_TAG_TYPE_BIT_STRING.matcher(value);
        if (matcher.matches())
        {
            return parseBitString(name, matcher);

        }
/* TODO MJF - implement
        // check if defining an Ia5String
        matcher = PATTERN_TYPE_DEFINITION_IA5_STRING.matcher(value);
        if (matcher.matches())
        {
            return ImmutableList.<OLDAsnSchemaTypeDefinition>of(parseIA5String(name, matcher));
        }

*/
        // check if defining a Utf8String
        matcher = PATTERN_TAG_TYPE_UTF8_STRING.matcher(value);
        if (matcher.matches())
        {
            return parseUTF8String(name, matcher);
        }
/* TODO MJF - implement
        // check if defining a NumericString
        matcher = PATTERN_TYPE_DEFINITION_NUMERIC_STRING.matcher(value);
        if (matcher.matches())
        {
            return ImmutableList.<OLDAsnSchemaTypeDefinition>of(parseNumericString(name, matcher));
        }

        // check if defining a VisibleString
        matcher = PATTERN_TYPE_DEFINITION_VISIBLE_STRING.matcher(value);
        if (matcher.matches())
        {
            return ImmutableList.<OLDAsnSchemaTypeDefinition>of(parseVisibleString(name, matcher));
        }

        // check if defining a GeneralString
        matcher = PATTERN_TYPE_DEFINITION_GENERAL_STRING.matcher(value);
        if (matcher.matches())
        {
            return ImmutableList.<OLDAsnSchemaTypeDefinition>of(parseGeneralString(name, matcher));
        }
*/
        // check if defining a PrintableString
        matcher = PATTERN_TAG_TYPE_PRINTABLE_STRING.matcher(value);
        if (matcher.matches())
        {
            return parsePrintableString(name, matcher);
        }


        // check if defining a GeneralizedTime
        matcher = PATTERN_TYPE_DEFINITION_GENERALIZED_TIME.matcher(value);
        if (matcher.matches())
        {
            return parseGeneralizedTime(name);
        }

        // check if defining a Integer
        matcher = PATTERN_TAG_TYPE_INTEGER.matcher(value);
        if (matcher.matches())
        {
            //return ImmutableList.<AsnSchemaTagType>of(parseInteger(name, matcher));
            return parseInteger(name, matcher);
        }

        // check if defining a SEQUENCE OF
        matcher = PATTERN_TAG_TYPE_SEQUENCE_OF.matcher(value);
        if (matcher.matches())
        {
            return parseSequenceOf(name, matcher);
        }

        // check if defining a SET OF
        matcher = PATTERN_TAG_TYPE_SET_OF.matcher(value);
        if (matcher.matches())
        {
            return parseSetOf(name, matcher);
        }
/*
        // check if defining a CLASS
        matcher = PATTERN_TYPE_DEFINITION_CLASS.matcher(value);
        if (matcher.matches())
        {
            // TODO ASN-39 - handle CLASS
            logger.warn("Type Definitions for CLASS not yet supported");
            return ImmutableList.<OLDAsnSchemaTypeDefinition>of(OLDAsnSchemaTypeDefinition.NULL);
        }
*/


        matcher = PATTERN_TAG_TYPE_OBJECT_IDENTIFIER.matcher(value);
        if (matcher.matches())
        {
            return parseObjectIdentifier(name, matcher);
        }

        matcher = PATTERN_TAG_TYPE_NULL.matcher(value);
        if (matcher.matches())
        {
            return AsnSchemaTagType.NULL;
        }


        matcher = PATTERN_TYPE_DEFINITION_PRIMITIVE.matcher(value);
        if (matcher.matches())
        {
            logger.debug("Did not otherwise parse Primitive: " + value);
        }

        /* TODO - MJF. What happens if this is an indirection, eg (within a Sequence):
         *    foo Bar
         * where Bar is defined somewhere else.  Surely that is legal?
         * Complicated by the fact that Bar may not have been parsed by the time we get to the line Foo ::= Bar
         */
        // So, to answer the above, if we get there then we must have a Tag that is of a non-Primitive type,
        // ie it must be of a Type Definition type.
        matcher = PATTERN_DEFINED_TYPE.matcher(value);
        if (matcher.matches())
        {
            //return ImmutableList.<AsnSchemaTagType>of(parseInteger(name, matcher));
            return parsePlaceHolder(name, matcher);
        }

        // TODO MJF - for now if we are here it is most likely because we have not implemented something
        // to see where we are at I will create a placeHolder
        final String constraintText = "";
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        logger.warn("Creating last ditch tag type placeholder for: " + name + " of type: " + value);
        return new AsnSchemaTagTypePlaceHolder("", value, constraint);


/*
        // unknown definition
        final String error = ERROR_UNKNOWN_BUILT_IN_TYPE + name + " ::= " + value;
        throw new ParseException(error, -1);
*/
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses an OCTET STRING type definition
     *
     * @param name
     *         name of the defined type
     * @param matcher
     *         matcher which matched on {@link #PATTERN_TAG_TYPE_OCTET_STRING}
     *
     * @return an {@link AsnSchemaTagTypeOctetString} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaTagTypeOctetString parseOctetString(String name, Matcher matcher)
            throws ParseException
    {
        final String constraintText = Strings.nullToEmpty(matcher.group(2));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        return new AsnSchemaTagTypeOctetString(constraint);
    }

    /**
     * Parses an BIT STRING type definition
     *
     * @param name
     *         name of the defined type
     * @param matcher
     *         matcher which matched on {@link #PATTERN_TAG_TYPE_BIT_STRING}
     *
     * @return an {@link AsnSchemaTagTypeBitString} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaTagTypeBitString parseBitString(String name, Matcher matcher)
            throws ParseException
    {
        final String constraintText = Strings.nullToEmpty(matcher.group(2));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        return new AsnSchemaTagTypeBitString(constraint);
    }

    /**
     * Parses a Utf8String type definition
     *
     * @param name
     *         name of the defined type
     * @param matcher
     *         matcher which matched on {@link #PATTERN_TAG_TYPE_UTF8_STRING}
     *
     * @return an {@link AsnSchemaTagTypeUtf8String} representing the parsed data
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
     * Parses a PrintableString type definition
     *
     * @param name
     *         name of the defined type
     * @param matcher
     *         matcher which matched on {@link #PATTERN_TAG_TYPE_PRINTABLE_STRING}
     *
     * @return an {@link AsnSchemaTagTypePrintableString} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */

    private static AsnSchemaTagTypePrintableString parsePrintableString(String name, Matcher matcher)
            throws ParseException
    {
        final String constraintText = Strings.nullToEmpty(matcher.group(2));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        return new AsnSchemaTagTypePrintableString(constraint);
    }

    /**
     * Parses a GeneralizedTime tag type
     *
     * @param name
     *         name of the defined type
     *
     * @return an {@link AsnSchemaTagTypeGeneralizedTime} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaTagTypeGeneralizedTime parseGeneralizedTime(String name)
            throws ParseException
    {
        // sub-type constraints are not applicable to GeneralizedTime.
        final AsnSchemaConstraint constraint = AsnSchemaConstraint.NULL;
        return new AsnSchemaTagTypeGeneralizedTime(constraint);
    }

    /**
     * Parses an Integer type definition
     *
     * @param name
     *         name of the defined type
     * @param matcher
     *         matcher which matched on {@link #PATTERN_TAG_TYPE_INTEGER}
     *
     * @return an {@link AsnSchemaTagTypeInteger} representing the parsed data
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
     * Parses a Sequence Of tag type
     *
     * @param name
     *         name of the defined type
     * @param matcher
     *         matcher which matched on {@link #PATTERN_TAG_TYPE_SEQUENCE_OF}
     *
     * @return an {@link AsnSchemaTagTypeSequenceOf} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaTagTypeSequenceOf parseSequenceOf(String name, Matcher matcher)
            throws ParseException
    {

        final String constraintText = Strings.nullToEmpty(matcher.group(1));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);

        final String sequenceType = Strings.nullToEmpty(matcher.group(3));
        // TODO MJF - at some point we have to deal with what Type it is a set of.  Where do we capture that???

        return new AsnSchemaTagTypeSequenceOf(constraint);
    }

    /**
     * Parses a Set Of tag type
     *
     * @param name
     *         name of the defined type
     * @param matcher
     *         matcher which matched on {@link #PATTERN_TAG_TYPE_SET_OF}
     *
     * @return an {@link AsnSchemaTagTypeSequenceOf} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaTagTypeSetOf parseSetOf(String name, Matcher matcher)
            throws ParseException
    {
        final String constraintText = Strings.nullToEmpty(matcher.group(1));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);

        final String setType = Strings.nullToEmpty(matcher.group(3));
        // TODO MJF - at some point we have to deal with what Type it is a set of.  Where do we capture that???

        return new AsnSchemaTagTypeSetOf(constraint);
    }


    /**
     * Parses a Set Of tag type
     *
     * @param name
     *         name of the defined type
     * @param matcher
     *         matcher which matched on {@link #PATTERN_TAG_TYPE_SET_OF}
     *
     * @return an {@link AsnSchemaTagTypeSequenceOf} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaTagTypeObjectIdentifier parseObjectIdentifier(String name, Matcher matcher)
            throws ParseException
    {
        final String constraintText = Strings.nullToEmpty(matcher.group(1));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);

        return new AsnSchemaTagTypeObjectIdentifier(constraint);
    }


    /**
     * Parses a type that we don't know about
     *
     * @param name
     *         name of the defined type
     * @param matcher
     *         matcher which matched on {@link #PATTERN_DEFINED_TYPE}
     *
     * @return an {@link AsnSchemaTagTypePlaceHolder} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */

    private static AsnSchemaTagTypePlaceHolder parsePlaceHolder(String name, Matcher matcher)
            throws ParseException
    {
        final String moduleName = Strings.nullToEmpty(matcher.group(3));
        final String typeName = Strings.nullToEmpty(matcher.group(4));

        final String constraintText = Strings.nullToEmpty(matcher.group(6));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);

        logger.debug("creating placeholder - moduleName:" + moduleName + " typeName: " + typeName + " contraints: " + constraintText);
        return new AsnSchemaTagTypePlaceHolder(moduleName, typeName, constraint);

    }

}
