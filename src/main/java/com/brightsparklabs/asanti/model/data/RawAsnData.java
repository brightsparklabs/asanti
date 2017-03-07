/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.data;

import java.util.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.regex.Pattern;

/**
 * Interface for modeling raw data (bytes) read from an ASN.1 binary file
 *
 * @author brightSPARK Labs
 */
public interface RawAsnData
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

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
     * Determines whether the data contains any tags matching the supplied regular expression
     *
     * @param regex
     *         regular expression to match tags against
     *
     * @return {@code true} if the tag is in the data; {@code false} otherwise
     */
    public boolean contains(Pattern regex);

    /**
     * Returns all tags found in the ASN data as a set of XPath like strings. The format of a singe
     * raw tag is index[tag] where index is oder within its parent that the tag was read in, and tag
     * is of the same format at an ASN.1 tag, so a context-specific tag of 2 is [2] and the implicit
     * tags for an Integer type is [UNIVERSAL 2]. <p> E.g. "/1[1]/1[UNIVERSAL 16]/1[4]",
     * "3[0]/1[4]/2[1]", "/2[UNIVERSAL 17]/4[UNIVERSAL 16]/0[UNIVERSAL 2]"
     *
     * @return all tags in the data
     */
    public ImmutableSet<String> getRawTags();

    /**
     * Returns the data (bytes) associated with the specified tag
     *
     * @param rawTag
     *         tag associated with the data
     *
     * @return data associated with the specified tag or {@link Optional#empty()} if the tag does not
     * exist
     */
    public Optional<byte[]> getBytes(String rawTag);

    /**
     * Returns a mapping of all tags to the data (bytes) associated with them <p> E.g.
     * "/0[1]/0[1]/0[1]" =&gt; "[0x00, 0x01]", "0[3]/1[1]/0[2]" =&gt; "[0x00, 0x01]"
     *
     * @return mapping of all tags to their associated data
     */
    public ImmutableMap<String, byte[]> getBytes();

    /**
     * Returns the data (bytes) from all tags matching the supplied regular expression <p> E.g.
     * <code>getBytesMatching(Pattern.compile("/0[1]/2[1]/.*")) =&gt; { "/0[1]/2[1]/0[1]" =&gt;
     * "[0x00, 0x01]", "/0[1]/2[1]/1[2]" =&gt; "[0x00, 0x01]" } </code>
     *
     * @param regex
     *         regular expression to match tags against
     *
     * @return data associated with the matching tags. Map is of form: {@code tag =&gt; data}
     */
    public ImmutableMap<String, byte[]> getBytesMatching(Pattern regex);
}
