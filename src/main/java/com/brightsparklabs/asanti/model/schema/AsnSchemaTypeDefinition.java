/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

/**
 * A type definition from a within a module specification within an ASN.1
 * schema.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinition
{

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    // private final String name;
    // private final String tag;
    // private final String builtInType;
    // private final ImmutableMap<String, String> tagsToNames;
    // private final ImmutableMap<String, String> namesToTags;
    // private final ImmutableMap<String, String> children;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------
    /**
     * Private constructor. Use {@link #builder()} to construct an instance.
     */
    private AsnSchemaTypeDefinition()
    {
        // private constructor
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    public String getName()
    {
        return "";
    }
}
