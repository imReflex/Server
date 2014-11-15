
import java.nio.ByteBuffer;
 
/**
 *
 *
 * @author Flamable
 */
public class OverLayFlo317 {
 
    public boolean aBoolean393;//To add
    public static OverLayFlo317[] cache;
 
    public int groundTextureOverlay = -1;
    public int groundColorOverlay;
    public boolean occludeOverlay;
    public int detailedColor;
    public int detailedTexture9;
    public boolean boolean_10;
    public int detailedTexture11;
    public boolean boolean_12;
    public int detailedColor13;
    public int detailedTexture14;
    public int int_15;
    public int alpha;
 
    /**
     * Hsl
     */
    public int groundHueOverlay;
    public int groundSaturationOverlay;
    public int groundLightnessOverlay;
    public int weighted_hue;
    public int chroma_overlay;
    public int hslOverlayColor;
	public int anInt390;
 
	
    public static void unpackConfig(CacheArchive streamLoader) {
    	ByteBuffer buffer = ByteBuffer.wrap(streamLoader.getDataForName("flo2.dat"));
        int count = buffer.getShort();
        System.out.println("Loaded: " + count + " floor[2] definitions.");
        cache = new OverLayFlo317[count];
        for (int i = 0; i < count ; i++) {
            if (cache[i] == null)
                cache[i] = new OverLayFlo317();
            cache[i].parse(buffer);
            if(i == 113)
            	cache[i].groundTextureOverlay = 25;
        }
    }
 
    private void parse(ByteBuffer byteBuffer) {
        for (;;) {
            int attributeId = byteBuffer.get();
            if (attributeId == 0)
                break;
            else if (attributeId == 1) {
                groundColorOverlay = ((byteBuffer.get() & 0xff) << 16) + ((byteBuffer.get() & 0xff) << 8) + (byteBuffer.get() & 0xff);
                if(Client.getOption("hd_tex")) {
                	groundColorOverlay = setColor(groundColorOverlay);
                } else
                	rgbhsl(groundColorOverlay);
            } else if (attributeId == 2) {
                groundTextureOverlay = byteBuffer.get() & 0xff;
            } else if (attributeId == 3) {
                groundTextureOverlay = byteBuffer.getShort() & 0xffff;
                if (groundTextureOverlay == 65535) {
                    groundTextureOverlay = -1;
                }
            } else if (attributeId == 4) {
 
            } else if (attributeId == 5) {
                occludeOverlay = false;
            } else if (attributeId == 6) {
 
            } else if (attributeId == 7) {
                detailedColor = ((byteBuffer.get() & 0xff) << 16) + ((byteBuffer.get() & 0xff) << 8) + (byteBuffer.get() & 0xff);
                if(Client.getOption("hd_tex"))
                	detailedColor = setColor(detailedColor);
                else
                	rgbhsl(detailedColor);
            } else if (attributeId == 8) {
 
            } else if (attributeId == 9) {
                detailedTexture9 = byteBuffer.getShort() & 0xffff;
            } else if (attributeId == 10) {
                boolean_10 = false;
            } else if (attributeId == 11) {
                detailedTexture11 = byteBuffer.get() & 0xff;
            } else if (attributeId == 12) {
                boolean_12 = true;
            } else if (attributeId == 13) {
                detailedColor13 = ((byteBuffer.get() & 0xff) << 16) + ((byteBuffer.get() & 0xff) << 8) + (byteBuffer.get() & 0xff);

            } else if (attributeId == 14) {
                detailedTexture14 = byteBuffer.get() & 0xff;
            } else if (attributeId == 15) {
                int_15 = byteBuffer.getShort() & 0xffff;
                if (int_15 == 65535) {
                    int_15 = -1;
                }
            } else if (attributeId == 16) {
                alpha = byteBuffer.get() & 0xff;
            } else {
                System.err.println("[OverlayFloor] Missing AttributeId: "+attributeId);
            }
        }
    }
 
	private static int setColor(int color) {
		return color != 0xff00ff ? rgb2hsl(color) : -1;
	}
	
	private static int rgb2hsl(int color) {
		double r = (double) ((color >>> 16) & 0xff) / 256.0D;
		double g = (double) ((color >>> 8) & 0xff) / 256.0D;
		double b = (double) (color & 0xff) / 256.0D;
		double min = r;
		if (min > g)
			min = g;

		if (min > b)
			min = b;

		double max = r;
		if (g > max)
			max = g;

		if (b > max)
			max = b;

		double hue = 0.0;
		double saturation = 0.0;
		double luminance = (min + max) / 2.0D;
		if (max != min)
		{
			if (luminance < 0.5D)
				saturation = (max - min) / (min + max);

			if (max != r)
			{
				if (max == g)
					hue = (b - r) / (max - min) + 2.0D;
				else if (max == b)
					hue = (r - g) / (max - min) + 4.0D;

			}
			else
				hue = (g - b) / (max - min);

			if (luminance >= 0.5D)
				saturation = (max - min) / (2.0D - min - max);

		}
		hue /= 6.0D;
		int hueOverlay = (int) (hue * 256.0D);
		int satOverlay = (int) (saturation * 256.0D);
		int lumOverlay = (int) (luminance * 256.0D);
		if (satOverlay < 0)
			satOverlay = 0;
		else if (satOverlay > 0xff)
			satOverlay = 0xff;

		if (lumOverlay < 0)
			lumOverlay = 0;
		else if (lumOverlay > 0xff)
			lumOverlay = 0xff;

		if (lumOverlay > 242)
			satOverlay >>= 4;
		else if (lumOverlay > 217)
			satOverlay >>= 3;
		else if (lumOverlay > 192)
			satOverlay >>= 2;
		else if (lumOverlay > 179)
			satOverlay >>= 1;

		return (lumOverlay >> 1) + ((satOverlay >> 5 << 7) + ((hueOverlay & 0xff) >> 2 << 10));
	}
	
    private void rgbhsl(int color) {
        double red = (double) (color >> 16 & 0xff) / 255.0;
        double green = (double) (color >> 8 & 0xff) / 255.0;
        double blue = (double) (color & 0xff) / 255.0;
        double min = red;
        if (green < min)
            min = green;
        if (blue < min)
            min = blue;
        double max = red;
        if (green > max)
            max = green;
        if (blue > max)
            max = blue;
        double hue = 0.0;
        double saturation = 0.0;
        double luminance = (min + max) / 2.0;
        if (min != max) {
            if (luminance < 0.5)
                saturation = (max - min) / (max + min);
            if (luminance >= 0.5)
                saturation = (max - min) / (2.0 - max - min);
            if (red == max)
                hue = (green - blue) / (max - min);
            else if (green == max)
                hue = 2.0 + (blue - red) / (max - min);
            else if (blue == max)
                hue = 4.0 + (red - green) / (max - min);
        }
        hue /= 6.0;
        groundHueOverlay = (int) (hue * 255.0);
        groundSaturationOverlay = (int) (saturation * 255.0);
        groundLightnessOverlay = (int) (luminance * 255.0);
		int hue_overlay = groundHueOverlay;
		int sat_overlay = groundSaturationOverlay;
		int lum_overlay = groundLightnessOverlay;
        if (sat_overlay < 0)
            sat_overlay = 0;
        else if (sat_overlay > 255)
            sat_overlay = 255;
        if (lum_overlay < 0)
            lum_overlay = 0;
        else if (lum_overlay > 255)
            lum_overlay = 255;
        if (luminance > 0.5)
            chroma_overlay = (int) ((1.0 - luminance) * saturation * 512.0);
        else
            chroma_overlay = (int) (luminance * saturation * 512.0);
        if (chroma_overlay < 1)
            chroma_overlay = 1;
        weighted_hue = (int) (hue * (double) chroma_overlay);
		int hue_offset = hue_overlay;
		int sat_offset = sat_overlay;
		int lum_offset = lum_overlay;
		hslOverlayColor = encode(hue_offset, sat_offset, lum_offset);
    }
 
    private final int encode(int arg0, int arg1, int arg2) {
        if (arg2 > 179)
            arg1 /= 2;
        if (arg2 > 192)
            arg1 /= 2;
        if (arg2 > 217)
            arg1 /= 2;
        if (arg2 > 243)
            arg1 /= 2;
        int i = (arg0 / 4 << 10) + (arg1 / 32 << 7) + arg2 / 2;
        return i;
    }
 
}