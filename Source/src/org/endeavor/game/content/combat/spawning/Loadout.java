package org.endeavor.game.content.combat.spawning;

import java.io.Serializable;

import org.endeavor.game.entity.item.Item;

public class Loadout implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4259265712208838805L;
	private final Item[] items;
	private final String name;
	
	public Loadout(Item[] items, String name) {
		this.items = items;
		this.name = name;
	}

	public Item[] getItems() {
		return items;
	}

	public String getName() {
		return name;
	}
	
}
