/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema;

import static com.google.common.base.Preconditions.*;

import com.brightsparklabs.asanti.model.schema.tag.DecodedTagsHelpers;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition;
import com.google.common.base.Splitter;
import com.google.common.collect.*;
import java.util.*;

/**
 * Default implementation of {@link AsnSchema}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaImpl implements AsnSchema {
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** splitter for separating tag strings */
    private static final Splitter tagSplitter = Splitter.on("/").omitEmptyStrings();

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
     * @param primaryModule the primary module defined in this schema
     * @param modules all modules defined in this schema
     * @throws NullPointerException if any of the parameters are {@code null}
     * @throws IllegalArgumentException if no modules are specified or the primary module is not
     *     contained in the modules
     */
    public AsnSchemaImpl(String primaryModule, Map<String, AsnSchemaModule> modules) {
        checkNotNull(primaryModule);
        checkNotNull(modules);
        checkArgument(!modules.isEmpty(), "A schema cannot contain no modules");
        checkArgument(
                modules.containsKey(primaryModule),
                "The primary module must be contained in the schema's modules");

        this.primaryModule = modules.get(primaryModule);
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchema
    // -------------------------------------------------------------------------

    @Override
    public Optional<AsnSchemaType> getType(String tag) {
        final Optional<AsnSchemaType> cacheHit = tagCache.get(tag);
        // ignore the warning about Optional being compared to null.
        // Optional.empty means we've already done the lookup and have an empty result.
        // Null means we haven't already done the lookup.
        if (cacheHit != null) {
            return cacheHit;
        }

        final ArrayList<String> tags = Lists.newArrayList(tagSplitter.split(tag));
        if (tags.isEmpty()) {
            final Optional<AsnSchemaType> result = Optional.empty();
            tagCache.put(tag, result);
            return result;
        }

        final Iterator<String> it = tags.iterator();

        final AsnSchemaTypeDefinition typeDefinition = primaryModule.getType(it.next());

        AsnSchemaType type = typeDefinition.getType();
        while (it.hasNext()) {
            final String nextTag = DecodedTagsHelpers.stripIndex(it.next());

            Optional<AsnSchemaType> next = getNext(type, nextTag);
            if (!next.isPresent()) {
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
     * For a given AsnSchemaType get the child component that matches tag. This differs from {@link
     * AsnSchemaType#getMatchingChild(String, DecodingSession)} in that it does not needs a
     * DecodingSession, it does not care about ordering of components, and it returns the component
     * type rather than the component
     *
     * @param type type to get the matching child type from
     * @param tag tag of the child to match
     * @return the child component that matches the tag, {@link Optional#empty()} if no match
     */
    private Optional<AsnSchemaType> getNext(AsnSchemaType type, String tag) {
        final ImmutableList<AsnSchemaComponentType> allComponents = type.getAllComponents();
        for (AsnSchemaComponentType component : allComponents) {
            if (component.getName().equals(tag)) {
                return Optional.of(component.getType());
            }
        }
        return Optional.empty();
    }
}
