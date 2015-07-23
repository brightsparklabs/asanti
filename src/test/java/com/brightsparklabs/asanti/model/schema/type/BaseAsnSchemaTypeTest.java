package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.AsnModuleTaggingMode;
import com.brightsparklabs.asanti.model.schema.DecodedTag;
import com.brightsparklabs.asanti.model.schema.DecodingSession;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link BaseAsnSchemaType}
 *
 * @author brightSPARK Labs
 */
public class BaseAsnSchemaTypeTest
{

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaType() throws Exception
    {
        try
        {
            new BaseAsnSchemaType(null, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        BaseAsnSchemaType type = new BaseAsnSchemaType(AsnPrimitiveType.NULL, AsnSchemaConstraint.NULL);
        assertEquals(AsnPrimitiveType.NULL, type.getPrimitiveType());

        type = new BaseAsnSchemaType(AsnPrimitiveType.INTEGER, AsnSchemaConstraint.NULL);
        assertEquals(AsnPrimitiveType.INTEGER, type.getPrimitiveType());
        assertEquals(AsnBuiltinType.Integer, type.getBuiltinType());

        DecodingSession decodingSession = mock(DecodingSession.class);
        assertFalse(type.getMatchingChild("", decodingSession).isPresent());
        verifyNoMoreInteractions(decodingSession);
    }

    @Test
    public void testGetAllComponents()
    {
        BaseAsnSchemaType type = new BaseAsnSchemaType(AsnPrimitiveType.INTEGER, AsnSchemaConstraint.NULL);

        assertEquals(0, type.getAllComponents().size());
    }

    @Test
    public void testVisitor() throws ParseException
    {
        AsnSchemaTypeVisitor v = getVisitor();

        BaseAsnSchemaType instance = new BaseAsnSchemaType(AsnPrimitiveType.INTEGER, AsnSchemaConstraint.NULL);

        Object o = instance.accept(v);
        assertEquals("Got BaseAsnSchemaType", o);
    }

    /**
     *
     * @return a helper/test Visitor to determine what Type was visited
     */
    public static AsnSchemaTypeVisitor getVisitor()
    {
        return new AsnSchemaTypeVisitor<String>() {
            @Override
            public String visit(final AsnSchemaTypeConstructed visitable) throws ParseException
            {
                return "Got AsnSchemaTypeConstructed";
            }

            @Override
            public String visit(final BaseAsnSchemaType visitable) throws ParseException
            {
                return "Got BaseAsnSchemaType";
            }

            @Override
            public String visit(final AsnSchemaTypeCollection visitable) throws ParseException
            {
                return "Got AsnSchemaTypeCollection";
            }

            @Override
            public String visit(final AsnSchemaTypeWithNamedTags visitable) throws ParseException
            {
                return "Got AsnSchemaTypeWithNamedTags";
            }

            @Override
            public String visit(final AsnSchemaTypePlaceholder visitable) throws ParseException
            {
                return "Got AsnSchemaTypePlaceholder";
            }

            @Override
            public String visit(final AsnSchemaType.Null visitable) throws ParseException
            {
                return "Got AsnSchemaType.Null";
            }
        };
    }

}