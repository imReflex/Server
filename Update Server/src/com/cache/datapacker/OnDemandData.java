package com.cache.datapacker;

public class OnDemandData extends NodeSub
{

    public OnDemandData()
    {
        incomplete = true;
    }

    int dataType;
    byte buffer[];
    int ID;
    boolean incomplete;
    int loopCycle;
    int datatype;
}