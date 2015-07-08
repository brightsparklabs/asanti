package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.tag.TagCreationStrategy;
import com.brightsparklabs.asanti.model.schema.tag.TagCreationStrategyImpl;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Tests for TagCreationStrategyImpl.
 */
public class TagCreationStrategyImplTest
{

    // TODO MJF - make real tests, javadoc class above.

    @Test
    public void testGetTagsForComponents() throws Exception
    {

        ImmutableList<AsnSchemaComponentType> components = ImmutableList.<AsnSchemaComponentType>builder()
                .add(getMockedComponent("1", "a", false))
                .add(getMockedComponent("2", "b", false))
                .add(getMockedComponent("3", "c", false))
                .build();

        TagCreationStrategy taggingStrategy
                = TagCreationStrategyImpl.create(AsnPrimitiveType.SEQUENCE,
                AsnModuleTaggingMode.DEFAULT);

        ImmutableMap<String, AsnSchemaComponentType> result = taggingStrategy.getTagsForComponents(
                components);
    }

    @Test
    public void testGetTagsForComponentsAuto() throws Exception
    {
        ImmutableList<AsnSchemaComponentType> components = ImmutableList.<AsnSchemaComponentType>builder()
                .add(getMockedComponent("", "a", false))
                .add(getMockedComponent("", "b", false))
                .add(getMockedComponent("", "c", false))
                .build();

        TagCreationStrategy taggingStrategy
                = TagCreationStrategyImpl.create(AsnPrimitiveType.SEQUENCE,
                AsnModuleTaggingMode.AUTOMATIC);

        ImmutableMap<String, AsnSchemaComponentType> result = taggingStrategy.getTagsForComponents(
                components);

        assertNotNull(result.get("0[0]"));
        assertNotNull(result.get("1[1]"));
        assertNotNull(result.get("2[2]"));
    }

    @Test
    public void testUniqueInSequence() throws Exception
    {

        ImmutableList<AsnSchemaComponentType> components = ImmutableList.<AsnSchemaComponentType>builder()
                .add(getMockedComponent("1", "a", false))
                .add(getMockedComponent("1", "b", false))
                .add(getMockedComponent("1", "c", true))
                .add(getMockedComponent("2", "d", false))
                .add(getMockedComponent("", "e", true, AsnBuiltinType.Integer))
                .add(getMockedComponent("1", "f", false))
                .build();


        TagCreationStrategy taggingStrategy
                = TagCreationStrategyImpl.create(AsnPrimitiveType.SEQUENCE,
                AsnModuleTaggingMode.DEFAULT);

        ImmutableMap<String, AsnSchemaComponentType> result = taggingStrategy.getTagsForComponents(
                components);

        assertNotNull(result.get("0[1]"));
        assertNotNull(result.get("1[1]"));
        assertNotNull(result.get("2[1]"));

        assertEquals("a", result.get("0[1]").getTagName());
        assertEquals("b", result.get("1[1]").getTagName());
        assertEquals("c", result.get("2[1]").getTagName());

    }

    @Test
    public void testGetTagsForComponentsOptional4() throws Exception
    {
        ImmutableList<AsnSchemaComponentType> components = ImmutableList.<AsnSchemaComponentType>builder()
                .add(getMockedComponent("", "a", false, AsnBuiltinType.Integer))
                .add(getMockedComponent("", "b", false, AsnBuiltinType.Utf8String))
                .add(getMockedComponent("", "c", false, AsnBuiltinType.Integer))
                .build();


        TagCreationStrategy taggingStrategy
                = TagCreationStrategyImpl.create(AsnPrimitiveType.SEQUENCE,
                AsnModuleTaggingMode.DEFAULT);

        ImmutableMap<String, AsnSchemaComponentType> result = taggingStrategy.getTagsForComponents(
                components);
    }

    @Test
    public void testDuplicateTagsSequence() throws Exception
    {
        ImmutableList<AsnSchemaComponentType> components = ImmutableList.<AsnSchemaComponentType>builder()
                .add(getMockedComponent("1", "a", true))
                .add(getMockedComponent("1", "b", false))
                .add(getMockedComponent("1", "c", false))
                .build();

        try
        {
            TagCreationStrategy taggingStrategy = TagCreationStrategyImpl.create(AsnPrimitiveType.SEQUENCE,
                    AsnModuleTaggingMode.DEFAULT);

            ImmutableMap<String, AsnSchemaComponentType> result = taggingStrategy.getTagsForComponents(
                    components);

            fail("Expected ParseExpection");
        }
        catch (ParseException e)
        {
        }
    }

    @Test
    public void testDuplicateTagsSequence2() throws Exception
    {
        ImmutableList<AsnSchemaComponentType> components = ImmutableList.<AsnSchemaComponentType>builder()
                .add(getMockedComponent("1", "a", true))
                .add(getMockedComponent("2", "b", true))
                .add(getMockedComponent("3", "c", true))
                .add(getMockedComponent("1", "d", false))
                .build();

        try
        {
            TagCreationStrategy taggingStrategy = TagCreationStrategyImpl.create(AsnPrimitiveType.SEQUENCE,
                    AsnModuleTaggingMode.DEFAULT);

            ImmutableMap<String, AsnSchemaComponentType> result = taggingStrategy.getTagsForComponents(
                    components);

            fail("Expected ParseExpection");
        }
        catch (ParseException e)
        {
        }
    }

    /**
     * Creates a Mocked AsnSchemaComponentType
     *
     * @param tag
     *         the value that will be returned for getTag
     * @param name
     *         the value that will be returned for getTageName
     * @param isOptional
     *         the value that will be returned for isOptional
     *
     * @return returns a mocked instance
     */
    private AsnSchemaComponentType getMockedComponent(String tag, String name, boolean isOptional)
    {
        AsnSchemaComponentType result = mock(AsnSchemaComponentType.class);

        when(result.getTag()).thenReturn(tag);
        when(result.getTagName()).thenReturn(name);
        when(result.isOptional()).thenReturn(isOptional);

        return result;
    }

    /**
     * Creates a Mocked AsnSchemaComponentType
     *
     * @param tag
     *         the value that will be returned for getTag
     * @param name
     *         the value that will be returned for getTageName
     * @param isOptional
     *         the value that will be returned for isOptional
     * @param type
     *         the value that will be returned for getType
     *
     * @return returns a mocked instance
     */
    private AsnSchemaComponentType getMockedComponent(String tag, String name, boolean isOptional,
            AsnBuiltinType type)
    {
        AsnSchemaType st = mock(AsnSchemaType.class);

        when(st.getBuiltinType()).thenReturn(type);

        AsnSchemaComponentType result = getMockedComponent(tag, name, isOptional);
        when(result.getType()).thenReturn(st);

        return result;
    }
}
