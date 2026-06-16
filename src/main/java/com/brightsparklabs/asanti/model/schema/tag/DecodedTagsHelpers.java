/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.tag;

import static com.google.common.base.Preconditions.*;
import static com.google.common.collect.Collections2.*;

import com.brightsparklabs.asanti.data.AsnData;
import com.brightsparklabs.asanti.model.data.AsantiAsnData;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Set;

/**
 * Helper functions to deal with decoded tags, ie what you get out of {@link AsantiAsnData}
 *
 * @author brightSPARK Labs
 */
public class DecodedTagsHelpers {

    /** splitter for separating tag strings */
    private static final Splitter tagSplitter = Splitter.on("/").omitEmptyStrings();

    // ---------------------------------------------------------------------
    // CONSTRUCTION
    // ---------------------------------------------------------------------

    /** Default constructor. Not needed, everything in this class is static. */
    private DecodedTagsHelpers() {}

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Take all the tags, and build out a simulation of a tree of tags. eg /X/Y/Z, /X/Y/A will
     * become /X, /X/Y, /X/Y/Z, X/Y/A more easily giving us the ability to walk the path. Then
     * filter that down so that there are only unique children. so from the above if tag is /X then
     * filter data is /X/Y, if the tag is /X/Y then the filter data is /X/Y/Z, /X/Y/A In "tree"
     * terms we are going from only leaf nodes to all nodes.
     *
     * @param asnData the AsantiAsnData that we want to extract the tags from
     * @return the set of all tags with each item representing a node that may or may not be a leaf
     *     node
     * @throws NullPointerException if asnData is null
     */
    public static ImmutableSet<String> buildTags(final AsnData asnData) {
        checkNotNull(asnData);

        Set<String> result = Sets.newHashSet();

        for (final String tag : asnData.getTags()) {
            String currentTag = tag.endsWith("/") ? tag.substring(0, tag.length() - 1) : tag;

            /*
             * For each tag we iterate backwards (e.g. `/X/Y/Z` -> `/X/Y` -> `/X`) to build out the tree.
             * This way we can abort the loop early if we reach a "parent" that we've already evaluated.
             * E.g. when evaluating the tags `/X/Y/Z` and `/X/Y/A` we would perform
             * the following evaluations:
             *
             * /X/Y/Z
             * /X/Y
             * /X
             * /X/Y/A
             * ---- Can stop here as /X/Y has already been evaluated.
             */
            while (!currentTag.isEmpty() && !result.contains(currentTag)) {
                result.add(currentTag);
                currentTag = currentTag.substring(0, currentTag.lastIndexOf("/"));
            }
        }

        // Unlike the above, unmapped tags typically have mapped prefixes with the tail being
        // unmapped. E.g. /X/Y/0[1]/4[0]
        // Because of this it makes more sense to just split it and evaluate them from the front.
        for (final String tag : asnData.getUnmappedTags()) {
            final ImmutableList<String> tags =
                    tagSplitter
                            .splitToStream(tag)
                            // Filter out the unmapped.  We know it is unmapped because it starts
                            // with a digit.
                            .filter(t -> !Character.isDigit(t.charAt(0)))
                            .collect(ImmutableList.toImmutableList());

            final StringBuilder reconstructed = new StringBuilder();

            for (final String tagName : tags) {
                reconstructed.append("/").append(tagName);
                result.add(reconstructed.toString());
            }
        }

        return ImmutableSet.copyOf(result);
    }

    /**
     * Get the name of the immediate children of the provided parent tag.
     *
     * @param asnData the AsantiAsnData that we want to extract the tags from
     * @param parentTag the tag to get the children of
     * @return The name of the immediate children, this is trimmed of any index (eg [1]). I.e. if
     *     parentTag is "/Parent/blah" then /Parent/blah/someTag[0] turns into someTag. If parentTag
     *     is "/Parent" then /Parent/blah/someTag[0] turns into blah
     * @throws NullPointerException If asnData or parentTag are null.
     */
    public static ImmutableSet<String> getImmediateChildren(
            final AsnData asnData, final String parentTag) {
        checkNotNull(asnData);
        checkNotNull(parentTag);

        // Ensure that the parent tag we're checking against ends with a slash so that the substring
        // is consistent.
        final String tag = parentTag.endsWith("/") ? parentTag : parentTag + "/";

        final ImmutableSet<String> children =
                asnData.getAllTags().stream()
                        // Only get the tags that are children to the parent tag and have been
                        // mapped (i.e. do not start with a digit).
                        .filter(
                                t ->
                                        t != null
                                                && t.startsWith(tag)
                                                && !Character.isDigit(
                                                        t.substring(tag.length()).charAt(0)))
                        .map(
                                t -> {
                                    // Strip off the parent tag part.
                                    String str = t.substring(tag.length());

                                    // Get just the immediate child part.
                                    final int nextSlashIndex = str.indexOf("/");
                                    str =
                                            nextSlashIndex == -1
                                                    ? str
                                                    : str.substring(0, nextSlashIndex);

                                    // Remove any trailing index part.
                                    return stripIndex(str);
                                })
                        .collect(ImmutableSet.toImmutableSet());

        return children;
    }

    /**
     * Removes the index portion of a tag if it appears at the end of the input string, ie the [].
     * Note that this only works on a "single" tag, not a fully qualified tag. If the tag has no
     * index part then it will return the input tag unaltered.
     *
     * <p>Example, someTag[1] will return someTag, anotherTag[1][0] will return anotherTag.
     *
     * @param tag the tags to strip
     * @return the stripped tag
     * @throws NullPointerException if tag is null
     */
    public static String stripIndex(final String tag) {
        checkNotNull(tag);

        // We want to make sure we're working with the last "tag node" of the given tag. E.g. for
        // /foo[1]/bar[0] we want to get /foo[1]/bar back.
        final int lastSlashIndex = tag.lastIndexOf("/");
        final String lastTagNode = lastSlashIndex != -1 ? tag.substring(lastSlashIndex) : tag;

        // Has no index block at all, check no further.
        final int openBracketIndex = lastTagNode.indexOf("[");
        if (openBracketIndex == -1 || !lastTagNode.endsWith("]")) {
            return tag;
        }

        // Check if that tag ends with an index, allowing for multiple indexes to be applied (i.e.
        // foo[1][0]).;
        for (int i = openBracketIndex; i < lastTagNode.length(); i++) {
            final char character = lastTagNode.charAt(i);
            // Tag doesn't end with an index, just return the tag as is.
            if (!Character.isDigit(character) && character != '[' && character != ']') {
                return tag;
            }
        }

        // Account for the "parent tag" where appropriate.
        return tag.substring(0, Math.max(lastSlashIndex, 0) + openBracketIndex);
    }
}
