/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.integration;

import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.decoder.AsnByteDecoder;
import com.brightsparklabs.asanti.decoder.AsnDecoder;
import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.model.data.AsnData;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.DecodedTag;
import com.brightsparklabs.asanti.reader.AsnSchemaFileReader;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import com.google.common.io.BaseEncoding;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Integration tests for {@link AsnDecoder}
 *
 * @author brightSPARK Labs
 */
public class AsnDecoderTest {

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(AsnDecoderTest.class);

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testAsantiSample() throws Exception
    {
        final File asnFile = new File(getClass().getResource("/AsantiSample.asn").getFile());
        AsnSchema instance = AsnSchemaFileReader.read(asnFile);

        String tag = "1/0/1";
        logger.info("get tag " + tag);
        OperationResult<DecodedTag> result = instance.getDecodedTag(tag, "Document");

        assertTrue(result.wasSuccessful());

        DecodedTag actualTag = result.getOutput();
        logger.info(actualTag.getTag() + " : " + actualTag.getType().getBuiltinType());
        assertEquals("/Document/header/published/date", actualTag.getTag());
        assertEquals(AsnBuiltinType.GeneralizedTime, actualTag.getType().getBuiltinType());


        assertEquals("/Document/header/published",
                instance.getDecodedTag("1/0", "Document").getOutput().getTag());
        assertEquals("/Document/body/lastModified/date",
                instance.getDecodedTag("2/0/0", "Document").getOutput().getTag());
        assertEquals("/Document/body/lastModified/modifiedBy/firstName",
                instance.getDecodedTag("2/0/1/1", "Document").getOutput().getTag());
        assertEquals("/Document/body/lastModified/modifiedBy/lastName",
                instance.getDecodedTag("2/0/1/2", "Document").getOutput().getTag());
        assertEquals("/Document/body/lastModified/modifiedBy/title",
                instance.getDecodedTag("2/0/1/3", "Document").getOutput().getTag());
        assertEquals("/Document/body/prefix/text",
                instance.getDecodedTag("2/1/1", "Document").getOutput().getTag());
        assertEquals("/Document/body/content/text",
                instance.getDecodedTag("2/2/1", "Document").getOutput().getTag());
        assertEquals("/Document/body/content/paragraphs/title",
                instance.getDecodedTag("2/2/2/1", "Document").getOutput().getTag());
        assertEquals("/Document/body/content/paragraphs[0]/title",
                instance.getDecodedTag("2/2/2[0]/1", "Document").getOutput().getTag());
        assertEquals("/Document/body/content/paragraphs[1]/title",
                instance.getDecodedTag("2/2/2[1]/1", "Document").getOutput().getTag());
        assertEquals("/Document/body/content/paragraphs[0]/contributor/firstName",
                instance.getDecodedTag("2/2/2[0]/2/1", "Document").getOutput().getTag());
        assertEquals("/Document/body/content/paragraphs[0]/contributor/lastName",
                instance.getDecodedTag("2/2/2[0]/2/2", "Document").getOutput().getTag());
        assertEquals("/Document/body/content/paragraphs[0]/contributor/title",
                instance.getDecodedTag("2/2/2[0]/2/3", "Document").getOutput().getTag());
        assertEquals("/Document/body/content/paragraphs[0]/points",
                instance.getDecodedTag("2/2/2[0]/3", "Document").getOutput().getTag());
        assertEquals("/Document/body/content/paragraphs[0]/points[0]",
                instance.getDecodedTag("2/2/2[0]/3[0]", "Document").getOutput().getTag());
        assertEquals("/Document/body/content/paragraphs[99]/title",
                instance.getDecodedTag("2/2/2[99]/1", "Document").getOutput().getTag());
        assertEquals("/Document/body/content/paragraphs[99]/contributor/firstName",
                instance.getDecodedTag("2/2/2[99]/2/1", "Document").getOutput().getTag());
        assertEquals("/Document/body/content/paragraphs[99]/contributor/lastName",
                instance.getDecodedTag("2/2/2[99]/2/2", "Document").getOutput().getTag());
        assertEquals("/Document/body/content/paragraphs[99]/contributor/title",
                instance.getDecodedTag("2/2/2[99]/2/3", "Document").getOutput().getTag());
        assertEquals("/Document/body/content/paragraphs[99]/points",
                instance.getDecodedTag("2/2/2[99]/3", "Document").getOutput().getTag());
        assertEquals("/Document/body/content/paragraphs[99]/points[99]",
                instance.getDecodedTag("2/2/2[99]/3[99]", "Document").getOutput().getTag());
        assertEquals("/Document/body/suffix/text",
                instance.getDecodedTag("2/3/1", "Document").getOutput().getTag());


        // test partial
        assertEquals("/Document/header/published/99/98",
                instance.getDecodedTag("1/0/99/98", "Document").getOutput().getTag());
        assertEquals("/Document/body/lastModified/99/98",
                instance.getDecodedTag("2/0/99/98", "Document").getOutput().getTag());

        // test unknown
        assertEquals("/Document/99/98",
                instance.getDecodedTag("/99/98", "Document").getOutput().getTag());

    }

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

        final ImmutableList<AsnData> allAsnData = AsnDecoder.readAsnBerFile(berFile);

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

        byte [] b0 = asnData.getBytes("/0");
        // we 'know' that this is a UTF8String
        String s = AsnByteDecoder.decodeAsUtf8String(b0);
        assertEquals("Adam", s);

        byte [] b1 = asnData.getBytes("/1");
        // we 'know' that this is an integer
        BigInteger big = AsnByteDecoder.decodeAsInteger(b1);
        assertEquals(32, big.intValue());

        assertEquals("Am expecting one PDU", 1, count);

    }

    @Test
    public void testDecodeAsnData1() throws Exception
    {

        logger.info("testing ber against schema");
        final File asnFile = new File(getClass().getResource("/TestMostSimple.asn").getFile());
        final File berFile = new File(getClass().getResource("/TestMostSimple.ber").getFile());

        final ImmutableList<DecodedAsnData> allDecodedData = AsnDecoder.decodeAsnData(berFile, asnFile, "Human");


        for (int i = 0; i < allDecodedData.size(); i++)
        {
            logger.info("Parsing PDU[{}]", i);
            final DecodedAsnData pdu = allDecodedData.get(i);
            for (String tag : pdu.getTags())
            {
                logger.info("\t{} => {}", tag, pdu.getHexString(tag));
                logger.info("\t\tbuiltinType {} ",
                        pdu.getType(tag).getBuiltinType());
                assertTrue("Tag is found with contains", pdu.contains(tag));
            }
            for (String tag : pdu.getUnmappedTags())
            {
                logger.info("?\t{} => {}", tag, pdu.getHexString(tag));
                logger.info("\t\tbuiltinType{} ",
                        pdu.getType(tag).getBuiltinType());
                assertTrue("Tag is found with contains", pdu.contains(tag));
            }
        }


        final DecodedAsnData pdu = allDecodedData.get(0);
        String tag = "/Human/name";
        byte[] b = pdu.getBytes(tag);
        String s = new String(b, Charsets.UTF_8);
        logger.info("{} is {}", tag, s);
        assertEquals("Adam", s);

        String name = (String)pdu.getDecodedObject(tag);
        assertEquals("Adam", name);

        b = pdu.getBytes("/Human");
        logger.info("/Human is {}", b);


    }

    @Test
    public void testDecodeAsnData2() throws Exception
    {
        logger.info("testing ber against schema");

        final File asnFile = new File(getClass().getResource("/barTypeDef.asn").getFile());
        final File berFile = new File(getClass().getResource("/bar.ber").getFile());

        final ImmutableList<DecodedAsnData> allDecodedData = AsnDecoder.decodeAsnData(berFile, asnFile, "Bar");


        for (int i = 0; i < allDecodedData.size(); i++)
        {
            logger.info("Parsing PDU[{}]", i);
            final DecodedAsnData pdu = allDecodedData.get(i);
            for (String tag : pdu.getTags())
            {
                logger.info("\t{} => {}", tag, pdu.getHexString(tag));
                logger.info("\t\tbuiltinType {} ", pdu.getType(tag).getBuiltinType());
                assertTrue("Tag is found with contains", pdu.contains(tag));
            }
            for (String tag : pdu.getUnmappedTags())
            {
                logger.info("?\t{} => {}", tag, pdu.getHexString(tag));
                logger.info("\t\tbuiltinType{} ", pdu.getType(tag).getBuiltinType());
                assertTrue("Tag is found with contains", pdu.contains(tag));
            }
        }
    }

    @Test
    public void testReadAsnBerFile() throws Exception
    {

        final File asnFile = new File(getClass().getResource("/TestMostSimpleTypeDef.asn").getFile());
        final File berFile = new File(getClass().getResource("/TestMostSimple.ber").getFile());


        final ImmutableList<DecodedAsnData> allDecodedData = AsnDecoder.decodeAsnData(berFile, asnFile, "Human");
        for (int i = 0; i < allDecodedData.size(); i++)
        {
            logger.info("Parsing PDU[{}]", i);
            final DecodedAsnData pdu = allDecodedData.get(i);
            for (String tag : pdu.getTags())
            {
                logger.info("\t{} => {}", tag, pdu.getHexString(tag));
                logger.info("\t\tbuiltinType {} ", pdu.getType(tag).getBuiltinType());
                assertTrue("Tag is found with contains", pdu.contains(tag));
            }
            for (String tag : pdu.getUnmappedTags())
            {
                logger.info("?\t{} => {}", tag, pdu.getHexString(tag));
                logger.info("\t\tbuiltinType{} ", pdu.getType(tag).getBuiltinType());
                assertTrue("Tag is found with contains", pdu.contains(tag));
            }
        }

        final DecodedAsnData pdu = allDecodedData.get(0);
        String tag = "/Human/name";
        // we 'know' that this is a UTF8String
        String s = AsnByteDecoder.decodeAsUtf8String( pdu.getBytes(tag));
        logger.info("{} is {}", tag, s);
        assertEquals("Adam", s);
        s = (String)pdu.getDecodedObject(tag);
        assertEquals("Adam", s);

        tag = "/Human/age";
        // we 'know' that this is an Integer
        BigInteger age = AsnByteDecoder.decodeAsInteger(pdu.getBytes(tag));
        logger.info("{} is {}", tag, age);
        assertEquals(new BigInteger("32"), age);
        age = (BigInteger)pdu.getDecodedObject(tag);
        assertEquals(new BigInteger("32"), age);

    }

    @Test
    public void testReadAsnBerFile1() throws Exception
    {

    }
}