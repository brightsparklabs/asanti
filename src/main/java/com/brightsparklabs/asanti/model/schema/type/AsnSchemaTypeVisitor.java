package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.common.Visitor;

import java.text.ParseException;

/**
 * TODO MJF
 */
public interface AsnSchemaTypeVisitor<T> extends Visitor
{

    // TODO MJF - javadoc
    T visit(AsnSchemaTypeConstructed visitable) throws ParseException;
    T visit(BaseAsnSchemaType visitable) throws ParseException;
    T visit(AsnSchemaTypeCollection visitable) throws ParseException;
    T visit(AsnSchemaTypeWithNamedTags visitable) throws ParseException;
    T visit(AsnSchemaTypePlaceholder visitable) throws ParseException;

    T visit(AsnSchemaType.Null visitable) throws ParseException;
}
