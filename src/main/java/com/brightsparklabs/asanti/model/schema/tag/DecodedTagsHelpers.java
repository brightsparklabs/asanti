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
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
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
     * Builds the map where the keys are the "tree" of tags described in {@link
     * #buildTags(AsnData)}, and the values are the immediate children of each tag as described in
     * {@link #getImmediateChildren(AsnData, String)}.
     *
     * <p>Combining this operation into one makes it so that we only have to evaluate each tag in
     * the data once.
     *
     * @param asnData The {@link AsnData} that contains the tags.
     * @return A map of tags where the keys represent a node that may or may not be a leaf node, and
     *     the values are the node's immediate children.
     * @throws NullPointerException If asnData is null.
     */
    public static ImmutableMap<String, ImmutableSet<String>> buildTagsWithImmediateChildren(
            final AsnData asnData) {
        checkNotNull(asnData);

        final Map<String, Set<String>> tagsToChildren = new HashMap<>();

        for (final String tag : asnData.getTags()) {

            // Make sure the tag doesn't end with a slash.
            String currentTag = tag.endsWith("/") ? tag.substring(0, tag.length() - 1) : tag;

            // Even if the tag has no children we still want to capture it in the map.
            tagsToChildren.computeIfAbsent(currentTag, (_) -> new HashSet<>());

            /*
             * For each tag we iterate backwards (e.g. `/X/Y/Z` -> `/X/Y` -> `/X`) to build out the tree
             * and record the immediate children. This way we can abort the loop early if we reach a "parent"
             * that we've already evaluated. E.g. when evaluating the tags `/X/Y/Z` and `/X/Y/A` we would perform
             * the following evaluations:
             *
             * /X/Y/Z -> [] (no children)
             * /X/Y -> [Z]
             * /X -> [Y]
             * /X/Y/A -> [] (no children)
             * /X/Y -> [Z, A]
             * ---- Can stop here as /X/Y has already been evaluated.
             */
            while (!currentTag.isEmpty()) {

                final String parentTag = currentTag.substring(0, currentTag.lastIndexOf("/"));

                // Parent is empty so we've reached the end.
                if (parentTag.isEmpty()) {
                    break;
                }

                // Check if the parent has been seen **before** we add the current tag as a child.
                // Even if it has been validated we still want to add this tag as one of its
                // immediate children.
                final boolean parentEvaluated = tagsToChildren.containsKey(parentTag);
                // Chop off the parent from this tag and remove any indices.
                final String currentTagWithoutParent =
                        stripIndex(currentTag.substring(parentTag.length() + 1));

                tagsToChildren
                        .computeIfAbsent(parentTag, (_) -> new HashSet<>())
                        .add(currentTagWithoutParent);

                // The parent has been evaluated so we don't need to proceed further with this tag.
                if (parentEvaluated) {
                    break;
                }

                currentTag = parentTag;
            }
        }

        for (final String tag : asnData.getUnmappedTags()) {
            final ImmutableList<String> tags =
                    tagSplitter
                            .splitToStream(tag)
                            // Filter out the unmapped.  We know it is unmapped because it starts
                            // with a digit.
                            .filter(t -> !Character.isDigit(t.charAt(0)))
                            .collect(ImmutableList.toImmutableList());

            final StringBuilder reconstructed = new StringBuilder();

            // Unlike the above, unmapped tags typically have mapped prefixes with the tail being
            // unmapped. E.g. /X/Y/0[1]/4[0]
            // Because of this it makes more sense to just split it and evaluate them from the
            // front.
            for (int i = 0; i < tags.size(); i++) {
                final String currentTag = tags.get(i);
                final String parentTag = reconstructed.append("/").append(currentTag).toString();

                final Optional<String> immediateChild =
                        i + 1 >= tags.size() ? Optional.empty() : Optional.of(tags.get(i + 1));
                final Set<String> tagChildren =
                        tagsToChildren.computeIfAbsent(parentTag, (k) -> new HashSet<>());

                immediateChild.ifPresent(tagChildren::add);
            }
        }

        return tagsToChildren.entrySet().stream()
                .collect(
                        ImmutableMap.toImmutableMap(
                                Entry::getKey, entry -> ImmutableSet.copyOf(entry.getValue())));
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
     * <p>Example, someTag[1] will return someTag.
     *
     * @param tag the tags to strip
     * @return the stripped tag
     * @throws NullPointerException if tag is null
     */
    public static String stripIndex(String tag) {
        checkNotNull(tag);
        // Has no index block at all, check no further.
        final int openBracketIndex = tag.indexOf("[");
        if (openBracketIndex == -1 || !tag.endsWith("]")) {
            return tag;
        }

        // Get the end of the tag from the opening square bracket to the end minus one (to account
        // for the closing bracket) and check if the enclosed substring is a number.
        final boolean hasIndexEnding =
                tag.substring(openBracketIndex + 1, tag.length() - 1)
                        .chars()
                        .allMatch(Character::isDigit);

        return hasIndexEnding ? tag.substring(0, openBracketIndex) : tag;
    }
}
