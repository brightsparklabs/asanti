/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.integration;

import static org.junit.Assert.*;

import com.brightsparklabs.asanti.Asanti;
import com.brightsparklabs.asanti.decoder.AsnByteDecoder;
import com.brightsparklabs.asanti.model.data.AsantiAsnData;
import com.brightsparklabs.asanti.model.data.RawAsnData;
import com.brightsparklabs.asanti.validator.Validators;
import com.brightsparklabs.assam.validator.FailureType;
import com.brightsparklabs.assam.validator.ValidationFailure;
import com.brightsparklabs.assam.validator.ValidationResult;
import com.brightsparklabs.assam.validator.Validator;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Ordering;
import com.google.common.io.*;
import java.io.File;
import java.math.BigInteger;
import java.util.Map;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Integration tests for {@link Asanti}
 *
 * @author brightSPARK Labs
 */
public class AsantiTest {
    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(AsantiTest.class);

    @Test
    public void testDecodeAsnData() throws Exception {
        /* These results are all tightly coupled to the test data files
         * which is sort of inevitable, but we should keep an eye on how to better
         * manage this dependency/coupling, eg should we auto generate those files etc?
         */

        logger.info("Testing just the ber");
        String berFilename = getClass().getResource("/TestMostSimple.ber").getFile();
        final File berFile = new File(berFilename);

        final ByteSource byteSource = Files.asByteSource(berFile);
        final ImmutableList<RawAsnData> allRawAsnData = Asanti.readAsnBerData(byteSource);

        int count = 0;
        for (final RawAsnData rawAsnData : allRawAsnData) {
            logger.info("PDU[" + count + "]");
            final Map<String, byte[]> tagsData = rawAsnData.getBytes();

            for (final String tag : Ordering.natural().immutableSortedCopy(tagsData.keySet())) {
                logger.info("\t {}: 0x{}", tag, BaseEncoding.base16().encode(tagsData.get(tag)));
            }
            count++;
        }

        RawAsnData rawAsnData = allRawAsnData.get(0);
        // expecting two tags.
        assertEquals(2, rawAsnData.getRawTags().size());

        byte[] b0 = rawAsnData.getBytes("/0[0]").get();
        // we 'know' that this is a UTF8String
        String s = AsnByteDecoder.decodeAsUtf8String(b0);
        assertEquals("Adam", s);

        byte[] b1 = rawAsnData.getBytes("/1[1]").get();
        // we 'know' that this is an integer
        BigInteger big = AsnByteDecoder.decodeAsInteger(b1);
        assertEquals(32, big.intValue());

        assertEquals("Am expecting one PDU", 1, count);
    }

    @Test
    public void testDecodeAsnData1() throws Exception {

        logger.info("testing ber against schema");
        final CharSource schemaSource =
                Resources.asCharSource(
                        getClass().getResource("/TestMostSimple.asn"), Charsets.UTF_8);
        final ByteSource berSource =
                Resources.asByteSource(getClass().getResource("/TestMostSimple.ber"));

        final ImmutableList<AsantiAsnData> allDecodedData =
                Asanti.decodeAsnData(berSource, schemaSource, "Human");

        for (int i = 0; i < allDecodedData.size(); i++) {
            logger.info("Parsing PDU[{}]", i);
            final AsantiAsnData pdu = allDecodedData.get(i);
            for (String tag : pdu.getTags()) {
                logger.info("\t{} => {}", tag, pdu.getHexString(tag).get());
                logger.info("\t\tbuiltinType {} ", pdu.getType(tag).get().getBuiltinType());
                assertTrue("Tag is found with contains", pdu.contains(tag));
            }
            for (String tag : pdu.getUnmappedTags()) {
                logger.info("?\t{} => {}", tag, pdu.getHexString(tag).get());
                logger.info("\t\tbuiltinType{} ", pdu.getType(tag).get().getBuiltinType());
                assertTrue("Tag is found with contains", pdu.contains(tag));
            }
        }

        final AsantiAsnData pdu = allDecodedData.get(0);
        String tag = "/Human/name";
        byte[] b = pdu.getBytes(tag).get();
        String s = new String(b, Charsets.UTF_8);
        logger.info("{} is {}", tag, s);
        assertEquals("Adam", s);

        String name = pdu.getDecodedObject(tag, String.class).get();
        assertEquals("Adam", name);
    }

    @Test
    public void testDecodeAsnData2() throws Exception {
        logger.info("testing ber against schema");

        final CharSource schemaSource =
                Resources.asCharSource(getClass().getResource("/barTypeDef.asn"), Charsets.UTF_8);
        final ByteSource berSource = Resources.asByteSource(getClass().getResource("/bar.ber"));

        final ImmutableList<AsantiAsnData> allDecodedData =
                Asanti.decodeAsnData(berSource, schemaSource, "Bar");

        for (int i = 0; i < allDecodedData.size(); i++) {
            logger.info("Parsing PDU[{}]", i);
            final AsantiAsnData pdu = allDecodedData.get(i);
            for (String tag : pdu.getTags()) {
                logger.info("\t{} => {}", tag, pdu.getHexString(tag).get());
                logger.info("\t\tbuiltinType {} ", pdu.getType(tag).get().getBuiltinType());
                assertTrue("Tag is found with contains", pdu.contains(tag));
            }
            for (String tag : pdu.getUnmappedTags()) {
                logger.info("?\t{} => {}", tag, pdu.getHexString(tag).get());
                logger.info("\t\tbuiltinType{} ", pdu.getType(tag).get().getBuiltinType());
                assertTrue("Tag is found with contains", pdu.contains(tag));
            }
        }
    }

    @Test
    public void testReadAsnBerFile() throws Exception {
        final CharSource schemaSource =
                Resources.asCharSource(
                        getClass().getResource("/TestMostSimpleTypeDef.asn"), Charsets.UTF_8);
        final ByteSource berSource =
                Resources.asByteSource(getClass().getResource("/TestMostSimple.ber"));

        final ImmutableList<AsantiAsnData> allDecodedData =
                Asanti.decodeAsnData(berSource, schemaSource, "Human");
        for (int i = 0; i < allDecodedData.size(); i++) {
            logger.info("Parsing PDU[{}]", i);
            final AsantiAsnData pdu = allDecodedData.get(i);
            for (String tag : pdu.getTags()) {
                logger.info("\t{} => {}", tag, pdu.getHexString(tag).get());
                logger.info("\t\tbuiltinType {} ", pdu.getType(tag).get().getBuiltinType());
                assertTrue("Tag is found with contains", pdu.contains(tag));
            }
            for (String tag : pdu.getUnmappedTags()) {
                logger.info("?\t{} => {}", tag, pdu.getHexString(tag).get());
                logger.info("\t\tbuiltinType{} ", pdu.getType(tag).get().getBuiltinType());
                assertTrue("Tag is found with contains", pdu.contains(tag));
            }
        }

        final AsantiAsnData pdu = allDecodedData.get(0);
        String tag = "/Human/name";
        // we 'know' that this is a UTF8String
        String s = AsnByteDecoder.decodeAsUtf8String(pdu.getBytes(tag).get());
        logger.info("{} is {}", tag, s);
        assertEquals("Adam", s);
        s = pdu.getDecodedObject(tag, String.class).get();
        assertEquals("Adam", s);

        tag = "/Human/age";
        // we 'know' that this is an Integer
        BigInteger age = AsnByteDecoder.decodeAsInteger(pdu.getBytes(tag).get());
        logger.info("{} is {}", tag, age);
        assertEquals(new BigInteger("32"), age);
        age = pdu.getDecodedObject(tag, BigInteger.class).get();
        assertEquals(new BigInteger("32"), age);
    }

    @Test
    public void testReadBerMissingField() throws Exception {
        final CharSource schemaSource =
                Resources.asCharSource(
                        getClass().getResource("/validation/Simple3.asn"), Charsets.UTF_8);
        final ByteSource berSource =
                Resources.asByteSource(getClass().getResource("/validation/Simple3_missing_b.ber"));

        final ImmutableList<AsantiAsnData> allDecodedData =
                Asanti.decodeAsnData(berSource, schemaSource, "Human");

        final Validator validator = Validators.getDefault();
        final ValidationResult validationResult = validator.validate(allDecodedData.get(0));

        // Ensure there are no unmapped tags
        assertEquals(0, allDecodedData.get(0).getUnmappedTags().size());

        final ImmutableSet<ValidationFailure> failures = validationResult.getFailures();
        assertEquals(1, failures.size());
        final ValidationFailure failure = failures.iterator().next();
        final FailureType failureType = failure.getFailureType();
        assertEquals(FailureType.MandatoryFieldMissing, failureType);
        assertEquals("/Human/b", failure.getFailureTag());
        int breakpoint = 0;
    }
}
