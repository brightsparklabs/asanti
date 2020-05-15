/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.reader;

import com.brightsparklabs.asanti.model.data.RawAsnData;
import com.brightsparklabs.asanti.model.data.RawAsnDataImpl;
import com.brightsparklabs.asanti.model.schema.tag.AsnSchemaTag;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.ByteSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import org.bouncycastle.asn1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reads data from ASN.1 BER/DER binary files
 *
 * @author brightSPARK Labs
 */
public class AsnBerDataReader {
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(AsnBerDataReader.class);

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Reads the supplied ASN.1 BER/DER binary data
     *
     * @param source data to decode
     * @return list of {@link RawAsnData} objects found in the data
     * @throws IOException if any errors occur reading the data
     */
    public static ImmutableList<RawAsnData> read(ByteSource source) throws IOException {
        return read(source, 0);
    }

    /**
     * Reads the supplied ASN.1 BER/DER binary data and stops reading when it has read the specified
     * number of PDUs
     *
     * @param source data to decode
     * @param maxPDUs number of PDUs to read from the data. The returned list will be less than or
     *     equal to this value. Set to {@code 0} for no maximum (or use {@link #read(ByteSource)}
     *     instead).
     * @return list of {@link RawAsnData} objects found in the data
     * @throws IOException if any errors occur reading the data
     */
    public static ImmutableList<RawAsnData> read(ByteSource source, int maxPDUs)
            throws IOException {
        final InputStream inputStream = source.openStream();
        final ASN1InputStream asnInputStream = new ASN1InputStream(inputStream);

        final List<RawAsnData> result = Lists.newArrayList();

        ASN1Primitive asnObject = asnInputStream.readObject();
        while (asnObject != null) {
            final Map<String, byte[]> tagsToData =
                    Maps.newLinkedHashMap(); // we want to preserve input order

            processDerObject(asnObject, "", tagsToData, 0);
            final RawAsnData rawAsnData = new RawAsnDataImpl(tagsToData);
            result.add(rawAsnData);
            asnObject = asnInputStream.readObject();

            /*
             * NOTE: do not use '>=' in the below if statement as we use 0 (or
             * negative numbers) to indicate no limit. We cannot use
             * Integer#MAX_VALUE as iterables do not need to be limited to this
             * size
             */
            if (result.size() == maxPDUs) {
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
     * @param derObject object to process
     * @param prefix prefix to prepend to any tags found
     * @param tagsToData storage for the tags/data found
     * @param index the index of this item within the parent container
     * @throws IOException if any errors occur reading from the file
     */
    private static void processDerObject(
            ASN1Primitive derObject, String prefix, Map<String, byte[]> tagsToData, int index)
            throws IOException {
        if (derObject instanceof ASN1Sequence) {
            processSequence((ASN1Sequence) derObject, prefix, tagsToData);
        } else if (derObject instanceof ASN1Set) {
            processSet((ASN1Set) derObject, prefix, tagsToData);
        } else if (derObject instanceof ASN1TaggedObject) {
            processTaggedObject((ASN1TaggedObject) derObject, prefix, tagsToData, index);
        } else if (derObject instanceof DERApplicationSpecific) {
            processApplicationSpecific(
                    (DERApplicationSpecific) derObject, prefix, tagsToData, index);
        } else {
            processPrimitiveDerObject(derObject, prefix, tagsToData);
        }
    }

    /**
     * Processes an ASN.1 'Sequence' and stores the tags/data found in it
     *
     * @param asnSequence sequence to process
     * @param prefix prefix to prepend to any tags found
     * @param tagsToData storage for the tags/data found
     * @throws IOException if any errors occur reading from the file
     */
    private static void processSequence(
            ASN1Sequence asnSequence, String prefix, Map<String, byte[]> tagsToData)
            throws IOException {
        processElementsFromSequenceOrSet(asnSequence.getObjects(), prefix, tagsToData);
    }

    /**
     * Processes an ASN.1 'Set' and stores the tags/data found in it
     *
     * @param asnSet set to process
     * @param prefix prefix to prepend to any tags found
     * @param tagsToData storage for the tags/data found
     * @throws IOException if any errors occur reading from the file
     */
    private static void processSet(ASN1Set asnSet, String prefix, Map<String, byte[]> tagsToData)
            throws IOException {
        processElementsFromSequenceOrSet(asnSet.getObjects(), prefix, tagsToData);
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
    private static void processElementsFromSequenceOrSet(
            Enumeration<?> elements, String prefix, Map<String, byte[]> tagsToData)
            throws IOException {
        int index = 0;
        while (elements.hasMoreElements()) {
            final Object obj = elements.nextElement();
            final ASN1Primitive derObject =
                    (obj instanceof ASN1Primitive)
                            ? (ASN1Primitive) obj
                            : ((ASN1Encodable) obj).toASN1Primitive();

            boolean isTagged = (derObject instanceof ASN1TaggedObject);
            String elementPrefix = prefix;

            if (!isTagged) {
                // Because this type is not tagged then we need to add a universal tag
                int tagNumber = getTagNumber(getType(derObject));
                elementPrefix = prefix + "/" + AsnSchemaTag.createRawTagUniversal(index, tagNumber);
            }

            processDerObject(derObject, elementPrefix, tagsToData, index);
            index++;
        }

        if (index == 0) {
            // Then there were no elements found in the Sequence or Set.  (Having an empty
            // Sequence/Set is valid, for example all the components could be OPTIONAL)
            // Make an empty data object against this tag so that we know we received the
            // Constructed type as this is important for decoding and validation.
            tagsToData.put(prefix, new byte[0]);
        }
    }

    /**
     * Processes an ASN.1 'Tagged' object and stores the tags/data found in it
     *
     * @param asnTaggedObject object to process
     * @param prefix prefix to prepend to any tags found
     * @param tagsToData storage for the tags/data found
     * @throws IOException if any errors occur reading from the file
     */
    private static void processTaggedObject(
            ASN1TaggedObject asnTaggedObject,
            String prefix,
            Map<String, byte[]> tagsToData,
            int index)
            throws IOException {

        ASN1Primitive obj = asnTaggedObject.getObject();

        prefix =
                prefix
                        + "/"
                        + AsnSchemaTag.createRawTag(
                                index, String.valueOf(asnTaggedObject.getTagNo()));

        int containingType = getType(asnTaggedObject);
        if (isConstructedType(containingType)) {
            index = 0;
        }

        int type = getType(obj);

        // We are looking for where UNIVERSAL tags are used in the encoding, so that we can insert
        // appropriate tags.
        // As part of extracting an object from a tagged object (asnTaggedObject.getObject() above)
        // if it is not explicit (ie it is not a tag around a universal) then the library seems
        // to just give it a universal type of either 16 or 4 (Sequence for constructed and
        // Octet String otherwise)
        // This means that we need to check for isExplicit as well as whether it is a Universal type
        if (asnTaggedObject.isExplicit() && isUniversalType(type)) {
            int number = getTagNumber(type);
            prefix = prefix + "/" + AsnSchemaTag.createRawTagUniversal(index, number);
        }

        processDerObject(asnTaggedObject.getObject(), prefix, tagsToData, index);
    }

    /**
     * Processes an ASN.1 'ApplicationSpecific' object and stores the tags/data found in it
     *
     * @param asnApplicationSpecific object to process
     * @param prefix prefix to prepend to any tags found
     * @param tagsToData storage for the tags/data found
     * @throws IOException if any errors occur reading from the file
     */
    private static void processApplicationSpecific(
            DERApplicationSpecific asnApplicationSpecific,
            String prefix,
            Map<String, byte[]> tagsToData,
            int index)
            throws IOException {
        prefix = prefix + "/" + asnApplicationSpecific.getApplicationTag();
        processDerObject(asnApplicationSpecific.getObject(), prefix, tagsToData, index);
    }

    /**
     * Processes an ASN.1 'primitive' object and stores the binary data in it against the supplied
     * tag
     *
     * @param derObject object to process
     * @param tag tag to associate the data with
     * @param tagsToData storage for the data found
     * @throws IOException if any errors occur reading from the file
     */
    private static void processPrimitiveDerObject(
            ASN1Primitive derObject, String tag, Map<String, byte[]> tagsToData)
            throws IOException {
        // get the bytes representing Tag-Length-Value
        final byte[] tlvData = derObject.getEncoded();

        // extract the value
        byte[] value = new byte[0];
        // must be at least three octets to contain a value
        if (tlvData.length >= 3) {
            // determine the number of bytes which define the length
            int numberOfAdditionalLengthBytes = 0;
            final int length = tlvData[1] & 0xff;
            if (length > 127) {
                // more than one byte used to specify length
                numberOfAdditionalLengthBytes = length & 0x7f;
            }

            // extract and store value
            final int firstDataByteIndex = 2 + numberOfAdditionalLengthBytes;
            value = Arrays.copyOfRange(tlvData, firstDataByteIndex, tlvData.length);
        }

        tagsToData.put(tag, value);
    }

    /**
     * Returns the T from a TLV (Type Length Value) triplet from the ASN1Primitive object
     *
     * @param object the ASN1Primitive to extract the T from
     * @return the T from the TLV
     */
    private static int getType(ASN1Primitive object) throws IOException {
        return object.getEncoded()[0] & 0xff;
    }

    /**
     * Encoding uses TLV (Type Length Value) triplets. This function is checking whether the T is a
     * Universal tag
     *
     * @param type The T from the TLV encoding
     * @return true if the type is Universal
     */
    private static boolean isUniversalType(int type) {
        return ((type & 0xC0) == 0);
    }

    /**
     * This masks out the Tag Class and the Constructed/Primitive bits and returns just the number
     *
     * @param type the T from the TLV triplet
     * @return the number part of the tag (the lower 5 bits).
     */
    private static int getTagNumber(int type) {
        return type & 0x1F;
    }

    /**
     * Encoding uses TLV (Type Length Value) triplets. This function is checking whether the T is a
     * Constructed type (bit 5 is 1)
     *
     * @param type The T from the TLV encoding
     * @return true if the type is Constructed
     */
    private static boolean isConstructedType(int type) {
        return (type & 0x20) == 0x20;
    }
}
