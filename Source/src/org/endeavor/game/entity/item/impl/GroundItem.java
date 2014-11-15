package org.endeavor.game.entity.item.impl;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;

public class GroundItem {
	private Item item;
	private Location location;
	private short time;
	private boolean global = false;
	private String owner;
	private final long longOwnerName;
	private boolean exists = true;
	
	/**
	 * If the item is a global item
	 */
	public boolean isGlobal;
	
	/**
	 * The time it takes to respawn the item
	 */
	private int respawnTimer;

	@Override
	public boolean equals(Object o) {
		if ((o instanceof GroundItem)) {
			GroundItem other = (GroundItem) o;

			return (item.equals(other.getItem())) && (location.equals(other.getLocation()))
					&& (longOwnerName == other.getLongOwnerName()) && (global == other.isGlobal());
		}

		return false;
	}

	public GroundItem(Item item, Location location, int time, String owner) {
		this.item = item;
		this.location = location;
		this.time = ((short) time);
		this.owner = owner;
		if (owner != null)
			longOwnerName = Misc.nameToLong(owner);
		else
			longOwnerName = 0L;
	}

	public GroundItem(Item item, Location location, String owner) {
		this(item, location, 0, owner);
	}

	public GroundItem(Item item, Location location) {
		this(item, location, 0, null);
	}
	
	/**
	 * Creates a new global ground item
	 * @param item
	 * @param location
	 * @param respawnTimer
	 */
	public GroundItem(Item item, Location location, int respawnTimer) {
		this(item, location, 0, null);
		this.respawnTimer = respawnTimer;
		this.global = true;
		this.isGlobal = true;
	}

	@Override
	public String toString() {
		return "GroundItem [item=" + item + ", owner=" + owner + "] Global: " + global +"";
	}

	public boolean globalize() {
		return (time == 100) && (item.getDefinition().isTradable());
	}

	public boolean remove() {
		return (time >= 350) || (time < 0);
	}

	public void countdown() {
		if (!isGlobal) {
			time = ((short) (time + 1));
		}
	}

	public void setTime(int time) {
		this.time = ((short) time);
	}

	public void resetTime() {
		time = 0;
	}

	public void erase() {
		exists = false;
	}

	public boolean exists() {
		return exists;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwnerName() {
		return owner;
	}

	public Player getOwner() {
		return World.getPlayerByName(owner);
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	public boolean isGlobal() {
		return global;
	}

	public Location getLocation() {
		return location;
	}

	public Item getItem() {
		return item;
	}

	public long getLongOwnerName() {
		return longOwnerName;
	}
	
	public int getRespawnTimer() {
		return respawnTimer;
	}
}
