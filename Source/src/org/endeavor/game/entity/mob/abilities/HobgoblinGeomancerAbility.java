package org.endeavor.game.entity.mob.abilities;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.combat.CombatEffect;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.player.Player;

public class HobgoblinGeomancerAbility implements CombatEffect {
	@Override
	public void execute(Entity e1, Entity e2) {
		if (Misc.randomNumber(8) == 0) {
			Player p = null;

			if (!e2.isNpc()) {
				p = org.endeavor.game.entity.World.getPlayers()[e2.getIndex()];
				if ((p != null) && (p.getPrayer().isProtectionActive())) {
					p.getPrayer().disableProtection(27);
					e2.getUpdateFlags().sendGraphic(new Graphic(2369, 0, 100));
					int tmp74_73 = 3;
					short[] tmp74_70 = e1.getLevels();
					if ((tmp74_70[tmp74_73] = (short) (tmp74_70[tmp74_73] + 20)) > e1.getMaxLevels()[3]) {
						e1.getLevels()[3] = e1.getMaxLevels()[3];
					}
				}

			}

			for (int i = 0; i <= 6; i++)
				if (i != 3) {
					if (e2.getLevels()[i] > e2.getMaxLevels()[i] * 0.9D) {
						int tmp148_146 = i;
						short[] tmp148_143 = e2.getLevels();
						tmp148_143[tmp148_146] = ((short) (tmp148_143[tmp148_146] - 2));

						if (p != null)
							p.getSkill().update(i);
					}
				}
		}
	}
}
