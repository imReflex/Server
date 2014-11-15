package org.endeavor.game.content.skill.fletching;

import java.util.HashMap;
import java.util.Map;

public enum FinishedBoltData {
	BRONZE(9375, 877, 9, 0.5D), IRON(9377, 9140, 39, 1.5D), STEEL(9378, 9141, 46, 3.5D), MITHRIL(9379, 9142, 54, 5.0D), ADAMANT(
			9380, 9143, 61, 7.0D), RUNE(9381, 9144, 69, 10.0D);

	private int id;
	private int reward;
	private int levelRequired;
	private double experience;
	private static final Map<Integer, FinishedBoltData> bolts = new HashMap<Integer, FinishedBoltData>();

	public static void declare() {
		for (FinishedBoltData bolt : values()) {
			bolts.put(Integer.valueOf(bolt.id), bolt);
		}
	}

	public static FinishedBoltData forId(int item) {
		return bolts.get(Integer.valueOf(item));
	}

	private FinishedBoltData(int id, int reward, int levelRequired, double experience) {
		this.id = id;
		this.reward = reward;
		this.levelRequired = levelRequired;
		this.experience = experience;
	}

	public double getExperience() {
		return experience;
	}

	public int getId() {
		return id;
	}

	public int getLevelRequired() {
		return levelRequired;
	}

	public int getReward() {
		return reward;
	}
}
