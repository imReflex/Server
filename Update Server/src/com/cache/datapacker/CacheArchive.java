package com.cache.datapacker;

public final class CacheArchive
{
    public static String weirdStreamLoadingMethod()
    {
        return (new StringBuilder()).append(field923_Sub7).append("-").append(area923_Sub9).toString();
    }
    public CacheArchive(byte abyte0[])
    {
        read(abyte0);
        return;
    }

    private void read(byte abyte0[])
    {
        Stream stream = new Stream(abyte0, 891);
        int resultLength = stream.read3Bytes1();
        int rawLength = stream.read3Bytes1();
        if(rawLength != resultLength)
        {
            byte abyte1[] = new byte[resultLength];
            BZ2InputStream.decompressBuffer(abyte1, resultLength, abyte0, rawLength, 6);
            outputData = abyte1;
            stream = new Stream(outputData, 891);
            isCompressed = true;
        } else
        {
            outputData = abyte0;
            isCompressed = false;
        }
        dataSize = stream.readUShort();
        myNameIndexes = new int[dataSize];
        myFileSizes = new int[dataSize];
        myOnDiskFileSizes = new int[dataSize];
        myFileOffsets = new int[dataSize];
        int k = stream.currentOffset + dataSize * 10;
        for(int l = 0; l < dataSize; l++)
        {
            myNameIndexes[l] = stream.readDWord();
            myFileSizes[l] = stream.read3Bytes1();
            myOnDiskFileSizes[l] = stream.read3Bytes1();
            myFileOffsets[l] = k;
            k += myOnDiskFileSizes[l];
        }

    }

    public byte[] getDataForName(String name, byte data[])
    {
        int i = 0;
        name = name.toUpperCase();
        for(int j = 0; j < name.length(); j++)
            i = (i * 61 + name.charAt(j)) - 32;

        for(int k = 0; k < dataSize; k++)
            if(myNameIndexes[k] == i)
            {
                if(data == null)
                    data = new byte[myFileSizes[k]];
                if(!isCompressed)
                {
                    BZ2InputStream.decompressBuffer(data, myFileSizes[k], outputData, myOnDiskFileSizes[k], myFileOffsets[k]);
                } else
                {
                    for(int l = 0; l < myFileSizes[k]; l++)
                        data[l] = outputData[myFileOffsets[k] + l];

                }
                return data;
            }

        return null;
    }
    public byte outputData[];
    public int dataSize;
    public int myNameIndexes[];
    public int myFileSizes[];
    public int myOnDiskFileSizes[];
    public int myFileOffsets[];    
    public static String field923_Sub7;
    public static String area923_Sub9;
    private boolean isCompressed;

}
