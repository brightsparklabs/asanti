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
import com.brightsparklabs.assam.exception.DecodeException;
import com.brightsparklabs.assam.schema.AsnPrimitiveType;
import com.brightsparklabs.assam.validator.*;
import com.google.common.collect.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

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

    /** custom validation rules */
    private final ImmutableSetMultimap<String, ValidationRule> customRules;

    /** visitor to determine which {@link ValidationRule} to apply to a tag */
    private final ValidationVisitor validationVisitor = new ValidationVisitor();

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Private constructor. Use @link{Builder} instead.
     *
     * @param customRules
     *         custom validation rules to apply to various tags
     */
    public ValidatorImpl(Multimap<String, ValidationRule> customRules)
    {
        this.customRules = ImmutableSetMultimap.copyOf(customRules);
    }

    /**
     * Returns a builder for creating instances of this class.
     *
     * @return a builder for creating instances of this class.
     */
    public static Builder builder()
    {
        return new Builder();
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: Validator
    // -------------------------------------------------------------------------

    @Override
    public ValidationResult validate(AsnData asnData)
    {
        final ValidationResultImpl.Builder builder = ValidationResultImpl.builder();

        // validate each mapped tag
        if (asnData instanceof AsantiAsnData)
        {
            final ImmutableSet<String> tags = DecodedTagsHelpers.buildTags(asnData);
            for (final String tag : tags)
            {
                // default validation
                Set<ValidationFailure> failures = validateDefault(tag, (AsantiAsnData) asnData);
                builder.addAll(failures);

                // custom validation
                failures = validateCustom(tag, asnData);
                builder.addAll(failures);
            }
        }
        else
        {
            // TODO: ASN-167 we wouldn't need to handle the instance of if we could just use AsnData directly
            logger.warn("Asanti cannot be used to validate AsnData produced by another library");
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

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * Validates the supplied data using the default ASN.1 schema rules
     *
     * @param asnData
     *         data to validate
     *
     * @return the results from validating the data
     */
    private Set<ValidationFailure> validateDefault(String tag, AsantiAsnData asnData)
    {
        final Set<ValidationFailure> failures = Sets.newHashSet();
        final AsnPrimitiveType type = asnData
                .getPrimitiveType(tag)
                .orElse(AsnPrimitiveTypes.INVALID);
        final BuiltinTypeValidator tagValidator = (BuiltinTypeValidator) type.accept(
                validationVisitor);
        if (tagValidator != null)
        {
            failures.addAll(tagValidator.validate(tag, asnData));
        }
        return failures;
    }

    /**
     * Validates the supplied tag using any custom rules in this decoder
     *
     * @param asnData
     *         data to validate
     *
     * @return the results from validating the data
     */
    private Set<ValidationFailure> validateCustom(String tag, AsnData asnData)
    {
        final Set<ValidationFailure> failures = Sets.newHashSet();
        final ImmutableSet<ValidationRule> rules = customRules.get(tag);
        for (ValidationRule rule : rules)
        {
            try
            {
                failures.addAll(rule.validate(tag, asnData));
            }
            catch (DecodeException ex)
            {
                final ValidationFailure failure = new DecodedTagValidationFailure(tag,
                        FailureType.CustomValidationFailed,
                        "Data was not in the expected format: " + ex.getMessage());
                failures.add(failure);
            }
        }
        return failures;
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASSES
    // -------------------------------------------------------------------------

    /**
     * Builder for creating instances of this class
     */
    public static class Builder
    {
        // ---------------------------------------------------------------------
        // INSTANCE VARIABLES
        // ---------------------------------------------------------------------

        /** custom validation rules */
        private final HashMultimap<String, ValidationRule> customRules = HashMultimap.create();

        // ---------------------------------------------------------------------
        // PUBLIC METHODS
        // ---------------------------------------------------------------------

        public Validator build()
        {
            return new ValidatorImpl(customRules);
        }

        public Builder withValidationRule(ValidationRule rule, String tag)
        {
            customRules.put(tag, rule);
            return this;
        }
    }
}
