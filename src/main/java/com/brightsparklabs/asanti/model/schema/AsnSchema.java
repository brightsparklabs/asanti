/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.model.data.AsnData;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * Interface for modeling an ASN.1 schema
 *
 * @author brightSPARK Labs
 */
public interface AsnSchema
{

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    //TODO MJF public OperationResult<DecodedTag> getDecodedTag(String rawTag, String topLevelTypeName, DecodingSession session);

    /**
     * Returns the decoded tags for the supplied raw tags. E.g. {@code getDecodedTag("/0.1/0.0/0.1",
     * "Document")} =&gt; {@code "/Document/header/published/date"}
     * Decoding needs to be done on a whole data file at once time, as the ordering of the data can
     * impact the (validity of the) decoding, as ASN.1 SEQUENCE objects are order dependent.
     *
     * @param rawTags
     *         raw tags to decode
     * @param topLevelTypeName
     *         the name of the top level type in this module from which to begin decoding the raw
     *         tag
     *
     * @return the result of the decode attempt containing the decoded tags for each of the rawTags
     *
     */
    public ImmutableSet<OperationResult<DecodedTag>> getDecodedTags(ImmutableSet<String> rawTags,
            String topLevelTypeName);

}
