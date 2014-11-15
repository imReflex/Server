package org.endeavor.game.content.minigames.dungeoneering.maps;

import org.endeavor.game.content.minigames.dungeoneering.DungChest;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.dungeoneering.DungMap;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.object.GameObject;

public class DungMapC implements DungMap {
	public static final Location[] MOB_SPAWNS = { new Location(2730, 9775), new Location(2728, 9760),
			new Location(2722, 9753), new Location(2714, 9750), new Location(2712, 9753), new Location(2717, 9744),
			new Location(2703, 9739), new Location(2704, 9750), new Location(2705, 9763), new Location(2705, 9773),
			new Location(2698, 9772) };

	@Override
	public Location getPlayerSpawn() {
		return new Location(2723, 9775);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return MOB_SPAWNS;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 2699, 9779, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 2726, 9772, game.getZ(), 10, 0));
	}
}
