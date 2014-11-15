package org.endeavor.game.content.minigames.dungeoneering.maps;

import org.endeavor.game.content.minigames.dungeoneering.DungChest;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.dungeoneering.DungMap;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.object.GameObject;

public class DungMapB implements DungMap {
	public static final Location[] MOB_SPAWNS = { new Location(2592, 9621), new Location(2585, 9623),
			new Location(2578, 9650), new Location(2571, 9639), new Location(2571, 9643), new Location(2598, 9658),
			new Location(2590, 9657), new Location(2585, 9657), new Location(2582, 9657), new Location(2599, 9604),
			new Location(2607, 9621), new Location(2616, 9630), new Location(2595, 9636), new Location(2608, 9639),
			new Location(2591, 9656), new Location(2581, 9656) };

	@Override
	public Location getPlayerSpawn() {
		return new Location(2585, 9609);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return MOB_SPAWNS;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 2620, 9661, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 2588, 9607, game.getZ(), 10, 0));
	}
}
