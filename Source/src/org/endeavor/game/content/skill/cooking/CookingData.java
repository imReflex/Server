package org.endeavor.game.content.skill.cooking;

import java.util.HashMap;
import java.util.Map;

public enum CookingData {
	RAW_SHRIMP(317, 1, 34, 315, 323, 30.0D), SARDINE(327, 1, 38, 325, 369, 40.0D), ANCHOVIES(321, 1, 34, 319, 323,
			30.0D), HERRING(345, 5, 41, 347, 353, 50.0D), MACKEREL(353, 10, 45, 355, 353, 60.0D), TROUT(335, 15, 50,
			333, 343, 70.0D), COD(341, 18, 52, 339, 343, 75.0D), PIKE(349, 20, 53, 351, 343, 80.0D), SALMON(331, 25,
			58, 329, 343, 90.0D), SLIMY_EEL(3379, 28, 58, 3381, 3383, 95.0D), TUNA(359, 30, 65, 361, 367, 100.0D), KARAMBWAN(
			3142, 30, 200, 3144, 3148, 190.0D), RAINBOW_FISH(10138, 35, 60, 10136, 10140, 110.0D), CAVE_EEL(5001, 38,
			40, 4003, 5002, 115.0D), LOBSTER(377, 40, 74, 379, 381, 120.0D), BASS(363, 43, 80, 365, 367, 130.0D), SWORDFISH(
			371, 45, 86, 373, 375, 140.0D), LAVA_EEL(2148, 53, 53, 2149, 3383, 30.0D), MONKFISH(7944, 62, 92, 7946,
			7948, 150.0D), SHARK(383, 80, 99, 385, 387, 210.0D), SEA_TURTLE(395, 82, 150, 397, 399, 212.0D), CAVEFISH(
			15264, 88, 150, 15266, 15268, 214.0D), MANTA_RAY(389, 91, 150, 391, 393, 216.0D), ROCKTAIL(15270, 93, 150,
			15272, 15274, 225.0D);

	int foodId;
	int levelRequired;
	int noBurnLevel;
	int replacement;
	int burnt;
	double experience;
	private static Map<Integer, CookingData> food = new HashMap<Integer, CookingData>();

	private CookingData(int food, int level, int noBurn, int replacement, int burnt, double exp) {
		foodId = food;
		levelRequired = level;
		noBurnLevel = noBurn;
		experience = exp;
		this.replacement = replacement;
		this.burnt = burnt;
	}

	public static final void declare() {
		for (CookingData data : values())
			food.put(Integer.valueOf(data.getFoodId()), data);
	}

	public int getFoodId() {
		return foodId;
	}

	public int getLevelRequired() {
		return levelRequired;
	}

	public int getNoBurnLevel() {
		return noBurnLevel;
	}

	public double getExperience() {
		return experience;
	}

	public int getReplacement() {
		return replacement;
	}

	public int getBurnt() {
		return burnt;
	}

	public static CookingData forId(int id) {
		return food.get(Integer.valueOf(id));
	}
}
