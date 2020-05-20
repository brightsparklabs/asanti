/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.selector;

import static com.google.common.base.Preconditions.*;

import com.brightsparklabs.assam.data.AsnData;
import com.brightsparklabs.assam.schema.AsnBuiltinType;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Class where the selection criteria are based on the value of data for a different tag. eg, decode
 * tag /x/y/z with this decoder if the value of tag /a/b/c is "fred"
 *
 * @author brightSPARK Labs
 */
public class SelectorByOtherTagValue<T> extends NonCachableSelector {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the regex pattern to use to match the tag - this is the selection criteria */
    private final Pattern tagMatcher;

    /**
     * The transformation to apply to the tested tag to generate the 'other' tag to get the 'value'
     * from
     */
    private final Function<String, String> transformation;

    /**
     * the value of the other tag that we are comparing. We will only offer a decoder if the value
     * of other tag is equal to this
     */
    private final T value;

    /** replacement for the data gotten from other tag if it is missing/invalid */
    private final T defaultValue;

    /** the type of the value we are comparing */
    private final Class<T> classOfT;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * This overload allows the selection criteria to be expressed as:
     *
     * <p>if the AsnData has valid data for tag ---- the selection criteria are met IFF that data
     * equals the value parameter else ---- the selection criteria are not met.
     *
     * @param tagMatcher the Pattern to match the tag we are being asked if we are prepared to
     *     decode
     * @param transformation the transformation to use on the testing tag to build the tag used to
     *     get the value to compare to
     * @param value the value of the other tag that we are comparing. We will only offer a decoder
     *     if the value of tag is equal to this
     * @param classOfT the type of the value we are comparing
     */
    public SelectorByOtherTagValue(
            final Pattern tagMatcher,
            final Function<String, String> transformation,
            final T value,
            final Class<T> classOfT) {
        this(tagMatcher, transformation, value, null, classOfT);
    }

    /**
     * This overload allows the selection criteria to be expressed as:
     *
     * <p>if the AsnData has valid data for tag ---- the selection criteria are met IFF that data
     * equals the value parameter else ---- the selection criteria are met IFF the value parameter
     * equals the defaultValue parameter
     *
     * @param tagMatcher the Pattern to match the tag we are being asked if we are prepared to
     *     decode
     * @param transformation the transformation to use on the testing tag to build the tag used to
     *     get the value to compare to
     * @param value the value of the other tag that we are comparing. We will only offer a decoder
     *     if the value of tag is equal to this
     * @param defaultValue if at the tim eof checking the AsnData does not contain any data for the
     *     specified tag then this defaultValue will be used to compare to the value parameter, and
     *     the selection criteria are met if value and defaultValue are equal. This parameter may be
     *     null, which results in the equivalent selection criteria of using the alternative
     *     constructor overload
     * @param classOfT the type of the value we are comparing
     */
    public SelectorByOtherTagValue(
            final Pattern tagMatcher,
            final Function<String, String> transformation,
            final T value,
            final T defaultValue,
            final Class<T> classOfT) {
        this.tagMatcher = checkNotNull(tagMatcher);
        this.transformation = checkNotNull(transformation);
        this.value = checkNotNull(value);
        this.defaultValue = defaultValue; // deliberately allow null.  Be warned...
        this.classOfT = checkNotNull(classOfT);
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION
    // -------------------------------------------------------------------------
    @Override
    public boolean matches(final String tag, final AsnBuiltinType type, final AsnData asnData) {
        try {
            if (tagMatcher.matcher(tag).matches()) {
                final String newCheckerTag = transformation.apply(tag);
                final T v = asnData.getDecodedObject(newCheckerTag, classOfT).orElse(defaultValue);
                return value.equals(v);
            }
        } catch (final Exception e) {
            // We don't really care...
        }
        return false;
    }
}
