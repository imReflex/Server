package org.endeavor.game.content.minigames.dungeoneering.maps;

import org.endeavor.game.content.minigames.dungeoneering.DungChest;
import org.endeavor.game.content.minigames.dungeoneering.DungConstants;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.dungeoneering.DungMap;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.VirtualMobRegion;
import org.endeavor.game.entity.object.GameObject;

public class DungMapN implements DungMap {
	public static final Location[] MOB_SPAWNS = { new Location(2765, 9336), new Location(2768, 9331),
			new Location(2765, 9329), new Location(2770, 9326), new Location(2774, 9325), new Location(2774, 9320),
			new Location(2764, 9321), new Location(2760, 9325), new Location(2757, 9320), new Location(2757, 9330),
			new Location(2767, 9309), new Location(2764, 9303), new Location(2758, 9304), new Location(2764, 9299),
			new Location(2777, 9291), new Location(2767, 9286) };

	@Override
	public Location getPlayerSpawn() {
		return new Location(2758, 9338);
	}

	@Override
	public Location[] getMobSpawnsForWave() {
		return MOB_SPAWNS;
	}

	@Override
	public void init(DungGame game) {
		game.spawn(new GameObject(2465, 2782, 9285, game.getZ(), 10, 0));
		game.spawn(new DungChest(2566, 2756, 9334, game.getZ(), 10, 0));

		game.spawn(new GameObject(2732, 2773, 9290, game.getZ(), 10, 0));
		game.addNonCombatMob(new Mob(DungConstants.ROCKTAIL_SPOT_ID, false, new Location(2774, 9293, game.getZ()),
				new VirtualMobRegion(2774, 9293, 5)));
	}
}
