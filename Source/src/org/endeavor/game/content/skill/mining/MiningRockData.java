package org.endeavor.game.content.skill.mining;

import java.util.HashMap;

public enum MiningRockData {
	
	COPPER_ORE(
		new int[] { 2090, 2091, 31082, 31080, 31081, 11938, 11937, 11936 }, 
		1, 436, 452, 15, 17.5D), 

	TIN_ORE(
		new int[] { 2094, 2095, 31077, 31078, 31079, 37304, 37306 },
		1, 438, 452, 15, 17.5D), 
	
	IRON_ORE(
			new int[] { 2092, 2093, 31072, 31073, 31071, 37309, 37307 }, 
			15,440, 452, 25, 35.0D), 
	
	COAL_ORE(
			new int[] { 2096, 2097, 31068, 31070, 31069, 11932, 11930,  }, 
			30, 453, 452, 35, 50.0D), 
	
	SILVER_ORE(
			new int[] { 37670, 37306, 37304, 37305,  }, 
			20, 442, 452, 40, 40.0D), 
			
	GOLD_ORE(
			new int[] { 2098, 2099, 45067, 45068, 31065, 31066, 37310, 37312 }, 
			40, 444, 452, 40, 65.0D), 
	
	MITHRIL_ORE(
			new int[] {2102, 2103, 31086, 31087, 31088, 11942, 11944,  }, 
			55, 447, 452, 55, 80.0D), 
	
	ADAMANT_ORE(
			new int[] { 2105, 29233, 29235, 31085, 31083, 11939, 11941,  }, 
			70, 449, 452, 65, 110.0D), 
	
	RUNITE_ORE(new int[] { 45070, 45069 }, 
			85, 451, 452, 210, 145.0D), 
	
	ESSENCE(new int[] { 2491 }, 
			30, 7936, -1, -1, 5.0D);

	int[] rockIds;
	int levelRequired;
	int replacementId;
	int reward;
	int respawnTimer;
	double experience;
	private static HashMap<Integer, MiningRockData> ore = new HashMap<Integer, MiningRockData>();

	private MiningRockData(int[] rocks, int level, int reward, int replacement, int respawn, double experience) {
		rockIds = rocks;
		levelRequired = level;
		this.reward = reward;
		replacementId = replacement;
		respawnTimer = respawn;
		this.experience = experience;
	}

	public static final void declare() {
		for (MiningRockData data : values())
			for (int i : data.getRocks())
				ore.put(Integer.valueOf(i), data);
	}

	public int[] getRocks() {
		return rockIds;
	}

	public int getLevel() {
		return levelRequired;
	}

	public int getReward() {
		return reward;
	}

	public int getReplacement() {
		return replacementId;
	}

	public int getRespawnTimer() {
		return respawnTimer;
	}

	public double getExperience() {
		return experience;
	}

	public static MiningRockData forId(int id) {
		return ore.get(Integer.valueOf(id));
	}
}
