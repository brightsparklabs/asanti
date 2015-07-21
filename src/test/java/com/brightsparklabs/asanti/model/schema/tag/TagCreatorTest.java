package com.brightsparklabs.asanti.model.schema.tag;

import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaComponentType;
import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaType;
import com.brightsparklabs.asanti.model.schema.AsnModuleTaggingMode;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.text.ParseException;

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

        ImmutableList<AsnSchemaComponentType> components = ImmutableList.of(mockedComponent("a",
                        "",
                        false,
                        AsnPrimitiveType.INTEGER),
                mockedComponent("b", "", false, AsnPrimitiveType.INTEGER));

        instance.setTagsForComponents(components);
        instance.checkForDuplicates(components);

        verify(components.get(0)).setTag("0");
        verify(components.get(1)).setTag("1");
    }

    @Test
    public void testAutomaticTaggingNotAllEmpty() throws Exception
    {

        TagCreator instance = TagCreator.create(AsnPrimitiveType.SEQUENCE,
                AsnModuleTaggingMode.AUTOMATIC);

        ImmutableList<AsnSchemaComponentType> components = ImmutableList.of(mockedComponent("a",
                        "0",
                        false,
                        AsnPrimitiveType.INTEGER),
                mockedComponent("b", "", false, AsnPrimitiveType.INTEGER));

        instance.setTagsForComponents(components);
        instance.checkForDuplicates(components);

        verify(components.get(0), never()).setTag(anyString());
        verify(components.get(1)).setTag("UNIVERSAL 2");
    }

    @Test
    public void testGetComponentTypes() throws Exception
    {

    }

    @Test
    public void testUniqueInSequence() throws Exception
    {
        final ImmutableList<AsnSchemaComponentType> components
                = ImmutableList.<AsnSchemaComponentType>builder()
                .add(mockedComponent("a", "1", false))
                .add(mockedComponent("b", "1", false))
                .add(mockedComponent("c", "1", true))
                .add(mockedComponent("d", "2", false))
                .add(mockedComponent("e", "", true, AsnPrimitiveType.INTEGER))
                .add(mockedComponent("f", "1", false))
                .build();

        final TagCreator instance = TagCreator.create(AsnPrimitiveType.SEQUENCE,
                AsnModuleTaggingMode.AUTOMATIC);

        instance.setTagsForComponents(components);
        instance.checkForDuplicates(components);

        // check that it only set a different tag for the one already with none.
        verify(components.get(0), never()).setTag(anyString());
        verify(components.get(1), never()).setTag(anyString());
        verify(components.get(2), never()).setTag(anyString());
        verify(components.get(3), never()).setTag(anyString());
        verify(components.get(4)).setTag("UNIVERSAL 2");
        verify(components.get(5), never()).setTag(anyString());
    }

    @Test
    public void testDuplicateInSequence() throws Exception
    {

        final ImmutableList<AsnSchemaComponentType> components
                = ImmutableList.<AsnSchemaComponentType>builder()
                .add(mockedComponent("a", "1", false))
                .add(mockedComponent("b", "1", false))
                .add(mockedComponent("c", "1", true))
                .add(mockedComponent("d", "1", false))
                .build();

        final TagCreator instance = TagCreator.create(AsnPrimitiveType.SEQUENCE,
                AsnModuleTaggingMode.AUTOMATIC);

        instance.setTagsForComponents(components);
        try
        {
            instance.checkForDuplicates(components);
            fail("Should have thrown ParseException");
        }
        catch (ParseException e)
        {
        }
    }

    @Test
    public void testUniqueInSet() throws Exception
    {

        final ImmutableList<AsnSchemaComponentType> components
                = ImmutableList.<AsnSchemaComponentType>builder()
                .add(mockedComponent("a", "1", false))
                .add(mockedComponent("b", "2", false))
                .add(mockedComponent("c", "3", true))
                .add(mockedComponent("d", "4", false))
                .add(mockedComponent("e", "", true, AsnPrimitiveType.INTEGER))
                .add(mockedComponent("f", "5", false))
                .build();

        final TagCreator instance = TagCreator.create(AsnPrimitiveType.SET,
                AsnModuleTaggingMode.AUTOMATIC);

        instance.setTagsForComponents(components);
        instance.checkForDuplicates(components);

        // check that it only set a different tag for the one already with none.
        verify(components.get(0), never()).setTag(anyString());
        verify(components.get(1), never()).setTag(anyString());
        verify(components.get(2), never()).setTag(anyString());
        verify(components.get(3), never()).setTag(anyString());
        verify(components.get(4)).setTag("UNIVERSAL 2");
        verify(components.get(5), never()).setTag(anyString());
    }

    @Test
    public void testDuplicateInSet() throws Exception
    {

        final ImmutableList<AsnSchemaComponentType> components
                = ImmutableList.<AsnSchemaComponentType>builder()
                .add(mockedComponent("a", "1", false))
                .add(mockedComponent("b", "1", false))
                .build();

        final TagCreator instance = TagCreator.create(AsnPrimitiveType.SET,
                AsnModuleTaggingMode.AUTOMATIC);

        instance.setTagsForComponents(components);
        try
        {
            instance.checkForDuplicates(components);
            fail("Should have thrown ParseException");
        }
        catch (ParseException e)
        {
        }
    }

    @Test
    public void testSequenceChoice() throws ParseException
    {
        final ImmutableList<AsnSchemaComponentType> choiceComponents
                = ImmutableList.<AsnSchemaComponentType>builder()
                .add(mockedComponent("x", "1", false, AsnPrimitiveType.INTEGER))
                .add(mockedComponent("y", "2", false, AsnPrimitiveType.INTEGER))
                .build();

        AsnSchemaType choice = MockAsnSchemaType.builder(AsnPrimitiveType.CHOICE)
                .addComponent(choiceComponents.get(0))
                .addComponent(choiceComponents.get(1))
                .build();

        final ImmutableList<AsnSchemaComponentType> components
                = ImmutableList.<AsnSchemaComponentType>builder()
                .add(mockedComponent("a", "1", false))
                .add(mockedComponent("b", "2", false))
                .add(mockedComponent("c", "", false, choice))
                .build();

        final TagCreator instance = TagCreator.create(AsnPrimitiveType.SEQUENCE,
                AsnModuleTaggingMode.AUTOMATIC);

        instance.setTagsForComponents(components);
        instance.checkForDuplicates(components);

        // verify that we recursed in to the Choice components, but did not try to set the tags
        verify(choiceComponents.get(0)).getType();
    }

    @Test
    public void testSequenceChoiceDuplicate1() throws ParseException
    {
        final ImmutableList<AsnSchemaComponentType> choiceComponents
                = ImmutableList.<AsnSchemaComponentType>builder()
                .add(mockedComponent("x", "1", false, AsnPrimitiveType.INTEGER))
                .add(mockedComponent("y", "2", false, AsnPrimitiveType.INTEGER))
                .build();

        AsnSchemaType choice = MockAsnSchemaType.builder(AsnPrimitiveType.CHOICE)
                .addComponent(choiceComponents.get(0))
                .addComponent(choiceComponents.get(1))
                .build();

        final ImmutableList<AsnSchemaComponentType> components
                = ImmutableList.<AsnSchemaComponentType>builder()
                .add(mockedComponent("a", "1", false))
                .add(mockedComponent("b", "2", true))
                .add(mockedComponent("c", "", false, choice))
                .build();

        final TagCreator instance = TagCreator.create(AsnPrimitiveType.SEQUENCE,
                AsnModuleTaggingMode.AUTOMATIC);

        instance.setTagsForComponents(components);
        try
        {
            // the c/y and b tags are duplicate (both 1[2])
            instance.checkForDuplicates(components);
            fail("Should have thrown ParseException");
        }
        catch (ParseException e)
        {
        }
    }

    /**
     * Creates a mock {@link AsnSchemaComponentType} instance, defaulting to a NULL type
     *
     * @param name
     *         value to return for {@link AsnSchemaComponentType#getName()}
     * @param tag
     *         value to return for {@link AsnSchemaComponentType#getTag()}
     * @param isOptional
     *         value to return for {@link AsnSchemaComponentType#isOptional()}
     *          an AsnSchemaType is mocked around this primitive type
     *
     * @return mock instance which returns the supplied values
     */
    private AsnSchemaComponentType mockedComponent(String name, String tag, boolean isOptional)
    {
        return mockedComponent(name, tag, isOptional, AsnPrimitiveType.NULL);
    }

    /**
     * Creates a mock {@link AsnSchemaComponentType} instance
     *
     * @param name
     *         value to return for {@link AsnSchemaComponentType#getName()}
     * @param tag
     *         value to return for {@link AsnSchemaComponentType#getTag()}
     * @param isOptional
     *         value to return for {@link AsnSchemaComponentType#isOptional()}
     * @param type
     *          an AsnSchemaType is mocked around this primitive type
     *
     * @return mock instance which returns the supplied values
     */
    private AsnSchemaComponentType mockedComponent(String name, String tag, boolean isOptional,
            AsnPrimitiveType type)
    {
        return MockAsnSchemaComponentType.createMockedComponentType(name, tag, isOptional, type);
    }

    /**
     * Creates a mock {@link AsnSchemaComponentType} instance
     *
     * @param name
     *         value to return for {@link AsnSchemaComponentType#getName()}
     * @param tag
     *         value to return for {@link AsnSchemaComponentType#getTag()}
     * @param isOptional
     *         value to return for {@link AsnSchemaComponentType#isOptional()}
     * @param type
     *          value to return for {@link AsnSchemaComponentType#getType()}
     *
     * @return mock instance which returns the supplied values
     */
    private AsnSchemaComponentType mockedComponent(String name, String tag, boolean isOptional,
            AsnSchemaType type)
    {
        return MockAsnSchemaComponentType.createMockedComponentType(name, tag, isOptional, type);
    }

}