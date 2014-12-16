/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model;

/**
 * Interface for modeling an ASN.1 schema
 *
 * @author brightSPARK Labs
 */
public interface AsnSchema
{

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns the decoded tag for the supplied raw tag. E.g.
     * {@code getDecodedTag("/1/0/1")} => {@code "/Header/Published/Date"}
     *
     * @return the decoded tag or an empty string if it cannot be decoded
     */
    public String getDecodedTag(String rawTag);

    /**
     * Returns the raw tag for the supplied decoded tag. E.g.
     * {@code getRawTag("/Header/Published/Date")} => {@code "/1/0/1"}
     *
     * @return the raw tag or an empty string if it cannot be determined
     */
    public String getRawTag(String decodedTag);

    /**
     * Gets the data (bytes) associated with the specified tag as a printable
     * string
     *
     * @param tag
     *            tag associated with the data
     *
     * @return data associated with the specified tag or an empty string if the
     *         tag does not exist or parameters are {@code null}
     */
    public String getPrintableString(String tag, byte[] data);

    /**
     * Gets the data (bytes) associated with the specified tag as the decoded
     * Java object most appropriate to its type
     *
     * @param tag
     *            tag associated with the data
     *
     * @return data associated with the specified tag or an empty byte array if
     *         the tag does not exist or parameters are {@code null}
     */
    public Object getDecodedObject(String tag, byte[] data);
}
