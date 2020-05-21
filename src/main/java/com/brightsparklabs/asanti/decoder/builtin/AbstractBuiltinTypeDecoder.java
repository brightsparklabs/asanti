/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.decoder.builtin;

import static com.google.common.base.Preconditions.*;

import com.brightsparklabs.asanti.exception.DecodeException;
import com.brightsparklabs.asanti.model.data.AsantiAsnData;
import java.util.Optional;

/**
 * Convenience class to simplify implementing the {@link BuiltinTypeDecoder} interface.
 *
 * <p>This class provides a default implementation of {@link #decodeAsString} which simply returns
 * {@code decode(data).toString()}. Sub-classes should override {@link #decodeAsString} if more
 * complicated string decoding is required.
 *
 * <p>It also provides a default implementation of the {@link #decode} and {@link #decodeAsString}
 * overrides taking the tag and decodedAsnData which simply return the bytes overload of same.
 * Sub-classes should override these if the decoding will produce a different result if the schema
 * is known.
 *
 * @author brightSPARK Labs
 */
public abstract class AbstractBuiltinTypeDecoder<T> implements BuiltinTypeDecoder<T> {
    // -------------------------------------------------------------------------
    // IMPLEMENTATION: BuiltinTypeDecoder
    // -------------------------------------------------------------------------

    @Override
    public T decode(final String tag, final AsantiAsnData asnData) throws DecodeException {
        checkNotNull(tag);
        checkNotNull(asnData);
        final Optional<byte[]> bytes = asnData.getBytes(tag);

        return decode(bytes.orElse(null));
    }

    @Override
    public String decodeAsString(final byte[] bytes) throws DecodeException {
        return decode(bytes).toString();
    }

    @Override
    public String decodeAsString(final String tag, final AsantiAsnData asnData)
            throws DecodeException {
        checkNotNull(tag);
        checkNotNull(asnData);
        final Optional<byte[]> bytes = asnData.getBytes(tag);

        return decodeAsString(bytes.orElse(null));
    }
}
