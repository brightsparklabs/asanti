/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.selector;

import com.brightsparklabs.assam.selector.Selector;

/** @author brightSPARK Labs */
public abstract class NonCachableSelector implements Selector {
    // -------------------------------------------------------------------------
    // IMPLEMENTATION
    // -------------------------------------------------------------------------

    @Override
    public boolean cachable() {
        return false;
    }
}
