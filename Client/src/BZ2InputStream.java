

@SuppressWarnings("all")
final class BZ2InputStream {

	public static int decompressBuffer(byte outputBuff[], int decompressedSize, byte input[], int cSize, int nextIn) {
		synchronized (block) {
			block.input = input;
			block.nextIn = nextIn;
			block.out = outputBuff;
			block.availOut = 0;
			block.compressedSize = cSize;
			block.decompressedSize = decompressedSize;
			block.bsLive = 0;
			block.bsBuff = 0;
			block.totalInLo32 = 0;
			block.totalInHi32 = 0;
			block.totalOutLo32 = 0;
			block.totalOutHi32 = 0;
			block.blockNo = 0;
			decompress(block);
			decompressedSize -= block.decompressedSize;
			return decompressedSize;
		}
	}

	private static void getNextFileHeader(BZ2Block archive) {
		byte byte4 = archive.state_out_ch;
		int i = archive.state_out_len;
		int j = archive.nBlock_used;
		int k = archive.k0;
		int ai[] = BZ2Block.ll8;
		int l = archive.nextOut;
		byte abyte0[] = archive.out;
		int i1 = archive.availOut;
		int j1 = archive.decompressedSize;
		int k1 = j1;
		int l1 = archive.nBlock_pp + 1;
		label0: do {
			if (i > 0) {
				do {
					if (j1 == 0)
						break label0;
					if (i == 1)
						break;
					abyte0[i1] = byte4;
					i--;
					i1++;
					j1--;
				} while (true);
				if (j1 == 0) {
					i = 1;
					break;
				}
				abyte0[i1] = byte4;
				i1++;
				j1--;
			}
			boolean flag = true;
			while (flag) {
				flag = false;
				if (j == l1) {
					i = 0;
					break label0;
				}
				byte4 = (byte) k;
				l = ai[l];
				byte byte0 = (byte) (l & 0xff);
				l >>= 8;
				j++;
				if (byte0 != k) {
					k = byte0;
					if (j1 == 0) {
						i = 1;
					} else {
						abyte0[i1] = byte4;
						i1++;
						j1--;
						flag = true;
						continue;
					}
					break label0;
				}
				if (j != l1)
					continue;
				if (j1 == 0) {
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
			byte byte1 = (byte) (l & 0xff);
			l >>= 8;
			if (++j != l1)
				if (byte1 != k) {
					k = byte1;
				} else {
					i = 3;
					l = ai[l];
					byte byte2 = (byte) (l & 0xff);
					l >>= 8;
					if (++j != l1)
						if (byte2 != k) {
							k = byte2;
						} else {
							l = ai[l];
							byte byte3 = (byte) (l & 0xff);
							l >>= 8;
							j++;
							i = (byte3 & 0xff) + 4;
							l = ai[l];
							k = (byte) (l & 0xff);
							l >>= 8;
							j++;
						}
				}
		} while (true);
		int i2 = archive.totalOutLo32;
		archive.totalOutLo32 += k1 - j1;
		if (archive.totalOutLo32 < i2)
			archive.totalOutHi32++;
		archive.state_out_ch = byte4;
		archive.state_out_len = i;
		archive.nBlock_used = j;
		archive.k0 = k;
		BZ2Block.ll8 = ai;
		archive.nextOut = l;
		archive.out = abyte0;
		archive.availOut = i1;
		archive.decompressedSize = j1;
	}

	private static void decompress(BZ2Block block) {
		int k8 = 0;
		int ai[] = null;
		int ai1[] = null;
		int ai2[] = null;
		block.blocksize_100k = 1;
		if (BZ2Block.ll8 == null)
			BZ2Block.ll8 = new int[block.blocksize_100k * 0x186a0];
		boolean flag19 = true;
		while (flag19) {
			byte byte0 = reachUChar(block);
			if (byte0 == 23)
				return;
			byte0 = reachUChar(block);
			byte0 = reachUChar(block);
			byte0 = reachUChar(block);
			byte0 = reachUChar(block);
			byte0 = reachUChar(block);
			block.blockNo++;
			byte0 = reachUChar(block);
			byte0 = reachUChar(block);
			byte0 = reachUChar(block);
			byte0 = reachUChar(block);
			byte0 = readBit(block);
			block.randomized = byte0 != 0;
			if (block.randomized)
				System.out.println("PANIC! RANDOMISED BLOCK!");
			block.origPtr = 0;
			byte0 = reachUChar(block);
			block.origPtr = block.origPtr << 8 | byte0 & 0xff;
			byte0 = reachUChar(block);
			block.origPtr = block.origPtr << 8 | byte0 & 0xff;
			byte0 = reachUChar(block);
			block.origPtr = block.origPtr << 8 | byte0 & 0xff;
			for (int j = 0; j < 16; j++) {
				byte byte1 = readBit(block);
				block.inUse16[j] = byte1 == 1;
			}

			for (int k = 0; k < 256; k++)
				block.inUse[k] = false;

			for (int l = 0; l < 16; l++)
				if (block.inUse16[l]) {
					for (int i3 = 0; i3 < 16; i3++) {
						byte byte2 = readBit(block);
						if (byte2 == 1)
							block.inUse[l * 16 + i3] = true;
					}

				}

			makeMaps(block);
			int i4 = block.nInUse + 2;
			int j4 = getBits(3, block);
			int k4 = getBits(15, block);
			for (int i1 = 0; i1 < k4; i1++) {
				int j3 = 0;
				do {
					byte byte3 = readBit(block);
					if (byte3 == 0)
						break;
					j3++;
				} while (true);
				block.selectorMtf[i1] = (byte) j3;
			}

			byte abyte0[] = new byte[6];
			for (byte byte16 = 0; byte16 < j4; byte16++)
				abyte0[byte16] = byte16;

			for (int j1 = 0; j1 < k4; j1++) {
				byte byte17 = block.selectorMtf[j1];
				byte byte15 = abyte0[byte17];
				for (; byte17 > 0; byte17--)
					abyte0[byte17] = abyte0[byte17 - 1];

				abyte0[0] = byte15;
				block.selector[j1] = byte15;
			}

			for (int k3 = 0; k3 < j4; k3++) {
				int l6 = getBits(5, block);
				for (int k1 = 0; k1 < i4; k1++) {
					do {
						byte byte4 = readBit(block);
						if (byte4 == 0)
							break;
						byte4 = readBit(block);
						if (byte4 == 0)
							l6++;
						else
							l6--;
					} while (true);
					block.len[k3][k1] = (byte) l6;
				}

			}

			for (int l3 = 0; l3 < j4; l3++) {
				byte byte8 = 32;
				int i = 0;
				for (int l1 = 0; l1 < i4; l1++) {
					if (block.len[l3][l1] > i)
						i = block.len[l3][l1];
					if (block.len[l3][l1] < byte8)
						byte8 = block.len[l3][l1];
				}

				createDecodeTables(block.limit[l3], block.base[l3], block.perm[l3], block.len[l3], byte8, i, i4);
				block.minLens[l3] = byte8;
			}

			int l4 = block.nInUse + 1;
			int l5 = 0x186a0 * block.blocksize_100k;
			int i5 = -1;
			int j5 = 0;
			for (int i2 = 0; i2 <= 255; i2++)
				block.unzftab[i2] = 0;

			int j9 = 4095;
			for (int l8 = 15; l8 >= 0; l8--) {
				for (int i9 = 15; i9 >= 0; i9--) {
					block.yy[j9] = (byte) (l8 * 16 + i9);
					j9--;
				}

				block.mtf16[l8] = j9 + 1;
			}

			int i6 = 0;
			if (j5 == 0) {
				i5++;
				j5 = 50;
				byte byte12 = block.selector[i5];
				k8 = block.minLens[byte12];
				ai = block.limit[byte12];
				ai2 = block.perm[byte12];
				ai1 = block.base[byte12];
			}
			j5--;
			int i7 = k8;
			int l7;
			byte byte9;
			for (l7 = getBits(i7, block); l7 > ai[i7]; l7 = l7 << 1 | byte9) {
				i7++;
				byte9 = readBit(block);
			}

			for (int k5 = ai2[l7 - ai1[i7]]; k5 != l4;)
				if (k5 == 0 || k5 == 1) {
					int j6 = -1;
					int k6 = 1;
					do {
						if (k5 == 0)
							j6 += k6;
						else if (k5 == 1)
							j6 += 2 * k6;
						k6 *= 2;
						if (j5 == 0) {
							i5++;
							j5 = 50;
							byte byte13 = block.selector[i5];
							k8 = block.minLens[byte13];
							ai = block.limit[byte13];
							ai2 = block.perm[byte13];
							ai1 = block.base[byte13];
						}
						j5--;
						int j7 = k8;
						int i8;
						byte byte10;
						for (i8 = getBits(j7, block); i8 > ai[j7]; i8 = i8 << 1 | byte10) {
							j7++;
							byte10 = readBit(block);
						}

						k5 = ai2[i8 - ai1[j7]];
					} while (k5 == 0 || k5 == 1);
					j6++;
					byte byte5 = block.seqToUnseq[block.yy[block.mtf16[0]] & 0xff];
					block.unzftab[byte5 & 0xff] += j6;
					for (; j6 > 0; j6--) {
						BZ2Block.ll8[i6] = byte5 & 0xff;
						i6++;
					}

				} else {
					int j11 = k5 - 1;
					byte byte6;
					if (j11 < 16) {
						int j10 = block.mtf16[0];
						byte6 = block.yy[j10 + j11];
						for (; j11 > 3; j11 -= 4) {
							int k11 = j10 + j11;
							block.yy[k11] = block.yy[k11 - 1];
							block.yy[k11 - 1] = block.yy[k11 - 2];
							block.yy[k11 - 2] = block.yy[k11 - 3];
							block.yy[k11 - 3] = block.yy[k11 - 4];
						}

						for (; j11 > 0; j11--)
							block.yy[j10 + j11] = block.yy[(j10 + j11) - 1];

						block.yy[j10] = byte6;
					} else {
						int l10 = j11 / 16;
						int i11 = j11 % 16;
						int k10 = block.mtf16[l10] + i11;
						byte6 = block.yy[k10];
						for (; k10 > block.mtf16[l10]; k10--)
							block.yy[k10] = block.yy[k10 - 1];

						block.mtf16[l10]++;
						for (; l10 > 0; l10--) {
							block.mtf16[l10]--;
							block.yy[block.mtf16[l10]] = block.yy[(block.mtf16[l10 - 1] + 16) - 1];
						}

						block.mtf16[0]--;
						block.yy[block.mtf16[0]] = byte6;
						if (block.mtf16[0] == 0) {
							int i10 = 4095;
							for (int k9 = 15; k9 >= 0; k9--) {
								for (int l9 = 15; l9 >= 0; l9--) {
									block.yy[i10] = block.yy[block.mtf16[k9] + l9];
									i10--;
								}

								block.mtf16[k9] = i10 + 1;
							}

						}
					}
					block.unzftab[block.seqToUnseq[byte6 & 0xff] & 0xff]++;
					BZ2Block.ll8[i6] = block.seqToUnseq[byte6 & 0xff] & 0xff;
					i6++;
					if (j5 == 0) {
						i5++;
						j5 = 50;
						byte byte14 = block.selector[i5];
						k8 = block.minLens[byte14];
						ai = block.limit[byte14];
						ai2 = block.perm[byte14];
						ai1 = block.base[byte14];
					}
					j5--;
					int k7 = k8;
					int j8;
					byte byte11;
					for (j8 = getBits(k7, block); j8 > ai[k7]; j8 = j8 << 1 | byte11) {
						k7++;
						byte11 = readBit(block);
					}

					k5 = ai2[j8 - ai1[k7]];
				}

			block.state_out_len = 0;
			block.state_out_ch = 0;
			block.cftab[0] = 0;
			for (int j2 = 1; j2 <= 256; j2++)
				block.cftab[j2] = block.unzftab[j2 - 1];

			for (int k2 = 1; k2 <= 256; k2++)
				block.cftab[k2] += block.cftab[k2 - 1];

			for (int l2 = 0; l2 < i6; l2++) {
				byte byte7 = (byte) (BZ2Block.ll8[l2] & 0xff);
				BZ2Block.ll8[block.cftab[byte7 & 0xff]] |= l2 << 8;
				block.cftab[byte7 & 0xff]++;
			}

			block.nextOut = BZ2Block.ll8[block.origPtr] >> 8;
			block.nBlock_used = 0;
			block.nextOut = BZ2Block.ll8[block.nextOut];
			block.k0 = (byte) (block.nextOut & 0xff);
			block.nextOut >>= 8;
			block.nBlock_used++;
			block.nBlock_pp = i6;
			getNextFileHeader(block);
			flag19 = block.nBlock_used == block.nBlock_pp + 1 && block.state_out_len == 0;
		}
	}

	private static byte reachUChar(BZ2Block block) {
		return (byte) getBits(8, block);
	}

	private static byte readBit(BZ2Block block) {
		return (byte) getBits(1, block);
	}

	private static int getBits(int i, BZ2Block block) {
		int j;
		do {
			if (block.bsLive >= i) {
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
			if (block.totalInLo32 == 0)
				block.totalInHi32++;
		} while (true);
		return j;
	}

	private static void makeMaps(BZ2Block block) {
		block.nInUse = 0;
		for (int i = 0; i < 256; i++)
			if (block.inUse[i]) {
				block.seqToUnseq[block.nInUse] = (byte) i;
				block.nInUse++;
			}

	}

	private static void createDecodeTables(int ai[], int ai1[], int ai2[], byte abyte0[], int i, int j, int k) {
		int l = 0;
		for (int i1 = i; i1 <= j; i1++) {
			for (int l2 = 0; l2 < k; l2++)
				if (abyte0[l2] == i1) {
					ai2[l] = l2;
					l++;
				}

		}

		for (int j1 = 0; j1 < 23; j1++)
			ai1[j1] = 0;

		for (int k1 = 0; k1 < k; k1++)
			ai1[abyte0[k1] + 1]++;

		for (int l1 = 1; l1 < 23; l1++)
			ai1[l1] += ai1[l1 - 1];

		for (int i2 = 0; i2 < 23; i2++)
			ai[i2] = 0;

		int i3 = 0;
		for (int j2 = i; j2 <= j; j2++) {
			i3 += ai1[j2 + 1] - ai1[j2];
			ai[j2] = i3 - 1;
			i3 <<= 1;
		}

		for (int k2 = i + 1; k2 <= j; k2++)
			ai1[k2] = (ai[k2 - 1] + 1 << 1) - ai1[k2];

	}

	private static final BZ2Block block = new BZ2Block();

}
