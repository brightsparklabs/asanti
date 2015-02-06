/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.brightsparklabs.asanti.mocks.MockAsnSchemaModule;
import com.google.common.collect.ImmutableMap;

public class AsnSchemaDefaultTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** default instance to test */
    private static AsnSchemaDefault instance;

    /** all modules in the default instance */
    private static ImmutableMap<String, AsnSchemaModule> modules;

    // -------------------------------------------------------------------------
    // SETUP/TEAR-DOWN
    // -------------------------------------------------------------------------

    @BeforeClass
    public static void setUpBeforeClass()
    {
        modules = MockAsnSchemaModule.createMockedAsnSchemaModules();
        instance = new AsnSchemaDefault("Document-PDU", modules);
    }

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaDefault() throws Exception
    {
        // test null
        try
        {
            new AsnSchemaDefault(null, modules);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new AsnSchemaDefault("Document-PDU", null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test empty
        try
        {
            new AsnSchemaDefault("", modules);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new AsnSchemaDefault(" ", modules);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new AsnSchemaDefault("Document-PDU", ImmutableMap.<String, AsnSchemaModule>of());
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }

        // test invalid primary module
        try
        {
            new AsnSchemaDefault("UNKNOWN", modules);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetDecodedTag() throws Exception
    {
        // test known
        assertEquals("/Document/header/published", instance.getDecodedTag("1/0", "Document")
                .getDecodedData());
        assertEquals("/Document/body/lastModified/date", instance.getDecodedTag("2/0/0", "Document")
                .getDecodedData());
        assertEquals("/Document/body/lastModified/modifiedBy/firstName", instance.getDecodedTag("2/0/1/1", "Document")
                .getDecodedData());
        assertEquals("/Document/body/lastModified/modifiedBy/lastName", instance.getDecodedTag("2/0/1/2", "Document")
                .getDecodedData());
        assertEquals("/Document/body/lastModified/modifiedBy/title", instance.getDecodedTag("2/0/1/3", "Document")
                .getDecodedData());
        assertEquals("/Document/body/prefix/text", instance.getDecodedTag("2/1/1", "Document")
                .getDecodedData());
        assertEquals("/Document/body/content/text", instance.getDecodedTag("2/2/1", "Document")
                .getDecodedData());
        assertEquals("/Document/body/content/paragraphs/title", instance.getDecodedTag("2/2/2/1", "Document")
                .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[0]/title", instance.getDecodedTag("2/2/2[0]/1", "Document")
                .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[1]/title", instance.getDecodedTag("2/2/2[1]/1", "Document")
                .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[0]/contributor/firstName",
                instance.getDecodedTag("2/2/2[0]/2/1", "Document")
                        .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[0]/contributor/lastName",
                instance.getDecodedTag("2/2/2[0]/2/2", "Document")
                        .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[0]/contributor/title",
                instance.getDecodedTag("2/2/2[0]/2/3", "Document")
                        .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[0]/points", instance.getDecodedTag("2/2/2[0]/3", "Document")
                .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[0]/points[0]",
                instance.getDecodedTag("2/2/2[0]/3[0]", "Document")
                        .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[99]/title", instance.getDecodedTag("2/2/2[99]/1", "Document")
                .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[99]/contributor/firstName",
                instance.getDecodedTag("2/2/2[99]/2/1", "Document")
                        .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[99]/contributor/lastName",
                instance.getDecodedTag("2/2/2[99]/2/2", "Document")
                        .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[99]/contributor/title",
                instance.getDecodedTag("2/2/2[99]/2/3", "Document")
                        .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[99]/points", instance.getDecodedTag("2/2/2[99]/3", "Document")
                .getDecodedData());
        assertEquals("/Document/body/content/paragraphs[99]/points[99]",
                instance.getDecodedTag("2/2/2[99]/3[99]", "Document")
                        .getDecodedData());
        assertEquals("/Document/body/suffix/text", instance.getDecodedTag("2/3/1", "Document")
                .getDecodedData());

        // test partial
        assertEquals("/Document/header/published/99/98", instance.getDecodedTag("1/0/99/98", "Document")
                .getDecodedData());
        assertEquals("/Document/body/lastModified/99/98", instance.getDecodedTag("2/0/99/98", "Document")
                .getDecodedData());

        // test unknown
        assertEquals("/Document/99/98", instance.getDecodedTag("/99/98", "Document")
                .getDecodedData());
    }

    @Test
    public void testGetPrintableString() throws Exception
    {
        // TODO ASN-8
    }

    @Test
    public void testGetDecodedObject() throws Exception
    {
        // TODO ASN-8
    }
}
