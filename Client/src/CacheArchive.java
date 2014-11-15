

// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("all")
public final class CacheArchive {

	public CacheArchive(byte abyte0[]) {
		Stream stream = new Stream(abyte0);
		int i = stream.read3Bytes();
		int j = stream.read3Bytes();
		if (j != i) {
			byte abyte1[] = new byte[i];
			BZ2InputStream.decompressBuffer(abyte1, i, abyte0, j, 6);
			outputData = abyte1;
			stream = new Stream(outputData);
			isCompressed = true;
		} else {
			outputData = abyte0;
			isCompressed = false;
		}
		dataSize = stream.readUnsignedWord();
		myNameIndexes = new int[dataSize];
		myFileSizes = new int[dataSize];
		myOnDiskFileSizes = new int[dataSize];
		myFileOffsets = new int[dataSize];
		int k = stream.currentOffset + dataSize * 10;
		for (int l = 0; l < dataSize; l++) {
			myNameIndexes[l] = stream.readDWord();
			myFileSizes[l] = stream.read3Bytes();
			myOnDiskFileSizes[l] = stream.read3Bytes();
			myFileOffsets[l] = k;
			k += myOnDiskFileSizes[l];
		}
	}

	public byte[] getDataForName(String s) {
		byte abyte0[] = null; // was a parameter
		int i = 0;
		s = s.toUpperCase();
		for (int j = 0; j < s.length(); j++)
			i = (i * 61 + s.charAt(j)) - 32;
		for (int k = 0; k < dataSize; k++)
			if (myNameIndexes[k] == i) {
				if (abyte0 == null)
					abyte0 = new byte[myFileSizes[k]];
				if (!isCompressed) {
					BZ2InputStream.decompressBuffer(abyte0, myFileSizes[k], outputData, myOnDiskFileSizes[k], myFileOffsets[k]);
				} else {
					System.arraycopy(outputData, myFileOffsets[k], abyte0, 0, myFileSizes[k]);
				}
				return abyte0;
			}

		return null;
	}

	public byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		if (length > Integer.MAX_VALUE) {
			// File is too large
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}

	private final byte[] outputData;
	private final int dataSize;
	private final int[] myNameIndexes;
	private final int[] myFileSizes;
	private final int[] myOnDiskFileSizes;
	private final int[] myFileOffsets;
	private final boolean isCompressed;
}
