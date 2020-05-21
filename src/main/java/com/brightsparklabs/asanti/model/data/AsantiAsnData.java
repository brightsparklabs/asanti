/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.data;

import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveTypes;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import java.util.Optional;

/**
 * Interface for modeling ASN.1 data which has been mapped against a schema
 *
 * @author brightSPARK Labs
 */
public interface AsantiAsnData extends com.brightsparklabs.asanti.data.AsnData {
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Gets the ASN.1 Type of the specified tag
     *
     * @param tag tag to retrieve the type of
     * @return the {@link AsnSchemaType} of the specified tag or {@link Optional#empty()} if the tag
     *     does not exist. To use this without caring if there was a match, and to get a {@link
     *     AsnPrimitiveTypes#INVALID} if the tag does not exist then use {@code
     *     getType(tag).or(AsnPrimitiveTypes.INVALID)}
     */
    Optional<AsnSchemaType> getType(String tag);
}
