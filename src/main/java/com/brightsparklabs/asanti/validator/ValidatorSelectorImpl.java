/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.assam.data.AsnData;
import com.brightsparklabs.assam.schema.AsnBuiltinType;
import com.brightsparklabs.assam.selector.Selector;
import com.brightsparklabs.assam.validator.ValidationRule;
import com.brightsparklabs.assam.validator.ValidatorSelector;

/** @author brightSPARK Labs */
public class ValidatorSelectorImpl implements ValidatorSelector {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the Selector to use to determine criteria match */
    private final Selector selector;

    /** the wrapped decoder */
    private final ValidationRule validator;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    public ValidatorSelectorImpl(final Selector selector, final ValidationRule validator) {
        this.selector = selector;
        this.validator = validator;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION
    // -------------------------------------------------------------------------
    @Override
    public boolean matches(final String tag, final AsnBuiltinType type, final AsnData asnData) {
        return selector.matches(tag, type, asnData);
    }

    @Override
    public boolean cachable() {
        return selector.cachable();
    }

    @Override
    public ValidationRule getValidator() {
        return validator;
    }
}
