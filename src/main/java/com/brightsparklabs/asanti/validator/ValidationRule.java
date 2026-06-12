/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.data.AsnData;
import com.brightsparklabs.asanti.exception.DecodeException;
import com.google.common.collect.ImmutableSet;
import java.util.Map;
import java.util.Optional;

/**
 * Represents a rule to validate {@link AsnData} against.
 *
 * @author brightSPARK Labs
 */
public interface ValidationRule {
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** null instance */
    ValidationRule.Null NULL = new ValidationRule.Null();

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Validates the specified tag in the data.
     *
     * @param tag The tag to validate in the data.
     * @param asnData the data to validate.
     * @return The results of validation.
     * @throws DecodeException If the specified tag could not be decoded.
     */
    ImmutableSet<ValidationFailure> validate(final String tag, final AsnData asnData)
            throws DecodeException;

    /**
     * Validates the specified tag in the data.
     *
     * @param tag The tag to validate in the data.
     * @param asnData The data to validate.
     * @param decodedTagValuesCache Optional cache to lookup and add decoded tag values.
     * @param decodedTagPrintableStringCache Optional cache to lookup and add decoded tag printable
     *     string values.
     * @return The results of validation
     * @throws DecodeException If the specified tag could not be decoded.
     */
    ImmutableSet<ValidationFailure> validate(
            final String tag,
            final AsnData asnData,
            final Optional<Map<String, Optional<Object>>> decodedTagValuesCache,
            final Optional<Map<String, Optional<String>>> decodedTagPrintableStringCache)
            throws DecodeException;

    /**
     * Gets the decoded tag value from the given cache. If the cache is not provided OR the tag's
     * value is not present in the cache, then the decoder function is run to retrieve the decoded
     * value.
     *
     * @param decodedTagValuesCache Optional cache to lookup and add decoded tag values.
     * @param tag The tag.
     * @param decoderFunction The function used to retrieve the decoded value
     * @return The decoded tag value.
     * @param <V> The type of the decoded value.
     * @param <E> The type of exception thrown by the decoder function.
     */
    default <V, E extends Exception> V getDecodedValueFromCache(
            final Optional<Map<String, V>> decodedTagValuesCache,
            final String tag,
            final ThrowingFunction<String, V, E> decoderFunction) {
        return decodedTagValuesCache
                .map(
                        (cache) ->
                                cache.computeIfAbsent(
                                        tag,
                                        (t) -> {
                                            try {
                                                return decoderFunction.apply(t);
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                        }))
                .orElseGet(
                        () -> {
                            try {
                                return decoderFunction.apply(tag);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: NULL
    // -------------------------------------------------------------------------

    /**
     * NULL instance of {@link ValidationRule}
     *
     * @author brightSPARK Labs
     */
    class Null implements ValidationRule {
        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor. This is private, use {@link ValidationRule#NULL} to obtain an
         * instance
         */
        private Null() {}

        // ---------------------------------------------------------------------
        // IMPLEMENTATION: ValidationRule
        // ---------------------------------------------------------------------

        @Override
        public ImmutableSet<ValidationFailure> validate(final String tag, final AsnData asnData) {
            return ImmutableSet.of();
        }

        @Override
        public ImmutableSet<ValidationFailure> validate(
                final String tag,
                final AsnData asnData,
                final Optional<Map<String, Optional<Object>>> decodedTagValuesCache,
                final Optional<Map<String, Optional<String>>> decodedTagPrintableStringCache) {
            return ImmutableSet.of();
        }
    }

    /**
     * Convenience interface for defining a functional interface that can throw exceptions.
     *
     * @param <T> The type of input for the function.
     * @param <R> The type returned by the function.
     * @param <E> The type of exception thrown by the function.
     */
    @FunctionalInterface
    interface ThrowingFunction<T, R, E extends Exception> {
        R apply(T t) throws E;
    }
}
