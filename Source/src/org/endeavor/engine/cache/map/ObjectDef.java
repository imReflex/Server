package org.endeavor.engine.cache.map;

import org.endeavor.engine.cache.ByteStreamExt;

public final class ObjectDef {

	private static ByteStreamExt stream;
	public static int[] streamIndices;
	public static ObjectDef class46;

	private static int objects = 0;

	public static int getObjects() {
		return objects;
	}

	public static ObjectDef getObjectDef(int i) {
		if (i > streamIndices.length) {
			i = streamIndices.length - 1;
		}

		for (int j = 0; j < 20; j++) {
			if (cache[j].type == i) {
				return cache[j];
			}
		}

		cacheIndex = (cacheIndex + 1) % 20;
		class46 = cache[cacheIndex];

		if (i > streamIndices.length - 1 || i < 0) {
			return null;
		}

		stream.currentOffset = streamIndices[i];

		class46.type = i;
		class46.setDefaults();
		class46.readValues(stream);
		
		if (i == 14210 || i == 14211) {
			class46.aBoolean779 = true;
			class46.aBoolean757 = false;
			class46.objectSizeX = 2;
		}
		
		if (class46.name != null && class46.name.equalsIgnoreCase("daisies") || i == 38692) {
			class46.aBoolean779 = false;
			class46.aBoolean757 = false;
		}
		
		return class46;
	}

	private void setDefaults() {
		anIntArray773 = null;
		anIntArray776 = null;
		name = null;
		description = null;
		modifiedModelColors = null;
		originalModelColors = null;
		objectSizeX = 1;
		objectSizeY = 1;
		aBoolean767 = true;
		aBoolean757 = true;
		hasActions = false;
		aBoolean762 = false;
		aBoolean769 = false;
		aBoolean764 = false;
		anInt781 = -1;
		anInt775 = 16;
		aByte737 = 0;
		aByte742 = 0;
		actions = null;
		anInt746 = -1;
		anInt758 = -1;
		aBoolean751 = false;
		aBoolean779 = true;
		anInt748 = 128;
		anInt772 = 128;
		anInt740 = 128;
		anInt768 = 0;
		anInt738 = 0;
		anInt745 = 0;
		anInt783 = 0;
		aBoolean736 = false;
		aBoolean766 = false;
		anInt760 = -1;
		anInt774 = -1;
		anInt749 = -1;
		childrenIDs = null;
	}

	public static void loadConfig() {
		stream = new ByteStreamExt(getBuffer("loc.dat"));
		ByteStreamExt stream = new ByteStreamExt(getBuffer("loc.idx"));
		objects = stream.readUnsignedWord();
		streamIndices = new int[objects];
		int i = 2;
		for (int j = 0; j < objects; j++) {
			streamIndices[j] = i;
			i += stream.readUnsignedWord();
		}
		cache = new ObjectDef[20];
		for (int k = 0; k < 20; k++) {
			cache[k] = new ObjectDef();
		}
		System.out.println("Loaded " + objects + " Objects.");
	}

	public static byte[] getBuffer(String s) {
		try {
			java.io.File f = new java.io.File("./data/map/objectdata/" + s);
			if (!f.exists()) {
				return null;
			}
			byte[] buffer = new byte[(int) f.length()];
			java.io.DataInputStream dis = new java.io.DataInputStream(new java.io.FileInputStream(f));
			dis.readFully(buffer);
			dis.close();
			return buffer;
		} catch (Exception e) {
		}
		return null;
	}

	private void readValues(ByteStreamExt buffer) {
		int i = -1;
		label0: do {
			int opcode;
			do {
				opcode = buffer.readUnsignedByte(); 
				if (opcode == 0)
					break label0;
				if (opcode == 1) {
					int k = buffer.readUnsignedByte();
					if (k > 0)
						if (anIntArray773 == null || lowMem) {
							anIntArray776 = new int[k];
							anIntArray773 = new int[k];
							for (int k1 = 0; k1 < k; k1++) {
								anIntArray773[k1] = buffer.readUnsignedWord();
								anIntArray776[k1] = buffer.readUnsignedByte();
							}
						} else {
							buffer.currentOffset += k * 3;
						}
				} else if (opcode == 2)
					name = buffer.readString();
				else if (opcode == 3)
					description = buffer.readBytes();
				else if (opcode == 5) {
					int l = buffer.readUnsignedByte();
					if (l > 0)
						if (anIntArray773 == null || lowMem) {
							anIntArray776 = null;
							anIntArray773 = new int[l];
							for (int l1 = 0; l1 < l; l1++)
								anIntArray773[l1] = buffer.readUnsignedWord();
						} else {
							;//buffer.currentOffset += l * 2;
						}
				} else if (opcode == 14)
					objectSizeX = buffer.readUnsignedByte();
				else if (opcode == 15)
					objectSizeY = buffer.readUnsignedByte();
				else if (opcode == 17)
					aBoolean767 = false;
				else if (opcode == 18)
					aBoolean757 = false;
				else if (opcode == 19) {
					i = buffer.readUnsignedByte();
					if (i == 1)
						hasActions = true;
				} else if (opcode == 21)
					aBoolean762 = true;
				else if (opcode == 22)
					aBoolean769 = false;//
				else if (opcode == 23)
					aBoolean764 = true;
				else if (opcode == 24) {
					anInt781 = buffer.readUnsignedWord();
					if (anInt781 == 65535)
						anInt781 = -1;
				} else if (opcode == 28)
					anInt775 = buffer.readUnsignedByte();
				else if (opcode == 29)
					aByte737 = buffer.readSignedByte();
				else if (opcode == 39)
					aByte742 = buffer.readSignedByte();
				else if (opcode >= 30 && opcode < 39) {
					if (actions == null)
						actions = new String[10];
					actions[opcode - 30] = buffer.readString();
					if (actions[opcode - 30].equalsIgnoreCase("hidden"))
						actions[opcode - 30] = null;
				} else if (opcode == 40) {
					int i1 = buffer.readUnsignedByte();
					modifiedModelColors = new int[i1];
					originalModelColors = new int[i1];
					for (int i2 = 0; i2 < i1; i2++) {
						modifiedModelColors[i2] = buffer.readUnsignedWord();
						originalModelColors[i2] = buffer.readUnsignedWord();
					}
				} else if (opcode == 60)
					anInt746 = buffer.readUnsignedWord();
				else if (opcode == 62)
					aBoolean751 = true;
				else if (opcode == 64)
					aBoolean779 = false;
				else if (opcode == 65)
					anInt748 = buffer.readUnsignedWord();
				else if (opcode == 66)
					anInt772 = buffer.readUnsignedWord();
				else if (opcode == 67)
					anInt740 = buffer.readUnsignedWord();
				else if (opcode == 68)
					anInt758 = buffer.readUnsignedWord();
				else if (opcode == 69)
					anInt768 = buffer.readUnsignedByte();
				else if (opcode == 70)
					anInt738 = buffer.readSignedWord();
				else if (opcode == 71)
					anInt745 = buffer.readSignedWord();
				else if (opcode == 72)
					anInt783 = buffer.readSignedWord();
				else if (opcode == 73)
					aBoolean736 = true;
				else if (opcode == 74) {
					aBoolean766 = true;
				} else {
					if (opcode != 75)
						continue;
					anInt760 = buffer.readUnsignedByte();
				}
				continue label0;
			} while (opcode != 77);
				anInt774 = buffer.readUnsignedWord();
			if (anInt774 == 65535)
				anInt774 = -1;
				anInt749 = buffer.readUnsignedWord();
			if (anInt749 == 65535)
				anInt749 = -1;
			int j1 = buffer.readUnsignedByte();
			childrenIDs = new int[j1 + 1];
			for (int j2 = 0; j2 <= j1; j2++) {
				childrenIDs[j2] = buffer.readUnsignedWord();
				if (childrenIDs[j2] == 65535)
					childrenIDs[j2] = -1;
			}

		} while (true);
		if (aBoolean766) {
			aBoolean767 = false;
			aBoolean757 = false;
		}
		if (anInt760 == -1) {
			anInt760 = aBoolean767 ? 1 : 0;
	    }
		/*int i = -1;
		label0: do {
			int j;
			do {
				j = stream.readUnsignedByte();
				if (j == 0)
					break label0;
				if (j == 1) {
					int k = stream.readUnsignedByte();
					if (k > 0)
						if (anIntArray773 == null || lowMem) {
							anIntArray776 = new int[k];
							anIntArray773 = new int[k];
							for (int k1 = 0; k1 < k; k1++) {
								anIntArray773[k1] = stream.readUnsignedWord();
								anIntArray776[k1] = stream.readUnsignedByte();
							}
						} else {
							stream.currentOffset += k * 3;
						}
				} else if (j == 2)
					name = stream.readString();
				else if (j == 3)
					description = stream.readBytes();
				else if (j == 5) {
					int l = stream.readUnsignedByte();
					if (l > 0)
						if (anIntArray773 == null || lowMem) {
							anIntArray776 = null;
							anIntArray773 = new int[l];
							for (int l1 = 0; l1 < l; l1++)
								anIntArray773[l1] = stream.readUnsignedWord();
						} else {
							stream.currentOffset += l * 2;
						}
				} else if (j == 14)
					xLength = stream.readUnsignedByte();
				else if (j == 15)
					yLength = stream.readUnsignedByte();
				else if (j == 17)
					aBoolean767 = false;
				else if (j == 18)
					unshootable = false;
				else if (j == 19) {
					i = stream.readUnsignedByte();
					if (i == 1)
						hasActions = true;
				} else if (j == 21)
					aBoolean762 = true;
				else if (j == 22)
					aBoolean769 = false;
				else if (j == 23)
					aBoolean764 = true;
				else if (j == 24) {
					anInt781 = stream.readUnsignedWord();
					if (anInt781 == 65535)
						anInt781 = -1;
				} else if (j == 28)
					anInt775 = stream.readUnsignedByte();
				else if (j == 29)
					aByte737 = stream.readSignedByte();
				else if (j == 39)
					aByte742 = stream.readSignedByte();
				else if (j >= 30 && j < 39) {
					if (actions == null)
						actions = new String[10];
					actions[j - 30] = stream.readString();
					if (actions[j - 30].equalsIgnoreCase("hidden"))
						actions[j - 30] = null;
				} else if (j == 40) {
					int i1 = stream.readUnsignedByte();
					modifiedModelColors = new int[i1];
					originalModelColors = new int[i1];
					for (int i2 = 0; i2 < i1; i2++) {
						modifiedModelColors[i2] = stream.readUnsignedWord();
						originalModelColors[i2] = stream.readUnsignedWord();
					}
				} else if (j == 60)
					anInt746 = stream.readUnsignedWord();
				else if (j == 62)
					aBoolean751 = true;
				else if (j == 64)
					unwalkable = false;
				else if (j == 65)
					anInt748 = stream.readUnsignedWord();
				else if (j == 66)
					anInt772 = stream.readUnsignedWord();
				else if (j == 67)
					anInt740 = stream.readUnsignedWord();
				else if (j == 68)
					anInt758 = stream.readUnsignedWord();
				else if (j == 69)
					anInt768 = stream.readUnsignedByte();
				else if (j == 70)
					anInt738 = stream.readSignedWord();
				else if (j == 71)
					anInt745 = stream.readSignedWord();
				else if (j == 72)
					anInt783 = stream.readSignedWord();
				else if (j == 73)
					aBoolean736 = true;
				else if (j == 74) {
					aBoolean766 = true;
				} else {
					if (j != 75) {
						continue;
					}
					anInt760 = stream.readUnsignedByte();
				}
				continue label0;
			} while (j != 77);
			anInt774 = stream.readUnsignedWord();
			if (anInt774 == 65535)
				anInt774 = -1;
			anInt749 = stream.readUnsignedWord();
			if (anInt749 == 65535)
				anInt749 = -1;
			int j1 = stream.readUnsignedByte();
			childrenIDs = new int[j1 + 1];
			for (int j2 = 0; j2 <= j1; j2++) {
				childrenIDs[j2] = stream.readUnsignedWord();
				if (childrenIDs[j2] == 65535)
					childrenIDs[j2] = -1;
			}

		} while (true);// 6774
		if (i == -1) {
			hasActions = anIntArray773 != null && (anIntArray776 == null || anIntArray776[0] == 10);
			if (actions != null)
				hasActions = true;
		}
		if (aBoolean766) {
			aBoolean767 = false;
			unshootable = false;
		}
		if (anInt760 == -1)
			anInt760 = aBoolean767 ? 1 : 0;*/
	}

	private ObjectDef() {
		type = -1;
	}

	public boolean hasActions() {
		return hasActions || actions != null;
	}

	public boolean hasName() {
		return name != null && name.length() > 1;
	}

	public int xLength() {
		return objectSizeX;
	}

	public int yLength() {
		return objectSizeY;
	}
	
	/*public boolean clipped() {
		return unwalkable;
	}*/

	public boolean aBoolean736;
	private byte aByte737;
	private int anInt738;
	public String name;
	private int anInt740;
	private byte aByte742;
	public int objectSizeX;
	private int anInt745;
	public int anInt746;
	int[] originalModelColors;
	private int anInt748;
	public int anInt749;
	private boolean aBoolean751;
	public static boolean lowMem;
	public int type;
	public boolean aBoolean757;
	public int anInt758;
	public int childrenIDs[];
	private int anInt760;
	public int objectSizeY;
	public boolean aBoolean762;
	public boolean aBoolean764;
	private boolean aBoolean766;
	public boolean aBoolean767;
	public int anInt768;
	private boolean aBoolean769;
	private static int cacheIndex;
	private int anInt772;
	int[] anIntArray773;
	public int anInt774;
	public int anInt775;
	int[] anIntArray776;
	public byte description[];
	public boolean hasActions;
	public boolean aBoolean779;
	public int anInt781;
	private static ObjectDef[] cache;
	private int anInt783;
	int[] modifiedModelColors;
	public String actions[];
}
