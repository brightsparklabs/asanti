/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.common;

/**
 * Interface for objects which can be visited as per the Visitor pattern
 *
 * @author brightSPARK Labs
 *
 * @param <T>
 *            type of {@link Visitor} that can visit this object
 */
public interface Visitable<T extends Visitor>
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Visits this objects with the supplied visitor
     *
     * @param visitor
     *            the visitor to visit the object with
     *
     * @return the result from the visitor
     */
    public Object visit(T visitor);
}
