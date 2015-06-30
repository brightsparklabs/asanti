package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link AsnSchemaTypeCollection}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeCollectionTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** the type the instance collection is wrapping */
    private AsnSchemaType wrappedSequence;

    /** the instance that will be used for testing */
    private AsnSchemaTypeCollection instance;

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
        when(wrappedSequence.getChildType("0")).thenReturn(sequenceComponent);
        when(wrappedSequence.getChildName("0")).thenReturn("foo");

        instance = new AsnSchemaTypeCollection(AsnPrimitiveType.SEQUENCE_OF, AsnSchemaConstraint.NULL,
                wrappedSequence);
    }

    @After
    public void validate()
    {
        // forces Mockito to cause the failure  (for verify) on the failing test, rather than the next one!
        validateMockitoUsage();
    }

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaTypeCollectionConstructorPreconditions() throws Exception
    {

        try
        {
            new AsnSchemaTypeCollection(null, AsnSchemaConstraint.NULL, AsnSchemaType.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        try
        {
            new AsnSchemaTypeCollection(AsnPrimitiveType.SEQUENCE_OF, AsnSchemaConstraint.NULL, null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // check we can't create one for non-collection types.
        try
        {
            new AsnSchemaTypeCollection(AsnPrimitiveType.SEQUENCE, AsnSchemaConstraint.NULL,
                    AsnSchemaType.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }

        AsnSchemaType wrappedInteger = mock(AsnSchemaType.class);
        when(wrappedInteger.getPrimitiveType()).thenReturn(AsnPrimitiveType.INTEGER);

        AsnSchemaTypeCollection collection = new AsnSchemaTypeCollection(AsnPrimitiveType.SEQUENCE_OF, AsnSchemaConstraint.NULL,
                wrappedInteger);

        // TODO ASN-140.  We currently get back the "wrapped" type, not the collection type.
        assertEquals(AsnBuiltinType.Integer, collection.getBuiltinType());
    }

    @Test
    public void testGetPrimitiveType() throws Exception
    {
        // TODO ASN-140.  We currently get back the "wrapped" type, not the collection type.
        assertEquals(AsnPrimitiveType.SEQUENCE, instance.getPrimitiveType());
        verify(wrappedSequence).getPrimitiveType();
    }

    @Test
    public void testGetChildType() throws Exception
    {
        assertFalse(null == instance.getChildType("0"));
        verify(wrappedSequence).getChildType("0");
    }

    @Test
    public void testGetChildName() throws Exception
    {
        assertEquals("foo", instance.getChildName("0"));
        verify(wrappedSequence).getChildName("0");
    }

    @Test
    public void testGetElementType() throws Exception
    {
        assertEquals(wrappedSequence, instance.getElementType());
        verifyZeroInteractions(wrappedSequence);
    }
}