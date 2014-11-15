

import java.math.BigInteger;

import sign.signlink;

@SuppressWarnings("all")
public final class ByteBuffer extends QueueNode {

	public byte buffer[];
	public int currentOffset;
	public int bitPosition;
	private static final int anIntArray1409[] = { 0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 0x1ffff, 0x3ffff, 0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff, -1 };
	public ISAACRandomGen encryption;
	private static int anInt1412;
	private static final Deque nodeList = new Deque();


	public static ByteBuffer create() {
		Deque nodelist = nodeList;
		ByteBuffer bytebuffer1;
		bytebuffer1 = null;
		if (anInt1412 > 0) {
			anInt1412--;
			bytebuffer1 = (ByteBuffer) nodeList.popFront();
		}
		if (bytebuffer1 == null) {
		}
		return bytebuffer1;
	}

	private ByteBuffer() {
	}

	public byte[] getData(byte abyte0[]) {
		for (int i = 0; i < abyte0.length; i++) {
			abyte0[i] = buffer[currentOffset++];
		}

		return abyte0;
	}

	final int b(int i) {
		int j = buffer[currentOffset] & 0xff;
		if (j < 128) {
			return readUnsignedByte() - 64;
		} else {
			return c(13111) - 49152;
		}
	}

	final int c(int i) {
		currentOffset += 2;
		return (buffer[currentOffset - 1] & 0xff) + (buffer[currentOffset - 2] << 8 & 0xff00);
	}

	final int v(int i) {
		currentOffset += 3;
		return (0xff & buffer[currentOffset - 3] << 16) + (0xff & buffer[currentOffset - 2] << 8) + (0xff & buffer[currentOffset - 1]);
	}

	public ByteBuffer(byte abyte0[]) {
		buffer = abyte0;
		currentOffset = 0;
	}

	public void writeByte(int i) {
		buffer[currentOffset++] = (byte) i;
	}

	public void createFrame(int i) {
		buffer[currentOffset++] = (byte) (i + encryption.getNextKey());
	}

	public void writeWordBigEndian(int i) {
		buffer[currentOffset++] = (byte) i;
	}

	public String readNewString() {
		int i = currentOffset;
		while (buffer[currentOffset++] != 0)
			;
		return new String(buffer, i, currentOffset - i - 1);
	}

	public int readUSmart2() {
		int i = 0;
		int j;
		for (j = 0; (j = method422()) == 32767;) {
			i += 32767;
		}

		return i + j;
	}

	public int readShort2() {
		currentOffset += 2;
		int i = ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] & 0xff);
		if (i > 60000) {
			i = -65535 + i;
		}
		return i;
	}

	public void writeWord(int i) {
		buffer[currentOffset++] = (byte) (i >> 8);
		buffer[currentOffset++] = (byte) i;
	}

	public void method400(int i) {
		buffer[currentOffset++] = (byte) i;
		buffer[currentOffset++] = (byte) (i >> 8);
	}

	public void writeDWordBigEndian(int i) {
		buffer[currentOffset++] = (byte) (i >> 16);
		buffer[currentOffset++] = (byte) (i >> 8);
		buffer[currentOffset++] = (byte) i;
	}

	public void writeDWord(int i) {
		buffer[currentOffset++] = (byte) (i >> 24);
		buffer[currentOffset++] = (byte) (i >> 16);
		buffer[currentOffset++] = (byte) (i >> 8);
		buffer[currentOffset++] = (byte) i;
	}

	public void method403(int i) {
		buffer[currentOffset++] = (byte) i;
		buffer[currentOffset++] = (byte) (i >> 8);
		buffer[currentOffset++] = (byte) (i >> 16);
		buffer[currentOffset++] = (byte) (i >> 24);
	}

	public void writeQWord(long l) {
		try {
			buffer[currentOffset++] = (byte) (int) (l >> 56);
			buffer[currentOffset++] = (byte) (int) (l >> 48);
			buffer[currentOffset++] = (byte) (int) (l >> 40);
			buffer[currentOffset++] = (byte) (int) (l >> 32);
			buffer[currentOffset++] = (byte) (int) (l >> 24);
			buffer[currentOffset++] = (byte) (int) (l >> 16);
			buffer[currentOffset++] = (byte) (int) (l >> 8);
			buffer[currentOffset++] = (byte) (int) l;
		} catch (RuntimeException runtimeexception) {
			signlink.reporterror((new StringBuilder()).append("14395, 5, ").append(l).append(", ").append(runtimeexception.toString()).toString());
			throw new RuntimeException();
		}
	}

	public void writeString(String s) {
		System.arraycopy(s.getBytes(), 0, buffer, currentOffset, s.length());
		currentOffset += s.length();
		buffer[currentOffset++] = 10;
	}

	public void writeBytes(byte abyte0[], int i, int j) {
		for (int k = j; k < j + i; k++) {
			buffer[currentOffset++] = abyte0[k];
		}

	}

	public void writeBytes(int i) {
		buffer[currentOffset - i - 1] = (byte) i;
	}

	public int readUnsignedByte() {
		// try {
		return buffer[currentOffset++] & 0xff;
		// } catch(Exception e) {
		// return readUnsignedByte2();
		// }
	}

	public byte readSignedByte() {
		return buffer[currentOffset++];
	}

	public int readUnsignedWord() {
		currentOffset += 2;
		return ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] & 0xff);
	}

	public int readSignedWord() {
		currentOffset += 2;
		int i = ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] & 0xff);
		if (i > 32767) {
			i -= 0x10000;
		}
		return i;
	}

	public int getMedium() {
		currentOffset += 3;
		return ((buffer[currentOffset - 3] & 0xff) << 16) + ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] & 0xff);
	}

	public int readDWord() {
		currentOffset += 4;
		return ((buffer[currentOffset - 4] & 0xff) << 24) + ((buffer[currentOffset - 3] & 0xff) << 16) + ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] & 0xff);
	}

	public long readQWord() {
		long l = (long) readDWord() & 0xffffffffL;
		long l1 = (long) readDWord() & 0xffffffffL;
		return (l << 32) + l1;
	}

	public String readString() {
		int i = currentOffset;
		while (buffer[currentOffset++] != 10)
			;
		return new String(buffer, i, currentOffset - i - 1);
	}

	public byte[] readBytes() {
		int i = currentOffset;
		while (buffer[currentOffset++] != 10)
			;
		byte abyte0[] = new byte[currentOffset - i - 1];
		System.arraycopy(buffer, i, abyte0, i - i, currentOffset - 1 - i);
		return abyte0;
	}

	public void readBytes(int i, int j, byte abyte0[]) {
		for (int k = j; k < j + i; k++) {
			abyte0[k] = buffer[currentOffset++];
		}

	}

	public void initBitAccess() {
		bitPosition = currentOffset * 8;
	}

	public int readBits(int i) {
		int j = bitPosition >> 3;
		int k = 8 - (bitPosition & 7);
		int l = 0;
		bitPosition += i;
		for (; i > k; k = 8) {
			l += (buffer[j++] & anIntArray1409[k]) << i - k;
			i -= k;
		}

		if (i == k) {
			l += buffer[j] & anIntArray1409[k];
		} else {
			l += buffer[j] >> k - i & anIntArray1409[i];
		}
		return l;
	}

	public void finishBitAccess() {
		currentOffset = (bitPosition + 7) / 8;
	}

	public int method421() {
		try {
			int i = buffer[currentOffset] & 0xff;
			if (i < 128)
				return readUnsignedByte() - 64;
			else
				return readUnsignedWord() - 49152;
		} catch (Exception e) {
			return -1;
		}
	}

	public int method422() {
		int i = buffer[currentOffset] & 0xff;
		if (i < 128) {
			return readUnsignedByte();
		} else {
			return readUnsignedWord() - 32768;
		}
	}

	public void doKeys() {
		int i = currentOffset;
		currentOffset = 0;
		byte abyte0[] = new byte[i];
		readBytes(i, 0, abyte0);
		BigInteger biginteger = new BigInteger(abyte0);
		BigInteger biginteger1 = biginteger;
		byte abyte1[] = biginteger1.toByteArray();
		currentOffset = 0;
		writeWordBigEndian(abyte1.length);
		writeBytes(abyte1, abyte1.length, 0);
	}

	public void method424(int i) {
		buffer[currentOffset++] = (byte) (-i);
	}

	public void method425(int i) {
		buffer[currentOffset++] = (byte) (128 - i);
	}

	public int method426() {
		return buffer[currentOffset++] - 128 & 0xff;
	}

	public int method427() {
		return -buffer[currentOffset++] & 0xff;
	}

	public int method428() {
		return 128 - buffer[currentOffset++] & 0xff;
	}

	public byte method429() {
		return (byte) (-buffer[currentOffset++]);
	}

	public byte method430() {
		return (byte) (128 - buffer[currentOffset++]);
	}

	public void method431(int i) {
		buffer[currentOffset++] = (byte) i;
		buffer[currentOffset++] = (byte) (i >> 8);
	}

	public void method432(int i) {
		buffer[currentOffset++] = (byte) (i >> 8);
		buffer[currentOffset++] = (byte) (i + 128);
	}

	public void method433(int i) {
		buffer[currentOffset++] = (byte) (i + 128);
		buffer[currentOffset++] = (byte) (i >> 8);
	}

	public int method434() {
		currentOffset += 2;
		return ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] & 0xff);
	}

	public int method435() {
		currentOffset += 2;
		return ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] - 128 & 0xff);
	}

	public int method436() {
		currentOffset += 2;
		return ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] - 128 & 0xff);
	}

	public int method437() {
		currentOffset += 2;
		int i = ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] & 0xff);
		if (i > 32767) {
			i -= 0x10000;
		}
		return i;
	}

	public int method438() {
		currentOffset += 2;
		int i = ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] - 128 & 0xff);
		if (i > 32767) {
			i -= 0x10000;
		}
		return i;
	}

	public int method439() {
		currentOffset += 4;
		return ((buffer[currentOffset - 2] & 0xff) << 24) + ((buffer[currentOffset - 1] & 0xff) << 16) + ((buffer[currentOffset - 4] & 0xff) << 8) + (buffer[currentOffset - 3] & 0xff);
	}

	public int method440() {
		currentOffset += 4;
		return ((buffer[currentOffset - 3] & 0xff) << 24) + ((buffer[currentOffset - 4] & 0xff) << 16) + ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] & 0xff);
	}

	public void method441(int i, byte abyte0[], int j) {
		for (int k = (i + j) - 1; k >= i; k--) {
			buffer[currentOffset++] = (byte) (abyte0[k] + 128);
		}

	}

	public void method442(int i, int j, byte abyte0[]) {
		for (int k = (j + i) - 1; k >= j; k--) {
			abyte0[k] = buffer[currentOffset++];
		}

	}

}
