/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.selector.transformations;

import static com.google.common.base.Preconditions.*;
import static java.util.Objects.*;

import com.google.common.base.Strings;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builds a tag from a passed tag via transformation using a given pattern and replacement string
 *
 * @author brightSPARK Labs
 */
public class SingleIndexTagTransformation implements Function<String, String> {
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger =
            LoggerFactory.getLogger(SingleIndexTagTransformation.class);

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /**
     * Pattern used to parse information from passed tag and build replacement tag. Expects at least
     * one capture group
     */
    private final Pattern patternToApplyToTag;

    /** The string used to construct a tag based on the capture group of patternToApplyToTag */
    private final String replacementTag;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    public SingleIndexTagTransformation(
            final Pattern patternToApplyToTag, final String replacementTag) {
        checkArgument(
                !Strings.isNullOrEmpty(replacementTag), "replacementTag must not be null or empty");

        this.patternToApplyToTag = requireNonNull(patternToApplyToTag);
        this.replacementTag = replacementTag;
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    @Override
    public String apply(final String tag) {
        checkArgument(!Strings.isNullOrEmpty(tag), "tag must not be null or empty");

        try {
            final Matcher matcher = patternToApplyToTag.matcher(tag);
            if (matcher.matches()) {
                final int index = Integer.parseInt(matcher.group(1));
                return String.format(replacementTag, index);
            }

            // the calling code uses this to test if a value equals another value, hence if we
            // cannot
            // match and build the appropriate tag then the equality check will always fail hence
            // throw
            throw new RuntimeException(
                    String.format(
                            "No match could be found for tag [%s] for pattern [%s]",
                            tag, patternToApplyToTag.toString()));
        } catch (RuntimeException e) {
            logger.error("Failed to apply transformation to tag [{}]", tag, e);
            throw new RuntimeException(
                    String.format("Could not apply transformation to tag [%s]", tag), e);
        }
    }
}
