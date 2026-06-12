/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.data.AsnData;
import java.util.Map;
import java.util.Optional;

/**
 * Used to validate {@link AsnData} against its associated schema or a custom validation rule.
 *
 * @author brightSPARK Labs
 */
public interface Validator {
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Validates the supplied data using the rules in this validator.
     *
     * @param asnData The data to validate.
     * @return The results from validating the data.
     */
    ValidationResult validate(AsnData asnData);

    /**
     * Validates the supplied data using the rules in this validator.
     *
     * @param asnData The data to validate.
     * @param decodedTagValuesCache Optional cache to lookup and add decoded tag values.
     * @param decodedTagPrintableStringCache Optional cache to lookup and add decoded tag printable
     *     string values.
     * @return The results from validating the data.
     */
    ValidationResult validate(
            AsnData asnData,
            Optional<Map<String, Optional<Object>>> decodedTagValuesCache,
            Optional<Map<String, Optional<String>>> decodedTagPrintableStringCache);
}
