package org.endeavor.game.content.skill.farming;

public enum Plants {
	GUAM(5291, 199, 4, 173, 170, 9, 5, SeedType.HERB, 11.0D, 12.5D, 4), MARENTILL(5292, 201, 11, 173, 170, 14, 5,
			SeedType.HERB, 13.5D, 15.0D, 4), TARROMIN(5293, 203, 18, 173, 170, 19, 5, SeedType.HERB, 16.0D, 18.0D, 4), HARRALANDER(
			5294, 205, 25, 173, 170, 26, 5, SeedType.HERB, 21.5D, 24.0D, 4), RANARR(5295, 207, 32, 173, 170, 32, 5,
			SeedType.HERB, 27.0D, 30.5D, 4), TOADFLAX(5296, 3050, 39, 173, 170, 36, 5, SeedType.HERB, 34.0D, 38.5D, 4), IRIT(
			5297, 209, 46, 173, 170, 44, 5, SeedType.HERB, 43.0D, 48.5D, 4), AVANTOE(5298, 211, 53, 173, 170, 50, 5,
			SeedType.HERB, 54.5D, 61.5D, 4), WERGALI(14870, 14836, 60, 173, 170, 46, 5, SeedType.HERB,
			52.799999999999997D, 52.799999999999997D, 4), KWUARM(5299, 213, 68, 173, 170, 56, 5, SeedType.HERB, 69.0D,
			78.0D, 4), SNAPDRAGON(5300, 3052, 75, 173, 170, 62, 5, SeedType.HERB, 87.5D, 98.5D, 4), CADANTINE(5301,
			215, 82, 173, 170, 67, 5, SeedType.HERB, 106.5D, 120.0D, 4), LANTADYME(5302, 2486, 89, 173, 170, 73, 5,
			SeedType.HERB, 134.5D, 151.5D, 4), DWARF_WEED(5303, 217, 96, 173, 170, 79, 5, SeedType.HERB, 170.5D,
			192.0D, 4), TORSTOL(5304, 219, 103, 173, 170, 85, 5, SeedType.HERB, 199.5D, 224.5D, 4),

	POTATO(5318, 1942, 6, 0, 0, 1, 7, SeedType.ALLOTMENT, 8.0D, 9.0D, 4), ONION(5319, 1957, 13, 0, 0, 5, 7,
			SeedType.ALLOTMENT, 9.5D, 10.5D, 4), CABBAGE(5324, 1967, 20, 0, 0, 7, 7, SeedType.ALLOTMENT, 10.0D, 11.5D,
			4), TOMATO(5322, 1982, 27, 0, 0, 12, 7, SeedType.ALLOTMENT, 12.5D, 14.0D, 4), SWEETCORN(5320, 7088, 34, 0,
			0, 20, 7, SeedType.ALLOTMENT, 17.0D, 19.0D, 5), STRAWBERRY(5323, 5504, 43, 0, 0, 31, 6, SeedType.ALLOTMENT,
			26.0D, 29.0D, 6), WATERMELON(5321, 5982, 52, 0, 0, 47, 4, SeedType.ALLOTMENT, 48.5D, 54.5D, 8),

	MARIGOLD(5096, 6010, 8, 0, 0, 2, 7, SeedType.FLOWER, 47.0D, 55.5D, 4), ROSEMARY(5097, 6014, 13, 0, 0, 11, 7,
			SeedType.FLOWER, 66.5D, 78.5D, 4), NASTURTIUM(5098, 6012, 18, 0, 0, 24, 7, SeedType.FLOWER, 111.0D, 130.5D,
			4), WOAD(5099, 5738, 23, 0, 0, 25, 7, SeedType.FLOWER, 115.5D, 136.0D, 4), LIMPWURT(5100, 225, 28, 0, 0,
			26, 7, SeedType.FLOWER, 21.5D, 120.0D, 5), WHITE_LILY(14589, 14583, 37, 0, 0, 52, 7, SeedType.FLOWER,
			70.0D, 250.0D, 4);

	public final int seed;
	public final int harvest;
	public final int healthy;
	public final int diseased;
	public final int dead;
	public final int level;
	public final int minutes;
	public final byte stages;
	public final double plantExperience;
	public final double harvestExperience;
	public final SeedType type;

	public static boolean isSeed(int id) {
		for (Plants i : values()) {
			if (i.seed == id) {
				return true;
			}
		}

		return false;
	}

	private Plants(int seed, int harvest, int config, int diseased, int dead, int level, int minutes, SeedType type,
			double plantExperience, double harvestExperience, int stages) {
		this.seed = seed;
		this.harvest = harvest;
		healthy = config;
		this.level = level;
		this.diseased = diseased;
		this.dead = dead;
		this.minutes = minutes;
		this.type = type;
		this.plantExperience = plantExperience;
		this.harvestExperience = harvestExperience;
		this.stages = ((byte) stages);
	}
}
