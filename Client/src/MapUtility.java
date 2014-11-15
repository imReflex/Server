

final class MapUtility {

	public static int getRotatedMapChunkX(int rotation, int tileX, int tileY) {
		rotation &= 3;
		if (rotation == 0)
			return tileY;
		if (rotation == 1)
			return tileX;
		if (rotation == 2)
			return 7 - tileY;
		else
			return 7 - tileX;
	}

	public static int getRotatedMapChunkY(int tileY, int rotation, int tileX) {
		rotation &= 3;
		if (rotation == 0)
			return tileY;
		if (rotation == 1)
			return 7 - tileX;
		if (rotation == 2)
			return 7 - tileY;
		else
			return tileX;
	}

	public static int getRotatedLandscapeChunkX(int rotation, int objectSizeY, int x, int y, int objectSizeX) {
		rotation &= 3;
		if (rotation == 0)
			return x;
		if (rotation == 1)
			return y;
		if (rotation == 2) {
			int ret = 7 - x - (objectSizeX - 1);
			if(ret < 0)
				ret = 0;
			return ret;
		} else {
			int ret = 7 - y - (objectSizeY - 1);
			if(ret < 0)
				ret = 0;
			return ret;
		}
	}

	public static int getRotatedLandscapeChunkY(int y, int objectSizeY, int rotation, int objectSizeX, int x) {
		rotation &= 3;
		if (rotation == 0)
			return y;
		if (rotation == 1) {
			int ret = 7 - x - (objectSizeX - 1);
			if(ret < 0)
				ret = 0;
			return ret;
		}
		if (rotation == 2) {
			int ret = 7 - y - (objectSizeY- 1);
			if(ret < 0)
				ret = 0;
			return ret;
		} else
			return x;
	}

}
