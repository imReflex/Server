package org.endeavor.game.content.minigames.dungeoneering.maps;

import org.endeavor.game.content.minigames.dungeoneering.DungChest;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.dungeoneering.DungMap;
import org.endeavor.game.content.minigames.dungeoneering.DungMob;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.VirtualMobRegion;
import org.endeavor.game.entity.object.GameObject;

public class DungBossA implements DungMap {
	@Override
	public Location getPlayerSpawn() {
		return new Location(2808, 10105);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return null;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 2758, 10089, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 2797, 10091, game.getZ(), 10, 0));

		game.add(new DungMob(10057, new Location(2774, 10086, game.getZ()), new VirtualMobRegion(2774, 10086, 500),
				game));
	}
}
