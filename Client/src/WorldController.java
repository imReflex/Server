

@SuppressWarnings("all")
final class WorldController {

	public WorldController(int ai[][][]) {
		int height = 104;// was parameter
		int width = 104;// was parameter
		int depth = 4;// was parameter
		interactableObjectCache = new InteractableObject[5000];
		anIntArray486 = new int[10000];
		anIntArray487 = new int[10000];
		zMapSize = depth;
		xMapSize = width;
		yMapSize = height;
		tileArray = new Tile[depth][width][height];
		cycleMap = new int[depth][width + 1][height + 1];
		heightMap = ai;
		initToNull();
	}

	public WallObject fetchWallObject(int i, int j, int k) {
		Tile tile = tileArray[i][j][k];
		if (tile == null || tile.wallObject == null)
			return null;
		else
			return tile.wallObject;
	}

	public WallDecoration fetchWallDecoration(int i, int j, int l) {
		Tile tile = tileArray[i][j][l];
		if (tile == null || tile.wallDecoration == null)
			return null;
		else
			return tile.wallDecoration;
	}

	public InteractableObject fetchInteractableObject(int z, int x, int y) {
		Tile tile = tileArray[z][x][y];
		if (tile == null)
			return null;
		for (int l = 0; l < tile.entityCount; l++) {
			InteractableObject interactableObject = tile.interactableObjects[l];
			if (interactableObject.tileLeft == x && interactableObject.tileTop == y) {
				return interactableObject;
			}
		}
		return null;
	}

	public GroundDecoration fetchGroundDecoration(int i, int j, int k) {
		Tile tile = tileArray[i][j][k];
		if (tile == null || tile.groundDecoration == null)
			return null;
		else
			return tile.groundDecoration;
	}
	
	
	public int fetchWallObjectNewUID(int i, int j, int k) {
		Tile tile = tileArray[i][j][k];
		if (tile == null || tile.wallObject == null)
			return 0;
		else
			return tile.wallObject.wallObjUID;
	}

	public int fetchWallDecorationNewUID(int i, int j, int l) {
		Tile tile = tileArray[i][j][l];
		if (tile == null || tile.wallDecoration == null)
			return 0;
		else
			return tile.wallDecoration.wallDecorUID;
	}

	public int fetchObjectMeshNewUID(int z, int x, int y) {
		Tile tile = tileArray[z][x][y];
		if (tile == null)
			return 0;
		for (int l = 0; l < tile.entityCount; l++) {
			InteractableObject interactableObject = tile.interactableObjects[l];
			if (interactableObject.tileLeft == x && interactableObject.tileTop == y) {
				return interactableObject.interactiveObjUID;
			}
		}
		return 0;
	}

	public int fetchGroundDecorationNewUID(int i, int j, int k) {
		Tile tile = tileArray[i][j][k];
		if (tile == null || tile.groundDecoration == null)
			return 0;
		else
			return tile.groundDecoration.groundDecorUID;
	}

	public static void nullLoader() {
		aClass28Array462 = null;
		cullingClusterPointer = null;
		cullingClusters = null;
		tileDeque = null;
		tile_visibility_maps = null;
		tile_visibility_map = null;
	}

	public void initToNull() {
		for (int z = 0; z < zMapSize; z++) {
			for (int x = 0; x < xMapSize; x++) {
				for (int y = 0; y < yMapSize; y++)
					tileArray[z][x][y] = null;

			}

		}
		for (int l = 0; l < amountOfCullingClusters; l++) {
			for (int j1 = 0; j1 < cullingClusterPointer[l]; j1++)
				cullingClusters[l][j1] = null;

			cullingClusterPointer[l] = 0;
		}

		for (int k1 = 0; k1 < amountOfInteractableObjects; k1++)
			interactableObjectCache[k1] = null;

		amountOfInteractableObjects = 0;
		for (int l1 = 0; l1 < aClass28Array462.length; l1++)
			aClass28Array462[l1] = null;

	}

	public void initTiles(int hl) {
		currentHL = hl;
		for (int k = 0; k < xMapSize; k++) {
			for (int l = 0; l < yMapSize; l++)
				if (tileArray[hl][k][l] == null)
					tileArray[hl][k][l] = new Tile(hl, k, l);

		}

	}

	public void applyBridgeMode(int y, int x) {
		Tile tile = tileArray[0][x][y];
		for (int l = 0; l < 3; l++) {
			Tile tile_ = tileArray[l][x][y] = tileArray[l + 1][x][y];
			if (tile_ != null) {
				tile_.tileZ--;
				for (int entityPtr = 0; entityPtr < tile_.entityCount; entityPtr++) {
					InteractableObject iObject = tile_.interactableObjects[entityPtr];
					if ((iObject.uid >> 29 & 3) == 2 && iObject.tileLeft == x && iObject.tileTop == y)
						iObject.zPos--;
				}

			}
		}
		if (tileArray[0][x][y] == null)
			tileArray[0][x][y] = new Tile(0, x, y);
		tileArray[0][x][y].tileBelowThisTile = tile;
		tileArray[3][x][y] = null;
	}

	public static void createCullingCluster(int id, int tileStartX, int worldEndZ, int tileEndX, int tileEndY, int worldStartZ, int tileStartY, int searchMask) {
		CullingCluster cluster = new CullingCluster();
		cluster.tileStartX = tileStartX / 128;
		cluster.tileEndX = tileEndX / 128;
		cluster.tileStartY = tileStartY / 128;
		cluster.tileEndY = tileEndY / 128;
		cluster.searchMask = searchMask;
		cluster.worldStartX = tileStartX;
		cluster.worldEndX = tileEndX;
		cluster.worldStartY = tileStartY;
		cluster.worldEndY = tileEndY;
		cluster.worldStartZ = worldStartZ;
		cluster.worldEndZ = worldEndZ;
		cullingClusters[id][cullingClusterPointer[id]++] = cluster;
	}

	public void setVisiblePlanesFor(int z, int x, int y, int logicHeight) {
		Tile tile = tileArray[z][x][y];
		if (tile != null) {
			tileArray[z][x][y].logicHeight = logicHeight;
		}
	}

	public void addTile(int plane, int x, int y, int shape, int rotation, 
			int texture, int zA, int zB, int zD, int zC, int colourA, 
			int colourB, int colourD, int colourC, int colourAA, int colourBA, 
			int colourDA, int colourCA, int rgbColour, int rgbColour_,
			int color, int copy_texture, int copy_color) {
		if (shape == 0) {
			PlainTile tile = new PlainTile(colourA, colourB, colourD, colourC, -1, rgbColour, false, color);
			for (int z = plane; z >= 0; z--)
				if (tileArray[z][x][y] == null)
					tileArray[z][x][y] = new Tile(z, x, y);

			tileArray[plane][x][y].plainTile = tile;
			return;
		}
		if (shape == 1) {
			PlainTile tile_1 = new PlainTile(colourAA, colourBA, colourDA, colourCA, texture, rgbColour_, zA == zB && zA == zD && zA == zC, color);
			for (int z = plane; z >= 0; z--)
				if (tileArray[z][x][y] == null)
					tileArray[z][x][y] = new Tile(z, x, y);

			tileArray[plane][x][y].plainTile = tile_1;
			return;
		}
		ShapedTile shapedTile = new ShapedTile(y, colourAA, colourC, zD, texture, colourDA, rotation, colourA, rgbColour, colourD, zC, zB, zA, shape, colourCA, colourBA, colourB, x, rgbColour_, color, copy_texture, copy_color);
		for (int z = plane; z >= 0; z--)
			if (tileArray[z][x][y] == null)
				tileArray[z][x][y] = new Tile(z, x, y);

		tileArray[plane][x][y].shapedTile = shapedTile;
	}

	public void addGroundDecoration(int plane, int zPos, int yPos, Animable animable, byte byte0, int uid, int xPos, int groundDecorUID) {
		if (animable == null)
			return;
		GroundDecoration decoration = new GroundDecoration();
		decoration.node = animable;
		decoration.groundDecorUID = groundDecorUID;
		decoration.xPos = xPos * 128 + 64;
		decoration.yPos = yPos * 128 + 64;
		decoration.zPos = zPos;
		decoration.uid = uid;
		decoration.objConfig = byte0;
		if (tileArray[plane][xPos][yPos] == null)
			tileArray[plane][xPos][yPos] = new Tile(plane, xPos, yPos);
		tileArray[plane][xPos][yPos].groundDecoration = decoration;
	}

	public void addGroundItemTile(int xPos, int uid, Animable secondItem, int zPos, Animable thirdItem, Animable firstItem, int plane, int yPos) {
		GroundItem groundItem = new GroundItem();
		groundItem.firstGroundItem = firstItem;
		groundItem.xPos = xPos * 128 + 64;
		groundItem.yPos = yPos * 128 + 64;
		groundItem.zPos = zPos;
		groundItem.uid = uid;
		groundItem.secondGroundItem = secondItem;
		groundItem.thirdGroundItem = thirdItem;
		int isHighestPriority = 0;
		Tile tile = tileArray[plane][xPos][yPos];
		if (tile != null) {
			for (int k1 = 0; k1 < tile.entityCount; k1++)
				if (tile.interactableObjects[k1].node instanceof Model) {
					int tempInt = ((Model) tile.interactableObjects[k1].node).myPriority;
					if (tempInt > isHighestPriority)
						isHighestPriority = tempInt;
				}

		}
		groundItem.topItem = isHighestPriority;
		if (tileArray[plane][xPos][yPos] == null)
			tileArray[plane][xPos][yPos] = new Tile(plane, xPos, yPos);
		tileArray[plane][xPos][yPos].groundItem = groundItem;
	}

	public void addWallObject(int orientation, Animable node, int uid, int yPos, byte objConfig, int xPos, Animable node2, int zPos, int orientation_2, int plane, int wallObjUID) {
		if (node == null && node2 == null)
			return;
		WallObject wallObject = new WallObject();
		wallObject.uid = uid;
		wallObject.objConfig = objConfig;
		wallObject.xPos = xPos * 128 + 64;
		wallObject.yPos = yPos * 128 + 64;
		wallObject.zPos = zPos;
		wallObject.node1 = node;
		wallObject.node2 = node2;
		wallObject.wallObjUID = wallObjUID;
		wallObject.orientation = orientation;
		wallObject.orientation1 = orientation_2;
		for (int zPtr = plane; zPtr >= 0; zPtr--)
			if (tileArray[zPtr][xPos][yPos] == null)
				tileArray[zPtr][xPos][yPos] = new Tile(zPtr, xPos, yPos);

		tileArray[plane][xPos][yPos].wallObject = wallObject;
	}

	public void addWallDecoration(int uid, int yPos, int rotation, int plane, int xOff, int zPos, Animable node, int xPos, byte config, int yOff, int configBits, int wallDecorUID) {
		if (node == null)
			return;
		WallDecoration dec = new WallDecoration();
		dec.uid = uid;
		dec.objConfig = config;
		dec.xPos = xPos * 128 + 64 + xOff;
		dec.yPos = yPos * 128 + 64 + yOff;
		dec.zPos = zPos;
		dec.node = node;
		dec.wallDecorUID = wallDecorUID;
		dec.configurationBits = configBits;
		dec.rotation = rotation;
		for (int zPtr = plane; zPtr >= 0; zPtr--)
			if (tileArray[zPtr][xPos][yPos] == null)
				tileArray[zPtr][xPos][yPos] = new Tile(zPtr, xPos, yPos);

		tileArray[plane][xPos][yPos].wallDecoration = dec;
	}

	public boolean addInteractableEntity(int ui, byte config, int worldZ, int tileBottom, Animable node, int tileRight, int z, int rotation, int tileTop, int tileLeft, int interactiveUID) {
		if (node == null) {
			return true;
		} else {
			int worldX = tileLeft * 128 + 64 * tileRight;
			int worldY = tileTop * 128 + 64 * tileBottom;
			return addEntity(z, tileLeft, tileTop, tileRight, tileBottom, 
					worldX, worldY, worldZ, node, rotation, false, ui, config, interactiveUID);
		}
	}

	public boolean addMutipleTileEntity(int z, int rotation, int worldZ, int ui, int worldY, int j1, int worldX, Animable nodeToAdd, boolean flag) {
		if (nodeToAdd == null)
			return true;
		int tileLeft = worldX - j1;
		int tileTop = worldY - j1;
		int tileRight = worldX + j1;
		int tileBottom = worldY + j1;
		if (flag) {
			if (rotation > 640 && rotation < 1408)
				tileBottom += 128;
			if (rotation > 1152 && rotation < 1920)
				tileRight += 128;
			if (rotation > 1664 || rotation < 384)
				tileTop -= 128;
			if (rotation > 128 && rotation < 896)
				tileLeft -= 128;
		}
		tileLeft /= 128;
		tileTop /= 128;
		tileRight /= 128;
		tileBottom /= 128;
		return addEntity(z, tileLeft, tileTop, (tileRight - tileLeft) + 1, (tileBottom - tileTop) + 1, 
				worldX, worldY, worldZ, nodeToAdd, rotation, true, ui, (byte) 0, 0);
	}

	public boolean addSingleTileEntity(int z, int worldY, Animable node, int rotation, int tileBottom, int worldX, int worldZ, int tileLeft, int tileRight, int ui, int tileTop) {
		return node == null || addEntity(z, tileLeft, tileTop, (tileRight - tileLeft) + 1, (tileBottom - tileTop) + 1, 
				worldX, worldY, worldZ, node, rotation, true, ui, (byte) 0, 0);
	}

	private boolean addEntity(int z, int tileLeft, int tileTop, int tileRight, int tileBottom, int worldX, int worldY, int worldZ, Animable node, int rotation, boolean flag, int ui, byte objConf, int interactiveObjUID) {
		/**
		 * Max entities on coord is 5 i guess
		 */
		for (int _x = tileLeft; _x < tileLeft + tileRight; _x++) {
			for (int _y = tileTop; _y < tileTop + tileBottom; _y++) {
				if (_x < 0 || _y < 0 || _x >= xMapSize || _y >= yMapSize)
					return false;
				Tile tile = tileArray[z][_x][_y];
				if (tile != null && tile.entityCount >= 5)
					return false;
			}

		}

		InteractableObject io = new InteractableObject();
		io.uid = ui;
		io.objConf = objConf;
		io.zPos = z;
		io.worldX = worldX;
		io.worldY = worldY;
		io.interactiveObjUID = interactiveObjUID;
		io.worldZ = worldZ;
		io.node = node;
		io.rotation = rotation;
		io.tileLeft = tileLeft;
		io.tileTop = tileTop;
		io.tileRight = (tileLeft + tileRight) - 1;
		io.tileBottom = (tileTop + tileBottom) - 1;
		for (int x = tileLeft; x < tileLeft + tileRight; x++) {
			for (int y = tileTop; y < tileTop + tileBottom; y++) {
				int position = 0;
				if (x > tileLeft)
					position++;
				if (x < (tileLeft + tileRight) - 1)
					position += 4;
				if (y > tileTop)
					position += 8;
				if (y < (tileTop + tileBottom) - 1)
					position += 2;
				for (int zPtr = z; zPtr >= 0; zPtr--)
					if (tileArray[zPtr][x][y] == null)
						tileArray[zPtr][x][y] = new Tile(zPtr, x, y);

				Tile tile = tileArray[z][x][y];
				tile.interactableObjects[tile.entityCount] = io;
				tile.objectFlags[tile.entityCount] = position;
				tile.anInt1320 |= position;
				tile.entityCount++;
			}

		}

		if (flag)
			interactableObjectCache[amountOfInteractableObjects++] = io;
		return true;
	}

	public void clearInteractableObjects() {
		for (int i = 0; i < amountOfInteractableObjects; i++) {
			InteractableObject iObject = interactableObjectCache[i];
			updateObjectEntities(iObject);
			interactableObjectCache[i] = null;
		}

		amountOfInteractableObjects = 0;
	}

	private void updateObjectEntities(InteractableObject iObject) {
		for (int j = iObject.tileLeft; j <= iObject.tileRight; j++) {
			for (int k = iObject.tileTop; k <= iObject.tileBottom; k++) {
				Tile tile = tileArray[iObject.zPos][j][k];
				if (tile != null) {
					for (int l = 0; l < tile.entityCount; l++) {
						if (tile.interactableObjects[l] != iObject)
							continue;
						tile.entityCount--;
						for (int entityPtr = l; entityPtr < tile.entityCount; entityPtr++) {
							tile.interactableObjects[entityPtr] = tile.interactableObjects[entityPtr + 1];
							tile.objectFlags[entityPtr] = tile.objectFlags[entityPtr + 1];
						}

						tile.interactableObjects[tile.entityCount] = null;
						break;
					}

					tile.anInt1320 = 0;
					for (int j1 = 0; j1 < tile.entityCount; j1++)
						tile.anInt1320 |= tile.objectFlags[j1];

				}
			}

		}

	}

	public void moveWallDec(int y, int moveAmt, int x, int z) {
		Tile tile = tileArray[z][x][y];
		if (tile == null)
			return;
		WallDecoration wallDec = tile.wallDecoration;
		if (wallDec != null) {
			int xCoord = x * 128 + 64;
			int yCoord = y * 128 + 64;
			wallDec.xPos = xCoord + ((wallDec.xPos - xCoord) * moveAmt) / 16;
			wallDec.yPos = yCoord + ((wallDec.yPos - yCoord) * moveAmt) / 16;
		}
	}

	public void removeWallObject(int x, int y, int z) {
		Tile tile = tileArray[y][x][z];
		if (tile != null) {
			tile.wallObject = null;
		}
	}

	public void removeWallDecoration(int y, int z, int x) {
		Tile tile = tileArray[z][x][y];
		if (tile != null) {
			tile.wallDecoration = null;
		}
	}

	public void removeInteractableObject(int z, int x, int y) {
		Tile tile = tileArray[z][x][y];
		if (tile == null)
			return;
		for (int i = 0; i < tile.entityCount; i++) {
			InteractableObject subObject = tile.interactableObjects[i];
			if ((subObject.uid >> 29 & 3) == 2 && subObject.tileLeft == x && subObject.tileTop == y) {
				updateObjectEntities(subObject);
				return;
			}
		}

	}

	public void removeGroundDecoration(int z, int y, int x) {
		Tile tile = tileArray[z][x][y];
		if (tile == null)
			return;
		tile.groundDecoration = null;
	}

	public void removeGroundItemFromTIle(int z, int x, int y) {
		Tile tile = tileArray[z][x][y];
		if (tile != null) {
			tile.groundItem = null;
		}
	}

	public WallObject getWallObject(int z, int x, int y) {
		Tile tile = tileArray[z][x][y];
		if (tile == null)
			return null;
		else
			return tile.wallObject;
	}

	public WallDecoration getWallDecoration(int x, int y, int z) {
		Tile tile = tileArray[z][x][y];
		if (tile == null)
			return null;
		else
			return tile.wallDecoration;
	}

	public InteractableObject getInteractableObject(int x, int y, int z) {
		Tile tile = tileArray[z][x][y];
		if (tile == null)
			return null;
		for (int l = 0; l < tile.entityCount; l++) {
			InteractableObject subObject = tile.interactableObjects[l];
			if ((subObject.uid >> 29 & 3) == 2 && subObject.tileLeft == x && subObject.tileTop == y)
				return subObject;
		}
		return null;
	}

	public GroundDecoration getGroundDecoration(int y, int x, int z) {
		Tile tile = tileArray[z][x][y];
		if (tile == null || tile.groundDecoration == null)
			return null;
		else
			return tile.groundDecoration;
	}

	public int getWallObjectUID(int z, int x, int y) {
		Tile tile = tileArray[z][x][y];
		if (tile == null || tile.wallObject == null)
			return 0;
		else
			return tile.wallObject.uid;
	}

	public int getWallDecorationUID(int z, int x, int y) {
		Tile tile = tileArray[z][x][y];
		if (tile == null || tile.wallDecoration == null)
			return 0;
		else
			return tile.wallDecoration.uid;
	}

	public int getInteractableObjectUID(int plane, int x, int y) {
		Tile tile = tileArray[plane][x][y];
		if (tile == null)
			return 0;
		for (int i = 0; i < tile.entityCount; i++) {
			InteractableObject iObject = tile.interactableObjects[i];
			if (iObject.tileLeft == x && iObject.tileTop == y)
				return iObject.uid;
		}

		return 0;
	}

	public int getGroundDecorationUID(int z, int x, int y) {
		Tile tile = tileArray[z][x][y];
		if (tile == null || tile.groundDecoration == null)
			return 0;
		else
			return tile.groundDecoration.uid;
	}

	public int getIDTagForXYZ(int z, int x, int y, int uidMatch) {
		Tile tile = tileArray[z][x][y];
		if (tile == null)
			return -1;
		if (tile.wallObject != null && tile.wallObject.uid == uidMatch)
			return tile.wallObject.objConfig & 0xff;
		if (tile.wallDecoration != null && tile.wallDecoration.uid == uidMatch)
			return tile.wallDecoration.objConfig & 0xff;
		if (tile.groundDecoration != null && tile.groundDecoration.uid == uidMatch)
			return tile.groundDecoration.objConfig & 0xff;
		for (int entityPtr = 0; entityPtr < tile.entityCount; entityPtr++)
			if (tile.interactableObjects[entityPtr].uid == uidMatch)
				return tile.interactableObjects[entityPtr].objConf & 0xff;

		return -1;
	}

	public void shadeModels(int i, int k, int i1) {
		int j = 100;
		int l = 5500;
		int j1 = (int) Math.sqrt(k * k + i * i + i1 * i1);
		int k1 = l >> 4;
		for (int l1 = 0; l1 < zMapSize; l1++) {
			for (int i2 = 0; i2 < xMapSize; i2++) {
				for (int j2 = 0; j2 < yMapSize; j2++) {
					Tile class30_sub3 = tileArray[l1][i2][j2];
					if (class30_sub3 != null) {
						WallObject class10 = class30_sub3.wallObject;
						if (class10 != null && class10.node1 != null && class10.node1.vertexNormals != null) {
							mergeModels(l1, 1, 1, i2, j2, (Model) class10.node1);
							if (class10.node2 != null && class10.node2.vertexNormals != null) {
								mergeModels(l1, 1, 1, i2, j2, (Model) class10.node2);
								renderModels((Model) class10.node1, (Model) class10.node2, 0, 0, 0, false);
								((Model) class10.node2).method480(j, k1, k, i, i1);
							}
							((Model) class10.node1).method480(j, k1, k, i, i1);
						}
						for (int k2 = 0; k2 < class30_sub3.entityCount; k2++) {
							InteractableObject class28 = class30_sub3.interactableObjects[k2];
							if (class28 != null && class28.node != null && class28.node.vertexNormals != null) {
								mergeModels(l1, (class28.tileRight - class28.tileLeft) + 1, (class28.tileBottom - class28.tileTop) + 1, i2, j2, (Model) class28.node);
								((Model) class28.node).method480(j, k1, k, i, i1);
							}
						}

						GroundDecoration class49 = class30_sub3.groundDecoration;
						if (class49 != null && class49.node.vertexNormals != null) {
							renderGrounDec(i2, l1, (Model) class49.node, j2);
							((Model) class49.node).method480(j, k1, k, i, i1);
						}
					}
				}

			}

		}

	}

	private void renderGrounDec(int i, int j, Model model, int k) {
		if (i < xMapSize) {
			Tile class30_sub3 = tileArray[j][i + 1][k];
			if (class30_sub3 != null && class30_sub3.groundDecoration != null && class30_sub3.groundDecoration.node.vertexNormals != null)
				renderModels(model, (Model) class30_sub3.groundDecoration.node, 128, 0, 0, true);
		}
		if (k < xMapSize) {
			Tile class30_sub3_1 = tileArray[j][i][k + 1];
			if (class30_sub3_1 != null && class30_sub3_1.groundDecoration != null && class30_sub3_1.groundDecoration.node.vertexNormals != null)
				renderModels(model, (Model) class30_sub3_1.groundDecoration.node, 0, 0, 128, true);
		}
		if (i < xMapSize && k < yMapSize) {
			Tile class30_sub3_2 = tileArray[j][i + 1][k + 1];
			if (class30_sub3_2 != null && class30_sub3_2.groundDecoration != null && class30_sub3_2.groundDecoration.node.vertexNormals != null)
				renderModels(model, (Model) class30_sub3_2.groundDecoration.node, 128, 0, 128, true);
		}
		if (i < xMapSize && k > 0) {
			Tile class30_sub3_3 = tileArray[j][i + 1][k - 1];
			if (class30_sub3_3 != null && class30_sub3_3.groundDecoration != null && class30_sub3_3.groundDecoration.node.vertexNormals != null)
				renderModels(model, (Model) class30_sub3_3.groundDecoration.node, 128, 0, -128, true);
		}
	}

	private void mergeModels(int z, int j, int k, int x, int y, Model model) {
		boolean flag = true;
		int j1 = x;
		int k1 = x + j;
		int l1 = y - 1;
		int i2 = y + k;
		for (int j2 = z; j2 <= z + 1; j2++)
			if (j2 != zMapSize) {
				for (int k2 = j1; k2 <= k1; k2++)
					if (k2 >= 0 && k2 < xMapSize) {
						for (int l2 = l1; l2 <= i2; l2++)
							if (l2 >= 0 && l2 < yMapSize && (!flag || k2 >= k1 || l2 >= i2 || l2 < y && k2 != x)) {
								Tile class30_sub3 = tileArray[j2][k2][l2];
								if (class30_sub3 != null) {
									int i3 = (heightMap[j2][k2][l2] + heightMap[j2][k2 + 1][l2] + heightMap[j2][k2][l2 + 1] + heightMap[j2][k2 + 1][l2 + 1]) / 4 - (heightMap[z][x][y] + heightMap[z][x + 1][y] + heightMap[z][x][y + 1] + heightMap[z][x + 1][y + 1]) / 4;
									WallObject class10 = class30_sub3.wallObject;
									if (class10 != null && class10.node1 != null && class10.node1.vertexNormals != null)
										renderModels(model, (Model) class10.node1, (k2 - x) * 128 + (1 - j) * 64, i3, (l2 - y) * 128 + (1 - k) * 64, flag);
									if (class10 != null && class10.node2 != null && class10.node2.vertexNormals != null)
										renderModels(model, (Model) class10.node2, (k2 - x) * 128 + (1 - j) * 64, i3, (l2 - y) * 128 + (1 - k) * 64, flag);
									for (int j3 = 0; j3 < class30_sub3.entityCount; j3++) {
										InteractableObject class28 = class30_sub3.interactableObjects[j3];
										if (class28 != null && class28.node != null && class28.node.vertexNormals != null) {
											int k3 = (class28.tileRight - class28.tileLeft) + 1;
											int l3 = (class28.tileBottom - class28.tileTop) + 1;
											renderModels(model, (Model) class28.node, (class28.tileLeft - x) * 128 + (k3 - j) * 64, i3, (class28.tileTop - y) * 128 + (l3 - k) * 64, flag);
										}
									}

								}
							}

					}

				j1--;
				flag = false;
			}

	}

	private void renderModels(Model model, Model model_1, int i, int j, int k, boolean flag) {
		anInt488++;
		int l = 0;
		int ai[] = model_1.verticesXCoordinate;
		int amtOfVertices = model_1.numberOfVerticeCoordinates;
		for (int verticeId = 0; verticeId < model.numberOfVerticeCoordinates; verticeId++) {
			VertexNormal vertexNormal = model.vertexNormals[verticeId];
			VertexNormal vertexNormalOff = model.vertexNormalOffset[verticeId];
			if (vertexNormalOff.anInt605 != 0) {
				int vertY = model.verticesYCoordinate[verticeId] - j;
				if (vertY <= model_1.anInt1651) {
					int vertX = model.verticesXCoordinate[verticeId] - i;
					if (vertX >= model_1.anInt1646 && vertX <= model_1.anInt1647) {
						int vertZ = model.verticesZCoordinate[verticeId] - k;
						if (vertZ >= model_1.anInt1649 && vertZ <= model_1.anInt1648) {
							for (int vertId_1 = 0; vertId_1 < amtOfVertices; vertId_1++) {
								VertexNormal class33_2 = model_1.vertexNormals[vertId_1];
								VertexNormal class33_3 = model_1.vertexNormalOffset[vertId_1];
								if (vertX == ai[vertId_1] && vertZ == model_1.verticesZCoordinate[vertId_1] && vertY == model_1.verticesYCoordinate[vertId_1] && class33_3.anInt605 != 0) {
									vertexNormal.anInt602 += class33_3.anInt602;
									vertexNormal.anInt603 += class33_3.anInt603;
									vertexNormal.anInt604 += class33_3.anInt604;
									vertexNormal.anInt605 += class33_3.anInt605;
									class33_2.anInt602 += vertexNormalOff.anInt602;
									class33_2.anInt603 += vertexNormalOff.anInt603;
									class33_2.anInt604 += vertexNormalOff.anInt604;
									class33_2.anInt605 += vertexNormalOff.anInt605;
									l++;
									anIntArray486[verticeId] = anInt488;
									anIntArray487[vertId_1] = anInt488;
								}
							}

						}
					}
				}
			}
		}

		if (l < 3 || !flag)
			return;
		for (int k1 = 0; k1 < model.numberOfTriangleFaces; k1++)
			if (anIntArray486[model.face_a[k1]] == anInt488 && anIntArray486[model.face_b[k1]] == anInt488 && anIntArray486[model.face_c[k1]] == anInt488)
				model.face_render_type[k1] = -1;

		for (int l1 = 0; l1 < model_1.numberOfTriangleFaces; l1++)
			if (anIntArray487[model_1.face_a[l1]] == anInt488 && anIntArray487[model_1.face_b[l1]] == anInt488 && anIntArray487[model_1.face_c[l1]] == anInt488)
				model_1.face_render_type[l1] = -1;

	}

	public void drawTileMinimap(int pixels[], int ptr, int z, int x, int y) {
		int j = 512;// was parameter
		Tile tile_ = tileArray[z][x][y];
		if (tile_ == null)
			return;
		PlainTile tile = tile_.plainTile;
		if (tile != null) {
			int j1 = tile.rgbColour;
			if (j1 == 0)
				return;
			for (int k1 = 0; k1 < 4; k1++) {
				pixels[ptr] = j1;
				pixels[ptr + 1] = j1;
				pixels[ptr + 2] = j1;
				pixels[ptr + 3] = j1;
				ptr += j;
			}

			return;
		}
		ShapedTile shapedTile = tile_.shapedTile;
		if (shapedTile == null)
			return;
		int l1 = shapedTile.shape;
		int i2 = shapedTile.rotation;
		int j2 = shapedTile.colourRGB;
		int k2 = shapedTile.colourRGBA;
		int ai1[] = tileSHapePoints[l1];
		int ai2[] = tileShapeIndices[i2];
		int l2 = 0;
		if (j2 != 0) {
			for (int i3 = 0; i3 < 4; i3++) {
				pixels[ptr] = ai1[ai2[l2++]] != 0 ? k2 : j2;
				pixels[ptr + 1] = ai1[ai2[l2++]] != 0 ? k2 : j2;
				pixels[ptr + 2] = ai1[ai2[l2++]] != 0 ? k2 : j2;
				pixels[ptr + 3] = ai1[ai2[l2++]] != 0 ? k2 : j2;
				ptr += j;
			}

			return;
		}
		for (int j3 = 0; j3 < 4; j3++) {
			if (ai1[ai2[l2++]] != 0)
				pixels[ptr] = k2;
			if (ai1[ai2[l2++]] != 0)
				pixels[ptr + 1] = k2;
			if (ai1[ai2[l2++]] != 0)
				pixels[ptr + 2] = k2;
			if (ai1[ai2[l2++]] != 0)
				pixels[ptr + 3] = k2;
			ptr += j;
		}

	}

	public static void setupViewport(int minZ, int maxZ, int width, int height, int ai[]) {
		left = 0;
		top = 0;
		right = width;
		bottom = height;
		midX = width / 2;
		midY = height / 2;
		boolean isOnScreen[][][][] = new boolean[9][32][53][53];
		for (int yAngle = 128; yAngle <= 384; yAngle += 32) {
			for (int xAngle = 0; xAngle < 2048; xAngle += 64) {
				yCurveSin = Model.SINE[yAngle];
				yCUrveCos = Model.COSINE[yAngle];
				xCurveSin = Model.SINE[xAngle];
				xCurveCos = Model.COSINE[xAngle];
				int l1 = (yAngle - 128) / 32;
				int j2 = xAngle / 64;
				for (int l2 = -26; l2 <= 26; l2++) {
					for (int j3 = -26; j3 <= 26; j3++) {
						int k3 = l2 * 128;
						int i4 = j3 * 128;
						boolean flag2 = false;
						for (int k4 = -minZ; k4 <= maxZ; k4 += 128) {
							if (!isOnScreen(ai[l1] + k4, i4, k3))
								continue;
							flag2 = true;
							break;
						}

						isOnScreen[l1][j2][l2 + 25 + 1][j3 + 25 + 1] = flag2;
					}

				}

			}

		}

		for (int k1 = 0; k1 < 8; k1++) {
			for (int i2 = 0; i2 < 32; i2++) {
				for (int k2 = -25; k2 < 25; k2++) {
					for (int i3 = -25; i3 < 25; i3++) {
						boolean flag1 = false;
						label0: for (int l3 = -1; l3 <= 1; l3++) {
							for (int j4 = -1; j4 <= 1; j4++) {
								if (isOnScreen[k1][i2][k2 + l3 + 25 + 1][i3 + j4 + 25 + 1])
									flag1 = true;
								else if (isOnScreen[k1][(i2 + 1) % 31][k2 + l3 + 25 + 1][i3 + j4 + 25 + 1])
									flag1 = true;
								else if (isOnScreen[k1 + 1][i2][k2 + l3 + 25 + 1][i3 + j4 + 25 + 1]) {
									flag1 = true;
								} else {
									if (!isOnScreen[k1 + 1][(i2 + 1) % 31][k2 + l3 + 25 + 1][i3 + j4 + 25 + 1])
										continue;
									flag1 = true;
								}
								break label0;
							}

						}

						tile_visibility_maps[k1][i2][k2 + 25][i3 + 25] = flag1;
					}

				}

			}

		}

	}

	private static boolean isOnScreen(int z, int y, int x) {
		int l = y * xCurveSin + x * xCurveCos >> 16;
		int i1 = y * xCurveCos - x * xCurveSin >> 16;
		int dist = z * yCurveSin + i1 * yCUrveCos >> 16;
		int k1 = z * yCUrveCos - i1 * yCurveSin >> 16;
		if (dist < 50 || dist > 3500)
			return false;
		int l1 = midX + (l << Client.log_view_dist) / dist;
		int i2 = midY + (k1 << Client.log_view_dist) / dist;
		return l1 >= left && l1 <= right && i2 >= top && i2 <= bottom;
	}

	public void request2DTrace(int x, int y) {
		isClicked = true;
		clickX = y;
		clickY = x;
		clickedTileX = -1;
		clickedTileY = -1;
	}

	public void render(int xCam, int yCam, int xCurve, int zCam, int plane, int yCurve) {
		if (xCam < 0)
			xCam = 0;
		else if (xCam >= xMapSize * 128)
			xCam = xMapSize * 128 - 1;
		if (yCam < 0)
			yCam = 0;
		else if (yCam >= yMapSize * 128)
			yCam = yMapSize * 128 - 1;
		cycle++;
		yCurveSin = Model.SINE[yCurve];
		yCUrveCos = Model.COSINE[yCurve];
		xCurveSin = Model.SINE[xCurve];
		xCurveCos = Model.COSINE[xCurve];
		tile_visibility_map = tile_visibility_maps[(yCurve - 128) / 32][xCurve / 64];
		xCamPos = xCam;
		zCamPos = zCam;
		yCamPos = yCam;
		xCamPosTile = xCam / 128;
		yCamPosTile = yCam / 128;
		plane__ = plane;
		minVisibleX = xCamPosTile - 25;
		if (minVisibleX < 0)
			minVisibleX = 0;
		minVisibleY = yCamPosTile - 25;
		if (minVisibleY < 0)
			minVisibleY = 0;
		maxVisibleX = xCamPosTile + 25;
		if (maxVisibleX > xMapSize)
			maxVisibleX = xMapSize;
		maxVisibleY = yCamPosTile + 25;
		if (maxVisibleY > yMapSize)
			maxVisibleY = yMapSize;
		processCulling();
		anInt446 = 0;
		for (int height = currentHL; height < zMapSize; height++) {
			Tile tiles[][] = tileArray[height];
			for (int x_ = minVisibleX; x_ < maxVisibleX; x_++) {
				for (int y_ = minVisibleY; y_ < maxVisibleY; y_++) {
					Tile tile = tiles[x_][y_];
					if (tile != null)
						if (tile.logicHeight > plane || !tile_visibility_map[(x_ - xCamPosTile) + 25][(y_ - yCamPosTile) + 25] && heightMap[height][x_][y_] - zCam < 2000) {
							tile.aBoolean1322 = false;
							tile.aBoolean1323 = false;
							tile.anInt1325 = 0;
						} else {
							tile.aBoolean1322 = true;
							tile.aBoolean1323 = true;
							tile.aBoolean1324 = tile.entityCount > 0;
							anInt446++;
						}
				}

			}

		}

		for (int height = currentHL; height < zMapSize; height++) {
			Tile tilePlane[][] = tileArray[height];
			for (int l2 = -25; l2 <= 0; l2++) {
				int i3 = xCamPosTile + l2;
				int x = xCamPosTile - l2;
				if (i3 >= minVisibleX || x < maxVisibleX) {
					for (int i4 = -25; i4 <= 0; i4++) {
						int y = yCamPosTile + i4;
						int i5 = yCamPosTile - i4;
						if (i3 >= minVisibleX) {
							if (y >= minVisibleY) {
								Tile tile = tilePlane[i3][y];
								if (tile != null && tile.aBoolean1322)
									renderTile(tile, true);
							}
							if (i5 < maxVisibleY) {
								Tile tile = tilePlane[i3][i5];
								if (tile != null && tile.aBoolean1322)
									renderTile(tile, true);
							}
						}
						if (x < maxVisibleX) {
							if (y >= minVisibleY) {
								Tile tile = tilePlane[x][y];
								if (tile != null && tile.aBoolean1322)
									renderTile(tile, true);
							}
							if (i5 < maxVisibleY) {
								Tile tile = tilePlane[x][i5];
								if (tile != null && tile.aBoolean1322)
									renderTile(tile, true);
							}
						}
						if (anInt446 == 0) {
							isClicked = false;
							return;
						}
					}

				}
			}

		}

		for (int height = currentHL; height < zMapSize; height++) {
			Tile tilesPlane[][] = tileArray[height];
			for (int j3 = -25; j3 <= 0; j3++) {
				int l3 = xCamPosTile + j3;
				int j4 = xCamPosTile - j3;
				if (l3 >= minVisibleX || j4 < maxVisibleX) {
					for (int l4 = -25; l4 <= 0; l4++) {
						int j5 = yCamPosTile + l4;
						int k5 = yCamPosTile - l4;
						if (l3 >= minVisibleX) {
							if (j5 >= minVisibleY) {
								Tile tile = tilesPlane[l3][j5];
								if (tile != null && tile.aBoolean1322)
									renderTile(tile, false);
							}
							if (k5 < maxVisibleY) {
								Tile tile = tilesPlane[l3][k5];
								if (tile != null && tile.aBoolean1322)
									renderTile(tile, false);
							}
						}
						if (j4 < maxVisibleX) {
							if (j5 >= minVisibleY) {
								Tile tile = tilesPlane[j4][j5];
								if (tile != null && tile.aBoolean1322)
									renderTile(tile, false);
							}
							if (k5 < maxVisibleY) {
								Tile tile = tilesPlane[j4][k5];
								if (tile != null && tile.aBoolean1322)
									renderTile(tile, false);
							}
						}
						if (anInt446 == 0) {
							isClicked = false;
							return;
						}
					}

				}
			}

		}

		isClicked = false;
	}

	private void renderTile(Tile mainTile, boolean flag) {
		tileDeque.insertBack(mainTile);
		do {
			Tile tempTile;
			do {
				tempTile = (Tile) tileDeque.popFront();
				if (tempTile == null)
					return;
			} while (!tempTile.aBoolean1323);
			int tileX = tempTile.tileX;
			int tileY = tempTile.tileY;
			int tileZ = tempTile.tileZ;
			int tilePlane = tempTile.plane;
			Tile tiles[][] = tileArray[tileZ];
			if (tempTile.aBoolean1322) {
				if (flag) {
					if (tileZ > 0) {
						Tile tile = tileArray[tileZ - 1][tileX][tileY];
						if (tile != null && tile.aBoolean1323)
							continue;
					}
					if (tileX <= xCamPosTile && tileX > minVisibleX) {
						Tile tile = tiles[tileX - 1][tileY];
						if (tile != null && tile.aBoolean1323 && (tile.aBoolean1322 || (tempTile.anInt1320 & 1) == 0))
							continue;
					}
					if (tileX >= xCamPosTile && tileX < maxVisibleX - 1) {
						Tile tile = tiles[tileX + 1][tileY];
						if (tile != null && tile.aBoolean1323 && (tile.aBoolean1322 || (tempTile.anInt1320 & 4) == 0))
							continue;
					}
					if (tileY <= yCamPosTile && tileY > minVisibleY) {
						Tile tile = tiles[tileX][tileY - 1];
						if (tile != null && tile.aBoolean1323 && (tile.aBoolean1322 || (tempTile.anInt1320 & 8) == 0))
							continue;
					}
					if (tileY >= yCamPosTile && tileY < maxVisibleY - 1) {
						Tile tile = tiles[tileX][tileY + 1];
						if (tile != null && tile.aBoolean1323 && (tile.aBoolean1322 || (tempTile.anInt1320 & 2) == 0))
							continue;
					}
				} else {
					flag = true;
				}
				tempTile.aBoolean1322 = false;
				if (tempTile.tileBelowThisTile != null) {
					Tile lowerTile = tempTile.tileBelowThisTile;
					if (lowerTile.plainTile != null) {
						if (!isTileCulled(0, tileX, tileY))
							drawPlainTile(lowerTile.plainTile, 0, yCurveSin, yCUrveCos, xCurveSin, xCurveCos, tileX, tileY);
					} else if (lowerTile.shapedTile != null && !isTileCulled(0, tileX, tileY))
						drawShapedTile(tileX, yCurveSin, xCurveSin, lowerTile.shapedTile, yCUrveCos, tileY, xCurveCos);
					WallObject wallObject = lowerTile.wallObject;
					if (wallObject != null)
						wallObject.node1.renderAtPoint(0, yCurveSin, yCUrveCos, xCurveSin, xCurveCos, wallObject.xPos - xCamPos, wallObject.zPos - zCamPos, wallObject.yPos - yCamPos, wallObject.uid, wallObject.wallObjUID);
					for (int ioCount = 0; ioCount < lowerTile.entityCount; ioCount++) {
						InteractableObject iObject = lowerTile.interactableObjects[ioCount];
						if (iObject != null)
							iObject.node.renderAtPoint(iObject.rotation, yCurveSin, yCUrveCos, xCurveSin, xCurveCos, iObject.worldX - xCamPos, iObject.worldZ - zCamPos, iObject.worldY - yCamPos, iObject.uid, iObject.interactiveObjUID);
					}

				}
				boolean flag1 = false;
				if (tempTile.plainTile != null) {
					if (!isTileCulled(tilePlane, tileX, tileY)) {
						flag1 = true;
						drawPlainTile(tempTile.plainTile, tilePlane, yCurveSin, yCUrveCos, xCurveSin, xCurveCos, tileX, tileY);
					}
				} else if (tempTile.shapedTile != null && !isTileCulled(tilePlane, tileX, tileY)) {
					flag1 = true;
					drawShapedTile(tileX, yCurveSin, xCurveSin, tempTile.shapedTile, yCUrveCos, tileY, xCurveCos);
				}
				int j1 = 0;
				int j2 = 0;
				WallObject wallObject = tempTile.wallObject;
				WallDecoration wallDecor = tempTile.wallDecoration;
				if (wallObject != null || wallDecor != null) {
					if (xCamPosTile == tileX)
						j1++;
					else if (xCamPosTile < tileX)
						j1 += 2;
					if (yCamPosTile == tileY)
						j1 += 3;
					else if (yCamPosTile > tileY)
						j1 += 6;
					j2 = anIntArray478[j1];
					tempTile.anInt1328 = anIntArray480[j1];
				}
				if (wallObject != null) {
					if ((wallObject.orientation & anIntArray479[j1]) != 0) {
						if (wallObject.orientation == 16) {
							tempTile.anInt1325 = 3;
							tempTile.anInt1326 = anIntArray481[j1];
							tempTile.anInt1327 = 3 - tempTile.anInt1326;
						} else if (wallObject.orientation == 32) {
							tempTile.anInt1325 = 6;
							tempTile.anInt1326 = anIntArray482[j1];
							tempTile.anInt1327 = 6 - tempTile.anInt1326;
						} else if (wallObject.orientation == 64) {
							tempTile.anInt1325 = 12;
							tempTile.anInt1326 = anIntArray483[j1];
							tempTile.anInt1327 = 12 - tempTile.anInt1326;
						} else {
							tempTile.anInt1325 = 9;
							tempTile.anInt1326 = anIntArray484[j1];
							tempTile.anInt1327 = 9 - tempTile.anInt1326;
						}
					} else {
						tempTile.anInt1325 = 0;
					}
					if ((wallObject.orientation & j2) != 0 && !isWallCulled(tilePlane, tileX, tileY, wallObject.orientation))
						wallObject.node1.renderAtPoint(0, yCurveSin, yCUrveCos, xCurveSin, xCurveCos, wallObject.xPos - xCamPos, wallObject.zPos - zCamPos, wallObject.yPos - yCamPos, wallObject.uid, wallObject.wallObjUID);
					if ((wallObject.orientation1 & j2) != 0 && !isWallCulled(tilePlane, tileX, tileY, wallObject.orientation1))
						wallObject.node2.renderAtPoint(0, yCurveSin, yCUrveCos, xCurveSin, xCurveCos, wallObject.xPos - xCamPos, wallObject.zPos - zCamPos, wallObject.yPos - yCamPos, wallObject.uid, wallObject.wallObjUID);
				}
				if (wallDecor != null && !isCulled(tilePlane, tileX, tileY, wallDecor.node.modelHeight))
					if ((wallDecor.configurationBits & j2) != 0)
						wallDecor.node.renderAtPoint(wallDecor.rotation, yCurveSin, yCUrveCos, xCurveSin, xCurveCos, wallDecor.xPos - xCamPos, wallDecor.zPos - zCamPos, wallDecor.yPos - yCamPos, wallDecor.uid, wallDecor.wallDecorUID);
					else if ((wallDecor.configurationBits & 0x300) != 0) {
						int j4 = wallDecor.xPos - xCamPos;
						int l5 = wallDecor.zPos - zCamPos;
						int k6 = wallDecor.yPos - yCamPos;
						int i8 = wallDecor.rotation;
						int k9;
						if (i8 == 1 || i8 == 2)
							k9 = -j4;
						else
							k9 = j4;
						int k10;
						if (i8 == 2 || i8 == 3)
							k10 = -k6;
						else
							k10 = k6;
						if ((wallDecor.configurationBits & 0x100) != 0 && k10 < k9) {
							int i11 = j4 + faceXoffset2[i8];
							int k11 = k6 + faceYOffset2[i8];
							wallDecor.node.renderAtPoint(i8 * 512 + 256, yCurveSin, yCUrveCos, xCurveSin, xCurveCos, i11, l5, k11, wallDecor.uid, wallDecor.wallDecorUID);
						}
						if ((wallDecor.configurationBits & 0x200) != 0 && k10 > k9) {
							int j11 = j4 + faceXOffset3[i8];
							int l11 = k6 + faceYOffset3[i8];
							wallDecor.node.renderAtPoint(i8 * 512 + 1280 & 0x7ff, yCurveSin, yCUrveCos, xCurveSin, xCurveCos, j11, l5, l11, wallDecor.uid, wallDecor.wallDecorUID);
						}
					}
				if (flag1) {
					GroundDecoration class49 = tempTile.groundDecoration;
					if (class49 != null)
						class49.node.renderAtPoint(0, yCurveSin, yCUrveCos, xCurveSin, xCurveCos, class49.xPos - xCamPos, class49.zPos - zCamPos, class49.yPos - yCamPos, class49.uid, class49.groundDecorUID);
					GroundItem object4_1 = tempTile.groundItem;
					if (object4_1 != null && object4_1.topItem == 0) {
						if (object4_1.secondGroundItem != null)
							object4_1.secondGroundItem.renderAtPoint(0, yCurveSin, yCUrveCos, xCurveSin, xCurveCos, object4_1.xPos - xCamPos, object4_1.zPos - zCamPos, object4_1.yPos - yCamPos, object4_1.uid, object4_1.newuid);
						if (object4_1.thirdGroundItem != null)
							object4_1.thirdGroundItem.renderAtPoint(0, yCurveSin, yCUrveCos, xCurveSin, xCurveCos, object4_1.xPos - xCamPos, object4_1.zPos - zCamPos, object4_1.yPos - yCamPos, object4_1.uid, object4_1.newuid);
						if (object4_1.firstGroundItem != null)
							object4_1.firstGroundItem.renderAtPoint(0, yCurveSin, yCUrveCos, xCurveSin, xCurveCos, object4_1.xPos - xCamPos, object4_1.zPos - zCamPos, object4_1.yPos - yCamPos, object4_1.uid, object4_1.newuid);
					}
				}
				int k4 = tempTile.anInt1320;
				if (k4 != 0) {
					if (tileX < xCamPosTile && (k4 & 4) != 0) {
						Tile tile = tiles[tileX + 1][tileY];
						if (tile != null && tile.aBoolean1323)
							tileDeque.insertBack(tile);
					}
					if (tileY < yCamPosTile && (k4 & 2) != 0) {
						Tile tile = tiles[tileX][tileY + 1];
						if (tile != null && tile.aBoolean1323)
							tileDeque.insertBack(tile);
					}
					if (tileX > xCamPosTile && (k4 & 1) != 0) {
						Tile tile = tiles[tileX - 1][tileY];
						if (tile != null && tile.aBoolean1323)
							tileDeque.insertBack(tile);
					}
					if (tileY > yCamPosTile && (k4 & 8) != 0) {
						Tile tile = tiles[tileX][tileY - 1];
						if (tile != null && tile.aBoolean1323)
							tileDeque.insertBack(tile);
					}
				}
			}
			if (tempTile.anInt1325 != 0) {
				boolean flag2 = true;
				for (int k1 = 0; k1 < tempTile.entityCount; k1++) {
					if (tempTile.interactableObjects[k1].height == cycle || (tempTile.objectFlags[k1] & tempTile.anInt1325) != tempTile.anInt1326)
						continue;
					flag2 = false;
					break;
				}

				if (flag2) {
					WallObject wallObject = tempTile.wallObject;
					if (!isWallCulled(tilePlane, tileX, tileY, wallObject.orientation))
						wallObject.node1.renderAtPoint(0, yCurveSin, yCUrveCos, xCurveSin, xCurveCos, wallObject.xPos - xCamPos, wallObject.zPos - zCamPos, wallObject.yPos - yCamPos, wallObject.uid, wallObject.wallObjUID);
					tempTile.anInt1325 = 0;
				}
			}
			if (tempTile.aBoolean1324)
				try {
					int entityCount = tempTile.entityCount;
					tempTile.aBoolean1324 = false;
					int l1 = 0;
					label0: for (int k2 = 0; k2 < entityCount; k2++) {
						InteractableObject interactObject = tempTile.interactableObjects[k2];
						if (interactObject.height == cycle)
							continue;
						for (int k3 = interactObject.tileLeft; k3 <= interactObject.tileRight; k3++) {
							for (int l4 = interactObject.tileTop; l4 <= interactObject.tileBottom; l4++) {
								Tile tile = tiles[k3][l4];
								if (tile.aBoolean1322) {
									tempTile.aBoolean1324 = true;
								} else {
									if (tile.anInt1325 == 0)
										continue;
									int l6 = 0;
									if (k3 > interactObject.tileLeft)
										l6++;
									if (k3 < interactObject.tileRight)
										l6 += 4;
									if (l4 > interactObject.tileTop)
										l6 += 8;
									if (l4 < interactObject.tileBottom)
										l6 += 2;
									if ((l6 & tile.anInt1325) != tempTile.anInt1327)
										continue;
									tempTile.aBoolean1324 = true;
								}
								continue label0;
							}

						}

						aClass28Array462[l1++] = interactObject;
						int i5 = xCamPosTile - interactObject.tileLeft;
						int i6 = interactObject.tileRight - xCamPosTile;
						if (i6 > i5)
							i5 = i6;
						int i7 = yCamPosTile - interactObject.tileTop;
						int j8 = interactObject.tileBottom - yCamPosTile;
						if (j8 > i7)
							interactObject.anInt527 = i5 + j8;
						else
							interactObject.anInt527 = i5 + i7;
					}

					while (l1 > 0) {
						int i3 = -50;
						int l3 = -1;
						for (int j5 = 0; j5 < l1; j5++) {
							InteractableObject class28_2 = aClass28Array462[j5];
							if (class28_2.height != cycle)
								if (class28_2.anInt527 > i3) {
									i3 = class28_2.anInt527;
									l3 = j5;
								} else if (class28_2.anInt527 == i3) {
									int j7 = class28_2.worldX - xCamPos;
									int k8 = class28_2.worldY - yCamPos;
									int l9 = aClass28Array462[l3].worldX - xCamPos;
									int l10 = aClass28Array462[l3].worldY - yCamPos;
									if (j7 * j7 + k8 * k8 > l9 * l9 + l10 * l10)
										l3 = j5;
								}
						}

						if (l3 == -1)
							break;
						InteractableObject class28_3 = aClass28Array462[l3];
						class28_3.height = cycle;
						if (!isCulled(tilePlane, class28_3.tileLeft, class28_3.tileRight, class28_3.tileTop, class28_3.tileBottom, class28_3.node.modelHeight))
							class28_3.node.renderAtPoint(class28_3.rotation, yCurveSin, yCUrveCos, xCurveSin, xCurveCos, class28_3.worldX - xCamPos, class28_3.worldZ - zCamPos, class28_3.worldY - yCamPos, class28_3.uid, class28_3.interactiveObjUID);
						for (int k7 = class28_3.tileLeft; k7 <= class28_3.tileRight; k7++) {
							for (int l8 = class28_3.tileTop; l8 <= class28_3.tileBottom; l8++) {
								Tile class30_sub3_22 = tiles[k7][l8];
								if (class30_sub3_22.anInt1325 != 0)
									tileDeque.insertBack(class30_sub3_22);
								else if ((k7 != tileX || l8 != tileY) && class30_sub3_22.aBoolean1323)
									tileDeque.insertBack(class30_sub3_22);
							}

						}

					}
					if (tempTile.aBoolean1324)
						continue;
				} catch (Exception _ex) {
					_ex.printStackTrace();
					tempTile.aBoolean1324 = false;
				}
			if (!tempTile.aBoolean1323 || tempTile.anInt1325 != 0)
				continue;
			if (tileX <= xCamPosTile && tileX > minVisibleX) {
				Tile class30_sub3_8 = tiles[tileX - 1][tileY];
				if (class30_sub3_8 != null && class30_sub3_8.aBoolean1323)
					continue;
			}
			if (tileX >= xCamPosTile && tileX < maxVisibleX - 1) {
				Tile class30_sub3_9 = tiles[tileX + 1][tileY];
				if (class30_sub3_9 != null && class30_sub3_9.aBoolean1323)
					continue;
			}
			if (tileY <= yCamPosTile && tileY > minVisibleY) {
				Tile class30_sub3_10 = tiles[tileX][tileY - 1];
				if (class30_sub3_10 != null && class30_sub3_10.aBoolean1323)
					continue;
			}
			if (tileY >= yCamPosTile && tileY < maxVisibleY - 1) {
				Tile class30_sub3_11 = tiles[tileX][tileY + 1];
				if (class30_sub3_11 != null && class30_sub3_11.aBoolean1323)
					continue;
			}
			tempTile.aBoolean1323 = false;
			anInt446--;
			GroundItem object4 = tempTile.groundItem;
			if (object4 != null && object4.topItem != 0) {
				if (object4.secondGroundItem != null)
					object4.secondGroundItem.renderAtPoint(0, yCurveSin, yCUrveCos, xCurveSin, xCurveCos, object4.xPos - xCamPos, object4.zPos - zCamPos - object4.topItem, object4.yPos - yCamPos, object4.uid, object4.newuid);
				if (object4.thirdGroundItem != null)
					object4.thirdGroundItem.renderAtPoint(0, yCurveSin, yCUrveCos, xCurveSin, xCurveCos, object4.xPos - xCamPos, object4.zPos - zCamPos - object4.topItem, object4.yPos - yCamPos, object4.uid, object4.newuid);
				if (object4.firstGroundItem != null)
					object4.firstGroundItem.renderAtPoint(0, yCurveSin, yCUrveCos, xCurveSin, xCurveCos, object4.xPos - xCamPos, object4.zPos - zCamPos - object4.topItem, object4.yPos - yCamPos, object4.uid, object4.newuid);
			}
			if (tempTile.anInt1328 != 0) {
				WallDecoration class26 = tempTile.wallDecoration;
				if (class26 != null && !isCulled(tilePlane, tileX, tileY, class26.node.modelHeight))
					if ((class26.configurationBits & tempTile.anInt1328) != 0)
						class26.node.renderAtPoint(class26.rotation, yCurveSin, yCUrveCos, xCurveSin, xCurveCos, class26.xPos - xCamPos, class26.zPos - zCamPos, class26.yPos - yCamPos, class26.uid, class26.wallDecorUID);
					else if ((class26.configurationBits & 0x300) != 0) {
						int l2 = class26.xPos - xCamPos;
						int j3 = class26.zPos - zCamPos;
						int i4 = class26.yPos - yCamPos;
						int k5 = class26.rotation;
						int j6;
						if (k5 == 1 || k5 == 2)
							j6 = -l2;
						else
							j6 = l2;
						int l7;
						if (k5 == 2 || k5 == 3)
							l7 = -i4;
						else
							l7 = i4;
						if ((class26.configurationBits & 0x100) != 0 && l7 >= j6) {
							int i9 = l2 + faceXoffset2[k5];
							int i10 = i4 + faceYOffset2[k5];
							class26.node.renderAtPoint(k5 * 512 + 256, yCurveSin, yCUrveCos, xCurveSin, xCurveCos, i9, j3, i10, class26.uid, class26.wallDecorUID);
						}
						if ((class26.configurationBits & 0x200) != 0 && l7 <= j6) {
							int j9 = l2 + faceXOffset3[k5];
							int j10 = i4 + faceYOffset3[k5];
							class26.node.renderAtPoint(k5 * 512 + 1280 & 0x7ff, yCurveSin, yCUrveCos, xCurveSin, xCurveCos, j9, j3, j10, class26.uid, class26.wallDecorUID);
						}
					}
				WallObject class10_2 = tempTile.wallObject;
				if (class10_2 != null) {
					if ((class10_2.orientation1 & tempTile.anInt1328) != 0 && !isWallCulled(tilePlane, tileX, tileY, class10_2.orientation1))
						class10_2.node2.renderAtPoint(0, yCurveSin, yCUrveCos, xCurveSin, xCurveCos, class10_2.xPos - xCamPos, class10_2.zPos - zCamPos, class10_2.yPos - yCamPos, class10_2.uid, class10_2.wallObjUID);
					if ((class10_2.orientation & tempTile.anInt1328) != 0 && !isWallCulled(tilePlane, tileX, tileY, class10_2.orientation))
						class10_2.node1.renderAtPoint(0, yCurveSin, yCUrveCos, xCurveSin, xCurveCos, class10_2.xPos - xCamPos, class10_2.zPos - zCamPos, class10_2.yPos - yCamPos, class10_2.uid, class10_2.wallObjUID);
				}
			}
			if (tileZ < zMapSize - 1) {
				Tile class30_sub3_12 = tileArray[tileZ + 1][tileX][tileY];
				if (class30_sub3_12 != null && class30_sub3_12.aBoolean1323)
					tileDeque.insertBack(class30_sub3_12);
			}
			if (tileX < xCamPosTile) {
				Tile class30_sub3_13 = tiles[tileX + 1][tileY];
				if (class30_sub3_13 != null && class30_sub3_13.aBoolean1323)
					tileDeque.insertBack(class30_sub3_13);
			}
			if (tileY < yCamPosTile) {
				Tile class30_sub3_14 = tiles[tileX][tileY + 1];
				if (class30_sub3_14 != null && class30_sub3_14.aBoolean1323)
					tileDeque.insertBack(class30_sub3_14);
			}
			if (tileX > xCamPosTile) {
				Tile class30_sub3_15 = tiles[tileX - 1][tileY];
				if (class30_sub3_15 != null && class30_sub3_15.aBoolean1323)
					tileDeque.insertBack(class30_sub3_15);
			}
			if (tileY > yCamPosTile) {
				Tile class30_sub3_16 = tiles[tileX][tileY - 1];
				if (class30_sub3_16 != null && class30_sub3_16.aBoolean1323)
					tileDeque.insertBack(class30_sub3_16);
			}
		} while (true);
	}
	
	public boolean opaque_floor_texture = false;
	private void drawPlainTile(PlainTile pTile, int tZ, int j, int k, int xSin, int xCos, int tX, int tY) {
		int xC;
		int xA = xC = (tX << 7) - xCamPos;
		int yB;
		int yA = yB = (tY << 7) - yCamPos;
		int xD;
		int xB = xD = xA + 128;
		int yC;
		int yD = yC = yA + 128;
		int zA = heightMap[tZ][tX][tY] - zCamPos;
		int zB = heightMap[tZ][tX + 1][tY] - zCamPos;
		int zD = heightMap[tZ][tX + 1][tY + 1] - zCamPos;
		int zC = heightMap[tZ][tX][tY + 1] - zCamPos;
		int angle = yA * xSin + xA * xCos >> 16;
		yA = yA * xCos - xA * xSin >> 16;
		xA = angle;
		angle = zA * k - yA * j >> 16;
		yA = zA * j + yA * k >> 16;
		zA = angle;
		if (yA < 50)
			return;
		angle = yB * xSin + xB * xCos >> 16;
		yB = yB * xCos - xB * xSin >> 16;
		xB = angle;
		angle = zB * k - yB * j >> 16;
		yB = zB * j + yB * k >> 16;
		zB = angle;
		if (yB < 50)
			return;
		angle = yD * xSin + xD * xCos >> 16;
		yD = yD * xCos - xD * xSin >> 16;
		xD = angle;
		angle = zD * k - yD * j >> 16;
		yD = zD * j + yD * k >> 16;
		zD = angle;
		if (yD < 50)
			return;
		angle = yC * xSin + xC * xCos >> 16;
		yC = yC * xCos - xC * xSin >> 16;
		xC = angle;
		angle = zC * k - yC * j >> 16;
		yC = zC * j + yC * k >> 16;
		zC = angle;
		if (yC < 50)
			return;
		int screenXA = Rasterizer.center_x + (xA << Client.log_view_dist) / yA;
		int screenYA = Rasterizer.center_y + (zA << Client.log_view_dist) / yA;
		int screenXB = Rasterizer.center_x + (xB << Client.log_view_dist) / yB;
		int screenYB = Rasterizer.center_y + (zB << Client.log_view_dist) / yB;
		int screenXD = Rasterizer.center_x + (xD << Client.log_view_dist) / yD;
		int screenYD = Rasterizer.center_y + (zD << Client.log_view_dist) / yD;
		int screenXC = Rasterizer.center_x + (xC << Client.log_view_dist) / yC;
		int screenYC = Rasterizer.center_y + (zC << Client.log_view_dist) / yC;
		Rasterizer.alpha = 0;
		if ((screenXD - screenXC) * (screenYB - screenYC) - (screenYD - screenYC) * (screenXB - screenXC) > 0) {
			Rasterizer.restrict_edges = screenXD < 0 || screenXC < 0 || screenXB < 0 || screenXD > DrawingArea.viewportRX || screenXC > DrawingArea.viewportRX || screenXB > DrawingArea.viewportRX;
			if (isClicked && mouseWithinTriangle(clickX, clickY, screenYD, screenYC, screenYB, screenXD, screenXC, screenXB)) {
				clickedTileX = tX;
				clickedTileY = tY;
			}
			/**
			 * Draws triangle CBD (first half)
			 */
			if(!Client.getOption("hd_tex")) {
				if (pTile.textureId == -1) {
					if (pTile.colourD != 0xbc614e)//LD
						Rasterizer.drawShadedTriangle(screenYD, screenYC, screenYB, screenXD, screenXC, screenXB, pTile.colourD, pTile.colourC, pTile.colourB);
				} else if (!lowMem) {
					if (pTile.isFlat)
						Rasterizer.drawTextureTriangle1(screenYD, screenYC, screenYB, screenXD, screenXC, screenXB, pTile.colourD, pTile.colourC, pTile.colourB, xA, xB, xC, zA, zB, zC, yA, yB, yC, pTile.textureId);
					else
						Rasterizer.drawTextureTriangle1(screenYD, screenYC, screenYB, screenXD, screenXC, screenXB, pTile.colourD, pTile.colourC, pTile.colourB, xD, xC, xB, zD, zC, zB, yD, yC, yB, pTile.textureId);
				} else {
					int i7 = textureRGBColour[pTile.textureId];//LD
					Rasterizer.drawShadedTriangle(screenYD, screenYC, screenYB, screenXD, screenXC, screenXB, mixColour(i7, pTile.colourD), mixColour(i7, pTile.colourC), mixColour(i7, pTile.colourB));
				}
			} else {
				if(pTile.textureId != -1) {
					if(pTile.isFlat)
						Rasterizer.render_texture_triangle(screenYD, screenYC, screenYB, screenXD, screenXC, screenXB, pTile.colourD, pTile.colourC, pTile.colourB, xA, xB, xC, zA, zB, zC, yA, yB, yC, !lowMem || pTile.rgbColour == -1 ? pTile.textureId : -1, pTile.color, true, opaque_floor_texture);
					else
						Rasterizer.render_texture_triangle(screenYD, screenYC, screenYB, screenXD, screenXC, screenXB, pTile.colourD, pTile.colourC, pTile.colourB, xD, xC, xB, zD, zC, zB, yD, yC, yB, !lowMem || pTile.rgbColour == -1 ? pTile.textureId : -1, pTile.color, true, opaque_floor_texture);
				} else if(pTile.colourD != 0xbc614e) {//LD
					Rasterizer.drawShadedTriangle(screenYD, screenYC, screenYB, screenXD, screenXC, screenXB, pTile.colourD, pTile.colourC, pTile.colourB);
				}
			}
		}
		if ((screenXA - screenXB) * (screenYC - screenYB) - (screenYA - screenYB) * (screenXC - screenXB) > 0) {
			Rasterizer.restrict_edges = screenXA < 0 || screenXB < 0 || screenXC < 0 || screenXA > DrawingArea.viewportRX || screenXB > DrawingArea.viewportRX || screenXC > DrawingArea.viewportRX;
			if (isClicked && mouseWithinTriangle(clickX, clickY, screenYA, screenYB, screenYC, screenXA, screenXB, screenXC)) {
				clickedTileX = tX;
				clickedTileY = tY;
			}
			/**
			 * Draws triangle ACD (second half)
			 */
			if (pTile.textureId == -1) {
				if (pTile.colourA != 0xbc614e) {
					//LD
					Rasterizer.drawShadedTriangle(screenYA, screenYB, screenYC, screenXA, screenXB, screenXC, pTile.colourA, pTile.colourB, pTile.colourC);
				}
			} else {
				if (!lowMem) {
					if(Client.getOption("hd_tex")) 
						Rasterizer.render_texture_triangle(screenYA, screenYB, screenYC, screenXA, screenXB, screenXC, pTile.colourA, pTile.colourB, pTile.colourC, xA, xB, xC, zA, zB, zC, yA, yB, yC, !lowMem || pTile.color == -1 ? pTile.textureId : -1, pTile.color, true, opaque_floor_texture);
					else
						Rasterizer.drawTextureTriangle1(screenYA, screenYB, screenYC, screenXA, screenXB, screenXC, pTile.colourA, pTile.colourB, pTile.colourC, xA, xB, xC, zA, zB, zC, yA, yB, yC, pTile.textureId);
					return;
				}
				if(!Client.getOption("hd_tex")) {
					int j7 = textureRGBColour[pTile.textureId];
					//LD
					Rasterizer.drawShadedTriangle(screenYA, screenYB, screenYC, screenXA, screenXB, screenXC, mixColour(j7, pTile.colourA), mixColour(j7, pTile.colourB), mixColour(j7, pTile.colourC));
				}
			}
		}
	}

	private void drawShapedTile(int i, int j, int k, ShapedTile sTile, int l, int i1, int j1) {
		int k1 = sTile.origVertexX.length;
		for (int l1 = 0; l1 < k1; l1++) {
			int viewSpaceX = sTile.origVertexX[l1] - xCamPos;
			int viewSpaceY = sTile.origVertexY[l1] - zCamPos;
			int viewSpaceZ = sTile.origVertexZ[l1] - yCamPos;
			int viewSpaceDepth = viewSpaceZ * k + viewSpaceX * j1 >> 16;
			viewSpaceZ = viewSpaceZ * j1 - viewSpaceX * k >> 16;
			viewSpaceX = viewSpaceDepth;
			viewSpaceDepth = viewSpaceY * l - viewSpaceZ * j >> 16;
			viewSpaceZ = viewSpaceY * j + viewSpaceZ * l >> 16;
			viewSpaceY = viewSpaceDepth;
			if (viewSpaceZ < 50)
				return;
			if (sTile.triangleTexture != null) {
				ShapedTile.viewSpaceX[l1] = viewSpaceX;
				ShapedTile.viewSpaceY[l1] = viewSpaceY;
				ShapedTile.viewSpaceZ[l1] = viewSpaceZ;
			}
			ShapedTile.screenX[l1] = Rasterizer.center_x + (viewSpaceX << Client.log_view_dist) / viewSpaceZ;
			ShapedTile.screenY[l1] = Rasterizer.center_y + (viewSpaceY << Client.log_view_dist) / viewSpaceZ;
		}

		Rasterizer.alpha = 0;
		k1 = sTile.triangleA.length;
		for (int j2 = 0; j2 < k1; j2++) {
			int indexA = sTile.triangleA[j2];
			int indexB = sTile.triangleB[j2];
			int indexC = sTile.triangleC[j2];
			int sXA = ShapedTile.screenX[indexA];
			int sXB = ShapedTile.screenX[indexB];
			int sXC = ShapedTile.screenX[indexC];
			int sYA = ShapedTile.screenY[indexA];
			int sYB = ShapedTile.screenY[indexB];
			int sYC = ShapedTile.screenY[indexC];
			if ((sXA - sXB) * (sYC - sYB) - (sYA - sYB) * (sXC - sXB) > 0) {
				Rasterizer.restrict_edges = sXA < 0 || sXB < 0 || sXC < 0 || sXA > DrawingArea.viewportRX || sXB > DrawingArea.viewportRX || sXC > DrawingArea.viewportRX;
				if (isClicked && mouseWithinTriangle(clickX, clickY, sYA, sYB, sYC, sXA, sXB, sXC)) {
					clickedTileX = i;
					clickedTileY = i1;
				}
				if(Client.getOption("hd_tex")) {
					if(sTile.triangleTexture == null || sTile.triangleTexture[j2] == -1) 
					{
						if(sTile.triangleHslA[j2] != 0xbc614e) 
						{//LD
							Rasterizer.drawShadedTriangle(sYA, sYB, sYC, sXA, sXB, sXC, sTile.triangleHslA[j2], sTile.triangleHslB[j2], sTile.triangleHslC[j2]);
						}
					} else
					{
						if(sTile.flat)
							Rasterizer.render_texture_triangle(sYA, sYB, sYC, sXA, sXB, sXC, sTile.triangleHslA[j2], sTile.triangleHslB[j2], sTile.triangleHslC[j2], ShapedTile.viewSpaceX[0], ShapedTile.viewSpaceX[1], ShapedTile.viewSpaceX[3], ShapedTile.viewSpaceY[0], ShapedTile.viewSpaceY[1], ShapedTile.viewSpaceY[3], ShapedTile.viewSpaceZ[0], ShapedTile.viewSpaceZ[1], ShapedTile.viewSpaceZ[3], !lowMem || sTile.displayColor[j2] == -1 ? sTile.triangleTexture[j2] : -1, sTile.displayColor[j2], true, opaque_floor_texture);
						else
							Rasterizer.render_texture_triangle(sYA, sYB, sYC, sXA, sXB, sXC, sTile.triangleHslA[j2], sTile.triangleHslB[j2], sTile.triangleHslC[j2], ShapedTile.viewSpaceX[indexA], ShapedTile.viewSpaceX[indexB], ShapedTile.viewSpaceX[indexC], ShapedTile.viewSpaceY[indexA], ShapedTile.viewSpaceY[indexB], ShapedTile.viewSpaceY[indexC], ShapedTile.viewSpaceZ[indexA], ShapedTile.viewSpaceZ[indexB], ShapedTile.viewSpaceZ[indexC], !lowMem || sTile.displayColor[j2] == -1 ? sTile.triangleTexture[j2] : -1, sTile.displayColor[j2], true, opaque_floor_texture);
					}
				} else {
					if (sTile.triangleTexture == null || sTile.triangleTexture[j2] == -1) {
						if (sTile.triangleHslA[j2] != 0xbc614e)//LD
							Rasterizer.drawShadedTriangle(sYA, sYB, sYC, sXA, sXB, sXC, sTile.triangleHslA[j2], sTile.triangleHslB[j2], sTile.triangleHslC[j2]);
					} else if (!lowMem) {
						if (sTile.flat)
							Rasterizer.drawTextureTriangle1(sYA, sYB, sYC, sXA, sXB, sXC, sTile.triangleHslA[j2], sTile.triangleHslB[j2], sTile.triangleHslC[j2], ShapedTile.viewSpaceX[0], ShapedTile.viewSpaceX[1], ShapedTile.viewSpaceX[3], ShapedTile.viewSpaceY[0], ShapedTile.viewSpaceY[1], ShapedTile.viewSpaceY[3], ShapedTile.viewSpaceZ[0], ShapedTile.viewSpaceZ[1], ShapedTile.viewSpaceZ[3], sTile.triangleTexture[j2]);
						else
							Rasterizer.drawTextureTriangle1(sYA, sYB, sYC, sXA, sXB, sXC, sTile.triangleHslA[j2], sTile.triangleHslB[j2], sTile.triangleHslC[j2], ShapedTile.viewSpaceX[indexA], ShapedTile.viewSpaceX[indexB], ShapedTile.viewSpaceX[indexC], ShapedTile.viewSpaceY[indexA], ShapedTile.viewSpaceY[indexB], ShapedTile.viewSpaceY[indexC], ShapedTile.viewSpaceZ[indexA], ShapedTile.viewSpaceZ[indexB], ShapedTile.viewSpaceZ[indexC], sTile.triangleTexture[j2]);
					} else {
						int rgb = textureRGBColour[sTile.triangleTexture[j2]];//LD
						Rasterizer.drawShadedTriangle(sYA, sYB, sYC, sXA, sXB, sXC, mixColour(rgb, sTile.triangleHslA[j2]), mixColour(rgb, sTile.triangleHslB[j2]), mixColour(rgb, sTile.triangleHslC[j2]));
					}
				}
			}
		}

	}

	private int mixColour(int colour1, int colour2) {
		colour2 = 127 - colour2;
		colour2 = (colour2 * (colour1 & 0x7f)) / 160;
		if (colour2 < 2)
			colour2 = 2;
		else if (colour2 > 126)
			colour2 = 126;
		return (colour1 & 0xff80) + colour2;
	}

	private boolean mouseWithinTriangle(int mouseX, int mouseY, int triangleYA, int triangleYB, int triangleYC, int triangleXA, int triangleXB, int triangleXC) {
		if (mouseY < triangleYA && mouseY < triangleYB && mouseY < triangleYC)
			return false;
		if (mouseY > triangleYA && mouseY > triangleYB && mouseY > triangleYC)
			return false;
		if (mouseX < triangleXA && mouseX < triangleXB && mouseX < triangleXC)
			return false;
		if (mouseX > triangleXA && mouseX > triangleXB && mouseX > triangleXC)
			return false;
		int i2 = (mouseY - triangleYA) * (triangleXB - triangleXA) - (mouseX - triangleXA) * (triangleYB - triangleYA);
		int j2 = (mouseY - triangleYC) * (triangleXA - triangleXC) - (mouseX - triangleXC) * (triangleYA - triangleYC);
		int k2 = (mouseY - triangleYB) * (triangleXC - triangleXB) - (mouseX - triangleXB) * (triangleYC - triangleYB);
		return i2 * k2 > 0 && k2 * j2 > 0;
	}

	private void processCulling() {
		int count = cullingClusterPointer[plane__];
		CullingCluster clusters[] = cullingClusters[plane__];
		processedClusterPtr = 0;
		for (int k = 0; k < count; k++) {
			CullingCluster cluster = clusters[k];
			if (cluster.searchMask == 1) {
				int xDistFromCamStart = (cluster.tileStartX - xCamPosTile) + 25;
				if (xDistFromCamStart < 0 || xDistFromCamStart > 50)
					continue;
				int yDistFromCamStart = (cluster.tileStartY - yCamPosTile) + 25;
				if (yDistFromCamStart < 0)
					yDistFromCamStart = 0;
				int yDistFromCamEnd = (cluster.tileEndY - yCamPosTile) + 25;
				if (yDistFromCamEnd > 50)
					yDistFromCamEnd = 50;
				boolean visisble = false;
				while (yDistFromCamStart <= yDistFromCamEnd)
					if (tile_visibility_map[xDistFromCamStart][yDistFromCamStart++]) {
						visisble = true;
						break;
					}
				if (!visisble)
					continue;
				int xDistFromCamStartReal = xCamPos - cluster.worldStartX;
				if (xDistFromCamStartReal > 32) {
					cluster.tileDistanceEnum = 1;
				} else {
					if (xDistFromCamStartReal >= -32)
						continue;
					cluster.tileDistanceEnum = 2;
					xDistFromCamStartReal = -xDistFromCamStartReal;
				}
				cluster.worldDistanceFromCameraStartY = (cluster.worldStartY - yCamPos << 8) / xDistFromCamStartReal;
				cluster.worldDistanceFromCameraEndY = (cluster.worldEndY - yCamPos << 8) / xDistFromCamStartReal;
				cluster.worldDistanceFromCameraStartZ = (cluster.worldStartZ - zCamPos << 8) / xDistFromCamStartReal;
				cluster.worldDistanceFromCameraEndZ = (cluster.worldEndZ - zCamPos << 8) / xDistFromCamStartReal;
				processedClusters[processedClusterPtr++] = cluster;
				continue;
			}
			if (cluster.searchMask == 2) {
				int yDIstFromCamStart = (cluster.tileStartY - yCamPosTile) + 25;
				if (yDIstFromCamStart < 0 || yDIstFromCamStart > 50)
					continue;
				int xDistFromCamStart = (cluster.tileStartX - xCamPosTile) + 25;
				if (xDistFromCamStart < 0)
					xDistFromCamStart = 0;
				int xDistFromCamEnd = (cluster.tileEndX - xCamPosTile) + 25;
				if (xDistFromCamEnd > 50)
					xDistFromCamEnd = 50;
				boolean visible = false;
				while (xDistFromCamStart <= xDistFromCamEnd)
					if (tile_visibility_map[xDistFromCamStart++][yDIstFromCamStart]) {
						visible = true;
						break;
					}
				if (!visible)
					continue;
				int yDistFromCamStartReal = yCamPos - cluster.worldStartY;
				if (yDistFromCamStartReal > 32) {
					cluster.tileDistanceEnum = 3;
				} else {
					if (yDistFromCamStartReal >= -32)
						continue;
					cluster.tileDistanceEnum = 4;
					yDistFromCamStartReal = -yDistFromCamStartReal;
				}
				cluster.worldDistanceFromCameraStartX = (cluster.worldStartX - xCamPos << 8) / yDistFromCamStartReal;
				cluster.worldDistanceFromCameraEndX = (cluster.worldEndX - xCamPos << 8) / yDistFromCamStartReal;
				cluster.worldDistanceFromCameraStartZ = (cluster.worldStartZ - zCamPos << 8) / yDistFromCamStartReal;
				cluster.worldDistanceFromCameraEndZ = (cluster.worldEndZ - zCamPos << 8) / yDistFromCamStartReal;
				processedClusters[processedClusterPtr++] = cluster;
			} else if (cluster.searchMask == 4) {
				int yDistFromCamStartReal = cluster.worldStartZ - zCamPos;
				if (yDistFromCamStartReal > 128) {
					int yDistFromCamStart = (cluster.tileStartY - yCamPosTile) + 25;
					if (yDistFromCamStart < 0)
						yDistFromCamStart = 0;
					int yDistFromCamEnd = (cluster.tileEndY - yCamPosTile) + 25;
					if (yDistFromCamEnd > 50)
						yDistFromCamEnd = 50;
					if (yDistFromCamStart <= yDistFromCamEnd) {
						int xDistFromCamStart = (cluster.tileStartX - xCamPosTile) + 25;
						if (xDistFromCamStart < 0)
							xDistFromCamStart = 0;
						int xDistFromCamEnd = (cluster.tileEndX - xCamPosTile) + 25;
						if (xDistFromCamEnd > 50)
							xDistFromCamEnd = 50;
						boolean visisble = false;
						label0: for (int _x = xDistFromCamStart; _x <= xDistFromCamEnd; _x++) {
							for (int _y = yDistFromCamStart; _y <= yDistFromCamEnd; _y++) {
								if (!tile_visibility_map[_x][_y])
									continue;
								visisble = true;
								break label0;
							}

						}

						if (visisble) {
							cluster.tileDistanceEnum = 5;
							cluster.worldDistanceFromCameraStartX = (cluster.worldStartX - xCamPos << 8) / yDistFromCamStartReal;
							cluster.worldDistanceFromCameraEndX = (cluster.worldEndX - xCamPos << 8) / yDistFromCamStartReal;
							cluster.worldDistanceFromCameraStartY = (cluster.worldStartY - yCamPos << 8) / yDistFromCamStartReal;
							cluster.worldDistanceFromCameraEndY = (cluster.worldEndY - yCamPos << 8) / yDistFromCamStartReal;
							processedClusters[processedClusterPtr++] = cluster;
						}
					}
				}
			}
		}

	}

	private boolean isTileCulled(int plane, int x, int y) {
		int l = cycleMap[plane][x][y];
		if (l == -cycle)
			return false;
		if (l == cycle)
			return true;
		int i1 = x << 7;
		int j1 = y << 7;
		if (isCulled(i1 + 1, heightMap[plane][x][y], j1 + 1) && isCulled((i1 + 128) - 1, heightMap[plane][x + 1][y], j1 + 1) && isCulled((i1 + 128) - 1, heightMap[plane][x + 1][y + 1], (j1 + 128) - 1) && isCulled(i1 + 1, heightMap[plane][x][y + 1], (j1 + 128) - 1)) {
			cycleMap[plane][x][y] = cycle;
			return true;
		} else {
			cycleMap[plane][x][y] = -cycle;
			return false;
		}
	}

	private boolean isWallCulled(int z, int x, int y, int l) {
		if (!isTileCulled(z, x, y))
			return false;
		int i1 = x << 7;
		int j1 = y << 7;
		int k1 = heightMap[z][x][y] - 1;
		int l1 = k1 - 120;
		int i2 = k1 - 230;
		int j2 = k1 - 238;
		if (l < 16) {
			if (l == 1) {
				if (i1 > xCamPos) {
					if (!isCulled(i1, k1, j1))
						return false;
					if (!isCulled(i1, k1, j1 + 128))
						return false;
				}
				if (z > 0) {
					if (!isCulled(i1, l1, j1))
						return false;
					if (!isCulled(i1, l1, j1 + 128))
						return false;
				}
				return isCulled(i1, i2, j1) && isCulled(i1, i2, j1 + 128);
			}
			if (l == 2) {
				if (j1 < yCamPos) {
					if (!isCulled(i1, k1, j1 + 128))
						return false;
					if (!isCulled(i1 + 128, k1, j1 + 128))
						return false;
				}
				if (z > 0) {
					if (!isCulled(i1, l1, j1 + 128))
						return false;
					if (!isCulled(i1 + 128, l1, j1 + 128))
						return false;
				}
				return isCulled(i1, i2, j1 + 128) && isCulled(i1 + 128, i2, j1 + 128);
			}
			if (l == 4) {
				if (i1 < xCamPos) {
					if (!isCulled(i1 + 128, k1, j1))
						return false;
					if (!isCulled(i1 + 128, k1, j1 + 128))
						return false;
				}
				if (z > 0) {
					if (!isCulled(i1 + 128, l1, j1))
						return false;
					if (!isCulled(i1 + 128, l1, j1 + 128))
						return false;
				}
				return isCulled(i1 + 128, i2, j1) && isCulled(i1 + 128, i2, j1 + 128);
			}
			if (l == 8) {
				if (j1 > yCamPos) {
					if (!isCulled(i1, k1, j1))
						return false;
					if (!isCulled(i1 + 128, k1, j1))
						return false;
				}
				if (z > 0) {
					if (!isCulled(i1, l1, j1))
						return false;
					if (!isCulled(i1 + 128, l1, j1))
						return false;
				}
				return isCulled(i1, i2, j1) && isCulled(i1 + 128, i2, j1);
			}
		}
		if (!isCulled(i1 + 64, j2, j1 + 64))
			return false;
		if (l == 16)
			return isCulled(i1, i2, j1 + 128);
		if (l == 32)
			return isCulled(i1 + 128, i2, j1 + 128);
		if (l == 64)
			return isCulled(i1 + 128, i2, j1);
		if (l == 128) {
			return isCulled(i1, i2, j1);
		} else {
			System.out.println("Warning unsupported wall type");
			return true;
		}
	}

	private boolean isCulled(int i, int j, int k, int l) {
		if (!isTileCulled(i, j, k))
			return false;
		int i1 = j << 7;
		int j1 = k << 7;
		return isCulled(i1 + 1, heightMap[i][j][k] - l, j1 + 1) && isCulled((i1 + 128) - 1, heightMap[i][j + 1][k] - l, j1 + 1) && isCulled((i1 + 128) - 1, heightMap[i][j + 1][k + 1] - l, (j1 + 128) - 1) && isCulled(i1 + 1, heightMap[i][j][k + 1] - l, (j1 + 128) - 1);
	}

	private boolean isCulled(int y, int x, int k, int z, int i1, int j1) {
		if (x == k && z == i1) {
			if (!isTileCulled(y, x, z))
				return false;
			int k1 = x << 7;
			int i2 = z << 7;
			return isCulled(k1 + 1, heightMap[y][x][z] - j1, i2 + 1) && isCulled((k1 + 128) - 1, heightMap[y][x + 1][z] - j1, i2 + 1) && isCulled((k1 + 128) - 1, heightMap[y][x + 1][z + 1] - j1, (i2 + 128) - 1) && isCulled(k1 + 1, heightMap[y][x][z + 1] - j1, (i2 + 128) - 1);
		}
		for (int l1 = x; l1 <= k; l1++) {
			for (int j2 = z; j2 <= i1; j2++)
				if (cycleMap[y][l1][j2] == -cycle)
					return false;

		}

		int k2 = (x << 7) + 1;
		int l2 = (z << 7) + 2;
		int i3 = heightMap[y][x][z] - j1;
		if (!isCulled(k2, i3, l2))
			return false;
		int j3 = (k << 7) - 1;
		if (!isCulled(j3, i3, l2))
			return false;
		int k3 = (i1 << 7) - 1;
		return isCulled(k2, i3, k3) && isCulled(j3, i3, k3);
	}

	private boolean isCulled(int x, int y, int z) {
		for (int l = 0; l < processedClusterPtr; l++) {
			CullingCluster cluster = processedClusters[l];
			if (cluster.tileDistanceEnum == 1) {
				int i1 = cluster.worldStartX - x;
				if (i1 > 0) {
					int j2 = cluster.worldStartY + (cluster.worldDistanceFromCameraStartY * i1 >> 8);
					int k3 = cluster.worldEndY + (cluster.worldDistanceFromCameraEndY * i1 >> 8);
					int l4 = cluster.worldStartZ + (cluster.worldDistanceFromCameraStartZ * i1 >> 8);
					int i6 = cluster.worldEndZ + (cluster.worldDistanceFromCameraEndZ * i1 >> 8);
					if (z >= j2 && z <= k3 && y >= l4 && y <= i6)
						return true;
				}
			} else if (cluster.tileDistanceEnum == 2) {
				int j1 = x - cluster.worldStartX;
				if (j1 > 0) {
					int k2 = cluster.worldStartY + (cluster.worldDistanceFromCameraStartY * j1 >> 8);
					int l3 = cluster.worldEndY + (cluster.worldDistanceFromCameraEndY * j1 >> 8);
					int i5 = cluster.worldStartZ + (cluster.worldDistanceFromCameraStartZ * j1 >> 8);
					int j6 = cluster.worldEndZ + (cluster.worldDistanceFromCameraEndZ * j1 >> 8);
					if (z >= k2 && z <= l3 && y >= i5 && y <= j6)
						return true;
				}
			} else if (cluster.tileDistanceEnum == 3) {
				int k1 = cluster.worldStartY - z;
				if (k1 > 0) {
					int l2 = cluster.worldStartX + (cluster.worldDistanceFromCameraStartX * k1 >> 8);
					int i4 = cluster.worldEndX + (cluster.worldDistanceFromCameraEndX * k1 >> 8);
					int j5 = cluster.worldStartZ + (cluster.worldDistanceFromCameraStartZ * k1 >> 8);
					int k6 = cluster.worldEndZ + (cluster.worldDistanceFromCameraEndZ * k1 >> 8);
					if (x >= l2 && x <= i4 && y >= j5 && y <= k6)
						return true;
				}
			} else if (cluster.tileDistanceEnum == 4) {
				int l1 = z - cluster.worldStartY;
				if (l1 > 0) {
					int i3 = cluster.worldStartX + (cluster.worldDistanceFromCameraStartX * l1 >> 8);
					int j4 = cluster.worldEndX + (cluster.worldDistanceFromCameraEndX * l1 >> 8);
					int k5 = cluster.worldStartZ + (cluster.worldDistanceFromCameraStartZ * l1 >> 8);
					int l6 = cluster.worldEndZ + (cluster.worldDistanceFromCameraEndZ * l1 >> 8);
					if (x >= i3 && x <= j4 && y >= k5 && y <= l6)
						return true;
				}
			} else if (cluster.tileDistanceEnum == 5) {
				int i2 = y - cluster.worldStartZ;
				if (i2 > 0) {
					int j3 = cluster.worldStartX + (cluster.worldDistanceFromCameraStartX * i2 >> 8);
					int k4 = cluster.worldEndX + (cluster.worldDistanceFromCameraEndX * i2 >> 8);
					int l5 = cluster.worldStartY + (cluster.worldDistanceFromCameraStartY * i2 >> 8);
					int i7 = cluster.worldEndY + (cluster.worldDistanceFromCameraEndY * i2 >> 8);
					if (x >= j3 && x <= k4 && z >= l5 && z <= i7)
						return true;
				}
			}
		}

		return false;
	}

	public static boolean lowMem = true;
	private final int zMapSize;
	private final int xMapSize;
	private final int yMapSize;
	private final int[][][] heightMap;
	private final Tile[][][] tileArray;
	private int currentHL;
	private int amountOfInteractableObjects;
	private final InteractableObject[] interactableObjectCache;
	private final int[][][] cycleMap;
	private static int anInt446;
	private static int plane__;
	private static int cycle;
	private static int minVisibleX;
	private static int maxVisibleX;
	private static int minVisibleY;
	private static int maxVisibleY;
	private static int xCamPosTile;
	private static int yCamPosTile;
	private static int xCamPos;
	private static int zCamPos;
	private static int yCamPos;
	private static int yCurveSin;
	private static int yCUrveCos;
	private static int xCurveSin;
	private static int xCurveCos;
	private static InteractableObject[] aClass28Array462 = new InteractableObject[100];
	private static final int[] faceXoffset2 = { 53, -53, -53, 53 };
	private static final int[] faceYOffset2 = { -53, -53, 53, 53 };
	private static final int[] faceXOffset3 = { -45, 45, 45, -45 };
	private static final int[] faceYOffset3 = { 45, 45, -45, -45 };
	private static boolean isClicked;
	private static int clickX;
	private static int clickY;
	public static int clickedTileX = -1;
	public static int clickedTileY = -1;
	private static final int amountOfCullingClusters;
	private static int[] cullingClusterPointer;
	private static CullingCluster[][] cullingClusters;
	private static int processedClusterPtr;
	private static final CullingCluster[] processedClusters = new CullingCluster[500];
	private static Deque tileDeque = new Deque();
	private static final int[] anIntArray478 = { 19, 55, 38, 155, 255, 110, 137, 205, 76 };
	private static final int[] anIntArray479 = { 160, 192, 80, 96, 0, 144, 80, 48, 160 };
	private static final int[] anIntArray480 = { 76, 8, 137, 4, 0, 1, 38, 2, 19 };
	private static final int[] anIntArray481 = { 0, 0, 2, 0, 0, 2, 1, 1, 0 };
	private static final int[] anIntArray482 = { 2, 0, 0, 2, 0, 0, 0, 4, 4 };
	private static final int[] anIntArray483 = { 0, 4, 4, 8, 0, 0, 8, 0, 0 };
	private static final int[] anIntArray484 = { 1, 1, 0, 0, 0, 8, 0, 0, 8 };
	private static final int[] textureRGBColour = { 41, 39248, 41, 4643, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 43086, 41, 41, 41, 41, 41, 41, 41, 8602, 41, 28992, 41, 41, 41, 41, 41, 5056, 41, 41, 41, 7079, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 3131, 41, 41, 41 };
	private final int[] anIntArray486;
	private final int[] anIntArray487;
	private int anInt488;
	private final int[][] tileSHapePoints = { new int[16], { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, { 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1 }, { 1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1 }, { 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, { 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1 }, { 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0 }, { 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1 }, { 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1 } };
	private final int[][] tileShapeIndices = { { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 }, { 12, 8, 4, 0, 13, 9, 5, 1, 14, 10, 6, 2, 15, 11, 7, 3 }, { 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 }, { 3, 7, 11, 15, 2, 6, 10, 14, 1, 5, 9, 13, 0, 4, 8, 12 } };
	private static boolean[][][][] tile_visibility_maps = new boolean[8][32][51][51];
	private static boolean[][] tile_visibility_map;
	private static int midX;
	private static int midY;
	private static int left;
	private static int top;
	private static int right;
	private static int bottom;

	static {
		amountOfCullingClusters = 4;
		cullingClusterPointer = new int[amountOfCullingClusters];
		cullingClusters = new CullingCluster[amountOfCullingClusters][500];
	}
}