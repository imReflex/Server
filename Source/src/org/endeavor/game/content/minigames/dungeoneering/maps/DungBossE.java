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

public class DungBossE implements DungMap {
	@Override
	public Location getPlayerSpawn() {
		return new Location(3256, 9224);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return null;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 3238, 9246, game.getZ(), 10, 0));

		game.spawn(new DungChest(2566, 3256, 9222, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 3257, 9222, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 3244, 9220, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 3243, 9220, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 3242, 9220, game.getZ(), 10, 0));

		game.spawn(new GameObject(2732, 3245, 9243, game.getZ(), 10, 0));
		game.addNonCombatMob(new Mob(DungConstants.ROCKTAIL_SPOT_ID, false, new Location(3245, 9240, game.getZ()),
				new VirtualMobRegion(3245, 9240, 5)));

		game.add(new DungMob(10141, new Location(3228, 9253, game.getZ()), new VirtualMobRegion(3228, 9253, 500), game));
	}
}
