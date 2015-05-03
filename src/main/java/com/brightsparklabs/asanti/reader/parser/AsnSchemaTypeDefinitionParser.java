/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader.parser;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
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
public final class AsnSchemaTypeDefinitionParser
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
    private AsnSchemaTypeDefinitionParser()
    {
        throw new AssertionError();
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses a type definition from a module from an ASN.1 schema
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
    public static ImmutableList<AsnSchemaTypeDefinition> parse(String name, String value)
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

        // check if defining a Utf8String
        matcher = PATTERN_TYPE_DEFINITION_UTF8_STRING.matcher(value);
        if (matcher.matches())
        {
            return ImmutableList.<AsnSchemaTypeDefinition>of(parseUTF8String(name, matcher));
        }

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

        // check if defining a Integer
        matcher = PATTERN_TYPE_DEFINITION_INTEGER.matcher(value);
        if (matcher.matches())
        {
            return ImmutableList.<AsnSchemaTypeDefinition>of(parseInteger(name, matcher));
        }

        // check if defining a PRIMITIVE
        matcher = PATTERN_TYPE_DEFINITION_PRIMITIVE.matcher(value);
        if (matcher.matches())
        {
            /*
             * TODO ASN-25 - handle all primitive types. Currently this is just
             * a catch-all to log warnings
             */
            final String builtinType = matcher.group(1);
            final String error = String.format(
                    "Cannot parse unsupported ASN.1 built-in type: %s for type: %s",
                    builtinType,
                    name);
            logger.warn(error);
            return ImmutableList.<AsnSchemaTypeDefinition>of(AsnSchemaTypeDefinition.NULL);
        }

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

        // unknown definition
        final String error = ERROR_UNKNOWN_BUILT_IN_TYPE + name + " ::= " + value;
        throw new ParseException(error, -1);
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses a SEQUENCE type definition
     *
     * @param name
     *         name of the defined type
     * @param matcher
     *         matcher which matched on {@link #PATTERN_TYPE_DEFINITION_SEQUENCE}
     *
     * @return an ImmutableList of {@link AsnSchemaTypeDefinition} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static ImmutableList<AsnSchemaTypeDefinition> parseSequence(String name,
            Matcher matcher) throws ParseException
    {
        final String componentTypesText = matcher.group(1);
        final String constraintText = Strings.nullToEmpty(matcher.group(2));

        final ImmutableList<AsnSchemaComponentType> componentTypes
                = AsnSchemaComponentTypeParser.parse(name, componentTypesText);

        // parse any pseudo type definitions from returned component types
        final List<AsnSchemaTypeDefinition> parsedTypes = parsePseudoTypes(componentTypes);

        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        final AsnSchemaTypeDefinitionSequence typeDefinition = new AsnSchemaTypeDefinitionSequence(
                name,
                componentTypes,
                constraint);
        parsedTypes.add(typeDefinition);

        return ImmutableList.copyOf(parsedTypes);
    }

    /**
     * Parses a SET type definition
     *
     * @param name
     *         name of the defined type
     * @param matcher
     *         matcher which matched on {@link #PATTERN_TYPE_DEFINITION_SET}
     *
     * @return an ImmutableList of {@link AsnSchemaTypeDefinition} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static ImmutableList<AsnSchemaTypeDefinition> parseSet(String name, Matcher matcher)
            throws ParseException
    {
        final String componentTypesText = matcher.group(1);
        final String constraintText = Strings.nullToEmpty(matcher.group(2));

        final ImmutableList<AsnSchemaComponentType> componentTypes
                = AsnSchemaComponentTypeParser.parse(name, componentTypesText);

        // parse any pseudo type definitions from returned component types
        final List<AsnSchemaTypeDefinition> parsedTypes = parsePseudoTypes(componentTypes);

        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        final AsnSchemaTypeDefinitionSet typeDefinition = new AsnSchemaTypeDefinitionSet(name,
                componentTypes,
                constraint);
        parsedTypes.add(typeDefinition);

        return ImmutableList.copyOf(parsedTypes);
    }

    /**
     * Parses a CHOICE type definition
     *
     * @param name
     *         name of the defined type
     * @param matcher
     *         matcher which matched on {@link #PATTERN_TYPE_DEFINITION_CHOICE}
     *
     * @return an {@link AsnSchemaTypeDefinitionChoice} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static ImmutableList<AsnSchemaTypeDefinition> parseChoice(String name, Matcher matcher)
            throws ParseException
    {
        final String componentTypesText = matcher.group(1);
        final String constraintText = Strings.nullToEmpty(matcher.group(2));

        final ImmutableList<AsnSchemaComponentType> componentTypes
                = AsnSchemaComponentTypeParser.parse(name, componentTypesText);

        // parse any pseudo type definitions from returned component types
        final List<AsnSchemaTypeDefinition> parsedTypes = parsePseudoTypes(componentTypes);

        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        final AsnSchemaTypeDefinitionChoice typeDefinition = new AsnSchemaTypeDefinitionChoice(name,
                componentTypes,
                constraint);
        parsedTypes.add(typeDefinition);

        return ImmutableList.copyOf(parsedTypes);
    }

    /**
     * Parses an ENUMERATED type definition
     *
     * @param name
     *         name of the defined type
     * @param matcher
     *         matcher which matched on {@link #PATTERN_TYPE_DEFINITION_ENUMERATED}
     *
     * @return an {@link AsnSchemaTypeDefinitionEnumerated} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaTypeDefinitionEnumerated parseEnumerated(String name, Matcher matcher)
            throws ParseException
    {
        final String enumeratedOptionsText = matcher.group(1);
        final ImmutableList<AsnSchemaNamedTag> enumeratedOptions
                = AsnSchemaNamedTagParser.parseEnumeratedOptions(enumeratedOptionsText);
        return new AsnSchemaTypeDefinitionEnumerated(name, enumeratedOptions);
    }

    /**
     * Parses an OCTET STRING type definition
     *
     * @param name
     *         name of the defined type
     * @param matcher
     *         matcher which matched on {@link #PATTERN_TYPE_DEFINITION_OCTET_STRING}
     *
     * @return an {@link AsnSchemaTypeDefinitionOctetString} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaTypeDefinitionOctetString parseOctetString(String name, Matcher matcher)
            throws ParseException
    {
        final String constraintText = Strings.nullToEmpty(matcher.group(2));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        return new AsnSchemaTypeDefinitionOctetString(name, constraint);
    }

    /**
     * Parses a BIT STRING type definition
     *
     * @param name
     *         name of the defined type
     * @param matcher
     *         matcher which matched on {@link #PATTERN_TYPE_DEFINITION_BIT_STRING}
     *
     * @return an {@link AsnSchemaTypeDefinitionBitString} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaTypeDefinitionBitString parseBitString(String name, Matcher matcher)
            throws ParseException
    {
        // TODO ASN-87 - parse list of named bits
        final String constraintText = Strings.nullToEmpty(matcher.group(6));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        return new AsnSchemaTypeDefinitionBitString(name, constraint);
    }

    /**
     * Parses an Ia5String type definition
     *
     * @param name
     *         name of the defined type
     * @param matcher
     *         matcher which matched on {@link #PATTERN_TYPE_DEFINITION_IA5_STRING}
     *
     * @return an {@link AsnSchemaTypeDefinitionIa5String} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaTypeDefinitionIa5String parseIA5String(String name, Matcher matcher)
            throws ParseException
    {
        final String constraintText = Strings.nullToEmpty(matcher.group(2));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        return new AsnSchemaTypeDefinitionIa5String(name, constraint);
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
    private static AsnSchemaTypeDefinitionUtf8String parseUTF8String(String name, Matcher matcher)
            throws ParseException
    {
        final String constraintText = Strings.nullToEmpty(matcher.group(2));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        return new AsnSchemaTypeDefinitionUtf8String(name, constraint);
    }

    /**
     * Parses a NumericString type definition
     *
     * @param name
     *         name of the defined type
     * @param matcher
     *         matcher which matched on {@link #PATTERN_TYPE_DEFINITION_NUMERIC_STRING}
     *
     * @return an {@link AsnSchemaTypeDefinitionNumericString} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaTypeDefinitionNumericString parseNumericString(String name,
            Matcher matcher) throws ParseException
    {
        final String constraintText = Strings.nullToEmpty(matcher.group(2));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        return new AsnSchemaTypeDefinitionNumericString(name, constraint);
    }

    /**
     * Parses a VisibleString type definition
     *
     * @param name
     *         name of the defined type
     * @param matcher
     *         matcher which matched on {@link #PATTERN_TYPE_DEFINITION_VISIBLE_STRING}
     *
     * @return an {@link AsnSchemaTypeDefinitionVisibleString} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaTypeDefinitionVisibleString parseVisibleString(String name,
            Matcher matcher) throws ParseException
    {
        final String constraintText = Strings.nullToEmpty(matcher.group(2));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        return new AsnSchemaTypeDefinitionVisibleString(name, constraint);
    }

    /**
     * Parses a General type definition
     *
     * @param name
     *         name of the defined type
     * @param matcher
     *         matcher which matched on {@link #PATTERN_TYPE_DEFINITION_GENERAL_STRING}
     *
     * @return an {@link AsnSchemaTypeDefinitionGeneralString} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaTypeDefinitionGeneralString parseGeneralString(String name,
            Matcher matcher) throws ParseException
    {
        final String constraintText = Strings.nullToEmpty(matcher.group(2));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        return new AsnSchemaTypeDefinitionGeneralString(name, constraint);
    }

    /**
     * Parses a GeneralizedTime type definition
     *
     * @param name
     *         name of the defined type
     *
     * @return an {@link AsnSchemaTypeDefinitionGeneralizedTime} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static AsnSchemaTypeDefinitionGeneralizedTime parseGeneralizedTime(String name)
            throws ParseException
    {
        // sub-type constraints are not applicable to GeneralizedTime.
        final AsnSchemaConstraint constraint = AsnSchemaConstraint.NULL;
        return new AsnSchemaTypeDefinitionGeneralizedTime(name, constraint);
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
    private static AsnSchemaTypeDefinitionInteger parseInteger(String name, Matcher matcher)
            throws ParseException
    {
        final String distinguishedValuesText = matcher.group(2);
        final ImmutableList<AsnSchemaNamedTag> distinguishedValues
                = AsnSchemaNamedTagParser.parseIntegerDistinguishedValues(distinguishedValuesText);
        final String constraintText = Strings.nullToEmpty(matcher.group(3));
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        return new AsnSchemaTypeDefinitionInteger(name, distinguishedValues, constraint);
    }

    /**
     * Parses a SEQUENCE OF type definition
     *
     * @param name
     *         name of the defined type
     * @param matcher
     *         matcher which matched on {@link #PATTERN_TYPE_DEFINITION_SEQUENCE_OF}
     *
     * @return an ImmutableList of {@link AsnSchemaTypeDefinition} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static ImmutableList<AsnSchemaTypeDefinition> parseSequenceOf(String name,
            Matcher matcher) throws ParseException
    {
        final String constraintText = Strings.nullToEmpty(matcher.group(1));
        String elementTypeName = matcher.group(3);

        final List<AsnSchemaTypeDefinition> typeDefinitions = Lists.newArrayList();

        // check if this is a SEQUENCE OF SEQUENCE/SET and create an explicit type definition
        // for the inner SEQUENCE or SET
        if (elementTypeName.startsWith("SEQUENCE") || elementTypeName.startsWith("SET"))
        {
            elementTypeName = parseInnerSetOrSequence(typeDefinitions, elementTypeName, name);
        }

        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        typeDefinitions.add(new AsnSchemaTypeDefinitionSequenceOf(name,
                elementTypeName,
                constraint));

        return ImmutableList.copyOf(typeDefinitions);
    }

    /**
     * Parses a SET OF type definition
     *
     * @param name
     *         name of the defined type
     * @param matcher
     *         matcher which matched on {@link #PATTERN_TYPE_DEFINITION_SET_OF}
     *
     * @return an ImmutableList of {@link AsnSchemaTypeDefinition} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static ImmutableList<AsnSchemaTypeDefinition> parseSetOf(String name, Matcher matcher)
            throws ParseException
    {
        final String constraintText = Strings.nullToEmpty(matcher.group(1));
        String elementTypeName = matcher.group(3);

        final List<AsnSchemaTypeDefinition> typeDefinitions = Lists.newArrayList();

        // check if this is a SET OF SEQUENCE/SET and create an explicit type definition
        // for the inner SEQUENCE or SET
        if (elementTypeName.startsWith("SEQUENCE") || elementTypeName.startsWith("SET"))
        {
            elementTypeName = parseInnerSetOrSequence(typeDefinitions, elementTypeName, name);
        }

        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        typeDefinitions.add(new AsnSchemaTypeDefinitionSetOf(name, elementTypeName, constraint));

        return ImmutableList.copyOf(typeDefinitions);
    }

    /**
     * Parses pseudo type definitions found in the supplied list of {@link AsnSchemaComponentType}.
     *
     * <p>Refer to {@code /docs/design/schema_parsing.md} for details of the design.
     *
     * @param componentTypes
     *         list of component types to parse
     *
     * @return an ImmutableList of {@link AsnSchemaTypeDefinition} representing the parsed pseudo
     * type definitions
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static List<AsnSchemaTypeDefinition> parsePseudoTypes(
            Iterable<AsnSchemaComponentType> componentTypes) throws ParseException
    {
        final List<AsnSchemaTypeDefinition> parsedTypes = Lists.newArrayList();

        for (final AsnSchemaComponentType component : componentTypes)
        {
            if (component instanceof AsnSchemaComponentTypeGenerated)
            {
                final AsnSchemaComponentTypeGenerated componentGenerated
                        = (AsnSchemaComponentTypeGenerated) component;
                final String pseudoTypeDefinitionText = componentGenerated.getTypeDefinitionText();
                final ImmutableList<AsnSchemaTypeDefinition> pseudoTypeDefinitions = parse(component
                        .getTypeName(), pseudoTypeDefinitionText);
                parsedTypes.addAll(pseudoTypeDefinitions);
            }
        }
        return parsedTypes;
    }

    /**
     * Parses the inner SEQUENCE or SET within a SEQUENCE/SET OF type definition
     *
     * <p>Refer to {@code /docs/design/schema_parsing.md} for details of the design.
     *
     * @param types
     *         list of {@link AsnSchemaTypeDefinition} to to which parsed type definitions will be
     *         added to
     * @param typeDefinitionText
     *         the type definition text of the SEQUENCE or SET
     * @param containingTypeName
     *         the name of the containing type definition
     *
     * @return the generated name of the inner SEQUENCE or SET type definition
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    private static String parseInnerSetOrSequence(List<AsnSchemaTypeDefinition> types,
            String typeDefinitionText, String containingTypeName) throws ParseException
    {
        final String typeName;

        if (!containingTypeName.startsWith("generated"))
        {
            // the SEQUENCE/SET OF type is an explicit type definition, auto-generate the pseudo
            // SEQUENCE type in the format generated.<containingTypeName>
            typeName = "generated." + containingTypeName;
        }
        else
        {
            // the SEQUENCE/SET OF type is a defined within a Pseudo Type definition, use the
            // already generated Pseudo Type name
            typeName = containingTypeName;
        }

        types.addAll(parse(typeName, typeDefinitionText));
        return typeName;
    }
}
