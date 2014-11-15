package org.endeavor.game.content.minigames.dungeoneering.maps;

import org.endeavor.game.content.minigames.dungeoneering.DungChest;
import org.endeavor.game.content.minigames.dungeoneering.DungConstants;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.dungeoneering.DungMap;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.VirtualMobRegion;
import org.endeavor.game.entity.object.GameObject;

public class DungMapG implements DungMap {
	public static final Location[] MOB_SPAWNS = { new Location(2405, 10197), new Location(2412, 10191),
			new Location(2396, 10199), new Location(2385, 10194), new Location(2405, 10197), new Location(2389, 10201),
			new Location(2378, 10212), new Location(2402, 10226), new Location(2420, 10200), new Location(2419, 10214) };

	@Override
	public Location getPlayerSpawn() {
		return new Location(2404, 10188);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return MOB_SPAWNS;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 2406, 10225, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 2401, 10186, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 2396, 10231, game.getZ(), 10, 0));

		game.spawn(new GameObject(2732, 2402, 10225, game.getZ(), 10, 0));
		game.addNonCombatMob(new Mob(DungConstants.ROCKTAIL_SPOT_ID, false, new Location(2405, 10224, game.getZ()),
				new VirtualMobRegion(2405, 10224, 5)));
	}
}
