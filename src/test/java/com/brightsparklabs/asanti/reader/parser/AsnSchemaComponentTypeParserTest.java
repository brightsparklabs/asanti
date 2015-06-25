/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader.parser;

import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link AsnSchemaComponentTypeParser}
 *
 * @author brightSPARK Labs
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AsnSchemaTypeParser.class)

public class AsnSchemaComponentTypeParserTest
{

    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------
    /** an argument capture helper for Constraints */
    private ArgumentCaptor<String> typeArgument;

    // -------------------------------------------------------------------------
    // SETUP/TEAR-DOWN
    // -------------------------------------------------------------------------
    @Before
    public void setUpBeforeTest() throws Exception
    {
        // mock AsnSchemaTypeParser.parse static method
        PowerMockito.mockStatic(AsnSchemaTypeParser.class);
        // we want to capture the typeArgument that gets passed to the AsnSchemaTypeParser
        typeArgument = ArgumentCaptor.forClass(String.class);
        when(AsnSchemaTypeParser.parse(typeArgument.capture())).thenReturn(AsnSchemaType.NULL);
    }

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testParse_NullOrEmpty() throws Exception
    {
        // test empty componentTypesText
        try
        {
            AsnSchemaComponentTypeParser.parse("");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }

        // test null componentTypesText
        try
        {
            AsnSchemaComponentTypeParser.parse(null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
    }

    @Test
    public void testParse_Exceptions() throws Exception
    {
        // test parse invalid type name
        try
        {
            AsnSchemaComponentTypeParser.parse("!invalidType [0] type");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // test parse no type
        try
        {
            AsnSchemaComponentTypeParser.parse("typeName [0]");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // test parse no type name
        try
        {
            AsnSchemaComponentTypeParser.parse("[0] type");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // test parse invalid type
        try
        {
            AsnSchemaComponentTypeParser.parse("typeName [0] type?");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // test invalid pseudo type
        try
        {
            AsnSchemaComponentTypeParser.parse("typeName [0] SEQUENCE {");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }
    }

    @Test
    public void testParse_DocumentPdu() throws Exception
    {
        // Document type
        ImmutableList<AsnSchemaComponentType> actualComponents = AsnSchemaComponentTypeParser.parse(
                "header [1] Header, body [2] Body, footer [3] Footer, dueDate [4] Date-Due, version [5] SEQUENCE { majorVersion [0] INTEGER, minorVersion [1] INTEGER }, description [6] SET { numberLines [0] INTEGER, summary [1] OCTET STRING } OPTIONAL");

        compareAsnSchemaComponentTypes(MockAsnSchemaComponentType.createMockedAsnSchemaComponentTypesForDocument(),
                actualComponents);

        // check that the AsnSchemaTypeParser got called the right number of times with the right inputs
        List<String> callArguments = typeArgument.getAllValues();
        assertEquals(6, callArguments.size());
        assertEquals("Header", callArguments.get(0));
        assertEquals("Body", callArguments.get(1));
        assertEquals("Footer", callArguments.get(2));
        assertEquals("Date-Due", callArguments.get(3));
        assertEquals("SEQUENCE { majorVersion [0] INTEGER, minorVersion [1] INTEGER }", callArguments.get(4));
        assertEquals("SET { numberLines [0] INTEGER, summary [1] OCTET STRING }", callArguments.get(5));


        // Body type
        actualComponents = AsnSchemaComponentTypeParser.parse(
                "lastModified [0] ModificationMetadata, prefix [1] Section-Note OPTIONAL, content [2] Section-Main, suffix [3] Section-Note OPTIONAL");
        assertEquals(6+4, callArguments.size());
        assertEquals("ModificationMetadata", callArguments.get(6));
        assertEquals("Section-Note", callArguments.get(7));
        assertEquals("Section-Main", callArguments.get(8));
        assertEquals("Section-Note", callArguments.get(9));


        compareAsnSchemaComponentTypes(MockAsnSchemaComponentType.createMockedAsnSchemaComponentTypesForBody(),
                actualComponents);

        // Section-Main type
        actualComponents = AsnSchemaComponentTypeParser.parse(
                "text [1] OCTET STRING OPTIONAL, paragraphs [2] SEQUENCE OF Paragraph, sections [3] SET OF SET { number [1] INTEGER, text [2] OCTET STRING }");

        compareAsnSchemaComponentTypes(MockAsnSchemaComponentType.createMockedAsnSchemaComponentTypesForSectionMain(),
                actualComponents);

        // check that the AsnSchemaTypeParser got called the right number of times with the right inputs
        assertEquals(10+3, callArguments.size());
        assertEquals("OCTET STRING", callArguments.get(10));
        assertEquals("SEQUENCE OF Paragraph", callArguments.get(11));
        assertEquals("SET OF SET { number [1] INTEGER, text [2] OCTET STRING }", callArguments.get(12));

    }

    @Test
    public void testParse_PeopleProtocol() throws Exception
    {
/* TODO ASN-138
        // Person
        ImmutableList<AsnSchemaComponentType> actualComponents = AsnSchemaComponentTypeParser.parse(
                "firstName [1] OCTET STRING, lastName [2] OCTET STRING, title [3] ENUMERATED { mr, mrs, ms, dr, rev } OPTIONAL, gender Gender OPTIONAL, maritalStatus CHOICE { Married [0], Single [1] }");

        compareAsnSchemaComponentTypes(MockAsnSchemaComponentType.createMockedAsnSchemaComponentTypesForPerson(),
                actualComponents);

        // check that the AsnSchemaTypeParser got called the right number of times with the right inputs
        List<String> callArguments = typeArgument.getAllValues();
        assertEquals(5, callArguments.size());
        assertEquals("OCTET STRING", callArguments.get(0));
        assertEquals("OCTET STRING", callArguments.get(1));
        assertEquals("ENUMERATED { mr, mrs, ms, dr, rev }", callArguments.get(2));
        assertEquals("Gender", callArguments.get(3));
        assertEquals("CHOICE { Married [0], Single [1] }", callArguments.get(4));
*/
    }

    /**
     * Compares two lists of {@link AsnSchemaComponentType} for equality
     *
     * @param expected
     *         the expected list of components types
     * @param actual
     *         the actual list of components types to compare against the expected
     */
    private void compareAsnSchemaComponentTypes(List<AsnSchemaComponentType> expected,
            List<AsnSchemaComponentType> actual)
    {
        assertEquals(expected.size(), actual.size());

        for (int i = 0; i < actual.size(); i++)
        {
            AsnSchemaComponentType actualComponentType = actual.get(i);
            AsnSchemaComponentType expectedComponentType = expected.get(i);

            assertEquals(expectedComponentType.getTag(), actualComponentType.getTag());
            assertEquals(expectedComponentType.getTagName(), actualComponentType.getTagName());
            assertEquals(expectedComponentType.getTypeName(), actualComponentType.getTypeName());
            assertEquals(expectedComponentType.isOptional(), actualComponentType.isOptional());

            // Not comparing the AsnSchemaTypes deliberately, as these are not part of the scope.
        }
    }
}
