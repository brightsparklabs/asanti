/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.model.data.AsantiAsnData;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveTypes;
import com.brightsparklabs.asanti.model.schema.tag.DecodedTagsHelpers;
import com.brightsparklabs.asanti.validator.builtin.BuiltinTypeValidator;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.brightsparklabs.asanti.validator.result.ValidationResultImpl;
import com.brightsparklabs.assam.data.AsnData;
import com.brightsparklabs.assam.schema.AsnPrimitiveType;
import com.brightsparklabs.assam.validator.FailureType;
import com.brightsparklabs.assam.validator.ValidationResult;
import com.brightsparklabs.assam.validator.ValidationRule;
import com.brightsparklabs.assam.validator.Validator;
import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link Validator}.
 *
 * @author brightSPARK Labs
 */
public class ValidatorImpl implements Validator
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(ValidatorImpl.class);

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** visitor to determine which {@link ValidationRule} to apply to a tag */
    private final ValidationVisitor validationVisitor = new ValidationVisitor();

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: Validator
    // -------------------------------------------------------------------------

    @Override
    public ValidationResult<DecodedTagValidationFailure> validate(AsnData asnData)
    {
        if (asnData instanceof AsantiAsnData)
        {
            return validate((AsantiAsnData) asnData);
        }

        // TODO: ASN-167 we wouldn't need to handle the instance of if we could just use AsnData directly
        logger.warn("Asanti cannot be used to validate AsnData produced by another library");
        return ValidationResultImpl.builder().build();
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * Validates
     *
     * @param asnData
     *
     * @return
     */
    private ValidationResult<DecodedTagValidationFailure> validate(AsantiAsnData asnData)
    {
        final ValidationResultImpl.Builder builder = ValidationResultImpl.builder();

        final ImmutableSet<String> tags = DecodedTagsHelpers.buildTags(asnData);

        for (final String tag : tags)
        {
            final AsnPrimitiveType type = asnData.getPrimitiveType(tag)
                    .or(AsnPrimitiveTypes.INVALID);
            final BuiltinTypeValidator tagValidator = (BuiltinTypeValidator) type.accept(
                    validationVisitor);
            if (tagValidator != null)
            {
                final ImmutableSet<DecodedTagValidationFailure> failures
                        = tagValidator.validate(tag, asnData);
                builder.addAll(failures);
            }
        }

        // add a failure for each unmapped tag
        for (final String tag : asnData.getUnmappedTags())
        {
            final DecodedTagValidationFailure failure = new DecodedTagValidationFailure(tag,
                    FailureType.UnknownTag,
                    "Tag could not be decoded against schema");
            builder.add(failure);
        }

        return builder.build();
    }
}
