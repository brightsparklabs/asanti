package com.brightsparklabs.asanti.model.schema.type;

import java.text.ParseException;

/**
 * @author brightSPARK Labs
 */
public class GetAsnSchemaTypeVisitor implements AsnSchemaTypeVisitor<AsnSchemaType>
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static final GetAsnSchemaTypeVisitor instance = new GetAsnSchemaTypeVisitor();

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor. This is a singleton, use the getInstance.
     */
    private GetAsnSchemaTypeVisitor()
    {

    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns an instance of this class
     *
     * @return an instance of this class
     */
    public static GetAsnSchemaTypeVisitor getInstance()
    {
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaTypeVisitor
    // -------------------------------------------------------------------------

    @Override
    public AsnSchemaType visit(AsnSchemaTypeConstructed visitable) throws ParseException
    {
        return visitable;
    }

    @Override
    public AsnSchemaType visit(BaseAsnSchemaType visitable) throws ParseException
    {
        return visitable;
    }

    @Override
    public AsnSchemaType visit(AsnSchemaTypeCollection visitable) throws ParseException
    {
        return visitable;
    }

    @Override
    public AsnSchemaType visit(AsnSchemaTypeWithNamedTags visitable) throws ParseException
    {
        return visitable;
    }

    @Override
    public AsnSchemaType visit(AsnSchemaTypePlaceholder visitable) throws ParseException
    {
        return (AsnSchemaType) visitable.getIndirectType().accept(this);
    }

    @Override
    public AsnSchemaType visit(AsnSchemaType.Null visitable) throws ParseException
    {
        return visitable;
    }
}
