/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader;

import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.reader.parser.AsnSchemaParser;
import com.google.common.io.CharSource;

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
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

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
