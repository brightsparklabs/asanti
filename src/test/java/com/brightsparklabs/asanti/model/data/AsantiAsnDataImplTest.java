/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.data;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.brightsparklabs.asanti.mocks.model.schema.TestAsnSchema;
import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.schema.AsnBuiltinType;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.io.BaseEncoding;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.regex.Pattern;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for public class {@link AsantiAsnDataImpl}
 *
 * @author brightSPARK Labs
 */
public class AsantiAsnDataImplTest {
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** default instance to test */
    private static AsantiAsnData instance;

    /** empty instance to test */
    private static AsantiAsnData emptyInstance;

    private static final String PUBLISHED_DATE_STRING = "20150101000000.00Z";
    private static final String ALIASED_PUBLISHED_DATE_STRING = "202101010000Z";
    private static final String MODIFIED_DATE_STRING = "20150102000000.00Z";
    private static final String PREFIX_TEXT = "prefix text";
    private static final String CONTENT_TEXT = "content text";
    private static final String FIRST_NAME = "firstName";

    private static final OffsetDateTime publishDateZ =
            OffsetDateTime.of(2015, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    private static final OffsetDateTime publishDate =
            OffsetDateTime.ofInstant(publishDateZ.toInstant(), ZoneId.systemDefault());

    private static final OffsetDateTime aliasedPublishDateZ =
            OffsetDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    private static final OffsetDateTime aliasedPublishDate =
            OffsetDateTime.ofInstant(aliasedPublishDateZ.toInstant(), ZoneId.systemDefault());

    private static final OffsetDateTime modifiedDateZ =
            OffsetDateTime.of(2015, 1, 2, 0, 0, 0, 0, ZoneOffset.UTC);

    private static final OffsetDateTime modifiedDate =
            OffsetDateTime.ofInstant(modifiedDateZ.toInstant(), ZoneId.systemDefault());

    // -------------------------------------------------------------------------
    // SETUP/TEAR-DOWN
    // -------------------------------------------------------------------------

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        /* data to construct rawAsnData from */

        // This byte array should parse as BER
        final byte[] aliasedBytes =
                BaseEncoding.base16()
                        // .decode("A00F810D3230323130313031303030305A");
                        .decode("A00F810D3230323130313031303030305A810101");

        final ImmutableMap<String, byte[]> tagsToData =
                ImmutableMap.<String, byte[]>builder()
                        .put("0[1]/0[0]/1[1]", PUBLISHED_DATE_STRING.getBytes(Charsets.UTF_8))
                        .put("1[2]/0[0]/0[0]", MODIFIED_DATE_STRING.getBytes(Charsets.UTF_8))
                        .put("1[2]/0[0]/0[99]", "/2/0/99".getBytes(Charsets.UTF_8))
                        .put("1[2]/1[1]/0[1]", PREFIX_TEXT.getBytes(Charsets.UTF_8))
                        .put("1[2]/2[2]/0[1]", CONTENT_TEXT.getBytes(Charsets.UTF_8))
                        .put("2[3]/0[0]/0[UNIVERSAL 16]/0[1]", FIRST_NAME.getBytes(Charsets.UTF_8))
                        .put("6[7]", aliasedBytes)
                        .put("0[99]/0[1]/0[1]", "/99/1/1".getBytes(Charsets.UTF_8))
                        .build();

        // create instance
        final RawAsnData rawAsnData = new RawAsnDataImpl(tagsToData);
        final AsnSchema asnSchema = TestAsnSchema.getInstance();
        instance = new AsantiAsnDataImpl(rawAsnData, asnSchema, "Document");

        // create empty instance
        final RawAsnData emptyAsnData = new RawAsnDataImpl(Maps.newHashMap());
        emptyInstance = new AsantiAsnDataImpl(emptyAsnData, asnSchema, "Document");
    }

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecodedAsnDataDefault() throws Exception {
        final RawAsnData mockData = mock(RawAsnData.class);
        final AsnSchema mockSchema = mock(AsnSchema.class);

        try {
            new AsantiAsnDataImpl(null, null, null);
            fail("NullPointerException not thrown");
        } catch (final NullPointerException ex) {
        }
        try {
            new AsantiAsnDataImpl(null, null, "Test");
            fail("NullPointerException not thrown");
        } catch (final NullPointerException ex) {
        }
        try {
            new AsantiAsnDataImpl(null, mockSchema, null);
            fail("NullPointerException not thrown");
        } catch (final NullPointerException ex) {
        }
        try {
            new AsantiAsnDataImpl(null, mockSchema, "Test");
            fail("NullPointerException not thrown");
        } catch (final NullPointerException ex) {
        }
        try {
            new AsantiAsnDataImpl(mockData, null, null);
            fail("NullPointerException not thrown");
        } catch (final NullPointerException ex) {
        }
        try {
            new AsantiAsnDataImpl(mockData, null, "Test");
            fail("NullPointerException not thrown");
        } catch (final NullPointerException ex) {
        }
        try {
            new AsantiAsnDataImpl(mockData, mockSchema, null);
            fail("NullPointerException not thrown");
        } catch (final NullPointerException ex) {
        }
        try {
            new AsantiAsnDataImpl(mockData, mockSchema, "");
            fail("IllegalArgumentException not thrown");
        } catch (final IllegalArgumentException ex) {
        }
    }

    @Test
    public void testGetTags() throws Exception {
        ImmutableSet<String> tags = instance.getTags();
        assertEquals(7, tags.size());
        assertTrue(tags.contains("/Document/header/published/date"));
        assertTrue(tags.contains("/Document/aliasHeader/published/date"));
        assertTrue(tags.contains("/Document/aliasHeader")); // The OCTET STRING itself
        assertTrue(tags.contains("/Document/body/lastModified/date"));
        assertTrue(tags.contains("/Document/body/prefix/text"));
        assertTrue(tags.contains("/Document/body/content/text"));
        assertTrue(tags.contains("/Document/footer/authors[0]/firstName"));

        tags = emptyInstance.getTags();
        assertEquals(tags.size(), 0);
    }

    @Test
    public void testGetTagsMatching() throws Exception {
        // test can match all tags
        final Pattern patternMatchAllTags = Pattern.compile("/\\Document.*");
        final ImmutableSet<String> matchingAllTags = instance.getTagsMatching(patternMatchAllTags);
        assertEquals(10, matchingAllTags.size());

        // test can match some tags
        final Pattern patternMatchDocumentBodyTags = Pattern.compile("/\\Document/body/.*");
        final ImmutableSet<String> matchingDocumentBodyTags =
                instance.getTagsMatching(patternMatchDocumentBodyTags);
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
    public void testGetUnmappedTags() throws Exception {
        ImmutableSet<String> tags = instance.getUnmappedTags();
        assertEquals(3, tags.size());
        assertTrue(tags.contains("/Document/aliasHeader/0[1]"));
        assertTrue(tags.contains("/Document/body/lastModified/0[99]"));
        assertTrue(tags.contains("/Document/0[99]/0[1]/0[1]"));

        tags = emptyInstance.getUnmappedTags();
        assertEquals(tags.size(), 0);
    }

    @Test
    public void testContains() throws Exception {
        assertTrue(instance.contains("/Document/header/published/date"));
        assertTrue(instance.contains("/Document/body/lastModified/date"));
        assertTrue(instance.contains("/Document/body/prefix/text"));
        assertTrue(instance.contains("/Document/body/content/text"));
        assertTrue(instance.contains("/Document/footer/authors[0]/firstName"));

        // test unmapped tags
        assertTrue(instance.contains("/Document/body/lastModified/0[99]"));
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
    public void testGetBytes() throws Exception {

        assertArrayEquals(
                PUBLISHED_DATE_STRING.getBytes(Charsets.UTF_8),
                instance.getBytes("/Document/header/published/date").get());
        assertArrayEquals(
                MODIFIED_DATE_STRING.getBytes(Charsets.UTF_8),
                instance.getBytes("/Document/body/lastModified/date").get());
        assertArrayEquals(
                ALIASED_PUBLISHED_DATE_STRING.getBytes(Charsets.UTF_8),
                instance.getBytes("/Document/aliasHeader/published/date").get());

        assertArrayEquals(
                PREFIX_TEXT.getBytes(Charsets.UTF_8),
                instance.getBytes("/Document/body/prefix/text").get());
        assertArrayEquals(
                CONTENT_TEXT.getBytes(Charsets.UTF_8),
                instance.getBytes("/Document/body/content/text").get());
        assertArrayEquals(
                FIRST_NAME.getBytes(Charsets.UTF_8),
                instance.getBytes("/Document/footer/authors[0]/firstName").get());

        // test unmapped tags
        assertArrayEquals(
                "/2/0/99".getBytes(Charsets.UTF_8),
                instance.getBytes("/Document/body/lastModified/0[99]").get());
        assertArrayEquals(
                "/99/1/1".getBytes(Charsets.UTF_8),
                instance.getBytes("/Document/0[99]/0[1]/0[1]").get());

        // test raw tags
        assertArrayEquals(
                "/2/0/99".getBytes(Charsets.UTF_8), instance.getBytes("1[2]/0[0]/0[99]").get());
        assertArrayEquals(
                "/99/1/1".getBytes(Charsets.UTF_8), instance.getBytes("0[99]/0[1]/0[1]").get());

        // test unknown tags
        assertFalse(instance.getBytes("").isPresent());
        assertFalse(instance.getBytes("/Document/0/0/0").isPresent());
        assertFalse(instance.getBytes("/Document/2/2/99").isPresent());

        assertFalse(emptyInstance.getBytes("").isPresent());
        assertFalse(emptyInstance.getBytes("/Document/0/0/0").isPresent());
        assertFalse(emptyInstance.getBytes("/Document/header/published/date").isPresent());
    }

    @Test
    public void testGetBytesMatching() throws Exception {
        Pattern regex = Pattern.compile("/Document/body/.+");
        ImmutableMap<String, byte[]> result = instance.getBytesMatching(regex);
        assertEquals(4, result.size());
        assertArrayEquals(
                MODIFIED_DATE_STRING.getBytes(Charsets.UTF_8),
                result.get("/Document/body/lastModified/date"));
        assertArrayEquals(
                PREFIX_TEXT.getBytes(Charsets.UTF_8), result.get("/Document/body/prefix/text"));
        assertArrayEquals(
                CONTENT_TEXT.getBytes(Charsets.UTF_8), result.get("/Document/body/content/text"));
        assertArrayEquals(
                "/2/0/99".getBytes(Charsets.UTF_8),
                result.get("/Document/body/lastModified/0[99]"));
        assertFalse(instance.getBytes("/Document/1[2]/0[0]/0[99]").isPresent());

        result = emptyInstance.getBytesMatching(regex);
        assertEquals(0, result.size());

        // regex = Pattern.compile("/Document/\\d+/1/\\d{1}");
        regex = Pattern.compile("/Document/.*?/0\\[1\\]/.*");
        result = instance.getBytesMatching(regex);
        assertEquals(1, result.size());
        assertArrayEquals(
                "/99/1/1".getBytes(Charsets.UTF_8), result.get("/Document/0[99]/0[1]/0[1]"));

        result = emptyInstance.getBytesMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile("1\\[2\\]/0\\[0\\]/.+");
        result = instance.getBytesMatching(regex);
        assertEquals(2, result.size());
        assertArrayEquals(
                MODIFIED_DATE_STRING.getBytes(Charsets.UTF_8), result.get("1[2]/0[0]/0[0]"));
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
    public void testGetHexString() throws Exception {
        final BaseEncoding enc = BaseEncoding.base16();
        assertEquals(
                enc.encode(PUBLISHED_DATE_STRING.getBytes(StandardCharsets.UTF_8)),
                instance.getHexString("/Document/header/published/date").get());
        assertEquals(
                enc.encode(MODIFIED_DATE_STRING.getBytes(StandardCharsets.UTF_8)),
                instance.getHexString("/Document/body/lastModified/date").get());
        assertEquals(
                enc.encode(PREFIX_TEXT.getBytes(StandardCharsets.UTF_8)),
                instance.getHexString("/Document/body/prefix/text").get());
        assertEquals(
                enc.encode(CONTENT_TEXT.getBytes(StandardCharsets.UTF_8)),
                instance.getHexString("/Document/body/content/text").get());
        assertEquals(
                enc.encode(FIRST_NAME.getBytes(StandardCharsets.UTF_8)),
                instance.getHexString("/Document/footer/authors[0]/firstName").get());

        // test unmapped tags
        assertEquals(
                "2F322F302F3939", instance.getHexString("/Document/body/lastModified/0[99]").get());
        assertEquals("2F39392F312F31", instance.getHexString("/Document/0[99]/0[1]/0[1]").get());

        // test raw tags
        assertEquals("2F322F302F3939", instance.getHexString("1[2]/0[0]/0[99]").get());
        assertEquals("2F39392F312F31", instance.getHexString("0[99]/0[1]/0[1]").get());

        // test unmapped is the same as respective raw
        assertEquals(
                instance.getHexString("/Document/body/lastModified/0[99]").get(),
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
    public void testGetHexStringsMatching() throws Exception {
        final BaseEncoding enc = BaseEncoding.base16();
        Pattern regex = Pattern.compile("/Document/bod[x-z]/.+");
        ImmutableMap<String, String> result = instance.getHexStringsMatching(regex);

        assertEquals(4, result.size());
        assertEquals(
                enc.encode(MODIFIED_DATE_STRING.getBytes(StandardCharsets.UTF_8)),
                result.get("/Document/body/lastModified/date"));
        assertEquals(
                enc.encode(PREFIX_TEXT.getBytes(StandardCharsets.UTF_8)),
                result.get("/Document/body/prefix/text"));
        assertEquals(
                enc.encode(CONTENT_TEXT.getBytes(StandardCharsets.UTF_8)),
                result.get("/Document/body/content/text"));
        assertEquals("2F322F302F3939", result.get("/Document/body/lastModified/0[99]"));

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
        assertEquals(
                enc.encode(MODIFIED_DATE_STRING.getBytes(StandardCharsets.UTF_8)),
                result.get("1[2]/0[0]/0[0]"));
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
    public void testGetPrintableString() throws Exception {
        assertEquals(
                PUBLISHED_DATE_STRING,
                instance.getPrintableString("/Document/header/published/date").get());

        assertEquals(
                MODIFIED_DATE_STRING,
                instance.getPrintableString("/Document/body/lastModified/date").get());

        assertEquals(
                ALIASED_PUBLISHED_DATE_STRING,
                instance.getPrintableString("/Document/aliasHeader/published/date").get());

        assertEquals(PREFIX_TEXT, instance.getPrintableString("/Document/body/prefix/text").get());
        assertEquals(
                CONTENT_TEXT, instance.getPrintableString("/Document/body/content/text").get());
        assertEquals(
                FIRST_NAME,
                instance.getPrintableString("/Document/footer/authors[0]/firstName").get());

        // test unmapped tags
        assertFalse(instance.getPrintableString("/Document/body/lastModified/0[99]").isPresent());
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
        assertFalse(
                emptyInstance.getPrintableString("/Document/header/published/date").isPresent());
    }

    @Test
    public void testGetPrintableStringsMatching() throws Exception {
        Pattern regex = Pattern.compile(".+text");
        ImmutableMap<String, String> result = instance.getPrintableStringsMatching(regex);
        assertEquals(2, result.size());
        assertEquals(PREFIX_TEXT, result.get("/Document/body/prefix/text"));
        assertEquals(CONTENT_TEXT, result.get("/Document/body/content/text"));
        result = emptyInstance.getPrintableStringsMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile("/Document.+/\\d{1}");
        result = instance.getPrintableStringsMatching(regex);
        assertEquals(0, result.size());
        result = emptyInstance.getPrintableStringsMatching(regex);
        assertEquals(0, result.size());
        regex = Pattern.compile(".*/z[^/]+");
        result = instance.getPrintableStringsMatching(regex);
        assertEquals(0, result.size());
        result = emptyInstance.getPrintableStringsMatching(regex);
        assertEquals(0, result.size());

        assertEquals(0, instance.getPrintableStringsMatching(null).size());
    }

    @Test
    public void testGetType() throws Exception {
        assertEquals(
                AsnBuiltinType.GeneralizedTime,
                instance.getType("/Document/header/published/date").get().getBuiltinType());
        assertEquals(
                AsnBuiltinType.GeneralizedTime,
                instance.getType("/Document/body/lastModified/date").get().getBuiltinType());
        assertEquals(
                AsnBuiltinType.OctetString,
                instance.getType("/Document/body/prefix/text").get().getBuiltinType());
        assertEquals(
                AsnBuiltinType.Utf8String,
                instance.getType("/Document/body/content/text").get().getBuiltinType());
        assertEquals(
                AsnBuiltinType.VisibleString,
                instance.getType("/Document/footer/authors[0]/firstName").get().getBuiltinType());

        // test unmapped tags
        assertFalse(instance.getType("/Document/body/content/0[99]").isPresent());
        assertFalse(instance.getType("/Document/0[99]/0[1]/0[1]").isPresent());

        // test raw tags (demonstrating two different usages or Optional)
        assertFalse(instance.getType("/2/2/99").isPresent());
        assertEquals(
                AsnBuiltinType.Null,
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
    public void testGetDecodedObjectWithType() throws Exception {
        assertEquals(
                publishDate,
                instance.getDecodedObject("/Document/header/published/date", OffsetDateTime.class)
                        .get());
        assertEquals(
                modifiedDate,
                instance.getDecodedObject("/Document/body/lastModified/date", OffsetDateTime.class)
                        .get());

        assertEquals(
                aliasedPublishDate,
                instance.getDecodedObject(
                                "/Document/aliasHeader/published/date", OffsetDateTime.class)
                        .get());

        byte[] b1 = new byte[0];
        final Class<? extends byte[]> aClass = b1.getClass();
        assertArrayEquals(
                PREFIX_TEXT.getBytes(StandardCharsets.UTF_8),
                instance.getDecodedObject("/Document/body/prefix/text", aClass).get());
        assertEquals(
                "content text",
                instance.getDecodedObject("/Document/body/content/text", String.class).get());
        assertEquals(
                "firstName",
                instance.getDecodedObject("/Document/footer/authors[0]/firstName", String.class)
                        .get());

        try {
            instance.getDecodedObject("/Document/header/published/date", String.class);
            fail("Should have thrown ClassCastException");
        } catch (ClassCastException e) {
            // expected
        }
        try {
            instance.getDecodedObject("/Document/body/prefix/text", BigInteger.class);
            fail("Should have thrown ClassCastException");
        } catch (ClassCastException e) {
            // expected
        }

        // test unmapped tags, where it should not matter if the type is wrong
        // (the fact that they are unmapped means that there is no known type)
        assertFalse(
                instance.getDecodedObject("/Document/body/content/99", String.class).isPresent());
        assertFalse(
                instance.getDecodedObject("/Document/body/content/99", Timestamp.class)
                        .isPresent());
        assertFalse(
                instance.getDecodedObject("/Document/body/content/99", BigInteger.class)
                        .isPresent());
        assertFalse(instance.getDecodedObject("/Document/99/1/1", String.class).isPresent());
    }

    @Test
    public void testGetDecodedObject() throws Exception {
        assertEquals(
                publishDate,
                instance.getDecodedObject("/Document/header/published/date", OffsetDateTime.class)
                        .get());
        assertEquals(
                modifiedDate,
                instance.getDecodedObject("/Document/body/lastModified/date", OffsetDateTime.class)
                        .get());
        assertEquals(
                aliasedPublishDate,
                instance.getDecodedObject(
                                "/Document/aliasHeader/published/date", OffsetDateTime.class)
                        .get());

        byte[] b1 = new byte[0];
        final Class<? extends byte[]> aClass = b1.getClass();
        assertArrayEquals(
                PREFIX_TEXT.getBytes(StandardCharsets.UTF_8),
                instance.getDecodedObject("/Document/body/prefix/text", aClass).get());
        assertEquals(
                CONTENT_TEXT,
                instance.getDecodedObject("/Document/body/content/text", String.class).get());
        assertEquals(
                FIRST_NAME,
                instance.getDecodedObject("/Document/footer/authors[0]/firstName", String.class)
                        .get());

        // test unmapped tags
        assertFalse(
                instance.getDecodedObject("/Document/body/content/99", String.class).isPresent());
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
        assertFalse(
                emptyInstance
                        .getDecodedObject("/Document/header/published/date", String.class)
                        .isPresent());
    }

    @Test
    public void testGetDecodedObjectsMatching() throws Exception {
        Pattern regex = Pattern.compile(".+dy.+");
        ImmutableMap<String, Object> result = instance.getDecodedObjectsMatching(regex);
        assertEquals(3, result.size());
        assertEquals(modifiedDate, result.get("/Document/body/lastModified/date"));

        assertArrayEquals(
                PREFIX_TEXT.getBytes(StandardCharsets.UTF_8),
                (byte[]) result.get("/Document/body/prefix/text"));
        assertEquals(CONTENT_TEXT, result.get("/Document/body/content/text"));
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
