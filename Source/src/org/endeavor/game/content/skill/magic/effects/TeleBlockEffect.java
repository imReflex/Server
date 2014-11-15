package org.endeavor.game.content.skill.magic.effects;

import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.player.CombatEffect;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class TeleBlockEffect implements CombatEffect {
	@Override
	public void execute(Player p, Entity e) {
		e.teleblock(180);
		if (!e.isNpc()) {
			Player p2 = org.endeavor.game.entity.World.getPlayers()[e.getIndex()];

			if (p2 == null) {
				return;
			}

			p2.getClient().queueOutgoingPacket(new SendMessage("You have been Tele-blocked!"));
		}
	}
}
