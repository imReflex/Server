package org.endeavor.game.entity.mob.abilities;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.combat.CombatEffect;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.player.Player;

public class TormentedDemonAbility implements CombatEffect {
	@Override
	public void execute(Entity e1, Entity e2) {
		if (!e2.isNpc()) {
			Player p = org.endeavor.game.entity.World.getPlayers()[e2.getIndex()];

			if ((p != null) && (Misc.randomNumber(5) == 0) && (p.getPrayer().isProtectionActive())) {
				for (int i = 0; i <= 6; i++) {
					if (i != 3) {
						int tmp58_56 = i;
						short[] tmp58_53 = p.getLevels();
						tmp58_53[tmp58_56] = ((short) (tmp58_53[tmp58_56] - 9));

						if (p.getLevels()[i] < 0) {
							p.getLevels()[i] = 0;
						}

						p.getSkill().update(i);
					}
				}
				p.getPrayer().disableProtection(30);
			}
		}
	}
}
