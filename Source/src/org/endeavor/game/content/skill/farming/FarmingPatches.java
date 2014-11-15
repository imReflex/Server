package org.endeavor.game.content.skill.farming;

public enum FarmingPatches {
	CATHERBY_ALLOTMENT_NORTH(2805, 3465, 2815, 3469, 65536, 504, 5343, 2275, 5329, SeedType.ALLOTMENT), 
	CATHERBY_ALLOTMENT_SOUTH(2805, 3458, 2815, 3461, 16777216, 504, 5343, 2275, 5329, SeedType.ALLOTMENT), 
	CATHERBY_HERB(2813, 3462, 2815, 3464, 256, 515, 5343, 2275, 5329, SeedType.HERB), 
	CATHERBY_FLOWER(2808, 3462, 2811, 3465, 256, 508, 5343, 2275, 5329, SeedType.FLOWER);

	public final int x;
	public final int y;
	public final int x2;
	public final int y2;
	public final int mod;
	public final int config;
	public final int harvestAnimation;
	public final int harvestItem;
	public final int planter;
	public final SeedType seedType;

	private FarmingPatches(int x, int y, int x2, int y2, int mod, int config, int planter, int harvestAnimation,
			int harvestItem, SeedType seedType) {
		this.x = x;
		this.y = y;
		this.x2 = x2;
		this.y2 = y2;
		this.mod = mod;
		this.config = config;
		this.harvestAnimation = harvestAnimation;
		this.harvestItem = harvestItem;
		this.planter = planter;
		this.seedType = seedType;
	}
}
