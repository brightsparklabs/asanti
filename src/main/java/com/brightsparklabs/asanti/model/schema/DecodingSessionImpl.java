package com.brightsparklabs.asanti.model.schema;

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

    /** storage of the offsets for each index for each "context" */
    private final Map<String, Map<Integer, Integer>> offsetMap = Maps.newHashMap();

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
            offsetMap.put(context, Maps.<Integer, Integer>newHashMap());
        }
    }

    @Override
    public int getOffset(int index)
    {
        Map<Integer, Integer> offsets = offsetMap.get(context);

        Integer offset = offsets.get(index);
        return (offset == null) ? 0 : offset;
    }

    @Override
    public void setOffset(int index, int offset)
    {
        Map<Integer, Integer> offsets = offsetMap.get(context);
        offsets.put(index, offset);
    }
}
