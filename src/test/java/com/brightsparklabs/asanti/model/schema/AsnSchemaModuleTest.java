/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.brightsparklabs.asanti.mocks.MockAsnSchemaModule;
import com.brightsparklabs.asanti.model.schema.AsnSchemaModule.Builder;
import com.google.common.collect.ImmutableMap;

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

    /** default instance to test */
    private static AsnSchemaModule instance;

    /** all schema modules referenced in the tests */
    private static ImmutableMap<String, AsnSchemaModule> allSchemaModules;

    // -------------------------------------------------------------------------
    // SETUP/TEAR-DOWN
    // -------------------------------------------------------------------------

    @BeforeClass
    public static void setUpBeforeClass()
    {
        allSchemaModules = MockAsnSchemaModule.createMockedAsnSchemaModules();
        instance = allSchemaModules.get("Document-PDU");
    }

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testBuilder() throws Exception
    {
        // test standard build works
        final Builder builder = AsnSchemaModule.builder();
        builder.setName("TEST")
                .build();

        // test null name
        try
        {
            builder.setName(null)
                    .build();
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test empty name
        try
        {
            builder.setName("")
                    .build();
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            builder.setName(" ")
                    .build();
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
    public void testGetDecodedTag() throws Exception
    {
        assertEquals("/Document/header/published", instance.getDecodedTag("1/0", "Document", allSchemaModules)
                .getDecodedData());
        assertEquals("/Document/body/lastModified/date", instance.getDecodedTag("2/0/0", "Document", allSchemaModules)
                .getDecodedData());
        assertEquals("/Document/body/lastModified/modifiedBy/firstName",
                instance.getDecodedTag("2/0/1/1", "Document", allSchemaModules)
                        .getDecodedData());
        assertEquals("/Document/body/lastModified/modifiedBy/lastName",
                instance.getDecodedTag("2/0/1/2", "Document", allSchemaModules)
                        .getDecodedData());
        assertEquals("/Document/body/lastModified/modifiedBy/title",
                instance.getDecodedTag("2/0/1/3", "Document", allSchemaModules)
                        .getDecodedData());
        assertEquals("/Document/body/prefix/text", instance.getDecodedTag("2/1/1", "Document", allSchemaModules)
                .getDecodedData());
        assertEquals("/Document/body/content/text", instance.getDecodedTag("2/2/1", "Document", allSchemaModules)
                .getDecodedData());
        assertEquals("/Document/body/content/paragraphs/title",
                instance.getDecodedTag("2/2/2/1", "Document", allSchemaModules)
                        .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[0]/title",
                instance.getDecodedTag("2/2/2[0]/1", "Document", allSchemaModules)
                        .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[1]/title",
                instance.getDecodedTag("2/2/2[1]/1", "Document", allSchemaModules)
                        .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[0]/contributor/firstName",
                instance.getDecodedTag("2/2/2[0]/2/1", "Document", allSchemaModules)
                        .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[0]/contributor/lastName",
                instance.getDecodedTag("2/2/2[0]/2/2", "Document", allSchemaModules)
                        .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[0]/contributor/title",
                instance.getDecodedTag("2/2/2[0]/2/3", "Document", allSchemaModules)
                        .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[0]/points",
                instance.getDecodedTag("2/2/2[0]/3", "Document", allSchemaModules)
                        .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[0]/points[0]",
                instance.getDecodedTag("2/2/2[0]/3[0]", "Document", allSchemaModules)
                        .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[99]/title",
                instance.getDecodedTag("2/2/2[99]/1", "Document", allSchemaModules)
                        .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[99]/contributor/firstName",
                instance.getDecodedTag("2/2/2[99]/2/1", "Document", allSchemaModules)
                        .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[99]/contributor/lastName",
                instance.getDecodedTag("2/2/2[99]/2/2", "Document", allSchemaModules)
                        .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[99]/contributor/title",
                instance.getDecodedTag("2/2/2[99]/2/3", "Document", allSchemaModules)
                        .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[99]/points",
                instance.getDecodedTag("2/2/2[99]/3", "Document", allSchemaModules)
                        .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[99]/points[99]",
                instance.getDecodedTag("2/2/2[99]/3[99]", "Document", allSchemaModules)
                        .getDecodedData());
        assertEquals("/Document/body/suffix/text", instance.getDecodedTag("2/3/1", "Document", allSchemaModules)
                .getDecodedData());
    }
}
