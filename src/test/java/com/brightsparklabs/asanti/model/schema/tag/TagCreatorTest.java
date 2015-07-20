package com.brightsparklabs.asanti.model.schema.tag;

import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.AsnModuleTaggingMode;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaComponentType;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Michael on 20/07/2015.
 */
public class TagCreatorTest
{

    @Test
    public void testCreateInvalid() throws Exception
    {
        try
        {
            TagCreator.create(null, AsnModuleTaggingMode.AUTOMATIC);
            fail("Should have thrown NullPointerException");
        }
        catch (NullPointerException e)
        {
        }
        try
        {
            TagCreator.create(AsnPrimitiveType.SEQUENCE, null);
            fail("Should have thrown NullPointerException");
        }
        catch (NullPointerException e)
        {
        }
    }

    @Test
    public void testAutomaticTaggingAllEmpty() throws Exception
    {

        TagCreator instance = TagCreator.create(AsnPrimitiveType.SEQUENCE,
                AsnModuleTaggingMode.AUTOMATIC);

        AsnSchemaComponentType component1
                = MockAsnSchemaComponentType.createMockedComponentType("a",
                "",
                false,
                AsnPrimitiveType.INTEGER);

        AsnSchemaComponentType component2
                = MockAsnSchemaComponentType.createMockedComponentType("b",
                "",
                false,
                AsnPrimitiveType.INTEGER);
        ImmutableList<AsnSchemaComponentType> components = ImmutableList.of(component1, component2);

        instance.setTagsForComponents(components);

        verify(component1).setTag("0");
        verify(component2).setTag("1");
    }

    @Test
    public void testAutomaticTaggingNotAllEmpty() throws Exception
    {

        TagCreator instance = TagCreator.create(AsnPrimitiveType.SEQUENCE,
                AsnModuleTaggingMode.AUTOMATIC);

        AsnSchemaComponentType component1
                = MockAsnSchemaComponentType.createMockedComponentType("a",
                "0",
                false,
                AsnPrimitiveType.INTEGER);

        AsnSchemaComponentType component2
                = MockAsnSchemaComponentType.createMockedComponentType("b",
                "",
                false,
                AsnPrimitiveType.INTEGER);
        ImmutableList<AsnSchemaComponentType> components = ImmutableList.of(component1, component2);

        instance.setTagsForComponents(components);

        verify(component1).setTag("0");
        verify(component2).setTag("UNIVERSAL 2");
    }

    @Test
    public void testGetComponentTypes() throws Exception
    {

    }
}