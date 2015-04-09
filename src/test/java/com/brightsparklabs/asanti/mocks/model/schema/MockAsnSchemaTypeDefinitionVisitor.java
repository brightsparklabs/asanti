/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.mocks.model.schema;

import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionBitString;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionChoice;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionEnumerated;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionGeneralString;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionIA5String;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionInteger;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionNumericString;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionOctetString;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionSequence;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionSequenceOf;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionSet;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionSetOf;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionUTF8String;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionVisibleString;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionVisitor;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition.Null;

/**
 * A simple visitor which returns the canonical class name of the object it
 * visits
 *
 * @author brightSPARK Labs
 */
public class MockAsnSchemaTypeDefinitionVisitor implements AsnSchemaTypeDefinitionVisitor<String>
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static final MockAsnSchemaTypeDefinitionVisitor instance = new MockAsnSchemaTypeDefinitionVisitor();

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------
    /**
     * Default constructor. This is private, use {@link #getInstance()} instead.
     */
    private MockAsnSchemaTypeDefinitionVisitor()
    {
        // private constructor
    }

    /**
     * Returns a singleton instance
     *
     * @return a singleton instance
     */
    public static MockAsnSchemaTypeDefinitionVisitor getInstance()
    {
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaTypeDefinitionVisitor
    // -------------------------------------------------------------------------

    @Override
    public String visit(Null visitable)
    {
        return visitable.getClass()
                .getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionBitString visitable)
    {
        return visitable.getClass()
                .getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionChoice visitable)
    {
        return visitable.getClass()
                .getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionEnumerated visitable)
    {
        return visitable.getClass()
                .getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionIA5String visitable)
    {
        return visitable.getClass()
                .getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionInteger visitable)
    {
        return visitable.getClass()
                .getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionNumericString visitable)
    {
        return visitable.getClass()
                .getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionOctetString visitable)
    {
        return visitable.getClass()
                .getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionSequence visitable)
    {
        return visitable.getClass()
                .getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionSequenceOf visitable)
    {
        return visitable.getClass()
                .getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionSet visitable)
    {
        return visitable.getClass()
                .getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionSetOf visitable)
    {
        return visitable.getClass()
                .getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionUTF8String visitable)
    {
        return visitable.getClass()
                .getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionVisibleString visitable)
    {
        return visitable.getClass()
                .getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionGeneralString visitable)
    {
        return visitable.getClass()
                .getCanonicalName();
    }
}
