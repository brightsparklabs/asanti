/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.typedefinition;

import static com.google.common.base.Preconditions.*;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.google.common.collect.ImmutableMap;

/**
 * An {@code Integer} type definition from a within a module specification
 * within an ASN.1 schema.
 *
 * @author brightSPARK Labs
 */
public class OLDAsnSchemaTypeDefinitionInteger extends OLDAsnSchemaTypeDefinitionPrimitive
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
     * @param name
     *            name of the Integer type definition
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
     *             if {@code name} or {@code distinguishedValues} is
     *             {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code name} is blank
     */
    public OLDAsnSchemaTypeDefinitionInteger(String name,
            Iterable<AsnSchemaNamedTag> distinguishedValues, AsnSchemaConstraint constraint)
    {
        super(name, AsnBuiltinType.Integer, constraint);
        checkNotNull(distinguishedValues);

        final ImmutableMap.Builder<String, AsnSchemaNamedTag> tagsToDistinguishedValuesBuilder = ImmutableMap.builder();

        for (final AsnSchemaNamedTag distinguishedValue : distinguishedValues)
        {
            final String tag = distinguishedValue.getTag();
            tagsToDistinguishedValuesBuilder.put(tag, distinguishedValue);
        }
        tagsToDistinguishedValues = tagsToDistinguishedValuesBuilder.build();
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: OLDAsnSchemaTypeDefinition
    // -------------------------------------------------------------------------

    @Override
    public String getTagName(String tag)
    {
        final AsnSchemaNamedTag distinguishedValue = tagsToDistinguishedValues.get(tag);
        return (distinguishedValue == null) ? "" : distinguishedValue.getTagName();
    }

    @Override
    public String getTypeName(String tag)
    {
        return "";
    }

    @Override
    public Object visit(AsnSchemaTagTypeVisitor<?> visitor)
    {
        return visitor.visit(this);
    }
}