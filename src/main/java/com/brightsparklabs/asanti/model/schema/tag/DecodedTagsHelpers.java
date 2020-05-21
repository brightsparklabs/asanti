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
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper functions to deal with decoded tags, ie what you get out of {@link AsantiAsnData}
 *
 * @author brightSPARK Labs
 */
public class DecodedTagsHelpers {

    /** splitter for separating tag strings */
    private static final Splitter tagSplitter = Splitter.on("/").omitEmptyStrings();

    /**
     * regex to determine if a tag starts with a digit, for decoded tags that means it is unmapped
     */
    private static final Pattern PATTERN_STARTS_WITH_DIGIT = Pattern.compile("^(\\d+.*)");

    /** regex to determine if the tag has index component */
    private static final Pattern PATTERN_HAS_INDEX = Pattern.compile("^(.+?)(\\[[0-9]+\\])+$");

    /** predicate used for collection filtering (remove entries that start with a digit */
    private static final DoesNotStartWithDigit filterOutDigits = new DoesNotStartWithDigit();

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
    public static ImmutableSet<String> buildTags(AsnData asnData) {
        checkNotNull(asnData);

        Set<String> result = Sets.newHashSet();

        // we have to work through all tags (even unmapped), because the
        // unmapped tags MAY be the only ones with some of the (decoded) path in them.
        for (final String tag : asnData.getTags()) {
            final ArrayList<String> tags = Lists.newArrayList(tagSplitter.split(tag));

            StringBuilder reconstructed = new StringBuilder("");
            for (final String tagName : tags) {
                reconstructed.append("/").append(tagName);
                result.add(reconstructed.toString());
            }
        }

        for (final String tag : asnData.getUnmappedTags()) {
            final ArrayList<String> tags = Lists.newArrayList(tagSplitter.split(tag));
            final StringBuilder reconstructed = new StringBuilder("");
            // filter out the unmapped.  We know it is unmapped because it starts
            // with a digit
            for (final String tagName : Collections2.filter(tags, filterOutDigits)) {
                reconstructed.append("/").append(tagName);
                result.add(reconstructed.toString());
            }
        }

        return ImmutableSet.copyOf(result);
    }

    /**
     * get the name of the immediate children of the provided parent tag
     *
     * @param asnData the AsantiAsnData that we want to extract the tags from
     * @param parentTag the tag to get the children of
     * @return just the name of the immediate children, this is trimmed of any index (eg [1]), ie if
     *     parentTag is "/Parent/blah" then /Parent/blah/someTag[0] turns in to someTag. If
     *     parentTag is "/Parent" then /Parent/blah/someTag[0] turns in to blah
     * @throws NullPointerException if asnData or parentTag are null
     */
    public static ImmutableSet<String> getImmediateChildren(AsnData asnData, String parentTag) {
        checkNotNull(asnData);
        checkNotNull(parentTag);
        // Instead of building out out all the tags, first filter by things that have at least our
        // tag
        // in them.
        final String tag = parentTag.endsWith("/") ? parentTag : parentTag + "/";
        final ImmutableSet<String> filtered =
                ImmutableSet.<String>builder()
                        .addAll(Collections2.filter(asnData.getTags(), new OnlyStartsWith(tag)))
                        .addAll(
                                Collections2.filter(
                                        asnData.getUnmappedTags(), new OnlyStartsWith(tag)))
                        .build();

        return ImmutableSet.copyOf(
                Collections2.filter(
                        transform(filtered, new TransformToJustChildName(parentTag)),
                        filterOutDigits));
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
        final Matcher matcher = PATTERN_HAS_INDEX.matcher(tag);
        return (matcher.matches()) ? matcher.replaceFirst("$1") : tag;
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: OnlyStartsWith
    // -------------------------------------------------------------------------

    /** Predicate to filter the tags so that only the tags starting with the parent are returned. */
    private static class DoesNotStartWithDigit implements Predicate<String> {

        // -------------------------------------------------------------------------
        // CONSTRUCTION
        // -------------------------------------------------------------------------

        /** Default Constructor */
        DoesNotStartWithDigit() {}

        // ---------------------------------------------------------------------
        // IMPLEMENTATION: Predicate
        // ---------------------------------------------------------------------
        @Override
        public boolean apply(final String input) {
            final Matcher matcher = PATTERN_STARTS_WITH_DIGIT.matcher(input);
            return !matcher.matches();
        }
    }

    /** Predicate to filter the tags so that only the tags starting with the parent are returned. */
    private static class OnlyStartsWith implements Predicate<String> {
        private final String parentTag;

        // -------------------------------------------------------------------------
        // CONSTRUCTION
        // -------------------------------------------------------------------------

        /**
         * Default Constructor
         *
         * @param parentTag the parent tag we are finding children for
         */
        OnlyStartsWith(String parentTag) {
            this.parentTag = parentTag;
        }

        // ---------------------------------------------------------------------
        // IMPLEMENTATION: Predicate
        // ---------------------------------------------------------------------
        @Override
        public boolean apply(final String input) {
            return input != null && input.startsWith(parentTag);
        }
    }

    /**
     * get just the child name, and get rid of any 'indexes', ie if parentTag is "/Parent/blah" then
     * /Parent/blah/someTag[0] turns in to someTag if parentTag is "/Parent" then
     * /Parent/blah/someTag[0] turns in to blah.
     *
     * <p>Will return an empty string if the input does not start with the expected parent tag, or
     * does not have any child
     */
    private static class TransformToJustChildName implements Function<String, String> {

        private final String parentTag;

        private final int index;

        TransformToJustChildName(String parentTag) {
            checkNotNull(parentTag);
            this.parentTag = parentTag.endsWith("/") ? parentTag : parentTag + "/";
            index = parentTag.length();
        }

        // ---------------------------------------------------------------------
        // IMPLEMENTATION: Function
        // ---------------------------------------------------------------------
        @Override
        public String apply(final String input) {
            if (input == null || !input.startsWith(parentTag)) {
                return "";
            }

            // strip off the parent tag part
            String str = input.substring(index);
            // get just the immediate child part
            str = tagSplitter.splitToList(str).get(0);
            return stripIndex(str);
        }
    }
}
