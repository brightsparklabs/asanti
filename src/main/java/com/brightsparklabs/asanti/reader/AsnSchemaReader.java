/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader;

import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.reader.parser.AsnSchemaParser;
import com.google.common.base.Charsets;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

/**
 * Reads data and models it as an {@link AsnSchema} object
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaReader
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(AsnSchemaReader.class);

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Reads the data from the supplied ASN.1 schema file
     *
     * @param asnSchemaFile
     *         schema file to parse
     *
     * @return the data from the supplied ASN.1 schema file
     */
    public static AsnSchema read(File asnSchemaFile) throws IOException
    {
        logger.debug("Parsing schema file: {}", asnSchemaFile.getAbsolutePath());
        final CharSource source = Files.asCharSource(asnSchemaFile, Charsets.UTF_8);
        return read(source);
    }

    /**
     * Reads the data from the supplied ASN.1 schema source
     *
     * @param source
     *         source of characters from an ASN.1 schema
     *
     * @return the data from the supplied ASN.1 schema
     *
     * @throws IOException
     *         if any errors occur while parsing the schema file
     */
    public static AsnSchema read(CharSource source) throws IOException
    {
        final String contents = source.read();
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
