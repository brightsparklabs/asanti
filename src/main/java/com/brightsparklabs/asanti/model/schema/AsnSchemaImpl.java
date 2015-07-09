/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.model.schema.tag.AsnSchemaTag;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaNamedType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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
    private static final Logger logger = LoggerFactory.getLogger(AsnSchemaModule.class);

    /** splitter for separating tag strings */
    private static final Splitter tagSplitter = Splitter.on("/").omitEmptyStrings();

    /** joiner for creating tag strings */
    private static final Joiner tagJoiner = Joiner.on("/");

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the primary module defined in this schema (defaults to the first module) */
    private final AsnSchemaModule primaryModule;

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
    public ImmutableSet<OperationResult<DecodedTag>> getDecodedTags(
            final ImmutableSet<String> rawTags, String topLevelTypeName)
    {
        DecodingSession session = new DecodingSessionImpl();

        final Set<OperationResult<DecodedTag>> results = Sets.newHashSet();
        for (String rawTag : rawTags)
        {
            final OperationResult<DecodedTag> decodeResult = getDecodedTag(rawTag,
                    topLevelTypeName,
                    session);

            results.add(decodeResult);

        }

        return ImmutableSet.copyOf(results);
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
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
     * @param session
     *         the session state that tracks the ordering and stateful part of decoding a complete
     *         set of asn data.
     *
     * @return the result of the decode attempt containing the decoded tag
     */
    private OperationResult<DecodedTag> getDecodedTag(String rawTag, String topLevelTypeName,
            DecodingSession session)
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
        final OperationResult<DecodedTag> result = decodeSuccessful ?
                OperationResult.createSuccessfulInstance(decodedTag) :
                OperationResult.createUnsuccessfulInstance(decodedTag,
                        "The supplied raw tag does not map to a type in this schema");
        return result;
    }
    // ToDO MJF - see the tags in the comments!  maybe search for "/

    /**
     * Returns the decoded tags for the supplied raw tags
     *
     * @param rawTags
     *         raw tags to decode. This should be an iterable in the order of the tags. E.g. The raw
     *         tag {code "/1/0/1"} should be provided as an iterator of {code ["1", "0", "1"]}
     * @param containingType
     *         the {@link AsnSchemaType} that is the parent of the chain to be decoded
     *
     * @return a list of all decoded tags. If a raw tag could not be decoded then processing stops.
     * E.g. for the raw tags {code "1", "0" "1", "99", "98"}, if the {@code "99"} raw tag cannot be
     * decoded, then a list containing the decoded tags for only the first three raw tags is
     * returned (e.g. {@code ["Header", "Published", "Date"]})
     */
    private DecodedTagsAndType decodeTags(Iterator<String> rawTags, AsnSchemaType containingType,
            DecodingSession session)
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

            // By definition the new tag is the child of its container.
            AsnSchemaNamedType namedType = type.getMatchingChild(tag, session);
            final String decodedTag = namedType.getName();
            result.type = namedType.getType();

            // ensure it was found
            if (result.type == AsnSchemaType.NULL)
            {
                // no type to delve into
                break;
            }

            result.decodedTags.add(decodedTag);
            type = result.type;
        }

        return result;
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: DecodedTagAndType
    // -------------------------------------------------------------------------

    boolean isConstructed(AsnBuiltinType type)
    {
        switch (type)
        {
            case SequenceOf:
            case Sequence:
            case Set:
            case SetOf:
            case Choice:
                return true;
            default:
                return false;
        }
    }

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
