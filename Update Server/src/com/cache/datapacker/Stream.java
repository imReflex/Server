package com.cache.datapacker;

public final class Stream extends NodeSub {
	
	int read3Bytes()
    {
        currentOffset += 3;
        return ((data[currentOffset - 3] & 0xff) << 16) + ((data[currentOffset - 2] & 0xff) << 8) + (data[currentOffset - 1] & 0xff);
    }

    final int readUnsignedSmart() {
    	int i_76_ = data[currentOffset] & 0xff;
    	if ((i_76_ ^ 0xffffffff) > -129)
    		return readUnsignedByte();
        return -32768 + readUShort();
    }
    final int read24BitInt() {//read24bitint
	    currentOffset += 3;
	    return ((0xff & data[-1 + currentOffset])
		    + ((data[-2 + currentOffset] << 8
			& 0xff00)
		       + ((data[-3 + currentOffset] & 0xff)
			  << 16)));
    }
    final int readInt() {//readInt?
	   currentOffset += 4;
	    return ((0xff & data[-1 + currentOffset]) + (data[currentOffset - 2] << 8 & 0xff00)
		    + (data[-4 + currentOffset] << 24
		       & ~0xffffff)
		    - -(0xff0000
			& data[-3 + currentOffset] << 16));
    }
    final byte readByte() {//readByte
	    return data[currentOffset++];
    }
    	final byte readShort() {//readShort
	   	 return (byte) (-data[currentOffset++] + 128);
    	}
	final int v(int i) {
		currentOffset += 3;
		return (0xff & data[currentOffset - 3] << 16) + (0xff & data[currentOffset - 2] << 8) + (0xff & data[currentOffset - 1]);
    	}
	public Stream(byte abyte0[])
	{
            data = abyte0;
            currentOffset = 0;

    }

    Stream(byte abyte0[], int i)
    {
        data = abyte0;
        currentOffset = 0;
        return;
    }
	int readUnsignedByte()
    {
        return data[currentOffset++] & 0xff;
    }

    int readUShort()
    {
        currentOffset += 2;
        return ((data[currentOffset - 2] & 0xff) << 8) + (data[currentOffset - 1] & 0xff);
    }
    int readUnsignedWord()
    {
        currentOffset += 2;
        return ((data[currentOffset - 2] & 0xff) << 8) + (data[currentOffset - 1] & 0xff);
    }
   int read3Bytes1()
    {
        currentOffset += 3;
        return ((data[currentOffset - 3] & 0xff) << 16) + ((data[currentOffset - 2] & 0xff) << 8) + (data[currentOffset - 1] & 0xff);
    }

    int readDWord()
    {
        currentOffset += 4;
        return ((data[currentOffset - 4] & 0xff) << 24) + ((data[currentOffset - 3] & 0xff) << 16) + ((data[currentOffset - 2] & 0xff) << 8) + (data[currentOffset - 1] & 0xff);
    }    

    public byte data[];
    int currentOffset;
}
