package org.endeavor.game.content.skill.crafting;

import java.util.HashMap;
import java.util.Map;

import org.endeavor.game.entity.item.Item;

public enum Jewelry {
	AMULET_GOLD(1673, 8, 30.0D, new int[] { 2357 }), AMULET_SAPPHIRE(1675, 20, 35.0D, new int[] { 2357, 1607 }), AMULET_EMERALD(
			1677, 30, 40.0D, new int[] { 2357, 1605 }), AMULET_RUBY(1679, 50, 80.0D, new int[] { 2357, 1603 }), AMULET_DIAMOND(
			1681, 70, 95.0D, new int[] { 2357, 1601 }), AMULET_DRAGONSTONE(1683, 80, 145.0D, new int[] { 2357, 1615 }), AMULET_ONYX(
			6579, 90, 160.0D, new int[] { 2357, 6573 }), GOLD_AMULET(1692, 1, 4.0D, new int[] { 1673, 1759 }), GOLD_RING(
			1635, 5, 15.0D, new int[] { 2357 }), GOLD_NECKLACE(1654, 6, 20.0D, new int[] { 2357 }), GOLD_BRACELET(
			11069, 7, 25.0D, new int[] { 2357 }), SAPPHIRE_AMULET(1694, 24, 65.0D, new int[] { 1675, 1759 }), SAPPHIRE_RING(
			2550, 20, 40.0D, new int[] { 2357, 1607 }), SAPPHIRE_NECKLACE(1656, 22, 55.0D, new int[] { 2357, 1607 }), SAPPHIRE_BRACELET(
			11072, 23, 60.0D, new int[] { 2357, 1607 }), EMERALD_AMULET(1696, 31, 70.0D, new int[] { 1677, 1759 }), EMERALD_RING(
			1639, 27, 55.0D, new int[] { 2357, 1605 }), EMERALD_NECKLACE(1658, 29, 60.0D, new int[] { 1605, 2357 }), EMERALD_BRACELET(
			11076, 29, 65.0D, new int[] { 1605, 2357 }), RUBY_AMULET(1698, 50, 85.0D, new int[] { 1759, 1679 }), RUBY_RING(
			1641, 34, 70.0D, new int[] { 2357, 1603 }), RUBY_NECKLACE(1660, 40, 75.0D, new int[] { 2357, 1603 }), RUBY_BRACELET(
			11085, 42, 80.0D, new int[] { 2357, 1603 }), DIAMOND_AMULET(1731, 70, 100.0D, new int[] { 1681, 1759 }), DIAMOND_RING(
			2570, 43, 85.0D, new int[] { 2357, 1601 }), DIAMOND_NECKLACE(1662, 56, 90.0D, new int[] { 2357, 1601 }), DIAMOND_BRACELET(
			11092, 58, 95.0D, new int[] { 2357, 1601 }), DRAGONSTONE_NECKLACE(1664, 72, 105.0D,
			new int[] { 2357, 1615 }), DRAGONSTONE_AMULET(1702, 80, 150.0D, new int[] { 1683, 1759 }), DRAGONSTONE_RING(
			1645, 55, 95.0D, new int[] { 2357, 1615 }), DRAGONSTONE_BRACELET(11115, 74, 110.0D,
			new int[] { 2357, 1615 }), ONYX_NECKLACE(11128, 82, 120.0D, new int[] { 6579, 1759 }), ONYX_AMULET(6585,
			90, 165.0D, new int[] { 6579, 1759 }), ONYX_RING(6575, 67, 115.0D, new int[] { 6573, 2357 }), ONYX_BRACELET(
			11130, 84, 115.0D, new int[] { 6573, 2357 });

	private Item reward;
	private short levelRequired;
	private double experienceGain;
	private int[] materialsRequired;
	private static Map<Integer, Jewelry> jewelry = new HashMap<Integer, Jewelry>();

	private Jewelry(int rewardId, int levelRequired, double experienceGain, int[] materialsRequired) {
		reward = new Item(rewardId);
		this.levelRequired = ((short) levelRequired);
		this.experienceGain = experienceGain;
		this.materialsRequired = materialsRequired;
	}

	public Item getReward() {
		return reward;
	}

	public short getRequiredLevel() {
		return levelRequired;
	}

	public double getExperience() {
		return experienceGain;
	}

	public int[] getMaterialsRequired() {
		return materialsRequired;
	}

	public static final void declare() {
		for (Jewelry jewel : values())
			jewelry.put(Integer.valueOf(jewel.getReward().getId()), jewel);
	}

	public static Jewelry forReward(int id) {
		return jewelry.get(Integer.valueOf(id));
	}
}
