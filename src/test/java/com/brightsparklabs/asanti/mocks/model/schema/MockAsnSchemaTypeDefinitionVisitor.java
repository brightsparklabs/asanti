/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.mocks.model.schema;

import com.brightsparklabs.asanti.model.schema.primitive.*;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnPrimitiveTypeVisitor;

/**
 * A simple visitor which returns the canonical class name of the object it visits
 *
 * @author brightSPARK Labs
 */
public class MockAsnSchemaTypeDefinitionVisitor implements AsnPrimitiveTypeVisitor<String>
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
    // IMPLEMENTATION: AsnPrimitiveTypeVisitor
    // -------------------------------------------------------------------------

    @Override
    public String visit(AsnPrimitiveType.Null visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeBitString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeBoolean visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeChoice visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeEnumerated visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeIA5String visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeInteger visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeNumericString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeOctetString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeSequence visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeSequenceOf visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeSet visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeSetOf visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeUtcTime visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeUtf8String visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeVisibleString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypePrintableString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeGeneralizedTime visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeGeneralString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeOid visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeRelativeOid visitable)
    {
        return visitable.getClass().getCanonicalName();
    }
}
