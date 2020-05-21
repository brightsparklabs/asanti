/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.visitor;

/**
 * Interface for objects which can be visited as per the Visitor pattern. Use this is version if the
 * visitation may/will throw exceptions that need to propagate.
 *
 * @param <VisitorType> type of {@link Visitor} that can visit this object
 * @param <ExceptionType> the type of exception that can be thrown
 * @author brightSPARK Labs
 */
public interface VisitableThrowing<VisitorType extends Visitor, ExceptionType extends Throwable> {
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Accept a visitor
     *
     * @param visitor the visitor to visit the object with
     * @return the result from the visitor
     * @throws ExceptionType dependent on visitor implementation
     */
    Object accept(VisitorType visitor) throws ExceptionType;
}
