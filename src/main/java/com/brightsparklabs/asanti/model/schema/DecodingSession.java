/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.model.schema.tag.AsnSchemaTag;

/**
 * An object to store state during the decoding of all the tags in an AsantiAsnData object. This is
 * needed because the ability to map the "raw" tags received from the BER data is dependent on what
 * other data has been received (notably OPTIONAL components of a SEQUENCE)
 */
public interface DecodingSession {
    /** @param context a unique string for each level of the hierarchy of a set of tags */
    void setContext(String context);

    /**
     * @param tag the AsnSchemaTag that we want the offset for
     * @return any offset needed to be applied to that index
     */
    int getIndex(AsnSchemaTag tag);

    /**
     * @param tag the AsnSchemaTag that we want to set the offset for
     * @param index the value to be returned at the next call to {@code getIndex} with the given
     *     context
     */
    void setIndex(AsnSchemaTag tag, int index);
}
