package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.AsnModuleTaggingMode;
import com.brightsparklabs.asanti.model.schema.DecodingSession;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
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

    /** the type the instance collection is wrapping */
    private AsnSchemaType wrappedSequence;

    /** the only component to add */
    private AsnSchemaComponentType component;

    /** mocked DecodingSession to be used */
    private DecodingSession session;

    /** the instance that will be used for testing */
    private AsnSchemaTypeConstructed instance;

    // -------------------------------------------------------------------------
    // SETUP/TEAR-DOWN
    // -------------------------------------------------------------------------

    @Before
    public void setUpBeforeTest() throws Exception
    {
        AsnSchemaType sequenceComponent = mock(AsnSchemaType.class);

        session = mock(DecodingSession.class);

        wrappedSequence = mock(AsnSchemaType.class);
        // For the sake of testing that the Collection is delegating to the element type make it
        // return testable values
        when(wrappedSequence.getPrimitiveType()).thenReturn(AsnPrimitiveType.SEQUENCE);

        // TODO MJF getMatchingChild
        //when(wrappedSequence.getChildType("0")).thenReturn(sequenceComponent);
        //when(wrappedSequence.getChildName("0")).thenReturn("foo");

        component = mock(AsnSchemaComponentType.class);
        when(component.getType()).thenReturn(wrappedSequence);
        when(component.getTag()).thenReturn("0");
        when(component.getName()).thenReturn("name");

        instance = new AsnSchemaTypeConstructed(AsnPrimitiveType.SEQUENCE,
                AsnSchemaConstraint.NULL,
                ImmutableList.of(component),
                AsnModuleTaggingMode.DEFAULT);
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
                    AsnModuleTaggingMode.DEFAULT);
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
                    AsnModuleTaggingMode.DEFAULT);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        // null tagging mode
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
                    AsnModuleTaggingMode.DEFAULT);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }

    }

    // TODO MJF - add a test for getMatchingChild (which essentially replaced getComponentTypes)
    @Test
    public void testGetMatchingChild() throws ParseException
    {

    }

    @Test
    public void testAutomaticTagGeneration() throws ParseException
    {
        AsnSchemaType type1 = mock(AsnSchemaType.class);
        AsnSchemaType type2 = mock(AsnSchemaType.class);
        AsnSchemaType type3 = mock(AsnSchemaType.class);
        AsnSchemaType type4 = mock(AsnSchemaType.class);

        AsnSchemaComponentType testComponent1 = mock(AsnSchemaComponentType.class);
        when(testComponent1.getType()).thenReturn(type1);
        when(testComponent1.getTag()).thenReturn(null);
        when(testComponent1.getName()).thenReturn("firstName");

        AsnSchemaComponentType testComponent2 = mock(AsnSchemaComponentType.class);
        when(testComponent2.getType()).thenReturn(type2);
        when(testComponent2.getTag()).thenReturn(null);
        when(testComponent2.getName()).thenReturn("lastName");

        AsnSchemaComponentType testComponent3 = mock(AsnSchemaComponentType.class);
        when(testComponent3.getType()).thenReturn(type3);
        when(testComponent3.getTag()).thenReturn("9");
        when(testComponent3.getName()).thenReturn("someComponent");

        AsnSchemaComponentType testComponent4 = mock(AsnSchemaComponentType.class);
        when(testComponent4.getType()).thenReturn(type4);
        when(testComponent4.getTag()).thenReturn(null);
        when(testComponent4.getName()).thenReturn("lastComponent");

        AsnSchemaTypeConstructed testInstance
                = new AsnSchemaTypeConstructed(AsnPrimitiveType.SEQUENCE,
                AsnSchemaConstraint.NULL,
                ImmutableList.of(testComponent1, testComponent2, testComponent3, testComponent4),
                AsnModuleTaggingMode.DEFAULT);

        // TODO MJF - auto tag creation tests can move to the strategy, and getComponentTypes has been deleted
        //        assertEquals(testComponent1, testInstance.getComponentTypes("0"));
        //        assertEquals(testComponent2, testInstance.getComponentTypes("1"));
        //        assertEquals(testComponent3, testInstance.getComponentTypes("9"));
        //        assertEquals(testComponent4, testInstance.getComponentTypes("10"));
    }

    // TODO MJF getMatchingChild

    @Test
    public void testGetAllComponents() throws Exception
    {
        // TODO MJF
        //assertEquals(1, instance.getAllComponents().size());
        //assertEquals(component, instance.getAllComponents().get("0"));
    }
}