package org.endeavor.game.content.skill.crafting;

import java.util.HashMap;
import java.util.Map;

import org.endeavor.game.entity.item.Item;

public enum Spinnable {
	BOWSTRING(new Item(1779), new Item(1777), 15.0D, 10), WOOL(new Item(1737), new Item(1759), 2.5D, 1), ROPE(new Item(
			10814), new Item(954), 25.0D, 30), MAGIC_STRING(new Item(6051), new Item(6038), 30.0D, 19), YEW_STRING(
			new Item(6049), new Item(9438), 15.0D, 10), SINEW_STRING(new Item(9436), new Item(9438), 15.0D, 10);

	private Item item;
	private Item outcome;
	private double experience;
	private int requiredLevel;
	public static Map<Integer, Spinnable> spins = new HashMap<Integer, Spinnable>();

	private Spinnable(Item item, Item outcome, double experience, int requiredLevel) {
		this.item = item;
		this.outcome = outcome;
		this.experience = experience;
		this.requiredLevel = requiredLevel;
	}

	public double getExperience() {
		return experience;
	}

	public Item getItem() {
		return item;
	}

	public Item getOutcome() {
		return outcome;
	}

	public int getRequiredLevel() {
		return requiredLevel;
	}

	public static final void declare() {
		for (Spinnable spinnable : values())
			spins.put(Integer.valueOf(spinnable.getItem().getId()), spinnable);
	}

	public static Spinnable forId(int id) {
		return spins.get(Integer.valueOf(id));
	}
}
