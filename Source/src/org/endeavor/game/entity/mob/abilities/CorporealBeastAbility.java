package org.endeavor.game.entity.mob.abilities;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.content.combat.CombatEffect;
import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;

public class CorporealBeastAbility implements CombatEffect {
	@Override
	public void execute(Entity e1, Entity e2) {
		if (e1.getCombat().getCombatType() == CombatTypes.MELEE) {
			Mob mob = org.endeavor.game.entity.World.getNpcs()[e1.getIndex()];
			if ((mob != null) && (mob.getCombatIndex() == 0))
				for (Player p : mob.getCombatants())
					if (p != e2) {
						mob.getCombat()
								.getMelee()
								.finish(p,
										new Hit(Misc.randomNumber(e1.getMaxHit(CombatTypes.MELEE)), Hit.HitTypes.MELEE));
					}
		}
	}
}
