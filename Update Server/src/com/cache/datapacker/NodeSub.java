package com.cache.datapacker;

public class NodeSub extends Node
{

    public void unlinkSub()
    {
        if(previousSub == null)
        {
            return;
        } else
        {
            previousSub.nextSub = nextSub;
            nextSub.previousSub = previousSub;
            nextSub = null;
            previousSub = null;
            return;
        }
    }

    public NodeSub()
    {
    }

    public NodeSub nextSub;
    NodeSub previousSub;
}
