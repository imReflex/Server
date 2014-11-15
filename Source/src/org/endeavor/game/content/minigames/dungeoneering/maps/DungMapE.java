package org.endeavor.game.content.minigames.dungeoneering.maps;

import org.endeavor.game.content.minigames.dungeoneering.DungChest;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.dungeoneering.DungMap;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.object.GameObject;

public class DungMapE implements DungMap {
	public static final Location[] MOB_SPAWNS = { new Location(2715, 10208), new Location(2720, 10202),
			new Location(2712, 10217), new Location(2720, 10214), new Location(2728, 10209), new Location(2729, 10217),
			new Location(2734, 10218), new Location(2733, 10206), new Location(2736, 10203) };

	@Override
	public Location getPlayerSpawn() {
		return new Location(2708, 10205);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return MOB_SPAWNS;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 2735, 10196, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 2711, 10202, game.getZ(), 10, 0));
	}
}
