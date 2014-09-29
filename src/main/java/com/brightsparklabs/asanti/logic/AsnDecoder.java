/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERApplicationSpecific;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.util.ASN1Dump;

import com.brightsparklabs.asanti.model.AsnData;
import com.brightsparklabs.asanti.model.AsnDataDefault;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.google.common.io.BaseEncoding;

/**
 * Logic for decoding tagged/application ASN.1 data
 *
 * @author brightSPARK Labs <enquire@brightsparklabs.com>
 */
public class AsnDecoder
{

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    public static void main(String[] args) throws IOException
    {
        final File asnFile = new File(args[0]);
        final ImmutableSet<AsnData> data = decodeFile(asnFile);
        int count = 0;
        for (AsnData asnData : data)
        {
            System.err.println("PDU[" + count + "]");
            final Map<String, byte[]> tagsData = asnData.getData();

            for (String tag : Ordering.natural().immutableSortedCopy(tagsData.keySet()))
            {
                System.err.println("\t" + tag + ": 0x" + BaseEncoding.base16().encode(tagsData.get(tag)));
            }
            count++;
        }
    }

    public static ImmutableSet<AsnData> decodeFile(File berFile) throws IOException
    {
        final FileInputStream fileInputStream = new FileInputStream(berFile);
        final ASN1InputStream asnInputStream = new ASN1InputStream(fileInputStream);

        final Set<AsnData> result = Sets.newHashSet();

        DERObject asnObject = asnInputStream.readObject();
        while (asnObject != null)
        {
            System.out.println(ASN1Dump.dumpAsString(asnObject));

            final Map<String, byte[]> tagsToData = Maps.newHashMap();
            processDerObject(asnObject, "", tagsToData);
            final AsnData asnData = new AsnDataDefault(tagsToData);
            result.add(asnData);
            asnObject = asnInputStream.readObject();
        }

        asnInputStream.close();

        return ImmutableSet.copyOf(result);
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

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

        // TODO: check/handle SetOf and SequenceOf elements
    }

    private static void processSequence(ASN1Sequence asnSequence, String prefix, Map<String, byte[]> tagsToData)
            throws IOException
    {
        final Enumeration<?> enumeration = asnSequence.getObjects();

        while (enumeration.hasMoreElements())
        {
            final Object obj = enumeration.nextElement();
            final DERObject derObject = (obj instanceof DERObject) ? (DERObject) obj : ((DEREncodable) obj)
                    .getDERObject();
            processDerObject(derObject, prefix, tagsToData);
        }
    }

    private static void processSet(ASN1Set asnSet, String prefix, Map<String, byte[]> tagsToData) throws IOException
    {
        final Enumeration<?> enumeration = asnSet.getObjects();

        while (enumeration.hasMoreElements())
        {
            final Object obj = enumeration.nextElement();

            if (obj instanceof DERObject)
            {
                processDerObject((DERObject) obj, prefix, tagsToData);
            }
            else
            {
                processDerObject(((DEREncodable) obj).getDERObject(), prefix, tagsToData);
            }
        }
    }

    private static void processTaggedObject(ASN1TaggedObject asnTaggedObject, String prefix,
            Map<String, byte[]> tagsToData) throws IOException
    {
        prefix = prefix + "/" + asnTaggedObject.getTagNo();
        processDerObject(asnTaggedObject.getObject(), prefix, tagsToData);
    }

    private static void processApplicationSpecific(DERApplicationSpecific asnApplicationSpecific, String prefix,
            Map<String, byte[]> tagsToData) throws IOException
    {
        prefix = prefix + "/" + asnApplicationSpecific.getApplicationTag();
        processDerObject(asnApplicationSpecific.getObject(), prefix, tagsToData);
    }

    private static void processPrimitiveDerObject(DERObject derObject, String tag, Map<String, byte[]> tagsToData)
            throws IOException
    {
        final byte[] data = derObject.getEncoded();
        tagsToData.put(tag, data);
    }
}
