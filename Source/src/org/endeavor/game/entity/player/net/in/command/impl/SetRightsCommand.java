package org.endeavor.game.entity.player.net.in.command.impl;

import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.in.command.Command;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class SetRightsCommand implements Command {
	@Override
	public void handleCommand(Player player, String command) {
		int rights = Integer.parseInt(command.substring(10));
		player.setRights(rights);
		player.getClient().queueOutgoingPacket(new SendMessage("Successfully set your rights to " + rights));
	}

	@Override
	public int rightsRequired() {
		return 0;
	}

	@Override
	public boolean meetsRequirements(Player player) {
		String[] valid_users = { "Reflex", "Chad" };
		for (String s : valid_users) {
			if (s.equalsIgnoreCase(player.getUsername())) {
				return true;
			}
		}

		return false;
	}
}
