package org.endeavor.game.content.skill.thieving;

import java.util.HashMap;

public enum ThievingStallData {
	KELDAGRIM_BAKER_STALL(6163, 1, -1, 15.0D, new int[][] { ThievingStallTask.THEIVING_ITEMS[0],
			ThievingStallTask.THEIVING_ITEMS[1], ThievingStallTask.THEIVING_ITEMS[2],
			ThievingStallTask.THEIVING_ITEMS[3], ThievingStallTask.THEIVING_ITEMS[4],
			ThievingStallTask.THEIVING_ITEMS[5] }),

	KELDAGRIM_CROSSBOW_STALL(17031, 10, -1, 30.0D, new int[][] { ThievingStallTask.THEIVING_ITEMS[1],
			ThievingStallTask.THEIVING_ITEMS[2], ThievingStallTask.THEIVING_ITEMS[3],
			ThievingStallTask.THEIVING_ITEMS[4], ThievingStallTask.THEIVING_ITEMS[5] }),

	KELDAGRIM_CRAFTING_STALL(6166, 25, -1, 40.0D, new int[][] { ThievingStallTask.THEIVING_ITEMS[2],
			ThievingStallTask.THEIVING_ITEMS[3], ThievingStallTask.THEIVING_ITEMS[4],
			ThievingStallTask.THEIVING_ITEMS[5] }),

	KELDAGRIM_CLOTHES_STALL(6165, 50, -1, 50.0D, new int[][] { ThievingStallTask.THEIVING_ITEMS[3],
			ThievingStallTask.THEIVING_ITEMS[4], ThievingStallTask.THEIVING_ITEMS[5] }),

	KELDAGRIM_SILVER_STALL(6164, 65, -1, 115.0D, new int[][] { ThievingStallTask.THEIVING_ITEMS[4],
			ThievingStallTask.THEIVING_ITEMS[5] }),

	KELDAGRIM_GEM_STALL(6162, 90, -1, 150.0D, new int[][] { ThievingStallTask.THEIVING_ITEMS[4],
			ThievingStallTask.THEIVING_ITEMS[5] }),

	// newest
	SEED_STALL_LUMB(7053, 35, -1, 50.0D, new int[][] { {5319, 1}, {5324, 1}, {5322, 1}, {5320, 1}, 
			{5096, 1}, {5097, 1}, {5098, 1}, {5318, 1}, {5319, 1}, {6036, 1} }),
	
	RELLEKKA_FISH(4277, 85, -1, 144.0D, new int[][] { {383, 1}, {7944, 1}, {377, 1}, {7944, 1}, {15270, 1}, {377, 1}, {327, 1}, {327, 1}, {327, 1}   }),
	
	RELLEKKA_FUR(4278, 20, -1, 50.0D, new int[][] { {6814, 1}}),
			
	;
	
	

	int objectId;
	int levelRequired;
	int replacementId;
	double experience;
	int[][] rewards;
	private static HashMap<Integer, ThievingStallData> stalls = new HashMap<Integer, ThievingStallData>();

	private ThievingStallData(int id, int level, int replace, double experience, int[][] rewards) {
		objectId = id;
		levelRequired = level;
		replacementId = replace;
		this.experience = experience;
		this.rewards = rewards;
	}

	public static final void declare() {
		for (ThievingStallData data : values())
			stalls.put(Integer.valueOf(data.getObjectId()), data);
	}

	public int getObjectId() {
		return objectId;
	}

	public int getLevelRequired() {
		return levelRequired;
	}

	public int getReplacementId() {
		return replacementId;
	}

	public double getExperience() {
		return experience;
	}

	public int[][] getRewards() {
		return rewards;
	}

	public static ThievingStallData getObjectById(int id) {
		return stalls.get(Integer.valueOf(id));
	}
}
