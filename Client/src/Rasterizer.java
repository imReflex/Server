


public final class Rasterizer extends DrawingArea {

	public static void clearCache() {
		shadowDecay = null;
		shadowDecay = null;
		SINE = null;
		COSINE = null;
		lineOffsets = null;
		hsl2rgb = null;
		TextureLoader317.clear();
		TextureLoader667.clear();
	}

	public static int blendrgba(int[] rgb, int x, int y, int width, int height, int arrayWidth)
	{
		x = y = y * arrayWidth + x - 1;
		arrayWidth -= width;
		x += height * arrayWidth;
		int alpha = 0;
		int red = 0;
		int green = 0;
		int blue = 0;
		for (; --height != -1; y += arrayWidth)
			for (blue = width; blue != 0; --blue)
			{
				int t9 = rgb[++y];
				alpha += (t9 >> 24) & 255;
				red += (t9 >> 16) & 255;
				green += (t9 >> 8) & 255;
				blue += t9 & 255;
			}


		y -= x;
		return ((alpha / y) << 24) | ((red / y) << 16) + ((green / y) << 8) + (blue / y);
	}

	public static int blendrgb(int[] n, int t, int t1, int t2, int t3, int t4)
	{
		t = t1 = t1 * t4 + t - 1;
		t4 -= t2;
		t += t3 * t4;
		int t5 = 0;
		int t6 = 0;
		int t7 = 0;
		for (; --t3 != -1; t1 += t4)
			for (int t8 = t2; t8 != 0; --t8)
			{
				int t9 = n[++t1];
				t5 += (t9 >> 16) & 255;
				t6 += (t9 >> 8) & 255;
				t7 += t9 & 255;
			}


		t1 -= t;
		return ((t5 / t1) << 16) + ((t6 / t1) << 8) + (t7 / t1);
	}
	
	public static int dot(int x1, int x2, int y1, int y2) {
    	return x1 * x2 + y1 * y1;
    }
    public static int interpolate(int x1, int y1, int x2, int y2, int x3, int y3, int v1, int v2, int v3, int px, int py) {
    	int v0x = x2 - x1;
    	int v0y = y2 - y1;
    	int v1x = x3 - x1;
    	int v1y = y3 - y1;
    	int v2x = px - x1;
    	int v2y = py - y1;
    	float d00 = dot(v0x, v0x, v0y, v0y);
    	float d01 = dot(v0x, v1x, v0y, v1y);
    	float d11 = dot(v1x, v1x, v1y, v1y);
    	float d20 = dot(v2x, v0x, v2y, v0y);
    	float d21 = dot(v2x, v1x, v2y, v1y);
    	float denom = d00 * d11 - d01 * d01;
    	float v = (d11 * d20 - d01 * d21) / denom;
        float w = (d00 * d21 - d01 * d20) / denom;
        float u = 1.0f - v - w;
    	return (int)(u*v1 + v*v2 + w*v3);
    }
	
	public static void calculatePalette(double brightness, boolean pri) {
		brightness += Math.random() * 0.029999999999999999D - 0.014999999999999999D;
		int j = 0;
		for (int k = 0; k < 512; k++) {
			double d1 = (double) (k / 8) / 64D + 0.0078125D;
			double d2 = (double) (k & 7) / 8D + 0.0625D;
			for (int k1 = 0; k1 < 128; k1++) {
				double d3 = (double) k1 / 128D;
				double d4 = d3;
				double d5 = d3;
				double d6 = d3;
				if (d2 != 0.0D) {
					double d7;
					if (d3 < 0.5D)
						d7 = d3 * (1.0D + d2);
					else
						d7 = (d3 + d2) - d3 * d2;
					double d8 = 2D * d3 - d7;
					double d9 = d1 + 0.33333333333333331D;
					if (d9 > 1.0D)
						d9--;
					double d10 = d1;
					double d11 = d1 - 0.33333333333333331D;
					if (d11 < 0.0D)
						d11++;
					if (6D * d9 < 1.0D)
						d4 = d8 + (d7 - d8) * 6D * d9;
					else if (2D * d9 < 1.0D)
						d4 = d7;
					else if (3D * d9 < 2D)
						d4 = d8 + (d7 - d8) * (0.66666666666666663D - d9) * 6D;
					else
						d4 = d8;
					if (6D * d10 < 1.0D)
						d5 = d8 + (d7 - d8) * 6D * d10;
					else if (2D * d10 < 1.0D)
						d5 = d7;
					else if (3D * d10 < 2D)
						d5 = d8 + (d7 - d8) * (0.66666666666666663D - d10) * 6D;
					else
						d5 = d8;
					if (6D * d11 < 1.0D)
						d6 = d8 + (d7 - d8) * 6D * d11;
					else if (2D * d11 < 1.0D)
						d6 = d7;
					else if (3D * d11 < 2D)
						d6 = d8 + (d7 - d8) * (0.66666666666666663D - d11) * 6D;
					else
						d6 = d8;
				}
				int l1 = (int) (d4 * 256D);
				int i2 = (int) (d5 * 256D);
				int j2 = (int) (d6 * 256D);
				int k2 = (l1 << 16) + (i2 << 8) + j2;
				k2 = adjustBrightness(k2, brightness);
				if (k2 == 0)
					k2 = 1;
				hsl2rgb[j++] = k2;
			}

		}
		if(pri) {
			TextureLoader317.calculateTexturePalette(brightness);
			TextureLoader667.calculateTexturePalette(brightness);
		}
	}
	
	public static int adjustBrightness(int i, double d) {
		double d1 = (double) (i >> 16) / 256D;
		double d2 = (double) (i >> 8 & 0xff) / 256D;
		double d3 = (double) (i & 0xff) / 256D;
		d1 = Math.pow(d1, d);
		d2 = Math.pow(d2, d);
		d3 = Math.pow(d3, d);
		int j = (int) (d1 * 256D);
		int k = (int) (d2 * 256D);
		int l = (int) (d3 * 256D);
		return (j << 16) + (k << 8) + l;
	}
	
	public static void setDefaultBounds() {
		lineOffsets = new int[DrawingArea.height];
		for (int j = 0; j < DrawingArea.height; j++)
			lineOffsets[j] = DrawingArea.width * j;

		center_x = DrawingArea.width / 2;
		center_y = DrawingArea.height / 2;
	}

	public static void setBounds(int j, int k) {
		lineOffsets = new int[k];
		for (int l = 0; l < k; l++)
			lineOffsets[l] = j * l;

		center_x = j / 2;
		center_y = k / 2;
	}

	/**
	 * Untextured triangle rendering.
	 */
	public static void drawShadedTriangle(int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2) {
		if(Client.getOption("smooth_shade") && notTextured) {
			//System.out.println("HD shading");
			drawHDShadedTriangle(i, j, k, l, i1, j1, k1, l1, i2);
		} else {
			//System.out.println("LD shading");
			drawLDShadedTriangle(i, j, k, l, i1, j1, k1, l1, i2);
		}
	}

	/**
	 * Untextured triangle WITHOUT smooth shading.
	 */
	public static void drawLDShadedTriangle(int i, int j, int k, int l, int i1, int j1, int k1, int l1, 
			int i2)
	{
		int j2 = 0;
		int k2 = 0;
		if(j != i)
		{
			j2 = (i1 - l << 16) / (j - i);
			k2 = (l1 - k1 << 15) / (j - i);
		}
		int l2 = 0;
		int i3 = 0;
		if(k != j)
		{
			l2 = (j1 - i1 << 16) / (k - j);
			i3 = (i2 - l1 << 15) / (k - j);
		}
		int j3 = 0;
		int k3 = 0;
		if(k != i)
		{
			j3 = (l - j1 << 16) / (i - k);
			k3 = (k1 - i2 << 15) / (i - k);
		}
		if(i <= j && i <= k)
		{
			if(i >= DrawingArea.bottomY)
				return;
			if(j > DrawingArea.bottomY)
				j = DrawingArea.bottomY;
			if(k > DrawingArea.bottomY)
				k = DrawingArea.bottomY;
			if(j < k)
			{
				j1 = l <<= 16;
				i2 = k1 <<= 15;
				if(i < 0)
				{
					j1 -= j3 * i;
					l -= j2 * i;
					i2 -= k3 * i;
					k1 -= k2 * i;
					i = 0;
				}
				i1 <<= 16;
				l1 <<= 15;
				if(j < 0)
				{
					i1 -= l2 * j;
					l1 -= i3 * j;
					j = 0;
				}
				if(i != j && j3 < j2 || i == j && j3 > l2)
				{
					k -= j;
					j -= i;
					for(i = lineOffsets[i]; --j >= 0; i += DrawingArea.width)
					{
						drawLDShadedLine(DrawingArea.pixels, i, j1 >> 16, l >> 16, i2 >> 7, k1 >> 7);
						j1 += j3;
						l += j2;
						i2 += k3;
						k1 += k2;
					}

					while(--k >= 0) 
					{
						drawLDShadedLine(DrawingArea.pixels, i, j1 >> 16, i1 >> 16, i2 >> 7, l1 >> 7);
						j1 += j3;
						i1 += l2;
						i2 += k3;
						l1 += i3;
						i += DrawingArea.width;
					}
					return;
				}
				k -= j;
				j -= i;
				for(i = lineOffsets[i]; --j >= 0; i += DrawingArea.width)
				{
					drawLDShadedLine(DrawingArea.pixels, i, l >> 16, j1 >> 16, k1 >> 7, i2 >> 7);
					j1 += j3;
					l += j2;
					i2 += k3;
					k1 += k2;
				}

				while(--k >= 0) 
				{
					drawLDShadedLine(DrawingArea.pixels, i, i1 >> 16, j1 >> 16, l1 >> 7, i2 >> 7);
					j1 += j3;
					i1 += l2;
					i2 += k3;
					l1 += i3;
					i += DrawingArea.width;
				}
				return;
			}
			i1 = l <<= 16;
			l1 = k1 <<= 15;
			if(i < 0)
			{
				i1 -= j3 * i;
				l -= j2 * i;
				l1 -= k3 * i;
				k1 -= k2 * i;
				i = 0;
			}
			j1 <<= 16;
			i2 <<= 15;
			if(k < 0)
			{
				j1 -= l2 * k;
				i2 -= i3 * k;
				k = 0;
			}
			if(i != k && j3 < j2 || i == k && l2 > j2)
			{
				j -= k;
				k -= i;
				for(i = lineOffsets[i]; --k >= 0; i += DrawingArea.width)
				{
					drawLDShadedLine(DrawingArea.pixels, i, i1 >> 16, l >> 16, l1 >> 7, k1 >> 7);
					i1 += j3;
					l += j2;
					l1 += k3;
					k1 += k2;
				}

				while(--j >= 0) 
				{
					drawLDShadedLine(DrawingArea.pixels, i, j1 >> 16, l >> 16, i2 >> 7, k1 >> 7);
					j1 += l2;
					l += j2;
					i2 += i3;
					k1 += k2;
					i += DrawingArea.width;
				}
				return;
			}
			j -= k;
			k -= i;
			for(i = lineOffsets[i]; --k >= 0; i += DrawingArea.width)
			{
				drawLDShadedLine(DrawingArea.pixels, i, l >> 16, i1 >> 16, k1 >> 7, l1 >> 7);
				i1 += j3;
				l += j2;
				l1 += k3;
				k1 += k2;
			}

			while(--j >= 0) 
			{
				drawLDShadedLine(DrawingArea.pixels, i, l >> 16, j1 >> 16, k1 >> 7, i2 >> 7);
				j1 += l2;
				l += j2;
				i2 += i3;
				k1 += k2;
				i += DrawingArea.width;
			}
			return;
		}
		if(j <= k)
		{
			if(j >= DrawingArea.bottomY)
				return;
			if(k > DrawingArea.bottomY)
				k = DrawingArea.bottomY;
			if(i > DrawingArea.bottomY)
				i = DrawingArea.bottomY;
			if(k < i)
			{
				l = i1 <<= 16;
				k1 = l1 <<= 15;
				if(j < 0)
				{
					l -= j2 * j;
					i1 -= l2 * j;
					k1 -= k2 * j;
					l1 -= i3 * j;
					j = 0;
				}
				j1 <<= 16;
				i2 <<= 15;
				if(k < 0)
				{
					j1 -= j3 * k;
					i2 -= k3 * k;
					k = 0;
				}
				if(j != k && j2 < l2 || j == k && j2 > j3)
				{
					i -= k;
					k -= j;
					for(j = lineOffsets[j]; --k >= 0; j += DrawingArea.width)
					{
						drawLDShadedLine(DrawingArea.pixels, j, l >> 16, i1 >> 16, k1 >> 7, l1 >> 7);
						l += j2;
						i1 += l2;
						k1 += k2;
						l1 += i3;
					}

					while(--i >= 0) 
					{
						drawLDShadedLine(DrawingArea.pixels, j, l >> 16, j1 >> 16, k1 >> 7, i2 >> 7);
						l += j2;
						j1 += j3;
						k1 += k2;
						i2 += k3;
						j += DrawingArea.width;
					}
					return;
				}
				i -= k;
				k -= j;
				for(j = lineOffsets[j]; --k >= 0; j += DrawingArea.width)
				{
					drawLDShadedLine(DrawingArea.pixels, j, i1 >> 16, l >> 16, l1 >> 7, k1 >> 7);
					l += j2;
					i1 += l2;
					k1 += k2;
					l1 += i3;
				}

				while(--i >= 0) 
				{
					drawLDShadedLine(DrawingArea.pixels, j, j1 >> 16, l >> 16, i2 >> 7, k1 >> 7);
					l += j2;
					j1 += j3;
					k1 += k2;
					i2 += k3;
					j += DrawingArea.width;
				}
				return;
			}
			j1 = i1 <<= 16;
			i2 = l1 <<= 15;
			if(j < 0)
			{
				j1 -= j2 * j;
				i1 -= l2 * j;
				i2 -= k2 * j;
				l1 -= i3 * j;
				j = 0;
			}
			l <<= 16;
			k1 <<= 15;
			if(i < 0)
			{
				l -= j3 * i;
				k1 -= k3 * i;
				i = 0;
			}
			if(j2 < l2)
			{
				k -= i;
				i -= j;
				for(j = lineOffsets[j]; --i >= 0; j += DrawingArea.width)
				{
					drawLDShadedLine(DrawingArea.pixels, j, j1 >> 16, i1 >> 16, i2 >> 7, l1 >> 7);
					j1 += j2;
					i1 += l2;
					i2 += k2;
					l1 += i3;
				}

				while(--k >= 0) 
				{
					drawLDShadedLine(DrawingArea.pixels, j, l >> 16, i1 >> 16, k1 >> 7, l1 >> 7);
					l += j3;
					i1 += l2;
					k1 += k3;
					l1 += i3;
					j += DrawingArea.width;
				}
				return;
			}
			k -= i;
			i -= j;
			for(j = lineOffsets[j]; --i >= 0; j += DrawingArea.width)
			{
				drawLDShadedLine(DrawingArea.pixels, j, i1 >> 16, j1 >> 16, l1 >> 7, i2 >> 7);
				j1 += j2;
				i1 += l2;
				i2 += k2;
				l1 += i3;
			}

			while(--k >= 0) 
			{
				drawLDShadedLine(DrawingArea.pixels, j, i1 >> 16, l >> 16, l1 >> 7, k1 >> 7);
				l += j3;
				i1 += l2;
				k1 += k3;
				l1 += i3;
				j += DrawingArea.width;
			}
			return;
		}
		if(k >= DrawingArea.bottomY)
			return;
		if(i > DrawingArea.bottomY)
			i = DrawingArea.bottomY;
		if(j > DrawingArea.bottomY)
			j = DrawingArea.bottomY;
		if(i < j)
		{
			i1 = j1 <<= 16;
			l1 = i2 <<= 15;
			if(k < 0)
			{
				i1 -= l2 * k;
				j1 -= j3 * k;
				l1 -= i3 * k;
				i2 -= k3 * k;
				k = 0;
			}
			l <<= 16;
			k1 <<= 15;
			if(i < 0)
			{
				l -= j2 * i;
				k1 -= k2 * i;
				i = 0;
			}
			if(l2 < j3)
			{
				j -= i;
				i -= k;
				for(k = lineOffsets[k]; --i >= 0; k += DrawingArea.width)
				{
					drawLDShadedLine(DrawingArea.pixels, k, i1 >> 16, j1 >> 16, l1 >> 7, i2 >> 7);
					i1 += l2;
					j1 += j3;
					l1 += i3;
					i2 += k3;
				}

				while(--j >= 0) 
				{
					drawLDShadedLine(DrawingArea.pixels, k, i1 >> 16, l >> 16, l1 >> 7, k1 >> 7);
					i1 += l2;
					l += j2;
					l1 += i3;
					k1 += k2;
					k += DrawingArea.width;
				}
				return;
			}
			j -= i;
			i -= k;
			for(k = lineOffsets[k]; --i >= 0; k += DrawingArea.width)
			{
				drawLDShadedLine(DrawingArea.pixels, k, j1 >> 16, i1 >> 16, i2 >> 7, l1 >> 7);
				i1 += l2;
				j1 += j3;
				l1 += i3;
				i2 += k3;
			}

			while(--j >= 0) 
			{
				drawLDShadedLine(DrawingArea.pixels, k, l >> 16, i1 >> 16, k1 >> 7, l1 >> 7);
				i1 += l2;
				l += j2;
				l1 += i3;
				k1 += k2;
				k += DrawingArea.width;
			}
			return;
		}
		l = j1 <<= 16;
		k1 = i2 <<= 15;
		if(k < 0)
		{
			l -= l2 * k;
			j1 -= j3 * k;
			k1 -= i3 * k;
			i2 -= k3 * k;
			k = 0;
		}
		i1 <<= 16;
		l1 <<= 15;
		if(j < 0)
		{
			i1 -= j2 * j;
			l1 -= k2 * j;
			j = 0;
		}
		if(l2 < j3)
		{
			i -= j;
			j -= k;
			for(k = lineOffsets[k]; --j >= 0; k += DrawingArea.width)
			{
				drawLDShadedLine(DrawingArea.pixels, k, l >> 16, j1 >> 16, k1 >> 7, i2 >> 7);
				l += l2;
				j1 += j3;
				k1 += i3;
				i2 += k3;
			}

			while(--i >= 0) 
			{
				drawLDShadedLine(DrawingArea.pixels, k, i1 >> 16, j1 >> 16, l1 >> 7, i2 >> 7);
				i1 += j2;
				j1 += j3;
				l1 += k2;
				i2 += k3;
				k += DrawingArea.width;
			}
			return;
		}
		i -= j;
		j -= k;
		for(k = lineOffsets[k]; --j >= 0; k += DrawingArea.width)
		{
			drawLDShadedLine(DrawingArea.pixels, k, j1 >> 16, l >> 16, i2 >> 7, k1 >> 7);
			l += l2;
			j1 += j3;
			k1 += i3;
			i2 += k3;
		}

		while(--i >= 0) 
		{
			drawLDShadedLine(DrawingArea.pixels, k, j1 >> 16, i1 >> 16, i2 >> 7, l1 >> 7);
			i1 += j2;
			j1 += j3;
			l1 += k2;
			i2 += k3;
			k += DrawingArea.width;
		}
	}
	
	private static void drawLDShadedLine(int ai[], int i, int l, int i1, int j1, int k1) {
		//drawShadedLine562(ai, i, l, i1, j1, k1);
		int j;// was parameter
		int k;// was parameter
		if (notTextured) {
			int l1;
			if (restrict_edges) {
				if (i1 - l > 3)
					l1 = (k1 - j1) / (i1 - l);
				else
					l1 = 0;
				if (i1 > DrawingArea.viewportRX)
					i1 = DrawingArea.viewportRX;
				if (l < 0) {
					j1 -= l * l1;
					l = 0;
				}
				if (l >= i1)
					return;
				i += l;
				k = i1 - l >> 2;
				l1 <<= 2;
			} else {
				if (l >= i1)
					return;
				i += l;
				k = i1 - l >> 2;
				if (k > 0)
					l1 = (k1 - j1) * shadowDecay[k] >> 15;
				else
					l1 = 0;
			}
			if (alpha == 0) {
				while (--k >= 0) {
					j = hsl2rgb[j1 >> 8];
					j1 += l1;
					ai[i++] = j;
					ai[i++] = j;
					ai[i++] = j;
					ai[i++] = j;
				}
				k = i1 - l & 3;
				if (k > 0) {
					try {
					j = hsl2rgb[j1 >> 8];
					do
						ai[i++] = j;
					while (--k > 0);
					} catch(Exception e) {}
					return;
				}
			} else {
				int j2 = alpha;
				int l2 = 256 - alpha;
				while (--k >= 0) {
					j = hsl2rgb[j1 >> 8];
					j1 += l1;
					j = ((j & 0xff00ff) * l2 >> 8 & 0xff00ff) + ((j & 0xff00) * l2 >> 8 & 0xff00);
					ai[i] = j + ((ai[i] & 0xff00ff) * j2 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j2 >> 8 & 0xff00);
					i++;
					ai[i] = j + ((ai[i] & 0xff00ff) * j2 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j2 >> 8 & 0xff00);
					i++;
					ai[i] = j + ((ai[i] & 0xff00ff) * j2 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j2 >> 8 & 0xff00);
					i++;
					ai[i] = j + ((ai[i] & 0xff00ff) * j2 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j2 >> 8 & 0xff00);
					i++;
				}
				k = i1 - l & 3;// dugi
				if (k > 0) {
					j = hsl2rgb[j1 >> 8];
					j = ((j & 0xff00ff) * l2 >> 8 & 0xff00ff) + ((j & 0xff00) * l2 >> 8 & 0xff00);
					do {
						ai[i] = j + ((ai[i] & 0xff00ff) * j2 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j2 >> 8 & 0xff00);
						i++;
					} while (--k > 0);
				}
			}
			return;
		}
		if (l >= i1)
			return;
		int i2 = (k1 - j1) / (i1 - l);
		if (restrict_edges) {
			if (i1 > DrawingArea.viewportRX)
				i1 = DrawingArea.viewportRX;
			if (l < 0) {
				j1 -= l * i2;
				l = 0;
			}
			if (l >= i1)
				return;
		}
		i += l;
		k = i1 - l;
		if (alpha == 0) {
			do {
				ai[i++] = hsl2rgb[j1 >> 8];
				j1 += i2;
			} while (--k > 0);
			return;
		}
		int k2 = alpha;
		int i3 = 256 - alpha;
		do {
			j = hsl2rgb[j1 >> 8];
			j1 += i2;
			j = ((j & 0xff00ff) * i3 >> 8 & 0xff00ff) + ((j & 0xff00) * i3 >> 8 & 0xff00);
			ai[i] = j + ((ai[i] & 0xff00ff) * k2 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * k2 >> 8 & 0xff00);
			i++;
		} while (--k > 0);
	}

	/**
	 * Untextured triangle WITH  smooth shading.
	 */
	public static void drawHDShadedTriangle(int y1, int y2, int offset, int x1, int x2, int x3, int hsl1, int hsl2, int hsl3) {
        int rgb1 = hsl2rgb[hsl1];
        int rgb2 = hsl2rgb[hsl2];
        int rgb3 = hsl2rgb[hsl3];
        int r1 = rgb1 >> 16 & 0xff;
        int g1 = rgb1 >> 8 & 0xff;
        int b1 = rgb1 & 0xff;
        int r2 = rgb2 >> 16 & 0xff;
        int g2 = rgb2 >> 8 & 0xff;
        int b2 = rgb2 & 0xff;
        int r3 = rgb3 >> 16 & 0xff;
        int g3 = rgb3 >> 8 & 0xff;
        int b3 = rgb3 & 0xff;
        int dx1 = 0;
        int dr1 = 0;
        int dg1 = 0;
        int db1 = 0;
        if (y2 != y1) {
                dx1 = (x2 - x1 << 16) / (y2 - y1);
                dr1 = (r2 - r1 << 16) / (y2 - y1);
                dg1 = (g2 - g1 << 16) / (y2 - y1);
                db1 = (b2 - b1 << 16) / (y2 - y1);
        }
        int dx2 = 0;
        int dr2 = 0;
        int dg2 = 0;
        int db2 = 0;
        if (offset != y2) {
                dx2 = (x3 - x2 << 16) / (offset - y2);
                dr2 = (r3 - r2 << 16) / (offset - y2);
                dg2 = (g3 - g2 << 16) / (offset - y2);
                db2 = (b3 - b2 << 16) / (offset - y2);
        }
        int dx3 = 0;
        int dr3 = 0;
        int dg3 = 0;
        int db3 = 0;
        if (offset != y1) {
                dx3 = (x1 - x3 << 16) / (y1 - offset);
                dr3 = (r1 - r3 << 16) / (y1 - offset);
                dg3 = (g1 - g3 << 16) / (y1 - offset);
                db3 = (b1 - b3 << 16) / (y1 - offset);
        }
        if(y1 <= y2 && y1 <= offset) {
                if(y1 >= DrawingArea.bottomY) {
                        return;
                }
                if(y2 > DrawingArea.bottomY) {
                        y2 = DrawingArea.bottomY;
                }
                if(offset > DrawingArea.bottomY) {
                        offset = DrawingArea.bottomY;
                }
                if(y2 < offset) {
                        x3 = x1 <<= 16;
                        r3 = r1 <<= 16;
                        g3 = g1 <<= 16;
                        b3 = b1 <<= 16;
                        if(y1 < 0) {
                                x3 -= dx3 * y1;
                                x1 -= dx1 * y1;
                                r3 -= dr3 * y1;
                                g3 -= dg3 * y1;
                                b3 -= db3 * y1;
                                r1 -= dr1 * y1;
                                g1 -= dg1 * y1;
                                b1 -= db1 * y1;
                                y1 = 0;
                        }
                        x2 <<= 16;
                        r2 <<= 16;
                        g2 <<= 16;
                        b2 <<= 16;
                        if(y2 < 0) {
                                x2 -= dx2 * y2;
                                r2 -= dr2 * y2;
                                g2 -= dg2 * y2;
                                b2 -= db2 * y2;
                                y2 = 0;
                        }
                        if(y1 != y2 && dx3 < dx1 || y1 == y2 && dx3 > dx2) {
                                offset -= y2;
                                y2 -= y1;
                                for(y1 = lineOffsets[y1]; --y2 >= 0; y1 += DrawingArea.width) {
                                        drawHDShadedScanline(DrawingArea.pixels, y1, x3 >> 16, x1 >> 16, r3, g3, b3, r1, g1, b1);
                                        x3 += dx3;
                                        x1 += dx1;
                                        r3 += dr3;
                                        g3 += dg3;
                                        b3 += db3;
                                        r1 += dr1;
                                        g1 += dg1;
                                        b1 += db1;
                                }
                                while(--offset >= 0) {
                                        drawHDShadedScanline(DrawingArea.pixels, y1, x3 >> 16, x2 >> 16, r3, g3, b3, r2, g2, b2);
                                        x3 += dx3;
                                        x2 += dx2;
                                        r3 += dr3;
                                        g3 += dg3;
                                        b3 += db3;
                                        r2 += dr2;
                                        g2 += dg2;
                                        b2 += db2;
                                        y1 += DrawingArea.width;
                                }
                                return;
                        }
                        offset -= y2;
                        y2 -= y1;
                        for(y1 = lineOffsets[y1]; --y2 >= 0; y1 += DrawingArea.width) {
                                drawHDShadedScanline(DrawingArea.pixels, y1, x1 >> 16, x3 >> 16, r1, g1, b1, r3, g3, b3);
                                x3 += dx3;
                                x1 += dx1;
                                r3 += dr3;
                                g3 += dg3;
                                b3 += db3;
                                r1 += dr1;
                                g1 += dg1;
                                b1 += db1;
                        }
                        while(--offset >= 0) {
                                drawHDShadedScanline(DrawingArea.pixels, y1, x2 >> 16, x3 >> 16, r2, g2, b2, r3, g3, b3);
                                x3 += dx3;
                                x2 += dx2;
                                r3 += dr3;
                                g3 += dg3;
                                b3 += db3;
                                r2 += dr2;
                                g2 += dg2;
                                b2 += db2;
                                y1 += DrawingArea.width;
                        }
                        return;
                }
                x2 = x1 <<= 16;
                r2 = r1 <<= 16;
                g2 = g1 <<= 16;
                b2 = b1 <<= 16;
                if(y1 < 0) {
                        x2 -= dx3 * y1;
                        x1 -= dx1 * y1;
                        r2 -= dr3 * y1;
                        g2 -= dg3 * y1;
                        b2 -= db3 * y1;
                        r1 -= dr1 * y1;
                        g1 -= dg1 * y1;
                        b1 -= db1 * y1;
                        y1 = 0;
                }
                x3 <<= 16;
                r3 <<= 16;
                g3 <<= 16;
                b3 <<= 16;
                if(offset < 0) {
                        x3 -= dx2 * offset;
                        r3 -= dr2 * offset;
                        g3 -= dg2 * offset;
                        b3 -= db2 * offset;
                        offset = 0;
                }
                if(y1 != offset && dx3 < dx1 || y1 == offset && dx2 > dx1) {
                        y2 -= offset;
                        offset -= y1;
                        for(y1 = lineOffsets[y1]; --offset >= 0; y1 += DrawingArea.width) {
                                drawHDShadedScanline(DrawingArea.pixels, y1, x2 >> 16, x1 >> 16, r2, g2, b2, r1, g1, b1);
                                x2 += dx3;
                                x1 += dx1;
                                r2 += dr3;
                                g2 += dg3;
                                b2 += db3;
                                r1 += dr1;
                                g1 += dg1;
                                b1 += db1;
                        }
                        while(--y2 >= 0) {
                                drawHDShadedScanline(DrawingArea.pixels, y1, x3 >> 16, x1 >> 16, r3, g3, b3, r1, g1, b1);
                                x3 += dx2;
                                x1 += dx1;
                                r3 += dr2;
                                g3 += dg2;
                                b3 += db2;
                                r1 += dr1;
                                g1 += dg1;
                                b1 += db1;
                                y1 += DrawingArea.width;
                        }
                        return;
                }
                y2 -= offset;
                offset -= y1;
                for(y1 = lineOffsets[y1]; --offset >= 0; y1 += DrawingArea.width) {
                        drawHDShadedScanline(DrawingArea.pixels, y1, x1 >> 16, x2 >> 16, r1, g1, b1, r2, g2, b2);
                        x2 += dx3;
                        x1 += dx1;
                        r2 += dr3;
                        g2 += dg3;
                        b2 += db3;
                        r1 += dr1;
                        g1 += dg1;
                        b1 += db1;
                }
                while(--y2 >= 0) {
                        drawHDShadedScanline(DrawingArea.pixels, y1, x1 >> 16, x3 >> 16, r1, g1, b1, r3, g3, b3);
                        x3 += dx2;
                        x1 += dx1;
                        r3 += dr2;
                        g3 += dg2;
                        b3 += db2;
                        r1 += dr1;
                        g1 += dg1;
                        b1 += db1;
                        y1 += DrawingArea.width;
                }
                return;
        }
        if(y2 <= offset) {
                if(y2 >= DrawingArea.bottomY) {
                        return;
                }
                if(offset > DrawingArea.bottomY) {
                        offset = DrawingArea.bottomY;
                }
                if(y1 > DrawingArea.bottomY) {
                        y1 = DrawingArea.bottomY;
                }
                if(offset < y1) {
                        x1 = x2 <<= 16;
                        r1 = r2 <<= 16;
                        g1 = g2 <<= 16;
                        b1 = b2 <<= 16;
                        if(y2 < 0) {
                                x1 -= dx1 * y2;
                                x2 -= dx2 * y2;
                                r1 -= dr1 * y2;
                                g1 -= dg1 * y2;
                                b1 -= db1 * y2;
                                r2 -= dr2 * y2;
                                g2 -= dg2 * y2;
                                b2 -= db2 * y2;
                                y2 = 0;
                        }
                        x3 <<= 16;
                        r3 <<= 16;
                        g3 <<= 16;
                        b3 <<= 16;
                        if(offset < 0) {
                                x3 -= dx3 * offset;
                                r3 -= dr3 * offset;
                                g3 -= dg3 * offset;
                                b3 -= db3 * offset;
                                offset = 0;
                        }
                        if(y2 != offset && dx1 < dx2 || y2 == offset && dx1 > dx3) {
                                y1 -= offset;
                                offset -= y2;
                                for(y2 = lineOffsets[y2]; --offset >= 0; y2 += DrawingArea.width) {
                                        drawHDShadedScanline(DrawingArea.pixels, y2, x1 >> 16, x2 >> 16, r1, g1, b1, r2, g2, b2);
                                        x1 += dx1;
                                        x2 += dx2;
                                        r1 += dr1;
                                        g1 += dg1;
                                        b1 += db1;
                                        r2 += dr2;
                                        g2 += dg2;
                                        b2 += db2;
                                }
                                while(--y1 >= 0) {
                                        drawHDShadedScanline(DrawingArea.pixels, y2, x1 >> 16, x3 >> 16, r1, g1, b1, r3, g3, b3);
                                        x1 += dx1;
                                        x3 += dx3;
                                        r1 += dr1;
                                        g1 += dg1;
                                        b1 += db1;
                                        r3 += dr3;
                                        g3 += dg3;
                                        b3 += db3;
                                        y2 += DrawingArea.width;
                                }
                                return;
                        }
                        y1 -= offset;
                        offset -= y2;
                        for(y2 = lineOffsets[y2]; --offset >= 0; y2 += DrawingArea.width) {
                                drawHDShadedScanline(DrawingArea.pixels, y2, x2 >> 16, x1 >> 16, r2, g2, b2, r1, g1, b1);
                                x1 += dx1;
                                x2 += dx2;
                                r1 += dr1;
                                g1 += dg1;
                                b1 += db1;
                                r2 += dr2;
                                g2 += dg2;
                                b2 += db2;
                        }
                        while(--y1 >= 0) {
                                drawHDShadedScanline(DrawingArea.pixels, y2, x3 >> 16, x1 >> 16, r3, g3, b3, r1, g1, b1);
                                x1 += dx1;
                                x3 += dx3;
                                r1 += dr1;
                                g1 += dg1;
                                b1 += db1;
                                r3 += dr3;
                                g3 += dg3;
                                b3 += db3;
                                y2 += DrawingArea.width;
                        }
                        return;
                }
                x3 = x2 <<= 16;
                r3 = r2 <<= 16;
                g3 = g2 <<= 16;
                b3 = b2 <<= 16;
                if(y2 < 0) {
                        x3 -= dx1 * y2;
                        x2 -= dx2 * y2;
                        r3 -= dr1 * y2;
                        g3 -= dg1 * y2;
                        b3 -= db1 * y2;
                        r2 -= dr2 * y2;
                        g2 -= dg2 * y2;
                        b2 -= db2 * y2;
                        y2 = 0;
                }
                x1 <<= 16;
                r1 <<= 16;
                g1 <<= 16;
                b1 <<= 16;
                if(y1 < 0) {
                        x1 -= dx3 * y1;
                        r1 -= dr3 * y1;
                        g1 -= dg3 * y1;
                        b1 -= db3 * y1;
                        y1 = 0;
                }
                if(dx1 < dx2) {
                        offset -= y1;
                        y1 -= y2;
                        for(y2 = lineOffsets[y2]; --y1 >= 0; y2 += DrawingArea.width) {
                                drawHDShadedScanline(DrawingArea.pixels, y2, x3 >> 16, x2 >> 16, r3, g3, b3, r2, g2, b2);
                                x3 += dx1;
                                x2 += dx2;
                                r3 += dr1;
                                g3 += dg1;
                                b3 += db1;
                                r2 += dr2;
                                g2 += dg2;
                                b2 += db2;
                        }
                        while(--offset >= 0) {
                                drawHDShadedScanline(DrawingArea.pixels, y2, x1 >> 16, x2 >> 16, r1, g1, b1, r2, g2, b2);
                                x1 += dx3;
                                x2 += dx2;
                                r1 += dr3;
                                g1 += dg3;
                                b1 += db3;
                                r2 += dr2;
                                g2 += dg2;
                                b2 += db2;
                                y2 += DrawingArea.width;
                        }
                        return;
                }
                offset -= y1;
                y1 -= y2;
                for(y2 = lineOffsets[y2]; --y1 >= 0; y2 += DrawingArea.width) {
                        drawHDShadedScanline(DrawingArea.pixels, y2, x2 >> 16, x3 >> 16, r2, g2, b2, r3, g3, b3);
                        x3 += dx1;
                        x2 += dx2;
                        r3 += dr1;
                        g3 += dg1;
                        b3 += db1;
                        r2 += dr2;
                        g2 += dg2;
                        b2 += db2;
                }
                while(--offset >= 0) {
                        drawHDShadedScanline(DrawingArea.pixels, y2, x2 >> 16, x1 >> 16, r2, g2, b2, r1, g1, b1);
                        x1 += dx3;
                        x2 += dx2;
                        r1 += dr3;
                        g1 += dg3;
                        b1 += db3;
                        r2 += dr2;
                        g2 += dg2;
                        b2 += db2;
                        y2 += DrawingArea.width;
                }
                return;
        }
        if(offset >= DrawingArea.bottomY) {
                return;
        }
        if(y1 > DrawingArea.bottomY) {
                y1 = DrawingArea.bottomY;
        }
        if(y2 > DrawingArea.bottomY) {
                y2 = DrawingArea.bottomY;
        }
        if(y1 < y2) {
                x2 = x3 <<= 16;
                r2 = r3 <<= 16;
                g2 = g3 <<= 16;
                b2 = b3 <<= 16;
                if(offset < 0) {
                        x2 -= dx2 * offset;
                        x3 -= dx3 * offset;
                        r2 -= dr2 * offset;
                        g2 -= dg2 * offset;
                        b2 -= db2 * offset;
                        r3 -= dr3 * offset;
                        g3 -= dg3 * offset;
                        b3 -= db3 * offset;
                        offset = 0;
                }
                x1 <<= 16;
                r1 <<= 16;
                g1 <<= 16;
                b1 <<= 16;
                if(y1 < 0) {
                        x1 -= dx1 * y1;
                        r1 -= dr1 * y1;
                        g1 -= dg1 * y1;
                        b1 -= db1 * y1;
                        y1 = 0;
                }
                if(dx2 < dx3) {
                        y2 -= y1;
                        y1 -= offset;
                        for(offset = lineOffsets[offset]; --y1 >= 0; offset += DrawingArea.width) {
                                drawHDShadedScanline(DrawingArea.pixels, offset, x2 >> 16, x3 >> 16, r2, g2, b2, r3, g3, b3);
                                x2 += dx2;
                                x3 += dx3;
                                r2 += dr2;
                                g2 += dg2;
                                b2 += db2;
                                r3 += dr3;
                                g3 += dg3;
                                b3 += db3;
                        }
                        while(--y2 >= 0) {
                                drawHDShadedScanline(DrawingArea.pixels, offset, x2 >> 16, x1 >> 16, r2, g2, b2, r1, g1, b1);
                                x2 += dx2;
                                x1 += dx1;
                                r2 += dr2;
                                g2 += dg2;
                                b2 += db2;
                                r1 += dr1;
                                g1 += dg1;
                                b1 += db1;
                                offset += DrawingArea.width;
                        }
                        return;
                }
                y2 -= y1;
                y1 -= offset;
                for(offset = lineOffsets[offset]; --y1 >= 0; offset += DrawingArea.width) {
                        drawHDShadedScanline(DrawingArea.pixels, offset, x3 >> 16, x2 >> 16, r3, g3, b3, r2, g2, b2);
                        x2 += dx2;
                        x3 += dx3;
                        r2 += dr2;
                        g2 += dg2;
                        b2 += db2;
                        r3 += dr3;
                        g3 += dg3;
                        b3 += db3;
                }
                while(--y2 >= 0) {
                        drawHDShadedScanline(DrawingArea.pixels, offset, x1 >> 16, x2 >> 16, r1, g1, b1, r2, g2, b2);
                        x2 += dx2;
                        x1 += dx1;
                        r2 += dr2;
                        g2 += dg2;
                        b2 += db2;
                        r1 += dr1;
                        g1 += dg1;
                        b1 += db1;
                        offset += DrawingArea.width;
                }
                return;
        }
        x1 = x3 <<= 16;
        r1 = r3 <<= 16;
        g1 = g3 <<= 16;
        b1 = b3 <<= 16;
        if(offset < 0) {
                x1 -= dx2 * offset;
                x3 -= dx3 * offset;
                r1 -= dr2 * offset;
                g1 -= dg2 * offset;
                b1 -= db2 * offset;
                r3 -= dr3 * offset;
                g3 -= dg3 * offset;
                b3 -= db3 * offset;
                offset = 0;
        }
        x2 <<= 16;
        r2 <<= 16;
        g2 <<= 16;
        b2 <<= 16;
        if(y2 < 0) {
                x2 -= dx1 * y2;
                r2 -= dr1 * y2;
                g2 -= dg1 * y2;
                b2 -= db1 * y2;
                y2 = 0;
        }
        if(dx2 < dx3) {
                y1 -= y2;
                y2 -= offset;
                for(offset = lineOffsets[offset]; --y2 >= 0; offset += DrawingArea.width) {
                        drawHDShadedScanline(DrawingArea.pixels, offset, x1 >> 16, x3 >> 16, r1, g1, b1, r3, g3, b3);
                        x1 += dx2;
                        x3 += dx3;
                        r1 += dr2;
                        g1 += dg2;
                        b1 += db2;
                        r3 += dr3;
                        g3 += dg3;
                        b3 += db3;
                }
                while(--y1 >= 0) {
                        drawHDShadedScanline(DrawingArea.pixels, offset, x2 >> 16, x3 >> 16, r2, g2, b2, r3, g3, b3);
                        x2 += dx1;
                        x3 += dx3;
                        r2 += dr1;
                        g2 += dg1;
                        b2 += db1;
                        r3 += dr3;
                        g3 += dg3;
                        b3 += db3;
                        offset += DrawingArea.width;
                }
                return;
        }
        y1 -= y2;
        y2 -= offset;
        for(offset = lineOffsets[offset]; --y2 >= 0; offset += DrawingArea.width) {
                drawHDShadedScanline(DrawingArea.pixels, offset, x3 >> 16, x1 >> 16, r3, g3, b3, r1, g1, b1);
                x1 += dx2;
                x3 += dx3;
                r1 += dr2;
                g1 += dg2;
                b1 += db2;
                r3 += dr3;
                g3 += dg3;
                b3 += db3;
        }
        while(--y1 >= 0) {
                drawHDShadedScanline(DrawingArea.pixels, offset, x3 >> 16, x2 >> 16, r3, g3, b3, r2, g2, b2);
                x2 += dx1;
                x3 += dx3;
                r2 += dr1;
                g2 += dg1;
                b2 += db1;
                r3 += dr3;
                g3 += dg3;
                b3 += db3;
                offset += DrawingArea.width;
        }
}


	public static void drawHDShadedScanline(int[] dest, int offset, int x1, int x2, int r1, int g1, int b1, int r2, int g2, int b2) {
        int n = x2 - x1;
        if (n <= 0) {
                return;
        }
        r2 = (r2 - r1) / n;
        g2 = (g2 - g1) / n;
        b2 = (b2 - b1) / n;
        if (restrict_edges) {
                if (x2 > DrawingArea.viewportRX) {
                        n -= x2 - DrawingArea.viewportRX;
                        x2 = DrawingArea.viewportRX;
                }
                if (x1 < 0) {
                        n = x2;
                        r1 -= x1 * r2;
                        g1 -= x1 * g2;
                        b1 -= x1 * b2;
                        x1 = 0;
                }
        }
        if (x1 < x2) {
                offset += x1;
                if (alpha == 0) {
                        while (--n >= 0) {
                                dest[offset] = (r1 & 0xff0000) | (g1 >> 8 & 0xff00) | (b1 >> 16 & 0xff);
                                r1 += r2;
                                g1 += g2;
                                b1 += b2;
                                offset++;
                        }
                } else {
                        final int a1 = alpha;
                        final int a2 = 256 - alpha;
                        int rgb;
                        int dst;
                        while (--n >= 0) {
                                rgb = (r1 & 0xff0000) | (g1 >> 8 & 0xff00) | (b1 >> 16 & 0xff);
                                rgb = ((rgb & 0xff00ff) * a2 >> 8 & 0xff00ff) + ((rgb & 0xff00) * a2 >> 8 & 0xff00);
                                dst = dest[offset];
                                dest[offset] = rgb + ((dst & 0xff00ff) * a1 >> 8 & 0xff00ff) + ((dst & 0xff00) * a1 >> 8 & 0xff00);
                                r1 += r2;
                                g1 += g2;
                                b1 += b2;
                                offset++;
                        }
                }
        }
	}
	
	/**
	 * Draw basic regular colored triangle.
	 */
	public static void drawFlatTriangle(int i, int j, int k, int l, int i1, int j1, int k1) {
		int l1 = 0;
		if (j != i)
			l1 = (i1 - l << 16) / (j - i);
		int i2 = 0;
		if (k != j)
			i2 = (j1 - i1 << 16) / (k - j);
		int j2 = 0;
		if (k != i)
			j2 = (l - j1 << 16) / (i - k);
		if (i <= j && i <= k) {
			if (i >= DrawingArea.bottomY)
				return;
			if (j > DrawingArea.bottomY)
				j = DrawingArea.bottomY;
			if (k > DrawingArea.bottomY)
				k = DrawingArea.bottomY;
			if (j < k) {
				j1 = l <<= 16;
				if (i < 0) {
					j1 -= j2 * i;
					l -= l1 * i;
					i = 0;
				}
				i1 <<= 16;
				if (j < 0) {
					i1 -= i2 * j;
					j = 0;
				}
				if (i != j && j2 < l1 || i == j && j2 > i2) {
					k -= j;
					j -= i;
					for (i = lineOffsets[i]; --j >= 0; i += DrawingArea.width) {
						drawFlatLine(DrawingArea.pixels, i, k1, j1 >> 16, l >> 16);
						j1 += j2;
						l += l1;
					}

					while (--k >= 0) {
						drawFlatLine(DrawingArea.pixels, i, k1, j1 >> 16, i1 >> 16);
						j1 += j2;
						i1 += i2;
						i += DrawingArea.width;
					}
					return;
				}
				k -= j;
				j -= i;
				for (i = lineOffsets[i]; --j >= 0; i += DrawingArea.width) {
					drawFlatLine(DrawingArea.pixels, i, k1, l >> 16, j1 >> 16);
					j1 += j2;
					l += l1;
				}

				while (--k >= 0) {
					drawFlatLine(DrawingArea.pixels, i, k1, i1 >> 16, j1 >> 16);
					j1 += j2;
					i1 += i2;
					i += DrawingArea.width;
				}
				return;
			}
			i1 = l <<= 16;
			if (i < 0) {
				i1 -= j2 * i;
				l -= l1 * i;
				i = 0;
			}
			j1 <<= 16;
			if (k < 0) {
				j1 -= i2 * k;
				k = 0;
			}
			if (i != k && j2 < l1 || i == k && i2 > l1) {
				j -= k;
				k -= i;
				for (i = lineOffsets[i]; --k >= 0; i += DrawingArea.width) {
					drawFlatLine(DrawingArea.pixels, i, k1, i1 >> 16, l >> 16);
					i1 += j2;
					l += l1;
				}

				while (--j >= 0) {
					drawFlatLine(DrawingArea.pixels, i, k1, j1 >> 16, l >> 16);
					j1 += i2;
					l += l1;
					i += DrawingArea.width;
				}
				return;
			}
			j -= k;
			k -= i;
			for (i = lineOffsets[i]; --k >= 0; i += DrawingArea.width) {
				drawFlatLine(DrawingArea.pixels, i, k1, l >> 16, i1 >> 16);
				i1 += j2;
				l += l1;
			}

			while (--j >= 0) {
				drawFlatLine(DrawingArea.pixels, i, k1, l >> 16, j1 >> 16);
				j1 += i2;
				l += l1;
				i += DrawingArea.width;
			}
			return;
		}
		if (j <= k) {
			if (j >= DrawingArea.bottomY)
				return;
			if (k > DrawingArea.bottomY)
				k = DrawingArea.bottomY;
			if (i > DrawingArea.bottomY)
				i = DrawingArea.bottomY;
			if (k < i) {
				l = i1 <<= 16;
				if (j < 0) {
					l -= l1 * j;
					i1 -= i2 * j;
					j = 0;
				}
				j1 <<= 16;
				if (k < 0) {
					j1 -= j2 * k;
					k = 0;
				}
				if (j != k && l1 < i2 || j == k && l1 > j2) {
					i -= k;
					k -= j;
					for (j = lineOffsets[j]; --k >= 0; j += DrawingArea.width) {
						drawFlatLine(DrawingArea.pixels, j, k1, l >> 16, i1 >> 16);
						l += l1;
						i1 += i2;
					}

					while (--i >= 0) {
						drawFlatLine(DrawingArea.pixels, j, k1, l >> 16, j1 >> 16);
						l += l1;
						j1 += j2;
						j += DrawingArea.width;
					}
					return;
				}
				i -= k;
				k -= j;
				for (j = lineOffsets[j]; --k >= 0; j += DrawingArea.width) {
					drawFlatLine(DrawingArea.pixels, j, k1, i1 >> 16, l >> 16);
					l += l1;
					i1 += i2;
				}

				while (--i >= 0) {
					drawFlatLine(DrawingArea.pixels, j, k1, j1 >> 16, l >> 16);
					l += l1;
					j1 += j2;
					j += DrawingArea.width;
				}
				return;
			}
			j1 = i1 <<= 16;
			if (j < 0) {
				j1 -= l1 * j;
				i1 -= i2 * j;
				j = 0;
			}
			l <<= 16;
			if (i < 0) {
				l -= j2 * i;
				i = 0;
			}
			if (l1 < i2) {
				k -= i;
				i -= j;
				for (j = lineOffsets[j]; --i >= 0; j += DrawingArea.width) {
					drawFlatLine(DrawingArea.pixels, j, k1, j1 >> 16, i1 >> 16);
					j1 += l1;
					i1 += i2;
				}

				while (--k >= 0) {
					drawFlatLine(DrawingArea.pixels, j, k1, l >> 16, i1 >> 16);
					l += j2;
					i1 += i2;
					j += DrawingArea.width;
				}
				return;
			}
			k -= i;
			i -= j;
			for (j = lineOffsets[j]; --i >= 0; j += DrawingArea.width) {
				drawFlatLine(DrawingArea.pixels, j, k1, i1 >> 16, j1 >> 16);
				j1 += l1;
				i1 += i2;
			}

			while (--k >= 0) {
				drawFlatLine(DrawingArea.pixels, j, k1, i1 >> 16, l >> 16);
				l += j2;
				i1 += i2;
				j += DrawingArea.width;
			}
			return;
		}
		if (k >= DrawingArea.bottomY)
			return;
		if (i > DrawingArea.bottomY)
			i = DrawingArea.bottomY;
		if (j > DrawingArea.bottomY)
			j = DrawingArea.bottomY;
		if (i < j) {
			i1 = j1 <<= 16;
			if (k < 0) {
				i1 -= i2 * k;
				j1 -= j2 * k;
				k = 0;
			}
			l <<= 16;
			if (i < 0) {
				l -= l1 * i;
				i = 0;
			}
			if (i2 < j2) {
				j -= i;
				i -= k;
				for (k = lineOffsets[k]; --i >= 0; k += DrawingArea.width) {
					drawFlatLine(DrawingArea.pixels, k, k1, i1 >> 16, j1 >> 16);
					i1 += i2;
					j1 += j2;
				}

				while (--j >= 0) {
					drawFlatLine(DrawingArea.pixels, k, k1, i1 >> 16, l >> 16);
					i1 += i2;
					l += l1;
					k += DrawingArea.width;
				}
				return;
			}
			j -= i;
			i -= k;
			for (k = lineOffsets[k]; --i >= 0; k += DrawingArea.width) {
				drawFlatLine(DrawingArea.pixels, k, k1, j1 >> 16, i1 >> 16);
				i1 += i2;
				j1 += j2;
			}

			while (--j >= 0) {
				drawFlatLine(DrawingArea.pixels, k, k1, l >> 16, i1 >> 16);
				i1 += i2;
				l += l1;
				k += DrawingArea.width;
			}
			return;
		}
		l = j1 <<= 16;
		if (k < 0) {
			l -= i2 * k;
			j1 -= j2 * k;
			k = 0;
		}
		i1 <<= 16;
		if (j < 0) {
			i1 -= l1 * j;
			j = 0;
		}
		if (i2 < j2) {
			i -= j;
			j -= k;
			for (k = lineOffsets[k]; --j >= 0; k += DrawingArea.width) {
				drawFlatLine(DrawingArea.pixels, k, k1, l >> 16, j1 >> 16);
				l += i2;
				j1 += j2;
			}

			while (--i >= 0) {
				drawFlatLine(DrawingArea.pixels, k, k1, i1 >> 16, j1 >> 16);
				i1 += l1;
				j1 += j2;
				k += DrawingArea.width;
			}
			return;
		}
		i -= j;
		j -= k;
		for (k = lineOffsets[k]; --j >= 0; k += DrawingArea.width) {
			drawFlatLine(DrawingArea.pixels, k, k1, j1 >> 16, l >> 16);
			l += i2;
			j1 += j2;
		}

		while (--i >= 0) {
			drawFlatLine(DrawingArea.pixels, k, k1, j1 >> 16, i1 >> 16);
			i1 += l1;
			j1 += j2;
			k += DrawingArea.width;
		}
	}

	private static void drawFlatLine(int ai[], int i, int j, int l, int i1) {
		int k;// was parameter
		if (restrict_edges) {
			if (i1 > DrawingArea.viewportRX)
				i1 = DrawingArea.viewportRX;
			if (l < 0)
				l = 0;
		}
		if (l >= i1)
			return;
		i += l;
		k = i1 - l >> 2;
		if (alpha == 0) {
			while (--k >= 0) {
				ai[i++] = j;
				ai[i++] = j;
				ai[i++] = j;
				ai[i++] = j;
			}
			for (k = i1 - l & 3; --k >= 0;)
				ai[i++] = j;

			return;
		}
		int j1 = alpha;
		int k1 = 256 - alpha;
		j = ((j & 0xff00ff) * k1 >> 8 & 0xff00ff) + ((j & 0xff00) * k1 >> 8 & 0xff00);
		while (--k >= 0) {
			ai[i++] = j + ((ai[i] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j1 >> 8 & 0xff00);
			ai[i++] = j + ((ai[i] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j1 >> 8 & 0xff00);
			ai[i++] = j + ((ai[i] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j1 >> 8 & 0xff00);
			ai[i++] = j + ((ai[i] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j1 >> 8 & 0xff00);
		}
		for (k = i1 - l & 3; --k >= 0;)
			ai[i++] = j + ((ai[i] & 0xff00ff) * j1 >> 8 & 0xff00ff) + ((ai[i] & 0xff00) * j1 >> 8 & 0xff00);

	}

	/**
	 * Ld Textured triangle -- 317 textures.
	 */
	public static void drawTextureTriangle1(int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2, int j2, int k2, int l2, int i3, int j3, int k3, int l3, int i4, int j4, int k4) {
		int ai[] = null;
		if(Client.getOption("hd_tex")) {
			ai = TextureLoader667.getTexturePixels(k4);
		} else {
			ai = TextureLoader317.getTexturePixels(k4);
		}
		opaque = false;
		if(!Client.getOption("hd_tex"))
			opaque = !TextureLoader317.textureIsTransparent[k4];
		k2 = j2 - k2;
		j3 = i3 - j3;
		i4 = l3 - i4;
		l2 -= j2;
		k3 -= i3;
		j4 -= l3;
		int l4 = l2 * i3 - k3 * j2 << (Client.log_view_dist == 9 ? 14 : 15);
		int i5 = k3 * l3 - j4 * i3 << 8;
		int j5 = j4 * j2 - l2 * l3 << 5;
		int k5 = k2 * i3 - j3 * j2 << (Client.log_view_dist == 9 ? 14 : 15);
		int l5 = j3 * l3 - i4 * i3 << 8;
		int i6 = i4 * j2 - k2 * l3 << 5;
		int j6 = j3 * l2 - k2 * k3 << (Client.log_view_dist == 9 ? 14 : 15);
		int k6 = i4 * k3 - j3 * j4 << 8;
		int l6 = k2 * j4 - i4 * l2 << 5;
		int i7 = 0;
		int j7 = 0;
		if (j != i) {
			i7 = (i1 - l << 16) / (j - i);
			j7 = (l1 - k1 << 16) / (j - i);
		}
		int k7 = 0;
		int l7 = 0;
		if (k != j) {
			k7 = (j1 - i1 << 16) / (k - j);
			l7 = (i2 - l1 << 16) / (k - j);
		}
		int i8 = 0;
		int j8 = 0;
		if (k != i) {
			i8 = (l - j1 << 16) / (i - k);
			j8 = (k1 - i2 << 16) / (i - k);
		}
		if (i <= j && i <= k) {
			if (i >= DrawingArea.bottomY)
				return;
			if (j > DrawingArea.bottomY)
				j = DrawingArea.bottomY;
			if (k > DrawingArea.bottomY)
				k = DrawingArea.bottomY;
			if (j < k) {
				j1 = l <<= 16;
				i2 = k1 <<= 16;
				if (i < 0) {
					j1 -= i8 * i;
					l -= i7 * i;
					i2 -= j8 * i;
					k1 -= j7 * i;
					i = 0;
				}
				i1 <<= 16;
				l1 <<= 16;
				if (j < 0) {
					i1 -= k7 * j;
					l1 -= l7 * j;
					j = 0;
				}
				int k8 = i - center_y;
				l4 += j5 * k8;
				k5 += i6 * k8;
				j6 += l6 * k8;
				if (i != j && i8 < i7 || i == j && i8 > k7) {
					k -= j;
					j -= i;
					i = lineOffsets[i];
					while (--j >= 0) {
						drawTexturesLine(DrawingArea.pixels, ai, i, j1 >> 16, l >> 16, i2 >> 8, k1 >> 8, l4, k5, j6, i5, l5, k6);
						j1 += i8;
						l += i7;
						i2 += j8;
						k1 += j7;
						i += DrawingArea.width;
						l4 += j5;
						k5 += i6;
						j6 += l6;
					}
					while (--k >= 0) {
						drawTexturesLine(DrawingArea.pixels, ai, i, j1 >> 16, i1 >> 16, i2 >> 8, l1 >> 8, l4, k5, j6, i5, l5, k6);
						j1 += i8;
						i1 += k7;
						i2 += j8;
						l1 += l7;
						i += DrawingArea.width;
						l4 += j5;
						k5 += i6;
						j6 += l6;
					}
					return;
				}
				k -= j;
				j -= i;
				i = lineOffsets[i];
				while (--j >= 0) {
					drawTexturesLine(DrawingArea.pixels, ai, i, l >> 16, j1 >> 16, k1 >> 8, i2 >> 8, l4, k5, j6, i5, l5, k6);
					j1 += i8;
					l += i7;
					i2 += j8;
					k1 += j7;
					i += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while (--k >= 0) {
					drawTexturesLine(DrawingArea.pixels, ai, i, i1 >> 16, j1 >> 16, l1 >> 8, i2 >> 8, l4, k5, j6, i5, l5, k6);
					j1 += i8;
					i1 += k7;
					i2 += j8;
					l1 += l7;
					i += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			i1 = l <<= 16;
			l1 = k1 <<= 16;
			if (i < 0) {
				i1 -= i8 * i;
				l -= i7 * i;
				l1 -= j8 * i;
				k1 -= j7 * i;
				i = 0;
			}
			j1 <<= 16;
			i2 <<= 16;
			if (k < 0) {
				j1 -= k7 * k;
				i2 -= l7 * k;
				k = 0;
			}
			int l8 = i - center_y;
			l4 += j5 * l8;
			k5 += i6 * l8;
			j6 += l6 * l8;
			if (i != k && i8 < i7 || i == k && k7 > i7) {
				j -= k;
				k -= i;
				i = lineOffsets[i];
				while (--k >= 0) {
					drawTexturesLine(DrawingArea.pixels, ai, i, i1 >> 16, l >> 16, l1 >> 8, k1 >> 8, l4, k5, j6, i5, l5, k6);
					i1 += i8;
					l += i7;
					l1 += j8;
					k1 += j7;
					i += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while (--j >= 0) {
					drawTexturesLine(DrawingArea.pixels, ai, i, j1 >> 16, l >> 16, i2 >> 8, k1 >> 8, l4, k5, j6, i5, l5, k6);
					j1 += k7;
					l += i7;
					i2 += l7;
					k1 += j7;
					i += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			j -= k;
			k -= i;
			i = lineOffsets[i];
			while (--k >= 0) {
				drawTexturesLine(DrawingArea.pixels, ai, i, l >> 16, i1 >> 16, k1 >> 8, l1 >> 8, l4, k5, j6, i5, l5, k6);
				i1 += i8;
				l += i7;
				l1 += j8;
				k1 += j7;
				i += DrawingArea.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			while (--j >= 0) {
				drawTexturesLine(DrawingArea.pixels, ai, i, l >> 16, j1 >> 16, k1 >> 8, i2 >> 8, l4, k5, j6, i5, l5, k6);
				j1 += k7;
				l += i7;
				i2 += l7;
				k1 += j7;
				i += DrawingArea.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			return;
		}
		if (j <= k) {
			if (j >= DrawingArea.bottomY)
				return;
			if (k > DrawingArea.bottomY)
				k = DrawingArea.bottomY;
			if (i > DrawingArea.bottomY)
				i = DrawingArea.bottomY;
			if (k < i) {
				l = i1 <<= 16;
				k1 = l1 <<= 16;
				if (j < 0) {
					l -= i7 * j;
					i1 -= k7 * j;
					k1 -= j7 * j;
					l1 -= l7 * j;
					j = 0;
				}
				j1 <<= 16;
				i2 <<= 16;
				if (k < 0) {
					j1 -= i8 * k;
					i2 -= j8 * k;
					k = 0;
				}
				int i9 = j - center_y;
				l4 += j5 * i9;
				k5 += i6 * i9;
				j6 += l6 * i9;
				if (j != k && i7 < k7 || j == k && i7 > i8) {
					i -= k;
					k -= j;
					j = lineOffsets[j];
					while (--k >= 0) {
						drawTexturesLine(DrawingArea.pixels, ai, j, l >> 16, i1 >> 16, k1 >> 8, l1 >> 8, l4, k5, j6, i5, l5, k6);
						l += i7;
						i1 += k7;
						k1 += j7;
						l1 += l7;
						j += DrawingArea.width;
						l4 += j5;
						k5 += i6;
						j6 += l6;
					}
					while (--i >= 0) {
						drawTexturesLine(DrawingArea.pixels, ai, j, l >> 16, j1 >> 16, k1 >> 8, i2 >> 8, l4, k5, j6, i5, l5, k6);
						l += i7;
						j1 += i8;
						k1 += j7;
						i2 += j8;
						j += DrawingArea.width;
						l4 += j5;
						k5 += i6;
						j6 += l6;
					}
					return;
				}
				i -= k;
				k -= j;
				j = lineOffsets[j];
				while (--k >= 0) {
					drawTexturesLine(DrawingArea.pixels, ai, j, i1 >> 16, l >> 16, l1 >> 8, k1 >> 8, l4, k5, j6, i5, l5, k6);
					l += i7;
					i1 += k7;
					k1 += j7;
					l1 += l7;
					j += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while (--i >= 0) {
					drawTexturesLine(DrawingArea.pixels, ai, j, j1 >> 16, l >> 16, i2 >> 8, k1 >> 8, l4, k5, j6, i5, l5, k6);
					l += i7;
					j1 += i8;
					k1 += j7;
					i2 += j8;
					j += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			j1 = i1 <<= 16;
			i2 = l1 <<= 16;
			if (j < 0) {
				j1 -= i7 * j;
				i1 -= k7 * j;
				i2 -= j7 * j;
				l1 -= l7 * j;
				j = 0;
			}
			l <<= 16;
			k1 <<= 16;
			if (i < 0) {
				l -= i8 * i;
				k1 -= j8 * i;
				i = 0;
			}
			int j9 = j - center_y;
			l4 += j5 * j9;
			k5 += i6 * j9;
			j6 += l6 * j9;
			if (i7 < k7) {
				k -= i;
				i -= j;
				j = lineOffsets[j];
				while (--i >= 0) {
					drawTexturesLine(DrawingArea.pixels, ai, j, j1 >> 16, i1 >> 16, i2 >> 8, l1 >> 8, l4, k5, j6, i5, l5, k6);
					j1 += i7;
					i1 += k7;
					i2 += j7;
					l1 += l7;
					j += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while (--k >= 0) {
					drawTexturesLine(DrawingArea.pixels, ai, j, l >> 16, i1 >> 16, k1 >> 8, l1 >> 8, l4, k5, j6, i5, l5, k6);
					l += i8;
					i1 += k7;
					k1 += j8;
					l1 += l7;
					j += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			k -= i;
			i -= j;
			j = lineOffsets[j];
			while (--i >= 0) {
				drawTexturesLine(DrawingArea.pixels, ai, j, i1 >> 16, j1 >> 16, l1 >> 8, i2 >> 8, l4, k5, j6, i5, l5, k6);
				j1 += i7;
				i1 += k7;
				i2 += j7;
				l1 += l7;
				j += DrawingArea.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			while (--k >= 0) {
				drawTexturesLine(DrawingArea.pixels, ai, j, i1 >> 16, l >> 16, l1 >> 8, k1 >> 8, l4, k5, j6, i5, l5, k6);
				l += i8;
				i1 += k7;
				k1 += j8;
				l1 += l7;
				j += DrawingArea.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			return;
		}
		if (k >= DrawingArea.bottomY)
			return;
		if (i > DrawingArea.bottomY)
			i = DrawingArea.bottomY;
		if (j > DrawingArea.bottomY)
			j = DrawingArea.bottomY;
		if (i < j) {
			i1 = j1 <<= 16;
			l1 = i2 <<= 16;
			if (k < 0) {
				i1 -= k7 * k;
				j1 -= i8 * k;
				l1 -= l7 * k;
				i2 -= j8 * k;
				k = 0;
			}
			l <<= 16;
			k1 <<= 16;
			if (i < 0) {
				l -= i7 * i;
				k1 -= j7 * i;
				i = 0;
			}
			int k9 = k - center_y;
			l4 += j5 * k9;
			k5 += i6 * k9;
			j6 += l6 * k9;
			if (k7 < i8) {
				j -= i;
				i -= k;
				k = lineOffsets[k];
				while (--i >= 0) {
					drawTexturesLine(DrawingArea.pixels, ai, k, i1 >> 16, j1 >> 16, l1 >> 8, i2 >> 8, l4, k5, j6, i5, l5, k6);
					i1 += k7;
					j1 += i8;
					l1 += l7;
					i2 += j8;
					k += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				while (--j >= 0) {
					drawTexturesLine(DrawingArea.pixels, ai, k, i1 >> 16, l >> 16, l1 >> 8, k1 >> 8, l4, k5, j6, i5, l5, k6);
					i1 += k7;
					l += i7;
					l1 += l7;
					k1 += j7;
					k += DrawingArea.width;
					l4 += j5;
					k5 += i6;
					j6 += l6;
				}
				return;
			}
			j -= i;
			i -= k;
			k = lineOffsets[k];
			while (--i >= 0) {
				drawTexturesLine(DrawingArea.pixels, ai, k, j1 >> 16, i1 >> 16, i2 >> 8, l1 >> 8, l4, k5, j6, i5, l5, k6);
				i1 += k7;
				j1 += i8;
				l1 += l7;
				i2 += j8;
				k += DrawingArea.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			while (--j >= 0) {
				drawTexturesLine(DrawingArea.pixels, ai, k, l >> 16, i1 >> 16, k1 >> 8, l1 >> 8, l4, k5, j6, i5, l5, k6);
				i1 += k7;
				l += i7;
				l1 += l7;
				k1 += j7;
				k += DrawingArea.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			return;
		}
		l = j1 <<= 16;
		k1 = i2 <<= 16;
		if (k < 0) {
			l -= k7 * k;
			j1 -= i8 * k;
			k1 -= l7 * k;
			i2 -= j8 * k;
			k = 0;
		}
		i1 <<= 16;
		l1 <<= 16;
		if (j < 0) {
			i1 -= i7 * j;
			l1 -= j7 * j;
			j = 0;
		}
		int l9 = k - center_y;
		l4 += j5 * l9;
		k5 += i6 * l9;
		j6 += l6 * l9;
		if (k7 < i8) {
			i -= j;
			j -= k;
			k = lineOffsets[k];
			while (--j >= 0) {
				drawTexturesLine(DrawingArea.pixels, ai, k, l >> 16, j1 >> 16, k1 >> 8, i2 >> 8, l4, k5, j6, i5, l5, k6);
				l += k7;
				j1 += i8;
				k1 += l7;
				i2 += j8;
				k += DrawingArea.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			while (--i >= 0) {
				drawTexturesLine(DrawingArea.pixels, ai, k, i1 >> 16, j1 >> 16, l1 >> 8, i2 >> 8, l4, k5, j6, i5, l5, k6);
				i1 += i7;
				j1 += i8;
				l1 += j7;
				i2 += j8;
				k += DrawingArea.width;
				l4 += j5;
				k5 += i6;
				j6 += l6;
			}
			return;
		}
		i -= j;
		j -= k;
		k = lineOffsets[k];
		while (--j >= 0) {
			drawTexturesLine(DrawingArea.pixels, ai, k, j1 >> 16, l >> 16, i2 >> 8, k1 >> 8, l4, k5, j6, i5, l5, k6);
			l += k7;
			j1 += i8;
			k1 += l7;
			i2 += j8;
			k += DrawingArea.width;
			l4 += j5;
			k5 += i6;
			j6 += l6;
		}
		while (--i >= 0) {
			drawTexturesLine(DrawingArea.pixels, ai, k, j1 >> 16, i1 >> 16, i2 >> 8, l1 >> 8, l4, k5, j6, i5, l5, k6);
			i1 += i7;
			j1 += i8;
			l1 += j7;
			i2 += j8;
			k += DrawingArea.width;
			l4 += j5;
			k5 += i6;
			j6 += l6;
		}
	}

	private static void drawTexturesLine(int ai[], int ai1[], int k, int l, int i1, int j1, int k1, int l1, int i2, int j2, int k2, int l2, int i3) {
		try {
		int i = 0;// was parameter
		int j = 0;// was parameter
		if (l >= i1)
			return;
		int j3;
		int k3;
		if (restrict_edges) {
			j3 = (k1 - j1) / (i1 - l);
			if (i1 > DrawingArea.viewportRX)
				i1 = DrawingArea.viewportRX;
			if (l < 0) {
				j1 -= l * j3;
				l = 0;
			}
			if (l >= i1)
				return;
			k3 = i1 - l >> 3;
			j3 <<= 12;
			j1 <<= 9;
		} else {
			if (i1 - l > 7) {
				k3 = i1 - l >> 3;
				j3 = (k1 - j1) * shadowDecay[k3] >> 6;
			} else {
				k3 = 0;
				j3 = 0;
			}
			j1 <<= 9;
		}
		k += l;
		if (lowMem) {
			int i4 = 0;
			int k4 = 0;
			int k6 = l - center_x;
			l1 += (k2 >> 3) * k6;
			i2 += (l2 >> 3) * k6;
			j2 += (i3 >> 3) * k6;
			int i5 = j2 >> 12;
			if (i5 != 0) {
				i = l1 / i5;
				j = i2 / i5;
				if (i < 0)
					i = 0;
				else if (i > 4032)
					i = 4032;
			}
			l1 += k2;
			i2 += l2;
			j2 += i3;
			i5 = j2 >> 12;
			if (i5 != 0) {
				i4 = l1 / i5;
				k4 = i2 / i5;
				if (i4 < 7)
					i4 = 7;
				else if (i4 > 4032)
					i4 = 4032;
			}
			int i7 = i4 - i >> 3;
			int k7 = k4 - j >> 3;
			i += (j1 & 0x600000) >> 3;
			int i8 = j1 >> 23;
			if (opaque) {
				while (k3-- > 0) {
					ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
					i += i7;
					j += k7;
					ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
					i += i7;
					j += k7;
					ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
					i += i7;
					j += k7;
					ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
					i += i7;
					j += k7;
					ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
					i += i7;
					j += k7;
					ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
					i += i7;
					j += k7;
					ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
					i += i7;
					j += k7;
					ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
					i = i4;
					j = k4;
					l1 += k2;
					i2 += l2;
					j2 += i3;
					int j5 = j2 >> 12;
					if (j5 != 0) {
						i4 = l1 / j5;
						k4 = i2 / j5;
						if (i4 < 7)
							i4 = 7;
						else if (i4 > 4032)
							i4 = 4032;
					}
					i7 = i4 - i >> 3;
					k7 = k4 - j >> 3;
					j1 += j3;
					i += (j1 & 0x600000) >> 3;
					i8 = j1 >> 23;
				}
				for (k3 = i1 - l & 7; k3-- > 0;) {
					ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
					i += i7;
					j += k7;
				}

				return;
			}
			while (k3-- > 0) {
				int k8;
				if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0)
					ai[k] = k8;
				k++;
				i += i7;
				j += k7;
				if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0)
					ai[k] = k8;
				k++;
				i += i7;
				j += k7;
				if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0)
					ai[k] = k8;
				k++;
				i += i7;
				j += k7;
				if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0)
					ai[k] = k8;
				k++;
				i += i7;
				j += k7;
				if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0)
					ai[k] = k8;
				k++;
				i += i7;
				j += k7;
				if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0)
					ai[k] = k8;
				k++;
				i += i7;
				j += k7;
				if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0)
					ai[k] = k8;
				k++;
				i += i7;
				j += k7;
				if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0)
					ai[k] = k8;
				k++;
				i = i4;
				j = k4;
				l1 += k2;
				i2 += l2;
				j2 += i3;
				int k5 = j2 >> 12;
				if (k5 != 0) {
					i4 = l1 / k5;
					k4 = i2 / k5;
					if (i4 < 7)
						i4 = 7;
					else if (i4 > 4032)
						i4 = 4032;
				}
				i7 = i4 - i >> 3;
				k7 = k4 - j >> 3;
				j1 += j3;
				i += (j1 & 0x600000) >> 3;
				i8 = j1 >> 23;
			}
			for (k3 = i1 - l & 7; k3-- > 0;) {
				int l8;
				if ((l8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0)
					ai[k] = l8;
				k++;
				i += i7;
				j += k7;
			}

			return;
		}
		int j4 = 0;
		int l4 = 0;
		int l6 = l - center_x;
		l1 += (k2 >> 3) * l6;
		i2 += (l2 >> 3) * l6;
		j2 += (i3 >> 3) * l6;
		int l5 = j2 >> 14;
		if (l5 != 0) {
			i = l1 / l5;
			j = i2 / l5;
			if (i < 0)
				i = 0;
			else if (i > 16256)
				i = 16256;
		}
		l1 += k2;
		i2 += l2;
		j2 += i3;
		l5 = j2 >> 14;
		if (l5 != 0) {
			j4 = l1 / l5;
			l4 = i2 / l5;
			if (j4 < 7)
				j4 = 7;
			else if (j4 > 16256)
				j4 = 16256;
		}
		int j7 = j4 - i >> 3;
		int l7 = l4 - j >> 3;
		i += j1 & 0x600000;
		int j8 = j1 >> 23;
		if (opaque) {
			while (k3-- > 0) {
				ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
				i += j7;
				j += l7;
				ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
				i += j7;
				j += l7;
				ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
				i += j7;
				j += l7;
				ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
				i += j7;
				j += l7;
				ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
				i += j7;
				j += l7;
				ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
				i += j7;
				j += l7;
				ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
				i += j7;
				j += l7;
				ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
				i = j4;
				j = l4;
				l1 += k2;
				i2 += l2;
				j2 += i3;
				int i6 = j2 >> 14;
				if (i6 != 0) {
					j4 = l1 / i6;
					l4 = i2 / i6;
					if (j4 < 7)
						j4 = 7;
					else if (j4 > 16256)
						j4 = 16256;
				}
				j7 = j4 - i >> 3;
				l7 = l4 - j >> 3;
				j1 += j3;
				i += j1 & 0x600000;
				j8 = j1 >> 23;
			}
			for (k3 = i1 - l & 7; k3-- > 0;) {
				ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
				i += j7;
				j += l7;
			}

			return;
		}
		while (k3-- > 0) {
			int i9;
			if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0)
				ai[k] = i9;
			k++;
			i += j7;
			j += l7;
			if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0)
				ai[k] = i9;
			k++;
			i += j7;
			j += l7;
			if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0)
				ai[k] = i9;
			k++;
			i += j7;
			j += l7;
			if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0)
				ai[k] = i9;
			k++;
			i += j7;
			j += l7;
			if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0)
				ai[k] = i9;
			k++;
			i += j7;
			j += l7;
			if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0)
				ai[k] = i9;
			k++;
			i += j7;
			j += l7;
			if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0)
				ai[k] = i9;
			k++;
			i += j7;
			j += l7;
			if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0)
				ai[k] = i9;
			k++;
			i = j4;
			j = l4;
			l1 += k2;
			i2 += l2;
			j2 += i3;
			int j6 = j2 >> 14;
			if (j6 != 0) {
				j4 = l1 / j6;
				l4 = i2 / j6;
				if (j4 < 7)
					j4 = 7;
				else if (j4 > 16256)
					j4 = 16256;
			}
			j7 = j4 - i >> 3;
			l7 = l4 - j >> 3;
			j1 += j3;
			i += j1 & 0x600000;
			j8 = j1 >> 23;
		}
		for (int l3 = i1 - l & 7; l3-- > 0;) {
			int j9;
			if ((j9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0)
				ai[k] = j9;
			k++;
			i += j7;
			j += l7;
		}
		} catch(Exception e) {}
	}

	private static int[] OFFSETS_512_334 = null;
	private static int[] OFFSETS_765_503 = null;

	public static int[] getOffsets(int j, int k) {
		if (j == 512 && k == 334 && OFFSETS_512_334 != null)
			return OFFSETS_512_334;

		if (j == 765 + 1 && k == 503 && OFFSETS_765_503 != null)
			return OFFSETS_765_503;

		int[] t = new int[k];
		for (int l = 0; l < k; l++)
			t[l] = j * l;

		if (j == 512 && k == 334)
			OFFSETS_512_334 = t;

		if (j == 765 + 1 && k == 503)
			OFFSETS_765_503 = t;

		return t;
	}

	public static final int anInt1459 = -477;
	public static boolean lowMem = true;
	static boolean restrict_edges;
	private static boolean opaque;
	public static boolean notTextured = true;
	public static int alpha;
	public static int center_x;
	public static int center_y;
	private static int[] shadowDecay;
	public static final int[] light_decay;
	public static int SINE[];
	public static int COSINE[];
	public static int lineOffsets[];
	public static int hsl2rgb[] = new int[0x10000];
	
	static {
		shadowDecay = new int[512];
		light_decay = new int[2048];
		SINE = new int[2048];
		COSINE = new int[2048];
		for (int i = 1; i < 512; i++)
			shadowDecay[i] = 32768 / i;

		for (int j = 1; j < 2048; j++)
			light_decay[j] = 0x10000 / j;

		for (int k = 0; k < 2048; k++) {
			SINE[k] = (int) (65536D * Math.sin((double) k * 0.0030679614999999999D));
			COSINE[k] = (int) (65536D * Math.cos((double) k * 0.0030679614999999999D));
		}

	}
	
	/**
	 * Texture triangle rendering..
	 * for hd_tex
	 */
	public static void render_texture_triangle(int y_a, int y_b, int y_c, int x_a, int x_b, int x_c, int grad_a, int grad_b, int grad_c,
	        int Px, int Mx, int Nx, int Pz, int My, int Nz, int Py, int Mz, int Ny, int t_id, int color, boolean force, boolean floor) {
		if(Client.getOption("smooth_shade"))// HD causing errors atm, so keeping LD
			drawLDTexturedTriangle(y_a,y_b, y_c, x_a, x_b,x_c, grad_a, grad_b, grad_c, Px,  Mx,Nx, Pz, My, Nz,Py, Mz, Ny, t_id, color, force, floor);
		else
			drawLDTexturedTriangle(y_a,y_b, y_c, x_a, x_b,x_c, grad_a, grad_b, grad_c, Px,  Mx,Nx, Pz, My, Nz,Py, Mz, Ny, t_id, color, force, floor);
	}
	
	
	public static int triangles = 0;

	/**
	 * LD Texture rendering method -- NO smooth shading.
	 */
	public static void drawLDTexturedTriangle(
	        int y_a, int y_b, int y_c, int x_a, int x_b, int x_c, int grad_a, int grad_b, int grad_c,
	        int Px, int Mx, int Nx, int Pz, int My, int Nz, int Py, int Mz, int Ny, int t_id, int color, boolean force, boolean floor) 
	    {
	        try {
	        if (t_id < 0 || t_id >= TextureDef.textures.length)
	        {
	            drawShadedTriangle(y_a, y_b, y_c, x_a, x_b, x_c, grad_a, grad_b, grad_c);
	            return;
	        }
	        TextureDef def = TextureDef.textures[t_id];
	        if (def == null)
	        {
	        	//System.out.println("Texture: " + t_id + " has null texture def.");
	            drawShadedTriangle(y_a, y_b, y_c, x_a, x_b, x_c, grad_a, grad_b, grad_c);
	            return;
	        }
	        
	        if (!force && !def.aBoolean1223 && lowMem)
	        {
	            drawShadedTriangle(y_a, y_b, y_c, x_a, x_b, x_c, grad_a, grad_b, grad_c);
	            return;
	        }
	        
			int texture[] = null;
			if(Client.getOption("hd_tex")) {
				texture = TextureLoader667.getTexturePixels(t_id);
			} else {
				texture = TextureLoader317.getTexturePixels(t_id);
			}
	        if (texture == null)
	        {
	        	//System.out.println("Texture: " + t_id + " has null texture pixels.");
	            drawShadedTriangle(y_a, y_b, y_c, x_a, x_b, x_c, grad_a, grad_b, grad_c);
	            return;
	        }
	        /*Texture textureDef = Texture.get(t_id);
	        if(textureDef != null)
	        	blendrgb(texture, 0, 0, textureDef.width, textureDef.height, texture.length);*/
	        if (color >= 0xffff)
	            color = -1;
	            
	        if (color >= 0 && color < 65535) {
	            color = hsl2rgb[color];
	        }
	        
	        Mx = Px - Mx;
	        My = Pz - My;
	        Mz = Py - Mz;
	        Nx -= Px;
	        Nz -= Pz;
	        Ny -= Py;
	        int Oa = Nx * Pz - Nz * Px << (Client.log_view_dist == 9 ? 14 : 15);
	        int Ha = Nz * Py - Ny * Pz << 8;
	        int Va = Ny * Px - Nx * Py << 5;
	        int Ob = Mx * Pz - My * Px << (Client.log_view_dist == 9 ? 14 : 15);
	        int Hb = My * Py - Mz * Pz << 8;
	        int Vb = Mz * Px - Mx * Py << 5;
	        int Oc = My * Nx - Mx * Nz << (Client.log_view_dist == 9 ? 14 : 15);
	        int Hc = Mz * Nz - My * Ny << 8;
	        int Vc = Mx * Ny - Mz * Nx << 5;
	        int x_a_off = 0;
	        int grad_a_off = 0;
	        if (y_b != y_a) {
	            x_a_off = (x_b - x_a << 16) / (y_b - y_a);
	            grad_a_off = (grad_b - grad_a << 16) / (y_b - y_a);
	        }
	        int x_b_off = 0;
	        int grad_b_off = 0;
	        if (y_c != y_b) {
	            x_b_off = (x_c - x_b << 16) / (y_c - y_b);
	            grad_b_off = (grad_c - grad_b << 16) / (y_c - y_b);
	        }
	        int x_c_off = 0;
	        int grad_c_off = 0;
	        if (y_c != y_a) {
	            x_c_off = (x_a - x_c << 16) / (y_a - y_c);
	            grad_c_off = (grad_a - grad_c << 16) / (y_a - y_c);
	        }
	        if (y_a <= y_b && y_a <= y_c) {
	            if (y_a >= DrawingArea.bottomY)
	                return;
	            if (y_b > DrawingArea.bottomY)
	                y_b = DrawingArea.bottomY;
	            if (y_c > DrawingArea.bottomY)
	                y_c = DrawingArea.bottomY;
	            if (y_b < y_c) {
	                x_c = x_a <<= 16;
	                grad_c = grad_a <<= 16;
	                if (y_a < 0) {
	                    x_c -= x_c_off * y_a;
	                    x_a -= x_a_off * y_a;
	                    grad_c -= grad_c_off * y_a;
	                    grad_a -= grad_a_off * y_a;
	                    y_a = 0;
	                }
	                x_b <<= 16;
	                grad_b <<= 16;
	                if (y_b < 0) {
	                    x_b -= x_b_off * y_b;
	                    grad_b -= grad_b_off * y_b;
	                    y_b = 0;
	                }
	                int jA = y_a - center_y;
	                Oa += Va * jA;
	                Ob += Vb * jA;
	                Oc += Vc * jA;
	                if (y_a != y_b && x_c_off < x_a_off || y_a == y_b && x_c_off > x_b_off) {
	                    y_c -= y_b;
	                    y_b -= y_a;
	                    y_a = lineOffsets[y_a];
	                    while (--y_b >= 0) {
	                        renderLDTexturedLine(pixels, texture, y_a, x_c >> 16, x_a >> 16,
	                                grad_c >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor);
	                        x_c += x_c_off;
	                        x_a += x_a_off;
	                        grad_c += grad_c_off;
	                        grad_a += grad_a_off;
	                        y_a += width;
	                        Oa += Va;
	                        Ob += Vb;
	                        Oc += Vc;
	                    }
	                    while (--y_c >= 0) {
	                        renderLDTexturedLine(pixels, texture, y_a, x_c >> 16,
	                                x_b >> 16, grad_c >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor);
	                        x_c += x_c_off;
	                        x_b += x_b_off;
	                        grad_c += grad_c_off;
	                        grad_b += grad_b_off;
	                        y_a += width;
	                        Oa += Va;
	                        Ob += Vb;
	                        Oc += Vc;
	                    }
	                    return;
	                }
	                y_c -= y_b;
	                y_b -= y_a;
	                y_a = lineOffsets[y_a];
	                while (--y_b >= 0) {
	                    renderLDTexturedLine(pixels, texture, y_a, x_a >> 16, x_c >> 16,
	                            grad_a >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor);
	                    x_c += x_c_off;
	                    x_a += x_a_off;
	                    grad_c += grad_c_off;
	                    grad_a += grad_a_off;
	                    y_a += width;
	                    Oa += Va;
	                    Ob += Vb;
	                    Oc += Vc;
	                }
	                while (--y_c >= 0) {
	                    renderLDTexturedLine(pixels, texture, y_a, x_b >> 16, x_c >> 16,
	                            grad_b >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor);
	                    x_c += x_c_off;
	                    x_b += x_b_off;
	                    grad_c += grad_c_off;
	                    grad_b += grad_b_off;
	                    y_a += width;
	                    Oa += Va;
	                    Ob += Vb;
	                    Oc += Vc;
	                }
	                return;
	            }
	            x_b = x_a <<= 16;
	            grad_b = grad_a <<= 16;
	            if (y_a < 0) {
	                x_b -= x_c_off * y_a;
	                x_a -= x_a_off * y_a;
	                grad_b -= grad_c_off * y_a;
	                grad_a -= grad_a_off * y_a;
	                y_a = 0;
	            }
	            x_c <<= 16;
	            grad_c <<= 16;
	            if (y_c < 0) {
	                x_c -= x_b_off * y_c;
	                grad_c -= grad_b_off * y_c;
	                y_c = 0;
	            }
	            int l8 = y_a - center_y;
	            Oa += Va * l8;
	            Ob += Vb * l8;
	            Oc += Vc * l8;
	            if (y_a != y_c && x_c_off < x_a_off || y_a == y_c && x_b_off > x_a_off) {
	                y_b -= y_c;
	                y_c -= y_a;
	                y_a = lineOffsets[y_a];
	                while (--y_c >= 0) {
	                    renderLDTexturedLine(pixels, texture, y_a, x_b >> 16, x_a >> 16,
	                            grad_b >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor);
	                    x_b += x_c_off;
	                    x_a += x_a_off;
	                    grad_b += grad_c_off;
	                    grad_a += grad_a_off;
	                    y_a += width;
	                    Oa += Va;
	                    Ob += Vb;
	                    Oc += Vc;
	                }
	                while (--y_b >= 0) {
	                    renderLDTexturedLine(pixels, texture, y_a, x_c >> 16, x_a >> 16,
	                            grad_c >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor);
	                    x_c += x_b_off;
	                    x_a += x_a_off;
	                    grad_c += grad_b_off;
	                    grad_a += grad_a_off;
	                    y_a += width;
	                    Oa += Va;
	                    Ob += Vb;
	                    Oc += Vc;
	                }
	                return;
	            }
	            y_b -= y_c;
	            y_c -= y_a;
	            y_a = lineOffsets[y_a];
	            while (--y_c >= 0) {
	                renderLDTexturedLine(pixels, texture, y_a, x_a >> 16, x_b >> 16,
	                        grad_a >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor);
	                x_b += x_c_off;
	                x_a += x_a_off;
	                grad_b += grad_c_off;
	                grad_a += grad_a_off;
	                y_a += width;
	                Oa += Va;
	                Ob += Vb;
	                Oc += Vc;
	            }
	            while (--y_b >= 0) {
	                renderLDTexturedLine(pixels, texture, y_a, x_a >> 16, x_c >> 16,
	                        grad_a >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor);
	                x_c += x_b_off;
	                x_a += x_a_off;
	                grad_c += grad_b_off;
	                grad_a += grad_a_off;
	                y_a += width;
	                Oa += Va;
	                Ob += Vb;
	                Oc += Vc;
	            }
	            return;
	        }
	        if (y_b <= y_c) {
	            if (y_b >= DrawingArea.bottomY)
	                return;
	            if (y_c > DrawingArea.bottomY)
	                y_c = DrawingArea.bottomY;
	            if (y_a > DrawingArea.bottomY)
	                y_a = DrawingArea.bottomY;
	            if (y_c < y_a) {
	                x_a = x_b <<= 16;
	                grad_a = grad_b <<= 16;
	                if (y_b < 0) {
	                    x_a -= x_a_off * y_b;
	                    x_b -= x_b_off * y_b;
	                    grad_a -= grad_a_off * y_b;
	                    grad_b -= grad_b_off * y_b;
	                    y_b = 0;
	                }
	                x_c <<= 16;
	                grad_c <<= 16;
	                if (y_c < 0) {
	                    x_c -= x_c_off * y_c;
	                    grad_c -= grad_c_off * y_c;
	                    y_c = 0;
	                }
	                int i9 = y_b - center_y;
	                Oa += Va * i9;
	                Ob += Vb * i9;
	                Oc += Vc * i9;
	                if (y_b != y_c && x_a_off < x_b_off || y_b == y_c && x_a_off > x_c_off) {
	                    y_a -= y_c;
	                    y_c -= y_b;
	                    y_b = lineOffsets[y_b];
	                    while (--y_c >= 0) {
	                        renderLDTexturedLine(pixels, texture, y_b, x_a >> 16, x_b >> 16,
	                                grad_a >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor);
	                        x_a += x_a_off;
	                        x_b += x_b_off;
	                        grad_a += grad_a_off;
	                        grad_b += grad_b_off;
	                        y_b += width;
	                        Oa += Va;
	                        Ob += Vb;
	                        Oc += Vc;
	                    }
	                    while (--y_a >= 0) {
	                        renderLDTexturedLine(pixels, texture, y_b, x_a >> 16, x_c >> 16,
	                                grad_a >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor);
	                        x_a += x_a_off;
	                        x_c += x_c_off;
	                        grad_a += grad_a_off;
	                        grad_c += grad_c_off;
	                        y_b += width;
	                        Oa += Va;
	                        Ob += Vb;
	                        Oc += Vc;
	                    }
	                    return;
	                }
	                y_a -= y_c;
	                y_c -= y_b;
	                y_b = lineOffsets[y_b];
	                while (--y_c >= 0) {
	                    renderLDTexturedLine(pixels, texture, y_b, x_b >> 16, x_a >> 16,
	                            grad_b >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor);
	                    x_a += x_a_off;
	                    x_b += x_b_off;
	                    grad_a += grad_a_off;
	                    grad_b += grad_b_off;
	                    y_b += width;
	                    Oa += Va;
	                    Ob += Vb;
	                    Oc += Vc;
	                }
	                while (--y_a >= 0) {
	                    renderLDTexturedLine(pixels, texture, y_b, x_c >> 16, x_a >> 16,
	                            grad_c >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor);
	                    x_a += x_a_off;
	                    x_c += x_c_off;
	                    grad_a += grad_a_off;
	                    grad_c += grad_c_off;
	                    y_b += width;
	                    Oa += Va;
	                    Ob += Vb;
	                    Oc += Vc;
	                }
	                return;
	            }
	            x_c = x_b <<= 16;
	            grad_c = grad_b <<= 16;
	            if (y_b < 0) {
	                x_c -= x_a_off * y_b;
	                x_b -= x_b_off * y_b;
	                grad_c -= grad_a_off * y_b;
	                grad_b -= grad_b_off * y_b;
	                y_b = 0;
	            }
	            x_a <<= 16;
	            grad_a <<= 16;
	            if (y_a < 0) {
	                x_a -= x_c_off * y_a;
	                grad_a -= grad_c_off * y_a;
	                y_a = 0;
	            }
	            int j9 = y_b - center_y;
	            Oa += Va * j9;
	            Ob += Vb * j9;
	            Oc += Vc * j9;
	            if (x_a_off < x_b_off) {
	                y_c -= y_a;
	                y_a -= y_b;
	                y_b = lineOffsets[y_b];
	                while (--y_a >= 0) {
	                    renderLDTexturedLine(pixels, texture, y_b, x_c >> 16, x_b >> 16,
	                            grad_c >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor);
	                    x_c += x_a_off;
	                    x_b += x_b_off;
	                    grad_c += grad_a_off;
	                    grad_b += grad_b_off;
	                    y_b += width;
	                    Oa += Va;
	                    Ob += Vb;
	                    Oc += Vc;
	                }
	                while (--y_c >= 0) {
	                    renderLDTexturedLine(pixels, texture, y_b, x_a >> 16, x_b >> 16,
	                            grad_a >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor);
	                    x_a += x_c_off;
	                    x_b += x_b_off;
	                    grad_a += grad_c_off;
	                    grad_b += grad_b_off;
	                    y_b += width;
	                    Oa += Va;
	                    Ob += Vb;
	                    Oc += Vc;
	                }
	                return;
	            }
	            y_c -= y_a;
	            y_a -= y_b;
	            y_b = lineOffsets[y_b];
	            while (--y_a >= 0) {
	                renderLDTexturedLine(pixels, texture, y_b, x_b >> 16, x_c >> 16,
	                        grad_b >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor);
	                x_c += x_a_off;
	                x_b += x_b_off;
	                grad_c += grad_a_off;
	                grad_b += grad_b_off;
	                y_b += width;
	                Oa += Va;
	                Ob += Vb;
	                Oc += Vc;
	            }
	            while (--y_c >= 0) {
	                renderLDTexturedLine(pixels, texture, y_b, x_b >> 16, x_a >> 16,
	                        grad_b >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor);
	                x_a += x_c_off;
	                x_b += x_b_off;
	                grad_a += grad_c_off;
	                grad_b += grad_b_off;
	                y_b += width;
	                Oa += Va;
	                Ob += Vb;
	                Oc += Vc;
	            }
	            return;
	        }
	        if (y_c >= DrawingArea.bottomY)
	            return;
	        if (y_a > DrawingArea.bottomY)
	            y_a = DrawingArea.bottomY;
	        if (y_b > DrawingArea.bottomY)
	            y_b = DrawingArea.bottomY;
	        if (y_a < y_b) {
	            x_b = x_c <<= 16;
	            grad_b = grad_c <<= 16;
	            if (y_c < 0) {
	                x_b -= x_b_off * y_c;
	                x_c -= x_c_off * y_c;
	                grad_b -= grad_b_off * y_c;
	                grad_c -= grad_c_off * y_c;
	                y_c = 0;
	            }
	            x_a <<= 16;
	            grad_a <<= 16;
	            if (y_a < 0) {
	                x_a -= x_a_off * y_a;
	                grad_a -= grad_a_off * y_a;
	                y_a = 0;
	            }
	            int k9 = y_c - center_y;
	            Oa += Va * k9;
	            Ob += Vb * k9;
	            Oc += Vc * k9;
	            if (x_b_off < x_c_off) {
	                y_b -= y_a;
	                y_a -= y_c;
	                y_c = lineOffsets[y_c];
	                while (--y_a >= 0) {
	                    renderLDTexturedLine(pixels, texture, y_c, x_b >> 16, x_c >> 16,
	                            grad_b >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor);
	                    x_b += x_b_off;
	                    x_c += x_c_off;
	                    grad_b += grad_b_off;
	                    grad_c += grad_c_off;
	                    y_c += width;
	                    Oa += Va;
	                    Ob += Vb;
	                    Oc += Vc;
	                }
	                while (--y_b >= 0) {
	                    renderLDTexturedLine(pixels, texture, y_c, x_b >> 16, x_a >> 16,
	                            grad_b >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor);
	                    x_b += x_b_off;
	                    x_a += x_a_off;
	                    grad_b += grad_b_off;
	                    grad_a += grad_a_off;
	                    y_c += width;
	                    Oa += Va;
	                    Ob += Vb;
	                    Oc += Vc;
	                }
	                return;
	            }
	            y_b -= y_a;
	            y_a -= y_c;
	            y_c = lineOffsets[y_c];
	            while (--y_a >= 0) {
	                renderLDTexturedLine(pixels, texture, y_c, x_c >> 16, x_b >> 16,
	                        grad_c >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor);
	                x_b += x_b_off;
	                x_c += x_c_off;
	                grad_b += grad_b_off;
	                grad_c += grad_c_off;
	                y_c += width;
	                Oa += Va;
	                Ob += Vb;
	                Oc += Vc;
	            }
	            while (--y_b >= 0) {
	                renderLDTexturedLine(pixels, texture, y_c, x_a >> 16, x_b >> 16,
	                        grad_a >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor);
	                x_b += x_b_off;
	                x_a += x_a_off;
	                grad_b += grad_b_off;
	                grad_a += grad_a_off;
	                y_c += width;
	                Oa += Va;
	                Ob += Vb;
	                Oc += Vc;
	            }
	            return;
	        }
	        x_a = x_c <<= 16;
	        grad_a = grad_c <<= 16;
	        if (y_c < 0) {
	            x_a -= x_b_off * y_c;
	            x_c -= x_c_off * y_c;
	            grad_a -= grad_b_off * y_c;
	            grad_c -= grad_c_off * y_c;
	            y_c = 0;
	        }
	        x_b <<= 16;
	        grad_b <<= 16;
	        if (y_b < 0) {
	            x_b -= x_a_off * y_b;
	            grad_b -= grad_a_off * y_b;
	            y_b = 0;
	        }
	        int l9 = y_c - center_y;
	        Oa += Va * l9;
	        Ob += Vb * l9;
	        Oc += Vc * l9;
	        if (x_b_off < x_c_off) {
	            y_a -= y_b;
	            y_b -= y_c;
	            y_c = lineOffsets[y_c];
	            while (--y_b >= 0) {
	                renderLDTexturedLine(pixels, texture, y_c, x_a >> 16, x_c >> 16,
	                        grad_a >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor);
	                x_a += x_b_off;
	                x_c += x_c_off;
	                grad_a += grad_b_off;
	                grad_c += grad_c_off;
	                y_c += width;
	                Oa += Va;
	                Ob += Vb;
	                Oc += Vc;
	            }
	            while (--y_a >= 0) {
	                renderLDTexturedLine(pixels, texture, y_c, x_b >> 16, x_c >> 16,
	                        grad_b >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor);
	                x_b += x_a_off;
	                x_c += x_c_off;
	                grad_b += grad_a_off;
	                grad_c += grad_c_off;
	                y_c += width;
	                Oa += Va;
	                Ob += Vb;
	                Oc += Vc;
	            }
	            return;
	        }
	        y_a -= y_b;
	        y_b -= y_c;
	        y_c = lineOffsets[y_c];
	        while (--y_b >= 0) {
	            renderLDTexturedLine(pixels, texture, y_c, x_c >> 16, x_a >> 16, grad_c >> 8,
	                    grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor);
	            x_a += x_b_off;
	            x_c += x_c_off;
	            grad_a += grad_b_off;
	            grad_c += grad_c_off;
	            y_c += width;
	            Oa += Va;
	            Ob += Vb;
	            Oc += Vc;
	        }
	        while (--y_a >= 0) {
	            renderLDTexturedLine(pixels, texture, y_c, x_c >> 16, x_b >> 16, grad_c >> 8,
	                    grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force, floor);
	            x_b += x_a_off;
	            x_c += x_c_off;
	            grad_b += grad_a_off;
	            grad_c += grad_c_off;
	            y_c += width;
	            Oa += Va;
	            Ob += Vb;
	            Oc += Vc;
	        }
	        return;
	        } catch(Exception e) { e.printStackTrace();}
	    }
	
	/**
	 * LD Texture, NON smooth shading scanline method.
	 */
	  private static void renderLDTexturedLine(int dest[], int texture[], int dest_off, int start_x, int end_x, int shadeValue, int gradient,
		        int arg7, int arg8, int arg9, int arg10, int arg11, int arg12, int color, boolean force, boolean floor) 
		    {
		        int rgb = 0;
		        int loops = 0;
		        if (start_x >= end_x) 
		        {
		            return;
		        }
		        int j3;
		        int k3;
		        if (restrict_edges) {
		            j3 = (gradient - shadeValue) / (end_x - start_x);
		            if (end_x > DrawingArea.viewportRX) {
		                end_x = DrawingArea.viewportRX;
		            }
		            if (start_x < 0) {
		                shadeValue -= start_x * j3;
		                start_x = 0;
		            }
		            if (start_x >= end_x) {
		                return;
		            }
		            k3 = end_x - start_x >> 3;
		            j3 <<= 12;
		            shadeValue <<= 9;
		        } else {
		            if (end_x - start_x > 7) {
		                k3 = end_x - start_x >> 3;
		                j3 = (gradient - shadeValue) * shadowDecay[k3] >> 6;
		            } else {
		                k3 = 0;
		                j3 = 0;
		            }
		            shadeValue <<= 9;
		        }
		        dest_off += start_x;
		        int j4 = 0;
		        int l4 = 0;
		        int l6 = start_x - center_x;
		        arg7 += (arg10 >> 3) * l6;
		        arg8 += (arg11 >> 3) * l6;
		        arg9 += (arg12 >> 3) * l6;
		        int l5 = arg9 >> 14;
		        if (l5 != 0) {
		            rgb = arg7 / l5;
		            loops = arg8 / l5;
		            if (rgb < 0) {
		                rgb = 0;
		            } else if (rgb > 16256) {
		                rgb = 16256;
		            }
		        }
		        arg7 += arg10;
		        arg8 += arg11;
		        arg9 += arg12;
		        l5 = arg9 >> 14;
		        if (l5 != 0) {
		            j4 = arg7 / l5;
		            l4 = arg8 / l5;
		            if (j4 < 7) {
		                j4 = 7;
		            } else if (j4 > 16256) {
		                j4 = 16256;
		            }
		        }
		        int j7 = j4 - rgb >> 3;
		        int l7 = l4 - loops >> 3;
		        rgb += shadeValue & 0x600000;
		        int j8 = shadeValue >> 23;
		        int glb_alpha = alpha;
		        if (glb_alpha < 0 || glb_alpha > 0xff)
		            glb_alpha = 0;


		        glb_alpha = 0xff - glb_alpha;
		        int src;
		        int src_alpha;
		        int src_delta;
		        int dst;
		        while (k3-- > 0)
		        {
		            for (int i = 0; i != 8; ++i)
		            {
		                src = texture[(loops & 0x3f80) + (rgb >> 7)];
		                src_alpha = src >>> 24;
		                if(floor) 
		                {
		                    int dest_alpha = 0xff - src_alpha;
		                    int ii;
		                    if(src_alpha != 0xff && color >= 0)
		                    {
		                        if(src_alpha == 0)
		                            src = color;
		                        else
		                        {
		                            src = ((0xff00ff00 & (0xff00ff & src) * src_alpha 
		                                | 0xff0000 & (src & 0xff00) * src_alpha) >>> 7) + (((0xff0000 & dest_alpha * (color & 0xff00) //shift 7 was 8,
		                                | dest_alpha * (color & 0xff00ff) & 0xff00ff00) >>> 8));
		                        }
		                        src_alpha = 0xff;
		                    }
		                    if(src_alpha != 0)
		                    {
		                        if(src_alpha == 0xff)
		                            dest[dest_off] = (src & 0xfffffff);
		                        else
		                        {
		                            dest[dest_off] = ((0xff00ff00 & (0xff00ff & src) * src_alpha 
		                                | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & dest_alpha * (dest[dest_off] & 0xff00) 
		                                | dest_alpha * (dest[dest_off] & 0xff00ff) & 0xff00ff00) >>> 8));
		                        }
		                    }
		                } else {
		                    
		                    if (src_alpha != 0 || force)
		                    {
		                        if (src_alpha != 0xff && color >= 0)
		                        {
		                            
		                            if (src_alpha == 0)
		                                src = color;
		                            else
		                            {
		                                src_delta = 0xff - src_alpha;
		                                src = ((0xff00ff00 & (0xff00ff & src) * src_alpha 
		                                    | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) 
		                                    | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
		                            }
		                            src_alpha = 0xff;//
		                        }
		                        if (glb_alpha != 0xff)
		                            src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;


		                        if (src_alpha != 0)
		                        {
		                            if (src_alpha == 0xff)
		                                dest[dest_off] = (src & 0xffffff);


		                            else
		                            {
		                                dst = dest[dest_off];
		                                src_delta = 0xff - src_alpha;
		                                dest[dest_off] = ((0xff00ff00 & (0xff00ff & src) * src_alpha 
		                                    | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) 
		                                    | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8));
		                            }
		                        }
		                    } else {
		                        /*src = texture[(loops & 0x3f80) + (rgb >> 7)];
		                        src_alpha = src >>> 24;
		                        if((src >>> j8) == 0) {
		                            src = color;
		                        }
		                        dest[dest_off] = src;*///Test
		                    }
		                }
		                dest_off++;
		                rgb += j7;
		                loops += l7;


		            }


		            rgb = j4;
		            loops = l4;
		            arg7 += arg10;
		            arg8 += arg11;
		            arg9 += arg12;
		            int i6 = arg9 >> 14;
		            if (i6 != 0) {
		                j4 = arg7 / i6;
		                l4 = arg8 / i6;
		                if (j4 < 7) {
		                    j4 = 7;
		                } else if (j4 > 16256) {
		                    j4 = 16256;
		                }
		            }
		            j7 = j4 - rgb >> 3;
		            l7 = l4 - loops >> 3;
		            shadeValue += j3;
		            rgb += shadeValue & 0x600000;
		            j8 = shadeValue >> 23;
		        }
		        
		        for (k3 = end_x - start_x & 7; k3-- > 0;) {
		            src = texture[(loops & 0x3f80) + (rgb >> 7)];
		            src_alpha = src >>> 24;
		            if(floor) 
		            {
		                int dest_alpha = 0xff - src_alpha;
		                int ii;
		                if(src_alpha != 0xff && color >= 0)
		                {
		                    if(src_alpha == 0)
		                        src = color;
		                    else
		                        src = ((0xff00ff00 & (0xff00ff & src) * src_alpha 
		                                | 0xff0000 & (src & 0xff00) * src_alpha) >>> 7) + (((0xff0000 & dest_alpha * (color & 0xff00) //shift 7 was 8,
		                                | dest_alpha * (color & 0xff00ff) & 0xff00ff00) >>> 8));
		                    src_alpha = 0xff;
		                }
		                if(src_alpha != 0)
		                {
		                    if(src_alpha == 0xff)
		                        dest[dest_off] = (src & 0xfffffff);
		                    else
		                    {
		                        dest[dest_off] = ((0xff00ff00 & (0xff00ff & src) * src_alpha 
		                            | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & dest_alpha * (dest[dest_off] & 0xff00) 
		                            | dest_alpha * (dest[dest_off] & 0xff00ff) & 0xff00ff00) >>> 8));
		                    }
		                }
		            } else {
		                if (src_alpha != 0 || force)
		                {
		                    if (src_alpha != 0xff && color >= 0)
		                    {
		                        if (src_alpha == 0) {
		                            src = color;
		                        } else
		                        {
		                            src_delta = 0xff - src_alpha;
		                            src = ((0xff00ff00 & (0xff00ff & src) * src_alpha 
		                            | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00)
		                            | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
		                        }
		                        src_alpha = 0xff;
		                    } 
		                    if (glb_alpha != 0xff)
		                        src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;


		                    if (src_alpha != 0)
		                    {
		                        if (src_alpha == 0xff) {
		                            dest[dest_off] = (src & 0xffffff);


		                        } else
		                        {
		                            dst = dest[dest_off];
		                            src_delta = 0xff - src_alpha;
		                            dest[dest_off] = ((0xff00ff00 & (0xff00ff & src) * src_alpha 
		                                | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) 
		                                | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8));
		                        }
		                    }
		                    
		                } else {
		                    /*src = texture[(loops & 0x3f80) + (rgb >> 7)];
		                    src_alpha = src >>> 24;
		                    if((src >>> j8) == 0) {
		                        src = color;
		                    }
		                    dest[dest_off] = src;*///Test
		                }
		            }
		            dest_off++;
		            rgb += j7;
		            loops += l7;
		        }
	    }
		
		/**
		 *	HD Textured Triangle, attempt at smooth shading.
		 **/
		public static void drawHDTexturedTriangle(int y1, int y2, int y3, int x1, int x2, int x3, int l1, int l2,
                int l3, int tx1, int tx2, int tx3, int ty1, int ty2, int ty3,
                int tz1, int tz2, int tz3, int tex, int color, boolean force, boolean floor)
		{
			if (tex < 0 || tex >= TextureDef.textures.length)
	        {
	            drawShadedTriangle(y1, y2, y3, x1, x2, x3, l1, l2, l3);
	            return;
	        }
	        TextureDef def = TextureDef.textures[tex];
	        if (def == null)
	        {
	        	//System.out.println("Texture: " + t_id + " has null texture def.");
	            drawShadedTriangle(y1, y2, y3, x1, x2, x3, l1, l2, l3);
	            return;
	        }
	        
	        if (!force && !def.aBoolean1223 && lowMem)
	        {
	            drawShadedTriangle(y1, y2, y3, x1, x2, x3, l1, l2, l3);
	            return;
	        }
	        
			int ai[] = null;
			if(Client.getOption("hd_tex")) {
				ai = TextureLoader667.getTexturePixels(tex);
			} else {
				ai = TextureLoader317.getTexturePixels(tex);
			}
	        if (ai == null)
	        {
	        	//System.out.println("Texture: " + t_id + " has null texture pixels.");
	            drawShadedTriangle(y1, y2, y3, x1, x2, x3, l1, l2, l3);
	            return;
	        }
	        /*Texture textureDef = Texture.get(t_id);
	        if(textureDef != null)
	        	blendrgb(texture, 0, 0, textureDef.width, textureDef.height, texture.length);*/
	        if (color >= 0xffff)
	            color = -1;
	            
	        if (color >= 0 && color < 65535) {
	            color = hsl2rgb[color];
	        }
        l1 = 0x7f - l1 << 1;
        l2 = 0x7f - l2 << 1;
        l3 = 0x7f - l3 << 1;
        if(!Client.getOption("hd_tex"))
			opaque = !TextureLoader317.textureIsTransparent[tex];
        tx2 = tx1 - tx2;
        ty2 = ty1 - ty2;
        tz2 = tz1 - tz2;
        tx3 -= tx1;
        ty3 -= ty1;
        tz3 -= tz1;
        int l4 = tx3 * ty1 - ty3 * tx1 << 14;
        int i5 = ty3 * tz1 - tz3 * ty1 << 8;
        int j5 = tz3 * tx1 - tx3 * tz1 << 5;
        int k5 = tx2 * ty1 - ty2 * tx1 << 14;
        int l5 = ty2 * tz1 - tz2 * ty1 << 8;
        int i6 = tz2 * tx1 - tx2 * tz1 << 5;
        int j6 = ty2 * tx3 - tx2 * ty3 << 14;
        int k6 = tz2 * ty3 - ty2 * tz3 << 8;
        int l6 = tx2 * tz3 - tz2 * tx3 << 5;
        int i7 = 0;
        int j7 = 0;
        if(y2 != y1)
        {
                i7 = (x2 - x1 << 16) / (y2 - y1);
                j7 = (l2 - l1 << 16) / (y2 - y1);
        }
        int k7 = 0;
        int l7 = 0;
        if(y3 != y2)
        {
                k7 = (x3 - x2 << 16) / (y3 - y2);
                l7 = (l3 - l2 << 16) / (y3 - y2);
        }
        int i8 = 0;
        int j8 = 0;
        if(y3 != y1)
        {
                i8 = (x1 - x3 << 16) / (y1 - y3);
                j8 = (l1 - l3 << 16) / (y1 - y3);
        }
        if(y1 <= y2 && y1 <= y3)
        {
                if(y1 >= DrawingArea.bottomY)
                        return;
                if(y2 > DrawingArea.bottomY)
                        y2 = DrawingArea.bottomY;
                if(y3 > DrawingArea.bottomY)
                        y3 = DrawingArea.bottomY;
                if(y2 < y3)
                {
                        x3 = x1 <<= 16;
                        l3 = l1 <<= 16;
                        if(y1 < 0)
                        {
                                x3 -= i8 * y1;
                                x1 -= i7 * y1;
                                l3 -= j8 * y1;
                                l1 -= j7 * y1;
                                y1 = 0;
                        }
                        x2 <<= 16;
                        l2 <<= 16;
                        if(y2 < 0)
                        {
                                x2 -= k7 * y2;
                                l2 -= l7 * y2;
                                y2 = 0;
                        }
                        int k8 = y1 - center_y;
                        l4 += j5 * k8;
                        k5 += i6 * k8;
                        j6 += l6 * k8;
                        if(y1 != y2 && i8 < i7 || y1 == y2 && i8 > k7)
                        {
                                y3 -= y2;
                                y2 -= y1;
                                y1 = lineOffsets[y1];
                                while(--y2 >= 0)
                                {
                                        drawHDTexturedScanline(DrawingArea.pixels, ai, y1, x3 >> 16, x1 >> 16, l3, l1, l4, k5, j6, i5, l5, k6);
                                        x3 += i8;
                                        x1 += i7;
                                        l3 += j8;
                                        l1 += j7;
                                        y1 += DrawingArea.width;
                                        l4 += j5;
                                        k5 += i6;
                                        j6 += l6;
                                }
                                while(--y3 >= 0)
                                {
                                        drawHDTexturedScanline(DrawingArea.pixels, ai, y1, x3 >> 16, x2 >> 16, l3, l2, l4, k5, j6, i5, l5, k6);
                                        x3 += i8;
                                        x2 += k7;
                                        l3 += j8;
                                        l2 += l7;
                                        y1 += DrawingArea.width;
                                        l4 += j5;
                                        k5 += i6;
                                        j6 += l6;
                                }
                                return;
                        }
                        y3 -= y2;
                        y2 -= y1;
                        y1 = lineOffsets[y1];
                        while(--y2 >= 0)
                        {
                                drawHDTexturedScanline(DrawingArea.pixels, ai, y1, x1 >> 16, x3 >> 16, l1, l3, l4, k5, j6, i5, l5, k6);
                                x3 += i8;
                                x1 += i7;
                                l3 += j8;
                                l1 += j7;
                                y1 += DrawingArea.width;
                                l4 += j5;
                                k5 += i6;
                                j6 += l6;
                        }
                        while(--y3 >= 0)
                        {
                                drawHDTexturedScanline(DrawingArea.pixels, ai, y1, x2 >> 16, x3 >> 16, l2, l3, l4, k5, j6, i5, l5, k6);
                                x3 += i8;
                                x2 += k7;
                                l3 += j8;
                                l2 += l7;
                                y1 += DrawingArea.width;
                                l4 += j5;
                                k5 += i6;
                                j6 += l6;
                        }
                        return;
                }
                x2 = x1 <<= 16;
                l2 = l1 <<= 16;
                if(y1 < 0)
                {
                        x2 -= i8 * y1;
                        x1 -= i7 * y1;
                        l2 -= j8 * y1;
                        l1 -= j7 * y1;
                        y1 = 0;
                }
                x3 <<= 16;
                l3 <<= 16;
                if(y3 < 0)
                {
                        x3 -= k7 * y3;
                        l3 -= l7 * y3;
                        y3 = 0;
                }
                int l8 = y1 - center_y;
                l4 += j5 * l8;
                k5 += i6 * l8;
                j6 += l6 * l8;
                if(y1 != y3 && i8 < i7 || y1 == y3 && k7 > i7)
                {
                        y2 -= y3;
                        y3 -= y1;
                        y1 = lineOffsets[y1];
                        while(--y3 >= 0)
                        {
                                drawHDTexturedScanline(DrawingArea.pixels, ai, y1, x2 >> 16, x1 >> 16, l2, l1, l4, k5, j6, i5, l5, k6);
                                x2 += i8;
                                x1 += i7;
                                l2 += j8;
                                l1 += j7;
                                y1 += DrawingArea.width;
                                l4 += j5;
                                k5 += i6;
                                j6 += l6;
                        }
                        while(--y2 >= 0)
                        {
                                drawHDTexturedScanline(DrawingArea.pixels, ai, y1, x3 >> 16, x1 >> 16, l3, l1, l4, k5, j6, i5, l5, k6);
                                x3 += k7;
                                x1 += i7;
                                l3 += l7;
                                l1 += j7;
                                y1 += DrawingArea.width;
                                l4 += j5;
                                k5 += i6;
                                j6 += l6;
                        }
                        return;
                }
                y2 -= y3;
                y3 -= y1;
                y1 = lineOffsets[y1];
                while(--y3 >= 0)
                {
                        drawHDTexturedScanline(DrawingArea.pixels, ai, y1, x1 >> 16, x2 >> 16, l1, l2, l4, k5, j6, i5, l5, k6);
                        x2 += i8;
                        x1 += i7;
                        l2 += j8;
                        l1 += j7;
                        y1 += DrawingArea.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                }
                while(--y2 >= 0)
                {
                        drawHDTexturedScanline(DrawingArea.pixels, ai, y1, x1 >> 16, x3 >> 16, l1, l3, l4, k5, j6, i5, l5, k6);
                        x3 += k7;
                        x1 += i7;
                        l3 += l7;
                        l1 += j7;
                        y1 += DrawingArea.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                }
                return;
        }
        if(y2 <= y3)
        {
                if(y2 >= DrawingArea.bottomY)
                        return;
                if(y3 > DrawingArea.bottomY)
                        y3 = DrawingArea.bottomY;
                if(y1 > DrawingArea.bottomY)
                        y1 = DrawingArea.bottomY;
                if(y3 < y1)
                {
                        x1 = x2 <<= 16;
                        l1 = l2 <<= 16;
                        if(y2 < 0)
                        {
                                x1 -= i7 * y2;
                                x2 -= k7 * y2;
                                l1 -= j7 * y2;
                                l2 -= l7 * y2;
                                y2 = 0;
                        }
                        x3 <<= 16;
                        l3 <<= 16;
                        if(y3 < 0)
                        {
                                x3 -= i8 * y3;
                                l3 -= j8 * y3;
                                y3 = 0;
                        }
                        int i9 = y2 - center_y;
                        l4 += j5 * i9;
                        k5 += i6 * i9;
                        j6 += l6 * i9;
                        if(y2 != y3 && i7 < k7 || y2 == y3 && i7 > i8)
                        {
                                y1 -= y3;
                                y3 -= y2;
                                y2 = lineOffsets[y2];
                                while(--y3 >= 0)
                                {
                                        drawHDTexturedScanline(DrawingArea.pixels, ai, y2, x1 >> 16, x2 >> 16, l1, l2, l4, k5, j6, i5, l5, k6);
                                        x1 += i7;
                                        x2 += k7;
                                        l1 += j7;
                                        l2 += l7;
                                        y2 += DrawingArea.width;
                                        l4 += j5;
                                        k5 += i6;
                                        j6 += l6;
                                }
                                while(--y1 >= 0)
                                {
                                        drawHDTexturedScanline(DrawingArea.pixels, ai, y2, x1 >> 16, x3 >> 16, l1, l3, l4, k5, j6, i5, l5, k6);
                                        x1 += i7;
                                        x3 += i8;
                                        l1 += j7;
                                        l3 += j8;
                                        y2 += DrawingArea.width;
                                        l4 += j5;
                                        k5 += i6;
                                        j6 += l6;
                                }
                                return;
                        }
                        y1 -= y3;
                        y3 -= y2;
                        y2 = lineOffsets[y2];
                        while(--y3 >= 0)
                        {
                                drawHDTexturedScanline(DrawingArea.pixels, ai, y2, x2 >> 16, x1 >> 16, l2, l1, l4, k5, j6, i5, l5, k6);
                                x1 += i7;
                                x2 += k7;
                                l1 += j7;
                                l2 += l7;
                                y2 += DrawingArea.width;
                                l4 += j5;
                                k5 += i6;
                                j6 += l6;
                        }
                        while(--y1 >= 0)
                        {
                                drawHDTexturedScanline(DrawingArea.pixels, ai, y2, x3 >> 16, x1 >> 16, l3, l1, l4, k5, j6, i5, l5, k6);
                                x1 += i7;
                                x3 += i8;
                                l1 += j7;
                                l3 += j8;
                                y2 += DrawingArea.width;
                                l4 += j5;
                                k5 += i6;
                                j6 += l6;
                        }
                        return;
                }
                x3 = x2 <<= 16;
                l3 = l2 <<= 16;
                if(y2 < 0)
                {
                        x3 -= i7 * y2;
                        x2 -= k7 * y2;
                        l3 -= j7 * y2;
                        l2 -= l7 * y2;
                        y2 = 0;
                }
                x1 <<= 16;
                l1 <<= 16;
                if(y1 < 0)
                {
                        x1 -= i8 * y1;
                        l1 -= j8 * y1;
                        y1 = 0;
                }
                int j9 = y2 - center_y;
                l4 += j5 * j9;
                k5 += i6 * j9;
                j6 += l6 * j9;
                if(i7 < k7)
                {
                        y3 -= y1;
                        y1 -= y2;
                        y2 = lineOffsets[y2];
                        while(--y1 >= 0)
                        {
                                drawHDTexturedScanline(DrawingArea.pixels, ai, y2, x3 >> 16, x2 >> 16, l3, l2, l4, k5, j6, i5, l5, k6);
                                x3 += i7;
                                x2 += k7;
                                l3 += j7;
                                l2 += l7;
                                y2 += DrawingArea.width;
                                l4 += j5;
                                k5 += i6;
                                j6 += l6;
                        }
                        while(--y3 >= 0)
                        {
                                drawHDTexturedScanline(DrawingArea.pixels, ai, y2, x1 >> 16, x2 >> 16, l1, l2, l4, k5, j6, i5, l5, k6);
                                x1 += i8;
                                x2 += k7;
                                l1 += j8;
                                l2 += l7;
                                y2 += DrawingArea.width;
                                l4 += j5;
                                k5 += i6;
                                j6 += l6;
                        }
                        return;
                }
                y3 -= y1;
                y1 -= y2;
                y2 = lineOffsets[y2];
                while(--y1 >= 0)
                {
                        drawHDTexturedScanline(DrawingArea.pixels, ai, y2, x2 >> 16, x3 >> 16, l2, l3, l4, k5, j6, i5, l5, k6);
                        x3 += i7;
                        x2 += k7;
                        l3 += j7;
                        l2 += l7;
                        y2 += DrawingArea.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                }
                while(--y3 >= 0)
                {
                        drawHDTexturedScanline(DrawingArea.pixels, ai, y2, x2 >> 16, x1 >> 16, l2, l1, l4, k5, j6, i5, l5, k6);
                        x1 += i8;
                        x2 += k7;
                        l1 += j8;
                        l2 += l7;
                        y2 += DrawingArea.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                }
                return;
        }
        if(y3 >= DrawingArea.bottomY)
                return;
        if(y1 > DrawingArea.bottomY)
                y1 = DrawingArea.bottomY;
        if(y2 > DrawingArea.bottomY)
                y2 = DrawingArea.bottomY;
        if(y1 < y2)
        {
                x2 = x3 <<= 16;
                l2 = l3 <<= 16;
                if(y3 < 0)
                {
                        x2 -= k7 * y3;
                        x3 -= i8 * y3;
                        l2 -= l7 * y3;
                        l3 -= j8 * y3;
                        y3 = 0;
                }
                x1 <<= 16;
                l1 <<= 16;
                if(y1 < 0)
                {
                        x1 -= i7 * y1;
                        l1 -= j7 * y1;
                        y1 = 0;
                }
                int k9 = y3 - center_y;
                l4 += j5 * k9;
                k5 += i6 * k9;
                j6 += l6 * k9;
                if(k7 < i8)
                {
                        y2 -= y1;
                        y1 -= y3;
                        y3 = lineOffsets[y3];
                        while(--y1 >= 0)
                        {
                                drawHDTexturedScanline(DrawingArea.pixels, ai, y3, x2 >> 16, x3 >> 16, l2, l3, l4, k5, j6, i5, l5, k6);
                                x2 += k7;
                                x3 += i8;
                                l2 += l7;
                                l3 += j8;
                                y3 += DrawingArea.width;
                                l4 += j5;
                                k5 += i6;
                                j6 += l6;
                        }
                        while(--y2 >= 0)
                        {
                                drawHDTexturedScanline(DrawingArea.pixels, ai, y3, x2 >> 16, x1 >> 16, l2, l1, l4, k5, j6, i5, l5, k6);
                                x2 += k7;
                                x1 += i7;
                                l2 += l7;
                                l1 += j7;
                                y3 += DrawingArea.width;
                                l4 += j5;
                                k5 += i6;
                                j6 += l6;
                        }
                        return;
                }
                y2 -= y1;
                y1 -= y3;
                y3 = lineOffsets[y3];
                while(--y1 >= 0)
                {
                        drawHDTexturedScanline(DrawingArea.pixels, ai, y3, x3 >> 16, x2 >> 16, l3, l2, l4, k5, j6, i5, l5, k6);
                        x2 += k7;
                        x3 += i8;
                        l2 += l7;
                        l3 += j8;
                        y3 += DrawingArea.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                }
                while(--y2 >= 0)
                {
                        drawHDTexturedScanline(DrawingArea.pixels, ai, y3, x1 >> 16, x2 >> 16, l1, l2, l4, k5, j6, i5, l5, k6);
                        x2 += k7;
                        x1 += i7;
                        l2 += l7;
                        l1 += j7;
                        y3 += DrawingArea.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                }
                return;
        }
        x1 = x3 <<= 16;
        l1 = l3 <<= 16;
        if(y3 < 0)
        {
                x1 -= k7 * y3;
                x3 -= i8 * y3;
                l1 -= l7 * y3;
                l3 -= j8 * y3;
                y3 = 0;
        }
        x2 <<= 16;
        l2 <<= 16;
        if(y2 < 0)
        {
                x2 -= i7 * y2;
                l2 -= j7 * y2;
                y2 = 0;
        }
        int l9 = y3 - center_y;
        l4 += j5 * l9;
        k5 += i6 * l9;
        j6 += l6 * l9;
        if(k7 < i8)
        {
                y1 -= y2;
                y2 -= y3;
                y3 = lineOffsets[y3];
                while(--y2 >= 0)
                {
                        drawHDTexturedScanline(DrawingArea.pixels, ai, y3, x1 >> 16, x3 >> 16, l1, l3, l4, k5, j6, i5, l5, k6);
                        x1 += k7;
                        x3 += i8;
                        l1 += l7;
                        l3 += j8;
                        y3 += DrawingArea.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                }
                while(--y1 >= 0)
                {
                        drawHDTexturedScanline(DrawingArea.pixels, ai, y3, x2 >> 16, x3 >> 16, l2, l3, l4, k5, j6, i5, l5, k6);
                        x2 += i7;
                        x3 += i8;
                        l2 += j7;
                        l3 += j8;
                        y3 += DrawingArea.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                }
                return;
        }
        y1 -= y2;
        y2 -= y3;
        y3 = lineOffsets[y3];
        while(--y2 >= 0)
        {
                drawHDTexturedScanline(DrawingArea.pixels, ai, y3, x3 >> 16, x1 >> 16, l3, l1, l4, k5, j6, i5, l5, k6);
                x1 += k7;
                x3 += i8;
                l1 += l7;
                l3 += j8;
                y3 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
        }
        while(--y1 >= 0)
        {
                drawHDTexturedScanline(DrawingArea.pixels, ai, y3, x3 >> 16, x2 >> 16, l3, l2, l4, k5, j6, i5, l5, k6);
                x2 += i7;
                x3 += i8;
                l2 += j7;
                l3 += j8;
                y3 += DrawingArea.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
        }
}

		/**
		 * Smooth shading on texture's scanline method.
		 */
		private static void drawHDTexturedScanline(int ai[], int ai1[], int k, int x1, int x2, int l1,
                                                          int l2, int a1, int i2, int j2, int k2, int a2, int i3) {
        int i = 0;//was parameter
        int j = 0;//was parameter
        if(x1 >= x2)
                return;
        int dl = (l2 - l1) / (x2 - x1);
        int n;
        if(restrict_edges)
        {
                if(x2 > DrawingArea.viewportRX)
                        x2 = DrawingArea.viewportRX;
                if(x1 < 0)
                {
                        l1 -= x1 * dl;
                        x1 = 0;
                }
        }
        if(x1 >= x2)
                return;
        n = x2 - x1 >> 3;
        k += x1;
        if(lowMem)
        {
                int i4 = 0;
                int k4 = 0;
                int k6 = x1 - center_x;
                a1 += (k2 >> 3) * k6;
                i2 += (a2 >> 3) * k6;
                j2 += (i3 >> 3) * k6;
                int i5 = j2 >> 12;
                if(i5 != 0)
                {
                        i = a1 / i5;
                        j = i2 / i5;
                        if(i < 0)
                                i = 0;
                        else
                        if(i > 4032)
                                i = 4032;
                }
                a1 += k2;
                i2 += a2;
                j2 += i3;
                i5 = j2 >> 12;
                if(i5 != 0)
                {
                        i4 = a1 / i5;
                        k4 = i2 / i5;
                        if(i4 < 7)
                                i4 = 7;
                        else
                        if(i4 > 4032)
                                i4 = 4032;
                }
                int i7 = i4 - i >> 3;
                int k7 = k4 - j >> 3;
                if(opaque)
                {
                        int rgb;
                        int l;
                        while(n-- > 0)
                        {
                                rgb = ai1[(j & 0xfc0) + (i >> 6)];
                                l = l1 >> 16;
                                ai[k++] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                                i += i7;
                                j += k7;
                                l1 += dl;
                                rgb = ai1[(j & 0xfc0) + (i >> 6)];
                                l = l1 >> 16;
                                ai[k++] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                                i += i7;
                                j += k7;
                                l1 += dl;
                                rgb = ai1[(j & 0xfc0) + (i >> 6)];
                                l = l1 >> 16;
                                ai[k++] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                                i += i7;
                                j += k7;
                                l1 += dl;
                                rgb = ai1[(j & 0xfc0) + (i >> 6)];
                                l = l1 >> 16;
                                ai[k++] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                                i += i7;
                                j += k7;
                                l1 += dl;
                                rgb = ai1[(j & 0xfc0) + (i >> 6)];
                                l = l1 >> 16;
                                ai[k++] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                                i += i7;
                                j += k7;
                                l1 += dl;
                                rgb = ai1[(j & 0xfc0) + (i >> 6)];
                                l = l1 >> 16;
                                ai[k++] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                                i += i7;
                                j += k7;
                                l1 += dl;
                                rgb = ai1[(j & 0xfc0) + (i >> 6)];
                                l = l1 >> 16;
                                ai[k++] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                                i += i7;
                                j += k7;
                                l1 += dl;
                                rgb = ai1[(j & 0xfc0) + (i >> 6)];
                                l = l1 >> 16;
                                ai[k++] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                                i += i7;
                                j += k7;
                                l1 += dl;
                                a1 += k2;
                                i2 += a2;
                                j2 += i3;
                                int j5 = j2 >> 12;
                                if(j5 != 0)
                                {
                                        i4 = a1 / j5;
                                        k4 = i2 / j5;
                                        if(i4 < 7)
                                                i4 = 7;
                                        else
                                        if(i4 > 4032)
                                                i4 = 4032;
                                }
                                i7 = i4 - i >> 3;
                                k7 = k4 - j >> 3;
                                l1 += dl;
                        }
                        for(n = x2 - x1 & 7; n-- > 0;)
                        {
                                rgb = ai1[(j & 0xfc0) + (i >> 6)];
                                l = l1 >> 16;
                                ai[k++] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                                i += i7;
                                j += k7;
                                l1 += dl;
                        }
                        return;
                }
                while(n-- > 0)
                {
                        int k8;
                        int l;
                        if((k8 = ai1[(j & 0xfc0) + (i >> 6)]) != 0) {
                                l = l1 >> 16;
                                ai[k] = ((k8 & 0xff00ff) * l & ~0xff00ff) + ((k8 & 0xff00) * l & 0xff0000) >> 8;
                        }
                        k++;
                        i += i7;
                        j += k7;
                        l1 += dl;
                        if((k8 = ai1[(j & 0xfc0) + (i >> 6)]) != 0) {
                                l = l1 >> 16;
                                ai[k] = ((k8 & 0xff00ff) * l & ~0xff00ff) + ((k8 & 0xff00) * l & 0xff0000) >> 8;
                        }
                        k++;
                        i += i7;
                        j += k7;
                        l1 += dl;
                        if((k8 = ai1[(j & 0xfc0) + (i >> 6)]) != 0) {
                                l = l1 >> 16;
                                ai[k] = ((k8 & 0xff00ff) * l & ~0xff00ff) + ((k8 & 0xff00) * l & 0xff0000) >> 8;
                        }
                        k++;
                        i += i7;
                        j += k7;
                        l1 += dl;
                        if((k8 = ai1[(j & 0xfc0) + (i >> 6)]) != 0) {
                                l = l1 >> 16;
                                ai[k] = ((k8 & 0xff00ff) * l & ~0xff00ff) + ((k8 & 0xff00) * l & 0xff0000) >> 8;
                        }
                        k++;
                        i += i7;
                        j += k7;
                        l1 += dl;
                        if((k8 = ai1[(j & 0xfc0) + (i >> 6)]) != 0) {
                                l = l1 >> 16;
                                ai[k] = ((k8 & 0xff00ff) * l & ~0xff00ff) + ((k8 & 0xff00) * l & 0xff0000) >> 8;
                        }
                        k++;
                        i += i7;
                        j += k7;
                        l1 += dl;
                        if((k8 = ai1[(j & 0xfc0) + (i >> 6)]) != 0) {
                                l = l1 >> 16;
                                ai[k] = ((k8 & 0xff00ff) * l & ~0xff00ff) + ((k8 & 0xff00) * l & 0xff0000) >> 8;
                        }
                        k++;
                        i += i7;
                        j += k7;
                        l1 += dl;
                        if((k8 = ai1[(j & 0xfc0) + (i >> 6)]) != 0) {
                                l = l1 >> 16;
                                ai[k] = ((k8 & 0xff00ff) * l & ~0xff00ff) + ((k8 & 0xff00) * l & 0xff0000) >> 8;
                        }
                        k++;
                        i += i7;
                        j += k7;
                        l1 += dl;
                        if((k8 = ai1[(j & 0xfc0) + (i >> 6)]) != 0) {
                                l = l1 >> 16;
                                ai[k] = ((k8 & 0xff00ff) * l & ~0xff00ff) + ((k8 & 0xff00) * l & 0xff0000) >> 8;
                        }
                        k++;
                        i += i7;
                        j += k7;
                        l1 += dl;
                        a1 += k2;
                        i2 += a2;
                        j2 += i3;
                        int k5 = j2 >> 12;
                        if(k5 != 0)
                        {
                                i4 = a1 / k5;
                                k4 = i2 / k5;
                                if(i4 < 7)
                                        i4 = 7;
                                else
                                if(i4 > 4032)
                                        i4 = 4032;
                        }
                        i7 = i4 - i >> 3;
                        k7 = k4 - j >> 3;
                        l1 += dl;
                }
                for(n = x2 - x1 & 7; n-- > 0;)
                {
                        int l8;
                        int l;
                        if((l8 = ai1[(j & 0xfc0) + (i >> 6)]) != 0) {
                                l = l1 >> 16;
                                ai[k] = ((l8 & 0xff00ff) * l & ~0xff00ff) + ((l8 & 0xff00) * l & 0xff0000) >> 8;
                        }
                        k++;
                        i += i7;
                        j += k7;
                        l1 += dl;
                }

                return;
        }
        int j4 = 0;
        int l4 = 0;
        int l6 = x1 - center_x;
        a1 += (k2 >> 3) * l6;
        i2 += (a2 >> 3) * l6;
        j2 += (i3 >> 3) * l6;
        int l5 = j2 >> 14;
        if(l5 != 0)
        {
                i = a1 / l5;
                j = i2 / l5;
                if(i < 0)
                        i = 0;
                else
                if(i > 16256)
                        i = 16256;
        }
        a1 += k2;
        i2 += a2;
        j2 += i3;
        l5 = j2 >> 14;
        if(l5 != 0)
        {
                j4 = a1 / l5;
                l4 = i2 / l5;
                if(j4 < 7)
                        j4 = 7;
                else
                if(j4 > 16256)
                        j4 = 16256;
        }
        int j7 = j4 - i >> 3;
        int l7 = l4 - j >> 3;
        if(opaque)
        {
                while(n-- > 0)
                {
                        int rgb;
                        int l;
                        rgb = ai1[(j & 0x3f80) + (i >> 7)];
                        l = l1 >> 16;
                        ai[k++] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                        i += j7;
                        j += l7;
                        l1 += dl;
                        rgb = ai1[(j & 0x3f80) + (i >> 7)];
                        l = l1 >> 16;
                        ai[k++] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                        i += j7;
                        j += l7;
                        l1 += dl;
                        rgb = ai1[(j & 0x3f80) + (i >> 7)];
                        l = l1 >> 16;
                        ai[k++] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                        i += j7;
                        j += l7;
                        l1 += dl;
                        rgb = ai1[(j & 0x3f80) + (i >> 7)];
                        l = l1 >> 16;
                        ai[k++] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                        i += j7;
                        j += l7;
                        l1 += dl;
                        rgb = ai1[(j & 0x3f80) + (i >> 7)];
                        l = l1 >> 16;
                        ai[k++] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                        i += j7;
                        j += l7;
                        l1 += dl;
                        rgb = ai1[(j & 0x3f80) + (i >> 7)];
                        l = l1 >> 16;
                        ai[k++] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                        i += j7;
                        j += l7;
                        l1 += dl;
                        rgb = ai1[(j & 0x3f80) + (i >> 7)];
                        l = l1 >> 16;
                        ai[k++] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                        i += j7;
                        j += l7;
                        l1 += dl;
                        rgb = ai1[(j & 0x3f80) + (i >> 7)];
                        l = l1 >> 16;
                        ai[k++] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                        i += j7;
                        j += l7;
                        l1 += dl;
                        a1 += k2;
                        i2 += a2;
                        j2 += i3;
                        int i6 = j2 >> 14;
                        if(i6 != 0)
                        {
                                j4 = a1 / i6;
                                l4 = i2 / i6;
                                if(j4 < 7)
                                        j4 = 7;
                                else
                                if(j4 > 16256)
                                        j4 = 16256;
                        }
                        j7 = j4 - i >> 3;
                        l7 = l4 - j >> 3;
                        l1 += dl;
                }
                for(n = x2 - x1 & 7; n-- > 0;)
                {
                        int rgb;
                        int l;
                        rgb = ai1[(j & 0x3f80) + (i >> 7)];
                        l = l1 >> 16;
                        ai[k++] = ((rgb & 0xff00ff) * l & ~0xff00ff) + ((rgb & 0xff00) * l & 0xff0000) >> 8;
                        i += j7;
                        j += l7;
                        l1 += dl;
                }

                return;
        }
        while(n-- > 0)
        {
                int i9;
                int l;
                if((i9 = ai1[(j & 0x3f80) + (i >> 7)]) != 0) {
                        l = l1 >> 16;
                        ai[k] = ((i9 & 0xff00ff) * l & ~0xff00ff) + ((i9 & 0xff00) * l & 0xff0000) >> 8;;
                }
                k++;
                i += j7;
                j += l7;
                l1 += dl;
                if((i9 = ai1[(j & 0x3f80) + (i >> 7)]) != 0) {
                        l = l1 >> 16;
                        ai[k] = ((i9 & 0xff00ff) * l & ~0xff00ff) + ((i9 & 0xff00) * l & 0xff0000) >> 8;;
                }
                k++;
                i += j7;
                j += l7;
                l1 += dl;
                if((i9 = ai1[(j & 0x3f80) + (i >> 7)]) != 0) {
                        l = l1 >> 16;
                        ai[k] = ((i9 & 0xff00ff) * l & ~0xff00ff) + ((i9 & 0xff00) * l & 0xff0000) >> 8;;
                }
                k++;
                i += j7;
                j += l7;
                l1 += dl;
                if((i9 = ai1[(j & 0x3f80) + (i >> 7)]) != 0) {
                        l = l1 >> 16;
                        ai[k] = ((i9 & 0xff00ff) * l & ~0xff00ff) + ((i9 & 0xff00) * l & 0xff0000) >> 8;;
                }
                k++;
                i += j7;
                j += l7;
                l1 += dl;
                if((i9 = ai1[(j & 0x3f80) + (i >> 7)]) != 0) {
                        l = l1 >> 16;
                        ai[k] = ((i9 & 0xff00ff) * l & ~0xff00ff) + ((i9 & 0xff00) * l & 0xff0000) >> 8;;
                }
                k++;
                i += j7;
                j += l7;
                l1 += dl;
                if((i9 = ai1[(j & 0x3f80) + (i >> 7)]) != 0) {
                        l = l1 >> 16;
                        ai[k] = ((i9 & 0xff00ff) * l & ~0xff00ff) + ((i9 & 0xff00) * l & 0xff0000) >> 8;;
                }
                k++;
                i += j7;
                j += l7;
                l1 += dl;
                if((i9 = ai1[(j & 0x3f80) + (i >> 7)]) != 0) {
                        l = l1 >> 16;
                        ai[k] = ((i9 & 0xff00ff) * l & ~0xff00ff) + ((i9 & 0xff00) * l & 0xff0000) >> 8;;
                }
                k++;
                i += j7;
                j += l7;
                l1 += dl;
                if((i9 = ai1[(j & 0x3f80) + (i >> 7)]) != 0) {
                        l = l1 >> 16;
                        ai[k] = ((i9 & 0xff00ff) * l & ~0xff00ff) + ((i9 & 0xff00) * l & 0xff0000) >> 8;;
                }
                k++;
                i += j7;
                j += l7;
                l1 += dl;
                a1 += k2;
                i2 += a2;
                j2 += i3;
                int j6 = j2 >> 14;
                if(j6 != 0)
                {
                        j4 = a1 / j6;
                        l4 = i2 / j6;
                        if(j4 < 7)
                                j4 = 7;
                        else
                        if(j4 > 16256)
                                j4 = 16256;
                }
                j7 = j4 - i >> 3;
                l7 = l4 - j >> 3;
                l1 += dl;
        }
        for(int l3 = x2 - x1 & 7; l3-- > 0;)
        {
                int j9;
                int l;
                if((j9 = ai1[(j & 0x3f80) + (i >> 7)]) != 0) {
                        l = l1 >> 16;
                        ai[k] = ((j9 & 0xff00ff) * l & ~0xff00ff) + ((j9 & 0xff00) * l & 0xff0000) >> 8;;
                }
                k++;
                i += j7;
                j += l7;
                l1 += dl;
        }

		}
		
		
		/**
		 * -----------------------
		 * All methods below deal with HDModel's
		 * 
		 */
		public static void renderHDModelTexturedTriangle(
	            int y_a, int y_b, int y_c, int x_a, int x_b, int x_c, int grad_a, int grad_b, int grad_c,
	            int Px, int Mx, int Nx, int Pz, int My, int Nz, int Py, int Mz, int Ny, int t_id, int color, boolean force) {
	    	if(Client.getOption("smooth_shade")/* && notTextured*/) {
	    		drawHDModelTexturedTriangle(y_a, y_b, y_c, x_a, x_b, x_c, grad_a, grad_b, grad_c, Px, Mx, Nx, Pz, My, Nz, Py, Mz, Ny, t_id, color, force);
	    	} else {
	    		drawHDModelTexturedTriangle(y_a, y_b, y_c, x_a, x_b, x_c, grad_a, grad_b, grad_c, Px, Mx, Nx, Pz, My, Nz, Py, Mz, Ny, t_id, color, force);
	    	}
	    }
	    
	    
		public static boolean drawHDModelTexturedTriangle(int y_a, int y_b, int y_c, int x_a, int x_b, int x_c, int grad_a, int grad_b, int grad_c, int Px, int Mx, int Nx, int Pz, int Mz, int Nz, int Py, int My, int Ny, int t_id, int color, boolean force) {
			if (t_id < 0 || t_id >= TextureDef.textures.length) {
				drawShadedTriangle(y_a, y_b, y_c, x_a, x_b, x_c, grad_a, grad_b, grad_c);
				return true;
			}
			TextureDef def = TextureDef.textures[t_id];
			if (def == null || (!force && !def.aBoolean1223 && lowMem)) {
				drawShadedTriangle(y_a, y_b, y_c, x_a, x_b, x_c, grad_a, grad_b, grad_c);
				return true;
			}
			int texture[] = null;
			if(Client.getOption("hd_tex"))
				texture = TextureLoader667.getTexturePixels(t_id);
			else
				texture = TextureLoader317.getTexturePixels(t_id);
			if (texture == null) {
				drawShadedTriangle(y_a, y_b, y_c, x_a, x_b, x_c, grad_a, grad_b, grad_c);
				return false;
			}
			if (color >= 0xffff) {
				color = -1;
			}

			if (color >= 0) {
				color = hsl2rgb[color];
			}

			++triangles;
			Mx = Px - Mx;
			Mz = Pz - Mz;
			My = Py - My;
			Nx -= Px;
			Nz -= Pz;
			Ny -= Py;
			int Oa = Nx * Pz - Nz * Px << (Client.log_view_dist == 9 ? 14 : 15);
			int Ha = Nz * Py - Ny * Pz << 8;
			int Va = Ny * Px - Nx * Py << 5;
			int Ob = Mx * Pz - Mz * Px << (Client.log_view_dist == 9 ? 14 : 15);
			int Hb = Mz * Py - My * Pz << 8;
			int Vb = My * Px - Mx * Py << 5;
			int Oc = Mz * Nx - Mx * Nz << (Client.log_view_dist == 9 ? 14 : 15);
			int Hc = My * Nz - Mz * Ny << 8;
			int Vc = Mx * Ny - My * Nx << 5;
			int x_a_off = 0;
			int grad_a_off = 0;
			if (y_b != y_a) {
				x_a_off = (x_b - x_a << 16) / (y_b - y_a);
				grad_a_off = (grad_b - grad_a << 16) / (y_b - y_a);
			}
			int x_b_off = 0;
			int grad_b_off = 0;
			if (y_c != y_b) {
				x_b_off = (x_c - x_b << 16) / (y_c - y_b);
				grad_b_off = (grad_c - grad_b << 16) / (y_c - y_b);
			}
			int x_c_off = 0;
			int grad_c_off = 0;
			if (y_c != y_a) {
				x_c_off = (x_a - x_c << 16) / (y_a - y_c);
				grad_c_off = (grad_a - grad_c << 16) / (y_a - y_c);
			}
			if (y_a <= y_b && y_a <= y_c) {
				if (y_a >= DrawingArea.bottomY) {
					return true;
				}
				if (y_b > DrawingArea.bottomY) {
					y_b = DrawingArea.bottomY;
				}
				if (y_c > DrawingArea.bottomY) {
					y_c = DrawingArea.bottomY;
				}
				if (y_b < y_c) {
					x_c = x_a <<= 16;
					grad_c = grad_a <<= 16;
					if (y_a < 0) {
						x_c -= x_c_off * y_a;
						x_a -= x_a_off * y_a;
						grad_c -= grad_c_off * y_a;
						grad_a -= grad_a_off * y_a;
						y_a = 0;
					}
					x_b <<= 16;
					grad_b <<= 16;
					if (y_b < 0) {
						x_b -= x_b_off * y_b;
						grad_b -= grad_b_off * y_b;
						y_b = 0;
					}
					int jA = y_a - center_y;
					Oa += Va * jA;
					Ob += Vb * jA;
					Oc += Vc * jA;
					y_c -= y_b;
					y_b -= y_a;
					y_a = lineOffsets[y_a];
					while (--y_b >= 0) {
						renderHDModelTextureLine(pixels, texture, y_a, x_a >> 16, x_c >> 16, grad_a >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
						x_c += x_c_off;
						x_a += x_a_off;
						grad_c += grad_c_off;
						grad_a += grad_a_off;
						y_a += width;
						Oa += Va;
						Ob += Vb;
						Oc += Vc;
					}
					while (--y_c >= 0) {
						renderHDModelTextureLine(pixels, texture, y_a, x_b >> 16, x_c >> 16, grad_b >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
						x_c += x_c_off;
						x_b += x_b_off;
						grad_c += grad_c_off;
						grad_b += grad_b_off;
						y_a += width;
						Oa += Va;
						Ob += Vb;
						Oc += Vc;
					}
					return true;
				}
				x_b = x_a <<= 16;
				grad_b = grad_a <<= 16;
				if (y_a < 0) {
					x_b -= x_c_off * y_a;
					x_a -= x_a_off * y_a;
					grad_b -= grad_c_off * y_a;
					grad_a -= grad_a_off * y_a;
					y_a = 0;
				}
				x_c <<= 16;
				grad_c <<= 16;
				if (y_c < 0) {
					x_c -= x_b_off * y_c;
					grad_c -= grad_b_off * y_c;
					y_c = 0;
				}
				int l8 = y_a - center_y;
				Oa += Va * l8;
				Ob += Vb * l8;
				Oc += Vc * l8;
				/*if (y_a != y_c && x_c_off < x_a_off || y_a == y_c && x_b_off > x_a_off) {
					y_b -= y_c;
					y_c -= y_a;
					y_a = lineOffsets[y_a];
					while (--y_c >= 0) {
						drawTexturedLine(pixels, texture, y_a, x_b >> 16, x_a >> 16, grad_b >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
						x_b += x_c_off;
						x_a += x_a_off;
						grad_b += grad_c_off;
						grad_a += grad_a_off;
						y_a += width;
						Oa += Va;
						Ob += Vb;
						Oc += Vc;
					}
					while (--y_b >= 0) {
						drawTexturedLine(pixels, texture, y_a, x_c >> 16, x_a >> 16, grad_c >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
						x_c += x_b_off;
						x_a += x_a_off;
						grad_c += grad_b_off;
						grad_a += grad_a_off;
						y_a += width;
						Oa += Va;
						Ob += Vb;
						Oc += Vc;
					}
					return true;
				}*/
				y_b -= y_c;
				y_c -= y_a;
				y_a = lineOffsets[y_a];
				while (--y_c >= 0) {
					renderHDModelTextureLine(pixels, texture, y_a, x_a >> 16, x_b >> 16, grad_a >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
					x_b += x_c_off;
					x_a += x_a_off;
					grad_b += grad_c_off;
					grad_a += grad_a_off;
					y_a += width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				while (--y_b >= 0) {
					renderHDModelTextureLine(pixels, texture, y_a, x_a >> 16, x_c >> 16, grad_a >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
					x_c += x_b_off;
					x_a += x_a_off;
					grad_c += grad_b_off;
					grad_a += grad_a_off;
					y_a += width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				return true;
			}
			if (y_b <= y_c) {
				if (y_b >= DrawingArea.bottomY) {
					return true;
				}
				if (y_c > DrawingArea.bottomY) {
					y_c = DrawingArea.bottomY;
				}
				if (y_a > DrawingArea.bottomY) {
					y_a = DrawingArea.bottomY;
				}
				if (y_c < y_a) {
					x_a = x_b <<= 16;
					grad_a = grad_b <<= 16;
					if (y_b < 0) {
						x_a -= x_a_off * y_b;
						x_b -= x_b_off * y_b;
						grad_a -= grad_a_off * y_b;
						grad_b -= grad_b_off * y_b;
						y_b = 0;
					}
					x_c <<= 16;
					grad_c <<= 16;
					if (y_c < 0) {
						x_c -= x_c_off * y_c;
						grad_c -= grad_c_off * y_c;
						y_c = 0;
					}
					int i9 = y_b - center_y;
					Oa += Va * i9;
					Ob += Vb * i9;
					Oc += Vc * i9;
					/*if (y_b != y_c && x_a_off < x_b_off || y_b == y_c && x_a_off > x_c_off) {
						y_a -= y_c;
						y_c -= y_b;
						y_b = lineOffsets[y_b];
						while (--y_c >= 0) {
							drawTexturedLine(pixels, texture, y_b, x_a >> 16, x_b >> 16, grad_a >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
							x_a += x_a_off;
							x_b += x_b_off;
							grad_a += grad_a_off;
							grad_b += grad_b_off;
							y_b += width;
							Oa += Va;
							Ob += Vb;
							Oc += Vc;
						}
						while (--y_a >= 0) {
							drawTexturedLine(pixels, texture, y_b, x_a >> 16, x_c >> 16, grad_a >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
							x_a += x_a_off;
							x_c += x_c_off;
							grad_a += grad_a_off;
							grad_c += grad_c_off;
							y_b += width;
							Oa += Va;
							Ob += Vb;
							Oc += Vc;
						}
						return true;
					}*/
					y_a -= y_c;
					y_c -= y_b;
					y_b = lineOffsets[y_b];
					//not these
					while (--y_c >= 0) {
						renderHDModelTextureLine(pixels, texture, y_b, x_b >> 16, x_a >> 16, grad_b >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
						x_a += x_a_off;
						x_b += x_b_off;
						grad_a += grad_a_off;
						grad_b += grad_b_off;
						y_b += width;
						Oa += Va;
						Ob += Vb;
						Oc += Vc;
					}
					while (--y_a >= 0) {
						renderHDModelTextureLine(pixels, texture, y_b, x_c >> 16, x_a >> 16, grad_c >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
						x_a += x_a_off;
						x_c += x_c_off;
						grad_a += grad_a_off;
						grad_c += grad_c_off;
						y_b += width;
						Oa += Va;
						Ob += Vb;
						Oc += Vc;
					}
					return true;
				}
				x_c = x_b <<= 16;
				grad_c = grad_b <<= 16;
				if (y_b < 0) {
					x_c -= x_a_off * y_b;
					x_b -= x_b_off * y_b;
					grad_c -= grad_a_off * y_b;
					grad_b -= grad_b_off * y_b;
					y_b = 0;
				}
				x_a <<= 16;
				grad_a <<= 16;
				if (y_a < 0) {
					x_a -= x_c_off * y_a;
					grad_a -= grad_c_off * y_a;
					y_a = 0;
				}
				int j9 = y_b - center_y;
				Oa += Va * j9;
				Ob += Vb * j9;
				Oc += Vc * j9;
				/*if (x_a_off < x_b_off) {
					y_c -= y_a;
					y_a -= y_b;
					y_b = lineOffsets[y_b];
					while (--y_a >= 0) {
						drawTexturedLine(pixels, texture, y_b, x_c >> 16, x_b >> 16, grad_c >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
						x_c += x_a_off;
						x_b += x_b_off;
						grad_c += grad_a_off;
						grad_b += grad_b_off;
						y_b += width;
						Oa += Va;
						Ob += Vb;
						Oc += Vc;
					}
					while (--y_c >= 0) {
						drawTexturedLine(pixels, texture, y_b, x_a >> 16, x_b >> 16, grad_a >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
						x_a += x_c_off;
						x_b += x_b_off;
						grad_a += grad_c_off;
						grad_b += grad_b_off;
						y_b += width;
						Oa += Va;
						Ob += Vb;
						Oc += Vc;
					}
					return true;
				}*/
				y_c -= y_a;
				y_a -= y_b;
				y_b = lineOffsets[y_b];
				//not these
				while (--y_a >= 0) {
					renderHDModelTextureLine(pixels, texture, y_b, x_b >> 16, x_c >> 16, grad_b >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
					x_c += x_a_off;
					x_b += x_b_off;
					grad_c += grad_a_off;
					grad_b += grad_b_off;
					y_b += width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				while (--y_c >= 0) {
					renderHDModelTextureLine(pixels, texture, y_b, x_b >> 16, x_a >> 16, grad_b >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
					x_a += x_c_off;
					x_b += x_b_off;
					grad_a += grad_c_off;
					grad_b += grad_b_off;
					y_b += width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				return true;
			}
			if (y_c >= DrawingArea.bottomY) {
				return true;
			}
			if (y_a > DrawingArea.bottomY) {
				y_a = DrawingArea.bottomY;
			}
			if (y_b > DrawingArea.bottomY) {
				y_b = DrawingArea.bottomY;
			}
			if (y_a < y_b) {
				x_b = x_c <<= 16;
				grad_b = grad_c <<= 16;
				if (y_c < 0) {
					x_b -= x_b_off * y_c;
					x_c -= x_c_off * y_c;
					grad_b -= grad_b_off * y_c;
					grad_c -= grad_c_off * y_c;
					y_c = 0;
				}
				x_a <<= 16;
				grad_a <<= 16;
				if (y_a < 0) {
					x_a -= x_a_off * y_a;
					grad_a -= grad_a_off * y_a;
					y_a = 0;
				}
				int k9 = y_c - center_y;
				Oa += Va * k9;
				Ob += Vb * k9;
				Oc += Vc * k9;
				/*if (x_b_off < x_c_off) {
					y_b -= y_a;
					y_a -= y_c;
					y_c = lineOffsets[y_c];
					while (--y_a >= 0) {
						drawTexturedLine(pixels, texture, y_c, x_b >> 16, x_c >> 16, grad_b >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
						x_b += x_b_off;
						x_c += x_c_off;
						grad_b += grad_b_off;
						grad_c += grad_c_off;
						y_c += width;
						Oa += Va;
						Ob += Vb;
						Oc += Vc;
					}
					while (--y_b >= 0) {
						drawTexturedLine(pixels, texture, y_c, x_b >> 16, x_a >> 16, grad_b >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
						x_b += x_b_off;
						x_a += x_a_off;
						grad_b += grad_b_off;
						grad_a += grad_a_off;
						y_c += width;
						Oa += Va;
						Ob += Vb;
						Oc += Vc;
					}
					return true;
				}*/
				y_b -= y_a;
				y_a -= y_c;
				y_c = lineOffsets[y_c];
				//not these
				while (--y_a >= 0) {
					renderHDModelTextureLine(pixels, texture, y_c, x_c >> 16, x_b >> 16, grad_c >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
					x_b += x_b_off;
					x_c += x_c_off;
					grad_b += grad_b_off;
					grad_c += grad_c_off;
					y_c += width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				while (--y_b >= 0) {
					renderHDModelTextureLine(pixels, texture, y_c, x_a >> 16, x_b >> 16, grad_a >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
					x_b += x_b_off;
					x_a += x_a_off;
					grad_b += grad_b_off;
					grad_a += grad_a_off;
					y_c += width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				return true;
			}
			x_a = x_c <<= 16;
			grad_a = grad_c <<= 16;
			if (y_c < 0) {
				x_a -= x_b_off * y_c;
				x_c -= x_c_off * y_c;
				grad_a -= grad_b_off * y_c;
				grad_c -= grad_c_off * y_c;
				y_c = 0;
			}
			x_b <<= 16;
			grad_b <<= 16;
			if (y_b < 0) {
				x_b -= x_a_off * y_b;
				grad_b -= grad_a_off * y_b;
				y_b = 0;
			}
			int l9 = y_c - center_y;
			Oa += Va * l9;
			Ob += Vb * l9;
			Oc += Vc * l9;
			if (x_b_off < x_c_off) {
				y_a -= y_b;
				y_b -= y_c;
				y_c = lineOffsets[y_c];
				/*while (--y_b >= 0) {
					drawTexturedLine(pixels, texture, y_c, x_a >> 16, x_c >> 16, grad_a >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
					x_a += x_b_off;
					x_c += x_c_off;
					grad_a += grad_b_off;
					grad_c += grad_c_off;
					y_c += width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}
				while (--y_a >= 0) {
					drawTexturedLine(pixels, texture, y_c, x_b >> 16, x_c >> 16, grad_b >> 8, grad_c >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
					x_b += x_a_off;
					x_c += x_c_off;
					grad_b += grad_a_off;
					grad_c += grad_c_off;
					y_c += width;
					Oa += Va;
					Ob += Vb;
					Oc += Vc;
				}*/
				return true;
			}
			y_a -= y_b;
			y_b -= y_c;
			y_c = lineOffsets[y_c];
			//not these
			while (--y_b >= 0) {
				renderHDModelTextureLine(pixels, texture, y_c, x_c >> 16, x_a >> 16, grad_c >> 8, grad_a >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
				x_a += x_b_off;
				x_c += x_c_off;
				grad_a += grad_b_off;
				grad_c += grad_c_off;
				y_c += width;
				Oa += Va;
				Ob += Vb;
				Oc += Vc;
			}
			while (--y_a >= 0) {
				renderHDModelTextureLine(pixels, texture, y_c, x_c >> 16, x_b >> 16, grad_c >> 8, grad_b >> 8, Oa, Ob, Oc, Ha, Hb, Hc, color, force);
				x_b += x_a_off;
				x_c += x_c_off;
				grad_b += grad_a_off;
				grad_c += grad_c_off;
				y_c += width;
				Oa += Va;
				Ob += Vb;
				Oc += Vc;
			}
			return true;
		}

		private static void renderHDModelTextureLine(int dest[], int texture[], int dest_off, int start_x, int end_x, int shadeValue, int gradient, int arg7, int arg8, int arg9, int arg10, int arg11, int arg12, int color, boolean force) {
			//shadeValue = 500;//lol makes textures ultra bright and makes triangles visible - slightly wrong name.. meh
			int rgb = 0;
			int loops = 0;
			if (start_x >= end_x) {
				return;
			}
			int j3;
			int k3;
			if (restrict_edges) {
				j3 = (gradient - shadeValue) / (end_x - start_x);
				if (end_x > DrawingArea.viewportRX) {
					end_x = DrawingArea.viewportRX;
				}
				if (start_x < 0) {
					shadeValue -= start_x * j3;
					start_x = 0;
				}
				if (start_x >= end_x) {
					return;
				}
				k3 = end_x - start_x >> 3;
				j3 <<= 12;
				shadeValue <<= 9;
			} else {
				if (end_x - start_x > 7) {
					k3 = end_x - start_x >> 3;
					j3 = (gradient - shadeValue) * shadowDecay[k3] >> 6;
				} else {
					k3 = 0;
					j3 = 0;
				}
				shadeValue <<= 9;
			}
			dest_off += start_x;
			int j4 = 0;
			int l4 = 0;
			int l6 = start_x - center_x;
			arg7 += (arg10 >> 3) * l6;
			arg8 += (arg11 >> 3) * l6;
			arg9 += (arg12 >> 3) * l6;
			int l5 = arg9 >> 14;
			if (l5 != 0) {
				rgb = arg7 / l5;
				loops = arg8 / l5;
				if (rgb < 0) {
					rgb = 0;
				} else if (rgb > 16256) {
					rgb = 16256;
				}
			}
			arg7 += arg10;
			arg8 += arg11;
			arg9 += arg12;
			l5 = arg9 >> 14;
			if (l5 != 0) {
				j4 = arg7 / l5;
				l4 = arg8 / l5;
				if (j4 < 7) {
					j4 = 7;
				} else if (j4 > 16256) {
					j4 = 16256;
				}
			}
			int j7 = j4 - rgb >> 3;
			int l7 = l4 - loops >> 3;
			rgb += shadeValue & 0x600000;
			int glb_alpha = alpha;
			if (glb_alpha < 0 || glb_alpha >= 0xff)
				glb_alpha = 0;

			int src;
			int src_alpha;
			int src_delta;
			int dst;
			while (k3-- > 0)
			{
				src = texture[(loops & 0x3f80) + (rgb >> 7)];
				src_alpha = src >>> 24;
				if (src_alpha != 0 || force)
				{
					if (src_alpha != 0xff && color >= 0)
					{
						if (src_alpha == 0)
							src = color;

						else
						{
							src_delta = 0xff - src_alpha;
							src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
						}
						src_alpha = 0xff;
					}
					if (glb_alpha > 0)
						src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

					if (src_alpha != 0)
					{
						if (src_alpha == 0xff)
							dest[dest_off] = src & 0xffffff;

						else
						{
							dst = dest[dest_off];
							src_delta = 0xff - src_alpha;
							dest[dest_off] = (((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff;
						}
					}
				}
				dest_off++;
				rgb += j7;
				loops += l7;

				src = texture[(loops & 0x3f80) + (rgb >> 7)];
				src_alpha = src >>> 24;
				if (src_alpha != 0 || force)
				{
					if (src_alpha != 0xff && color >= 0)
					{
						if (src_alpha == 0)
							src = color;

						else
						{
							src_delta = 0xff - src_alpha;
							src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
						}
						src_alpha = 0xff;
					}
					if (glb_alpha > 0)
						src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

					if (src_alpha != 0)
					{
						if (src_alpha == 0xff)
							dest[dest_off] = src & 0xffffff;

						else
						{
							dst = dest[dest_off];
							src_delta = 0xff - src_alpha;
							dest[dest_off] = (((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff;
						}
					}
				}
				dest_off++;
				rgb += j7;
				loops += l7;

				src = texture[(loops & 0x3f80) + (rgb >> 7)];
				src_alpha = src >>> 24;
				if (src_alpha != 0 || force)
				{
					if (src_alpha != 0xff && color >= 0)
					{
						if (src_alpha == 0)
							src = color;

						else
						{
							src_delta = 0xff - src_alpha;
							src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
						}
						src_alpha = 0xff;
					}
					if (glb_alpha > 0)
						src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

					if (src_alpha != 0)
					{
						if (src_alpha == 0xff)
							dest[dest_off] = src & 0xffffff;

						else
						{
							dst = dest[dest_off];
							src_delta = 0xff - src_alpha;
							dest[dest_off] = (((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff;
						}
					}
				}
				dest_off++;
				rgb += j7;
				loops += l7;

				src = texture[(loops & 0x3f80) + (rgb >> 7)];
				src_alpha = src >>> 24;
				if (src_alpha != 0 || force)
				{
					if (src_alpha != 0xff && color >= 0)
					{
						if (src_alpha == 0)
							src = color;

						else
						{
							src_delta = 0xff - src_alpha;
							src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
						}
						src_alpha = 0xff;
					}
					if (glb_alpha > 0)
						src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

					if (src_alpha != 0)
					{
						if (src_alpha == 0xff)
							dest[dest_off] = src & 0xffffff;

						else
						{
							dst = dest[dest_off];
							src_delta = 0xff - src_alpha;
							dest[dest_off] = (((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff;
						}
					}
				}
				dest_off++;
				rgb += j7;
				loops += l7;

				src = texture[(loops & 0x3f80) + (rgb >> 7)];
				src_alpha = src >>> 24;
				if (src_alpha != 0 || force)
				{
					if (src_alpha != 0xff && color >= 0)
					{
						if (src_alpha == 0)
							src = color;

						else
						{
							src_delta = 0xff - src_alpha;
							src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
						}
						src_alpha = 0xff;
					}
					if (glb_alpha > 0)
						src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

					if (src_alpha != 0)
					{
						if (src_alpha == 0xff)
							dest[dest_off] = src & 0xffffff;

						else
						{
							dst = dest[dest_off];
							src_delta = 0xff - src_alpha;
							dest[dest_off] = (((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff;
						}
					}
				}
				dest_off++;
				rgb += j7;
				loops += l7;

				src = texture[(loops & 0x3f80) + (rgb >> 7)];
				src_alpha = src >>> 24;
				if (src_alpha != 0 || force)
				{
					if (src_alpha != 0xff && color >= 0)
					{
						if (src_alpha == 0)
							src = color;

						else
						{
							src_delta = 0xff - src_alpha;
							src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
						}
						src_alpha = 0xff;
					}
					if (glb_alpha > 0)
						src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

					if (src_alpha != 0)
					{
						if (src_alpha == 0xff)
							dest[dest_off] = src & 0xffffff;

						else
						{
							dst = dest[dest_off];
							src_delta = 0xff - src_alpha;
							dest[dest_off] = (((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff;
						}
					}
				}
				dest_off++;
				rgb += j7;
				loops += l7;

				src = texture[(loops & 0x3f80) + (rgb >> 7)];
				src_alpha = src >>> 24;
				if (src_alpha != 0 || force)
				{
					if (src_alpha != 0xff && color >= 0)
					{
						if (src_alpha == 0)
							src = color;

						else
						{
							src_delta = 0xff - src_alpha;
							src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
						}
						src_alpha = 0xff;
					}
					if (glb_alpha > 0)
						src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

					if (src_alpha != 0)
					{
						if (src_alpha == 0xff)
							dest[dest_off] = src & 0xffffff;

						else
						{
							dst = dest[dest_off];
							src_delta = 0xff - src_alpha;
							dest[dest_off] = (((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff;
						}
					}
				}
				dest_off++;
				rgb += j7;
				loops += l7;

				src = texture[(loops & 0x3f80) + (rgb >> 7)];
				src_alpha = src >>> 24;
				if (src_alpha != 0 || force)
				{
					if (src_alpha != 0xff && color >= 0)
					{
						if (src_alpha == 0)
							src = color;

						else
						{
							src_delta = 0xff - src_alpha;
							src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
						}
						src_alpha = 0xff;
					}
					if (glb_alpha > 0)
						src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

					if (src_alpha != 0)
					{
						if (src_alpha == 0xff)
							dest[dest_off] = src & 0xffffff;

						else
						{
							dst = dest[dest_off];
							src_delta = 0xff - src_alpha;
							dest[dest_off] = (((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff;
						}
					}
				}
				dest_off++;
				//rgb += j7;
				//loops += l7;

				rgb = j4;
				loops = l4;
				arg7 += arg10;
				arg8 += arg11;
				arg9 += arg12;
				int i6 = arg9 >> 14;
				if (i6 != 0) {
					j4 = arg7 / i6;
					l4 = arg8 / i6;
					if (j4 < 7) {
						j4 = 7;
					} else if (j4 > 16256) {
						j4 = 16256;
					}
				}
				j7 = j4 - rgb >> 3;
				l7 = l4 - loops >> 3;
				shadeValue += j3;
				rgb += shadeValue & 0x600000;
			}
			for (k3 = end_x - start_x & 7; k3-- > 0; ) {
				src = texture[(loops & 0x3f80) + (rgb >> 7)];
				src_alpha = src >>> 24;
				if (src_alpha != 0 || force)
				{
					if (src_alpha != 0xff && color >= 0)
					{
						if (src_alpha == 0)
							src = color;

						else
						{
							src_delta = 0xff - src_alpha;
							src = ((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (color & 0xff00) | src_delta * (color & 0xff00ff) & 0xff00ff00) >>> 8));
						}
						src_alpha = 0xff;
					}
					if (glb_alpha > 0)
						src_alpha = (src_alpha * (glb_alpha + 1)) >>> 8;

					if (src_alpha != 0)
					{
						if (src_alpha == 0xff)
							dest[dest_off] = src & 0xffffff;

						else
						{
							dst = dest[dest_off];
							src_delta = 0xff - src_alpha;
							dest[dest_off] = (((0xff00ff00 & (0xff00ff & src) * src_alpha | 0xff0000 & (src & 0xff00) * src_alpha) >>> 8) + (((0xff0000 & src_delta * (dst & 0xff00) | src_delta * (dst & 0xff00ff) & 0xff00ff00) >>> 8))) & 0xffffff;
						}
					}
				}
				dest_off++;
				rgb += j7;
				loops += l7;
			}

		}
		
}