/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader.parser;

import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentTypeGenerated;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link AsnSchemaComponentTypeParser}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaComponentTypeParserTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testParse_NullOrEmpty() throws Exception
    {
        // test empty containingTypeName
        try
        {
            AsnSchemaComponentTypeParser.parse("", "TEST_COMPONENT_TYPES_TEXT");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }

        // test null containingTypeName
        try
        {
            AsnSchemaComponentTypeParser.parse(null, "TEST_COMPONENT_TYPES_TEXT");
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test empty componentTypesText
        try
        {
            AsnSchemaComponentTypeParser.parse("TEST_CONTAINING_TYPE_NAME", "");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }

        // test null componentTypesText
        try
        {
            AsnSchemaComponentTypeParser.parse("TEST_CONTAINING_TYPE_NAME", null);
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
            AsnSchemaComponentTypeParser.parse("TEST_CONTAINING_TYPE_NAME",
                    "!invalidType [0] type");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // test parse no type
        try
        {
            AsnSchemaComponentTypeParser.parse("TEST_CONTAINING_TYPE_NAME", "typeName [0]");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // test parse no type name
        try
        {
            AsnSchemaComponentTypeParser.parse("TEST_CONTAINING_TYPE_NAME", "[0] type");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // test parse invalid type
        try
        {
            AsnSchemaComponentTypeParser.parse("TEST_CONTAINING_TYPE_NAME", "typeName [0] type?");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // test invalid pseudo type
        try
        {
            AsnSchemaComponentTypeParser.parse("TEST_CONTAINING_TYPE_NAME",
                    "typeName [0] SEQUENCE {");
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
                "Document",
                "header [1] Header, body [2] Body, footer [3] Footer, dueDate [4] Date-Due, version [5] SEQUENCE { majorVersion [0] INTEGER, minorVersion [1] INTEGER }, description [6] SET { numberLines [0] INTEGER, summary [1] OCTET STRING } OPTIONAL");

        compareAsnSchemaComponentTypes(MockAsnSchemaComponentType.createMockedAsnSchemaComponentTypesForDocument(),
                actualComponents);

        // Body type
        actualComponents = AsnSchemaComponentTypeParser.parse("Body",
                "lastModified [0] ModificationMetadata, prefix [1] Section-Note OPTIONAL, content [2] Section-Main, suffix [3] Section-Note OPTIONAL");

        compareAsnSchemaComponentTypes(MockAsnSchemaComponentType.createMockedAsnSchemaComponentTypesForBody(),
                actualComponents);

        // Section-Main type
        actualComponents = AsnSchemaComponentTypeParser.parse("Section-Main",
                "text [1] OCTET STRING OPTIONAL, paragraphs [2] SEQUENCE OF Paragraph, sections [3] SET OF SET { number [1] INTEGER, text [2] OCTET STRING }");

        compareAsnSchemaComponentTypes(MockAsnSchemaComponentType.createMockedAsnSchemaComponentTypesForSectionMain(),
                actualComponents);
    }

    @Test
    public void testParse_PeopleProtocol() throws Exception
    {
        // Person
        ImmutableList<AsnSchemaComponentType> actualComponents = AsnSchemaComponentTypeParser.parse(
                "Person",
                "firstName [1] OCTET STRING, lastName [2] OCTET STRING, title [3] ENUMERATED { mr, mrs, ms, dr, rev } OPTIONAL, gender Gender OPTIONAL, maritalStatus CHOICE { Married [0], Single [1] }");

        compareAsnSchemaComponentTypes(MockAsnSchemaComponentType.createMockedAsnSchemaComponentTypesForPerson(),
                actualComponents);
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

            if (actualComponentType instanceof AsnSchemaComponentTypeGenerated)
            {
                assertEquals(((AsnSchemaComponentTypeGenerated) expectedComponentType).getTypeDefinitionText(),
                        ((AsnSchemaComponentTypeGenerated) actualComponentType).getTypeDefinitionText());
            }
        }
    }
}
