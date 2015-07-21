package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.DecodedTag;
import com.brightsparklabs.asanti.model.schema.DecodingSession;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import org.junit.Test;

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
}