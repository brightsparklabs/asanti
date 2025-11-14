/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti;

import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.exception.DecodeException;
import com.brightsparklabs.asanti.model.data.AsantiAsnData;
import com.brightsparklabs.asanti.model.data.RawAsnData;
import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.DecodedTag;
import com.brightsparklabs.asanti.model.schema.Decoder;
import com.brightsparklabs.asanti.reader.AsnSchemaReader;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Ordering;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteSource;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Map;

/**
 * Main class as an example of how to use the library.
 *
 * @author brightSPARK Labs
 */
public class AsantiCli {
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** constant to use to add new lines to output */
    private static final String NEW_LINE = System.lineSeparator();

    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(AsantiCli.class);

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Entry point to the application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        try {
            Options options = getOptions();
            CommandLineParser parser = new DefaultParser();
            final CommandLine cmdLine = parser.parse(options, args);
            validateCommandLine(cmdLine);

            switch (cmdLine.getArgs().length) {
                case 1:
                    final String filename = cmdLine.getArgs()[0];
                    final File file = new File(filename);

                    if (filename.endsWith(".asn")) {
                        testReadingAsnFile(file);
                    } else {
                        testReadingBerFile(file);
                    }
                    break;

                case 2:
                    throw new ParseException("Top level type name not supplied");

                case 3:
                    {
                        final String asnFilename =
                                cmdLine.getArgs()[0].endsWith(".asn")
                                        ? cmdLine.getArgs()[0]
                                        : cmdLine.getArgs()[1];
                        final String berFilename =
                                cmdLine.getArgs()[0].endsWith(".asn")
                                        ? cmdLine.getArgs()[1]
                                        : cmdLine.getArgs()[0];
                        final String topLevelType = cmdLine.getArgs()[2];
                        final File asnFile = new File(asnFilename);
                        final File berFile = new File(berFilename);
                        // Load the schema once, and use it for all data files.
                        final CharSource schemaSource = Files.asCharSource(asnFile, Charsets.UTF_8);
                        final AsnSchema asnSchema = AsnSchemaReader.read(schemaSource);
                        handleDataFile(berFile, asnSchema, topLevelType);
                    }
                    break;

                default:
                    throw new ParseException(
                            "No ASN Schema (.asn) or ASN Data (.ber) file supplied");
            }
        } catch (final IOException ex) {
            logger.error("Could not parse file", ex);
            System.exit(1);
        } catch (final ParseException e) {
            printUsage(e.getMessage());
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
     * @param berFile ASN.1 BER binary file to decode
     * @param asnSchema schema to decode against
     * @param topLevelType top level type in the schema to decode objects as
     */
    private static void loadDataFile(
            final File berFile, final AsnSchema asnSchema, final String topLevelType) {
        try {

            logger.info("Loading file: " + berFile.getCanonicalPath());

            final ByteSource byteSource = Files.asByteSource(berFile);
            final ImmutableList<AsantiAsnData> pdus =
                    Asanti.decodeAsnData(byteSource, asnSchema, topLevelType);
            for (int i = 0; i < pdus.size(); i++) {

                logger.info("Parsing PDU[{}]", i);
                final AsantiAsnData pdu = pdus.get(i);
                for (final String tag : pdu.getTags()) {
                    try {
                        logger.info(
                                "\t{} => {} as {}",
                                tag,
                                pdu.getPrintableString(tag).get(),
                                pdu.getType(tag).get().getBuiltinType());
                    } catch (final DecodeException e) {
                        logger.info(
                                "\t{} => {} as {} (as HexString because {})",
                                tag,
                                pdu.getHexString(tag).get(),
                                pdu.getType(tag).get().getBuiltinType(),
                                e.getMessage());
                    }
                }
                for (final String tag : pdu.getUnmappedTags()) {
                    logger.info("\t?{} => {}", tag, pdu.getHexString(tag));
                }
            }
        } catch (final Exception e) {
            logger.error("Exception loading data file: " + e.getMessage());
        }
    }

    /**
     * This function will take a schema file and run it against the data file(s) passed. If dataFile
     * is a directory then it will load all files in the directory (against the schema), and recurse
     * directories. This will attempt to ignore/skip files that are not ASN.1 BER files. This will
     * not propagate exceptions, will only log them.
     *
     * @param rootFile either a directory or ASN.1 BER binary file to decode
     * @param asnSchema schema to decode against
     * @param topLevelType the name of the top level
     */
    private static void handleDataFile(
            final File rootFile, final AsnSchema asnSchema, final String topLevelType) {
        for (final File file : Files.fileTraverser().depthFirstPreOrder(rootFile)) {
            try {
                if (!file.isDirectory()) {
                    final String name = file.getCanonicalPath();

                    // Do not really know what the 'right' file extensions are, so let's just rule
                    // out some of the ones that we have come across that are not BER files.
                    if (!name.toLowerCase(Locale.ENGLISH).endsWith(".txt")
                            && !name.toLowerCase(Locale.ENGLISH).endsWith(".jpg")
                            && !name.toLowerCase(Locale.ENGLISH).endsWith(".bmp")
                            && !name.toLowerCase(Locale.ENGLISH).endsWith(".asn")
                            && !name.toLowerCase(Locale.ENGLISH).endsWith(".zip")
                            && !name.toLowerCase(Locale.ENGLISH).endsWith(".wav")
                            && !name.toLowerCase(Locale.ENGLISH).endsWith(".pcap")
                            && !name.toLowerCase(Locale.ENGLISH).endsWith(".rtp")
                            && !name.toLowerCase(Locale.ENGLISH).endsWith(".csv")
                            && !name.toLowerCase(Locale.ENGLISH).endsWith(".xlsx")
                            && !name.toLowerCase(Locale.ENGLISH).endsWith(".xls")
                            && !name.toLowerCase(Locale.ENGLISH).endsWith(".yaml")
                            ) {
                        loadDataFile(file, asnSchema, topLevelType);
                    } else {
                        logger.debug("Ignoring file: " + name);
                    }
                }
            } catch (final Exception e) {
                logger.error("Exception: " + e.getMessage());
            }
        }
    }

    /**
     * Test parsing an ASN.1 schema file
     *
     * @param asnFile file to parse
     * @throws IOException if any errors occur while parsing
     */
    private static void testReadingAsnFile(final File asnFile) throws IOException {
        final CharSource schemaSource = Files.asCharSource(asnFile, Charsets.UTF_8);
        final AsnSchema asnSchema = AsnSchemaReader.read(schemaSource);

        logger.info("User testing:");
        final BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in, Charsets.UTF_8));
        while (true) {
            System.out.print("\tEnter raw tag: ");
            final String rawTag = reader.readLine();

            ImmutableSet<OperationResult<DecodedTag, String>> results =
                    Decoder.getDecodedTags(ImmutableList.of(rawTag), "PS-PDU", asnSchema);
            OperationResult<DecodedTag, String> result = results.iterator().next();
            logger.info(
                    "\t{}:\t decode {} => {}",
                    result.wasSuccessful() ? "PASS" : "FAIL",
                    rawTag,
                    result.getOutput().getTag());
        }
    }

    /**
     * Test parsing a BER file
     *
     * @param berFile file to parse
     * @throws IOException if any errors occur while parsing
     */
    private static void testReadingBerFile(final File berFile) throws IOException {
        final ByteSource byteSource = Files.asByteSource(berFile);
        final ImmutableList<RawAsnData> data = Asanti.readAsnBerData(byteSource);
        int count = 0;
        for (final RawAsnData rawAsnData : data) {
            logger.info("PDU[" + count + "]");
            final Map<String, byte[]> tagsData = rawAsnData.getBytes();

            for (final String tag : Ordering.natural().immutableSortedCopy(tagsData.keySet())) {
                logger.info("\t {}: 0x{}", tag, BaseEncoding.base16().encode(tagsData.get(tag)));
            }
            count++;
        }
    }

    /**
     * Returns the Command Line Options for this application
     *
     * @return the Command Line Options for this application
     */
    private static Options getOptions() {
        return new Options().addOption("h", "help", false, "Print out help");
    }

    /**
     * Performs application validation against the provided command line. If the validation fails
     * then an appropriate exception will be thrown (ParseException or subclasses)
     *
     * @param cmdLine the command line passes to main
     * @throws ParseException if there are issues with the options or arguments
     */
    private static void validateCommandLine(final CommandLine cmdLine) throws ParseException {
        if (cmdLine.hasOption("h")) {
            throw new ParseException("");
        }

        if (cmdLine.getArgs().length != 1 && cmdLine.getArgs().length != 3) {
            throw new MissingArgumentException("Must specify 1 or 3 arguments");
        }

        // All good!
    }

    /**
     * Prints the usage message
     *
     * @param footerMessage adds to the footer of the message, useful for specifying known issues
     *     with usage.
     */
    private static void printUsage(final String footerMessage) {
        final String callPattern =
                "USAGE: asanti [options] <asn_schema_file>"
                        + NEW_LINE
                        + "    asanti [options] <asn_ber_file>"
                        + NEW_LINE
                        + "    asanti [options] <asn_schema_file> <asn_ber_file> <top_level_type>"
                        + NEW_LINE
                        + NEW_LINE
                        + "Where:"
                        + NEW_LINE
                        + "    asn_schema_file        the ASN.1 schema file to parse (must end in '.asn')"
                        + NEW_LINE
                        + "    asn_ber_file           the ASN.1 BER file to parse (must end in '.ber')"
                        + NEW_LINE
                        + "    top_level_type         the name of the top level type in the schema file";

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(callPattern, "Options:", getOptions(), NEW_LINE + footerMessage);
    }
}
