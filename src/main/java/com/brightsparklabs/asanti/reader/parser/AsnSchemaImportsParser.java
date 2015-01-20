/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader.parser;

import java.text.ParseException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinition;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

/**
 * Logic for parsing the {@code IMPORTS} from a module within an ASN.1 schema
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaImportsParser
{
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** pattern to match an import statement pertaining to a single module */
    private static final Pattern PATTERN_MODULE_IMPORT = Pattern.compile("(.+?) FROM (.+?) \\{.+?\\}");

    /** splitter to separate comma delimited strings */
    private static final Splitter COMMA_SPLITTER = Splitter.on(',')
            .omitEmptyStrings()
            .trimResults();

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
    public static ImmutableMap<String, String> parse(String imports) throws ParseException
    {
        final Map<String, String> typeToModule = Maps.newHashMap();

        final Matcher matcher = PATTERN_MODULE_IMPORT.matcher(imports);
        while (matcher.find())
        {
            final Iterable<String> types = COMMA_SPLITTER.split(matcher.group(1));
            final String module = matcher.group(2);
            for (final String type : types)
            {
                typeToModule.put(type, module);
            }
        }
        return ImmutableMap.copyOf(typeToModule);
    }
}
