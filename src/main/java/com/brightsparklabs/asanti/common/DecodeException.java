/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.common;

import com.brightsparklabs.asanti.validator.ValidationFailure;
import com.brightsparklabs.asanti.validator.ValidationResult;

/**
 * Signals that invalid data was supplied to a decoder
 *
 * @author brightSPARK Labs
 */
public class DecodeException extends Exception
{
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     *
     * @param message
     *         reason for the failure
     */
    public DecodeException(String message)
    {
        super(message);
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Throws a DecodeException if the supplied validation results contain any failures. The
     * exception will contain details of the failures.
     *
     * @throws DecodeException
     *         if the validation result contains failures
     */
    public static void throwIfHasFailures(ValidationResult validationResult) throws DecodeException
    {
        if (!validationResult.hasFailures())
        {
            return;
        }

        final StringBuilder builder = new StringBuilder();
        for (ValidationFailure failure : validationResult.getFailures())
        {
            builder.append(failure.getFailureReason()).append("\n");
        }
        throw new DecodeException(builder.toString());
    }
}
