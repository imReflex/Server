package org.endeavor.engine.cache.map;

import org.endeavor.engine.utility.GameDefinitionLoader;

public class Door {

	private int id;

	private int id2;

	private int currentId;

	private short x;

	private short y;

	private byte xMod = 0;

	private byte yMod = 0;

	private byte z;

	private byte currentFace;

	private byte type;

	private byte face;

	private byte face2;

	private byte append = 0;

	public Door(int id, int x, int y, int z, int type, int face) {
		this.currentId = id;
		this.id = currentId;
		this.x = (short) x;
		this.y = (short) y;
		this.z = (byte) z;
		this.currentFace = (byte) face;
		this.type = (byte) type;
		this.face = (byte) face;

		boolean flag = !MapConstants.isOpen(id);
		this.id2 = GameDefinitionLoader.getAlternate(id);

		if (id2 <= 0) {
			id2 = id;
		}

		if (type == 9) {// diagonal doors
			if (!flag) {// open
				switch (face) {
				case 0:
					face2 = 1;
					xMod = 1;
					break;
				case 1:
					face2 = 0;
					yMod = -1;
					break;
				case 2:
					face2 = 1;
					xMod = -1;
					break;
				}
			} else {// closed
				switch (face) {
				case 0:
					face2 = 3;
					xMod = 1;
					break;
				case 1:
					face2 = 0;
					yMod = -1;
					break;
				case 2:
					face2 = 3;
					yMod = -1;
					break;
				case 3:
					face2 = 0;
					xMod = -1;
					break;
				}
			}

			return;
		}

		if (!flag) {// open
			switch (face) {
			case 0:
				face2 = 3;
				yMod = 1;
				break;
			case 1:
				face2 = 0;
				xMod = 1;
				break;
			case 2:
				face2 = 1;
				yMod = -1;
				break;
			case 3:
				face2 = 2;
				xMod = -1;
				break;
			}
		} else {// closed
			switch (face) {
			case 0:
				face2 = 1;
				xMod = -1;
				break;
			case 1:
				face2 = 2;
				yMod = 1;
				break;
			case 2:
				face2 = -1;
				xMod = 1;
				break;
			case 3:
				face2 = 0;
				yMod = -1;
				break;
			}
		}
	}

	public boolean equals(Door door) {
		return door.id == id && door.x == x && door.y == y && door.z == z;
	}

	public void append() {
		if (currentId == id) {
			currentId = id2;
		} else if (currentId == id2) {
			currentId = id;
		}

		if (currentFace == face) {
			currentFace = face2;
			x += xMod;
			y += yMod;
			append = (byte) 1;
		} else if (currentFace == face2) {
			currentFace = face;
			x -= xMod;
			y -= yMod;
			append = (byte) 0;
		}

		System.out.println("to: " + x + " " + y);
	}

	public int getFace() {
		return face;
	}

	public int getFace2() {
		return face2;
	}

	public int getCurrentFace() {
		return currentFace;
	}

	public void setCurrentFace(int currentFace) {
		this.currentFace = (byte) currentFace;
	}

	public int getType() {
		return type;
	}

	public int getId() {
		return id;
	}

	public int getId2() {
		return id2;
	}

	public int getCurrentId() {
		return currentId;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public boolean original() {
		return append == (byte) 0;
	}
}
