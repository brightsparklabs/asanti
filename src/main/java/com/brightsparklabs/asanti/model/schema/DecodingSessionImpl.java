package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.model.schema.tag.AsnSchemaTag;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Michael on 7/07/2015.
 */
public class DecodingSessionImpl implements DecodingSession
{
    Map<Object, LevelTagTracker> levelTagTrackerMap = Maps.newHashMap();

    @Override
    public AsnSchemaTag getTag(Object object, AsnSchemaTag tag)
    {
        LevelTagTracker levelTagTracker = levelTagTrackerMap.get(object);
        if (levelTagTracker == null)
        {
            levelTagTracker = new LevelTagTracker();
            levelTagTrackerMap.put(object, levelTagTracker);
        }

        return levelTagTracker.getTag(tag);

    }

    @Override
    public void setTag(Object object, AsnSchemaTag tag, boolean isOptional)
    {
        LevelTagTracker levelTagTracker = levelTagTrackerMap.get(object);
        if (levelTagTracker == null)
        {
            levelTagTracker = new LevelTagTracker();
            levelTagTrackerMap.put(this, levelTagTracker);
        }

        levelTagTracker.putUsedTag(tag, isOptional);
    }

    private static class LevelTagTracker
    {
        Set<AsnSchemaTag> rawTagsUsed = Sets.newHashSet();
        Set<Integer> offsets = Sets.newLinkedHashSet();

        int lastOffset = 0;

        void putUsedTag(AsnSchemaTag tag, boolean isOptional)
        {
            rawTagsUsed.add(tag);
            Integer index = Integer.parseInt(tag.getTagIndex());
            if (isOptional)
            {
                offsets.add(index+lastOffset);
            }
        }

        public AsnSchemaTag getTag(AsnSchemaTag tag)
        {
            Integer index = Integer.parseInt(tag.getTagIndex());
            Iterator<Integer> iterator = offsets.iterator();
            int subtract = 0;
            while(iterator.hasNext())
            {
                Integer offset = iterator.next();
                if (offset < (index))
                {
                    subtract++;
                }
            }

            if (subtract != 0)
            {
                tag = AsnSchemaTag.create((index - subtract), tag);
            }

            lastOffset = subtract;

            return tag;
        }
    }

}
