

final class Sounds {

	private static byte[] fileData;

	public static final int[] anIntArray326 = new int[5000];

	private static final Sounds[] soundCache = new Sounds[5000];

	private static Stream outStream;

	public static Stream fetchSoundData(int i, int j) {
		if (soundCache[j] != null) {
			Sounds sounds = soundCache[j];
			return sounds.pack(i);
		} else {
			return null;
		}
	}

	public static void unpack(Stream stream) {
		fileData = new byte[0x6baa8];//441000
		outStream = new Stream(fileData);
		Synthesizer.initialise();
		do {
			int j = stream.readUnsignedWord();
			if (j == 65535)
				return;
			soundCache[j] = new Sounds();
			soundCache[j].decode(stream);
			anIntArray326[j] = soundCache[j].method243();
		} while (true);
	}

	private final Synthesizer[] samples;

	private int anInt330;
	private int anInt331;

	private Sounds() {
		samples = new Synthesizer[10];
	}

	private void decode(Stream stream) {
		for (int i = 0; i < 10; i++) {
			int j = stream.readUnsignedByte();
			if (j != 0) {
				stream.currentOffset--;
				samples[i] = new Synthesizer();
				samples[i].decode(stream);
			}
		}
		anInt330 = stream.readUnsignedWord();
		anInt331 = stream.readUnsignedWord();
	}

	private int method243() {
		int j = 0x98967f;
		for (int k = 0; k < 10; k++)
			if (samples[k] != null && samples[k].remaining / 20 < j)
				j = samples[k].remaining / 20;

		if (anInt330 < anInt331 && anInt330 / 20 < j)
			j = anInt330 / 20;
		if (j == 0x98967f || j == 0)
			return 0;
		for (int l = 0; l < 10; l++)
			if (samples[l] != null)
				samples[l].remaining -= j * 20;

		if (anInt330 < anInt331) {
			anInt330 -= j * 20;
			anInt331 -= j * 20;
		}
		return j;
	}

	private Stream pack(int volume) {
		int k = encode(volume);
		outStream.currentOffset = 0;
		outStream.writeDWord(0x52494646);
		outStream.writeLEInt(36 + k);
		outStream.writeDWord(0x57415645);
		outStream.writeDWord(0x666d7420);
		outStream.writeLEInt(16);
		outStream.writeLEShort(1);
		outStream.writeLEShort(1);
		outStream.writeLEInt(22050);
		outStream.writeLEInt(22050);
		outStream.writeLEShort(1);
		outStream.writeLEShort(8);
		outStream.writeDWord(0x64617461);
		outStream.writeLEInt(k);
		outStream.currentOffset += k;
		return outStream;
	}

	private int encode(int i) {
		int j = 0;
		for (int k = 0; k < 10; k++)
			if (samples[k] != null && samples[k].offset + samples[k].remaining > j)
				j = samples[k].offset + samples[k].remaining;

		if (j == 0)
			return 0;
		int l = (22050 * j) / 1000;
		int i1 = (22050 * anInt330) / 1000;
		int j1 = (22050 * anInt331) / 1000;
		if (i1 < 0 || i1 > l || j1 < 0 || j1 > l || i1 >= j1)
			i = 0;
		int k1 = l + (j1 - i1) * (i - 1);
		for (int l1 = 44; l1 < k1 + 44; l1++)
			fileData[l1] = -128;

		for (int i2 = 0; i2 < 10; i2++)
			if (samples[i2] != null) {
				int j2 = (samples[i2].offset * 22050) / 1000;
				int i3 = (samples[i2].remaining * 22050) / 1000;
				int ai[] = samples[i2].synthesize(j2, samples[i2].offset);
				for (int l3 = 0; l3 < j2; l3++)
					fileData[l3 + i3 + 44] += (byte) (ai[l3] >> 8);

			}

		if (i > 1) {
			i1 += 44;
			j1 += 44;
			l += 44;
			int k2 = (k1 += 44) - l;
			for (int j3 = l - 1; j3 >= j1; j3--)
				fileData[j3 + k2] = fileData[j3];

			for (int k3 = 1; k3 < i; k3++) {
				int l2 = (j1 - i1) * k3;
				System.arraycopy(fileData, i1, fileData, i1 + l2, j1 - i1);

			}

			k1 -= 44;
		}
		return k1;
	}

}