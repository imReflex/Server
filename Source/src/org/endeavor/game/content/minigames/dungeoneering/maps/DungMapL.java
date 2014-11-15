package org.endeavor.game.content.minigames.dungeoneering.maps;

import org.endeavor.game.content.minigames.dungeoneering.DungChest;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.dungeoneering.DungMap;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.object.GameObject;

public class DungMapL implements DungMap {
	public static final Location[] MOB_SPAWNS = { new Location(3281, 9224), new Location(3285, 9227),
			new Location(3295, 9232), new Location(3298, 9228), new Location(3302, 9224), new Location(3309, 9224),
			new Location(3304, 9234), new Location(3311, 9236), new Location(3317, 9240), new Location(3300, 9245),
			new Location(3298, 9249), new Location(3302, 9252), new Location(3313, 9257), new Location(3316, 9254),
			new Location(3309, 9263), new Location(3295, 9262), new Location(3289, 9264), new Location(3286, 9271),
			new Location(3273, 9263), new Location(3271, 9266) };

	@Override
	public Location getPlayerSpawn() {
		return new Location(3270, 9222);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return MOB_SPAWNS;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 3272, 9272, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 3271, 9220, game.getZ(), 10, 0));
	}
}
