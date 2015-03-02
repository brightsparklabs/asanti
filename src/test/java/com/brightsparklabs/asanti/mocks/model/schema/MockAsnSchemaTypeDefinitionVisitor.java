/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.mocks.model.schema;

import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinition.AsnSchemaTypeDefinitionNull;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionBitString;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionChoice;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionEnumerated;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionIA5String;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionInteger;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionNumericString;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionOctetString;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionSequence;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionSequenceOf;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionSet;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionSetOf;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionUTF8String;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionVisitor;

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
    public String visit(AsnSchemaTypeDefinitionNull visitable)
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
}
