package org.endeavor.game.content.combat.special.effects;

import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.player.CombatEffect;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class StatiusWarhammerEffect implements CombatEffect {
	@Override
	public void execute(Player p, Entity e) {
		int decrease = (int) (e.getLevels()[1] * 0.3D);
		int tmp18_17 = 1;
		short[] tmp18_14 = e.getLevels();
		tmp18_14[tmp18_17] = ((short) (tmp18_14[tmp18_17] - decrease));
		if (e.getLevels()[1] < 0) {
			e.getLevels()[1] = 0;
		}

		if (!e.isNpc()) {
			org.endeavor.game.entity.World.getPlayers()[e.getIndex()].getSkill().update(1);
		}

		p.getClient().queueOutgoingPacket(new SendMessage("You decrease your opponent's Defence level by 30%."));
	}
}
