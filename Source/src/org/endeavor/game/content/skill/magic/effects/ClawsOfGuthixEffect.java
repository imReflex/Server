package org.endeavor.game.content.skill.magic.effects;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.player.CombatEffect;
import org.endeavor.game.entity.player.Player;

public class ClawsOfGuthixEffect implements CombatEffect {
	@Override
	public void execute(Player p, Entity e) {
		if ((Misc.randomNumber(4) == 0) && (p.getLastDamageDealt() > 0) && (!e.isNpc())) {
			Player other = org.endeavor.game.entity.World.getPlayers()[e.getIndex()];

			if (other != null) {
				int tmp39_38 = 1;
				short[] tmp39_35 = other.getLevels();
				tmp39_35[tmp39_38] = ((short) (int) (tmp39_35[tmp39_38] - other.getLevels()[1] * 0.05D));

				if (other.getLevels()[1] < 0) {
					other.getLevels()[1] = 0;
				}

				other.getSkill().update(1);
			}
		}
	}
}
