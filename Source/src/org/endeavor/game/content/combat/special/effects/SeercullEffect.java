package org.endeavor.game.content.combat.special.effects;

import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.player.CombatEffect;
import org.endeavor.game.entity.player.Player;

public class SeercullEffect implements CombatEffect {
	@Override
	public void execute(Player p, Entity e) {
		int drain = (int) (p.getLastDamageDealt() * 0.1D);

		if (drain == 0)
			return;
		short[] tmp22_17 = e.getLevels();
		tmp22_17[6] = ((short) (tmp22_17[6] - drain));
		if (e.getLevels()[6] < 0) {
			e.getLevels()[6] = 0;
		}

		if (!e.isNpc()) {
			Player p2 = org.endeavor.game.entity.World.getPlayers()[e.getIndex()];

			if (p2 == null) {
				return;
			}

			p2.getSkill().update(6);
		}
	}
}
