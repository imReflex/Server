package com.cache.crcpacker;

import java.io.*;

final class Decompressor {

	public Decompressor(RandomAccessFile randomaccessfile, RandomAccessFile randomaccessfile1, int j) {
		anInt311 = j;
		dataFile = randomaccessfile;
		indexFile = randomaccessfile1;
	}


	private synchronized void seekTo(RandomAccessFile randomaccessfile, int j) throws IOException {
		try {
			randomaccessfile.seek(j);
		} catch(Exception e) {
			e.printStackTrace();
		} 
	}
	/**
	 * Returns the number of files in the cache index.
	 * @return
	 */
	public long getFileCount() {
		try {
			if (indexFile != null) {
				return (indexFile.length() / 6);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	public synchronized byte[] get(int i) {
		try {
			seekTo(indexFile, i * 6);
			int l;
			for(int j = 0; j < 6; j += l)
			{
				l = indexFile.read(buffer, j, 6 - j);
				if(l == -1)
					return null;
			}
			int i1 = ((buffer[0] & 0xff) << 16) + ((buffer[1] & 0xff) << 8) + (buffer[2] & 0xff);
			int j1 = ((buffer[3] & 0xff) << 16) + ((buffer[4] & 0xff) << 8) + (buffer[5] & 0xff);
			if(j1 <= 0 || (long)j1 > dataFile.length() / 520L)
				return null;
			byte abyte0[] = new byte[i1];
			int k1 = 0;
			for(int l1 = 0; k1 < i1; l1++) {
				if(j1 == 0)
					return null;
				seekTo(dataFile, j1 * 520);
				int k = 0;
				int i2 = i1 - k1;
				if(i2 > 512)
					i2 = 512;
				int j2;
				for(; k < i2 + 8; k += j2) {
					j2 = dataFile.read(buffer, k, (i2 + 8) - k);
					if(j2 == -1)
						return null;
				}
				int k2 = ((buffer[0] & 0xff) << 8) + (buffer[1] & 0xff);
				int l2 = ((buffer[2] & 0xff) << 8) + (buffer[3] & 0xff);
				int i3 = ((buffer[4] & 0xff) << 16) + ((buffer[5] & 0xff) << 8) + (buffer[6] & 0xff);
				int j3 = buffer[7] & 0xff;
				if(k2 != i || l2 != l1 || j3 != anInt311)
					return null;
				if(i3 < 0 || (long)i3 > dataFile.length() / 520L)
					return null;
				for(int k3 = 0; k3 < i2; k3++)
					abyte0[k1++] = buffer[k3 + 8];

				j1 = i3;
			}

			return abyte0;
		} catch(IOException _ex) {
			return null;
		}
	}

	private static final byte[] buffer = new byte[520];
	private final RandomAccessFile dataFile;
	private final RandomAccessFile indexFile;
	private final int anInt311;

}
