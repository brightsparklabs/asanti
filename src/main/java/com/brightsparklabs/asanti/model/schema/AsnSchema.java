/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.google.common.collect.ImmutableSet;
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
     * Returns the decoded tags for the supplied raw tags. E.g. {@code
     * getDecodedTag("/0[1]/0[0]/0[1]", "Document")} =&gt; {@code "/Document/header/published/date"}
     * Decoding needs to be done on a whole data file at once time, as the ordering of the data can
     * impact the (validity of the) decoding, as ASN.1 SEQUENCE objects are order dependent. The
     * iteration order of the returned data will preserve the iteration order of rawTags, ie the
     * iteration order of the decoded tags will be as read from the data file (which should, for the
     * most part, be as defined in the schema).
     *
     * @param rawTags raw tags to decode
     * @param topLevelTypeName the name of the top level type in this module from which to begin
     *     decoding the raw tag
     * @return the result of the decode attempt containing the decoded tags for each of the rawTags
     */
    ImmutableSet<OperationResult<DecodedTag, String>> getDecodedTags(
            Iterable<String> rawTags, String topLevelTypeName);

    /**
     * Returns the AsnSchemaType of the supplied decoded tag.
     *
     * @param tag fully qualified decoded tag
     * @return the AsnSchemaType of the tag, {@link Optional#empty()} if no match
     */
    Optional<AsnSchemaType> getType(String tag);
}
