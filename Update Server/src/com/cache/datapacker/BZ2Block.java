package com.cache.datapacker;

class BZ2Block
{

    BZ2Block()
    {
        unzftab = new int[256];
        cftab = new int[257];
        use = new boolean[256];
        inUse = new boolean[16];
        seqToUnseq = new byte[256];
        yy = new byte[4096];
        mtf16 = new int[16];
        selector = new byte[18002];
        selectorMtf = new byte[18002];
        len = new byte[6][258];
        limit = new int[6][258];
        base = new int[6][258];
        perm = new int[6][258];
        minLens = new int[6];
    }

    byte input[];
    int nextIn;
    int compressedSize;
    int totalInLo32;
    int totalInHi32;
    byte output[];
    int availOut;
    int decompressedSize;
    int totalOutLo32;
    int totalOutHi32;
    byte stateOutCH;
    int stateOutLEN;
    boolean randomized;
    int bsBuff;
    int bsLive;
    int blockSize_100k;
    int blockNo;
    int origPtr;
    int nextOut;
    int k0;
    int unzftab[];
    int nBlockUsed;
    int cftab[];
    public static int ll8[];
    int nInUse;
    boolean use[];
    boolean inUse[];
    byte seqToUnseq[];
    byte yy[];
    int mtf16[];
    byte selector[];
    byte selectorMtf[];
    byte len[][];
    int limit[][];
    int base[][];
    int perm[][];
    int minLens[];
    int nBlockPP;
}
