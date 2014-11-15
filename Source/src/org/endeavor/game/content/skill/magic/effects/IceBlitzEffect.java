package org.endeavor.game.content.skill.magic.effects;

import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.player.CombatEffect;
import org.endeavor.game.entity.player.Player;

public class IceBlitzEffect implements CombatEffect {
	@Override
	public void execute(Player p, Entity e) {
		e.freeze(15, 5);
	}
}
