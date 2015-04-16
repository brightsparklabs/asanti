/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.validator.builtin.BuiltinTypeValidator;
import com.brightsparklabs.asanti.validator.result.DecodedAsnDataValidationResultImpl;
import com.brightsparklabs.asanti.validator.result.DecodedDataValidationResult;
import com.brightsparklabs.asanti.validator.result.DecodedTagValidationResult;
import com.brightsparklabs.asanti.validator.rule.ValidationRule;

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
    public DecodedDataValidationResult validate(DecodedAsnData decodedAsnData)
    {
        final DecodedAsnDataValidationResultImpl.Builder builder
                = DecodedAsnDataValidationResultImpl.builder();
        for (final String tag : decodedAsnData.getTags())
        {
            final AsnSchemaTypeDefinition type = decodedAsnData.getType(tag);
            final BuiltinTypeValidator tagValidator = (BuiltinTypeValidator) type.visit(
                    validationVisitor);
            final DecodedTagValidationResult result = tagValidator.validate(tag, decodedAsnData);
            builder.addAll(result);
        }

        // TODO: ASN-92 process unmapped tags
        decodedAsnData.getUnmappedTags();

        return builder.build();
    }
}
