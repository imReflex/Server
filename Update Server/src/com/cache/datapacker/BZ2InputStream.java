package com.cache.datapacker;

public final class BZ2InputStream
{

    public static int decompressBuffer(byte output[], int decompressed, byte input[], int compressed, int nextIN)
    {
        synchronized(block)
        {
            block.input = input;
            block.nextIn = nextIN;
            block.output = output;
            block.availOut = 0;
            block.compressedSize = compressed;
            block.decompressedSize = decompressed;
            block.bsLive = 0;
            block.bsBuff = 0;
            block.totalInLo32 = 0;
            block.totalInHi32 = 0;
            block.totalOutLo32 = 0;
            block.totalOutHi32 = 0;
            block.blockNo = 0;
            decompress(block);
            decompressed -= block.decompressedSize;
            int l = decompressed;
            return l;
        }
    }

    private static void method226(BZ2Block bz2Block)
    {
        byte byte4 = bz2Block.stateOutCH;
        int i = bz2Block.stateOutLEN;
        int j = bz2Block.nBlockUsed;
        int k = bz2Block.k0;
        int ai[] = BZ2Block.ll8;
        int l = bz2Block.nextOut;
        byte abyte0[] = bz2Block.output;
        int i1 = bz2Block.availOut;
        int j1 = bz2Block.decompressedSize;
        int k1 = j1;
        int l1 = bz2Block.nBlockPP + 1;
label0:
        do
        {
            if(i > 0)
            {
                do
                {
                    if(j1 == 0)
                        break label0;
                    if(i == 1)
                        break;
                    abyte0[i1] = byte4;
                    i--;
                    i1++;
                    j1--;
                } while(true);
                if(j1 == 0)
                {
                    i = 1;
                    break;
                }
                abyte0[i1] = byte4;
                i1++;
                j1--;
            }
            boolean flag = true;
            while(flag) 
            {
                flag = false;
                if(j == l1)
                {
                    i = 0;
                    break label0;
                }
                byte4 = (byte)k;
                l = ai[l];
                byte byte0 = (byte)(l & 0xff);
                l >>= 8;
                j++;
                if(byte0 != k)
                {
                    k = byte0;
                    if(j1 == 0)
                    {
                        i = 1;
                    } else
                    {
                        abyte0[i1] = byte4;
                        i1++;
                        j1--;
                        flag = true;
                        continue;
                    }
                    break label0;
                }
                if(j != l1)
                    continue;
                if(j1 == 0)
                {
                    i = 1;
                    break label0;
                }
                abyte0[i1] = byte4;
                i1++;
                j1--;
                flag = true;
            }
            i = 2;
            l = ai[l];
            byte byte1 = (byte)(l & 0xff);
            l >>= 8;
            if(++j != l1)
                if(byte1 != k)
                {
                    k = byte1;
                } else
                {
                    i = 3;
                    l = ai[l];
                    byte byte2 = (byte)(l & 0xff);
                    l >>= 8;
                    if(++j != l1)
                        if(byte2 != k)
                        {
                            k = byte2;
                        } else
                        {
                            l = ai[l];
                            byte byte3 = (byte)(l & 0xff);
                            l >>= 8;
                            j++;
                            i = (byte3 & 0xff) + 4;
                            l = ai[l];
                            k = (byte)(l & 0xff);
                            l >>= 8;
                            j++;
                        }
                }
        } while(true);
        int i2 = bz2Block.totalOutLo32;
        bz2Block.totalOutLo32 += k1 - j1;
        if(bz2Block.totalOutLo32 < i2)
            bz2Block.totalOutHi32++;
        bz2Block.stateOutCH = byte4;
        bz2Block.stateOutLEN = i;
        bz2Block.nBlockUsed = j;
        bz2Block.k0 = k;
        BZ2Block.ll8 = ai;
        bz2Block.nextOut = l;
        bz2Block.output = abyte0;
        bz2Block.availOut = i1;
        bz2Block.decompressedSize = j1;
    }

    private static void decompress(BZ2Block block)
    {
        int tMinLen = 0;
        int tLimit[] = null;
        int tBase[] = null;
        int tPerm[] = null;
        block.blockSize_100k = 1;
        if(BZ2Block.ll8 == null)
            BZ2Block.ll8 = new int[block.blockSize_100k * 0x186a0];
        boolean reading = true;
        while(reading) 
        {
            byte head = readUChar(block);
            if(head == 23)
                return;
            head = readUChar(block);
            head = readUChar(block);
            head = readUChar(block);
            head = readUChar(block);
            head = readUChar(block);
            block.blockNo++;
            head = readUChar(block);
            head = readUChar(block);
            head = readUChar(block);
            head = readUChar(block);
            head = readBit(block);
            if(head != 0)
                block.randomized = true;
            else
                block.randomized = false;
            if(block.randomized)
                System.out.println("PANIC! RANDOMISED BLOCK!");
            block.origPtr = 0;
            head = readUChar(block);
            block.origPtr = block.origPtr << 8 | head & 0xff;
            head = readUChar(block);
            block.origPtr = block.origPtr << 8 | head & 0xff;
            head = readUChar(block);
            block.origPtr = block.origPtr << 8 | head & 0xff;
            for(int j = 0; j < 16; j++)
            {
                byte byte1 = readBit(block);
                if(byte1 == 1)
                    block.inUse[j] = true;
                else
                    block.inUse[j] = false;
            }

            for(int k = 0; k < 256; k++)
                block.use[k] = false;

            for(int l = 0; l < 16; l++)
                if(block.inUse[l])
                {
                    for(int i3 = 0; i3 < 16; i3++)
                    {
                        byte v = readBit(block);
                        if(v == 1)
                            block.use[l * 16 + i3] = true;
                    }

                }

            makeMaps(block);
            int alphaSize = block.nInUse + 2;
            int groups = getBits(3, block);
            int k4 = getBits(15, block);
            for(int i1 = 0; i1 < k4; i1++)
            {
                int selectorValue = 0;
                do
                {
                    byte v = readBit(block);
                    if(v == 0)
                        break;
                    selectorValue++;
                } while(true);
                block.selectorMtf[i1] = (byte)selectorValue;
            }

            byte pos[] = new byte[6];
            for(byte byteposIdx = 0; byteposIdx < groups; byteposIdx++)
                pos[byteposIdx] = byteposIdx;

            for(int selectorIdx = 0; selectorIdx < k4; selectorIdx++)
            {
                byte selectorMtf = block.selectorMtf[selectorIdx];
                byte curSelectorMtf = pos[selectorMtf];
                for(; selectorMtf > 0; selectorMtf--)
                    pos[selectorMtf] = pos[selectorMtf - 1];

                pos[0] = curSelectorMtf;
                block.selector[selectorIdx] = curSelectorMtf;
            }

            for(int i = 0; i < groups; i++)
            {
                int curr = getBits(5, block);
                for(int j = 0; j < alphaSize; j++)
                {
                    do
                    {
                        byte flag = readBit(block);
                        if(flag == 0)
                            break;
                        flag = readBit(block);
                        if(flag == 0)
                            curr++;
                        else
                            curr--;
                    } while(true);
                    block.len[i][j] = (byte)curr;
                }

            }

            for(int i = 0; i < groups; i++)
            {
                byte minLen = 32;
                int maxLen = 0;
                for(int l1 = 0; l1 < alphaSize; l1++)
                {
                    if(block.len[i][l1] > maxLen)
                        maxLen = block.len[i][l1];
                    if(block.len[i][l1] < minLen)
                        minLen = block.len[i][l1];
                }

                createDecodeTables(block.limit[i], block.base[i], block.perm[i], block.len[i], minLen, maxLen, alphaSize);
                block.minLens[i] = minLen;
            }

            int endOfBlock = block.nInUse + 1;
            int groupNo = -1;
            int groupPos = 0;
            for(int i = 0; i <= 255; i++)
                block.unzftab[i] = 0;

            int kk = 4095;
            for(int i = 15; i >= 0; i--)
            {
                for(int j = 15; j >= 0; j--)
                {
                    block.yy[kk] = (byte)(i * 16 + j);
                    kk--;
                }

                block.mtf16[i] = kk + 1;
            }

            int last = 0;
            if(groupPos == 0)
            {
                groupNo++;
                groupPos = 50;
                byte zt = block.selector[groupNo];
                tMinLen = block.minLens[zt];
                tLimit = block.limit[zt];
                tPerm = block.perm[zt];
                tBase = block.base[zt];
            }
            groupPos--;
            int zt = tMinLen;
            int zvec;
            byte bit;
            for(zvec = getBits(zt, block); zvec > tLimit[zt]; zvec = zvec << 1 | bit)
            {
                zt++;
                bit = readBit(block);
            }

            for(int nextSym = tPerm[zvec - tBase[zt]]; nextSym != endOfBlock;)
                if(nextSym == 0 || nextSym == 1)
                {
                    int es = -1;
                    int n = 1;
                    do
                    {
                        if(nextSym == 0)
                            es += n;
                        else
                        if(nextSym == 1)
                            es += 2 * n;
                        n *= 2;
                        if(groupPos == 0)
                        {
                            groupNo++;
                            groupPos = 50;
                            byte tSelector = block.selector[groupNo];
                            tMinLen = block.minLens[tSelector];
                            tLimit = block.limit[tSelector];
                            tPerm = block.perm[tSelector];
                            tBase = block.base[tSelector];
                        }
                        groupPos--;
                        int j7 = tMinLen;
                        int i8;
                        byte byte10;
                        for(i8 = getBits(j7, block); i8 > tLimit[j7]; i8 = i8 << 1 | byte10)
                        {
                            j7++;
                            byte10 = readBit(block);
                        }

                        nextSym = tPerm[i8 - tBase[j7]];
                    } while(nextSym == 0 || nextSym == 1);
                    es++;
                    byte byte5 = block.seqToUnseq[block.yy[block.mtf16[0]] & 0xff];
                    block.unzftab[byte5 & 0xff] += es;
                    for(; es > 0; es--)
                    {
                        BZ2Block.ll8[last] = byte5 & 0xff;
                        last++;
                    }

                } else
                {
                    int nn = nextSym - 1;
                    byte tmp;
                    if(nn < 16)
                    {
                        int j10 = block.mtf16[0];
                        tmp = block.yy[j10 + nn];
                        for(; nn > 3; nn -= 4)
                        {
                            int k11 = j10 + nn;
                            block.yy[k11] = block.yy[k11 - 1];
                            block.yy[k11 - 1] = block.yy[k11 - 2];
                            block.yy[k11 - 2] = block.yy[k11 - 3];
                            block.yy[k11 - 3] = block.yy[k11 - 4];
                        }

                        for(; nn > 0; nn--)
                            block.yy[j10 + nn] = block.yy[(j10 + nn) - 1];

                        block.yy[j10] = tmp;
                    } else
                    {
                        int lno = nn / 16;
                        int off = nn % 16;
                        int pp = block.mtf16[lno] + off;
                        tmp = block.yy[pp];
                        for(; pp > block.mtf16[lno]; pp--)
                            block.yy[pp] = block.yy[pp - 1];

                        block.mtf16[lno]++;
                        for(; lno > 0; lno--)
                        {
                            block.mtf16[lno]--;
                            block.yy[block.mtf16[lno]] = block.yy[(block.mtf16[lno - 1] + 16) - 1];
                        }

                        block.mtf16[0]--;
                        block.yy[block.mtf16[0]] = tmp;
                        if(block.mtf16[0] == 0)
                        {
                            int i10 = 4095;
                            for(int k9 = 15; k9 >= 0; k9--)
                            {
                                for(int l9 = 15; l9 >= 0; l9--)
                                {
                                    block.yy[i10] = block.yy[block.mtf16[k9] + l9];
                                    i10--;
                                }

                                block.mtf16[k9] = i10 + 1;
                            }

                        }
                    }
                    block.unzftab[block.seqToUnseq[tmp & 0xff] & 0xff]++;
                    BZ2Block.ll8[last] = block.seqToUnseq[tmp & 0xff] & 0xff;
                    last++;
                    if(groupPos == 0)
                    {
                        groupNo++;
                        groupPos = 50;
                        byte gsel = block.selector[groupNo];
                        tMinLen = block.minLens[gsel];
                        tLimit = block.limit[gsel];
                        tPerm = block.perm[gsel];
                        tBase = block.base[gsel];
                    }
                    groupPos--;
                    int k7 = tMinLen;
                    int j8;
                    byte byte11;
                    for(j8 = getBits(k7, block); j8 > tLimit[k7]; j8 = j8 << 1 | byte11)
                    {
                        k7++;
                        byte11 = readBit(block);
                    }

                    nextSym = tPerm[j8 - tBase[k7]];
                }

            block.stateOutLEN = 0;
            block.stateOutCH = 0;
            block.cftab[0] = 0;
            for(int j2 = 1; j2 <= 256; j2++)
                block.cftab[j2] = block.unzftab[j2 - 1];

            for(int k2 = 1; k2 <= 256; k2++)
                block.cftab[k2] += block.cftab[k2 - 1];

            for(int l2 = 0; l2 < last; l2++)
            {
                byte ch = (byte)(BZ2Block.ll8[l2] & 0xff);
                BZ2Block.ll8[block.cftab[ch & 0xff]] |= l2 << 8;
                block.cftab[ch & 0xff]++;
            }

            block.nextOut = BZ2Block.ll8[block.origPtr] >> 8;
            block.nBlockUsed = 0;
            block.nextOut = BZ2Block.ll8[block.nextOut];
            block.k0 = (byte)(block.nextOut & 0xff);
            block.nextOut >>= 8;
            block.nBlockUsed++;
            block.nBlockPP = last;
            method226(block);
            if(block.nBlockUsed == block.nBlockPP + 1 && block.stateOutLEN == 0)
                reading = true;
            else
                reading = false;
        }
    }

    private static byte readUChar(BZ2Block block)
    {
        return (byte)getBits(8, block);
    }

    private static byte readBit(BZ2Block block)
    {
        return (byte)getBits(1, block);
    }

    private static int getBits(int i, BZ2Block block)
    {
        int j;
        do
        {
            if(block.bsLive >= i)
            {
                int k = block.bsBuff >> block.bsLive - i & (1 << i) - 1;
                block.bsLive -= i;
                j = k;
                break;
            }
            block.bsBuff = block.bsBuff << 8 | block.input[block.nextIn] & 0xff;
            block.bsLive += 8;
            block.nextIn++;
            block.compressedSize--;
            block.totalInLo32++;
            if(block.totalInLo32 == 0)
                block.totalInHi32++;
        } while(true);
        return j;
    }

    private static void makeMaps(BZ2Block block)
    {
        block.nInUse = 0;
        for(int i = 0; i < 256; i++)
            if(block.use[i])
            {
                block.seqToUnseq[block.nInUse] = (byte)i;
                block.nInUse++;
            }

    }

    private static void createDecodeTables(int limit[], int base[], int perm[], byte len[], int minLen, int j, int k)
    {
        int pp = 0;
        for(int i = minLen; i <= j; i++)
        {
            for(int i_ = 0; i_ < k; i_++)
                if(len[i_] == i)
                {
                    perm[pp] = i_;
                    pp++;
                }

        }

        for(int i = 0; i < 23; i++)
            base[i] = 0;

        for(int i = 0; i < k; i++)
            base[len[i] + 1]++;

        for(int i = 1; i < 23; i++)
            base[i] += base[i - 1];

        for(int i = 0; i < 23; i++)
            limit[i] = 0;

        int vec = 0;
        for(int i = minLen; i <= j; i++)
        {
            vec += base[i + 1] - base[i];
            limit[i] = vec - 1;
            vec <<= 1;
        }

        for(int k2 = minLen + 1; k2 <= j; k2++)
            base[k2] = (limit[k2 - 1] + 1 << 1) - base[k2];

    }

    private static BZ2Block block = new BZ2Block();

}
