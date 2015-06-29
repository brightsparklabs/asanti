/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.typedefinition;

import com.brightsparklabs.asanti.common.Visitor;
import com.brightsparklabs.asanti.model.schema.primitive.*;

/**
 * Interface for visitors that can visit {@link AsnPrimitiveType} objects
 *
 * @param <T>
 *         type of value returned by this {@link Visitor}
 *
 * @author brightSPARK Labs
 */
public interface AsnPrimitiveTypeVisitor<T> extends Visitor
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnPrimitiveType.Null visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnPrimitiveTypeBitString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnPrimitiveTypeBoolean visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnPrimitiveTypeChoice visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnPrimitiveTypeEnumerated visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnPrimitiveTypeGeneralizedTime visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnPrimitiveTypeGeneralString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnPrimitiveTypePrintableString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnPrimitiveTypeIA5String visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnPrimitiveTypeInteger visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnPrimitiveTypeNumericString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnPrimitiveTypeOctetString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnPrimitiveTypeSequence visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnPrimitiveTypeSequenceOf visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnPrimitiveTypeSet visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnPrimitiveTypeSetOf visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnPrimitiveTypeUtf8String visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnPrimitiveTypeVisibleString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnPrimitiveTypeOid visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnPrimitiveTypeRelativeOid visitable);
}
