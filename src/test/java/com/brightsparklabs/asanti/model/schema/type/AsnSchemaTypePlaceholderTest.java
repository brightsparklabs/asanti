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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.text.ParseException;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link AsnSchemaTypePlaceholder}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypePlaceholderTest {
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** the type the instance collection is wrapping */
    private AsnSchemaType indirectType;

    /** the instance that will be used for testing */
    private AsnSchemaTypePlaceholder instance;

    // -------------------------------------------------------------------------
    // SETUP/TEAR-DOWN
    // -------------------------------------------------------------------------

    @Before
    public void setUpBeforeTest() throws Exception {

        indirectType = mock(AsnSchemaType.class);
        // For the sake of testing that the Collection is delegating to the element type make it
        // return testable values

        AsnSchemaConstraint constraint1 = mock(AsnSchemaConstraint.class);
        AsnSchemaConstraint constraint2 = mock(AsnSchemaConstraint.class);

        when(indirectType.getPrimitiveType()).thenReturn(AsnPrimitiveTypes.SEQUENCE);
        when(indirectType.getAllComponents())
                .thenReturn(
                        ImmutableList.of(
                                mock(AsnSchemaComponentType.class),
                                mock(AsnSchemaComponentType.class)));

        AsnSchemaComponentType component = mock(AsnSchemaComponentType.class);

        when(indirectType.getMatchingChild(eq("0[0]"), any(DecodingSession.class)))
                .thenReturn(Optional.of(component));
        when(indirectType.getConstraints()).thenReturn(ImmutableSet.of(constraint1, constraint2));

        instance = new AsnSchemaTypePlaceholder("Module", "Type", AsnSchemaConstraint.NULL);
    }

    @After
    public void validate() {
        // forces Mockito to cause the failure (for verify) on the failing test, rather than the
        // next one!
        validateMockitoUsage();
    }

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaTypePlaceholderConstructorPreconditions() throws Exception {
        // null moduleName
        try {
            new AsnSchemaTypePlaceholder(null, "Type", AsnSchemaConstraint.NULL);
        } catch (final NullPointerException ex) {
            throw new AssertionError("Module name can be null", ex);
        }
        // blank moduleName
        try {
            new AsnSchemaTypePlaceholder("", "Type", AsnSchemaConstraint.NULL);
        } catch (final NullPointerException ex) {
            throw new AssertionError("Module name can be null", ex);
        }

        // null Type
        try {
            new AsnSchemaTypePlaceholder("Module", null, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        } catch (final NullPointerException ex) {
        }

        // blank type
        try {
            new AsnSchemaTypePlaceholder("Module", "", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        } catch (final IllegalArgumentException ex) {
        }
    }

    @Test
    public void testGetModuleName() throws Exception {
        assertEquals("Module", instance.getModuleName());
        Mockito.verifyNoInteractions(indirectType);

        instance.setIndirectType(indirectType);
        assertEquals("Module", instance.getModuleName());
        Mockito.verifyNoInteractions(indirectType);
    }

    @Test
    public void testGetTypeName() throws Exception {
        assertEquals("Type", instance.getTypeName());
        Mockito.verifyNoInteractions(indirectType);

        instance.setIndirectType(indirectType);
        assertEquals("Type", instance.getTypeName());
        Mockito.verifyNoInteractions(indirectType);
    }

    @Test
    public void testSetIndirectType() throws Exception {
        // test that if the placeholder is not resolved it does not delegate
        assertEquals(AsnPrimitiveTypes.INVALID, instance.getPrimitiveType());
        // TODO INC-53
        assertNull(verify(indirectType, never()).getPrimitiveType());
        instance.setIndirectType(indirectType);

        // and that it does delegate after it is resolved
        assertEquals(AsnPrimitiveTypes.SEQUENCE, instance.getPrimitiveType());
        // TODO INC-53
        assertNull(verify(indirectType).getPrimitiveType());
    }

    @Test
    public void testGetConstraints() throws Exception {
        // test that if the placeholder is not resolved it does not delegate
        assertEquals(1, instance.getConstraints().size());
        verify(indirectType, never()).getConstraints();
        instance.setIndirectType(indirectType);

        // and that it does delegate after it is resolved
        assertEquals(3, instance.getConstraints().size());
        verify(indirectType).getConstraints();
    }

    @Test
    public void testGetAllComponents() {
        // test that if the placeholder is not resolved it does not delegate
        assertEquals(0, instance.getAllComponents().size());
        // TODO INC-53
        assertNull(verify(indirectType, never()).getAllComponents());
        instance.setIndirectType(indirectType);

        // and that it does delegate after it is resolved
        assertEquals(2, instance.getAllComponents().size());
        // TODO INC-53
        assertNull(verify(indirectType).getAllComponents());
    }

    @Test
    public void testGetIndirectType() {
        // test that if the placeholder is not resolved it does not delegate
        assertEquals(AsnSchemaType.NULL, instance.getIndirectType());

        instance.setIndirectType(indirectType);

        // and that it does delegate after it is resolved
        assertEquals(indirectType, instance.getIndirectType());

        verifyNoMoreInteractions(indirectType);
    }

    @Test
    public void testGetMatchingChild() {
        // test that if the placeholder is not resolved it does not delegate
        DecodingSession decodingSession = mock(DecodingSession.class);
        assertFalse(instance.getMatchingChild("0[0]", decodingSession).isPresent());
        verify(indirectType, never()).getMatchingChild(anyString(), any(DecodingSession.class));
        instance.setIndirectType(indirectType);

        // and that it does delegate after it is resolved
        assertTrue(instance.getMatchingChild("0[0]", decodingSession).isPresent());
        verify(indirectType).getMatchingChild("0[0]", decodingSession);
    }

    @Test
    public void testVisitor() throws ParseException {
        AsnSchemaTypeVisitor v = BaseAsnSchemaTypeTest.getVisitor();

        Object o = instance.accept(v);
        assertEquals("Got AsnSchemaTypePlaceholder", o);
    }
}
