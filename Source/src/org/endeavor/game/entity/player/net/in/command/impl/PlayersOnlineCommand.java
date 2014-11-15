package org.endeavor.game.entity.player.net.in.command.impl;

import org.endeavor.game.entity.World;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.in.command.Command;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class PlayersOnlineCommand implements Command {
	@Override
	public void handleCommand(Player player, String command) {
		player.getClient().queueOutgoingPacket(
				new SendMessage("There are currently " + World.getActivePlayers() + " players on RevolutionX."));
	}

	@Override
	public int rightsRequired() {
		return 0;
	}

	@Override
	public boolean meetsRequirements(Player player) {
		return true;
	}
}
