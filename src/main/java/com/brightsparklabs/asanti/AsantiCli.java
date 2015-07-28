/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti;

import com.brightsparklabs.asanti.common.DecodeException;
import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.model.data.AsnData;
import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.DecodedTag;
import com.brightsparklabs.asanti.reader.AsnSchemaReader;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Ordering;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteSource;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
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
public class AsantiCli
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static Logger logger = LoggerFactory.getLogger(AsantiCli.class);

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Entry point to the application.
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

                case 3:
                {
                    final String asnFilename = args[0].endsWith(".asn") ? args[0] : args[1];
                    final String berFilename = args[0].endsWith(".asn") ? args[1] : args[0];
                    final String topLevelType = args[2];
                    final File asnFile = new File(asnFilename);
                    final File berFile = new File(berFilename);
                    // Load the schema once, and use it for all data files.
                    final CharSource schemaSource = Files.asCharSource(asnFile, Charsets.UTF_8);
                    final AsnSchema asnSchema = AsnSchemaReader.read(schemaSource);
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
     * Handle the loading of a single data (ber) file against the provided schema This will not
     * propagate exceptions, will only log them.
     *
     * @param berFile
     *         ASN.1 BER binary file to decode
     * @param asnSchema
     *         schema to decode against
     * @param topLevelType
     *         top level type in the schema to decode objects as
     */
    private static void loadDataFile(File berFile, AsnSchema asnSchema, String topLevelType)
    {
        try
        {

            logger.info("Loading file: " + berFile.getCanonicalPath());

            final ByteSource byteSource = Files.asByteSource(berFile);
            final ImmutableList<DecodedAsnData> pdus = Asanti.decodeAsnData(byteSource,
                    asnSchema,
                    topLevelType);
            for (int i = 0; i < pdus.size(); i++)
            {

                logger.info("Parsing PDU[{}]", i);
                final DecodedAsnData pdu = pdus.get(i);
                for (String tag : pdu.getTags())
                {
                    try
                    {
                        logger.info("\t{} => {} as {}",
                                tag,
                                pdu.getPrintableString(tag).get(),
                                pdu.getType(tag).get().getBuiltinType());
                    }
                    catch (DecodeException e)
                    {
                        logger.info("\t{} => {} as {} (as HexString because {})",
                                tag,
                                pdu.getHexString(tag).get(),
                                pdu.getType(tag).get().getBuiltinType(),
                                e.getMessage());
                    }
                }
                for (String tag : pdu.getUnmappedTags())
                {
                    logger.info("\t?{} => {}", tag, pdu.getHexString(tag));
                }
            }
        }
        catch (Exception e)
        {
            logger.error("Exception loading data file: " + e.getMessage());
        }

    }

    /**
     * This function will take a schema file and run it against the data file(s) passed.  If
     * dataFile is a directory then it will load all files in the directory (against the schema),
     * and recurse directories. This will attempt to ignore/skip files that are not ASN.1 BER files.
     * This will not propagate exceptions, will only log them.
     *
     * @param dataFile
     *         either a directory or ASN.1 BER binary file to decode
     * @param asnSchema
     *         schema to decode against
     * @param topLevelType
     *         the name of the top level
     */
    private static void handleDataFile(File dataFile, AsnSchema asnSchema, String topLevelType)
    {

        try
        {
            if (dataFile.isDirectory())
            {
                for (File file : dataFile.listFiles())
                {
                    handleDataFile(file, asnSchema, topLevelType);
                }
            }
            else
            {
                final String name = dataFile.getCanonicalPath();

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
        final CharSource schemaSource = Files.asCharSource(asnFile, Charsets.UTF_8);
        final AsnSchema asnSchema = AsnSchemaReader.read(schemaSource);

        logger.info("User testing:");
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in,
                Charsets.UTF_8));
        while (true)
        {
            System.out.print("\tEnter raw tag: ");
            final String rawTag = reader.readLine();

            ImmutableSet<OperationResult<DecodedTag, String>> results = asnSchema.getDecodedTags(
                    ImmutableList.of(rawTag),
                    "PS-PDU");
            OperationResult<DecodedTag, String> result = results.iterator().next();
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
        final ByteSource byteSource = Files.asByteSource(berFile);
        final ImmutableList<AsnData> data = Asanti.readAsnBerData(byteSource);
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
