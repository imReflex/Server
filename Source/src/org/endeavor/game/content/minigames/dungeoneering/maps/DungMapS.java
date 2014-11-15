package org.endeavor.game.content.minigames.dungeoneering.maps;

import org.endeavor.game.content.minigames.dungeoneering.DungChest;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.dungeoneering.DungMap;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.object.GameObject;

public class DungMapS implements DungMap {
	public static final Location[] MOB_SPAWNS = { new Location(3312, 5560), new Location(3315, 5562),
			new Location(3308, 5563), new Location(3300, 5564), new Location(3298, 5561), new Location(3305, 5554),
			new Location(3303, 5550), new Location(3302, 5547), new Location(3299, 5547), new Location(3300, 5551),
			new Location(3300, 5543), new Location(3299, 5540), new Location(3293, 5556) };

	@Override
	public Location getPlayerSpawn() {
		return new Location(3313, 5554);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return MOB_SPAWNS;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 3297, 5536, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 3315, 5552, game.getZ(), 10, 0));
	}
}
