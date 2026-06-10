/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema;

import com.google.common.collect.ImmutableSet;
import java.util.Map;

/**
 * A cache for decoding tags from the raw ASN data.
 *
 * @param decodeCache A cache of raw ASN tags to the decoded PDU schema for those tags.
 * @param tagCache A cache of individual raw ASN tags to their decoded tag.
 * @author brightSPARK Labs
 */
public record DecoderTagCache(
        Map<ImmutableSet<String>, PduSchema> decodeCache, Map<String, DecodedTag> tagCache) {}
