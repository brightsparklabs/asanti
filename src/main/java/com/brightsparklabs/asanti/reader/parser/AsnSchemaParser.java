/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader.parser;

import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.AsnSchemaImpl;
import com.brightsparklabs.asanti.model.schema.AsnSchemaModule;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Logic for parsing an ASN.1 schema
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaParser
{
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** pattern to match carriage returns */
    private static final Pattern PATTERN_CARRIAGE_RETURN = Pattern.compile("\\r");

    /** pattern to match comments that are started with -- and end with newline */
    private static final Pattern PATTERN_ENDLINE_COMMENTS = Pattern.compile("[\\t ]*--.*?\\n");

    /** pattern to match comments that are started and ended with -- */
    private static final Pattern PATTERN_INLINE_COMMENTS = Pattern.compile("[\\t ]*--.*?--");

    /** pattern to match block comments */
    private static final Pattern PATTERN_BLOCK_COMMENTS = Pattern.compile("(?s)/\\*.*?\\*/");

    /** pattern to match new lines */
    private static final Pattern PATTERN_NEW_LINE = Pattern.compile("\\n+");

    /** pattern to match tabs/spaces */
    private static final Pattern PATTERN_TABS_SPACES = Pattern.compile("[\\t ]+");

    /** pattern to match module header keywords */
    private static final Pattern PATTERN_SCHEMA_KEYWORDS = Pattern.compile(
            "(DEFINITIONS|BEGIN|EXPORTS|IMPORTS|END)");

    /** pattern to match semicolons */
    private static final Pattern PATTERN_SEMICOLONS = Pattern.compile(";");

    /** error message if schema is missing 'END' keyword */
    private static final String ERROR_MISSING_END_KEYWORD = "Schema is missing an 'END' keyword";

    /** error message if an empty file is encountered */
    private static final String ERROR_EMPTY_FILE = "Schema is empty";

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses the supplied ASN.1 schema text
     *
     * @param asnSchema
     *         all text from the ASN.1 schema
     *
     * @return the parsed schema
     *
     * @throws ParseException
     *         if any errors occur while parsing the schema
     */
    public static AsnSchema parse(String asnSchema) throws ParseException
    {
        if (Strings.isNullOrEmpty(asnSchema))
        {
            throw new ParseException(ERROR_EMPTY_FILE, -1);
        }

        final Map<String, AsnSchemaModule> modules = Maps.newHashMap();
        final List<AsnSchemaModule.Builder> moduleBuilders = Lists.newArrayList();
        final Iterator<String> lineIterator = getLines(asnSchema);

        String primaryModule = null;

        // parse each module in the schema
        final List<String> moduleLines = Lists.newArrayList();
        while (lineIterator.hasNext())
        {
            final String line = lineIterator.next();
            moduleLines.add(line);
            if ("END".equals(line))
            {
                // keep track of all the ModuleBuilders so that we can resolve all the
                // imports and placeholders at the end.
                moduleBuilders.add(AsnSchemaModuleParser.parse(moduleLines));
                moduleLines.clear();
            }
        }

        if (!moduleLines.isEmpty())
        {
            throw new ParseException(ERROR_MISSING_END_KEYWORD, -1);
        }

        // resolve all the placeholders and imports.
        for (AsnSchemaModule.Builder builder : moduleBuilders)
        {
            builder.resolveTypes(moduleBuilders);
        }

        for (AsnSchemaModule.Builder builder : moduleBuilders)
        {
            builder.performTagging();
        }

        // do the final build, which will also calculate all the tags, which can't be done until
        // all the types are known (even across module boundaries), which is not until the above loop
        for (AsnSchemaModule.Builder builder : moduleBuilders)
        {
            builder.checkForDuplicates();
            final AsnSchemaModule module = builder.build();
            modules.put(module.getName(), module);

            if (primaryModule == null)
            {
                primaryModule = module.getName();
            }
        }

        return new AsnSchemaImpl(primaryModule, modules);
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * Strips out comments and redundant whitespace from the supplied ASN.1 schema and returns the
     * resulting lines. The schema keywords (DEFINITIONS, BEGIN, EXPORTS, IMPORTS and END) will all
     * be presented on their own line. Semicolons which mark the end of IMPORTS/EXPORTS will also be
     * on their own line. Lines generally appear in the following order:
     *
     * <ul>
     *
     * <li>module name and identification</li>
     *
     * <li>'DEFINITIONS' keyword</li>
     *
     * <ul>
     *
     * <li>tagging environment definition</li>
     *
     * <li>extensibility environment definition</li>
     *
     * </ul>
     *
     * <li>'BEGIN' keyword</li>
     *
     * <ul>
     *
     * <li>'EXPORTS' keyword</li>
     *
     * <ul>
     *
     * <li>export statements</li>
     *
     * <li>semicolon</li>
     *
     * </ul>
     *
     * <li>'IMPORTS' keyword</li>
     *
     * <ul>
     *
     * <li>import statements</li>
     *
     * <li>semicolon</li>
     *
     * </ul>
     *
     * <li>value/type definitions</li>
     *
     * </ul>
     *
     * <li>'END' keyword</li>
     *
     * </ul>
     *
     * @param asnSchema
     *         schema to parse
     *
     * @return the lines from the schema
     */
    private static Iterator<String> getLines(String asnSchema)
    {
        // cull comments and collapse whitespace
        asnSchema = PATTERN_CARRIAGE_RETURN.matcher(asnSchema).replaceAll("");
        asnSchema = PATTERN_ENDLINE_COMMENTS.matcher(asnSchema).replaceAll("\n");
        asnSchema = PATTERN_INLINE_COMMENTS.matcher(asnSchema).replaceAll("");
        asnSchema = PATTERN_BLOCK_COMMENTS.matcher(asnSchema).replaceAll("");
        asnSchema = PATTERN_NEW_LINE.matcher(asnSchema).replaceAll("\n");
        asnSchema = PATTERN_TABS_SPACES.matcher(asnSchema).replaceAll(" ");
        // ensure module header keywords appear on separate lines
        asnSchema = PATTERN_SCHEMA_KEYWORDS.matcher(asnSchema).replaceAll("\n$1\n");
        asnSchema = PATTERN_SEMICOLONS.matcher(asnSchema).replaceAll("\n;\n");

        final Iterable<String> lines = Splitter.on("\n")
                .trimResults()
                .omitEmptyStrings()
                .split(asnSchema);
        return lines.iterator();
    }
}
