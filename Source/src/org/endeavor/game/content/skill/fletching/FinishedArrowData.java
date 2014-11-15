package org.endeavor.game.content.skill.fletching;

import java.util.HashMap;
import java.util.Map;

public enum FinishedArrowData {
	BRONZE(39, 882, 1, 2.6D), IRON(40, 884, 15, 3.8D), STEEL(41, 886, 30, 6.3D), MITHRIL(42, 888, 45,
			8.800000000000001D), ADAMANT(43, 890, 60, 11.300000000000001D), RUNE(44, 892, 75, 13.800000000000001D);

	private int id;
	private int reward;
	private int levelRequired;
	private double experience;
	private static Map<Integer, FinishedArrowData> arrowtips = new HashMap<Integer, FinishedArrowData>();

	public static final void declare() {
		for (FinishedArrowData arrowtip : values()) {
			arrowtips.put(Integer.valueOf(arrowtip.id), arrowtip);
		}
	}

	public static FinishedArrowData forId(int item) {
		return arrowtips.get(Integer.valueOf(item));
	}

	private FinishedArrowData(int id, int reward, int levelRequired, double experience) {
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
