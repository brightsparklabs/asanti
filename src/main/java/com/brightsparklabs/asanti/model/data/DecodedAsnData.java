/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.data;

import com.brightsparklabs.asanti.common.DecodeException;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.google.common.base.Optional;
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
    public Optional<byte[]> getBytes(String tag);

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
    public Optional<String> getHexString(String tag);

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
    public Optional<String> getPrintableString(String tag) throws DecodeException;

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
     * @return the {@link AsnSchemaType} of the specified tag or {@link Optional#absent()} if the
     * tag does not exist. To use this without caring if there was a match, and to get a {@link
     * AsnPrimitiveType#NULL} if the tag does not exist the use {@code
     * getType(tag).or(AsnPrimitiveType.NULL)}
     */
    public Optional<AsnSchemaType> getType(String tag);

    /**
     * Gets the data (bytes) associated with the specified tag as the decoded Java object most
     * appropriate to its type
     *
     * @param tag
     *         tag associated with the data
     * @param <T>
     *         the type of data that will be returned in the {@link Optional}
     *
     * @return data associated with the specified tag or an empty byte array if the tag does not
     * exist
     *
     * @throws DecodeException
     *         if any errors occur decoding the data associated with the tag
     * @throws ClassCastException
     *         if the decoded object can't be cast to T
     */
    public <T> Optional<T> getDecodedObject(String tag) throws DecodeException, ClassCastException;

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
        public Optional<byte[]> getBytes(String tag)
        {
            return Optional.absent();
        }

        @Override
        public ImmutableMap<String, byte[]> getBytesMatching(Pattern regex)
        {
            return ImmutableMap.<String, byte[]>of();
        }

        @Override
        public Optional<String> getHexString(String tag)
        {
            return Optional.absent();
        }

        @Override
        public ImmutableMap<String, String> getHexStringsMatching(Pattern regex)
        {
            return ImmutableMap.<String, String>of();
        }

        @Override
        public Optional<String> getPrintableString(String tag)
        {
            return Optional.absent();
        }

        @Override
        public ImmutableMap<String, String> getPrintableStringsMatching(Pattern regex)
        {
            return ImmutableMap.<String, String>of();
        }

        @Override
        public Optional<AsnSchemaType> getType(String tag)
        {
            return Optional.absent();
        }

        @Override
        public <T> Optional<T> getDecodedObject(String tag)
        {
            return Optional.absent();
        }

        @Override
        public ImmutableMap<String, Object> getDecodedObjectsMatching(Pattern regex)
        {
            return ImmutableMap.<String, Object>of();
        }
    }
}
