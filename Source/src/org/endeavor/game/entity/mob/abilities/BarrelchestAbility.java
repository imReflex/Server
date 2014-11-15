package org.endeavor.game.entity.mob.abilities;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.combat.CombatEffect;
import org.endeavor.game.content.skill.prayer.PrayerBook;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.player.Player;

public class BarrelchestAbility implements CombatEffect {
	@Override
	public void execute(Entity e1, Entity e2) {
		if (e1.getLastDamageDealt() <= 0) {
			return;
		}

		if ((e2.getLevels()[5] > 0) && (!e2.isNpc())) {
			Player p = org.endeavor.game.entity.World.getPlayers()[e2.getIndex()];

			if (p != null) {
				p.getPrayer().drain(10 + Misc.randomNumber(10));
				if ((p.getPrayer().getPrayerBookType() == PrayerBook.PrayerBookType.DEFAULT)
						&& (p.getPrayer().active(18)))
					p.getPrayer().disable(18);
			}
		}
	}
}
