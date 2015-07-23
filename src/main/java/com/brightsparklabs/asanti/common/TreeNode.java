package com.brightsparklabs.asanti.common;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * TODO MJF
 */
public class TreeNode<T>
{
    private T payload;

    private final TreeNode<T> parent;

    List<TreeNode<T>> children = Lists.newArrayList();

    public TreeNode(T payload)
    {
        this.payload = payload;
        this.parent = null;
    }

    public TreeNode(T payload, TreeNode<T> parent)
    {
        this.payload = payload;
        this.parent = parent;
    }

    public T getPayload()
    {
        return payload;
    }

    public void setPayload(T payload)
    {
        this.payload = payload;
    }

    public TreeNode<T> addChild(T payload)
    {
        TreeNode<T> child = new TreeNode<>(payload, this);

        this.children.add(child);
        return child;
    }

    public Optional<TreeNode<T>> getChild(T other)
    {
        for (TreeNode<T> child : children)
        {
            if (child.getPayload().equals(other))
            {
                return Optional.of(child);
            }
        }
        return Optional.absent();
    }

    public TreeNode<T> getParent()
    {
        return parent;
    }

    public ImmutableList<TreeNode<T>> getChildren()
    {
        return ImmutableList.copyOf(children);
    }

    public boolean removeChild(TreeNode<T> child)
    {
        return children.remove(child);
    }

    public int getLevel()
    {
        return (parent == null) ? 0 : parent.getLevel() + 1;
    }

}
