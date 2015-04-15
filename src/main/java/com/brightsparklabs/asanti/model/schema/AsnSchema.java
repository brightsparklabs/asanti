/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.common.OperationResult;

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

    /**
     * Returns the decoded tag for the supplied raw tag. E.g. {@code getDecodedTag("/1/0/1",
     * "Document")} =&gt; {@code "/Document/header/published/date"}
     *
     * @param rawTag
     *         raw tag to decode
     * @param topLevelTypeName
     *         the name of the top level type in this module from which to begin decoding the raw
     *         tag
     *
     * @return the result of the decode attempt containing the decoded tag
     */
    public OperationResult<DecodedTag> getDecodedTag(String rawTag, String topLevelTypeName);
}
