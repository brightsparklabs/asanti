package com.brightsparklabs.asanti.model.schema.tag;

import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaComponentType;
import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaType;
import com.brightsparklabs.asanti.model.schema.AsnModuleTaggingMode;
import com.brightsparklabs.asanti.model.schema.DecodingSession;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.google.common.base.Optional;
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
        // check that we do auto tag if that is the mode, and no components have tags.
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
        // check that we don't auto tag if any component has a context-specific tag already
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
    public void testNotAutomaticTaggingAllEmpty() throws Exception
    {
        // check that we don't auto tag if the mode is not Automatic, even if none of the
        // components have a tag
        TagCreator instance = TagCreator.create(AsnPrimitiveType.SEQUENCE,
                AsnModuleTaggingMode.DEFAULT);

        ImmutableList<AsnSchemaComponentType> components = ImmutableList.of(mockedComponent("a",
                        "",
                        false,
                        AsnPrimitiveType.INTEGER),
                mockedComponent("b", "", false, AsnPrimitiveType.OCTET_STRING));

        instance.setTagsForComponents(components);
        instance.checkForDuplicates(components);

        verify(components.get(0)).setTag("UNIVERSAL 2");
        verify(components.get(1)).setTag("UNIVERSAL 4");
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

    @Test
    public void testGetComponentTypesSequence() throws Exception
    {
        TagCreator instance = TagCreator.create(AsnPrimitiveType.SEQUENCE,
                AsnModuleTaggingMode.DEFAULT);

        ImmutableList<AsnSchemaComponentType> components = ImmutableList.of(mockedComponent("a",
                        "1",
                        false,
                        AsnPrimitiveType.INTEGER),
                mockedComponent("b", "2", true, AsnPrimitiveType.INTEGER),
                mockedComponent("c", "3", false, AsnPrimitiveType.INTEGER));

        AsnSchemaTag tag = mock(AsnSchemaTag.class);
        when(tag.getTagPortion()).thenReturn("1");
        DecodingSession decodingSession = mock(DecodingSession.class);
        when(decodingSession.getIndex(eq(tag))).thenReturn(0);

        final Optional<AsnSchemaComponentType> resultA = instance.getComponentType(tag,
                components,
                decodingSession);
        assertTrue(resultA.isPresent());
        assertEquals(components.get(0), resultA.get());

        // check that we can skip an optional
        when(tag.getTagPortion()).thenReturn("3");
        when(decodingSession.getIndex(eq(tag))).thenReturn(1);
        final Optional<AsnSchemaComponentType> resultC = instance.getComponentType(tag,
                components,
                decodingSession);
        assertTrue(resultC.isPresent());
        assertEquals(components.get(2), resultC.get());

        // check that we fail if the tag does not match
        when(tag.getTagPortion()).thenReturn("4");
        when(decodingSession.getIndex(eq(tag))).thenReturn(1);
        final Optional<AsnSchemaComponentType> resultFail = instance.getComponentType(tag,
                components,
                decodingSession);
        assertFalse(resultFail.isPresent());
        // if we fail we should ensure that all the rest of the matches at this level fail
        verify(decodingSession).setIndex(eq(tag), eq(3));

        // check that we don't throw if we have data that is not in the schema
        when(tag.getTagPortion()).thenReturn("4");
        when(decodingSession.getIndex(eq(tag))).thenReturn(10);
        final Optional<AsnSchemaComponentType> resultEmpty = instance.getComponentType(tag,
                components,
                decodingSession);
        assertFalse(resultEmpty.isPresent());

    }

    @Test
    public void testGetComponentTypesSet() throws Exception
    {
        TagCreator instance = TagCreator.create(AsnPrimitiveType.SET, AsnModuleTaggingMode.DEFAULT);

        ImmutableList<AsnSchemaComponentType> components = ImmutableList.of(mockedComponent("a",
                        "1",
                        false,
                        AsnPrimitiveType.INTEGER),
                mockedComponent("b", "2", false, AsnPrimitiveType.INTEGER));

        AsnSchemaTag tag = mock(AsnSchemaTag.class);
        when(tag.getTagPortion()).thenReturn("2");
        DecodingSession decodingSession = mock(DecodingSession.class);
        when(decodingSession.getIndex(eq(tag))).thenReturn(10); // it should not matter what the index is

        final Optional<AsnSchemaComponentType> result = instance.getComponentType(tag,
                components,
                decodingSession);

        assertTrue(result.isPresent());
        assertEquals(components.get(1), result.get());

        // check that we don't throw if we have data that is not in the schema
        when(tag.getTagPortion()).thenReturn("3");
        when(decodingSession.getIndex(eq(tag))).thenReturn(2);
        final Optional<AsnSchemaComponentType> resultEmpty = instance.getComponentType(tag,
                components,
                decodingSession);
        assertFalse(resultEmpty.isPresent());
    }

    @Test
    public void testGetComponentTypesSequenceChoice() throws Exception
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

        DecodingSession decodingSession = mock(DecodingSession.class);
        AsnSchemaTag tag = mock(AsnSchemaTag.class);
        when(tag.getTagPortion()).thenReturn("1");
        when(tag.getRawTag()).thenReturn("2[1]");

        when(choice.getMatchingChild(eq("2[1]"),
                any(DecodingSession.class))).thenReturn(Optional.of(choiceComponents.get(0)));

        // go straight to the choice
        when(decodingSession.getIndex(eq(tag))).thenReturn(2);

        final Optional<AsnSchemaComponentType> result = instance.getComponentType(tag,
                components,
                decodingSession);
        assertTrue(result.isPresent());
        // TODO ASN-115 (review). The internals of getComponentType create a new AsnSchemaComponentType
        // which means that we are dragging in AsnSchemaComponentType to unit test TagCreator
        assertEquals("c/x", result.get().getName());
        assertEquals(choiceComponents.get(0).getType(), result.get().getType());

    }

    /**
     * Creates a mock {@link AsnSchemaComponentType} instance, defaulting to a NULL type
     *
     * @param name
     *         value to return for {@link AsnSchemaComponentType#getName()}
     * @param tag
     *         value to return for {@link AsnSchemaComponentType#getTag()}
     * @param isOptional
     *         value to return for {@link AsnSchemaComponentType#isOptional()} an AsnSchemaType is
     *         mocked around this primitive type
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
     *         an AsnSchemaType is mocked around this primitive type
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
     *         value to return for {@link AsnSchemaComponentType#getType()}
     *
     * @return mock instance which returns the supplied values
     */
    private AsnSchemaComponentType mockedComponent(String name, String tag, boolean isOptional,
            AsnSchemaType type)
    {
        return MockAsnSchemaComponentType.createMockedComponentType(name, tag, isOptional, type);
    }

}