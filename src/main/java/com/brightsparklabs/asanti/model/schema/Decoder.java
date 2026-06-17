/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.exception.DecodeException;
import com.brightsparklabs.asanti.model.data.RawAsnData;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaTypePrimitiveAliased;
import com.brightsparklabs.asanti.reader.AsnBerDataReader;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities for decoding the raw tags from {@link RawAsnData} into proper ASN {@link DecodedTag}
 * from the schema.
 *
 * @author brightSPARK Labs
 */
public class Decoder {
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
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Decodes the {@link RawAsnData}, producing a {@link PduSchema} which contains mappings of the
     * decoded tag strings.
     *
     * <p>The {@link RawAsnData} may contain {@link AsnSchemaTypePrimitiveAliased aliased} values,
     * which can only be decoded with the schema. E.g. {@code OCTET STRING (CONTAINS otherType)}. To
     * handle this, we need to recursively "unpack" and decode these values.
     *
     * <p>The returned {@link RawAsnData} will contain all original tags as well as the "unpacked"
     * aliased data.
     *
     * @param rawAsnData Data to unpack and decode the tags from against the schema.
     * @param asnSchema Schema to use to decode tags.
     * @param topLevelTypeName The name of the top level type in this module from which to begin
     *     decoding the raw tag.
     * @return The unpacked (if necessary) {@link RawAsnData} and {@link PduSchema} for the tag
     *     mappings.
     */
    public static UnpackedDecodedTags unpackAndDecode(
            final RawAsnData rawAsnData, final AsnSchema asnSchema, final String topLevelTypeName) {
        return decodedTags(rawAsnData, asnSchema, topLevelTypeName, Optional.empty());
    }

    /**
     * Alternative to {@link #unpackAndDecode(RawAsnData, AsnSchema, String)}, which allows passing
     * in a {@link DecoderTagCache} to prevent re-calculating the decoded tags.
     *
     * @param rawAsnData Data to unpack and decode the tags from against the schema.
     * @param asnSchema Schema to use to decode tags.
     * @param topLevelTypeName The name of the top level type in this module from which to begin
     *     decoding the raw tag.
     * @param cache A cache to lookup and add decoded raw tags.
     * @return The unpacked (if necessary) {@link RawAsnData} and {@link PduSchema} for the tag
     *     mappings.
     */
    public static UnpackedDecodedTags unpackAndDecode(
            final RawAsnData rawAsnData,
            final AsnSchema asnSchema,
            final String topLevelTypeName,
            final DecoderTagCache cache) {
        return decodedTags(rawAsnData, asnSchema, topLevelTypeName, Optional.of(cache));
    }

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
            throw new RuntimeException(
                    "type [%s] does not exist in schema".formatted(topLevelTypeName));
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
     *     complete set of asn data.
     * @return The result of the decode attempt containing the decoded tag.
     */
    public static OperationResult<DecodedTag, String> getDecodedTag(
            final String rawTag, final AsnSchemaType type, final DecodingSession session) {
        final List<String> tags = tagSplitter.splitToList(rawTag);
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
                logger.debug("Unable to parse {} : {}", rawTag, unknownTag);
            }
        }

        // The raw tags create a new '/' for collection elements (eg .../foo/[0])
        // and we would rather have .../foo[0]
        final String decodedTagPath = tagJoiner.join(decodedTags).replace("/[", "[");

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
            final Iterator<String> rawTags,
            final AsnSchemaType containingType,
            final DecodingSession decodingSession) {
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

            final String decodedTagPath = tagJoiner.join(result.decodedTags).replace("/[", "[");
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

    /**
     * Business logic which is proxied by the following to streamline providing the cache:
     *
     * <ul>
     *   <li>{@link #unpackAndDecode(RawAsnData, AsnSchema, String)}
     *   <li>{@link #unpackAndDecode(RawAsnData, AsnSchema, String, DecoderTagCache)}
     * </ul>
     *
     * @param rawAsnData Data to unpack and decode the tags from against the schema.
     * @param asnSchema Schema to use to decode tags.
     * @param topLevelTypeName The name of the top level type in this module from which to begin
     *     decoding the raw tag.
     * @param cache An optional cache to lookup and add decoded raw tags.
     * @return The unpacked (if necessary) {@link RawAsnData} and {@link PduSchema} for the tag
     *     mappings.
     */
    private static UnpackedDecodedTags decodedTags(
            final RawAsnData rawAsnData,
            final AsnSchema asnSchema,
            final String topLevelTypeName,
            final Optional<DecoderTagCache> cache) {
        final AsnSchemaType rootType =
                asnSchema
                        .getType(topLevelTypeName)
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "type [%s] does not exist in schema"
                                                        .formatted(topLevelTypeName)));

        final Supplier<PduSchema> createSchema =
                () ->
                        decodeTagsToSchema(
                                rawAsnData.getRawTags(),
                                rootType,
                                "/" + topLevelTypeName,
                                "",
                                cache);
        final PduSchema tagsResult =
                cache.map(
                                c ->
                                        c.decodeCache()
                                                .computeIfAbsent(
                                                        rawAsnData.getRawTags(),
                                                        (_) -> createSchema.get()))
                        .orElseGet(createSchema);

        if (tagsResult.aliasedTags().isEmpty()) {
            return new UnpackedDecodedTags(rawAsnData, tagsResult);
        }

        // The RawAsnData is where we get the data (byte array) associated with a raw tag.
        // Since INS-434 we are supporting "CONTAINS" constraints, that appear in the schema as
        // an octet string, but we should treat as an aliased type.
        // Our current mechanism for handling this is to extract the bytes of the octet string
        // and parse it with our BER/DER parser.  This then produces a new RawAsnData that we
        // slot in to the appropriate spot in the "tree".  We then perform the normal mapping
        // of raw tags to the schema to produce decoded tags.
        final var unpackedBuilder =
                UnpackedDecodedTags.builder().add(tagsResult).add(rawAsnData.getBytes());
        recursivelyDecodeAliasedTags(tagsResult.aliasedTags(), rawAsnData, unpackedBuilder, cache);
        return unpackedBuilder.build();
    }

    /**
     * The process of Decoding is matching the raw data provided with the schema provided. This may
     * need to be recursive in the case of some schema constructs, eg:
     *
     * <p>OCTET STRING (CONTAINS otherType)
     *
     * @param rawTags The raw ASN tags from the data as a series of XPaths.
     * @param rootType The type from the schema that the rawAsnData should align to.
     * @param decodedPrefix Any prefix that should be applied to decoded tags to "fully qualify"
     *     them.
     * @param rawPrefix Any prefix that should be applied to raw tags to "fully qualify" them.
     * @param cache An optional cache to lookup and add decoded raw tags.
     */
    private static PduSchema decodeTagsToSchema(
            final ImmutableSet<String> rawTags,
            final AsnSchemaType rootType,
            final String decodedPrefix,
            final String rawPrefix,
            final Optional<DecoderTagCache> cache) {
        final var session = new DecodingSessionImpl();
        final var builder = PduSchema.builder();

        for (final var rawTag : rawTags) {
            // We may be decoding at the "root" or somewhere part way through the schema
            // hierarchy, so we need to be able to establish the fully qualified parth
            // for both decoded and raw tags.

            final Supplier<DecodedTag> createTag =
                    () -> {
                        final var decodedTag =
                                Decoder.getDecodedTag(rawTag, rootType, session).getOutput();
                        return new DecodedTag(
                                decodedPrefix + "/" + decodedTag.tag(),
                                rawPrefix + decodedTag.rawTag(),
                                decodedTag.type(),
                                decodedTag.isFullyDecoded());
                    };
            final var fullyQualifiedTag =
                    cache.map(c -> c.tagCache().computeIfAbsent(rawTag, (_) -> createTag.get()))
                            .orElseGet(createTag);

            builder.add(fullyQualifiedTag);
        }
        return builder.build();
    }

    /**
     * Takes the bytes from an OCTET STRING that is an alias for another type and parses them and
     * then decodes.
     *
     * @param aliased The bytes from the OCTET STRING.
     * @param rawAsnData the tag that the bytes came from.
     * @param builder [OUTPUT] add the newly parsed data to this.
     * @param cache [OUTPUT] add to this with new unmapped tags.
     */
    private static void recursivelyDecodeAliasedTags(
            final Collection<DecodedTag> aliased,
            final RawAsnData rawAsnData,
            final UnpackedDecodedTags.Builder builder,
            final Optional<DecoderTagCache> cache) {
        for (final var tag : aliased) {
            final var bytes = rawAsnData.getBytes(tag.rawTag());
            if (bytes.isEmpty()) {
                continue;
            }

            var aliasedAsnData = ImmutableList.<RawAsnData>of();
            try {
                aliasedAsnData =
                        AsnBerDataReader.read(bytes.get()).collect(ImmutableList.toImmutableList());
                // TODO INS-434: should we ever expect anything other than 1 PDU from this???
                // what if the CONTAINS is a collection?
                if (aliasedAsnData.isEmpty()) {
                    throw new DecodeException(
                            "No pdus found when parsing aliased bytes from " + tag.tag());
                }
            } catch (Exception e) {
                // If we had issues processing the bytes and aligning it to the aliased type
                // then we should attempt to deal with that as a validation issue as opposed
                // to just throwing
                // The AsnSchemaContainsConstraint will also attempt to parse the bytes, so should
                // create a validation failure for issues with that.
                logger.error("Exception while processing aliased type at {}", tag.tag(), e);
            }

            for (final var data : aliasedAsnData) {
                final ImmutableMap<String, byte[]> bytesMatching = data.getBytes();
                for (final Map.Entry<String, byte[]> e : bytesMatching.entrySet()) {
                    final String fullQualifiedTag = tag.rawTag() + e.getKey();
                    builder.add(fullQualifiedTag, e.getValue());
                }

                final var decodeResult =
                        decodeTagsToSchema(
                                data.getRawTags(), tag.type(), tag.tag(), tag.rawTag(), cache);
                builder.add(decodeResult);

                recursivelyDecodeAliasedTags(decodeResult.aliasedTags(), data, builder, cache);
            }
        }
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
