package org.endeavor.game.content.minigames.dungeoneering;

import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.VirtualMobRegion;

public class DungMob extends Mob {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5847982277287332944L;

	public DungMob(int npcId, Location location, VirtualMobRegion virtualRegion, DungGame game) {
		super(npcId, true, location, null, false, false, virtualRegion);
		setDungGame(game);

		if (!isBoss(npcId)) {
			for (int i = 0; i < getBonuses().length; i++) {
				int tmp36_34 = i;
				short[] tmp36_31 = getBonuses();
				tmp36_31[tmp36_34] = ((short) (tmp36_31[tmp36_34] + game.getWave() * 2));
			}

			for (int i = 0; i < 6; i++) {
				int m = game.getWave() * 2;
				int tmp82_80 = i;
				short[] tmp82_77 = getLevels();
				tmp82_77[tmp82_80] = ((short) (tmp82_77[tmp82_80] + m));
				int tmp95_93 = i;
				short[] tmp95_90 = getMaxLevels();
				tmp95_90[tmp95_93] = ((short) (tmp95_90[tmp95_93] + m));
			}

			if (game.getFloorWeakness() == CombatTypes.MELEE) {
				setMeleeWeaknessMod(0.7D);
				short[] tmp136_131 = getBonuses();
				tmp136_131[8] = ((short) (tmp136_131[8] + (short) (int) (getBonuses()[8] * 0.4D)));
				short[] tmp161_156 = getBonuses();
				tmp161_156[9] = ((short) (tmp161_156[9] + (short) (int) (getBonuses()[9] * 0.4D)));
			} else if (game.getFloorWeakness() == CombatTypes.RANGED) {
				setRangedWeaknessMod(0.7D);
				short[] tmp207_202 = getBonuses();
				tmp207_202[8] = ((short) (tmp207_202[8] + (short) (int) (getBonuses()[8] * 0.4D)));
				short[] tmp232_227 = getBonuses();
				tmp232_227[6] = ((short) (tmp232_227[6] + (short) (int) (getBonuses()[6] * 0.4D)));
				int tmp256_255 = 5;
				short[] tmp256_252 = getBonuses();
				tmp256_252[tmp256_255] = ((short) (tmp256_252[tmp256_255] + (short) (int) (getBonuses()[5] * 0.4D)));
				short[] tmp280_275 = getBonuses();
				tmp280_275[7] = ((short) (tmp280_275[7] + (short) (int) (getBonuses()[7] * 0.4D)));
			} else {
				setMagicWeaknessMod(0.7D);
				short[] tmp315_310 = getBonuses();
				tmp315_310[9] = ((short) (tmp315_310[9] + (short) (int) (getBonuses()[9] * 0.4D)));
				short[] tmp340_335 = getBonuses();
				tmp340_335[6] = ((short) (tmp340_335[6] + (short) (int) (getBonuses()[6] * 0.4D)));
				int tmp364_363 = 5;
				short[] tmp364_360 = getBonuses();
				tmp364_360[tmp364_363] = ((short) (tmp364_360[tmp364_363] + (short) (int) (getBonuses()[5] * 0.4D)));
				short[] tmp388_383 = getBonuses();
				tmp388_383[7] = ((short) (tmp388_383[7] + (short) (int) (getBonuses()[7] * 0.4D)));
			}
		}

		getFollowing().setIgnoreDistance(true);
	}

	public static boolean isBoss(int id) {
		return (id == 9733) || (id == 9909) || (id == 10072) || (id == 10127) || (id == 10141);
	}
}
