/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema.typedefinition;

import static org.junit.Assert.*;

import org.junit.Test;

import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaTypeDefinitionVisitor;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;

/**
 * Unit tests for {@link OLDAsnSchemaTypeDefinitionNumericString}
 *
 * @author brightSPARK Labs
 */
public class OLDAsnSchemaTypeDefinitionNumericStringTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaTypeDefinitionNumericString() throws Exception
    {
        // test valid
        new OLDAsnSchemaTypeDefinitionNumericString("TEST_NAME", AsnSchemaConstraint.NULL);

        // test null
        try
        {
            new OLDAsnSchemaTypeDefinitionNumericString(null, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new OLDAsnSchemaTypeDefinitionNumericString("TEST_NAME", null);
        }
        catch (final NullPointerException ex)
        {
            fail("NullPointerException thrown");
        }

        // test empty
        try
        {
            new OLDAsnSchemaTypeDefinitionNumericString("", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new OLDAsnSchemaTypeDefinitionNumericString(" ", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetBuiltinType() throws Exception
    {
        final OLDAsnSchemaTypeDefinitionNumericString instance =
                new OLDAsnSchemaTypeDefinitionNumericString("TEST_NAME", AsnSchemaConstraint.NULL);
        assertEquals(AsnBuiltinType.NumericString, instance.getBuiltinType());
    }

    @Test
    public void testVisit() throws Exception
    {
        final OLDAsnSchemaTypeDefinitionNumericString instance =
                new OLDAsnSchemaTypeDefinitionNumericString("TEST_NAME", AsnSchemaConstraint.NULL);
        assertEquals(OLDAsnSchemaTypeDefinitionNumericString.class.getCanonicalName(),
                instance.visit(MockAsnSchemaTypeDefinitionVisitor.getInstance()));
    }
}
