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

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

import static com.brightsparklabs.asanti.common.ByteArrays.toHexString;

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
            logger.debug(ASN1Dump.dumpAsString(asnObject));

            final Map<String, byte[]> tagsToData = Maps.newHashMap();
            logger.debug("processDerObject: {}", asnObject.toString());

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
    private static void processDerObject(DERObject derObject, String prefix, Map<String, byte[]> tagsToData) throws IOException
    {
        processDerObject(derObject, prefix, tagsToData, false);
    }

    private static void processDerObject(DERObject derObject, String prefix, Map<String, byte[]> tagsToData, boolean explicit) throws IOException
    {
        // TODO MJF
        logger.debug("{}DerObject entry,  prefix {}, tagsToData.size() {} => {} : {}", indent(), prefix, tagsToData.size(), toHexString(derObject.getDEREncoded()), derObject);

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
            processTaggedObject((ASN1TaggedObject) derObject, prefix, tagsToData);
        }
        else if (derObject instanceof DERApplicationSpecific)
        {
            processApplicationSpecific((DERApplicationSpecific) derObject, prefix, tagsToData);
        }
        else
        {
            processPrimitiveDerObject(derObject, prefix, tagsToData, explicit);
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
    private static void processSequence(ASN1Sequence asnSequence, String prefix, Map<String, byte[]> tagsToData, boolean explicit) throws IOException
    {
        processElementsFromSequenceOrSet(asnSequence.getObjects(), prefix, tagsToData, explicit);
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
    private static void processSet(ASN1Set asnSet, String prefix, Map<String, byte[]> tagsToData, boolean explicit) throws IOException
    {
        processElementsFromSequenceOrSet(asnSet.getObjects(), prefix, tagsToData, explicit);
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
    private static int globalIndent = 0;
    private static String indent()
    {
        String indent = "";
        for(int i=0; i < globalIndent;i++) indent+= "    ";
        return indent;
    }
    private static void processElementsFromSequenceOrSet(Enumeration<?> elements, String prefix, Map<String, byte[]> tagsToData, boolean explicit)
            throws IOException
    {
        // TODO MJF
        logger.debug("{}ElementsFromSequenceOrSet prefix {}, tagsToData.size() {}", indent(), prefix, tagsToData.size());
        globalIndent++;
        int index = 0;
        while (elements.hasMoreElements())
        {
            final Object obj = elements.nextElement();

            //final DERObject derObject = (obj instanceof DERObject) ? (DERObject) obj
            //        : ((DEREncodable) obj).getDERObject();
            DERObject derObject;
            if (obj instanceof DERObject)
            {
                derObject = (DERObject) obj;
            }
            else
            {
                derObject = ((DEREncodable) obj).getDERObject();
            }
            // if object is not tagged, then include index in prefix
            String isExplicit = "not set";
            //boolean explicit = false;
            if (derObject instanceof ASN1TaggedObject)
            {
                isExplicit = ((ASN1TaggedObject) derObject).isExplicit() ? "true" : "false";
                //explicit = ((ASN1TaggedObject) derObject).isExplicit() ? true : false;
            }


            boolean isTagged = (derObject instanceof ASN1TaggedObject);
            if (!isTagged)
            {
                logger.debug("not tagged is type: {}", derObject.getClass().getName() );
            }
            if (explicit)
            {
                logger.debug("Explicit Sequence");
                logger.debug("Explicit Sequence is {} {}", derObject.getClass().getName(), (derObject instanceof ASN1Sequence) ? "ASN1Sequence" : "");
            }
            String elementPrefix = isTagged ? prefix
                    : String.format("%s[%d]", prefix, index);
            //final String elementPrefix = (!explicit && isTagged) ? prefix
            //        : String.format("%s[%d]", prefix, index);
            //String elementPrefix;
            if (!isTagged) elementPrefix = String.format("%s[%d]", prefix, index);
            //if (!isTagged) elementPrefix = String.format("%s(u.Sequence.%d)", prefix, index);
            //if (!isTagged) { elementPrefix = prefix; explicit = true; }
            //else if (explicit) elementPrefix = String.format("%s/u.Sequence[%d]", prefix, index);
            //else if (explicit) elementPrefix = String.format("%s/u.Sequence.%d", prefix, index);
            else if (explicit) elementPrefix = String.format("%s(u.Sequence.%d)", prefix, index);
            else elementPrefix = prefix;

            logger.debug("elementPrefix {}, isExplicit {} isTagged {} explicit {}", elementPrefix, isExplicit, isTagged, explicit);
            processDerObject(derObject, elementPrefix, tagsToData, explicit);
            index++;
        }
        globalIndent--;
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

    private static boolean inTagged = false;
    private static void processTaggedObject(ASN1TaggedObject asnTaggedObject, String prefix, Map<String, byte[]> tagsToData)
            throws IOException
    {
        // TODO MJF
        boolean isExplicit = asnTaggedObject.isExplicit();

        DERObject obj = asnTaggedObject.getObject();


        inTagged = true;
        logger.debug("{}TaggedObject entry - prefix {}, adding {}, explicit {} ", indent(), prefix, asnTaggedObject.getTagNo(), asnTaggedObject.isExplicit());
        prefix = prefix + "/" + asnTaggedObject.getTagNo();
        processDerObject(asnTaggedObject.getObject(), prefix, tagsToData, isExplicit);
        inTagged = false;
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
    private static void processPrimitiveDerObject(DERObject derObject, String tag, Map<String, byte[]> tagsToData, boolean explicit) throws IOException
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

        // TODO MJF
        if (explicit)
        {
            String sss = derObject.toString();
            // add a faux tag.
            tag += "(u." + universalTagToAsnBuiltinType(t) + ")";
            //tag += "/u." + universalTagToAsnBuiltinType(t);
            // to get back to the AsnBuiltinType
            AsnBuiltinType type = AsnBuiltinType.valueOf(universalTagToAsnBuiltinType(t).toString());
            int breakpoint = 0;

        }
        logger.debug("{}PrimitiveDerObject.  T: {} {},  tagsToData.put {} : {} - object => {}",
                indent(),
                (explicit ? " UNIVERSAL" : "context-specific"),
                t,
                tag,
                toHexString(value),
                toHexString(derObject.getDEREncoded()));
        tagsToData.put(tag, value);
    }


    private static AsnBuiltinType universalTagToAsnBuiltinType(int universalTag)
    {
        switch (universalTag)
        {
            case 1: return AsnBuiltinType.Boolean;
            case 2: return AsnBuiltinType.Integer;
            case 3: return AsnBuiltinType.BitString;
            case 4: return AsnBuiltinType.OctetString;
            // ...
            case 12: return AsnBuiltinType.Utf8String;
            default: return AsnBuiltinType.Null;
        }
    }
}
