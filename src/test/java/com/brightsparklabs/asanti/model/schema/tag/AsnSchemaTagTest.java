package com.brightsparklabs.asanti.model.schema.tag;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Michael on 7/07/2015.
 */
public class AsnSchemaTagTest
{

    @Test
    public void testCreate() throws Exception
    {

        AsnSchemaTag tag = AsnSchemaTag.create(null);
        assertEquals("", tag.getRawTag());
        assertEquals("", tag.getTagContextSpecific());
        assertEquals("", tag.getTagIndex());
        assertEquals("", tag.getTagPortion());
        assertEquals("", tag.getTagUniversal());
    }

    @Test
    public void testCreateHelpers() throws Exception
    {
        AsnSchemaTag tag = AsnSchemaTag.create("0[1]");
        assertEquals("0[1]", tag.getRawTag());

        tag = AsnSchemaTag.create(1,"2");
        assertEquals("1[2]", tag.getRawTag());
        tag = AsnSchemaTag.create("2", AsnBuiltinType.Integer);
        assertEquals("2[UNIVERSAL 2]", tag.getRawTag());
        tag = AsnSchemaTag.create(3, tag);
        assertEquals("3[UNIVERSAL 2]", tag.getRawTag());

        tag = AsnSchemaTag.create(AsnSchemaTag.createRawTagUniversal(4, 5));
        assertEquals("4[UNIVERSAL 5]", tag.getRawTag());

        tag = AsnSchemaTag.create(AsnSchemaTag.createRawTagUniversal(4, 31));
        assertEquals("", tag.getRawTag());

    }

    @Test
    public void testToStringAndGetRawTag() throws Exception
    {
        AsnSchemaTag tag = AsnSchemaTag.create("1[0]");
        assertEquals("1[0]", tag.toString());

        tag = AsnSchemaTag.create("0[UNIVERSAL 2]");
        assertEquals("0[UNIVERSAL 2]", tag.toString());

        // Test invalid tags
        tag = AsnSchemaTag.create("0");
        assertEquals("", tag.toString());
        assertEquals("", tag.getRawTag());

        tag = AsnSchemaTag.create("0.Fred");
        assertEquals("", tag.toString());
        assertEquals("", tag.getRawTag());

        tag = AsnSchemaTag.create(".1");
        assertEquals("", tag.toString());
        assertEquals("", tag.getRawTag());

        tag = AsnSchemaTag.create("0.");
        assertEquals("", tag.toString());
        assertEquals("", tag.getRawTag());

        tag = AsnSchemaTag.create("[UNIVERSAL 2]");
        assertEquals("", tag.toString());
        assertEquals("", tag.getRawTag());
    }

    @Test
    public void testGetTagIndex() throws Exception
    {
        AsnSchemaTag tag = AsnSchemaTag.create("1[0]");
        assertEquals("1", tag.getTagIndex());

        tag = AsnSchemaTag.create("0[UNIVERSAL 2]");
        assertEquals("0", tag.getTagIndex());

        // Test invalid tags
        tag = AsnSchemaTag.create("0");
        assertEquals("", tag.getTagIndex());
        tag = AsnSchemaTag.create("0.Fred");
        assertEquals("", tag.getTagIndex());
        tag = AsnSchemaTag.create(".1");
        assertEquals("", tag.getTagIndex());
        tag = AsnSchemaTag.create("0.");
        assertEquals("", tag.getTagIndex());
        tag = AsnSchemaTag.create("[UNIVERSAL 2]");
        assertEquals("", tag.getTagIndex());
    }

    @Test
    public void testGetTagContextSpecific() throws Exception
    {
        AsnSchemaTag tag = AsnSchemaTag.create("1[0]");
        assertEquals("0", tag.getTagContextSpecific());

        tag = AsnSchemaTag.create("0[UNIVERSAL 2]");
        assertEquals("", tag.getTagContextSpecific());

        // Test invalid tags
        tag = AsnSchemaTag.create("0");
        assertEquals("", tag.getTagIndex());
        tag = AsnSchemaTag.create("0.Fred");
        assertEquals("", tag.getTagIndex());
        tag = AsnSchemaTag.create(".1");
        assertEquals("", tag.getTagIndex());
        tag = AsnSchemaTag.create("0.");
        assertEquals("", tag.getTagIndex());
        tag = AsnSchemaTag.create("[UNIVERSAL 2]");
        assertEquals("", tag.getTagIndex());
    }

    @Test
    public void testGetTagUniversal() throws Exception
    {
        AsnSchemaTag tag = AsnSchemaTag.create("1[0]");
        assertEquals("", tag.getTagUniversal());

        tag = AsnSchemaTag.create("0[UNIVERSAL 2]");
        assertEquals("UNIVERSAL 2", tag.getTagUniversal());

        // Test invalid tags
        tag = AsnSchemaTag.create("0");
        assertEquals("", tag.getTagIndex());
        tag = AsnSchemaTag.create("0.Fred");
        assertEquals("", tag.getTagIndex());
        tag = AsnSchemaTag.create(".1");
        assertEquals("", tag.getTagIndex());
        tag = AsnSchemaTag.create("0.");
        assertEquals("", tag.getTagIndex());
        tag = AsnSchemaTag.create("[UNIVERSAL 2]");
        assertEquals("", tag.getTagIndex());
    }

    @Test
    public void testGetTagPortion() throws Exception
    {
        AsnSchemaTag tag = AsnSchemaTag.create("1[0]");
        assertEquals("0", tag.getTagPortion());

        tag = AsnSchemaTag.create("0[UNIVERSAL 2]");
        assertEquals("UNIVERSAL 2", tag.getTagPortion());

        // Test invalid tags
        tag = AsnSchemaTag.create("0");
        assertEquals("", tag.getTagIndex());
        tag = AsnSchemaTag.create("0.Fred");
        assertEquals("", tag.getTagIndex());
        tag = AsnSchemaTag.create(".1");
        assertEquals("", tag.getTagIndex());
        tag = AsnSchemaTag.create("0.");
        assertEquals("", tag.getTagIndex());
        tag = AsnSchemaTag.create("[UNIVERSAL 2]");
        assertEquals("", tag.getTagIndex());
    }

    @Test
    public void testGetBuiltInTypeForUniversalTag()
    {
        assertEquals(AsnBuiltinType.Integer, AsnSchemaTag.getBuiltInTypeForUniversalTag(2).get());
        assertFalse(AsnSchemaTag.getBuiltInTypeForUniversalTag(31).isPresent());
    }
}