/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.mocks.model.schema;

import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition.Null;
import com.brightsparklabs.asanti.model.schema.typedefinition.*;

/**
 * A simple visitor which returns the canonical class name of the object it visits
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
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionBitString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionChoice visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionEnumerated visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionIa5String visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionInteger visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionNumericString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionOctetString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionSequence visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionSequenceOf visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionSet visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionSetOf visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionUtf8String visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionVisibleString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTypeDefinitionGeneralString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }
}
