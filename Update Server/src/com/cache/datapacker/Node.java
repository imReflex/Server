package com.cache.datapacker;

public class Node
{

    public void unlink()
    {
        if(previous == null)
        {
            return;
        } else
        {
            previous.next = next;
            next.previous = previous;
            next = null;
            previous = null;
            return;
        }
    }

    public Node()
    {
    }

    public long hash;
    public Node next;
    Node previous;
}
