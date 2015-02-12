/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.reader.parser;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.fail;

/**
 * Unit tests for {@link AsnSchemaModuleParser}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaModuleParserTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** an invalid schema (missing a BEGIN tag) **/
    private static final Iterable<String> TEST_MODULE_NO_BEGIN =
        Lists.newArrayList(
                "People-Protocol",
                "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }",
                "DEFINITIONS",
                "AUTOMATIC TAGS ::=",
                "People ::= SET OF Person",
                "Person ::= SEQUENCE",
                "{",
                "firstName [1] OCTET STRING,",
                "lastName [2] OCTET STRING,",
                "title [3] ENUMERATED",
                "{ mr, mrs, ms, dr, rev } OPTIONAL,",
                "gender OPTIONAL",
                "}",
                "Gender ::= ENUMERATED",
                "{ male(0),",
                "female(1)",
                "}",
                "END");

    /** an invalid IMPORTS statement **/
    private static final Iterable<String> TEST_MODULE_INVALID_IMPORTS =
            Lists.newArrayList();

    /** an invalid EXPORTS statement **/
    private static final Iterable<String> TEST_MODULE_INVALID_EXPORTS =
            Lists.newArrayList();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testParse_NullOrEmpty() throws Exception
    {
        // test empty file
        try
        {
            AsnSchemaModuleParser.parse(Lists.newArrayList());
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // test null argument
        try
        {
            AsnSchemaModuleParser.parse(null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
    }

    @Test
    public void testParse_Exceptions() throws Exception
    {
        try {
            // test schema with no BEGIN keyword
            AsnSchemaModuleParser.parse(TEST_MODULE_NO_BEGIN);
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }
    }

    /*@Test
    public void testParseDocumentPDU() throws Exception
    {
    }*/

    /*@Test
    public void testParsePeopleProtocol() throws Exception {
    }*/
}
