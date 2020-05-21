/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.DecodeExceptions;
import com.brightsparklabs.asanti.exception.DecodeException;
import com.brightsparklabs.asanti.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.validator.AsnByteValidator;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.google.common.collect.ImmutableSet;

/**
 * Decoder for data of type {@link AsnBuiltinType#Oid}
 *
 * @author brightSPARK Labs
 */
public class OidDecoder extends AbstractBuiltinTypeDecoder<String> {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static OidDecoder instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance
     */
    private OidDecoder() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static OidDecoder getInstance() {
        if (instance == null) {
            instance = new OidDecoder();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AbstractBuiltinTypeDecoder
    // -------------------------------------------------------------------------

    @Override
    public String decode(final byte[] bytes) throws DecodeException {
        final ImmutableSet<ByteValidationFailure> failures = AsnByteValidator.validateAsOid(bytes);
        DecodeExceptions.throwIfHasFailures(failures);

        long currentSID = 0;
        StringBuilder oidBuilder = new StringBuilder();

        // The first two sub IDs are encoded into the first byte
        // First byte encoded as 40 * id1 + id2
        byte firstByte = bytes[0];
        oidBuilder.append(firstByte / 40);
        oidBuilder.append(".");
        oidBuilder.append(firstByte % 40);

        // The remaining ids may be encoded on one byte (within range 0x00 - 0x7F), or multiple
        // bytes (where signed bit indicates another octet follows).
        for (int i = 1; i < bytes.length; i++) {
            int currentByte = bytes[i] & 0xFF;
            currentSID = (currentSID << 7) | (currentByte & 0x7F);
            // SID may be encoded across multiple octets. If 7th bit is not set, this is the last
            // (or only) encoded octet.
            if ((currentByte & 0x80) == 0) {
                oidBuilder.append(".");
                oidBuilder.append(currentSID);

                // Reset currentSID and decode the next sub id in the OID.
                currentSID = 0;
            }
        }
        return oidBuilder.toString();
    }
}
