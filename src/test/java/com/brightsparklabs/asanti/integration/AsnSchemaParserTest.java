package com.brightsparklabs.asanti.integration;

import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.decoder.AsnDecoder;
import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.DecodedTag;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.reader.AsnSchemaFileReader;
import com.brightsparklabs.asanti.reader.parser.AsnSchemaParser;
import com.brightsparklabs.asanti.validator.ValidatorImpl;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.brightsparklabs.asanti.validator.result.ValidationResult;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
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
            "       name UTF8String,\n" +
            "       age INTEGER\n" +
            "   }\n" +
            "END";

    private static final String HUMAN_SIMPLE3 = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
            "IMPORTS\n" +
            "BEGIN\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       name UTF8String,\n" +
            "       age PersonAge\n" +
            "   }\n" +
            "   PersonAge ::= INTEGER\n" +
            "END";

    private static final String HUMAN_SIMPLE_ENUMERATED = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
            "IMPORTS\n" +
            "BEGIN\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       pickOne ENUMERATED\n" +
            "       {\n" +
            "           optA(0)\n" +
            "           optB(1)\n" +
            "       }\n" +
            "   }\n" +
            "END";

    private static final String HUMAN_SIMPLE_CHOICE = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
            "IMPORTS\n" +
            "BEGIN\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       payload Payload\n" +
            "   }\n" +
            "   Payload ::= CHOICE\n" +
            "   {\n" +
            "       optA [0] TypeA,\n" +
            "       optB [1] TypeB\n" +
            "   }\n" +
            "   TypeA ::= SEQUENCE\n" +
            "   {\n" +
            "       name UTF8String,\n" +
            "       age INTEGER\n" +
            "   }\n" +
            "   TypeB ::= SEQUENCE\n" +
            "   {\n" +
            "       number INTEGER,\n" +
            "       also INTEGER\n" +
            "   }\n" +
            "END";


    private static final String HUMAN_SIMPLExx = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
            "IMPORTS\n" +
            "BEGIN\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       age [0] INTEGER,\n" +
            "       gender ENUMERATED{male(0),female(1)}" +
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
            "       age INTEGER(1..100),\n" +
            "       name UTF8String(SIZE( 1..10))\n" +
            "   }\n" +
            "END";

    private static final String HUMAN_SIMPLE_SET = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
            "IMPORTS\n" +
            "BEGIN\n" +
            "   Human ::= SET\n" +
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
            "       age [0] PersonAge (1..15) OPTIONAL\n" +
            "   }\n" +
            "   PersonAge ::= INTEGER (1..200)\n" +
            "END";

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
            "   ShortInt ::= Int32 (0..32767)\n" +
            "   Int32 ::= INTEGER (0..65536)\n" +
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


    private static final String HUMAN_SEQUENCEOF_PRIMITIVE = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
            "IMPORTS\n" +
            "BEGIN\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       age SEQUENCE OF INTEGER (1..100)\n" +
            "   }\n" +
            "END";


    private static final String HUMAN_SEQUENCEOF_SEQUENCE = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
            "IMPORTS\n" +
            "BEGIN\n" +
            "   Human ::= SEQUENCE OF SEQUENCE\n" +
            "   {\n" +
            "       age INTEGER (1..100),\n" +
            "       name UTF8String\n" +
            "   }\n" +
            "END";

    private static final String HUMAN_SEQUENCEOF_SEQUENCE2 = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
            "IMPORTS\n" +
            "BEGIN\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       age INTEGER (1..100),\n" +
            "       name UTF8String,\n" +
            "       friends SEQUENCE OF Name\n" +
            "   }\n" +
            "   Name ::= SEQUENCE\n" +
            "   {\n" +
            "       first UTF8String,\n" +
            "       last  UTF8String\n" +
            "   }\n" +
            "END";

    private static final String HUMAN_SEQUENCEOF_SEQUENCE3 = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
            "IMPORTS\n" +
            "BEGIN\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       age INTEGER (1..100),\n" +
            "       name UTF8String,\n" +
            "       friends SEQUENCE OF SEQUENCE\n" +
            "       {\n" +
            "           age INTEGER (1..100),\n" +
            "           name UTF8String\n" +
            "       }\n" +
            "   }\n" +
            "END";

    private static final String HUMAN_SEQUENCEOF_SEQUENCE4 = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
            "IMPORTS\n" +
            "BEGIN\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       age INTEGER (1..100),\n" +
            "       name UTF8String,\n" +
            "       friends SEQUENCE OF Friend\n" +
            "   }\n" +
            "   Friend ::= SEQUENCE {\n" +
            "           age INTEGER (1..100),\n" +
            "           name UTF8String\n" +
            "   }\n" +
            "END";

    private static final String HUMAN_USING_TYPEDEF_SETOF = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
            "IMPORTS\n" +
            "BEGIN\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       faveNumbers FaveNumbers,\n" +
            "       name PersonName,\n" +
            "       bitString BIT STRING (SIZE (4))" +
            "   }\n" +
            "   PersonName ::= SEQUENCE\n" +
            "   {\n" +
            "       first NameType,\n" +
            "       last  NameType\n" +
            "   }\n" +
            "   FaveNumbers ::= SET OF INTEGER\n" +
            "   NameType ::= UTF8String\n" +
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


    // TODO ASN-123 - rationalise these.  Determine if we want many small examples, or one more
    // comprehensive example (using the AsantiSample schema).  The small examples were useful
    // during the refactoring (ASN-126), not sure how useful they will be beyond that.
    // Also, consolidate this and AsnDecoderTest in to one, as they are both
    // doing end-to-end testing.


    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------
    @Test
    public void testAsantiSample() throws Exception
    {
        // loads the AsantiSample test schema and checks all the tag decoding.

        final File asnFile = new File(getClass().getResource("/AsantiSample.asn").getFile());
        AsnSchema instance = AsnSchemaFileReader.read(asnFile);

        assertEquals("/Document",
                instance.getDecodedTag("", "Document").getOutput().getTag());

        assertEquals("/Document/header",
                instance.getDecodedTag("1", "Document").getOutput().getTag());
        assertEquals("/Document/header/published",
                instance.getDecodedTag("1/0", "Document").getOutput().getTag());
        assertEquals("/Document/header/published/date",
                instance.getDecodedTag("1/0/1", "Document").getOutput().getTag());
        assertEquals("/Document/header/published/country",
                instance.getDecodedTag("1/0/2", "Document").getOutput().getTag());

        assertEquals("/Document/body",
                instance.getDecodedTag("2", "Document").getOutput().getTag());
        assertEquals("/Document/body/lastModified",
                instance.getDecodedTag("2/0", "Document").getOutput().getTag());
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

        assertEquals("/Document/footer",
                instance.getDecodedTag("3", "Document").getOutput().getTag());
        assertEquals("/Document/footer/authors",
                instance.getDecodedTag("3/0", "Document").getOutput().getTag());
        assertEquals("/Document/footer/authors[0]/firstName",
                instance.getDecodedTag("3/0[0]/1", "Document").getOutput().getTag());
        assertEquals("/Document/footer/authors[0]/lastName",
                instance.getDecodedTag("3/0[0]/2", "Document").getOutput().getTag());

        assertEquals("/Document/dueDate",
                instance.getDecodedTag("4", "Document").getOutput().getTag());

        assertEquals("/Document/version",
                instance.getDecodedTag("5", "Document").getOutput().getTag());
        assertEquals("/Document/version/majorVersion",
                instance.getDecodedTag("5/0", "Document").getOutput().getTag());
        assertEquals("/Document/version/minorVersion",
                instance.getDecodedTag("5/1", "Document").getOutput().getTag());

        assertEquals("/Document/description",
                instance.getDecodedTag("6", "Document").getOutput().getTag());
        assertEquals("/Document/description/numberLines",
                instance.getDecodedTag("6/0", "Document").getOutput().getTag());
        assertEquals("/Document/description/summary",
                instance.getDecodedTag("6/1", "Document").getOutput().getTag());

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

        assertTrue(result.wasSuccessful());

        DecodedTag actualTag = result.getOutput();
        logger.info(actualTag.getTag() + " : " + actualTag.getType().getBuiltinType());


        tag = "/1";
        logger.info("get tag " + tag);
        result = schema.getDecodedTag(tag, "Human");


        actualTag = result.getOutput();
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

        tag = "/Human/name";
        String name = (String)pdu.getDecodedObject(tag);
        logger.info("{} : {}", tag, name);
        assertEquals("Adam", name);

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
    public void testParse_HumanEnumerated() throws Exception
    {
        AsnSchema schema = AsnSchemaParser.parse(HUMAN_SIMPLE_ENUMERATED);

        String tag = "/0";
        logger.info("get tag " + tag);
        OperationResult<DecodedTag> result = schema.getDecodedTag(tag, "Human");

        assertTrue(result.wasSuccessful());

        DecodedTag actualTag = result.getOutput();
        logger.info(actualTag.getTag() + " : " + actualTag.getType().getBuiltinType());

        //String berFilename = "d:\\tmp\\Human_Simple2.ber";
        String berFilename = getClass().getResource("/Human_SimpleEnumerated.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        DecodedAsnData pdu = pdus.get(0);

        assertEquals(0, pdu.getUnmappedTags().size());

        tag = "/Human/pickOne";
        assertEquals(AsnPrimitiveType.ENUMERATED, pdu.getType(tag).getPrimitiveType());

        byte [] bytes = pdu.getBytes(tag);
        assertEquals(1,bytes[0] );

    }

    @Test
    public void testParse_HumanSimpleChoice() throws Exception
    {
        AsnSchema schema = AsnSchemaParser.parse(HUMAN_SIMPLE_CHOICE);

        String berFilename = getClass().getResource("/Human_SimpleChoice.ber").getFile();
        final File berFile = new File(berFilename);

        String topLevelType = "Human";
        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        DecodedAsnData pdu = pdus.get(0);
        String tag = "/Human/payload/optA/age";
        BigInteger age = (BigInteger)pdu.getDecodedObject(tag);
        assertEquals(new BigInteger("32"), age);

        tag = "/Human/payload/optA/name";
        String name = (String)pdu.getDecodedObject(tag);
        assertEquals("Adam", name);
}

    @Test
    public void testParse_HumanSimpleSet() throws Exception
    {
        AsnSchema schema = AsnSchemaParser.parse(HUMAN_SIMPLE_SET);

        String tag = "/0";
        logger.info("get tag " + tag);
        OperationResult<DecodedTag> result = schema.getDecodedTag(tag, "Human");

        assertTrue(result.wasSuccessful());

        DecodedTag actualTag = result.getOutput();
        logger.info(actualTag.getTag() + " : " + actualTag.getType().getBuiltinType());

        String berFilename = getClass().getResource("/Human_SimpleSet.ber").getFile();
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

        tag = "/0/0";
        logger.info("get tag " + tag);
        result = schema.getDecodedTag(tag, "Human");
        assertTrue(result.wasSuccessful());

        actualTag = result.getOutput();
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
    public void testParse_HumanUsingTypeDefIndirect() throws Exception
    {

        AsnSchema schema = AsnSchemaParser.parse(HUMAN_USING_TYPEDEF_INDIRECT);

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

        debugPdus(pdus);

        DecodedAsnData pdu = pdus.get(0);

        assertEquals(0, pdu.getUnmappedTags().size());

        tag = "/Human/age";
        BigInteger age = (BigInteger) pdu.getDecodedObject(tag);
        logger.info(tag + " : " + age);

        ValidatorImpl validator = new ValidatorImpl();
        ValidationResult validationresult = validator.validate(pdu);

        // dump any failures so we can see what went wrong
        for (DecodedTagValidationFailure fail : validationresult.getFailures())
        {
            logger.info("Validation Failure for : " + fail.getTag() +
                    " reason: " + fail.getFailureReason() +
                    " type: " + fail.getFailureType());
        }


        assertFalse(validationresult.hasFailures());

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


        tag = "/1/0";
        logger.info("get tag " + tag);
        result = schema.getDecodedTag(tag, "Human");
        actualTag = result.getOutput();
        logger.info(actualTag.getTag() + " : " + actualTag.getType().getBuiltinType());

        tag = "/1/1";
        logger.info("get tag " + tag);
        result = schema.getDecodedTag(tag, "Human");
        actualTag = result.getOutput();
        logger.info(actualTag.getTag() + " : " + actualTag.getType().getBuiltinType());


        //String berFilename = "d:\\tmp\\Human_TypedefSequence.ber";
        String berFilename = getClass().getResource("/Human_TypedefSequence.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        debugPdus(pdus);

        DecodedAsnData pdu = pdus.get(0);

        assertEquals(0, pdu.getUnmappedTags().size());

        tag = "/Human/age";

        AsnBuiltinType builtinType = pdu.getType(tag).getBuiltinType();
        assertEquals(AsnBuiltinType.Integer, builtinType);

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

/*
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
*/
    }



    @Test
    public void testParse_HumanSequenceOfPrimitive() throws Exception
    {
        AsnSchema schema = AsnSchemaParser.parse(HUMAN_SEQUENCEOF_PRIMITIVE);

        String berFilename = getClass().getResource("/Human_SequenceOfPrimitive.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        debugPdus(pdus);

    }

    @Test
    public void testParse_HumanSequenceOfSequence3() throws Exception
    {
        AsnSchema schema = AsnSchemaParser.parse(HUMAN_SEQUENCEOF_SEQUENCE3);


        String tag = "/2[0]/0";
        logger.info("get tag " + tag);
        OperationResult<DecodedTag> result = schema.getDecodedTag(tag, "Human");

        assertTrue(result.wasSuccessful());

        DecodedTag actualTag = result.getOutput();
        logger.info(actualTag.getTag() + " : " + actualTag.getType().getBuiltinType());



        String berFilename = getClass().getResource("/Human_SequenceOfSequence3.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        DecodedAsnData pdu = pdus.get(0);

        assertEquals(0, pdu.getUnmappedTags().size());

        debugPdus(pdus);

        tag = "/Human/age";
        BigInteger age = (BigInteger)pdu.getDecodedObject(tag);
        assertEquals(32, age.intValue());

        tag = "/Human/name";
        String name = (String)pdu.getDecodedObject(tag);
        assertEquals("Adam", name);

        tag = "/Human/friends[0]/name";
        name = (String)pdu.getDecodedObject(tag);
        assertEquals("Finn", name);

        tag = "/Human/friends[0]/age";
        age = (BigInteger)pdu.getDecodedObject(tag);
        assertEquals(5, age.intValue());

        tag = "/Human/friends[1]/name";
        name = (String)pdu.getDecodedObject(tag);
        assertEquals("Fatty", name);

        tag = "/Human/friends[1]/age";
        age = (BigInteger)pdu.getDecodedObject(tag);
        assertEquals(3, age.intValue());
    }


    @Test
    public void testParse_HumanSequenceOfSequence4() throws Exception
    {

        AsnSchema schema = AsnSchemaParser.parse(HUMAN_SEQUENCEOF_SEQUENCE4);

        String tag = "/2[0]/0";
        logger.info("get tag " + tag);
        OperationResult<DecodedTag> result = schema.getDecodedTag(tag, "Human");

        assertTrue(result.wasSuccessful());

        DecodedTag actualTag = result.getOutput();
        logger.info(actualTag.getTag() + " : " + actualTag.getType().getBuiltinType());


        String berFilename = getClass().getResource("/Human_SequenceOfSequence3.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        DecodedAsnData pdu = pdus.get(0);

        assertEquals(0, pdu.getUnmappedTags().size());

        debugPdus(pdus);

        tag = "/Human/age";
        BigInteger age = (BigInteger)pdu.getDecodedObject(tag);
        assertEquals(32, age.intValue());

        tag = "/Human/name";
        String name = (String)pdu.getDecodedObject(tag);
        assertEquals("Adam", name);

        tag = "/Human/friends[0]/name";
        name = (String)pdu.getDecodedObject(tag);
        assertEquals("Finn", name);

        tag = "/Human/friends[0]/age";
        age = (BigInteger)pdu.getDecodedObject(tag);
        assertEquals(5, age.intValue());

        tag = "/Human/friends[1]/name";
        name = (String)pdu.getDecodedObject(tag);
        assertEquals("Fatty", name);

        tag = "/Human/friends[1]/age";
        age = (BigInteger)pdu.getDecodedObject(tag);
        assertEquals(3, age.intValue());
    }

    @Test
    public void testParse_HumanSequenceOfSequence2() throws Exception
    {

        AsnSchema schema = AsnSchemaParser.parse(HUMAN_SEQUENCEOF_SEQUENCE2);

        String berFilename = getClass().getResource("/Human_SequenceOfSequence2.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);



        DecodedAsnData pdu = pdus.get(0);

        //assertEquals(0, pdu.getUnmappedTags().size());

        for (String tag : pdu.getTags())
        {
            logger.info("\t{} => {}", tag, pdu.getHexString(tag));
        }
        for (String tag : pdu.getUnmappedTags())
        {
            logger.info("\t{} => {}", tag, pdu.getHexString(tag));
        }

    }

    @Test
    public void testParse_HumanUsingTypeDefSetOf() throws Exception
    {

        AsnSchema schema = AsnSchemaParser.parse(HUMAN_USING_TYPEDEF_SETOF);

        String berFilename = getClass().getResource("/Human_TypedefSetOf.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        DecodedAsnData pdu = pdus.get(0);

        assertEquals(0, pdu.getUnmappedTags().size());

        for (String tag : pdu.getTags())
        {
            logger.info("\t{} => {} as {}", tag, pdu.getHexString(tag), pdu.getType(tag).getBuiltinType());
        }

        String tag = "/Human/faveNumbers";
        BigInteger fave = (BigInteger)pdu.getDecodedObject(tag+"[0]");
        logger.info("fave[0]: {}", fave);

        fave = (BigInteger)pdu.getDecodedObject(tag+"[1]");
        logger.info("fave[1]: {}", fave);

        tag = "/Human/faveNumbers";
        fave = (BigInteger)pdu.getDecodedObject(tag+"[2]");
        logger.info("fave[2]: {}", fave);

    }

    @Test
    public void testParse_HumanUsingTypeEtsi() throws Exception
    {
        // TODO ASN-133, ASN-137, ASN-141 prevent us from being able to parse the EIFv122.asn schema
//        String schemaFilename = getClass().getResource("/EIFv122.asn").getFile();
//        File schemaFile = new File(schemaFilename);
//        final AsnSchema schema = AsnSchemaFileReader.read(schemaFile);
//
//        {
//            String tag = "/2/1/2/2/1/0";
//            logger.info("get tag " + tag);
//            OperationResult<DecodedTag> result = schema.getDecodedTag(tag, "PS-PDU");
//        }
//        {
//            String tag = "1/1";
//            logger.info("get tag " + tag);
//            OperationResult<DecodedTag> result = schema.getDecodedTag(tag, "PS-PDU");
//        }
//
//
//        {
//            String berFilename = getClass().getResource("/test5.ber").getFile();
//            final File berFile = new File(berFilename);
//            String topLevelType = "PS-PDU";
//
//            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
//                    schema,
//                    topLevelType);
//
//            logger.debug("Results of /test5.ber");
//
//            debugPdus(pdus);
//
//        }



//        {
//            String berFilename = getClass().getResource("/mock/SSI_CC.etsi").getFile();
//            final File berFile = new File(berFilename);
//            String topLevelType = "PS-PDU";
//
//            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
//                    schema,
//                    topLevelType);
//
//            logger.debug("Results of /mock/SSI_CC.etsi");
//            debugPdus(pdus);
///*
//            for (int i = 0; i < pdus.size(); i++)
//            {
//
//
//                ValidatorImpl validator = new ValidatorImpl();
//                ValidationResult validationresult = validator.validate(pdu);
//                // TODO - we should get a validation failure where we can't determine the type of a tag
//                //assertTrue(validationresult.hasFailures());
//
//                ImmutableSet<DecodedTagValidationFailure> failures = validationresult.getFailures();
//                //assertEquals(1, failures.size());
//                logger.warn("Validation failures: {}", failures.size());
//
//                for (DecodedTagValidationFailure fail : failures)
//                {
//
//                    logger.info("Tag: " + fail.getTag() +
//                            " reason: " + fail.getFailureReason() +
//                            " type: " + fail.getFailureType());
//                }
//
//            }
//*/
//
//            String tag = "/PS-PDU/pSHeader/communicationIdentifier/communicationIdentityNumber";
//
//            BigInteger number = (BigInteger)pdus.get(0).getDecodedObject(tag);
//            logger.info("communicationIdentityNumber: " + number);
//
//            String str = (String)pdus.get(0).getDecodedObject("/PS-PDU/pSHeader/authorizationCountryCode");
//            logger.info("authorizationCountryCode: " + str);
//
//            str = (String)pdus.get(1).getDecodedObject("/PS-PDU/pSHeader/communicationIdentifier/deliveryCountryCode");
//            logger.info("deliveryCountryCode: {}", str);
//
//            byte [] bytes = (byte [])pdus.get(1).getDecodedObject("/PS-PDU/pSHeader/communicationIdentifier/networkIdentifier/networkElementIdentifier");
//            String s = new String(bytes, Charsets.UTF_8);
//            logger.info("networkElementIdentifier: {} - from Octet String", s);
//
//
//            try
//            {
//
//            }
//            catch (Exception e)
//            {
//
//            }
//        }
/*
        {
            String berFilename = getClass().getResource("/mock/SSI_IRI.etsi").getFile();
            final File berFile = new File(berFilename);
            String topLevelType = "PS-PDU";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);

            for (int i = 0; i < pdus.size(); i++)
            {

                logger.info("Parsing PDU[{}]", i);
                final DecodedAsnData pdu = pdus.get(i);
                for (String tag : pdu.getTags())
                {
                    logger.info("\t{} => {} as {}",
                            tag,
                            pdu.getHexString(tag),
                            pdu.getType(tag).getBuiltinType());
                }
                for (String tag : pdu.getUnmappedTags())
                {
                    logger.info("\t? {} => {}", tag, pdu.getHexString(tag));
                }
            }
        }
*/
        /*
        BigInteger sequenceNumber = (BigInteger)pdus.get(0).getDecodedObject("/PS-PDU/pSHeader/sequenceNumber");
        logger.info("sequenceNumber[0]: " + sequenceNumber);
        BigInteger communicationIdentityNumber = (BigInteger)pdus.get(0).getDecodedObject("/PS-PDU/pSHeader/communicationIdentifier/communicationIdentityNumber");
        logger.info("communicationIdentityNumber[0]: " + communicationIdentityNumber);

        communicationIdentityNumber = (BigInteger)pdus.get(1).getDecodedObject("/PS-PDU/pSHeader/communicationIdentifier/communicationIdentityNumber");
        logger.info("communicationIdentityNumber[1]: {}", communicationIdentityNumber);

        sequenceNumber = (BigInteger)pdus.get(2).getDecodedObject("/PS-PDU/pSHeader/sequenceNumber");
        logger.info("sequenceNumber[2]: " + sequenceNumber);
*/

    }

    @Test
    public void testParse_HumanUsingTypeDefBroken() throws Exception
    {
        try
        {
            AsnSchemaParser.parse(HUMAN_USING_TYPEDEF_BROKEN);
            fail("Should have thrown parse exception");
        }
        catch (ParseException e)
        {

        }
    }

    @Test
    public void testImports() throws Exception
    {
        String schemaFilename = getClass().getResource("/TestImports.asn").getFile();
        File schemaFile = new File(schemaFilename);
        final AsnSchema schema = AsnSchemaFileReader.read(schemaFile);

        {
            String tag = "/0/1/1/0/4/7";
            logger.info("get tag " + tag);
            OperationResult<DecodedTag> result = schema.getDecodedTag(tag, "PS-PDU");
            if (result.wasSuccessful())
            {
                DecodedTag r = result.getOutput();
                logger.info("decoded tag: {}", r.getTag());
            }
        }
    }


    private void debugPdus(ImmutableList<DecodedAsnData> pdus)
    {
        for (int i = 0; i < pdus.size(); i++)
        {

            logger.info("Parsing PDU[{}]", i);
            final DecodedAsnData pdu = pdus.get(i);
            for (String t : pdu.getTags())
            {
                logger.info("\t{} => {} as {}",
                        t, pdu.getHexString(t), pdu.getType(t).getBuiltinType() );
            }
            for (String t : pdu.getUnmappedTags())
            {
                logger.info("\t?{} => {}", t, pdu.getHexString(t));
            }
        }

    }
}