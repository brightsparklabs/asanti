/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition;
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
    public ValidationResults validate(DecodedAsnData decodedAsnData)
    {
        final ValidationResultsImpl.Builder builder = ValidationResultsImpl.builder();
        for (final String tag : decodedAsnData.getTags())
        {
            final AsnSchemaTypeDefinition type = decodedAsnData.getType(tag);
            final ValidationRule rule = (ValidationRule) type.visit(validationVisitor);
            final ValidationResult result = rule.validate(tag, decodedAsnData);
            builder.add(result);
        }

        // process unmapped tags
        decodedAsnData.getUnmappedTags();

        return builder.build();
    }
}
