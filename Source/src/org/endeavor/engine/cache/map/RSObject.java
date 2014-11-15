package org.endeavor.engine.cache.map;

public class RSObject {

	private short x;
	private short y;
	private byte z;
	protected int id;
	private byte type;
	private byte direction;
	
	public RSObject(int x, int y, int z) {
		this.x = (short) x;
		this.y = (short) y;
		this.z = (byte) z;
	}

	public RSObject(int x, int y, int z, int id, int type, int direction) {
		this.x = (short) x;
		this.y = (short) y;
		this.z = (byte) z;
		this.id = id;
		this.type = (byte) type;
		this.direction = (byte) direction;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof RSObject) {
			RSObject l = (RSObject) o;
			
			return l.getX() == getX() && l.getY() == getY() && l.getZ() == getZ();
		}
		
		return false;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public int getFace() {
		return direction;
	}
}
