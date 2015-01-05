/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;


/**
 * Default implementation of {@link AsnSchema}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaDefault implements AsnSchema
{

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchema
    // -------------------------------------------------------------------------

    @Override
    public String getDecodedTag(String rawTag)
    {
        return "";
    }

    @Override
    public String getRawTag(String decodedTag)
    {
        return "";
    }

    @Override
    public String getPrintableString(String tag, byte[] data)
    {
        return "";
    }

    @Override
    public Object getDecodedObject(String tag, byte[] data)
    {
        return "";
    }
}
