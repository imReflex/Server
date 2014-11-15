package org.endeavor.game.content.skill.magic.effects;

import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.player.CombatEffect;
import org.endeavor.game.entity.player.Player;

public class BindEffect implements CombatEffect {
	@Override
	public void execute(Player p, Entity e) {
		if (p.wasLastHitSuccess())
			e.freeze(10, 5);
	}
}
