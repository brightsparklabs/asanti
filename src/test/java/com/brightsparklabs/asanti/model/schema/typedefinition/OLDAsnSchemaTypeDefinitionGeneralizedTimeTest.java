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
 * Unit tests for {@link OLDAsnSchemaTypeDefinitionGeneralizedTime}
 *
 * @author brightSPARK Labs
 */
public class OLDAsnSchemaTypeDefinitionGeneralizedTimeTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaTypeDefinitionGeneralizedTime() throws Exception
    {
        // test valid
        new OLDAsnSchemaTypeDefinitionGeneralizedTime("TEST_NAME", AsnSchemaConstraint.NULL);

        // test null
        try
        {
            new OLDAsnSchemaTypeDefinitionGeneralizedTime(null, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new OLDAsnSchemaTypeDefinitionGeneralizedTime("TEST_NAME", null);
        }
        catch (final NullPointerException ex)
        {
            fail("NullPointerException thrown");
        }

        // test empty
        try
        {
            new OLDAsnSchemaTypeDefinitionGeneralizedTime("", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new OLDAsnSchemaTypeDefinitionGeneralizedTime(" ", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetBuiltinType() throws Exception
    {
        final OLDAsnSchemaTypeDefinitionGeneralizedTime instance =
                new OLDAsnSchemaTypeDefinitionGeneralizedTime("TEST_NAME", AsnSchemaConstraint.NULL);
        assertEquals(AsnBuiltinType.GeneralizedTime, instance.getBuiltinType());
    }

    @Test
    public void testVisit() throws Exception
    {
        final OLDAsnSchemaTypeDefinitionGeneralizedTime instance =
                new OLDAsnSchemaTypeDefinitionGeneralizedTime("TEST_NAME", AsnSchemaConstraint.NULL);
        assertEquals(OLDAsnSchemaTypeDefinitionGeneralizedTime.class.getCanonicalName(),
                instance.visit(MockAsnSchemaTypeDefinitionVisitor.getInstance()));
    }
}
