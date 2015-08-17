/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator.failure;

import com.brightsparklabs.assam.validator.FailureType;

/**
 * Represents a validation failure from checking a schema constraint.
 *
 * @author brightSPARK Labs
 */
public class SchemaConstraintValidationFailure extends AbstractValidationFailure
{
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param failureType
     *         the type of failure that occurred
     * @param failureReason
     *         the reason for the failure
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     * @throws IllegalArgumentException
     *         if location or failureReason are empty
     */
    public SchemaConstraintValidationFailure(FailureType failureType, String failureReason)
    {
        super(failureType, failureReason);
    }
}
