/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.type;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.brightsparklabs.asanti.model.schema.DecodingSession;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveTypes;
import com.brightsparklabs.assam.schema.AsnBuiltinType;
import java.text.ParseException;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link AsnSchemaTypeCollection}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeCollectionTest {
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
    public void setUpBeforeTest() throws Exception {
        AsnSchemaComponentType component = mock(AsnSchemaComponentType.class);

        wrappedSequence = mock(AsnSchemaType.class);
        // For the sake of testing that the Collection is delegating to the element type make it
        // return testable values
        when(wrappedSequence.getPrimitiveType()).thenReturn(AsnPrimitiveTypes.SEQUENCE);
        when(wrappedSequence.getBuiltinType()).thenReturn(AsnBuiltinType.Sequence);
        when(wrappedSequence.getMatchingChild(anyString(), any(DecodingSession.class)))
                .thenReturn(Optional.empty());
        when(wrappedSequence.getMatchingChild(eq("0[0]"), any(DecodingSession.class)))
                .thenReturn(Optional.of(component));

        instance =
                new AsnSchemaTypeCollection(
                        AsnPrimitiveTypes.SEQUENCE_OF, AsnSchemaConstraint.NULL, wrappedSequence);

        // instance.performTagging();
    }

    @After
    public void validate() {
        // forces Mockito to cause the failure (for the call to verify) on the failing test, rather
        // than the next one!
        validateMockitoUsage();
    }

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaTypeCollectionConstructorPreconditions() throws Exception {

        try {
            new AsnSchemaTypeCollection(null, AsnSchemaConstraint.NULL, AsnSchemaType.NULL);
            fail("NullPointerException not thrown");
        } catch (final NullPointerException ex) {
        }

        try {
            new AsnSchemaTypeCollection(
                    AsnPrimitiveTypes.SEQUENCE_OF, AsnSchemaConstraint.NULL, null);
            fail("NullPointerException not thrown");
        } catch (final NullPointerException ex) {
        }

        // check we can't create one for non-collection types.
        try {
            new AsnSchemaTypeCollection(
                    AsnPrimitiveTypes.IA5_STRING, AsnSchemaConstraint.NULL, AsnSchemaType.NULL);
            fail("IllegalArgumentException not thrown");
        } catch (final IllegalArgumentException ex) {
        }

        AsnSchemaType wrappedInteger = mock(AsnSchemaType.class);
        when(wrappedInteger.getPrimitiveType()).thenReturn(AsnPrimitiveTypes.INTEGER);

        AsnSchemaTypeCollection collection =
                new AsnSchemaTypeCollection(
                        AsnPrimitiveTypes.SEQUENCE_OF, AsnSchemaConstraint.NULL, wrappedInteger);

        assertEquals(AsnBuiltinType.SequenceOf, collection.getBuiltinType());
    }

    @Test
    public void testGetPrimitiveType() throws Exception {
        assertEquals(AsnPrimitiveTypes.SEQUENCE_OF, instance.getPrimitiveType());
        // TODO INC-53
        assertNull(verify(wrappedSequence, never()).getPrimitiveType());
    }

    @Test
    public void testGetMatchingChild() {
        instance.performTagging();

        final DecodingSession decodingSession = mock(DecodingSession.class);
        Optional<AsnSchemaComponentType> result =
                instance.getMatchingChild("0[UNIVERSAL 16]", decodingSession);

        assertTrue(result.isPresent());
        assertEquals("[0]", result.get().getName());

        result = instance.getMatchingChild("77[UNIVERSAL 16]", decodingSession);
        assertEquals("[77]", result.get().getName());

        // Something that does not match
        result = instance.getMatchingChild("0[UNIVERSAL 2]", decodingSession);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetMatchingChildChoice() {
        AsnSchemaComponentType component = mock(AsnSchemaComponentType.class);
        AsnSchemaType type = mock(AsnSchemaType.class);
        when(component.getName()).thenReturn("a");
        when(component.getType()).thenReturn(type);

        DecodingSession decodingSession = mock(DecodingSession.class);

        AsnSchemaType wrappedChoice = mock(AsnSchemaType.class);
        when(wrappedChoice.getPrimitiveType()).thenReturn(AsnPrimitiveTypes.CHOICE);
        when(wrappedChoice.getBuiltinType()).thenReturn(AsnBuiltinType.Choice);
        when(wrappedChoice.getMatchingChild(anyString(), any(DecodingSession.class)))
                .thenReturn(Optional.empty());
        when(wrappedChoice.getMatchingChild(eq("0[0]"), any(DecodingSession.class)))
                .thenReturn(Optional.of(component));

        AsnSchemaTypeCollection collection =
                new AsnSchemaTypeCollection(
                        AsnPrimitiveTypes.SEQUENCE_OF, AsnSchemaConstraint.NULL, wrappedChoice);

        collection.performTagging();

        Optional<AsnSchemaComponentType> result =
                collection.getMatchingChild("0[0]", decodingSession);

        assertTrue(result.isPresent());

        // verify that the call was delegated
        verify(wrappedChoice).getMatchingChild(eq("0[0]"), any(DecodingSession.class));

        // verify that the name gets qualified appropriately, and that the type is correct
        assertEquals("[0]/a", result.get().getName());
        assertEquals(type, result.get().getType());

        // something that does not match
        result = collection.getMatchingChild("0[44]", decodingSession);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetElementType() throws Exception {
        assertEquals(wrappedSequence, instance.getElementType());
        verifyZeroInteractions(wrappedSequence);
    }

    @Test
    public void testVisitor() throws ParseException {
        AsnSchemaTypeVisitor v = BaseAsnSchemaTypeTest.getVisitor();

        Object o = instance.accept(v);
        assertEquals("Got AsnSchemaTypeCollection", o);
    }
}
