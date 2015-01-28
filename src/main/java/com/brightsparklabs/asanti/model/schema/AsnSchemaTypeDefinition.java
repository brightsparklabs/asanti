/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import static com.google.common.base.Preconditions.*;

/**
 * A type definition from a within a module specification within an ASN.1
 * schema.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinition
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** null instance */
    public static final AsnSchemaTypeDefinitionNull NULL = new AsnSchemaTypeDefinitionNull();

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** name of the type definition */
    private final String name;

    /**
     * the underlying ASN.1 built-in type for this type (SEQUENCE, INTEGER, etc)
     */
    private final AsnBuiltinType builtinType;

    /** the constraints on the type */
    private final String constraints;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param name
     *            name of the defined type
     *
     * @param builtinType
     *            the underlying ASN.1 type of the defined type
     *
     * @param constraints
     *            The constraints on the type. Use {@code null} if no
     *            constraints.
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
    public AsnSchemaTypeDefinition(String name, AsnBuiltinType builtinType, String constraints)
    {
        checkNotNull(name);
        checkArgument(!name.trim()
                .isEmpty(), "Tag name must be specified");
        checkNotNull(builtinType);

        this.name = name.trim();
        this.builtinType = builtinType;
        this.constraints = (constraints == null) ? "" : constraints.trim();
    }

    /**
     * Constructor for types with no constraints.
     *
     * @deprecated Use
     *             {@link #AsnSchemaTypeDefinition(String, AsnBuiltinType, String)}
     *             instead
     *
     * @param name
     *            name of the defined type
     *
     * @param builtinType
     *            the underlying ASN.1 type of the defined type
     *
     * @throws NullPointerException
     *             if {@code name} or {@code builtinType} are {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code name} is blank
     */
    @Deprecated
    public AsnSchemaTypeDefinition(String name, AsnBuiltinType builtinType)
    {
        this(name, builtinType, null);
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns the name of this type definition
     *
     * @return the name of this type definition
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the ASN.1 built-in type for this type definition
     *
     * @return the ASN.1 built-in type for this type definition
     */
    public AsnBuiltinType getBuiltinType()
    {
        return builtinType;
    }

    /**
     * Returns the name of the specified tag
     *
     * @param tag
     *            a tag within this construct
     *
     * @return name of the specified tag; or an empty string if tag is not
     *         recognised.
     */
    public String getTagName(String tag)
    {
        // no constructs within base type definition
        return "";
    }

    /**
     * Returns the name of the type definition associated with the specified tag
     *
     * @param tag
     *            a tag within this construct
     *
     * @return name of the type definition associated with the specified tag; or
     *         an empty string if tag is not recognised.
     */
    public String getTypeName(String tag)
    {
        // no constructs within base type definition
        return "";
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: AsnSchemaConstructTypeDefinitionNUll
    // -------------------------------------------------------------------------
    /**
     * Null instance of {@link AsnSchemaTypeDefinition}
     */
    private static class AsnSchemaTypeDefinitionNull extends AsnSchemaTypeDefinition
    {

        private AsnSchemaTypeDefinitionNull()
        {
            super("NULL", AsnBuiltinType.Null);
        }
    }
}
