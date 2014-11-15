package org.endeavor.game.content.skill.thieving;

import java.util.HashMap;
import java.util.Map;

public enum ThievingNpcData {
	MAN(new int[] {1, 3, 4, }, 1, 8.0D, 5, new int[][] { { 995, 125 } }), 
	
	FARMER(new int[] {7}, 10, 14.5D, 5, new int[][] { { 995, 125 } }), 
	
	FEMALE_HAM(
			new int[] {-1}, 15, 18.5D, 5, new int[][] { { 995, 125 } }), 
			
	MALE_HAM(new int[] {-1}, 20, 22.5D, 5, new int[][] { { 995, 125 } }), 
	
	HAM_GUARD(
			new int[] {-1}, 20, 22.5D, 5, new int[][] { { 995, 125 } }), 
	
	WARRIOR_WOMAN(new int[] {15}, 25, 26.0D, 5,
	new int[][] { { 995, 125 } }), 
	
	AL_KHARID_WARRIOR(new int[] {18}, 25, 26.0D, 5, new int[][] { { 995, 125 } }), 
	
	ROGUE(
			new int[] {187}, 32, 35.5D, 5, new int[][] { { 995, 125 } }), 
	
	CAVE_GOBLIN(new int[] {5752}, 36, 40.0D, 5, new int[][] { { 995,
	1000 } }), 
	
	GUARD(new int[] {18, 9, 3230}, 40, 46.5D, 5, new int[][] { { 995, 125 } }),
	
	FREMENNIK_CITIZEN(new int[] {-1}, 45, 46.5D, 5,
	new int[][] { { 995, 125 } }), 
	
	BEARDED_POLLNIVNIAN_BANDIT(new int[] {6174}, 45, 46.5D, 5, new int[][] { { 995, 40 } }), 
	
	DESERT_BANDIT(
			new int[] {1926}, 53, 79.5D, 5, new int[][] { { 995, 30 } }), 
	
	KNIGHT_OF_ARDOUGNE(new int[] {23}, 55, 84.299999999999997D, 5,
	new int[][] { { 995, 50 } }), 
	
	POLLNIVNIAN_BANDIT(new int[] {1880}, 55, 84.299999999999997D, 5,
	new int[][] { { 995, 50 } }), 
	
	YANILLE_WATCHMAN(new int[] {34}, 65, 137.5D, 5, new int[][] { { 995, 60 } }), 
	
	MENAPHITE_THUG(
			new int[] {1904}, 65, 137.5D, 5, new int[][] { { 995, 60 } }), 
	
	PALADIN(new int[] {20}, 70, 151.75D, 5, new int[][] { { 995, 80 } }), 
	
	MONKEY_KNIFE_FIGHTER(
			new int[] {-1}, 70, 150.0D, 0, new int[][] { { 995, 50 } }), 
	
	GNOME(new int[] {-1}, 75, 198.5D, 5, new int[][] { { 995, 300 } }), 
	
	HERO(
			new int[] {21}, 80, 273.30000000000001D, 6, new int[][] { { 995, 300 } }), 
	
	ELVES(new int[] {1183}, 85, 353.30000000000001D, 6,
	new int[][] { {995, 280}, { 995, 350 } }), 
	
	DWARF_TRADERS(new int[] {-1}, 90, 556.5D, 4, new int[][] { { 995, 400 } }), 
	
	
	//special cases
	MASTER_FARMER(
			new int[] {2234}, 90, 556.5D, 6, null),

	MASTER_FARMER2(new int[] {3299}, 90, 556.5D, 6, null),
	
	
	//end special cases
	;

	int[] npcIds;
	int levelRequired;
	int stunTime;
	int[][] retrievableItems;
	double experience;
	private static Map<Integer, ThievingNpcData> npcs = new HashMap<Integer, ThievingNpcData>();

	private ThievingNpcData(int[] ids, int level, double exp, int time, int[][] items) {
		npcIds = ids;
		levelRequired = level;
		experience = exp;
		stunTime = time;
		retrievableItems = items;
	}

	public static final void declare() {
		for (ThievingNpcData data : values()) {
			for (int i : data.npcIds) {
				npcs.put(i, data);
			}
		}
	}

	public int[] getNpcIds() {
		return npcIds;
	}

	public int getLevelRequired() {
		return levelRequired;
	}

	public double getExperience() {
		return experience;
	}

	public int getStunTime() {
		return stunTime;
	}

	public int[][] getItems() {
		return retrievableItems;
	}

	public static ThievingNpcData getNpcById(int id) {
		return npcs.get(Integer.valueOf(id));
	}
}
