/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.typedefinition;

import com.brightsparklabs.asanti.common.Visitor;
import com.brightsparklabs.asanti.model.schema.primitive.*;
import com.brightsparklabs.asanti.model.schema.tagtype.*;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;

/**
 * Interface for visitors that can visit {@link AbstractOLDAsnSchemaTypeDefinition} objects
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
    public T visit(OLDAsnSchemaTypeDefinition.Null visitable);
    public T visit(AsnSchemaTagType.Null visitable);
    public T visit(AsnPrimitiveType.Null visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(OLDAsnSchemaTypeDefinitionBitString visitable);
    public T visit(AsnSchemaTagTypeBitString visitable);
    public T visit(AsnPrimitiveTypeBitString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(OLDAsnSchemaTypeDefinitionChoice visitable);
    public T visit(AsnPrimitiveTypeChoice visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(OLDAsnSchemaTypeDefinitionEnumerated visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(OLDAsnSchemaTypeDefinitionGeneralizedTime visitable);
    public T visit(AsnSchemaTagTypeGeneralizedTime visitable);
    public T visit(AsnPrimitiveTypeGeneralizedTime visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(OLDAsnSchemaTypeDefinitionGeneralString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */

    public T visit(AsnSchemaTagTypePrintableString visitable);
    public T visit(AsnPrimitiveTypePrintableString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(OLDAsnSchemaTypeDefinitionIa5String visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(OLDAsnSchemaTypeDefinitionInteger visitable);
    public T visit(AsnSchemaTagTypeInteger visitable);
    public T visit(AsnPrimitiveTypeInteger visitable);


    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(OLDAsnSchemaTypeDefinitionNumericString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(OLDAsnSchemaTypeDefinitionOctetString visitable);
    public T visit(AsnSchemaTagTypeOctetString visitable);
    public T visit(AsnPrimitiveTypeOctetString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(OLDAsnSchemaTypeDefinitionSequence visitable);
    public T visit(AsnSchemaTagTypeSequence visitable);
    public T visit(AsnPrimitiveTypeSequence visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(OLDAsnSchemaTypeDefinitionSequenceOf visitable);
    public T visit(AsnSchemaTagTypeSequenceOf visitable);
    public T visit(AsnPrimitiveTypeSequenceOf visitable);


    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(OLDAsnSchemaTypeDefinitionSet visitable);
    public T visit(AsnPrimitiveTypeSet visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(OLDAsnSchemaTypeDefinitionSetOf visitable);
    public T visit(AsnSchemaTagTypeSetOf visitable);
    public T visit(AsnPrimitiveTypeSetOf visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(OLDAsnSchemaTypeDefinitionUtf8String visitable);
    public T visit(AsnSchemaTagTypeUtf8String visitable);
    public T visit(AsnPrimitiveTypeUtf8String visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(OLDAsnSchemaTypeDefinitionVisibleString visitable);

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    public T visit(AsnSchemaTagTypeObjectIdentifier visitable);
    public T visit(AsnPrimitiveTypeOid visitable);

}
