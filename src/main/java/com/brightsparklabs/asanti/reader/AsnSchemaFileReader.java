/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.AsnSchemaDefault;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.io.Files;

/**
 * Reads data from an ASN.1 schema file
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaFileReader
{
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** pattern to match carriage returns */
    private final static Pattern PATTERN_CARRIAGE_RETURN = Pattern.compile("\\r");

    /** pattern to match commented lines */
    private final static Pattern PATTERN_COMMENTS = Pattern.compile("[\\t ]*--.*\\n");

    /** pattern to match new lines */
    private final static Pattern PATTERN_NEW_LINE = Pattern.compile("\\n");

    /** pattern to match tabs/spaces */
    private final static Pattern PATTERN_TABS_SPACES = Pattern.compile("[\\t ]+");

    /** pattern to match module header keywords */
    private final static Pattern PATTERN_MODULE_HEADER_KEYWORDS = Pattern.compile("(DEFINITIONS|BEGIN|EXPORTS|IMPORTS|END)");

    /** pattern to match semicolons */
    private final static Pattern PATTERN_SEMICOLONS = Pattern.compile(";");

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Reads the data from the supplied ASN.1 schema file
     *
     * @param asnSchemaFile
     *            schema file to parse
     *
     * @throws IOException
     *             if any errors occur while parsing the schema file
     *
     */
    public static AsnSchema read(File asnSchemaFile) throws IOException
    {
        final Iterable<String> lines = getLines(asnSchemaFile);
        int i = 0;
        for (String line : lines)
        {
            System.out.println(i++ + ": " + line);
        }
        return new AsnSchemaDefault();
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * Strips out comments and redundant whitespace from the supplied ASN.1
     * schema file and returns the resulting lines. Lines generally appear in
     * the following order:
     * <ul>
     * <li>module name and identification</li>
     * <li>'DEFINITIONS' keyword</li>
     * <ul>
     * <li>tagging environment definition</li>
     * <li>extensibility environment definition</li>
     * </ul>
     * <li>'BEGIN' keyword</li>
     * <ul>
     * <li>'EXPORTS' keyword</li>
     * <ul>
     * <li>export statements</li>
     * <li>semicolon</li>
     * </ul>
     * <li>'IMPORTS' keyword</li>
     * <ul>
     * <li>import statements</li>
     * <li>semicolon</li>
     * </ul>
     * <li>value/type definitions</li> </ul> <li>'END' keyword</li> </ul>
     *
     * @param asnSchemaFile
     *            schema file to parse
     *
     * @return the lines from the schema
     *
     * @throws IOException
     *             if any errors occur while parsing the schema file
     */
    private static Iterable<String> getLines(File asnSchemaFile) throws IOException
    {
        // cull comments and collapse whitespace
        String contents = Files.toString(asnSchemaFile, Charsets.UTF_8);
        contents = PATTERN_CARRIAGE_RETURN.matcher(contents).replaceAll("");
        contents = PATTERN_COMMENTS.matcher(contents).replaceAll("");
        contents = PATTERN_NEW_LINE.matcher(contents).replaceAll(" ");
        contents = PATTERN_TABS_SPACES.matcher(contents).replaceAll(" ");
        // ensure module header keywords appear on separate lines
        contents = PATTERN_MODULE_HEADER_KEYWORDS.matcher(contents).replaceAll("\n$1\n");
        contents = PATTERN_SEMICOLONS.matcher(contents).replaceAll("\n;\n");

        final Iterable<String> lines = Splitter.on("\n").trimResults().omitEmptyStrings().split(contents);
        return lines;
    }
}
