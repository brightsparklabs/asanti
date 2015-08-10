/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.data;

import com.brightsparklabs.asanti.common.DecodeException;
import com.brightsparklabs.asanti.decoder.AsnByteDecoder;
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
public interface AsnData
{
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
     * "/Document/body/content/99", "/Document/0[99]/0[1]/0[1]"
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
     * Gets the data (bytes) associated with the specified tag. <p>Note that because this method is
     * returning unprocessed bytes it will provide results for partially matched tags as well as
     * "raw" tags</p>
     *
     * @param tag
     *         tag associated with the data
     *
     * @return data associated with the specified tag or {@code Optional.absent()} if the tag does
     * not exist
     */
    public Optional<byte[]> getBytes(String tag);

    /**
     * Gets the data (bytes) from all tags matching the supplied regular expression.
     *
     * @param regex
     *         regular expression to match tags against
     *
     * @return data associated with the matching tags. Map is of form: {@code tag => data}
     */
    public ImmutableMap<String, byte[]> getBytesMatching(Pattern regex);

    /**
     * Gets the data (bytes) associated with the specified tag as a hex string.  <p>Note that
     * because this method is returning unprocessed bytes it will provide results for partially
     * matched tags as well as "raw" tags</p>
     *
     * @param tag
     *         tag associated with the data
     *
     * @return data associated with the specified tag (e.g. {@code "0x010203"}) or {@code
     * Optional.absent()} if the tag does not exist
     */
    public Optional<String> getHexString(String tag);

    /**
     * Gets the data (bytes) from all tags matching the supplied regular expression as hex strings.
     *
     * @param regex
     *         regular expression to match tags against
     *
     * @return data associated with the matching tags. Map is of form: {@code tag => data}
     */
    public ImmutableMap<String, String> getHexStringsMatching(Pattern regex);

    /**
     * Gets the data (bytes) associated with the specified tag as a printable string.  <p>Note that
     * because this method needs to process the bytes in a way that requires knowing the tags type
     * it will only return results for fully decoded tags</p>
     *
     * @param tag
     *         tag associated with the data
     *
     * @return data associated with the specified tag or {@code Optional.absent()} if the tag does
     * not exist
     *
     * @throws DecodeException
     *         if any errors occur decoding the data associated with the tag
     */
    public Optional<String> getPrintableString(String tag) throws DecodeException;

    /**
     * Gets the data (bytes) from all tags matching the supplied regular expression as printable
     * strings.<p>Note that because this method needs to process the bytes in a way that requires
     * knowing the tags type it will only return results for fully decoded tags</p>
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
     * @return the {@link AsnSchemaType} of the specified tag or {@code Optional.absent()} if the
     * tag does not exist. To use this without caring if there was a match, and to get a {@link
     * AsnPrimitiveType#INVALID} if the tag does not exist then use {@code
     * getType(tag).or(AsnPrimitiveType.INVALID)}
     */
    public Optional<AsnSchemaType> getType(String tag);

    /**
     * Gets the data associated with the specified tag as the decoded Java object most appropriate
     * to its type.<p>Note that because this method needs to process the bytes in a way that
     * requires knowing the tags' type it will only return results for fully decoded tags</p>
     *
     * @param tag
     *         tag associated with the data
     * @param <T>
     *         the type of data that will be returned in the {@link Optional}
     *
     * @return data associated with the specified tag or {@code Optional.absent()} if the tag does
     * not exist <p>Note: There is no inherit type safety with this method.  The type of object
     * returned will match the tag.  If the caller mis-aligns the return type with the actual type
     * dictated by the schema then on result.get() (from the returned optional) a ClassCastException
     * will likely be thrown.</p><p>To see the expected Java object type for each ASN.1 schema type
     * see {@link AsnByteDecoder}</p>
     *
     * @throws DecodeException
     *         if any errors occur decoding the data associated with the tag
     */
    public <T> Optional<T> getDecodedObject(String tag) throws DecodeException;

    /**
     * Gets the data from all tags matching the supplied regular expression as the decoded Java
     * object most appropriate to its type.<p>Note that because this method needs to process the
     * bytes in a way that requires knowing the tags type it will only return results for fully
     * decoded tags</p>
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
}
