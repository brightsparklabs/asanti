/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.logic;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import com.brightsparklabs.asanti.model.AsnData;
import com.brightsparklabs.asanti.reader.AsnBerFileReader;
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
            final File asnFile = new File(filename);
            final ImmutableList<AsnData> data = readAsnBerFile(asnFile);
            int count = 0;
            for (AsnData asnData : data)
            {
                log.info("PDU[" + count + "]");
                final Map<String, byte[]> tagsData = asnData.getData();

                for (String tag : Ordering.natural().immutableSortedCopy(tagsData.keySet()))
                {
                    log.info("\t" + tag + ": 0x" + BaseEncoding.base16().encode(tagsData.get(tag)));
                }
                count++;
            }
        }
        catch (Exception ex)
        {
            log.severe("Could not parser file - " + ex.getMessage());
        }
    }

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
}
