package org.endeavor.game.content;

import org.endeavor.game.entity.player.Player;

public class LocationUnlocking {
	public static final String LOCATION_UN_KEY = "locationunlockingkey";
	public static final UnlockedLocation[] LOCATIONS_TO_UNLOCK = {
			new UnlockedLocation("Training dungeon", 2845, 10205, 0),
			new UnlockedLocation("Rock crab dungeon", 2851, 10211, 0),
			new UnlockedLocation("Experiments dungeon", 2837, 10230, 0),
			new UnlockedLocation("Brimhaven dungeon", 2823, 10204, 0),
			new UnlockedLocation("Taverly dungeon", 2840, 10189, 0),
			new UnlockedLocation("Dagganoth cave", 2940, 10179, 0),
			new UnlockedLocation("Varrock sewers", 2854, 10194, 0), new UnlockedLocation("God Wars", 2937, 10231, 0),
			new UnlockedLocation("Corporeal/Wraiths", 2935, 10163, 0),
			new UnlockedLocation("King black dragon", 2936, 10202, 0),
			new UnlockedLocation("Tormented demons", 2839, 10149, 0),
			new UnlockedLocation("Barrelchest/Giants", 2868, 10247, 0) };

	private UnlockedLocation[] unlockedLocations = new UnlockedLocation[LOCATIONS_TO_UNLOCK.length];

	public UnlockedLocation[] getUnlockedLocations() {
		return unlockedLocations;
	}

	public void setUnlockedLocations(UnlockedLocation[] unlockedLocations) {
		this.unlockedLocations = unlockedLocations;
	}

	public void add(Player player, UnlockedLocation loc) {
	}

	public static class UnlockedLocation {
		public final String name;
		public final int x;
		public final int y;
		public final int z;

		public UnlockedLocation(String name, int x, int y, int z) {
			this.name = name;
			this.x = x;
			this.y = y;
			this.z = z;
		}

		@Override
		public boolean equals(Object o) {
			return ((UnlockedLocation) o).name.equals(name);
		}
	}
}
