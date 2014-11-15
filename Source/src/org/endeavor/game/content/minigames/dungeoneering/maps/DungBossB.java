package org.endeavor.game.content.minigames.dungeoneering.maps;

import org.endeavor.game.content.minigames.dungeoneering.DungChest;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.dungeoneering.DungMap;
import org.endeavor.game.content.minigames.dungeoneering.DungMob;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.VirtualMobRegion;
import org.endeavor.game.entity.object.GameObject;

public class DungBossB implements DungMap {
	@Override
	public Location getPlayerSpawn() {
		return new Location(3305, 5495);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return null;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 3265, 5491, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 3305, 5492, game.getZ(), 10, 0));

		game.add(new DungMob(10127, new Location(3271, 5489, game.getZ()), new VirtualMobRegion(3271, 5489, 200), game));
	}
}
