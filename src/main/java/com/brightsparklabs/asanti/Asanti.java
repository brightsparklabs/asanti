/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti;

import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.decoder.AsnDecoder;
import com.brightsparklabs.asanti.model.data.AsnData;
import com.brightsparklabs.asanti.model.data.DecodedAsnData;
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
            switch (args.length)
            {
                case 1:
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
                    break;

                case 2:
                    throw new Exception("Top level type name not supplied");

                case 3: {
                    final String asnFilename = args[0].endsWith(".asn") ? args[0] : args[1];
                    final String berFilename = args[0].endsWith(".asn") ? args[1] : args[0];
                    final String topLevelType = args[2];
                    final File asnFile = new File(asnFilename);
                    final File berFile = new File(berFilename);
                    // Load the schema once, and use it for all data files.
                    final AsnSchema asnSchema = AsnSchemaFileReader.read(asnFile);
                    handleDataFile(berFile, asnSchema, topLevelType);
                }
                    break;

                default:
                    throw new Exception("No ASN Schema (.asn) or ASN Data (.ber) file supplied");
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
     * Handle the loading of a single data (ber) file against the provided schema
     * This will not propagate exceptions, will only log them.
     * @param berFile ASN.1 BER binary file to decode
     * @param asnSchema schema to decode against
     * @param topLevelType top level type in the schema to decode objects as
     */
    private static void loadDataFile(File berFile, AsnSchema asnSchema, String topLevelType)
    {
        try
        {

            logger.info("Loading file: " + berFile.getCanonicalPath());

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    asnSchema,
                    topLevelType);
            for (int i = 0; i < pdus.size(); i++)
            {

                logger.info("Parsing PDU[{}]", i);
                final DecodedAsnData pdu = pdus.get(i);
                for (String tag : pdu.getTags())
                {
                    logger.info("\t{} => {}", tag, pdu.getHexString(tag));
                }
                for (String tag : pdu.getUnmappedTags())
                {
                    logger.info("\t{} => {}", tag, pdu.getHexString(tag));
                }
            }
        }
        catch(Exception e)
        {
            logger.error("Exception loading data file: " + e.getMessage());
        }

    }

    /**
     * This function will take a schema file and run it against the data file(s) passed.  If dataFile is a directory
     * then it will load all files in the directory (against the schema), and recurse directories.
     * This will attempt to ignore/skip files that are not ASN.1 BER files.
     * This will not propagate exceptions, will only log them.
     * @param dataFile either a directory or ASN.1 BER binary file to decode
     * @param asnSchema schema to decode against
     * @param topLevelType the name of the top level
     */
    private static void handleDataFile(File dataFile, AsnSchema asnSchema, String topLevelType)
    {

        try
        {
            if (dataFile.isDirectory())
            {
                File[] files = dataFile.listFiles();
                for (File f : files)
                {
                    handleDataFile(f, asnSchema, topLevelType);
                }
            }
            else
            {
                String name = dataFile.getCanonicalPath();

                // I don't really know what the 'right' file extensions are, so let's just rule out
                // some of the ones that we have come across!
                if (!name.toLowerCase().endsWith(".txt") &&
                        !name.toLowerCase().endsWith(".jpg") &&
                        !name.toLowerCase().endsWith(".bmp") &&
                        !name.toLowerCase().endsWith(".asn") &&
                        !name.toLowerCase().endsWith(".zip") &&
                        !name.toLowerCase().endsWith(".wav") &&
                        !name.toLowerCase().endsWith(".csv") &&
                        !name.toLowerCase().endsWith(".xlsx") &&
                        !name.toLowerCase().endsWith(".xls"))
                {
                    loadDataFile(dataFile, asnSchema, topLevelType);
                }
                else
                {
                    logger.debug("Ignoring file: " + name);
                }
            }
        }
        catch (Exception e)
        {
            logger.error("Exception: " + e.getMessage());
        }

    }


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
