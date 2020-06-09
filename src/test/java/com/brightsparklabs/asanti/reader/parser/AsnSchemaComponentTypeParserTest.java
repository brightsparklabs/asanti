/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.reader.parser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.AsnModuleTaggingMode;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.schema.AsnBuiltinType;
import com.google.common.collect.ImmutableList;
import java.text.ParseException;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Unit tests for {@link AsnSchemaComponentTypeParser}
 *
 * @author brightSPARK Labs
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AsnSchemaTypeParser.class)
public class AsnSchemaComponentTypeParserTest {

    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // SETUP/TEAR-DOWN
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testParseEmpty() throws Exception {
        AsnSchemaComponentTypeParser.parse("", AsnModuleTaggingMode.DEFAULT);
    }

    @Test(expected = NullPointerException.class)
    public void testParseNull() throws Exception {
        AsnSchemaComponentTypeParser.parse(null, AsnModuleTaggingMode.DEFAULT);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidName() throws Exception {
        AsnSchemaComponentTypeParser.parse("!invalidType [0] type", AsnModuleTaggingMode.DEFAULT);
    }

    @Test(expected = ParseException.class)
    public void testParseNoType() throws Exception {
        AsnSchemaComponentTypeParser.parse("typeName [0]", AsnModuleTaggingMode.DEFAULT);
    }

    @Test(expected = ParseException.class)
    public void testParseNoName() throws Exception {
        AsnSchemaComponentTypeParser.parse("[0] type", AsnModuleTaggingMode.DEFAULT);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidType() throws Exception {
        AsnSchemaComponentTypeParser.parse("typeName [0] type?", AsnModuleTaggingMode.DEFAULT);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidPseudoType() throws Exception {
        AsnSchemaComponentTypeParser.parse("typeName [0] SEQUENCE {", AsnModuleTaggingMode.DEFAULT);
    }

    @Test
    public void testParse_DocumentPdu() throws Exception {
        // mock AsnSchemaTypeParser.parse static method
        PowerMockito.mockStatic(AsnSchemaTypeParser.class);
        // we want to capture the typeArgument that gets passed to the AsnSchemaTypeParser
        ArgumentCaptor<String> typeArgument = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<AsnModuleTaggingMode> taggingMode =
                ArgumentCaptor.forClass(AsnModuleTaggingMode.class);
        when(AsnSchemaTypeParser.parse(typeArgument.capture(), taggingMode.capture()))
                .thenReturn(AsnSchemaType.NULL);

        // Document type
        ImmutableList<AsnSchemaComponentType> actualComponents =
                AsnSchemaComponentTypeParser.parse(
                        "header [1] Header, body [2] Body, footer [3] Footer, dueDate [4] Date-Due, version [5] SEQUENCE { majorVersion [0] INTEGER, minorVersion [1] INTEGER }, description [6] SET { numberLines [0] INTEGER, summary [1] OCTET STRING } OPTIONAL",
                        AsnModuleTaggingMode.DEFAULT);

        compareAsnSchemaComponentTypes(
                MockAsnSchemaComponentType.createMockedAsnSchemaComponentTypesForDocument(),
                actualComponents);

        // check that the AsnSchemaTypeParser got called the right number of times with the right
        // inputs
        final List<String> initialCallArguments = typeArgument.getAllValues();
        assertEquals(6, initialCallArguments.size());
        assertEquals("Header", initialCallArguments.get(0));
        assertEquals("Body", initialCallArguments.get(1));
        assertEquals("Footer", initialCallArguments.get(2));
        assertEquals("Date-Due", initialCallArguments.get(3));
        assertEquals(
                "SEQUENCE { majorVersion [0] INTEGER, minorVersion [1] INTEGER }",
                initialCallArguments.get(4));
        assertEquals(
                "SET { numberLines [0] INTEGER, summary [1] OCTET STRING }",
                initialCallArguments.get(5));

        // Body type
        actualComponents =
                AsnSchemaComponentTypeParser.parse(
                        "lastModified [0] ModificationMetadata, prefix [1] Section-Note OPTIONAL, content [2] Section-Main, suffix [3] Section-Note OPTIONAL",
                        AsnModuleTaggingMode.DEFAULT);
        final List<String> callArgumentsWithBody = typeArgument.getAllValues();

        assertEquals(6 + 4, callArgumentsWithBody.size());
        assertEquals("ModificationMetadata", callArgumentsWithBody.get(6));
        assertEquals("Section-Note", callArgumentsWithBody.get(7));
        assertEquals("Section-Main", callArgumentsWithBody.get(8));
        assertEquals("Section-Note", callArgumentsWithBody.get(9));

        compareAsnSchemaComponentTypes(
                MockAsnSchemaComponentType.createMockedAsnSchemaComponentTypesForBody(),
                actualComponents);

        // Section-Main type
        actualComponents =
                AsnSchemaComponentTypeParser.parse(
                        "text [1] OCTET STRING OPTIONAL, paragraphs [2] SEQUENCE OF Paragraph, sections [3] SET OF SET { number [1] INTEGER, text [2] OCTET STRING }",
                        AsnModuleTaggingMode.DEFAULT);

        compareAsnSchemaComponentTypes(
                MockAsnSchemaComponentType.createMockedAsnSchemaComponentTypesForSectionMain(),
                actualComponents);
        final List<String> callArgumentsWithBodyAndSectionMain = typeArgument.getAllValues();

        // check that the AsnSchemaTypeParser got called the right number of times with the right
        // inputs
        assertEquals(10 + 3, callArgumentsWithBodyAndSectionMain.size());
        assertEquals("OCTET STRING", callArgumentsWithBodyAndSectionMain.get(10));
        assertEquals("SEQUENCE OF Paragraph", callArgumentsWithBodyAndSectionMain.get(11));
        assertEquals(
                "SET OF SET { number [1] INTEGER, text [2] OCTET STRING }",
                callArgumentsWithBodyAndSectionMain.get(12));
    }

    @Test
    public void testDefault() throws Exception {
        final ImmutableList<AsnSchemaComponentType> parse =
                AsnSchemaComponentTypeParser.parse(
                        "text [1] OCTET STRING OPTIONAL, value [2] INTEGER DEFAULT 25, other BOOLEAN",
                        AsnModuleTaggingMode.DEFAULT);

        assertEquals(3, parse.size());
        final AsnSchemaComponentType optString = parse.get(0);
        assertEquals("text", optString.getName());
        assertEquals(AsnBuiltinType.OctetString, optString.getType().getBuiltinType());
        assertEquals("1", optString.getTag());
        assertTrue(optString.isOptional());
        // final List<String> allValues = typeArgument.getAllValues();

        final AsnSchemaComponentType defaultInt = parse.get(1);
        assertEquals("value", defaultInt.getName());
        assertEquals(AsnBuiltinType.Integer, defaultInt.getType().getBuiltinType());
        assertEquals("2", defaultInt.getTag());
        assertTrue(defaultInt.isOptional());

        final AsnSchemaComponentType other = parse.get(2);
        assertEquals("other", other.getName());
        assertEquals(AsnBuiltinType.Boolean, other.getType().getBuiltinType());
        assertEquals("", other.getTag());
        assertFalse(other.isOptional());
    }

    @Test
    public void testParse_PeopleProtocol() throws Exception {
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
     * @param expected the expected list of components types
     * @param actual the actual list of components types to compare against the expected
     */
    private void compareAsnSchemaComponentTypes(
            List<AsnSchemaComponentType> expected, List<AsnSchemaComponentType> actual) {
        assertEquals(expected.size(), actual.size());

        for (int i = 0; i < actual.size(); i++) {
            AsnSchemaComponentType actualComponentType = actual.get(i);
            AsnSchemaComponentType expectedComponentType = expected.get(i);

            assertEquals(expectedComponentType.getTag(), actualComponentType.getTag());
            assertEquals(expectedComponentType.getName(), actualComponentType.getName());
            assertEquals(expectedComponentType.isOptional(), actualComponentType.isOptional());

            // Not comparing the AsnSchemaTypes deliberately, as these are not part of the scope.
        }
    }
}
