/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader.parser;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.brightsparklabs.asanti.model.schema.AsnSchemaEnumeratedOption;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

/**
 * Logic for parsing ENUMERATED types
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaEnumeratedParser
{
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** pattern to break text into: tag name, tag */
    private static final Pattern PATTERN_ENUMERATED_OPTION = Pattern.compile("([^\\(]+)(\\((.+)\\))?$");

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
    public static ImmutableList<AsnSchemaEnumeratedOption> parse(String enumeratedOptionsText) throws ParseException
    {
        final ImmutableList.Builder<AsnSchemaEnumeratedOption> builder = ImmutableList.builder();
        if (enumeratedOptionsText == null) { return builder.build(); }

        final Iterable<String> lines = COMMA_SPLITTER.split(enumeratedOptionsText);

        for (final String optionLine : lines)
        {
            if ("...".equals(optionLine))
            {
                // ignore extension markers
                continue;
            }

            final AsnSchemaEnumeratedOption option = parseEnumeratedOption(optionLine);
            builder.add(option);
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
     * @return an {@link AsnSchemaEnumeratedOption} representing the parsed text
     *
     * @throws ParseException
     *             if any errors occur while parsing the data
     */
    private static AsnSchemaEnumeratedOption parseEnumeratedOption(String optionLine) throws ParseException
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
        final String tag = matcher.group(3);
        return new AsnSchemaEnumeratedOption(tagName, tag);
    }
}
