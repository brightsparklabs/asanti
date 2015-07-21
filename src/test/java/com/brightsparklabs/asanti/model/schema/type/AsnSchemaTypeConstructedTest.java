package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.DecodingSession;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.tag.AsnSchemaTag;
import com.brightsparklabs.asanti.model.schema.tag.TagCreator;
import com.google.common.base.Optional;
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
    // FIXTURES
    // -------------------------------------------------------------------------

    /** the only component to add */
    private AsnSchemaComponentType component;

    /** the instance that will be used for testing */
    private AsnSchemaTypeConstructed instance;

    /** the instance used in construction */
    private TagCreator tagCreator;

    // -------------------------------------------------------------------------
    // SETUP/TEAR-DOWN
    // -------------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    @Before
    public void setUpBeforeTest() throws Exception
    {
        component = mock(AsnSchemaComponentType.class);

        tagCreator = mock(TagCreator.class);

        when(tagCreator.getComponentTypes(any(AsnSchemaTag.class),
                any(ImmutableList.class),
                any(DecodingSession.class))).thenReturn(Optional.of(component));

        instance = new AsnSchemaTypeConstructed(AsnPrimitiveType.SEQUENCE,
                AsnSchemaConstraint.NULL,
                ImmutableList.of(component),
                tagCreator);
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
                    tagCreator);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        // null components
        try
        {
            new AsnSchemaTypeConstructed(AsnPrimitiveType.SEQUENCE,
                    AsnSchemaConstraint.NULL,
                    null,
                    tagCreator);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        // null tag creator
        try
        {
            new AsnSchemaTypeConstructed(AsnPrimitiveType.SEQUENCE,
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
            new AsnSchemaTypeConstructed(AsnPrimitiveType.INTEGER,
                    AsnSchemaConstraint.NULL,
                    ImmutableList.<AsnSchemaComponentType>of(),
                    tagCreator);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetMatchingChild() throws ParseException
    {

        DecodingSession decodingSession = mock(DecodingSession.class);
        final Optional<AsnSchemaComponentType> matchingChild = instance.getMatchingChild("0",
                decodingSession);

        assertTrue(matchingChild.isPresent());

        verify(tagCreator).getComponentTypes(any(AsnSchemaTag.class),
                any(ImmutableList.class),
                any(DecodingSession.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testPerformTagging() throws ParseException
    {
        // check that this delegates through to the tag creator
        verify(tagCreator, never()).setTagsForComponents(any(Iterable.class));
        instance.performTagging();
        verify(tagCreator).setTagsForComponents(any(Iterable.class));
    }

    @Test
    public void testGetAllComponents() throws Exception
    {
        assertEquals(1, instance.getAllComponents().size());
        assertEquals(component, instance.getAllComponents().get(0));
    }
}