
public final class SpotAnim {

	public static void unpackConfig(CacheArchive streamLoader) {
		Stream stream = new Stream(streamLoader.getDataForName("spotanim.dat"));
		int length = stream.readUnsignedWord();
		System.out.println("Loaded: " + length + " spot animation definitions.");
		if (cache == null)
			cache = new SpotAnim[length];
		for (int j = 0; j < length; j++) {
			if (cache[j] == null)
				cache[j] = new SpotAnim();
			cache[j].id = j;
			cache[j].readValues(stream);
		}
		custom();
	}

	private static void custom() {
		cache[2274].modelId = cache[2281].modelId;
		cache[2274].animationId = cache[2281].animationId;
		cache[2274].rotation = 90;
		cache[2274].animation = cache[2281].animation;

	}

	private void readValues(Stream stream) {
		do {
			int i = stream.readUnsignedByte();
			if (i == 0)
				return;
			if (i == 1)
				modelId = stream.readUnsignedWord();
			else if (i == 2) {
				animationId = stream.readUnsignedWord();
				if (Animation.anims != null)
					animation = Animation.anims[animationId];
			} else if (i == 4)
				sizeXY = stream.readUnsignedWord();
			else if (i == 5)
				sizeZ = stream.readUnsignedWord();
			else if (i == 6)
				rotation = stream.readUnsignedWord();
			else if (i == 7)
				shadow = stream.readUnsignedByte();
			else if (i == 8)
				lightness = stream.readUnsignedByte();
			else if (i == 40) {
				int j = stream.readUnsignedByte();
				for (int k = 0; k < j; k++) {
					originalColours[k] = stream.readUnsignedWord();
					destColours[k] = stream.readUnsignedWord();
				}
			} else
				System.out.println("Error unrecognised spotanim config code: "
						+ i);
		} while (true);
	}

	public Model getModel() {
			Model model = (Model) modelCache.get(id);
			if (model != null)
				return model;
			model = Model.fetchModel(modelId);
			if (model == null)
				return null;
			for (int i = 0; i < 6; i++)
				if (originalColours[0] != 0)
					model.recolour(originalColours[i], destColours[i]);
			modelCache.put(model, id);
			return model;
		
	}

	private SpotAnim() {
		anInt400 = 9;
		animationId = -1;
		originalColours = new int[6];
		destColours = new int[6];
		sizeXY = 128;
		sizeZ = 128;
	}

	public int anInt400;
	public static SpotAnim cache[];
	private int id;
	private int modelId;
	private int animationId;
	public Animation animation;
	private final int[] originalColours;
	private final int[] destColours;
	public int sizeXY;
	public int sizeZ;
	public int rotation;
	public int shadow;
	public int lightness;
	public static MemCache modelCache = new MemCache(30);

}