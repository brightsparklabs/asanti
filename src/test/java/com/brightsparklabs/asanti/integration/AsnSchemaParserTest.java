package com.brightsparklabs.asanti.integration;

import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.DecodedTag;
import com.brightsparklabs.asanti.model.schema.tagtype.AsnSchemaTagType;
import com.brightsparklabs.asanti.reader.parser.AnsSchemaTagTypeParser;
import com.brightsparklabs.asanti.reader.parser.AsnSchemaParser;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        }
    }

    @Test
    public void testParse_HumanUsingTypeDef() throws Exception
    {
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