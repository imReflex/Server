package org.endeavor.game.content.skill.fletching;

import java.util.HashMap;
import java.util.Map;

public enum FletchLogData {
	NORMAL(1511, new int[] { 52, 50, 48, 9440 }, new int[] { 1, 5, 10, 9 }, new double[] { 5.0D, 10.0D, 20.0D, 24.0D }), OAK(
			1521, new int[] { 54, 56, 9442 }, new int[] { 20, 24, 25 }, new double[] { 16.5D, 30.0D, 25.0D }), WILLOW(
			1519, new int[] { 60, 58, 9444 }, new int[] { 35, 39, 40 }, new double[] { 30.0D, 35.0D, 35.0D }), MAPLE(
			1517, new int[] { 64, 62, 9448 }, new int[] { 50, 54, 55 }, new double[] { 35.0D, 40.0D, 40.0D }), YEW(
			1515, new int[] { 68, 66, 9452 }, new int[] { 65, 69, 70 }, new double[] { 67.5D, 70.0D, 75.0D }), MAGIC(
			1513, new int[] { 72, 70 }, new int[] { 80, 85 }, new double[] { 83.25D, 91.5D });

	private int logId;
	private int[] item;
	private int[] level;
	private double[] experience;
	private static Map<Integer, FletchLogData> logs = new HashMap<Integer, FletchLogData>();

	public static void declare() {
		for (FletchLogData log : values()) {
			logs.put(Integer.valueOf(log.logId), log);
		}
	}

	public static FletchLogData forId(int item) {
		return logs.get(Integer.valueOf(item));
	}

	private FletchLogData(int logId, int[] item, int[] level, double[] experience) {
		this.logId = logId;
		this.item = item;
		this.level = level;
		this.experience = experience;
	}

	public double[] getExperience() {
		return experience;
	}

	public int[] getRewards() {
		return item;
	}

	public int[] getRequiredLevel() {
		return level;
	}

	public int getLogId() {
		return logId;
	}
}
