package com.cache.datapacker;

public final class NodeSubList
{

    public NodeSubList()
    {
        head = new NodeSub();
        head.nextSub = head;
        head.previousSub = head;
    }

    public void insertBack(NodeSub node)
    {
        if(node.previousSub != null)
            node.unlinkSub();
        node.previousSub = head.previousSub;
        node.nextSub = head;
        node.previousSub.nextSub = node;
        node.nextSub.previousSub = node;
    }

    public NodeSub popFront()
    {
        NodeSub node = head.nextSub;
        if(node == head)
        {
            return null;
        } else
        {
            node.unlinkSub();
            return node;
        }
    }

    public NodeSub getFront()
    {
        NodeSub node = head.nextSub;
        if(node == head)
        {
            current = null;
            return null;
        } else
        {
            current = node.nextSub;
            return node;
        }
    }

    public NodeSub getNext()
    {
        NodeSub nodeSub = current;
        if(nodeSub == head)
        {
            current = null;
            return null;
        } else
        {
            current = nodeSub.nextSub;
            return nodeSub;
        }
    }

    public int getSize()
    {
        int i = 0;
        for(NodeSub node = head.nextSub; 
        	node != head; 
        node = node.nextSub)
        i++;
        return i;
    }

    public NodeSub head;
    private NodeSub current;
}
