package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
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

    /** the instance that will be used for testing */
    private AsnSchemaTypeConstructed instance;

    // -------------------------------------------------------------------------
    // SETUP/TEAR-DOWN
    // -------------------------------------------------------------------------

    @Before
    public void setUpBeforeTest() throws Exception
    {
        AsnSchemaType sequenceComponent = mock(AsnSchemaType.class);

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
        when(component.getTagName()).thenReturn("name");

        instance = new AsnSchemaTypeConstructed(AsnPrimitiveType.SEQUENCE, AsnSchemaConstraint.NULL,
                ImmutableSet.of(component));
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
            new AsnSchemaTypeConstructed(null, AsnSchemaConstraint.NULL, ImmutableSet.<AsnSchemaComponentType>of());
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        // null components
        try
        {
            new AsnSchemaTypeConstructed(AsnPrimitiveType.SEQUENCE, AsnSchemaConstraint.NULL, null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // non Constructed type
        try
        {
            new AsnSchemaTypeConstructed(AsnPrimitiveType.INTEGER, AsnSchemaConstraint.NULL, ImmutableSet.<AsnSchemaComponentType>of());
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }



    }

    // TODO MJF - add a test for getMatchingChild (which essentially replaced getComponent)

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
        when(testComponent1.getTagName()).thenReturn("firstName");

        AsnSchemaComponentType testComponent2 = mock(AsnSchemaComponentType.class);
        when(testComponent2.getType()).thenReturn(type2);
        when(testComponent2.getTag()).thenReturn(null);
        when(testComponent2.getTagName()).thenReturn("lastName");

        AsnSchemaComponentType testComponent3 = mock(AsnSchemaComponentType.class);
        when(testComponent3.getType()).thenReturn(type3);
        when(testComponent3.getTag()).thenReturn("9");
        when(testComponent3.getTagName()).thenReturn("someComponent");

        AsnSchemaComponentType testComponent4 = mock(AsnSchemaComponentType.class);
        when(testComponent4.getType()).thenReturn(type4);
        when(testComponent4.getTag()).thenReturn(null);
        when(testComponent4.getTagName()).thenReturn("lastComponent");


        AsnSchemaTypeConstructed testInstance = new AsnSchemaTypeConstructed(AsnPrimitiveType.SEQUENCE, AsnSchemaConstraint.NULL,
                ImmutableSet.of(testComponent1, testComponent2, testComponent3, testComponent4));

        // TODO MJF - auto tag creation tests can move to the strategy, and getComponent has been deleted
//        assertEquals(testComponent1, testInstance.getComponent("0"));
//        assertEquals(testComponent2, testInstance.getComponent("1"));
//        assertEquals(testComponent3, testInstance.getComponent("9"));
//        assertEquals(testComponent4, testInstance.getComponent("10"));
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