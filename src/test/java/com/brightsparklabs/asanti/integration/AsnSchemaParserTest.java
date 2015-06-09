package com.brightsparklabs.asanti.integration;

import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.decoder.AsnDecoder;
import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.DecodedTag;
import com.brightsparklabs.asanti.model.schema.tagtype.AsnSchemaTagType;
import com.brightsparklabs.asanti.reader.parser.AnsSchemaTagTypeParser;
import com.brightsparklabs.asanti.reader.parser.AsnSchemaParser;
import com.brightsparklabs.asanti.validator.Validator;
import com.brightsparklabs.asanti.validator.ValidatorImpl;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.brightsparklabs.asanti.validator.result.ValidationResult;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.text.ParseException;

import static org.junit.Assert.*;

/**
 * Testing end-to-end parsing, no mocks.
 * Mostly I have just made this as a way to exercise the parser(s) while making changes
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaParserTest
{
    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(AsnSchemaParserTest.class);



    private static final String NO_ROOT_STRUCTURE = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
            "IMPORTS\n" +
            "BEGIN\n" +
            "    MyInt ::= INTEGER\n" +
            "END";

    private static final String HUMAN_SIMPLE = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
            "IMPORTS\n" +
            "BEGIN\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       age [0] INTEGER (1..100)\n" +
            "   }\n" +
            "END";

    private static final String HUMAN_SIMPLE2 = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
            "IMPORTS\n" +
            "BEGIN\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       age INTEGER (1..100),\n" +
            "       name UTF8String\n" +
            "   }\n" +
            "END";

    private static final String HUMAN_NESTED = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
            "IMPORTS\n" +
            "BEGIN\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       name SEQUENCE\n" +
            "       {\n" +
            "           first UTF8String,\n" +
            "           last  UTF8String\n" +
            "       },\n" +
            "       age INTEGER (1..100)\n" +
            "   }\n" +
            "END";


    private static final String HUMAN_USING_TYPEDEF = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
            "IMPORTS\n" +
            "BEGIN\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       age [0] PersonAge (1..150) OPTIONAL\n" +
            "   }\n" +
            "   PersonAge ::= INTEGER (1..200)\n" +
            "END";

    /** TODO - This does NOT work yet... */
    private static final String HUMAN_USING_TYPEDEF_INDIRECT = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
            "IMPORTS\n" +
            "BEGIN\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       age [0] PersonAge (0..150) OPTIONAL\n" +
            "   }\n" +
            "   PersonAge ::= ShortInt (0..200)\n" +
            "   ShortInt ::= INTEGER (0..32767)\n" +
            "END";

    private static final String HUMAN_USING_TYPEDEF_SEQUENCE = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
            "IMPORTS\n" +
            "BEGIN\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       age [0] PersonAge (1..15) OPTIONAL,\n" +
            "       name PersonName" +
            "   }\n" +
            "   PersonName ::= SEQUENCE\n" +
            "   {\n" +
            "       first UTF8String,\n" +
            "       last  UTF8String\n" +
            "   }\n" +
            "   PersonAge ::= INTEGER (1..200)\n" +
            "END";

    private static final String HUMAN_USING_TYPEDEF_SET = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
            "IMPORTS\n" +
            "BEGIN\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       faveNumbers FaveNumbers,\n" +
            "       name PersonName" +
            "   }\n" +
            "   PersonName ::= SEQUENCE\n" +
            "   {\n" +
            //            "           first UTF8String,\n" +
            "           first OCTET STRING,\n" +
            "       last  UTF8String\n" +
            "   }\n" +
            "   FaveNumbers ::= SET OF INTEGER\n" +
            "END";

    private static final String HUMAN_USING_TYPEDEF_BROKEN = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
            "IMPORTS\n" +
            "BEGIN\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       age [0] PersonAge (1..150) OPTIONAL\n" +
            "   }\n" +
            "END";

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testParse_NoRoot() throws Exception
    {
        AsnSchema schema = AsnSchemaParser.parse(NO_ROOT_STRUCTURE);

        String tag = "/";
        logger.info("get tag " + tag);
        OperationResult<DecodedTag> result = schema.getDecodedTag(tag, "MyInt");
        if (result.wasSuccessful())
        {
            DecodedTag actualTag = result.getOutput();
            logger.info(actualTag.getTag() + " : " + actualTag.getType().getBuiltinType());
        }
    }

    @Test
    public void testParse_HumanSimple() throws Exception
    {
        AsnSchema schema = AsnSchemaParser.parse(HUMAN_SIMPLE);

        String tag = "/0";
        logger.info("get tag " + tag);
        OperationResult<DecodedTag> result = schema.getDecodedTag(tag, "Human");
        if (result.wasSuccessful())
        {
            DecodedTag actualTag = result.getOutput();
            logger.info(actualTag.getTag() + " : " + actualTag.getType().getBuiltinType());

            //String berFilename = "d:\\tmp\\Human_Simple.ber";
            String berFilename = getClass().getResource("/Human_Simple.ber").getFile();
            final File berFile = new File(berFilename);


            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);

            DecodedAsnData pdu = pdus.get(0);
            tag = "/Human/age";
            BigInteger age = (BigInteger)pdu.getDecodedObject(tag);
            logger.info(tag + " : " + age);
            assertEquals(new BigInteger("32"), age);


        }
    }

    @Test
    public void testParse_HumanSimple2() throws Exception
    {
        AsnSchema schema = AsnSchemaParser.parse(HUMAN_SIMPLE2);

        String tag = "/0";
        logger.info("get tag " + tag);
        OperationResult<DecodedTag> result = schema.getDecodedTag(tag, "Human");

        assertTrue(result.wasSuccessful());

        DecodedTag actualTag = result.getOutput();
        logger.info(actualTag.getTag() + " : " + actualTag.getType().getBuiltinType());

        //String berFilename = "d:\\tmp\\Human_Simple2.ber";
        String berFilename = getClass().getResource("/Human_Simple2.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        DecodedAsnData pdu = pdus.get(0);

        assertEquals(0, pdu.getUnmappedTags().size());

        tag = "/Human/age";
        BigInteger age = (BigInteger)pdu.getDecodedObject(tag);
        logger.info(tag + " : " + age);
        assertEquals(new BigInteger("32"), age);


        tag = "/Human/name";
        String name = (String)pdu.getDecodedObject(tag);
        logger.info(tag + " : " + name);
        assertEquals("Adam", name);

    }

    @Test
    public void testParse_HumanNested() throws Exception
    {
        AsnSchema schema = AsnSchemaParser.parse(HUMAN_NESTED);

        String tag = "/0";
        logger.info("get tag " + tag);
        OperationResult<DecodedTag> result = schema.getDecodedTag(tag, "Human");
        assertTrue(result.wasSuccessful());

        DecodedTag actualTag = result.getOutput();
        logger.info(actualTag.getTag() + " : " + actualTag.getType().getBuiltinType());

        String berFilename = getClass().getResource("/Human_Nested.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        DecodedAsnData pdu = pdus.get(0);

        assertEquals(0, pdu.getUnmappedTags().size());

        tag = "/Human/name/first";
        String first = (String)pdu.getDecodedObject(tag);
        logger.info(tag + " : " + first);
        assertEquals("Adam", first);

        tag = "/Human/name/last";
        String last = (String)pdu.getDecodedObject(tag);
        logger.info(tag + " : " + last);
        assertEquals("Smith", last);

        tag = "/Human/age";
        BigInteger age = (BigInteger)pdu.getDecodedObject(tag);
        logger.info(tag + " : " + age);
        assertEquals(new BigInteger("32"), age);

        ValidatorImpl validator = new ValidatorImpl();
        ValidationResult validationresult = validator.validate(pdu);

        assertFalse(validationresult.hasFailures());

    }
    
    @Test
    public void testParse_HumanUsingTypeDef() throws Exception
    {
        // TODO - In order for this to work we need to figure out how to handle TypeDefs as types,
        // Eg age PersonAge in a sequence.  This will require some update to AsnSchemaComponentTypeParser.parse
        // at least, but also some mechanism to store and sweep after the whole file is processed to
        // attempt to re-align things that were not declared when they were first used.

        AsnSchema schema = AsnSchemaParser.parse(HUMAN_USING_TYPEDEF);


        String tag = "/0";
        logger.info("get tag " + tag);
        OperationResult<DecodedTag> result = schema.getDecodedTag(tag, "Human");

        assertTrue(result.wasSuccessful());

        DecodedTag actualTag = result.getOutput();
        logger.info(actualTag.getTag() + " : " + actualTag.getType().getBuiltinType());

        //String berFilename = "d:\\tmp\\Human_Typedef.ber";
        String berFilename = getClass().getResource("/Human_Typedef.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        DecodedAsnData pdu = pdus.get(0);

        assertEquals(0, pdu.getUnmappedTags().size());

        tag = "/Human/age";
        BigInteger age = (BigInteger) pdu.getDecodedObject(tag);
        logger.info(tag + " : " + age);
        assertEquals(new BigInteger("32"), age);
    }

    @Test
    public void testParse_HumanUsingTypeDefIndirect() throws Exception
    {

        try
        {
            AsnSchema schema = AsnSchemaParser.parse(HUMAN_USING_TYPEDEF_INDIRECT);

            String tag = "/0";
            logger.info("get tag " + tag);
            OperationResult<DecodedTag> result = schema.getDecodedTag(tag, "Human");
            if (result.wasSuccessful())
            {
                DecodedTag actualTag = result.getOutput();
                logger.info(actualTag.getTag() + " : " + actualTag.getType().getBuiltinType());

                //String berFilename = "d:\\tmp\\Human_Typedef.ber";
                String berFilename = getClass().getResource("/Human_Typedef.ber").getFile();
                final File berFile = new File(berFilename);
                String topLevelType = "Human";

                final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                        schema,
                        topLevelType);

                DecodedAsnData pdu = pdus.get(0);

                assertEquals(0, pdu.getUnmappedTags().size());

                tag = "/Human/age";
                BigInteger age = (BigInteger) pdu.getDecodedObject(tag);
                logger.info(tag + " : " + age);
            }
        }
        catch(Exception e)
        {
            // TODO - we are expecting this to fail as this feature is not yet implemented
            logger.error("Expected (though undesirable error: " + e.getMessage());
        }

    }


    @Test
    public void testParse_HumanUsingTypeDefSequence() throws Exception
    {
        // TODO - In order for this to work we need to figure out how to handle TypeDefs as types,
        // Eg age PersonAge in a sequence.  This will require some update to AsnSchemaComponentTypeParser.parse
        // at least, but also some mechanism to store and sweep after the whole file is processed to
        // attempt to re-align things that were not declared when they were first used.

        AsnSchema schema = AsnSchemaParser.parse(HUMAN_USING_TYPEDEF_SEQUENCE);


        String tag = "/0";
        logger.info("get tag " + tag);
        OperationResult<DecodedTag> result = schema.getDecodedTag(tag, "Human");

        assertTrue(result.wasSuccessful());

        DecodedTag actualTag = result.getOutput();
        logger.info(actualTag.getTag() + " : " + actualTag.getType().getBuiltinType());

        //String berFilename = "d:\\tmp\\Human_TypedefSequence.ber";
        String berFilename = getClass().getResource("/Human_TypedefSequence.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        DecodedAsnData pdu = pdus.get(0);

        assertEquals(0, pdu.getUnmappedTags().size());

        tag = "/Human/age";

        AsnBuiltinType builtinType = pdu.getType(tag).getBuiltinType();
        assertEquals(builtinType, AsnBuiltinType.Integer);

        BigInteger age = (BigInteger) pdu.getDecodedObject(tag);
        logger.info(tag + " : " + age);
        assertEquals(new BigInteger("32"), age);

        tag = "/Human/name/first";
        String first = (String)pdu.getDecodedObject(tag);
        logger.info(tag + " : " + first);
        assertEquals("Adam", first);

        tag = "/Human/name/last";
        String last = (String) pdu.getDecodedObject(tag);
        logger.info(tag + " : " + last);
        assertEquals("Smith", last);


        ValidatorImpl validator = new ValidatorImpl();
        ValidationResult validationresult = validator.validate(pdu);

        assertTrue(validationresult.hasFailures());

        ImmutableSet<DecodedTagValidationFailure> failures = validationresult.getFailures();
        assertEquals(1, failures.size());

        for (DecodedTagValidationFailure fail : failures)
        {
            assertEquals("/Human/age", fail.getTag());

            logger.info("Tag: " + fail.getTag() +
                    " reason: " + fail.getFailureReason() +
                    " type: " + fail.getFailureType());
        }
    }

    @Test
    public void testParse_HumanUsingTypeDefSet() throws Exception
    {

        AsnSchema schema = AsnSchemaParser.parse(HUMAN_USING_TYPEDEF_SET);

        String berFilename = getClass().getResource("/Human_TypedefSet.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        DecodedAsnData pdu = pdus.get(0);

        assertEquals(0, pdu.getUnmappedTags().size());

        for (String tag : pdu.getTags())
        {
            logger.info("\t{} => {}", tag, pdu.getHexString(tag));
        }

        /* TODO MJF
        String tag = "/Human/faveNumbers";
        BigInteger fave0 = (BigInteger)pdu.getDecodedObject(tag+"[0]");
        */

    }


    @Test
    public void testParse_HumanUsingTypeDefBroken() throws Exception
    {
        // TODO - In order for this to work we need to figure out how to handle TypeDefs as types,
        // Eg age PersonAge in a sequence.  This will require some update to AsnSchemaComponentTypeParser.parse
        // at least, but also some mechanism to store and sweep after the whole file is processed to
        // attempt to re-align things that were not declared when they were first used.

        AsnSchema schema = AsnSchemaParser.parse(HUMAN_USING_TYPEDEF_BROKEN);

        String tag = "/";
        logger.info("get tag " + tag);
        OperationResult<DecodedTag> result = schema.getDecodedTag(tag, "Human");
        if (result.wasSuccessful())
        {
            DecodedTag actualTag = result.getOutput();
            logger.info(actualTag.getTag() + " : " + actualTag.getType().getBuiltinType());
        }

        tag = "/0";
        logger.info("get tag " + tag);
        result = schema.getDecodedTag(tag, "Human");

        if (result.wasSuccessful())
        {
            DecodedTag actualTag = result.getOutput();
            logger.info(actualTag.getTag() + " : " + actualTag.getType().getBuiltinType());
        }

        //String berFilename = "d:\\tmp\\Human_Typedef.ber";
        String berFilename = getClass().getResource("/Human_Typedef.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        assertEquals(1, pdus.size());

        final DecodedAsnData pdu = pdus.get(0);

        // We COULD map the tags
        assertEquals(0, pdu.getUnmappedTags().size());

        // However, we could not correctly determine the Type of "/0"
        AsnBuiltinType builtinType = pdu.getType(tag).getBuiltinType();
        assertNotEquals(builtinType, AsnBuiltinType.Integer);

        try
        {
            // Because we should not have been able to determine the type of age we should not be
            // able to decode it.
            tag = "/Human/age";
            BigInteger age = (BigInteger) pdu.getDecodedObject(tag);
            fail("Should have thrown");
        }
        catch (Exception e)
        {
            logger.error("e: " + e.getMessage());
        }

/*
        try
        {
            ValidatorImpl validator = new ValidatorImpl();
            ValidationResult validationresult = validator.validate(pdu);
            // TODO - we should get a validation failure where we can't determine the type of a tag
            assertTrue(validationresult.hasFailures());

            ImmutableSet<DecodedTagValidationFailure> failures = validationresult.getFailures();
            assertEquals(1, failures.size());

            for (DecodedTagValidationFailure fail : failures)
            {
                assertEquals("/Human/age", fail.getTag());

                logger.info("Tag: " + fail.getTag() +
                        " reason: " + fail.getFailureReason() +
                        " type: " + fail.getFailureType());
            }

        }
        catch (Exception e)
        {

        }
*/
    }
}