
public class WorldMapObjectData {

	public static void loadLocData(byte locData[], byte wallDrawTypeData[][], byte objectIconData[][], byte mapIconData[][])//for the white outlines
	{
		for(int i = 0; i < locData.length;)
		{
			int k = (locData[i++] & 0xff) * 64 - instance.baseX;
			int l = (locData[i++] & 0xff) * 64 - instance.baseY;
			if(k > 0 && l > 0 && k + 64 < instance.mapWidth && l + 64 < instance.mapHeight)
			{
				for(int i1 = 0; i1 < 64; i1++)
				{
					byte wallDrawType[] = wallDrawTypeData[i1 + k];
					byte objectIcon[] = objectIconData[i1 + k];
					byte mapIcon[] = mapIconData[i1 + k];
					int l1 = instance.mapHeight - l - 1;
					for(int i2 = -64; i2 < 0; i2++)
					{
						do
						{
							int j = locData[i++] & 0xff;
							if(j == 0)
								break;
							if(j < 29)
							{	wallDrawType[l1] = (byte)j;
							}
							else
							if(j < 160)
							{
								objectIcon[l1] = (byte)(j - 28);
							} else
							{
								mapIcon[l1] = (byte)(j - 159);
								instance.targetingWindowX[instance.targetingConfigPointer] = i1 + k;
								instance.targetingWindowY[instance.targetingConfigPointer] = l1;
								instance.targetingIconID[instance.targetingConfigPointer] = j - 160;
								instance.targetingConfigPointer++;
								}
						} while(true);
						l1--;
					}

				}

			} else
			{
				for(int j1 = 0; j1 < 64; j1++)
				{
					for(int k1 = -64; k1 < 0; k1++)
					{
						byte byte0;
						do
							byte0 = locData[i++];
						while(byte0 != 0);
					}

				}

			}
		}

	}
	
	public static WorldMap instance;
}
