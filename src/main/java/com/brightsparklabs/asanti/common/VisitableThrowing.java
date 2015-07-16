/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.common;

/**
 * Interface for objects which can be visited as per the Visitor pattern
 *
 * @param <ArgType>
 *         type of {@link Visitor} that can visit this object
 * @param <ExceptionType>
 *         the type of exception that can be thrown
 *
 * @author brightSPARK Labs
 */
public interface VisitableThrowing<ArgType extends Visitor, ExceptionType extends Throwable>
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Accept a visitor
     *
     * @param visitor
     *         the visitor to visit the object with
     *
     * @return the result from the visitor
     */
    public Object accept(ArgType visitor) throws ExceptionType;
}
