/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.integration;

import com.brightsparklabs.asanti.Asanti;
import com.brightsparklabs.asanti.decoder.AsnByteDecoder;
import com.brightsparklabs.asanti.model.data.AsnData;
import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import com.google.common.io.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigInteger;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Integration tests for {@link Asanti}
 *
 * @author brightSPARK Labs
 */
public class AsantiTest
{
    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(AsantiTest.class);

    @Test
    public void testDecodeAsnData() throws Exception
    {
        /* These results are all tightly coupled to the test data files
         * which is sort of inevitable, but we should keep an eye on how to better
         * manage this dependency/coupling, eg should we auto generate those files etc?
         */

        logger.info("Testing just the ber");
        String berFilename = getClass().getResource("/TestMostSimple.ber").getFile();
        final File berFile = new File(berFilename);

        final ByteSource byteSource = Files.asByteSource(berFile);
        final ImmutableList<AsnData> allAsnData = Asanti.readAsnBerData(byteSource);

        int count = 0;
        for (final AsnData asnData : allAsnData)
        {
            logger.info("PDU[" + count + "]");
            final Map<String, byte[]> tagsData = asnData.getBytes();

            for (final String tag : Ordering.natural().immutableSortedCopy(tagsData.keySet()))
            {
                logger.info("\t {}: 0x{}", tag, BaseEncoding.base16().encode(tagsData.get(tag)));
            }
            count++;
        }

        AsnData asnData = allAsnData.get(0);
        // expecting two tags.
        assertEquals(2, asnData.getRawTags().size());

        byte [] b0 = asnData.getBytes("/0[0]");
        // we 'know' that this is a UTF8String
        String s = AsnByteDecoder.decodeAsUtf8String(b0);
        assertEquals("Adam", s);

        byte [] b1 = asnData.getBytes("/1[1]");
        // we 'know' that this is an integer
        BigInteger big = AsnByteDecoder.decodeAsInteger(b1);
        assertEquals(32, big.intValue());

        assertEquals("Am expecting one PDU", 1, count);
    }

    @Test
    public void testDecodeAsnData1() throws Exception
    {

        logger.info("testing ber against schema");
        final CharSource schemaSource = Resources.asCharSource(getClass().getResource(
                "/TestMostSimple.asn"), Charsets.UTF_8);
        final ByteSource berSource = Resources.asByteSource(getClass().getResource(
                "/TestMostSimple.ber"));

        final ImmutableList<DecodedAsnData> allDecodedData = Asanti.decodeAsnData(berSource,
                schemaSource,
                "Human");

        for (int i = 0; i < allDecodedData.size(); i++)
        {
            logger.info("Parsing PDU[{}]", i);
            final DecodedAsnData pdu = allDecodedData.get(i);
            for (String tag : pdu.getTags())
            {
                logger.info("\t{} => {}", tag, pdu.getHexString(tag).get());
                logger.info("\t\tbuiltinType {} ", pdu.getType(tag).getBuiltinType());
                assertTrue("Tag is found with contains", pdu.contains(tag));
            }
            for (String tag : pdu.getUnmappedTags())
            {
                logger.info("?\t{} => {}", tag, pdu.getHexString(tag).get());
                logger.info("\t\tbuiltinType{} ", pdu.getType(tag).getBuiltinType());
                assertTrue("Tag is found with contains", pdu.contains(tag));
            }
        }

        final DecodedAsnData pdu = allDecodedData.get(0);
        String tag = "/Human/name";
        byte[] b = pdu.getBytes(tag).get();
        String s = new String(b, Charsets.UTF_8);
        logger.info("{} is {}", tag, s);
        assertEquals("Adam", s);

        String name = pdu.<String>getDecodedObject(tag).get();
        assertEquals("Adam", name);

        b = pdu.getBytes("/Human").get();
        logger.info("/Human is {}", b);

    }

    @Test
    public void testDecodeAsnData2() throws Exception
    {
        logger.info("testing ber against schema");

        final CharSource schemaSource = Resources.asCharSource(getClass().getResource(
                "/barTypeDef.asn"), Charsets.UTF_8);
        final ByteSource berSource = Resources.asByteSource(getClass().getResource("/bar.ber"));

        final ImmutableList<DecodedAsnData> allDecodedData = Asanti.decodeAsnData(berSource,
                schemaSource,
                "Bar");

        for (int i = 0; i < allDecodedData.size(); i++)
        {
            logger.info("Parsing PDU[{}]", i);
            final DecodedAsnData pdu = allDecodedData.get(i);
            for (String tag : pdu.getTags())
            {
                logger.info("\t{} => {}", tag, pdu.getHexString(tag).get());
                logger.info("\t\tbuiltinType {} ", pdu.getType(tag).getBuiltinType());
                assertTrue("Tag is found with contains", pdu.contains(tag));
            }
            for (String tag : pdu.getUnmappedTags())
            {
                logger.info("?\t{} => {}", tag, pdu.getHexString(tag).get());
                logger.info("\t\tbuiltinType{} ", pdu.getType(tag).getBuiltinType());
                assertTrue("Tag is found with contains", pdu.contains(tag));
            }
        }
    }

    @Test
    public void testReadAsnBerFile() throws Exception
    {
        final CharSource schemaSource = Resources.asCharSource(getClass().getResource(
                "/TestMostSimpleTypeDef.asn"), Charsets.UTF_8);
        final ByteSource berSource = Resources.asByteSource(getClass().getResource(
                "/TestMostSimple.ber"));

        final ImmutableList<DecodedAsnData> allDecodedData = Asanti.decodeAsnData(berSource,
                schemaSource,
                "Human");
        for (int i = 0; i < allDecodedData.size(); i++)
        {
            logger.info("Parsing PDU[{}]", i);
            final DecodedAsnData pdu = allDecodedData.get(i);
            for (String tag : pdu.getTags())
            {
                logger.info("\t{} => {}", tag, pdu.getHexString(tag).get());
                logger.info("\t\tbuiltinType {} ", pdu.getType(tag).getBuiltinType());
                assertTrue("Tag is found with contains", pdu.contains(tag));
            }
            for (String tag : pdu.getUnmappedTags())
            {
                logger.info("?\t{} => {}", tag, pdu.getHexString(tag).get());
                logger.info("\t\tbuiltinType{} ", pdu.getType(tag).getBuiltinType());
                assertTrue("Tag is found with contains", pdu.contains(tag));
            }
        }

        final DecodedAsnData pdu = allDecodedData.get(0);
        String tag = "/Human/name";
        // we 'know' that this is a UTF8String
        String s = AsnByteDecoder.decodeAsUtf8String(pdu.getBytes(tag).get());
        logger.info("{} is {}", tag, s);
        assertEquals("Adam", s);
        s = pdu.<String>getDecodedObject(tag).get();
        assertEquals("Adam", s);

        tag = "/Human/age";
        // we 'know' that this is an Integer
        BigInteger age = AsnByteDecoder.decodeAsInteger(pdu.getBytes(tag).get());
        logger.info("{} is {}", tag, age);
        assertEquals(new BigInteger("32"), age);
        age = pdu.<BigInteger>getDecodedObject(tag).get();
        assertEquals(new BigInteger("32"), age);
    }
}
