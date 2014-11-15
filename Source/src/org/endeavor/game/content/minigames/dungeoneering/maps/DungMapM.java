package org.endeavor.game.content.minigames.dungeoneering.maps;

import org.endeavor.game.content.minigames.dungeoneering.DungChest;
import org.endeavor.game.content.minigames.dungeoneering.DungConstants;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.dungeoneering.DungMap;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.VirtualMobRegion;
import org.endeavor.game.entity.object.GameObject;

public class DungMapM implements DungMap {
	public static final Location[] MOB_SPAWNS = { new Location(3237, 9670), new Location(3231, 9688),
			new Location(3233, 9690), new Location(3235, 9697), new Location(3235, 9701), new Location(3231, 9697),
			new Location(3218, 9695), new Location(3218, 9692), new Location(3213, 9693), new Location(3210, 9692),
			new Location(3211, 9699), new Location(3214, 9700), new Location(3217, 9699), new Location(3214, 9691) };

	@Override
	public Location getPlayerSpawn() {
		return new Location(3244, 9664);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return MOB_SPAWNS;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 3210, 9700, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 3245, 9662, game.getZ(), 10, 0));

		game.spawn(new GameObject(2732, 3213, 9694, game.getZ(), 10, 0));
		game.addNonCombatMob(new Mob(DungConstants.ROCKTAIL_SPOT_ID, false, new Location(3213, 9691, game.getZ()),
				new VirtualMobRegion(3213, 9691, 5)));
	}
}
