package org.endeavor.game.content.minigames.dungeoneering.maps;

import org.endeavor.game.content.minigames.dungeoneering.DungChest;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.dungeoneering.DungMap;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.object.GameObject;

public class DungMapT implements DungMap {
	public static final Location[] MOB_SPAWNS = { new Location(3286, 5543), new Location(3289, 5544),
			new Location(3282, 5546), new Location(3279, 5546), new Location(3276, 5547), new Location(3273, 5547),
			new Location(3271, 5548), new Location(3269, 5550), new Location(3269, 5553), new Location(3272, 5557),
			new Location(3280, 5557), new Location(3282, 5556), new Location(3284, 5554), new Location(3289, 5546) };

	@Override
	public Location getPlayerSpawn() {
		return new Location(3287, 5537);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return MOB_SPAWNS;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 3266, 5552, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 3288, 5536, game.getZ(), 10, 0));
	}
}
