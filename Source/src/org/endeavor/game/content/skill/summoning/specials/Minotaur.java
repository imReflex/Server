package org.endeavor.game.content.skill.summoning.specials;

import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.content.skill.summoning.FamiliarMob;
import org.endeavor.game.content.skill.summoning.FamiliarSpecial;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.player.Player;

public class Minotaur implements FamiliarSpecial {
	@Override
	public boolean execute(Player player, FamiliarMob mob) {
		int max = 13;

		switch (mob.getData().ordinal()) {
		case 63:
			max = 19;
			break;
		case 52:
			max = 16;
			break;
		}

		mob.getCombat().setCombatType(CombatTypes.MAGIC);
		mob.getAttributes().set("summonfammax", Integer.valueOf(max));

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
