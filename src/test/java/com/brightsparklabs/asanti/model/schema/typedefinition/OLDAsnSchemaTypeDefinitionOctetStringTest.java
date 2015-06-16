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
 * Unit tests for {@link OLDAsnSchemaTypeDefinitionOctetString}
 *
 * @author brightSPARK Labs
 */
public class OLDAsnSchemaTypeDefinitionOctetStringTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaTypeDefinitionOctetString() throws Exception
    {
        // test valid
        new OLDAsnSchemaTypeDefinitionOctetString("TEST_NAME", AsnSchemaConstraint.NULL);
        new OLDAsnSchemaTypeDefinitionOctetString("TEST_NAME", AsnSchemaConstraint.NULL);

        // test null
        try
        {
            new OLDAsnSchemaTypeDefinitionOctetString(null, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new OLDAsnSchemaTypeDefinitionOctetString("TEST_NAME", null);
        }
        catch (final NullPointerException ex)
        {
            fail("NullPointerException thrown");
        }

        // test empty
        try
        {
            new OLDAsnSchemaTypeDefinitionOctetString("", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new OLDAsnSchemaTypeDefinitionOctetString(" ", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetBuiltinType() throws Exception
    {
        final OLDAsnSchemaTypeDefinitionOctetString instance =
                new OLDAsnSchemaTypeDefinitionOctetString("TEST_NAME", AsnSchemaConstraint.NULL);
        assertEquals(AsnBuiltinType.OctetString, instance.getBuiltinType());
    }

    @Test
    public void testVisit() throws Exception
    {
        final OLDAsnSchemaTypeDefinitionOctetString instance =
                new OLDAsnSchemaTypeDefinitionOctetString("TEST_NAME", AsnSchemaConstraint.NULL);
        assertEquals(OLDAsnSchemaTypeDefinitionOctetString.class.getCanonicalName(),
                instance.visit(MockAsnSchemaTypeDefinitionVisitor.getInstance()));
    }
}
