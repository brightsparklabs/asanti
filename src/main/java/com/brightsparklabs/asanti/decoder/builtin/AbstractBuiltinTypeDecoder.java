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
import com.brightsparklabs.asanti.model.data.AsnData;
import com.google.common.base.Optional;

import static com.google.common.base.Preconditions.*;

/**
 * Convenience class to simplify implementing the {@link BuiltinTypeDecoder} interface.
 *
 * <p>This class provides a default implementation of {@link #decodeAsString} which simply returns
 * {@code decode(data).toString()}. Sub-classes should override {@link #decodeAsString} if more
 * complicated string decoding is required.</p> <p>It also provides a default implementation of the
 * {@link #decode} and {@link #decodeAsString} overrides taking the tag and decodedAsnData which
 * simply return the bytes overload of same.  Sub-classes should override these if the decoding will
 * produce a different result if the schema is known.</p>
 *
 * @author brightSPARK Labs
 */
public abstract class AbstractBuiltinTypeDecoder<T> implements BuiltinTypeDecoder<T>
{
    // -------------------------------------------------------------------------
    // IMPLEMENTATION: BuiltinTypeDecoder
    // -------------------------------------------------------------------------

    @Override
    public T decode(final String tag, final AsnData asnData) throws DecodeException
    {
        checkNotNull(tag);
        checkNotNull(asnData);
        final Optional<byte[]> bytes = asnData.getBytes(tag);

        return decode(bytes.orNull());
    }

    @Override
    public String decodeAsString(final byte[] bytes) throws DecodeException
    {
        return decode(bytes).toString();
    }

    @Override
    public String decodeAsString(final String tag, final AsnData asnData)
            throws DecodeException
    {
        checkNotNull(tag);
        checkNotNull(asnData);
        final Optional<byte[]> bytes = asnData.getBytes(tag);

        return decodeAsString(bytes.orNull());
    }
}
