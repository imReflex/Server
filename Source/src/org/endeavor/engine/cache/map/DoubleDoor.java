package org.endeavor.engine.cache.map;

import org.endeavor.engine.utility.GameDefinitionLoader;

public class DoubleDoor {

	private int currentId1;
	private int currentId2;

	private int currentFace1;
	private int currentFace2;

	private final byte type;

	private short x;
	private short y;
	private short x2;
	private short y2;
	private byte z;

	private int id1;
	private int id2;
	private int id3;
	private int id4;

	private byte xMod;
	private byte yMod;

	private byte face1;
	private byte face2;
	private byte face3;
	private byte face4;

	public DoubleDoor(QueuedDoor door, RSObject other) {
		this.type = (byte) door.getType();

		currentId1 = door.getId();
		currentId2 = other.getId();

		id1 = currentId1;
		id2 = currentId2;
		id3 = GameDefinitionLoader.getAlternate(id1);
		id4 = GameDefinitionLoader.getAlternate(id2);

		if (id3 <= 0)
			id3 = id1;

		if (id4 <= 0)
			id4 = id2;

		x = (short) door.getX();
		y = (short) door.getY();
		z = (byte) door.getZ();
		x2 = (short) other.getX();
		y2 = (short) other.getY();

		// System.out.println("" + x + " " + y + " " + z);

		currentFace1 = door.getFace();
		currentFace2 = other.getFace();
		face1 = (byte) currentFace1;
		face2 = (byte) currentFace2;

		// if (flag) {//closed
		switch (face1) {
		case 0:
			xMod = -1;
			face3 = 3;
			face4 = 1;
			break;
		case 1:
			yMod = 1;
			face3 = 0;
			face4 = 2;
			break;
		case 2:
			xMod = 1;
			face3 = 1;
			face4 = 3;
			break;
		case 3:
			// 5187
			// 15642
			yMod = -1;
			face3 = 2;
			face4 = 0;
			break;
		}
		
		if (MapConstants.isReverseOrientation(currentId1) || MapConstants.isReverseOrientation(currentId2)) {
			face3 -= 2;
			face4 -= 2;
		}
		
		/*
		 * } else { switch (face1) { case 1: case 3: xMod = 1; face3 = 1; face4
		 * = 1; break; case 2: yMod = -1; face3 = 1; face4 = 1; break; } }
		 */
	}

	public void append() {
		if (currentId1 == id1) {
			currentId1 = id3;
			currentId2 = id4;
		} else if (currentId2 == id3) {
			currentId1 = id1;
			currentId2 = id2;
		}

		if (currentFace1 == face1) {
			currentFace1 = face3;
			currentFace2 = face4;
			x += xMod;
			y += yMod;
			x2 += xMod;
			y2 += yMod;
		} else if (currentFace1 == face3) {
			currentFace1 = face1;
			currentFace2 = face2;
			x -= xMod;
			y -= yMod;
			x2 -= xMod;
			y2 -= yMod;
		}
	}

	public int getCurrentId1() {
		return currentId1;
	}

	public int getCurrentId2() {
		return currentId2;
	}

	public int getCurrentFace1() {
		return currentFace1;
	}

	public int getCurrentFace2() {
		return currentFace2;
	}

	public int getX1() {
		return x;
	}

	public int getY1() {
		return y;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	public int getZ() {
		return z;
	}

	public int getType() {
		return type;
	}
}
