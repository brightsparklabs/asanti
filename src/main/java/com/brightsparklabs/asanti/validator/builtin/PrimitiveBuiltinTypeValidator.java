/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.brightsparklabs.asanti.validator.failure.SchemaConstraintValidationFailure;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Convenience class to simplify implementing {@link BuiltinTypeValidator} for {@link AsnBuiltinType
 * ASN.1 Built-in Types}. Sub-classes should override {@link #validateNonNullBytes(byte[])}.
 *
 * @author brightSPARK Labs
 */
public abstract class PrimitiveBuiltinTypeValidator implements BuiltinTypeValidator
{
    // -------------------------------------------------------------------------
    // IMPLEMENTATION: BuiltinTypeValidator
    // -------------------------------------------------------------------------

    @Override
    public ImmutableSet<DecodedTagValidationFailure> validate(String tag,
            DecodedAsnData decodedAsnData)
    {
        final Set<DecodedTagValidationFailure> tagFailures = Sets.newHashSet();

        // validate data
        final byte[] bytes = decodedAsnData.getBytes(tag).orNull();
        final Iterable<ByteValidationFailure> byteFailures = validate(bytes);
        for (ByteValidationFailure byteFailure : byteFailures)
        {
            final DecodedTagValidationFailure tagFailure = new DecodedTagValidationFailure(tag,
                    byteFailure.getFailureType(),
                    byteFailure.getFailureReason());
            tagFailures.add(tagFailure);
        }

        // validate against the tag's constraint
        final AsnSchemaType type = decodedAsnData.getType(tag);
        final ImmutableSet<AsnSchemaConstraint> constraints = type.getConstraints();
        final Set<SchemaConstraintValidationFailure> constraintFailures = Sets.newHashSet();
        for (AsnSchemaConstraint constraint : constraints)
        {
            constraintFailures.addAll(constraint.apply(bytes));
        }
        for (SchemaConstraintValidationFailure constraintFailure : constraintFailures)
        {
            final DecodedTagValidationFailure tagFailure = new DecodedTagValidationFailure(tag,
                    constraintFailure.getFailureType(),
                    constraintFailure.getFailureReason());
            tagFailures.add(tagFailure);
        }

        return ImmutableSet.copyOf(tagFailures);
    }

    @Override
    public ImmutableSet<ByteValidationFailure> validate(final byte[] bytes)
    {
        if (bytes == null)
        {
            final ByteValidationFailure failure = new ByteValidationFailure(0,
                    FailureType.DataMissing,
                    "No bytes present to validate");
            return ImmutableSet.of(failure);
        }
        return validateNonNullBytes(bytes);
    }

    // -------------------------------------------------------------------------
    // PROTECTED METHODS
    // -------------------------------------------------------------------------

    /**
     * Validates the supplied bytes based on the the kind of ASN.1 Built-in Type represented by this
     * validator. The bytes parameter is guaranteed to be non-{@code null}.
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures encountered while validating the bytes
     */
    protected abstract ImmutableSet<ByteValidationFailure> validateNonNullBytes(byte[] bytes);
}
