package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link BaseAsnSchemaType}
 *
 * @author brightSPARK Labs
 */
public class BaseAsnSchemaTypeTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------


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
    }

    @Test
    public void testGetChildType()
    {
        BaseAsnSchemaType type = new BaseAsnSchemaType(AsnPrimitiveType.INTEGER, AsnSchemaConstraint.NULL);
        assertEquals(AsnSchemaType.NULL, type.getChildType("0"));
    }

    @Test
    public void testGetChildName()
    {
        BaseAsnSchemaType type = new BaseAsnSchemaType(AsnPrimitiveType.INTEGER, AsnSchemaConstraint.NULL);
        assertEquals("", type.getChildName("0"));
    }

}