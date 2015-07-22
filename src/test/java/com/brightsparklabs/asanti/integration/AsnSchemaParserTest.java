package com.brightsparklabs.asanti.integration;

import com.brightsparklabs.asanti.common.DecodeException;
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
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;

import static org.junit.Assert.*;

/**
 * Testing end-to-end parsing, no mocks. Mostly I have just made this as a way to exercise the
 * parser(s) while making changes
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
            "BEGIN\n" +
            "    MyInt ::= INTEGER\n" +
            "END";

    private static final String HUMAN_SIMPLE = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
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
            "BEGIN\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       pickOne ENUMERATED\n" +
            "       {\n" +
            "           optA(0),\n" +
            "           optB(1)\n" +
            "       }\n" +
            "   }\n" +
            "END";

    private static final String HUMAN_SIMPLE_CHOICE = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
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
            "BEGIN\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       age [0]  PersonAge (1..15) OPTIONAL,\n" +
            "       name [1] PersonName" +
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
            "BEGIN\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       age [0] INTEGER (1..100),\n" +
            "       name [1] UTF8String,\n" +
            "       friends [2] SEQUENCE OF SEQUENCE\n" +
            "       {\n" +
            "           age [0] INTEGER (1..100),\n" +
            "           name [1] UTF8String\n" +
            "       }\n" +
            "   }\n" +
            "END";

    private static final String HUMAN_SEQUENCEOF_SEQUENCE4 = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
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

    private static final String HUMAN_BROKEN_MISSING_TYPEDEF = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
            "BEGIN\n" +
            "IMPORTS\n" +
            ";\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       age [0] PersonAge (1..150) OPTIONAL\n" +
            "   }\n" +
            "END";

    private static final String HUMAN_BROKEN_MISSING_IMPORT = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
            "BEGIN\n" +
            "IMPORTS\n" +
            "    PersonAge\n" +
            "    FROM MissingModule { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) missing(5) }\n"
            +
            ";\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       age [0] PersonAge (1..150) OPTIONAL\n" +
            "   }\n" +
            "END";

    private static final String HUMAN_BROKEN_MISSING_IMPORTED_TYPEDEF = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
            "BEGIN\n" +
            "IMPORTS\n" +
            "    PersonAge\n" +
            "    FROM OtherModule { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) missing(5) }\n"
            +
            ";\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       age [0] PersonAge (1..150) OPTIONAL\n" +
            "   }\n" +
            "END\n" +
            "OtherModule\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) missing(5) }\n"
            +
            "DEFINITIONS\n" +
            "AUTOMATIC TAGS ::=\n" +
            "BEGIN\n" +
            ";\n" +
            "   Foo ::= SEQUENCE\n" +
            "   {\n" +
            "       bar UTF8String\n" +
            "   }\n" +
            "END";

    private static final String HUMAN_DUPLICATE_CHOICE = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS IMPLICIT TAGS ::=\n" +
            "BEGIN\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       payload Payload\n" +
            "   }\n" +
            "   Payload ::= CHOICE\n" +
            "   {\n" +
            "       optA INTEGER,\n" +
            "       optB INTEGER\n" +
            "   }\n" +
            "END";

    private static final String HUMAN_DUPLICATE_SET = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS IMPLICIT TAGS ::=\n" +
            "BEGIN\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       payload Payload\n" +
            "   }\n" +
            "   Payload ::= SET\n" +
            "   {\n" +
            "       optA INTEGER,\n" +
            "       optB INTEGER\n" +
            "   }\n" +
            "END";

    private static final String HUMAN_DUPLICATE_SEQUENCE = "Test-Protocol\n" +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n"
            +
            "DEFINITIONS IMPLICIT TAGS ::=\n" +
            "BEGIN\n" +
            "   Human ::= SEQUENCE\n" +
            "   {\n" +
            "       payload Payload\n" +
            "   }\n" +
            "   Payload ::= SEQUENCE\n" +
            "   {\n" +
            "       optA INTEGER OPTIONAL,\n" +
            "       optB INTEGER\n" +
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

        ImmutableMap<String, String> tags = ImmutableMap.<String, String>builder()
                .put("", "/Document")
                .put("0[1]", "/Document/header")
                .put("0[1]/0[0]", "/Document/header/published")
                .put("0[1]/0[0]/1[1]", "/Document/header/published/date")
                .put("0[1]/0[0]/2[2]", "/Document/header/published/country")
                .put("1[2]", "/Document/body")
                .put("1[2]/0[0]", "/Document/body/lastModified")
                .put("1[2]/0[0]/0[0]", "/Document/body/lastModified/date")
                .put("1[2]/0[0]/1[1]/0[1]", "/Document/body/lastModified/modifiedBy/firstName")
                .put("1[2]/0[0]/1[1]/1[2]", "/Document/body/lastModified/modifiedBy/lastName")
                .put("1[2]/0[0]/1[1]/2[3]", "/Document/body/lastModified/modifiedBy/title")
                .put("1[2]/1[1]/0[1]", "/Document/body/prefix/text")
                .put("1[2]/2[2]/0[1]", "/Document/body/content/text")
                .put("1[2]/2[2]/1[2]/0[UNIVERSAL 16]/0[1]",
                        "/Document/body/content/paragraphs[0]/title")
                .put("1[2]/2[2]/1[2]/1[UNIVERSAL 16]/0[1]",
                        "/Document/body/content/paragraphs[1]/title")
                .put("1[2]/2[2]/1[2]/0[UNIVERSAL 16]/1[2]/0[1]",
                        "/Document/body/content/paragraphs[0]/contributor/firstName")
                .put("1[2]/2[2]/1[2]/0[UNIVERSAL 16]/1[2]/1[2]",
                        "/Document/body/content/paragraphs[0]/contributor/lastName")
                .put("1[2]/2[2]/1[2]/0[UNIVERSAL 16]/1[2]/2[3]",
                        "/Document/body/content/paragraphs[0]/contributor/title")
                .put("1[2]/2[2]/1[2]/99[UNIVERSAL 16]/0[1]",
                        "/Document/body/content/paragraphs[99]/title")
                .put("1[2]/2[2]/1[2]/99[UNIVERSAL 16]/1[2]/0[1]",
                        "/Document/body/content/paragraphs[99]/contributor/firstName")
                .put("1[2]/2[2]/1[2]/99[UNIVERSAL 16]/1[2]/1[2]",
                        "/Document/body/content/paragraphs[99]/contributor/lastName")
                .put("1[2]/2[2]/1[2]/99[UNIVERSAL 16]/1[2]/2[3]",
                        "/Document/body/content/paragraphs[99]/contributor/title")
                .put("1[2]/2[2]/1[2]/0[UNIVERSAL 16]/3[3]",
                        "/Document/body/content/paragraphs[0]/points")
                .put("1[2]/2[2]/1[2]/0[UNIVERSAL 16]/3[3]/0[UNIVERSAL 4]",
                        "/Document/body/content/paragraphs[0]/points[0]")
                .put("1[2]/2[2]/1[2]/11[UNIVERSAL 16]/0[1]",
                        "/Document/body/content/paragraphs[11]/title")
                .put("1[2]/2[2]/1[2]/11[UNIVERSAL 16]/3[3]/44[UNIVERSAL 4]",
                        "/Document/body/content/paragraphs[11]/points[44]")

                .put("1[2]/3[3]/0[1]", "/Document/body/suffix/text")

                .put("2[3]", "/Document/footer")
                .put("2[3]/0[0]", "/Document/footer/authors")
                .put("2[3]/0[0]/0[UNIVERSAL 16]/0[1]", "/Document/footer/authors[0]/firstName")
                .put("2[3]/0[0]/0[UNIVERSAL 16]/1[2]", "/Document/footer/authors[0]/lastName")
                .put("3[4]", "/Document/dueDate")
                .put("4[5]", "/Document/version")
                .put("4[5]/0[0]", "/Document/version/majorVersion")
                .put("4[5]/1[1]", "/Document/version/minorVersion")
                .put("5[6]", "/Document/description")
                .put("5[6]/0[0]", "/Document/description/numberLines")
                .put("5[6]/1[1]", "/Document/description/summary")
                .build();

        final ImmutableSet<OperationResult<DecodedTag>> decodedTags
                = instance.getDecodedTags(tags.keySet(), "Document");

        for (OperationResult<DecodedTag> decodedTag : decodedTags)
        {
            assertTrue(decodedTag.wasSuccessful());

            final DecodedTag output = decodedTag.getOutput();
            assertEquals(tags.get(output.getRawTag()), output.getTag());
        }

        //        assertEquals("/Document", instance.getDecodedTag("", "Document").getOutput().getTag());
        //
        //                assertEquals("/Document/header",
        //                        instance.getDecodedTag("1", "Document").getOutput().getTag());
        //                assertEquals("/Document/header/published",
        //                        instance.getDecodedTag("1/0", "Document").getOutput().getTag());
        //                assertEquals("/Document/header/published/date",
        //                        instance.getDecodedTag("1/0/1", "Document").getOutput().getTag());
        //                assertEquals("/Document/header/published/country",
        //                        instance.getDecodedTag("1/0/2", "Document").getOutput().getTag());
        //
        //                assertEquals("/Document/body",
        //                        instance.getDecodedTag("2", "Document").getOutput().getTag());
        //                assertEquals("/Document/body/lastModified",
        //                        instance.getDecodedTag("2/0", "Document").getOutput().getTag());
        //                assertEquals("/Document/body/lastModified/date",
        //                        instance.getDecodedTag("2/0/0", "Document").getOutput().getTag());
        //                assertEquals("/Document/body/lastModified/modifiedBy/firstName",
        //                        instance.getDecodedTag("2/0/1/1", "Document").getOutput().getTag());
        //                assertEquals("/Document/body/lastModified/modifiedBy/lastName",
        //                        instance.getDecodedTag("2/0/1/2", "Document").getOutput().getTag());
        //                assertEquals("/Document/body/lastModified/modifiedBy/title",
        //                        instance.getDecodedTag("2/0/1/3", "Document").getOutput().getTag());
        //                assertEquals("/Document/body/prefix/text",
        //                        instance.getDecodedTag("2/1/1", "Document").getOutput().getTag());
        //                assertEquals("/Document/body/content/text",
        //                        instance.getDecodedTag("2/2/1", "Document").getOutput().getTag());
        //                assertEquals("/Document/body/content/paragraphs/title",
        //                        instance.getDecodedTag("2/2/2/1", "Document").getOutput().getTag());
        //                assertEquals("/Document/body/content/paragraphs[0]/title",
        //                        instance.getDecodedTag("2/2/2[0]/1", "Document").getOutput().getTag());
        //                assertEquals("/Document/body/content/paragraphs[1]/title",
        //                        instance.getDecodedTag("2/2/2[1]/1", "Document").getOutput().getTag());
        //                assertEquals("/Document/body/content/paragraphs[0]/contributor/firstName",
        //                        instance.getDecodedTag("2/2/2[0]/2/1", "Document").getOutput().getTag());
        //                assertEquals("/Document/body/content/paragraphs[0]/contributor/lastName",
        //                        instance.getDecodedTag("2/2/2[0]/2/2", "Document").getOutput().getTag());
        //                assertEquals("/Document/body/content/paragraphs[0]/contributor/title",
        //                        instance.getDecodedTag("2/2/2[0]/2/3", "Document").getOutput().getTag());
        //                assertEquals("/Document/body/content/paragraphs[0]/points",
        //                        instance.getDecodedTag("2/2/2[0]/3", "Document").getOutput().getTag());
        //                assertEquals("/Document/body/content/paragraphs[0]/points[0]",
        //                        instance.getDecodedTag("2/2/2[0]/3[0]", "Document").getOutput().getTag());
        //                assertEquals("/Document/body/content/paragraphs[99]/title",
        //                        instance.getDecodedTag("2/2/2[99]/1", "Document").getOutput().getTag());
        //                assertEquals("/Document/body/content/paragraphs[99]/contributor/firstName",
        //                        instance.getDecodedTag("2/2/2[99]/2/1", "Document").getOutput().getTag());
        //                assertEquals("/Document/body/content/paragraphs[99]/contributor/lastName",
        //                        instance.getDecodedTag("2/2/2[99]/2/2", "Document").getOutput().getTag());
        //                assertEquals("/Document/body/content/paragraphs[99]/contributor/title",
        //                        instance.getDecodedTag("2/2/2[99]/2/3", "Document").getOutput().getTag());
        //                assertEquals("/Document/body/content/paragraphs[99]/points",
        //                        instance.getDecodedTag("2/2/2[99]/3", "Document").getOutput().getTag());
        //                assertEquals("/Document/body/content/paragraphs[99]/points[99]",
        //                        instance.getDecodedTag("2/2/2[99]/3[99]", "Document").getOutput().getTag());
        //                assertEquals("/Document/body/suffix/text",
        //                        instance.getDecodedTag("2/3/1", "Document").getOutput().getTag());
        //
        //                assertEquals("/Document/footer",
        //                        instance.getDecodedTag("3", "Document").getOutput().getTag());
        //                assertEquals("/Document/footer/authors",
        //                        instance.getDecodedTag("3/0", "Document").getOutput().getTag());
        //                assertEquals("/Document/footer/authors[0]/firstName",
        //                        instance.getDecodedTag("3/0[0]/1", "Document").getOutput().getTag());
        //                assertEquals("/Document/footer/authors[0]/lastName",
        //                        instance.getDecodedTag("3/0[0]/2", "Document").getOutput().getTag());
        //
        //                assertEquals("/Document/dueDate",
        //                        instance.getDecodedTag("4", "Document").getOutput().getTag());
        //
        //                assertEquals("/Document/version",
        //                        instance.getDecodedTag("5", "Document").getOutput().getTag());
        //                assertEquals("/Document/version/majorVersion",
        //                        instance.getDecodedTag("5/0", "Document").getOutput().getTag());
        //                assertEquals("/Document/version/minorVersion",
        //                        instance.getDecodedTag("5/1", "Document").getOutput().getTag());
        //
        //                assertEquals("/Document/description",
        //                        instance.getDecodedTag("6", "Document").getOutput().getTag());
        //                assertEquals("/Document/description/numberLines",
        //                        instance.getDecodedTag("6/0", "Document").getOutput().getTag());
        //                assertEquals("/Document/description/summary",
        //                        instance.getDecodedTag("6/1", "Document").getOutput().getTag());
        //
        //                // test partial
        //                assertEquals("/Document/header/published/99/98",
        //                        instance.getDecodedTag("1/0/99/98", "Document").getOutput().getTag());
        //                assertEquals("/Document/body/lastModified/99/98",
        //                        instance.getDecodedTag("2/0/99/98", "Document").getOutput().getTag());
        //
        //                // test unknown
        //                assertEquals("/Document/99/98",
        //                        instance.getDecodedTag("/99/98", "Document").getOutput().getTag());

    }

    @Test
    public void testParse_NoRoot() throws Exception
    {
        String schemaFilename = getClass().getResource("/Root_MyInt.asn").getFile();
        File schemaFile = new File(schemaFilename);
        final AsnSchema schema = AsnSchemaFileReader.read(schemaFile);

        String berFilename = getClass().getResource("/Root_MyInt.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "MyInt";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        debugPdus(pdus);

        // TODO ASN-151, we can't decode this at the root level because it is not a constructed type.
/*
        AsnSchema schema = AsnSchemaParser.parse(NO_ROOT_STRUCTURE);

        String tag = "/";
        logger.info("get tag " + tag);
        ImmutableSet<String> tags = ImmutableSet.of(tag);
        ImmutableSet<OperationResult<DecodedTag>> results = schema.getDecodedTags(tags, "MyInt");
        assertEquals(1, results.size());

        if (results.asList().get(0).wasSuccessful())
        {
            DecodedTag actualTag = results.asList().get(0).getOutput();
            logger.info(actualTag.getTag() + " : " + actualTag.getType().getBuiltinType());
        }
*/
    }

    @Test
    public void testParse_HumanSimple() throws Exception
    {
        AsnSchema schema = AsnSchemaParser.parse(HUMAN_SIMPLE);

        String berFilename = getClass().getResource("/Human_Simple.ber").getFile();
        final File berFile = new File(berFilename);

        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        DecodedAsnData pdu = pdus.get(0);
        String tag = "/Human/age";
        BigInteger age = (BigInteger) pdu.getDecodedObject(tag);
        logger.info(tag + " : " + age);
        assertEquals(new BigInteger("32"), age);

        tag = "/Human/name";
        String name = (String) pdu.getDecodedObject(tag);
        logger.info("{} : {}", tag, name);
        assertEquals("Adam", name);

    }

    @Test
    public void testParse_HumanSimple2() throws Exception
    {
        AsnSchema schema = AsnSchemaParser.parse(HUMAN_SIMPLE2);

        String berFilename = getClass().getResource("/Human_Simple2.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        DecodedAsnData pdu = pdus.get(0);

        assertEquals(0, pdu.getUnmappedTags().size());

        String tag = "/Human/age";
        BigInteger age = (BigInteger) pdu.getDecodedObject(tag);
        logger.info(tag + " : " + age);
        assertEquals(new BigInteger("32"), age);

        tag = "/Human/name";
        String name = (String) pdu.getDecodedObject(tag);
        logger.info(tag + " : " + name);
        assertEquals("Adam", name);

    }

    @Test
    public void testParse_HumanEnumerated() throws Exception
    {
        AsnSchema schema = AsnSchemaParser.parse(HUMAN_SIMPLE_ENUMERATED);

        String berFilename = getClass().getResource("/Human_SimpleEnumerated.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        DecodedAsnData pdu = pdus.get(0);

        assertEquals(0, pdu.getUnmappedTags().size());

        String tag = "/Human/pickOne";
        assertEquals(AsnPrimitiveType.ENUMERATED, pdu.getType(tag).getPrimitiveType());

        byte[] bytes = pdu.getBytes(tag);
        assertEquals(1, bytes[0]);

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
        BigInteger age = (BigInteger) pdu.getDecodedObject(tag);
        assertEquals(new BigInteger("32"), age);

        tag = "/Human/payload/optA/name";
        String name = (String) pdu.getDecodedObject(tag);
        assertEquals("Adam", name);
    }

    @Test
    public void testParse_HumanSimpleSet() throws Exception
    {
        AsnSchema schema = AsnSchemaParser.parse(HUMAN_SIMPLE_SET);

        String berFilename = getClass().getResource("/Human_SimpleSet.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        DecodedAsnData pdu = pdus.get(0);

        assertEquals(0, pdu.getUnmappedTags().size());

        String tag = "/Human/age";
        BigInteger age = (BigInteger) pdu.getDecodedObject(tag);
        logger.info(tag + " : " + age);
        assertEquals(new BigInteger("32"), age);

        tag = "/Human/name";
        String name = (String) pdu.getDecodedObject(tag);
        logger.info(tag + " : " + name);
        assertEquals("Adam", name);
    }

    @Test
    public void testParse_HumanNested() throws Exception
    {
        AsnSchema schema = AsnSchemaParser.parse(HUMAN_NESTED);

        String berFilename = getClass().getResource("/Human_Nested.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        DecodedAsnData pdu = pdus.get(0);

        assertEquals(0, pdu.getUnmappedTags().size());

        String tag = "/Human/name/first";
        String first = (String) pdu.getDecodedObject(tag);
        logger.info(tag + " : " + first);
        assertEquals("Adam", first);

        tag = "/Human/name/last";
        String last = (String) pdu.getDecodedObject(tag);
        logger.info(tag + " : " + last);
        assertEquals("Smith", last);

        tag = "/Human/age";
        BigInteger age = (BigInteger) pdu.getDecodedObject(tag);
        logger.info(tag + " : " + age);
        assertEquals(new BigInteger("32"), age);

        final ValidatorImpl validator = new ValidatorImpl();
        final ValidationResult validationresult = validator.validate(pdu);

        assertFalse(validationresult.hasFailures());

    }

    @Test
    public void testParse_HumanUsingTypeDef() throws Exception
    {
        AsnSchema schema = AsnSchemaParser.parse(HUMAN_USING_TYPEDEF);

        String berFilename = getClass().getResource("/Human_Typedef.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        DecodedAsnData pdu = pdus.get(0);

        assertEquals(0, pdu.getUnmappedTags().size());

        String tag = "/Human/age";
        BigInteger age = (BigInteger) pdu.getDecodedObject(tag);
        logger.info(tag + " : " + age);
        assertEquals(new BigInteger("32"), age);

        final ValidatorImpl validator = new ValidatorImpl();
        final ValidationResult validationresult = validator.validate(pdu);

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

        String berFilename = getClass().getResource("/Human_Typedef.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        debugPdus(pdus);

        DecodedAsnData pdu = pdus.get(0);

        assertEquals(0, pdu.getUnmappedTags().size());

        String tag = "/Human/age";
        BigInteger age = (BigInteger) pdu.getDecodedObject(tag);
        logger.info(tag + " : " + age);

        final ValidatorImpl validator = new ValidatorImpl();
        final ValidationResult validationresult = validator.validate(pdu);

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

        AsnSchema schema = AsnSchemaParser.parse(HUMAN_USING_TYPEDEF_SEQUENCE);

        String berFilename = getClass().getResource("/Human_TypedefSequence.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        debugPdus(pdus);

        DecodedAsnData pdu = pdus.get(0);

        assertEquals(0, pdu.getUnmappedTags().size());

        String tag = "/Human/age";

        AsnBuiltinType builtinType = pdu.getType(tag).getBuiltinType();
        assertEquals(AsnBuiltinType.Integer, builtinType);

        BigInteger age = (BigInteger) pdu.getDecodedObject(tag);
        logger.info(tag + " : " + age);
        assertEquals(new BigInteger("32"), age);

        tag = "/Human/name/first";
        String first = (String) pdu.getDecodedObject(tag);
        logger.info(tag + " : " + first);
        assertEquals("Adam", first);

        tag = "/Human/name/last";
        String last = (String) pdu.getDecodedObject(tag);
        logger.info(tag + " : " + last);
        assertEquals("Smith", last);

/*
        final ValidatorImpl validator = new ValidatorImpl();
        final ValidationResult validationresult = validator.validate(pdu);

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

        String tag = "/Human/age[0]";
        assertEquals(new BigInteger("1"), pdus.get(0).getDecodedObject(tag));
        tag = "/Human/age[1]";
        assertEquals(new BigInteger("2"), pdus.get(0).getDecodedObject(tag));
        tag = "/Human/age[2]";
        assertEquals(new BigInteger("3"), pdus.get(0).getDecodedObject(tag));

        final ValidatorImpl validator = new ValidatorImpl();
        final ValidationResult validationresult = validator.validate(pdus.get(0));
        assertFalse(validationresult.hasFailures());

    }

    @Test
    public void testParse_HumanSequenceOfSequence3() throws Exception
    {
        AsnSchema schema = AsnSchemaParser.parse(HUMAN_SEQUENCEOF_SEQUENCE3);

        String berFilename = getClass().getResource("/Human_SequenceOfSequence3.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        DecodedAsnData pdu = pdus.get(0);

        assertEquals(0, pdu.getUnmappedTags().size());

        debugPdus(pdus);

        String tag = "/Human/age";
        BigInteger age = (BigInteger) pdu.getDecodedObject(tag);
        assertEquals(32, age.intValue());

        tag = "/Human/name";
        String name = (String) pdu.getDecodedObject(tag);
        assertEquals("Adam", name);

        tag = "/Human/friends[0]/name";
        name = (String) pdu.getDecodedObject(tag);
        assertEquals("Finn", name);

        tag = "/Human/friends[0]/age";
        age = (BigInteger) pdu.getDecodedObject(tag);
        assertEquals(5, age.intValue());

        tag = "/Human/friends[1]/name";
        name = (String) pdu.getDecodedObject(tag);
        assertEquals("Fatty", name);

        tag = "/Human/friends[1]/age";
        age = (BigInteger) pdu.getDecodedObject(tag);
        assertEquals(3, age.intValue());

        final ValidatorImpl validator = new ValidatorImpl();
        final ValidationResult validationresult = validator.validate(pdus.get(0));
        assertFalse(validationresult.hasFailures());
    }

    @Test
    public void testParse_HumanSequenceOfSequence4() throws Exception
    {

        AsnSchema schema = AsnSchemaParser.parse(HUMAN_SEQUENCEOF_SEQUENCE4);

        String berFilename = getClass().getResource("/Human_SequenceOfSequence3.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        DecodedAsnData pdu = pdus.get(0);

        assertEquals(0, pdu.getUnmappedTags().size());

        debugPdus(pdus);

        String tag = "/Human/age";
        BigInteger age = (BigInteger) pdu.getDecodedObject(tag);
        assertEquals(32, age.intValue());

        tag = "/Human/name";
        String name = (String) pdu.getDecodedObject(tag);
        assertEquals("Adam", name);

        tag = "/Human/friends[0]/name";
        name = (String) pdu.getDecodedObject(tag);
        assertEquals("Finn", name);

        tag = "/Human/friends[0]/age";
        age = (BigInteger) pdu.getDecodedObject(tag);
        assertEquals(5, age.intValue());

        tag = "/Human/friends[1]/name";
        name = (String) pdu.getDecodedObject(tag);
        assertEquals("Fatty", name);

        tag = "/Human/friends[1]/age";
        age = (BigInteger) pdu.getDecodedObject(tag);
        assertEquals(3, age.intValue());

        final ValidatorImpl validator = new ValidatorImpl();
        final ValidationResult validationresult = validator.validate(pdus.get(0));
        assertFalse(validationresult.hasFailures());
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

        assertEquals(0, pdu.getUnmappedTags().size());

        for (String tag : pdu.getTags())
        {
            logger.info("\t{} => {}", tag, pdu.getHexString(tag));
        }
        for (String tag : pdu.getUnmappedTags())
        {
            logger.info("\t{} => {}", tag, pdu.getHexString(tag));
        }

        final ValidatorImpl validator = new ValidatorImpl();
        final ValidationResult validationresult = validator.validate(pdus.get(0));
        assertFalse(validationresult.hasFailures());
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
            logger.info("\t{} => {} as {}",
                    tag,
                    pdu.getHexString(tag),
                    pdu.getType(tag).getBuiltinType());
        }

        String tag = "/Human/faveNumbers";
        BigInteger fave = (BigInteger) pdu.getDecodedObject(tag + "[0]");
        logger.info("fave[0]: {}", fave);

        fave = (BigInteger) pdu.getDecodedObject(tag + "[1]");
        logger.info("fave[1]: {}", fave);

        tag = "/Human/faveNumbers";
        fave = (BigInteger) pdu.getDecodedObject(tag + "[2]");
        logger.info("fave[2]: {}", fave);

        final ValidatorImpl validator = new ValidatorImpl();
        final ValidationResult validationresult = validator.validate(pdus.get(0));
        assertFalse(validationresult.hasFailures());
    }

    @Test
    public void testParse_ImplicitTagging() throws Exception
    {
        String schemaFilename = getClass().getResource("/Human_ImplicitTagging.asn").getFile();
        File schemaFile = new File(schemaFilename);
        final AsnSchema schema = AsnSchemaFileReader.read(schemaFile);

        String berFilename = getClass().getResource("/Human_ImplicitTagging.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);
        debugPdus((pdus));

        final ValidatorImpl validator = new ValidatorImpl();
        final ValidationResult validationresult = validator.validate(pdus.get(0));
        assertFalse(validationresult.hasFailures());
    }

    @Test
    public void testParse_ImplicitTagging2() throws Exception
    {
        String schemaFilename = getClass().getResource("/Human_ImplicitTagging2.asn").getFile();
        File schemaFile = new File(schemaFilename);
        final AsnSchema schema = AsnSchemaFileReader.read(schemaFile);

        String berFilename = getClass().getResource("/Human_ImplicitTagging2.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);
        debugPdus((pdus));
        String tag = "/Human/payload/lastName";
        assertEquals("Smith", pdus.get(0).getDecodedObject(tag));
        tag = "/Human/payload/firstName";
        assertEquals("Adam", pdus.get(0).getDecodedObject(tag));

        final ValidatorImpl validator = new ValidatorImpl();
        final ValidationResult validationresult = validator.validate(pdus.get(0));
        assertFalse(validationresult.hasFailures());
    }

    @Test
    public void testParse_ImplicitTagging3() throws Exception
    {
        String schemaFilename = getClass().getResource("/Human_ImplicitTagging3.asn").getFile();
        File schemaFile = new File(schemaFilename);
        final AsnSchema schema = AsnSchemaFileReader.read(schemaFile);

        String berFilename = getClass().getResource("/Human_ImplicitTagging3.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);
        debugPdus((pdus));
        String tag = "/Human/payload/a";
        assertEquals(new BigInteger("10"), pdus.get(0).getDecodedObject(tag));
        tag = "/Human/payload/b/i";
        assertEquals(new BigInteger("1"), pdus.get(0).getDecodedObject(tag));
        tag = "/Human/payload/c";
        assertEquals("U", pdus.get(0).getDecodedObject(tag));
    }

    @Test
    public void test_ReuseTypeWithOptional() throws Exception
    {
        // This tests our decoding session management by re-using a Type Definition and
        // having optional components.
        String schemaFilename = getClass().getResource("/Human_ReuseWithOptional.asn").getFile();
        File schemaFile = new File(schemaFilename);
        final AsnSchema schema = AsnSchemaFileReader.read(schemaFile);

        String berFilename = getClass().getResource("/Human_ReuseWithOptional.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);
        debugPdus(pdus);

        String tag = "/Human/a/a";
        assertEquals(new BigInteger("10"), pdus.get(0).getDecodedObject(tag));
        tag = "/Human/a/b";
        assertEquals("U", pdus.get(0).getDecodedObject(tag));
        tag = "/Human/a/c";
        assertEquals("V", pdus.get(0).getDecodedObject(tag));
        tag = "/Human/b/b";
        assertEquals("A", pdus.get(0).getDecodedObject(tag));
        tag = "/Human/b/c";
        assertEquals("B", pdus.get(0).getDecodedObject(tag));
    }

    @Test
    public void testParse_NonUniqueTags() throws Exception
    {
        String schemaFilename = getClass().getResource("/Human_NonUniqueTags.asn").getFile();
        File schemaFile = new File(schemaFilename);
        final AsnSchema schema = AsnSchemaFileReader.read(schemaFile);

        {
            String berFilename = getClass().getResource("/Human_NonUniqueTags.ber").getFile();
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);
            debugPdus((pdus));

            String tag = "/Human/c";
            assertEquals("A", pdus.get(0).getDecodedObject(tag));
            tag = "/Human/b";
            assertEquals(new BigInteger("10"), pdus.get(0).getDecodedObject(tag));
            tag = "/Human/a";
            assertEquals("U", pdus.get(0).getDecodedObject(tag));
        }
    }

    @Test
    public void testParse_NonUniqueTagsImplicit() throws Exception
    {
        String schemaFilename = getClass().getResource("/Human_NonUniqueTagsImplicit.asn")
                .getFile();
        File schemaFile = new File(schemaFilename);
        final AsnSchema schema = AsnSchemaFileReader.read(schemaFile);

        {
            String berFilename = getClass().getResource("/Human_NonUniqueTagsImplicit.ber")
                    .getFile();
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);
            debugPdus((pdus));

            String tag = "/Human/c";
            assertEquals("A", pdus.get(0).getDecodedObject(tag));
            tag = "/Human/b";
            assertEquals(new BigInteger("10"), pdus.get(0).getDecodedObject(tag));
            tag = "/Human/a";
            assertEquals("U", pdus.get(0).getDecodedObject(tag));
        }
    }

    @Test
    public void testParse_NonUniqueTagsOptional() throws Exception
    {
        String schemaFilename = getClass().getResource("/Human_NonUniqueTagsOptional.asn")
                .getFile();
        File schemaFile = new File(schemaFilename);
        final AsnSchema schema = AsnSchemaFileReader.read(schemaFile);

        {
            String berFilename = getClass().getResource(
                    "/Human_NonUniqueTagsOptional_allpresent.ber").getFile();
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);
            debugPdus((pdus));

            String tag = "/Human/c";
            assertEquals("A", pdus.get(0).getDecodedObject(tag));
            tag = "/Human/b";
            assertEquals(new BigInteger("10"), pdus.get(0).getDecodedObject(tag));
            tag = "/Human/a";
            assertEquals("U", pdus.get(0).getDecodedObject(tag));
        }
    }

    @Test
    public void testParse_NonUniqueTagsOptional_missing() throws Exception
    {
        String schemaFilename = getClass().getResource("/Human_NonUniqueTagsOptional.asn")
                .getFile();
        File schemaFile = new File(schemaFilename);
        final AsnSchema schema = AsnSchemaFileReader.read(schemaFile);

        {
            String berFilename = getClass().getResource(
                    "/Human_NonUniqueTagsOptional_noOptional.ber").getFile();
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);
            debugPdus((pdus));

            String tag = "/Human/b";
            assertEquals(new BigInteger("10"), pdus.get(0).getDecodedObject(tag));
            tag = "/Human/a";
            assertEquals("U", pdus.get(0).getDecodedObject(tag));
        }
    }

    @Test
    public void testParse_SetOfChoice() throws Exception
    {
        String schemaFilename = getClass().getResource("/Human_SetOfChoice.asn").getFile();
        File schemaFile = new File(schemaFilename);
        final AsnSchema schema = AsnSchemaFileReader.read(schemaFile);

        {
            String berFilename = getClass().getResource("/Human_SetOfChoice.ber").getFile();
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);

            debugPdus((pdus));

            String tag = "/Human/payload/name";
            assertEquals("Adam", pdus.get(0).getDecodedObject(tag));
            tag
                    = "/Human/payload/supplementary-Services-Information/non-Standard-Supplementary-Services[0]/simpleIndication";
            byte[] bytes = pdus.get(0).getBytes(tag);
            assertEquals(0, bytes[0]);
        }
        {
            String berFilename = getClass().getResource("/Human_SetOfChoice_2items.ber").getFile();
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);

            debugPdus((pdus));

            String tag = "/Human/payload/name";
            assertEquals("Adam", pdus.get(0).getDecodedObject(tag));
            tag
                    = "/Human/payload/supplementary-Services-Information/non-Standard-Supplementary-Services[0]/simpleIndication";
            byte[] bytes = pdus.get(0).getBytes(tag);
            assertEquals(0, bytes[0]);
            tag
                    = "/Human/payload/supplementary-Services-Information/non-Standard-Supplementary-Services[1]/simpleIndication";
            bytes = pdus.get(0).getBytes(tag);
            assertEquals(1, bytes[0]);
        }

    }

    @Test
    public void testParse_SetOfUnTaggedChoice() throws Exception
    {
        String schemaFilename = getClass().getResource("/Human_SetOfUnTaggedChoice.asn").getFile();
        File schemaFile = new File(schemaFilename);
        final AsnSchema schema = AsnSchemaFileReader.read(schemaFile);

        String berFilename = getClass().getResource("/Human_SetOfUnTaggedChoice.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        debugPdus((pdus));

        String tag = "/Human/name[0]/a";
        assertEquals(new BigInteger("10"), pdus.get(0).getDecodedObject(tag));
        tag = "/Human/name[1]/b";
        assertEquals("U", pdus.get(0).getDecodedObject(tag));
    }

    @Test
    public void testParse_SetOfSetOfUnTaggedChoice() throws Exception
    {
        String schemaFilename = getClass().getResource("/Human_SetOfSetOfUnTaggedChoice.asn")
                .getFile();
        File schemaFile = new File(schemaFilename);
        final AsnSchema schema = AsnSchemaFileReader.read(schemaFile);

        String berFilename = getClass().getResource("/Human_SetOfSetOfUnTaggedChoice.ber")
                .getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);

        debugPdus((pdus));

        String tag = "/Human/name[0][0]/a";
        assertEquals(new BigInteger("10"), pdus.get(0).getDecodedObject(tag));
        tag = "/Human/name[0][1]/b";
        assertEquals("U", pdus.get(0).getDecodedObject(tag));
        tag = "/Human/name[1][0]/a";
        assertEquals(new BigInteger("11"), pdus.get(0).getDecodedObject(tag));
        tag = "/Human/name[1][1]/b";
        assertEquals("V", pdus.get(0).getDecodedObject(tag));
    }

    @Test
    public void testParse_SequenceOf() throws Exception
    {
        String schemaFilename = getClass().getResource("/Human_SequenceOf.asn").getFile();
        File schemaFile = new File(schemaFilename);
        final AsnSchema schema = AsnSchemaFileReader.read(schemaFile);

        {
            String berFilename = getClass().getResource("/Human_SequenceOf_optA.ber").getFile();
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);

            debugPdus((pdus));

            String tag = "/Human/selection/optA/ints[0]";
            assertEquals(new BigInteger("1"), pdus.get(0).getDecodedObject(tag));
            tag = "/Human/selection/optA/ints[1]";
            assertEquals(new BigInteger("2"), pdus.get(0).getDecodedObject(tag));

            final ValidatorImpl validator = new ValidatorImpl();
            final ValidationResult validationresult = validator.validate(pdus.get(0));
            assertFalse(validationresult.hasFailures());
        }
        {
            String berFilename = getClass().getResource("/Human_SequenceOf_optB.ber").getFile();
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);

            debugPdus((pdus));

            String tag = "/Human/selection/optB/namesInline[0]/first";
            assertEquals("Adam", pdus.get(0).getDecodedObject(tag));
            tag = "/Human/selection/optB/namesInline[0]/last";
            assertEquals("Smith", pdus.get(0).getDecodedObject(tag));

            tag = "/Human/selection/optB/namesInline[1]/first";
            assertEquals("Michael", pdus.get(0).getDecodedObject(tag));
            tag = "/Human/selection/optB/namesInline[1]/last";
            assertEquals("Brown", pdus.get(0).getDecodedObject(tag));

            final ValidatorImpl validator = new ValidatorImpl();
            final ValidationResult validationresult = validator.validate(pdus.get(0));
            assertFalse(validationresult.hasFailures());
        }
        {
            String berFilename = getClass().getResource("/Human_SequenceOf_optC.ber").getFile();
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);

            debugPdus((pdus));

            String tag = "/Human/selection/optC/names[0]/first";
            assertEquals("Adam", pdus.get(0).getDecodedObject(tag));
            tag = "/Human/selection/optC/names[0]/last";
            assertEquals("Smith", pdus.get(0).getDecodedObject(tag));

            tag = "/Human/selection/optC/names[1]/first";
            assertEquals("Michael", pdus.get(0).getDecodedObject(tag));
            tag = "/Human/selection/optC/names[1]/last";
            assertEquals("Brown", pdus.get(0).getDecodedObject(tag));

            final ValidatorImpl validator = new ValidatorImpl();
            final ValidationResult validationresult = validator.validate(pdus.get(0));
            assertFalse(validationresult.hasFailures());
        }

    }

    @Test
    public void testParse_SetOf() throws Exception
    {
        String schemaFilename = getClass().getResource("/Human_SetOf.asn").getFile();
        File schemaFile = new File(schemaFilename);
        final AsnSchema schema = AsnSchemaFileReader.read(schemaFile);

        {
            String berFilename = getClass().getResource("/Human_SetOf_optA.ber").getFile();
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);

            debugPdus((pdus));

            String tag = "/Human/selection/optA/ints[0]";
            assertEquals(new BigInteger("1"), pdus.get(0).getDecodedObject(tag));
            tag = "/Human/selection/optA/ints[1]";
            assertEquals(new BigInteger("2"), pdus.get(0).getDecodedObject(tag));

            final ValidatorImpl validator = new ValidatorImpl();
            final ValidationResult validationresult = validator.validate(pdus.get(0));
            assertFalse(validationresult.hasFailures());
        }
        {
            String berFilename = getClass().getResource("/Human_SetOf_optB.ber").getFile();
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);

            debugPdus((pdus));

            String tag = "/Human/selection/optB/namesInline[0]/first";
            assertEquals("Adam", pdus.get(0).getDecodedObject(tag));
            tag = "/Human/selection/optB/namesInline[0]/last";
            assertEquals("Smith", pdus.get(0).getDecodedObject(tag));

            tag = "/Human/selection/optB/namesInline[1]/first";
            assertEquals("Michael", pdus.get(0).getDecodedObject(tag));
            tag = "/Human/selection/optB/namesInline[1]/last";
            assertEquals("Brown", pdus.get(0).getDecodedObject(tag));

            final ValidatorImpl validator = new ValidatorImpl();
            final ValidationResult validationresult = validator.validate(pdus.get(0));
            assertFalse(validationresult.hasFailures());
        }
        {
            String berFilename = getClass().getResource("/Human_SetOf_optC.ber").getFile();
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);

            debugPdus((pdus));

            String tag = "/Human/selection/optC/names[0]/first";
            assertEquals("Adam", pdus.get(0).getDecodedObject(tag));
            tag = "/Human/selection/optC/names[0]/last";
            assertEquals("Smith", pdus.get(0).getDecodedObject(tag));

            tag = "/Human/selection/optC/names[1]/first";
            assertEquals("Michael", pdus.get(0).getDecodedObject(tag));
            tag = "/Human/selection/optC/names[1]/last";
            assertEquals("Brown", pdus.get(0).getDecodedObject(tag));

            final ValidatorImpl validator = new ValidatorImpl();
            final ValidationResult validationresult = validator.validate(pdus.get(0));
            assertFalse(validationresult.hasFailures());
        }

    }

    @Test
    public void testParse_ChoiceImplicit() throws Exception
    {
        String schemaFilename = getClass().getResource("/Human_ChoiceImplicit.asn").getFile();
        File schemaFile = new File(schemaFilename);
        final AsnSchema schema = AsnSchemaFileReader.read(schemaFile);

        String berFilename = getClass().getResource("/Human_ChoiceImplicit_milliSeconds.ber")
                .getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);
        debugPdus(pdus);

        DecodedAsnData pdu = pdus.get(0);
        String tag = "/Human/payload/name";
        assertEquals("Adam", pdu.getDecodedObject(tag));

        tag = "/Human/payload/open/milliSeconds";
        assertEquals(new BigInteger("100"), pdu.getDecodedObject(tag));

        final ValidatorImpl validator = new ValidatorImpl();
        final ValidationResult validationresult = validator.validate(pdus.get(0));
        assertFalse(validationresult.hasFailures());
    }

    @Test
    public void testParse_Choice_ZZZ() throws Exception
    {
        String schemaFilename = getClass().getResource("/Human_Choice_ZZZ.asn").getFile();
        File schemaFile = new File(schemaFilename);
        final AsnSchema schema = AsnSchemaFileReader.read(schemaFile);

        {
            String berFilename = getClass().getResource("/Human_Choice_ZZZ.ber").getFile();
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);
            debugPdus(pdus);

            DecodedAsnData pdu = pdus.get(0);
            String tag = "/Human/payload/age/dob";
            byte[] actual = (byte[]) pdu.getDecodedObject(tag);
            assertEquals("1973", new String(actual, Charsets.UTF_8));

            tag = "/Human/payload/name";
            assertEquals("Fred", pdu.getDecodedObject(tag));

            tag = "/Human/payload/cin/iri-to-CC/cc[0]";
            actual = (byte[]) pdu.getDecodedObject(tag);
            assertEquals("123", new String(actual, Charsets.UTF_8));

            final ValidatorImpl validator = new ValidatorImpl();
            final ValidationResult validationresult = validator.validate(pdus.get(0));
            assertFalse(validationresult.hasFailures());
        }
        {
            String berFilename = getClass().getResource("/Human_Choice_ZZZ_2.ber").getFile();
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);
            debugPdus(pdus);

            DecodedAsnData pdu = pdus.get(0);
            String tag = "/Human/payload/age/dob";
            byte[] actual = (byte[]) pdu.getDecodedObject(tag);
            assertEquals("1973", new String(actual, Charsets.UTF_8));

            tag = "/Human/payload/name";
            assertEquals("Fred", pdu.getDecodedObject(tag));

            tag = "/Human/payload/cin/iri-to-CC/cc[0]";
            actual = (byte[]) pdu.getDecodedObject(tag);
            assertEquals("123", new String(actual, Charsets.UTF_8));

            tag = "/Human/payload/cin/iri-to-CC/cc[1]";
            actual = (byte[]) pdu.getDecodedObject(tag);
            assertEquals("456", new String(actual, Charsets.UTF_8));

            final ValidatorImpl validator = new ValidatorImpl();
            final ValidationResult validationresult = validator.validate(pdus.get(0));
            assertFalse(validationresult.hasFailures());
        }
    }

    @Test
    public void testParse_ChoicePassthrough_basic() throws Exception
    {
        String schemaFilename = getClass().getResource("/Human_Choice_basic.asn").getFile();
        File schemaFile = new File(schemaFilename);
        final AsnSchema schema = AsnSchemaFileReader.read(schemaFile);

        {
            String berFilename = getClass().getResource("/Human_Choice_basic_roundYears.ber")
                    .getFile();
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);
            debugPdus(pdus);

            DecodedAsnData pdu = pdus.get(0);
            String tag = "/Human/payload/age/roundYears";
            assertEquals(new BigInteger("42"), pdu.getDecodedObject(tag));

            tag = "/Human/payload/name";
            assertEquals("Fred", pdu.getDecodedObject(tag));

            final ValidatorImpl validator = new ValidatorImpl();
            final ValidationResult validationresult = validator.validate(pdus.get(0));
            assertFalse(validationresult.hasFailures());
        }
        {
            String berFilename = getClass().getResource("/Human_Choice_basic_ymd.ber").getFile();
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);
            debugPdus(pdus);

            DecodedAsnData pdu = pdus.get(0);
            String tag = "/Human/payload/age/ymd/years";
            assertEquals(new BigInteger("42"), pdu.getDecodedObject(tag));
            tag = "/Human/payload/age/ymd/months";
            assertEquals(new BigInteger("2"), pdu.getDecodedObject(tag));
            tag = "/Human/payload/age/ymd/days";
            assertEquals(new BigInteger("22"), pdu.getDecodedObject(tag));

            tag = "/Human/payload/name";
            assertEquals("Fred", pdu.getDecodedObject(tag));

            final ValidatorImpl validator = new ValidatorImpl();
            final ValidationResult validationresult = validator.validate(pdus.get(0));
            assertFalse(validationresult.hasFailures());
        }
        {
            String berFilename = getClass().getResource("/Human_Choice_basic_dob.ber").getFile();
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);
            debugPdus(pdus);

            DecodedAsnData pdu = pdus.get(0);
            String tag = "/Human/payload/age/dob";
            byte[] actual = (byte[]) pdu.getDecodedObject(tag);
            assertEquals("1973", new String(actual, Charsets.UTF_8));

            tag = "/Human/payload/name";
            assertEquals("Fred", pdu.getDecodedObject(tag));

            final ValidatorImpl validator = new ValidatorImpl();
            final ValidationResult validationresult = validator.validate(pdus.get(0));
            assertFalse(validationresult.hasFailures());
        }

    }

    @Test
    public void testParse_ChoicePassthrough() throws Exception
    {
        String schemaFilename = getClass().getResource("/Human_Choice2.asn").getFile();
        File schemaFile = new File(schemaFilename);
        final AsnSchema schema = AsnSchemaFileReader.read(schemaFile);

        {
            String berFilename = getClass().getResource("/Human_Choice2_typeA.ber").getFile();
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);
            debugPdus(pdus);

            DecodedAsnData pdu = pdus.get(0);
            String tag = "/Human/payload/iRIsContent/typeA/mid/other";
            assertEquals(new BigInteger("10"), pdu.getDecodedObject(tag));

            tag = "/Human/payload/iRIsContent/typeA/mid/stuff";
            assertEquals("U", pdu.getDecodedObject(tag));

            tag = "/Human/payload/name";
            assertEquals("payload", pdu.getDecodedObject(tag));

            final ValidatorImpl validator = new ValidatorImpl();
            final ValidationResult validationresult = validator.validate(pdus.get(0));
            assertFalse(validationresult.hasFailures());
        }
        {
            String berFilename = getClass().getResource("/Human_Choice2_int.ber").getFile();
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);
            debugPdus(pdus);

            DecodedAsnData pdu = pdus.get(0);
            String tag = "/Human/payload/iRIsContent/int";
            assertEquals(new BigInteger("10"), pdu.getDecodedObject(tag));

            tag = "/Human/payload/name";
            assertEquals("payload", pdu.getDecodedObject(tag));

            final ValidatorImpl validator = new ValidatorImpl();
            final ValidationResult validationresult = validator.validate(pdus.get(0));
            assertFalse(validationresult.hasFailures());
        }

        {
            String berFilename = getClass().getResource("/Human_Choice2_sofA.ber").getFile();
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);
            debugPdus(pdus);

            DecodedAsnData pdu = pdus.get(0);
            String tag = "/Human/payload/iRIsContent/sequenceOfA[0]/mid/other";
            assertEquals(new BigInteger("10"), pdu.getDecodedObject(tag));

            tag = "/Human/payload/iRIsContent/sequenceOfA[0]/mid/stuff";
            assertEquals("U", pdu.getDecodedObject(tag));

            tag = "/Human/payload/name";
            assertEquals("payload", pdu.getDecodedObject(tag));

            final ValidatorImpl validator = new ValidatorImpl();
            final ValidationResult validationresult = validator.validate(pdus.get(0));
            assertFalse(validationresult.hasFailures());
        }
        {
            String berFilename = getClass().getResource("/Human_Choice2_sofA_2_mid_entries.ber")
                    .getFile();
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);
            debugPdus(pdus);

            DecodedAsnData pdu = pdus.get(0);
            String tag = "/Human/payload/iRIsContent/sequenceOfA[0]/mid/other";
            assertEquals(new BigInteger("10"), pdu.getDecodedObject(tag));

            tag = "/Human/payload/iRIsContent/sequenceOfA[0]/mid/stuff";
            assertEquals("U", pdu.getDecodedObject(tag));
            tag = "/Human/payload/name";
            assertEquals("payload", pdu.getDecodedObject(tag));

            tag = "/Human/payload/iRIsContent/sequenceOfA[1]/mid/other";
            assertEquals(new BigInteger("11"), pdu.getDecodedObject(tag));
            tag = "/Human/payload/iRIsContent/sequenceOfA[1]/mid/stuff";
            assertEquals("V", pdu.getDecodedObject(tag));

            final ValidatorImpl validator = new ValidatorImpl();
            final ValidationResult validationresult = validator.validate(pdus.get(0));
            assertFalse(validationresult.hasFailures());
        }
        {
            String berFilename = getClass().getResource("/Human_Choice2_setOfA.ber").getFile();
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);
            debugPdus(pdus);

            DecodedAsnData pdu = pdus.get(0);
            String tag = "/Human/payload/iRIsContent/setOfA[0]/mid/other";
            assertEquals(new BigInteger("10"), pdu.getDecodedObject(tag));

            tag = "/Human/payload/iRIsContent/setOfA[0]/mid/stuff";
            assertEquals("U", pdu.getDecodedObject(tag));

            tag = "/Human/payload/name";
            assertEquals("payload", pdu.getDecodedObject(tag));

            final ValidatorImpl validator = new ValidatorImpl();
            final ValidationResult validationresult = validator.validate(pdus.get(0));
            assertFalse(validationresult.hasFailures());
        }
        {
            String berFilename = getClass().getResource("/Human_Choice2_setOfA_2entries.ber")
                    .getFile();
            final File berFile = new File(berFilename);
            String topLevelType = "Human";

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);
            debugPdus(pdus);

            DecodedAsnData pdu = pdus.get(0);
            String tag = "/Human/payload/iRIsContent/setOfA[0]/mid/other";
            assertEquals(new BigInteger("10"), pdu.getDecodedObject(tag));

            tag = "/Human/payload/iRIsContent/setOfA[0]/mid/stuff";
            assertEquals("U", pdu.getDecodedObject(tag));
            tag = "/Human/payload/name";
            assertEquals("payload", pdu.getDecodedObject(tag));

            tag = "/Human/payload/iRIsContent/setOfA[1]/mid/other";
            assertEquals(new BigInteger("11"), pdu.getDecodedObject(tag));
            tag = "/Human/payload/iRIsContent/setOfA[1]/mid/stuff";
            assertEquals("V", pdu.getDecodedObject(tag));

            final ValidatorImpl validator = new ValidatorImpl();
            final ValidationResult validationresult = validator.validate(pdus.get(0));
            assertFalse(validationresult.hasFailures());
        }
    }

    @Test
    public void testParse_ChoiceDuplicate() throws Exception
    {
        try
        {
            String schemaFilename = getClass().getResource("/ChoiceDuplicate.asn").getFile();
            File schemaFile = new File(schemaFilename);
            AsnSchemaFileReader.read(schemaFile);
            fail("Should have thrown an IOException because of duplicate tags");
        }
        catch (IOException e)
        {
        }
    }

    @Test
    public void testParse_Choice3() throws Exception
    {
        String schemaFilename = getClass().getResource("/Human_Choice3.asn").getFile();
        File schemaFile = new File(schemaFilename);
        final AsnSchema schema = AsnSchemaFileReader.read(schemaFile);

        String berFilename = getClass().getResource("/Human_Choice3_typeB.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                schema,
                topLevelType);
        debugPdus(pdus);

        DecodedAsnData pdu = pdus.get(0);
        String tag = "/Human/payload/iRIsContent/typeB/other";
        assertEquals(new BigInteger("10"), pdu.getDecodedObject(tag));
        tag = "/Human/payload/iRIsContent/typeB/stuff";
        assertEquals("U", pdu.getDecodedObject(tag));

        final ValidatorImpl validator = new ValidatorImpl();
        final ValidationResult validationresult = validator.validate(pdus.get(0));
        assertFalse(validationresult.hasFailures());
    }

    @Test
    public void testParse_EtsiV122() throws Exception
    {
        //fail("for quick runs");

        long start = System.currentTimeMillis();

        // TODO ASN-137, ASN-141 prevent us from being able to parse the EIFv122.asn schema
        String schemaFilename = getClass().getResource("/EIFv122.asn").getFile();
        File schemaFile = new File(schemaFilename);
        final AsnSchema schema = AsnSchemaFileReader.read(schemaFile);

        long mid = System.currentTimeMillis();

        String berFilename = getClass().getResource("/test.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "PS-PDU";

        String berFilename5 = getClass().getResource("/test5.ber").getFile();
        final File berFile5 = new File(berFilename5);

        long mid2 = System.currentTimeMillis();

        {

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);

            logger.debug("Results of /test.ber");

            debugPdus(pdus);

            assertEquals(3, pdus.size());
            assertEquals(0, pdus.get(0).getUnmappedTags().size());
            assertEquals(0, pdus.get(1).getUnmappedTags().size());
            assertEquals(0, pdus.get(2).getUnmappedTags().size());

            String tag = "/PS-PDU/pSHeader/communicationIdentifier/communicationIdentityNumber";

            BigInteger number = (BigInteger) pdus.get(0).getDecodedObject(tag);
            assertEquals(new BigInteger("622697890"), number);

            tag = "/PS-PDU/pSHeader/sequenceNumber";
            number = (BigInteger) pdus.get(0).getDecodedObject(tag);
            assertEquals(new BigInteger("0"), number);

            tag = "/PS-PDU/pSHeader/authorizationCountryCode";
            String str = (String) pdus.get(0).getDecodedObject(tag);
            assertEquals("AU", str);

            tag = "/PS-PDU/pSHeader/communicationIdentifier/deliveryCountryCode";
            str = (String) pdus.get(1).getDecodedObject(tag);
            assertEquals("AU", str);

            tag
                    = "/PS-PDU/pSHeader/communicationIdentifier/networkIdentifier/networkElementIdentifier";
            byte[] bytes = (byte[]) pdus.get(1).getDecodedObject(tag);
            str = new String(bytes, Charsets.UTF_8);
            assertEquals("BAEProd2", str);

            tag = "/PS-PDU/pSHeader/sequenceNumber";
            number = (BigInteger) pdus.get(2).getDecodedObject(tag);
            assertEquals(new BigInteger("8"), number);
        }

        {

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile5,
                    schema,
                    topLevelType);

            logger.debug("Results of /test5.ber");

            debugPdus(pdus);

            String tag = "/PS-PDU/pSHeader/communicationIdentifier/communicationIdentityNumber";

            BigInteger number = (BigInteger) pdus.get(0).getDecodedObject(tag);
            assertEquals(new BigInteger("622697903"), number);

            tag = "/PS-PDU/pSHeader/sequenceNumber";
            number = (BigInteger) pdus.get(0).getDecodedObject(tag);
            assertEquals(new BigInteger("0"), number);

            tag = "/PS-PDU/pSHeader/authorizationCountryCode";
            String str = (String) pdus.get(0).getDecodedObject(tag);
            assertEquals("AU", str);

            tag = "/PS-PDU/pSHeader/communicationIdentifier/deliveryCountryCode";
            str = (String) pdus.get(1).getDecodedObject(tag);
            assertEquals("AU", str);

            tag
                    = "/PS-PDU/pSHeader/communicationIdentifier/networkIdentifier/networkElementIdentifier";
            byte[] bytes = (byte[]) pdus.get(1).getDecodedObject(tag);
            str = new String(bytes, Charsets.UTF_8);
            assertEquals("BAEProd2", str);

            tag = "/PS-PDU/pSHeader/communicationIdentifier/cINExtension/iri-to-CC/cc[0]";
            bytes = (byte[]) pdus.get(1).getDecodedObject(tag);
            str = new String(bytes, Charsets.UTF_8);
            assertEquals("3030", str);

            assertEquals(15, pdus.size());
            for (int i = 0; i < 14; i++)
            {
                assertEquals(0, pdus.get(i).getUnmappedTags().size());
            }
        }

        long end = System.currentTimeMillis();

        logger.error("Duration {}ms, just schema {}ms, files {}ms",
                (end - mid2),
                (mid - start),
                (mid2 - mid));

        //        {
        //            String berFilename = getClass().getResource("/mock/SSI_CC.etsi").getFile();
        //            final File berFile = new File(berFilename);
        //            String topLevelType = "PS-PDU";
        //
        //            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.getDecodedTags(berFile,
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
        //                final ValidatorImpl validator = new ValidatorImpl();
        //                final ValidationResult validationresult = validator.validate(pdu);
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

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.getDecodedTags(berFile,
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
    public void testParse_Duplicates() throws Exception
    {
        try
        {
            AsnSchemaParser.parse(HUMAN_DUPLICATE_CHOICE);
            fail("Should have thrown parse exception");
        }
        catch (ParseException e)
        {
        }

        try
        {
            AsnSchemaParser.parse(HUMAN_DUPLICATE_SET);
            fail("Should have thrown parse exception");
        }
        catch (ParseException e)
        {
        }
        try
        {
            AsnSchemaParser.parse(HUMAN_DUPLICATE_SEQUENCE);
            fail("Should have thrown parse exception");
        }
        catch (ParseException e)
        {
        }

    }

    @Test
    public void testParse_HumanUsingTypeDefBroken() throws Exception
    {
        try
        {
            AsnSchemaParser.parse(HUMAN_BROKEN_MISSING_TYPEDEF);
            fail("Should have thrown parse exception");
        }
        catch (ParseException e)
        {
        }
        try
        {
            AsnSchemaParser.parse(HUMAN_BROKEN_MISSING_IMPORT);
            fail("Should have thrown parse exception");
        }
        catch (ParseException e)
        {
        }
        try
        {
            AsnSchemaParser.parse(HUMAN_BROKEN_MISSING_IMPORTED_TYPEDEF);
            fail("Should have thrown parse exception");
        }
        catch (ParseException e)
        {
        }
    }

    @Test
    public void testPerformance() throws Exception
    {
        long start = System.currentTimeMillis();

        // TODO ASN-137, ASN-141 prevent us from being able to parse the EIFv122.asn schema
        String schemaFilename = getClass().getResource("/EIFv122.asn").getFile();
        File schemaFile = new File(schemaFilename);
        final AsnSchema schema = AsnSchemaFileReader.read(schemaFile);

        String berFilename = getClass().getResource("/test.ber").getFile();
        final File berFile = new File(berFilename);
        String topLevelType = "PS-PDU";

        String berFilename5 = getClass().getResource("/test5.ber").getFile();
        final File berFile5 = new File(berFilename5);

        for (int z = 0; z < 5; ++z)
        {

            final ImmutableList<DecodedAsnData> pdus = AsnDecoder.decodeAsnData(berFile,
                    schema,
                    topLevelType);
            assertEquals(3, pdus.size());
            for (int i = 0; i < 3; i++)
            {
                assertEquals(0, pdus.get(i).getUnmappedTags().size());
            }

            final ImmutableList<DecodedAsnData> pdus2 = AsnDecoder.decodeAsnData(berFile5,
                    schema,
                    topLevelType);
            assertEquals(15, pdus2.size());
            for (int i = 0; i < 14; i++)
            {
                assertEquals(0, pdus2.get(i).getUnmappedTags().size());
            }
        }

        long end = System.currentTimeMillis();

        long duration = end - start;
        logger.error("Duration {}ms", duration);

        // On a Michael's dev machine this takes less than 1000ms with logging set to INFO
        // and less than 2000ms set to DEBUG, but the whole test take 30+ seconds set at debug!!
        int threshold = 3000;
        if (logger.isDebugEnabled())
        {
            threshold *= 2;
        }

        assertTrue(duration < threshold);

    }

    /**
     * Do a dump of all the data, both Mapped and Unmapped in all DecodedAsnData Defaults to using
     * {@link DecodedAsnData#getHexString} format.
     *
     * @param pdus
     *         the input DecodedAsnData objects
     */
    private void debugPdus(Iterable<DecodedAsnData> pdus)
    {
        debugPdus(pdus, true);
    }

    /**
     * @param pdus
     *         the input DecodedAsnData objects
     * @param printHexString
     *         determines whether to use {@link DecodedAsnData#getHexString} (if true) or {@link
     *         DecodedAsnData#getPrintableString} (if false)
     */
    private void debugPdus(Iterable<DecodedAsnData> pdus, boolean printHexString)
    {
        int i = 0;
        for (DecodedAsnData pdu : pdus)
        {
            logger.info("Parsing PDU[{}]", i);
            for (String t : pdu.getTags())
            {
                try
                {
                    logger.info("\t{} => {} as {}",
                            t,
                            (printHexString ? pdu.getHexString(t) : pdu.getPrintableString(t)),
                            pdu.getType(t).getBuiltinType());
                }
                catch (DecodeException e)
                {
                    logger.info("\t\tDecodeException {}", e.getMessage());
                }
            }

            for (String t : pdu.getUnmappedTags())
            {
                logger.info("\t?{} => {}", t, pdu.getHexString(t));
            }

            i++;
        }
    }
}