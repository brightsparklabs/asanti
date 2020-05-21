/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.visitor;

/**
 * Interface for objects which can be visited as per the Visitor pattern
 *
 * @param <VisitorType> type of {@link Visitor} that can visit this object
 * @author brightSPARK Labs
 */
public interface Visitable<VisitorType extends Visitor> {
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Accepts a visit from a suitable visitor
     *
     * @param visitor the visitor to visit the object with
     * @return the result from the visitor
     */
    Object accept(VisitorType visitor);
}
