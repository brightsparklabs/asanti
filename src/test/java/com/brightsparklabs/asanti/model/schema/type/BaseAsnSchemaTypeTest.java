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
import com.brightsparklabs.asanti.schema.AsnBuiltinType;
import java.text.ParseException;
import org.junit.Test;

/**
 * Unit tests for {@link AbstractAsnSchemaType}
 *
 * @author brightSPARK Labs
 */
public class BaseAsnSchemaTypeTest {

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaType() throws Exception {
        try {
            new AsnSchemaTypePrimitive(null, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        } catch (final NullPointerException ex) {
        }
        try {
            new AsnSchemaTypePrimitiveAliased(null, AsnSchemaConstraint.NULL, "", "");
            fail("NullPointerException not thrown");
        } catch (final NullPointerException ex) {
        }
        try {
            new AsnSchemaTypePrimitiveAliased(
                    AsnPrimitiveTypes.OCTET_STRING, AsnSchemaConstraint.NULL, null, "");
            fail("NullPointerException not thrown");
        } catch (final NullPointerException ex) {
        }
        try {
            new AsnSchemaTypePrimitiveAliased(
                    AsnPrimitiveTypes.OCTET_STRING, AsnSchemaConstraint.NULL, "", null);
            fail("NullPointerException not thrown");
        } catch (final NullPointerException ex) {
        }
        AbstractAsnSchemaType type =
                new AsnSchemaTypePrimitive(AsnPrimitiveTypes.INVALID, AsnSchemaConstraint.NULL);
        assertEquals(AsnPrimitiveTypes.INVALID, type.getPrimitiveType());

        type = new AsnSchemaTypePrimitive(AsnPrimitiveTypes.INTEGER, AsnSchemaConstraint.NULL);
        assertEquals(AsnPrimitiveTypes.INTEGER, type.getPrimitiveType());
        assertEquals(AsnBuiltinType.Integer, type.getBuiltinType());

        DecodingSession decodingSession = mock(DecodingSession.class);
        assertFalse(type.getMatchingChild("", decodingSession).isPresent());
        verifyNoMoreInteractions(decodingSession);
    }

    @Test
    public void testGetAllComponents() {
        AbstractAsnSchemaType type =
                new AsnSchemaTypePrimitive(AsnPrimitiveTypes.INTEGER, AsnSchemaConstraint.NULL);

        assertEquals(0, type.getAllComponents().size());
    }

    @Test
    public void testVisitor() throws ParseException {
        AsnSchemaTypeVisitor<String> v = getVisitor();

        AbstractAsnSchemaType instance =
                new AsnSchemaTypePrimitive(AsnPrimitiveTypes.INTEGER, AsnSchemaConstraint.NULL);

        Object o = instance.accept(v);
        assertEquals("Got AsnSchemaTypePrimitive", o);
    }

    /** @return a helper/test Visitor to determine what Type was visited */
    public static AsnSchemaTypeVisitor<String> getVisitor() {
        return new AsnSchemaTypeVisitor<>() {
            @Override
            public String visit(final AsnSchemaTypeConstructed visitable) throws ParseException {
                return "Got AsnSchemaTypeConstructed";
            }

            @Override
            public String visit(final AsnSchemaTypePrimitive visitable) throws ParseException {
                return "Got AsnSchemaTypePrimitive";
            }

            @Override
            public String visit(final AsnSchemaTypePrimitiveAliased visitable)
                    throws ParseException {
                return "Got AsnSchemaTypePrimitiveAliased";
            }

            @Override
            public String visit(final AsnSchemaTypeCollection visitable) throws ParseException {
                return "Got AsnSchemaTypeCollection";
            }

            @Override
            public String visit(final AsnSchemaTypeWithNamedTags visitable) throws ParseException {
                return "Got AsnSchemaTypeWithNamedTags";
            }

            @Override
            public String visit(final AsnSchemaTypePlaceholder visitable) throws ParseException {
                return "Got AsnSchemaTypePlaceholder";
            }

            @Override
            public String visit(final AsnSchemaType.Null visitable) throws ParseException {
                return "Got AsnSchemaType.Null";
            }
        };
    }
}
