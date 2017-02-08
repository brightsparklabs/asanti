package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaComponentType;
import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaType;
import com.brightsparklabs.asanti.model.schema.AsnModuleTaggingMode;
import com.brightsparklabs.asanti.model.schema.DecodingSession;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveTypes;
import com.brightsparklabs.asanti.model.schema.tag.AsnSchemaTag;
import com.brightsparklabs.assam.schema.AsnPrimitiveType;
import java.util.Optional;
import com.google.common.collect.ImmutableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AsnSchemaTypeConstructed}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeConstructedTest
{

    // -------------------------------------------------------------------------
    // SETUP/TEAR-DOWN
    // -------------------------------------------------------------------------
    @Before
    public void setUpBeforeTest() throws Exception
    {
    }

    @After
    public void validate()
    {
        // forces Mockito to cause the failure (for verify) on the failing test, rather than the next one!
        validateMockitoUsage();
    }

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaTypeConstructedConstructorPreconditions() throws Exception
    {
        // null primitive
        try
        {
            new AsnSchemaTypeConstructed(null,
                    AsnSchemaConstraint.NULL,
                    ImmutableList.<AsnSchemaComponentType>of(),
                    AsnModuleTaggingMode.AUTOMATIC);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        // null components
        try
        {
            new AsnSchemaTypeConstructed(AsnPrimitiveTypes.SEQUENCE,
                    AsnSchemaConstraint.NULL,
                    null,
                    AsnModuleTaggingMode.AUTOMATIC);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        // null tag creator
        try
        {
            new AsnSchemaTypeConstructed(AsnPrimitiveTypes.SEQUENCE,
                    AsnSchemaConstraint.NULL,
                    ImmutableList.<AsnSchemaComponentType>of(),
                    null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // non Constructed type
        try
        {
            new AsnSchemaTypeConstructed(AsnPrimitiveTypes.INTEGER,
                    AsnSchemaConstraint.NULL,
                    ImmutableList.<AsnSchemaComponentType>of(),
                    AsnModuleTaggingMode.AUTOMATIC);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testAutomaticTaggingAllEmpty() throws Exception
    {
        // check that we do auto tag if that is the mode, and no components have tags.
        ImmutableList<AsnSchemaComponentType> components = ImmutableList.of(mockedComponent("a",
                        "",
                        false,
                        AsnPrimitiveTypes.INTEGER),
                mockedComponent("b", "", false, AsnPrimitiveTypes.INTEGER));

        AsnSchemaTypeConstructed instance = new AsnSchemaTypeConstructed(AsnPrimitiveTypes.SEQUENCE,
                AsnSchemaConstraint.NULL,
                components,
                AsnModuleTaggingMode.AUTOMATIC);
        instance.performTagging();
        instance.checkForDuplicates();

        verify(components.get(0)).setTag("0");
        verify(components.get(1)).setTag("1");
    }

    @Test
    public void testAutomaticTaggingNotAllEmpty() throws Exception
    {
        // check that we don't auto tag if any component has a context-specific tag already
        ImmutableList<AsnSchemaComponentType> components = ImmutableList.of(mockedComponent("a",
                        "0",
                        false,
                        AsnPrimitiveTypes.INTEGER),
                mockedComponent("b", "", false, AsnPrimitiveTypes.INTEGER));

        AsnSchemaTypeConstructed instance = new AsnSchemaTypeConstructed(AsnPrimitiveTypes.SEQUENCE,
                AsnSchemaConstraint.NULL,
                components,
                AsnModuleTaggingMode.AUTOMATIC);
        instance.performTagging();
        instance.checkForDuplicates();

        verify(components.get(0), never()).setTag(anyString());
        verify(components.get(1)).setTag("UNIVERSAL 2");
    }

    @Test
    public void testNotAutomaticTaggingAllEmpty() throws Exception
    {
        // check that we don't auto tag if the mode is not Automatic, even if none of the
        // components have a tag
        ImmutableList<AsnSchemaComponentType> components = ImmutableList.of(mockedComponent("a",
                        "",
                        false,
                        AsnPrimitiveTypes.INTEGER),
                mockedComponent("b", "", false, AsnPrimitiveTypes.OCTET_STRING));

        AsnSchemaTypeConstructed instance = new AsnSchemaTypeConstructed(AsnPrimitiveTypes.SEQUENCE,
                AsnSchemaConstraint.NULL,
                components,
                AsnModuleTaggingMode.DEFAULT);
        instance.performTagging();
        instance.checkForDuplicates();

        verify(components.get(0)).setTag("UNIVERSAL 2");
        verify(components.get(1)).setTag("UNIVERSAL 4");
    }

    @Test
    public void testUniqueInSequence() throws Exception
    {
        // check that we don't think that having the same tag is a duplicate (when they are not)
        final ImmutableList<AsnSchemaComponentType> components
                = ImmutableList.<AsnSchemaComponentType>builder()
                .add(mockedComponent("a", "1", false))
                .add(mockedComponent("b", "1", false))
                .add(mockedComponent("c", "1", true))
                .add(mockedComponent("d", "2", false))
                .add(mockedComponent("e", "", true, AsnPrimitiveTypes.INTEGER))
                .add(mockedComponent("f", "1", false))
                .build();

        AsnSchemaTypeConstructed instance = new AsnSchemaTypeConstructed(AsnPrimitiveTypes.SEQUENCE,
                AsnSchemaConstraint.NULL,
                components,
                AsnModuleTaggingMode.AUTOMATIC);
        instance.performTagging();
        instance.checkForDuplicates();

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
        // check that we do throw when there are duplicates.  c and d are duplicate tags
        final ImmutableList<AsnSchemaComponentType> components
                = ImmutableList.<AsnSchemaComponentType>builder()
                .add(mockedComponent("a", "1", false))
                .add(mockedComponent("b", "1", false))
                .add(mockedComponent("c", "1", true))
                .add(mockedComponent("d", "1", false))
                .build();

        AsnSchemaTypeConstructed instance = new AsnSchemaTypeConstructed(AsnPrimitiveTypes.SEQUENCE,
                AsnSchemaConstraint.NULL,
                components,
                AsnModuleTaggingMode.AUTOMATIC);
        instance.performTagging();

        try
        {
            instance.checkForDuplicates();
            fail("Should have thrown ParseException");
        }
        catch (ParseException e)
        {
        }
    }

    @Test
    public void testUniqueInSet() throws Exception
    {
        // Check the rules for duplicates in a set
        final ImmutableList<AsnSchemaComponentType> components
                = ImmutableList.<AsnSchemaComponentType>builder()
                .add(mockedComponent("a", "1", false))
                .add(mockedComponent("b", "2", false))
                .add(mockedComponent("c", "3", true))
                .add(mockedComponent("d", "4", false))
                .add(mockedComponent("e", "", true, AsnPrimitiveTypes.INTEGER))
                .add(mockedComponent("f", "5", false))
                .build();

        AsnSchemaTypeConstructed instance = new AsnSchemaTypeConstructed(AsnPrimitiveTypes.SEQUENCE,
                AsnSchemaConstraint.NULL,
                components,
                AsnModuleTaggingMode.AUTOMATIC);
        instance.performTagging();
        instance.checkForDuplicates();

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
        // Check the rules for duplicates in a set
        final ImmutableList<AsnSchemaComponentType> components
                = ImmutableList.<AsnSchemaComponentType>builder()
                .add(mockedComponent("a", "1", false))
                .add(mockedComponent("b", "1", false))
                .build();

        AsnSchemaTypeConstructed instance = new AsnSchemaTypeConstructed(AsnPrimitiveTypes.SET,
                AsnSchemaConstraint.NULL,
                components,
                AsnModuleTaggingMode.AUTOMATIC);
        instance.performTagging();
        try
        {
            instance.checkForDuplicates();
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
                .add(mockedComponent("x", "1", false, AsnPrimitiveTypes.INTEGER))
                .add(mockedComponent("y", "2", false, AsnPrimitiveTypes.INTEGER))
                .build();

        AsnSchemaType choice = MockAsnSchemaType.builder(AsnPrimitiveTypes.CHOICE)
                .addComponent(choiceComponents.get(0))
                .addComponent(choiceComponents.get(1))
                .build();

        final ImmutableList<AsnSchemaComponentType> components
                = ImmutableList.<AsnSchemaComponentType>builder()
                .add(mockedComponent("a", "1", false))
                .add(mockedComponent("b", "2", false))
                .add(mockedComponent("c", "", false, choice))
                .build();

        AsnSchemaTypeConstructed instance = new AsnSchemaTypeConstructed(AsnPrimitiveTypes.SEQUENCE,
                AsnSchemaConstraint.NULL,
                components,
                AsnModuleTaggingMode.AUTOMATIC);
        instance.performTagging();
        instance.checkForDuplicates();

        // verify that we recursed in to the Choice components, but did not try to set the tags
        //TODO INC-53
        assertNull(verify(choiceComponents.get(0)).getType());
    }

    @Test
    public void testSequenceChoiceDuplicate1() throws ParseException
    {
        final ImmutableList<AsnSchemaComponentType> choiceComponents
                = ImmutableList.<AsnSchemaComponentType>builder()
                .add(mockedComponent("x", "1", false, AsnPrimitiveTypes.INTEGER))
                .add(mockedComponent("y", "2", false, AsnPrimitiveTypes.INTEGER))
                .build();

        AsnSchemaType choice = MockAsnSchemaType.builder(AsnPrimitiveTypes.CHOICE)
                .addComponent(choiceComponents.get(0))
                .addComponent(choiceComponents.get(1))
                .build();

        final ImmutableList<AsnSchemaComponentType> components
                = ImmutableList.<AsnSchemaComponentType>builder()
                .add(mockedComponent("a", "1", false))
                .add(mockedComponent("b", "2", true))
                .add(mockedComponent("c", "", false, choice))
                .build();

        AsnSchemaTypeConstructed instance = new AsnSchemaTypeConstructed(AsnPrimitiveTypes.SEQUENCE,
                AsnSchemaConstraint.NULL,
                components,
                AsnModuleTaggingMode.AUTOMATIC);
        instance.performTagging();
        try
        {
            // the c/y and b tags are duplicate (both 1[2])
            instance.checkForDuplicates();
            fail("Should have thrown ParseException");
        }
        catch (ParseException e)
        {
        }
    }

    @Test
    public void testGetComponentTypesSequence() throws Exception
    {
        ImmutableList<AsnSchemaComponentType> components = ImmutableList.of(mockedComponent("a",
                        "1",
                        false,
                        AsnPrimitiveTypes.INTEGER),
                mockedComponent("b", "2", true, AsnPrimitiveTypes.INTEGER),
                mockedComponent("c", "3", false, AsnPrimitiveTypes.INTEGER));

        DecodingSession decodingSession = mock(DecodingSession.class);
        when(decodingSession.getIndex(any(AsnSchemaTag.class))).thenReturn(0);

        AsnSchemaTypeConstructed instance = new AsnSchemaTypeConstructed(AsnPrimitiveTypes.SEQUENCE,
                AsnSchemaConstraint.NULL,
                components,
                AsnModuleTaggingMode.DEFAULT);
        instance.performTagging();
        instance.checkForDuplicates();

        final Optional<AsnSchemaComponentType> resultA = instance.getMatchingChild("0[1]",
                decodingSession);
        assertTrue(resultA.isPresent());
        assertEquals(components.get(0), resultA.get());

        verify(decodingSession).setIndex(any(AsnSchemaTag.class), eq(0));

        // check that we can skip an optional
        when(decodingSession.getIndex(any(AsnSchemaTag.class))).thenReturn(1);
        final Optional<AsnSchemaComponentType> resultC = instance.getMatchingChild("1[3]",
                decodingSession);
        assertTrue(resultC.isPresent());
        assertEquals(components.get(2), resultC.get());

        // check that we fail if the tag does not match
        when(decodingSession.getIndex(any(AsnSchemaTag.class))).thenReturn(1);
        final Optional<AsnSchemaComponentType> resultFail = instance.getMatchingChild("1[4]",
                decodingSession);
        assertFalse(resultFail.isPresent());
        // if we fail we should ensure that all the rest of the matches at this level fail
        verify(decodingSession).setIndex(any(AsnSchemaTag.class), eq(3));

        // check that we handle "missing" fields by expecting a but receiving b as the 0th field.
        when(decodingSession.getIndex(any(AsnSchemaTag.class))).thenReturn(0);
        final Optional<AsnSchemaComponentType> resultB = instance.getMatchingChild("0[2]",
                decodingSession);
        assertTrue(resultB.isPresent());
        assertEquals(components.get(1), resultB.get());
        // and ensure that we set the index to b
        verify(decodingSession).setIndex(any(AsnSchemaTag.class), eq(1));

        // check that we don't throw if we have data that is not in the schema
        when(decodingSession.getIndex(any(AsnSchemaTag.class))).thenReturn(10);
        final Optional<AsnSchemaComponentType> resultEmpty = instance.getMatchingChild("4[10]",
                decodingSession);
        assertFalse(resultEmpty.isPresent());
    }

    @Test
    public void testGetComponentTypesSet() throws Exception
    {
        ImmutableList<AsnSchemaComponentType> components = ImmutableList.of(mockedComponent("a",
                        "1",
                        false,
                        AsnPrimitiveTypes.INTEGER),
                mockedComponent("b", "2", false, AsnPrimitiveTypes.INTEGER));

        DecodingSession decodingSession = mock(DecodingSession.class);
        when(decodingSession.getIndex(any(AsnSchemaTag.class))).thenReturn(10); // it should not matter what the index is

        AsnSchemaTypeConstructed instance = new AsnSchemaTypeConstructed(AsnPrimitiveTypes.SET,
                AsnSchemaConstraint.NULL,
                components,
                AsnModuleTaggingMode.DEFAULT);
        instance.performTagging();
        instance.checkForDuplicates();

        final Optional<AsnSchemaComponentType> result = instance.getMatchingChild("10[2]",
                decodingSession);

        assertTrue(result.isPresent());
        assertEquals(components.get(1), result.get());

        // check that we don't throw if we have data that is not in the schema
        when(decodingSession.getIndex(any(AsnSchemaTag.class))).thenReturn(2);
        final Optional<AsnSchemaComponentType> resultEmpty = instance.getMatchingChild("2[3]",
                decodingSession);
        assertFalse(resultEmpty.isPresent());
    }

    @Test
    public void testGetComponentTypesSequenceChoice() throws Exception
    {

        final ImmutableList<AsnSchemaComponentType> choicechoiceComponents
                = ImmutableList.<AsnSchemaComponentType>builder()
                .add(mockedComponent("m", "3", false, AsnPrimitiveTypes.INTEGER))
                .add(mockedComponent("n", "4", false, AsnPrimitiveTypes.INTEGER))
                .build();

        AsnSchemaType choicechoice = MockAsnSchemaType.builder(AsnPrimitiveTypes.CHOICE)
                .addComponent(choicechoiceComponents.get(0))
                .addComponent(choicechoiceComponents.get(1))
                .build();

        final ImmutableList<AsnSchemaComponentType> choiceComponents
                = ImmutableList.<AsnSchemaComponentType>builder()
                .add(mockedComponent("x", "1", false, AsnPrimitiveTypes.INTEGER))
                .add(mockedComponent("y", "2", false, AsnPrimitiveTypes.INTEGER))
                .add(mockedComponent("z", "", false, choicechoice))
                .build();

        AsnSchemaType choice = MockAsnSchemaType.builder(AsnPrimitiveTypes.CHOICE)
                .addComponent(choiceComponents.get(0))
                .addComponent(choiceComponents.get(1))
                .addComponent(choiceComponents.get(2))
                .build();

        final ImmutableList<AsnSchemaComponentType> components
                = ImmutableList.<AsnSchemaComponentType>builder()
                .add(mockedComponent("a", "1", false))
                .add(mockedComponent("b", "2", false))
                .add(mockedComponent("c", "", false, choice))
                .build();

        DecodingSession decodingSession = mock(DecodingSession.class);

        // account for the recursive call that would decorate the tag further.
        AsnSchemaComponentType recursive = mockedComponent("z/n",
                "4",
                false,
                AsnPrimitiveTypes.INTEGER);
        when(choice.getMatchingChild(eq("2[4]"),
                any(DecodingSession.class))).thenReturn(Optional.of(recursive));

        // go straight to the choice
        when(decodingSession.getIndex(any(AsnSchemaTag.class))).thenReturn(2);

        AsnSchemaTypeConstructed instance = new AsnSchemaTypeConstructed(AsnPrimitiveTypes.SEQUENCE,
                AsnSchemaConstraint.NULL,
                components,
                AsnModuleTaggingMode.AUTOMATIC);
        instance.performTagging();
        instance.checkForDuplicates();

        final Optional<AsnSchemaComponentType> result = instance.getMatchingChild("2[4]",
                decodingSession);
        assertTrue(result.isPresent());
        assertEquals("c/z/n", result.get().getName());
        assertEquals(recursive.getType(), result.get().getType());
    }

    @Test
    public void testGetAllComponents() throws Exception
    {
        // check that we only return back the immediate child components, not flattened out
        // choice components
        final ImmutableList<AsnSchemaComponentType> choiceComponents
                = ImmutableList.<AsnSchemaComponentType>builder()
                .add(mockedComponent("x", "1", false, AsnPrimitiveTypes.INTEGER))
                .add(mockedComponent("y", "2", false, AsnPrimitiveTypes.INTEGER))
                .build();

        AsnSchemaType choice = MockAsnSchemaType.builder(AsnPrimitiveTypes.CHOICE)
                .addComponent(choiceComponents.get(0))
                .addComponent(choiceComponents.get(1))
                .build();

        final ImmutableList<AsnSchemaComponentType> components
                = ImmutableList.<AsnSchemaComponentType>builder()
                .add(mockedComponent("a", "1", false))
                .add(mockedComponent("b", "2", false))
                .add(mockedComponent("c", "", false, choice))
                .build();

        AsnSchemaTypeConstructed instance = new AsnSchemaTypeConstructed(AsnPrimitiveTypes.SEQUENCE,
                AsnSchemaConstraint.NULL,
                components,
                AsnModuleTaggingMode.AUTOMATIC);

        assertEquals(3, instance.getAllComponents().size());
        assertEquals(components.get(0), instance.getAllComponents().get(0));

        instance.performTagging();
        instance.checkForDuplicates();

        // check that nothing changes after tagging and duplicate checks.
        assertEquals(3, instance.getAllComponents().size());
        assertEquals(components.get(0), instance.getAllComponents().get(0));

    }

    @Test
    public void testVisitor() throws ParseException
    {

        AsnSchemaTypeConstructed instance = new AsnSchemaTypeConstructed(AsnPrimitiveTypes.SEQUENCE,
                AsnSchemaConstraint.NULL,
                ImmutableList.<AsnSchemaComponentType>of(),
                AsnModuleTaggingMode.AUTOMATIC);

        Object o = instance.accept(BaseAsnSchemaTypeTest.getVisitor());
        assertEquals("Got AsnSchemaTypeConstructed", o);
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
        return mockedComponent(name, tag, isOptional, AsnPrimitiveTypes.INVALID);
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
