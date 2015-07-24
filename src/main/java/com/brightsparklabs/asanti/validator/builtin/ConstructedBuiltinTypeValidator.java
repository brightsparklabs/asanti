/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * Class implementing {@link BuiltinTypeValidator} for Constructed types, ie{@link
 * AsnBuiltinType#Sequence}, {@link AsnBuiltinType#Set} and {@link AsnBuiltinType#Choice} ASN.1
 *
 * @author brightSPARK Labs
 */
public class ConstructedBuiltinTypeValidator implements BuiltinTypeValidator
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** splitter for separating tag strings */
    private static final Splitter tagSplitter = Splitter.on("/").omitEmptyStrings();

    private static ConstructedBuiltinTypeValidator instance;

    public static ConstructedBuiltinTypeValidator getInstance()
    {
        if (instance == null)
        {
            instance = new ConstructedBuiltinTypeValidator();
        }
        return instance;
    }

    private ConstructedBuiltinTypeValidator()
    {

    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: BuiltinTypeValidator
    // -------------------------------------------------------------------------

    @Override
    public ImmutableSet<DecodedTagValidationFailure> validate(String tag,
            DecodedAsnData decodedAsnData)
    {

        ImmutableSet<String> tags = buildTags(decodedAsnData);
        final Collection<String> filter = Collections2.filter(tags, new MyPredicate(tag));

        Set<String> childTags = Sets.newHashSet();
        int index = tag.length() + 1;
        for (String childTag : filter)
        {
            String cleanTag = childTag.substring(index);
            int indexOf = cleanTag.indexOf("[");
            if (indexOf >= 0)
            {
                cleanTag = cleanTag.substring(0, indexOf);
            }
            childTags.add(cleanTag);
        }

        final AsnSchema schema = decodedAsnData.getSchema();
        final AsnSchemaType type = schema.getType(tag).get();// TODO MJF - protect

        final Set<DecodedTagValidationFailure> failures = Sets.newHashSet();

        final ImmutableList<AsnSchemaComponentType> allComponents = type.getAllComponents();
        for (AsnSchemaComponentType component : allComponents)
        {
            if (!component.isOptional())
            {
                if (!childTags.contains(component.getName()))
                {
                    // TODO MJF - get the fully qualified tag that is missing noting that we
                    // may have an empty iterable so may not know how to get to the parents
                    // so we may need a slight interface change
                    // for now just put anything...
                    final DecodedTagValidationFailure failure = new DecodedTagValidationFailure(
                            component.getName(),
                            FailureType.MandatoryFieldMissing,
                            "Mandatory field was not found in the data");
                    failures.add(failure);
                }
            }
        }

        return ImmutableSet.copyOf(failures);
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
