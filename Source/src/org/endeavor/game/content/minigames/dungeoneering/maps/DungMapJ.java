package org.endeavor.game.content.minigames.dungeoneering.maps;

import org.endeavor.game.content.minigames.dungeoneering.DungChest;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.dungeoneering.DungMap;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.object.GameObject;

public class DungMapJ implements DungMap {
	public static final Location[] MOB_SPAWNS = { new Location(2996, 9545), new Location(2996, 9549),
			new Location(2993, 9568), new Location(2992, 9574), new Location(2998, 9579), new Location(2994, 9584),
			new Location(3020, 9580), new Location(3024, 9585), new Location(3023, 9591), new Location(3034, 9583),
			new Location(3041, 9585), new Location(3045, 9580), new Location(3050, 9588), new Location(3053, 9579),
			new Location(3060, 9578), new Location(3057, 9569), new Location(3053, 9566) };

	@Override
	public Location getPlayerSpawn() {
		return new Location(3007, 9550);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return MOB_SPAWNS;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 3061, 9566, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 3007, 9552, game.getZ(), 10, 2));
	}
}
