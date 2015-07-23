/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaModule;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.*;

public class AsnSchemaImplTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** default instance to test */
    private static AsnSchemaImpl instance;

    /** all modules in the default instance */
    private static ImmutableMap<String, AsnSchemaModule> modules;

    // -------------------------------------------------------------------------
    // SETUP/TEAR-DOWN
    // -------------------------------------------------------------------------

    @BeforeClass
    public static void setUpBeforeClass() throws ParseException
    {
        modules = MockAsnSchemaModule.createMockedAsnSchemaModules();
        instance = new AsnSchemaImpl("Document-PDU", modules);
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
            new AsnSchemaImpl(null, modules);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new AsnSchemaImpl("Document-PDU", null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test empty
        try
        {
            new AsnSchemaImpl("", modules);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new AsnSchemaImpl(" ", modules);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new AsnSchemaImpl("Document-PDU", ImmutableMap.<String, AsnSchemaModule>of());
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }

        // test invalid primary module
        try
        {
            new AsnSchemaImpl("UNKNOWN", modules);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetDecodedTag() throws Exception
    {
        final ImmutableMap<String, String> tagsGood = ImmutableMap.<String, String>builder()
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

        final ImmutableSet<OperationResult<DecodedTag>> decodedTagsGood
                = instance.getDecodedTags(tagsGood.keySet(), "Document");

        for (OperationResult<DecodedTag> decodedTag : decodedTagsGood)
        {
            assertTrue(decodedTag.wasSuccessful());

            final DecodedTag output = decodedTag.getOutput();
            assertEquals(tagsGood.get(output.getRawTag()), output.getTag());
        }

        final ImmutableMap<String, String> tagsBad  = ImmutableMap.<String, String>builder()
                .put("0[1]/0[0]/99[99]/98[98]", "/Document/header/published/99[99]/98[98]")
                .put("99[99]/98[98]", "/Document/99[99]/98[98]")
                .build();

        final ImmutableSet<OperationResult<DecodedTag>> decodedTagsBad
                = instance.getDecodedTags(tagsBad.keySet(), "Document");

        for (OperationResult<DecodedTag> decodedTag : decodedTagsBad)
        {
            assertFalse(decodedTag.wasSuccessful());

            final DecodedTag output = decodedTag.getOutput();
            assertEquals(tagsBad.get(output.getRawTag()), output.getTag());
        }
    }
}
