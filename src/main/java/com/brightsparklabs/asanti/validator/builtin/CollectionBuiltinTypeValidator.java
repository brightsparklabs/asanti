package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * TODO MJF
 */
public class CollectionBuiltinTypeValidator implements BuiltinTypeValidator
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** splitter for separating tag strings */
    private static final Splitter tagSplitter = Splitter.on("/").omitEmptyStrings();

    private static CollectionBuiltinTypeValidator instance;

    public static CollectionBuiltinTypeValidator getInstance()
    {
        if (instance == null)
        {
            instance = new CollectionBuiltinTypeValidator();
        }
        return instance;
    }

    private CollectionBuiltinTypeValidator()
    {

    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: BuiltinTypeValidator
    // -------------------------------------------------------------------------

    @Override
    public ImmutableSet<DecodedTagValidationFailure> validate(String tag,
            DecodedAsnData decodedAsnData)
    {
        String strippedTag = tag;
        int index = strippedTag.lastIndexOf("[");
        if (index >= 0)
        {
            strippedTag = strippedTag.substring(0, index);
        }

        ImmutableSet<String> tags = buildTags(decodedAsnData);
        final Collection<String> filter = Collections2.filter(tags, new MyPredicate(strippedTag));


        final AsnSchema schema = decodedAsnData.getSchema();
        final AsnSchemaType type = schema.getType(strippedTag).get();// TODO MJF - protect

        final ImmutableSet<AsnSchemaConstraint> constraints = type.getConstraints();

        final Set<DecodedTagValidationFailure> failures = Sets.newHashSet();

        return ImmutableSet.of();
    }

    @Override
    public ImmutableSet<ByteValidationFailure> validate(final byte[] bytes)
    {
        return ImmutableSet.of();
    }


    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    // TODO MJF
    // If this concpet works then it should probably go in the DecodedAsnData class
    // so that it only needs to be called once.
    private ImmutableSet<String> buildTags(DecodedAsnData decodedAsnData)
    {
        AsnSchema schema = decodedAsnData.getSchema();

        Set<String> result = Sets.newHashSet();

        // TODO MJF - we are going to have to work through all tags (even unmapped), because the
        // unmapped tags MAY be the only ones with some of the path in them.
        // Obviously we need to understand where to stop (ie don't get to the unmapped part)
        for (final String tag : decodedAsnData.getTags())
        {
            final ArrayList<String> tags = Lists.newArrayList(tagSplitter.split(tag));

            // we ignore the first one because it is the Module name.
            String reconstructed = "";
            for (int i = 0; i < tags.size(); i++)
            {
                final String tagName = tags.get(i);

                reconstructed += "/" + tagName;
                result.add(reconstructed);
            }
        }
        return ImmutableSet.copyOf(result);
    }

    private static class MyPredicate implements Predicate<String>
    {
        private final String tag;

        MyPredicate(String tag)
        {
            this.tag = tag;
        }

        @Override
        public boolean apply(final String input)
        {

            if (!input.startsWith(tag) || input.equals(tag))
            {
                return false;
            }

            // only true for the next level child, not all its children too
            String s = input.substring(tag.length() + 1);

            return !s.contains("/");
        }
    }

}
