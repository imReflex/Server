package org.endeavor.game.content.minigames.dungeoneering.maps;

import org.endeavor.game.content.minigames.dungeoneering.DungChest;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.dungeoneering.DungMap;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.object.GameObject;

public class DungMapF implements DungMap {
	public static final Location[] MOB_SPAWNS = { new Location(2699, 9694), new Location(2710, 9701),
			new Location(2720, 9700), new Location(2722, 9714), new Location(2719, 9715), new Location(2723, 9688),
			new Location(2721, 9681), new Location(2721, 9668), new Location(2704, 9699), new Location(2721, 9703),
			new Location(2722, 9694), new Location(2720, 9687), new Location(2721, 9679) };

	@Override
	public Location getPlayerSpawn() {
		return new Location(2696, 9684);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return MOB_SPAWNS;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 2718, 9668, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 2700, 9688, game.getZ(), 10, 3));
	}
}
