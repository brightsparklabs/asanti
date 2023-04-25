/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author brightSPARK Labs
 */
public class Decoder {
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** Class logger. */
    private static final Logger logger = LoggerFactory.getLogger(Decoder.class);

    /** Splitter for separating tag strings. */
    private static final Splitter tagSplitter = Splitter.on("/").omitEmptyStrings();

    /** Joiner for creating tag strings. */
    private static final Joiner tagJoiner = Joiner.on("/");

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

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
     * @param rawTags Raw tags to decode.
     * @param topLevelTypeName The name of the top level type in this module from which to begin
     *     decoding the raw tag.
     * @param schema The schema to decode against.
     * @return The result of the decode attempt containing the decoded tags for each of the rawTags.
     */
    public static ImmutableSet<OperationResult<DecodedTag, String>> getDecodedTags(
            final Iterable<String> rawTags, final String topLevelTypeName, final AsnSchema schema) {

        final Optional<AsnSchemaType> type = schema.getType(topLevelTypeName);
        if (type.isEmpty()) {
            throw new RuntimeException("type [" + topLevelTypeName + "] does not exist in schema");
        }
        return getDecodedTags(rawTags, type.get());
    }

    /**
     * Returns the decoded tags for the supplied raw tags using a new {@link DecodingSession}.
     *
     * @param rawTags Raw tags to decode.
     * @param rootType The name of the top level type in this module from which to begin decoding
     *     the raw tag.
     * @return The result of the decode attempt containing the decoded tags for each of the rawTags.
     */
    public static ImmutableSet<OperationResult<DecodedTag, String>> getDecodedTags(
            final Iterable<String> rawTags, final AsnSchemaType rootType) {
        DecodingSession session = new DecodingSessionImpl();

        // use LinkedHashSet to preserve insertion order
        final Set<OperationResult<DecodedTag, String>> results = Sets.newLinkedHashSet();
        for (String rawTag : rawTags) {
            final OperationResult<DecodedTag, String> decodeResult =
                    getDecodedTag(rawTag, rootType, session);

            results.add(decodeResult);
        }

        return ImmutableSet.copyOf(results);
    }

    /**
     * Returns the decoded tag for the supplied raw tag.
     *
     * <p>E.g. {@code getDecodedTag("/0[1]/0[0]/0[1]", "Document")} =&gt; {@code
     * "/Document/header/published/date"}
     *
     * @param rawTag Raw tag to decode.
     * @param type The top level type from which to begin decoding the raw tag.
     * @param session The session state that tracks the ordering and stateful part of decoding a
     *     complete set of asn data..
     * @return The result of the decode attempt containing the decoded tag.
     */
    public static OperationResult<DecodedTag, String> getDecodedTag(
            String rawTag, AsnSchemaType type, DecodingSession session) {
        final ArrayList<String> tags = Lists.newArrayList(tagSplitter.split(rawTag));
        final DecodedTagsAndType decodedTagsAndType = decodeTags(tags.iterator(), type, session);
        final List<String> decodedTags = decodedTagsAndType.decodedTags;

        // check if decode was successful
        boolean decodeSuccessful = true;
        if (tags.size() != decodedTags.size()) {
            // could not decode
            decodeSuccessful = false;

            // copy unknown tags into result
            for (int i = decodedTags.size(); i < tags.size(); i++) {
                final String unknownTag = tags.get(i);
                decodedTags.add(unknownTag);
                logger.debug("Unable to parse " + rawTag + " : " + unknownTag);
            }
        }

        // The raw tags create a new '/' for collection elements (eg .../foo/[0])
        // and we would rather have .../foo[0]
        final String decodedTagPath = tagJoiner.join(decodedTags).replaceAll("/\\[", "\\[");

        logger.trace("getDecodedTag {} => {}", rawTag, decodedTagPath);

        final DecodedTag decodedTag =
                new DecodedTag(decodedTagPath, rawTag, decodedTagsAndType.type, decodeSuccessful);

        return decodeSuccessful
                ? OperationResult.createSuccessfulInstance(decodedTag)
                : OperationResult.createUnsuccessfulInstance(
                        decodedTag, "The supplied raw tag does not map to a type in this schema");
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    private static DecodedTagsAndType decodeTags(
            Iterator<String> rawTags,
            AsnSchemaType containingType,
            DecodingSession decodingSession) {
        /* TODO: ASN-143.  does this functionality now belong here?
         * all the logic is about AsnSchemaType object - should it have a decodeTags function?
         */

        final DecodedTagsAndType result = new DecodedTagsAndType();
        AsnSchemaType type = containingType;
        // It is possible to decode "/", which means return the containingType
        result.type = type;

        while (rawTags.hasNext()) {
            // Get the tag that we are decoding
            final String tag = rawTags.next();

            final String decodedTagPath =
                    tagJoiner.join(result.decodedTags).replaceAll("/\\[", "\\[");
            // By definition the new tag is the child of its container.
            decodingSession.setContext(decodedTagPath);

            Optional<AsnSchemaComponentType> child = type.getMatchingChild(tag, decodingSession);
            if (child.isEmpty()) {
                // no type to delve into
                break;
            }

            final String decodedTag = child.get().getName();
            result.type = child.get().getType();
            result.decodedTags.add(decodedTag);
            type = result.type;
        }

        return result;
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: DecodedTagAndType
    // -------------------------------------------------------------------------

    /**
     * Transfer object to support returning a tuple of "Decoded Tags" and "Type"
     *
     * @author brightSPARK Labs
     */
    public static class DecodedTagsAndType {
        /** list of decoded tags */
        private final List<String> decodedTags = Lists.newArrayList();

        /** the type of the final tag */
        private AsnSchemaType type = null;
    }
}
