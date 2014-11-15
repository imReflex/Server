package org.endeavor.game.content.skill.hunter.snare;

import org.endeavor.game.entity.Location;

public enum Birds {

	CRIMSON_SWIFT("Crimson Swift", 5073, 1, 34, 10088, 19180, 19179, new Location(2614, 2931, 0), new Location(2611, 2928, 0), new Location(2614, 2925, 0), new Location(2610, 2922, 0), 
			new Location(2613, 2919, 0), new Location(2608, 2918, 0), new Location(2608, 2916, 0), new Location(2606, 2914, 9), new Location(2607, 2912, 0)),
	GOLDEN_WARBLER("Golden Warbler", 5075, 5, 48, 10090, 19184, 19183, new Location(3398, 3104, 0), new Location(3402, 3106, 0), new Location(3399, 3109, 0), new Location(3404, 3112, 0), 
			new Location(3409, 3112, 0), new Location(3405, 3115, 0), new Location(3407, 3102, 0), new Location(3410, 3105, 0)),
	COPPER_LONGTAILS("Copper Longtails", 5076, 9, 61, 10091, 19186, 19185, new Location(2341, 3598, 0), new Location(2344, 3595, 0), new Location(2337, 3597, 0), new Location(2345, 3600, 0),
			new Location(2342, 3604, 0), new Location(2348, 3600, 0), new Location(2338, 3591, 0), new Location(2340, 3588, 0), new Location(2344, 3590, 0)),
	CERULEAN_TWITCH("Cerulean Twitch", 5074, 11, 64.67, 10089, 19182, 19181, new Location(2721, 3769, 0), new Location(2718, 3767, 0), new Location(2720, 3766, 0), new Location(2717, 3773, 0),
			new Location(2723, 3774, 0), new Location(2724, 3769, 0), new Location(2724, 3765, 0), new Location(2727, 3769, 0), new Location(2725, 3771, 0)),
	TROPICAL_WAGTAIL("Tropical Wagtail", 5072, 19, 95.2, 10087, 19178, 19177, new Location(2542, 2888, 0), new Location(2541, 2892, 0), new Location(2544, 2894, 0), new Location(2550, 2894, 0),
			new Location(2550, 2889, 0), new Location(2545, 2885, 0), new Location(2544, 2889, 0), new Location(2547, 2892, 0), new Location(2536, 2889, 0));
	/*WIMPY_BIRD("Wimpy Bird", 7031, 39, 167, 11525, )*/
	
	private String name;
	private int npcId;
	private int level;
	private double experience;
	private int feather;
	private int snaredCaught;
	private int snaredFell;
	private Location[] spawnPoints;
	
	Birds(String name, int npcId, int level, double experience, int feather, int snaredCaught, int snaredFell, Location ... spawnPoints) {
		this.name = name;
		this.npcId = npcId;
		this.level = level;
		this.experience = experience;
		this.feather = feather;
		this.snaredCaught = snaredCaught;
		this.snaredFell = snaredFell;
		this.spawnPoints = spawnPoints;
	}
	
	public String getName() {
		return name;
	}
	
	public int getNpcId() {
		return npcId;
	}
	
	public int getLevel() {
		return level;
	}
	
	public double getExperience() {
		return experience;
	}
	
	public int getFeather() {
		return feather;
	}
	
	public int getSnaredCaught() {
		return snaredCaught;
	}
	
	public int getSnaredFell() {
		return snaredFell;
	}
	
	public Location[] getSpawnPoints() {
		return spawnPoints;
	}
	
	public Location getSpawn(int index) {
		return spawnPoints[index];
	}
	
}
