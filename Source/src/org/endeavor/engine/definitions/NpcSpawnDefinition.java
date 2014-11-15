package org.endeavor.engine.definitions;

import org.endeavor.game.entity.Location;

public class NpcSpawnDefinition {

	private short id;
	private Location location;
	private boolean walk;
	private byte face = -1;

	public int getId() {
		return id;
	}

	public Location getLocation() {
		return location;
	}

	public boolean isWalk() {
		return walk;
	}

	public int getFace() {
		return face;
	}
}
