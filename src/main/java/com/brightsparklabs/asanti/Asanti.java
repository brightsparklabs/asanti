/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti;

import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.decoder.AsnDecoder;
import com.brightsparklabs.asanti.model.data.AsnData;
import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.DecodedTag;
import com.brightsparklabs.asanti.reader.AsnSchemaFileReader;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import com.google.common.io.BaseEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Main class as an example of how to use the library.
 *
 * @author brightSPARK Labs
 */
public class Asanti
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static Logger logger = LoggerFactory.getLogger(Asanti.class);

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Main method. Used for testing.
     *
     * @param args
     *         command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            final String filename = args[0];
            final File file = new File(filename);

            if (filename.endsWith(".asn"))
            {
                testReadingAsnFile(file);
            }
            else
            {
                testReadingBerFile(file);
            }
        }
        catch (final Exception ex)
        {
            logger.error("Could not parse file", ex);
            System.exit(1);
        }
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * Test parsing an ASN.1 schema file
     *
     * @param asnFile
     *         file to parse
     *
     * @throws IOException
     *         if any errors occur while parsing
     */
    private static void testReadingAsnFile(File asnFile) throws IOException
    {
        final AsnSchema asnSchema = AsnSchemaFileReader.read(asnFile);

        logger.info("Expecting PASS");
        ImmutableList<String> rawTags = ImmutableList.of("/1/1",
                "/2/3/2/3/0/1",
                "/2/0/2/2/1/18/0",
                "/2/0/2/2/1/18/0",
                "/1/3/0/0");
        for (final String rawTag : rawTags)
        {
            final OperationResult<DecodedTag> result = asnSchema.getDecodedTag(rawTag, "PS-PDU");
            logger.info("\t{}:\t decode {} => {}",
                    result.wasSuccessful() ? "PASS" : "FAIL",
                    rawTag,
                    result.getOutput().getTag());
        }

        logger.info("Expecting FAIL");
        rawTags = ImmutableList.of("/1/14",
                "/1/1/5",
                "/2/3/2/3/0/100",
                "/2/0/2/2/1/18/90",
                "/1/3/0/80");
        for (final String rawTag : rawTags)
        {
            final OperationResult<DecodedTag> result = asnSchema.getDecodedTag(rawTag, "PS-PDU");
            logger.info("\t{}:\t decode {} => {}",
                    result.wasSuccessful() ? "PASS" : "FAIL",
                    rawTag,
                    result.getOutput().getTag());
        }

        logger.info("User testing:");
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in,
                Charsets.UTF_8));
        while (true)
        {
            System.out.print("\tEnter raw tag: ");
            final String rawTag = reader.readLine();
            final OperationResult<DecodedTag> result = asnSchema.getDecodedTag(rawTag, "PS-PDU");
            logger.info("\t{}:\t decode {} => {}",
                    result.wasSuccessful() ? "PASS" : "FAIL",
                    rawTag,
                    result.getOutput().getTag());
        }
    }

    /**
     * Test parsing a BER file
     *
     * @param berFile
     *         file to parse
     *
     * @throws IOException
     *         if any errors occur while parsing
     */
    private static void testReadingBerFile(File berFile) throws IOException
    {
        final ImmutableList<AsnData> data = AsnDecoder.readAsnBerFile(berFile);
        int count = 0;
        for (final AsnData asnData : data)
        {
            logger.info("PDU[" + count + "]");
            final Map<String, byte[]> tagsData = asnData.getBytes();

            for (final String tag : Ordering.natural().immutableSortedCopy(tagsData.keySet()))
            {
                logger.info("\t {}: 0x{}", tag, BaseEncoding.base16().encode(tagsData.get(tag)));
            }
            count++;
        }
    }
}
