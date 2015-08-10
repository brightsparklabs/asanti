/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.mocks.model.schema;

import com.brightsparklabs.assam.schema.AsnPrimitiveType;
import com.brightsparklabs.assam.schema.AsnPrimitiveTypeVisitor;

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
    private static final MockAsnSchemaTypeDefinitionVisitor instance
            = new MockAsnSchemaTypeDefinitionVisitor();

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
    // IMPLEMENTATION: AsnPrimitiveType.Visitor
    // -------------------------------------------------------------------------

    @Override
    public String visit(AsnPrimitiveType.Invalid visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.BitString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.BmpString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.Boolean visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.CharacterString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.Choice visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.EmbeddedPdv visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.Enumerated visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.GeneralizedTime visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.Real visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.GeneralString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.GraphicString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.IA5String visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.Integer visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.Null visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.NumericString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.ObjectDescriptor visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.OctetString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.Oid visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.PrintableString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.RelativeOid visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.Sequence visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.SequenceOf visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.Set visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.SetOf visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.TeletexString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.UniversalString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.UtcTime visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.Utf8String visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.VideotexString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveType.VisibleString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }
}
