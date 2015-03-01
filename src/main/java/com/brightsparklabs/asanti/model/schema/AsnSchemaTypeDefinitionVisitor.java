/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.common.Visitor;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinition.AsnSchemaTypeDefinitionNull;

/**
 * Interface for visitors that can visit {@link AsnSchemaTypeDefinition} objects
 *
 * @param <T>
 *            type of value returned by this {@link Visitor}
 *
 * @author brightSPARK Labs
 */
public interface AsnSchemaTypeDefinitionVisitor<T> extends Visitor
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *            the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionNull visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *            the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionBitString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *            the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionChoice visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *            the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionEnumerated visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *            the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionIA5String visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *            the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionInteger visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *            the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionNumericString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *            the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionOctetString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *            the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionSequence visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *            the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionSequenceOf visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *            the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionSet visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *            the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionSetOf visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *            the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionUTF8String visitable);
}
