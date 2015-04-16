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
import com.brightsparklabs.asanti.validator.ValidationResult;
import com.brightsparklabs.asanti.validator.bytes.AsnByteValidator;

/**
 * Convenience class to simplify implementing the {@link BuiltinTypeDecoder} interface. Sub-classes
 * should override {@link #decodeValidatedBytes(byte[])}.
 *
 * <p>This class provides a default implementation of {@link #decodeAsString} which simply returns
 * {@code decode(data).toString()}. Sub-classes should override {@link #decodeAsString} if more
 * complicated string decoding is required.</p>
 *
 * @author brightSPARK Labs
 */
public abstract class AbstractBuiltinTypeDecoder<T> implements BuiltinTypeDecoder<T>
{
    // -------------------------------------------------------------------------
    // IMPLEMENTATION: BuiltinTypeDecoder
    // -------------------------------------------------------------------------

    @Override
    public T decode(final byte[] bytes) throws DecodeException
    {
        final ValidationResult validationResult = AsnByteValidator.validateAsIa5String(bytes);
        DecodeException.throwIfHasFailures(validationResult);
        return decodeValidatedBytes(bytes);
    }

    @Override
    public String decodeAsString(final byte[] data) throws DecodeException
    {
        return decode(data).toString();
    }

    // -------------------------------------------------------------------------
    // PROTECTED METHODS
    // -------------------------------------------------------------------------

    /**
     * Decodes supplied data based on the the kind of Type Definition represented by this decoder.
     * The bytes will have already been validated prior to calling this method.
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the results from decoding the bytes
     *
     * @throws DecodeException
     *         if any errors occur while decoding the supplied bytes
     */
    protected abstract T decodeValidatedBytes(final byte[] bytes) throws DecodeException;
}
