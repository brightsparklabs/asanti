package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.model.schema.tag.AsnSchemaTag;

/**
 * Created by Michael on 7/07/2015.
 */
public interface DecodingSession
{

    AsnSchemaTag getTag(Object object, AsnSchemaTag tag);

    void setTag(Object object, AsnSchemaTag tag, boolean isOptional);
}
