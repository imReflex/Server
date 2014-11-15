package org.endeavor.game.entity.mob.abilities;

import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.content.combat.CombatEffect;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;

public class BorkAbility implements CombatEffect {
	@Override
	public void execute(Entity e1, Entity e2) {
		if (e1.isNpc()) {
			Mob m = org.endeavor.game.entity.World.getNpcs()[e1.getIndex()];

			if ((m != null) && (m.getCombat().getCombatType() == CombatTypes.MAGIC) && (m.getCombatants() != null)
					&& (m.getCombatants().size() > 0))
				for (Player p : m.getCombatants())
					if (!p.equals(e1.getCombat().getAttacking()))
						m.getCombat().getMagic().finish(p);
		}
	}
}
