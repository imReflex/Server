package org.endeavor.game.content.skill.summoning.specials;

import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.content.skill.summoning.FamiliarMob;
import org.endeavor.game.content.skill.summoning.FamiliarSpecial;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.player.Player;

public class SpiritJelly implements FamiliarSpecial {
	@Override
	public boolean execute(Player player, FamiliarMob mob) {
		Entity a = mob.getOwner().getCombat().getAttacking();

		if (a != null) {
			mob.getAttributes().set("summonfammax", Integer.valueOf(13));
			mob.getCombat().setCombatType(CombatTypes.MAGIC);

			if (a.getLevels()[0] > a.getMaxLevels()[0] - 5) {
				int tmp61_60 = 0;
				short[] tmp61_57 = a.getLevels();
				tmp61_57[tmp61_60] = ((short) (tmp61_57[tmp61_60] - 5));

				if (a.getLevels()[0] < a.getMaxLevels()[0] - 5) {
					a.getLevels()[0] = ((short) (a.getMaxLevels()[0] - 5));
				}

				if (!a.isNpc()) {
					Player p = org.endeavor.game.entity.World.getPlayers()[a.getIndex()];

					if (p != null) {
						p.getSkill().update(0);
					}
				}
			}
		}

		return true;
	}

	@Override
	public int getAmount() {
		return 6;
	}

	@Override
	public FamiliarSpecial.SpecialType getSpecialType() {
		return FamiliarSpecial.SpecialType.COMBAT;
	}

	@Override
	public double getExperience() {
		return 1.0D;
	}
}
