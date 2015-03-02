/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator;

import static org.junit.Assert.*;

import org.junit.Test;

import com.brightsparklabs.asanti.model.schema.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.AsnSchemaEnumeratedOption;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinition.AsnSchemaTypeDefinitionNull;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionChoice;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionEnumerated;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionIA5String;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionInteger;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionOctetString;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionSequence;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionSequenceOf;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionSet;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionSetOf;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionUTF8String;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionVisibleString;
import com.google.common.collect.ImmutableList;

/**
 * Unit tests for {@link ValidationVisitor}
 *
 * @author brightSPARK Labs
 */
public class ValidationVisitorTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final ValidationVisitor instance = new ValidationVisitor();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testVisitAsnSchemaTypeDefinitionNull()
    {
        final AsnSchemaTypeDefinitionNull visitable = AsnSchemaTypeDefinition.NULL;
        assertEquals(ValidationRule.NULL, instance.visit(visitable));
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionChoice()
    {
        final AsnSchemaTypeDefinitionChoice visitable =
                new AsnSchemaTypeDefinitionChoice("TEST_NAME",
                        ImmutableList.<AsnSchemaComponentType>of(),
                        AsnSchemaConstraint.NULL);
        assertEquals(ValidationRule.NULL, instance.visit(visitable));
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionEnumerated()
    {
        final AsnSchemaTypeDefinitionEnumerated visitable =
                new AsnSchemaTypeDefinitionEnumerated("TEST_NAME", ImmutableList.<AsnSchemaEnumeratedOption>of());
        assertEquals(ValidationRule.NULL, instance.visit(visitable));
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionIA5String()
    {
        final AsnSchemaTypeDefinitionIA5String visitable =
                new AsnSchemaTypeDefinitionIA5String("TEST_NAME", AsnSchemaConstraint.NULL);
        assertEquals(ValidationRule.NULL, instance.visit(visitable));
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionInteger()
    {
        final AsnSchemaTypeDefinitionInteger visitable =
                new AsnSchemaTypeDefinitionInteger("TEST_NAME", AsnSchemaConstraint.NULL);
        assertEquals(ValidationRule.NULL, instance.visit(visitable));
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionOctetString()
    {
        final AsnSchemaTypeDefinitionOctetString visitable =
                new AsnSchemaTypeDefinitionOctetString("TEST_NAME", AsnSchemaConstraint.NULL);
        assertEquals(ValidationRule.NULL, instance.visit(visitable));
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSequence()
    {
        final AsnSchemaTypeDefinitionSequence visitable =
                new AsnSchemaTypeDefinitionSequence("TEST_NAME",
                        ImmutableList.<AsnSchemaComponentType>of(),
                        AsnSchemaConstraint.NULL);
        assertEquals(ValidationRule.NULL, instance.visit(visitable));
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSequenceOf()
    {
        final AsnSchemaTypeDefinitionSequenceOf visitable =
                new AsnSchemaTypeDefinitionSequenceOf("TEST_NAME", "TEST_TYPE", AsnSchemaConstraint.NULL);
        assertEquals(ValidationRule.NULL, instance.visit(visitable));
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSet()
    {
        final AsnSchemaTypeDefinitionSet visitable =
                new AsnSchemaTypeDefinitionSet("TEST_NAME",
                        ImmutableList.<AsnSchemaComponentType>of(),
                        AsnSchemaConstraint.NULL);
        assertEquals(ValidationRule.NULL, instance.visit(visitable));
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSetOf()
    {
        final AsnSchemaTypeDefinitionSetOf visitable =
                new AsnSchemaTypeDefinitionSetOf("TEST_NAME", "TEST_TYPE", AsnSchemaConstraint.NULL);
        assertEquals(ValidationRule.NULL, instance.visit(visitable));
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionUTF8String()
    {
        final AsnSchemaTypeDefinitionUTF8String visitable =
                new AsnSchemaTypeDefinitionUTF8String("TEST_NAME", AsnSchemaConstraint.NULL);
        assertEquals(ValidationRule.NULL, instance.visit(visitable));
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionVisibleString()
    {
        final AsnSchemaTypeDefinitionVisibleString visitable =
                new AsnSchemaTypeDefinitionVisibleString("TEST_NAME", AsnSchemaConstraint.NULL);
        assertEquals(ValidationRule.NULL, instance.visit(visitable));
    }
}
