package org.endeavor.game.content.minigames.dungeoneering.maps;

import org.endeavor.game.content.minigames.dungeoneering.DungChest;
import org.endeavor.game.content.minigames.dungeoneering.DungConstants;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.dungeoneering.DungMap;
import org.endeavor.game.content.minigames.dungeoneering.DungMob;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.VirtualMobRegion;
import org.endeavor.game.entity.object.GameObject;

public class DungMapH implements DungMap {
	@Override
	public Location getPlayerSpawn() {
		return new Location(2705, 10154);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return null;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 2716, 10163, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 2705, 10153, game.getZ(), 10, 0));

		game.spawn(new GameObject(2732, 2701, 10166, game.getZ(), 10, 0));
		game.addNonCombatMob(new Mob(DungConstants.ROCKTAIL_SPOT_ID, false, new Location(2704, 10168, game.getZ()),
				new VirtualMobRegion(2704, 10168, 5)));

		game.add(new DungMob(5666, new Location(2706, 10159, game.getZ()), new VirtualMobRegion(2706, 10159, 50), game));
	}
}
