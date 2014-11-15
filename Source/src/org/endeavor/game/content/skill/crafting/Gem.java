 package org.endeavor.game.content.skill.crafting;

import java.util.HashMap;
import java.util.Map;

import org.endeavor.engine.utility.GameDefinitionLoader;

public enum Gem {
	OPAL(1625, 1609, 891, 1, 15, true), JADE(1627, 1611, 891, 13, 20, true), REDTOPAZ(1629, 1613, 892, 16, 25, true), SAPPHIRE(
			1623, 1607, 888, 1, 50, false), EMERALD(1621, 1605, 889, 27, 68, false), RUBY(1619, 1603, 887, 34, 85,
			false), DIAMOND(1617, 1601, 890, 43, 108, false), DRAGONSTONE(1631, 1615, 890, 55, 138, false), ONYX(6571,
			6573, 2717, 67, 168, false);

	private int uncutGem;
	private int cutGem;
	private int animation;
	private int requiredLevel;
	private int experience;
	private boolean isSemiPrecious;
	private static Map<Integer, Gem> gems = new HashMap<Integer, Gem>();

	private Gem(int uncutID, int cutID, int animation, int levelReq, int XP, boolean semiPrecious) {
		uncutGem = uncutID;
		cutGem = cutID;
		this.animation = animation;
		requiredLevel = levelReq;
		experience = XP;
		isSemiPrecious = semiPrecious;
	}

	public static final void declare() {
		for (Gem gem : values()) {
			gems.put(Integer.valueOf(gem.uncutGem), gem);

			GameDefinitionLoader.setValue(gem.uncutGem, gem.ordinal() * 100, gem.ordinal() * 50);
			GameDefinitionLoader.setValue(gem.cutGem, gem.ordinal() * 300, gem.ordinal() * 225);
		}
	}

	public short getAnimation() {
		return (short) animation;
	}

	public int getCutGem() {
		return cutGem;
	}

	public int getRequiredLevel() {
		return requiredLevel;
	}

	public int getUncutGem() {
		return uncutGem;
	}

	public int getExperience() {
		return experience;
	}

	public boolean isSemiPrecious() {
		return isSemiPrecious;
	}

	public static Gem forId(int gemId) {
		return gems.get(Integer.valueOf(gemId));
	}
}
