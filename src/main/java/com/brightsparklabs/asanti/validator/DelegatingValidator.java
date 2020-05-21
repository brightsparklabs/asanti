/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.data.AsnData;
import com.brightsparklabs.asanti.exception.DecodeException;
import com.brightsparklabs.asanti.schema.AsnPrimitiveType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.Optional;

/**
 * Class to make "smart" choice of how to Select a Validator to delegate to. This is bridging the
 * "gap" with teh extant custom validation registration where a custom validator is registered
 * against a fully qualified tag. TODO - INC-97. If we improve the custom validation rules, this
 * class may not need to exist
 *
 * @author brightSPARK Labs
 */
public class DelegatingValidator implements ValidationRule {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** validators that we can delegate to */
    private final ImmutableList<ValidatorSelector> validators;
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Constructor
     *
     * @param validators the validators to delegate to.
     */
    public DelegatingValidator(final Iterable<ValidatorSelector> validators) {
        this.validators = ImmutableList.copyOf(validators);
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION
    // -------------------------------------------------------------------------
    @Override
    public ImmutableSet<ValidationFailure> validate(final String tag, final AsnData asnData)
            throws DecodeException {
        final ImmutableSet.Builder<ValidationFailure> result = ImmutableSet.builder();
        for (final ValidatorSelector selector : validators) {
            final Optional<AsnPrimitiveType> primitiveType = asnData.getPrimitiveType(tag);
            if (primitiveType.isPresent()) {
                if (selector.matches(tag, primitiveType.get().getBuiltinType(), asnData)) {
                    result.addAll(selector.getValidator().validate(tag, asnData));
                }
            }
        }
        return result.build();
    }
}
