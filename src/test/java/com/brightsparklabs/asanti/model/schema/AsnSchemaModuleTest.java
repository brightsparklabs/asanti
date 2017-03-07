/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaModule;
import com.brightsparklabs.asanti.model.schema.AsnSchemaModule.Builder;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link AsnSchemaModule}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaModuleTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static AsnSchemaModule instance;

    // -------------------------------------------------------------------------
    // SETUP/TEAR-DOWN
    // -------------------------------------------------------------------------

    @BeforeClass
    public static void setUpBeforeClass() throws ParseException
    {
        instance = MockAsnSchemaModule.createMockedAsnSchemaModuleForDocumentPdu().build();
    }

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testBuilder() throws Exception
    {

        // test standard build works
        final Builder builder = AsnSchemaModule.builder();
        builder.setName("TEST").build();

        // test null name
        try
        {
            builder.setName(null).build();
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test empty name
        try
        {
            builder.setName("").build();
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            builder.setName(" ").build();
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetName() throws Exception
    {
        assertEquals("Document-PDU", instance.getName());
    }

    @Test
    public void testGetType() throws Exception
    {
        // test known
        assertEquals("Document", instance.getType("Document").getName());
        assertEquals("Header", instance.getType("Header").getName());
        assertEquals("Body", instance.getType("Body").getName());
        assertEquals("Footer", instance.getType("Footer").getName());
        assertEquals("PublishedMetadata", instance.getType("PublishedMetadata").getName());
        assertEquals("ModificationMetadata", instance.getType("ModificationMetadata").getName());
        assertEquals("Section-Note", instance.getType("Section-Note").getName());
        assertEquals("Section-Main", instance.getType("Section-Main").getName());
        assertEquals("Section-Note", instance.getType("Section-Note").getName());
        assertEquals("Paragraph", instance.getType("Paragraph").getName());
        // test imports
        assertEquals(AsnSchemaTypeDefinition.NULL, instance.getType("People"));
        assertEquals(AsnSchemaTypeDefinition.NULL, instance.getType("Person"));
        // test unknown
        assertEquals(AsnSchemaTypeDefinition.NULL, instance.getType("NON_EXISTING_TYPE"));
        // test blank
        assertEquals(AsnSchemaTypeDefinition.NULL, instance.getType(""));
        assertEquals(AsnSchemaTypeDefinition.NULL, instance.getType(" "));
        // test null
        assertEquals(AsnSchemaTypeDefinition.NULL, instance.getType(null));
    }

    @Test
    public void testGetImportedModuleFor() throws Exception
    {
        // test imports
        assertEquals("People-Protocol", instance.getImportedModuleFor("People"));
        assertEquals("People-Protocol", instance.getImportedModuleFor("Person"));
        // test non-imported
        assertEquals("", instance.getImportedModuleFor("Document"));
        assertEquals("", instance.getImportedModuleFor("Header"));
        assertEquals("", instance.getImportedModuleFor("Body"));
        assertEquals("", instance.getImportedModuleFor("Footer"));
        assertEquals("", instance.getImportedModuleFor("PublishedMetadata"));
        assertEquals("", instance.getImportedModuleFor("ModificationMetadata"));
        assertEquals("", instance.getImportedModuleFor("Section-Note"));
        assertEquals("", instance.getImportedModuleFor("Section-Main"));
        assertEquals("", instance.getImportedModuleFor("Section-Note"));
        assertEquals("", instance.getImportedModuleFor("Paragraph"));
        // test unknown
        assertEquals("", instance.getImportedModuleFor("NON_EXISTING_TYPE"));
        // test blank
        assertEquals("", instance.getImportedModuleFor(""));
        assertEquals("", instance.getImportedModuleFor(" "));
        // test null
        assertEquals("", instance.getImportedModuleFor(null));
    }
}
