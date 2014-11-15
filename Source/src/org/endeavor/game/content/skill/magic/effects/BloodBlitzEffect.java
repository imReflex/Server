package org.endeavor.game.content.skill.magic.effects;

import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.player.CombatEffect;
import org.endeavor.game.entity.player.Player;

public class BloodBlitzEffect implements CombatEffect {
	@Override
	public void execute(Player p, Entity e) {
		int dmg = p.getLastDamageDealt();
		if (dmg >= 4) {
			int heal = dmg / 4;
			int tmp20_19 = 3;
			short[] tmp20_16 = p.getLevels();
			tmp20_16[tmp20_19] = ((short) (tmp20_16[tmp20_19] + heal));
			if (p.getLevels()[3] > p.getMaxLevels()[3]) {
				p.getLevels()[3] = p.getMaxLevels()[3];
			}
			p.getSkill().update(3);
		}
	}
}
