package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaNamedTag;
import com.google.common.collect.ImmutableMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A type used to model the types for objects within ASN.1 schema that may contain Named Values,
 * for example Enumerated and Integer.
 * These objects can be either Type Definitions, eg Type ::= SomeType,
 * or components within a constructed type (SEQUENCE etc), eg component SomeType
 *
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeWithNamedTags extends BaseAsnSchemaType
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** mapping from raw tag to named value */
    private final ImmutableMap<String, AsnSchemaNamedTag> tagsToNamedValues;


    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param primitiveType
     *            The {@link AsnPrimitiveType} for this type
     *
     * @param constraint
     *            The constraint on the type. Use
     *            {@link AsnSchemaConstraint#NULL} if no constraint.
     *            <p>
     *            E.g. For {@code Integer (1..56)} this would be {@code (1..56)}
     *
     * @param namedValues
     *            the optional list of named values in this type.
     *            Use an empty iterable if there are no
     *            named values.
     *
     * @throws NullPointerException
     *             if {@code namedValues} or {@code primitiveType} is {@code null}
     *
     */
    public AsnSchemaTypeWithNamedTags(AsnPrimitiveType primitiveType,
            AsnSchemaConstraint constraint, Iterable<AsnSchemaNamedTag> namedValues)
    {
        super(primitiveType, constraint);
        checkNotNull(namedValues);

        final ImmutableMap.Builder<String, AsnSchemaNamedTag> tagsToNamedValuesBuilder = ImmutableMap.builder();

        for (final AsnSchemaNamedTag namedValue : namedValues)
        {
            final String tag = namedValue.getTag();
            tagsToNamedValuesBuilder.put(tag, namedValue);
        }
        tagsToNamedValues = tagsToNamedValuesBuilder.build();
    }
}
