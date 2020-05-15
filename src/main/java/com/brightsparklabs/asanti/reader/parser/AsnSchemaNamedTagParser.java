/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.reader.parser;

import com.brightsparklabs.asanti.model.schema.AsnSchemaModule;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaNamedTag;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import java.text.ParseException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logic for parsing ENUMERATED options and INTEGER distinguished values
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaNamedTagParser {
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(AsnSchemaModule.class);

    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /**
     * pattern for an option in an ENUMERATED type definition. Breaks text into: tag name, tag
     * (where the tag is optional)
     */
    private static final Pattern PATTERN_ENUMERATED_OPTION =
            Pattern.compile("([^\\(]+)(\\((.+)\\))?$");

    /**
     * pattern for a distinguished value in an INTEGER type definition. Breaks text into: tag name,
     * tag (where the tag is required)
     */
    private static final Pattern PATTERN_DISTINGUISHED_VALUE =
            Pattern.compile("([^\\(]+)(\\((.+)\\))$");

    /** splitter which splits strings on commas */
    private static final Splitter COMMA_SPLITTER =
            Splitter.on(",").omitEmptyStrings().trimResults();

    /** error message if enumerated tag numbering fails */
    private static final String ERROR_ENUMERATED_TAG_REUSE =
            "Parser encountered multiple Enumerated values using the same tag number: ";

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses an ENUMERATED definition
     *
     * @param enumeratedOptionsText the enumerated definition text as a string. This is the text
     *     between the curly braces following the word ENUMERATED. E.g. for <code>
     *     ENUMERATED { north(0), south(1) }</code> this is {@code "north(0), south(1)"}
     * @return each option found in the ENUMERATED
     * @throws ParseException if any errors occur while parsing the data
     */
    public static ImmutableList<AsnSchemaNamedTag> parseEnumeratedOptions(
            String enumeratedOptionsText) throws ParseException {
        final ImmutableList.Builder<AsnSchemaNamedTag> builder = ImmutableList.builder();
        if (enumeratedOptionsText == null) {
            return builder.build();
        }

        final Iterable<String> lines = COMMA_SPLITTER.split(enumeratedOptionsText);

        // In order to keep track of the tag (numbers) that have been used, to ensure uniqueness
        final Set<Integer> usedTags = Sets.newHashSet();

        for (final String optionLine : lines) {
            if ("...".equals(optionLine)) {
                // ignore extension markers
                continue;
            }

            final AsnSchemaNamedTag option = parseOption(optionLine, usedTags);
            builder.add(option);
        }
        return builder.build();
    }

    /**
     * Parses an INTEGER definition list of distinguished values
     *
     * @param distinguishedValuesText the distinguished values text as a string. This is the text
     *     between the curly braces following the word INTEGER. E.g. for <code>
     *     INTEGER { tomorrow(0), three-day(1) }</code> this is {@code "tomorrow(0), three-day(1)"}
     * @return each distinguished value found in the INTEGER distinguished values list
     * @throws ParseException if any errors occur while parsing the data
     */
    public static ImmutableList<AsnSchemaNamedTag> parseIntegerDistinguishedValues(
            String distinguishedValuesText) throws ParseException {
        final ImmutableList.Builder<AsnSchemaNamedTag> builder = ImmutableList.builder();
        if (Strings.isNullOrEmpty(distinguishedValuesText)) {
            return builder.build();
        }

        final Iterable<String> lines = COMMA_SPLITTER.split(distinguishedValuesText);
        for (final String distinguishedValueLine : lines) {
            final AsnSchemaNamedTag distinguishedValue =
                    parseDistinguishedValue(distinguishedValueLine);
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
     * @param optionLine the option text to parse
     * @param usedTags the tags (numbers) that are already in use for this type
     * @return an {@link AsnSchemaNamedTag} representing the parsed text
     * @throws ParseException if any errors occur while parsing the data
     */
    private static AsnSchemaNamedTag parseOption(String optionLine, Set<Integer> usedTags)
            throws ParseException {
        final Matcher matcher = PATTERN_ENUMERATED_OPTION.matcher(optionLine);

        if (!matcher.matches()) {
            final String error =
                    "Could not match ENUMERATED option definition. Expected: optionName(optionNumber), found: "
                            + optionLine;
            throw new ParseException(error, -1);
        }

        final String tagName = matcher.group(1);
        String tag = matcher.group(3);

        // We need to ensure that we store a unique, non-null tag.
        if (tag == null) {
            tag = generateUniqueTagNumber(usedTags, tagName);
        } else {
            ensureTagUnique(tag, usedTags, tagName);
        }

        usedTags.add(Integer.parseInt(tag));

        return new AsnSchemaNamedTag(tagName, tag);
    }

    /**
     * Parses a single distinguished value from an INTEGER definition list of distinguished values
     *
     * @param distinguishedValueLine the distinguished value text to parse
     * @return an {@link AsnSchemaNamedTag} representing the parsed text
     * @throws ParseException if any errors occur while parsing the data
     */
    private static AsnSchemaNamedTag parseDistinguishedValue(String distinguishedValueLine)
            throws ParseException {
        final Matcher matcher = PATTERN_DISTINGUISHED_VALUE.matcher(distinguishedValueLine);

        if (!matcher.matches()) {
            final String error =
                    "Could not match INTEGER distinguished value definition. Expected: distinguishedValueName(tag), found: "
                            + distinguishedValueLine;
            throw new ParseException(error, -1);
        }

        final String tagName = matcher.group(1);
        final String tag = matcher.group(3);
        return new AsnSchemaNamedTag(tagName, tag);
    }

    /**
     * Provides a tag that is unique (not in the Set of tags provided,
     *
     * @param usedTags The set of tags that are already in use
     * @param tagName The name of the tag, used for logging useful messages.
     * @return The tag that we should use, which may be what we were provided or will be a newly
     *     generated tag if needed
     * @throws ParseException If the provided tag is already in use
     */
    private static String generateUniqueTagNumber(Set<Integer> usedTags, String tagName)
            throws ParseException {
        // Generate a new, unused tag
        Integer newTag = 0;
        while (usedTags.contains(newTag)) {
            newTag++;
        }

        logger.debug("Generated automatic tag ({}) for {}", newTag, tagName);
        return newTag.toString();
    }

    /**
     * Ensures that the provided tag is not already in use, throws if it is
     *
     * @param tag The new tag to test the uniqueness of
     * @param usedTags The set of tags that are already in use
     * @param tagName The name of the tag, used for logging useful errors.
     * @throws ParseException If the provided tag is already in use
     */
    private static void ensureTagUnique(String tag, Set<Integer> usedTags, String tagName)
            throws ParseException {
        // Ensure this tag is not already in use.
        Integer tagNumber = Integer.parseInt(tag);
        if (usedTags.contains(tagNumber)) {
            final String error = ERROR_ENUMERATED_TAG_REUSE + tagName + " (" + tag + ")";
            logger.warn(error);
            throw new ParseException(error, -1);
        }
    }
}
