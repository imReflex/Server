package org.endeavor.game.content.skill.magic.effects;

import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.player.CombatEffect;
import org.endeavor.game.entity.player.Player;

public class IceBarrageEffect implements CombatEffect {
	@Override
	public void execute(Player p, Entity e) {
		e.freeze(20, 5);
	}
}
