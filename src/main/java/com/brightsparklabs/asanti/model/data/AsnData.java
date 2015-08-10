/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.data;

import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveTypes;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.google.common.base.Optional;

/**
 * Interface for modeling ASN.1 data which has been mapped against a schema
 *
 * @author brightSPARK Labs
 */
public interface AsnData extends com.brightsparklabs.assam.data.AsnData
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Gets the ASN.1 Type of the specified tag
     *
     * @param tag
     *         tag to retrieve the type of
     *
     * @return the {@link AsnSchemaType} of the specified tag or {@code Optional.absent()} if the
     * tag does not exist. To use this without caring if there was a match, and to get a {@link
     * AsnPrimitiveTypes#INVALID} if the tag does not exist then use {@code
     * getType(tag).or(AsnPrimitiveTypes.INVALID)}
     */
    Optional<AsnSchemaType> getType(String tag);
}
