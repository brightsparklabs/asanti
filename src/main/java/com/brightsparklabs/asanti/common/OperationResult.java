/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.common;

import com.google.common.base.Strings;

/**
 * A result from an operation which was either successful or not successful. The result contains the output from the
 * operation regardless of whether the operation was successful. If the operation was not successful it also contains a
 * reason as to why it was not.
 *
 * @param <T>
 *         the type of output produced by the operation
 *
 * @author brightSPARK Labs
 */
public class OperationResult<T>
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** whether the operation was successful */
    private final boolean wasSuccessful;

    /** the resulting output from the operation */
    private final T output;

    /** the reason the operation failed (or an empty string if it did not fail) */
    private final String failureReason;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     *
     * @param wasSuccessful
     *         {@code true} if the operation was successful
     * @param output
     *         the resulting output from the operation
     * @param failureReason
     *         the reason the operation failed (or an empty string if it did not fail)
     */
    private OperationResult(boolean wasSuccessful, T output, String failureReason)
    {
        this.wasSuccessful = wasSuccessful;
        this.output = output;
        this.failureReason = Strings.nullToEmpty(failureReason).trim();
    }

    /**
     * Convenience method to create a result indicating the operation was successful. Results can be created via:<br>
     * {@code OperationResult<String> result = createSuccessfulInstance(outputString);}
     *
     * <p>Which is more concise than:<br> {@code OperationResult<String> result = new OperationResult<String>(true,
     * outputString, "");}
     *
     * @param output
     *         the resulting output from the operation
     * @param <T>
     *         the type of the result
     *
     * @return a 'successful' result instance containing the supplied data
     */
    public static <T> OperationResult<T> createSuccessfulInstance(T output)
    {
        return new OperationResult<T>(true, output, "");
    }

    /**
     * Convenience method to create a result indicating the operation was unsuccessful. Results can be created via:<br>
     * {@code OperationResult<String> result = createUnsuccessfulInstance(outputString);}
     *
     * <p>Which is more concise than:<br> {@code OperationResult<String> result = new OperationResult<String>(false,
     * outputString, reason);}
     *
     * @param output
     *         the resulting output from the operation
     * @param failureReason
     *         the reason the operation failed
     * @param <T>
     *         the type of the result
     *
     * @return an 'unsuccessful' result instance containing the supplied data
     */
    public static <T> OperationResult<T> createUnsuccessfulInstance(T output, String failureReason)
    {
        return new OperationResult<T>(false, output, failureReason);
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns {@code true} if the operation was successful
     *
     * @return {@code true} if the operation was successful
     */
    public boolean wasSuccessful()
    {
        return wasSuccessful;
    }

    /**
     * Returns the resulting output from the operation
     *
     * @return the resulting output from the operation
     */
    public T getOutput()
    {
        return output;
    }

    /**
     * Returns the reason the operation failed
     *
     * @return the reason the operation failed (or an empty string if it did not fail)
     */
    public String getFailureReason()
    {
        return failureReason;
    }
}
