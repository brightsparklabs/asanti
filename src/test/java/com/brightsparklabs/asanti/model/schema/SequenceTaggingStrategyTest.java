package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Michael on 6/07/2015.
 */
public class SequenceTaggingStrategyTest
{


    @Test
    public void testGetTagsForComponents() throws Exception
    {

        List<AsnSchemaComponentType> components = Lists.newArrayList(
                getComponent("1", "a", false),
                getComponent("2", "b", false),
                getComponent("3", "c", false));


        SequenceTaggingStrategy taggingStrategy = new SequenceTaggingStrategy();

        ImmutableMap<String, AsnSchemaComponentType> result =
        taggingStrategy.getTagsForComponents(components, AsnModuleTaggingMode.DEFAULT);
    }

    @Test
    public void testGetTagsForComponentsAuto() throws Exception
    {



        List<AsnSchemaComponentType> components = Lists.newArrayList(
                getComponent("", "a", false),
                getComponent("", "b", false),
                getComponent("", "c", false));

        SequenceTaggingStrategy taggingStrategy = new SequenceTaggingStrategy();

        ImmutableMap<String, AsnSchemaComponentType> result =
                taggingStrategy.getTagsForComponents(components, AsnModuleTaggingMode.AUTOMATIC);
    }


    @Test
    public void testGetTagsForComponentsOptional() throws Exception
    {


        List<AsnSchemaComponentType> components = Lists.newArrayList(
                getComponent("1", "a", true),
                getComponent("1", "b", false),
                getComponent("1", "c", false));

        SequenceTaggingStrategy taggingStrategy = new SequenceTaggingStrategy();

        ImmutableMap<String, AsnSchemaComponentType> result =
                taggingStrategy.getTagsForComponents(components, AsnModuleTaggingMode.DEFAULT);
    }



    @Test
    public void testGetTagsForComponentsOptional4() throws Exception
    {

        List<AsnSchemaComponentType> components = Lists.newArrayList(
                getComponent("", "a", true, AsnBuiltinType.Integer),
                getComponent("", "b", true, AsnBuiltinType.Utf8String),
                getComponent("", "c", false, AsnBuiltinType.Integer));

        SequenceTaggingStrategy taggingStrategy = new SequenceTaggingStrategy();

        ImmutableMap<String, AsnSchemaComponentType> result =
                taggingStrategy.getTagsForComponents(components, AsnModuleTaggingMode.DEFAULT);
    }



    private AsnSchemaComponentType getComponent(String tag, String name, boolean isOptional)
    {
        AsnSchemaComponentType result = mock(AsnSchemaComponentType.class);

        when(result.getTag()).thenReturn(tag);
        when(result.getTagName()).thenReturn(name);
        when(result.isOptional()).thenReturn(isOptional);

        return result;
    }

    private AsnSchemaComponentType getComponent(String tag, String name, boolean isOptional, AsnBuiltinType type)
    {
        AsnSchemaType st = mock(AsnSchemaType.class);

        when(st.getBuiltinType()).thenReturn(type);

        AsnSchemaComponentType result = getComponent(tag, name, isOptional);
        when(result.getType()).thenReturn(st);

        return result;
    }
}
