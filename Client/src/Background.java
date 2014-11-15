

public final class Background extends DrawingArea {

	public Background(CacheArchive streamLoader, String s, int i)
	{
		Stream stream = new Stream(streamLoader.getDataForName(s + ".dat"));
		Stream stream_1 = new Stream(streamLoader.getDataForName("index.dat"));
		stream_1.currentOffset = stream.readUnsignedWord();
		libWidth = stream_1.readUnsignedWord();
		libHeight = stream_1.readUnsignedWord();
		int j = stream_1.readUnsignedByte();
		palette = new int[j];
		for(int k = 0; k < j - 1; k++)
			palette[k + 1] = stream_1.read3Bytes();

		for(int l = 0; l < i; l++)
		{
			stream_1.currentOffset += 2;
			stream.currentOffset += stream_1.readUnsignedWord() * stream_1.readUnsignedWord();
			stream_1.currentOffset++;
		}

		xDrawOffset = stream_1.readUnsignedByte();
		yDrawOffset = stream_1.readUnsignedByte();
		imgWidth = stream_1.readUnsignedWord();
		imgHeight = stream_1.readUnsignedWord();
		int type = stream_1.readUnsignedByte();
		int j1 = imgWidth * imgHeight;
		imgPixels = new byte[j1];
		if(type == 0)
		{
			for(int k1 = 0; k1 < j1; k1++)
				imgPixels[k1] = stream.readSignedByte();

			return;
		}
		if(type == 1)
		{
			for(int l1 = 0; l1 < imgWidth; l1++)
			{
				for(int i2 = 0; i2 < imgHeight; i2++)
					imgPixels[l1 + i2 * imgWidth] = stream.readSignedByte();

			}

		}
	}

	public void reduceSetOffset()
	{
		libWidth /= 2;
		libHeight /= 2;
		byte pixelBuffer[] = new byte[libWidth * libHeight];
		int i = 0;
		for(int j = 0; j < imgHeight; j++)
		{
			for(int k = 0; k < imgWidth; k++)
				pixelBuffer[(k + xDrawOffset >> 1) + (j + yDrawOffset >> 1) * libWidth] = imgPixels[i++];

		}

		imgPixels = pixelBuffer;
		imgWidth = libWidth;
		imgHeight = libHeight;
		xDrawOffset = 0;
			yDrawOffset = 0;
	}

	public void setOffset()
	{
		if(imgWidth == libWidth && imgHeight == libHeight)
			return;
		byte pixelBuffer[] = new byte[libWidth * libHeight];
		int i = 0;
		for(int y = 0; y < imgHeight; y++)
		{
			for(int x = 0; x < imgWidth; x++)
				pixelBuffer[x + xDrawOffset + (y + yDrawOffset) * libWidth] = imgPixels[i++];

		}

		imgPixels = pixelBuffer;
		imgWidth = libWidth;
		imgHeight = libHeight;
		xDrawOffset = 0;
		yDrawOffset = 0;
	}

	public void spriteClip(int x, int y, int stretchWidth, int stretchHeight)
	{
		try
		{
			int width = imgWidth;
			int height = imgHeight;
			int k1 = 0;
			int l1 = 0;
			int i2 = (width << 16) / stretchWidth;
			int j2 = (height << 16) / stretchHeight;
			int k2 = libWidth;
			int l2 = libHeight;
			i2 = (k2 << 16) / stretchWidth;
			j2 = (l2 << 16) / stretchHeight;
			x += ((xDrawOffset * stretchWidth + k2) - 1) / k2;
			y += ((yDrawOffset * stretchHeight + l2) - 1) / l2;
			if((xDrawOffset * stretchWidth) % k2 != 0)
				k1 = (k2 - (xDrawOffset * stretchWidth) % k2 << 16) / stretchWidth;
			if((yDrawOffset * stretchHeight) % l2 != 0)
				l1 = (l2 - (yDrawOffset * stretchHeight) % l2 << 16) / stretchHeight;
			stretchWidth = (stretchWidth * (imgWidth - (k1 >> 16))) / k2;
			stretchHeight = (stretchHeight * (imgHeight - (l1 >> 16))) / l2;
			int i3 = x + y * DrawingArea.width;
			int j3 = DrawingArea.width - stretchWidth;
			if(y < DrawingArea.topY)
			{
				int k3 = DrawingArea.topY - y;
				stretchHeight -= k3;
				y = 0;
				i3 += k3 * DrawingArea.width;
				l1 += j2 * k3;
			}
			if(y + stretchHeight > DrawingArea.bottomX)
				stretchHeight -= (y + stretchHeight) - DrawingArea.bottomX;
			if(x < DrawingArea.topX)
			{
				int width2 = DrawingArea.topX - x;
				stretchWidth -= width2;
				x = 0;
				i3 += width2;
				k1 += i2 * width2;
				j3 += width2;
			}
			if(x + stretchWidth > DrawingArea.bottomY)
			{
				int i4 = (x + stretchWidth) - DrawingArea.bottomY;
				stretchWidth -= i4;
				j3 += i4;
			}
			plotScale(DrawingArea.pixels, imgPixels, palette, k1, l1, i3, j3, stretchWidth, stretchHeight, i2, j2, width);
		}
		catch(Exception exception)
		{
			System.out.println("error in sprite clipping routine");
		}
	}

	public void plotScale(int pixels[], byte imgPixels[], int palette[], int i, int j, int k, int l, 
			int stretchWidth, int stretchHeight, int k1, int l1, int width)
	{
		try
		{
			int j2 = i;
			for(int k2 = -stretchHeight; k2 < 0; k2++)
			{
				int l2 = (j >> 16) * width;
				for(int i3 = -stretchWidth; i3 < 0; i3++)
				{
					byte byte0 = imgPixels[(i >> 16) + l2];
					if(byte0 != 0)
						pixels[k++] = palette[byte0 & 0xff];
					else
						k++;
					i += k1;
				}

				j += l1;
				i = j2;
				k += l;
			}

		}
		catch(Exception exception)
		{
			System.out.println("error in plot_scale");
		}
	}

	public void decodePalette(int r, int g, int b)
	{
		for(int i1 = 0; i1 < palette.length; i1++)
		{
			int j1 = palette[i1] >> 16 & 0xff;
			j1 += r;
			if(j1 < 0)
				j1 = 0;
			else
			if(j1 > 255)
				j1 = 255;
			int k1 = palette[i1] >> 8 & 0xff;
			k1 += g;
			if(k1 < 0)
				k1 = 0;
			else
			if(k1 > 255)
				k1 = 255;
			int l1 = palette[i1] & 0xff;
			l1 += b;
			if(l1 < 0)
				l1 = 0;
			else
			if(l1 > 255)
				l1 = 255;
			palette[i1] = (j1 << 16) + (k1 << 8) + l1;
		}
	}

	public void drawBackground(int i, int k)
	{
		i += xDrawOffset;
		k += yDrawOffset;
		int l = i + k * DrawingArea.width;
		int i1 = 0;
		int j1 = imgHeight;
		int k1 = imgWidth;
		int l1 = DrawingArea.width - k1;
		int i2 = 0;
		if(k < DrawingArea.topY)
		{
			int j2 = DrawingArea.topY - k;
			j1 -= j2;
			k = DrawingArea.topY;
			i1 += j2 * k1;
			l += j2 * DrawingArea.width;
		}
		if(k + j1 > DrawingArea.bottomY)
			j1 -= (k + j1) - DrawingArea.bottomY;
		if(i < DrawingArea.topX)
		{
			int k2 = DrawingArea.topX - i;
			k1 -= k2;
			i = DrawingArea.topX;
			i1 += k2;
			l += k2;
			i2 += k2;
			l1 += k2;
		}
		if(i + k1 > DrawingArea.bottomX)
		{
			int l2 = (i + k1) - DrawingArea.bottomX;
			k1 -= l2;
			i2 += l2;
			l1 += l2;
		}
		if(!(k1 <= 0 || j1 <= 0))
		{
			drawPixels(j1, DrawingArea.pixels, imgPixels, l1, l, k1, i1, palette, i2);
		}
	}

	private void drawPixels(int i, int ai[], byte abyte0[], int j, int k, int l,
						   int i1, int ai1[], int j1)
	{
		int k1 = -(l >> 2);
		l = -(l & 3);
		for(int l1 = -i; l1 < 0; l1++)
		{
			for(int i2 = k1; i2 < 0; i2++)
			{
				byte byte1 = abyte0[i1++];
				if(byte1 != 0)
					ai[k++] = ai1[byte1 & 0xff];
				else
					k++;
				byte1 = abyte0[i1++];
				if(byte1 != 0)
					ai[k++] = ai1[byte1 & 0xff];
				else
					k++;
				byte1 = abyte0[i1++];
				if(byte1 != 0)
					ai[k++] = ai1[byte1 & 0xff];
				else
					k++;
				byte1 = abyte0[i1++];
				if(byte1 != 0)
					ai[k++] = ai1[byte1 & 0xff];
				else
					k++;
			}

			for(int j2 = l; j2 < 0; j2++)
			{
				byte byte2 = abyte0[i1++];
				if(byte2 != 0)
					ai[k++] = ai1[byte2 & 0xff];
				else
					k++;
			}

			k += j;
			i1 += j1;
		}

	}

	public byte imgPixels[];
	public final int[] palette;
	public int imgWidth;
	public int imgHeight;
	public int xDrawOffset;
	public int yDrawOffset;
	public int libWidth;
	private int libHeight;
}
