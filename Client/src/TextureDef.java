



public final class TextureDef
{
	private TextureDef()
	{
	}

	public static void unpackConfig(CacheArchive streamLoader)
	{
		byte[] data = streamLoader.getDataForName("textures.dat");
		ByteBuffer buffer = new ByteBuffer(data);
		int count = buffer.readUnsignedWord();
		System.out.println("Loaded: " + count + " texture definitions.");
		textures = new TextureDef[count];
		for (int i = 0; i != count; ++i)
			if (buffer.readUnsignedByte() == 1)
				textures[i] = new TextureDef();


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aBoolean1223 = buffer.readUnsignedByte() == 1;


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aBoolean1204 = buffer.readUnsignedByte() == 1;


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aBoolean1205 = buffer.readUnsignedByte() == 1;


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aByte1217 = buffer.readSignedByte();


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aByte1225 = buffer.readSignedByte();


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aByte1214 = buffer.readSignedByte();


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aByte1213 = buffer.readSignedByte();


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aShort1221 = (short) buffer.readUnsignedWord();


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aByte1211 = buffer.readSignedByte();


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aByte1203 = buffer.readSignedByte();


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aBoolean1222 = buffer.readUnsignedByte() == 1;


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aBoolean1216 = buffer.readUnsignedByte() == 1;


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aByte1207 = buffer.readSignedByte();


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aBoolean1212 = buffer.readUnsignedByte() == 1;


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aBoolean1210 = buffer.readUnsignedByte() == 1;


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].aBoolean1215 = buffer.readUnsignedByte() == 1;


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].anInt1202 = buffer.readUnsignedByte();


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].anInt1206 = buffer.readDWord();


		for (int i = 0; i != count; ++i)
			if (textures[i] != null)
				textures[i].anInt1226 = buffer.readUnsignedByte();


	}

	public static void nullLoader()
	{
		textures = null;
	}

	boolean aBoolean1223;
	 boolean aBoolean1204;
	 boolean aBoolean1205;
	 byte aByte1217;
	 byte aByte1225;
	 byte aByte1214;
	 byte aByte1213;
	 short aShort1221;
	 byte aByte1211;
	 byte aByte1203;
	 boolean aBoolean1222;
	 boolean aBoolean1216;
	 byte aByte1207;
	 boolean aBoolean1212;
	 boolean aBoolean1210;
	 boolean aBoolean1215;
	 int anInt1202;
	 int anInt1206;
	int anInt1226;
	public static TextureDef[] textures;
}
