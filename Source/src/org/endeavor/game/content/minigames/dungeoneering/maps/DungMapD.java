package org.endeavor.game.content.minigames.dungeoneering.maps;

import org.endeavor.game.content.minigames.dungeoneering.DungChest;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.dungeoneering.DungMap;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.object.GameObject;

public class DungMapD implements DungMap {
	public static final Location[] MOB_SPAWNS = { new Location(2660, 9809), new Location(2661, 9806),
			new Location(2661, 9797), new Location(2689, 9808), new Location(2695, 9823), new Location(2698, 9839),
			new Location(2710, 9844), new Location(2720, 9844), new Location(2740, 9844), new Location(2667, 9827),
			new Location(2660, 9822), new Location(2656, 9827), new Location(2656, 9831), new Location(2665, 9852),
			new Location(2646, 9848), new Location(2645, 9867), new Location(2649, 9876) };

	@Override
	public Location getPlayerSpawn() {
		return new Location(2676, 9805);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return MOB_SPAWNS;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 2745, 9827, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 2672, 9808, game.getZ(), 10, 2));
		game.spawn(new DungChest(2566, 2656, 9876, game.getZ(), 10, 3));
	}
}
