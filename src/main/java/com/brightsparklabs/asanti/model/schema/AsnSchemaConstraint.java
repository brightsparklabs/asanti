/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

/**
 * Models a Constraint from within a {@link AsnSchemaTypeDefinition} or
 * {@link AsnSchemaComponentType}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaConstraint
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** null instance */
    public static final AsnSchemaTypeDefinitionNull NULL = new AsnSchemaTypeDefinitionNull();

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------
    // TODO ASN-38 - define constraint

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: AsnSchemaTypeDefinitionNull
    // -------------------------------------------------------------------------

    /**
     * Null instance of {@link AsnSchemaTypeDefinition}
     */
    private static class AsnSchemaTypeDefinitionNull extends AsnSchemaConstraint
    {
        /**
         * Default constructor. Private, use {@link AsnSchemaConstraint#NULL}
         * instead to obtain a singleton instance
         */
        private AsnSchemaTypeDefinitionNull()
        {
        }
    }
}
