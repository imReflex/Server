package org.endeavor.game.content.minigames.dungeoneering.maps;

import org.endeavor.game.content.minigames.dungeoneering.DungChest;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.dungeoneering.DungMap;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.object.GameObject;

public class DungMapK implements DungMap {
	public static final Location[] MOB_SPAWNS = { new Location(2836, 9491), new Location(2833, 9494),
			new Location(2835, 9500), new Location(2832, 9511), new Location(2836, 9514), new Location(2829, 9516),
			new Location(2830, 9520), new Location(2827, 9526), new Location(2838, 9523), new Location(2841, 9518),
			new Location(2853, 9518), new Location(2865, 9527), new Location(2865, 9514), new Location(2873, 9513),
			new Location(2851, 9509), new Location(2854, 9504), new Location(2865, 9493) };

	@Override
	public Location getPlayerSpawn() {
		return new Location(2853, 9484);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return MOB_SPAWNS;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 2870, 9495, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 2850, 9477, game.getZ(), 10, 0));
	}
}
