/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.reader.parser;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.AsnSchemaModule;

/**
 * Unit tests for {@link AsnSchemaParser}
 *
 * @author brightSPARK Labs
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AsnSchemaModuleParser.class)
public class AsnSchemaParserTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    private static final String SCHEMA_NO_END =
        "People-Protocol\n" +
        "    { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n" +
        "\n" +
        "DEFINITIONS\n" +
        "    AUTOMATIC TAGS ::=\n" +
        "\n" +
        "BEGIN\n" +
        "    /*Person ::= SEQUENCE\n" +
        "    {\n" +
        "        firstName [1] OCTET STRING,\n" +
        "        lastName  [2] OCTET STRING,\n" +
        "        title     [3] ENUMERATED\n" +
        "            { mr, mrs, ms, dr, rev } OPTIONAL,\n" +
        "        gender OPTIONAL\n" +
        "    }*/\n" +
        "\n" +
        "    Gender ::= ENUMERATED\n" +
        "    {\n" +
        "        male(0),\n" +
        "        female(1)\n" +
        "    }\n";

    private static final String SCHEMA_VALID_BLOCK_COMMENTS =
        "/*Document-PDU\n" +
        "    { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) document(1) }\n" +
        "\n" +
        "DEFINITIONS\n" +
        "    AUTOMATIC TAGS ::=\n" +
        "\n" +
        "IMPORTS\n" +
        "  Gender\n" +
        "    FROM People-Protocol\n" +
        "    { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n" +
        "\n" +
        "BEGIN\n" +
        "\n" +
        "    Document ::= SEQUENCE\n" +
        "    {\n" +
        "        header [1] Header,\n" +
        "        body   [2] Body,\n" +
        "        footer [3] Footer\n" +
        "    }\n" +
        "END\n" +
        "*/\n" +
        "People-Protocol\n" +
        "    { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n" +
        "\n" +
        "DEFINITIONS\n" +
        "    AUTOMATIC TAGS ::=\n" +
        "\n" +
        "BEGIN\n" +
        "    /*Person ::= SEQUENCE\n" +
        "    {\n" +
        "        firstName [1] OCTET STRING,\n" +
        "        lastName  [2] OCTET STRING,\n" +
        "        title     [3] ENUMERATED\n" +
        "            { mr, mrs, ms, dr, rev } OPTIONAL,\n" +
        "        gender OPTIONAL\n" +
        "    }*/\n" +
        "\n" +
        "    Gender ::= ENUMERATED\n" +
        "    {\n" +
        "        male(0),\n" +
        "        female(1)\n" +
        "    }\n" +
        "END";

    private static final String SCHEMA_VALID_BLOCK_COMMENTS_EXPECTED_PARSE_INPUT =
        "People-Protocol\n" +
        "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n" +
        "DEFINITIONS\n" +
        "AUTOMATIC TAGS ::=\n" +
        "BEGIN\n" +
        "Gender ::= ENUMERATED\n" +
        "{\n" +
        "male(0),\n" +
        "female(1)\n" +
        "}\n" +
        "END\n";

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testParse_NullOrEmpty() throws Exception
    {
        // test empty file
        try
        {
            AsnSchemaParser.parse("");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // test null argument
        try
        {
            AsnSchemaParser.parse(null);
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }
    }

    @Test
    public void testParse_Exceptions() throws Exception
    {
        // test schema with no End keyword
        try
        {
            AsnSchemaParser.parse(SCHEMA_NO_END);
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }
    }

    @Test
    public void testParse() throws Exception
    {
        // prepare objects for mocking of AsnSchemaModuleParser.parse static method
        // set up mock AsnSchemaModule to return
        final AsnSchemaModule.Builder moduleBuilder = AsnSchemaModule.builder();
        moduleBuilder.setName("People-Protocol");
        AsnSchemaModule mockAsnSchemaModule = moduleBuilder.build();

        // prepare expected input to the AsnSchemaModuleParser.parse static method for
        // valid block comments
        List<String> expectedParseInput =
                Arrays.asList(SCHEMA_VALID_BLOCK_COMMENTS_EXPECTED_PARSE_INPUT.split("\n"));

        // mock AsnSchemaModuleParser.parse static method to only return mock object
        // if argument matches expected input
        PowerMockito.mockStatic(AsnSchemaModuleParser.class);
        when(AsnSchemaModuleParser
                .parse(expectedParseInput))
                .thenReturn(mockAsnSchemaModule);

        // test schema with block comments
        AsnSchema actualSchema = AsnSchemaParser.parse(SCHEMA_VALID_BLOCK_COMMENTS);
        assertNotNull(actualSchema);
    }
}