/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.brightsparklabs.asanti.model.schema.AsnSchemaConstructedTypeDefinition.AsnSchemaComponentType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * Logic for parsing Component Types from a 'constructed' Type Definition
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaComponentTypeParser
{

    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** pattern to match a SET/SEQUENCE type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_SEQUENCE = Pattern.compile("^SEQUENCE ?\\{(.+)\\} ?(.*)$");

    /** pattern to match a SET/SEQUENCE type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION_SET = Pattern.compile("^SET ?\\{(.+)\\} ?(.*)$");

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
    private static final Logger log = Logger.getLogger(AsnSchemaComponentTypeParser.class.getName());

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses the items in a construct
     *
     * @param componentTypesText
     *            the component types contained in the construct as a string
     *
     * @return each component type found in the construct
     */
    public static ImmutableList<AsnSchemaComponentType> parse(String componentTypesText)
    {
        final List<String> componentTypeLines = splitComponentTypesText(componentTypesText);
        final ImmutableList.Builder<AsnSchemaComponentType> builder = ImmutableList.builder();
        for (String componentTypeLine : componentTypeLines)
        {
            final AsnSchemaComponentType componentType = parseComponentType(componentTypeLine);
            builder.add(componentType);
        }
        return builder.build();
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * Splits the single block of text containing all component type definitions
     * into a list. Each element of the list represents an individual component
     * type definition.
     *
     * @param componentTypesText
     *            the component types expressed as a single text block
     *
     * @return list of each component type found in the text
     */
    private static List<String> splitComponentTypesText(String componentTypesText)
    {
        final ArrayList<String> items = Lists.newArrayList();
        int begin = 0;
        int bracketCount = 0;
        final int length = componentTypesText.length();
        for (int i = 0; i < length; i++)
        {
            if (i == (length - 1))
            {
                // at end of string
                final String item = componentTypesText.substring(begin, length).trim();
                if (!item.equals("..."))
                {
                    items.add(item);
                }
                break;
            }

            final char character = componentTypesText.charAt(i);
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
                        final String item = componentTypesText.substring(begin, i).trim();
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

        return items;
    }

    private static AsnSchemaComponentType parseComponentType(String componentTypeLine)
    {

        // TODO Auto-generated method stub
        return null;
    }

}
