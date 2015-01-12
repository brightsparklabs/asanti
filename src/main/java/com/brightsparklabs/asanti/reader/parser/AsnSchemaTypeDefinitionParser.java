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

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.AsnSchemaConstructedTypeDefinition;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinition;
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
    private static final Pattern PATTERN_TYPE_DEFINITION_SEQUENCE = Pattern.compile("^SEQUENCE ?\\{(.+)\\} ?(\\(.+\\))?$");

    /** pattern to match a SET/SEQUENCE type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_SET = Pattern.compile("^SET ?\\{(.+)\\} ?(\\(.+\\))?$");

    /** pattern to match a ENUMERATED type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_ENUMERATED = Pattern.compile("^ENUMERATED ?\\{(.+)\\}$");

    /** pattern to match a CHOICE type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_CHOICE = Pattern.compile("^CHOICE ?\\{(.+)\\} ?(\\(.+\\))?$");

    /** pattern to match a SET OF/SEQUENCE OF type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_SET_OF_OR_SEQUENCE_OF = Pattern.compile("^(SEQUENCE|SET)( .+)? OF ?(.+)$");

    /** pattern to match a CLASS type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_CLASS = Pattern.compile("^CLASS ?\\{(.+)\\}$");

    /** pattern to match a PRIMITIVE type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_PRIMITIVE = Pattern.compile("^(BIT STRING|GeneralizedTime|INTEGER|NumericString|OCTET STRING|UTF8String|VisibleString) ?(.*)$");

    // TODO add all primitives to error message
    /** error message if an unknown ASN.1 built-in type is found */
    private static final String ERROR_UNKNOWN_BUILT_IN_TYPE = "Parser expected a built-in type of SEQUENCE, SET, ENUMERATED, BIT STRING, GeneralizedTime, INTEGER, NumericString, OCTET STRING, UTF8String, VisibleString, SEQUENCE OF, SET OF, CHOICE or CLASS but found: ";

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
    public static AsnSchemaTypeDefinition<?> parse(String name, String value) throws ParseException
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

        // check if defining an ENUMERATED
        matcher = PATTERN_TYPE_DEFINITION_ENUMERATED.matcher(value);
        if (matcher.matches())
        {
            // TODO handle ENUMERATED
            return AsnSchemaTypeDefinition.NULL;
        }

        // check if defining a PRIMITIVE
        matcher = PATTERN_TYPE_DEFINITION_PRIMITIVE.matcher(value);
        if (matcher.matches())
        {
            // TODO handle *all* PRIMITIVEs
            return AsnSchemaTypeDefinition.NULL;
        }

        // check if defining a CHOICE
        matcher = PATTERN_TYPE_DEFINITION_CHOICE.matcher(value);
        if (matcher.matches())
        {
            // TODO handle CHOICE
            return AsnSchemaTypeDefinition.NULL;
        }

        // check if defining a SET OF or SEQUENCE OF
        matcher = PATTERN_TYPE_DEFINITION_SET_OF_OR_SEQUENCE_OF.matcher(value);
        if (matcher.matches())
        {
            // TODO handle SET OF or SEQUENCE OF
            return AsnSchemaTypeDefinition.NULL;
        }

        // check if defining a CLASS
        matcher = PATTERN_TYPE_DEFINITION_CLASS.matcher(value);
        if (matcher.matches())
        {
            // TODO handle CLASS
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
     * Parses a SET/SEQUENCE type definition
     *
     * @param name
     *            name of the defined type
     *
     * @param componentTypesText
     *            the component types contained in the construct as a string
     *
     * @return an {@link AsnSchemaConstructedTypeDefinition} representing the
     *         parsed data
     *
     * @throws ParseException
     *             if any errors occur while parsing the type
     */
    private static AsnSchemaConstructedTypeDefinition parseSequence(String name, String componentTypesText)
            throws ParseException
    {
        final ImmutableList<AsnSchemaComponentType> componentTypes = AsnSchemaComponentTypeParser.parse(componentTypesText);
        final AsnSchemaConstructedTypeDefinition typeDefinition = new AsnSchemaConstructedTypeDefinition(name,
                AsnBuiltinType.Sequence, componentTypes);
        return typeDefinition;
    }

    /**
     * Parses a SET type definition
     *
     * @param name
     *            name of the defined type
     *
     * @param componentTypesText
     *            the component types contained in the construct as a string
     *
     * @return an {@link AsnSchemaConstructedTypeDefinition} representing the
     *         parsed data
     *
     * @throws ParseException
     *             if any errors occur while parsing the type
     */
    private static AsnSchemaConstructedTypeDefinition parseSet(String name, String componentTypesText)
            throws ParseException
    {
        final ImmutableList<AsnSchemaComponentType> componentTypes = AsnSchemaComponentTypeParser.parse(componentTypesText);
        final AsnSchemaConstructedTypeDefinition typeDefinition = new AsnSchemaConstructedTypeDefinition(name,
                AsnBuiltinType.Set, componentTypes);
        return typeDefinition;
    }
}
