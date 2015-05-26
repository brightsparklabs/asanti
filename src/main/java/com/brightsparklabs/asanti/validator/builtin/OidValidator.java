/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Validator for data of type {@link AsnBuiltinType#Oid}
 *
 * @author brightSPARK Labs
 */
public class OidValidator extends PrimitiveBuiltinTypeValidator
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static OidValidator instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance</p>
     */
    private OidValidator() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static OidValidator getInstance()
    {
        if (instance == null)
        {
            instance = new OidValidator();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: PrimitiveBuiltinTypeValidator
    // -------------------------------------------------------------------------

    @Override
    protected ImmutableSet<ByteValidationFailure> validateNonNullBytes(final byte[] bytes)
    {
        final Set<ByteValidationFailure> failures = Sets.newHashSet();

        // TODO (review ASN-114) - MJF. Are we sure that a 0 byte input is valid???  What would be an appropriate return value?
        // Check encoding of first byte
        if (bytes.length > 0)
        {
            int firstByte = bytes[0] & 0xFF;
            if (firstByte > 0x7F)
            {
                final String error = OID_VALIDATION_ERROR + String.format("0x%02X ", firstByte);
                final ByteValidationFailure failure = new ByteValidationFailure(0,
                        FailureType.DataIncorrectlyFormatted,
                        error);
                failures.add(failure);
            }

            /* TODO (review ASN-114) - MJF.  If the only failure is that the last byte has
             * its top bit set then do we actually need to loop through?
             * ie can we just do:

            int i = bytes.length - 1;
            if (i > 1)
            {
                byte b = bytes[i];
                if ((b & (byte) 0x80) != 0) {
                    final String error = OID_VALIDATION_ERROR_INCOMPLETE +
                                         String.format("0x%02X ", b);
                    final ByteValidationFailure failure = new ByteValidationFailure(i,
                            FailureType.DataIncorrectlyFormatted,
                            error);
                    failures.add(failure);
                }
            }
            */

            // Check the encoding of all bytes.
            long currentSID = 0;

            for (int i = 1; i < bytes.length; i++)
            {
                byte b = bytes[i];
                int currentByte = b & 0xFF;
                currentSID = (currentSID << 7) | (currentByte & 0x7F);

                // SID may be encoded across multiple octets. If signed bit is not set, this is the
                // last (or only) encoded octet.
                if ((currentByte & 0x80) == 0)
                {
                    // No errors in current sequence of bytes. Check the next SID.
                    currentSID = 0;
                }
                else
                {
                    // If the signed bit on the last byte is set, the encoding is incomplete.
                    if (i == bytes.length - 1)
                    {
                        final String error = OID_VALIDATION_ERROR_INCOMPLETE + String.format(
                                "0x%02X ",
                                b);
                        final ByteValidationFailure failure = new ByteValidationFailure(i,
                                FailureType.DataIncorrectlyFormatted,
                                error);
                        failures.add(failure);
                    }
                }
            }

        }

        return ImmutableSet.copyOf(failures);
    }
}
