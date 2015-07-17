package com.brightsparklabs.asanti.model.schema;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

/**
 * Created by Michael on 7/07/2015.
 */
public class DecodingSessionImpl implements DecodingSession
{
    //Map<String, LevelTagTracker> levelTagTrackerMap = Maps.newHashMap();

    Map<String, Map<Integer, Integer>> offsetMap = Maps.newHashMap();

    String context;

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

    //    @Override
    //    public AsnSchemaTag getTag(AsnSchemaTag tag)
    //    {
    //        LevelTagTracker levelTagTracker = levelTagTrackerMap.get(context);
    //        if (levelTagTracker == null)
    //        {
    //            levelTagTracker = new LevelTagTracker();
    //            levelTagTrackerMap.put(context, levelTagTracker);
    //        }
    //
    //        return levelTagTracker.getTag(tag);
    //    }
    //
    //    @Override
    //    public void setTag(AsnSchemaTag tag, boolean isOptional)
    //    {
    //        LevelTagTracker levelTagTracker = levelTagTrackerMap.get(context);
    //        if (levelTagTracker == null)
    //        {
    //            levelTagTracker = new LevelTagTracker();
    //            levelTagTrackerMap.put(context, levelTagTracker);
    //        }
    //
    //        levelTagTracker.putUsedTag(tag, isOptional);
    //    }
    //
    //    private static class LevelTagTracker
    //    {
    //        Set<AsnSchemaTag> rawTagsUsed = Sets.newHashSet();
    //        Set<Integer> offsets = Sets.newLinkedHashSet();
    //
    //        int lastOffset = 0;
    //
    //        void putUsedTag(AsnSchemaTag tag, boolean isOptional)
    //        {
    //            rawTagsUsed.add(tag);
    //            Integer index = Integer.parseInt(tag.getTagIndex());
    //            if (isOptional)
    //            {
    //                offsets.add(index+lastOffset);
    //            }
    //        }
    //
    //        public AsnSchemaTag getTag(AsnSchemaTag tag)
    //        {
    //            Integer index = Integer.parseInt(tag.getTagIndex());
    //            Iterator<Integer> iterator = offsets.iterator();
    //            int subtract = 0;
    //            while(iterator.hasNext())
    //            {
    //                Integer offset = iterator.next();
    //                if (offset < (index))
    //                {
    //                    subtract++;
    //                }
    //            }
    //
    //            if (subtract != 0)
    //            {
    //                tag = AsnSchemaTag.create((index - subtract), tag);
    //            }
    //
    //            lastOffset = subtract;
    //
    //            return tag;
    //        }
    //    }
}
