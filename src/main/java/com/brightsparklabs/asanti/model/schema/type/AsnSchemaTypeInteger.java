package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaNamedTag;
import com.google.common.collect.ImmutableMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Michael on 16/06/2015.
 */
public class AsnSchemaTypeInteger extends AbstractAsnSchemaType
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** mapping from raw tag to distinguished value */
    private final ImmutableMap<String, AsnSchemaNamedTag> tagsToDistinguishedValues;


    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param distinguishedValues
     *            the optional list of distinguished values in this INTEGER type
     *            definition. Use an empty iterable if there are no
     *            distinguished values.
     *
     * @param constraint
     *            The constraint on the type. Use
     *            {@link AsnSchemaConstraint#NULL} if no constraint.
     *            <p>
     *            E.g. For {@code Integer (1..56)} this would be {@code (1..56)}
     *
     * @throws NullPointerException
     *             if {@code distinguishedValues} is {@code null}
     *
     */
    public AsnSchemaTypeInteger(Iterable<AsnSchemaNamedTag> distinguishedValues,
            AsnSchemaConstraint constraint)
    {
        super(AsnPrimitiveType.Integer, constraint);
        checkNotNull(distinguishedValues);

        final ImmutableMap.Builder<String, AsnSchemaNamedTag> tagsToDistinguishedValuesBuilder = ImmutableMap.builder();

        for (final AsnSchemaNamedTag distinguishedValue : distinguishedValues)
        {
            final String tag = distinguishedValue.getTag();
            tagsToDistinguishedValuesBuilder.put(tag, distinguishedValue);
        }
        tagsToDistinguishedValues = tagsToDistinguishedValuesBuilder.build();

    }

}