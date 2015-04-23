/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.DecodeException;

/**
 * Used to decode bytes based on the kind of ASN.1 Built-in Type they came from
 *
 * @param <T>
 *         type of object returned by this decoder
 *
 * @author brightSPARK Labs
 */
public interface BuiltinTypeDecoder<T>
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Decodes the supplied bytes based on the kind of ASN.1 Built-in Type represented by this
     * decoder
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the results from decoding the bytes
     *
     * @throws DecodeException
     *         if any errors occur while decoding the supplied bytes
     */
    public T decode(byte[] bytes) throws DecodeException;

    /**
     * Decodes the supplied bytes as a string based on the the kind of ASN.1 Built-in Type
     * represented by this validator
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the results from decoding the bytes
     *
     * @throws DecodeException
     *         if any errors occur while decoding the supplied bytes
     */
    public String decodeAsString(byte[] bytes) throws DecodeException;

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: Null
    // -------------------------------------------------------------------------

    /**
     * Null instance of {@link BuiltinTypeDecoder}.
     *
     * <p>The decode methods will throw a {@link DecodeException} when called. The error message in
     * the exception will be as supplied to the constructor.</p>
     *
     * @author brightSPARK Labs
     */
    public static class Null implements BuiltinTypeDecoder<Object>
    {
        // ---------------------------------------------------------------------
        // INSTANCE VARIABLES
        // ---------------------------------------------------------------------

        /**
         * error message to use in the exceptions which will be thrown when any of the implemented
         * methods are called
         */
        private final String errorMessage;

        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor.
         *
         * @param errorMessage
         *         error message to use in the exceptions which will be thrown when any of the
         *         implemented methods are called.
         */
        public Null(String errorMessage)
        {
            this.errorMessage = errorMessage;
        }

        // ---------------------------------------------------------------------
        // IMPLEMENTATION: BuiltinTypeDecoder
        // ---------------------------------------------------------------------

        @Override
        public Object decode(final byte[] bytes) throws DecodeException
        {
            throw new DecodeException(errorMessage);
        }

        @Override
        public String decodeAsString(final byte[] bytes) throws DecodeException
        {
            throw new DecodeException(errorMessage);
        }
    }
}
