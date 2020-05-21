/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.data.AsnData;
import com.brightsparklabs.asanti.exception.DecodeException;
import com.brightsparklabs.asanti.model.data.AsantiAsnData;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveTypes;
import com.brightsparklabs.asanti.model.schema.tag.DecodedTagsHelpers;
import com.brightsparklabs.asanti.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.schema.AsnPrimitiveType;
import com.brightsparklabs.asanti.selector.Selector;
import com.brightsparklabs.asanti.selector.SelectorByTagMatch;
import com.brightsparklabs.asanti.validator.builtin.BuiltinTypeValidator;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.brightsparklabs.asanti.validator.result.ValidationResultImpl;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link com.brightsparklabs.asanti.validator.Validator}.
 *
 * @author brightSPARK Labs
 */
public class ValidatorImpl implements Validator {
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(ValidatorImpl.class);

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** custom validation rules */
    private final ImmutableMap<ValidationRule, Selector> customRules;

    /**
     * visitor to determine which {@link com.brightsparklabs.asanti.validator.ValidationRule} to
     * apply to a tag
     */
    private final ValidationVisitor validationVisitor = new ValidationVisitor();

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Private constructor. Use @link{Builder} instead.
     *
     * @param customRules custom validation rules to apply to various tags
     */
    public ValidatorImpl(Map<ValidationRule, Selector> customRules) {
        this.customRules = ImmutableMap.copyOf(customRules);
    }

    /**
     * Returns a builder for creating instances of this class.
     *
     * @return a builder for creating instances of this class.
     */
    public static Builder builder() {
        return new Builder();
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: Validator
    // -------------------------------------------------------------------------

    @Override
    public ValidationResult validate(AsnData asnData) {
        final ValidationResultImpl.Builder builder = ValidationResultImpl.builder();

        if (!(asnData instanceof AsantiAsnData)) {
            // TODO: ASN-167 we wouldn't need to handle the instance of if we could just use AsnData
            // directly
            logger.warn("Asanti cannot be used to validate AsnData produced by another library");
            return builder.build();
        }

        // validate each mapped tag
        final ImmutableSet<String> tags = DecodedTagsHelpers.buildTags(asnData);
        for (final String tag : tags) {
            // default validation
            Set<ValidationFailure> failures = validateDefault(tag, (AsantiAsnData) asnData);
            builder.addAll(failures);

            // custom validation
            failures = validateCustom(tag, asnData);
            builder.addAll(failures);
        }

        // add a failure for each unmapped tag
        for (final String tag : asnData.getUnmappedTags()) {
            final DecodedTagValidationFailure failure =
                    new DecodedTagValidationFailure(
                            tag, FailureType.UnknownTag, "Tag could not be decoded against schema");
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
     * @param asnData data to validate
     * @return the results from validating the data
     */
    private Set<ValidationFailure> validateDefault(String tag, AsantiAsnData asnData) {
        final Set<ValidationFailure> failures = Sets.newHashSet();
        final AsnPrimitiveType type =
                asnData.getPrimitiveType(tag).orElse(AsnPrimitiveTypes.INVALID);
        final BuiltinTypeValidator tagValidator =
                (BuiltinTypeValidator) type.accept(validationVisitor);
        if (tagValidator != null) {
            failures.addAll(tagValidator.validate(tag, asnData));
        }
        return failures;
    }

    /**
     * Validates the supplied tag using any custom rules in this decoder
     *
     * @param asnData data to validate
     * @return the results from validating the data
     */
    private Set<ValidationFailure> validateCustom(String tag, AsnData asnData) {
        final Set<ValidationFailure> failures = Sets.newHashSet();
        final AsnPrimitiveType primitiveType =
                asnData.getPrimitiveType(tag).orElse(AsnPrimitiveTypes.INVALID);
        final AsnBuiltinType type = primitiveType.getBuiltinType();

        final ImmutableSet<ValidationRule> rules =
                customRules.entrySet().stream()
                        .filter(e -> e.getValue().matches(tag, type, asnData))
                        .map(Entry::getKey)
                        .collect(ImmutableSet.toImmutableSet());

        rules.forEach(
                rule -> {
                    try {
                        failures.addAll(rule.validate(tag, asnData));
                    } catch (DecodeException ex) {
                        final ValidationFailure failure =
                                new DecodedTagValidationFailure(
                                        tag,
                                        FailureType.CustomValidationFailed,
                                        "Data was not in the expected format: " + ex.getMessage());
                        failures.add(failure);
                    }
                });
        return failures;
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASSES
    // -------------------------------------------------------------------------

    /** Builder for creating instances of this class */
    public static class Builder {
        // ---------------------------------------------------------------------
        // INSTANCE VARIABLES
        // ---------------------------------------------------------------------

        /** custom validation rules */
        private final ImmutableMap.Builder<ValidationRule, Selector> customRules =
                ImmutableMap.builder();

        // ---------------------------------------------------------------------
        // PUBLIC METHODS
        // ---------------------------------------------------------------------

        /**
         * Builds and returns the Validator.
         *
         * @return The built Validator.
         */
        public Validator build() {
            return new ValidatorImpl(customRules.build());
        }

        /**
         * Register a validation rule which applies to the specified selector.
         *
         * @param rule Rule to apply when a match occurs.
         * @param selector Selector to match on.
         * @return This builder.
         */
        public Builder withValidationRule(ValidationRule rule, Selector selector) {
            customRules.put(rule, selector);
            return this;
        }

        /**
         * Register a validation rule which applies to the specified tag.
         *
         * @param rule Rule to apply when a match occurs.
         * @param tag Tag to match on.
         * @return This builder.
         * @deprecated Use {@link #withValidationRule(ValidationRule, Selector)}.
         */
        @Deprecated
        public Builder withValidationRule(ValidationRule rule, String tag) {
            return withValidationRule(rule, new SelectorByTagMatch(tag));
        }

        /**
         * Register all the supplied validation rules.
         *
         * @param rules Rules to register.
         * @return This builder.
         */
        public Builder withValidationRules(Map<ValidationRule, Selector> rules) {
            customRules.putAll(rules);
            return this;
        }
    }
}
