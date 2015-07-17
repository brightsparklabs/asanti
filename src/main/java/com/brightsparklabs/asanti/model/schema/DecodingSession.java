package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.model.schema.tag.AsnSchemaTag;

/**
 * Created by Michael on 7/07/2015.
 */
public interface DecodingSession
{
    void setContext(String context);

    int getOffset(int index);

    void setOffset(int index, int offset);

//    void setContext(String context);
//
//    AsnSchemaTag getTag(AsnSchemaTag tag);
//
//    void setTag(AsnSchemaTag tag, boolean isOptional);
}
