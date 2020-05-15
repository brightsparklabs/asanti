/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.type;

import static com.google.common.base.Preconditions.*;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveTypes;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaNamedTag;
import com.brightsparklabs.assam.schema.AsnPrimitiveType;
import com.google.common.collect.ImmutableMap;
import java.text.ParseException;

/**
 * A type used to model the types for objects within ASN.1 schema that may contain Named Values, for
 * example Enumerated and Integer. These objects can be either Type Definitions, e.g. Type ::=
 * SomeType, or components within a constructed type (SEQUENCE etc), e.g. component SomeType
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeWithNamedTags extends BaseAsnSchemaType {
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
     * @param primitiveType The {@link AsnPrimitiveTypes} for this type
     * @param constraint The constraint on the type. Use {@link AsnSchemaConstraint#NULL} if no
     *     constraint.
     *     <p>E.g. For {@code Integer (1..56)} this would be {@code (1..56)}
     * @param namedValues the optional list of named values in this type. Use an empty iterable if
     *     there are no named values.
     * @throws NullPointerException if {@code namedValues} or {@code primitiveType} is {@code null}
     */
    public AsnSchemaTypeWithNamedTags(
            AsnPrimitiveType primitiveType,
            AsnSchemaConstraint constraint,
            Iterable<AsnSchemaNamedTag> namedValues) {
        super(primitiveType, constraint);
        checkNotNull(namedValues);

        final ImmutableMap.Builder<String, AsnSchemaNamedTag> tagsToNamedValuesBuilder =
                ImmutableMap.builder();

        for (final AsnSchemaNamedTag namedValue : namedValues) {
            final String tag = namedValue.getTag();
            tagsToNamedValuesBuilder.put(tag, namedValue);
        }
        tagsToNamedValues = tagsToNamedValuesBuilder.build();
    }

    // ---------------------------------------------------------------------
    // PUBLIC METHODS
    // ---------------------------------------------------------------------

    /**
     * Returns the map of all the "named values" for this type.
     *
     * @return the map of all the "named values" for this type.
     */
    public ImmutableMap<String, AsnSchemaNamedTag> getTagsToNamedValues() {
        return tagsToNamedValues;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: BaseAsnSchemaType
    // -------------------------------------------------------------------------

    @Override
    public Object accept(final AsnSchemaTypeVisitor<?> visitor) throws ParseException {
        return visitor.visit(this);
    }
}
