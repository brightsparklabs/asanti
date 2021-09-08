/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.visitor.Visitor;
import java.text.ParseException;

/**
 * Interface for visitors that can visit {@link AsnSchemaType} objects
 *
 * @param <T> type of value returned by this {@link Visitor}
 * @author brightSPARK Labs
 */
public interface AsnSchemaTypeVisitor<T> extends Visitor {

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     * @throws ParseException dependent on visitor implementation
     */
    T visit(AsnSchemaTypeConstructed visitable) throws ParseException;

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     * @throws ParseException dependent on visitor implementation
     */
    T visit(AsnSchemaTypePrimitive visitable) throws ParseException;

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     * @throws ParseException dependent on visitor implementation
     */
    T visit(AsnSchemaTypePrimitiveAliased visitable) throws ParseException;

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     * @throws ParseException dependent on visitor implementation
     */
    T visit(AsnSchemaTypeCollection visitable) throws ParseException;

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     * @throws ParseException dependent on visitor implementation
     */
    T visit(AsnSchemaTypeWithNamedTags visitable) throws ParseException;

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     * @throws ParseException dependent on visitor implementation
     */
    T visit(AsnSchemaTypePlaceholder visitable) throws ParseException;

    /**
     * Processes the supplied object with this visitor
     *
     * @param visitable the object to process
     * @return the results from processing the supplied object
     * @throws ParseException dependent on visitor implementation
     */
    T visit(AsnSchemaType.Null visitable) throws ParseException;
}
