package com.brightsparklabs.asanti.integration;

import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.decoder.AsnDecoder;
import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.DecodedTag;
import com.brightsparklabs.asanti.model.schema.tagtype.AsnSchemaTagType;
import com.brightsparklabs.asanti.reader.parser.AnsSchemaTagTypeParser;
import com.brightsparklabs.asanti.reader.parser.AsnSchemaParser;
import com.brightsparklabs.asanti.validator.Validator;
import com.brightsparklabs.asanti.validator.ValidatorImpl;
import com.brightsparklabs.asanti.validator.result.ValidationResult;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigInteger;
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
            "       age INTEGER (1..100)\n" +
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
            "       age PersonAge\n" +
            "   }\n" +
            "   PersonAge ::= INTEGER (1..200)\n" +
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

            String berFilename = "d:\\tmp\\Human_Simple.ber";
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);
            for (int i = 0; i < pdus.size(); i++)
            {

                logger.info("Parsing PDU[{}]", i);
                final DecodedAsnData pdu = pdus.get(i);
                for (String tag2 : pdu.getTags())
                {
                    logger.info("\t{} => {}", tag2, pdu.getHexString(tag2));
                }
                for (String tag2 : pdu.getUnmappedTags())
                {
                    logger.info("\t{} => {}", tag2, pdu.getHexString(tag2));
                }
            }

            DecodedAsnData pdu = pdus.get(0);
            tag = "/Human/age";
            BigInteger yay = (BigInteger)pdu.getDecodedObject(tag);
            logger.info(tag + " : " + yay);

        }
    }

    @Test
    public void testParse_HumanSimple2() throws Exception
    {
        AsnSchema schema = AsnSchemaParser.parse(HUMAN_SIMPLE2);

        String tag = "/0";
        logger.info("get tag " + tag);
        OperationResult<DecodedTag> result = schema.getDecodedTag(tag, "Human");
        if (result.wasSuccessful())
        {
            DecodedTag actualTag = result.getOutput();
            logger.info(actualTag.getTag() + " : " + actualTag.getType().getBuiltinType());

            String berFilename = "d:\\tmp\\Human_Simple2.ber";
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);
            for (int i = 0; i < pdus.size(); i++)
            {

                logger.info("Parsing PDU[{}]", i);
                final DecodedAsnData pdu = pdus.get(i);
                for (String tag2 : pdu.getTags())
                {
                    logger.info("\t{} => {}", tag2, pdu.getHexString(tag2));
                }
                for (String tag2 : pdu.getUnmappedTags())
                {
                    logger.info("\t{} => {}", tag2, pdu.getHexString(tag2));
                }
            }

            DecodedAsnData pdu = pdus.get(0);
            tag = "/Human/age";
            BigInteger age = (BigInteger)pdu.getDecodedObject(tag);
            logger.info(tag + " : " + age);

            tag = "/Human/name";
            String name = (String)pdu.getDecodedObject(tag);
            logger.info(tag + " : " + name);

        }
    }

    @Test
    public void testParse_HumanNested() throws Exception
    {
        AsnSchema schema = AsnSchemaParser.parse(HUMAN_NESTED);

        String tag = "/0";
        logger.info("get tag " + tag);
        OperationResult<DecodedTag> result = schema.getDecodedTag(tag, "Human");
        if (result.wasSuccessful())
        {
            DecodedTag actualTag = result.getOutput();
            logger.info(actualTag.getTag() + " : " + actualTag.getType().getBuiltinType());

            String berFilename = "d:\\tmp\\Human_Nested.ber";
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);
            for (int i = 0; i < pdus.size(); i++)
            {

                logger.info("Parsing PDU[{}]", i);
                final DecodedAsnData pdu = pdus.get(i);
                for (String tag2 : pdu.getTags())
                {
                    logger.info("\t{} => {}", tag2, pdu.getHexString(tag2));
                }
                for (String tag2 : pdu.getUnmappedTags())
                {
                    logger.info("\t{} => {}", tag2, pdu.getHexString(tag2));
                }
            }

            DecodedAsnData pdu = pdus.get(0);
            tag = "/Human/name/first";
            String first = (String)pdu.getDecodedObject(tag);
            logger.info(tag + " : " + first);

            tag = "/Human/name/last";
            String last = (String)pdu.getDecodedObject(tag);
            logger.info(tag + " : " + last);

            tag = "/Human/age";
            BigInteger age = (BigInteger)pdu.getDecodedObject(tag);
            logger.info(tag + " : " + age);

            ValidatorImpl validator = new ValidatorImpl();
            ValidationResult validationresult = validator.validate(pdu);

            if (validationresult.hasFailures())
            {

            }


        }

        int breakpoint = 0;

    }
    
    @Test
    public void testParse_HumanUsingTypeDef() throws Exception
    {
        // TODO - In order for this to work we need to figure out how to handle TypeDefs as types,
        // Eg age PersonAge in a sequence.  This will require some update to AsnSchemaComponentTypeParser.parse
        // at least, but also some mechanism to store and sweep after the whole file is processed to
        // attempt to re-align things that were not declared when they were first used.

        AsnSchema schema = AsnSchemaParser.parse(HUMAN_USING_TYPEDEF);

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

        int breakpoint = 0;

    }

    @Test
    public void testParseComponent() throws Exception
    {
        final AsnSchemaTagType tagTypes = AnsSchemaTagTypeParser.parse("myVar", "INTEGER");


        final AsnSchemaTagType tagTypes2 = AnsSchemaTagTypeParser.parse("myVar", "INTEGER (1..100)");
        int breakpoint = 0;
    }
}