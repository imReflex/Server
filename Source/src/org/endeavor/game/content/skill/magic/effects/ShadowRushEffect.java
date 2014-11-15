package org.endeavor.game.content.skill.magic.effects;

import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.player.CombatEffect;
import org.endeavor.game.entity.player.Player;

public class ShadowRushEffect implements CombatEffect {
	@Override
	public void execute(Player p, Entity e) {
		if (p.getLastDamageDealt() > -1) {
			int tmp13_12 = 0;
			short[] tmp13_9 = e.getLevels();
			tmp13_9[tmp13_12] = ((short) (int) (tmp13_9[tmp13_12] - e.getLevels()[0] * 0.5D));
			if (e.getLevels()[0] < 0) {
				e.getLevels()[0] = 0;
			}

			if (!e.isNpc()) {
				Player p2 = org.endeavor.game.entity.World.getPlayers()[e.getIndex()];

				if (p2 == null) {
					return;
				}

				p2.getSkill().update(0);
			}
		}
	}
}
