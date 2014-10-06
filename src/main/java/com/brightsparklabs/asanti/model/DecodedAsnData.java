/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model;

import com.google.common.collect.ImmutableSet;

/**
 * Interface for modeling ASN.1 data which has been mapped against a schema
 *
 * @author brightSPARK Labs <enquire@brightsparklabs.com>
 */
public interface DecodedAsnData
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** null instance of interface */
    public static final DecodedAsnData NULL = new DecodedAsnDataNull();

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns all tags found in the ASN data as a set of XPath like strings.
     * E.g. "/Header/Published/Date", "/Header/LastModfied/Date",
     * "/Body/Prefix/Text"
     *
     * @return all tags in the data
     */
    public ImmutableSet<String> getTags();

    /**
     * Returns the tags from the data which could not be mapped using the
     * schema. E.g. "/Body/Content/99", "/99/1/1"
     *
     * @return all unmapped tags in the data
     */
    public ImmutableSet<String> getUnmappedTags();

    /**
     * Determines whether the specified tag is contained in the data
     *
     * @param tag
     *            tag to check
     *
     * @return {@code true} if the tag is in the data; {@code false} otherwise
     */
    public boolean contains(String tag);

    /**
     * Gets the data (bytes) associated with the specified tag
     *
     * @param tag
     *            tag associated with the data
     *
     * @return data associated with the specified tag or an empty byte array if
     *         the tag does not exist
     */
    public byte[] getBytes(String tag);

    /**
     * Gets the data (bytes) associated with the specified tag as a hex string
     *
     * @param tag
     *            tag associated with the data
     *
     * @return data associated with the specified tag (e.g. {@code "0x010203"})
     *         or {@code "0x"} if the tag does not exist
     */
    public String getHexString(String tag);

    /**
     * Gets the data (bytes) associated with the specified tag as a printable
     * string
     *
     * @param tag
     *            tag associated with the data
     *
     * @return data associated with the specified tag or an empty string if the
     *         tag does not exist
     */
    public String getPrintableString(String tag);

    /**
     * Gets the data (bytes) associated with the specified tag as the decoded
     * Java object most appropriate to its type
     *
     * @param tag
     *            tag associated with the data
     *
     * @return data associated with the specified tag or an empty byte array if
     *         the tag does not exist
     */
    public Object getDecodedObject(String tag);
}
