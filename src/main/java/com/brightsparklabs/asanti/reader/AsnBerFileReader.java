/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader;

import com.brightsparklabs.asanti.model.data.AsnData;
import com.brightsparklabs.asanti.model.data.AsnDataImpl;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.tag.AsnSchemaTag;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import static com.brightsparklabs.asanti.common.ByteArrays.*;

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
    private static final Logger logger = LoggerFactory.getLogger(AsnBerFileReader.class);

    /** this is used to construct tags of the UNIVERSAL type */
    //private static final String TAG_FORMATTER = "%s/%d.%s";
    //private static final String TAG_FORMATTER_UNIVERSAL = "%s/%d.u.%s";
    private static final String TAG_FORMATTER = "%s/%d[%s]";
    private static final String TAG_FORMATTER_UNIVERSAL = "%s/%d[UNIVERSAL %s]";

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Reads the supplied ASN.1 BER/DER binary file
     *
     * @param berFile
     *         file to decode
     *
     * @return list of {@link AsnData} objects found in the file
     *
     * @throws IOException
     *         if any errors occur reading from the file
     */
    public static ImmutableList<AsnData> read(File berFile) throws IOException
    {
        return read(berFile, 0);
    }

    /**
     * Reads the supplied ASN.1 BER/DER binary file and stops reading when it has read the specified
     * number of PDUs
     *
     * @param berFile
     *         file to decode
     * @param maxPDUs
     *         number of PDUs to read from the file. The returned list will be less than or equal to
     *         this value. Set to {@code 0} for no maximum (or use {@link #read(File)} instead).
     *
     * @return list of {@link AsnData} objects found in the file
     *
     * @throws IOException
     *         if any errors occur reading from the file
     */
    public static ImmutableList<AsnData> read(File berFile, int maxPDUs) throws IOException
    {
        final FileInputStream fileInputStream = new FileInputStream(berFile);
        final ASN1InputStream asnInputStream = new ASN1InputStream(fileInputStream);

        final List<AsnData> result = Lists.newArrayList();

        int index = 0;
        DERObject asnObject = asnInputStream.readObject();
        while (asnObject != null)
        {
            logger.trace(ASN1Dump.dumpAsString(asnObject));

            //final Map<String, byte[]> tagsToData = Maps.newHashMap();
            final Map<String, byte[]> tagsToData
                    = Maps.newLinkedHashMap();  // we want to preserve input order
            logger.trace("processDerObject {}: {}", index, asnObject.toString());

            processDerObject(asnObject, "", tagsToData);
            final AsnData asnData = new AsnDataImpl(tagsToData);
            result.add(asnData);
            asnObject = asnInputStream.readObject();
            index++;

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
     *         object to process
     * @param prefix
     *         prefix to prepend to any tags found
     * @param tagsToData
     *         storage for the tags/data found
     *
     * @throws IOException
     *         if any errors occur reading from the file
     */
    private static void processDerObject(DERObject derObject, String prefix,
            Map<String, byte[]> tagsToData) throws IOException
    {
        processDerObject(derObject, prefix, tagsToData, false, -1, false);
    }

    private static void processDerObject(DERObject derObject, String prefix,
            Map<String, byte[]> tagsToData, boolean explicit, int index, boolean isTagged)
            throws IOException
    {
        // TODO MJF
        logger.trace("{}DerObject entry,  prefix {}, tagsToData.size() {} => {} : {}",
                indent(),
                prefix,
                tagsToData.size(),
                toHexString(derObject.getDEREncoded()),
                derObject);

        if (derObject instanceof ASN1Sequence)
        {
            processSequence((ASN1Sequence) derObject, prefix, tagsToData, explicit);
        }
        else if (derObject instanceof ASN1Set)
        {
            processSet((ASN1Set) derObject, prefix, tagsToData, explicit);
        }
        else if (derObject instanceof ASN1TaggedObject)
        {
            processTaggedObject((ASN1TaggedObject) derObject, prefix, tagsToData, index);
        }
        else if (derObject instanceof DERApplicationSpecific)
        {
            processApplicationSpecific((DERApplicationSpecific) derObject, prefix, tagsToData);
        }
        else
        {
            processPrimitiveDerObject(derObject, prefix, tagsToData, index, isTagged);
        }
    }

    /**
     * Processes an ASN.1 'Sequence' and stores the tags/data found in it
     *
     * @param asnSequence
     *         sequence to process
     * @param prefix
     *         prefix to prepend to any tags found
     * @param tagsToData
     *         storage for the tags/data found
     *
     * @throws IOException
     *         if any errors occur reading from the file
     */
    private static void processSequence(ASN1Sequence asnSequence, String prefix,
            Map<String, byte[]> tagsToData, boolean explicit) throws IOException
    {
        processElementsFromSequenceOrSet(asnSequence.getObjects(),
                prefix,
                tagsToData,
                explicit,
                AsnBuiltinType.Sequence);
    }

    /**
     * Processes an ASN.1 'Set' and stores the tags/data found in it
     *
     * @param asnSet
     *         set to process
     * @param prefix
     *         prefix to prepend to any tags found
     * @param tagsToData
     *         storage for the tags/data found
     *
     * @throws IOException
     *         if any errors occur reading from the file
     */
    private static void processSet(ASN1Set asnSet, String prefix, Map<String, byte[]> tagsToData,
            boolean explicit) throws IOException
    {
        processElementsFromSequenceOrSet(asnSet.getObjects(),
                prefix,
                tagsToData,
                explicit,
                AsnBuiltinType.Set);
    }

    /**
     * Processes the elements found in an ASN.1 'Sequence' or ASN.1 'Set' stores the tags/data found
     * in them
     *
     * @param elements elements from the sequence or set
     * @param prefix prefix to prepend to any tags found
     * @param tagsToData storage for the tags/data found
     * @throws IOException if any errors occur reading from the file
     */
    // TODO MJF - clean up of get rid of.
    private static int globalIndent = 0;

    private static String indent()
    {
        String indent = "";
        for (int i = 0; i < globalIndent; i++) indent += "    ";
        return indent;
    }

    private static void processElementsFromSequenceOrSet(Enumeration<?> elements, String prefix,
            Map<String, byte[]> tagsToData, boolean explicit, AsnBuiltinType type)
            throws IOException
    {
        // TODO MJF
        logger.trace("{}ElementsFromSequenceOrSet prefix {}, tagsToData.size() {}",
                indent(),
                prefix,
                tagsToData.size());
        globalIndent++;
        int index = 0;
        while (elements.hasMoreElements())
        {
            final Object obj = elements.nextElement();

            final DERObject derObject = (obj instanceof DERObject) ?
                    (DERObject) obj :
                    ((DEREncodable) obj).getDERObject();

            boolean isTagged = (derObject instanceof ASN1TaggedObject);
            if (!isTagged)
            {
                logger.trace("not tagged is type: {}", derObject.getClass().getName());
            }
            String elementPrefix = prefix;

            if (!isTagged)
            {
                // Ideally we would extract Choice this way too, but since a Choice does not get
                // encoded we have to extract that using the schema during the decoding stage.
                if (derObject instanceof ASN1Sequence)
                {
                    elementPrefix = String.format(TAG_FORMATTER_UNIVERSAL,
                            prefix,
                            index,
                            AsnSchemaTag.getUniversalTagForBuiltInType(AsnBuiltinType.Sequence));
                }
                else if (derObject instanceof ASN1Set)
                {
                    elementPrefix = String.format(TAG_FORMATTER_UNIVERSAL,
                            prefix,
                            index,
                            AsnSchemaTag.getUniversalTagForBuiltInType(AsnBuiltinType.Set));
                }
            }
            else if (explicit)
            {
                elementPrefix = String.format(TAG_FORMATTER_UNIVERSAL,
                        prefix,
                        index,
                        AsnSchemaTag.getUniversalTagForBuiltInType(type));
            }

            logger.trace("elementPrefix {}, isTagged {} explicit {}, index {}",
                    elementPrefix,
                    isTagged,
                    explicit,
                    index);
            processDerObject(derObject, elementPrefix, tagsToData, explicit, index, isTagged);
            index++;
        }
        globalIndent--;
    }

    /**
     * Processes an ASN.1 'Tagged' object and stores the tags/data found in it
     *
     * @param asnTaggedObject
     *         object to process
     * @param prefix
     *         prefix to prepend to any tags found
     * @param tagsToData
     *         storage for the tags/data found
     *
     * @throws IOException
     *         if any errors occur reading from the file
     */

    private static void processTaggedObject(ASN1TaggedObject asnTaggedObject, String prefix,
            Map<String, byte[]> tagsToData, int index) throws IOException
    {
        // TODO MJF
        boolean isExplicit = asnTaggedObject.isExplicit();

        DERObject obj = asnTaggedObject.getObject();

        prefix = String.format(TAG_FORMATTER, prefix, index, asnTaggedObject.getTagNo());

        byte[] bytes = obj.getDEREncoded();
        if ((bytes[0] & 0xE0) == 0xA0)
        {
            // This is a "constructed" type
            index = 0;
        }

        logger.trace("{}TaggedObject entry - prefix {}, adding {}, explicit {} ",
                indent(),
                prefix,
                asnTaggedObject.getTagNo(),
                asnTaggedObject.isExplicit());

        processDerObject(asnTaggedObject.getObject(),
                prefix,
                tagsToData,
                isExplicit,
                index,
                !isExplicit);
    }

    /**
     * Processes an ASN.1 'ApplicationSpecific' object and stores the tags/data found in it
     *
     * @param asnApplicationSpecific
     *         object to process
     * @param prefix
     *         prefix to prepend to any tags found
     * @param tagsToData
     *         storage for the tags/data found
     *
     * @throws IOException
     *         if any errors occur reading from the file
     */
    private static void processApplicationSpecific(DERApplicationSpecific asnApplicationSpecific,
            String prefix, Map<String, byte[]> tagsToData) throws IOException
    {
        prefix = prefix + "/" + asnApplicationSpecific.getApplicationTag();
        processDerObject(asnApplicationSpecific.getObject(), prefix, tagsToData);
    }

    /**
     * Processes an ASN.1 'primitive' object and stores the binary data in it against the supplied
     * tag
     *
     * @param derObject
     *         object to process
     * @param tag
     *         tag to associate the data with
     * @param tagsToData
     *         storage for the data found
     *
     * @throws IOException
     *         if any errors occur reading from the file
     */
    private static void processPrimitiveDerObject(DERObject derObject, String tag,
            Map<String, byte[]> tagsToData, int index, boolean isTagged) throws IOException
    {
        // get the bytes representing Tag-Length-Value
        final byte[] tlvData = derObject.getEncoded();

        int t = tlvData[0] & 0xff;
        // extract the value
        byte[] value = new byte[0];
        // must be at least three octets to contain a value
        if (tlvData.length >= 3)
        {
            // determine the number of bytes which define the length
            int numberOfAdditionalLengthBytes = 0;
            final int length = tlvData[1] & 0xff;
            if (length > 127)
            {
                // more than one byte used to specify length
                numberOfAdditionalLengthBytes = length & 0x7f;
            }

            // extract and store value
            final int firstDataByteIndex = 2 + numberOfAdditionalLengthBytes;
            value = Arrays.copyOfRange(tlvData, firstDataByteIndex, tlvData.length);
        }

        if (!isTagged)
        {
            // add a faux tag.
            tag = String.format(TAG_FORMATTER_UNIVERSAL, tag, index, t);
        }
        logger.trace("{}PrimitiveDerObject.  T: [{}],  tagsToData.put {} : {} - object => {}",
                indent(),
                (!isTagged ? ("UNIVERSAL " + t) : index),
                tag,
                toHexString(value),
                toHexString(derObject.getDEREncoded()));

        tagsToData.put(tag, value);
    }
}
