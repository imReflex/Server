package org.endeavor.game.content.minigames.dungeoneering.maps;

import org.endeavor.game.content.minigames.dungeoneering.DungChest;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.dungeoneering.DungMap;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.object.GameObject;

public class DungMapQ implements DungMap {
	public static final Location[] MOB_SPAWNS = { new Location(3146, 5468), new Location(3146, 5473),
			new Location(3145, 5476), new Location(3145, 5478), new Location(3146, 5481), new Location(3151, 5475),
			new Location(3153, 5476), new Location(3159, 5479), new Location(3161, 5482), new Location(3160, 5484),
			new Location(3164, 5478) };

	@Override
	public Location getPlayerSpawn() {
		return new Location(3144, 5464);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return MOB_SPAWNS;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 3167, 5478, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 3142, 5462, game.getZ(), 10, 0));
	}
}
