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
import com.brightsparklabs.asanti.model.schema.typedefinition.OLDAsnSchemaTypeDefinition.Null;

public class OLDAsnSchemaTypeDefinitionNullInstanceTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testGetName() throws Exception
    {
        final OLDAsnSchemaTypeDefinition.Null instance = OLDAsnSchemaTypeDefinition.NULL;
        assertEquals("NULL", instance.getName());
    }
/* TODO MJF
    @Test
    public void testGetBuiltinType() throws Exception
    {
        final NULL instance = OLDAsnSchemaTypeDefinition.NULL;
        assertEquals(AsnBuiltinType.NULL, instance.getBuiltinType());
    }

    @Test
    public void testGetConstraint() throws Exception
    {
        final OLDAsnSchemaTypeDefinition.NULL instance = OLDAsnSchemaTypeDefinition.NULL;
        assertEquals(AsnSchemaConstraint.NULL, instance.getConstraint());
    }

    @Test
    public void testGetTagName() throws Exception
    {
        final OLDAsnSchemaTypeDefinition.NULL instance = OLDAsnSchemaTypeDefinition.NULL;
        assertEquals("", instance.getTagName("0"));
        assertEquals("", instance.getTagName("1"));
        assertEquals("", instance.getTagName("2"));
        assertEquals("", instance.getTagName("5"));
        assertEquals("", instance.getTagName("10"));
    }

    @Test
    public void testGetTypeName() throws Exception
    {
        final OLDAsnSchemaTypeDefinition.NULL instance = OLDAsnSchemaTypeDefinition.NULL;
        assertEquals("", instance.getTypeName("0"));
        assertEquals("", instance.getTypeName("1"));
        assertEquals("", instance.getTypeName("2"));
        assertEquals("", instance.getTypeName("5"));
        assertEquals("", instance.getTypeName("10"));
    }

    @Test
    public void testVisit() throws Exception
    {
        final OLDAsnSchemaTypeDefinition.NULL instance = OLDAsnSchemaTypeDefinition.NULL;
        assertEquals(OLDAsnSchemaTypeDefinition.NULL.class.getCanonicalName(),
                instance.visit(MockAsnSchemaTypeDefinitionVisitor.getInstance()));
    }
*/
}
