package org.endeavor.game.content.skill.runecrafting;

import java.util.HashMap;
import java.util.Map;

public enum RunecraftingData {
	AIR(2478, 556, 5.0D, new int[] { 1, 11, 22, 33, 44, 55, 66, 77, 88, 99 }), MIND(2479, 558, 5.5D, new int[] { 1, 14,
			28, 42, 56, 70, 84, 98 }), WATER(2480, 555, 6.0D, new int[] { 5, 19, 38, 57, 76, 95 }), EARTH(2481, 557,
			6.5D, new int[] { 9, 26, 52, 78 }), FIRE(2482, 554, 7.0D, new int[] { 14, 35, 70 }), BODY(2483, 559, 7.5D,
			new int[] { 20, 46, 92 }), COSMIC(2484, 564, 8.0D, new int[] { 27, 59 }), CHAOS(2487, 562, 8.5D, new int[] {
			35, 74 }), NATURE(2486, 561, 9.0D, new int[] { 44, 91 }), LAW(2485, 563, 9.5D, new int[] { 54 }), DEATH(
			2488, 560, 10.0D, new int[] { 65 }), BLOOD(30624, 565, 10.5D, new int[] { 77 });

	private int altarId;
	private int runeId;
	private double xp;
	private int[] multiplier;
	private static Map<Integer, RunecraftingData> altars = new HashMap<Integer, RunecraftingData>();

	private RunecraftingData(int altarId, int runeId, double xp, int[] multiplier) {
		this.altarId = altarId;
		this.runeId = runeId;
		this.xp = xp;
		this.multiplier = multiplier;
	}

	public static final void declare() {
		for (RunecraftingData data : values())
			altars.put(Integer.valueOf(data.getAltarId()), data);
	}

	public int getAltarId() {
		return altarId;
	}

	public int getRuneId() {
		return runeId;
	}

	public double getXp() {
		return xp;
	}

	public int getLevel() {
		return multiplier[0];
	}

	public int[] getMultiplier() {
		return multiplier;
	}

	public static RunecraftingData forId(int id) {
		return altars.get(Integer.valueOf(id));
	}
}
