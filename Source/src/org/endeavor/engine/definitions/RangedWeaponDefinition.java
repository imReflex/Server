package org.endeavor.engine.definitions;

import org.endeavor.game.entity.item.Item;

public class RangedWeaponDefinition {

	public enum RangedTypes {
		THROWN, SHOT
	}

	private short id;
	private RangedTypes type;
	private Item[] arrows;

	public int getId() {
		return id;
	}

	public RangedTypes getType() {
		return type;
	}

	public Item[] getArrows() {
		return arrows;
	}
}
