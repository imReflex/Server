package org.endeavor.game.content.minigames.dungeoneering.maps;

import org.endeavor.game.content.minigames.dungeoneering.DungChest;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.dungeoneering.DungMap;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.object.GameObject;

public class DungMapO implements DungMap {
	public static final Location[] MOB_SPAWNS = { new Location(3309, 5544), new Location(3309, 5541),
			new Location(3321, 5539), new Location(3322, 5537), new Location(3322, 5534), new Location(3309, 5536),
			new Location(3315, 5531), new Location(3307, 5525), new Location(3311, 5525), new Location(3311, 5522),
			new Location(3313, 5520), new Location(3310, 5519) };

	@Override
	public Location getPlayerSpawn() {
		return new Location(3321, 5552);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return MOB_SPAWNS;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 3299, 5533, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 3320, 5547, game.getZ(), 10, 0));
	}
}
