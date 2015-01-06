/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader.parser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinition;
import com.google.common.collect.Lists;

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
    private static final Pattern PATTERN_TYPE_DEFINITION_SET_OR_SEQUENCE = Pattern.compile("^(SEQUENCE|SET) ?\\{(.+)\\} ?(.*)$");

    /** pattern to match a ENUMERATED type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_ENUMERATED = Pattern.compile("^ENUMERATED ?\\{(.+)\\}$");

    /** pattern to match a CHOICE type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_CHOICE = Pattern.compile("^CHOICE ?\\{(.+)\\}(\\(.+\\))?$");

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
     * @throws ParseException
     *             if any errors occur while parsing the type
     */
    public static AsnSchemaTypeDefinition parse(String name, String value) throws ParseException
    {
        log.log(Level.FINE, "Found type definition: {0} = {1}", new Object[] { name, value });

        // check if defining a SET or SEQUENCE
        Matcher matcher = PATTERN_TYPE_DEFINITION_SET_OR_SEQUENCE.matcher(value);
        if (matcher.matches())
        {
            final String asnBuiltinType = matcher.group(1);
            final String items = matcher.group(2);
            return parseSetOrSequence(name, asnBuiltinType, items);
        }

        // check if defining an ENUMERATED
        matcher = PATTERN_TYPE_DEFINITION_ENUMERATED.matcher(value);
        if (matcher.matches())
        {
            // TODO handle ENUMERATED
            return new AsnSchemaTypeDefinition();
        }

        // check if defining a PRIMITIVE
        matcher = PATTERN_TYPE_DEFINITION_PRIMITIVE.matcher(value);
        if (matcher.matches())
        {
            // TODO handle *all* PRIMITIVEs
            return new AsnSchemaTypeDefinition();
        }

        // check if defining a CHOICE
        matcher = PATTERN_TYPE_DEFINITION_CHOICE.matcher(value);
        if (matcher.matches())
        {
            // TODO handle CHOICE
            return new AsnSchemaTypeDefinition();
        }

        // check if defining a SET OF or SEQUENCE OF
        matcher = PATTERN_TYPE_DEFINITION_SET_OF_OR_SEQUENCE_OF.matcher(value);
        if (matcher.matches())
        {
            // TODO handle SET OF or SEQUENCE OF
            return new AsnSchemaTypeDefinition();
        }

        // check if defining a CLASS
        matcher = PATTERN_TYPE_DEFINITION_CLASS.matcher(value);
        if (matcher.matches())
        {
            // TODO handle CLASS
            return new AsnSchemaTypeDefinition();
        }

        // unknown definition
        final String error = ERROR_UNKNOWN_BUILT_IN_TYPE + name + " ::= " + value;
        throw new ParseException(error, 0);
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
     * @param asnBuiltinType
     *            ASN.1 built-in type of the defined type (SET or SEQUENCE)
     *
     * @param itemsString
     *            the items which comprise this SET/SEQUENCE
     */
    private static AsnSchemaTypeDefinition parseSetOrSequence(String name, String asnBuiltinType, String itemsString)
    {
        final ArrayList<String> items = Lists.newArrayList();
        int begin = 0;
        int bracketCount = 0;
        final int length = itemsString.length();
        for (int i = 0; i < length; i++)
        {
            if (i == (length - 1))
            {
                // at end of string
                final String item = itemsString.substring(begin, length).trim();
                if (!item.equals("..."))
                {
                    items.add(item);
                }
                break;
            }

            final char character = itemsString.charAt(i);
            switch (character)
            {
                case '{':
                case '(':
                    bracketCount++;
                    break;

                case '}':
                case ')':
                    bracketCount--;
                    break;

                case ',':
                    if (bracketCount == 0)
                    {
                        final String item = itemsString.substring(begin, i).trim();
                        if (!item.equals("..."))
                        {
                            items.add(item);
                        }
                        begin = i + 1;
                    }
                    break;

                default:
            }
        }

        for (String item : items)
        {
            log.log(Level.FINER, "  - {0}", item);
        }

        return new AsnSchemaTypeDefinition();
    }
}
