/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * Interface for modeling raw data (bytes) read from an ASN.1 binary file
 *
 * @author brightSPARK Labs <enquire@brightsparklabs.com>
 */
public interface AsnData
{

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns all tags found in the ASN data as a set of XPath like strings.
     * E.g. "/1/1/1", "3/1/2", "/2/4/1"
     *
     * @return all tags in the data
     */
    public ImmutableSet<String> getTags();

    /**
     * Returns a mapping of all tags to the data (bytes) associated with them
     * E.g. "/1/1/1" => "[0x00, 0x01]", "3/1/2" => "[0x00, 0x01]"
     *
     * @return mapping of all tags to their associated data
     */
    public ImmutableMap<String, byte[]> getData();
}
