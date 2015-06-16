package com.brightsparklabs.asanti.model.schema.tagtype;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTagTypeVisitor;

import static com.google.common.base.Preconditions.*;

/**
 * A place holder type definition. This is used while parsing the schema file and coming across
 * a Component Type that is using a non-Primitive type (ie a Type Definition).  It stores the
 * constraints and the name of the type so that we can do a lookup later for the actual type
 * definition.  The constrains should "reduce" down to a single set - that is TODO
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTagTypePlaceHolder extends AbstractAsnSchemaTagType
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------
    private final String moduleName;
    private final String typeName;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param typeName
     *            the name of the Type Definition
     * @param constraint
     *            The constraint on the type. Use
     *            {@link AsnSchemaConstraint#NULL} if no constraint.
     *            <p>
     *            E.g. For {@code Utf8String (SIZE (1..50))} this would be
     *            {@code SIZE (1..50)}
     *
     * @throws NullPointerException
     *             if {@code name} is {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code typeName} is blank
     */
    public AsnSchemaTagTypePlaceHolder(String moduleName, String typeName, AsnSchemaConstraint constraint)
    {
        super(AsnBuiltinType.Null, constraint);

        checkNotNull(typeName);
        checkArgument(!typeName.trim().isEmpty(), "Type name must be specified");  // TODO MJF. Test case it!

        this.typeName = typeName;
        this.moduleName =moduleName;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: OLDAsnSchemaTypeDefinition
    // -------------------------------------------------------------------------

    @Override
    public Object visit(AsnSchemaTagTypeVisitor<?> visitor)
    {
        // TODO MJF - what to do here?  Should we add a prototype for AsnSchemaTagTypePlaceHolder
        // to the visitors?  In theory we should never have this called, but I'm sure there is a way.
        return null;//visitor.visit(this);
    }

    public String getTypeName()
    {
        return typeName;
    }

    public String getModuleName()
    {
        return moduleName;
    }
}
