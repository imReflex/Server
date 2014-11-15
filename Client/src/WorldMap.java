@SuppressWarnings("all")
public class WorldMap
{

   	public int brownTopLeftOutlineColour = 0x887755;
	public int brownButtonColour = 0x776644;
	public int brownBottemRightOutlineColour = 0x665533;
	public int redTopLeftOutlineColour = 0xaa0000;
	public int redButtonColour = 0x990000;
	public int redBottemRightOutlineColour = 0x880000;

	public int[] mapIconX = new int[2000];
	public int[] mapIconY = new int[2000];
	public int[] mapIconIDs = new int[2000];
	public int[] targetingWindowX = new int[2000];
	public int[] targetingWindowY = new int[2000];
	public int[] targetingIconID = new int[2000];

	public int x = 5;
	public int y = 13;
	public int width = 140;
	public int height = 470;
	public boolean keyOpen = false;
	public int keyItemIndex = -1;
	public int lastClickedKeyItem = -1;
	public boolean mapTargeterOpen = true;
	public int maxLabels = 1000;
	public String[] labelString = new String[maxLabels];
	public int[] labelXPos = new int[maxLabels];
	public int[] labelYPos = new int[maxLabels];
	public int[] labelDisplayType = new int[maxLabels];
	public double currentZoomLevel = 4D;
	public double endZoomLevel = 4D;
	public CacheArchive archive;
	public int baseX;
	public int baseY;
	public int mapWidth;
	public int mapHeight;
	public int floorColour[];
	public int floorOverlayColour[];
	public int processedUnderlayColours[][];
	public int processedOverlayColours[][];
	public byte processedOverlayShapeData[][];
	public byte wallDrawTypeData[][];
	public byte mapIconData[][];
	public byte objectIconData[][];
	public TypeFace boldFont;
	public TypeFace size11Font;
	public TypeFace size12Font;
	public TypeFace size14Font;
	public TypeFace size17Font;
	public TypeFace size19Font;
	public TypeFace size22Font;
	public TypeFace size26Font;
	public TypeFace size30Font;
	public int targetingConfigPointer;
	public int itemKeyAtTopOfPage;
	public int mapKeyIndex;
	public int loopCycle;
	public int targeterWindowHeight;
	public int targeterWindowWidth;
	public int targeterWindowX;
	public int targeterWindowY;
	public Sprite targeterWindow;
	public int clickX;
	public int clickY;
	public int mapPosXb;
	public int mapPosYb;
	public int labelCount;
	public int mapPosX;
	public int mapPosY;
	public boolean redrawMap = true;
	public String iconExplainers[] = {
			"General Store", "Sword Shop", "Magic Shop", "Axe Shop", "Helmet Shop", "Bank", "Quest Start", "Amulet Shop", "Mining Site", "Furnace", 
			"Anvil", "Combat Training", "Dungeon", "Staff Shop", "Platebody Shop", "Platelegs Shop", "Scimitar Shop", "Archery Shop", "Shield Shop", "Altar", 
			"Herbalist", "Jewelery", "Gem Shop", "Crafting Shop", "Candle Shop", "Fishing Shop", "Fishing Spot", "Clothes Shop", "Apothecary", "Silk Trader", 
			"Kebab Seller", "Pub/Bar", "Mace Shop", "Tannery", "Rare Trees", "Spinning Wheel", "Food Shop", "Cookery Shop", "Mini-Game", "Water Source", 
			"Cooking Range", "Skirt Shop", "Potters Wheel", "Windmill", "Mining Shop", "Chainmail Shop", "Silver Shop", "Fur Trader", "Spice Shop", "Agility Training", 
			"Vegetable Store", "Slayer Master", "Hair Dressers", "Farming patch", "Makeover Mage", "Guide", "Transportation", "???", "Farming shop", "Loom", 
			"Brewery", "1", "2", "3", "4", "5", "6", "7", "8", "9",//limit

		};
	
	
	public WorldMap()
	{
		startUp();
	}
	
	public boolean loaded = false;
	
	public void startUp()
	{
		try {
			WorldMap.instance = this;
			WorldMapObjectData.instance = this;
			WorldMapFloor.instance = this;
			archive = WorldArchive.loadWorldMap();
			Stream stream = new Stream(archive.getDataForName("size.dat"));
			baseX = stream.readShort();
			baseY = stream.readShort();
			mapWidth = stream.readShort();
			mapHeight = stream.readShort();
			mapPosX = 3200 - baseX;
			mapPosY = (baseY + mapHeight) - 3200;
			targeterWindowHeight = 180;
			targeterWindowWidth = (mapWidth * targeterWindowHeight) / mapHeight;
			targeterWindowX = Client.clientWidth - targeterWindowWidth - 5;
			targeterWindowY = Client.clientHeight - targeterWindowHeight - 20;
			stream = new Stream(archive.getDataForName("labels.dat"));
			labelCount = stream.readShort();
			for(int i = 0; i < labelCount; i++) {
				labelString[i] = stream.readString();
				labelXPos[i] = stream.readShort();
				labelYPos[i] = stream.readShort();
				labelDisplayType[i] = stream.readUnsignedByte();//0 if small white text, 1 if large white text, 2 if large orange text
			}

			stream = new Stream(archive.getDataForName("floorcol.dat"));
			int length = stream.readShort();
			floorColour = new int[length + 1];
			floorOverlayColour = new int[length + 1];
			for(int k = 0; k < length; k++) {
				floorColour[k + 1] = stream.readInt();
				floorOverlayColour[k + 1] = stream.readInt();
			}

			byte underlayData[] = archive.getDataForName("underlay.dat");
			byte loadedUnderlayData[][] = new byte[mapWidth][mapHeight];
			WorldMapFloor.loadUnderlayData(underlayData, loadedUnderlayData);
			byte overlayData[] = archive.getDataForName("overlay.dat");
			processedOverlayColours = new int[mapWidth][mapHeight];
			processedOverlayShapeData = new byte[mapWidth][mapHeight];
			WorldMapFloor.loadOverlayData(overlayData, processedOverlayColours, processedOverlayShapeData);
			
			byte locData[] = archive.getDataForName("loc.dat");
			wallDrawTypeData = new byte[mapWidth][mapHeight];
			wallDrawTypeData = new byte[mapWidth][mapHeight];
			objectIconData = new byte[mapWidth][mapHeight];
			mapIconData = new byte[mapWidth][mapHeight];
			WorldMapObjectData.loadLocData(locData, wallDrawTypeData, objectIconData, mapIconData);
			boldFont = new TypeFace(false, "b12_full", archive);
			size11Font = boldFont;
			size12Font = boldFont;
			size14Font = boldFont;
			size17Font = boldFont;
			size19Font = boldFont;
			size22Font = boldFont;
			size26Font = boldFont;
			size30Font = boldFont;
			processedUnderlayColours = new int[mapWidth][mapHeight];
			WorldMapFloor.processUnderlays(loadedUnderlayData, processedUnderlayColours);
			targeterWindow = new Sprite(targeterWindowWidth, targeterWindowHeight);
			targeterWindow.method343();
			WorldMap.drawMap(0, 0, mapWidth, mapHeight, 0, 0, targeterWindowWidth, targeterWindowHeight);//the map scroll window
			loaded = true;
		} catch(Exception exception) {
			System.out.println("[CLIENT]: loaderror occured - startUp()");
			exception.printStackTrace();
		}
	}
	
	public void processLoop()
	{
		//clientWidth = clientInstance.applet.getFrameWidth();
		//clientHeight = super.applet.getFrameHeight();
		if(clientInstance.keyArray[1] == 1) {
			mapPosX = (int)((double)mapPosX - 16D / currentZoomLevel);
			redrawMap = true;
		}
		if(clientInstance.keyArray[2] == 1) {
			mapPosX = (int)((double)mapPosX + 16D / currentZoomLevel);
			redrawMap = true;
		}
		if(clientInstance.keyArray[3] == 1) {
			mapPosY = (int)((double)mapPosY - 16D / currentZoomLevel);
			redrawMap = true;
		}
		if(clientInstance.keyArray[4] == 1) {
			mapPosY = (int)((double)mapPosY + 16D / currentZoomLevel);
			redrawMap = true;
		}/*
		for(int charCode = 1; charCode > 0;)
		{
			charCode = clientInstance.readChar(-796);
			if(charCode == 49)//number 1
			{
				endZoomLevel = 3D;
				redrawMap = true;
			}
			if(charCode == 50)//number 2
			{
				endZoomLevel = 4D;
				redrawMap = true;
			}
			if(charCode == 51)//number 3
			{
				endZoomLevel = 6D;
				redrawMap = true;
			}
			if(charCode == 52)//number 4
			{
				endZoomLevel = 8D;
				redrawMap = true;
			}
			if(charCode == 107 || charCode == 75)//letter k
			{
				keyOpen = !keyOpen;
				redrawMap = true;
			}
			if(charCode == 111 || charCode == 79)//letter o
			{
				mapTargeterOpen = !mapTargeterOpen;
				redrawMap = true;
			}
		}*/

		if(clientInstance.clickType == RSApplet.LEFT)
		{
			clickX = clientInstance.clickX;
			clickY = clientInstance.clickY;
			mapPosXb = mapPosX;
			mapPosYb = mapPosY;
			if(clientInstance.clickX > 170 && clientInstance.clickX < 220 && clientInstance.clickY > Client.clientHeight - 55 && clientInstance.clickY < Client.clientHeight)//37%
			{
				endZoomLevel = 3D;
				clickX = -1;
			}
			if(clientInstance.clickX > 230 && clientInstance.clickX < 280 && clientInstance.clickY > Client.clientHeight - 55 && clientInstance.clickY < Client.clientHeight)//50%
			{
				endZoomLevel = 4D;
				clickX = -1;
			}
			if(clientInstance.clickX > 290 && clientInstance.clickX < 340 && clientInstance.clickY > Client.clientHeight - 55 && clientInstance.clickY < Client.clientHeight)//75%
			{
				endZoomLevel = 6D;
				clickX = -1;
			}
			if(clientInstance.clickX > 350 && clientInstance.clickX < 400 && clientInstance.clickY > Client.clientHeight - 55 && clientInstance.clickY < Client.clientHeight)//100%
			{
				endZoomLevel = 8D;
				clickX = -1;
			}
			if(clientInstance.clickX > x && clientInstance.clickY > /*y + height*/targeterWindowY + targeterWindowHeight && clientInstance.clickX < x + width && clientInstance.clickY < Client.clientHeight)
			{
				keyOpen = !keyOpen;
				clickX = -1;
			}
			if(clientInstance.clickX > targeterWindowX && clientInstance.clickY > targeterWindowY + targeterWindowHeight && clientInstance.clickX < targeterWindowX + targeterWindowWidth && clientInstance.clickY < Client.clientHeight)
			{
				mapTargeterOpen = !mapTargeterOpen;
				clickX = -1;
			}
			if(keyOpen)
			{
				if(clientInstance.clickX > x && clientInstance.clickY > y && clientInstance.clickX < x + width && clientInstance.clickY < /*y + height*/targeterWindowY + targeterWindowHeight)
					clickX = -1;
				if(clientInstance.clickX > x && clientInstance.clickY > y && clientInstance.clickX < x + width && clientInstance.clickY < y + 18 && mapKeyIndex > 0)
					mapKeyIndex -= 25;//page up
				if(clientInstance.clickX > x && clientInstance.clickY > (/*y + height*/targeterWindowY + targeterWindowHeight) - 18 && clientInstance.clickX < x + width && clientInstance.clickY < /*y + height*/targeterWindowY + targeterWindowHeight && mapKeyIndex < 50)
					mapKeyIndex += 25;//page down
			}
			redrawMap = true;
		}
		if(keyOpen)
		{
			keyItemIndex = -1;
			if(clientInstance.mouseX > x && clientInstance.mouseX < x + width)
			{
				int j = y + 21 + 5;
				for(int j1 = 0; j1 < 25; j1++)
					if(j1 + itemKeyAtTopOfPage >= iconExplainers.length || !iconExplainers[j1 + itemKeyAtTopOfPage].equals("???"))
					{
						if(clientInstance.mouseY >= j && clientInstance.mouseY < j + 17)
						{
							keyItemIndex = j1 + itemKeyAtTopOfPage;
							if(clientInstance.clickType == RSApplet.RIGHT)
							{
								lastClickedKeyItem = j1 + itemKeyAtTopOfPage;
								loopCycle = 50;
							}
						}
						j += 17;
					}

			}
		}
		if((clientInstance.clickMode2 == 1 || clientInstance.clickType == RSApplet.RIGHT) && mapTargeterOpen)
		{
			int k = clientInstance.clickX;
			int k1 = clientInstance.clickY;
			if(clientInstance.clickMode2 == 1)
			{
				k = clientInstance.mouseX;
				k1 = clientInstance.mouseY;
			}
			if(k > targeterWindowX && k1 > targeterWindowY && k < targeterWindowX + targeterWindowWidth && k1 < targeterWindowY + targeterWindowHeight)
			{
				mapPosX = ((k - targeterWindowX) * mapWidth) / targeterWindowWidth;
				mapPosY = ((k1 - targeterWindowY) * mapHeight) / targeterWindowHeight;
				clickX = -1;
				redrawMap = true;
			}
		}
		if(clientInstance.clickMode2 == 1 && clickX != -1)
		{
			mapPosX = mapPosXb + (int)(((double)(clickX - clientInstance.mouseX) * 2D) / endZoomLevel);
			mapPosY = mapPosYb + (int)(((double)(clickY - clientInstance.mouseY) * 2D) / endZoomLevel);
			redrawMap = true;
		}
		if(currentZoomLevel < endZoomLevel)
		{
			redrawMap = true;
			currentZoomLevel += currentZoomLevel / 30D;
			if(currentZoomLevel > endZoomLevel)
				currentZoomLevel = endZoomLevel;
		}
		if(currentZoomLevel > endZoomLevel)
		{
			redrawMap = true;
			currentZoomLevel -= currentZoomLevel / 30D;
			if(currentZoomLevel < endZoomLevel)
				currentZoomLevel = endZoomLevel;
		}
		if(itemKeyAtTopOfPage < mapKeyIndex)
		{
			redrawMap = true;
			itemKeyAtTopOfPage++;
		}
		if(itemKeyAtTopOfPage > mapKeyIndex)
		{
			redrawMap = true;
			itemKeyAtTopOfPage--;
		}
		if(loopCycle > 0)
		{
			redrawMap = true;
			loopCycle--;
		}
		int l = mapPosX - (int)(Client.clientWidth / currentZoomLevel);
		int l1 = mapPosY - (int)(Client.clientHeight / currentZoomLevel);
		int i2 = mapPosX + (int)(Client.clientWidth / currentZoomLevel);
		int k2 = mapPosY + (int)(Client.clientHeight / currentZoomLevel);
		if(l < 48)
			mapPosX = 48 + (int)(Client.clientWidth / currentZoomLevel);
		if(l1 < 48)
			mapPosY = 48 + (int)(Client.clientHeight / currentZoomLevel);
		if(i2 > mapWidth - 48)
			mapPosX = mapWidth - 48 - (int)(Client.clientWidth / currentZoomLevel);
		if(k2 > mapHeight - 48)
			mapPosY = mapHeight - 48 - (int)(Client.clientHeight / currentZoomLevel);
	
	}
	
	public void processDrawing()
	{
		WorldMap.worldImageProcess();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void worldImageProcess() 
	{
		if(instance.redrawMap)
		{
			//instance.redrawMap = false;
			int i = instance.mapPosX - (int)(Client.clientWidth / instance.currentZoomLevel);
			int j = instance.mapPosY - (int)(Client.clientHeight / instance.currentZoomLevel);
			int k = instance.mapPosX + (int)(Client.clientWidth / instance.currentZoomLevel);
			int l = instance.mapPosY + (int)(Client.clientHeight / instance.currentZoomLevel);
			drawMap(i, j, k, l, 0, 0, Client.clientWidth, Client.clientHeight);
			if(instance.mapTargeterOpen)
			{
				instance.targeterWindow.drawSprite(instance.targeterWindowX, instance.targeterWindowY);
				DrawingArea.fillRectangle(
						instance.targeterWindowY + (instance.targeterWindowHeight * j) / instance.mapHeight, 
						((l - j) * instance.targeterWindowHeight) / instance.mapHeight,
						128, 0xff0000,
						((k - i) * instance.targeterWindowWidth) / instance.mapWidth, 
					instance.targeterWindowX + (instance.targeterWindowWidth * i) / instance.mapWidth
					
				);
				DrawingArea.drawRectangle(
						instance.targeterWindowY + (instance.targeterWindowHeight * j) / instance.mapHeight,
						((l - j) * instance.targeterWindowHeight) / instance.mapHeight,
						256, 0xff0000,
						((k - i) * instance.targeterWindowWidth) / instance.mapWidth,
					instance.targeterWindowX + (instance.targeterWindowWidth * i) / instance.mapWidth
				);
				if(Client.loopCycle > 0 && Client.loopCycle % 10 < 5)
				{
					for(int i1 = 0; i1 < instance.targetingConfigPointer; i1++)
						if(instance.targetingIconID[i1] == instance.lastClickedKeyItem)
						{
							int posX = instance.targeterWindowX + (instance.targeterWindowWidth * instance.targetingWindowX[i1]) 
								/ instance.mapWidth;
							int posY = instance.targeterWindowY + (instance.targeterWindowHeight * instance.targetingWindowY[i1]) 
								/ instance.mapHeight;
							DrawingArea.fillCircle(posX, posY, 2, 0xffff00, 256);
						}

				}
			}
			if(instance.keyOpen)
			{
				drawBoxedText(instance.x, instance.y, instance.width, 18, 0x999999, 0x777777, 0x555555, "Prev page");
				drawBoxedText(instance.x, instance.y + 18, instance.width, instance.height - 36, 0x999999, 0x777777, 0x555555, "");
				drawBoxedText(instance.x, (instance.y + instance.height) - 18, instance.width, 18, 0x999999, 0x777777, 0x555555, "Next page");
				int y2 = instance.y + 3 + 18;
				for(int currentKeyItem = 0; currentKeyItem < 25; currentKeyItem++)
				{
					if(currentKeyItem + instance.itemKeyAtTopOfPage < clientInstance.mapFunctions.length 
						&& currentKeyItem + instance.itemKeyAtTopOfPage < instance.iconExplainers.length)
					{
						if(instance.iconExplainers[currentKeyItem + instance.itemKeyAtTopOfPage].equals("???"))
							continue;
						clientInstance.mapFunctions[currentKeyItem + instance.itemKeyAtTopOfPage].drawAdvancedSprite(instance.x + 3, y2);
						Client.boldFont.drawStringCenter(0, instance.iconExplainers[currentKeyItem + instance.itemKeyAtTopOfPage], instance.x + 21, y2 + 14);
						int colour = 0xffffff;//standard colour
						if(instance.keyItemIndex == currentKeyItem + instance.itemKeyAtTopOfPage)
							colour = 0xbbaaaa;//highlight colour
						if(Client.loopCycle > 0 && Client.loopCycle % 10 < 5 && instance.lastClickedKeyItem == currentKeyItem + instance.itemKeyAtTopOfPage)
							colour = 0xffff00;//flashing colour
						Client.boldFont.drawStringCenter(colour, instance.iconExplainers[currentKeyItem + instance.itemKeyAtTopOfPage], instance.x + 20, y2 + 13);
					}
					y2 += 17;
				}

			}
			//drawBoxedText(instance.targeterWindowX, instance.targeterWindowY + instance.targeterWindowHeight, instance.targeterWindowWidth, 18, 
			//		instance.brownTopLeftOutlineColour, instance.brownButtonColour, instance.brownButtonColour, "Overview");
			
			//drawBoxedText(instance.x, /*instance.y + instance.height*/instance.targeterWindowY + instance.targeterWindowHeight, instance.width, 18, 
			//		instance.brownTopLeftOutlineColour, instance.brownButtonColour, instance.brownButtonColour, "Key");
			if(instance.endZoomLevel == 3D)
				drawBoxedText(170, Client.clientHeight - 31, 50, 30, 
					instance.redTopLeftOutlineColour, instance.redButtonColour, instance.redBottemRightOutlineColour, "37%");
			else
				drawBoxedText(170, Client.clientHeight - 31, 50, 30, 
					instance.brownTopLeftOutlineColour, instance.brownButtonColour, instance.brownButtonColour, "37%");
			if(instance.endZoomLevel == 4D)
				drawBoxedText(230, Client.clientHeight - 31, 50, 30, 
					instance.redTopLeftOutlineColour, instance.redButtonColour, instance.redBottemRightOutlineColour, "50%");
			else
				drawBoxedText(230, Client.clientHeight - 31, 50, 30, 
					instance.brownTopLeftOutlineColour, instance.brownButtonColour, instance.brownButtonColour, "50%");
			if(instance.endZoomLevel == 6D)
				drawBoxedText(290, Client.clientHeight - 31, 50, 30, 
					instance.redTopLeftOutlineColour, instance.redButtonColour, instance.redBottemRightOutlineColour, "75%");
			else
				drawBoxedText(290, Client.clientHeight - 31, 50, 30, 
					instance.brownTopLeftOutlineColour, instance.brownButtonColour, instance.brownButtonColour, "75%");
			if(instance.endZoomLevel == 8D)
				drawBoxedText(350, Client.clientHeight - 31, 50, 30, 
					instance.redTopLeftOutlineColour, instance.redButtonColour, instance.redBottemRightOutlineColour, "100%");
			else
				drawBoxedText(350, Client.clientHeight - 31, 50, 30, 
					instance.brownTopLeftOutlineColour, instance.brownButtonColour, instance.brownButtonColour, "100%");
		}
	}
	
	public static void drawMap(int i, int j, int k, int l, int i1, int j1, int k1, int l1) 
	{
		int i2 = k - i;
		int j2 = l - j;
		int k2 = (k1 - i1 << 16) / i2;
		int l2 = (l1 - j1 << 16) / j2;
		for(int i3 = 0; i3 < i2; i3++) 
		{
			int x = k2 * i3 >> 16;
			int width2 = k2 * (i3 + 1) >> 16;
			int width = width2 - x;
			if(width > 0) 
			{
				x += i1;
				width2 += i1;
				int underlayColours[] = instance.processedUnderlayColours[i3 + i];
				int overlayColours[] = instance.processedOverlayColours[i3 + i];
				byte overlayShapeData[] = instance.processedOverlayShapeData[i3 + i];
				for(int j7 = 0; j7 < j2; j7++) 
				{
					int y = l2 * j7 >> 16;
					int height2 = l2 * (j7 + 1) >> 16;
					int height = height2 - y;
					if(height > 0) 
					{
						y += j1;
						height2 += j1;
						int overlayColour = overlayColours[j7 + j];
						if(overlayColour == 0) {
							DrawingArea.fillRectangle(underlayColours[j7 + j], y,width2 - x, height2 - y, 256, x);//
						}
						else 
						{
							byte shapeRotation = overlayShapeData[j7 + j];
							int shapeType = shapeRotation & 0xfc;
							if(shapeType == 0 || width <= 1 || height <= 1)
								DrawingArea.fillRectangle(overlayColour, y, width, height, 256, x);//
							else
								//Tile.drawShapedTile(DrawingArea.pixels, y * DrawingArea.width + x, underlayColours[j7 + j],
								//	overlayColour, width, height, shapeType >> 2, shapeRotation & 3);//
								DrawingArea.fillRectangle(overlayColour, y, width, height, 256, x);
						}
					}
				}
			}
		}

		if(k - i > k1 - i1)
			return;
		int iconPtr = 0;
		for(int i4 = 0; i4 < i2; i4++)
		{
			int x = k2 * i4 >> 16;
			int i5 = k2 * (i4 + 1) >> 16;
			int lineWidth = i5 - x;
			if(lineWidth > 0)
			{
				byte wallDrawTypeData_[] = instance.wallDrawTypeData[i4 + i];
				byte objectIconData_[] = instance.objectIconData[i4 + i];
				byte mapIconData_[] = instance.mapIconData[i4 + i];
				for(int i9 = 0; i9 < j2; i9++)
				{
					int y2 = l2 * i9 >> 16;
					int i11 = l2 * (i9 + 1) >> 16;
					int lineHeight = i11 - y2;
					if(lineHeight > 0)
					{
						int wallType = wallDrawTypeData_[i9 + j] & 0xff;
						if(wallType != 0)
						{
							int x2;
							if(lineWidth == 1)
								x2 = x;
							else
								x2 = i5 - 1;
							int y;
							if(lineHeight == 1)
								y = y2;
							else
								y = i11 - 1;
							int colour = 0xcccccc;//building outline colour
							if(wallType >= 5 && wallType <= 8 || wallType >= 13 && wallType <= 16 || wallType >= 21 && wallType <= 24 || wallType == 27 || wallType == 28)
							{
								colour = 0xcc0000;
								wallType -= 4;
							}
							if(wallType == 1)
								DrawingArea.drawVLine(x, y2, lineHeight, colour);
							else
							if(wallType == 2)
								DrawingArea.drawHLine(x, y2, lineWidth, colour);
							else
							if(wallType == 3)
								DrawingArea.drawVLine(x2, y2, lineHeight, colour);
							else
							if(wallType == 4)
								DrawingArea.drawHLine(x, y, lineWidth, colour);
							else
							if(wallType == 9)
							{
								DrawingArea.drawVLine(x, y2, lineHeight, 0xffffff);
								DrawingArea.drawHLine(x, y2, lineWidth, colour);
							} else
							if(wallType == 10)
							{
								DrawingArea.drawVLine(x2, y2, lineHeight, 0xffffff);
								DrawingArea.drawHLine(x, y2, lineWidth, colour);
							} else
							if(wallType == 11)
							{
								DrawingArea.drawVLine(x2, y2, lineHeight, 0xffffff);
								DrawingArea.drawHLine(x, y, lineWidth, colour);
							} else
							if(wallType == 12)
							{
								DrawingArea.drawVLine(x, y2, lineHeight, 0xffffff);
								DrawingArea.drawHLine(x, y, lineWidth, colour);
							} else
							if(wallType == 17)
								DrawingArea.drawHLine(x, y2, 1, colour);
							else
							if(wallType == 18)
								DrawingArea.drawHLine(x2, y2, 1, colour);
							else
							if(wallType == 19)
								DrawingArea.drawHLine(x2, y, 1, colour);
							else
							if(wallType == 20)
								DrawingArea.drawHLine(x, y, 1, colour);
							else
							if(wallType == 25)
							{
								for(int j14 = 0; j14 < lineHeight; j14++)
									DrawingArea.drawHLine(x + j14, y - j14, 1, colour);

							} else
							if(wallType == 26)
							{
								for(int k14 = 0; k14 < lineHeight; k14++)
									DrawingArea.drawHLine(x + k14, y2 + k14, 1, colour);

							}
						}
						int clipSprite = objectIconData_[i9 + j] & 0xff;
						if(clipSprite != 0)
							clientInstance.mapScenes[clipSprite - 1].spriteClip(x - lineWidth / 2, y2 - lineHeight / 2, lineWidth * 2, lineHeight * 2);
						int mapIcon = mapIconData_[i9 + j] & 0xff;
						if(mapIcon != 0)
						{
							instance.mapIconIDs[iconPtr] = mapIcon - 1;
							instance.mapIconX[iconPtr] = x + lineWidth / 2;
							instance.mapIconY[iconPtr] = y2 + lineHeight / 2;
							iconPtr++;
						}
					}
				}

			}
		}

		for(int mapIconPtr = 0; mapIconPtr < iconPtr; mapIconPtr++)
			if(clientInstance.mapFunctions[instance.mapIconIDs[mapIconPtr]] != null)
				clientInstance.mapFunctions[instance.mapIconIDs[mapIconPtr]].drawAdvancedSprite(instance.mapIconX[mapIconPtr] - 7, instance.mapIconY[mapIconPtr] - 7);

		if(Client.loopCycle > 0)
		{
			for(int mapIconPtr = 0; mapIconPtr < iconPtr; mapIconPtr++)
				if(instance.mapIconIDs[mapIconPtr] == instance.lastClickedKeyItem)
				{
					clientInstance.mapFunctions[instance.mapIconIDs[mapIconPtr]].drawAdvancedSprite(instance.mapIconX[mapIconPtr] - 7, instance.mapIconY[mapIconPtr] - 7);
					if(instance.loopCycle % 10 < 5)
					{
						DrawingArea.fillCircle(instance.mapIconX[mapIconPtr], instance.mapIconY[mapIconPtr], 15, 0xffff00, 128);
						DrawingArea.fillCircle(instance.mapIconX[mapIconPtr], instance.mapIconY[mapIconPtr], 7, 0xffffff, 256);
					}
				}

		}
		if(instance.currentZoomLevel == instance.endZoomLevel && displayLabels)
		{
			for(int currentLabel = 0; currentLabel < instance.labelCount; currentLabel++)
			{
				int labelX = instance.labelXPos[currentLabel];
				int labelY = instance.labelYPos[currentLabel];
				labelX -= instance.baseX;
				labelY = (instance.baseY + instance.mapHeight) - labelY;
				int k7 = i1 + ((k1 - i1) * (labelX - i)) / (k - i);
				int j8 = j1 + ((l1 - j1) * (labelY - j)) / (l - j);
				int labelType = instance.labelDisplayType[currentLabel];
				int labelColour = 0xffffff;
				TypeFace fontSprite = null;
				
				if(labelType == 0)//small white text
				{
					if(instance.currentZoomLevel == 3D)
						fontSprite = instance.size11Font;
					if(instance.currentZoomLevel == 4D)
						fontSprite = instance.size12Font;
					if(instance.currentZoomLevel == 6D)
						fontSprite = instance.size14Font;
					if(instance.currentZoomLevel == 8D)
						fontSprite = instance.size17Font;
				}
				if(labelType == 1)//large white text
				{
					if(instance.currentZoomLevel == 3D)
						fontSprite = instance.size14Font;
					if(instance.currentZoomLevel == 4D)
						fontSprite = instance.size17Font;
					if(instance.currentZoomLevel == 6D)
						fontSprite = instance.size19Font;
					if(instance.currentZoomLevel == 8D)
						fontSprite = instance.size22Font;
				}
				if(labelType == 2)//large orange text
				{
					labelColour = 0xffaa00;
					if(instance.currentZoomLevel == 3D)
						fontSprite = instance.size19Font;
					if(instance.currentZoomLevel == 4D)
						fontSprite = instance.size22Font;
					if(instance.currentZoomLevel == 6D)
						fontSprite = instance.size26Font;
					if(instance.currentZoomLevel == 8D)
						fontSprite = instance.size30Font;
				}
				
				if(fontSprite != null)
				{
					String s = instance.labelString[currentLabel];
					int j12 = 1;
					for(int i13 = 0; i13 < s.length(); i13++)
						if(s.charAt(i13) == '/')
							j12++;

					j8 -= (fontSprite.method40() * (j12 - 1)) / 2;
					j8 += fontSprite.method44() / 2;
					/*do
					{
						int l13 = s.indexOf("/");
						if(l13 == -1)
						{
							fontSprite.method38(s, k7, j8, labelColour, true);
							break;
						}
						String s1 = s.substring(0, l13);
						fontSprite.method38(s1, k7, j8, labelColour, true);
						j8 += fontSprite.method40();
						s = s.substring(l13 + 1);
					} while(true);*/
				}
			}

		}
		if(mapDebugGridEnabled)
		{
			for(int mapRegionX = instance.baseX / 64; mapRegionX < (instance.baseX + instance.mapWidth) / 64; mapRegionX++)
			{
				for(int mapRegionY = instance.baseY / 64; mapRegionY < (instance.baseY + instance.mapHeight) / 64; mapRegionY++)
				{
					int coordX = mapRegionX * 64;
					int coordY = mapRegionY * 64;
					coordX -= instance.baseX;
					coordY = (instance.baseY + instance.mapHeight) - coordY;
					int x = i1 + ((k1 - i1) * (coordX - i)) / (k - i);
					int y = j1 + ((l1 - j1) * (coordY - 64 - j)) / (l - j);
					int width = i1 + ((k1 - i1) * ((coordX + 64) - i)) / (k - i);
					int height = j1 + ((l1 - j1) * (coordY - j)) / (l - j);
					DrawingArea.drawRectangle(y, height-y, 256, 0xffffff, width-x, x);
					Client.boldFont.drawStringCenter(0xffffff, width-5, mapRegionX + "_" + mapRegionY, height - 5, true);
					if(mapRegionX == 33 && mapRegionY >= 71 && mapRegionY <= 73)
						Client.boldFont.drawStringCenter(0xff0000, "u_pass", (width + x) / 2, (height + y) / 2);
				}

			}

		}
	}
	
	public static void drawBoxedText(int x, int y, int width, int height, 
		int topLeftOutlineColour, int buttonColour, int bottemRightOutlineColour, String text) 
	{
		DrawingArea.drawRectangle(y, height, 0, 256, width, x);
		x++;
		y++;
		width -= 2;
		height -= 2;
		DrawingArea.fillRectangle(buttonColour, y, width, height, 256, x);
		DrawingArea.drawHLine(x, y, width, topLeftOutlineColour);
		DrawingArea.drawVLine(x, y, height, topLeftOutlineColour);
		DrawingArea.drawHLine(x, (y + height) - 1, width, bottemRightOutlineColour);
		DrawingArea.drawVLine((x + width) - 1, y, height, bottemRightOutlineColour);
		Client.boldFont.drawStringCenter(0, text, x + width / 2 + 1, y + height / 2 + 1 + 4);
		Client.boldFont.drawStringCenter(0xffffff, text, x + width / 2, y + height / 2 + 4);
	}
	
	public static Client clientInstance;
	public static WorldMap instance;
	public static boolean mapDebugGridEnabled = true;
	public static boolean displayLabels = true;
}