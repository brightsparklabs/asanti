/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.data;

import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchema;
import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.assam.schema.AsnBuiltinType;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for public class {@link AsantiAsnDataImpl}
 *
 * @author brightSPARK Labs
 */
public class AsantiAsnDataImplTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** default instance to test */
    private static AsantiAsnData instance;

    /** empty instance to test */
    private static AsantiAsnData emptyInstance;

    // -------------------------------------------------------------------------
    // SETUP/TEAR-DOWN
    // -------------------------------------------------------------------------

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        /** data to construct rawAsnData from */
        final ImmutableMap<String, byte[]> tagsToData = ImmutableMap.<String, byte[]>builder().put(
                "0[1]/0[0]/1[1]",
                "/1/0/1".getBytes(Charsets.UTF_8))
                .put("1[2]/0[0]/0[0]", "/2/0/0".getBytes(Charsets.UTF_8))
                .put("1[2]/1[1]/0[1]", "/2/1/1".getBytes(Charsets.UTF_8))
                .put("1[2]/2[2]/0[1]", "/2/2/1".getBytes(Charsets.UTF_8))
                .put("2[3]/0[0]/0[UNIVERSAL 16]/0[1]", "/3/0/1".getBytes(Charsets.UTF_8))
                .put("1[2]/0[0]/0[99]", "/2/0/99".getBytes(Charsets.UTF_8))
                .put("0[99]/0[1]/0[1]", "/99/1/1".getBytes(Charsets.UTF_8))
                .build();

        // create instance
        final RawAsnData rawAsnData = new RawAsnDataImpl(tagsToData);
        final AsnSchema asnSchema = MockAsnSchema.getInstance();
        instance = new AsantiAsnDataImpl(rawAsnData, asnSchema, "Document");

        // create empty instance
        final RawAsnData emptyAsnData = new RawAsnDataImpl(Maps.newHashMap());
        emptyInstance = new AsantiAsnDataImpl(emptyAsnData, asnSchema, "Document");
    }

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecodedAsnDataDefault() throws Exception
    {
        final RawAsnData mockData = mock(RawAsnData.class);
        final AsnSchema mockSchema = mock(AsnSchema.class);

        try
        {
            new AsantiAsnDataImpl(null, null, null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new AsantiAsnDataImpl(null, null, "Test");
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new AsantiAsnDataImpl(null, mockSchema, null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new AsantiAsnDataImpl(null, mockSchema, "Test");
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new AsantiAsnDataImpl(mockData, null, null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new AsantiAsnDataImpl(mockData, null, "Test");
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new AsantiAsnDataImpl(mockData, mockSchema, null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new AsantiAsnDataImpl(mockData, mockSchema, "");
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
        assertTrue(tags.contains("/Document/footer/authors[0]/firstName"));

        tags = emptyInstance.getTags();
        assertEquals(tags.size(), 0);
    }

    @Test
    public void testGetTagsMatching() throws Exception
    {
        // test can match all tags
        final Pattern patternMatchAllTags = Pattern.compile("/\\Document.*");
        final ImmutableSet<String> matchingAllTags = instance.getTagsMatching(patternMatchAllTags);
        assertEquals(7, matchingAllTags.size());

        // test can match some tags
        final Pattern patternMatchDocumentBodyTags = Pattern.compile("/\\Document/body/.*");
        final ImmutableSet<String> matchingDocumentBodyTags = instance.getTagsMatching(patternMatchDocumentBodyTags);
        assertEquals(4, matchingDocumentBodyTags.size());

        // test match no tags
        final Pattern patternMatchNoTags = Pattern.compile("/\\Document/garbage.*");
        final ImmutableSet<String> matchingNoTags = instance.getTagsMatching(patternMatchNoTags);
        assertEquals(0, matchingNoTags.size());

        // test null regex returns empty set
        final ImmutableSet<String> shouldBeEmpty = instance.getTagsMatching(null);
        assertTrue(shouldBeEmpty.isEmpty());
    }

    @Test
    public void testGetUnmappedTags() throws Exception
    {
        ImmutableSet<String> tags = instance.getUnmappedTags();
        assertEquals(tags.size(), 2);
        assertTrue(tags.contains("/Document/body/content/0[99]"));
        assertTrue(tags.contains("/Document/0[99]/0[1]/0[1]"));

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
        assertTrue(instance.contains("/Document/footer/authors[0]/firstName"));

        // test unmapped tags
        assertTrue(instance.contains("/Document/body/content/0[99]"));
        assertTrue(instance.contains("/Document/0[99]/0[1]/0[1]"));

        // test raw tags
        assertFalse(instance.contains("/1[2]/0[0]/0[99]"));
        assertFalse(instance.contains("/99/1/1"));

        // test unknown tags
        assertFalse(instance.contains(""));
        assertFalse(instance.contains("/Document/0/0/0"));

        assertFalse(emptyInstance.contains(""));
        assertFalse(emptyInstance.contains("/Document/0/0/0"));
        assertFalse(emptyInstance.contains("/Document/header/published/date"));

        // Test the Pattern overload
        Pattern pattern = Pattern.compile("(.*?)authors\\[\\d\\](.*?)");
        assertTrue(instance.contains(pattern));
        assertFalse(emptyInstance.contains(pattern));

        pattern = Pattern.compile("(.*?)text$");
        assertTrue(instance.contains(pattern));
        assertFalse(emptyInstance.contains(pattern));

        pattern = Pattern.compile("(.*?)(.*?)authors\\[\\d\\]$");
        assertFalse(instance.contains(pattern));
        assertFalse(emptyInstance.contains(pattern));

        assertFalse(instance.contains((Pattern) null));
        assertFalse(emptyInstance.contains((Pattern) null));
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
                instance.getBytes("/Document/footer/authors[0]/firstName").get());

        // test unmapped tags
        assertArrayEquals("/2/0/99".getBytes(Charsets.UTF_8),
                instance.getBytes("/Document/body/content/0[99]").get());
        assertArrayEquals("/99/1/1".getBytes(Charsets.UTF_8),
                instance.getBytes("/Document/0[99]/0[1]/0[1]").get());

        // test raw tags
        assertArrayEquals("/2/0/99".getBytes(Charsets.UTF_8),
                instance.getBytes("1[2]/0[0]/0[99]").get());
        assertArrayEquals("/99/1/1".getBytes(Charsets.UTF_8),
                instance.getBytes("0[99]/0[1]/0[1]").get());

        // test unknown tags
        assertFalse(instance.getBytes("").isPresent());
        assertFalse(instance.getBytes("/Document/0/0/0").isPresent());
        assertFalse(instance.getBytes("/Document/2/2/99").isPresent());

        assertFalse(emptyInstance.getBytes("").isPresent());
        assertFalse(emptyInstance.getBytes("/Document/0/0/0").isPresent());
        assertFalse(emptyInstance.getBytes("/Document/header/published/date").isPresent());
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
        assertArrayEquals("/2/0/99".getBytes(Charsets.UTF_8),
                result.get("/Document/body/content/0[99]"));
        assertFalse(instance.getBytes("/Document/1[2]/0[0]/0[99]").isPresent());

        result = emptyInstance.getBytesMatching(regex);
        assertEquals(0, result.size());

        //regex = Pattern.compile("/Document/\\d+/1/\\d{1}");
        regex = Pattern.compile("/Document/.*?/0\\[1\\]/.*");
        result = instance.getBytesMatching(regex);
        assertEquals(1, result.size());
        assertArrayEquals("/99/1/1".getBytes(Charsets.UTF_8),
                result.get("/Document/0[99]/0[1]/0[1]"));

        result = emptyInstance.getBytesMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile("1\\[2\\]/0\\[0\\]/.+");
        result = instance.getBytesMatching(regex);
        assertEquals(2, result.size());
        assertArrayEquals("/2/0/0".getBytes(Charsets.UTF_8), result.get("1[2]/0[0]/0[0]"));
        assertArrayEquals("/2/0/99".getBytes(Charsets.UTF_8), result.get("1[2]/0[0]/0[99]"));
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
        assertEquals("2F312F302F31",
                instance.getHexString("/Document/header/published/date").get());
        assertEquals("2F322F302F30",
                instance.getHexString("/Document/body/lastModified/date").get());
        assertEquals("2F322F312F31", instance.getHexString("/Document/body/prefix/text").get());
        assertEquals("2F322F322F31", instance.getHexString("/Document/body/content/text").get());
        assertEquals("2F332F302F31",
                instance.getHexString("/Document/footer/authors[0]/firstName").get());

        // test unmapped tags
        assertEquals("2F322F302F3939", instance.getHexString("/Document/body/content/0[99]").get());
        assertEquals("2F39392F312F31", instance.getHexString("/Document/0[99]/0[1]/0[1]").get());

        // test raw tags
        assertEquals("2F322F302F3939", instance.getHexString("1[2]/0[0]/0[99]").get());
        assertEquals("2F39392F312F31", instance.getHexString("0[99]/0[1]/0[1]").get());

        // test unmapped is the same as respective raw
        assertEquals(instance.getHexString("/Document/body/content/0[99]").get(),
                instance.getHexString("1[2]/0[0]/0[99]").get());

        // test unknown tags
        assertFalse(instance.getHexString("").isPresent());
        assertFalse(instance.getHexString("/0[0]/0[0]/0[0]").isPresent());
        assertFalse(instance.getHexString("/Document/1[2]/0[0]/0[99]").isPresent());

        assertFalse(emptyInstance.getHexString("").isPresent());
        assertFalse(emptyInstance.getHexString("/Document/0/0/0").isPresent());
        assertFalse(emptyInstance.getHexString("/Document/header/published/date").isPresent());
    }

    @Test
    public void testGetHexStringsMatching() throws Exception
    {
        Pattern regex = Pattern.compile("/Document/bod[x-z]/.+");
        ImmutableMap<String, String> result = instance.getHexStringsMatching(regex);
        assertEquals(4, result.size());
        assertEquals("2F322F302F30", result.get("/Document/body/lastModified/date"));
        assertEquals("2F322F312F31", result.get("/Document/body/prefix/text"));
        assertEquals("2F322F322F31", result.get("/Document/body/content/text"));
        assertEquals("2F322F302F3939", result.get("/Document/body/content/0[99]"));
        result = emptyInstance.getHexStringsMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile("/Document/.\\[\\d{2,4}\\]/\\d+\\[\\d+\\]/\\d\\[1\\]");
        result = instance.getHexStringsMatching(regex);
        assertEquals(1, result.size());
        assertEquals("2F39392F312F31", result.get("/Document/0[99]/0[1]/0[1]"));
        result = emptyInstance.getHexStringsMatching(regex);
        assertEquals(0, result.size());

        // raw
        regex = Pattern.compile("1\\[2\\]/0\\[0\\]/.+");
        result = instance.getHexStringsMatching(regex);
        assertEquals(2, result.size());
        assertEquals("2F322F302F30", result.get("1[2]/0[0]/0[0]"));
        assertEquals("2F322F302F3939", result.get("1[2]/0[0]/0[99]"));
        result = emptyInstance.getHexStringsMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile("/Document/header/a.*");
        result = instance.getHexStringsMatching(regex);
        assertEquals(0, result.size());
        result = emptyInstance.getHexStringsMatching(regex);
        assertEquals(0, result.size());

        assertEquals(0, instance.getHexStringsMatching(null).size());
    }

    @Test
    public void testGetPrintableString() throws Exception
    {
        assertEquals(MockAsnSchema.getPublishDate().toString(),
                instance.getPrintableString("/Document/header/published/date").get());

        assertEquals(MockAsnSchema.getLastModifiedDate().toString(),
                instance.getPrintableString("/Document/body/lastModified/date").get());

        assertEquals("prefix text",
                instance.getPrintableString("/Document/body/prefix/text").get());
        assertEquals("content text",
                instance.getPrintableString("/Document/body/content/text").get());
        assertEquals("firstName",
                instance.getPrintableString("/Document/footer/authors[0]/firstName").get());

        // test unmapped tags
        assertFalse(instance.getPrintableString("/Document/body/content/0[99]").isPresent());
        assertFalse(instance.getPrintableString("/Document/99/1/1").isPresent());

        // test raw tags
        assertFalse(instance.getPrintableString("/2/2/99").isPresent());
        assertFalse(instance.getPrintableString("/99/1/1").isPresent());

        // test unknown tags
        assertFalse(instance.getPrintableString("").isPresent());
        assertFalse(instance.getPrintableString("/0/0/0").isPresent());
        assertFalse(instance.getPrintableString("/Document/2/2/99").isPresent());

        assertFalse(emptyInstance.getPrintableString("").isPresent());
        assertFalse(emptyInstance.getPrintableString("/0/0/0").isPresent());
        assertFalse(emptyInstance.getPrintableString("/Document/header/published/date")
                .isPresent());
    }

    @Test
    public void testGetPrintableStringsMatching() throws Exception
    {
        Pattern regex = Pattern.compile(".+text");
        ImmutableMap<String, String> result = instance.getPrintableStringsMatching(regex);
        assertEquals(2, result.size());
        assertEquals("prefix text", result.get("/Document/body/prefix/text"));
        assertEquals("content text", result.get("/Document/body/content/text"));
        result = emptyInstance.getPrintableStringsMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile("/Document.+/\\d{1}");
        result = instance.getPrintableStringsMatching(regex);
        assertEquals(0, result.size());
        result = emptyInstance.getPrintableStringsMatching(regex);
        assertEquals(0, result.size());
        regex = Pattern.compile(".*/a[^/]+");
        result = instance.getPrintableStringsMatching(regex);
        assertEquals(0, result.size());
        result = emptyInstance.getPrintableStringsMatching(regex);
        assertEquals(0, result.size());

        assertEquals(0, instance.getPrintableStringsMatching(null).size());
    }

    @Test
    public void testGetType() throws Exception
    {
        assertEquals(AsnBuiltinType.GeneralizedTime,
                instance.getType("/Document/header/published/date").get().getBuiltinType());
        assertEquals(AsnBuiltinType.GeneralizedTime,
                instance.getType("/Document/body/lastModified/date").get().getBuiltinType());
        assertEquals(AsnBuiltinType.Utf8String,
                instance.getType("/Document/body/prefix/text").get().getBuiltinType());
        assertEquals(AsnBuiltinType.Utf8String,
                instance.getType("/Document/body/content/text").get().getBuiltinType());
        assertEquals(AsnBuiltinType.Utf8String,
                instance.getType("/Document/footer/authors[0]/firstName").get().getBuiltinType());

        // test unmapped tags
        assertFalse(instance.getType("/Document/body/content/0[99]").isPresent());
        assertFalse(instance.getType("/Document/0[99]/0[1]/0[1]").isPresent());

        // test raw tags (demonstrating two different usages or Optional)
        assertFalse(instance.getType("/2/2/99").isPresent());
        assertEquals(AsnBuiltinType.Null,
                instance.getType("/99/1/1").orElse(AsnSchemaType.NULL).getBuiltinType());

        // test unknown tags
        assertFalse(instance.getType("").isPresent());
        assertFalse(instance.getType("/Document/0/0/0").isPresent());

        assertFalse(emptyInstance.getType("").isPresent());
        assertFalse(emptyInstance.getType("/Document/0/0/0").isPresent());

        // even though there is no data, the tag is still a valid schema tag
        assertTrue(emptyInstance.getType("/Document/header/published/date").isPresent());
    }

    @Test
    public void testGetDecodedObjectWithType() throws Exception
    {
        assertEquals(MockAsnSchema.getPublishDate(),
                instance.getDecodedObject("/Document/header/published/date", OffsetDateTime.class)
                        .get());
        assertEquals(MockAsnSchema.getLastModifiedDate(),
                instance.getDecodedObject("/Document/body/lastModified/date", OffsetDateTime.class)
                        .get());
        assertEquals("prefix text",
                instance.getDecodedObject("/Document/body/prefix/text", String.class).get());
        assertEquals("content text",
                instance.getDecodedObject("/Document/body/content/text", String.class).get());
        assertEquals("firstName",
                instance.getDecodedObject("/Document/footer/authors[0]/firstName", String.class)
                        .get());

        try
        {
            instance.getDecodedObject("/Document/header/published/date", String.class);
            fail("Should have thrown ClassCastException");
        }
        catch (ClassCastException e)
        {
            // expected
        }
        try
        {
            instance.getDecodedObject("/Document/body/prefix/text", BigInteger.class);
            fail("Should have thrown ClassCastException");
        }
        catch (ClassCastException e)
        {
            // expected
        }

        // test unmapped tags, where it should not matter if the type is wrong
        // (the fact that they are unmapped means that there is no known type)
        assertFalse(instance.getDecodedObject("/Document/body/content/99", String.class)
                .isPresent());
        assertFalse(instance.getDecodedObject("/Document/body/content/99", Timestamp.class)
                .isPresent());
        assertFalse(instance.getDecodedObject("/Document/body/content/99", BigInteger.class)
                .isPresent());
        assertFalse(instance.getDecodedObject("/Document/99/1/1", String.class).isPresent());
    }

    @Test
    public void testGetDecodedObject() throws Exception
    {
        assertEquals(MockAsnSchema.getPublishDate(),
                instance.getDecodedObject("/Document/header/published/date", OffsetDateTime.class)
                        .get());
        assertEquals(MockAsnSchema.getLastModifiedDate(),
                instance.getDecodedObject("/Document/body/lastModified/date", OffsetDateTime.class)
                        .get());
        assertEquals("prefix text",
                instance.getDecodedObject("/Document/body/prefix/text", String.class).get());
        assertEquals("content text",
                instance.getDecodedObject("/Document/body/content/text", String.class).get());
        assertEquals("firstName",
                instance.getDecodedObject("/Document/footer/authors[0]/firstName", String.class)
                        .get());

        // test unmapped tags
        assertFalse(instance.getDecodedObject("/Document/body/content/99", String.class)
                .isPresent());
        assertFalse(instance.getDecodedObject("/Document/99/1/1", String.class).isPresent());

        // test raw tags
        assertFalse(instance.getDecodedObject("/1[2]/0[0]/0[99]", String.class).isPresent());
        assertFalse(instance.getDecodedObject("/0[99]/0[1]/0[1]", String.class).isPresent());

        // test unknown tags
        assertFalse(instance.getDecodedObject("", String.class).isPresent());
        assertFalse(instance.getDecodedObject("/0/0/0", String.class).isPresent());
        assertFalse(instance.getDecodedObject("/Document/2/2/99", String.class).isPresent());

        assertFalse(emptyInstance.getDecodedObject("", String.class).isPresent());
        assertFalse(emptyInstance.getDecodedObject("/0/0/0", String.class).isPresent());
        assertFalse(emptyInstance.getDecodedObject("/Document/header/published/date", String.class)
                .isPresent());
    }

    @Test
    public void testGetDecodedObjectsMatching() throws Exception
    {
        Pattern regex = Pattern.compile(".+dy.+");
        ImmutableMap<String, Object> result = instance.getDecodedObjectsMatching(regex);
        assertEquals(3, result.size());
        assertEquals(MockAsnSchema.getLastModifiedDate(),
                result.get("/Document/body/lastModified/date"));
        assertEquals("prefix text", result.get("/Document/body/prefix/text"));
        assertEquals("content text", result.get("/Document/body/content/text"));
        result = emptyInstance.getDecodedObjectsMatching(regex);
        assertEquals(0, result.size());

        // don't match partial tags
        regex = Pattern.compile("/Document/9[0-9]/\\d+/\\d{1}");
        result = instance.getDecodedObjectsMatching(regex);
        assertEquals(0, result.size());
        result = emptyInstance.getDecodedObjectsMatching(regex);
        assertEquals(0, result.size());
        regex = Pattern.compile(".*/[a-zA-Z]+Name$");
        result = instance.getDecodedObjectsMatching(regex);
        assertEquals(1, result.size());
        assertEquals("firstName", result.get("/Document/footer/authors[0]/firstName"));
        result = emptyInstance.getDecodedObjectsMatching(regex);
        assertEquals(0, result.size());

        assertEquals(0, instance.getDecodedObjectsMatching(null).size());
    }
}
