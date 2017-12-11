/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.model.schema.tag.DecodedTagsHelpers;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.google.common.base.Preconditions.*;

/**
 * Default implementation of {@link AsnSchema}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaImpl implements AsnSchema
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(AsnSchemaImpl.class);

    /** splitter for separating tag strings */
    private static final Splitter tagSplitter = Splitter.on("/").omitEmptyStrings();

    /** joiner for creating tag strings */
    private static final Joiner tagJoiner = Joiner.on("/");

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the primary module defined in this schema (defaults to the first module) */
    private final AsnSchemaModule primaryModule;

    /** a simple cache to avoid recalculating Tag to Type mapping */
    private final Map<String, Optional<AsnSchemaType>> tagCache = Maps.newConcurrentMap();

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     *
     * @param primaryModule
     *         the primary module defined in this schema
     * @param modules
     *         all modules defined in this schema
     *
     * @throws NullPointerException
     *         if any of the parameters are {@code null}
     * @throws IllegalArgumentException
     *         if no modules are specified or the primary module is not contained in the modules
     */
    public AsnSchemaImpl(String primaryModule, Map<String, AsnSchemaModule> modules)
    {
        checkNotNull(primaryModule);
        checkNotNull(modules);
        checkArgument(!modules.isEmpty(), "A schema cannot contain no modules");
        checkArgument(modules.containsKey(primaryModule),
                "The primary module must be contained in the schema's modules");

        this.primaryModule = modules.get(primaryModule);
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchema
    // -------------------------------------------------------------------------

    @Override
    public ImmutableSet<OperationResult<DecodedTag, String>> getDecodedTags(
            final Iterable<String> rawTags, String topLevelTypeName)
    {
        DecodingSession session = new DecodingSessionImpl();

        // use LinkedHashSet to preserve insertion order
        final Set<OperationResult<DecodedTag, String>> results = Sets.newLinkedHashSet();
        for (String rawTag : rawTags)
        {
            final OperationResult<DecodedTag, String> decodeResult = getDecodedTag(rawTag,
                    topLevelTypeName,
                    session);

            results.add(decodeResult);

        }

        return ImmutableSet.copyOf(results);
    }

    @Override
    public Optional<AsnSchemaType> getType(String tag)
    {
        final Optional<AsnSchemaType> cacheHit = tagCache.get(tag);
        if (cacheHit != null)
        {
            return cacheHit;
        }

        final ArrayList<String> tags = Lists.newArrayList(tagSplitter.split(tag));
        final Iterator<String> it = tags.iterator();

        final AsnSchemaTypeDefinition typeDefinition = primaryModule.getType(it.next());

        AsnSchemaType type = typeDefinition.getType();
        while (it.hasNext())
        {
            final String nextTag = DecodedTagsHelpers.stripIndex(it.next());

            Optional<AsnSchemaType> next = getNext(type, nextTag);
            if (!next.isPresent())
            {
                return Optional.empty();
            }
            type = next.get();
        }

        final Optional<AsnSchemaType> result = Optional.of(type);
        tagCache.put(tag, result);
        return result;
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * For a given AsnSchemaType get the child component that matches tag.  This differs from {@link
     * AsnSchemaType#getMatchingChild(String, DecodingSession)} in that it does not needs a
     * DecodingSession, it does not care about ordering of components, and it returns the component
     * type rather than the component
     *
     * @param type
     *         type to get the matching child type from
     * @param tag
     *         tag of the child to match
     *
     * @return the child component that matches the tag, {@link Optional#empty()} if no match
     */
    private Optional<AsnSchemaType> getNext(AsnSchemaType type, String tag)
    {
        final ImmutableList<AsnSchemaComponentType> allComponents = type.getAllComponents();
        for (AsnSchemaComponentType component : allComponents)
        {
            if (component.getName().equals(tag))
            {
                return Optional.of(component.getType());
            }
        }
        return Optional.empty();
    }

    /**
     * Returns the decoded tag for the supplied raw tag. E.g. {@code getDecodedTag("/0[1]/0[0]/0[1]",
     * "Document")} =&gt; {@code "/Document/header/published/date"}
     *
     * @param rawTag
     *         raw tag to decode
     * @param topLevelTypeName
     *         the name of the top level type in this module from which to begin decoding the raw
     *         tag
     * @param session
     *         the session state that tracks the ordering and stateful part of decoding a complete
     *         set of asn data.
     *
     * @return the result of the decode attempt containing the decoded tag
     */
    private OperationResult<DecodedTag, String> getDecodedTag(String rawTag,
            String topLevelTypeName, DecodingSession session)
    {
        final ArrayList<String> tags = Lists.newArrayList(tagSplitter.split(rawTag));
        final AsnSchemaTypeDefinition typeDefinition = primaryModule.getType(topLevelTypeName);
        final DecodedTagsAndType decodedTagsAndType = decodeTags(tags.iterator(),
                typeDefinition.getType(),
                session);
        final List<String> decodedTags = decodedTagsAndType.decodedTags;

        // check if decode was successful
        boolean decodeSuccessful = true;
        if (tags.size() != decodedTags.size())
        {
            // could not decode
            decodeSuccessful = false;

            // copy unknown tags into result
            for (int i = decodedTags.size(); i < tags.size(); i++)
            {
                final String unknownTag = tags.get(i);
                decodedTags.add(unknownTag);
                logger.debug("Unable to parse " + rawTag + " : " + unknownTag);
            }
        }

        decodedTags.add(0, topLevelTypeName);
        decodedTags.add(0, ""); // empty string prefixes just the root separator
        // The raw tags create a new '/' for collection elements (eg .../foo/[0])
        // and we would rather have .../foo[0]
        final String decodedTagPath = tagJoiner.join(decodedTags).replaceAll("/\\[", "\\[");

        logger.trace("getDecodedTag {} => {}", rawTag, decodedTagPath);

        final DecodedTag decodedTag = new DecodedTag(decodedTagPath,
                rawTag,
                decodedTagsAndType.type,
                decodeSuccessful);

        return decodeSuccessful ?
                OperationResult.createSuccessfulInstance(decodedTag) :
                OperationResult.createUnsuccessfulInstance(decodedTag,
                        "The supplied raw tag does not map to a type in this schema");
    }

    /**
     * Returns the decoded tags for the supplied raw tags
     *
     * @param rawTags
     *         raw tags to decode. This should be an iterable in the order of the tags. E.g. The raw
     *         tag {code "/1[0]/0[1]/1[2]"} should be provided as an iterator of {code ["1[0]",
     *         "0[1]", "1[2]"]}
     * @param containingType
     *         the {@link AsnSchemaType} that is the parent of the chain to be decoded
     *
     * @return a list of all decoded tags. If a raw tag could not be decoded then processing stops.
     * E.g. for the raw tags {code "1", "0" "1", "99", "98"}, if the {@code "99"} raw tag cannot be
     * decoded, then a list containing the decoded tags for only the first three raw tags is
     * returned (e.g. {@code ["Header", "Published", "Date"]})
     */
    private DecodedTagsAndType decodeTags(Iterator<String> rawTags, AsnSchemaType containingType,
            DecodingSession decodingSession)
    {
        /* TODO: ASN-143.  does this functionality now belong here?
         * all the logic is about AsnSchemaType object - should it have a decodeTags function?
         */

        final DecodedTagsAndType result = new DecodedTagsAndType();
        AsnSchemaType type = containingType;
        // It is possible to decode "/", which means return the containingType
        result.type = type;

        while (rawTags.hasNext())
        {
            // Get the tag that we are decoding
            final String tag = rawTags.next();

            final String decodedTagPath = tagJoiner.join(result.decodedTags)
                    .replaceAll("/\\[", "\\[");
            // By definition the new tag is the child of its container.
            decodingSession.setContext(decodedTagPath);

            Optional<AsnSchemaComponentType> child = type.getMatchingChild(tag, decodingSession);
            if (!child.isPresent())
            {
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
    private static class DecodedTagsAndType
    {
        /** list of decoded tags */
        private final List<String> decodedTags = Lists.newArrayList();

        /** the type of the final tag */
        private AsnSchemaType type = null;
    }
}
