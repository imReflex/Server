package org.endeavor.game.content.minigames.dungeoneering.maps;

import org.endeavor.game.content.minigames.dungeoneering.DungChest;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.dungeoneering.DungMap;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.object.GameObject;

public class DungMapR implements DungMap {
	public static final Location[] MOB_SPAWNS = { new Location(3307, 5463), new Location(3310, 5462),
			new Location(3313, 5459), new Location(3317, 5456), new Location(3314, 5454), new Location(3318, 5452),
			new Location(3316, 5449), new Location(3318, 5446), new Location(3320, 5448), new Location(3313, 5445),
			new Location(3312, 5449), new Location(3307, 5445), new Location(3305, 5448), new Location(3306, 5452),
			new Location(3306, 5455), new Location(3305, 5458) };

	@Override
	public Location getPlayerSpawn() {
		return new Location(3302, 5467);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return MOB_SPAWNS;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 3299, 5450, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 3301, 5463, game.getZ(), 10, 0));
	}
}
