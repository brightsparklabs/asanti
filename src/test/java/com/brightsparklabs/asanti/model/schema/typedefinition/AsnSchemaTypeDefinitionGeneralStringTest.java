/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema.typedefinition;

import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaTypeDefinitionVisitor;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link AsnSchemaTypeDefinitionGeneralString}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionGeneralStringTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaTypeDefinitionGeneralString() throws Exception
    {
        // test valid
        new AsnSchemaTypeDefinitionGeneralString("TEST_NAME", AsnSchemaConstraint.NULL);

        // test null
        try
        {
            new AsnSchemaTypeDefinitionGeneralString(null, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinitionGeneralString("TEST_NAME", null);
        }
        catch (final NullPointerException ex)
        {
            fail("NullPointerException thrown");
        }

        // test empty
        try
        {
            new AsnSchemaTypeDefinitionGeneralString("", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinitionGeneralString(" ", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetBuiltinType() throws Exception
    {
        final AsnSchemaTypeDefinitionGeneralString instance =
                new AsnSchemaTypeDefinitionGeneralString("TEST_NAME", AsnSchemaConstraint.NULL);
        assertEquals(AsnBuiltinType.GeneralString, instance.getBuiltinType());
    }

    @Test
    public void testVisit() throws Exception
    {
        final AsnSchemaTypeDefinitionGeneralString instance =
                new AsnSchemaTypeDefinitionGeneralString("TEST_NAME", AsnSchemaConstraint.NULL);
        assertEquals(AsnSchemaTypeDefinitionGeneralString.class.getCanonicalName(),
                instance.visit(MockAsnSchemaTypeDefinitionVisitor.getInstance()));
    }
}
