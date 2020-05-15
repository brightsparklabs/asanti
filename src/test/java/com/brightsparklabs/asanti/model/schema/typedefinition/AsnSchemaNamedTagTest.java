/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.typedefinition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Units tests for {@link AsnSchemaNamedTag}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaNamedTagTest {
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaNamedTag_Name() throws Exception {
        // test null tagName
        try {
            new AsnSchemaNamedTag(null, null);
            fail("NullPointerException not thrown");
        } catch (final NullPointerException ex) {
        }

        // test blank
        try {
            new AsnSchemaNamedTag("", null);
            fail("IllegalArgumentException not thrown");
        } catch (final IllegalArgumentException ex) {
        }
        try {
            new AsnSchemaNamedTag(" ", null);
            fail("IllegalArgumentException not thrown");
        } catch (final IllegalArgumentException ex) {
        }

        // test null tag
        try {
            new AsnSchemaNamedTag("TAG_NAME", null);
            fail("NullPointerException not thrown");
        } catch (final NullPointerException ex) {
        }

        // test blank
        try {
            new AsnSchemaNamedTag("TAG_NAME", "");
            fail("IllegalArgumentException not thrown");
        } catch (final IllegalArgumentException ex) {
        }
        try {
            new AsnSchemaNamedTag("TAG_NAME", " ");
            fail("IllegalArgumentException not thrown");
        } catch (final IllegalArgumentException ex) {
        }
    }

    @Test
    public void testGetTagName() throws Exception {
        final AsnSchemaNamedTag instance = new AsnSchemaNamedTag("TAG_NAME", "0");
        assertEquals("TAG_NAME", instance.getTagName());
    }

    @Test
    public void testGetTag() throws Exception {
        AsnSchemaNamedTag instance = new AsnSchemaNamedTag("TAG_NAME", "0");
        assertEquals("0", instance.getTag());
    }
}
