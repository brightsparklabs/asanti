/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader.parser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.brightsparklabs.asanti.model.schema.AsnSchemaComponentType;
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
     *
     * @throws ParseException
     *             if any errors occur while parsing the data
     */
    public static ImmutableList<AsnSchemaComponentType> parse(String componentTypesText) throws ParseException
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
        if(componentTypesText.startsWith(" invokeId InvokeId (CONSTRAINED BY {} ! RejectProblem:returnResult-unrecognizedInvocation) (CONSTRAINED BY {} ! RejectProblem:returnResult-resultResponseUnexpected), result"))
        {
            int i = 0;
        }
        final ArrayList<String> items = Lists.newArrayList();
        int begin = 0;
        int bracketCount = 0;
        final int length = componentTypesText.length();
        for (int i = 0; i < length; i++)
        {
            // check if at end of string
            if (i == (length - 1))
            {
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

    private static AsnSchemaComponentType parseComponentType(String componentTypeLine) throws ParseException
    {
        final Matcher matcher = PATTERN_COMPONENT_TYPE.matcher(componentTypeLine);
        if (matcher.matches()) { return parseComponentType(matcher); }

        throw new ParseException("Could not match component type definition. Found: " + componentTypeLine, -1);

    }

    private static AsnSchemaComponentType parseComponentType(Matcher matcher)
    {

        final String tagName = matcher.group(1);
        final String tag = matcher.group(3);
        final String constructOf = matcher.group(5);
        final String constructOfConstraint = matcher.group(6);
        final String typeName = matcher.group(7);
        final String constraints = matcher.group(10);
        final boolean isOptional = matcher.group(11) != null;
        final String defaultValue = matcher.group(13);

        final AsnSchemaComponentType componentType = new AsnSchemaComponentType(tagName, tag, typeName, isOptional);
        return componentType;
    }

    /** pattern for matching component types */
    private static final Pattern PATTERN_COMPONENT_TYPE = Pattern.compile("([a-zA-z0-9\\-]+) ?(\\[(\\d+)\\])? ?((SET|SEQUENCE)( SIZE \\(.+\\)) OF )?([a-zA-z0-9\\-\\.& ]+)(\\{.+\\})? ?(\\((.+)\\))? ?(OPTIONAL)?(DEFAULT (.+))?");

    // TODO not entirely working, does not correctly group
    // "partyInformation [9] SET SIZE (1..10) OF PartyInformation OPTIONAL"
    private static final Pattern PATTERN_COMPONENT_TYPE_CONSTRUCT_OF = Pattern.compile("([a-zA-z0-9\\-]+) ?(\\[(\\d+)\\])? ?(SET|SEQUENCE)( SIZE .+)? OF ([a-zA-z0-9 ]+) ?(\\((.+)\\))? ?(OPTIONAL)?(DEFAULT (.+))?");
}
