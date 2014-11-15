package org.endeavor.engine.definitions;

public class NpcDefinition {

	private String name;
	private short id;
	private short level;
	private byte size;
	private boolean attackable;

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public int getLevel() {
		return level;
	}

	public int getSize() {
		return size;
	}

	public boolean isAttackable() {
		return attackable;
	}
}
