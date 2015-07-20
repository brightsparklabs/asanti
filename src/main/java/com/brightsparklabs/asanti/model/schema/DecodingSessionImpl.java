package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.model.schema.tag.AsnSchemaTag;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Implementation of DecodingSession
 *
 * @author brightSPARK Labs
 */
public class DecodingSessionImpl implements DecodingSession
{

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    class ZZ
    {
        AsnSchemaTag tag = null;
        Integer index = 0;
    }

    /** storage of the offsets for each index for each "context" */
    private final Map<String, ZZ> offsetMap = Maps.newHashMap();
    //private final Map<String, Map<Integer, Integer>> offsetMap = Maps.newHashMap();

    /** the context is how we provide unique offsets for each level of the hierarchy of data */
    String context;


    // -------------------------------------------------------------------------
    // IMPLEMENTATION: DecodingSession
    // -------------------------------------------------------------------------

    @Override
    public void setContext(String context)
    {
        this.context = context;

        if (offsetMap.get(context) == null)
        {
            offsetMap.put(context, new ZZ());
        }

//        if (offsetMap.get(context) == null)
//        {
//            offsetMap.put(context, Maps.<Integer, Integer>newHashMap());
//        }
    }

    @Override
    public int getOffset(AsnSchemaTag tag)
    {
        //"0[0], 0[0], 0[0], 1[1], 2[3],..."

        ZZ zz = offsetMap.get(context);

        if (zz.tag == null)
        {
            zz.tag = tag;
            return zz.index;
        }

        if (zz.tag.getRawTag().equals(tag.getRawTag()))
        {
            return zz.index;
        }

        zz.tag = tag;
        zz.index++;

        return zz.index;

//        Map<Integer, Integer> offsets = offsetMap.get(context);
//
//        Integer offset = offsets.get(index);
//        return (offset == null) ? 0 : offset;
    }

    @Override
    public void setOffset(AsnSchemaTag tag, int offset)
    {

        ZZ zz = offsetMap.get(context);
        zz.tag = tag;
        zz.index = offset;
//        Map<Integer, Integer> offsets = offsetMap.get(context);
//        offsets.put(index, offset);
    }
}
