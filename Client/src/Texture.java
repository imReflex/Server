


public class Texture
{
	public Texture(int width, int height)
	{
		this.width = width;
		this.height = height;
		opaque = true;
	}

	public int getPixel(int i)
	{
		return 0xffffffff;
	}

	public static Texture get(int id)
	{
		if (id < 0 || id >= textures.length)
			return null;

		if (loaded[id])
			return textures[id];
		updateManager.requestFileData(Client.TEXTURE_IDX-1, id);
		return null;
	}

	public static void init(int count, OnDemandFetcher updateManager_)
	{
		textures = new Texture[count];
		loaded = new boolean[count];
		updateManager = updateManager_;
		 
		/** Preload certain textures **/
		/*for(int i = 0; i <= count; i++) {
			get(i);
		}*/
	}

	public static void load(int id, byte[] data)
	{
		System.out.println("Loading texture ID: " + id);
		loaded[id] = true;
		if (data != null && data.length >= 5)
		{
			ByteBuffer buffer = new ByteBuffer(data);
			int type = buffer.readUnsignedByte();
			int width = buffer.readUnsignedWord();
			int height = buffer.readUnsignedWord();
			System.out.println("Tex ID: " + id + " Type: " + type + " Width: " + width + " Height: " + height);
			if (type == 0)
				textures[id] = new PalettedTexture(width, height, buffer, false);
			else if (type == 1)
				textures[id] = new RGBTexture(width, height, buffer);
			else if (type == 2)
				textures[id] = new AlphaPalettedTexture(width, height, buffer);
			else if (type == 3)
				textures[id] = new ARGBTexture(width, height, buffer);

		} else {
			System.out.println("Data is null for texture: " + id);
		}
	}

	public String toString()
	{
		return width + " X " + height + "	" + (opaque ? "+opaque":"-opaque") + "	" + (hasAlpha ? "+alpha":"-alpha");
	}

	public static void nullLoader()
	{
		loaded = null;
		textures = null;
		updateManager = null;
	}

	public static int getTotal()
	{
		return textures.length;
	}

	public boolean opaque;
	public boolean hasAlpha;
	public final int width;
	public final int height;
	private static boolean[] loaded;
	private static Texture[] textures;
	static OnDemandFetcher updateManager;
	public static Texture NULL_TEXTURE = new Texture(64, 64);
}
