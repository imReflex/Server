package org.endeavor.game.content.combat.special.effects;

import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.player.CombatEffect;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class AbyssalWhipEffect implements CombatEffect {
	@Override
	public void execute(Player p, Entity e) {
		if (!e.isNpc()) {
			Player p2 = org.endeavor.game.entity.World.getPlayers()[e.getIndex()];
			if (p2 == null) {
				return;
			}
			if (p2.getRunEnergy().getEnergy() >= 4) {
				int absorb = (int) (p2.getRunEnergy().getEnergy() * 0.25D);
				p2.getRunEnergy().deduct(absorb);
				p.getRunEnergy().add(absorb);
				p.getClient().queueOutgoingPacket(new SendMessage("You absorb 25% of your opponents energy."));
				p2.getClient().queueOutgoingPacket(new SendMessage("25% of your energy has been absorbed!"));
			}
		}
	}
}
