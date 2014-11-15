

public class DrawingArea extends QueueNode {

	public static void initDrawingArea(int i, int j, int ai[])
	{
		initDrawingArea(i, j, ai, null);
	}
	
	public static void initDrawingArea(int i, int j, int ai[], float db[])
	{
		pixels = ai;
		depthBuffer = db;
		width = j;
		height = i;
		setDrawingArea(i, 0, j, 0);
	}
	
	public static void defaultDrawingAreaSize()
	{
			topX = 0;
			topY = 0;
			bottomX = width;
			bottomY = height;
			viewportRX = bottomX - 0;
			viewport_centerX = bottomX / 1;
	}
	
	public static void setDrawingArea(int yBottom, int xTop, int xBottom, int yTop)
	{
		if(xTop < 0)
			xTop = 0;
		if(yTop < 0)
			yTop = 0;
		if(xBottom > width)
			xBottom = width;
		if(yBottom > height)
			yBottom = height;
		topX = xTop;
		topY = yTop;
		bottomX = xBottom;
		bottomY = yBottom;
		viewportRX = bottomX - 0;
		viewport_centerX = bottomX / 2;
		viewport_centerY = bottomY / 2;
	}
	
	public static int clamp(int x, int a, int b) {
		return (x < a) ? a : (x > b) ? b : x;
	}
    public static void blur( int[] in, int[] out, int width, int height, int radius ) {
        int widthMinus1 = width-1;
        int tableSize = 2*radius+1;
        int divide[] = new int[256*tableSize];

        for ( int i = 0; i < 256*tableSize; i++ )
            divide[i] = i/tableSize;

        int inIndex = 0;
        
        for ( int y = 0; y < height; y++ ) {
            int outIndex = y;
            int ta = 0, tr = 0, tg = 0, tb = 0;

            for ( int i = -radius; i <= radius; i++ ) {
                int rgb = in[inIndex + clamp(i, 0, width-1)];
                ta += (rgb >> 24) & 0xff;
                tr += (rgb >> 16) & 0xff;
                tg += (rgb >> 8) & 0xff;
                tb += rgb & 0xff;
            }

            for ( int x = 0; x < width; x++ ) {
                out[ outIndex ] = (divide[ta] << 24) | (divide[tr] << 16) | (divide[tg] << 8) | divide[tb];

                int i1 = x+radius+1;
                if ( i1 > widthMinus1 )
                    i1 = widthMinus1;
                int i2 = x-radius;
                if ( i2 < 0 )
                    i2 = 0;
                int rgb1 = in[inIndex+i1];
                int rgb2 = in[inIndex+i2];
                
                ta += ((rgb1 >> 24) & 0xff)-((rgb2 >> 24) & 0xff);
                tr += ((rgb1 & 0xff0000)-(rgb2 & 0xff0000)) >> 16;
                tg += ((rgb1 & 0xff00)-(rgb2 & 0xff00)) >> 8;
                tb += (rgb1 & 0xff)-(rgb2 & 0xff);
                outIndex += height;
            }
            inIndex += width;
        }
    }
    
	public static void drawDiagonalLine(int x, int y, int areaWidth, int areaHeight, int color) {//method577
		areaWidth -= x;
		areaHeight -= y;
		if(areaHeight == 0)
			if(areaWidth >= 0) {
				drawHorizontalLine(x, y, areaWidth + 1, color);
				return;
			} else {
				drawHorizontalLine(x + areaWidth, y, -areaWidth + 1, color);
				return;
			}
		if(areaWidth == 0)
			if(areaHeight >= 0) {
				drawVLine(x, y, areaHeight + 1, color);
				return;
			} else {
				drawHLine(x, y + areaHeight, -areaHeight + 1, color);
				return;
			}
		if(areaWidth + areaHeight < 0) {
			x += areaWidth;
			areaWidth = -areaWidth;
			y += areaHeight;
			areaHeight = -areaHeight;
		}
		if(areaWidth > areaHeight) {
			y <<= 16;
			y += 32768;
			areaHeight <<= 16;
			int j1 = (int)Math.floor((double)areaHeight / (double)areaWidth + 0.5D);
			areaWidth += x;
			if(x < topX) {
				y += j1 * (topX - x);
				x = topX;
			}
			if(areaWidth >= bottomX)
				areaWidth = bottomX - 1;
			for(; x <= areaWidth; x++) {
				int l1 = y >> 16;
				if(l1 >= topY && l1 < bottomY)
					pixels[x + l1 * width] = color;
				y += j1;
			}
			return;
		}
		x <<= 16;
		x += 32768;
		areaWidth <<= 16;
		int k1 = (int)Math.floor((double)areaWidth / (double)areaHeight + 0.5D);
		areaHeight += y;
		if(y < topY) {
			x += k1 * (topY - y);
			y = topY;
		}
		if(areaHeight >= bottomY)
			areaHeight = bottomY - 1;
		for(; y <= areaHeight; y++) {
			int i2 = x >> 16;
			if(i2 >= topX && i2 < bottomX)
				pixels[i2 + y * width] = color;
			x += k1;
		}
	}
	
	public static void drawGradient(int x, int y, int gradientWidth,
			int gradientHeight, int startColor, int endColor) {
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
	
	public static void drawAlphaGradient2(int x, int y, int gradientWidth,
			int gradientHeight, int startColor, int endColor, int alpha) {
		int k1 = 0;
		int l1 = 0x10000 / gradientHeight;
		if (x < topX) {
			gradientWidth -= topX - x;
			x = topX;
		}
		if (y < topY) {
			k1 -= (topY - y) * l1;
			gradientHeight -= topY - y;
			y = topY;
		}
		if (x + gradientWidth > bottomX)
			gradientWidth = bottomX - x;
		if (y + gradientHeight > bottomY)
			gradientHeight = bottomY - y;
		int i2 = width - gradientWidth;
		int total_pixels = x + y * width;
		for (int k2 = -gradientHeight; k2 < 0; k2++) {
			int alpha2 = (gradientHeight+k2)*(gradientHeight/alpha);
			int result_alpha = 256 - alpha2;
			int gradient1 = 0x10000 - k1 >> 8;
			int gradient2 = k1 >> 8;
			int gradient_color = ((startColor & 0xff00ff) * gradient1
					+ (endColor & 0xff00ff) * gradient2 & 0xff00ff00)
					+ ((startColor & 0xff00) * gradient1 + (endColor & 0xff00)
							* gradient2 & 0xff0000) >>> 8;
			int color = ((gradient_color & 0xff00ff) * alpha >> 8 & 0xff00ff)
					+ ((gradient_color & 0xff00) * alpha >> 8 & 0xff00);
			for (int k3 = -gradientWidth; k3 < 0; k3++) {
				int colored_pixel = pixels[total_pixels];
				colored_pixel = ((colored_pixel & 0xff00ff) * result_alpha >> 8 & 0xff00ff)
						+ ((colored_pixel & 0xff00) * result_alpha >> 8 & 0xff00);
				pixels[total_pixels++] = color + colored_pixel;
			}
			total_pixels += i2;
			k1 -= l1;
		}
	}

	public void drawAlphaGradient(int x, int y, int gradientWidth,
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
				int colored_pixel = pixels[total_pixels];
				colored_pixel = ((colored_pixel & 0xff00ff) * result_alpha >> 8 & 0xff00ff)
						+ ((colored_pixel & 0xff00) * result_alpha >> 8 & 0xff00);
				pixels[total_pixels++] = color + colored_pixel;
			}
			total_pixels += i2;
			k1 += l1;
		}
	}
	
	public static void drawAlphaHorizontalLine2(int x, int y, int lineWidth,
			int color, int alpha) {// drawAlphaHorizontalLine
		if (y < topY || y >= bottomY)
			return;
		if (x < topX) {
			lineWidth -= topX - x;
			x = topX;
		}
		if (x + lineWidth > bottomX)
			lineWidth = bottomX - x;
		int i3 = x + y * width;
		for (int j3 = 0; j3 < lineWidth; j3++) {
			int alpha2 = (lineWidth-j3)/(lineWidth/alpha);
			int j1 = 256 - alpha2;
			int k1 = (color >> 16 & 0xff) * alpha2;
			int l1 = (color >> 8 & 0xff) * alpha2;
			int i2 = (color & 0xff) * alpha2;
			int j2 = (pixels[i3] >> 16 & 0xff) * j1;
			int k2 = (pixels[i3] >> 8 & 0xff) * j1;
			int l2 = (pixels[i3] & 0xff) * j1;
			int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8)
					+ (i2 + l2 >> 8);
			pixels[i3++] = k3;
		}
	}

	public static void resetImage()
	{
		int i = width * height;
		for(int j = 0; j < i; j++)
			pixels[j] = 0;

	}
	public static void drawVLine(int x, int y, int height, int colour)
	{
		if(x < topX || x >= bottomY)
			return;
		if(y < topY)
		{
			height -= topY - y;
			y = topY;
		}
		if(y + height > bottomX)
			height = bottomX - y;
		int ptr = x + y * width;
		for(int y_off = 0; y_off < height; y_off++)
			pixels[ptr + y_off * width] = colour;

	}

	public static void blur(int x, int y, int width, int height, int speed)
	{
		try {
		blur(pixels, pixels, width, height, speed);
		blur(pixels, pixels, height, width, speed);
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void drawHLine(int x, int y, int width, int colour)
	{
		if(y < topY || y >= bottomY)
			return;
		if(x < topX)
		{
			width -= topX - x;
			x = topX;
		}
		if(x + width > bottomX)
			width = bottomX - x;
		int i1 = x + y * DrawingArea.width;
		for(int x_off = 0; x_off < width; x_off++)
			pixels[i1 + x_off] = colour;

	}
	public static void fillRectangle(int color, int y, int widthz, int heightz, int opacity, int x)
	{
		if(x < topX)
		{
			widthz -= topX - x;
			x = topX;
		}
		if(y < topY)
		{
			heightz -= topY - y;
			y = topY;
		}
		if(x + widthz > bottomX)
			widthz = bottomX - x;
		if(y + heightz > bottomY)
			heightz = bottomY - y;
		int decodedOpacity = 256 - opacity;
		int red = (color >> 16 & 0xff) * opacity;
		int green = (color >> 8 & 0xff) * opacity;
		int blue = (color & 0xff) * opacity;
		int pixelWidthStep = width - widthz;
		int startPixel = x + y * width;
		for(int h = 0; h < heightz; h++)
		{
			for(int w = -widthz; w < 0; w++)
			{
				int pixelRed = (pixels[startPixel] >> 16 & 0xff) * decodedOpacity;
				int pixelBlue = (pixels[startPixel] >> 8 & 0xff) * decodedOpacity;
				int pixelGreen = (pixels[startPixel] & 0xff) * decodedOpacity;
				int pixelRGB = ((red + pixelRed >> 8) << 16) + ((green + pixelBlue >> 8) << 8) + (blue + pixelGreen >> 8);
				pixels[startPixel++] = pixelRGB;
			}

			startPixel += pixelWidthStep;
		}
	}
	public static void drawPixels(int height_, int yPos, int xPos, int color, int width_, int alpha)
	{
		if(xPos < topX)
		{
			width_ -= topX - xPos;
			xPos = topX;
		}
		if(yPos < topY)
		{
			height_ -= topY - yPos;
			yPos = topY;
		}
		if(xPos + width_ > bottomX)
			width_ = bottomX - xPos;
		if(yPos + height_ > bottomY)
			height_ = bottomY - yPos;
		int decodedOpacity = 256 - alpha;
		int widthPixelStep = width - width_;
		int startPixel = xPos + yPos * width;
		int red = (color >> 16 & 0xff) * alpha;
		int green = (color >> 8 & 0xff) * alpha;
		int blue = (color & 0xff) * alpha;
		for(int i2 = -height_; i2 < 0; i2++)
		{
			for(int j2 = -width_; j2 < 0; j2++) {
				int pixelRed = (pixels[startPixel] >> 16 & 0xff) * decodedOpacity;
				int pixelBlue = (pixels[startPixel] >> 8 & 0xff) * decodedOpacity;
				int pixelGreen = (pixels[startPixel] & 0xff) * decodedOpacity;
				int pixelRGB = ((red + pixelRed >> 8) << 16) + ((green + pixelBlue >> 8) << 8) + (blue + pixelGreen >> 8);
				pixels[startPixel++] = pixelRGB;
			}

			startPixel += widthPixelStep;
		}

	}

	public static void drawPixels(int height_, int yPos, int xPos, int color, int width_)
	{
		if(xPos < topX)
		{
			width_ -= topX - xPos;
			xPos = topX;
		}
		if(yPos < topY)
		{
			height_ -= topY - yPos;
			yPos = topY;
		}
		if(xPos + width_ > bottomX)
			width_ = bottomX - xPos;
		if(yPos + height_ > bottomY)
			height_ = bottomY - yPos;
		int k1 = width - width_;
		int l1 = xPos + yPos * width;
		for(int i2 = -height_; i2 < 0; i2++)
		{
			for(int j2 = -width_; j2 < 0; j2++)
				pixels[l1++] = color;

			l1 += k1;
		}

	}

	public static void fillPixels(int i, int j, int k, int l, int i1)
	{
		drawLine(i1, l, j, i);
		drawLine((i1 + k) - 1, l, j, i);
		drawLineVertical(i1, l, k, i);
		drawLineVertical(i1, l, k, (i + j) - 1);
	}

	public static void drawRectangle(int y, int height, int opacity, int color, int width, int x)
	{
		drawHLine(color, width, y, opacity, x);
		drawHLine(color, width, (y + height) - 1, opacity, x);
		if(height >= 3)
		{
			drawVLine(color, x, opacity, y + 1, height - 2);
			drawVLine(color, (x + width) - 1, opacity, y + 1, height - 2);
		}
	}

	public static void drawLine(int yPos, int color, int widthToDraw, int xPos)
	{
		if(yPos < topY || yPos >= bottomY)
			return;
		if(xPos < topX)
		{
			widthToDraw -= topX - xPos;
			xPos = topX;
		}
		if(xPos + widthToDraw > bottomX)
			widthToDraw = bottomX - xPos;
		int base = xPos + yPos * width;
		for(int ptr = 0; ptr < widthToDraw; ptr++)
			pixels[base + ptr] = color;

	}

	public static void drawHorizontalLine(int i, int j, int k, int l) {
		if (i < topY || i >= bottomY)
			return;
		if (l < topX) {
			k -= topX - l;
			l = topX;
		}
		if (l + k > bottomX)
			k = bottomX - l;
		int i1 = l + i * width;
		for (int j1 = 0; j1 < k; j1++)
			pixels[i1 + j1] = j;

	}
	
	protected static void drawHLine(int i, int j, int k, int l, int i1)
	{
		if(k < topY || k >= bottomY)
			return;
		if(i1 < topX)
		{
			j -= topX - i1;
			i1 = topX;
		}
		if(i1 + j > bottomX)
			j = bottomX - i1;
		int j1 = 256 - l;
		int k1 = (i >> 16 & 0xff) * l;
		int l1 = (i >> 8 & 0xff) * l;
		int i2 = (i & 0xff) * l;
		int i3 = i1 + k * width;
		for(int j3 = 0; j3 < j; j3++)
		{
			int j2 = (pixels[i3] >> 16 & 0xff) * j1;
			int k2 = (pixels[i3] >> 8 & 0xff) * j1;
			int l2 = (pixels[i3] & 0xff) * j1;
			int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8) + (i2 + l2 >> 8);
			pixels[i3++] = k3;
		}

	}

	public static void drawLineVertical(int heights, int color, int yPos, int xPos)
	{
		if(xPos < topX || xPos >= bottomX)
			return;
		if(heights < topY)
		{
			yPos -= topY - heights;
			heights = topY;
		}
		if(heights + yPos > bottomY)
			yPos = bottomY - heights;
		int j1 = xPos + heights * width;
		for(int k1 = 0; k1 < yPos; k1++)
			pixels[j1 + k1 * width] = color;

	}

	protected static void drawVLine(int i, int j, int k, int l, int i1) {
		if(j < topX || j >= bottomX)
			return;
		if(l < topY) {
			i1 -= topY - l;
			l = topY;
		}
		if(l + i1 > bottomY)
			i1 = bottomY - l;
		int j1 = 256 - k;
		int k1 = (i >> 16 & 0xff) * k;
		int l1 = (i >> 8 & 0xff) * k;
		int i2 = (i & 0xff) * k;
		int i3 = j + l * width;
		for(int j3 = 0; j3 < i1; j3++) {
			int j2 = (pixels[i3] >> 16 & 0xff) * j1;
			int k2 = (pixels[i3] >> 8 & 0xff) * j1;
			int l2 = (pixels[i3] & 0xff) * j1;
			int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8) + (i2 + l2 >> 8);
			pixels[i3] = k3;
			i3 += width;
		}
	}

	public static void fillCircle(int posX, int posY, int radius, int colour, int alpha)
	{
		int dest_intensity = 256 - alpha;
		int src_red = (colour >> 16 & 0xff) * alpha;
		int src_green = (colour >> 8 & 0xff) * alpha;
		int src_blue = (colour & 0xff) * alpha;
		int radiusPixels = posY - radius;
		if(radiusPixels < 0)
			radiusPixels = 0;
		int max = posY + radius;
		if(max >= height)
			max = height - 1;
		for(int y = radiusPixels; y <= max; y++)
		{
			int beginYPos = y - posY;
			int heighestXPos = (int)Math.sqrt(radius * radius - beginYPos * beginYPos);
			int x = posX - heighestXPos;
			if(x < 0)
				x = 0;
			int maxPixelX = posX + heighestXPos;
			if(maxPixelX >= width)
				maxPixelX = width - 1;
			int pixel_offset = x + y * width;
			for(int pixelX = x; pixelX <= maxPixelX; pixelX++)
			{
				int dest_red = (pixels[pixel_offset] >> 16 & 0xff) * dest_intensity;
				int dest_green = (pixels[pixel_offset] >> 8 & 0xff) * dest_intensity;
				int dest_blue = (pixels[pixel_offset] & 0xff) * dest_intensity;
				int result_rgb = ((src_red + dest_red >> 8) << 16) + ((src_green + dest_green >> 8) << 8) + (src_blue + dest_blue >> 8);
				pixels[pixel_offset++] = result_rgb;
			}

		}

	}
    
	public DrawingArea() {
		
	}

	public static int pixels[];
	public static float depthBuffer[];
	public static int width;
	public static int height;
	public static int topY;
	public static int bottomY;
	public static int topX;
	public static int bottomX;
	public static int viewportRX;
	public static int viewport_centerX;
	public static int viewport_centerY;
}
