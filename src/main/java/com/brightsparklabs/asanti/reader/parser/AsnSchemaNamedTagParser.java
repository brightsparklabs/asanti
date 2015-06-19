/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader.parser;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaNamedTag;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

/**
 * Logic for parsing ENUMERATED options and INTEGER distinguished values
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaNamedTagParser
{
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /**
     * pattern for an option in an ENUMERATED type definition. Breaks text into:
     * tag name, tag (where the tag is optional)
     */
    private static final Pattern PATTERN_ENUMERATED_OPTION = Pattern.compile("([^\\(]+)(\\((.+)\\))?$");

    /**
     * pattern for a distinguished value in an INTEGER type definition. Breaks
     * text into: tag name, tag (where the tag is required)
     */
    private static final Pattern PATTERN_DISTINGUISHED_VALUE = Pattern.compile("([^\\(]+)(\\((.+)\\))$");

    /** splitter which splits strings on commas */
    private static final Splitter COMMA_SPLITTER = Splitter.on(",")
            .omitEmptyStrings()
            .trimResults();

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses an ENUMERATED definition
     *
     * @param enumeratedOptionsText
     *            the enumerated definition text as a string. This is the text
     *            between the curly braces following the word ENUMERATED. E.g.
     *            for <code>ENUMERATED { north(0), south(1) }</code> this is
     *            {@code "north(0), south(1)"}
     *
     * @return each option found in the ENUMERATED
     *
     * @throws ParseException
     *             if any errors occur while parsing the data
     */
    public static ImmutableList<AsnSchemaNamedTag> parseEnumeratedOptions(String enumeratedOptionsText)
            throws ParseException
    {
        final ImmutableList.Builder<AsnSchemaNamedTag> builder = ImmutableList.builder();
        if (enumeratedOptionsText == null) { return builder.build(); }

        final Iterable<String> lines = COMMA_SPLITTER.split(enumeratedOptionsText);

        int i = 0;
        for (final String optionLine : lines)
        {
            if ("...".equals(optionLine))
            {
                // ignore extension markers
                continue;
            }
            // TODO MJF - get rid of the optionalTag

            final AsnSchemaNamedTag option = parseOption(optionLine, i);
            builder.add(option);

            i++;
        }
        return builder.build();
    }

    /**
     * Parses an INTEGER definition list of distinguished values
     *
     * @param distinguishedValuesText
     *            the distinguished values text as a string. This is the text
     *            between the curly braces following the word INTEGER. E.g. for
     *            <code>INTEGER { tomorrow(0), three-day(1) }</code> this is
     *            {@code "tomorrow(0), three-day(1)"}
     *
     * @return each distinguished value found in the INTEGER distinguished
     *         values list
     *
     * @throws ParseException
     *             if any errors occur while parsing the data
     */
    public static ImmutableList<AsnSchemaNamedTag> parseIntegerDistinguishedValues(String distinguishedValuesText)
            throws ParseException
    {
        final ImmutableList.Builder<AsnSchemaNamedTag> builder = ImmutableList.builder();
        if (Strings.isNullOrEmpty(distinguishedValuesText)) { return builder.build(); }

        final Iterable<String> lines = COMMA_SPLITTER.split(distinguishedValuesText);
        for (final String distinguishedValueLine : lines)
        {
            final AsnSchemaNamedTag distinguishedValue = parseDistinguishedValue(distinguishedValueLine);
            builder.add(distinguishedValue);
        }
        return builder.build();
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses a single option from an ENUMERATED definition
     *
     * @param optionLine
     *            the option text to parse
     *
     * @param optionalTag
     *          an optional tag to use if one is not parse out from the optionLine
     *
     * @return an {@link AsnSchemaNamedTag} representing the parsed text
     *
     * @throws ParseException
     *             if any errors occur while parsing the data
     */
    private static AsnSchemaNamedTag parseOption(String optionLine, Integer optionalTag) throws ParseException
    {
        final Matcher matcher = PATTERN_ENUMERATED_OPTION.matcher(optionLine);

        if (!matcher.matches())
        {
            final String error =
                    "Could not match ENUMERATED option definition. Expected: optionName(optionNumber), found: "
                            + optionLine;
            throw new ParseException(error, -1);
        }

        final String tagName = matcher.group(1);
        String tag = matcher.group(3);

        // TODO MJF - just here so I can parse the ETSI schema for testing.
        if (tag == null)
        {
            tag = optionalTag.toString();
        }

            return new AsnSchemaNamedTag(tagName, tag);
    }

    /**
     * Parses a single distinguished value from an INTEGER definition list of
     * distinguished values
     *
     * @param distinguishedValueLine
     *            the distinguished value text to parse
     *
     * @return an {@link AsnSchemaNamedTag} representing the parsed text
     *
     * @throws ParseException
     *             if any errors occur while parsing the data
     */
    private static AsnSchemaNamedTag parseDistinguishedValue(String distinguishedValueLine) throws ParseException
    {
        final Matcher matcher = PATTERN_DISTINGUISHED_VALUE.matcher(distinguishedValueLine);

        if (!matcher.matches())
        {
            final String error =
                    "Could not match INTEGER distinguished value definition. Expected: distinguishedValueName(tag), found: "
                            + distinguishedValueLine;
            throw new ParseException(error, -1);
        }

        final String tagName = matcher.group(1);
        final String tag = matcher.group(3);
        return new AsnSchemaNamedTag(tagName, tag);
    }
}
