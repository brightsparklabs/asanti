/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.validator.builtin.BuiltinTypeValidator;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.brightsparklabs.asanti.validator.result.DecodedAsnDataValidationResult;
import com.brightsparklabs.asanti.validator.result.ValidationResult;
import com.brightsparklabs.asanti.validator.rule.ValidationRule;
import com.google.common.collect.ImmutableSet;

/**
 * Default implementation of {@link Validator}.
 *
 * @author brightSPARK Labs
 */
public class ValidatorImpl implements Validator
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** visitor to determine which {@link ValidationRule} to apply to a tag */
    private final ValidationVisitor validationVisitor = new ValidationVisitor();

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: Validator
    // -------------------------------------------------------------------------

    @Override
    public ValidationResult validate(DecodedAsnData decodedAsnData)
    {
        final DecodedAsnDataValidationResult.Builder builder
                = DecodedAsnDataValidationResult.builder();
        for (final String tag : decodedAsnData.getTags())
        {
            final AsnSchemaTypeDefinition type = decodedAsnData.getType(tag);
            final BuiltinTypeValidator tagValidator = (BuiltinTypeValidator) type.visit(
                    validationVisitor);
            final ImmutableSet<DecodedTagValidationFailure> failures = tagValidator.validate(tag,
                    decodedAsnData);
            builder.addAll(failures);
        }

        // add a failure for each unmapped tag
        for (final String tag : decodedAsnData.getUnmappedTags())
        {
            final DecodedTagValidationFailure failure = new DecodedTagValidationFailure(tag,
                    FailureType.UnknownTag,
                    "Tag could not be decoded against schema");
            builder.add(failure);
        }
        return builder.build();
    }
}
