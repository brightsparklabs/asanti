/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.typedefinition;

import static com.google.common.base.Preconditions.*;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

/**
 * A primitive type definition is an ASN.1 type which is not 'constructed'. The
 * 'constructed' types are SET, SEQUENCE, SET OF, SEQUENCE OF, CHOICE and
 * ENUMERATED (see {@link AsnSchemaTypeDefinitionConstructed}).
 *
 * @author brightSPARK Labs
 */
public abstract class AsnSchemaTypeDefinitionPrimitive extends AbstractAsnSchemaTypeDefinition
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /**
     * built-in types which are 'constructed'
     */
    private static final ImmutableSet<AsnBuiltinType> invalidTypes = ImmutableSet.of(AsnBuiltinType.Set,
            AsnBuiltinType.Sequence,
            AsnBuiltinType.SetOf,
            AsnBuiltinType.SequenceOf,
            AsnBuiltinType.Choice,
            AsnBuiltinType.Enumerated);

    /**
     * built-in types which are not 'constructed'
     */
    public static final ImmutableSet<AsnBuiltinType> validTypes =
            ImmutableSet.copyOf(Sets.difference(ImmutableSet.copyOf(AsnBuiltinType.values()), invalidTypes));

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param name
     *            name of the defined type
     *
     * @param builtinType
     *            the underlying ASN.1 type of the defined type
     *
     * @param constraint
     *            The constraint on the type. Use
     *            {@link AsnSchemaConstraint#NULL} if no constraint.
     *            <p>
     *            Example 1<br>
     *            For {@code SET (SIZE (1..100) OF OCTET STRING (SIZE (10))}
     *            this would be {@code (SIZE (10)}.
     *            <p>
     *            Example 2<br>
     *            For {@code INTEGER (1..256)} this would be {@code (1..256)}.
     *
     * @throws NullPointerException
     *             if {@code name} or {@code builtinType} are {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code name} is blank
     */
    public AsnSchemaTypeDefinitionPrimitive(String name, AsnBuiltinType builtinType, AsnSchemaConstraint constraint)
    {
        super(name, builtinType, constraint);
        checkArgument(validTypes.contains(builtinType),
                "Type cannot be SET, SEQUENCE, SET OF, SEQUENCE OF, CHOICE or ENUMERATED. Found: {0}",
                builtinType.toString());
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaTypeDefinition
    // -------------------------------------------------------------------------

    @Override
    public String getTagName(String tag)
    {
        // no constructs within primitive type definition
        return "";
    }

    @Override
    public String getTypeName(String tag)
    {
        // no constructs within primitive type definition
        return "";
    }
}
