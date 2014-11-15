package org.endeavor.game.content.skill.magic.effects;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.player.CombatEffect;
import org.endeavor.game.entity.player.Player;

public class SmokeBurstEffect implements CombatEffect {
	@Override
	public void execute(Player p, Entity e) {
		if ((p.getLastDamageDealt() >= 0) && (Misc.randomNumber(2) == 0))
			e.poison(3);
	}
}
