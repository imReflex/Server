package org.endeavor.game.content.combat.special.effects;

import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.player.CombatEffect;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class SaradominGodswordEffect implements CombatEffect {
	@Override
	public void execute(Player p, Entity e) {
		int dmg = p.getLastDamageDealt();
		if (dmg == 0) {
			return;
		}

		int hp = (int) (dmg * 0.5D);
		int pray = (int) (dmg * 0.25D);

		if ((hp > 9) && (p.getLevels()[3] < p.getMaxLevels()[3])) {
			int tmp55_54 = 3;
			short[] tmp55_51 = p.getLevels();
			tmp55_51[tmp55_54] = ((short) (tmp55_51[tmp55_54] + hp));
			if (p.getLevels()[3] > p.getMaxLevels()[3]) {
				hp = p.getMaxLevels()[3] - p.getLevels()[3];
				p.getLevels()[3] = p.getMaxLevels()[3];
			}
			p.getSkill().update(3);
		} else {
			hp = 0;
		}

		if ((pray > 4) && (p.getLevels()[5] < p.getMaxLevels()[5])) {
			int tmp144_143 = 5;
			short[] tmp144_140 = p.getLevels();
			tmp144_140[tmp144_143] = ((short) (tmp144_140[tmp144_143] + pray));
			if (p.getLevels()[5] > p.getMaxLevels()[5]) {
				pray = p.getMaxLevels()[5] - p.getLevels()[5];
				p.getLevels()[5] = p.getMaxLevels()[5];
			}
			p.getSkill().update(5);
		} else {
			pray = 0;
		}

		String message = "";

		if ((pray > 0) && (hp > 0))
			message = "You regenerate " + pray + " Prayer and " + hp + " Hitpoints.";
		else if ((pray == 0) && (hp > 0))
			message = "You regenerate " + hp + " Hitpoints.";
		else if ((hp == 0) && (pray > 0))
			message = "You regenerate " + pray + " Prayer.";
		else {
			return;
		}

		p.getClient().queueOutgoingPacket(new SendMessage(message));
	}
}
