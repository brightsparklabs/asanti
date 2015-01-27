/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.brightsparklabs.asanti.model.data.AsnData;
import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.DecodeResult;
import com.brightsparklabs.asanti.reader.AsnBerFileReader;
import com.brightsparklabs.asanti.reader.AsnSchemaFileReader;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import com.google.common.io.BaseEncoding;

/**
 * Logic for decoding tagged/application ASN.1 data
 *
 * @author brightSPARK Labs
 */
public class AsnDecoder
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static Logger log = Logger.getLogger(AsnDecoder.class.getName());

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Reads the supplied ASN.1 BER/DER binary file
     *
     * @param berFile
     *            file to decode
     *
     * @return list of {@link AsnData} objects found in the file
     *
     * @throws IOException
     *             if any errors occur reading from the file
     */
    public static ImmutableList<AsnData> readAsnBerFile(File berFile) throws IOException
    {
        return AsnBerFileReader.read(berFile);
    }

    // -------------------------------------------------------------------------
    // TEST METHODS
    // -------------------------------------------------------------------------

    /**
     * Main method. Used for testing.
     *
     * @param args
     *            command line arguments
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
            log.severe("Could not parse file - " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Test parsing an ASN.1 schema file
     *
     * @param asnFile
     *            file to parse
     *
     * @throws IOException
     *             if any errors occur while parsing
     */
    private static void testReadingAsnFile(File asnFile) throws IOException
    {
        final AsnSchema asnSchema = AsnSchemaFileReader.read(asnFile);

        log.info("Expecting PASS");
        ImmutableList<String> rawTags =
                ImmutableList.of("/1/1", "/2/3/2/3/0/1", "/2/0/2/2/1/18/0", "/2/0/2/2/1/18/0", "/1/3/0/0");
        for (final String rawTag : rawTags)
        {
            final DecodeResult<String> result = asnSchema.getDecodedTag(rawTag, "PS-PDU");
            log.log(Level.INFO, "\t{0}:\t decode {1} => {2}", new Object[] { result.wasSuccessful() ? "PASS" : "FAIL",
                    rawTag, result.getDecodedData() });
        }

        log.info("Expecting FAIL");
        rawTags = ImmutableList.of("/1/14", "/1/1/5", "/2/3/2/3/0/100", "/2/0/2/2/1/18/90", "/1/3/0/80");
        for (final String rawTag : rawTags)
        {
            final DecodeResult<String> result = asnSchema.getDecodedTag(rawTag, "PS-PDU");
            log.log(Level.INFO, "\t{0}:\t decode {1} => {2}", new Object[] { result.wasSuccessful() ? "PASS" : "FAIL",
                    rawTag, result.getDecodedData() });
        }

        log.info("User testing:");
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, Charsets.UTF_8));
        while (true)
        {
            System.out.print("\tEnter raw tag: ");
            final String rawTag = reader.readLine();
            final DecodeResult<String> result = asnSchema.getDecodedTag(rawTag, "PS-PDU");
            log.log(Level.INFO, "\t{0}:\t decode {1} => {2}", new Object[] { result.wasSuccessful() ? "PASS" : "FAIL",
                    rawTag, result.getDecodedData() });
        }
    }

    /**
     * Test parsing a BER file
     *
     * @param berFile
     *            file to parse
     *
     * @throws IOException
     *             if any errors occur while parsing
     */
    private static void testReadingBerFile(File berFile) throws IOException
    {
        final ImmutableList<AsnData> data = readAsnBerFile(berFile);
        int count = 0;
        for (final AsnData asnData : data)
        {
            log.info("PDU[" + count + "]");
            final Map<String, byte[]> tagsData = asnData.getBytes();

            for (final String tag : Ordering.natural()
                    .immutableSortedCopy(tagsData.keySet()))
            {
                log.info("\t" + tag + ": 0x" + BaseEncoding.base16()
                        .encode(tagsData.get(tag)));
            }
            count++;
        }
    }
}
