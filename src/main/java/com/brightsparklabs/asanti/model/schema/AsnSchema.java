/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import java.util.Optional;

/**
 * Interface for modeling an ASN.1 schema
 *
 * @author brightSPARK Labs
 */
public interface AsnSchema {

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns the AsnSchemaType of the supplied decoded tag.
     *
     * @param tag fully qualified decoded tag
     * @return the AsnSchemaType of the tag, {@link Optional#empty()} if no match
     */
    Optional<AsnSchemaType> getType(String tag);
}
