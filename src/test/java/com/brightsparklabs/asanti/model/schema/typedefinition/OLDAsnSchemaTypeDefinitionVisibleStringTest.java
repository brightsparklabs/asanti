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
 * Unit tests for {@link OLDAsnSchemaTypeDefinitionVisibleString}
 *
 * @author brightSPARK Labs
 */
public class OLDAsnSchemaTypeDefinitionVisibleStringTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaTypeDefinitionVisibleString() throws Exception
    {
        // test valid
        new OLDAsnSchemaTypeDefinitionVisibleString("TEST_NAME", AsnSchemaConstraint.NULL);

        // test null
        try
        {
            new OLDAsnSchemaTypeDefinitionVisibleString(null, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new OLDAsnSchemaTypeDefinitionVisibleString("TEST_NAME", null);
        }
        catch (final NullPointerException ex)
        {
            fail("NullPointerException thrown");
        }

        // test empty
        try
        {
            new OLDAsnSchemaTypeDefinitionVisibleString("", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new OLDAsnSchemaTypeDefinitionVisibleString(" ", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetBuiltinType() throws Exception
    {
        final OLDAsnSchemaTypeDefinitionVisibleString instance =
                new OLDAsnSchemaTypeDefinitionVisibleString("TEST_NAME", AsnSchemaConstraint.NULL);
        assertEquals(AsnBuiltinType.VisibleString, instance.getBuiltinType());
    }

    @Test
    public void testVisit() throws Exception
    {
        final OLDAsnSchemaTypeDefinitionVisibleString instance =
                new OLDAsnSchemaTypeDefinitionVisibleString("TEST_NAME", AsnSchemaConstraint.NULL);
        assertEquals(OLDAsnSchemaTypeDefinitionVisibleString.class.getCanonicalName(),
                instance.visit(MockAsnSchemaTypeDefinitionVisitor.getInstance()));
    }
}
