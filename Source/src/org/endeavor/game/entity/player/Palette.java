package org.endeavor.game.entity.player;

import java.io.Serializable;

public final class Palette implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4627124516031885647L;
	public static final byte DIRECTION_NORMAL = 0;
	private PaletteTile[][][] tiles = new PaletteTile[13][13][4];

	public PaletteTile getTile(int x, int y, int z) {
		return tiles[x][y][z];
	}

	public void setTile(int x, int y, int z, PaletteTile tile) {
		tiles[x][y][z] = tile;
	}

	public static class PaletteTile {
		private int x;
		private int y;
		private int z;
		private int rot;

		public PaletteTile(int x, int y) {
			this(x, y, 0);
		}

		public PaletteTile(int x, int y, int z) {
			this(x, y, z, 0);
		}

		public PaletteTile(int x, int y, int z, int rot) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.rot = rot;
		}

		public int getX() {
			return x / 8;
		}

		public int getY() {
			return y / 8;
		}

		public int getZ() {
			return z % 4;
		}

		public int getRotation() {
			return rot % 4;
		}
	}

	private static enum RotationDirections {
		CLOCK_WISE_0,

		CLOCK_WISE_90,

		CLOCK_WISE_180,

		CLOCK_WISE_270;

		public int toInteger() {
			return ordinal();
		}
	}
}
