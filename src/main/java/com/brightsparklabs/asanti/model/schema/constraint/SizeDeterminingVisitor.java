/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.constraint;

import com.brightsparklabs.asanti.decoder.builtin.BitStringDecoder;
import com.brightsparklabs.asanti.schema.AsnPrimitiveType;
import com.brightsparklabs.asanti.schema.AsnPrimitiveTypeVisitor;

/**
 * A Visitor to help determine the "size" of a value that we are applying a size constraint to. The
 * "size" of a value is dependent on its type, eg an OctetString is the length of the byte array,
 * whereas some "String" types have a size that is the length of the decoded string representation
 * of the bytes.
 *
 * @author brightSPARK Labs
 */
public class SizeDeterminingVisitor implements AsnPrimitiveTypeVisitor<SizeDeterminer> {

    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /* TODO ASN-208: we don't currently support decoding all types, and up until this code
      was written the size calculation was just byte.length for all types, and the only
      type that "failed" that was BitString.  All the other String type decoders do
      something like
            return new String(bytes, Charsets.UTF_8)
      so they are also essentially the same as bytes.length.
      So the **assumption** in the code below is that BitString is the only type that we need
      to explicitly decode.  The pattern is in place should we need to decode others in order
      to determine appropriate size.
      This code also doesn't explicitly deal with the fact that the SIZE constraint should not
      be able to be applied to all Types.  That should be a schema parse time check.
    */

    /** determine size based on input bytes length */
    private static final SizeDeterminer RAW_BYTES_SIZE = bytes -> bytes.length;

    /** determines size based on length of decoded bytes */
    private static final SizeDeterminer BITSTRING_SIZE =
            bytes -> BitStringDecoder.getInstance().decode(bytes).length();

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.Invalid visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.BitString visitable) {
        return BITSTRING_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.BmpString visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.Boolean visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.CharacterString visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.Choice visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.EmbeddedPdv visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.Enumerated visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.GeneralizedTime visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.GeneralString visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.GraphicString visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.IA5String visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.Integer visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.Null visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.NumericString visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.ObjectDescriptor visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.OctetString visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.Oid visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.PrintableString visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.Real visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.RelativeOid visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.Sequence visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.SequenceOf visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.Set visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.SetOf visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.TeletexString visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.UniversalString visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.UtcTime visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.Utf8String visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.VideotexString visitable) {
        return RAW_BYTES_SIZE;
    }

    @Override
    public SizeDeterminer visit(final AsnPrimitiveType.VisibleString visitable) {
        return RAW_BYTES_SIZE;
    }
}
