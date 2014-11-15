
public class TextureAnimating {

	private static final int[] Animated_Textures = { 17, 24, 34, 40, 1};
	
	private static byte[] pixels = new byte[16384];
	private static int[] hdPixels = new int[16384];
	
	/**
	 * Animates on screen textures with a certain id.
	 */
	public static void animateTexture() {
		try {
		if (!Rasterizer.lowMem) {
			for(int tex : Animated_Textures ) {
				if(Client.getOption("hd_tex")) {
					Texture t = Texture.get(tex);
					if(t instanceof RGBTexture) {
						RGBTexture rgbT = (RGBTexture)t;
						int indexes = rgbT.width * rgbT.height - 1;
						int noise = rgbT.width * Client.instance.cycleTimer * 2;
						int current[] = rgbT.pixels;
						int next[] = hdPixels;
						for (int i2 = 0; i2 <= indexes; i2++)
							next[i2] = current[i2 - noise & indexes];
	
						rgbT.setPixels(next);
						hdPixels = current;
						TextureLoader667.resetTexture(tex);
					} else
					if(t instanceof ARGBTexture) {
						ARGBTexture rgbT = (ARGBTexture)t;
						int indexes = rgbT.width * rgbT.height - 1;
						int noise = rgbT.width * Client.instance.cycleTimer * 2;
						int current[] = rgbT.pixels;
						int next[] = hdPixels;
						for (int i2 = 0; i2 <= indexes; i2++)
							next[i2] = current[i2 - noise & indexes];
	
						rgbT.setPixels(next);
						hdPixels = current;
						TextureLoader667.resetTexture(tex);
					} else
					if(t instanceof PalettedTexture || t instanceof AlphaPalettedTexture) {
						PalettedTexture rgbT = (PalettedTexture)t;
						int indexes = rgbT.width * rgbT.height - 1;
						int noise = rgbT.width * Client.instance.cycleTimer * 2;
						byte current[] = rgbT.getIndices();
						byte next[] = pixels;
						for (int i2 = 0; i2 <= indexes; i2++)
							next[i2] = current[i2 - noise & indexes];
	
						rgbT.setIndices(next);
						pixels = current;
						TextureLoader667.resetTexture(tex);
					} 
				} else {
					Background background = TextureLoader317.textureImages[tex];
					int indexes = background.imgWidth * background.imgHeight - 1;
					int noise = background.imgWidth * Client.instance.cycleTimer * 2;
					byte current[] = background.imgPixels;
					byte next[] = pixels;
					for (int i2 = 0; i2 <= indexes; i2++)
						next[i2] = current[i2 - noise & indexes];

					background.imgPixels = next;
					pixels = current;
					TextureLoader317.resetTexture(tex);
				}
			}
		}
		} catch(Exception e) { }
	}
}
