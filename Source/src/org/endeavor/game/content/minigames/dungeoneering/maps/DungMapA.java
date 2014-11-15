package org.endeavor.game.content.minigames.dungeoneering.maps;

import org.endeavor.game.content.minigames.dungeoneering.DungChest;
import org.endeavor.game.content.minigames.dungeoneering.DungConstants;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.dungeoneering.DungMap;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.VirtualMobRegion;
import org.endeavor.game.entity.object.GameObject;

public class DungMapA implements DungMap {
	public static final Location[] MOB_SPAWNS = { new Location(2962, 9637), new Location(2963, 9632),
			new Location(2967, 9633), new Location(2980, 9633), new Location(2985, 9634), new Location(2989, 9637),
			new Location(2985, 9642), new Location(2979, 9640) };

	@Override
	public Location getPlayerSpawn() {
		return new Location(2962, 9650);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return MOB_SPAWNS;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 2989, 9654, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 2958, 9639, game.getZ(), 10, 1));

		game.spawn(new GameObject(2732, 2957, 9637, game.getZ(), 10, 0));
		game.addNonCombatMob(new Mob(DungConstants.ROCKTAIL_SPOT_ID, false, new Location(2955, 9637, game.getZ()),
				new VirtualMobRegion(2955, 9637, 5)));
	}
}
