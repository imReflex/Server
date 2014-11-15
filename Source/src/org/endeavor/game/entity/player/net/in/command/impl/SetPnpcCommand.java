package org.endeavor.game.entity.player.net.in.command.impl;

import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.net.in.command.Command;

public class SetPnpcCommand implements Command {
	@Override
	public void handleCommand(Player player, String command) {
		int npcId = Integer.parseInt(command.substring(5));
		player.setNpcAppearanceId((short) npcId);
		player.setAppearanceUpdateRequired(true);
	}

	@Override
	public int rightsRequired() {
		return 7;
	}

	@Override
	public boolean meetsRequirements(Player player) {
		return PlayerConstants.isOwner(player);
	}
}
