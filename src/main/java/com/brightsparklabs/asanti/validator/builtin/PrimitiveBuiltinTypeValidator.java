/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.asanti.validator.result.DecodedTagValidationResult;

/**
 * Convenience class to simplify implementing {@link BuiltinTypeValidator} for {@link AsnBuiltinType
 * ASN.1 Built-in Types}. Sub-classes should override {@link #validate(byte[])}.
 *
 * @author brightSPARK Labs
 */
public abstract class PrimitiveBuiltinTypeValidator implements BuiltinTypeValidator
{
    // -------------------------------------------------------------------------
    // IMPLEMENTATION: BuiltinTypeValidator
    // -------------------------------------------------------------------------

    @Override
    public DecodedTagValidationResult validate(String tag, DecodedAsnData decodedAsnData)
    {
        final DecodedTagValidationResult.Builder result = DecodedTagValidationResult.builder(tag);

        // validate data
        final byte[] bytes = decodedAsnData.getBytes(tag);
        final Iterable<ByteValidationFailure> failures = validate(bytes);
        for (ByteValidationFailure failure : failures)
        {
            result.add(failure.getFailureType(), failure.getFailureReason());
        }

        // validate against the tag's constraint
        final AsnSchemaTypeDefinition type = decodedAsnData.getType(tag);
        final AsnSchemaConstraint constraint = type.getConstraint();
        final OperationResult<byte[]> constraintResult = constraint.apply(bytes);
        if (!constraintResult.wasSuccessful())
        {
            result.add(FailureType.MandatoryFieldMissing, constraintResult.getFailureReason());
        }

        return result.build();
    }

    // -------------------------------------------------------------------------
    // PROTECTED METHODS
    // -------------------------------------------------------------------------

    /**
     * Validates the supplied bytes based on the the kind of ASN.1 Built-in Type represented by this
     * validator
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures encountered while validating the bytes
     */
    protected abstract Iterable<ByteValidationFailure> validate(byte[] bytes);
}
