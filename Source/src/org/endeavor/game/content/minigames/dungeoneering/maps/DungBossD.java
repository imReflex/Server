package org.endeavor.game.content.minigames.dungeoneering.maps;

import org.endeavor.game.content.minigames.dungeoneering.DungChest;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.dungeoneering.DungMap;
import org.endeavor.game.content.minigames.dungeoneering.DungMob;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.VirtualMobRegion;
import org.endeavor.game.entity.object.GameObject;

public class DungBossD implements DungMap {
	@Override
	public Location getPlayerSpawn() {
		return new Location(3250, 5549);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return null;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 3253, 5561, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 3249, 5546, game.getZ(), 10, 0));

		game.add(new DungMob(9733, new Location(3239, 5556, game.getZ()), new VirtualMobRegion(3239, 5556, 150), game));
	}
}
