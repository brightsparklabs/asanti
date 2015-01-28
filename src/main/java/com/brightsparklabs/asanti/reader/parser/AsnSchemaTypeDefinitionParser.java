/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader.parser;

import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.brightsparklabs.asanti.model.schema.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.AsnSchemaEnumeratedOption;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionChoice;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionEnumerated;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionSequence;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionSequenceOf;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionSet;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionSetOf;
import com.google.common.collect.ImmutableList;

/**
 * Logic for parsing a Type Definition form a module within an ASN.1 schema
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionParser
{
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** pattern to match a SET/SEQUENCE type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_SEQUENCE =
            Pattern.compile("^SEQUENCE ?\\{(.+)\\} ?(\\(.+\\))?$");

    /** pattern to match a SET/SEQUENCE type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_SET = Pattern.compile("^SET ?\\{(.+)\\} ?(\\(.+\\))?$");

    /** pattern to match a ENUMERATED type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_ENUMERATED = Pattern.compile("^ENUMERATED ?\\{(.+)\\}$");

    /** pattern to match a CHOICE type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_CHOICE = Pattern.compile("^CHOICE ?\\{(.+)\\} ?(\\(.+\\))?$");

    /** pattern to match a SEQUENCE OF type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_SEQUENCE_OF = Pattern.compile("^SEQUENCE( .+)? OF (.+)$");

    /** pattern to match a SET OF type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_SET_OF = Pattern.compile("^SET( .+)? OF (.+)$");

    /** pattern to match a CLASS type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_CLASS = Pattern.compile("^CLASS ?\\{(.+)\\}$");

    /** pattern to match a PRIMITIVE type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_PRIMITIVE =
            Pattern.compile("^(BIT STRING|GeneralizedTime|IA5String|INTEGER|NumericString|OCTET STRING|UTF8String|VisibleString) ?(.*)$");

    // TODO add all primitives to error message
    /** error message if an unknown ASN.1 built-in type is found */
    private static final String ERROR_UNKNOWN_BUILT_IN_TYPE =
            "Parser expected a built-in type of SEQUENCE, SET, ENUMERATED, SEQUENCE OF, SET OF, CHOICE, CLASS or a primitive (BIT STRING, GeneralizedTime, IA5String, INTEGER, NumericString, OCTET STRING, UTF8String, VisibleString) but found: ";

    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger log = Logger.getLogger(AsnSchemaTypeDefinitionParser.class.getName());

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses a type definition from a module from an ASN.1 schema
     *
     * @param name
     *            the name of the defined type (i.e. the text on the left hand
     *            side of the {@code ::=})
     * @param value
     *            the value of the defined type (i.e. the text on the right hand
     *            side of the {@code ::=})
     *
     * @return an {@link AsnSchemaTypeDefinition} representing the parsed data
     *
     * @throws ParseException
     *             if any errors occur while parsing the type
     */
    public static AsnSchemaTypeDefinition parse(String name, String value) throws ParseException
    {
        log.log(Level.FINE, "Found type definition: {0} = {1}", new Object[] { name, value });

        // check if defining a SEQUENCE
        Matcher matcher = PATTERN_TYPE_DEFINITION_SEQUENCE.matcher(value);
        if (matcher.matches())
        {
            final String items = matcher.group(1);
            return parseSequence(name, items);
        }

        // check if defining a SET
        matcher = PATTERN_TYPE_DEFINITION_SET.matcher(value);
        if (matcher.matches())
        {
            final String items = matcher.group(1);
            return parseSet(name, items);
        }

        // check if defining a CHOICE
        matcher = PATTERN_TYPE_DEFINITION_CHOICE.matcher(value);
        if (matcher.matches())
        {
            final String items = matcher.group(1);
            return parseChoice(name, items);
        }

        // check if defining an ENUMERATED
        matcher = PATTERN_TYPE_DEFINITION_ENUMERATED.matcher(value);
        if (matcher.matches())
        {
            final String items = matcher.group(1);
            return parseEnumerated(name, items);
        }

        // check if defining a PRIMITIVE
        matcher = PATTERN_TYPE_DEFINITION_PRIMITIVE.matcher(value);
        if (matcher.matches())
        {
            // TODO handle *all* PRIMITIVEs
            log.warning("Type Definitions for PRIMITIVES not yet supported");
            return AsnSchemaTypeDefinition.NULL;
        }

        // check if defining a SEQUENCE OF
        matcher = PATTERN_TYPE_DEFINITION_SEQUENCE_OF.matcher(value);
        if (matcher.matches())
        {
            final String contraints = matcher.group(1);
            final String elementTypeName = matcher.group(2);
            return parseSequenceOf(name, elementTypeName, contraints);
        }

        // check if defining a SET OF
        matcher = PATTERN_TYPE_DEFINITION_SET_OF.matcher(value);
        if (matcher.matches())
        {
            final String contraints = matcher.group(1);
            final String elementTypeName = matcher.group(2);
            return parseSetOf(name, elementTypeName, contraints);
        }

        // check if defining a CLASS
        matcher = PATTERN_TYPE_DEFINITION_CLASS.matcher(value);
        if (matcher.matches())
        {
            // TODO handle CLASS
            log.warning("Type Definitions for CLASS not yet supported");
            return AsnSchemaTypeDefinition.NULL;
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
     *            name of the defined type
     *
     * @param componentTypesText
     *            the component types contained in the SEQUENCE as a string
     *
     * @return an {@link AsnSchemaTypeDefinitionSequence} representing the
     *         parsed data
     *
     * @throws ParseException
     *             if any errors occur while parsing the type
     */
    private static AsnSchemaTypeDefinitionSequence parseSequence(String name, String componentTypesText)
            throws ParseException
    {
        final ImmutableList<AsnSchemaComponentType> componentTypes =
                AsnSchemaComponentTypeParser.parse(componentTypesText);
        final AsnSchemaTypeDefinitionSequence typeDefinition =
                new AsnSchemaTypeDefinitionSequence(name, componentTypes);
        return typeDefinition;
    }

    /**
     * Parses a SET type definition
     *
     * @param name
     *            name of the defined type
     *
     * @param componentTypesText
     *            the component types contained in the SET as a string
     *
     * @return an {@link AsnSchemaTypeDefinitionSet} representing the parsed
     *         data
     *
     * @throws ParseException
     *             if any errors occur while parsing the type
     */
    private static AsnSchemaTypeDefinitionSet parseSet(String name, String componentTypesText) throws ParseException
    {
        final ImmutableList<AsnSchemaComponentType> componentTypes =
                AsnSchemaComponentTypeParser.parse(componentTypesText);
        final AsnSchemaTypeDefinitionSet typeDefinition = new AsnSchemaTypeDefinitionSet(name, componentTypes);
        return typeDefinition;
    }

    /**
     * Parses a CHOICE type definition
     *
     * @param name
     *            name of the defined type
     *
     * @param componentTypesText
     *            the component types contained in the CHOICE as a string
     *
     * @return an {@link AsnSchemaTypeDefinitionChoice} representing the parsed
     *         data
     *
     * @throws ParseException
     *             if any errors occur while parsing the type
     */
    private static AsnSchemaTypeDefinitionChoice parseChoice(String name, String componentTypesText)
            throws ParseException
    {
        final ImmutableList<AsnSchemaComponentType> componentTypes =
                AsnSchemaComponentTypeParser.parse(componentTypesText);
        final AsnSchemaTypeDefinitionChoice typeDefinition = new AsnSchemaTypeDefinitionChoice(name, componentTypes);
        return typeDefinition;
    }

    /**
     * Parses a SEQUENCE OF type definition
     *
     * @param name
     *            name of the defined type
     *
     * @param elementTypeName
     *            the name of the type for the elements in this SEQUENCE OF.
     *            E.g. for {@code SEQUENCE OF OCTET STRING}, this would be
     *            {@code OCTET STRING}
     *
     * @param constraints
     *            the constraints on the SEQUENCE OF. E.g for
     *            {@code SEQUENCE (SIZE (1..100) OF OCTET STRING} this would be
     *            {@code (SIZE (1..100)}
     *
     * @return an {@link AsnSchemaTypeDefinitionSequenceOf} representing the
     *         parsed data
     *
     * @throws ParseException
     *             if any errors occur while parsing the type
     */
    private static AsnSchemaTypeDefinitionSequenceOf parseSequenceOf(String name, String elementTypeName,
            String constraints) throws ParseException
    {
        final AsnSchemaTypeDefinitionSequenceOf typeDefinition =
                new AsnSchemaTypeDefinitionSequenceOf(name, elementTypeName, constraints);
        return typeDefinition;
    }

    /**
     * Parses a SET OF type definition
     *
     * @param name
     *            name of the defined type
     *
     * @param elementTypeName
     *            the name of the type for the elements in this SET OF. E.g. for
     *            {@code SET OF OCTET STRING}, this would be
     *            {@code OCTET STRING}
     *
     * @param constraints
     *            the constraints on the SET OF. E.g for
     *            {@code SET (SIZE (1..100) OF OCTET STRING} this would be
     *            {@code (SIZE (1..100)}
     *
     * @return an {@link AsnSchemaTypeDefinitionSetOf} representing the parsed
     *         data
     *
     * @throws ParseException
     *             if any errors occur while parsing the type
     */
    private static AsnSchemaTypeDefinitionSetOf parseSetOf(String name, String elementTypeName, String constraints)
            throws ParseException
    {
        final AsnSchemaTypeDefinitionSetOf typeDefinition =
                new AsnSchemaTypeDefinitionSetOf(name, elementTypeName, constraints);
        return typeDefinition;
    }

    /**
     * Parses an ENUMERATED type definition
     *
     * @param name
     *            name of the defined type
     *
     * @param enumeratedOptionsText
     *            the enumerated definition text as a string. This is the text
     *            between the curly braces following the word ENUMERATED. E.g.
     *            for <code>ENUMERATED { north(0), south(1) }</code> this is
     *            {@code "north(0), south(1)"}
     *
     * @return an {@link AsnSchemaTypeDefinitionEnumerated} representing the
     *         parsed data
     *
     * @throws ParseException
     *             if any errors occur while parsing the type
     */
    private static AsnSchemaTypeDefinitionEnumerated parseEnumerated(String name, String enumeratedOptionsText)
            throws ParseException
    {
        final ImmutableList<AsnSchemaEnumeratedOption> enumeratedOptions =
                AsnSchemaEnumeratedParser.parse(enumeratedOptionsText);
        final AsnSchemaTypeDefinitionEnumerated typeDefinition =
                new AsnSchemaTypeDefinitionEnumerated(name, enumeratedOptions);
        return typeDefinition;
    }
}
