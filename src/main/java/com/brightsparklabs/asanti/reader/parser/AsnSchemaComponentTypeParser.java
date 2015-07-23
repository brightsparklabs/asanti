/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader.parser;

import com.brightsparklabs.asanti.model.schema.AsnModuleTaggingMode;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaComponentType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.*;

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

    /** pattern to break text into: tag name, tag, type, optional/default */
    private static final Pattern PATTERN_COMPONENT_TYPE = Pattern.compile(
            "([a-zA-Z0-9\\-]+) ?(\\[(\\d+)\\])? ?(.+?) ?((OPTIONAL)|(DEFAULT ([a-zA-Z0-9\\-]+)))?");

    /**
     * pattern to break the raw type string into: set/sequence of, construct constraints, type name,
     * type definition, type constraints
     */
    private static final Pattern PATTERN_RAW_TYPE = Pattern.compile(
            "(((SET)|(SEQUENCE))(( SIZE)? \\(.+?\\)?)? OF )?([a-zA-Z0-9\\-\\.& ]+)(\\{.+\\})? ?(\\((.+)\\))?");

    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger
            = LoggerFactory.getLogger(AsnSchemaComponentTypeParser.class);

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses the component types in a construct
     *
     * @param componentTypesText
     *         all component types contained in the construct as a string
     * @param taggingMode
     *          dictates the mode in which to handle/generate tags
     * @return each component type found in the construct
     *
     * @throws NullPointerException
     *         if {@code componentTypesText} or {@code taggingMode} is {@code null}
     * @throws IllegalArgumentException
     *         if {@code componentTypesText} is blank
     * @throws ParseException
     *         if any errors occur while parsing the data
     */
    public static ImmutableList<AsnSchemaComponentType> parse(String componentTypesText,
            AsnModuleTaggingMode taggingMode) throws ParseException
    {
        checkNotNull(componentTypesText);
        checkArgument(!componentTypesText.trim().isEmpty(),
                "Component Types Text must be specified");
        checkNotNull(taggingMode);

        final List<String> componentTypeLines = splitComponentTypesText(componentTypesText);
        final ImmutableList.Builder<AsnSchemaComponentType> builder = ImmutableList.builder();
        for (final String componentTypeLine : componentTypeLines)
        {
            final AsnSchemaComponentType componentType = parseComponentType(componentTypeLine, taggingMode);
            builder.add(componentType);
        }
        return builder.build();
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * Splits the single block of text containing all component type definitions into a list. Each
     * element of the list represents an individual component type definition.
     *
     * @param componentTypesText
     *         all component types expressed as a single text block
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

        for (final String item : items)
        {
            logger.debug("  - {}", item);
        }

        return items;
    }

    /**
     * Parses a single component type definition from a construct
     *
     * @param componentTypeLine
     *         the component type definition
     *
     * @return an {@link AsnSchemaComponentType} representing the parsed text
     *
     * @throws ParseException
     *         if any errors occur while parsing the data
     */
    private static AsnSchemaComponentType parseComponentType(String componentTypeLine,
            AsnModuleTaggingMode taggingMode) throws ParseException
    {
        Matcher matcher = PATTERN_COMPONENT_TYPE.matcher(componentTypeLine);
        if (!matcher.matches())
        {
            final String error = "Could not match component type definition. Found: "
                    + componentTypeLine;
            throw new ParseException(error, -1);
        }

        final String tagName = matcher.group(1);
        final String tag = matcher.group(3);
        final String rawType = matcher.group(4);
        final boolean isOptional = matcher.group(6) != null;

        matcher = PATTERN_RAW_TYPE.matcher(rawType);
        if (!matcher.matches())
        {
            final String error = "Could not match type within component type definition. Found: "
                    + rawType;
            throw new ParseException(error, -1);
        }

        final AsnSchemaType tagType = AsnSchemaTypeParser.parse(rawType, taggingMode);
        return new AsnSchemaComponentType(tagName, tag, isOptional, tagType);
    }
}
