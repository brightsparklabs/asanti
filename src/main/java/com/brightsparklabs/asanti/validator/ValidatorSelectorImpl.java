/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.data.AsnData;
import com.brightsparklabs.asanti.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.selector.Selector;

/**
 * @author brightSPARK Labs
 */
public class ValidatorSelectorImpl implements ValidatorSelector {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** The Selector to use to determine criteria match. */
    private final Selector selector;

    /** The wrapped decoder. */
    private final ValidationRule validator;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param selector The Selector to use to determine criteria match.
     * @param validator The wrapped decoder.
     */
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
