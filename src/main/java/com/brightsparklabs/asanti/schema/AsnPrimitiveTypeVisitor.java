/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.schema;

import com.brightsparklabs.asanti.visitor.Visitor;

/**
 * Interface for visitors that can visit {@link AsnPrimitiveType} objects
 *
 * @param <T> type of value returned by this {@link Visitor}
 * @author brightSPARK Labs
 */
public interface AsnPrimitiveTypeVisitor<T> extends Visitor {
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.Invalid visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.BitString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.BmpString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.Boolean visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.CharacterString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.Choice visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.EmbeddedPdv visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.Enumerated visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.GeneralizedTime visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.GeneralString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.GraphicString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.IA5String visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.Integer visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.Null visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.NumericString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.ObjectDescriptor visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.OctetString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.Oid visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.PrintableString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.Real visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.RelativeOid visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.Sequence visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.SequenceOf visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.Set visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.SetOf visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.TeletexString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.UniversalString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.UtcTime visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.Utf8String visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.VideotexString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     */
    T visit(AsnPrimitiveType.VisibleString visitable);
}
