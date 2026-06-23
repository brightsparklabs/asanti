/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.selector;

/**
 * Base class for selectors that cannot be cached.
 *
 * @author brightSPARK Labs
 */
public abstract class NonCachableSelector implements Selector {
    // -------------------------------------------------------------------------
    // IMPLEMENTATION
    // -------------------------------------------------------------------------

    @Override
    public boolean cachable() {
        return false;
    }
}
