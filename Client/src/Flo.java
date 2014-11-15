import java.util.Date;


@SuppressWarnings("all")
public final class Flo {

	public static void unpackConfig(CacheArchive streamLoader) {
		Stream stream = new Stream(streamLoader.getDataForName("flo.dat"));
		int cacheSize = stream.readUnsignedWord();
		System.out.println("Loaded: " + cacheSize + " floor definitions.");
		if (cache == null)
			cache = new Flo[cacheSize];
		for (int j = 0; j < cacheSize; j++) {
			if (cache[j] == null)
				cache[j] = new Flo();
			cache[j].readValues(stream);
		}

	}
	
	private void readValues(Stream stream) {
		do {
			int opcode = stream.readUnsignedByte();
			boolean dummy;
			if (opcode == 0)
				return;
			else if (opcode == 1) {
				colour2 = stream.read3Bytes();
				Date date = new Date();
			/*	
			 * December Christmas snow timed
			 * Works with HD too
			 */
				if (date.getMonth() == 11 || date.getMonth() == 0
						&& date.getDate() <= 27) {
					colour2 = 0xffffff;
				}
				rgb2hsl(colour2);
			} else if (opcode == 2)
				texture = stream.readUnsignedByte();
			Date date = new Date();
			if (date.getMonth() == 11 || date.getMonth() == 0
					&& date.getDate() <= 27) {
				texture = 0xffffff;
			} else if (opcode == 3)
				dummy = true;
			else if (opcode == 5)
				occlude = false;
			else if (opcode == 6)
				name = stream.readString();
			else if (opcode == 7) {
				int j = hue;
				int k = saturation;
				int l = lightness;
				int i1 = hue2;
				int j1 = stream.read3Bytes();
				rgb2hsl(j1);
				hue = j;
				saturation = k;
				lightness = l;
				hue2 = i1;
				hue_weight = i1;
			} else {
				System.out.println("Error unrecognised config code: " + opcode);
			}
		} while (true);
	}

	private void rgb2hsl(int i) {
		double d = (double) (i >> 16 & 0xff) / 256D;
		double d1 = (double) (i >> 8 & 0xff) / 256D;
		double d2 = (double) (i & 0xff) / 256D;
		double d3 = d;
		if (d1 < d3)
			d3 = d1;
		if (d2 < d3)
			d3 = d2;
		double d4 = d;
		if (d1 > d4)
			d4 = d1;
		if (d2 > d4)
			d4 = d2;
		double d5 = 0.0D;
		double d6 = 0.0D;
		double d7 = (d3 + d4) / 2D;
		if (d3 != d4) {
			if (d7 < 0.5D)
				d6 = (d4 - d3) / (d4 + d3);
			if (d7 >= 0.5D)
				d6 = (d4 - d3) / (2D - d4 - d3);
			if (d == d4)
				d5 = (d1 - d2) / (d4 - d3);
			else if (d1 == d4)
				d5 = 2D + (d2 - d) / (d4 - d3);
			else if (d2 == d4)
				d5 = 4D + (d - d1) / (d4 - d3);
		}
		d5 /= 6D;
		hue = (int) (d5 * 256D);
		saturation = (int) (d6 * 256D);
		lightness = (int) (d7 * 256D);
		if (saturation < 0)
			saturation = 0;
		else if (saturation > 255)
			saturation = 255;
		if (lightness < 0)
			lightness = 0;
		else if (lightness > 255)
			lightness = 255;
		if (d7 > 0.5D)
			hue_weight = (int) ((1.0D - d7) * d6 * 512D);
		else
			hue_weight = (int) (d7 * d6 * 512D);
		if (hue_weight < 1)
			hue_weight = 1;
		hue2 = (int) (d5 * (double) hue_weight);
		int k = (hue + (int) (Math.random() * 16D)) - 8;
		if (k < 0)
			k = 0;
		else if (k > 255)
			k = 255;
		int l = (saturation + (int) (Math.random() * 48D)) - 24;
		if (l < 0)
			l = 0;
		else if (l > 255)
			l = 255;
		int i1 = (lightness + (int) (Math.random() * 48D)) - 24;
		if (i1 < 0)
			i1 = 0;
		else if (i1 > 255)
			i1 = 255;
		hslColour = packHSL(k, l, i1);
	}

	private int packHSL(int i, int j, int k) {
		if (k > 179)
			j /= 2;
		if (k > 192)
			j /= 2;
		if (k > 217)
			j /= 2;
		if (k > 243)
			j /= 2;
		return (i / 4 << 10) + (j / 32 << 7) + k / 2;
	}

	private Flo() {
		texture = -1;
		occlude = true;
	}

	public static Flo cache[];
	public static int colour2;
	public int texture;
	public boolean occlude;
	public int hue;
	public int saturation;
	public int lightness;
	public int hue2;
	public int hue_weight;
	public int hslColour;
	public String name;
}
