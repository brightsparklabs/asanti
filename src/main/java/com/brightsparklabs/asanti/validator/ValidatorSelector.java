/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.selector.Selector;

/**
 * Combines a {@link Selector}, with the ability to provide a respective {@link Validator} if the
 * selection criteria is met.
 *
 * @author brightSPARK Labs
 */
public interface ValidatorSelector extends Selector {
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Allows a ValidationRule to be associated with a Selector.
     *
     * @return The wrapped ValidationRule.
     */
    ValidationRule getValidator();
}
