/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERApplicationSpecific;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.util.ASN1Dump;

import com.brightsparklabs.asanti.model.data.AsnData;
import com.brightsparklabs.asanti.model.data.AsnDataImpl;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Reads data from ASN.1 BER/DER binary files
 *
 * @author brightSPARK Labs
 */
public class AsnBerFileReader
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static Logger log = Logger.getLogger(AsnBerFileReader.class.getName());

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
    public static ImmutableList<AsnData> read(File berFile) throws IOException
    {
        return read(berFile, 0);
    }

    /**
     * Reads the supplied ASN.1 BER/DER binary file and stops reading when it
     * has read the specified number of PDUs
     *
     * @param berFile
     *            file to decode
     *
     * @param maxPDUs
     *            number of PDUs to read from the file. The returned list will
     *            be less than or equal to this value. Set to {@code 0} for no
     *            maximum (or use {@link #read(File)} instead).
     *
     * @return list of {@link AsnData} objects found in the file
     *
     * @throws IOException
     *             if any errors occur reading from the file
     */
    public static ImmutableList<AsnData> read(File berFile, int maxPDUs) throws IOException
    {
        final FileInputStream fileInputStream = new FileInputStream(berFile);
        final ASN1InputStream asnInputStream = new ASN1InputStream(fileInputStream);

        final List<AsnData> result = Lists.newArrayList();

        DERObject asnObject = asnInputStream.readObject();
        while (asnObject != null)
        {
            log.fine(ASN1Dump.dumpAsString(asnObject));

            final Map<String, byte[]> tagsToData = Maps.newHashMap();
            processDerObject(asnObject, "", tagsToData);
            final AsnData asnData = new AsnDataImpl(tagsToData);
            result.add(asnData);
            asnObject = asnInputStream.readObject();

            /*
             * NOTE: do not use '>=' in the below if statement as we use 0 (or
             * negative numbers) to indicate no limit. We cannot use
             * Integer#MAX_VALUE as iterables do not need to be limited to this
             * size
             */
            if (result.size() == maxPDUs)
            {
                break;
            }
        }

        asnInputStream.close();

        return ImmutableList.copyOf(result);
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * Processes a DER object and stores the tags/data found in it
     *
     * @param derObject
     *            object to process
     *
     * @param prefix
     *            prefix to prepend to any tags found
     *
     * @param tagsToData
     *            storage for the tags/data found
     *
     * @throws IOException
     *             if any errors occur reading from the file
     */
    private static void processDerObject(DERObject derObject, String prefix, Map<String, byte[]> tagsToData)
            throws IOException
    {
        if (derObject instanceof ASN1Sequence)
        {
            processSequence((ASN1Sequence) derObject, prefix, tagsToData);
        }
        else if (derObject instanceof ASN1Set)
        {
            processSet((ASN1Set) derObject, prefix, tagsToData);
        }
        else if (derObject instanceof ASN1TaggedObject)
        {
            processTaggedObject((ASN1TaggedObject) derObject, prefix, tagsToData);
        }
        else if (derObject instanceof DERApplicationSpecific)
        {
            processApplicationSpecific((DERApplicationSpecific) derObject, prefix, tagsToData);
        }
        else
        {
            processPrimitiveDerObject(derObject, prefix, tagsToData);
        }
    }

    /**
     * Processes an ASN.1 'Sequence' and stores the tags/data found in it
     *
     * @param asnSequence
     *            sequence to process
     *
     * @param prefix
     *            prefix to prepend to any tags found
     *
     * @param tagsToData
     *            storage for the tags/data found
     *
     * @throws IOException
     *             if any errors occur reading from the file
     */
    private static void processSequence(ASN1Sequence asnSequence, String prefix, Map<String, byte[]> tagsToData)
            throws IOException
    {
        processElementsFromSequenceOrSet(asnSequence.getObjects(), prefix, tagsToData);
    }

    /**
     * Processes an ASN.1 'Set' and stores the tags/data found in it
     *
     * @param asnSet
     *            set to process
     *
     * @param prefix
     *            prefix to prepend to any tags found
     *
     * @param tagsToData
     *            storage for the tags/data found
     *
     * @throws IOException
     *             if any errors occur reading from the file
     */
    private static void processSet(ASN1Set asnSet, String prefix, Map<String, byte[]> tagsToData) throws IOException
    {
        processElementsFromSequenceOrSet(asnSet.getObjects(), prefix, tagsToData);
    }

    /**
     * Processes the elements found in an ASN.1 'Sequence' or ASN.1 'Set' stores
     * the tags/data found in them
     *
     * @param elements
     *            elements from the sequence or set
     *
     * @param prefix
     *            prefix to prepend to any tags found
     *
     * @param tagsToData
     *            storage for the tags/data found
     *
     * @throws IOException
     *             if any errors occur reading from the file
     */
    private static void processElementsFromSequenceOrSet(Enumeration<?> elements, String prefix,
            Map<String, byte[]> tagsToData) throws IOException
    {
        int index = 0;
        while (elements.hasMoreElements())
        {
            final Object obj = elements.nextElement();
            final DERObject derObject =
                    (obj instanceof DERObject) ? (DERObject) obj : ((DEREncodable) obj).getDERObject();
            // if object is not tagged, then include index in prefix
            final String elementPrefix =
                    (derObject instanceof ASN1TaggedObject) ? prefix : String.format("%s[%d]", prefix, index);
            processDerObject(derObject, elementPrefix, tagsToData);
            index++;
        }
    }

    /**
     * Processes an ASN.1 'Tagged' object and stores the tags/data found in it
     *
     * @param asnTaggedObject
     *            object to process
     *
     * @param prefix
     *            prefix to prepend to any tags found
     *
     * @param tagsToData
     *            storage for the tags/data found
     *
     * @throws IOException
     *             if any errors occur reading from the file
     */
    private static void processTaggedObject(ASN1TaggedObject asnTaggedObject, String prefix,
            Map<String, byte[]> tagsToData) throws IOException
    {
        prefix = prefix + "/" + asnTaggedObject.getTagNo();
        processDerObject(asnTaggedObject.getObject(), prefix, tagsToData);
    }

    /**
     * Processes an ASN.1 'ApplicationSpecific' object and stores the tags/data
     * found in it
     *
     * @param asnApplicationSpecific
     *            object to process
     *
     * @param prefix
     *            prefix to prepend to any tags found
     *
     * @param tagsToData
     *            storage for the tags/data found
     *
     * @throws IOException
     *             if any errors occur reading from the file
     */
    private static void processApplicationSpecific(DERApplicationSpecific asnApplicationSpecific, String prefix,
            Map<String, byte[]> tagsToData) throws IOException
    {
        prefix = prefix + "/" + asnApplicationSpecific.getApplicationTag();
        processDerObject(asnApplicationSpecific.getObject(), prefix, tagsToData);
    }

    /**
     * Processes an ASN.1 'primitive' object and stores the binary data in it
     * against the supplied tag
     *
     * @param derObject
     *            object to process
     *
     * @param tag
     *            tag to associate the data with
     *
     * @param tagsToData
     *            storage for the data found
     *
     * @throws IOException
     *             if any errors occur reading from the file
     */
    private static void processPrimitiveDerObject(DERObject derObject, String tag, Map<String, byte[]> tagsToData)
            throws IOException
    {
        // get the bytes representing Tag-Length-Value
        final byte[] tlvData = derObject.getEncoded();
        // extract and store value
        final byte[] value = (tlvData.length < 3) ? new byte[0] : Arrays.copyOfRange(tlvData, 2, tlvData.length);
        tagsToData.put(tag, value);
    }
}
