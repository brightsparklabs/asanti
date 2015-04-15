/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator;

import static com.google.common.base.Preconditions.*;

/**
 * Default implementation of {@link ValidationFailure}
 *
 * @author brightSPARK Labs
 */
public class ValidationFailureImpl implements ValidationFailure
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the location (e.g. location) the validation result pertains to */
    private final String location;

    /** the type of failure that occurred */
    private final FailureType failureType;

    /** the reason for the failure */
    private final String failureReason;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param location
     *         the location the validation result pertains to
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
    public ValidationFailureImpl(String location, FailureType failureType, String failureReason)
    {
        checkNotNull(location);
        checkNotNull(failureType);
        checkNotNull(failureReason);
        this.location = location.trim();
        checkArgument(!this.location.isEmpty(), "Tag cannot be empty");
        this.failureType = failureType;
        this.failureReason = failureReason.trim();
        checkArgument(!this.failureReason.isEmpty(), "Failure reason cannot be empty");
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: ValidationFailure
    // -------------------------------------------------------------------------

    @Override
    public String getLocation()
    {
        return location;
    }

    @Override
    public FailureType getFailureType()
    {
        return failureType;
    }

    @Override
    public String getFailureReason()
    {
        return failureReason;
    }
}
