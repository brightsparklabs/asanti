/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import java.util.List;

/**
 * A type definition from a within a module specification within an ASN.1
 * schema.
 *
 * @author brightSPARK Labs
 *
 * @param <T>
 *            the type of value which can be assigned to this type. For
 *            constructed types (SET, SEQUENCE, SET OF, SEQUENCE OF, CHOICE or
 *            ENUMERATED) this is a {@link List}. For primitives this is the
 *            most appropriate Java type (Long, Double, String, etc)
 */
public class AsnSchemaTypeDefinition<T>
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
     */
    public AsnSchemaTypeDefinition(String name, AsnBuiltinType builtinType)
    {
        this.name = name;
        this.builtinType = builtinType;
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
    public static class AsnSchemaTypeDefinitionNull extends AsnSchemaTypeDefinition<String>
    {

        public AsnSchemaTypeDefinitionNull()
        {
            super("NULL", AsnBuiltinType.Null);
        }
    }
}
