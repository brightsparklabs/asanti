/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.reader.parser;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link AsnSchemaImportsParser}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaImportsParserTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** an invalid imports statement (missing object identifier) **/
    private static final String TEST_INVALID_IMPORTS_STATEMENT =
            "Person FROM People-Protocol ";

    /** the imports statement from the Document-PDU module **/
    private static final String TEST_IMPORTS_STATEMENT =
            "Person FROM People-Protocol { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) } ; ";

    /** an imports statement with multiple types **/
    private static final String TEST_IMPORTS_STATEMENT_MULTIPLE_TYPES =
            "Person, Gender FROM People-Protocol { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) } ; ";

    /** multiple import statements **/
    private static final String TEST_IMPORTS_MULTIPLE =
            "Person FROM People-Protocol " +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\n" +
            "Vehicle FROM Vehicle-Protocol " +
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) vehicle(2) } ; ";

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testParse_NullOrEmpty() throws Exception
    {
        // test imports statement
        ImmutableMap<String, String> importsMap = AsnSchemaImportsParser.parse("");
        assertEquals(0, importsMap.size());

        // test null argument
        importsMap = AsnSchemaImportsParser.parse(null);
        assertEquals(0, importsMap.size());
    }

    @Test
    public void testParse_Exceptions() throws Exception
    {
        // test invalid Imports statement
        try
        {
            AsnSchemaImportsParser.parse(TEST_INVALID_IMPORTS_STATEMENT);
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }
    }

    @Test
    public void testParse() throws Exception
    {
        final ImmutableMap<String, String> expectedMap = ImmutableMap.<String, String>builder()
                .put("Person", "People-Protocol")
                .build();

        final ImmutableMap<String, String> actualMap = AsnSchemaImportsParser.parse(TEST_IMPORTS_STATEMENT);
        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testParse_MultipleTypes() throws Exception
    {
        final ImmutableMap<String, String> expectedMap = ImmutableMap.<String, String>builder()
                .put("Person", "People-Protocol")
                .put("Gender", "People-Protocol")
                .build();

        final ImmutableMap<String, String> actualMap =
                AsnSchemaImportsParser.parse(TEST_IMPORTS_STATEMENT_MULTIPLE_TYPES);
        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testParse_MultipleImports() throws Exception
    {
        final ImmutableMap<String, String> expectedMap = ImmutableMap.<String, String>builder()
                .put("Person", "People-Protocol")
                .put("Vehicle", "Vehicle-Protocol")
                .build();

        final ImmutableMap<String, String> actualMap =
                AsnSchemaImportsParser.parse(TEST_IMPORTS_MULTIPLE);
        assertEquals(expectedMap, actualMap);
    }
}
