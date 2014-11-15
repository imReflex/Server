package org.endeavor.game.content.minigames.dungeoneering.maps;

import org.endeavor.game.content.minigames.dungeoneering.DungChest;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.dungeoneering.DungMap;
import org.endeavor.game.content.minigames.dungeoneering.DungMob;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.VirtualMobRegion;
import org.endeavor.game.entity.object.GameObject;

public class DungBossC implements DungMap {
	@Override
	public Location getPlayerSpawn() {
		return new Location(3234, 5472);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return null;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 3260, 5491, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 3236, 5470, game.getZ(), 10, 0));

		game.add(new DungMob(10072, new Location(3251, 5488, game.getZ()), new VirtualMobRegion(3251, 5488, 500), game));
	}
}
