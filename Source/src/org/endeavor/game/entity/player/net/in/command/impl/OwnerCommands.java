package org.endeavor.game.entity.player.net.in.command.impl;

import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.content.combat.Hit.HitTypes;
import org.endeavor.game.content.combat.impl.MaxHitFormulas;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.in.command.Command;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendString;

/**
 * Commands that can be used by owners
 * @author Home
 *
 */
public class OwnerCommands implements Command {

	@Override
	public void handleCommand(Player paramPlayer, String paramString) {
		switch (paramString) {
		case "maxhit":
			paramPlayer.getClient().queueOutgoingPacket(new SendMessage("Max Hit: " + MaxHitFormulas.getMeleeMaxHit(paramPlayer)));
			break;
		case "hit":
			int damage = Integer.parseInt(paramString.substring(4));
			paramPlayer.hit(new Hit(paramPlayer, damage, HitTypes.MELEE));
			break;
		case "string":
			int frameId = Integer.parseInt(paramString.substring(7));
			paramPlayer.getClient().queueOutgoingPacket(new SendString("test: " + frameId, frameId));
			break;
		}
	}

	@Override
	public int rightsRequired() {
		return 4;
	}

	@Override
	public boolean meetsRequirements(Player paramPlayer) {
		return true;
	}

}
