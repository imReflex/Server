package org.endeavor.engine.cache.map;

public class QueuedDoor {

	private final int id;

	private final int x;

	private final int y;

	private final int z;

	private final int type;

	private final int face;

	public QueuedDoor(int id, int x, int y, int z, int type, int face) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
		this.face = face;
	}

	@Override
	public boolean equals(Object o) {
		return ((QueuedDoor) o).getX() == x && ((QueuedDoor) o).getY() == y && ((QueuedDoor) o).getZ() == z;
	}

	public int getId() {
		return id;
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

	public int getType() {
		return type;
	}

	public int getFace() {
		return face;
	}
}
