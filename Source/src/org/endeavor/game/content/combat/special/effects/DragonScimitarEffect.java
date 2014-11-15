package org.endeavor.game.content.combat.special.effects;

import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.player.CombatEffect;
import org.endeavor.game.entity.player.Player;

public class DragonScimitarEffect implements CombatEffect {
	@Override
	public void execute(Player p, Entity e) {
		if ((p.getLastDamageDealt() > 0) && (!e.isNpc())) {
			Player p2 = org.endeavor.game.entity.World.getPlayers()[e.getIndex()];

			if (p2 == null) {
				return;
			}

			p2.getPrayer().disableProtection(9);
		}
	}
}
