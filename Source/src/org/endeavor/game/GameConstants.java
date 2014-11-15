package org.endeavor.game;

import org.endeavor.engine.cache.map.Region;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.VirtualMobRegion;

/**
 * Constant data for the game server
 * 
 * @author Michael Sasse
 * 
 * 
 */
public class GameConstants {
	
	/**
	 * Is the double experience weekend enabled
	 */
	public static final boolean IS_DOUBLE_EXP_WEEKEND = false;
	
	/**
	 * Should sounds be disabled
	 */
	public static final boolean DISABLE_SOUNDS = false;
	
	/**
	 * Sizes of npcs, usd for clipping
	 */
	public static final int[][][] SIZES = { { { 0, 0 } },
			{ { 0, 0 } }, // 1
			{ { 0, 1 }, { 1, 0 }, { 1, 1 } }, // 2
			{ { 2, 0 }, { 2, 1 }, { 2, 2 }, { 1, 2 }, { 0, 2 } }, // 3
			{ { 3, 0 }, { 3, 1 }, { 3, 2 }, { 3, 3 }, { 2, 3 }, { 1, 3 }, { 0, 3 } }, // 4
			{ { 4, 0 }, { 4, 1 }, { 4, 2 }, { 4, 3 }, { 4, 4 }, { 3, 4 }, { 2, 4 }, { 1, 4 }, { 0, 4 } }, // 5
			{ { 5, 0 }, { 5, 1 }, { 5, 2 }, { 5, 3 }, { 5, 4 }, { 5, 5 }, { 4, 5 }, { 3, 5 }, { 2, 5 }, { 1, 5 },
					{ 0, 5 } }, // 6
	};

	/**
	 * Different walking directions of npcs
	 */
	public static final int[][] DIR = { { -1, 1 }, { 0, 1 }, { 1, 1 }, { -1, 0 }, { 1, 0 }, { -1, -1 }, { 0, -1 },
			{ 1, -1 } };

	/**
	 * Gets if the coordinates are within a block
	 * @param blockX
	 * @param blockY
	 * @param size
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean withinBlock(int blockX, int blockY, int size, int x, int y) {
		return (x - blockX < size) && (x - blockX > -1) && (y - blockY < size) && (y - blockY > -1);
	}

	public static boolean withinBlock(int blockX, int blockY, int blockSize, int x, int y, int checkSize) {
		for (int i = 1; i < checkSize + 1; i++) {
			for (int k = 0; k < SIZES[i].length; k++) {
				int x2 = x + SIZES[i][k][0];
				int y2 = y + SIZES[i][k][1];

				if ((x2 - blockX < blockSize) && (x2 - blockX > -1) && (y2 - blockY < blockSize) && (y2 - blockY > -1)) {
					return true;
				}
			}
		}

		return false;
	}
	
	public static Location getClearAdjacentLocation(Location l, int size) {
		return getClearAdjacentLocation(l, size, null);
	}

	public static Location getClearAdjacentLocation(Location l, int size, VirtualMobRegion virtual) {
		int x = l.getX();
		int y = l.getY();
		int z = l.getZ();

		int lowDist = 99999;
		int lowX = 0;
		int lowY = 0;

		main: for (int i = 0; i < DIR.length; i++) {
			int x2 = x + DIR[i][0] * size;
			int y2 = y + DIR[i][1] * size;

			for (int k = 0; k < size - 1; k++) {
				int y3 = y2 + k;

				for (int j = 0; j < size - 1; j++) {
					int x3 = x2 + j;

					Region r = Region.getRegion(x3, y3);

					if (r == null) {
						continue main;
					}
					
					if (virtual == null) {
					
						if ((!r.canMove(x3, y3, z, 1)) || (!r.canMove(x3, y3, z, 4)) || (!r.canMove(x3, y3, z, 2))
								|| (r.isNpcOnTile(x3 + DIR[1][0], y3 + DIR[1][1], z))
								|| (r.isNpcOnTile(x3 + DIR[4][0], y3 + DIR[4][1], z))
								|| (r.isNpcOnTile(x3 + DIR[2][0], y3 + DIR[2][1], z))) {
							continue main;
						}
						
					} else {
						
						if ((!r.canMove(x3, y3, z, 1)) || (!r.canMove(x3, y3, z, 4)) || (!r.canMove(x3, y3, z, 2))
								|| (virtual.isMobOnTile(x3 + DIR[1][0], y3 + DIR[1][1], z))
								|| (virtual.isMobOnTile(x3 + DIR[4][0], y3 + DIR[4][1], z))
								|| (virtual.isMobOnTile(x3 + DIR[2][0], y3 + DIR[2][1], z))) {
							continue main;
						}
						
					}
				}
			}
			
			int dist = Misc.getManhattanDistance(x2, x2, x, y);

			if (dist < lowDist) {
				lowX = x2;
				lowY = y2;
				lowDist = dist;
			}
		}

		return lowX != 0 ? new Location(lowX, lowY, z) : null;
	}

	public static Location[] getBorder(int x, int y, int size) {
		if (size == 1) {
			return new Location[] { new Location(x, y) };
		}

		Location[] border = new Location[4 * (size - 1)];
		int j = 0;

		border[0] = new Location(x, y);

		for (int i = 0; i < 4; i++) {
			for (int k = 0; k < (i < 3 ? size - 1 : size - 2); k++) {
				if (i == 0)
					x++;
				else if (i == 1)
					y++;
				else if (i == 2)
					x--;
				else if (i == 3) {
					y--;
				}
				border[(++j)] = new Location(x, y);
			}
		}

		return border;
	}
	
	public static Location[] getBorder(int x, int y, int xsize, int ysize) {
		if (xsize <= 1 && ysize <= 1) {
			return new Location[] { new Location(x, y) };
		}

		Location[] border = new Location[(xsize * 2) + (ysize * 2)];
		int j = 0;

		border[0] = new Location(x, y);

		for (int i = 0; i < 4; i++) {
			for (int k = 0; k < (i < 3 ? (i == 0 || i == 2 ? xsize : ysize)  - 1 : (i == 0 || i == 2 ? xsize : ysize) - 2); k++) {
				if (i == 0)
					x++;
				else if (i == 1)
					y++;
				else if (i == 2)
					x--;
				else if (i == 3) {
					y--;
				}
				border[(++j)] = new Location(x, y);
			}
		}

		return border;
	}

	public static Location[] getEdges(int x, int y, int size) {
		if (size == 1) {
			return new Location[] { new Location(x, y) };
		}

		return new Location[] { new Location(x, y), new Location(x + size, y), new Location(x + size, y + size),
				new Location(x, y + size) };
	}

	public static final int getDirection(int x, int y) {
		for (int i = 0; i < 8; i++) {
			if ((DIR[i][0] == x) && (DIR[i][1] == y)) {
				return i;
			}
		}
		return -1;
	}

	public static final int getDirection(int x, int y, int x2, int y2) {
		int xDiff = x2 - x;
		int yDiff = y2 - y;
		for (int i = 0; i < DIR.length; i++) {
			if ((xDiff == DIR[i][0]) && (yDiff == DIR[i][1])) {
				return i;
			}
		}
		return -1;
	}
}
