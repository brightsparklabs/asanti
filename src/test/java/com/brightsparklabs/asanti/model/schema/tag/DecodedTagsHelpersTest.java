/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.tag;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.brightsparklabs.asanti.model.data.AsantiAsnData;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;

/** Tests for DecodedTagsHelpers */
public class DecodedTagsHelpersTest {

    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** default instance to test */
    private static AsantiAsnData instance;

    /** empty instance to test */
    private static AsantiAsnData emptyInstance;

    private static final ImmutableSet<String> decodedTags =
            ImmutableSet.<String>builder()
                    .add("/Document/header/published")
                    .add("/Document/header/published/date")
                    .add("/Document/body/lastModified/date")
                    .add("/Document/body/prefix/text")
                    .add("/Document/body/content/text")
                    .add("/Document/footer/author/firstName[0]")
                    .build();

    private static final ImmutableSet<String> unmappedTags =
            ImmutableSet.<String>builder()
                    .add("/Document/body/content/99")
                    .add("/Document/99/1/1")
                    .build();

    // -------------------------------------------------------------------------
    // SETUP/TEAR-DOWN
    // -------------------------------------------------------------------------

    @BeforeClass
    public static void setUpBeforeClass() {
        /** data to construct asnData from */
        instance = mock(AsantiAsnData.class);

        when(instance.getTags()).thenReturn(decodedTags);
        when(instance.getUnmappedTags()).thenReturn(unmappedTags);

        emptyInstance = mock(AsantiAsnData.class);
        when(emptyInstance.getTags()).thenReturn(ImmutableSet.of());
        when(emptyInstance.getUnmappedTags()).thenReturn(ImmutableSet.of());
    }

    @Test
    public void testBuildTagsBadInput() {
        try {
            DecodedTagsHelpers.buildTags(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void testBuildTags() {
        final ImmutableSet<String> fullPath = DecodedTagsHelpers.buildTags(instance);

        final ImmutableSet<String> expected =
                ImmutableSet.<String>builder()
                        .add("/Document")
                        .add("/Document/header")
                        .add("/Document/header/published")
                        .add("/Document/header/published/date")
                        .add("/Document/body")
                        .add("/Document/body/lastModified")
                        .add("/Document/body/lastModified/date")
                        .add("/Document/body/prefix")
                        .add("/Document/body/prefix/text")
                        .add("/Document/body/content")
                        .add("/Document/body/content/text")
                        .add("/Document/footer")
                        .add("/Document/footer/author")
                        .add("/Document/footer/author/firstName[0]")
                        .build();

        List<String> e = Lists.newArrayList(expected);
        Collections.sort(e);

        List<String> f = Lists.newArrayList(fullPath);
        Collections.sort(f);

        assertEquals(e, f);
    }

    @Test
    public void testGetImmediateChildrenBadInput() {

        try {
            DecodedTagsHelpers.getImmediateChildren(null, "/Document");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
        }

        try {
            DecodedTagsHelpers.getImmediateChildren(instance, null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void testGetImmediateChildren() {
        ImmutableSet<String> path = DecodedTagsHelpers.getImmediateChildren(instance, "/Document");

        ImmutableSet<String> expected =
                ImmutableSet.<String>builder().add("header").add("body").add("footer").build();

        List<String> e = Lists.newArrayList(expected);
        Collections.sort(e);

        List<String> f = Lists.newArrayList(path);
        Collections.sort(f);

        assertEquals(e, f);

        path = DecodedTagsHelpers.getImmediateChildren(instance, "/Document/header");

        final ImmutableSet<String> expectedHeaderChildren =
                ImmutableSet.<String>builder().add("published").build();

        e = Lists.newArrayList(expectedHeaderChildren);
        Collections.sort(e);

        f = Lists.newArrayList(path);
        Collections.sort(f);
        assertEquals(e, f);

        path = DecodedTagsHelpers.getImmediateChildren(instance, "/Document/body");

        expected =
                ImmutableSet.<String>builder()
                        .add("lastModified")
                        .add("prefix")
                        .add("content")
                        .build();

        e = Lists.newArrayList(expected);
        Collections.sort(e);

        f = Lists.newArrayList(path);
        Collections.sort(f);
        assertEquals(e, f);

        path = DecodedTagsHelpers.getImmediateChildren(instance, "/Document/body/lastModified");

        expected = ImmutableSet.<String>builder().add("date").build();

        e = Lists.newArrayList(expected);
        Collections.sort(e);

        f = Lists.newArrayList(path);
        Collections.sort(f);
        assertEquals(e, f);

        path = DecodedTagsHelpers.getImmediateChildren(instance, "/Document/body/prefix");

        expected = ImmutableSet.<String>builder().add("text").build();

        e = Lists.newArrayList(expected);
        Collections.sort(e);

        f = Lists.newArrayList(path);
        Collections.sort(f);
        assertEquals(e, f);

        path = DecodedTagsHelpers.getImmediateChildren(instance, "/Document/body/content");

        expected = ImmutableSet.<String>builder().add("text").build();

        e = Lists.newArrayList(expected);
        Collections.sort(e);

        f = Lists.newArrayList(path);
        Collections.sort(f);
        assertEquals(e, f);

        path = DecodedTagsHelpers.getImmediateChildren(instance, "/Document/footer");

        expected = ImmutableSet.<String>builder().add("author").build();

        e = Lists.newArrayList(expected);
        Collections.sort(e);

        f = Lists.newArrayList(path);
        Collections.sort(f);
        assertEquals(e, f);

        path = DecodedTagsHelpers.getImmediateChildren(instance, "/Document/footer/author");

        // Note the stripped index
        expected = ImmutableSet.<String>builder().add("firstName").build();

        e = Lists.newArrayList(expected);
        Collections.sort(e);

        f = Lists.newArrayList(path);
        Collections.sort(f);
        assertEquals(e, f);

        // check a leaf node
        path = DecodedTagsHelpers.getImmediateChildren(instance, "/Document/header/published/date");

        expected = ImmutableSet.<String>builder().build();

        e = Lists.newArrayList(expected);
        Collections.sort(e);

        f = Lists.newArrayList(path);
        Collections.sort(f);
        assertEquals(e, f);

        // check a garbage tag
        path = DecodedTagsHelpers.getImmediateChildren(instance, "/Document/header/DoesNotExist");

        expected = ImmutableSet.<String>builder().build();

        e = Lists.newArrayList(expected);
        Collections.sort(e);

        f = Lists.newArrayList(path);
        Collections.sort(f);
        assertEquals(e, f);

        // test at the root
        path = DecodedTagsHelpers.getImmediateChildren(instance, "");

        expected = ImmutableSet.<String>builder().add("Document").build();

        e = Lists.newArrayList(expected);
        Collections.sort(e);

        f = Lists.newArrayList(path);
        Collections.sort(f);
        assertEquals(e, f);
    }

    @Test
    public void testStripIndexBadInput() throws Exception {
        try {
            DecodedTagsHelpers.stripIndex(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void testStripIndex() throws Exception {
        assertEquals("someTag", DecodedTagsHelpers.stripIndex("someTag[1]"));
        assertEquals("someTag", DecodedTagsHelpers.stripIndex("someTag"));

        assertEquals("", DecodedTagsHelpers.stripIndex(""));

        assertEquals("someTag[n]", DecodedTagsHelpers.stripIndex("someTag[n]"));

        assertEquals("23sdfg[4]45235", DecodedTagsHelpers.stripIndex("23sdfg[4]45235"));

        assertEquals("someTag[1]/otherTag", DecodedTagsHelpers.stripIndex("someTag[1]/otherTag"));
        assertEquals(
                "someTag[1]/otherTag",
                DecodedTagsHelpers.stripIndex("someTag[1]/otherTag[324523452345]"));
    }
}
