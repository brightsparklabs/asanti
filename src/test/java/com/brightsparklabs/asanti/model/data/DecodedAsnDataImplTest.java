/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.data;

import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchema;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for public class {@link DecodedAsnDataImpl}
 *
 * @author brightSPARK Labs
 */
public class DecodedAsnDataImplTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** default instance to test */
    private static DecodedAsnData instance;

    /** empty instance to test */
    private static DecodedAsnData emptyInstance;

    // -------------------------------------------------------------------------
    // SETUP/TEAR-DOWN
    // -------------------------------------------------------------------------

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        /** data to construct asnData from */
        final ImmutableMap<String, byte[]> tagsToData = ImmutableMap.<String, byte[]>builder()
                .put("/1/0/1", "/1/0/1".getBytes(Charsets.UTF_8))
                .put("/2/0/0", "/2/0/0".getBytes(Charsets.UTF_8))
                .put("/2/1/1", "/2/1/1".getBytes(Charsets.UTF_8))
                .put("/2/2/1", "/2/2/1".getBytes(Charsets.UTF_8))
                .put("/3/0/1", "/3/0/1".getBytes(Charsets.UTF_8))
                .put("/2/2/99", "/2/2/99".getBytes(Charsets.UTF_8))
                .put("/99/1/1", "/99/1/1".getBytes(Charsets.UTF_8))
                .build();

        // create instance
        final AsnData asnData = new AsnDataImpl(tagsToData);
        final AsnSchema asnSchema = MockAsnSchema.getInstance();
        instance = new DecodedAsnDataImpl(asnData, asnSchema, "Document");

        // create empty instance
        final AsnData emptyAsnData = new AsnDataImpl(Maps.<String, byte[]>newHashMap());
        emptyInstance = new DecodedAsnDataImpl(emptyAsnData, asnSchema, "Document");
    }

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecodedAsnDataDefault() throws Exception
    {
        final AsnData mockData = mock(AsnData.class);
        final AsnSchema mockSchema = mock(AsnSchema.class);

        try
        {
            new DecodedAsnDataImpl(null, null, null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new DecodedAsnDataImpl(null, null, "Test");
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new DecodedAsnDataImpl(null, mockSchema, null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new DecodedAsnDataImpl(null, mockSchema, "Test");
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new DecodedAsnDataImpl(mockData, null, null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new DecodedAsnDataImpl(mockData, null, "Test");
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new DecodedAsnDataImpl(mockData, mockSchema, null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new DecodedAsnDataImpl(mockData, mockSchema, "");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetTags() throws Exception
    {
        ImmutableSet<String> tags = instance.getTags();
        assertEquals(tags.size(), 5);
        assertTrue(tags.contains("/Document/header/published/date"));
        assertTrue(tags.contains("/Document/body/lastModified/date"));
        assertTrue(tags.contains("/Document/body/prefix/text"));
        assertTrue(tags.contains("/Document/body/content/text"));
        assertTrue(tags.contains("/Document/footer/author/firstName"));

        tags = emptyInstance.getTags();
        assertEquals(tags.size(), 0);
    }

    @Test
    public void testGetUnmappedTags() throws Exception
    {
        ImmutableSet<String> tags = instance.getUnmappedTags();
        assertEquals(tags.size(), 2);
        assertTrue(tags.contains("/Document/body/content/99"));
        assertTrue(tags.contains("/Document/99/1/1"));

        tags = emptyInstance.getUnmappedTags();
        assertEquals(tags.size(), 0);
    }

    @Test
    public void testContains() throws Exception
    {
        assertTrue(instance.contains("/Document/header/published/date"));
        assertTrue(instance.contains("/Document/body/lastModified/date"));
        assertTrue(instance.contains("/Document/body/prefix/text"));
        assertTrue(instance.contains("/Document/body/content/text"));
        assertTrue(instance.contains("/Document/footer/author/firstName"));

        // test unmapped tags
        assertTrue(instance.contains("/Document/body/content/99"));
        assertTrue(instance.contains("/Document/99/1/1"));

        // test raw tags
        assertFalse(instance.contains("/2/2/99"));
        assertFalse(instance.contains("/99/1/1"));

        // test unknown tags
        assertFalse(instance.contains(""));
        assertFalse(instance.contains("/Document/0/0/0"));

        assertFalse(emptyInstance.contains(""));
        assertFalse(emptyInstance.contains("/Document/0/0/0"));
        assertFalse(emptyInstance.contains("/Document/header/published/date"));
    }

    @Test
    public void testGetBytes() throws Exception
    {
        assertArrayEquals("/1/0/1".getBytes(Charsets.UTF_8),
                instance.getBytes("/Document/header/published/date").get());
        assertArrayEquals("/2/0/0".getBytes(Charsets.UTF_8),
                instance.getBytes("/Document/body/lastModified/date").get());
        assertArrayEquals("/2/1/1".getBytes(Charsets.UTF_8),
                instance.getBytes("/Document/body/prefix/text").get());
        assertArrayEquals("/2/2/1".getBytes(Charsets.UTF_8),
                instance.getBytes("/Document/body/content/text").get());
        assertArrayEquals("/3/0/1".getBytes(Charsets.UTF_8),
                instance.getBytes("/Document/footer/author/firstName").get());

        // test unmapped tags
        assertArrayEquals("/2/2/99".getBytes(Charsets.UTF_8),
                instance.getBytes("/Document/body/content/99").get());
        assertArrayEquals("/99/1/1".getBytes(Charsets.UTF_8),
                instance.getBytes("/Document/99/1/1").get());

        // test raw tags
        assertArrayEquals("/2/2/99".getBytes(Charsets.UTF_8), instance.getBytes("/2/2/99").get());
        assertArrayEquals("/99/1/1".getBytes(Charsets.UTF_8), instance.getBytes("/99/1/1").get());

        // test unknown tags
        assertArrayEquals("".getBytes(Charsets.UTF_8), instance.getBytes("").get());
        assertArrayEquals("".getBytes(Charsets.UTF_8), instance.getBytes("/Document/0/0/0").get());
        assertArrayEquals("".getBytes(Charsets.UTF_8), instance.getBytes("/Document/2/2/99").get());

        assertArrayEquals("".getBytes(Charsets.UTF_8), emptyInstance.getBytes("").get());
        assertArrayEquals("".getBytes(Charsets.UTF_8),
                emptyInstance.getBytes("/Document/0/0/0").get());
        assertArrayEquals("".getBytes(Charsets.UTF_8),
                emptyInstance.getBytes("/Document/header/published/date").get());
    }

    @Test
    public void testGetBytesMatching() throws Exception
    {
        Pattern regex = Pattern.compile("/Document/body/.+");
        ImmutableMap<String, byte[]> result = instance.getBytesMatching(regex);
        assertEquals(4, result.size());
        assertArrayEquals("/2/0/0".getBytes(Charsets.UTF_8),
                result.get("/Document/body/lastModified/date"));
        assertArrayEquals("/2/1/1".getBytes(Charsets.UTF_8),
                result.get("/Document/body/prefix/text"));
        assertArrayEquals("/2/2/1".getBytes(Charsets.UTF_8),
                result.get("/Document/body/content/text"));
        assertArrayEquals("/2/2/99".getBytes(Charsets.UTF_8),
                result.get("/Document/body/content/99"));
        assertArrayEquals("".getBytes(Charsets.UTF_8), instance.getBytes("/Document/2/2/99").get());
        result = emptyInstance.getBytesMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile("/Document/\\d+/1/\\d{1}");
        result = instance.getBytesMatching(regex);
        assertEquals(1, result.size());
        assertArrayEquals("/99/1/1".getBytes(Charsets.UTF_8), result.get("/Document/99/1/1"));
        result = emptyInstance.getBytesMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile("/0/.+");
        result = instance.getBytesMatching(regex);
        assertEquals(0, result.size());
        result = emptyInstance.getBytesMatching(regex);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetHexString() throws Exception
    {
        assertEquals("0x2F312F302F31",
                instance.getHexString("/Document/header/published/date").get());
        assertEquals("0x2F322F302F30",
                instance.getHexString("/Document/body/lastModified/date").get());
        assertEquals("0x2F322F312F31", instance.getHexString("/Document/body/prefix/text").get());
        assertEquals("0x2F322F322F31", instance.getHexString("/Document/body/content/text").get());
        assertEquals("0x2F332F302F31",
                instance.getHexString("/Document/footer/author/firstName").get());

        // test unmapped tags
        assertEquals("0x2F322F322F3939", instance.getHexString("/Document/body/content/99").get());
        assertEquals("0x2F39392F312F31", instance.getHexString("/Document/99/1/1").get());

        // test raw tags
        assertEquals("0x2F322F322F3939", instance.getHexString("/2/2/99").get());
        assertEquals("0x2F39392F312F31", instance.getHexString("/99/1/1").get());

        // test unknown tags
        assertEquals("0x", instance.getHexString("").get());
        assertEquals("0x", instance.getHexString("/0/0/0").get());
        assertEquals("0x", instance.getHexString("/Document/2/2/99").get());

        assertEquals("0x", emptyInstance.getHexString("").get());
        assertEquals("0x", emptyInstance.getHexString("/Document/0/0/0").get());
        assertEquals("0x", emptyInstance.getHexString("/Document/header/published/date").get());
    }

    @Test
    public void testGetHexStringsMatching() throws Exception
    {
        Pattern regex = Pattern.compile("/Document/bod[x-z]/.+");
        ImmutableMap<String, String> result = instance.getHexStringsMatching(regex);
        assertEquals(4, result.size());
        assertEquals("0x2F322F302F30", result.get("/Document/body/lastModified/date"));
        assertEquals("0x2F322F312F31", result.get("/Document/body/prefix/text"));
        assertEquals("0x2F322F322F31", result.get("/Document/body/content/text"));
        assertEquals("0x2F322F322F3939", result.get("/Document/body/content/99"));
        result = emptyInstance.getHexStringsMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile("/Document/\\d{2,4}/\\d/1");
        result = instance.getHexStringsMatching(regex);
        assertEquals(1, result.size());
        assertEquals("0x2F39392F312F31", result.get("/Document/99/1/1"));
        result = emptyInstance.getHexStringsMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile("/Document/header/a.*");
        result = instance.getHexStringsMatching(regex);
        assertEquals(0, result.size());
        result = emptyInstance.getHexStringsMatching(regex);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetPrintableString() throws Exception
    {
        // TODO: ASN-107 - requires decoder logic to be complete
        /*
        assertEquals("printableString",
                instance.getPrintableString("/Document/header/published/date"));
        assertEquals("printableString",
                instance.getPrintableString("/Document/body/lastModified/date"));
        assertEquals("printableString", instance.getPrintableString("/Document/body/prefix/text"));
        assertEquals("printableString", instance.getPrintableString("/Document/body/content/text"));
        assertEquals("printableString",
                instance.getPrintableString("/Document/footer/author/firstName"));

        // test unmapped tags
        assertEquals("printableString", instance.getPrintableString("/Document/body/content/99"));
        assertEquals("printableString", instance.getPrintableString("/Document/99/1/1"));

        // test raw tags
        assertEquals("printableString", instance.getPrintableString("/2/2/99"));
        assertEquals("printableString", instance.getPrintableString("/99/1/1"));

        // test unknown tags
        assertEquals("", instance.getPrintableString(""));
        assertEquals("", instance.getPrintableString("/0/0/0"));
        assertEquals("", instance.getPrintableString("/Document/2/2/99"));

        assertEquals("", emptyInstance.getPrintableString(""));
        assertEquals("", emptyInstance.getPrintableString("/0/0/0"));
        assertEquals("", emptyInstance.getPrintableString("/Document/header/published/date"));
        */
    }

    @Test
    public void testGetPrintableStringsMatching() throws Exception
    {
        // TODO: ASN-107 - requires decoder logic to be complete
        /*
        Pattern regex = Pattern.compile(".+text");
        ImmutableMap<String, String> result = instance.getPrintableStringsMatching(regex);
        assertEquals(2, result.size());
        assertEquals("printableString", result.get("/Document/body/prefix/text"));
        assertEquals("printableString", result.get("/Document/body/content/text"));
        result = emptyInstance.getPrintableStringsMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile("/Document.+/\\d{1}");
        result = instance.getPrintableStringsMatching(regex);
        assertEquals(1, result.size());
        assertEquals("printableString", result.get("/Document/99/1/1"));
        result = emptyInstance.getPrintableStringsMatching(regex);
        assertEquals(0, result.size());
        */
        //regex = Pattern.compile(".*/a[^/]+");
        /*
        result = instance.getPrintableStringsMatching(regex);
        assertEquals(0, result.size());
        result = emptyInstance.getPrintableStringsMatching(regex);
        assertEquals(0, result.size());
        */
    }

    @Test
    public void testGetType() throws Exception
    {
        assertEquals(AsnBuiltinType.Date,
                instance.getType("/Document/header/published/date").get().getBuiltinType());
        assertEquals(AsnBuiltinType.Date,
                instance.getType("/Document/body/lastModified/date").get().getBuiltinType());
        assertEquals(AsnBuiltinType.OctetString,
                instance.getType("/Document/body/prefix/text").get().getBuiltinType());
        assertEquals(AsnBuiltinType.OctetString,
                instance.getType("/Document/body/content/text").get().getBuiltinType());
        assertEquals(AsnBuiltinType.OctetString,
                instance.getType("/Document/footer/author/firstName").get().getBuiltinType());

        // test unmapped tags
        assertEquals(AsnBuiltinType.Null,
                instance.getType("/Document/body/content/99").get().getBuiltinType());
        assertEquals(AsnBuiltinType.Null, instance.getType("/Document/99/1/1").or(AsnSchemaType.NULL).getBuiltinType());

        // test raw tags
        assertEquals(AsnBuiltinType.Null,
                instance.getType("/2/2/99").or(AsnSchemaType.NULL).getBuiltinType());
        assertEquals(AsnBuiltinType.Null,
                instance.getType("/99/1/1").or(AsnSchemaType.NULL).getBuiltinType());

        // test unknown tags
        assertEquals(AsnBuiltinType.Null, instance.getType("").or(AsnSchemaType.NULL).getBuiltinType());
        assertEquals(AsnBuiltinType.Null, instance.getType("/Document/0/0/0").or(AsnSchemaType.NULL).getBuiltinType());

        assertEquals(AsnBuiltinType.Null, emptyInstance.getType("").or(AsnSchemaType.NULL).getBuiltinType());
        assertEquals(AsnBuiltinType.Null,
                emptyInstance.getType("/Document/0/0/0").or(AsnSchemaType.NULL).getBuiltinType());

        assertEquals(AsnBuiltinType.Date,
                emptyInstance.getType("/Document/header/published/date").or(AsnSchemaType.NULL).getBuiltinType());
    }

    @Test
    public void testGetDecodedObject() throws Exception
    {
        // TODO: ASN-107 - requires decoder logic to be complete
        /*
        assertEquals("decodedObject", instance.getDecodedObject("/Document/header/published/date"));
        assertEquals("decodedObject",
                instance.getDecodedObject("/Document/body/lastModified/date"));
        assertEquals("decodedObject", instance.getDecodedObject("/Document/body/prefix/text"));
        assertEquals("decodedObject", instance.getDecodedObject("/Document/body/content/text"));
        assertEquals("decodedObject",
                instance.getDecodedObject("/Document/footer/author/firstName"));

        // test unmapped tags
        assertEquals("decodedObject", instance.getDecodedObject("/Document/body/content/99"));
        assertEquals("decodedObject", instance.getDecodedObject("/Document/99/1/1"));

        // test raw tags
        assertEquals("decodedObject", instance.getDecodedObject("/2/2/99"));
        assertEquals("decodedObject", instance.getDecodedObject("/99/1/1"));

        // test unknown tags
        assertEquals("", instance.getDecodedObject(""));
        assertEquals("", instance.getDecodedObject("/Document/0/0/0"));

        assertEquals("", emptyInstance.getDecodedObject(""));
        assertEquals("", emptyInstance.getDecodedObject("/Document/0/0/0"));
        assertEquals("", emptyInstance.getDecodedObject("/Document/header/published/date"));
        */
    }

    @Test
    public void testGetDecodedObjectsMatching() throws Exception
    {
        // TODO: ASN-107 - requires decoder logic to be complete
        /*
        Pattern regex = Pattern.compile(".+dy.+");
        ImmutableMap<String, Object> result = instance.getDecodedObjectsMatching(regex);
        assertEquals(4, result.size());
        assertEquals("decodedObject", result.get("/Document/body/lastModified/date"));
        assertEquals("decodedObject", result.get("/Document/body/prefix/text"));
        assertEquals("decodedObject", result.get("/Document/body/content/text"));
        assertEquals("decodedObject", result.get("/Document/body/content/99"));
        result = emptyInstance.getDecodedObjectsMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile("/Document/9[0-9]/\\d+/\\d{1}");
        result = instance.getDecodedObjectsMatching(regex);
        assertEquals(1, result.size());
        assertEquals("decodedObject", result.get("/Document/99/1/1"));
        result = emptyInstance.getDecodedObjectsMatching(regex);
        assertEquals(0, result.size());
        */
        //regex = Pattern.compile(".*/
        /*
        A[ ^/]+"); result = instance.getDecodedObjectsMatching(regex);
        assertEquals(0, result.size());
        result = emptyInstance.getDecodedObjectsMatching(regex);
        assertEquals(0, result.size());
        */
    }
}
