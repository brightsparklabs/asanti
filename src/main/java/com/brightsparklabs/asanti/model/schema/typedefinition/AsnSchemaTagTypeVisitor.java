/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.typedefinition;

import com.brightsparklabs.asanti.common.Visitor;
import com.brightsparklabs.asanti.model.schema.tagtype.*;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition.Null;

/**
 * Interface for visitors that can visit {@link AbstractAsnSchemaTypeDefinition} objects
 *
 * @param <T>
 *         type of value returned by this {@link Visitor}
 *
 * @author brightSPARK Labs
 */
public interface AsnSchemaTagTypeVisitor<T> extends Visitor
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
    public T visit(AsnSchemaTypeDefinition.Null visitable);

    public T visit(AsnSchemaTagType.Null visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionBitString visitable);
    public T visit(AsnSchemaTagTypeBitString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionChoice visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionEnumerated visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionGeneralizedTime visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionGeneralString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */

    public T visit(AsnSchemaTagTypePrintableString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionIa5String visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionInteger visitable);

    public T visit(AsnSchemaTagTypeInteger visitable);


    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionNumericString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionOctetString visitable);
    public T visit(AsnSchemaTagTypeOctetString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionSequence visitable);
    public T visit(AsnSchemaTagTypeSequence visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionSequenceOf visitable);
    public T visit(AsnSchemaTagTypeSequenceOf visitable);


    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionSet visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionSetOf visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionUtf8String visitable);
    public T visit(AsnSchemaTagTypeUtf8String visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTypeDefinitionVisibleString visitable);
}
