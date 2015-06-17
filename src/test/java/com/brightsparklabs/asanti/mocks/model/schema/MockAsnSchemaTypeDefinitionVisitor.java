/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.mocks.model.schema;

import com.brightsparklabs.asanti.model.schema.primitive.*;
import com.brightsparklabs.asanti.model.schema.tagtype.*;
import com.brightsparklabs.asanti.model.schema.typedefinition.*;

/**
 * A simple visitor which returns the canonical class name of the object it visits
 *
 * @author brightSPARK Labs
 */
public class MockAsnSchemaTypeDefinitionVisitor implements AsnSchemaTagTypeVisitor<String>
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
    // IMPLEMENTATION: AsnSchemaTagTypeVisitor
    // -------------------------------------------------------------------------

    @Override
    public String visit(OLDAsnSchemaTypeDefinition.Null visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTagType.Null visitable)
    {
        return visitable.getClass().getCanonicalName();
    }
    @Override
    public String visit(AsnPrimitiveType.Null visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(OLDAsnSchemaTypeDefinitionBitString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTagTypeBitString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeBitString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(OLDAsnSchemaTypeDefinitionChoice visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeChoice visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(OLDAsnSchemaTypeDefinitionEnumerated visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeEnumerated visitable)
    {
        return visitable.getClass().getCanonicalName();
    }
    @Override
    public String visit(OLDAsnSchemaTypeDefinitionIa5String visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeIA5String visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(OLDAsnSchemaTypeDefinitionInteger visitable)
    {
        return visitable.getClass().getCanonicalName();
    }
    @Override
    public String visit(AsnSchemaTagTypeInteger visitable)
    {
        return visitable.getClass().getCanonicalName();
    }
    @Override
    public String visit(AsnPrimitiveTypeInteger visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(OLDAsnSchemaTypeDefinitionNumericString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeNumericString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(OLDAsnSchemaTypeDefinitionOctetString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTagTypeOctetString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeOctetString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(OLDAsnSchemaTypeDefinitionSequence visitable)
    {
        return visitable.getClass().getCanonicalName();
    }
    @Override
    public String visit(AsnSchemaTagTypeSequence visitable)
    {
        return visitable.getClass().getCanonicalName();
    }
    @Override
    public String visit(AsnPrimitiveTypeSequence visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(OLDAsnSchemaTypeDefinitionSequenceOf visitable)
    {
        return visitable.getClass().getCanonicalName();
    }
    @Override
    public String visit(AsnSchemaTagTypeSequenceOf visitable)
    {
        return visitable.getClass().getCanonicalName();
    }
    @Override
    public String visit(AsnPrimitiveTypeSequenceOf visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(OLDAsnSchemaTypeDefinitionSet visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeSet visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(OLDAsnSchemaTypeDefinitionSetOf visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTagTypeSetOf visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeSetOf visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(OLDAsnSchemaTypeDefinitionUtf8String visitable)
    {
        return visitable.getClass().getCanonicalName();
    }
    @Override
    public String visit(AsnSchemaTagTypeUtf8String visitable)
    {
        return visitable.getClass().getCanonicalName();
    }
    @Override
    public String visit(AsnPrimitiveTypeUtf8String visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(OLDAsnSchemaTypeDefinitionVisibleString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeVisibleString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(OLDAsnSchemaTypeDefinitionGeneralString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypePrintableString visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTagTypePrintableString visitable)
    {
        return visitable.getClass()
                .getCanonicalName();
    }

    @Override
    public String visit(AsnPrimitiveTypeGeneralizedTime visitable)
    {
        return visitable.getClass()
                .getCanonicalName();
    }

    @Override
    public String visit(OLDAsnSchemaTypeDefinitionGeneralizedTime visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTagTypeGeneralizedTime visitable)
    {
        return visitable.getClass().getCanonicalName();
    }

    @Override
    public String visit(AsnSchemaTagTypeObjectIdentifier visitable)
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
