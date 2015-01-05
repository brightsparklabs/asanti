/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private final static Pattern PATTERN_COMMENTS = Pattern.compile("[\\t ]*--.*?(--|\\n)");

    /** pattern to match new lines */
    private final static Pattern PATTERN_NEW_LINE = Pattern.compile("\\n+");

    /** pattern to match tabs/spaces */
    private final static Pattern PATTERN_TABS_SPACES = Pattern.compile("[\\t ]+");

    /** pattern to match module header keywords */
    private final static Pattern PATTERN_SCHEMA_KEYWORDS = Pattern.compile("(DEFINITIONS|BEGIN|EXPORTS|IMPORTS|END)");

    /** pattern to match semicolons */
    private final static Pattern PATTERN_SEMICOLONS = Pattern.compile(";");

    /** error message if schema is missing header keywords */
    private static final String ERROR_MISSING_HEADERS = "Schema does not contain all expected module headers";

    /** error message if schema is missing content */
    private static final String ERROR_MISSING_CONTENT = "Schema does not contain any information within the 'BEGIN' and 'END' keywords";

    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static Logger log = Logger.getLogger(AsnSchemaFileReader.class.getName());

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Private constructor. Use {@link AsnSchemaFileReader#read(File)} instead
     * of direct construction.
     */
    private AsnSchemaFileReader()
    {
        // private constructor
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     *
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
        AsnSchemaFileReader reader = new AsnSchemaFileReader();
        return reader.parse(asnSchemaFile);
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses the supplied ASN.1 schema file
     *
     * @param asnSchemaFile
     *            schema file to parse
     *
     * @throws IOException
     *             if any errors occur while parsing the schema file
     *
     */
    private AsnSchema parse(File asnSchemaFile) throws IOException
    {
        log.log(Level.FINE, "Parsing schema file: {0}", asnSchemaFile.getAbsolutePath());
        final Iterator<String> lineIterator = getLines(asnSchemaFile);
        while (lineIterator.hasNext())
        {
            parseModuleHeader(lineIterator);
            parseContent(lineIterator);
        }

        return new AsnSchemaDefault();
    }

    /**
     * Strips out comments and redundant whitespace from the supplied ASN.1
     * schema file and returns the resulting lines. The schema keywords
     * (DEFINITIONS, BEGIN, EXPORTS, IMPORTS and END) will all be presented on
     * their own line. Semicolons which mark the end of IMPORTS/EXPORTS will
     * also be on their own line. Lines generally appear in the following order:
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
    private Iterator<String> getLines(File asnSchemaFile) throws IOException
    {
        // cull comments and collapse whitespace
        String contents = Files.toString(asnSchemaFile, Charsets.UTF_8);
        contents = PATTERN_CARRIAGE_RETURN.matcher(contents).replaceAll("");
        contents = PATTERN_COMMENTS.matcher(contents).replaceAll("");
        contents = PATTERN_NEW_LINE.matcher(contents).replaceAll("\n");
        contents = PATTERN_TABS_SPACES.matcher(contents).replaceAll(" ");
        // ensure module header keywords appear on separate lines
        contents = PATTERN_SCHEMA_KEYWORDS.matcher(contents).replaceAll("\n$1\n");
        contents = PATTERN_SEMICOLONS.matcher(contents).replaceAll("\n;\n");

        final Iterable<String> lines = Splitter.on("\n").trimResults().omitEmptyStrings().split(contents);

        for (String line : lines)
        {
            log.log(Level.FINE, "{0}", line);
        }

        return lines.iterator();
    }

    /**
     * Parses the module header. This data is located before the 'BEGIN'
     * keyword.
     * <p>
     * Prior to calling this method, the iterator should be pointing at the
     * first line in the schema.
     * <p>
     * After calling this method, the iterator will be pointing at the line
     * following the 'BEGIN' keyword
     *
     * @param lineIterator
     *            iterator pointing at the first line after the 'BEGIN' keyword
     *
     * @throws IOException
     *             if any errors occur while parsing the schema file
     */
    private void parseModuleHeader(Iterator<String> lineIterator) throws IOException
    {
        try
        {
            final String moduleName = lineIterator.next().split(" ")[0];
            log.log(Level.INFO, "Found module: {0}", moduleName);

            // skip through to the BEGIN tag
            for (String line = lineIterator.next(); !"BEGIN".equals(line); line = lineIterator.next())
            {
            }
        }
        catch (NoSuchElementException ex)
        {
            throw new IOException(ERROR_MISSING_HEADERS);
        }
    }

    /**
     * Parses the data located between the 'BEGIN' and 'END' keywords.
     * <p>
     * Prior to calling this method, the iterator should be pointing at the line
     * following the 'BEGIN' keyword.
     * <p>
     * After calling this method, the iterator will be pointing at the line
     * following the 'END' keyword
     *
     * @param lineIterator
     *            iterator pointing at the first line after the 'BEGIN' keyword
     *
     * @throws IOException
     *             if any errors occur while parsing the schema file
     */
    private void parseContent(Iterator<String> lineIterator) throws IOException
    {
        try
        {
            // skip past 'IMPORTS' and 'EXPORTS'
            String line = lineIterator.next();
            while (line.startsWith("EXPORTS") || line.startsWith("IMPORTS"))
            {
                while (!";".equals(line))
                {
                    line = lineIterator.next();
                }
                line = lineIterator.next();
            }
        }
        catch (NoSuchElementException ex)
        {
            throw new IOException(ERROR_MISSING_CONTENT);
        }

        parseTypeDefinitionsAndValueAssignments(lineIterator);
    }

    /**
     * Parses the type definitions and value assignments data. This data located
     * after the imports/exports and before the 'END' keyword.
     * <p>
     * Prior to calling this method, the iterator should be pointing at the line
     * following all imports/exports.
     * <p>
     * After calling this method, the iterator will be pointing at the line
     * following the 'END' keyword
     *
     * @param lineIterator
     *            iterator pointing at the first line after the 'BEGIN' keyword
     *
     * @throws IOException
     *             if any errors occur while parsing the schema file
     */
    private void parseTypeDefinitionsAndValueAssignments(Iterator<String> lineIterator)
    {
        String line = lineIterator.next();
        while (!"END".equals(line))
        {
            // TODO parse type definition or value assignment
            line = lineIterator.next();
        }
    }
}
