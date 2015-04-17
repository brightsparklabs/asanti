/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.common;

import com.brightsparklabs.asanti.validator.failure.ValidationFailure;
import com.google.common.collect.Iterables;

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
     * Throws a DecodeException if the supplied iterable is non-empty (i.e. it contains failures).
     * The exception will contain details of the failures.
     *
     * @param failures
     *         iterable to check through for failures
     *
     * @throws DecodeException
     *         if the iterable is non-empty
     */
    public static void throwIfHasFailures(Iterable<? extends ValidationFailure> failures)
            throws DecodeException
    {
        if (Iterables.isEmpty(failures))
        {
            return;
        }

        final StringBuilder builder = new StringBuilder();
        for (ValidationFailure failure : failures)
        {
            builder.append(failure.getFailureReason()).append("\n");
        }
        throw new DecodeException(builder.toString());
    }
}
