/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.common;

import java.util.Optional;

/**
 * A result from an operation which was either successful or not successful. The result contains the
 * output from the operation regardless of whether the operation was successful. If the operation
 * was not successful it also contains a reason as to why it was not.
 *
 * @param <T> the type of output produced by the operation
 * @param <FailureType> specifies the type of failure object (returned in getFailureReason)
 * @author brightSPARK Labs
 */
public class OperationResult<T, FailureType> {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** whether the operation was successful */
    private final boolean wasSuccessful;

    /** the resulting output from the operation */
    private final T output;

    /** the reason the operation failed (or Option.absent() if it did not fail) */
    private final Optional<FailureType> failureReason;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     *
     * @param wasSuccessful {@code true} if the operation was successful
     * @param output the resulting output from the operation
     * @param failureReason the (Optional) reason the operation failed
     */
    private OperationResult(
            boolean wasSuccessful, final T output, final FailureType failureReason) {
        this.wasSuccessful = wasSuccessful;
        this.output = output;
        this.failureReason = Optional.ofNullable(failureReason);
    }

    /**
     * Convenience method to create a result indicating the operation was successful. Results can be
     * created via:<br>
     * {@code OperationResult<String, String> result = createSuccessfulInstance(outputString);}
     *
     * <p>Which is more concise than:<br>
     * {@code OperationResult<String> result = new OperationResult<String, String>(true,
     * outputString, null);}
     *
     * @param output the resulting output from the operation
     * @param <T> the type of the result
     * @param <FailureType> specifies the type of failure object (return in getFailureReason)
     * @return a 'successful' result instance containing the supplied data
     */
    public static <T, FailureType> OperationResult<T, FailureType> createSuccessfulInstance(
            final T output) {
        return new OperationResult<>(true, output, null);
    }

    /**
     * Convenience method to create a result indicating the operation was unsuccessful. Results can
     * be created via:<br>
     * {@code OperationResult result = createUnsuccessfulInstance(outputString, reason);}
     *
     * <p>Which is more concise than:<br>
     * {@code OperationResult<String, String> result = new OperationResult<String, String>(false,
     * outputString, reason);}
     *
     * @param output the resulting output from the operation
     * @param failureReason the reason the operation failed
     * @param <T> the type of the result
     * @param <FailureType> specifies the type of failure object (returned in getFailureReason)
     * @return an 'unsuccessful' result instance containing the supplied data
     */
    public static <T, FailureType> OperationResult<T, FailureType> createUnsuccessfulInstance(
            final T output, final FailureType failureReason) {
        return new OperationResult<>(false, output, failureReason);
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns {@code true} if the operation was successful
     *
     * @return {@code true} if the operation was successful
     */
    public boolean wasSuccessful() {
        return wasSuccessful;
    }

    /**
     * Returns the resulting output from the operation
     *
     * @return the resulting output from the operation
     */
    public T getOutput() {
        return output;
    }

    /**
     * Returns the reason the operation failed, wrapped in {@link Optional}. An example use might be
     * that FailureType is a String, to get the failure reason, or an empty string if none was
     * supplied then use {@code result.getFailureReason().or(""); }
     *
     * @return the reason the operation failed.
     */
    public Optional<FailureType> getFailureReason() {
        return failureReason;
    }
}
