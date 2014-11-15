
/**
 * 474 Drawing Area Refactored by Galkon
 **/

public class DrawingArea474 extends DrawingArea {

	public static void drawDiagonalLine(int x, int y, int areaWidth,
			int areaHeight, int color) {// method577
		areaWidth -= x;
		areaHeight -= y;
		if (areaHeight == 0)
			if (areaWidth >= 0) {
				drawHorizontalLine(x, y, areaWidth + 1, color);
				return;
			} else {
				drawHorizontalLine(x + areaWidth, y, -areaWidth + 1, color);
				return;
			}
		if (areaWidth == 0)
			if (areaHeight >= 0) {
				drawVerticalLine(x, y, areaHeight + 1, color);
				return;
			} else {
				drawVerticalLine(x, y + areaHeight, -areaHeight + 1, color);
				return;
			}
		if (areaWidth + areaHeight < 0) {
			x += areaWidth;
			areaWidth = -areaWidth;
			y += areaHeight;
			areaHeight = -areaHeight;
		}
		if (areaWidth > areaHeight) {
			y <<= 16;
			y += 32768;
			areaHeight <<= 16;
			int j1 = (int) Math.floor((double) areaHeight / (double) areaWidth
					+ 0.5D);
			areaWidth += x;
			if (x < topX) {
				y += j1 * (topX - x);
				x = topX;
			}
			if (areaWidth >= bottomX)
				areaWidth = bottomX - 1;
			for (; x <= areaWidth; x++) {
				int l1 = y >> 16;
				if (l1 >= topY && l1 < bottomY)
					pixels[x + l1 * width] = color;
				y += j1;
			}
			return;
		}
		x <<= 16;
		x += 32768;
		areaWidth <<= 16;
		int k1 = (int) Math.floor((double) areaWidth / (double) areaHeight
				+ 0.5D);
		areaHeight += y;
		if (y < topY) {
			x += k1 * (topY - y);
			y = topY;
		}
		if (areaHeight >= bottomY)
			areaHeight = bottomY - 1;
		for (; y <= areaHeight; y++) {
			int i2 = x >> 16;
			if (i2 >= topX && i2 < bottomX)
				pixels[i2 + y * width] = color;
			x += k1;
		}
	}

	public static void drawFilledPixels(int x, int y, int pixelWidth,
			int pixelHeight, int color) {// method578
		if (x < topX) {
			pixelWidth -= topX - x;
			x = topX;
		}
		if (y < topY) {
			pixelHeight -= topY - y;
			y = topY;
		}
		if (x + pixelWidth > bottomX)
			pixelWidth = bottomX - x;
		if (y + pixelHeight > bottomY)
			pixelHeight = bottomY - y;
		int j1 = width - pixelWidth;
		int k1 = x + y * width;
		for (int l1 = -pixelHeight; l1 < 0; l1++) {
			for (int i2 = -pixelWidth; i2 < 0; i2++)
				pixels[k1++] = color;
			k1 += j1;
		}
	}

	public static void method579() {
		int i = 0;
		int j;
		for (j = width * height - 7; i < j;) {
			pixels[i++] = 0;
			pixels[i++] = 0;
			pixels[i++] = 0;
			pixels[i++] = 0;
			pixels[i++] = 0;
			pixels[i++] = 0;
			pixels[i++] = 0;
			pixels[i++] = 0;
		}
		for (j += 7; i < j;)
			pixels[i++] = 0;
	}

	public static void drawHorizontalLine(int x, int y, int lineWidth, int color) {// drawHorizontalLine
		if (y < topY || y >= bottomY)
			return;
		if (x < topX) {
			lineWidth -= topX - x;
			x = topX;
		}
		if (x + lineWidth > bottomX)
			lineWidth = bottomX - x;
		int pixelCount = x + y * width;
		for (int j1 = 0; j1 < lineWidth; j1++)
			pixels[pixelCount + j1] = color;
	}

	public static void drawVerticalLine(int x, int y, int lineHeight, int color) {// drawVerticalLine
		if (x < topX || x >= bottomX)
			return;
		if (y < topY) {
			lineHeight -= topY - y;
			y = topY;
		}
		if (y + lineHeight > bottomY)
			lineHeight = bottomY - y;
		int pixelCount = x + y * width;
		for (int j1 = 0; j1 < lineHeight; j1++)
			pixels[pixelCount + j1 * width] = color;
	}

	public static void method582() {
		pixels = null;
	}

	public DrawingArea474() {
	}

	public static void drawGradient(int x, int y, int gradientWidth,
			int gradientHeight, int startColor, int endColor) {// method583
		int k1 = 0;
		int l1 = 0x10000 / gradientHeight;
		if (x < topX) {
			gradientWidth -= topX - x;
			x = topX;
		}
		if (y < topY) {
			k1 += (topY - y) * l1;
			gradientHeight -= topY - y;
			y = topY;
		}
		if (x + gradientWidth > bottomX)
			gradientWidth = bottomX - x;
		if (y + gradientHeight > bottomY)
			gradientHeight = bottomY - y;
		int i2 = width - gradientWidth;
		int j2 = x + y * width;
		for (int k2 = -gradientHeight; k2 < 0; k2++) {
			int l2 = 0x10000 - k1 >> 8;
			int i3 = k1 >> 8;
			int j3 = ((startColor & 0xff00ff) * l2 + (endColor & 0xff00ff) * i3 & 0xff00ff00)
					+ ((startColor & 0xff00) * l2 + (endColor & 0xff00) * i3 & 0xff0000) >>> 8;
			for (int k3 = -gradientWidth; k3 < 0; k3++)
				pixels[j2++] = j3;
			j2 += i2;
			k1 += l1;
		}
	}

	public  void drawAlphaGradient(int x, int y, int gradientWidth,
			int gradientHeight, int startColor, int endColor, int alpha) {
		int k1 = 0;
		int l1 = 0x10000 / gradientHeight;
		if (x < topX) {
			gradientWidth -= topX - x;
			x = topX;
		}
		if (y < topY) {
			k1 += (topY - y) * l1;
			gradientHeight -= topY - y;
			y = topY;
		}
		if (x + gradientWidth > bottomX)
			gradientWidth = bottomX - x;
		if (y + gradientHeight > bottomY)
			gradientHeight = bottomY - y;
		int i2 = width - gradientWidth;
		int result_alpha = 256 - alpha;
		int total_pixels = x + y * width;
		for (int k2 = -gradientHeight; k2 < 0; k2++) {
			int gradient1 = 0x10000 - k1 >> 8;
			int gradient2 = k1 >> 8;
			int gradient_color = ((startColor & 0xff00ff) * gradient1
					+ (endColor & 0xff00ff) * gradient2 & 0xff00ff00)
					+ ((startColor & 0xff00) * gradient1 + (endColor & 0xff00)
							* gradient2 & 0xff0000) >>> 8;
			int color = ((gradient_color & 0xff00ff) * alpha >> 8 & 0xff00ff)
					+ ((gradient_color & 0xff00) * alpha >> 8 & 0xff00);
			for (int k3 = -gradientWidth; k3 < 0; k3++) {
				int pixel_pixels = pixels[total_pixels];
				pixel_pixels = ((pixel_pixels & 0xff00ff) * result_alpha >> 8 & 0xff00ff)
						+ ((pixel_pixels & 0xff00) * result_alpha >> 8 & 0xff00);
				pixels[total_pixels++] = color + pixel_pixels;
			}
			total_pixels += i2;
			k1 += l1;
		}
	}

	public static void drawAlphaHorizontalLine(int x, int y, int lineWidth,
			int color, int alpha) {// drawAlphaHorizontalLine
		if (y < topY || y >= bottomY)
			return;
		if (x < topX) {
			lineWidth -= topX - x;
			x = topX;
		}
		if (x + lineWidth > bottomX)
			lineWidth = bottomX - x;
		int j1 = 256 - alpha;
		int k1 = (color >> 16 & 0xff) * alpha;
		int l1 = (color >> 8 & 0xff) * alpha;
		int i2 = (color & 0xff) * alpha;
		int i3 = x + y * width;
		for (int j3 = 0; j3 < lineWidth; j3++) {
			int j2 = (pixels[i3] >> 16 & 0xff) * j1;
			int k2 = (pixels[i3] >> 8 & 0xff) * j1;
			int l2 = (pixels[i3] & 0xff) * j1;
			int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8)
					+ (i2 + l2 >> 8);
			pixels[i3++] = k3;
		}
	}

	public static void drawAlphaVerticalLine(int x, int y, int lineHeight,
			int color, int alpha) {// drawAlphaVerticalLine
		if (x < topX || x >= bottomX)
			return;
		if (y < topY) {
			lineHeight -= topY - y;
			y = topY;
		}
		if (y + lineHeight > bottomY)
			lineHeight = bottomY - y;
		int j1 = 256 - alpha;
		int k1 = (color >> 16 & 0xff) * alpha;
		int l1 = (color >> 8 & 0xff) * alpha;
		int i2 = (color & 0xff) * alpha;
		int i3 = x + y * width;
		for (int j3 = 0; j3 < lineHeight; j3++) {
			int j2 = (pixels[i3] >> 16 & 0xff) * j1;
			int k2 = (pixels[i3] >> 8 & 0xff) * j1;
			int l2 = (pixels[i3] & 0xff) * j1;
			int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8)
					+ (i2 + l2 >> 8);
			pixels[i3] = k3;
			i3 += width;
		}
	}

	public static void drawAlphaFilledPixels(int xPos, int yPos,
			int pixelWidth, int pixelHeight, int color, int alpha) {// method586
		if (xPos < topX) {
			pixelWidth -= topX - xPos;
			xPos = topX;
		}
		if (yPos < topY) {
			pixelHeight -= topY - yPos;
			yPos = topY;
		}
		if (xPos + pixelWidth > bottomX)
			pixelWidth = bottomX - xPos;
		if (yPos + pixelHeight > bottomY)
			pixelHeight = bottomY - yPos;
		color = ((color & 0xff00ff) * alpha >> 8 & 0xff00ff)
				+ ((color & 0xff00) * alpha >> 8 & 0xff00);
		int k1 = 256 - alpha;
		int l1 = width - pixelWidth;
		int i2 = xPos + yPos * width;
		for (int j2 = 0; j2 < pixelHeight; j2++) {
			for (int k2 = -pixelWidth; k2 < 0; k2++) {
				int l2 = pixels[i2];
				l2 = ((l2 & 0xff00ff) * k1 >> 8 & 0xff00ff)
						+ ((l2 & 0xff00) * k1 >> 8 & 0xff00);
				pixels[i2++] = color + l2;
			}
			i2 += l1;
		}
	}

	public static void method587(int ai[], int i, int j) {
		pixels = ai;
		width = i;
		height = j;
		method589(0, 0, i, j);
	}

	public static void drawAlphaUnfilledPixels(int x, int y, int width,
			int height, int color, int alpha) {// method588
		drawAlphaHorizontalLine(x, y, width, color, alpha);
		drawAlphaHorizontalLine(x, (y + height) - 1, width, color, alpha);
		if (height >= 3) {
			drawAlphaVerticalLine(x, y + 1, height - 2, color, alpha);
			drawAlphaVerticalLine((x + width) - 1, y + 1, height - 2, color,
					alpha);
		}
	}

	public static void method589(int i, int j, int k, int l) {
		if (i < 0)
			i = 0;
		if (j < 0)
			j = 0;
		if (k > width)
			k = width;
		if (l > height)
			l = height;
		topX = i;
		topY = j;
		bottomX = k;
		bottomY = l;
	}

	public static void method590() {
		topX = 0;
		topY = 0;
		bottomX = width;
		bottomY = height;
	}

	public static void method591(int ai[]) {
		topX = ai[0];
		topY = ai[1];
		bottomX = ai[2];
		bottomY = ai[3];
	}

	public static void method592(int i, int j, int k, int l) {
		if (topX < i)
			topX = i;
		if (topY < j)
			topY = j;
		if (bottomX > k)
			bottomX = k;
		if (bottomY > l)
			bottomY = l;
	}

	public static void method593(int i, int j, int k, int ai[], int ai1[]) {
		int l = i + j * width;
		for (j = 0; j < ai.length; j++) {
			int i1 = l + ai[j];
			for (i = -ai1[j]; i < 0; i++)
				pixels[i1++] = k;
			l += width;
		}
	}

	public static void method594(int ai[]) {
		ai[0] = topX;
		ai[1] = topY;
		ai[2] = bottomX;
		ai[3] = bottomY;
	}

	public static void drawUnfilledPixels(int x, int y, int width, int height,
			int color) {// method595
		drawHorizontalLine(x, y, width, color);
		drawHorizontalLine(x, (y + height) - 1, width, color);
		drawVerticalLine(x, y, height, color);
		drawVerticalLine((x + width) - 1, y, height, color);
	}

	public static void drawRoundedRectangle(int x, int y, int width,
			int height, int color, int alpha, boolean filled, boolean shadowed) {
		if (shadowed)
			drawRoundedRectangle(x + 1, y + 1, width, height, 0, alpha, filled,
					false);
		if (alpha == -1) {
			if (filled) {
				drawHorizontalLine(y + 1, color, width - 4, x + 2);// method339
				drawHorizontalLine(y + height - 2, color, width - 4, x + 2);// method339
				drawPixels(height - 4, y + 2, x + 1, color, width - 2);// method336
			}
			drawHorizontalLine(y, color, width - 4, x + 2);// method339
			drawHorizontalLine(y + height - 1, color, width - 4, x + 2);// method339
			drawLineVertical(y + 2, color, height - 4, x);// method341
			drawLineVertical(y + 2, color, height - 4, x + width - 1);// method341
			drawPixels(1, y + 1, x + 1, color, 1);// method336
			drawPixels(1, y + 1, x + width - 2, color, 1);// method336
			drawPixels(1, y + height - 2, x + width - 2, color, 1);// method336
			drawPixels(1, y + height - 2, x + 1, color, 1);// method336
		} else if (alpha != -1) {
			if (filled) {
				drawHLine(color, width - 4, y + 1, alpha, x + 2);// method340
				drawHLine(color, width - 4, y + height - 2, alpha, x + 2);// method340
				fillRectangle(color, y + 2, width - 2, height - 4, alpha, x + 1);// method335
			}
			drawHLine(color, width - 4, y, alpha, x + 2);// method340
			drawHLine(color, width - 4, y + height - 1, alpha, x + 2);// method340
			drawVLine(color, x, alpha, y + 2, height - 4);// method342
			drawVLine(color, x + width - 1, alpha, y + 2, height - 4);// method342
			fillRectangle(color, y + 1, 1, 1, alpha, x + 1);// method335
			fillRectangle(color, y + 1, 1, 1, alpha, x + width - 2);// method335
			fillRectangle(color, y + height - 2, 1, 1, alpha, x + 1);// method335
			fillRectangle(color, y + height - 2, 1, 1, alpha, x + width - 2);// method335
		}
	}
}