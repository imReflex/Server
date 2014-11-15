package org.endeavor.game.content.minigames.dungeoneering.maps;

import org.endeavor.game.content.minigames.dungeoneering.DungChest;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.dungeoneering.DungMap;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.object.GameObject;

public class DungMapI implements DungMap {
	public static final Location[] MOB_SPAWNS = { new Location(3024, 9941), new Location(3036, 9939),
			new Location(3046, 9934), new Location(3054, 9934), new Location(3062, 9938), new Location(3024, 9941),
			new Location(3060, 9954), new Location(3053, 9954), new Location(3032, 9952), new Location(3024, 9960),
			new Location(3025, 9963), new Location(3044, 9967), new Location(3015, 9970) };

	@Override
	public Location getPlayerSpawn() {
		return new Location(3017, 9923, 1);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return MOB_SPAWNS;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 3016, 9978, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 3016, 9922, game.getZ(), 10, 0));
	}
}
