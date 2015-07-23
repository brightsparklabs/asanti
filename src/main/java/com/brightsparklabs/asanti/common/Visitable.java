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
 * @param <VisitorType>
 *            type of {@link Visitor} that can visit this object
 */
public interface Visitable<VisitorType extends Visitor>
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Accepts a visit from a suitable visitor
     *
     * @param visitor
     *            the visitor to visit the object with
     *
     * @return the result from the visitor
     */
    public Object accept(VisitorType visitor);
}
