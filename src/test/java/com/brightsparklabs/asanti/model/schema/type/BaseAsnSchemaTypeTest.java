package com.brightsparklabs.asanti.model.schema.type;

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
    public void testAsnSchemaTypeDefinition() throws Exception
    {
        try
        {
            BaseAsnSchemaType type = new BaseAsnSchemaType(null, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }


        BaseAsnSchemaType type = new BaseAsnSchemaType(AsnPrimitiveType.NULL, AsnSchemaConstraint.NULL);
        assertEquals(AsnPrimitiveType.NULL, type.getPrimitiveType());

    }



}