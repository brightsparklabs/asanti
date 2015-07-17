package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.common.Visitor;

import java.text.ParseException;

/**
 * Interface for visitors that can visit {@link AsnSchemaType} objects
 *
 * @param <T>
 *         type of value returned by this {@link Visitor}
 *
 * @author brightSPARK Labs
 */
public interface AsnSchemaTypeVisitor<T> extends Visitor
{

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    T visit(AsnSchemaTypeConstructed visitable) throws ParseException;

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    T visit(BaseAsnSchemaType visitable) throws ParseException;

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    T visit(AsnSchemaTypeCollection visitable) throws ParseException;

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    T visit(AsnSchemaTypeWithNamedTags visitable) throws ParseException;

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    T visit(AsnSchemaTypePlaceholder visitable) throws ParseException;

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable
     *         the object to process
     *
     * @return the results from processing the supplied object
     */
    T visit(AsnSchemaType.Null visitable) throws ParseException;
}
