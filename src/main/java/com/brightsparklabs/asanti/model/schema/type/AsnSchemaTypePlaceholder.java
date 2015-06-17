package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;

import static com.google.common.base.Preconditions.*;

/**
 * A place holder type definition. This is used while parsing the schema file and coming across
 * a Component Type that is using a non-Primitive type (ie a Type Definition).  It stores the
 * constraints and the name of the type so that we can do a lookup later for the actual type
 * definition.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypePlaceholder extends AbstractAsnSchemaType
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------
    /** the name of a module the type is defined in */
    private final String moduleName;
    /** the name of the type we are placeholder for */
    private final String typeName;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param constraint
     *            The constraint on the type. Use
     *            {@link AsnSchemaConstraint#NULL} if no constraint.
     *            <p>
     *            Example 1<br>
     *            For {@code SET (SIZE (1..100) OF OCTET STRING (SIZE (10))}
     *            this would be {@code (SIZE (10)}.
     *            <p>
     *            Example 2<br>
     *            For {@code INTEGER (1..256)} this would be {@code (1..256)}.
     *
     * @throws NullPointerException
     *             if {@code name} or {@code builtinType} are {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code name} is blank
     */
    public AsnSchemaTypePlaceholder(String moduleName, String typeName, AsnSchemaConstraint constraint)
    {
        super(AsnPrimitiveType.NULL, constraint);

        checkNotNull(typeName);
        checkArgument(!typeName.trim().isEmpty(), "Type name must be specified");  // TODO MJF. Test case it!

        this.typeName = typeName;
        this.moduleName = moduleName;

    }

    public String getModuleName()
    {
        return moduleName;
    }

    public String getTypeName()
    {
        return typeName;
    }
}
