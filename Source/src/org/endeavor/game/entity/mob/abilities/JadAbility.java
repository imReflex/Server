package org.endeavor.game.entity.mob.abilities;

import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.content.combat.CombatEffect;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Graphic;

public class JadAbility implements CombatEffect {
	@Override
	public void execute(Entity e1, Entity e2) {
		if (e1.getCombat().getCombatType() == CombatTypes.RANGED)
			e2.getUpdateFlags().sendGraphic(new Graphic(451, 0, 0));
	}
}
