package org.endeavor.game.content.minigames.dungeoneering.maps;

import org.endeavor.game.content.minigames.dungeoneering.DungChest;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.dungeoneering.DungMap;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.object.GameObject;

public class DungMapP implements DungMap {
	public static final Location[] MOB_SPAWNS = { new Location(3239, 5533), new Location(3239, 5530),
			new Location(3236, 5529), new Location(3236, 5526), new Location(3233, 5525), new Location(3233, 5521),
			new Location(3238, 5514), new Location(3239, 5511), new Location(3235, 5510), new Location(3231, 5511),
			new Location(3227, 5510), new Location(3224, 5512), new Location(3222, 5522), new Location(3220, 5524),
			new Location(3218, 5528), new Location(3216, 5531), new Location(3217, 5534), new Location(3220, 5538),
			new Location(3223, 5540), new Location(3226, 5540) };

	@Override
	public Location getPlayerSpawn() {
		return new Location(3234, 5547);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return MOB_SPAWNS;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 3222, 5509, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 3233, 5544, game.getZ(), 10, 0));
	}
}
