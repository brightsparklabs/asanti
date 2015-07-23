/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema.type;

import static org.junit.Assert.*;

import com.brightsparklabs.asanti.model.schema.type.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Units tests for {@link AsnSchemaComponentType}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaComponentTypeTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testConstructionPreConditions() throws Exception
    {

        // test null tag name
        try
        {
            new AsnSchemaComponentType(null, "TAG", true, AsnSchemaType.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test empty
        try
        {
            new AsnSchemaComponentType("", "TAG", true, AsnSchemaType.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new AsnSchemaComponentType(" ", "TAG",  true, AsnSchemaType.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }



    }


    @Test
    public void testGetTagName() throws Exception
    {
        final AsnSchemaComponentType instance = new AsnSchemaComponentType("TAG_NAME", "TAG", true, AsnSchemaType.NULL);
        assertEquals("TAG_NAME", instance.getName());
    }

    @Test
    public void testGetTag() throws Exception
    {
        AsnSchemaComponentType instance = new AsnSchemaComponentType("TAG_NAME", "TAG", true,
                AsnSchemaType.NULL);
        assertEquals("TAG", instance.getTag());

        // test null
        instance = new AsnSchemaComponentType("TAG_NAME", null, true, AsnSchemaType.NULL);
        assertEquals("", instance.getTag());

        // test empty
        instance = new AsnSchemaComponentType("TAG_NAME", "", true, AsnSchemaType.NULL);
        assertEquals("", instance.getTag());
    }

    @Test
    public void testIsOptional() throws Exception
    {
        AsnSchemaComponentType instance = new AsnSchemaComponentType("TAG_NAME", "TAG", true, AsnSchemaType.NULL);
        assertEquals(true, instance.isOptional());

        instance = new AsnSchemaComponentType("TAG_NAME", "TAG", false, AsnSchemaType.NULL);
        assertEquals(false, instance.isOptional());
    }

    @Test
    public void testGetType()
    {
        final AsnSchemaComponentType instanceNull = new AsnSchemaComponentType("TAG_NAME", "TAG", true, AsnSchemaType.NULL);
        assertEquals(AsnSchemaType.NULL, instanceNull.getType());

        // Test that we get out the type that we put in
        AsnSchemaType mocked = mock(AsnSchemaType.class);
        final AsnSchemaComponentType instance = new AsnSchemaComponentType("TAG_NAME", "TAG", true, mocked);
        assertEquals(mocked, instance.getType());
    }

    @Test
    public void testSetTag() throws Exception
    {
        AsnSchemaComponentType instance = new AsnSchemaComponentType("TAG_NAME", "TAG", true,
                AsnSchemaType.NULL);
        assertEquals("TAG", instance.getTag());

        instance.setTag("OTHER");
        assertEquals("OTHER", instance.getTag());

        // null, converts to empty
        instance.setTag(null);
        assertEquals("", instance.getTag());

        instance.setTag("OTHER");
        assertEquals("OTHER", instance.getTag());

        // blank
        instance.setTag("");
        assertEquals("", instance.getTag());
    }

}
