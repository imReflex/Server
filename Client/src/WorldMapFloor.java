


public class WorldMapFloor {

	public static void loadUnderlayData(byte underlayData[], byte abyte1[][])//underlays are like the ground, sand etc
	{
		for(int i = 0; i < underlayData.length;)
		{
			int j = (underlayData[i++] & 0xff) * 64 - instance.baseX;
			int k = (underlayData[i++] & 0xff) * 64 - instance.baseY;
			if(j > 0 && k > 0 && j + 64 < instance.mapWidth && k + 64 < instance.mapHeight)
			{
				for(int l = 0; l < 64; l++)
				{
					byte abyte2[] = abyte1[l + j];
					int i1 = instance.mapHeight - k - 1;
					for(int j1 = -64; j1 < 0; j1++)
						abyte2[i1--] = underlayData[i++];

				}

			} else
			{
				i += 4096;
			}
		}

	}

	public static void loadOverlayData(byte overlayData[], int ai[][], byte abyte1[][])//overlays are like water and lava
	{
		for(int i = 0; i < overlayData.length;)
		{
			int j = (overlayData[i++] & 0xff) * 64 - instance.baseX;
			int k = (overlayData[i++] & 0xff) * 64 - instance.baseY;
			if(j > 0 && k > 0 && j + 64 < instance.mapWidth && k + 64 < instance.mapHeight)
			{
				for(int l = 0; l < 64; l++)
				{
					int ai1[] = ai[l + j];
					byte abyte2[] = abyte1[l + j];
					int j1 = instance.mapHeight - k - 1;
					for(int k1 = -64; k1 < 0; k1++)
					{
						byte byte0 = overlayData[i++];
						if(byte0 != 0)
						{
							abyte2[j1] = overlayData[i++];
							int l1 = 0;
							if(byte0 > 0)
								l1 = instance.floorOverlayColour[byte0];
							ai1[j1--] = l1;
						} else
						{
							ai1[j1--] = 0;
						}
					}

				}

			} else
			{
				for(int i1 = -4096; i1 < 0; i1++)
				{
					byte byte1 = overlayData[i++];
					if(byte1 != 0)
						i++;
				}

			}
		}

	}

	public static void processUnderlays(byte underlayColours[][], int processedUnderlayData[][])
	{//draw underlays?
		int i = instance.mapWidth;
		int j = instance.mapHeight;
		int ai1[] = new int[j];
		for(int k = 0; k < j; k++)
			ai1[k] = 0;

		for(int l = 5; l < i - 5; l++)
		{
			byte abyte1[] = underlayColours[l + 5];
			byte abyte2[] = underlayColours[l - 5];
			for(int i1 = 0; i1 < j; i1++)
				ai1[i1] += instance.floorColour[abyte1[i1] & 0xff] - instance.floorColour[abyte2[i1] & 0xff];

			if(l > 10 && l < i - 10)
			{
				int j1 = 0;
				int k1 = 0;
				int l1 = 0;
				int ai2[] = processedUnderlayData[l];
				for(int i2 = 5; i2 < j - 5; i2++)
				{
					int j2 = ai1[i2 - 5];
					int k2 = ai1[i2 + 5];
					j1 += (k2 >> 20) - (j2 >> 20);
					k1 += (k2 >> 10 & 0x3ff) - (j2 >> 10 & 0x3ff);
					l1 += (k2 & 0x3ff) - (j2 & 0x3ff);
					if(l1 > 0)
						ai2[i2] = calculatePalette((double)j1 / 8533D, (double)k1 / 8533D, (double)l1 / 8533D);
				}

			}
		}

	}

	public static int calculatePalette(double d, double d1, double d2)
	{
		double r = d2;
		double g = d2;
		double b = d2;
		if(d1 != 0.0D)
		{
			double d6;
			if(d2 < 0.5D)
				d6 = d2 * (1.0D + d1);
			else
				d6 = (d2 + d1) - d2 * d1;
			double d7 = 2D * d2 - d6;
			double d8 = d + 0.33333333333333331D;
			if(d8 > 1.0D)
				d8--;
			double d9 = d;
			double d10 = d - 0.33333333333333331D;
			if(d10 < 0.0D)
				d10++;
			
			if(6D * d8 < 1.0D)
				r = d7 + (d6 - d7) * 6D * d8;
			else
			if(2D * d8 < 1.0D)
				r = d6;
			else
			if(3D * d8 < 2D)
				r = d7 + (d6 - d7) * (0.66666666666666663D - d8) * 6D;
			else
				r = d7;
			
			if(6D * d9 < 1.0D)
				g = d7 + (d6 - d7) * 6D * d9;
			else
			if(2D * d9 < 1.0D)
				g = d6;
			else
			if(3D * d9 < 2D)
				g = d7 + (d6 - d7) * (0.66666666666666663D - d9) * 6D;
			else
				g = d7;
			
			if(6D * d10 < 1.0D)
				b = d7 + (d6 - d7) * 6D * d10;
			else
			if(2D * d10 < 1.0D)
				b = d6;
			else
			if(3D * d10 < 2D)
				b = d7 + (d6 - d7) * (0.66666666666666663D - d10) * 6D;
			else
				b = d7;
		}
		int byteR = (int)(r * 256D);
		int byteG = (int)(g * 256D);
		int byteB = (int)(b * 256D);
		int rgb = (byteR << 16) + (byteG << 8) + byteB;
		return rgb;
	}
	
	public static WorldMap instance;
}
