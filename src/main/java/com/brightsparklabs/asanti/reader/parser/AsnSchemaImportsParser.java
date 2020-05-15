/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.reader.parser;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.text.ParseException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Logic for parsing the {@code IMPORTS} from a module within an ASN.1 schema
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaImportsParser {
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** pattern to match an import statement pertaining to a single module */
    private static final Pattern PATTERN_MODULE_IMPORT =
            Pattern.compile("(.+?) FROM (.+?) \\{.+?\\}");

    /** splitter to separate comma delimited strings */
    private static final Splitter COMMA_SPLITTER =
            Splitter.on(',').omitEmptyStrings().trimResults();

    /** error message if imports statement is invalid */
    private static final String ERROR_INVALID_IMPORTS_STATEMENT = "Imports statement is invalid";

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses an Imports statement from a module from an ASN.1 schema
     *
     * @param imports the imports statement string
     * @return an ImmutableMap representing the imports
     * @throws ParseException if any errors occur while parsing the type
     */
    public static ImmutableMap<String, String> parse(String imports) throws ParseException {
        final Map<String, String> typeToModule = Maps.newHashMap();
        if (Strings.isNullOrEmpty(imports)) {
            return ImmutableMap.copyOf(typeToModule);
        }

        final Matcher matcher = PATTERN_MODULE_IMPORT.matcher(imports);
        Boolean matchFound = false;
        while (matcher.find()) {
            matchFound = true;
            final Iterable<String> types = COMMA_SPLITTER.split(matcher.group(1));
            final String module = matcher.group(2);
            for (final String type : types) {
                typeToModule.put(type, module);
            }
        }

        if (!matchFound) {
            throw new ParseException(ERROR_INVALID_IMPORTS_STATEMENT, -1);
        }

        return ImmutableMap.copyOf(typeToModule);
    }
}
