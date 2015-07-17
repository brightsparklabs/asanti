/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.data;

import com.brightsparklabs.asanti.common.DecodeException;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.regex.Pattern;

/**
 * Interface for modeling ASN.1 data which has been mapped against a schema
 *
 * @author brightSPARK Labs
 */
public interface DecodedAsnData
{
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** null instance */
    public static final DecodedAsnData.Null NULL = new DecodedAsnData.Null();

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns all tags found in the ASN data as a set of XPath like strings. E.g.
     * "/Document/header/published/date", "/Document/header/lastModfied/date",
     * "/Document/body/prefix/text"
     *
     * @return all tags in the data
     */
    public ImmutableSet<String> getTags();

    /**
     * Returns the tags from the data which could not be mapped using the schema. E.g.
     * "/Document/body/content/99", "/Document/99/1/1"
     *
     * @return all unmapped tags in the data
     */
    public ImmutableSet<String> getUnmappedTags();

    /**
     * Determines whether the specified tag is contained in the data
     *
     * @param tag
     *         tag to check
     *
     * @return {@code true} if the tag is in the data; {@code false} otherwise
     */
    public boolean contains(String tag);

    /**
     * Gets the data (bytes) associated with the specified tag
     *
     * @param tag
     *         tag associated with the data
     *
     * @return data associated with the specified tag or an empty byte array if the tag does not
     * exist
     */
    public byte[] getBytes(String tag);

    /**
     * Gets the data (bytes) from all tags matching the supplied regular expression
     *
     * @param regex
     *         regular expression to match tags against
     *
     * @return data associated with the matching tags. Map is of form: {@code tag => data}
     */
    public ImmutableMap<String, byte[]> getBytesMatching(Pattern regex);

    /**
     * Gets the data (bytes) associated with the specified tag as a hex string
     *
     * @param tag
     *         tag associated with the data
     *
     * @return data associated with the specified tag (e.g. {@code "0x010203"}) or {@code "0x"} if
     * the tag does not exist
     */
    public String getHexString(String tag);

    /**
     * Gets the data (bytes) from all tags matching the supplied regular expression as hex strings
     *
     * @param regex
     *         regular expression to match tags against
     *
     * @return data associated with the matching tags. Map is of form: {@code tag => data}
     */
    public ImmutableMap<String, String> getHexStringsMatching(Pattern regex);

    /**
     * Gets the data (bytes) associated with the specified tag as a printable string
     *
     * @param tag
     *         tag associated with the data
     *
     * @return data associated with the specified tag or an empty string if the tag does not exist
     *
     * @throws DecodeException
     *         if any errors occur decoding the data associated with the tag
     */
    public String getPrintableString(String tag) throws DecodeException;

    /**
     * Gets the data (bytes) from all tags matching the supplied regular expression as printable
     * strings
     *
     * @param regex
     *         regular expression to match tags against
     *
     * @return data associated with the matching tags. Map is of form: {@code tag => data}
     *
     * @throws DecodeException
     *         if any errors occur decoding the data associated with the tags
     */
    public ImmutableMap<String, String> getPrintableStringsMatching(Pattern regex)
            throws DecodeException;

    /**
     * Gets the ASN.1 Type of the specified tag
     *
     * @param tag
     *         tag to retrieve the type of
     *
     * @return the ASN.1 Type Definition of the specified tag or {@link AsnPrimitiveType#NULL} if
     * the tag does not exist
     */
    public AsnSchemaType getType(String tag);

    /**
     * Gets the data (bytes) associated with the specified tag as the decoded Java object most
     * appropriate to its type
     *
     * @param tag
     *         tag associated with the data
     *
     * @return data associated with the specified tag or an empty byte array if the tag does not
     * exist
     *
     * @throws DecodeException
     *         if any errors occur decoding the data associated with the tag
     */
    public Object getDecodedObject(String tag) throws DecodeException;

    /**
     * Gets the data (bytes) from all tags matching the supplied regular expression as the decoded
     * Java object most appropriate to its type
     *
     * @param regex
     *         regular expression to match tags against
     *
     * @return data associated with the matching tags. Map is of form: {@code tag => data}
     *
     * @throws DecodeException
     *         if any errors occur decoding the data associated with the tags
     */
    public ImmutableMap<String, Object> getDecodedObjectsMatching(Pattern regex)
            throws DecodeException;

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: Null
    // -------------------------------------------------------------------------

    /**
     * Null instance of {@link DecodedAsnData}
     *
     * @author brightSPARK Labs
     */

    public static class Null implements DecodedAsnData
    {
        // ---------------------------------------------------------------------
        // IMPLEMENTATION: DecodedAsnData
        // ---------------------------------------------------------------------

        @Override
        public ImmutableSet<String> getTags()
        {
            return ImmutableSet.<String>of();
        }

        @Override
        public ImmutableSet<String> getUnmappedTags()
        {
            return ImmutableSet.<String>of();
        }

        @Override
        public boolean contains(String tag)
        {
            return false;
        }

        @Override
        public byte[] getBytes(String tag)
        {
            return new byte[0];
        }

        @Override
        public ImmutableMap<String, byte[]> getBytesMatching(Pattern regex)
        {
            return ImmutableMap.<String, byte[]>of();
        }

        @Override
        public String getHexString(String tag)
        {
            return "0x";
        }

        @Override
        public ImmutableMap<String, String> getHexStringsMatching(Pattern regex)
        {
            return ImmutableMap.<String, String>of();
        }

        @Override
        public String getPrintableString(String tag)
        {
            return "";
        }

        @Override
        public ImmutableMap<String, String> getPrintableStringsMatching(Pattern regex)
        {
            return ImmutableMap.<String, String>of();
        }

        @Override
        public AsnSchemaType getType(String tag)
        {
            return AsnSchemaType.NULL;
        }

        @Override
        public Object getDecodedObject(String tag)
        {
            return new byte[0];
        }

        @Override
        public ImmutableMap<String, Object> getDecodedObjectsMatching(Pattern regex)
        {
            return ImmutableMap.<String, Object>of();
        }
    }
}
