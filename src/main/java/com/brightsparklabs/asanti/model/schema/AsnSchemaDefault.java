/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.io.Files;

/**
 * Default implementation of {@link AsnSchema}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaDefault implements AsnSchema
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

    /** pattern to match curly braces */
    private final static Pattern PATTERN_CURLY_BRACES = Pattern.compile("([\\{\\}])");

    /** pattern to match tabs/spaces */
    private final static Pattern PATTERN_TABS_SPACES = Pattern.compile("[\\t ]+");

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     *
     * @param asnSchemaFile
     *            schema file to parse
     *
     * @throws IOException
     *             if any errors occur while passing the schema file
     *
     */
    public AsnSchemaDefault(File asnSchemaFile) throws IOException
    {
        String contents = Files.toString(asnSchemaFile, Charsets.UTF_8);
        contents = PATTERN_CARRIAGE_RETURN.matcher(contents).replaceAll("");
        contents = PATTERN_COMMENTS.matcher(contents).replaceAll("");
        contents = PATTERN_NEW_LINE.matcher(contents).replaceAll(" ");
        contents = PATTERN_CURLY_BRACES.matcher(contents).replaceAll("\n$1\n");
        contents = PATTERN_TABS_SPACES.matcher(contents).replaceAll(" ");

        final Iterable<String> lines = Splitter.on("\n").omitEmptyStrings().split(contents);
        int i = 0;
        for (String line : lines)
        {
            System.out.println(i++ + ": " + line);
        }
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchema
    // -------------------------------------------------------------------------

    @Override
    public String getDecodedTag(String rawTag)
    {
        return "";
    }

    @Override
    public String getRawTag(String decodedTag)
    {
        return "";
    }

    @Override
    public String getPrintableString(String tag, byte[] data)
    {
        return "";
    }

    @Override
    public Object getDecodedObject(String tag, byte[] data)
    {
        return "";
    }
}
