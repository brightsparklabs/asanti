/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.common;

import com.brightsparklabs.assam.exception.DecodeException;
import com.brightsparklabs.assam.validator.ValidationFailure;
import com.google.common.collect.Iterables;

/**
 * Signals that invalid data was supplied to a decoder
 *
 * @author brightSPARK Labs
 */
public class DecodeExceptions {
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /** Default constructor. Private for static class. */
    private DecodeExceptions() {
        // static class should never be initialized
        throw new AssertionError();
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Throws a DecodeExceptions if the supplied iterable is non-empty (i.e. it contains failures).
     * The exception will contain details of the failures.
     *
     * @param failures iterable to check through for failures
     * @throws DecodeException if the iterable is non-empty
     */
    public static void throwIfHasFailures(final Iterable<? extends ValidationFailure> failures)
            throws DecodeException {
        if (Iterables.isEmpty(failures)) {
            return;
        }

        final StringBuilder builder = new StringBuilder();
        for (final ValidationFailure failure : failures) {
            builder.append(failure.getFailureReason()).append("\n");
        }
        throw new DecodeException(builder.toString());
    }
}
