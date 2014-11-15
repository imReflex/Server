package org.endeavor.game.entity.mob.abilities;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.combat.CombatEffect;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.player.Player;

public class UnholyCurseBearerAbility implements CombatEffect {
	@Override
	public void execute(Entity e1, Entity e2) {
		if (Misc.randomNumber(3) == 0) {
			Player p = null;

			if (!e2.isNpc()) {
				p = org.endeavor.game.entity.World.getPlayers()[e2.getIndex()];
			}

			e2.getUpdateFlags().sendGraphic(new Graphic(2440, 0, 100));

			for (int i = 0; i <= 6; i++)
				if (i != 3) {
					int tmp66_64 = i;
					short[] tmp66_61 = e2.getLevels();
					tmp66_61[tmp66_64] = ((short) (tmp66_61[tmp66_64] - 1));

					if (e2.getLevels()[i] < 0) {
						e2.getLevels()[i] = 0;
					}

					if (p != null)
						p.getSkill().update(i);
				}
		}
	}
}
