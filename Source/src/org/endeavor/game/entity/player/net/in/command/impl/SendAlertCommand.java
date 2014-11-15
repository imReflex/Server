package org.endeavor.game.entity.player.net.in.command.impl;

import org.endeavor.engine.utility.Misc;
import org.endeavor.engine.utility.NameUtil;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.in.command.Command;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class SendAlertCommand implements Command {
	@Override
	public void handleCommand(Player player, String command) {
		String message = command.substring(6);
		player.getClient().queueOutgoingPacket(
				new SendMessage("Alert##Notification##" + NameUtil.uppercaseFirstLetter(message) + ". By "
						+ Misc.formatPlayerName(player.getUsername())));
	}

	@Override
	public int rightsRequired() {
		return 7;
	}

	@Override
	public boolean meetsRequirements(Player player) {
		return true;
	}
}
