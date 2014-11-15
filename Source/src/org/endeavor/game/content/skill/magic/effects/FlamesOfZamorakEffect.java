package org.endeavor.game.content.skill.magic.effects;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.player.CombatEffect;
import org.endeavor.game.entity.player.Player;

public class FlamesOfZamorakEffect implements CombatEffect {
	@Override
	public void execute(Player p, Entity e) {
		if ((Misc.randomNumber(4) == 0) && (p.getLastDamageDealt() > 0) && (!e.isNpc())) {
			Player other = org.endeavor.game.entity.World.getPlayers()[e.getIndex()];

			if (other != null) {
				short[] tmp40_35 = other.getLevels();
				tmp40_35[6] = ((short) (int) (tmp40_35[6] - other.getLevels()[6] * 0.05D));

				if (other.getLevels()[6] < 0) {
					other.getLevels()[6] = 0;
				}

				other.getSkill().update(6);
			}
		}
	}
}
