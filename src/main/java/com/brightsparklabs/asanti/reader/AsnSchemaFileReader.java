/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.reader.parser.AsnSchemaParser;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

/**
 * Reads data from an ASN.1 schema file
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaFileReader
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(AsnSchemaFileReader.class);

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
     * @return the data from the supplied ASN.1 schema file
     */
    public static AsnSchema read(File asnSchemaFile) throws IOException
    {
        logger.debug("Parsing schema file: {}", asnSchemaFile.getAbsolutePath());
        final String contents = Files.toString(asnSchemaFile, Charsets.UTF_8);
        try
        {
            return AsnSchemaParser.parse(contents);
        }
        catch (final ParseException ex)
        {
            throw new IOException(ex);
        }
    }
}
