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
 * Unit tests for {@link OLDAsnSchemaTypeDefinitionUtf8String}
 *
 * @author brightSPARK Labs
 */
public class OLDAsnSchemaTypeDefinitionUTF8StringTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaTypeDefinitionUTF8String() throws Exception
    {
        // test valid
        new OLDAsnSchemaTypeDefinitionUtf8String("TEST_NAME", AsnSchemaConstraint.NULL);
        new OLDAsnSchemaTypeDefinitionUtf8String("TEST_NAME", AsnSchemaConstraint.NULL);

        // test null
        try
        {
            new OLDAsnSchemaTypeDefinitionUtf8String(null, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new OLDAsnSchemaTypeDefinitionUtf8String("TEST_NAME", null);
        }
        catch (final NullPointerException ex)
        {
            fail("NullPointerException thrown");
        }

        // test empty
        try
        {
            new OLDAsnSchemaTypeDefinitionUtf8String("", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new OLDAsnSchemaTypeDefinitionUtf8String(" ", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetBuiltinType() throws Exception
    {
        final OLDAsnSchemaTypeDefinitionUtf8String instance =
                new OLDAsnSchemaTypeDefinitionUtf8String("TEST_NAME", AsnSchemaConstraint.NULL);
        assertEquals(AsnBuiltinType.Utf8String, instance.getBuiltinType());
    }

    @Test
    public void testVisit() throws Exception
    {
        final OLDAsnSchemaTypeDefinitionUtf8String instance =
                new OLDAsnSchemaTypeDefinitionUtf8String("TEST_NAME", AsnSchemaConstraint.NULL);
        assertEquals(OLDAsnSchemaTypeDefinitionUtf8String.class.getCanonicalName(),
                instance.visit(MockAsnSchemaTypeDefinitionVisitor.getInstance()));
    }
}
