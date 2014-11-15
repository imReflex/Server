package org.endeavor.game.content.skill.smithing;

import java.util.HashMap;

import org.endeavor.game.entity.item.Item;

public enum SmeltingData {
	BRONZE_BAR(new Item[] { new Item(436), new Item(438) }, new Item(2349), 1, 6.2D), BLURITE_BAR(
			new Item[] { new Item(668) }, new Item(9467), 8, 8.0D), IRON_BAR(new Item[] { new Item(440) }, new Item(
			2351), 15, 12.5D), SILVER_BAR(new Item[] { new Item(442) }, new Item(2355), 20, 13.699999999999999D), STEEL_BAR(
			new Item[] { new Item(440), new Item(453, 2) }, new Item(2353), 30, 17.5D), GOLD_BAR(new Item[] { new Item(
			444) }, new Item(2357), 40, 22.5D), PERFECT_GOLD_BAR(new Item[] { new Item(446) }, new Item(2365), 40,
			22.5D), MITHRIL_BAR(new Item[] { new Item(447), new Item(453, 4) }, new Item(2359), 50, 30.0D), ADAMANITE_BAR(
			new Item[] { new Item(449), new Item(453, 6) }, new Item(2361), 70, 37.5D), RUNITE_BAR(new Item[] {
			new Item(451), new Item(453, 8) }, new Item(2363), 85, 50.0D);

	final Item[] requiredOres;
	final Item result;
	final int levelRequired;
	final double exp;
	private static HashMap<Integer, SmeltingData> smelting = new HashMap<Integer, SmeltingData>();

	private SmeltingData(Item[] requiredOres, Item result, int levelRequired, double exp) {
		this.requiredOres = requiredOres;
		this.result = result;
		this.levelRequired = levelRequired;
		this.exp = exp;
	}

	public static final void declare() {
		for (SmeltingData ores : values())
			smelting.put(Integer.valueOf(ores.getResult().getId()), ores);
	}

	public Item[] getRequiredOres() {
		return requiredOres;
	}

	public Item getResult() {
		return result;
	}

	public int getLevelRequired() {
		return levelRequired;
	}

	public double getExp() {
		return exp;
	}
}
