package org.endeavor.game.content.skill.woodcutting;

import java.util.HashMap;
import java.util.Map;

public enum WoodcuttingTreeData {
	NORMAL_FREE(new int[] { 1276, 1278 }, 1, 1511, 1342, 22, 25.0D), DEAD_TREE(new int[] { 1286 }, 1, 1511, 1342, 22,
			25.0D), OAK_TREE(new int[] { 1281 }, 15, 1521, 1356, 30, 37.5D), WILLOW_TREE(new int[] { 1308, 5551, 5552,
			5553 }, 30, 1519, 7399, 38, 67.5D), MAPLE_TREE(new int[] { 1307 }, 45, 1517, 1343, 45, 100.0D), YEW_TREE(
			new int[] { 1309 }, 60, 1515, 7402, 50, 175.0D), MAGIC_TREE(new int[] { 1306 }, 75, 1513, 7401, 55, 250.0D);

	int levelRequired;
	int reward;
	int replacementId;
	int respawnTimer;
	double experience;
	int[] objectId;
	private static Map<Integer, WoodcuttingTreeData> trees = new HashMap<Integer, WoodcuttingTreeData>();

	private WoodcuttingTreeData(int[] id, int level, int reward, int replacement, int respawnTimer, double experience) {
		objectId = id;
		levelRequired = level;
		this.reward = reward;
		replacementId = replacement;
		this.respawnTimer = respawnTimer;
		this.experience = experience;
	}

	public static final void declare() {
		for (WoodcuttingTreeData i : values()) {
			int[] arrayOfInt;
			int m = (arrayOfInt = i.getIds()).length;
			for (int k = 0; k < m; k++) {
				trees.put(arrayOfInt[k], i);
			}
		}
	}

	public int getLevelRequired() {
		return levelRequired;
	}

	public int getReward() {
		return reward;
	}

	public int getReplacement() {
		return replacementId;
	}

	public double getExperience() {
		return experience;
	}

	public int[] getIds() {
		return objectId;
	}

	public int getRespawnTimer() {
		return respawnTimer;
	}

	public static WoodcuttingTreeData forId(int id) {
		return trees.get(Integer.valueOf(id));
	}

	public static boolean isTree(int objectId) {
		for (WoodcuttingTreeData data : values()) {
			for (int i : data.getIds()) {
				if (i == objectId) {
					return true;
				}
			}
		}
		return false;
	}
}
