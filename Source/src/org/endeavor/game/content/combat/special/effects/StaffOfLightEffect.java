package org.endeavor.game.content.combat.special.effects;

import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.player.CombatEffect;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class StaffOfLightEffect implements CombatEffect {
	@Override
	public void execute(Player p, Entity e) {
		if (p.getAttributes().get("staffOfLightEffect") != null) {
			if (System.currentTimeMillis() - ((Long) p.getAttributes().get("staffOfLightEffect")).longValue() < 420000L) {
				p.getClient().queueOutgoingPacket(
						new SendMessage("You are already being protected by the Staff of light spirits!"));
				return;
			}
			p.getAttributes().remove("staffOfLightEffect");

			p.getAttributes().set("staffOfLightEffect", Long.valueOf(System.currentTimeMillis()));
			p.getClient()
					.queueOutgoingPacket(new SendMessage("You are shielded by the spirits of the Staff of light!"));
		}
	}
}
